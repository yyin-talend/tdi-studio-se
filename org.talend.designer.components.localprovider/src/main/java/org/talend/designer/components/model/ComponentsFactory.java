// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.AbstractComponentsProvider;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ComponentSetting;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.components.i18n.Messages;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.process.AbstractProcessProvider;
import org.talend.repository.ProjectManager;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Component factory that look for each component and load their information. <br/>
 * 
 * $Id$
 */
public class ComponentsFactory implements IComponentsFactory {

    private static final String OLD_COMPONENTS_USER_INNER_FOLDER = "user"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(ComponentsFactory.class);

    private static List<IComponent> componentList = null;

    private static List<IComponent> customComponentList = null;

    private List<IComponent> userComponentList = null;

    private IProgressMonitor monitor;

    private SubProgressMonitor subMonitor;

    private static Map<String, IComponent> componentsCache = new HashMap<String, IComponent>();

    // keep a list of the current provider for the selected component, to have the family translation
    // only for components that are loaded
    private static Map<IComponent, AbstractComponentsProvider> componentToProviderMap;

    private static Map<String, AbstractComponentsProvider> componentsAndProvider = new HashMap<String, AbstractComponentsProvider>();

    private Map<String, ImageDescriptor> allComponents;

    // 1. only the in the directory /components ,not including /resource
    // 2. include the skeleton files and external include files
    private static List<String> skeletonList = null;

    private static final String SKELETON_SUFFIX = ".skeleton"; //$NON-NLS-1$

    private static final String INCLUDEFILEINJET_SUFFIX = ".inc.javajet"; //$NON-NLS-1$

    private static final String FAMILY_SPEARATOR = "--FAMILY--"; //$NON-NLS-1$

    // this list of component is always needed, they must always be loaded at least, since they can be used for code
    // generation indirectly.
    private static final String[] COMPONENTS_ALWAYS_NEEDED = { "tPrejob", "tPostjob", //$NON-NLS-1$ //$NON-NLS-2$ 
            "tJava", "tLibraryLoad" }; //$NON-NLS-1$ //$NON-NLS-2$ 

    public ComponentsFactory() {
        if (!INCLUDEFILEINJET_SUFFIX.equals(".inc.javajet")) { //$NON-NLS-1$
            ExceptionHandler.process(new IllegalStateException(Messages.getString("ComponentsFactory.parentNotRecompiled")), //$NON-NLS-1$
                    Priority.WARN);
        }
    }

    private List<ComponentSetting> getComponentsFromProject() {
        // TODO check components used from ref projects.
        List<ComponentSetting> components = (List<ComponentSetting>) ProjectManager.getInstance().getCurrentProject()
                .getEmfProject().getComponentsSettings();
        return components;
    }

    private boolean isComponentVisible(String componentName) {
        Boolean visible = Boolean.TRUE;

        if (ArrayUtils.contains(COMPONENTS_ALWAYS_NEEDED, componentName)) {
            return true;
        }

        List<ComponentSetting> settingsFromCompName = new ArrayList<ComponentSetting>();
        // here we just check if the component is visible somewhere in the settings.
        // if it's visible in any category, we will load the component
        // if the component is unknown (new component?), we will load also
        // (technical components will always loaded by default as they're not saved in componentSettings)
        // to avoid any problem, we also load by default the category named "Technical".
        for (ComponentSetting componentSetting : getComponentsFromProject()) {
            if (componentSetting.getName().equals(componentName)) {
                settingsFromCompName.add(componentSetting);
                // if (componentSetting.isHidden()) {
                // // hide component only if hidden in all projects
                // if (manager.getReferencedProjects(manager.getCurrentProject()).size() > 0
                // && isHiddenInRefProjects(componentName)) {
                // visible = Boolean.FALSE;
                // } else {
                // visible = Boolean.TRUE;
                // componentSetting.setHidden(false);
                // }
                // } else {
                // return true;
                // }
                if ("Technical".equals(componentSetting.getFamily())) { //$NON-NLS-1$
                    return true;
                }
            }
        }

        // load components those visible in any category
        int hideCount = 0;
        for (ComponentSetting componentSetting : settingsFromCompName) {
            if (componentSetting.isHidden()) {
                // hide component only if hidden in all projects
                if (isHiddenInRefProjects(componentSetting)) {
                    hideCount++;
                } else {
                    visible = Boolean.TRUE;
                    componentSetting.setHidden(false);
                }
            } else {
                visible = Boolean.TRUE;
            }
        }
        if (settingsFromCompName.size() > 0 && hideCount == settingsFromCompName.size()) {
            visible = Boolean.FALSE;
        }

        return visible;
    }

    private boolean isHiddenInRefProjects(ComponentSetting settingInMain) {
        if (settingInMain == null) {
            return false;
        }
        int hiddenCount = 0;
        ProjectManager manager = ProjectManager.getInstance();
        List<Project> referencedProjects = manager.getReferencedProjects();
        for (Project curProject : referencedProjects) {
            List<ComponentSetting> componentsSettings = (List<ComponentSetting>) curProject.getEmfProject()
                    .getComponentsSettings();
            for (ComponentSetting setting : componentsSettings) {
                if (setting.isHidden() && setting.getName().equals(settingInMain.getName())
                        && setting.getFamily().equals(settingInMain.getFamily())) {
                    hiddenCount++;
                }
            }
        }
        if (hiddenCount == referencedProjects.size()) {
            return true;
        }

        return false;
    }

    private void init() {
        removeOldComponentsUserFolder(); // not used anymore

        TimeMeasure.measureActive = false;
        // TimeMeasure.display = true;
        // TimeMeasure.displaySteps = true;
        TimeMeasure.begin("ComponentsFactory.init"); //$NON-NLS-1$
        long startTime = System.currentTimeMillis();
        componentList = new ArrayList<IComponent>();
        customComponentList = new ArrayList<IComponent>();
        skeletonList = new ArrayList<String>();

        componentToProviderMap = new HashMap<IComponent, AbstractComponentsProvider>();

        XsdValidationCacheManager.getInstance().load();

        // 1. Load system components:
        loadComponentsFromFolder(IComponentsFactory.COMPONENTS_INNER_FOLDER);
        TimeMeasure.step("ComponentsFactory.init", Messages.getString("ComponentsFactory.afterSystemComponent")); //$NON-NLS-1$ //$NON-NLS-2$

        // 3.Load Component from extension point: components_provider
        loadComponentsFromComponentsProviderExtension();

        // 3.Load Component from extension point: component_definition
        loadComponentsFromExtensions();

        XsdValidationCacheManager.getInstance().save();

        TimeMeasure.end("ComponentsFactory.init"); //$NON-NLS-1$
        log.debug(componentList.size() + " components loaded in " + (System.currentTimeMillis() - startTime) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$

        try {
            CorePlugin.getDefault().getRunProcessService().updateLibraries();
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
        TimeMeasure.measureActive = false;
    }

    private void loadComponentsFromComponentsProviderExtension() {
        ComponentsProviderManager componentsProviderManager = ComponentsProviderManager.getInstance();
        for (AbstractComponentsProvider componentsProvider : componentsProviderManager.getProviders()) {
            try {
                componentsProvider.preComponentsLoad();
                if (componentsProvider.getInstallationFolder().exists()) {
                    loadComponentsFromFolder(componentsProvider.getComponentsLocation(), componentsProvider);
                }
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    public void loadUserComponentsFromComponentsProviderExtension() {
        ComponentsProviderManager componentsProviderManager = ComponentsProviderManager.getInstance();
        AbstractComponentsProvider componentsProvider = componentsProviderManager.loadUserComponentsProvidersFromExtension();
        try {
            componentsProvider.preComponentsLoad();
            // remove old user components
            if (this.userComponentList != null) {
                for (IComponent component : userComponentList) {
                    if (componentList.contains(component)) {
                        componentList.remove(component);
                    }
                }

            }
            if (componentsProvider.getInstallationFolder().exists()) {
                loadComponentsFromFolder(componentsProvider.getComponentsLocation(), componentsProvider);
            }
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
    }

    private void removeOldComponentsUserFolder() {
        String userPath = IComponentsFactory.COMPONENTS_INNER_FOLDER + File.separatorChar
                + ComponentUtilities.getExtFolder(OLD_COMPONENTS_USER_INNER_FOLDER);
        File componentsLocation = getComponentsLocation(userPath);
        if (componentsLocation != null && componentsLocation.exists()) {
            FilesUtils.removeFolder(componentsLocation, true);
        }
    }

    /**
     * DOC qzhang Comment method "loadComponentsFromExtensions".
     */
    private void loadComponentsFromExtensions() {
        AbstractProcessProvider.loadComponentsFromProviders();
    }

    private void loadComponentsFromFolder(String pathSource, AbstractComponentsProvider... provider) {
        TimeMeasure.begin("ComponentsFactory.loadComponentsFromFolder"); //$NON-NLS-1$

        TimeMeasure.begin("ComponentsFactory.loadComponentsFromFolder.checkFiles"); //$NON-NLS-1$
        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.checkFiles"); //$NON-NLS-1$

        TimeMeasure.begin("ComponentsFactory.loadComponentsFromFolder.emf1"); //$NON-NLS-1$
        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.emf1"); //$NON-NLS-1$

        TimeMeasure.begin("ComponentsFactory.loadComponentsFromFolder.emf2"); //$NON-NLS-1$
        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.emf2"); //$NON-NLS-1$

        TimeMeasure.begin("ComponentsFactory.loadComponentsFromFolder.loadIcons"); //$NON-NLS-1$
        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.loadIcons"); //$NON-NLS-1$

        // TimeMeasure.display=false;
        if (pathSource != null) {
            Path userComponent = new Path(pathSource);
            Path templatePath = new Path(IComponentsFactory.COMPONENTS_INNER_FOLDER + File.separatorChar
                    + IComponentsFactory.EXTERNAL_COMPONENTS_INNER_FOLDER + File.separatorChar
                    + ComponentUtilities.getExtFolder(OLD_COMPONENTS_USER_INNER_FOLDER));
            if (userComponent.equals(templatePath)) {
                this.userComponentList = new ArrayList<IComponent>();
            }
        }

        boolean isCustom = false;
        if (provider != null && provider.length == 1) {
            if ("org.talend.designer.components.model.UserComponentsProvider".equals(provider[0].getId())
                    || "org.talend.designer.components.ecosystem.EcosystemComponentsProvider".equals(provider[0].getId())) {
                isCustom = true;
            }
        }

        File source = getComponentsLocation(pathSource);
        File[] childDirectories;

        FileFilter fileFilter = new FileFilter() {

            public boolean accept(final File file) {
                return file.isDirectory() && file.getName().charAt(0) != '.'
                        && !file.getName().equals(IComponentsFactory.EXTERNAL_COMPONENTS_INNER_FOLDER)
                        && isComponentVisible(file.getName());
            }

        };
        if (source == null) {
            ExceptionHandler.process(new Exception(Messages.getString("ComponentsFactory.componentNotFound") + pathSource)); //$NON-NLS-1$
            return;
        }

        TimeMeasure.resume("ComponentsFactory.loadComponentsFromFolder.listComponentsFolders"); //$NON-NLS-1$
        childDirectories = source.listFiles(fileFilter);
        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.listComponentsFolders"); //$NON-NLS-1$

        IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);

        String[] availableComponents = service.getBrandingConfiguration().getAvailableComponents();

        FileFilter skeletonFilter = new FileFilter() {

            public boolean accept(final File file) {
                String fileName = file.getName();
                return file.isFile() && fileName.charAt(0) != '.'
                        && (fileName.endsWith(SKELETON_SUFFIX) || fileName.endsWith(INCLUDEFILEINJET_SUFFIX));
            }

        };

        if (childDirectories != null) {
            if (monitor != null) {
                this.subMonitor = new SubProgressMonitor(monitor, childDirectories.length);
            }

            for (File currentFolder : childDirectories) {
                // get the skeleton files first, then XML config files later.
                TimeMeasure.resume("ComponentsFactory.loadComponentsFromFolder.listSkeletonFiles"); //$NON-NLS-1$
                File[] skeletonFiles = currentFolder.listFiles(skeletonFilter);
                TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.listSkeletonFiles"); //$NON-NLS-1$
                if (skeletonFiles != null) {
                    for (File file : skeletonFiles) {
                        skeletonList.add(file.getAbsolutePath()); // path
                    }
                }

                try {
                    TimeMeasure.resume("ComponentsFactory.loadComponentsFromFolder.checkFiles"); //$NON-NLS-1$
                    ComponentFileChecker.checkComponentFolder(currentFolder, getCodeLanguageSuffix());
                    TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.checkFiles"); //$NON-NLS-1$
                    TimeMeasure.resume("ComponentsFactory.loadComponentsFromFolder.emf1"); //$NON-NLS-1$
                    File xmlMainFile = new File(currentFolder, ComponentFilesNaming.getInstance().getMainXMLFileName(
                            currentFolder.getName(), getCodeLanguageSuffix()));
                    TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.emf1"); //$NON-NLS-1$
                    TimeMeasure.resume("ComponentsFactory.loadComponentsFromFolder.emf2"); //$NON-NLS-1$

                    if (CommonsPlugin.isHeadless() && componentsCache.containsKey(xmlMainFile.getAbsolutePath())) {
                        // In headless mode, we assume the components won't change and we will use a cache
                        componentList.add(componentsCache.get(xmlMainFile.getAbsolutePath()));
                        if (isCustom) {
                            customComponentList.add(componentsCache.get(xmlMainFile.getAbsolutePath()));
                        }
                        continue;
                    }

                    EmfComponent currentComp = new EmfComponent(xmlMainFile, pathSource);
                    // if the component is not needed in the current branding,
                    // and that this one IS NOT a specific component for code generation
                    // just don't load it
                    if (availableComponents != null
                            && !ArrayUtils.contains(availableComponents, currentComp.getName())
                            && !(ArrayUtils.contains(COMPONENTS_ALWAYS_NEEDED, currentComp.getName())
                                    || currentComp.getOriginalFamilyName().contains("Technical") || currentComp.isTechnical())) {
                        continue;
                    }

                    if (provider.length == 1) {
                        componentToProviderMap.put(currentComp, provider[0]);
                    }

                    // if the component is not needed in the current branding,
                    // and that this one IS a specific component for code generation,
                    // hide it
                    if (availableComponents != null
                            && !ArrayUtils.contains(availableComponents, currentComp.getName())
                            && (ArrayUtils.contains(COMPONENTS_ALWAYS_NEEDED, currentComp.getName())
                                    || currentComp.getOriginalFamilyName().contains("Technical") || currentComp.isTechnical())) {
                        currentComp.setVisible(false);
                        currentComp.setTechnical(true);
                    }

                    TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.emf2"); //$NON-NLS-1$

                    if (componentList.contains(currentComp)) {
                        log.warn("Component " + currentComp.getName() + " already exists. Cannot load user version."); //$NON-NLS-1$ //$NON-NLS-2$
                    } else {
                        currentComp.setResourceBundle(getComponentResourceBundle(currentComp, pathSource));
                        TimeMeasure.resume("ComponentsFactory.loadComponentsFromFolder.loadIcons"); //$NON-NLS-1$
                        loadIcons(currentFolder, currentComp);
                        TimeMeasure.pause("ComponentsFactory.loadComponentsFromFolder.loadIcons"); //$NON-NLS-1$
                        componentList.add(currentComp);
                        if (isCustom) {
                            customComponentList.add(currentComp);
                        }
                        if (pathSource != null) {
                            Path userComponent = new Path(pathSource);
                            Path templatePath = new Path(IComponentsFactory.COMPONENTS_INNER_FOLDER + File.separatorChar
                                    + IComponentsFactory.EXTERNAL_COMPONENTS_INNER_FOLDER + File.separatorChar
                                    + ComponentUtilities.getExtFolder(OLD_COMPONENTS_USER_INNER_FOLDER));
                            if (userComponent.equals(templatePath)) {
                                userComponentList.add(currentComp);
                            }
                        }
                    }

                    if (CommonsPlugin.isHeadless()) {
                        componentsCache.put(xmlMainFile.getAbsolutePath(), currentComp);
                    }
                } catch (MissingMainXMLComponentFileException e) {
                    log.trace(currentFolder.getName() + " is not a " + getCodeLanguageSuffix() + " component", e); //$NON-NLS-1$ //$NON-NLS-2$
                } catch (BusinessException e) {
                    BusinessException ex = new BusinessException("Cannot load component \"" + currentFolder.getName() + "\": " //$NON-NLS-1$ //$NON-NLS-2$
                            + e.getMessage(), e);
                    ExceptionHandler.process(ex, Level.WARN);
                }

                if (this.subMonitor != null) {
                    this.subMonitor.worked(1);
                }
                if (this.monitor != null && this.monitor.isCanceled()) {
                    return;
                }
            }
        }
        // TimeMeasure.display=true;
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder.listComponentsFolders"); //$NON-NLS-1$
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder.listSkeletonFiles"); //$NON-NLS-1$
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder.checkFiles"); //$NON-NLS-1$
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder.emf1"); //$NON-NLS-1$
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder.emf2"); //$NON-NLS-1$
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder.loadIcons"); //$NON-NLS-1$
        TimeMeasure.end("ComponentsFactory.loadComponentsFromFolder"); //$NON-NLS-1$
    }

    /**
     * DOC smallet Comment method "checkComponentFolder".
     * 
     * @param currentFolder
     * @return
     * @throws BusinessException
     */

    private File getComponentsLocation(String folder) {
        Bundle b = Platform.getBundle(IComponentsFactory.COMPONENTS_LOCATION);

        File file = null;
        try {
            URL url = FileLocator.find(b, new Path(folder), null);
            if (url == null) {
                return null;
            }
            URL fileUrl = FileLocator.toFileURL(url);
            file = new File(fileUrl.getPath());
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }

        return file;
    }

    /**
     * 
     * 
     * Needs to create our own class loader in order to clear the cache for a ResourceBundle. Without using a new class
     * loader each time the values would not be reread from the .properties file
     * 
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4212439
     * 
     * yzhang ComponentsFactory class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private static class ResClassLoader extends ClassLoader {

        ResClassLoader(ClassLoader parent) {
            super(parent);
        }
    }

    private ResourceBundle getComponentResourceBundle(IComponent currentComp, String source) {
        String label = ComponentFilesNaming.getInstance().getBundleName(currentComp.getName(), source);
        // String pluginFullName = currentComp.getPluginFullName();
        // System.out.println(pluginFullName);
        // Bundle bundle = Platform.getBundle(pluginFullName);
        // ClassLoader classLoader = bundle.getClass().getClassLoader();
        // return ResourceBundle.getBundle(label, Locale.getDefault(), classLoader);

        ResourceBundle bundle = ResourceBundle.getBundle(label, Locale.getDefault(), new ResClassLoader(getClass()
                .getClassLoader()));

        return bundle;
    }

    private String getCodeLanguageSuffix() {
        return LanguageManager.getCurrentLanguage().getName();
    }

    private void loadIcons(File folder, IComponent component) {
        ComponentIconLoading cil = new ComponentIconLoading(folder);

        component.setIcon32(cil.getImage32());
        component.setIcon24(cil.getImage24());
        component.setIcon16(cil.getImage16());
    }

    public int size() {
        if (componentList == null) {
            init();
        }
        return componentList.size();
    }

    public IComponent get(final String name) {
        IComponent comp = null;
        if (componentList == null) {
            init();
        }

        for (int i = 0; i < componentList.size(); i++) {
            comp = componentList.get(i);
            if (comp != null) {
                if (comp.getName().equals(name)) {
                    return comp;
                }
            }
        }
        return null;
    }

    public void initializeComponents(IProgressMonitor monitor) {
        this.monitor = monitor;
        if (componentList == null) {
            init();
        }
        this.monitor = null;
        this.subMonitor = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#getComponents()
     */
    public List<IComponent> getComponents() {
        if (componentList == null) {
            init();
        }
        return componentList;
    }

    public List<IComponent> getCustomComponents() {
        if (customComponentList == null) {
            init();
        }
        return customComponentList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#getComponentPath()
     */
    public URL getComponentPath() throws IOException {
        Bundle b = Platform.getBundle(IComponentsFactory.COMPONENTS_LOCATION);
        URL url = FileLocator.toFileURL(FileLocator.find(b, new Path(IComponentsFactory.COMPONENTS_INNER_FOLDER), null));
        return url;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#getSkeletons()
     */
    public List<String> getSkeletons() {
        if (skeletonList == null) {
            init();
        }
        return skeletonList;
    }

    public void reset() {
        componentList = null;
        skeletonList = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#getFamilyTranslation(IComponent component,
     * java.lang.String)
     */
    public String getFamilyTranslation(Object component, String text) {
        String translated = Messages.getString(text);

        // if text translated is not in local provider, look into other providers.
        if (translated.startsWith("!!")) { //$NON-NLS-1$
            if (component instanceof IComponent) {
                if (componentToProviderMap.containsKey(component)) {
                    String translatedFromProvider = componentToProviderMap.get(component).getFamilyTranslation(text);
                    if (translatedFromProvider != null) {
                        translated = translatedFromProvider;
                    }
                }
            } else if (component instanceof String) {
                if (componentsAndProvider.containsKey(component)) {
                    String translatedFromProvider = componentsAndProvider.get(component).getFamilyTranslation(text);
                    if (translatedFromProvider != null) {
                        translated = translatedFromProvider;
                    }
                }
            }
        }

        return translated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#getAllComponentsCanBeProvided()
     */
    public Map<String, ImageDescriptor> getAllComponentsCanBeProvided() {
        List source = new ArrayList();
        if (allComponents == null) {
            allComponents = new HashMap<String, ImageDescriptor>();
            source.add(IComponentsFactory.COMPONENTS_INNER_FOLDER);
            ComponentsProviderManager componentsProviderManager = ComponentsProviderManager.getInstance();
            source.addAll(componentsProviderManager.getProviders());
            for (int i = 0; i < source.size(); i++) {
                String path = null;
                Object object = source.get(i);
                if (object instanceof String) {
                    path = (String) object;
                } else if (object instanceof AbstractComponentsProvider) {
                    path = ((AbstractComponentsProvider) object).getComponentsLocation();
                }
                if (path != null) {
                    File sourceFile = getComponentsLocation(path);
                    File[] childDirectories;

                    FileFilter fileFilter = new FileFilter() {

                        public boolean accept(final File file) {
                            return file.isDirectory() && file.getName().charAt(0) != '.'
                                    && !file.getName().equals(IComponentsFactory.EXTERNAL_COMPONENTS_INNER_FOLDER);
                        }

                    };
                    if (sourceFile == null) {
                        ExceptionHandler.process(new Exception("Component Not Found")); //$NON-NLS-1$
                        continue;
                    }

                    childDirectories = sourceFile.listFiles(fileFilter);
                    if (childDirectories != null) {
                        for (File currentFolder : childDirectories) {
                            try {
                                ComponentFileChecker.checkComponentFolder(currentFolder, getCodeLanguageSuffix());
                            } catch (BusinessException e) {
                                continue;
                            }
                            File xmlMainFile = new File(currentFolder, ComponentFilesNaming.getInstance().getMainXMLFileName(
                                    currentFolder.getName(), getCodeLanguageSuffix()));
                            List<String> families = getComponentsFamilyFromXML(xmlMainFile);
                            ComponentIconLoading cil = new ComponentIconLoading(currentFolder);
                            ImageDescriptor image32 = cil.getImage32();
                            if (families != null) {
                                for (String family : families) {
                                    allComponents.put(family + FAMILY_SPEARATOR + currentFolder.getName(), image32);
                                    if (object instanceof AbstractComponentsProvider) {
                                        if (!componentsAndProvider.containsKey(family)) {
                                            componentsAndProvider.put(family, (AbstractComponentsProvider) object);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }

        }
        return allComponents;
    }

    private List<String> getComponentsFamilyFromXML(File xmlMainFile) {
        final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
        final String familysTag = "FAMILIES"; //$NON-NLS-1$
        final String header = "HEADER"; //$NON-NLS-1$
        final String technical = "TECHNICAL"; //$NON-NLS-1$

        List<String> familyNames = new ArrayList<String>();

        DocumentBuilder analyseur;
        try {
            analyseur = fabrique.newDocumentBuilder();
            analyseur.setErrorHandler(new ErrorHandler() {

                public void error(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                public void fatalError(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                public void warning(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

            });

            Document document = analyseur.parse(xmlMainFile);
            NodeList elementsByTagName = document.getElementsByTagName(header);
            String technicalValue = null;
            if (elementsByTagName != null && elementsByTagName.getLength() > 0) {
                Node item = elementsByTagName.item(0);
                NamedNodeMap attributes = item.getAttributes();
                if (attributes != null) {
                    Node namedItem = attributes.getNamedItem(technical);
                    if (namedItem != null) {
                        technicalValue = namedItem.getNodeValue();
                    }
                }
            }
            // techenical node are not visible ,so no need to return it's family
            if (technicalValue == null || !"true".equals(technicalValue)) { //$NON-NLS-1$
                NodeList element = document.getElementsByTagName(familysTag);
                if (element != null && element.getLength() > 0) {
                    Node family = element.item(0);
                    NodeList childNodes = family.getChildNodes();
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        final Node item = childNodes.item(i);
                        if (item instanceof com.sun.org.apache.xerces.internal.dom.DeferredElementImpl
                                || item instanceof org.apache.xerces.dom.DeferredElementImpl) {
                            familyNames.add(item.getTextContent());
                        }
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            ExceptionHandler.process(e);
        } catch (SAXException e) {
            ExceptionHandler.process(e);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

        return familyNames;
    }

}
