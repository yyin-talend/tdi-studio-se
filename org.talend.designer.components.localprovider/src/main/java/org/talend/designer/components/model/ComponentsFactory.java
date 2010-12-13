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
import org.eclipse.core.runtime.CoreException;
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
import org.talend.commons.CommonsPlugin;
import org.talend.commons.emf.EmfHelper;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.component_cache.ComponentCacheFactory;
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
import org.talend.designer.components.i18n.Messages;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.components.manager.ComponentManager;
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

    private static ComponentsCache cache = ComponentManager.getInstance();

    private static Map<String, IComponent> componentsCache = new HashMap<String, IComponent>();

    // keep a list of the current provider for the selected component, to have the family translation
    // only for components that are loaded
    private static Map<IComponent, AbstractComponentsProvider> componentToProviderMap;

    private static Map<String, AbstractComponentsProvider> componentsAndProvider = new HashMap<String, AbstractComponentsProvider>();

    private Map<String, ImageDescriptor> allComponents;

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
        isCreated = hasComponentFile(installLocation) && !isNeedClean;
        if (isReset) {
            isCreated = false;
            cache.getComponentEntryMap().clear();
        }

        if (isCreated) {
            ComponentsCache loadCache = null;
            try {
                loadCache = loadComponentResource(installLocation);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
            cache = loadCache;
        }
        if (cache == null) {
            cache = ComponentCacheFactory.eINSTANCE.createComponentsCache();
        }
        XsdValidationCacheManager.getInstance().load();
        // 1. Load system components:
        if (!isCreated) {
            loadComponentsFromFolder(IComponentsFactory.COMPONENTS_INNER_FOLDER);
        }
        // TimeMeasure.step("initComponents", "loadComponents");

        // 2.Load Component from extension point: components_provider
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
        // 3.Load Component from extension point: component_definition
        loadComponentsFromExtensions();
        // TimeMeasure.step("initComponents", "loadComponentsFromExtension[joblets?]");

        // TimeMeasure.step("initComponents", "reloadFromCache");

        if (!isCreated) {
            XsdValidationCacheManager.getInstance().save();

            try {
                Resource resource = createComponentCacheResource(installLocation);
                resource.getContents().add(cache);
                EmfHelper.saveResource(cache.eResource());
            } catch (PersistenceException e1) {
                ExceptionHandler.process(e1);
            }
            isReset = false;
        }
        // TimeMeasure.step("initComponents", "createCache");
        log.debug(componentList.size() + " components loaded in " + (System.currentTimeMillis() - startTime) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$
        try {
            CorePlugin.getDefault().getRunProcessService().updateLibraries();
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
        // TimeMeasure.step("initComponents", "updateLibraries");

        // TimeMeasure.end("initComponents");

        // TimeMeasure.display = false;
        // TimeMeasure.displaySteps = false;
        // TimeMeasure.measureActive = false;
    }

    /**
     * DOC guanglong.du Comment method "reloadComponentsFromCache".
     * 
     * @throws BusinessException
     */
    private void reloadComponentsFromCache() throws BusinessException {
        Iterator it = cache.getComponentEntryMap().entrySet().iterator();
        File pathFile = getComponentsLocation(IComponentsFactory.COMPONENTS_INNER_FOLDER);
        while (it.hasNext()) {
            Map.Entry<String, ComponentInfo> entry = (Map.Entry<String, ComponentInfo>) it.next();
            ComponentInfo info = entry.getValue();
            String name = entry.getKey();
            if (!isComponentVisible(name)) {
                continue;
            }
            IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);
            String[] availableComponents = service.getBrandingConfiguration().getAvailableComponents();
            EmfComponent currentComp = new EmfComponent(info.getUriString(), name, info.getPathSource(), cache, true);
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
            if (!componentList.contains(currentComp)) {
                currentComp.setResourceBundle(getComponentResourceBundle(currentComp, info.getPathSource()));

                File currentFile = new File(pathFile.getAbsoluteFile() + info.getUriString());
                loadIcons(currentFile.getParentFile(), currentComp);
                componentList.add(currentComp);
            }
        }
    }

    /**
     * DOC guanglong.du Comment method "createComponentCacheResource".
     * 
     * @param eclipseProject
     * @return
     */
    private Resource createComponentCacheResource(String installLocation) {
        return ComponentManager.createComponentCacheResource(installLocation);
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
        File file = file = new File(new Path(installLocation).append(filePath).toString());
        return file.exists();
    }

    private boolean cleanComponentCache() {
        return ArrayUtils.contains(Platform.getApplicationArgs(), "--clean_component_cache");
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
                    if (customComponentList.contains(component)) {
                        customComponentList.remove(component);
                    }
                    if (cache.getComponentEntryMap().get(component.getName()) != null) {
                        cache.getComponentEntryMap().remove(component.getName());
                    }
                }
            }
            if (componentsProvider.getInstallationFolder().exists()) {
                loadComponentsFromFolder(componentsProvider.getComponentsLocation(), componentsProvider);
            }
            ComponentManager.saveResource();
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
        File replaceSource = getComponentsLocation(IComponentsFactory.COMPONENTS_INNER_FOLDER);
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

        if (childDirectories != null) {
            if (monitor != null) {
                this.subMonitor = SubMonitor.convert(monitor,
                        Messages.getString("ComponentsFactory.load.components"), childDirectories.length); //$NON-NLS-1$
            }
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
                    pathName = pathName.replace(replaceSource.getAbsolutePath(), "");
                    EmfComponent currentComp = new EmfComponent(pathName, xmlMainFile.getParentFile().getName(), pathSource,
                            cache, isCreated);
                    // force to call some functions to update the cache. (to improve)
                    currentComp.isVisibleInComponentDefinition();
                    currentComp.isTechnical();
                    currentComp.getOriginalFamilyName();
                    currentComp.getTranslatedFamilyName();
                    currentComp.getPluginFullName();
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

                    if (componentList.contains(currentComp)) {
                        log.warn("Component " + currentComp.getName() + " already exists. Cannot load user version."); //$NON-NLS-1$ //$NON-NLS-2$
                    } else {
                        currentComp.setResourceBundle(getComponentResourceBundle(currentComp, pathSource));
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
            skeletonList.trimToSize();// to optimize the size of the array
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

    public IComponent get(String name) {
        if (componentList == null) {
            init();
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
    public Set<IComponent> getComponents() {
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
        customComponentList = null;
    }

    public void resetCache() {
        componentList = null;
        skeletonList = null;
        customComponentList = null;
        isReset = true;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponentsFactory#resetSpecificComponents()
     */
    public void resetSpecificComponents() {
        loadComponentsFromExtensions();
    }

}
