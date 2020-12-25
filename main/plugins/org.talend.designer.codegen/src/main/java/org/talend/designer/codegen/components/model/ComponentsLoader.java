// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.codegen.components.model;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.component_cache.ComponentsCache;
import org.talend.core.model.components.AbstractComponentsProvider;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.ComponentManager;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.filters.ComponentsFactoryProviderManager;
import org.talend.core.model.components.filters.IComponentFactoryFilter;
import org.talend.designer.codegen.CodeGeneratorActivator;
import org.talend.designer.core.model.components.ComponentBundleToPath;
import org.talend.designer.core.model.components.ComponentFilesNaming;
import org.talend.designer.core.model.components.EmfComponent;

/*
 * Created by bhe on Dec 25, 2020
 */
public class ComponentsLoader {

    private static final ComponentsLoader INSTANCE = new ComponentsLoader();

    private ComponentsLoader() {
    }

    public static ComponentsLoader getInstance() {
        return INSTANCE;
    }

    public void loadComponents(List<AbstractComponentsProvider> providers, Set<IComponent> componentList,
            HashSet<IComponent> customComponentList, HashSet<IComponent> userComponentList, ArrayList<String> skeletonList,
            Map<IComponent, AbstractComponentsProvider> componentToProviderMap) {

        // this is a disk heavy task
        int nThreads = Runtime.getRuntime().availableProcessors() * 2;
        nThreads = Math.max(nThreads, 8);
        nThreads = Math.min(nThreads, 12);

        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(nThreads);
        CompletionService<Boolean> service = new ExecutorCompletionService<Boolean>(WORKER_THREAD_POOL);

        ConcurrentLinkedQueue<TaskInput> taskInputs = new ConcurrentLinkedQueue<TaskInput>();
        for (AbstractComponentsProvider provider : providers) {
            List<TaskInput> tis = getTaskInputs(provider);
            taskInputs.addAll(tis);
        }

        for (int i = 0; i < nThreads; i++) {
            LoadComponentTask task = new LoadComponentTask(taskInputs, componentList, customComponentList, userComponentList,
                    skeletonList, componentToProviderMap);
            service.submit(task);
        }

        // wait for all of task completed
        for (int i = 0; i < nThreads; i++) {
            try {
                service.take();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        WORKER_THREAD_POOL.shutdown();
    }

    private List<TaskInput> getTaskInputs(AbstractComponentsProvider provider) {
        List<TaskInput> ret = new ArrayList<TaskInput>();
        if (provider != null) {
            try {
                provider.preComponentsLoad();
                File componentFile = provider.getInstallationFolder();
                if (componentFile != null && componentFile.exists()) {
                    File source = provider.getInstallationFolder();
                    FileFilter fileFilter = new FileFilter() {

                        @Override
                        public boolean accept(final File file) {
                            return file.isDirectory() && file.getName().charAt(0) != '.'
                                    && !file.getName().equals(IComponentsFactory.EXTERNAL_COMPONENTS_INNER_FOLDER);
                        }

                    };

                    File[] childDirectories = source.listFiles(fileFilter);

                    for (File f : childDirectories) {
                        TaskInput ti = new TaskInput(provider, f, provider.getComponentsLocation());
                        ret.add(ti);
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return ret;
    }

    private static class TaskInput {

        AbstractComponentsProvider componentsProvider;

        File dir;

        String pathSource;

        public TaskInput(AbstractComponentsProvider componentsProvider, File dir, String pathSource) {
            this.componentsProvider = componentsProvider;
            this.dir = dir;
            this.pathSource = pathSource;
        }
    }

    private static class LoadComponentTask implements Callable<Boolean> {

        private static Logger log = Logger.getLogger(LoadComponentTask.class);

        private static final String SKELETON_SUFFIX = ".skeleton"; //$NON-NLS-1$

        private static final String INCLUDEFILEINJET_SUFFIX = ".inc.javajet"; //$NON-NLS-1$

        private static final String OLD_COMPONENTS_USER_INNER_FOLDER = "user";

        ConcurrentLinkedQueue<TaskInput> tis;

        Set<IComponent> componentList;

        HashSet<IComponent> customComponentList;

        HashSet<IComponent> userComponentList;

        ArrayList<String> skeletonList;

        Map<IComponent, AbstractComponentsProvider> componentToProviderMap;

        public LoadComponentTask(ConcurrentLinkedQueue<TaskInput> tis, Set<IComponent> componentList,
                HashSet<IComponent> customComponentList, HashSet<IComponent> userComponentList, ArrayList<String> skeletonList,
                Map<IComponent, AbstractComponentsProvider> componentToProviderMap) {
            this.tis = tis;
            this.componentList = componentList;
            this.customComponentList = customComponentList;
            this.userComponentList = userComponentList;
            this.skeletonList = skeletonList;
            this.componentToProviderMap = componentToProviderMap;
        }

        @Override
        public Boolean call() throws Exception {
            TaskInput ti = null;
            while ((ti = this.tis.poll()) != null) {
                loadComponentsFromFolder(ti);
            }
            return true;

        }

        private void loadComponentsFromFolder(TaskInput ti) {
            if (ti != null) {
                boolean isCustom = ti.componentsProvider.isCustom();

                FileFilter skeletonFilter = new FileFilter() {

                    @Override
                    public boolean accept(final File file) {
                        String fileName = file.getName();
                        return file.isFile() && fileName.charAt(0) != '.'
                                && (fileName.endsWith(SKELETON_SUFFIX) || fileName.endsWith(INCLUDEFILEINJET_SUFFIX));
                    }

                };
                // Changed by Marvin Wang on Feb.22 for bug TDI-19166, caz the test ConnectionManagerTest maybe get the
                // null
                // context.
                BundleContext context = null;
                if (Platform.getProduct() != null) {
                    final Bundle definingBundle = Platform.getProduct().getDefiningBundle();
                    if (definingBundle != null) {
                        context = definingBundle.getBundleContext();
                    }
                }
                if (context == null) {
                    context = CodeGeneratorActivator.getDefault().getBundle().getBundleContext();
                }

                ServiceReference sref = context.getServiceReference(PackageAdmin.class.getName());
                PackageAdmin admin = (PackageAdmin) context.getService(sref);

                String bundleName;
                if (!isCustom) {
                    bundleName = admin.getBundle(ti.componentsProvider.getClass()).getSymbolicName();
                } else if (ti.componentsProvider.getComponentsBundle() == null) {
                    bundleName = IComponentsFactory.COMPONENTS_LOCATION;
                } else {
                    bundleName = ti.componentsProvider.getComponentsBundle();
                }
                ComponentsCache cache = ComponentManager.getComponentCache();
                if (ti.dir != null) {
                    if (skeletonList != null) {
                        // get the skeleton files first, then XML config files later.
                        File[] skeletonFiles = ti.dir.listFiles(skeletonFilter);
                        if (skeletonFiles != null) {
                            synchronized (skeletonList) {
                                for (File file : skeletonFiles) {
                                    skeletonList.add(file.getAbsolutePath()); // path
                                }
                            }
                        }

                        try {
                            File xmlMainFile = new File(ti.dir, ComponentFilesNaming.getInstance()
                                    .getMainXMLFileName(ti.dir.getName(), getCodeLanguageSuffix()));
                            if (!xmlMainFile.exists()) {
                                // if not a component folder, ignore it.
                                return;
                            }
                            String currentXmlSha1 = null;
                            try (FileInputStream fis = new FileInputStream(xmlMainFile)) {
                                currentXmlSha1 = DigestUtils.shaHex(fis);
                            } catch (Exception e) {
                                // nothing since exceptions are directly in the check bellow
                            }

                            ComponentFileChecker.checkComponentFolder(ti.dir, getCodeLanguageSuffix());
                            String pathName = xmlMainFile.getAbsolutePath();

                            String applicationPath = ComponentBundleToPath.getPathFromBundle(bundleName);

                            // pathName = C:\myapp\plugins\myplugin\components\mycomponent\mycomponent.xml
                            pathName = (new Path(pathName)).toPortableString();
                            // pathName = C:/myapp/plugins/myplugin/components/mycomponent/mycomponent.xml
                            pathName = pathName.replace(applicationPath, ""); //$NON-NLS-1$
                            // pathName = /components/mycomponent/mycomponent.xml

                            // if not already in memory, just load the component from cache.
                            // if the component is already existing in cache and if it's the same, it won't reload all
                            // (cf
                            // flag: foundComponentIsSame)
                            EmfComponent currentComp = new EmfComponent(pathName, bundleName,
                                    xmlMainFile.getParentFile().getName(), ti.pathSource, cache, false, ti.componentsProvider);
                            // force to call some functions to update the cache. (to improve)
                            currentComp.isVisibleInComponentDefinition();
                            currentComp.isTechnical();
                            currentComp.getOriginalFamilyName();
                            currentComp.getTranslatedFamilyName();
                            currentComp.getPluginExtension();
                            currentComp.getVersion();
                            currentComp.getModulesNeeded(null);
                            currentComp.getPluginDependencies();
                            currentComp.setSha1(currentXmlSha1);
                            // end of force cache update.
                            ComponentManager.setModified(true); // this will force to save the cache later.

                            boolean hiddenComponent = false;

                            Collection<IComponentFactoryFilter> filters = ComponentsFactoryProviderManager.getInstance()
                                    .getProviders();
                            for (IComponentFactoryFilter filter : filters) {
                                if (!filter.isAvailable(currentComp.getName())) {
                                    hiddenComponent = true;
                                    break;
                                }
                            }

                            // if the component is not needed in the current branding,
                            // and that this one IS NOT a specific component for code generation
                            // just don't load it
                            if (hiddenComponent && !(currentComp.getOriginalFamilyName().contains("Technical")
                                    || currentComp.isTechnical())) {
                                return;
                            }

                            synchronized (componentToProviderMap) {
                                componentToProviderMap.put(currentComp, ti.componentsProvider);
                            }
                            // if the component is not needed in the current branding,
                            // and that this one IS a specific component for code generation,
                            // hide it
                            if (hiddenComponent
                                    && (currentComp.getOriginalFamilyName().contains("Technical") || currentComp.isTechnical())) {
                                currentComp.setVisible(false);
                                currentComp.setTechnical(true);
                            }
                            if (ti.componentsProvider.getId().contains("Camel")) {
                                currentComp.setPaletteType(ComponentCategory.CATEGORY_4_CAMEL.getName());
                            } else {
                                currentComp.setPaletteType(currentComp.getType());
                            }

                            currentComp.setProvider(ti.componentsProvider);

                            synchronized (componentList) {
                                if (componentList.contains(currentComp)) {
                                    log.warn("Component " + currentComp.getName() + " already exists. Cannot load user version.");
                                    return;
                                }
                                componentList.add(currentComp);
                            }

                            if (isCustom) {
                                synchronized (customComponentList) {
                                    customComponentList.add(currentComp);
                                }
                            }
                            if (ti.pathSource != null) {
                                Path userComponent = new Path(ti.pathSource);
                                Path templatePath = new Path(IComponentsFactory.COMPONENTS_INNER_FOLDER + File.separatorChar
                                        + IComponentsFactory.EXTERNAL_COMPONENTS_INNER_FOLDER + File.separatorChar
                                        + ComponentUtilities.getExtFolder(OLD_COMPONENTS_USER_INNER_FOLDER));
                                if (userComponent.equals(templatePath)) {
                                    synchronized (userComponentList) {
                                        userComponentList.add(currentComp);
                                    }
                                }
                            }

                        } catch (MissingMainXMLComponentFileException e) {
                            log.trace(ti.dir.getName() + " is not a " + getCodeLanguageSuffix() + " component", e); //$NON-NLS-1$ //$NON-NLS-2$
                        } catch (BusinessException e) {
                            BusinessException ex = new BusinessException("Cannot load component \"" + ti.dir.getName() + "\": " //$NON-NLS-1$ //$NON-NLS-2$
                                    + e.getMessage(), e);
                            ExceptionHandler.process(ex, Level.ERROR);
                        }
                    }
                }
            }
        }

        private String getCodeLanguageSuffix() {
            return LanguageManager.getCurrentLanguage().getName();
        }

    }

}
