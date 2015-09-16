// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.component.core.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.utils.image.ColorUtils;
import org.talend.component.core.i18n.Messages;
import org.talend.components.api.properties.ComponentConnector;
import org.talend.components.api.properties.ComponentDefinition;
import org.talend.components.api.properties.Property;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.component_cache.ComponentInfo;
import org.talend.core.model.components.AbstractComponentsProvider;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.components.EReadOnlyComlumnPosition;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IMultipleComponentItem;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.general.InstallModule;
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
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.temp.ECodePart;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.core.ui.component.settings.ComponentsSettingsHelper;
import org.talend.core.ui.services.IComponentsLocalProviderService;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ITisLocalProviderService;
import org.talend.designer.core.model.components.AbstractComponent;
import org.talend.designer.core.model.components.ComponentBundleToPath;
import org.talend.designer.core.model.components.ComponentFilesNaming;
import org.talend.designer.core.model.components.ComponentIconLoading;
import org.talend.designer.core.model.components.ComponentsProviderManager;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.model.components.ElementParameterDefaultValue;
import org.talend.designer.core.model.components.MultiDefaultValuesUtils;
import org.talend.designer.core.model.components.NodeConnector;
import org.talend.designer.core.model.components.NodeReturn;
import org.talend.designer.core.model.utils.emf.component.COLUMNType;
import org.talend.designer.core.model.utils.emf.component.CONNECTORType;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.component.INSTALLType;
import org.talend.designer.core.model.utils.emf.component.ITEMSType;
import org.talend.designer.core.model.utils.emf.component.ITEMType;
import org.talend.designer.core.model.utils.emf.component.PARAMETERType;
import org.talend.designer.core.model.utils.emf.component.RETURNType;
import org.talend.designer.core.model.utils.emf.component.TABLEType;
import org.talend.designer.core.model.utils.emf.component.util.ComponentResourceFactoryImpl;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;

/**
 * created by hcyi on Sep 10, 2015 Detailled comment
 *
 */
public class Component extends AbstractComponent {

    private static Logger log = Logger.getLogger(Component.class);

    private ComponentDefinition componentDefinition;

    private static final String EQUALS = "=="; //$NON-NLS-1$

    private static final String DEFAULT_COLOR = "255;255;255"; //$NON-NLS-1$

    private static final long serialVersionUID = 1L;

    private String uriString;

    private String name;

    private boolean isLoaded = false;

    private Map<String, ImageDescriptor> imageRegistry = new HashMap<String, ImageDescriptor>();

    public static final String BUILTIN = "BUILT_IN"; //$NON-NLS-1$

    public static final String REPOSITORY = "REPOSITORY"; //$NON-NLS-1$

    public static final String TNS_FILE = "USE_TNS_FILE"; //$NON-NLS-1$

    public static final String TEXT_BUILTIN = Messages.getString("Component.builtIn"); //$NON-NLS-1$

    public static final String TEXT_REPOSITORY = Messages.getString("Component.repository"); //$NON-NLS-1$

    public static final String TEXT_TNS_FILE = Messages.getString("Component.tnsfile"); //$NON-NLS-1$

    private static final String TSTATCATCHER_NAME = "tStatCatcher"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_UTF_8 = "UTF-8"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_ISO_8859_15 = "ISO-8859-15"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_CUSTOM = "CUSTOM"; //$NON-NLS-1$

    private static final String STRING_TYPE = "String"; //$NON-NLS-1$

    private List<IMultipleComponentManager> multipleComponentManagers;

    private static final boolean ADVANCED_PROPERTY = true;

    private String pathSource;

    private List<ECodePart> codePartListX;

    private Boolean useMerge = null;

    private Boolean visible = null;

    private Boolean technical = null;

    private ComponentInfo info;

    private boolean isAlreadyLoad = false;

    // weak ref used so that memory is not used by a static ComponentResourceFactoryImpl instance
    private static SoftReference<ComponentResourceFactoryImpl> compResFactorySoftRef;

    // weak ref used so that memory is not used by a static HashMap instance
    private static SoftReference<Map> optionMapSoftRef;

    private String type;

    private ComponentsProvider provider;

    private String bundleName;

    public Component(ComponentDefinition componentDefinition) throws BusinessException {
        this.componentDefinition = componentDefinition;
        // TODO: default init a component load into palette
        this.name = componentDefinition.getName();
        this.setPaletteType("DI"); //$NON-NLS-1$
        this.pathSource = "components"; //$NON-NLS-1$
        this.bundleName = "org.talend.designer.components.localprovider";//$NON-NLS-1$
        this.uriString = "/components/tSalesforceInput/tSalesforceInput_java.xml";//$NON-NLS-1$
        this.isLoaded = true;
        this.isAlreadyLoad = true;
        this.visible = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getName()
     */
    @Override
    public String getName() {
        return componentDefinition.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getLongName()
     */
    @Override
    public String getLongName() {
        return "Asks a Access database to upload a bulk file into the database defined";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getOriginalFamilyName()
     */
    @Override
    public String getOriginalFamilyName() {
        String[] families = componentDefinition.getSupportedFamilies();
        StringBuffer sb = new StringBuffer();
        for (String familyName : families) {
            if (sb.length() > 0) {
                sb.append("|");//$NON-NLS-1$
            }
            // later no need this
            sb.append("Service/");//$NON-NLS-1$
            sb.append(familyName);
        }
        // return "Service/Business/Salesforce|Service/Cloud/Salesforce";
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getTranslatedFamilyName()
     */
    @Override
    public String getTranslatedFamilyName() {
        String[] families = componentDefinition.getSupportedFamilies();
        StringBuffer sb = new StringBuffer();
        for (String familyName : families) {
            if (sb.length() > 0) {
                sb.append("|");//$NON-NLS-1$
            }
            // later no need this
            sb.append("Service/");//$NON-NLS-1$
            sb.append(familyName);
        }
        // return "Service/Business/Salesforce|Service/Cloud/Salesforce";
        return sb.toString();
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

    public static class ResClassLoader extends ClassLoader {

        public ResClassLoader(ClassLoader parent) {
            super(parent);
        }
    }

    private String getTranslatedValue(final String nameValue) {
        return "org.talend.help.tSalesforceInput";
    }

    @SuppressWarnings("unchecked")
    private void load() throws BusinessException {
        //
    }

    @Override
    public List<ElementParameter> createElementParameters(INode node) {
        List<ElementParameter> listParam;
        listParam = new ArrayList<ElementParameter>();
        // TODO
        addMainParameters(listParam, node);
        // addPropertyParameters(listParam, node, NORMAL_PROPERTY);
        addPropertyParameters(listParam, node, ADVANCED_PROPERTY);
        initializePropertyParameters(listParam);
        checkSchemaParameter(listParam, node);
        addViewParameters(listParam, node);
        addDocParameters(listParam, node);
        addValidationRulesParameters(listParam, node);
        return listParam;
    }

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
    public List<NodeReturn> createReturns() {
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
        // ****************** end of standard returns ******************
        Property[] propertys = componentDefinition.createProperties().getProperties();
        for (Property property : propertys) {
            nodeRet = new NodeReturn();
            nodeRet.setAvailability("");
            nodeRet.setVarName("");
            nodeRet.setDisplayName(property.getDisplayName());
            nodeRet.setName(property.getName());
            nodeRet.setType(property.getTypeName());
            nodeRet.setShowIf("");
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
        parentParam.setValue("");

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

    private String getNodeFormatIdWithoutFormatType(String nodeLabel, String nodeFamily) {
        String ids = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsLocalProviderService.class)) {
            IComponentsLocalProviderService service = (IComponentsLocalProviderService) GlobalServiceRegister.getDefault()
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
        ElementParameter param;
        IPreferenceStore localComponentProviderStore = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsLocalProviderService.class)) {
            IComponentsLocalProviderService service = (IComponentsLocalProviderService) GlobalServiceRegister.getDefault()
                    .getService(IComponentsLocalProviderService.class);
            if (service != null) {
                localComponentProviderStore = service.getPreferenceStore();
            }
        }

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
        param.setValue(""); // TODO
        if (formatId != null) {
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
        param.setValue("");// TODO
        if (formatId != null) {
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
        param.setValue("");// TODO
        if (formatId != null) {
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

    public void addMainParameters(final List<ElementParameter> listParam, INode node) {
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
        param.setValue(""); //$NON-NLS-1$ //TODO
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
                param.setValue(new Boolean("false"));// TODO
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
        param.setCategory(EComponentCategory.ADVANCED);
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
        param.setValue("");// TODO
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
        param.setValue("");// TODO
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
        if (PluginChecker.isTeamEdition() && !"CAMEL".equals(getPaletteType())) {
            boolean defaultParalelize = new Boolean("false");// TODO
            param = new ElementParameter(node);
            param.setReadOnly(!defaultParalelize);
            param.setName(EParameterName.PARALLELIZE.getName());
            param.setValue(Boolean.FALSE);
            param.setDisplayName(EParameterName.PARALLELIZE.getDisplayName());
            param.setFieldType(EParameterFieldType.CHECK);
            param.setCategory(EComponentCategory.ADVANCED);
            param.setNumRow(200);
            param.setShow(true);
            param.setDefaultValue(param.getValue());
            listParam.add(param);

            param = new ElementParameter(node);
            param.setReadOnly(!defaultParalelize);
            param.setName(EParameterName.PARALLELIZE_NUMBER.getName());
            param.setValue("");// TODO
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
            String displayName = getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME); //$NON-NLS-1$
            if (displayName.startsWith("!!")) { //$NON-NLS-1$
                displayName = EParameterName.PROPERTY_TYPE.getDisplayName();
            }
            newParam.setDisplayName(displayName);
            if (node.getComponent() != null && node.getComponent().getName().equals("tOracleConnection")) {
                newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY, TEXT_TNS_FILE });
                newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY, TNS_FILE });
                newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY, TNS_FILE });
            } else {
                newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
                newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY });
                newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
            }

            // if(xmlParam.isUSETNSFILE){
            //
            // }
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
            // listParam.add(newParam);

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
            // listParam.add(newParam);
        }
        if (type == EParameterFieldType.SCHEMA_TYPE) {
            String context = xmlParam.getCONTEXT();
            if (context == null) {
                // by default the schema will be set to the "FLOW" connector.
                context = EConnectionType.FLOW_MAIN.getName();
                if (getOriginalFamilyName().startsWith("ELT")) { //$NON-NLS-1$
                    context = EConnectionType.TABLE.getName();
                }
            }

            boolean useInputLinkSelection = connectorUseInputLinkSelection(context);

            String displayName = getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME); //$NON-NLS-1$
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
                newParam.setDisplayName(Messages.getString("Component.attachedConnection")); //$NON-NLS-1$
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
        }
        if (type == EParameterFieldType.ENCODING_TYPE) {
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
            // listParam.add(newParam);
        }// Ends
        if (type == EParameterFieldType.QUERYSTORE_TYPE) {
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
        }

        if (type == EParameterFieldType.PROCESS_TYPE || type == EParameterFieldType.ROUTE_INPUT_PROCESS_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.PROCESS_TYPE_PROCESS.getName());
            if (getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME).startsWith("!!")) { //$NON-NLS-1$ //$NON-NLS-2$
                newParam.setDisplayName(EParameterName.PROCESS_TYPE_PROCESS.getDisplayName());
            } else {
                newParam.setDisplayName(getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME)); //$NON-NLS-1$
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

        // http://jira.talendforge.org/browse/TESB-6285 Xiaopeng Li
        if (type == EParameterFieldType.ROUTE_RESOURCE_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.ROUTE_RESOURCE_TYPE_ID.getName());
            if (getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME).startsWith("!!")) { //$NON-NLS-1$ //$NON-NLS-2$
                newParam.setDisplayName(EParameterName.ROUTE_RESOURCE_TYPE_ID.getDisplayName());
            } else {
                newParam.setDisplayName(getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME)); //$NON-NLS-1$
            }
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setRequired(false);
            newParam.setParentParameter(parentParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.ROUTE_RESOURCE_TYPE_RES_URI.getName());
            newParam.setDisplayName(EParameterName.ROUTE_RESOURCE_TYPE_RES_URI.getName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setRequired(false);
            newParam.setParentParameter(parentParam);

            // http://jira.talendforge.org/browse/TESB-6481
            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.ROUTE_RESOURCE_TYPE_VERSION.getName());
            newParam.setDisplayName(EParameterName.ROUTE_RESOURCE_TYPE_VERSION.getDisplayName());
            newParam.setListItemsDisplayName(new String[] { "Latest" });
            newParam.setListItemsValue(new String[] { "Latest" });
            newParam.setValue("Latest");
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setFieldType(EParameterFieldType.TECHNICAL);
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setRequired(false);
            newParam.setParentParameter(parentParam);
        }
    }

    @SuppressWarnings("unchecked")
    private void addPropertyParameters(final List<ElementParameter> listParam, final INode node, boolean advanced) {
        ElementParameter param;
        boolean autoSwitchAdded = false;
        Property[] propertys = componentDefinition.createProperties().getProperties();
        for (Property property : propertys) {
            if (!autoSwitchAdded) {
                param = new ElementParameter(node);
                param.setCategory(EComponentCategory.TECHNICAL);
                param.setName(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName());
                param.setDisplayName(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getDisplayName());
                param.setNumRow(1);
                param.setFieldType(EParameterFieldType.CHECK);
                param.setValue(Boolean.FALSE);
                param.setShow(false);
                param.setRequired(true);
                param.setReadOnly(true);
                listParam.add(param);
                autoSwitchAdded = true;
            }
        }

        // TODO to remove later, need to find another way to do this (for feature 18686)
        // adds manually all definitions to avoid to modify the component
        if (ArrayUtils.contains(JavaTypesManager.getJavaTypesLabels(), "Geometry") && "tOracleInput".equals(name)) {
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
                newParam.setShowIf("FORCE_CRS == 'true'");
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue("\"EPSG:4326\"");
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
                newParam.setValue("import org.talend.sdi.geometry.Geometry;");
                listParam.add(newParam);
            }
        }

        if (ArrayUtils.contains(JavaTypesManager.getJavaTypesLabels(), "Geometry") && "tOracleOutput".equals(name)) {
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
                newParam.setShowIf("(TABLE_ACTION=='CREATE') or (TABLE_ACTION=='DROP_CREATE') or"
                        + " (TABLE_ACTION=='CREATE_IF_NOT_EXISTS') or (TABLE_ACTION=='DROP_IF_EXISTS_AND_CREATE')");
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
                newParam.setShowIf("(USE_SPATIAL_OPTIONS == 'true') and ((TABLE_ACTION=='CREATE') or "
                        + "(TABLE_ACTION=='DROP_CREATE') or (TABLE_ACTION=='CREATE_IF_NOT_EXISTS') or"
                        + " (TABLE_ACTION=='DROP_IF_EXISTS_AND_CREATE'))");
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
                newParam.setShowIf("(USE_SPATIAL_OPTIONS == 'true') and ((TABLE_ACTION=='CREATE') or "
                        + "(TABLE_ACTION=='DROP_CREATE') or (TABLE_ACTION=='CREATE_IF_NOT_EXISTS') or"
                        + " (TABLE_ACTION=='DROP_IF_EXISTS_AND_CREATE'))");
                newParam.setNumRow(201);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue("0.001");
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
                newParam.setShowIf("USE_SPATIAL_OPTIONS == 'true'");
                newParam.setNumRow(208);
                newParam.setCategory(EComponentCategory.BASIC);
                newParam.setValue("-1");
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
                newParam.setValue("import org.talend.sdi.geometry.Geometry;");
                listParam.add(newParam);
            }
        }
    }

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
        // TODO
        return "";
    }

    private void initializeTableFromXml(PARAMETERType xmlParam, ElementParameter param) {
        List<TABLEType> tableList = xmlParam.getTABLE();
        if ((tableList == null) || (tableList.size() == 0)) {
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
            }

            defaultTable.setListColumns(talendColumnList);

            // store the default table in default value
            IElementParameterDefaultValue defaultValue = new ElementParameterDefaultValue();
            defaultValue.setDefaultValue(defaultTable);
            defaultValue.setIfCondition(tableType.getIF());
            defaultValue.setNotIfCondition(tableType.getNOTIF());
            param.getDefaultValues().add(defaultValue);

            // param.setValue(defaultTable);
        }
    }

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
                String ifCondition = elementParameterDefaultValue.getIfCondition();
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#hasConditionalOutputs()
     */
    @Override
    public boolean hasConditionalOutputs() {
        return false;
    }

    @Override
    public String getShortName() {
        String originalComponentName = getName();
        String calculatedShortName = "";
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

    public boolean canStart() {
        // TODO
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IComponent#createConnectors()
     */
    @Override
    public List<NodeConnector> createConnectors(INode parentNode) {
        EList listConnType;
        CONNECTORType connType;
        NodeConnector nodeConnector;
        List<NodeConnector> listConnector = new ArrayList<NodeConnector>();
        ComponentConnector[] listConnectors = componentDefinition.getConnectors();
        for (ComponentConnector componentConnector : listConnectors) {
            EConnectionType currentType = EConnectionType.getTypeFromName(componentConnector.getType().name());
            if (currentType == null
                    || ("LOOKUP").equals(componentConnector.getType().name()) || ("MERGE").equals(componentConnector.getType().name())) { //$NON-NLS-1$ //$NON-NLS-2$
                if (currentType == null) {
                    log.warn(Messages.getString("Component.componentNotExist", this.getName() //$NON-NLS-1$
                            , componentConnector.getType().name()));
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

            nodeConnector.setMaxLinkInput(componentConnector.getMaxInput());
            nodeConnector.setMaxLinkOutput(componentConnector.getMaxOutput());
            listConnector.add(nodeConnector);
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
        String pluginFullName = null;
        return pluginFullName;
    }

    @Override
    public boolean isSchemaAutoPropagated() {
        // TODO
        return false;
    }

    @Override
    public boolean isDataAutoPropagated() {
        // TODO
        return false;
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
        // TODO
        return false;
    }

    @Override
    public String getVersion() {
        // TODO
        return "";
    }

    private List<ModuleNeeded> componentImportNeedsList = new ArrayList<ModuleNeeded>();

    private static final String DB_VERSION = "DB_VERSION";

    @Override
    public List<ModuleNeeded> getModulesNeeded() {
        // TODO
        if (componentImportNeedsList != null && componentImportNeedsList.size() > 0) {
            return componentImportNeedsList;
        }
        List<String> moduleNames = new ArrayList<String>();
        List<String> componentList = info.getComponentNames();
        for (IMultipleComponentManager multipleComponentManager : getMultipleComponentManagers()) {
            for (IMultipleComponentItem multipleComponentItem : multipleComponentManager.getItemList()) {
                IComponent component = ComponentsFactoryProvider.getInstance().get(multipleComponentItem.getComponent());
                componentList.add(multipleComponentItem.getComponent());
                if (component == null) {
                    continue;
                }
                for (ModuleNeeded moduleNeeded : component.getModulesNeeded()) {
                    if (!moduleNames.contains(moduleNeeded.getModuleName())) {
                        ModuleNeeded componentImportNeeds = new ModuleNeeded(this.getName(), moduleNeeded.getModuleName(),
                                moduleNeeded.getInformationMsg(), moduleNeeded.isRequired(), moduleNeeded.getInstallURL(),
                                moduleNeeded.getRequiredIf(), moduleNeeded.getMavenUri());
                        componentImportNeeds.setModuleLocaion(moduleNeeded.getModuleLocaion());
                        componentImportNeedsList.add(componentImportNeeds);
                    }
                }
            }
        }

        // TODO to remove later, need to find another way to do this (for feature 18686)
        // adds manually all definitions to avoid to modify the component
        if (ArrayUtils.contains(JavaTypesManager.getJavaTypesLabels(), "Geometry") && "tOracleInput".equals(name)) {
            // <IMPORT NAME="oracle-sdoapi" MODULE="sdoapi.jar" REQUIRED="true" />
            ModuleNeeded componentImportNeeds = new ModuleNeeded("oracle-sdoapi", "sdoapi.jar",
                    Messages.getString("modules.required"), true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="oracle-sdoutil" MODULE="sdoutil.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("oracle-sdoutil", "sdoutil.jar", Messages.getString("modules.required"),
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="jts-1.12" MODULE="jts-1.12.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("jts-1.12", "jts-1.12.jar", Messages.getString("modules.required"), true,
                    new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="org.talend.sdi" MODULE="org.talend.sdi.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("org.talend.sdi", "org.talend.sdi.jar",
                    Messages.getString("modules.required"), true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="Java-DOM4J" MODULE="dom4j-1.6.1.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("Java-DOM4J", "dom4j-1.6.1.jar", Messages.getString("modules.required"),
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="Java-JAXEN" MODULE="jaxen-1.1.1.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("Java-JAXEN", "jaxen-1.1.1.jar", Messages.getString("modules.required"),
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);
        }

        if (ArrayUtils.contains(JavaTypesManager.getJavaTypesLabels(), "Geometry") && "tOracleOutput".equals(name)) {
            // <IMPORT NAME="oracle-sdoapi" MODULE="sdoapi.jar" REQUIRED="true" />
            ModuleNeeded componentImportNeeds = new ModuleNeeded("oracle-sdoapi", "sdoapi.jar",
                    Messages.getString("modules.required"), true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="oracle-sdoutil" MODULE="sdoutil.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("oracle-sdoutil", "sdoutil.jar", Messages.getString("modules.required"),
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="Java-DOM4J" MODULE="dom4j-1.6.1.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("Java-DOM4J", "dom4j-1.6.1.jar", Messages.getString("modules.required"),
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);

            // <IMPORT NAME="Java-JAXEN" MODULE="jaxen-1.1.1.jar" REQUIRED="true" />
            componentImportNeeds = new ModuleNeeded("Java-JAXEN", "jaxen-1.1.1.jar", Messages.getString("modules.required"),
                    true, new ArrayList<String>(), null, null);
            componentImportNeedsList.add(componentImportNeeds);
        }

        return componentImportNeedsList;
    }

    protected void initBundleID(IMPORTType importType, ModuleNeeded componentImportNeeds) {
        String bundleID = importType.getBundleID();
        if (bundleID != null) {
            String bundleName = null;
            String bundleVersion = null;
            if (bundleID.contains(":")) {
                String[] nameAndVersion = bundleID.split(":");
                bundleName = nameAndVersion[0];
                bundleVersion = nameAndVersion[1];
            } else {
                bundleName = bundleID;
            }
            componentImportNeeds.setBundleName(bundleName);
            componentImportNeeds.setBundleVersion(bundleVersion);
        }
    }

    public List<String> getInstallURL(IMPORTType importType) {
        List<String> list = new ArrayList<String>();
        EList emfInstall = importType.getURL();
        for (int j = 0; j < emfInstall.size(); j++) {
            String installtype = (String) emfInstall.get(j);
            list.add(installtype);
        }
        return list;
    }

    public List<InstallModule> getInstallCommand(IMPORTType importType) {
        List<InstallModule> list = new ArrayList<InstallModule>();
        EList emfInstall = importType.getINSTALL();
        for (int j = 0; j < emfInstall.size(); j++) {
            INSTALLType installtype = (INSTALLType) emfInstall.get(j);
            InstallModule installModuleNeeds = new InstallModule(installtype.getOS(), installtype.getCOMMAND());
            list.add(installModuleNeeds);
        }
        return list;
    }

    @Override
    public List<IMultipleComponentManager> getMultipleComponentManagers() {
        if (multipleComponentManagers == null) {
            multipleComponentManagers = createMultipleComponentManagerFromTemplates();
        }// else already exist so return it
        return multipleComponentManagers;
    }

    private ArrayList<IMultipleComponentManager> createMultipleComponentManagerFromTemplates() {
        // TODO
        ArrayList<IMultipleComponentManager> theMultipleComponentManagers = new ArrayList<IMultipleComponentManager>();
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
        if (!this.imageRegistry.containsKey(getName() + "_16")) {
            String path = new Path(ComponentBundleToPath.getPathFromBundle(bundleName)).append(this.pathSource).append(this.name)
                    .toPortableString();
            ComponentIconLoading cil = new ComponentIconLoading(imageRegistry, new File(path));

            // only call to initialize the icons in the registry
            cil.getImage32();
            cil.getImage16();
            cil.getImage24();
        }
        return this.imageRegistry.get(getName() + "_16");
    }

    /**
     * Getter for icon24.
     * 
     * @return the icon24
     */
    @Override
    public ImageDescriptor getIcon24() {
        if (!this.imageRegistry.containsKey(getName() + "_24")) {
            String path = new Path(ComponentBundleToPath.getPathFromBundle(bundleName)).append(this.pathSource).append(this.name)
                    .toPortableString();
            ComponentIconLoading cil = new ComponentIconLoading(imageRegistry, new File(path));

            // only call to initialize the icons in the registry
            cil.getImage32();
            cil.getImage16();
            cil.getImage24();
        }
        return this.imageRegistry.get(getName() + "_24");
    }

    /**
     * Getter for icon32.
     * 
     * @return the icon32
     */
    @Override
    public ImageDescriptor getIcon32() {
        if (!this.imageRegistry.containsKey(getName() + "_32")) {
            String path = new Path(ComponentBundleToPath.getPathFromBundle(bundleName)).append(this.pathSource).append(this.name)
                    .toPortableString();
            ComponentIconLoading cil = new ComponentIconLoading(imageRegistry, new File(path));

            // only call to initialize the icons in the registry
            cil.getImage32();
            cil.getImage16();
            cil.getImage24();
        }
        return this.imageRegistry.get(getName() + "_32");
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
        String applicationPath;
        try {
            applicationPath = FileLocator.getBundleFile(Platform.getBundle(bundleName)).getPath();
            applicationPath = (new Path(applicationPath)).toPortableString();
        } catch (IOException e2) {
            ExceptionHandler.process(e2);
            return (ArrayList<ECodePart>) Collections.EMPTY_LIST;
        }
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
    @SuppressWarnings("unchecked")
    public List<String> getPluginDependencies() {
        // TODO
        List<String> pluginDependencyList = new ArrayList<String>();
        return pluginDependencyList;
    }

    @Override
    public boolean useMerge() {
        // TODO
        return useMerge;
    }

    public boolean useFlow() {
        // TODO
        boolean useFlow = false;
        return useFlow;
    }

    public boolean useSchema() {
        // TODO
        boolean useSchema = false;
        return useSchema;
    }

    @Override
    public boolean isMultiplyingOutputs() {
        // TODO
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getComponentType()
     */
    @Override
    public boolean isMultipleOutput() {
        // TODO
        return false;
    }

    public boolean isMultiSchemaOutput() {
        // TODO
        return false;
    }

    private boolean connectorUseInputLinkSelection(String name) {
        // TODO
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#useLookup()
     */
    @Override
    public boolean useLookup() {
        // TODO
        return false;
    }

    @Override
    public boolean useImport() {
        // TODO
        return false;
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
        // TODO
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isTechnical()
     */
    @Override
    public boolean isTechnical() {
        boolean isTrchnical = false;
        return isTrchnical;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        // TODO
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isMainCodeCalled()
     */
    @Override
    public boolean isMainCodeCalled() {
        // TODO
        return false;
    }

    /**
     * get this component's repository type <br>
     * see <PARAMETER NAME="PROPERTY" ...> in the component's xml definition.
     * 
     * @return
     */
    @Override
    public String getRepositoryType() {
        // TODO
        Property[] propertys = componentDefinition.createProperties().getProperties();
        for (Property property : propertys) {
        }
        return null;
    }

    @Override
    public boolean canParallelize() {
        // TODO
        return false;
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
        // TODO
        return "";
    }

    @Override
    public IProcess getProcess() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    @Override
    public String getType() {
        return "DI";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getInputType()
     */
    @Override
    public String getInputType() {
        // TODO
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getOutputType()
     */
    @Override
    public String getOutputType() {
        // TODO
        return "";
    }

    /**
     * Getter for reduce.
     * 
     * @return the reduce
     */
    @Override
    public boolean isReduce() {
        // TODO
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isSparkAction()
     */
    @Override
    public boolean isSparkAction() {
        // TODO
        return false;
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
        // TODO
        return "";
    }

    /**
     * Getter for provider.
     * 
     * @return the provider
     */
    public ComponentsProvider getProvider() {
        return this.provider;
    }

    /**
     * Sets the provider.
     * 
     * @param provider the provider to set
     */
    public void setProvider(ComponentsProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean isSupportDbType() {
        // TODO
        return false;
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
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
        Component other = (Component) obj;
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
        // TODO
        return false;
    }

    public String getEquivalent() {
        // TODO
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getCONNECTORList()
     */
    @Override
    public EList getCONNECTORList() {
        // TODO
        return null;
    }

}
