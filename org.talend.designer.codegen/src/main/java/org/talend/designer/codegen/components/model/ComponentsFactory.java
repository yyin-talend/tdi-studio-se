// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.component_cache.ComponentCachePackage;
import org.talend.core.model.component_cache.ComponentInfo;
import org.talend.core.model.component_cache.ComponentsCache;
import org.talend.core.model.component_cache.util.ComponentCacheResourceFactoryImpl;
import org.talend.core.model.components.AbstractComponentsProvider;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ComponentSetting;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.codegen.i18n.Messages;
import org.talend.designer.core.ITisLocalProviderService;
import org.talend.designer.core.ITisLocalProviderService.ResClassLoader;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.components.manager.ComponentManager;
import org.talend.designer.core.model.process.AbstractProcessProvider;
import org.talend.designer.core.ui.editor.palette.TalendEntryEditPart;
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
 * $Id: ComponentsFactory.java 52892 2010-12-20 05:52:17Z nrousseau $
 */
public class ComponentsFactory implements IComponentsFactory {

    /**
     * 
     */
    private static final String TALEND_COMPONENT_CACHE = "ComponentsCache.";

    private static final String TALEND_FILE_NAME = "cache";

    private static final String OLD_COMPONENTS_USER_INNER_FOLDER = "user"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(ComponentsFactory.class);

    private static HashSet<IComponent> componentList = null;

    private static List<IComponent> customComponentList = null;

    private List<IComponent> userComponentList = null;

    private IProgressMonitor monitor;

    private SubMonitor subMonitor;

    private static Map<String, IComponent> componentsCache = new HashMap<String, IComponent>();

    // keep a list of the current provider for the selected component, to have the family translation
    // only for components that are loaded
    private static Map<IComponent, AbstractComponentsProvider> componentToProviderMap;

    private static Map<String, AbstractComponentsProvider> componentsAndProvider = new HashMap<String, AbstractComponentsProvider>();

    private Map<String, String> allComponents;

    // 1. only the in the directory /components ,not including /resource
    // 2. include the skeleton files and external include files
    private static ArrayList<String> skeletonList = null;

    private static final String SKELETON_SUFFIX = ".skeleton"; //$NON-NLS-1$

    private static final String INCLUDEFILEINJET_SUFFIX = ".inc.javajet"; //$NON-NLS-1$

    private static final String FAMILY_SPEARATOR = "--FAMILY--"; //$NON-NLS-1$

    private boolean isCreated = false;

    private boolean isReset = false;

    // public XmiResourceManager xmiResourceManager = new XmiResourceManager();

    // this list of component is always needed, they must always be loaded at least, since they can be used for code
    // generation indirectly.
    // tFileInputFullRow + tSocketOutput : needed for DataViewer
    private static final String[] COMPONENTS_ALWAYS_NEEDED = { "tPrejob", "tPostjob", //$NON-NLS-1$ //$NON-NLS-2$ 
            "tJava", "tLibraryLoad", "tFileInputFullRow", "tSocketOutput" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

    public ComponentsFactory() {
        if (!INCLUDEFILEINJET_SUFFIX.equals(".inc.javajet")) { //$NON-NLS-1$
            ExceptionHandler.process(new IllegalStateException(Messages.getString("ComponentsFactory.parentNotRecompiled")), //$NON-NLS-1$
                    Priority.WARN);
        }
    }

    private List<ComponentSetting> getComponentsFromProject() {
        // TODO check components used from ref projects.
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        if (currentProject != null && currentProject.getEmfProject() != null) {

            List<ComponentSetting> components = (List<ComponentSetting>) currentProject.getEmfProject().getComponentsSettings();
            return components;
        }
        return null;
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
            String name = componentSetting.getName();
            if (name != null && name.equals(componentName)) {
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

    private void init(boolean duringLogon) {
        removeOldComponentsUserFolder(); // not used anymore
        long startTime = System.currentTimeMillis();

        // TimeMeasure.display = true;
        // TimeMeasure.displaySteps = true;
        // TimeMeasure.measureActive = true;

        // TimeMeasure.begin("initComponents");

        componentList = new HashSet<IComponent>();
        customComponentList = new ArrayList<IComponent>();
        skeletonList = new ArrayList<String>();
        String installLocation = new Path(Platform.getConfigurationLocation().getURL().getPath()).toFile().getAbsolutePath();
        componentToProviderMap = new HashMap<IComponent, AbstractComponentsProvider>();
        boolean isNeedClean = cleanComponentCache();
        isCreated = hasComponentFile(installLocation) && !isNeedClean && !CommonsPlugin.isHeadless();
        if (isReset) {
            isCreated = false;
        }
        ComponentsCache cache = ComponentManager.getInstance();
        try {
            if (isCreated) {
                ComponentsCache loadCache = loadComponentResource(installLocation);
                cache.getComponentEntryMap().putAll(loadCache.getComponentEntryMap());
            } else {
                cache.getComponentEntryMap().clear();
            }
            // check if any component is missing from any provider
            // if yes, re-create the cache.
            if (isCreated && isAnyComponentMissing()) {
                cache.getComponentEntryMap().clear();
                isCreated = false;
            }
        } catch (IOException e) {
            ExceptionHandler.process(e);
            cache.getComponentEntryMap().clear();
            isCreated = false;
        }

        XsdValidationCacheManager.getInstance().load();
        // 1.Load Component from extension point: components_provider
        if (!isCreated) {
            loadComponentsFromComponentsProviderExtension();
        }

        // TimeMeasure.step("initComponents", "loadComponentsFromProvider");
        if (isCreated) {
            try {
                reloadComponentsFromCache();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        // 2.Load Component from extension point: component_definition
        loadComponentsFromExtensions();
        // TimeMeasure.step("initComponents", "loadComponentsFromExtension[joblets?]");

        // TimeMeasure.step("initComponents", "reloadFromCache");

        if (!isCreated) {
            XsdValidationCacheManager.getInstance().save();
            ComponentManager.saveResource();
            isReset = false;
        }
        // TimeMeasure.step("initComponents", "createCache");
        log.debug(componentList.size() + " components loaded in " + (System.currentTimeMillis() - startTime) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$

        if (!duringLogon) {
            // CorePlugin.getDefault().getRunProcessService().updateLibraries(new HashSet<String>(), null);
        }
        // TimeMeasure.step("initComponents", "updateLibraries");

        // TimeMeasure.end("initComponents");

        // TimeMeasure.display = false;
        // TimeMeasure.displaySteps = false;
        // TimeMeasure.measureActive = false;
    }

    private boolean isAnyComponentMissing() throws IOException {
        Iterator it = ComponentManager.getInstance().getComponentEntryMap().entrySet().iterator();
        List<String> componentsList = new ArrayList<String>();
        while (it.hasNext()) {
            Map.Entry<String, ComponentInfo> entry = (Map.Entry<String, ComponentInfo>) it.next();
            componentsList.add(entry.getKey());
        }

        ECodeLanguage currentLanguage = LanguageManager.getCurrentLanguage();
        ComponentsProviderManager componentsProviderManager = ComponentsProviderManager.getInstance();
        for (AbstractComponentsProvider componentsProvider : componentsProviderManager.getProviders()) {
            if (componentsProvider.getInstallationFolder().exists()) {
                File source = componentsProvider.getInstallationFolder();
                File[] childDirectories;

                FileFilter fileFilter = new FileFilter() {

                    public boolean accept(final File file) {
                        return file.isDirectory() && file.getName().charAt(0) != '.'
                                && !file.getName().equals(IComponentsFactory.EXTERNAL_COMPONENTS_INNER_FOLDER)
                                && isComponentVisible(file.getName());
                    }

                };
                if (source == null) {
                    continue;
                }

                childDirectories = source.listFiles(fileFilter);

                for (File component : childDirectories) {
                    if (!componentsList.contains(component.getName())) {
                        String mainXmlFileName = ComponentFilesNaming.getInstance().getMainXMLFileName(component.getName(),
                                currentLanguage.getName());
                        File mainXmlFile = new File(component, mainXmlFileName);
                        if (mainXmlFile.exists()) {
                            return true;
                        }
                        // no xml file for this language, ignore it
                    }
                }
            }
        }
        return false;
    }

    /**
     * DOC guanglong.du Comment method "reloadComponentsFromCache".
     * 
     * @throws BusinessException
     */
    private void reloadComponentsFromCache() throws BusinessException {
        Map<String, String> bundleIdToPath = new HashMap<String, String>();
        ComponentsCache cache = ComponentManager.getInstance();
        Iterator it = cache.getComponentEntryMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ComponentInfo> entry = (Map.Entry<String, ComponentInfo>) it.next();
            ComponentInfo info = entry.getValue();
            String name = entry.getKey();
            if (!isComponentVisible(name)) {
                continue;
            }
            IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);
            String[] availableComponents = service.getBrandingConfiguration().getAvailableComponents();
            EmfComponent currentComp = new EmfComponent(info.getUriString(), info.getSourceBundleName(), name,
                    info.getPathSource(), cache, true);
            // if the component is not needed in the current branding,
            // and that this one IS NOT a specific component for code generation
            // just don't load it
            if (availableComponents != null
                    && !ArrayUtils.contains(availableComponents, currentComp.getName())
                    && !(ArrayUtils.contains(COMPONENTS_ALWAYS_NEEDED, currentComp.getName())
                            || currentComp.getOriginalFamilyName().contains("Technical") || currentComp.isTechnical())) {
                continue;
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
            if (currentComp.getSourceBundleName().contains("camel")) {
                currentComp.setPaletteType("CAMEL");
            } else {
                currentComp.setPaletteType("DI");
            }
            String applicationPath = bundleIdToPath.get(info.getSourceBundleName());
            if (applicationPath == null) {
                try {
                    applicationPath = FileLocator.getBundleFile(Platform.getBundle(info.getSourceBundleName())).getPath();
                    applicationPath = (new Path(applicationPath)).toPortableString();
                } catch (IOException e2) {
                    ExceptionHandler.process(e2);
                    return;
                }
                bundleIdToPath.put(info.getSourceBundleName(), applicationPath);
            }

            if (!componentList.contains(currentComp)) {
                currentComp
                        .setResourceBundle(getComponentResourceBundle(currentComp, applicationPath + info.getUriString(), null));

                File currentFile = new File(applicationPath + info.getUriString());
                loadIcons(currentFile.getParentFile(), currentComp);
                componentList.add(currentComp);
            }
        }
    }

    /**
     * DOC guanglong.du Comment method "loadComponentResource".
     * 
     * @param eclipseProject
     * @return
     * @throws IOException
     */
    private ComponentsCache loadComponentResource(String installLocation) throws IOException {
        String filePath = ComponentsFactory.TALEND_COMPONENT_CACHE
                + LanguageManager.getCurrentLanguage().toString().toLowerCase() + ComponentsFactory.TALEND_FILE_NAME;
        URI uri = URI.createFileURI(installLocation).appendSegment(filePath);
        ComponentCacheResourceFactoryImpl compFact = new ComponentCacheResourceFactoryImpl();
        Resource resource = compFact.createResource(uri);
        Map optionMap = new HashMap();
        optionMap.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
        optionMap.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        optionMap.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
        optionMap.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap());
        optionMap.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);
        resource.load(optionMap);
        ComponentsCache cache = (ComponentsCache) EcoreUtil.getObjectByType(resource.getContents(),
                ComponentCachePackage.eINSTANCE.getComponentsCache());
        return cache;
    }

    /**
     * DOC guanglong.du Comment method "hasComponentFile".
     * 
     * @param eclipseProject
     * @return
     */
    private boolean hasComponentFile(String installLocation) {
        String filePath = ComponentsFactory.TALEND_COMPONENT_CACHE
                + LanguageManager.getCurrentLanguage().toString().toLowerCase() + ComponentsFactory.TALEND_FILE_NAME;
        File file = new File(new Path(installLocation).append(filePath).toString());
        return file.exists();
    }

    private boolean cleanComponentCache() {
        return ArrayUtils.contains(Platform.getApplicationArgs(), "--clean_component_cache")
                || ArrayUtils.contains(Platform.getApplicationArgs(), "-clean");
    }

    private void loadComponentsFromComponentsProviderExtension() {
        ComponentsProviderManager componentsProviderManager = ComponentsProviderManager.getInstance();
        for (AbstractComponentsProvider componentsProvider : componentsProviderManager.getProviders()) {
            loadComponents(componentsProvider);
        }
    }

    public void loadComponentsFromExtension(String id) {
        ComponentsProviderManager componentsProviderManager = ComponentsProviderManager.getInstance();
        for (AbstractComponentsProvider componentsProvider : componentsProviderManager.getProviders()) {
            if (componentsProvider.getId() != null && componentsProvider.getId().equals(id)) {
                loadComponents(componentsProvider);
            }
        }
    }

    private void loadComponents(AbstractComponentsProvider componentsProvider) {
        if (componentsProvider != null) {
            try {
                componentsProvider.preComponentsLoad();
                File componentFile = componentsProvider.getInstallationFolder();
                if (componentFile.exists()) {
                    loadComponentsFromFolder(componentsProvider.getComponentsLocation(), componentsProvider);
                }
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    public void loadUserComponentsFromComponentsProviderExtension() {
        ComponentsProviderManager.getInstance().getProviders();
        ComponentsProviderManager componentsProviderManager = ComponentsProviderManager.getInstance();
        AbstractComponentsProvider componentsProvider = componentsProviderManager.loadUserComponentsProvidersFromExtension();
        // remove old user components
        if (this.userComponentList != null) {
            ComponentsCache cache = ComponentManager.getInstance();
            for (IComponent component : userComponentList) {
                if (componentList != null && componentList.contains(component)) {
                    componentList.remove(component);
                }
                if (customComponentList.contains(component)) {
                    customComponentList.remove(component);
                }
                if (cache.getComponentEntryMap().get(component.getName()) != null) {
                    cache.getComponentEntryMap().remove(component.getName());
                }
            }
        }
        loadComponents(componentsProvider);
        if (!CommonsPlugin.isHeadless()) {
            ComponentManager.saveResource();
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

    private void loadComponentsFromFolder(String pathSource, AbstractComponentsProvider provider) {

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
        if ("org.talend.designer.components.model.UserComponentsProvider".equals(provider.getId())
                || "org.talend.designer.components.ecosystem.EcosystemComponentsProvider".equals(provider.getId())) {
            isCustom = true;
        }

        File source;
        try {
            source = provider.getInstallationFolder();
        } catch (IOException e1) {
            ExceptionHandler.process(e1);
            return;
        }
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

        childDirectories = source.listFiles(fileFilter);

        IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);

        String[] availableComponents = service.getBrandingConfiguration().getAvailableComponents();

        FileFilter skeletonFilter = new FileFilter() {

            public boolean accept(final File file) {
                String fileName = file.getName();
                return file.isFile() && fileName.charAt(0) != '.'
                        && (fileName.endsWith(SKELETON_SUFFIX) || fileName.endsWith(INCLUDEFILEINJET_SUFFIX));
            }

        };
        BundleContext context = Platform.getProduct().getDefiningBundle().getBundleContext();
        // if (context == null) {
        // context = CodeGeneratorActivator.getDefault().getBundle().getBundleContext();
        // }
        ServiceReference sref = context.getServiceReference(PackageAdmin.class.getName());
        PackageAdmin admin = (PackageAdmin) context.getService(sref);

        if (childDirectories != null) {
            if (monitor != null) {
                this.subMonitor = SubMonitor.convert(monitor,
                        Messages.getString("ComponentsFactory.load.components"), childDirectories.length); //$NON-NLS-1$
            }
            if (skeletonList != null) {
                skeletonList.ensureCapacity(childDirectories.length);// to optimize the size of the array
                for (File currentFolder : childDirectories) {
                    // get the skeleton files first, then XML config files later.
                    File[] skeletonFiles = currentFolder.listFiles(skeletonFilter);
                    if (skeletonFiles != null) {
                        for (File file : skeletonFiles) {
                            skeletonList.add(file.getAbsolutePath()); // path
                        }
                    }

                    try {
                        ComponentFileChecker.checkComponentFolder(currentFolder, getCodeLanguageSuffix());
                        File xmlMainFile = new File(currentFolder, ComponentFilesNaming.getInstance().getMainXMLFileName(
                                currentFolder.getName(), getCodeLanguageSuffix()));

                        if (CommonsPlugin.isHeadless() && componentsCache.containsKey(xmlMainFile.getAbsolutePath())) {
                            // In headless mode, we assume the components won't change and we will use a cache
                            componentList.add(componentsCache.get(xmlMainFile.getAbsolutePath()));
                            if (isCustom) {
                                customComponentList.add(componentsCache.get(xmlMainFile.getAbsolutePath()));
                            }
                            continue;
                        }
                        String pathName = xmlMainFile.getAbsolutePath();

                        String bundleName;
                        if (!isCustom) {
                            bundleName = admin.getBundle(provider.getClass()).getSymbolicName();
                        } else {
                            bundleName = IComponentsFactory.COMPONENTS_LOCATION;
                        }
                        String applicationPath;
                        try {
                            applicationPath = FileLocator.getBundleFile(Platform.getBundle(bundleName)).getPath();
                            // applicationPath= C:\myapp\plugins\myplugin
                            applicationPath = (new Path(applicationPath)).toPortableString();
                            // applicationPath= C:/myapp/plugins/myplugin
                        } catch (IOException e2) {
                            ExceptionHandler.process(e2);
                            return;
                        }

                        // pathName = C:\myapp\plugins\myplugin\components\mycomponent\mycomponent.xml
                        pathName = (new Path(pathName)).toPortableString();
                        // pathName = C:/myapp/plugins/myplugin/components/mycomponent/mycomponent.xml
                        pathName = pathName.replace(applicationPath, "");

                        // pathName = /components/mycomponent/mycomponent.xml

                        EmfComponent currentComp = new EmfComponent(pathName, bundleName, xmlMainFile.getParentFile().getName(),
                                pathSource, ComponentManager.getInstance(), isCreated);
                        // force to call some functions to update the cache. (to improve)
                        currentComp.isVisibleInComponentDefinition();
                        currentComp.isTechnical();
                        currentComp.getOriginalFamilyName();
                        currentComp.getTranslatedFamilyName();
                        currentComp.getPluginExtension();
                        currentComp.getVersion();
                        currentComp.getModulesNeeded();
                        currentComp.getPluginDependencies();
                        // end of force cache update.

                        // if the component is not needed in the current branding,
                        // and that this one IS NOT a specific component for code generation
                        // just don't load it
                        if (availableComponents != null
                                && !ArrayUtils.contains(availableComponents, currentComp.getName())
                                && !(ArrayUtils.contains(COMPONENTS_ALWAYS_NEEDED, currentComp.getName())
                                        || currentComp.getOriginalFamilyName().contains("Technical") || currentComp.isTechnical())) {
                            continue;
                        }

                        componentToProviderMap.put(currentComp, provider);

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
                        if (provider.getId().contains("Camel")) {
                            currentComp.setPaletteType("CAMEL");
                        } else {
                            currentComp.setPaletteType("DI");
                        }

                        if (componentList.contains(currentComp)) {
                            log.warn("Component " + currentComp.getName() + " already exists. Cannot load user version."); //$NON-NLS-1$ //$NON-NLS-2$
                        } else {
                            currentComp.setResourceBundle(getComponentResourceBundle(currentComp, source.toString(), provider));
                            loadIcons(currentFolder, currentComp);
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
                        BusinessException ex = new BusinessException(
                                "Cannot load component \"" + currentFolder.getName() + "\": " //$NON-NLS-1$ //$NON-NLS-2$
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
                skeletonList.trimToSize();// to optimize the size of the array
            }
        }
    }

    /**
     * DOC smallet Comment method "checkComponentFolder".
     * 
     * @param currentFolder
     * @return
     * @throws BusinessException
     */

    private File getComponentsLocation(String folder) {
        String componentsPath = IComponentsFactory.COMPONENTS_LOCATION;
        IBrandingService breaningService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        if (breaningService.isPoweredOnlyCamel()) {
            componentsPath = IComponentsFactory.CAMEL_COMPONENTS_LOCATION;
        }
        Bundle b = Platform.getBundle(componentsPath);

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

    private ResourceBundle getComponentResourceBundle(IComponent currentComp, String source, AbstractComponentsProvider provider) {

        try {
            AbstractComponentsProvider currentProvider = provider;
            if (currentProvider == null) {
                ComponentsProviderManager componentsProviderManager = ComponentsProviderManager.getInstance();
                for (AbstractComponentsProvider curProvider : componentsProviderManager.getProviders()) {
                    String path = new Path(curProvider.getInstallationFolder().toString()).toPortableString();
                    if (source.startsWith(path)) {
                        currentProvider = curProvider;
                        break;
                    }
                }
            }
            String installPath = currentProvider.getInstallationFolder().toString();
            String label = ComponentFilesNaming.getInstance().getBundleName(currentComp.getName(),
                    installPath.substring(installPath.lastIndexOf(IComponentsFactory.COMPONENTS_INNER_FOLDER)));

            if (currentProvider.isUseLocalProvider()) {
                // if the component use local provider as storage (for user / ecosystem components)
                // then get the bundle resource from the current main component provider.

                // note: code here to review later, service like this shouldn't be used...
                ResourceBundle bundle = null;
                IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                        IBrandingService.class);
                if (brandingService.isPoweredOnlyCamel()) {
                    bundle = currentProvider.getResourceBundle(label);
                } else {
                    ITisLocalProviderService service = (ITisLocalProviderService) GlobalServiceRegister.getDefault().getService(
                            ITisLocalProviderService.class);
                    bundle = service.getResourceBundle(label);
                }
                return bundle;
            } else {
                ResourceBundle bundle = ResourceBundle.getBundle(label, Locale.getDefault(), new ResClassLoader(currentProvider
                        .getClass().getClassLoader()));
                return bundle;
            }
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

        return null;
    }

    private String getCodeLanguageSuffix() {
        return LanguageManager.getCurrentLanguage().getName();
    }

    private Map<String, ImageDescriptor> componentsImageRegistry = new HashMap<String, ImageDescriptor>();

    private void loadIcons(File folder, IComponent component) {

        ComponentIconLoading cil = new ComponentIconLoading(componentsImageRegistry, folder);

        // only call to initialize the icons in the registry
        cil.getImage32();
        cil.getImage24();
        cil.getImage16();

        component.setImageRegistry(componentsImageRegistry);
    }

    public int size() {
        if (componentList == null) {
            init(false);
        }
        return componentList.size();
    }

    public IComponent get(String name) {
        if (componentList == null) {
            init(false);
        }

        for (IComponent comp : componentList) {
            if (comp != null && comp.getName().equals(name)) {
                return comp;
            }// else keep looking
        }
        return null;
    }

    public void initializeComponents(IProgressMonitor monitor) {
        this.monitor = monitor;
        if (componentList == null) {
            init(false);
        }
        this.monitor = null;
        this.subMonitor = null;
    }

    public void initializeComponents(IProgressMonitor monitor, boolean duringLogon) {
        this.monitor = monitor;
        if (componentList == null) {
            init(duringLogon);
        }
        this.monitor = null;
        this.subMonitor = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#getComponents()
     */
    public Set<IComponent> getComponents() {
        if (componentList == null) {
            init(false);
        }
        return componentList;
    }

    public List<IComponent> getCustomComponents() {
        if (customComponentList == null) {
            init(false);
        }
        return customComponentList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#getComponentPath()
     */
    public URL getComponentPath() throws IOException {
        String componentsPath = IComponentsFactory.COMPONENTS_LOCATION;
        IBrandingService breaningService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        if (breaningService.isPoweredOnlyCamel()) {
            componentsPath = IComponentsFactory.CAMEL_COMPONENTS_LOCATION;
        }
        Bundle b = Platform.getBundle(componentsPath);
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
            init(false);
        }
        return skeletonList;
    }

    public void reset() {
        componentsImageRegistry.clear();
        componentList = null;
        skeletonList = null;
        customComponentList = null;
        allComponents = null;

    }

    public void resetCache() {
        componentsImageRegistry.clear();
        componentList = null;
        skeletonList = null;
        customComponentList = null;
        allComponents = null;
        isReset = true;
        TalendEntryEditPart.resetImageCache();
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
    public Map<String, String> getAllComponentsCanBeProvided() {
        List source = new ArrayList();
        if (allComponents == null) {
            allComponents = new HashMap<String, String>();
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
                            ComponentIconLoading cil = new ComponentIconLoading(componentsImageRegistry, currentFolder);
                            cil.getImage32();
                            if (families != null) {
                                for (String family : families) {
                                    allComponents.put(family + FAMILY_SPEARATOR + currentFolder.getName(),
                                            currentFolder.getName() + "_32");
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#resetSpecificComponents()
     */
    public void resetSpecificComponents() {
        loadComponentsFromExtensions();
    }

    public List<File> getComponentsProvidersFolder() {
        List<File> list = new ArrayList<File>();

        ComponentsProviderManager componentsProviderManager = ComponentsProviderManager.getInstance();
        for (AbstractComponentsProvider componentsProvider : componentsProviderManager.getProviders()) {
            try {
                list.add(componentsProvider.getInstallationFolder());
            } catch (IOException e) {
                ExceptionHandler.process(e);
                continue;
            }
        }
        return list;
    }

    public Map<String, ImageDescriptor> getComponentsImageRegistry() {
        return componentsImageRegistry;
    }
}
