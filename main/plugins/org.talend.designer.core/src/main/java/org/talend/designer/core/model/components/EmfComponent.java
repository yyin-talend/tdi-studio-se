// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.model.components;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.utils.image.ColorUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.component_cache.ComponentCacheFactory;
import org.talend.core.model.component_cache.ComponentInfo;
import org.talend.core.model.component_cache.ComponentsCache;
import org.talend.core.model.components.AbstractComponentsProvider;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.components.EReadOnlyComlumnPosition;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IMultipleComponentItem;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.param.ERepositoryCategoryType;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnectionProperty;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.ExternalNodesFactory;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.temp.ECodePart;
import org.talend.core.model.utils.SQLPatternUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.util.ComponentsLocationProvider;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.core.ui.component.settings.ComponentsSettingsHelper;
import org.talend.core.ui.services.IComponentsLocalProviderService;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ITisLocalProviderService;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.manager.ImportModuleManager;
import org.talend.designer.core.model.utils.emf.component.ADVANCEDPARAMETERSType;
import org.talend.designer.core.model.utils.emf.component.ARGType;
import org.talend.designer.core.model.utils.emf.component.COLUMNType;
import org.talend.designer.core.model.utils.emf.component.COMPONENTType;
import org.talend.designer.core.model.utils.emf.component.CONNECTORType;
import org.talend.designer.core.model.utils.emf.component.ComponentFactory;
import org.talend.designer.core.model.utils.emf.component.DEFAULTType;
import org.talend.designer.core.model.utils.emf.component.DocumentRoot;
import org.talend.designer.core.model.utils.emf.component.FORMATType;
import org.talend.designer.core.model.utils.emf.component.HEADERType;
import org.talend.designer.core.model.utils.emf.component.IMPORTSType;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.component.ITEMSType;
import org.talend.designer.core.model.utils.emf.component.ITEMType;
import org.talend.designer.core.model.utils.emf.component.LINKTOType;
import org.talend.designer.core.model.utils.emf.component.PARAMETERType;
import org.talend.designer.core.model.utils.emf.component.RETURNType;
import org.talend.designer.core.model.utils.emf.component.SQLTEMPLATESType;
import org.talend.designer.core.model.utils.emf.component.SQLTEMPLATEType;
import org.talend.designer.core.model.utils.emf.component.TABLEType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATEPARAMType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATESType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATEType;
import org.talend.designer.core.model.utils.emf.component.impl.PLUGINDEPENDENCYTypeImpl;
import org.talend.designer.core.model.utils.emf.component.util.ComponentResourceFactoryImpl;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;
import org.talend.designer.core.utils.JavaProcessUtil;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.hadoop.distribution.ComponentType;
import org.talend.hadoop.distribution.DistributionFactory;
import org.talend.hadoop.distribution.DistributionModuleGroup;
import org.talend.hadoop.distribution.ESparkVersion;
import org.talend.hadoop.distribution.component.HadoopComponent;
import org.talend.hadoop.distribution.component.SparkComponent;
import org.talend.hadoop.distribution.condition.BasicExpression;
import org.talend.hadoop.distribution.condition.ComponentCondition;
import org.talend.hadoop.distribution.condition.EqualityOperator;
import org.talend.hadoop.distribution.condition.NestedComponentCondition;
import org.talend.hadoop.distribution.condition.SimpleComponentCondition;
import org.talend.hadoop.distribution.constants.SparkBatchConstant;
import org.talend.hadoop.distribution.constants.apache.ESparkMode;
import org.talend.hadoop.distribution.constants.databricks.DatabricksRuntimeVersion;
import org.talend.hadoop.distribution.dynamic.template.AbstractDynamicDistributionTemplate;
import org.talend.hadoop.distribution.dynamic.template.IDynamicDistributionTemplate;
import org.talend.hadoop.distribution.helper.DistributionsManager;
import org.talend.hadoop.distribution.helper.HadoopDistributionsHelper;
import org.talend.hadoop.distribution.model.DistributionBean;
import org.talend.hadoop.distribution.model.DistributionVersion;
import org.talend.hadoop.distribution.model.DistributionVersionModule;
import org.talend.hadoop.distribution.model.DynamicDistributionVersion;
import org.talend.hadoop.distribution.utils.ComponentConditionUtil;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;

/**
 *
 * Component manager that read each information in a xml file with Emf. <br/>
 * $Id$
 */
public class EmfComponent extends AbstractBasicComponent {

    private static Logger log = Logger.getLogger(EmfComponent.class);

    private static final String EQUALS = "=="; //$NON-NLS-1$

    private static final String DEFAULT_COLOR = "255;255;255"; //$NON-NLS-1$

    private final String uriString;

    private final String name;

    private boolean isLoaded, areHadoopLibsLoaded, areHadoopDistribsLoaded = false;

    private String hadoopDistribsCacheVersion = ""; //$NON-NLS-1$

    private String hadoopLibCacheVersion = ""; //$NON-NLS-1$

    private COMPONENTType compType;

    public static final String BUILTIN = "BUILT_IN"; //$NON-NLS-1$

    public static final String REPOSITORY = "REPOSITORY"; //$NON-NLS-1$

    public static final String TNS_FILE = "USE_TNS_FILE"; //$NON-NLS-1$

    public static final String TEXT_BUILTIN = Messages.getString("EmfComponent.builtIn"); //$NON-NLS-1$

    public static final String TEXT_REPOSITORY = Messages.getString("EmfComponent.repository"); //$NON-NLS-1$

    public static final String TEXT_TNS_FILE = Messages.getString("EmfComponent.tnsfile"); //$NON-NLS-1$

    public static final String TSTATCATCHER_NAME = "tStatCatcher"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_UTF_8 = "UTF-8"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_ISO_8859_15 = "ISO-8859-15"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_CUSTOM = "CUSTOM"; //$NON-NLS-1$

    private static final String STRING_TYPE = "String"; //$NON-NLS-1$

    private static final boolean ADVANCED_PROPERTY = true;

    private static final boolean NORMAL_PROPERTY = false;

    private static final String SIMPLE = "simple"; //$NON-NLS-1$

    private static final String[] CAMEL_LANGUAGE_DISPLAY_NAMES = {
            "JavaScript", //$NON-NLS-1$
            "JSonPath", //$NON-NLS-1$
            "JXPath", //$NON-NLS-1$
            "Property", //$NON-NLS-1$
            "Simple", //$NON-NLS-1$
            "XPath", //$NON-NLS-1$
            "XQuery" //$NON-NLS-1$
    };

    private static final String[] CAMEL_LANGUAGE_ITEM_VALUES = {
            "javaScript", //$NON-NLS-1$
            "jsonpath", //$NON-NLS-1$
            "jxpath", //$NON-NLS-1$
            "property", //$NON-NLS-1$
            "simple", //$NON-NLS-1$
            "xpath", //$NON-NLS-1$
            "xquery" //$NON-NLS-1$
    };

    private static final String[] CAMEL_LANGUAGE_DISPLAY_CODE_NAMES = {
            "JAVASCRIPT", //$NON-NLS-1$
            "JSONPATH", //$NON-NLS-1$
            "JXPATH", //$NON-NLS-1$
            "PROPERTY", //$NON-NLS-1$
            "SIMPLE", //$NON-NLS-1$
            "XPATH", //$NON-NLS-1$
            "XQUERY" //$NON-NLS-1$
    };

    private ResourceBundle resourceBundle;

    private String pathSource;

    private Boolean useMerge = null;

    private Boolean useLookup = null;

    private Boolean useImport = null;

    private Map<String, String> translatedMap = new HashMap<>();

    private String translatedFamilyName;

    private ComponentInfo info;

    // weak ref used so that memory is not used by a static ComponentResourceFactoryImpl instance
    private static SoftReference<ComponentResourceFactoryImpl> compResFactorySoftRef;

    // weak ref used so that memory is not used by a static HashMap instance
    private static SoftReference<Map> optionMapSoftRef;

    private static final String PROP_LOAD_DYNAMIC_DISTRIBUTION_MODULE = "studio.dynamic.distribution.modules.load";

    private AbstractComponentsProvider provider;

    public EmfComponent(String name, ComponentInfo ci,
            AbstractComponentsProvider provider) throws BusinessException {
        this.uriString = ci.getUriString();
        this.name = name;
        this.pathSource = ci.getPathSource();
        this.bundleName = ci.getSourceBundleName();
        this.provider = provider;
        this.info = ci;
    }

    public EmfComponent(String uriString, String bundleId, String name, String pathSource, ComponentsCache cache, boolean isload,
            AbstractComponentsProvider provider) throws BusinessException {
        this.uriString = uriString;
        this.name = name;
        this.pathSource = pathSource;
        this.bundleName = bundleId;
        this.provider = provider;
        if (provider.isCustom() && !isload) {
            loadForCacheAtrributes();
            return;
        }
        if (!isload) {
            loadForCacheAtrributes();

            if (!cache.getComponentEntryMap().containsKey(getName())) {
                cache.getComponentEntryMap().put(getName(), new BasicEList<ComponentInfo>());
            }
            EList<ComponentInfo> componentsInfo = cache.getComponentEntryMap().get(getName());
            Iterator<ComponentInfo> it = componentsInfo.iterator();
            while (it.hasNext()) {
                ComponentInfo cInfo = it.next();
                if (cInfo.getSourceBundleName().equals(bundleId)) {
                    it.remove();
                    // in case we already had in the cache the same component
                    // just remove it to force to reload all from cache.
                    break;
                }
            }
            componentsInfo.add(info);
        } else {
            EList<ComponentInfo> componentsInfo = cache.getComponentEntryMap().get(getName());
            for (ComponentInfo cInfo : componentsInfo) {
                if (cInfo.getSourceBundleName().equals(bundleId)) {
                    info = cInfo;
                    break;
                }
            }
            if (info == null) {
                throw new BusinessException("Component " + name + " not found in cache for bundle " + bundleId //$NON-NLS-1$ //$NON-NLS-2$
                        + ". Please reinitalize the cache."); //$NON-NLS-1$
            }
            isLoaded = true;
        }
    }

    public COMPONENTType getEmfComponentType() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        return compType;
    }

    private ResourceBundle getComponentResourceBundle(IComponent currentComp, String source, String cachedPathSource,
            AbstractComponentsProvider provider) {
        try {
            AbstractComponentsProvider currentProvider = provider;
            if (currentProvider == null) {
                ComponentsProviderManager componentsProviderManager = ComponentsProviderManager.getInstance();
                Collection<AbstractComponentsProvider> providers = componentsProviderManager.getProviders();
                for (AbstractComponentsProvider curProvider : providers) {
                    String path = new Path(curProvider.getInstallationFolder().toString()).toPortableString();
                    if (source.startsWith(path)) {
                        // fix for TDI-19889 and TDI-20507 to get the correct component provider
                        if (cachedPathSource != null) {
                            if (path.contains(cachedPathSource)) {
                                currentProvider = curProvider;
                                break;
                            }
                        } else {
                            currentProvider = curProvider;
                            break;
                        }
                    }
                }
            }
            String installPath = currentProvider.getInstallationFolder().toString();
            String label = ComponentFilesNaming.getInstance().getBundleName(currentComp.getName(),
                    installPath.substring(installPath.lastIndexOf(IComponentsFactory.COMPONENTS_INNER_FOLDER)));

            if (currentProvider instanceof ComponentsLocationProvider) {
                return currentProvider.getResourceBundle(label);
            } else if (currentProvider.isUseLocalProvider()) {
                // if the component use local provider as storage (for user / ecosystem components)
                // then get the bundle resource from the current main component provider.

                // note: code here to review later, service like this shouldn't be used...
                ResourceBundle bundle = null;
                IBrandingService brandingService = GlobalServiceRegister.getDefault().getService(
                        IBrandingService.class);
                if (brandingService.isPoweredOnlyCamel()) {
                    bundle = currentProvider.getResourceBundle(label);
                } else {
                    ITisLocalProviderService service = GlobalServiceRegister.getDefault().getService(
                            ITisLocalProviderService.class);
                    bundle = service.getResourceBundle(label);
                }
                return bundle;
            } else {
                ResourceBundle bundle = ResourceBundle.getBundle(label, Locale.getDefault(), new ResClassLoader(currentProvider
                        .getClass().getClassLoader()));
                return bundle;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        return null;
    }

    public static class ResClassLoader extends ClassLoader {

        public ResClassLoader(ClassLoader parent) {
            super(parent);
        }
    }

    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null && provider != null) {
            try {
                resourceBundle = getComponentResourceBundle(this, provider.getInstallationFolder().toString(), null, provider);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        }
        return this.resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    private String getTranslatedValue(final String nameValue) {
        String returnValue = nameValue;
        if (translatedMap.containsKey(nameValue)) {
            return translatedMap.get(nameValue);
        }
        // modified by wzhang. update translations for components.
        returnValue = Messages.getString(nameValue, this.getName(), getResourceBundle());
        translatedMap.put(nameValue, returnValue);
        return returnValue;
    }

    @SuppressWarnings("unchecked")
    private void load() throws BusinessException {
        if (!isLoaded) {
            File file = new File((ComponentBundleToPath.getPathFromBundle(bundleName) + uriString).replaceAll("\\\\", "/")); //$NON-NLS-1$ //$NON-NLS-2$
            URI createURI = URI.createURI(file.toURI().toString());
            Resource res = getComponentResourceFactoryImpl().createResource(createURI);
            try {
                res.load(getLoadingOptionMap());

                DocumentRoot xmlDoc;
                xmlDoc = (DocumentRoot) res.getContents().get(0);

                compType = xmlDoc.getCOMPONENT();

                // just load the externalNode plugin to check if the plugin
                // exists.
                if (compType.getHEADER().getEXTENSION() != null) {
                    try {
                        ExternalNodesFactory.getInstance(compType.getHEADER().getEXTENSION());
                    } catch (RuntimeException re) {// unfortunatly this methos throws a runtime Exception which is bad
                        Exception compLoadException = new Exception("Component " + this.name //$NON-NLS-1$
                                + " load error.\nbecause the exception:" + re.getCause().getMessage(), re); //$NON-NLS-1$
                        MessageBoxExceptionHandler.process(compLoadException);
                        throw new BusinessException("Failed to load plugin :" + this.getPluginExtension(), re); //$NON-NLS-1$
                    }
                }

                if (compType.getFAMILIES() == null || compType.getFAMILIES().getFAMILY().isEmpty()) {
                    throw new BusinessException("FAMILIES definition missing or is empty in the component"); //$NON-NLS-1$
                }

                isLoaded = true;
            } catch (IOException e) {
                throw new BusinessException(Messages.getString("EmfComponent.0", uriString), e); //$NON-NLS-1$
            }
        }
    }

    @Override
    public List<ElementParameter> createElementParameters(INode node) {
        List<ElementParameter> listParam;
        listParam = new ArrayList<ElementParameter>();
        addMainParameters(listParam, node);
        addPropertyParameters(listParam, node, NORMAL_PROPERTY);
        addPropertyParameters(listParam, node, ADVANCED_PROPERTY);
        initializePropertyParameters(listParam);
        checkSchemaParameter(listParam, node);
        addViewParameters(listParam, node);
        addRouteExpressionParameters(listParam, node);
        addDocParameters(listParam, node);
        addSqlPatternParameters(listParam, node);
        addValidationRulesParameters(listParam, node);
        return listParam;
    }

    /**
     * DOC nrousseau Comment method "checkSchemaParameter".
     *
     * @param listParam
     */
    private void checkSchemaParameter(List<ElementParameter> listParam, INode node) {
        boolean acceptInputFlow = false;
        List<NodeConnector> connectors = createConnectors(node);
        for (NodeConnector connector : connectors) {
            if (connector.getName().equals(EConnectionType.FLOW_MAIN.getName())) {
                if (connector.getMaxLinkInput() != 0 && !connector.isBuiltIn()) {
                    acceptInputFlow = true;
                }
                break;
            }
        }

        boolean hasSchemaType = false;
        for (ElementParameter param : listParam) {
            if (param.getFieldType().equals(EParameterFieldType.SCHEMA_TYPE)
                    || param.getFieldType().equals(EParameterFieldType.DCSCHEMA)) {
                hasSchemaType = true;
                break;
            }
        }

        // if the
        if (acceptInputFlow && !hasSchemaType) {
            // increment the row number for each parameter
            for (ElementParameter param : listParam) {
                if (param.getCategory().equals(EComponentCategory.BASIC)) {
                    param.setNumRow(param.getNumRow() + 1);
                }
            }

            String context = "FLOW"; //$NON-NLS-1$
            ElementParameter parentParam = new ElementParameter(node);
            parentParam.setName(EParameterName.NOT_SYNCHRONIZED_SCHEMA.getName());
            parentParam.setDisplayName(EParameterName.SCHEMA_TYPE.getDisplayName());
            parentParam.setFieldType(EParameterFieldType.SCHEMA_TYPE);
            parentParam.setCategory(EComponentCategory.BASIC);
            parentParam.setNumRow(1);
            parentParam.setReadOnly(false);
            parentParam.setShow(false);
            parentParam.setContext(context);
            listParam.add(parentParam);

            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.SCHEMA_TYPE.getName());
            newParam.setDisplayName(EParameterName.SCHEMA_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY, TNS_FILE });
            newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY, TNS_FILE });
            newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY, TNS_FILE });
            newParam.setValue(BUILTIN);
            newParam.setNumRow(1);
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            newParam.setShow(true);
            newParam.setReadOnly(true);

            newParam.setContext(context);
            newParam.setParentParameter(parentParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
            newParam.setDisplayName(EParameterName.REPOSITORY_SCHEMA_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setNumRow(1);
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setShow(false);
            newParam.setRequired(true);
            newParam.setContext(context);
            newParam.setParentParameter(parentParam);
        }

    }

    @Override
    public List<NodeReturn> createReturns(INode parentNode) {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        List<NodeReturn> listReturn;
        RETURNType retType;
        EList returnList;
        NodeReturn nodeRet;

        listReturn = new ArrayList<NodeReturn>();
        // ****************** add standard returns ******************
        nodeRet = new NodeReturn();
        nodeRet.setAvailability("AFTER"); //$NON-NLS-1$
        nodeRet.setType(STRING_TYPE);
        nodeRet.setVarName("ERROR_MESSAGE"); //$NON-NLS-1$
        nodeRet.setDisplayName("Error Message"); //$NON-NLS-1$
        nodeRet.setName("ERROR_MESSAGE"); //$NON-NLS-1$
        listReturn.add(nodeRet);

        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
            nodeRet = new NodeReturn();
            nodeRet.setAvailability("AFTER"); //$NON-NLS-1$
            nodeRet.setVarName("PERL_ERROR_MESSAGE"); //$NON-NLS-1$
            nodeRet.setDisplayName("Perl Error Message"); //$NON-NLS-1$
            nodeRet.setName("PERL_ERROR_MESSAGE"); //$NON-NLS-1$
            nodeRet.setType(STRING_TYPE);
            listReturn.add(nodeRet);

            nodeRet = new NodeReturn();
            nodeRet.setAvailability("AFTER"); //$NON-NLS-1$
            nodeRet.setVarName("PERL_ERROR_CODE"); //$NON-NLS-1$
            nodeRet.setDisplayName("Perl Error Code"); //$NON-NLS-1$
            nodeRet.setName("PERL_ERROR_CODE"); //$NON-NLS-1$
            nodeRet.setType(STRING_TYPE);
            listReturn.add(nodeRet);
        }
        // ****************** end of standard returns ******************
        returnList = compType.getRETURNS().getRETURN();

        for (int i = 0; i < returnList.size(); i++) {
            retType = (RETURNType) returnList.get(i);
            nodeRet = new NodeReturn();
            nodeRet.setAvailability(retType.getAVAILABILITY());
            nodeRet.setVarName(retType.getNAME());
            nodeRet.setDisplayName(getTranslatedValue(retType.getNAME() + "." + PROP_NAME)); //$NON-NLS-1$
            nodeRet.setName(retType.getNAME());
            nodeRet.setType(retType.getTYPE());
            nodeRet.setShowIf(retType.getSHOWIF());
            listReturn.add(nodeRet);
        }
        return listReturn;
    }

    private void addDocParameters(final List<ElementParameter> listParam, INode node) {
        ElementParameter param;

        param = new ElementParameter(node);
        param.setName(EParameterName.INFORMATION.getName());
        param.setValue(new Boolean(false));
        param.setDisplayName(EParameterName.INFORMATION.getDisplayName());
        param.setFieldType(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.DOC);
        param.setNumRow(1);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        param.setDefaultValue(param.getValue());
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.COMMENT.getName());
        param.setValue(""); //$NON-NLS-1$
        param.setDisplayName(EParameterName.COMMENT.getDisplayName());
        param.setFieldType(EParameterFieldType.MEMO);
        param.setNbLines(10);
        param.setCategory(EComponentCategory.DOC);
        param.setNumRow(2);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        param.setDefaultValue(param.getValue());
        listParam.add(param);
    }

    /**
     * DOC ycbai Comment method "addValidationRulesParameters".
     *
     * @param listParam
     * @param node
     */
    private void addValidationRulesParameters(final List<ElementParameter> listParam, INode node) {
        ElementParameter param;

        param = new ElementParameter(node);
        param.setName(EParameterName.VALIDATION_RULES.getName());
        param.setValue(new Boolean(false));
        param.setDisplayName(EParameterName.VALIDATION_RULES.getDisplayName());
        param.setFieldType(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.VALIDATION_RULES);
        param.setNumRow(1);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setDefaultValue(param.getValue());
        param.setShow(true);
        listParam.add(param);

        listParam.add(addValidationRuleType(node, 3));
    }

    private ElementParameter addValidationRuleType(INode node, int rowNb) {
        String context = "FLOW"; //$NON-NLS-1$
        ElementParameter parentParam = new ElementParameter(node);
        parentParam.setName(EParameterName.VALIDATION_RULE_TYPE.getName());
        parentParam.setDisplayName(EParameterName.VALIDATION_RULE_TYPE.getDisplayName());
        parentParam.setFieldType(EParameterFieldType.VALIDATION_RULE_TYPE);
        parentParam.setCategory(EComponentCategory.VALIDATION_RULES);
        parentParam.setNumRow(rowNb);
        parentParam.setReadOnly(false);
        parentParam.setShow(true);
        parentParam.setShowIf(EParameterName.VALIDATION_RULES.getName() + " == 'true'"); //$NON-NLS-1$
        parentParam.setContext(context);
        parentParam.setRepositoryValue(ERepositoryCategoryType.VALIDATIONRULES.getName());
        parentParam.setValue(""); //$NON-NLS-1$

        ElementParameter newParam = new ElementParameter(node);
        newParam.setCategory(EComponentCategory.VALIDATION_RULES);
        newParam.setName(EParameterName.VALIDATION_RULE_TYPE.getName());
        newParam.setDisplayName(EParameterName.VALIDATION_RULE_TYPE.getDisplayName());
        newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
        newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY });
        newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
        newParam.setValue(BUILTIN);
        newParam.setNumRow(rowNb);
        newParam.setFieldType(EParameterFieldType.TECHNICAL);
        newParam.setShow(true);
        newParam.setShowIf(parentParam.getName() + " =='" + REPOSITORY + "'"); //$NON-NLS-1$ //$NON-NLS-2$
        newParam.setReadOnly(false);
        newParam.setContext(context);
        newParam.setParentParameter(parentParam);

        newParam = new ElementParameter(node);
        newParam.setCategory(EComponentCategory.VALIDATION_RULES);
        newParam.setName(EParameterName.REPOSITORY_VALIDATION_RULE_TYPE.getName());
        newParam.setDisplayName(EParameterName.REPOSITORY_VALIDATION_RULE_TYPE.getDisplayName());
        newParam.setListItemsDisplayName(new String[] {});
        newParam.setListItemsValue(new String[] {});
        newParam.setNumRow(rowNb);
        newParam.setFieldType(EParameterFieldType.TECHNICAL);
        newParam.setValue(""); //$NON-NLS-1$
        newParam.setShow(false);
        newParam.setRequired(true);
        newParam.setContext(context);
        newParam.setParentParameter(parentParam);

        return parentParam;
    }

    private void addSqlPatternParameters(final List<ElementParameter> listParam, INode node) {
        if (!node.isELTComponent()) {
            return;
        }
        ElementParameter param;
        SQLTEMPLATESType patterns = compType.getSQLTEMPLATES();
        if (patterns == null) {
            return;
        }
        param = new ElementParameter(node);
        param.setName(EParameterName.SQLPATTERN_DB_NAME.getName());
        param.setDisplayName(EParameterName.SQLPATTERN_DB_NAME.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setNbLines(5);
        param.setValue(patterns.getDB());
        param.setCategory(EComponentCategory.SQL_PATTERN);
        param.setNumRow(1);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.SQLPATTERN_VALUE.getName());
        param.setDisplayName(EParameterName.SQLPATTERN_VALUE.getDisplayName());
        param.setFieldType(EParameterFieldType.TABLE);
        param.setNbLines(10);
        param.setCategory(EComponentCategory.SQL_PATTERN);
        param.setNumRow(1);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);

        EList patternList = patterns.getSQLTEMPLATE();

        // this is the sql patterns defined in the node's xml
        List<String> patternNames = new ArrayList<String>();
        List<Map> value = new ArrayList<Map>();
        for (Iterator iterator = patternList.iterator(); iterator.hasNext();) {
            SQLTEMPLATEType pattern = (SQLTEMPLATEType) iterator.next();
            Map map = new HashMap();
            SQLPatternItem sqlItem = getSQLPatternItem(pattern.getNAME(), patterns.getDB());
            if (sqlItem == null) {
                continue;
            }
            map.put(SQLPatternUtils.SQLPATTERNLIST, sqlItem.getProperty().getId() + SQLPatternUtils.ID_SEPARATOR
                    + sqlItem.getProperty().getLabel());
            patternNames.add(pattern.getNAME());
            value.add(map);
        }
        param.setValue(value);

        String[] listRepositoryItem = new String[1];
        String[] listItemsDisplayValue = new String[1];
        String[] listItemsDisplayCodeValue = new String[1];
        Object[] listItemsValue = new Object[1];
        String[] listItemsShowIf = new String[1];
        String[] listItemsNotShowIf = new String[1];
        String[] listField = new String[1];

        listItemsDisplayCodeValue[0] = SQLPatternUtils.SQLPATTERNLIST;
        listItemsDisplayValue[0] = "SQLTemplate List"; //$NON-NLS-1$

        ElementParameter newParam = new ElementParameter(node);
        newParam = new ElementParameter(node);
        newParam.setName(SQLPatternUtils.SQLPATTERNLIST);
        newParam.setFilter(""); //$NON-NLS-1$
        newParam.setDisplayName(""); //$NON-NLS-1$
        newParam.setFieldType(EParameterFieldType.CLOSED_LIST);
        newParam.setContext(""); //$NON-NLS-1$
        newParam.setValue(""); //$NON-NLS-1$
        {
            String[] allPatternNames = getSqlPatternsByDB(patterns.getDB());

            String[] names = allPatternNames;
            newParam.setListItemsDisplayName(names);
            newParam.setListItemsDisplayCodeName(names);
            newParam.setListItemsValue(names);
            newParam.setListRepositoryItems(names);
            newParam.setListItemsShowIf(new String[names.length]);
            newParam.setListItemsNotShowIf(new String[names.length]);
            newParam.setDefaultClosedListValue(allPatternNames[0]);
            newParam.setValue(allPatternNames[0]);
        }

        listItemsValue[0] = newParam;

        listField[0] = EParameterFieldType.CLOSED_LIST.getName();
        param.setListItemsDisplayName(listItemsDisplayValue);
        param.setListItemsDisplayCodeName(listItemsDisplayCodeValue);
        param.setListItemsValue(listItemsValue);
        param.setListRepositoryItems(listRepositoryItem);
        param.setListItemsShowIf(listItemsShowIf);
        param.setListItemsNotShowIf(listItemsNotShowIf);
        listParam.add(param);

    }

    /**
     * yzhang Comment method "getSQLPatternItem".
     *
     * @param sqlpatternName
     * @param eltNodeName
     * @return
     */
    private SQLPatternItem getSQLPatternItem(String sqlpatternName, String eltNodeName) {

        SQLPatternItem sqlpatternItem = null;
        try {
            List<IRepositoryViewObject> list = DesignerPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory()
                    .getAll(ERepositoryObjectType.SQLPATTERNS, false);

            for (IRepositoryViewObject repositoryObject : list) {
                SQLPatternItem item = (SQLPatternItem) repositoryObject.getProperty().getItem();
                if (item.getEltName().equals(eltNodeName) && item.getProperty().getLabel().equals(sqlpatternName)) {
                    sqlpatternItem = item;
                    break;
                }

            }

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return sqlpatternItem;
    }

    /**
     * DOC bqian Comment method "getSqlPatternsByDB".
     *
     * @param db
     * @return
     */
    private String[] getSqlPatternsByDB(String db) {
        List<String> patterns = new ArrayList<String>();
        try {

            List<IRepositoryViewObject> list = DesignerPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory()
                    .getAll(ERepositoryObjectType.SQLPATTERNS, false);
            for (IRepositoryViewObject repositoryObject : list) {
                SQLPatternItem item = (SQLPatternItem) repositoryObject.getProperty().getItem();
                if (item.getEltName().equals(db)) {
                    patterns.add(item.getProperty().getLabel());
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return patterns.toArray(new String[0]);
    }

    // private final IPreferenceStore store =
    // CorePlugin.getDefault().getComponentsLocalProviderService().getPreferenceStore();

    /**
     * yzhang Comment method "getNodeFormatId".
     *
     * @param nodeLabel
     * @param nodeFamily
     * @return
     */
    private String getNodeFormatIdWithoutFormatType(String nodeLabel, String nodeFamily) {
        String ids = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsLocalProviderService.class)) {
            IComponentsLocalProviderService service = GlobalServiceRegister.getDefault()
                    .getService(IComponentsLocalProviderService.class);
            if (service != null) {
                ids = service.getPreferenceStore().getString(IComponentsLocalProviderService.FORMAT_IDS);
            }
        }
        String[] idArray = null;
        if (!StringUtils.isBlank(ids)) {
            idArray = ids.split(IComponentsLocalProviderService.IDS_SEPARATOR);

            String label = nodeLabel + IComponentsLocalProviderService.PALETTE_ENTRY_TYPE;
            for (String id : idArray) {
                if (id.contains(label)) {
                    return nodeLabel + IComponentsLocalProviderService.PALETTE_ENTRY_TYPE;
                }
            }

            label = nodeFamily + IComponentsLocalProviderService.PALETTE_CONTAINER_TYPE;
            for (String id : idArray) {
                if (id.contains(label)) {
                    return nodeFamily + IComponentsLocalProviderService.PALETTE_CONTAINER_TYPE;
                }
            }

            label = IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL;
            for (String id : idArray) {
                if (id.contains(label)) {
                    return IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL;
                }
            }

            label = IComponentsLocalProviderService.PREFERENCE_TYPE_HINT;
            for (String id : idArray) {
                if (id.contains(label)) {
                    return IComponentsLocalProviderService.PREFERENCE_TYPE_HINT;
                }
            }

            label = IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION;
            for (String id : idArray) {
                if (id.contains(label)) {
                    return IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION;
                }
            }

            if (nodeFamily.contains("/")) { //$NON-NLS-1$
                String rootFamily = nodeFamily.split("/")[0]; //$NON-NLS-1$
                label = rootFamily + IComponentsLocalProviderService.PALETTE_CONTAINER_TYPE;
                for (String id : idArray) {
                    if (id.contains(label)) {
                        return rootFamily + IComponentsLocalProviderService.PALETTE_CONTAINER_TYPE;
                    }
                }
            }

        }

        return null;
    }

    public void addViewParameters(final List<ElementParameter> listParam, INode node) {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        ElementParameter param;
        IPreferenceStore localComponentProviderStore = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsLocalProviderService.class)) {
            IComponentsLocalProviderService service = GlobalServiceRegister.getDefault()
                    .getService(IComponentsLocalProviderService.class);
            if (service != null) {
                localComponentProviderStore = service.getPreferenceStore();
            }
        }
        FORMATType formatTypeInXML = compType.getHEADER().getFORMAT();

        // qli modified to fix the bug 7074.
        String formatId = getNodeFormatIdWithoutFormatType(getName(), getTranslatedFamilyName());

        param = new ElementParameter(node);
        param.setName(EParameterName.LABEL.getName());
        param.setDisplayName(EParameterName.LABEL.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.VIEW);
        param.setNumRow(1);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        if (formatTypeInXML != null) {
            param.setValue(formatTypeInXML.getLABEL());
        } else if (formatId != null) {
            if (localComponentProviderStore != null) {
                String label = localComponentProviderStore.getString(IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL);
                if (!"".equals(label)) { //$NON-NLS-1$
                    param.setValue(label);
                }
            }
        } else {
            // in case label/format is not set in the preferences.
            String label = DesignerPlugin.getDefault().getPreferenceStore().getString(TalendDesignerPrefConstants.DEFAULT_LABEL);
            if (!"".equals(label)) { //$NON-NLS-1$
                param.setValue(label);
            }
        }
        param.setDefaultValue(param.getValue());

        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.HINT.getName());
        param.setDisplayName(EParameterName.HINT.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.VIEW);
        param.setNumRow(2);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        if (formatTypeInXML != null) {
            param.setValue(formatTypeInXML.getHINT());
        } else if (formatId != null) {
            if (localComponentProviderStore != null) {
                String label = localComponentProviderStore.getString(IComponentsLocalProviderService.PREFERENCE_TYPE_HINT);
                if (!"".equals(label)) { //$NON-NLS-1$
                    param.setValue(label);
                }
            }
        } else {
            // in case hint/format is not set in the preferences.
            String label = DesignerPlugin.getDefault().getPreferenceStore().getString(TalendDesignerPrefConstants.DEFAULT_HINT);
            if (!"".equals(label)) { //$NON-NLS-1$
                param.setValue(label);
            }
        }
        param.setDefaultValue(param.getValue());

        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.CONNECTION_FORMAT.getName());
        param.setDisplayName(EParameterName.CONNECTION_FORMAT.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.VIEW);
        param.setNumRow(3);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        if (formatTypeInXML != null) {
            param.setValue(formatTypeInXML.getCONNECTION());
        } else if (formatId != null) {
            if (localComponentProviderStore != null) {
                String label = localComponentProviderStore.getString(IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION);
                if (!"".equals(label)) { //$NON-NLS-1$
                    param.setValue(label);
                }
            }
        }
        param.setDefaultValue(param.getValue());
        listParam.add(param);
    }

    //rename for route break points
    public void addRouteExpressionParameters(final List<ElementParameter> listParam, INode node) {
        // fix for headless issue
        if (!ComponentCategory.CATEGORY_4_CAMEL.getName().equals(getPaletteType()) || !PluginChecker.isRouteletLoaded()) {
            return;
        }

        ElementParameter param = new ElementParameter(node);
        param.setName(EParameterName.ACTIVEBREAKPOINT.getName());
        param.setDisplayName(EParameterName.ACTIVEBREAKPOINT.getDisplayName());
        param.setFieldType(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.BREAKPOINT_CAMEL);
        param.setNumRow(13);
        param.setValue(false);
        param.setContextMode(false);
        param.setDefaultValue(param.getValue());
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.ROUTE_BREAKPOINT_LANGUAGES.getName());
        param.setDisplayName(EParameterName.ROUTE_BREAKPOINT_LANGUAGES.getDisplayName());
        param.setFieldType(EParameterFieldType.CLOSED_LIST);
        param.setCategory(EComponentCategory.BREAKPOINT_CAMEL);
        param.setNumRow(14);

        param.setListItemsValue(CAMEL_LANGUAGE_ITEM_VALUES);
        param.setListItemsDisplayCodeName(CAMEL_LANGUAGE_DISPLAY_CODE_NAMES);
        param.setListItemsDisplayName(CAMEL_LANGUAGE_DISPLAY_NAMES);
        param.setListRepositoryItems(new String[CAMEL_LANGUAGE_ITEM_VALUES.length]);
        param.setListItemsShowIf(new String[CAMEL_LANGUAGE_ITEM_VALUES.length]);
        param.setListItemsNotShowIf(new String[CAMEL_LANGUAGE_ITEM_VALUES.length]);
        param.setListItemsReadOnlyIf(new String[CAMEL_LANGUAGE_ITEM_VALUES.length]);
        param.setListItemsNotReadOnlyIf(new String[CAMEL_LANGUAGE_ITEM_VALUES.length]);
        param.setValue(SIMPLE);
        param.setContextMode(false);
        param.setDefaultValue(SIMPLE);
        listParam.add(param);


        param = new ElementParameter(node);
        param.setName(EParameterName.ROUTE_BREAKPOINT_EXPRESSION.getName());
        param.setDisplayName(EParameterName.ROUTE_BREAKPOINT_EXPRESSION.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.BREAKPOINT_CAMEL);
        param.setNumRow(15);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setValue("\"\""); //$NON-NLS-1$
        param.setDefaultValue(param.getValue());
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.ROUTE_BREAKPOINT_EXAMPLE.getName());
        param.setDisplayName(EParameterName.ROUTE_BREAKPOINT_EXAMPLE.getDisplayName());
        param.setValue("Example: Lauguages=Simple, Expression=\"${body} contains 'Camel'\""); //$NON-NLS-1$
        param.setFieldType(EParameterFieldType.LABEL);
        param.setCategory(EComponentCategory.BREAKPOINT_CAMEL);
        param.setNumRow(16);
        listParam.add(param);

    }

    public void addMainParameters(final List<ElementParameter> listParam, INode node) {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        ElementParameter param;
        param = new ElementParameter(node);
        param.setName(EParameterName.UNIQUE_NAME.getName());
        param.setValue(""); //$NON-NLS-1$
        param.setDisplayName(EParameterName.UNIQUE_NAME.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.COMPONENT_NAME.getName());
        param.setValue(getName());
        param.setDisplayName(EParameterName.COMPONENT_NAME.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.VERSION.getName());
        param.setValue(compType.getHEADER().getVERSION() + " (" + compType.getHEADER().getSTATUS() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        //$NON-NLS-1$ //$NON-NLS-2$
        param.setDisplayName(EParameterName.VERSION.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(2);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.FAMILY.getName());
        param.setValue(getOriginalFamilyName());
        param.setDisplayName(EParameterName.FAMILY.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(3);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        if (canStart()) {
            param = new ElementParameter(node);
            param.setName(EParameterName.START.getName());
            param.setValue(new Boolean(false));
            param.setDisplayName(EParameterName.START.getDisplayName());
            param.setFieldType(EParameterFieldType.CHECK);
            param.setCategory(EComponentCategory.TECHNICAL);
            param.setNumRow(5);
            param.setReadOnly(true);
            param.setRequired(false);
            param.setShow(false);
            listParam.add(param);
        }

        param = new ElementParameter(node);
        param.setName(EParameterName.STARTABLE.getName());
        param.setValue(new Boolean(canStart()));
        param.setDisplayName(EParameterName.STARTABLE.getDisplayName());
        param.setFieldType(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(5);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.SUBTREE_START.getName());
        param.setValue(new Boolean(canStart()));
        param.setDisplayName(EParameterName.SUBTREE_START.getDisplayName());
        param.setFieldType(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(5);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.END_OF_FLOW.getName());
        param.setValue(new Boolean(canStart()));
        param.setDisplayName(EParameterName.END_OF_FLOW.getDisplayName());
        param.setFieldType(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(5);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.ACTIVATE.getName());
        param.setValue(new Boolean(true));
        param.setDisplayName(EParameterName.ACTIVATE.getDisplayName());
        param.setFieldType(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(5);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setDefaultValue(param.getValue());
        param.setShow(true);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.DUMMY.getName());
        param.setValue(Boolean.FALSE);
        param.setDefaultValue(Boolean.FALSE);
        param.setDisplayName(EParameterName.DUMMY.getDisplayName());
        param.setFieldType(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(5);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        if (ComponentCategory.CATEGORY_4_DI.getName().equals(this.getPaletteType())) {
            boolean isStatCatcherComponent = false;
            if (this.name != null && this.name.equals(TSTATCATCHER_NAME)) {
                isStatCatcherComponent = true;
            }
            /* for bug 0021961,should not show parameter TSTATCATCHER_STATS in UI on component tStatCatcher */
            if (!isStatCatcherComponent) {
                boolean tStatCatcherAvailable = ComponentsFactoryProvider.getInstance().get(TSTATCATCHER_NAME,
                        ComponentCategory.CATEGORY_4_DI.getName()) != null;
                param = new ElementParameter(node);
                param.setName(EParameterName.TSTATCATCHER_STATS.getName());
                param.setValue(new Boolean(compType.getHEADER().isTSTATCATCHERSTATS()));
                param.setDisplayName(EParameterName.TSTATCATCHER_STATS.getDisplayName());
                param.setFieldType(EParameterFieldType.CHECK);
                param.setCategory(EComponentCategory.ADVANCED);
                param.setNumRow(199);
                param.setReadOnly(false);
                param.setRequired(false);
                param.setDefaultValue(param.getValue());
                param.setShow(tStatCatcherAvailable);
                listParam.add(param);
            }
        }

        param = new ElementParameter(node);
        param.setName(EParameterName.HELP.getName());
        param.setValue(getTranslatedValue(PROP_HELP));
        param.setDisplayName(EParameterName.HELP.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(6);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.UPDATE_COMPONENTS.getName());
        param.setValue(new Boolean(false));
        param.setDisplayName(EParameterName.UPDATE_COMPONENTS.getDisplayName());
        param.setFieldType(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(5);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);
        //
        param = new ElementParameter(node);
        param.setName(EParameterName.IREPORT_PATH.getName());
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setFieldType(EParameterFieldType.DIRECTORY);
        param.setDisplayName(EParameterName.IREPORT_PATH.getDisplayName());
        param.setNumRow(99);
        param.setShow(false);
        // "cmd /c cd \"C:\Program Files\JasperSoft\iReport-2.0.3\" && iReport.exe"
        param.setValue(CorePlugin.getDefault().getPluginPreferences().getString(ITalendCorePrefConstants.IREPORT_PATH));
        param.setReadOnly(true);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.JAVA_LIBRARY_PATH.getName());
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setFieldType(EParameterFieldType.DIRECTORY);
        param.setDisplayName(EParameterName.JAVA_LIBRARY_PATH.getDisplayName());
        param.setNumRow(99);
        param.setShow(false);
        // param.setValue(CorePlugin.getDefault().getLibrariesService().getJavaLibrariesPath());
        param.setValue(LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA));
        param.setReadOnly(true);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.SUBJOB_COLOR.getName());
        param.setValue(compType.getHEADER().getSUBJOBCOLOR());
        param.setDisplayName(EParameterName.SUBJOB_COLOR.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(99);
        param.setReadOnly(true);
        param.setShow(false);
        param.setDefaultValue(param.getValue());
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.SUBJOB_TITLE_COLOR.getName());
        param.setValue(compType.getHEADER().getSUBJOBTITLECOLOR());
        param.setDisplayName(EParameterName.SUBJOB_TITLE_COLOR.getDisplayName());
        param.setFieldType(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(99);
        param.setReadOnly(true);
        param.setShow(false);
        param.setDefaultValue(param.getValue());
        listParam.add(param);

        // These parameters is only work when TIS is loaded
        // GLiu Added for Task http://jira.talendforge.org/browse/TESB-4279
        if (PluginChecker.isTeamEdition() && !ComponentCategory.CATEGORY_4_CAMEL.getName().equals(getPaletteType())) {
            boolean defaultParalelize = new Boolean(compType.getHEADER().isPARALLELIZE());
            param = new ElementParameter(node);
            param.setReadOnly(!defaultParalelize);
            param.setName(EParameterName.PARALLELIZE.getName());
            param.setValue(Boolean.FALSE);
            param.setDisplayName(EParameterName.PARALLELIZE.getDisplayName());
            param.setFieldType(EParameterFieldType.CHECK);
            param.setCategory(EComponentCategory.ADVANCED);
            param.setNumRow(200);
            param.setShow(defaultParalelize);
            param.setDefaultValue(param.getValue());
            listParam.add(param);

            param = new ElementParameter(node);
            param.setReadOnly(!defaultParalelize);
            param.setName(EParameterName.PARALLELIZE_NUMBER.getName());
            param.setValue(compType.getHEADER().getNUMBERPARALLELIZE());
            param.setDisplayName(EParameterName.PARALLELIZE_NUMBER.getDisplayName());
            param.setFieldType(EParameterFieldType.TEXT);
            param.setCategory(EComponentCategory.ADVANCED);
            param.setNumRow(200);
            param.setShowIf(EParameterName.PARALLELIZE.getName() + " == 'true'"); //$NON-NLS-1$
            param.setDefaultValue(param.getValue());
            listParam.add(param);

            param = new ElementParameter(node);
            param.setReadOnly(!defaultParalelize);
            param.setName(EParameterName.PARALLELIZE_KEEP_EMPTY.getName());
            param.setValue(Boolean.FALSE);
            param.setDisplayName(EParameterName.PARALLELIZE_KEEP_EMPTY.getDisplayName());
            param.setFieldType(EParameterFieldType.CHECK);
            param.setCategory(EComponentCategory.ADVANCED);
            param.setNumRow(200);
            param.setShow(false);
            param.setDefaultValue(param.getValue());
            listParam.add(param);
        }

    }

    private void createSpecificParametersFromType(final List<ElementParameter> listParam, final PARAMETERType xmlParam,
            final INode node, final EParameterFieldType type, ElementParameter parentParam) {
        if (type == EParameterFieldType.PROPERTY_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.PROPERTY_TYPE.getName());
            String displayName = getTranslatedValue(xmlParam.getNAME() + '.' + PROP_NAME);
            if (displayName.startsWith("!!")) { //$NON-NLS-1$
                displayName = EParameterName.PROPERTY_TYPE.getDisplayName();
            }
            newParam.setDisplayName(displayName);
            if (node.getComponent() != null && node.getComponent().getName().equals("tOracleConnection")) { //$NON-NLS-1$
                newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY, TEXT_TNS_FILE });
                newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY, TNS_FILE });
                newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY, TNS_FILE });
            } else {
                newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
                newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY });
                newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
            }

            newParam.setValue(BUILTIN);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            newParam.setRepositoryValue(xmlParam.getREPOSITORYVALUE());
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            newParam.setParentParameter(parentParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
            newParam.setDisplayName(EParameterName.REPOSITORY_PROPERTY_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setShow(false);
            newParam.setRequired(true);
            newParam.setParentParameter(parentParam);
        } else if (type == EParameterFieldType.SCHEMA_TYPE) {
            String context = xmlParam.getCONTEXT();
            if (context == null) {
                // by default the schema will be set to the "FLOW" connector.
                context = EConnectionType.FLOW_MAIN.getName();
                if (getOriginalFamilyName().startsWith("ELT")) { //$NON-NLS-1$
                    context = EConnectionType.TABLE.getName();
                }
            }

            boolean useInputLinkSelection = connectorUseInputLinkSelection(context);

            String displayName = getTranslatedValue(xmlParam.getNAME() + '.' + PROP_NAME);
            if (displayName.startsWith("!!")) { //$NON-NLS-1$
                displayName = EParameterName.SCHEMA_TYPE.getDisplayName();
            }

            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.SCHEMA_TYPE.getName());
            newParam.setDisplayName(displayName);
            newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
            newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY });
            newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
            newParam.setValue(BUILTIN);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            newParam.setShow(xmlParam.isSHOW());
            newParam.setShowIf(parentParam.getName() + " =='" + REPOSITORY + "'"); //$NON-NLS-1$ //$NON-NLS-2$
            newParam.setReadOnly(xmlParam.isREADONLY() || useInputLinkSelection);
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());

            newParam.setContext(context);
            newParam.setParentParameter(parentParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
            newParam.setDisplayName(EParameterName.REPOSITORY_SCHEMA_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setShow(false);
            newParam.setRequired(true);
            newParam.setReadOnly(xmlParam.isREADONLY() || useInputLinkSelection);
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            newParam.setContext(context);
            newParam.setParentParameter(parentParam);

            if (useInputLinkSelection) {
                newParam = new ElementParameter(node);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setName(EParameterName.CONNECTION.getName());
                newParam.setDisplayName(Messages.getString("EmfComponent.attachedConnection")); //$NON-NLS-1$
                newParam.setListItemsDisplayName(new String[] {});
                newParam.setListItemsValue(new String[] {});
                newParam.setNumRow(xmlParam.getNUMROW());
                newParam.setFieldType(EParameterFieldType.CONNECTION_LIST);
                newParam.setValue(""); //$NON-NLS-1$
                newParam.setShow(true);
                newParam.setRequired(true);
                newParam.setFilter("INPUT:FLOW_MAIN|FLOW_REF|FLOW_MERGE"); //$NON-NLS-1$
                newParam.setReadOnly(xmlParam.isREADONLY());
                newParam.setShowIf(xmlParam.getSHOWIF());
                newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
                newParam.setContext(context);
                newParam.setParentParameter(parentParam);
                parentParam.setReadOnly(true);
            }
        } else if (type == EParameterFieldType.ENCODING_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.ENCODING_TYPE.getName());
            newParam.setDisplayName(EParameterName.ENCODING_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] { ENCODING_TYPE_ISO_8859_15, ENCODING_TYPE_UTF_8, ENCODING_TYPE_CUSTOM });
            newParam.setListItemsDisplayCodeName(new String[] { ENCODING_TYPE_ISO_8859_15, ENCODING_TYPE_UTF_8,
                    ENCODING_TYPE_CUSTOM });
            newParam.setListItemsValue(new String[] { ENCODING_TYPE_ISO_8859_15, ENCODING_TYPE_UTF_8, ENCODING_TYPE_CUSTOM });
            newParam.setValue(ENCODING_TYPE_ISO_8859_15);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            newParam.setShow(true);
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            newParam.setParentParameter(parentParam);
        }// Ends
        else if (type == EParameterFieldType.QUERYSTORE_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.QUERYSTORE_TYPE.getName());
            newParam.setDisplayName(EParameterName.QUERYSTORE_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
            newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY });
            newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
            newParam.setValue(BUILTIN);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            newParam.setParentParameter(parentParam);
            // listParam.add(newParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName());
            newParam.setDisplayName(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setShow(false);
            newParam.setRequired(true);
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            newParam.setParentParameter(parentParam);
            // listParam.add(newParam);
        } else if (type == EParameterFieldType.PROCESS_TYPE || type == EParameterFieldType.ROUTE_INPUT_PROCESS_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.PROCESS_TYPE_PROCESS.getName());
            if (getTranslatedValue(xmlParam.getNAME() + '.' + PROP_NAME).startsWith("!!")) { //$NON-NLS-1$
                newParam.setDisplayName(EParameterName.PROCESS_TYPE_PROCESS.getDisplayName());
            } else {
                newParam.setDisplayName(getTranslatedValue(xmlParam.getNAME() + '.' + PROP_NAME));
            }
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setRequired(true);
            newParam.setParentParameter(parentParam);
            // listParam.add(newParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.PROCESS_TYPE_VERSION.getName());
            newParam.setDisplayName(EParameterName.PROCESS_TYPE_VERSION.getDisplayName());
            newParam.setListItemsDisplayName(new String[] { ItemCacheManager.LATEST_VERSION });
            newParam.setListItemsValue(new String[] { ItemCacheManager.LATEST_VERSION });
            newParam.setValue(ItemCacheManager.LATEST_VERSION);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setRequired(true);
            newParam.setParentParameter(parentParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.PROCESS_TYPE_CONTEXT.getName());
            newParam.setDisplayName(EParameterName.PROCESS_TYPE_CONTEXT.getDisplayName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            newParam.setValue(""); //$NON-NLS-1$
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setRequired(true);
            newParam.setParentParameter(parentParam);
        }
        // http://jira.talendforge.org/browse/TESB-6285 Create a new Component Field ROUTE_RESOURCE for Route components
        else if (type == EParameterFieldType.ROUTE_RESOURCE_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.ROUTE_RESOURCE_TYPE_VERSION.getName());
            newParam.setDisplayName(EParameterName.ROUTE_RESOURCE_TYPE_VERSION.getDisplayName());
            newParam.setListItemsDisplayName(new String[] { ItemCacheManager.LATEST_VERSION });
            newParam.setListItemsValue(new String[] { ItemCacheManager.LATEST_VERSION });
            newParam.setValue(ItemCacheManager.LATEST_VERSION);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setRequired(false);
            newParam.setParentParameter(parentParam);
        } else if (type == EParameterFieldType.HADOOP_LIBRARIES) {
            String cacheVersion = HadoopDistributionsHelper.getCacheVersion();
            if (!StringUtils.equals(cacheVersion, hadoopLibCacheVersion)) {
                areHadoopLibsLoaded = false;
                // We get the component type defined by the NAME of the HADOOP_LIBRARIES parameter.
                ComponentType componentType = ComponentType.getComponentType(parentParam.getName());
                hadoopLibParamName = componentType;

                for (List<ModuleNeeded> cachedList : hadoopLibModuleNeededMap.values()) {
                    if (cachedList != null) {
                        componentImportNeedsList.removeAll(cachedList);
                    }
                }

                // We retrieve all the implementations of the HadoopComponent service.
                BundleContext bc = FrameworkUtil.getBundle(DistributionFactory.class).getBundleContext();
                Collection<ServiceReference<? extends HadoopComponent>> distributions = new LinkedList<>();
                try {
                    Class<? extends HadoopComponent> clazz = (Class<? extends HadoopComponent>) Class.forName(componentType
                            .getService());
                    distributions.addAll(bc.getServiceReferences(clazz, null));
                } catch (InvalidSyntaxException | ClassNotFoundException e) {
                    CommonExceptionHandler.process(e);
                }
                for (ServiceReference<? extends HadoopComponent> sr : distributions) {
                    HadoopComponent hc = bc.getService(sr);
                    hadoopLibModuleNeededMap.put(hc, null);
                }
                hadoopLibCacheVersion = cacheVersion;
                areHadoopLibsLoaded = true;
            }
        } else if (type == EParameterFieldType.HADOOP_DISTRIBUTION) {
            ComponentType componentType = ComponentType.getComponentType(parentParam.getName());
            DistributionsManager distributionsHelper = new DistributionsManager(componentType);
            final DistributionBean[] hadoopDistributions = distributionsHelper.getDistributions();

            // Distribution parameter
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            hadoopDistributionParamName = componentType;
            newParam.setName(componentType.getDistributionParameter());
            newParam.setDisplayName(SparkBatchConstant.getName(componentType.getDistributionParameter()));

            String[] displayName = new String[hadoopDistributions.length];
            String[] itemValue = new String[hadoopDistributions.length];
            String[] showIfVersion = new String[hadoopDistributions.length];
            String[] notShowIfVersion = new String[hadoopDistributions.length];

            List<DistributionVersion> versionsList = new ArrayList<>();

            Map<ESparkVersion, Set<DistributionVersion>> supportedSparkVersions = new HashMap<>();

            for (int i = 0; i < hadoopDistributions.length; i++) {
                DistributionBean that = hadoopDistributions[i];
                itemValue[i] = that.name;
                displayName[i] = that.displayName;
                showIfVersion[i] = that.getDisplayShowIf();
                notShowIfVersion[i] = null;
                if (!that.useCustom()) { // ignore custom version, because it's fake one
                    versionsList.addAll(Arrays.asList(that.getVersions()));
                    if (ComponentType.isSparkComponent(componentType)) {
                        for (DistributionVersion version : that.getVersions()) {
                            try {
                                // Here, we build a map of Spark versions with the corresponding Hadoop versions that
                                // support these Spark versions.
                                SparkComponent sparkComponent = (SparkComponent) DistributionFactory.buildDistribution(that.name,
                                        version.version);

                                for (ESparkVersion v : sparkComponent.getSparkVersions()) {
                                    Set<DistributionVersion> versionsPerSparkVersion = supportedSparkVersions.getOrDefault(v,
                                            new HashSet<>());
                                    versionsPerSparkVersion.add(version);
                                    supportedSparkVersions.put(v, versionsPerSparkVersion);
                                }
                            } catch (Exception e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    }
                }
            }

            String defaultValue = itemValue.length > 0 ? itemValue[0] : "";//$NON-NLS-1$

            newParam.setListItemsDisplayName(displayName);
            newParam.setListItemsDisplayCodeName(displayName);
            newParam.setListItemsValue(itemValue);
            newParam.setListItemsShowIf(showIfVersion);
            newParam.setListItemsNotShowIf(notShowIfVersion);
            newParam.setValue(defaultValue);
            newParam.setDefaultValue(defaultValue);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.CLOSED_LIST);
            newParam.setShow(true);
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            newParam.setGroup(xmlParam.getGROUP());
            newParam.setGroupDisplayName(parentParam.getGroupDisplayName());
            newParam.setRepositoryValue(componentType.getDistributionRepositoryValueParameter());

            listParam.add(newParam);

            boolean cacheVersionChanged = false;
            String cacheVersion = HadoopDistributionsHelper.getCacheVersion();
            cacheVersionChanged = !StringUtils.equals(cacheVersion, hadoopDistribsCacheVersion);

            if (cacheVersionChanged) {
                for (List<ModuleNeeded> cachedList : distributionModuleNeededMap.values()) {
                    if (cachedList != null) {
                        componentImportNeedsList.removeAll(cachedList);
                    }
                }
                distributionModuleNeededMap.clear();
                areHadoopDistribsLoaded = false;
            }

            displayName = new String[versionsList.size()];
            itemValue = new String[versionsList.size()];
            showIfVersion = new String[versionsList.size()];
            notShowIfVersion = new String[versionsList.size()];

            int index = 0;

            Iterator<DistributionVersion> versionIter = versionsList.iterator();
            while (versionIter.hasNext()) {
                DistributionVersion that = versionIter.next();
                itemValue[index] = that.version;
                displayName[index] = that.displayVersion;

                showIfVersion[index] = that.getDisplayShowIf();
                notShowIfVersion[index] = null;

                if (cacheVersionChanged) {
                    distributionModuleNeededMap.put(that, null);
                }

                index++;
            }

            if (cacheVersionChanged) {
                hadoopDistribsCacheVersion = cacheVersion;
            }
            areHadoopDistribsLoaded = true;

            defaultValue = itemValue.length > 0 ? itemValue[0] : defaultValue;

            // Distribution version
            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(componentType.getVersionParameter());
            newParam.setDisplayName(SparkBatchConstant.getName(componentType.getVersionParameter()));
            newParam.setListItemsDisplayName(displayName);
            newParam.setListItemsDisplayCodeName(displayName);
            newParam.setListItemsValue(itemValue);
            newParam.setListItemsShowIf(showIfVersion);
            newParam.setListItemsNotShowIf(notShowIfVersion);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.CLOSED_LIST);
            newParam.setShow(true);
            String showIf = xmlParam.getSHOWIF();
            Optional<ElementParameter> componentName = listParam.stream().filter(x -> "COMPONENT_NAME".equals(x.getName())).findFirst();
            if (componentName.isPresent() && "tSparkConfiguration".equals(componentName.get().getValue())) {
                if (showIf != null) {
                    newParam.setShowIf(showIf + " AND (" + componentType.getDistributionParameter() + "!='CUSTOM')"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    newParam.setShowIf("(" + componentType.getDistributionParameter() + "!='CUSTOM')"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            } else {
                if (showIf != null) {
                    newParam.setShowIf(showIf + " AND (" + componentType.getDistributionParameter() + "!='CUSTOM')"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    newParam.setShowIf("(" + componentType.getDistributionParameter() + "!='CUSTOM')"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }

            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            newParam.setGroup(xmlParam.getGROUP());
            newParam.setGroupDisplayName(parentParam.getGroupDisplayName());
            newParam.setRepositoryValue(componentType.getVersionRepositoryValueParameter());
            for (DistributionBean b : hadoopDistributions) {
                IElementParameterDefaultValue defaultType = new ElementParameterDefaultValue();
                final DistributionVersion defaultVersion = "SPARK".equals(b.name) ? b.getVersion("SPARK_3_2_x", false) : b.getDefaultVersion();
                if (defaultVersion == null || defaultVersion.version == null || defaultVersion.version.isEmpty()) {
                    defaultType.setDefaultValue(defaultValue);
                } else {
                    defaultType.setDefaultValue(defaultVersion.version);
                }
                defaultType.setIfCondition(new SimpleComponentCondition(new BasicExpression(componentType
                        .getDistributionParameter(), EqualityOperator.EQ, b.name)).getConditionString());
                newParam.getDefaultValues().add(defaultType);
            }

            listParam.add(newParam);

            // If the ComponentType is SparkBatch or SparkStreaming, then we are in a tSparkConfiguration case and
            // need to display an additional SparkVersion list.
            if (ComponentType.isSparkComponent(componentType)) {

                newParam = new ElementParameter(node);
                String[] showIfSparkVersion = ComponentConditionUtil.generateSparkVersionShowIfConditions(supportedSparkVersions);

                int supportedSparkVersionsCount = supportedSparkVersions.keySet().size();
                String[] sparkVersionValues = new String[supportedSparkVersionsCount];
                String[] sparkVersionLabels = new String[supportedSparkVersionsCount];
                index = 0;
                for (ESparkVersion sv : supportedSparkVersions.keySet()) {
                    sparkVersionValues[index] = sv.getSparkVersion();
                    sparkVersionLabels[index] = sv.getVersionLabel();
                    IElementParameterDefaultValue defaultType = new ElementParameterDefaultValue();
                    defaultType.setDefaultValue(sv.getSparkVersion());
                    defaultType.setIfCondition(showIfSparkVersion[index]);
                    newParam.getDefaultValues().add(defaultType);
                    index++;
                }

                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setName(SparkBatchConstant.SUPPORTED_SPARK_VERSION_PARAMETER);
                newParam.setDisplayName(SparkBatchConstant.getName(SparkBatchConstant.SUPPORTED_SPARK_VERSION_PARAMETER));
                newParam.setListItemsDisplayName(sparkVersionLabels);
                newParam.setListItemsDisplayCodeName(sparkVersionLabels);
                newParam.setListItemsValue(sparkVersionValues);
                newParam.setListItemsShowIf(showIfSparkVersion);
                newParam.setListItemsNotShowIf(new String[supportedSparkVersionsCount]);
                newParam.setNumRow(xmlParam.getNUMROW());
                newParam.setFieldType(EParameterFieldType.CLOSED_LIST);
                newParam.setShow(true);
                newParam.setGroup(xmlParam.getGROUP());
                newParam.setGroupDisplayName(parentParam.getGroupDisplayName());
                showIf = xmlParam.getSHOWIF();
                if (showIf != null) {
                    newParam.setShowIf(showIf + " AND (" + componentType.getDistributionParameter() + "!='CUSTOM')" + " AND (" + componentType.getDistributionParameter() + "!='SPARK')"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    newParam.setShowIf("(" + componentType.getDistributionParameter() + "!='CUSTOM')" + " AND (" + componentType.getDistributionParameter() + "!='SPARK')"); //$NON-NLS-1$ //$NON-NLS-2$
                }
                newParam.setNotShowIf(xmlParam.getNOTSHOWIF());

                listParam.add(newParam);

                // Spark mode
                String[] sparkModeLabels = Arrays.asList(ESparkMode.values()).stream().map(ESparkMode::getLabel)
                        .collect(Collectors.toList()).toArray(new String[0]);
                String[] sparkModeValues = Arrays.asList(ESparkMode.values()).stream().map(ESparkMode::getValue)
                        .collect(Collectors.toList()).toArray(new String[0]);
                String[] sparkModeDisplayConditions = Arrays.asList(ESparkMode.values()).stream()
                        .map(ESparkMode::getDisplayCondition).collect(Collectors.toList()).toArray(new String[0]);
                List<IElementParameterDefaultValue> sparkModesDefaultValues = Arrays.asList(ESparkMode.values()).stream()
                        .map(m -> new ElementParameterDefaultValue(m.getValue(), m.getDisplayCondition()))
                        .collect(Collectors.toList());

                newParam = new ElementParameter(node);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setName(SparkBatchConstant.SPARK_MODE_PARAMETER);
                newParam.setDisplayName(SparkBatchConstant.getName(SparkBatchConstant.SPARK_MODE_PARAMETER));
                newParam.setListItemsDisplayName(sparkModeLabels);
                newParam.setListItemsDisplayCodeName(sparkModeLabels);
                newParam.setListItemsValue(sparkModeValues);
                newParam.setListItemsShowIf(sparkModeDisplayConditions);
                newParam.setListItemsNotShowIf(new String[ESparkMode.values().length]);
                newParam.setNumRow(xmlParam.getNUMROW() + 1);
                newParam.setFieldType(EParameterFieldType.CLOSED_LIST);
                newParam.setShow(true);
                newParam.setGroup(xmlParam.getGROUP());
                newParam.setGroupDisplayName(parentParam.getGroupDisplayName());
                newParam.setDefaultValues(sparkModesDefaultValues);
                newParam.setRepositoryValue(SparkBatchConstant.SPARK_MODE_PARAMETER);
                listParam.add(newParam);
                
                // databricksRuntimeVersion for universal
                String[] databricksRuntimeVersionLabels = DatabricksRuntimeVersion.getAvailableRuntimeAndSparkVersion().stream().map(x -> x.getRuntimeVersion())
                        .collect(Collectors.toList()).toArray(new String[0]);
                String[] databricksRuntimeVersionDisplayConditions = DatabricksRuntimeVersion.getAvailableRuntimeAndSparkVersion().stream()
                		.map(x -> "(SPARK_VERSION=='"+x.getSparkVersion()+"')")
                        .collect(Collectors.toList()).toArray(new String[0]);

                newParam = new ElementParameter(node);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setName(SparkBatchConstant.DATABRICKS_RUNTIME_VERSION);
                newParam.setDisplayName(SparkBatchConstant.getName(SparkBatchConstant.DATABRICKS_RUNTIME_VERSION));
                newParam.setShowIf(DatabricksRuntimeVersion.showIfCondition);
                newParam.setListItemsDisplayName(databricksRuntimeVersionLabels);
                newParam.setListItemsDisplayCodeName(databricksRuntimeVersionLabels);
                newParam.setListItemsValue(databricksRuntimeVersionLabels);
                newParam.setListItemsShowIf(databricksRuntimeVersionDisplayConditions);
                newParam.setListItemsNotShowIf(new String[DatabricksRuntimeVersion.getAvailableRuntimeAndSparkVersion().size()]);
                newParam.setNumRow(xmlParam.getNUMROW() + 1);
                newParam.setFieldType(EParameterFieldType.OPENED_LIST);
                newParam.setRepositoryValue(SparkBatchConstant.DATABRICKS_RUNTIME_VERSION);
                newParam.setShow(true);
                newParam.setGroup(xmlParam.getGROUP());
                newParam.setDefaultValues(Arrays.asList(new ElementParameterDefaultValue("", "'true'=='true'")));
                listParam.add(newParam);
            }
        }
    }

    private ComponentType hadoopDistributionParamName = null;

    private Map<DistributionVersion, List<ModuleNeeded>> distributionModuleNeededMap = new HashMap<>();

    private ComponentType hadoopLibParamName = null;

    private Map<HadoopComponent, List<ModuleNeeded>> hadoopLibModuleNeededMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    private void addPropertyParameters(final List<ElementParameter> listParam, final INode node, boolean advanced) {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        EList listXmlParam;
        PARAMETERType xmlParam;
        ElementParameter param;

        boolean autoSwitchAdded = false;

        if (advanced) {
            if (compType.getADVANCEDPARAMETERS() == null) {
                return;
            }
            listXmlParam = compType.getADVANCEDPARAMETERS().getPARAMETER();

        } else {
            listXmlParam = compType.getPARAMETERS().getPARAMETER();
        }

        for (int i = 0; i < listXmlParam.size(); i++) {
            xmlParam = (PARAMETERType) listXmlParam.get(i);
            EParameterFieldType type = EParameterFieldType.getFieldTypeByName(xmlParam.getFIELD());

            if (type.equals(EParameterFieldType.SCHEMA_TYPE) || type.equals(EParameterFieldType.DCSCHEMA)) {
                if (!autoSwitchAdded) {
                    param = new ElementParameter(node);
                    param.setCategory(EComponentCategory.TECHNICAL);
                    param.setName(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName());
                    param.setDisplayName(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getDisplayName());
                    param.setNumRow(xmlParam.getNUMROW());
                    param.setFieldType(EParameterFieldType.CHECK);
                    param.setValue(Boolean.FALSE);
                    param.setShow(false);
                    param.setRequired(true);
                    param.setReadOnly(true);
                    listParam.add(param);
                    autoSwitchAdded = true;
                }
            }

            param = new ElementParameter(node);
            param.setName(xmlParam.getNAME());
            param.setDisplayName(getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME)); //$NON-NLS-1$
            if (xmlParam.getGROUP() != null) {
                param.setGroupDisplayName(getTranslatedValue(xmlParam.getGROUP() + "." + PROP_NAME));//$NON-NLS-1$
            }
            param.setFieldType(type);
            param.setNumRow(xmlParam.getNUMROW());
            if (xmlParam.isSetREADONLY()) {
                param.setReadOnly(xmlParam.isREADONLY());
            }
            if (xmlParam.isSetREQUIRED()) {
                param.setRequired(xmlParam.isREQUIRED());
            }
            if (xmlParam.isSetSHOW()) {
                param.setShow(xmlParam.isSHOW());
            }
            if (xmlParam.isSetNBLINES()) {
                param.setNbLines(xmlParam.getNBLINES());
            }
            if (xmlParam.isSetDYNAMICSETTINGS()) {
                param.setDynamicSettings(xmlParam.isDYNAMICSETTINGS());
            }

            param.setFilter(xmlParam.getFILTER());
            param.setShowIf(xmlParam.getSHOWIF());
            param.setRequiredIF(xmlParam.getREQUIREDIF());
            param.setNotShowIf(xmlParam.getNOTSHOWIF());
            param.setReadOnlyIf(xmlParam.getREADONLYIF());
            param.setNotReadOnlyIf(xmlParam.getNOTREADONLYIF());
            if (xmlParam.getREPOSITORYVALUE() != null) {
                if (xmlParam.getREPOSITORYVALUE().contains("/")) { //$NON-NLS-1$
                    String values[] = xmlParam.getREPOSITORYVALUE().split("/"); //$NON-NLS-1$
                    param.setRepositoryProperty(values[0]);
                    param.setRepositoryValue(values[1]);
                } else {
                    param.setRepositoryValue(xmlParam.getREPOSITORYVALUE());
                }
            }
            param.setGroup(xmlParam.getGROUP());
            param.setContext(xmlParam.getCONTEXT());
            param.setBackgroundColor(getColor(param, xmlParam.getBACKGROUND()));
            param.setColor(getColor(param, xmlParam.getCOLOR()));
            param.setContextMode(xmlParam.isCONTEXTMODE());
            param.setNoContextAssist(xmlParam.isNOCONTEXTASSIST());
            if (xmlParam.isSetMAXLENGTH()) {
                param.setMaxLength(xmlParam.getMAXLENGTH());
            }
            param.setRaw(xmlParam.isRAW());
            param.setLog4JEnabled(xmlParam.isLOG4J_ENABLED());
            switch (type) {
            case COLOR:
                param.setValue(DEFAULT_COLOR);
                break;
            case CHECK:
                param.setValue(new Boolean(false));
                break;
            case RADIO:
                param.setValue(new Boolean(false));
                break;
            case AS400_CHECK:
                param.setValue(new Boolean(false));
                break;
            case TABLE:
                param.setValue(new ArrayList<Map<String, Object>>());
                break;
            case TABLE_BY_ROW:
                param.setValue(new ArrayList<Map<String, Object>>());
                break;
            case SCHEMA_TYPE:
            case DCSCHEMA:
                if (param.getContext() == null) {
                    // by default the schema will be set to the "FLOW"
                    // connector.
                    param.setContext(EConnectionType.FLOW_MAIN.getName());
                    if (getOriginalFamilyName().startsWith("ELT")) { //$NON-NLS-1$
                        param.setContext(EConnectionType.TABLE.getName());
                    }
                }
                List<NodeConnector> list = createConnectors(node);
                boolean toShow = true;
                for (INodeConnector nodeConnector : list) {
                    if (nodeConnector.getName().equals(param.getContext())
                            && (!nodeConnector.getBaseSchema().equals(param.getContext()))) {
                        toShow = false;
                    }
                }
                param.setShow(xmlParam.isSHOW() && toShow);
                initializeTableFromXml(xmlParam, param);
                break;
            case DBTABLE:
                if (getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME).startsWith("!!")) { //$NON-NLS-1$ //$NON-NLS-2$
                    param.setDisplayName(EParameterName.DBTABLE.getDisplayName());
                }
                break;
            case MAPPING_TYPE:
                if (getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME).startsWith("!!")) { //$NON-NLS-1$ //$NON-NLS-2$
                    param.setDisplayName(EParameterName.MAPPING_TYPE.getDisplayName());
                }
                break;
            case PROCESS_TYPE:
                param.setRequired(false);
                if (getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME).startsWith("!!")) { //$NON-NLS-1$ //$NON-NLS-2$
                    param.setDisplayName(EParameterName.PROCESS_TYPE.getDisplayName());
                }
                param.setValue(""); // TODO to change ? //$NON-NLS-1$
                break;
            case PROPERTY_TYPE:
                param.setValue(""); //$NON-NLS-1$
                break;
            case ROUTE_RESOURCE_TYPE:
                param.setValue(""); //$NON-NLS-1$
                break;
            case JAVA_COMMAND:
                param.setValue(""); //$NON-NLS-1$
                if (xmlParam.getJAVACOMMAND() != null) {
                    param.setJar(xmlParam.getJAVACOMMAND().getJAR());
                    param.setJavaClass(xmlParam.getJAVACOMMAND().getCLASS());
                    param.setJavaFunction(xmlParam.getJAVACOMMAND().getFUNCTION());
                    List<String> args = new ArrayList<String>();
                    for (ARGType argType : (List<ARGType>) xmlParam.getJAVACOMMAND().getARG()) {
                        args.add(argType.getValue());
                    }
                    param.setArgs(args.toArray(new String[0]));
                }
            case TREE_TABLE:
                param.setValue(new ArrayList<Map<String, Object>>());
                break;
            default:
                param.setValue(""); //$NON-NLS-1$
            }

            if (!param.getFieldType().equals(EParameterFieldType.TABLE)
                    && !param.getFieldType().equals(EParameterFieldType.CLOSED_LIST)
                    && !param.getFieldType().equals(EParameterFieldType.SCHEMA_TYPE)
                    && !param.getFieldType().equals(EParameterFieldType.TREE_TABLE)) {
                List<DEFAULTType> listDefault = xmlParam.getDEFAULT();

                for (DEFAULTType defaultType : listDefault) {
                    IElementParameterDefaultValue defaultValue = new ElementParameterDefaultValue();

                    if (node.getProcess() != null) {
                        defaultValue.setDefaultValue(ElementParameterParser.parse(node.getProcess(), defaultType.getValue()));
                        if (param.getFieldType() == EParameterFieldType.FILE
                                || param.getFieldType() == EParameterFieldType.DIRECTORY) {
                            IPath path = Path.fromOSString(defaultValue.getDefaultValue().toString());
                            defaultValue.setDefaultValue(path.toPortableString());
                        }
                    } else {
                        defaultValue.setDefaultValue(defaultType.getValue());
                    }
                    defaultValue.setIfCondition(defaultType.getIF());
                    defaultValue.setNotIfCondition(defaultType.getNOTIF());
                    param.getDefaultValues().add(defaultValue);
                }
            }

            if (xmlParam.getITEMS() != null) {
                addItemsPropertyParameters(xmlParam.getNAME(), xmlParam.getITEMS(), param, type, node);
                // achen add one parameter BASED_ON_INPUT_SCHEMAS
                ITEMSType items = xmlParam.getITEMS();
                if (items.isSetBASEDONINPUTSCHEMAS()) {
                    ElementParameter newParam = new ElementParameter(node);
                    newParam.setName(EParameterName.BASED_ON_INPUT_SCHEMAS.getName());
                    newParam.setDisplayName(EParameterName.BASED_ON_INPUT_SCHEMAS.getDisplayName());
                    newParam.setFieldType(EParameterFieldType.TEXT);
                    newParam.setCategory(EComponentCategory.BASIC);
                    newParam.setShow(false);
                    newParam.setValue(String.valueOf(items.isBASEDONINPUTSCHEMAS()));
                    listParam.add(newParam);
                }
            }
            if (advanced) {
                param.setCategory(EComponentCategory.ADVANCED);
            } else {
                param.setCategory(EComponentCategory.BASIC);
            }

            createSpecificParametersFromType(listParam, xmlParam, node, type, param);
            listParam.add(param);
        }

        // TODO to remove later, need to find another way to do this (for feature 18686)
        // adds manually all definitions to avoid to modify the component
        if (ArrayUtils.contains(JavaTypesManager.getJavaTypesLabels(), "Geometry") && "tOracleInput".equals(name)) { //$NON-NLS-1$ //$NON-NLS-2$
            if (!advanced) {
                // <PARAMETER NAME="FORCE_CRS" FIELD="CHECK" REQUIRED="true"
                // NUM_ROW="110">
                // <DEFAULT>false</DEFAULT>
                // </PARAMETER>
                ElementParameter newParam = new ElementParameter(node);
                newParam.setName("FORCE_CRS"); //$NON-NLS-1$
                newParam.setDisplayName("Force coordinate reference system"); //$NON-NLS-1$
                newParam.setFieldType(EParameterFieldType.CHECK);
                newParam.setRequired(true);
                newParam.setNumRow(110);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue(new Boolean(false));
                listParam.add(newParam);

                // <PARAMETER NAME="CRS" FIELD="TEXT" NUM_ROW="110" REQUIRED="true"
                // SHOW_IF="FORCE_CRS == 'true'">
                // <DEFAULT>"EPSG:4326"</DEFAULT>
                // </PARAMETER>
                newParam = new ElementParameter(node);
                newParam.setName("CRS"); //$NON-NLS-1$
                newParam.setDisplayName("EPSG"); //$NON-NLS-1$
                newParam.setFieldType(EParameterFieldType.TEXT);
                newParam.setRequired(true);
                newParam.setNumRow(110);
                newParam.setShowIf("FORCE_CRS == 'true'"); //$NON-NLS-1$
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue("\"EPSG:4326\""); //$NON-NLS-1$
                listParam.add(newParam);

                // <PARAMETER NAME="IMPORT" FIELD="MEMO_IMPORT" REQUIRED="false" SHOW="false" NUM_ROW="2">
                // <DEFAULT>import org.talend.sdi.geometry.Geometry;</DEFAULT>
                // </PARAMETER>
                newParam = new ElementParameter(node);
                newParam.setName("IMPORT"); //$NON-NLS-1$
                newParam.setDisplayName("Imports"); //$NON-NLS-1$
                newParam.setFieldType(EParameterFieldType.MEMO_IMPORT);
                newParam.setRequired(true);
                newParam.setNumRow(2);
                newParam.setShow(false);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue("import org.talend.sdi.geometry.Geometry;"); //$NON-NLS-1$
                listParam.add(newParam);
            }
        }

        if (ArrayUtils.contains(JavaTypesManager.getJavaTypesLabels(), "Geometry") && "tOracleOutput".equals(name)) { //$NON-NLS-1$ //$NON-NLS-2$
            if (!advanced) {
                // <PARAMETER
                // NAME="USE_SPATIAL_OPTIONS"
                // FIELD="CHECK"
                // NUM_ROW="200"
                // SHOW_IF="(TABLE_ACTION=='CREATE') or (TABLE_ACTION=='DROP_CREATE') or
                // (TABLE_ACTION=='CREATE_IF_NOT_EXISTS') or (TABLE_ACTION=='DROP_IF_EXISTS_AND_CREATE')"
                // >
                // <DEFAULT>false</DEFAULT>
                // </PARAMETER>
                ElementParameter newParam = new ElementParameter(node);
                newParam.setName("USE_SPATIAL_OPTIONS"); //$NON-NLS-1$
                newParam.setDisplayName("Use spatial options"); //$NON-NLS-1$
                newParam.setFieldType(EParameterFieldType.CHECK);
                newParam.setRequired(true);
                newParam.setShowIf("(TABLE_ACTION=='CREATE') or (TABLE_ACTION=='DROP_CREATE') or" //$NON-NLS-1$
                        + " (TABLE_ACTION=='CREATE_IF_NOT_EXISTS') or (TABLE_ACTION=='DROP_IF_EXISTS_AND_CREATE')"); //$NON-NLS-1$
                newParam.setNumRow(200);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue(new Boolean(false));
                listParam.add(newParam);

                // <PARAMETER
                // NAME="SPATIAL_INDEX"
                // FIELD="CHECK"
                // SHOW_IF="(USE_SPATIAL_OPTIONS == 'true') and ((TABLE_ACTION=='CREATE') or
                // (TABLE_ACTION=='DROP_CREATE') or
                // (TABLE_ACTION=='CREATE_IF_NOT_EXISTS') or (TABLE_ACTION=='DROP_IF_EXISTS_AND_CREATE'))"
                // NUM_ROW="200"
                // >
                // <DEFAULT>false</DEFAULT>
                // </PARAMETER>
                newParam = new ElementParameter(node);
                newParam.setName("SPATIAL_INDEX"); //$NON-NLS-1$
                newParam.setDisplayName("Create Spatial index"); //$NON-NLS-1$
                newParam.setFieldType(EParameterFieldType.CHECK);
                newParam.setRequired(true);
                newParam.setShowIf("(USE_SPATIAL_OPTIONS == 'true') and ((TABLE_ACTION=='CREATE') or " //$NON-NLS-1$
                        + "(TABLE_ACTION=='DROP_CREATE') or (TABLE_ACTION=='CREATE_IF_NOT_EXISTS') or" //$NON-NLS-1$
                        + " (TABLE_ACTION=='DROP_IF_EXISTS_AND_CREATE'))"); //$NON-NLS-1$
                newParam.setNumRow(200);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue(new Boolean(false));
                listParam.add(newParam);

                // <PARAMETER
                // NAME="SPATIAL_INDEX_ACCURACY"
                // FIELD="TEXT"
                // REQUIRED="true"
                // SHOW_IF="(USE_SPATIAL_OPTIONS == 'true') and ((TABLE_ACTION=='CREATE') or
                // (TABLE_ACTION=='DROP_CREATE') or
                // (TABLE_ACTION=='CREATE_IF_NOT_EXISTS') or (TABLE_ACTION=='DROP_IF_EXISTS_AND_CREATE'))"
                // NUM_ROW="201"
                // >
                // <DEFAULT>0.001</DEFAULT>
                // </PARAMETER>
                newParam = new ElementParameter(node);
                newParam.setName("SPATIAL_INDEX_ACCURACY"); //$NON-NLS-1$
                newParam.setDisplayName("Index accuracy"); //$NON-NLS-1$
                newParam.setFieldType(EParameterFieldType.TEXT);
                newParam.setRequired(true);
                newParam.setShowIf("(USE_SPATIAL_OPTIONS == 'true') and ((TABLE_ACTION=='CREATE') or " //$NON-NLS-1$
                        + "(TABLE_ACTION=='DROP_CREATE') or (TABLE_ACTION=='CREATE_IF_NOT_EXISTS') or" //$NON-NLS-1$
                        + " (TABLE_ACTION=='DROP_IF_EXISTS_AND_CREATE'))"); //$NON-NLS-1$
                newParam.setNumRow(201);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue("0.001"); //$NON-NLS-1$
                listParam.add(newParam);

                // <PARAMETER NAME="SRID" FIELD="TEXT" NUM_ROW="208" REQUIRED="true"
                // SHOW_IF="USE_SPATIAL_OPTIONS == 'true'">
                // <DEFAULT>-1</DEFAULT>
                // </PARAMETER>
                newParam = new ElementParameter(node);
                newParam.setName("SRID"); //$NON-NLS-1$
                newParam.setDisplayName("Oracle Spatial Reference System Identifier"); //$NON-NLS-1$
                newParam.setFieldType(EParameterFieldType.TEXT);
                newParam.setRequired(true);
                newParam.setShowIf("USE_SPATIAL_OPTIONS == 'true'"); //$NON-NLS-1$
                newParam.setNumRow(208);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue("-1"); //$NON-NLS-1$
                listParam.add(newParam);

                // <PARAMETER NAME="IMPORT" FIELD="MEMO_IMPORT" REQUIRED="false" SHOW="false" NUM_ROW="2">
                // <DEFAULT>import org.talend.sdi.geometry.Geometry;</DEFAULT>
                // </PARAMETER>
                newParam = new ElementParameter(node);
                newParam.setName("IMPORT"); //$NON-NLS-1$
                newParam.setDisplayName("Imports"); //$NON-NLS-1$
                newParam.setFieldType(EParameterFieldType.MEMO_IMPORT);
                newParam.setRequired(true);
                newParam.setNumRow(2);
                newParam.setShow(false);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue("import org.talend.sdi.geometry.Geometry;"); //$NON-NLS-1$
                listParam.add(newParam);
            }
        }
    }

    /**
     * yzhang Comment method "getColor".
     *
     * @param param
     * @param color
     */
    private RGB getColor(ElementParameter param, String color) {
        if (CommonsPlugin.isHeadless()) {
            return null;
        }

        if (color != null && color.contains(";")) { //$NON-NLS-1$
            String rgb[] = color.split(";"); //$NON-NLS-1$
            if (rgb.length != 3) {
                throw new RuntimeException(Messages.getString("EmfComponent.RGBNotCorrect" //$NON-NLS-1$
                        , param.getDisplayName()));
            }
            return ColorUtils.stringToRGB(color);
        }
        return null;
    }

    private String getMappingType() {
        for (Object parameter : compType.getPARAMETERS().getPARAMETER()) {
            if (parameter instanceof PARAMETERType) {
                PARAMETERType paramType = (PARAMETERType) parameter;
                if ("MAPPING_TYPE".equals(paramType.getFIELD())) { //$NON-NLS-1$
                    if (paramType.getDEFAULT().size() > 0) {
                        return ((DEFAULTType) paramType.getDEFAULT().get(0)).getValue();
                    } else {
                        return null;
                    }
                }
            }
        }
        if (compType.getADVANCEDPARAMETERS() != null) {
            for (Object parameter : compType.getADVANCEDPARAMETERS().getPARAMETER()) {
                if (parameter instanceof PARAMETERType) {
                    PARAMETERType paramType = (PARAMETERType) parameter;
                    if ("MAPPING_TYPE".equals(paramType.getFIELD())) { //$NON-NLS-1$
                        if (paramType.getDEFAULT().size() > 0) {
                            return ((DEFAULTType) paramType.getDEFAULT().get(0)).getValue();
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void initializeTableFromXml(PARAMETERType xmlParam, ElementParameter param) {
        List<TABLEType> tableList = xmlParam.getTABLE();
        if ((tableList == null) || (tableList.size() == 0)) {
            // int nbFlowMaxInput = 0;
            // for (INodeConnector connector : createConnectors()) {
            // if
            // (connector.getName().equals(EConnectionType.FLOW_MAIN.getName()))
            // {
            // nbFlowMaxInput = connector.getMaxLinkInput();
            // }
            // }
            // if (this.isSchemaAutoPropagated() && (nbFlowMaxInput != 0)) {
            // IMetadataTable defaultTable = new MetadataTable();
            // defaultTable.setReadOnly(true);
            //
            // // store the default table in default value
            // IElementParameterDefaultValue defaultValue = new
            // ElementParameterDefaultValue();
            // defaultValue.setDefaultValue(defaultTable);
            // param.getDefaultValues().add(defaultValue);
            // }
            return;
        }

        String mappingType = getMappingType();
        for (TABLEType tableType : tableList) {
            IMetadataTable defaultTable = new MetadataTable();
            EList xmlColumnList = tableType.getCOLUMN();
            COLUMNType xmlColumn;
            List<IMetadataColumn> talendColumnList = new ArrayList<IMetadataColumn>();
            MetadataColumn talendColumn;

            boolean isReadOnly;
            if (tableType.isSetREADONLY()) {
                defaultTable.setReadOnly(tableType.isREADONLY());
                isReadOnly = tableType.isREADONLY();
            } else {
                defaultTable.setReadOnly(param.isReadOnly());
                isReadOnly = param.isReadOnly();
            }

            String readOnlyColumnPosition = tableType.getREADONLYCOLUMNPOSITION();
            if (readOnlyColumnPosition == null) {
                readOnlyColumnPosition = EReadOnlyComlumnPosition.BOTTOM.toString();
            }
            defaultTable.setReadOnlyColumnPosition(readOnlyColumnPosition);

            List<String> originalColumns = new ArrayList<>();

            int nbCustom = 0;
            for (int i = 0; i < xmlColumnList.size(); i++) {
                xmlColumn = (COLUMNType) xmlColumnList.get(i);

                talendColumn = new MetadataColumn();
                talendColumn.setLabel(xmlColumn.getNAME());
                talendColumn.setOriginalDbColumnName(xmlColumn.getNAME());
                talendColumn.setTalendType(xmlColumn.getTYPE());
                talendColumn.setPrecision(new Integer(xmlColumn.getPRECISION()));
                talendColumn.setLength(new Integer(xmlColumn.getLENGTH()));
                talendColumn.setNullable(xmlColumn.isNULLABLE());
                talendColumn.setKey(xmlColumn.isKEY());
                talendColumn.setPattern(xmlColumn.getPATTERN());
                talendColumn.setComment(xmlColumn.getCOMMENT());
                if (xmlColumn.getDBTYPE() != null && !"".equals(xmlColumn.getDBTYPE())) { //$NON-NLS-1$
                    talendColumn.setType(xmlColumn.getDBTYPE());
                } else if (mappingType != null) {
                    String defaultSelectedDbType = MetadataTalendType.getMappingTypeRetriever(mappingType)
                            .getDefaultSelectedDbType(xmlColumn.getTYPE());
                    talendColumn.setType(defaultSelectedDbType);
                }
                if (xmlColumn.isSetREADONLY()) {
                    talendColumn.setReadOnly(xmlColumn.isREADONLY());
                } else if (isReadOnly) {
                    talendColumn.setReadOnly(isReadOnly);
                } else {
                    talendColumn.setReadOnly(xmlParam.isREADONLY());
                }
                if (xmlColumn.isSetCUSTOM()) {
                    talendColumn.setCustom(xmlColumn.isCUSTOM());
                    talendColumn.setCustomId(nbCustom++);
                } else {
                    talendColumn.setCustomId(-1);
                }
                talendColumnList.add(talendColumn);
                originalColumns.add(talendColumn.getLabel());
            }

            defaultTable.setListColumns(talendColumnList);
            defaultTable.setOriginalColumns(originalColumns);

            // store the default table in default value
            IElementParameterDefaultValue defaultValue = new ElementParameterDefaultValue();
            defaultValue.setDefaultValue(defaultTable);
            defaultValue.setIfCondition(tableType.getIF());
            defaultValue.setNotIfCondition(tableType.getNOTIF());
            param.getDefaultValues().add(defaultValue);

            // param.setValue(defaultTable);
        }
    }

    /**
     * DOC nrousseau Comment method "initializePropertyParameters".
     *
     * @param listParam
     */
    private void initializePropertyParameters(List<ElementParameter> listParam) {
        for (ElementParameter param : listParam) {
            if (param.getDefaultValues().size() > 0) {
                boolean isSet = false;
                if (param.getFieldType().equals(EParameterFieldType.COMMAND)) {
                    // convert the values of COMMMAND
                    param.setValue(MultiDefaultValuesUtils.convertDefaultValues(param));
                    continue;
                }
                for (IElementParameterDefaultValue defaultValue : param.getDefaultValues()) {
                    String conditionIf = defaultValue.getIfCondition();
                    String conditionNotIf = defaultValue.getNotIfCondition();

                    if (param.isCondition(conditionIf, conditionNotIf, listParam)) {
                        isSet = true;
                        if (param.getFieldType().equals(EParameterFieldType.RADIO)
                                || param.getFieldType().equals(EParameterFieldType.CHECK)
                                || param.getFieldType().equals(EParameterFieldType.AS400_CHECK)) {
                            param.setValue(new Boolean(defaultValue.getDefaultValue().toString()));
                        } else {
                            param.setValue(defaultValue.getDefaultValue());
                        }
                    }
                }
                if (!isSet) {
                    if (param.getFieldType().equals(EParameterFieldType.RADIO)
                            || param.getFieldType().equals(EParameterFieldType.CHECK)
                            || param.getFieldType().equals(EParameterFieldType.AS400_CHECK)) {
                        int index = this.computeIndex(listParam, param);
                        if (index >= 0) {
                            param.setValue(new Boolean(param.getDefaultValues().get(index).getDefaultValue().toString()));
                        }
                    } else {
                        int index = this.computeIndex(listParam, param);
                        if (index >= 0) {
                            Object defaultValue = param.getDefaultValues().get(index).getDefaultValue();
                            param.setValue(defaultValue);
                            if (param.getFieldType() == EParameterFieldType.ENCODING_TYPE) {
                                String encodingType = TalendTextUtils.removeQuotes((String) defaultValue);
                                IElementParameter elementParameter = param.getChildParameters().get(
                                        EParameterName.ENCODING_TYPE.getName());
                                if (elementParameter != null) {
                                    elementParameter.setValue(encodingType);
                                }
                            }

                        }
                    }
                }
            }
        }

        initializePropertyParametersForSchema(listParam);
    }

    private int computeIndex(List<ElementParameter> listParam, ElementParameter param) {
        String[] types = null;
        int index = 0;
        boolean isDBTYPEANDMYSQL = false;
        List<IElementParameterDefaultValue> elementParameterDefaultValueList = param.getDefaultValues();

        if (EParameterFieldType.MAPPING_TYPE.equals(param.getFieldType())) {
            for (IElementParameterDefaultValue elementParameterDefaultValue : elementParameterDefaultValueList) {
                String ifCondition = ((ElementParameterDefaultValue) elementParameterDefaultValue).ifCondition;
                if (ifCondition != null) {
                    types = ifCondition.split(EQUALS);
                    if (types.length == 2) {
                        if (types[0] != null && types[1] != null) {
                            for (ElementParameter elementParameter : listParam) {
                                if (types[0].equals(elementParameter.getName())
                                        && types[1].substring(1, types[1].length() - 1).equals(elementParameter.getValue())) {
                                    isDBTYPEANDMYSQL = true;
                                    break;
                                }
                            }
                            if (isDBTYPEANDMYSQL) {
                                index = param.getDefaultValues().indexOf(elementParameterDefaultValue);
                                isDBTYPEANDMYSQL = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return index;
    }

    /**
     * Sometimes the property parameters of schema are base on other parameters,but they might be initialized after the
     * schema. So there need to initialize the schema's again.
     *
     * @param listParam
     */
    private void initializePropertyParametersForSchema(List<ElementParameter> listParam) {
        for (ElementParameter param : listParam) {
            if (!param.getFieldType().equals(EParameterFieldType.SCHEMA_TYPE)
                    || !param.getFieldType().equals(EParameterFieldType.DCSCHEMA)) {
                continue;
            }
            if (param.getDefaultValues().size() > 0) {
                boolean isSet = false;
                for (IElementParameterDefaultValue defaultValue : param.getDefaultValues()) {
                    String conditionIf = defaultValue.getIfCondition();
                    String conditionNotIf = defaultValue.getNotIfCondition();

                    if (param.isShow(conditionIf, conditionNotIf, listParam)) {
                        isSet = true;
                        param.setValue(defaultValue.getDefaultValue());
                    }
                }
                if (!isSet) {
                    param.setValue(param.getDefaultValues().get(0).getDefaultValue());
                }
            }
        }
    }

    public void addItemsPropertyParameters(String paramName, ITEMSType items, ElementParameter param, EParameterFieldType type,
            INode node) {
        ITEMType item;
        ElementParameter newParam;

        int nbItems = 0;
        if (items != null) {
            if (items.isSetBASEDONSCHEMA()) {
                param.setBasedOnSchema(items.isBASEDONSCHEMA());
            }
            if (items.isSetBASEDONSUBJOBSTARTS()) {
                param.setBasedOnSubjobStarts(items.isBASEDONSUBJOBSTARTS());
            }
            if (items.isSetCOLUMNSBASEDONSCHEMA()) {
                param.setColumnsBasedOnSchema(items.isCOLUMNSBASEDONSCHEMA());
            }
            nbItems = items.getITEM().size();
            if (param.isBasedOnSchema() || param.isBasedOnSubjobStarts()) {
                nbItems++;
            }
        }

        String[] listRepositoryItem = new String[nbItems];
        String[] listItemsDisplayValue = new String[nbItems];
        String[] listItemsDisplayCodeValue = new String[nbItems];
        Object[] listItemsValue = new Object[nbItems];
        String[] listItemsShowIf = new String[nbItems];
        String[] listItemsNotShowIf = new String[nbItems];
        String[] listField = new String[nbItems];
        String[] listReadonlyIf = new String[nbItems];
        String[] listNotReadonlyIf = new String[nbItems];

        for (int k = 0; k < nbItems; k++) {
            int currentItem = k;
            if (param.isBasedOnSchema() || param.isBasedOnSubjobStarts()) {
                if (k == 0) {
                    if (param.isBasedOnSchema()) {
                        listItemsDisplayCodeValue[k] = "SCHEMA_COLUMN"; //$NON-NLS-1$
                        listItemsDisplayValue[k] = "Column"; //$NON-NLS-1$
                        listField[k] = ""; //$NON-NLS-1$
                        listRepositoryItem[k] = ""; //$NON-NLS-1$
                        listItemsShowIf[k] = null;
                        listItemsNotShowIf[k] = null;
                        newParam = new ElementParameter(node);
                        newParam.setName("SCHEMA_COLUMN"); //$NON-NLS-1$
                        newParam.setDisplayName(""); //$NON-NLS-1$
                        newParam.setFieldType(EParameterFieldType.TEXT);
                        newParam.setValue(""); //$NON-NLS-1$
                        listItemsValue[k] = newParam;
                        continue;
                    } else { // based on subjobs starts
                        listItemsDisplayCodeValue[k] = "SUBJOB_START_COLUMN"; //$NON-NLS-1$
                        listItemsDisplayValue[k] = "Subjob Start"; //$NON-NLS-1$
                        listField[k] = ""; //$NON-NLS-1$
                        listRepositoryItem[k] = ""; //$NON-NLS-1$
                        listItemsShowIf[k] = null;
                        listItemsNotShowIf[k] = null;
                        newParam = new ElementParameter(node);
                        newParam.setName("SUBJOB_START_COLUMN"); //$NON-NLS-1$
                        newParam.setDisplayName(""); //$NON-NLS-1$
                        newParam.setFieldType(EParameterFieldType.TEXT);
                        newParam.setValue(""); //$NON-NLS-1$
                        listItemsValue[k] = newParam;
                        continue;
                    }
                } else {
                    currentItem = k - 1;
                }
            }
            item = (ITEMType) items.getITEM().get(currentItem);
            listItemsDisplayCodeValue[k] = item.getNAME();
            // wzhang modified for 10846
            boolean displayAsValue = item.isDISPLAYNAMEASVALUE();
            if (displayAsValue) {
                String value = item.getVALUE();
                if (value != null) {
                    listItemsDisplayValue[k] = value;
                } else {
                    listItemsDisplayValue[k] = getTranslatedValue(paramName + ".ITEM." + item.getNAME()); //$NON-NLS-1$
                }
            } else {
                listItemsDisplayValue[k] = getTranslatedValue(paramName + ".ITEM." + item.getNAME()); //$NON-NLS-1$
            }
            if (type == EParameterFieldType.ROUTE_COMPONENT_TYPE) {
                listItemsValue[k] = new String[] { item.getNAME(), item.getFILTER() };
                // {component name, attributes filter}
            } else if (type != EParameterFieldType.TABLE && type != EParameterFieldType.TREE_TABLE
                    && type != EParameterFieldType.TABLE_BY_ROW) {
                listItemsValue[k] = item.getVALUE();
            } else {
                EParameterFieldType currentField = EParameterFieldType.getFieldTypeByName(item.getFIELD());
                newParam = new ElementParameter(node);
                newParam.setName(item.getNAME());
                newParam.setFilter(item.getFILTER());
                newParam.setDisplayName(""); //$NON-NLS-1$
                newParam.setFieldType(currentField);
                newParam.setContext(item.getCONTEXT());
                newParam.setShowIf(item.getSHOWIF());
                newParam.setNotShowIf(item.getNOTSHOWIF());
                newParam.setReadOnlyIf(item.getREADONLYIF());
                newParam.setNotReadOnlyIf(item.getNOTREADONLYIF());
                newParam.setNoContextAssist(item.isNOCONTEXTASSIST());
                newParam.setRaw(item.isRAW());
                if (item.isSetREADONLY()) {
                    newParam.setReadOnly(item.isREADONLY());
                }
                switch (currentField) {
                case CLOSED_LIST:
                case OPENED_LIST:
                case COLUMN_LIST:
                case COMPONENT_LIST:
                case CONNECTION_LIST:
                case DBTYPE_LIST:
                case LOOKUP_COLUMN_LIST:
                case PREV_COLUMN_LIST:
                case CONTEXT_PARAM_NAME_LIST:
                case MODULE_LIST:
                    addItemsPropertyParameters(paramName + ".ITEM." + item.getNAME(), item.getITEMS(), newParam, currentField, //$NON-NLS-1$
                            node);
                    break;
                case COLOR:
                    newParam.setValue(DEFAULT_COLOR);
                    break;
                case CHECK:
                case RADIO:
                    if (item.getVALUE() == null || item.getVALUE().equals("")) { //$NON-NLS-1$
                        newParam.setValue(Boolean.FALSE);
                    } else {
                        newParam.setValue(new Boolean(item.getVALUE()));
                    }
                    break;
                case SCHEMA_TYPE:
                    newParam.setValue(""); //$NON-NLS-1$
                    break;
                case SAP_SCHEMA_TYPE:
                    newParam.setValue(""); //$NON-NLS-1$
                    break;
                case SCHEMA_XPATH_QUERYS:
                    newParam.setValue(""); //$NON-NLS-1$
                    break;
                case RULE_TYPE: // hywang add for feature 6484
                    newParam.setFieldType(EParameterFieldType.RULE_TYPE);
                    break;
                    // case VALIDATION_RULE_TYPE:
                    // newParam.setFieldType(EParameterFieldType.VALIDATION_RULE_TYPE);
                    // break;
                case MULTI_PATTERN:
                    newParam.setFieldType(EParameterFieldType.MULTI_PATTERN);
                    break;
                case PATTERN_PROPERTY:
                    newParam.setFieldType(EParameterFieldType.PATTERN_PROPERTY);
                    break;
                case NUMBERLIMITTEXT:
                    newParam.setFieldType(EParameterFieldType.NUMBERLIMITTEXT);
                    if (item.getVALUE() == null || item.getVALUE().equals("")) { //$NON-NLS-1$
                        newParam.setValue(""); //$NON-NLS-1$
                    } else {
                        newParam.setValue(item.getVALUE());
                    }
                    break;
                default: // TEXT by default
                    newParam.setFieldType(EParameterFieldType.TEXT);
                    if (item.getVALUE() == null || item.getVALUE().equals("")) { //$NON-NLS-1$
                        newParam.setValue(""); //$NON-NLS-1$
                    } else {
                        newParam.setValue(item.getVALUE());
                    }

                }
                listItemsValue[k] = newParam;
            }
            listField[k] = item.getFIELD();
            listRepositoryItem[k] = item.getREPOSITORYITEM();
            listItemsShowIf[k] = item.getSHOWIF();
            listItemsNotShowIf[k] = item.getNOTSHOWIF();
            listReadonlyIf[k] = item.getREADONLYIF();
            listNotReadonlyIf[k] = item.getNOTREADONLYIF();
        }

        param.setListItemsDisplayName(listItemsDisplayValue);
        param.setListItemsDisplayCodeName(listItemsDisplayCodeValue);
        param.setListItemsValue(listItemsValue);
        param.setListRepositoryItems(listRepositoryItem);
        param.setListItemsShowIf(listItemsShowIf);
        param.setListItemsNotShowIf(listItemsNotShowIf);
        // hshen 6930
        param.setListItemsNotReadOnlyIf(listNotReadonlyIf);
        param.setListItemsReadOnlyIf(listReadonlyIf);
        if (type != EParameterFieldType.TABLE && type != EParameterFieldType.TREE_TABLE) {
            Object defaultValue = ""; //$NON-NLS-1$
            if (items != null) {
                if (items.getDEFAULT() != null) {
                    boolean found = false;
                    String temp = items.getDEFAULT();
                    for (int i = 0; i < listItemsDisplayCodeValue.length & !found; i++) {
                        if (listItemsDisplayCodeValue[i].equals(items.getDEFAULT())) {
                            found = true;
                            temp = (String) listItemsValue[i];
                        }
                    }
                    defaultValue = new String(temp);
                }
            }
            param.setDefaultClosedListValue(defaultValue);
            param.setValue(defaultValue);
        }
    }

    @Override
    public String getOriginalFamilyName() {
        if (familyName != null) {
            return familyName;
        }
        String originalFamilyName = ""; //$NON-NLS-1$
        if (info != null) {
            originalFamilyName = info.getOriginalFamilyName();
        } else {
            System.out.println("bug?"); //$NON-NLS-1$
        }
        return originalFamilyName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#getTranslatedFamilyName()
     */
    @Override
    public String getTranslatedFamilyName() {
        if (newTranslatedFamilyName != null) {
            return newTranslatedFamilyName;
        }
        if (translatedFamilyName != null) {
            return translatedFamilyName;
        }

        translatedFamilyName = ""; //$NON-NLS-1$
        IComponentsFactory factory = ComponentsFactoryProvider.getInstance();
        String[] familyNames = getOriginalFamilyName().split("\\|"); //$NON-NLS-1$
        int nbTotal = familyNames.length;
        int nb = 0;
        for (String curFamily : familyNames) {
            String[] namesToTranslate = curFamily.split("/"); //$NON-NLS-1$
            int nbSubTotal = namesToTranslate.length;
            int nbSub = 0;
            for (String toTranslate : namesToTranslate) {
                String translated = factory.getFamilyTranslation(this, "FAMILY." + toTranslate.replace(" ", "_")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                if (translated.startsWith("!!")) { //$NON-NLS-1$
                    // no key to translate, so use original
                    translatedFamilyName += toTranslate;
                } else {
                    translatedFamilyName += translated;
                }
                nbSub++;
                if (nbSubTotal != nbSub) {
                    translatedFamilyName += "/"; //$NON-NLS-1$
                }
            }
            nb++;
            if (nbTotal != nb) {
                translatedFamilyName += "|"; //$NON-NLS-1$
            }
        }
        return translatedFamilyName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#hasConditionalOutputs()
     */
    @Override
    public boolean hasConditionalOutputs() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isHASCONDITIONALOUTPUTS();
    }

    @Override
    public String getName() {
        return name;
    }

    // if doesn't exist, return by default the name of the component.
    @Override
    public String getShortName() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        if (!StringUtils.isEmpty(compType.getHEADER().getSHORTNAME())) {
            return compType.getHEADER().getSHORTNAME();
        } else {
            String originalComponentName = getName();
            String calculatedShortName = ""; //$NON-NLS-1$
            char[] cars = new char[originalComponentName.length()];
            int nbChars = 0;

            for (int i = 0; i < originalComponentName.length(); i++) {
                for (char c = 'A'; c <= 'Z'; c++) {
                    if (originalComponentName.charAt(i) == c) {
                        cars[nbChars] = originalComponentName.charAt(i);
                        nbChars++;
                    }
                }
            }
            calculatedShortName = String.copyValueOf(cars, 0, nbChars);
            calculatedShortName = calculatedShortName.toLowerCase();
            return calculatedShortName;
        }
    }

    @Override
    public String getLongName() {
        return getTranslatedValue(PROP_LONG_NAME);
    }

    public boolean canStart() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isSTARTABLE();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.designer.core.model.components.IComponent#createConnectors()
     */
    @Override
    public List<NodeConnector> createConnectors(INode parentNode) {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        EList listConnType;
        CONNECTORType connType;
        NodeConnector nodeConnector;

        List<NodeConnector> listConnector = new ArrayList<NodeConnector>();

        listConnType = compType.getCONNECTORS().getCONNECTOR();
        for (int i = 0; i < listConnType.size(); i++) {
            connType = (CONNECTORType) listConnType.get(i);
            EConnectionType currentType = EConnectionType.getTypeFromName(connType.getCTYPE());
            if (currentType == null || connType.getCTYPE().equals("LOOKUP") || connType.getCTYPE().equals("MERGE")) { //$NON-NLS-1$ //$NON-NLS-2$
                if (currentType == null) {
                    log.warn(Messages.getString("EmfComponent.componentNotExist", this.getName() //$NON-NLS-1$
                            , connType.getCTYPE()));
                }
                continue;
            }
            nodeConnector = new NodeConnector(parentNode);
            nodeConnector.setDefaultConnectionType(currentType);
            // set the default values
            nodeConnector.setLinkName(currentType.getDefaultLinkName());
            nodeConnector.setMenuName(currentType.getDefaultMenuName());
            RGB rgb = currentType.getRGB();
            Integer lineStyle = currentType.getDefaultLineStyle();

            if (connType.isSetMAXINPUT()) {
                nodeConnector.setMaxLinkInput(connType.getMAXINPUT());
            }
            if (connType.isSetMININPUT()) {
                nodeConnector.setMinLinkInput(connType.getMININPUT());
            }
            if (connType.isSetMAXOUTPUT()) {
                nodeConnector.setMaxLinkOutput(connType.getMAXOUTPUT());
            }
            if (connType.isSetMINOUTPUT()) {
                nodeConnector.setMinLinkOutput(connType.getMINOUTPUT());
            }
            if (connType.isSetBUILTIN()) {
                nodeConnector.setBuiltIn(connType.isBUILTIN());
                nodeConnector.setMultiSchema(connType.isBUILTIN());
            }
            if (connType.isSetMERGEALLOWDIFFERENTSCHEMA()) {
                nodeConnector.setMergeAllowDifferentSchema(connType.isMERGEALLOWDIFFERENTSCHEMA());
            }
            if (connType.isSetMULTISCHEMA()) {
                nodeConnector.setMultiSchema(connType.isMULTISCHEMA());
            }

            if (connType.getNAME() == null) {
                nodeConnector.setName(connType.getCTYPE());
            } else {
                nodeConnector.setName(connType.getNAME());
                nodeConnector.setMenuName(getTranslatedValue(connType.getNAME() + ".MENU")); //$NON-NLS-1$
                nodeConnector.setLinkName(getTranslatedValue(connType.getNAME() + ".LINK")); //$NON-NLS-1$
            }

            if (connType.isSetLINESTYLE()) {
                lineStyle = new Integer(connType.getLINESTYLE());
            }

            if (connType.getCOLOR() != null) {
                String colorCode = connType.getCOLOR();
                int r = Integer.parseInt(colorCode.substring(0, 2), 16);
                int g = Integer.parseInt(colorCode.substring(2, 4), 16);
                int b = Integer.parseInt(colorCode.substring(4, 6), 16);
                rgb = new RGB(r, g, b);
            }

            String notShowIf = connType.getNOTSHOWIF();
            if (notShowIf != null && !("".equals(notShowIf))) { //$NON-NLS-1$
                nodeConnector.setNotShowIf(notShowIf);
            }

            String showIf = connType.getSHOWIF();
            if (showIf != null && !("".equals(showIf))) { //$NON-NLS-1$
                nodeConnector.setShowIf(showIf);
            }

            nodeConnector.addConnectionProperty(currentType, rgb, lineStyle);
            if (connType.getCOLOR() != null) {
                // force RGB color (need code review, as this shouldn't be needed here)
                nodeConnector.getConnectionProperty(currentType).setRGB(rgb);
            }
            String baseSchema = connType.getBASESCHEMA();
            if (baseSchema != null && (!baseSchema.equals(""))) { //$NON-NLS-1$
                nodeConnector.setBaseSchema(baseSchema);
            } else {
                nodeConnector.setBaseSchema(nodeConnector.getName());
            }
            listConnector.add(nodeConnector);
            if (connType.getCTYPE().equals("FLOW")) { //$NON-NLS-1$
                // if kind is "flow" (main type), then add the same for the lookup and merge.
                currentType = EConnectionType.FLOW_REF;

                if (connType.getCOLOR() == null) {
                    rgb = currentType.getRGB();
                }
                if (!connType.isSetLINESTYLE()) {
                    lineStyle = currentType.getDefaultLineStyle();
                }
                nodeConnector.addConnectionProperty(currentType, rgb, lineStyle);
                nodeConnector.getConnectionProperty(currentType).setRGB(rgb);
                currentType = EConnectionType.FLOW_MERGE;

                if (connType.getCOLOR() == null) {
                    rgb = currentType.getRGB();
                }
                if (!connType.isSetLINESTYLE()) {
                    lineStyle = currentType.getDefaultLineStyle();
                }
                nodeConnector.addConnectionProperty(currentType, rgb, lineStyle);
                nodeConnector.getConnectionProperty(currentType).setRGB(rgb);
            }
        }

        // set the specific parameters for the Lookup (if there is for merge later, this should be placed here)
        for (int i = 0; i < listConnType.size(); i++) {
            connType = (CONNECTORType) listConnType.get(i);
            EConnectionType currentType = EConnectionType.getTypeFromName(connType.getCTYPE());
            if (currentType == null) {
                continue;
            }
            if (connType.getCTYPE().equals("LOOKUP") && (connType.getCOMPONENT() != null)) { //$NON-NLS-1$
                for (INodeConnector connector : listConnector) {
                    if (connector.getName().equals("FLOW")) { //$NON-NLS-1$
                        IConnectionProperty property = connector.getConnectionProperty(EConnectionType.FLOW_REF);
                        property.setLinkedComponent(connType.getCOMPONENT());
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < EConnectionType.values().length; i++) {
            EConnectionType currentType = EConnectionType.values()[i];

            if ((currentType == EConnectionType.FLOW_REF) || (currentType == EConnectionType.FLOW_MERGE)) {
                continue;
            }
            boolean exists = false;
            for (INodeConnector curNodeConn : listConnector) {
                if (curNodeConn.getDefaultConnectionType().equals(currentType)) {
                    exists = true;
                }
            }
            if (!exists) { // will add by default all connectors not defined in
                // the xml files
                nodeConnector = new NodeConnector(parentNode);
                nodeConnector.setDefaultConnectionType(currentType);
                nodeConnector.setName(currentType.getName());
                nodeConnector.setBaseSchema(currentType.getName());
                nodeConnector.addConnectionProperty(currentType, currentType.getRGB(), currentType.getDefaultLineStyle());
                nodeConnector.setLinkName(currentType.getDefaultLinkName());
                nodeConnector.setMenuName(currentType.getDefaultMenuName());
                if ((currentType == EConnectionType.PARALLELIZE) || (currentType == EConnectionType.SYNCHRONIZE)) {
                    nodeConnector.setMaxLinkInput(1);
                } else {
                    nodeConnector.setMaxLinkInput(0);
                }
                nodeConnector.setMaxLinkOutput(0);
                nodeConnector.setMinLinkInput(0);
                nodeConnector.setMinLinkOutput(0);
                if (currentType == EConnectionType.FLOW_MAIN) {
                    nodeConnector.addConnectionProperty(EConnectionType.FLOW_REF, EConnectionType.FLOW_REF.getRGB(),
                            EConnectionType.FLOW_REF.getDefaultLineStyle());
                    nodeConnector.addConnectionProperty(EConnectionType.FLOW_MERGE, EConnectionType.FLOW_MERGE.getRGB(),
                            EConnectionType.FLOW_MERGE.getDefaultLineStyle());
                }
                listConnector.add(nodeConnector);
            }
        }
        return listConnector;
    }

    @Override
    public String getPluginExtension() {
        if (info != null) {
            return info.getPluginExtension();
        }
        return null;
    }

    @Override
    public boolean isSchemaAutoPropagated() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isSCHEMAAUTOPROPAGATE();
    }

    @Override
    public boolean isDataAutoPropagated() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isDATAAUTOPROPAGATE();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#isVisible()
     */
    @Override
    public boolean isVisible() {
        return isVisible(null);
    }

    @Override
    public boolean isVisible(String family) {
        if (visible != null) {
            return visible;
        }
        return ComponentsSettingsHelper.isComponentVisible(this, family);
    }

    @Override
    public boolean isVisibleInComponentDefinition() {
        if (visible != null) {
            return visible;
        }
        if (visibleFromComponentDefinition != null) {
            return visibleFromComponentDefinition;
        }
        if (info != null) {
            visibleFromComponentDefinition = info.isIsVisibleInComponentDefinition();
            return visibleFromComponentDefinition;
        } else {
            return false;
        }
    }

    @Override
    public String getVersion() {
        return info.getVersion();
    }

    private List<ModuleNeeded> componentImportNeedsList = new ArrayList<ModuleNeeded>();

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#getModulesNeeded()
     */
    @Override
    public List<ModuleNeeded> getModulesNeeded() {
        return getModulesNeeded(null);
    }

    private void setImportTypes() throws BusinessException {
        IMPORTSType imports = compType.getCODEGENERATION().getIMPORTS();
        List<IMPORTType> importTypes = new ArrayList<>();
        if (imports != null) {
            importTypes.addAll(ImportModuleManager.getInstance().getImportTypes(imports));
            info.getImportType().addAll(importTypes);
        }
    }

    private boolean isBigdataProcess(String componentsType) {
        if (ComponentCategory.CATEGORY_4_MAPREDUCE.getName().equals(componentsType)) {
            return true;
        }
        if (ComponentCategory.CATEGORY_4_SPARK.getName().equals(componentsType)) {
            return true;
        }
        if (ComponentCategory.CATEGORY_4_SPARKSTREAMING.getName().equals(componentsType)) {
            return true;
        }
        if (ComponentCategory.CATEGORY_4_STORM.getName().equals(componentsType)) {
            return true;
        }
        return false;
    }

    private String getDistributionVersion(INode node, ComponentType compType) {
        IProcess process = node.getProcess();
        if (process != null && isBigdataProcess(process.getComponentsType())) {
            List<? extends INode> nodes = process.getNodesOfType("tSparkConfiguration");
            if (nodes != null && !nodes.isEmpty()) {
                node = nodes.get(0);
                // As we are changing the node to tSparkConfiguration we need to change compType accordingly.
                // This is compatible also with Spark streaming jobs because compType.getVersionParameter() returns the
                // same in batch and streaming
                compType = ComponentType.SPARKBATCH;
            }
        }
        if (node == null || compType == null) {
            return null;
        }
        IElementParameter hdElemParam = node.getElementParameter(compType.getVersionParameter());
        Object value = null;
        if (hdElemParam != null && (hdElemParam.isShow(node.getElementParameters()) || hdElemParam.getValue().toString().contains("SPARK"))) {
            // for universal feature, the parameter is not shown in DI components, yet we still need to retrieve its value
            value = hdElemParam.getValue();
        } else {
            if (JavaProcessUtil.isUseExistingConnection(node)) {
                IElementParameter connection = node.getElementParameter(EParameterName.CONNECTION.getName());
                if (connection != null) {
                    String connNodeName = (String) connection.getValue();
                    List<? extends INode> graphicalNodes = node.getProcess().getGraphicalNodes();
                    INode connNode = null;
                    for (INode gNode : graphicalNodes) {
                        if (StringUtils.equals(connNodeName, gNode.getUniqueName())) {
                            connNode = gNode;
                            break;
                        }
                    }
                    if (connNode != null) {
                        hdElemParam = connNode.getElementParameter(compType.getVersionParameter());
                        if (hdElemParam != null) {
                            value = hdElemParam.getValue();
                        }
                    }
                }
            }
        }
        return (String) value;
    }

    private void addHadoopDistributionModuleNeededList(INode node, List<ModuleNeeded> moduleNeededList) {
        ExceptionHandler.logDebug("addHadoopDistributionModuleNeededList, node: " + node);
        if (node != null) {
            String value = null;
            try {
                value = getDistributionVersion(node, hadoopDistributionParamName);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            if (value != null) {
                List<ModuleNeeded> cachedModuleNeededList = Collections.EMPTY_LIST;
                DistributionVersion dv = null;
                for (Map.Entry<DistributionVersion, List<ModuleNeeded>> entry : distributionModuleNeededMap.entrySet()) {
                    DistributionVersion key = entry.getKey();
                    if (value.equals(key.getVersion())) {
                        dv = key;
                        cachedModuleNeededList = entry.getValue();
                        break;
                    }
                }
                if (cachedModuleNeededList == null) {
                    /**
                     * Means not added yet, retrieve and add it
                     */
                    cachedModuleNeededList = new LinkedList<>();
                    loadHadoopDistributionModules(dv, node.getComponent().getName(), moduleNeededList, cachedModuleNeededList);
                } else {
                    /**
                     * Means already added
                     */
                }
            }

            if (Boolean.getBoolean(PROP_LOAD_DYNAMIC_DISTRIBUTION_MODULE)) {

                for (Map.Entry<DistributionVersion, List<ModuleNeeded>> entry : distributionModuleNeededMap.entrySet()) {
                    DistributionVersion key = entry.getKey();
                    List<ModuleNeeded> cachedModuleNeededList = entry.getValue();
                    if (cachedModuleNeededList == null) {
                        /**
                         * Means not added yet, retrieve and add it
                         */
                        cachedModuleNeededList = new LinkedList<>();
                        loadHadoopDistributionModules(key, node.getComponent().getName(), moduleNeededList, cachedModuleNeededList);
                    }
                }
            }
        }
    }

    private void loadHadoopDistributionModules(DistributionVersion dv, String compName, List<ModuleNeeded> moduleNeededList,List<ModuleNeeded> cachedModuleNeededList) {
        if (dv instanceof DynamicDistributionVersion) {
            try {
                IDynamicDistributionTemplate distributionTemplate = ((DynamicDistributionVersion) dv).getDistributionTemplate();
                if (!distributionTemplate.isPluginExtensionsRegisted()) {
                    distributionTemplate.registPluginExtensions();
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        for (DistributionVersionModule versionModule : dv.getVersionModules()) {
            IMPORTType importType = ComponentFactory.eINSTANCE.createIMPORTType();
            importType.setMODULEGROUP(versionModule.moduleGroup.getModuleName());
            importType.setMRREQUIRED(versionModule.moduleGroup.isMrRequired());
            importType.setREQUIREDIF(versionModule.getModuleRequiredIf().getConditionString());
            ModulesNeededProvider
            .collectModuleNeeded(compName, // $NON-NLS-1$
                    importType, cachedModuleNeededList, versionModule.distributionVersion.getVersion());
        }
        distributionModuleNeededMap.put(dv, cachedModuleNeededList);
        moduleNeededList.addAll(cachedModuleNeededList);
        ExceptionHandler.logDebug("addHadoopDistributionModuleNeededList, size of cachedModuleNeededList: " + cachedModuleNeededList.size());
    }

    private void addHadoopLibModuleNeededList(INode node, List<ModuleNeeded> moduleNeededList) {
        ExceptionHandler.logDebug("addHadoopDistributionModuleNeededList, node: " + node);
        if (node != null) {
            String value = null;
            try {
                value = getDistributionVersion(node, hadoopLibParamName);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            if (value != null) {
                List<ModuleNeeded> cachedModuleNeededList = Collections.EMPTY_LIST;
                HadoopComponent hc = null;
                for (Map.Entry<HadoopComponent, List<ModuleNeeded>> entry : hadoopLibModuleNeededMap.entrySet()) {
                    HadoopComponent key = entry.getKey();
                    if (value.equals(key.getVersion())) {
                        hc = key;
                        cachedModuleNeededList = entry.getValue();
                        break;
                    }
                }
                if (cachedModuleNeededList == null) {
                    /**
                     * means not added yet, retrieve and add it
                     */
                    cachedModuleNeededList = new LinkedList<>();
                    loadHadoopLibDynamicModules(hc, node.getComponent().getName(), moduleNeededList, cachedModuleNeededList);
                } else {
                    /**
                     * means already added
                     */
                }
            }

            if (Boolean.getBoolean(PROP_LOAD_DYNAMIC_DISTRIBUTION_MODULE)) {
                List<ModuleNeeded> cachedModuleNeededList = null;
                for (Map.Entry<HadoopComponent, List<ModuleNeeded>> entry : hadoopLibModuleNeededMap.entrySet()) {
                    cachedModuleNeededList = entry.getValue();
                    if (cachedModuleNeededList == null) {
                        /**
                         * means not added yet, retrieve and add it
                         */
                        cachedModuleNeededList = new LinkedList<>();

                        loadHadoopLibDynamicModules(entry.getKey(), node.getComponent().getName(), moduleNeededList, cachedModuleNeededList);
                    }
                }
            }
        }
    }

    private void loadHadoopLibDynamicModules(HadoopComponent hc, String compName, List<ModuleNeeded> moduleNeededList, List<ModuleNeeded> cachedModuleNeededList) {
        try {
            if (hc instanceof AbstractDynamicDistributionTemplate) {
                AbstractDynamicDistributionTemplate ddt = (AbstractDynamicDistributionTemplate) hc;
                if (!ddt.isPluginExtensionsRegisted()) {
                    ddt.registPluginExtensions();
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        Set<DistributionModuleGroup> nodeModuleGroups = hc.getModuleGroups(hadoopLibParamName, compName);
        if (nodeModuleGroups != null) {
            Iterator<DistributionModuleGroup> moduleGroups = nodeModuleGroups.iterator();
            while (moduleGroups.hasNext()) {
                DistributionModuleGroup group = moduleGroups.next();
                IMPORTType importType = ComponentFactory.eINSTANCE.createIMPORTType();
                importType.setMODULEGROUP(group.getModuleName());

                ComponentCondition condition = group.getRequiredIf();
                if (condition != null) {
                    importType.setREQUIREDIF(new NestedComponentCondition(condition).getConditionString());
                }
                importType.setMRREQUIRED(group.isMrRequired());
                ModulesNeededProvider.collectModuleNeeded(compName, importType, cachedModuleNeededList, hc.getVersion());
            }
        }
        hadoopLibModuleNeededMap.put(hc, cachedModuleNeededList);
        moduleNeededList.addAll(cachedModuleNeededList);
        ExceptionHandler.logDebug("addHadoopLibModuleNeededList, size of cachedModuleNeededList: " + cachedModuleNeededList.size());
    }

    private boolean loadDynamicDistributionModules() {
        return areHadoopDistribsLoaded || Boolean.getBoolean(PROP_LOAD_DYNAMIC_DISTRIBUTION_MODULE);
    }

    private boolean loadDynamicLibsDistributionModules() {
        return areHadoopLibsLoaded || Boolean.getBoolean(PROP_LOAD_DYNAMIC_DISTRIBUTION_MODULE);
    }

    @Override
    public List<ModuleNeeded> getModulesNeeded(INode node) {
        if (componentImportNeedsList != null && componentImportNeedsList.size() > 0) {
            if (loadDynamicDistributionModules()) {
                try {
                    addHadoopDistributionModuleNeededList(node, componentImportNeedsList);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }

            }

            if (loadDynamicLibsDistributionModules()) {

                try {
                    addHadoopLibModuleNeededList(node, componentImportNeedsList);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }

            return componentImportNeedsList;
        }

        Set<String> moduleNames = new HashSet<>();
        List<IMPORTType> importTypes = info.getImportType();
        for (IMPORTType importType : importTypes) {
            ModulesNeededProvider.collectModuleNeeded(this.getName(), importType, componentImportNeedsList);
        }
        for (String name : info.getComponentNames()) {
            IComponent component = null;
            String type = getType();
            if (StringUtils.isNotBlank(type)) {
                component = ComponentsFactoryProvider.getInstance().get(name, type);
            } else {
                component = ComponentsFactoryProvider.getInstance().get(name);
            }
            if (component == null) {
                continue;
            }
            for (ModuleNeeded moduleNeeded : component.getModulesNeeded(node)) {
                if (!moduleNames.contains(moduleNeeded.getModuleName())) {
                    ModuleNeeded componentImportNeeds = new ModuleNeeded(this.getName(), moduleNeeded.getModuleName(),
                            moduleNeeded.getInformationMsg(), moduleNeeded.isRequired(), moduleNeeded.getInstallURL(),
                            moduleNeeded.getRequiredIf(), moduleNeeded.getMavenURIFromConfiguration());
                    componentImportNeeds.setModuleLocaion(moduleNeeded.getModuleLocaion());
                    componentImportNeedsList.add(componentImportNeeds);
                }
            }
        }

        // TODO to remove later, need to find another way to do this (for feature 18686)
        // adds manually all definitions to avoid to modify the component
        if (ArrayUtils.contains(JavaTypesManager.getJavaTypesLabels(), "Geometry") && "tOracleInput".equals(name)) { //$NON-NLS-1$ //$NON-NLS-2$
            // <IMPORT NAME="oracle-sdoapi" MODULE="sdoapi.jar" REQUIRED="true" />
            ModuleNeeded componentImportNeeds = new ModuleNeeded("oracle-sdoapi", "sdoapi.jar", //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString("modules.required"), true, new ArrayList<String>(), null, null); //$NON-NLS-1$
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="oracle-sdoutil" MODULE="sdoutil.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("oracle-sdoutil", "sdoutil.jar", Messages.getString("modules.required"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="jts-1.12" MODULE="jts-1.12.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("jts-1.12", "jts-1.12.jar", Messages.getString("modules.required"), true, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="org.talend.sdi" MODULE="org.talend.sdi.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("org.talend.sdi", "org.talend.sdi.jar", //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString("modules.required"), true, new ArrayList<String>(), null, null); //$NON-NLS-1$
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="Java-DOM4J" MODULE="dom4j-1.6.1.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("Java-DOM4J", "dom4j-1.6.1.jar", Messages.getString("modules.required"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="Java-JAXEN" MODULE="jaxen-1.1.1.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("Java-JAXEN", "jaxen-1.1.1.jar", Messages.getString("modules.required"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);
        }

        if (ArrayUtils.contains(JavaTypesManager.getJavaTypesLabels(), "Geometry") && "tOracleOutput".equals(name)) { //$NON-NLS-1$ //$NON-NLS-2$
            // <IMPORT NAME="oracle-sdoapi" MODULE="sdoapi.jar" REQUIRED="true" />
            ModuleNeeded componentImportNeeds = new ModuleNeeded("oracle-sdoapi", "sdoapi.jar", //$NON-NLS-1$ //$NON-NLS-2$
                    Messages.getString("modules.required"), true, new ArrayList<String>(), null, null); //$NON-NLS-1$
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="oracle-sdoutil" MODULE="sdoutil.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("oracle-sdoutil", "sdoutil.jar", Messages.getString("modules.required"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="Java-DOM4J" MODULE="dom4j-1.6.1.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("Java-DOM4J", "dom4j-1.6.1.jar", Messages.getString("modules.required"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="Java-JAXEN" MODULE="jaxen-1.1.1.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("Java-JAXEN", "jaxen-1.1.1.jar", Messages.getString("modules.required"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);
        }

        if (loadDynamicDistributionModules()) {
            addHadoopDistributionModuleNeededList(node, componentImportNeedsList);
        }

        if(loadDynamicLibsDistributionModules()) {
            addHadoopLibModuleNeededList(node, componentImportNeedsList);
        }

        return componentImportNeedsList;
    }

    protected void initBundleID(IMPORTType importType, ModuleNeeded componentImportNeeds) {
        String bundleID = importType.getBundleID();
        if (bundleID != null) {
            String bundleName = null;
            String bundleVersion = null;
            if (bundleID.contains(":")) { //$NON-NLS-1$
                String[] nameAndVersion = bundleID.split(":"); //$NON-NLS-1$
                bundleName = nameAndVersion[0];
                bundleVersion = nameAndVersion[1];
            } else {
                bundleName = bundleID;
            }
            componentImportNeeds.setBundleName(bundleName);
            componentImportNeeds.setBundleVersion(bundleVersion);
        }
    }

    /**
     * DOC nrousseau Comment method "createMultipleComponentManagers".
     *
     * @return
     */
    @Override
    protected ArrayList<IMultipleComponentManager> createMultipleComponentManagers() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        ArrayList<IMultipleComponentManager> theMultipleComponentManagers = new ArrayList<IMultipleComponentManager>();
        EList templatesTypes = compType.getCODEGENERATION().getTEMPLATES();
        if (templatesTypes != null) {
            for (int ii = 0; ii < templatesTypes.size(); ii++) {
                TEMPLATESType templatesType = (TEMPLATESType) templatesTypes.get(ii);
                String input, output, connector;

                input = templatesType.getINPUT();
                output = templatesType.getOUTPUT();
                connector = templatesType.getCONNECTOR();
                boolean lookupMode = templatesType.isLOOKUP();

                if (!lookupMode && (input == null || output == null)) {
                    continue;
                }
                IMultipleComponentManager multipleComponentManager = null;
                if (lookupMode) {
                    multipleComponentManager = new MultipleComponentManager(lookupMode);
                } else if (connector == null || connector.equals("")) { //$NON-NLS-1$
                    multipleComponentManager = new MultipleComponentManager(input, output);
                } else {
                    multipleComponentManager = new MultipleComponentManager(input, output, connector);
                }

                EList listTempType = templatesType.getTEMPLATE();
                for (int i = 0; i < listTempType.size(); i++) {
                    TEMPLATEType templateType = (TEMPLATEType) listTempType.get(i);

                    String name, component;
                    name = templateType.getNAME();
                    component = templateType.getCOMPONENT();

                    IMultipleComponentItem currentItem = multipleComponentManager.addItem(name, component);
                    EList listLinkTo = templateType.getLINKTO();
                    if (listLinkTo.size() > 0 && !multipleComponentManager.existsLinkTo()) {
                        multipleComponentManager.setExistsLinkTo(true);
                    }
                    for (int j = 0; j < listLinkTo.size(); j++) {
                        LINKTOType linkTo = (LINKTOType) listLinkTo.get(j);

                        name = linkTo.getNAME();
                        String cType = linkTo.getCTYPE();
                        currentItem.getOutputConnections().add(new MultipleComponentConnection(cType, name));
                    }
                }

                EList listTempParamType = templatesType.getTEMPLATEPARAM();
                for (int i = 0; i < listTempParamType.size(); i++) {
                    TEMPLATEPARAMType templateParamType = (TEMPLATEPARAMType) listTempParamType.get(i);

                    if ((templateParamType.getSOURCE() != null) && (templateParamType.getTARGET() != null)) {
                        String source, target;
                        source = templateParamType.getSOURCE();
                        target = templateParamType.getTARGET();

                        if (lookupMode) {
                            target = "LOOKUP." + target; //$NON-NLS-1$
                        }

                        multipleComponentManager.addParam(source, target);
                    }
                    if ((templateParamType.getTARGET() != null) && (templateParamType.getVALUE() != null)) {
                        String value, target;
                        value = templateParamType.getVALUE();
                        target = templateParamType.getTARGET();

                        if (lookupMode) {
                            target = "LOOKUP." + target; //$NON-NLS-1$
                        }

                        multipleComponentManager.addValue(target, value);
                    }
                }

                multipleComponentManager.validateItems();
                theMultipleComponentManagers.add(multipleComponentManager);
            }
        }// else return an empty array
        return theMultipleComponentManagers;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#isLoaded()
     */
    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void setImageRegistry(Map<String, ImageDescriptor> imageRegistry) {
        this.imageRegistry = imageRegistry;
    }

    @Override
    public Map<String, ImageDescriptor> getImageRegistry() {
        return imageRegistry;
    }

    /**
     * Getter for icon16.
     *
     * @return the icon16
     */
    @Override
    public ImageDescriptor getIcon16() {
        if (!this.imageRegistry.containsKey(getName() + "_16")) { //$NON-NLS-1$
            String path = new Path(ComponentBundleToPath.getPathFromBundle(bundleName)).append(this.pathSource).append(this.name)
                    .toPortableString();
            ComponentIconLoading cil = new ComponentIconLoading(imageRegistry, new File(path));

            // only call to initialize the icons in the registry
            cil.getImage32();
            cil.getImage16();
            cil.getImage24();
        }
        return this.imageRegistry.get(getName() + "_16"); //$NON-NLS-1$
    }

    /**
     * Getter for icon24.
     *
     * @return the icon24
     */
    @Override
    public ImageDescriptor getIcon24() {
        if (!this.imageRegistry.containsKey(getName() + "_24")) { //$NON-NLS-1$
            String path = new Path(ComponentBundleToPath.getPathFromBundle(bundleName)).append(this.pathSource).append(this.name)
                    .toPortableString();
            ComponentIconLoading cil = new ComponentIconLoading(imageRegistry, new File(path));

            // only call to initialize the icons in the registry
            cil.getImage32();
            cil.getImage16();
            cil.getImage24();
        }
        return this.imageRegistry.get(getName() + "_24"); //$NON-NLS-1$
    }

    /**
     * Getter for icon32.
     *
     * @return the icon32
     */
    @Override
    public ImageDescriptor getIcon32() {
        if (!this.imageRegistry.containsKey(getName() + "_32")) { //$NON-NLS-1$
            String path = new Path(ComponentBundleToPath.getPathFromBundle(bundleName)).append(this.pathSource).append(this.name)
                    .toPortableString();
            ComponentIconLoading cil = new ComponentIconLoading(imageRegistry, new File(path));

            // only call to initialize the icons in the registry
            cil.getImage32();
            cil.getImage16();
            cil.getImage24();
        }
        return this.imageRegistry.get(getName() + "_32"); //$NON-NLS-1$
    }

    @Override
    public String getPathSource() {
        return this.pathSource;
    }

    public void setPathSource(String pathSource) {
        this.pathSource = pathSource;
    }

    private ArrayList<ECodePart> createCodePartList() {
        ArrayList<ECodePart> theCodePartList = new ArrayList<ECodePart>();
        String applicationPath = ComponentBundleToPath.getPathFromBundle(bundleName);
        applicationPath = (new Path(applicationPath)).toPortableString();
        File dirChildFile = new File(applicationPath + uriString);
        File dirFile = dirChildFile.getParentFile();
        final String extension = "." + LanguageManager.getCurrentLanguage().getName() + "jet"; //$NON-NLS-1$ //$NON-NLS-2$
        FilenameFilter fileNameFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(extension);
            }
        };

        String[] jetFiles = dirFile.list(fileNameFilter);

        for (String jetFile : jetFiles) {
            String name = jetFile;
            name = jetFile.replace(getName() + "_", ""); //$NON-NLS-1$ //$NON-NLS-2$
            name = name.replace(extension, ""); //$NON-NLS-1$
            ECodePart part = ECodePart.getCodePartByName(name);
            if (part != null) {
                theCodePartList.add(part);
            }
        }
        return theCodePartList;
    }

    @Override
    public List<ECodePart> getAvailableCodeParts() {
        if (codePartListX == null) {
            codePartListX = createCodePartList();
        }
        return codePartListX;
    }

    @Override
    public List<String> getPluginDependencies() {
        List<String> pluginDependencyList = new ArrayList<>();
        if (info != null) {
            pluginDependencyList = info.getPluginDependencies();
        } else {
            System.out.println("bug?"); //$NON-NLS-1$
        }
        return pluginDependencyList;
    }

    @Override
    public boolean useMerge() {
        if (useMerge == null) {
            if (compType == null) {
                isLoaded = false;
                try {
                    load();
                } catch (BusinessException e) {
                    // TODO Auto-generated catch block
                    ExceptionHandler.process(e);
                }
            }
            useMerge = false;
            EList listConnType;
            CONNECTORType connType;

            listConnType = compType.getCONNECTORS().getCONNECTOR();
            for (int i = 0; i < listConnType.size(); i++) {
                connType = (CONNECTORType) listConnType.get(i);
                if (connType.getCTYPE().equals(EConnectionType.FLOW_MERGE.getName())) {
                    useMerge = true;
                    break;
                }
            }
        }
        return useMerge;
    }

    @Override
    public boolean useFlow() {
        boolean useFlow = false;
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        EList listConnType;
        CONNECTORType connType;

        listConnType = compType.getCONNECTORS().getCONNECTOR();
        for (int i = 0; i < listConnType.size(); i++) {
            connType = (CONNECTORType) listConnType.get(i);
            if (connType.getCTYPE().equals(EConnectionType.FLOW_MAIN.getName())
                    && !(connType.isSetMAXINPUT() && connType.getMAXINPUT() == 0 && connType.isSetMAXOUTPUT()
                    && connType.getMAXOUTPUT() == 0 || connType.isSetMININPUT() && connType.getMININPUT() == 0
                    && connType.isSetMINOUTPUT() && connType.getMINOUTPUT() == 0)) {
                useFlow = true;
                break;
            }
        }
        return useFlow;
    }

    @Override
    public boolean useSchema(Node node) {
        boolean useSchema = false;
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        PARAMETERType pType;
        EList parameters = compType.getPARAMETERS().getPARAMETER();
        for (Object parameter : parameters) {
            pType = (PARAMETERType) parameter;
            if ("SCHEMA_TYPE".equals(pType.getFIELD()) && !pType.isREADONLY() && //$NON-NLS-1$
                    (pType.getCONTEXT() == null || "FLOW".equals(pType.getCONTEXT()))) { //$NON-NLS-1$
                useSchema = true;
                break;
            }
        }
        return useSchema;
    }

    @Override
    public boolean isMultiplyingOutputs() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isISMULTIPLYINGOUTPUTS();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#getComponentType()
     */
    @Override
    public boolean isMultipleOutput() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        EList listConnType = compType.getCONNECTORS().getCONNECTOR();
        for (int i = 0; i < listConnType.size(); i++) {
            CONNECTORType connType = (CONNECTORType) listConnType.get(i);
            EConnectionType currentType = EConnectionType.getTypeFromName(connType.getCTYPE());
            if (currentType == EConnectionType.FLOW_MAIN) {

                if (connType.getMAXOUTPUT() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isMultiSchemaOutput() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        EList listConnType = compType.getCONNECTORS().getCONNECTOR();
        for (int i = 0; i < listConnType.size(); i++) {
            CONNECTORType connType = (CONNECTORType) listConnType.get(i);
            EConnectionType currentType = EConnectionType.getTypeFromName(connType.getCTYPE());
            if (currentType == EConnectionType.FLOW_MAIN) {
                return connType.isMULTISCHEMA();
            }
        }
        return false;
    }

    private boolean connectorUseInputLinkSelection(String name) {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        EList listConnType;
        CONNECTORType connType;

        listConnType = compType.getCONNECTORS().getCONNECTOR();
        for (int i = 0; i < listConnType.size(); i++) {
            connType = (CONNECTORType) listConnType.get(i);
            if (connType.getNAME() == null) {
                if (connType.getCTYPE().equals(name)) {
                    return connType.isINPUTLINKSELECTION();
                }
            } else if (connType.getNAME().equals(name)) {
                return connType.isINPUTLINKSELECTION();
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#useLookup()
     */
    @Override
    public boolean useLookup() {
        if (useLookup == null) {
            if (compType == null) {
                isLoaded = false;
                try {
                    load();
                } catch (BusinessException e) {
                    // TODO Auto-generated catch block
                    ExceptionHandler.process(e);
                }
            }
            useLookup = false;
            EList listConnType;
            CONNECTORType connType;

            listConnType = compType.getCONNECTORS().getCONNECTOR();
            for (int i = 0; i < listConnType.size(); i++) {
                connType = (CONNECTORType) listConnType.get(i);
                if (connType.getCTYPE().equals(EConnectionType.FLOW_REF.getName())) {
                    useLookup = true;
                    break;
                }
            }
        }
        return useLookup;
    }

    /**
     * there only search in the <ADVANCEDPARAMETERS/> node, it can be a little faster.
     *
     * @see org.talend.core.model.components.IComponent#useImport()
     */
    @Override
    public boolean useImport() {
        if (useImport == null) {
            if (compType == null) {
                isLoaded = false;
                try {
                    load();
                } catch (BusinessException e) {
                    // TODO Auto-generated catch block
                    ExceptionHandler.process(e);
                }
            }
            useImport = false;
            EList listParameterType;
            PARAMETERType parameterType;
            ADVANCEDPARAMETERSType advancedparameters = compType.getADVANCEDPARAMETERS();
            if (advancedparameters != null && advancedparameters.getPARAMETER() != null) {
                listParameterType = advancedparameters.getPARAMETER();
                for (int i = 0; i < listParameterType.size(); i++) {
                    parameterType = (PARAMETERType) listParameterType.get(i);
                    if (parameterType.getFIELD().equals(EParameterFieldType.MEMO_IMPORT.getName())) {
                        useImport = true;
                        break;
                    }
                }

            }
        }
        return useImport;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#getComponentType()
     */
    @Override
    public EComponentType getComponentType() {
        return EComponentType.EMF;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#isHashComponent()
     */
    @Override
    public boolean isHashComponent() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isHASHCOMPONENT();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#isTechnical()
     */
    @Override
    public boolean isTechnical() {
        if (technical != null) {
            info.setIsTechnical(technical);
            return technical;
        }
        boolean isTrchnical = false;
        if (info != null) {
            isTrchnical = info.isIsTechnical();
        }
        return isTrchnical;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isSINGLETON();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#isMainCodeCalled()
     */
    @Override
    public boolean isMainCodeCalled() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isMAINCODECALLED();
    }

    /**
     * get this component's repository type <br>
     * see <PARAMETER NAME="PROPERTY" ...> in the component's xml definition.
     *
     * @return
     */
    @Override
    public String getRepositoryType() {
        return info.getRepositoryType();
    }

    @Override
    public boolean canParallelize() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isPARALLELIZE();
    }

    /**
     * return the common ComponentResourceFactoryImpl to retreive component resource from URI
     *
     * @return factoryImpl
     */
    // here we are using soft references so that whenever the GC runs it can collect the ComponentResourceFactoryImpl
    private static ComponentResourceFactoryImpl getComponentResourceFactoryImpl() {
        ComponentResourceFactoryImpl factoryImpl = compResFactorySoftRef == null ? null : compResFactorySoftRef.get();
        if (factoryImpl == null) {// if weak ref has not been created or if referenced factory has been GCed then create
            // a new one
            factoryImpl = new ComponentResourceFactoryImpl();
            compResFactorySoftRef = new SoftReference<ComponentResourceFactoryImpl>(factoryImpl);
        }
        return factoryImpl;
    }

    /**
     * return the common ComponentResourceFactoryImpl to retreive component resource from URI
     *
     * @return factoryImpl
     */
    // here we are using soft references so that whenever the GC runs it can collect the ComponentResourceFactoryImpl
    private static Map getLoadingOptionMap() {
        Map optionMap = (optionMapSoftRef == null ? null : optionMapSoftRef.get());
        if (optionMap == null) {// if weak ref has not been created or if referenced factory has been GCed then create
            // a new one
            optionMap = new HashMap();
            optionMap.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
            optionMap.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
            optionMap.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
            optionMap.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap());
            optionMap.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);
            optionMap.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE,Boolean.TRUE);
            optionMapSoftRef = new SoftReference<Map>(optionMap);
        }
        return optionMap;
    }

    /**
     * Sets the visible.
     *
     * @param visible the visible to set
     */
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    /**
     * Sets the technical.
     *
     * @param technical the technical to set
     */
    public void setTechnical(Boolean technical) {
        this.technical = technical;
    }

    @Override
    public String getCombine() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().getCOMBINE();
    }

    @Override
    public IProcess getProcess() {
        // TODO Auto-generated method stub
        return null;
    }

    String bundleName;

    public String getSourceBundleName() {
        return bundleName;
    }

    /**
     * Getter for type.
     *
     * @return the type
     */
    @Override
    public String getType() {
        String type = info.getType();
        if (type == null) {
            return super.getType();
        }
        return type;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#getInputType()
     */
    @Override
    public String getInputType() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().getINPUTTYPE();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#getOutputType()
     */
    @Override
    public String getOutputType() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().getOUTPUTTYPE();
    }

    /**
     * Getter for reduce.
     *
     * @return the reduce
     */
    @Override
    public boolean isReduce() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isREDUCE();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#isSparkAction()
     */
    @Override
    public boolean isSparkAction() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isSPARKACTION();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.designer.core.model.components.AbstractComponent#setPaletteType(java.lang.String)
     */
    @Override
    public void setPaletteType(String paletteType) {
        super.setPaletteType(paletteType);
        if (info != null) {
            info.setType(paletteType);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#getPartitioning()
     */
    @Override
    public String getPartitioning() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().getPARTITIONING();
    }

    /**
     * Getter for provider.
     *
     * @return the provider
     */
    public AbstractComponentsProvider getProvider() {
        return this.provider;
    }

    /**
     * Sets the provider.
     *
     * @param provider the provider to set
     */
    public void setProvider(AbstractComponentsProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean isSupportDbType() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isSUPPORTS_DB_TYPE();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.getPaletteType() == null) ? 0 : this.getPaletteType().hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EmfComponent other = (EmfComponent) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.getPaletteType() == null) {
            if (other.getPaletteType() != null) {
                return false;
            }
        } else if (!this.getPaletteType().equals(other.getPaletteType())) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#isLog4JEnabled()
     */
    @Override
    public boolean isLog4JEnabled() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().isLOG4J_ENABLED();
    }

    public String getEquivalent() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        return compType.getHEADER().getEQUIVALENT();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.components.IComponent#getCONNECTORList()
     */
    @Override
    public EList getCONNECTORList() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }

        EList listConnType = compType.getCONNECTORS().getCONNECTOR();
        return listConnType;
    }

    @Override
    public String toString() {
        return getName() + ":" + getLongName(); //$NON-NLS-1$
    }

    @Override
    public String getTemplateFolder() {
        return getPathSource() + "/" + getName(); //$NON-NLS-1$
    }

    @Override
    public boolean isActiveDbColumns() {
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }
        if (compType != null) {
            HEADERType header = compType.getHEADER();
            if (header != null) {
                return header.isACTIVE_DB_COLUMNS();
            }
        }
        return super.isActiveDbColumns();
    }

    public ComponentInfo getComponentInfo() {
        return info;
    }

    @SuppressWarnings("unchecked")
    public void loadForCacheAtrributes() throws BusinessException {
        info = ComponentCacheFactory.eINSTANCE.createComponentInfo();
        if (compType == null) {
            isLoaded = false;
            try {
                load();
            } catch (BusinessException e) {
                ExceptionHandler.process(e);
            }
        }

        // originalFamilyName
        {
            String originalFamilyName = "";
            int nbTotal = compType.getFAMILIES().getFAMILY().size();
            int nb = 0;
            for (Object objFam : compType.getFAMILIES().getFAMILY()) {
                String curFamily = (String) objFam;
                originalFamilyName += curFamily;
                nb++;
                if (nbTotal != nb) {
                    originalFamilyName += "|"; //$NON-NLS-1$
                }
            }
            info.setOriginalFamilyName(originalFamilyName);
        }

        // pluginExtension
        info.setPluginExtension(compType.getHEADER().getEXTENSION());

        // version
        info.setVersion(String.valueOf(compType.getHEADER().getVERSION()));


        // isTechnical
        info.setIsTechnical(compType.getHEADER().isTECHNICAL());

        // pluginDependencies
        List<String> pluginDependencyList = new ArrayList<>();
        if (compType.getPLUGINDEPENDENCIES() != null) {
            List<PLUGINDEPENDENCYTypeImpl> pti = compType.getPLUGINDEPENDENCIES().getPLUGINDEPENDENCY();
            for (PLUGINDEPENDENCYTypeImpl pt : pti) {
                pluginDependencyList.add(pt.getID());
            }
        }
        info.getPluginDependencies().addAll(pluginDependencyList);

        // componentNames
        List<String> componentList = info.getComponentNames();
        for (IMultipleComponentManager multipleComponentManager : getMultipleComponentManagers()) {
            for (IMultipleComponentItem multipleComponentItem : multipleComponentManager.getItemList()) {
                componentList.add(multipleComponentItem.getComponent());
            }
        }

        // importType (modulesNeeded)
        setImportTypes();

        // isVisibleInComponentDefinition
        info.setIsVisibleInComponentDefinition(compType.getHEADER().isVISIBLE());

        // uriString
        info.setUriString(uriString);

        // pathSource
        info.setPathSource(pathSource);

        // repositoryType
        for (PARAMETERType pType : (List<PARAMETERType>) compType.getPARAMETERS().getPARAMETER()) {
            if (pType.getFIELD().equals("PROPERTY_TYPE")) { //$NON-NLS-1$
                info.setRepositoryType(pType.getREPOSITORYVALUE());
                break;
            }
        }

        // sourceBundleName
        info.setSourceBundleName(bundleName);

        // type
        info.setType(compType.getHEADER().getTYPE());

        // providerId
        info.setProviderId(provider.getId());
    }

}
