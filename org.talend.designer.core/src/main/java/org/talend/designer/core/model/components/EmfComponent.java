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
package org.talend.designer.core.model.components;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.system.EnvironmentUtils;
import org.talend.core.CorePlugin;
import org.talend.core.PluginChecker;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.components.EReadOnlyComlumnPosition;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IMultipleComponentItem;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.general.InstallModule;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnectionProperty;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.properties.ComponentSetting;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.temp.ECodePart;
import org.talend.core.model.utils.SQLPatternUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.components.IComponentsLocalProviderService;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.utils.emf.component.ADVANCEDPARAMETERSType;
import org.talend.designer.core.model.utils.emf.component.ARGType;
import org.talend.designer.core.model.utils.emf.component.COLUMNType;
import org.talend.designer.core.model.utils.emf.component.COMPONENTType;
import org.talend.designer.core.model.utils.emf.component.CONNECTORType;
import org.talend.designer.core.model.utils.emf.component.DEFAULTType;
import org.talend.designer.core.model.utils.emf.component.DocumentRoot;
import org.talend.designer.core.model.utils.emf.component.FORMATType;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.component.INSTALLType;
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
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.ExternalNodesFactory;

/**
 * 
 * Component manager that read each information in a xml file with Emf. <br/>
 * $Id$
 */
public class EmfComponent implements IComponent {

    private static Logger log = Logger.getLogger(EmfComponent.class);

    private static final String EQUALS = "=="; //$NON-NLS-1$

    private static final String DEFAULT_COLOR = "255;255;255"; //$NON-NLS-1$

    private final File file;

    private static final long serialVersionUID = 1L;

    private Boolean isLoaded = null;

    private COMPONENTType compType;

    private ImageDescriptor icon32;

    private ImageDescriptor icon24;

    private ImageDescriptor icon16;

    private final ECodeLanguage codeLanguage;

    public static final String BUILTIN = "BUILT_IN"; //$NON-NLS-1$

    public static final String REPOSITORY = "REPOSITORY"; //$NON-NLS-1$

    public static final String TNS_FILE = "USE_TNS_FILE"; //$NON-NLS-1$

    public static final String TEXT_BUILTIN = Messages.getString("EmfComponent.builtIn"); //$NON-NLS-1$

    public static final String TEXT_REPOSITORY = Messages.getString("EmfComponent.repository"); //$NON-NLS-1$

    public static final String TEXT_TNS_FILE = Messages.getString("EmfComponent.tnsfile"); //$NON-NLS-1$

    private static final String TSTATCATCHER_NAME = "tStatCatcher"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_UTF_8 = "UTF-8"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_ISO_8859_15 = "ISO-8859-15"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_CUSTOM = "CUSTOM"; //$NON-NLS-1$

    private static final String STRING_TYPE = "String"; //$NON-NLS-1$

    private List<IMultipleComponentManager> multipleComponentManagers = new ArrayList<IMultipleComponentManager>();

    private static final boolean ADVANCED_PROPERTY = true;

    private static final boolean NORMAL_PROPERTY = false;

    private ResourceBundle resourceBundle;

    private String pathSource;

    private List<ECodePart> codePartList;

    private Boolean useMerge = null;

    private Boolean useLookup = null;

    private Boolean useImport = null;

    private Boolean visible = null;

    private Boolean technical = null;

    // weak ref used so that memory is not used by a static ComponentResourceFactoryImpl instance
    private static WeakReference<ComponentResourceFactoryImpl> compResFactoryWeakRef;

    // weak ref used so that memory is not used by a static HashMap instance
    private static WeakReference<Map> optionMapWeakRef;

    public EmfComponent(File file, String pathSource) throws BusinessException {
        this.file = file;
        this.pathSource = pathSource;
        load();
        codeLanguage = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject()
                .getLanguage();
    }

    public ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    private String getTranslatedValue(final String nameValue) {
        String returnValue = nameValue;
        // modified by wzhang. update translations for components.
        returnValue = Messages.getString(nameValue, this.getName(), getResourceBundle());
        return returnValue;
    }

    @SuppressWarnings("unchecked")
    private void load() throws BusinessException {
        if (isLoaded == null) {
            try {
                URI createURI = URI.createURI(file.toURI().toString());
                Resource res = getComponentResourceFactoryImpl().createResource(createURI);
                res.load(getLoadingOptionMap());

                DocumentRoot xmlDoc;
                xmlDoc = (DocumentRoot) res.getContents().get(0);

                compType = xmlDoc.getCOMPONENT();

                loadMultipleComponentManagerFromTemplates();

                // just load the externalNode plugin to check if the plugin
                // exists.
                if (compType.getHEADER().getEXTENSION() != null) {
                    ExternalNodesFactory.getInstance(this.getPluginFullName());
                }

                checkAvailableCodeParts();

                if (compType.getFAMILIES() == null) {
                    throw new Exception("FAMILIES definition missing in the component"); //$NON-NLS-1$
                }
                if (compType.getFAMILIES().getFAMILY().isEmpty()) {
                    throw new Exception("FAMILIES definition is empty in the component"); //$NON-NLS-1$
                }

                isLoaded = true;
            } catch (Exception e) {
                isLoaded = false;
                throw new BusinessException(Messages.getString("EmfComponent.0", file.getName()), e); //$NON-NLS-1$
            }
        }
    }

    public List<ElementParameter> createElementParameters(INode node) {
        List<ElementParameter> listParam;
        listParam = new ArrayList<ElementParameter>();
        addMainParameters(listParam, node);
        addPropertyParameters(listParam, node, NORMAL_PROPERTY);
        addPropertyParameters(listParam, node, ADVANCED_PROPERTY);
        initializePropertyParameters(listParam);
        checkSchemaParameter(listParam, node);
        addViewParameters(listParam, node);
        addDocParameters(listParam, node);
        addSqlPatternParameters(listParam, node);
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
            if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
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
            parentParam.setField(EParameterFieldType.SCHEMA_TYPE);
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
            newParam.setField(EParameterFieldType.TECHNICAL);
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
            newParam.setField(EParameterFieldType.TECHNICAL);
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setShow(false);
            newParam.setRequired(true);
            newParam.setContext(context);
            newParam.setParentParameter(parentParam);
        }

    }

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

        if (codeLanguage.equals(ECodeLanguage.PERL)) {
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
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.DOC);
        param.setNumRow(1);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.COMMENT.getName());
        param.setValue(""); //$NON-NLS-1$
        param.setDisplayName(EParameterName.COMMENT.getDisplayName());
        param.setField(EParameterFieldType.MEMO);
        param.setNbLines(10);
        param.setCategory(EComponentCategory.DOC);
        param.setNumRow(2);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        listParam.add(param);
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
        param.setField(EParameterFieldType.TEXT);
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
        param.setField(EParameterFieldType.TABLE);
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

        IElementParameter sqlPatternValue = param;

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
        newParam.setField(EParameterFieldType.CLOSED_LIST);
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

    private final IPreferenceStore store = CorePlugin.getDefault().getComponentsLocalProviderService().getPreferenceStore();

    /**
     * yzhang Comment method "getNodeFormatId".
     * 
     * @param nodeLabel
     * @param nodeFamily
     * @return
     */
    private String getNodeFormatIdWithoutFormatType(String nodeLabel, String nodeFamily) {

        String ids = store.getString(IComponentsLocalProviderService.FORMAT_IDS);
        String[] idArray = null;
        if (ids != "") { //$NON-NLS-1$
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

        FORMATType formatTypeInXML = compType.getHEADER().getFORMAT();

        // qli modified to fix the bug 7074.
        String formatId = getNodeFormatIdWithoutFormatType(getName(), getTranslatedFamilyName());

        param = new ElementParameter(node);
        param.setName(EParameterName.LABEL.getName());
        param.setDisplayName(EParameterName.LABEL.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.VIEW);
        param.setNumRow(1);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        if (formatTypeInXML != null) {
            param.setValue(formatTypeInXML.getLABEL());
        } else if (formatId != null) {
            String label = store.getString(formatId + IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL);
            if (!"".equals(label)) { //$NON-NLS-1$
                param.setValue(label);
            }
        } else {
            // in case label/format is not set in the preferences.
            String label = DesignerPlugin.getDefault().getPreferenceStore().getString(TalendDesignerPrefConstants.DEFAULT_LABEL);
            if (!"".equals(label)) { //$NON-NLS-1$
                param.setValue(label);
            }
        }

        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.HINT.getName());
        param.setDisplayName(EParameterName.HINT.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.VIEW);
        param.setNumRow(2);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        if (formatTypeInXML != null) {
            param.setValue(formatTypeInXML.getHINT());
        } else if (formatId != null) {
            String label = store.getString(formatId + IComponentsLocalProviderService.PREFERENCE_TYPE_HINT);
            if (!"".equals(label)) { //$NON-NLS-1$
                param.setValue(label);
            }
        } else {
            // in case hint/format is not set in the preferences.
            String label = DesignerPlugin.getDefault().getPreferenceStore().getString(TalendDesignerPrefConstants.DEFAULT_HINT);
            if (!"".equals(label)) { //$NON-NLS-1$
                param.setValue(label);
            }
        }

        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.CONNECTION_FORMAT.getName());
        param.setDisplayName(EParameterName.CONNECTION_FORMAT.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.VIEW);
        param.setNumRow(3);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        if (formatTypeInXML != null) {
            param.setValue(formatTypeInXML.getCONNECTION());
        } else if (formatId != null) {
            String label = store.getString(formatId + IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION);
            if (!"".equals(label)) { //$NON-NLS-1$
                param.setValue(label);
            }
        }
        listParam.add(param);

        // Remove show hint item in view section for feature 2389.
        // param = new ElementParameter(node);
        // param.setName(EParameterName.SHOW_HINT.getName());
        // param.setValue(new Boolean(false));
        // param.setDisplayName(EParameterName.SHOW_HINT.getDisplayName());
        // param.setField(EParameterFieldType.CHECK);
        // param.setCategory(EComponentCategory.VIEW);
        // param.setNumRow(4);
        // param.setReadOnly(false);
        // param.setRequired(false);
        // param.setShow(true);
        // listParam.add(param);
    }

    public void addMainParameters(final List<ElementParameter> listParam, INode node) {
        ElementParameter param;
        param = new ElementParameter(node);
        param.setName(EParameterName.UNIQUE_NAME.getName());
        param.setValue(""); //$NON-NLS-1$
        param.setDisplayName(EParameterName.UNIQUE_NAME.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.COMPONENT_NAME.getName());
        param.setValue(getName());
        param.setDisplayName(EParameterName.COMPONENT_NAME.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.VERSION.getName());
        param.setValue(compType.getHEADER().getVERSION() + " (" + compType.getHEADER().getSTATUS() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        //$NON-NLS-1$ //$NON-NLS-2$
        param.setDisplayName(EParameterName.VERSION.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(2);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.FAMILY.getName());
        param.setValue(getOriginalFamilyName());
        param.setDisplayName(EParameterName.FAMILY.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(3);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);
        /*
         * param = new ElementParameter(node); param.setName(EParameterName.LOG.getName()); param.setValue("");
         * //$NON-NLS-1$ param.setDisplayName(EParameterName.LOG.getDisplayName()); param .setListItemsValue(new
         * String[] { "NONE", "COUNT", "TIME", "PERF" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ param
         * .setListItemsDisplayName(new String[] { Messages.getString("EmfComponent.None"),
         * Messages.getString("EmfComponent.Count"), //$NON-NLS-1$ //$NON-NLS-2$
         * Messages.getString("EmfComponent.Time"), Messages.getString("EmfComponent.Perf") }); //$NON-NLS-1$
         * //$NON-NLS-2$ param.setField(EParameterFieldType.CLOSED_LIST); param.setCategory(EComponentCategory.MAIN);
         * param.setNumRow(4); param.setReadOnly(false); param.setRequired(false); param.setShow(false);
         * listParam.add(param);
         */
        if (canStart()) {
            param = new ElementParameter(node);
            param.setName(EParameterName.START.getName());
            param.setValue(new Boolean(false));
            param.setDisplayName(EParameterName.START.getDisplayName());
            param.setField(EParameterFieldType.CHECK);
            param.setCategory(EComponentCategory.ADVANCED);
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
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(5);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.ACTIVATE.getName());
        param.setValue(new Boolean(true));
        param.setDisplayName(EParameterName.ACTIVATE.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.TECHNICAL);
        param.setNumRow(5);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.DUMMY.getName());
        param.setValue(Boolean.FALSE);
        param.setDisplayName(EParameterName.DUMMY.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(5);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        boolean tStatCatcherAvailable = ComponentsFactoryProvider.getInstance().get(TSTATCATCHER_NAME) != null;
        param = new ElementParameter(node);
        param.setName(EParameterName.TSTATCATCHER_STATS.getName());
        param.setValue(new Boolean(compType.getHEADER().isTSTATCATCHERSTATS()));
        param.setDisplayName(EParameterName.TSTATCATCHER_STATS.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(99);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(tStatCatcherAvailable);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.HELP.getName());
        param.setValue(getTranslatedValue(PROP_HELP));
        param.setDisplayName(EParameterName.HELP.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(6);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.UPDATE_COMPONENTS.getName());
        param.setValue(new Boolean(false));
        param.setDisplayName(EParameterName.UPDATE_COMPONENTS.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(5);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.CURRENT_OS.getName());
        param.setValue(EnvironmentUtils.getEnvOs());
        param.setDisplayName(EParameterName.CURRENT_OS.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(false);
        listParam.add(param);
        //
        param = new ElementParameter(node);
        param.setName(EParameterName.IREPORT_PATH.getName());
        param.setCategory(EComponentCategory.ADVANCED);
        param.setField(EParameterFieldType.DIRECTORY);
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
        param.setField(EParameterFieldType.DIRECTORY);
        param.setDisplayName(EParameterName.JAVA_LIBRARY_PATH.getDisplayName());
        param.setNumRow(99);
        param.setShow(false);
        param.setValue(CorePlugin.getDefault().getLibrariesService().getJavaLibrariesPath());
        param.setReadOnly(true);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.SUBJOB_COLOR.getName());
        param.setValue(compType.getHEADER().getSUBJOBCOLOR());
        param.setDisplayName(EParameterName.SUBJOB_COLOR.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(99);
        param.setReadOnly(true);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.SUBJOB_TITLE_COLOR.getName());
        param.setValue(compType.getHEADER().getSUBJOBTITLECOLOR());
        param.setDisplayName(EParameterName.SUBJOB_TITLE_COLOR.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.ADVANCED);
        param.setNumRow(99);
        param.setReadOnly(true);
        param.setShow(false);
        listParam.add(param);

        // These parameters is only work when TIS is loaded
        if (PluginChecker.isTIS()) {
            boolean defaultParalelize = new Boolean(compType.getHEADER().isPARALLELIZE());
            param = new ElementParameter(node);
            param.setReadOnly(!defaultParalelize);
            param.setName(EParameterName.PARALLELIZE.getName());
            param.setValue(Boolean.FALSE);
            param.setDisplayName(EParameterName.PARALLELIZE.getDisplayName());
            param.setField(EParameterFieldType.CHECK);
            param.setCategory(EComponentCategory.ADVANCED);
            param.setNumRow(100);
            param.setShow(true);
            listParam.add(param);

            param = new ElementParameter(node);
            param.setReadOnly(!defaultParalelize);
            param.setName(EParameterName.PARALLELIZE_NUMBER.getName());
            param.setValue(compType.getHEADER().getNUMBERPARALLELIZE());
            param.setDisplayName(EParameterName.PARALLELIZE_NUMBER.getDisplayName());
            param.setField(EParameterFieldType.TEXT);
            param.setCategory(EComponentCategory.ADVANCED);
            param.setNumRow(100);
            param.setShowIf(EParameterName.PARALLELIZE.getName() + " == 'true'"); //$NON-NLS-1$
            listParam.add(param);
        }

    }

    private void createSpecificParametersFromType(final List<ElementParameter> listParam, final PARAMETERType xmlParam,
            final INode node, final EParameterFieldType type, ElementParameter parentParam) {
        if (type == EParameterFieldType.PROPERTY_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.BASIC);
            newParam.setName(EParameterName.PROPERTY_TYPE.getName());
            newParam.setDisplayName(EParameterName.PROPERTY_TYPE.getDisplayName());
            if (node.getComponent() != null) {
                if (node.getComponent().getName().equals("tOracleConnection")
                        && LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
                    newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY, TEXT_TNS_FILE });
                    newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY, TNS_FILE });
                    newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY, TNS_FILE });
                } else {
                    newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
                    newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY });
                    newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
                }
            }
            // if(xmlParam.isUSETNSFILE){
            //
            // }
            newParam.setValue(BUILTIN);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setField(EParameterFieldType.TECHNICAL);
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
            newParam.setField(EParameterFieldType.TECHNICAL);
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
            newParam.setField(EParameterFieldType.TECHNICAL);
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
            newParam.setField(EParameterFieldType.TECHNICAL);
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
                newParam.setField(EParameterFieldType.CONNECTION_LIST);
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
            newParam.setField(EParameterFieldType.TECHNICAL);
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
            newParam.setField(EParameterFieldType.TECHNICAL);
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
            newParam.setField(EParameterFieldType.TECHNICAL);
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setShow(false);
            newParam.setRequired(true);
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            newParam.setParentParameter(parentParam);
            // listParam.add(newParam);
        }

        if (type == EParameterFieldType.PROCESS_TYPE) {
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
            newParam.setField(EParameterFieldType.TECHNICAL);
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
            newParam.setField(EParameterFieldType.TECHNICAL);
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
            newParam.setField(EParameterFieldType.TECHNICAL);
            newParam.setValue(""); //$NON-NLS-1$
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setRequired(true);
            newParam.setParentParameter(parentParam);
        }
    }

    @SuppressWarnings("unchecked")
    private void addPropertyParameters(final List<ElementParameter> listParam, final INode node, boolean advanced) {
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

            if (type.equals(EParameterFieldType.SCHEMA_TYPE)) {
                if (!autoSwitchAdded) {
                    param = new ElementParameter(node);
                    param.setCategory(EComponentCategory.TECHNICAL);
                    param.setName(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName());
                    param.setDisplayName(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getDisplayName());
                    param.setNumRow(xmlParam.getNUMROW());
                    param.setField(EParameterFieldType.CHECK);
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
            param.setGroupDisplayName(getTranslatedValue(xmlParam.getGROUP() + "." + PROP_NAME));//$NON-NLS-1$
            param.setField(type);
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
            param.setNotShowIf(xmlParam.getNOTSHOWIF());
            param.setReadOnlyIf(xmlParam.getREADONLYIF());
            param.setNotReadOnlyIf(xmlParam.getNOTREADONLYIF());
            param.setRepositoryValue(xmlParam.getREPOSITORYVALUE());
            param.setGroup(xmlParam.getGROUP());
            param.setContext(xmlParam.getCONTEXT());
            param.setBackgroundColor(getColor(param, xmlParam.getBACKGROUND()));
            param.setColor(getColor(param, xmlParam.getCOLOR()));
            param.setContextMode(xmlParam.isCONTEXTMODE());
            param.setNoContextAssist(xmlParam.isNOCONTEXTASSIST());

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
            case SCHEMA_TYPE:
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
            case JAVA_COMMAND:
                param.setValue("");
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
            default:
                param.setValue(""); //$NON-NLS-1$
            }

            if (!param.getField().equals(EParameterFieldType.TABLE) && !param.getField().equals(EParameterFieldType.CLOSED_LIST)
                    && !param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                List<DEFAULTType> listDefault = xmlParam.getDEFAULT();
                for (DEFAULTType defaultType : listDefault) {
                    IElementParameterDefaultValue defaultValue = new ElementParameterDefaultValue();

                    if (node.getProcess() != null) {
                        defaultValue.setDefaultValue(ElementParameterParser.parse(node.getProcess(), defaultType.getValue()));
                        if (param.getField() == EParameterFieldType.FILE || param.getField() == EParameterFieldType.DIRECTORY) {
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
                    newParam.setName(EParameterName.BASED_ON_INPUT_SCHEMAS.getName()); //$NON-NLS-1$
                    newParam.setDisplayName(EParameterName.BASED_ON_INPUT_SCHEMAS.getDisplayName()); //$NON-NLS-1$
                    newParam.setField(EParameterFieldType.TEXT);
                    newParam.setCategory(EComponentCategory.BASIC);
                    newParam.setShow(false);
                    newParam.setValue(String.valueOf(items.isBASEDONINPUTSCHEMAS())); //$NON-NLS-1$  
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
            return TalendTextUtils.stringToRGB(color);
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

        for (TABLEType tableType : tableList) {
            IMetadataTable defaultTable = new MetadataTable();
            EList xmlColumnList = tableType.getCOLUMN();
            COLUMNType xmlColumn;
            List<IMetadataColumn> talendColumnList = new ArrayList<IMetadataColumn>();
            IMetadataColumn talendColumn;

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

    /**
     * DOC nrousseau Comment method "initializePropertyParameters".
     * 
     * @param listParam
     */
    private void initializePropertyParameters(List<ElementParameter> listParam) {
        for (ElementParameter param : listParam) {
            if (param.getDefaultValues().size() > 0) {
                boolean isSet = false;
                if (param.getField().equals(EParameterFieldType.COMMAND)) {
                    // convert the values of COMMMAND
                    param.setValue(MultiDefaultValuesUtils.convertDefaultValues(param));
                    continue;
                }
                for (IElementParameterDefaultValue defaultValue : param.getDefaultValues()) {
                    String conditionIf = defaultValue.getIfCondition();
                    String conditionNotIf = defaultValue.getNotIfCondition();

                    if (param.isCondition(conditionIf, conditionNotIf, listParam)) {
                        isSet = true;
                        if (param.getField().equals(EParameterFieldType.RADIO)
                                || param.getField().equals(EParameterFieldType.CHECK)
                                || param.getField().equals(EParameterFieldType.AS400_CHECK)) {
                            param.setValue(new Boolean(defaultValue.getDefaultValue().toString()));
                        } else {
                            param.setValue(defaultValue.getDefaultValue());
                        }
                    }
                }
                if (!isSet) {
                    if (param.getField().equals(EParameterFieldType.RADIO) || param.getField().equals(EParameterFieldType.CHECK)
                            || param.getField().equals(EParameterFieldType.AS400_CHECK)) {
                        int index = this.computeIndex(listParam, param);
                        if (index >= 0) {
                            param.setValue(new Boolean(param.getDefaultValues().get(index).getDefaultValue().toString()));
                        }
                    } else {
                        int index = this.computeIndex(listParam, param);
                        if (index >= 0) {
                            Object defaultValue = param.getDefaultValues().get(index).getDefaultValue();
                            param.setValue(defaultValue);
                            if (param.getField() == EParameterFieldType.ENCODING_TYPE) {
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

        if (EParameterFieldType.MAPPING_TYPE.equals(param.getField())) {
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
            if (!param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
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
                        newParam.setField(EParameterFieldType.TEXT);
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
                        newParam.setField(EParameterFieldType.TEXT);
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
            if (type != EParameterFieldType.TABLE) {
                listItemsValue[k] = item.getVALUE();
            } else {
                EParameterFieldType currentField = EParameterFieldType.getFieldTypeByName(item.getFIELD());
                newParam = new ElementParameter(node);
                newParam.setName(item.getNAME());
                newParam.setFilter(item.getFILTER());
                newParam.setDisplayName(""); //$NON-NLS-1$
                newParam.setField(currentField);
                newParam.setContext(item.getCONTEXT());
                newParam.setShowIf(item.getSHOWIF());
                newParam.setNotShowIf(item.getNOTSHOWIF());
                newParam.setReadOnlyIf(item.getREADONLYIF());
                newParam.setNotReadOnlyIf(item.getNOTREADONLYIF());
                newParam.setNoContextAssist(item.isNOCONTEXTASSIST());
                if (item.isSetREADONLY()) {
                    newParam.setReadOnly(item.isREADONLY());
                }
                switch (currentField) {
                case CLOSED_LIST:
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
                case SCHEMA_XPATH_QUERYS:
                    newParam.setValue(""); //$NON-NLS-1$
                    break;
                case RULE_TYPE: // hywang add for feature 6484
                    newParam.setField(EParameterFieldType.RULE_TYPE);
                    break;
                default: // TEXT by default
                    newParam.setField(EParameterFieldType.TEXT);
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
        if (type != EParameterFieldType.TABLE) {
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

    public String getOriginalFamilyName() {
        String originalFamilyName = ""; //$NON-NLS-1$

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

        return originalFamilyName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getTranslatedFamilyName()
     */
    public String getTranslatedFamilyName() {
        String translatedFamilyName = ""; //$NON-NLS-1$

        IComponentsFactory factory = ComponentsFactoryProvider.getInstance();

        int nbTotal = compType.getFAMILIES().getFAMILY().size();
        int nb = 0;
        for (Object objFam : compType.getFAMILIES().getFAMILY()) {

            String curFamily = (String) objFam;
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
    public boolean hasConditionalOutputs() {
        return compType.getHEADER().isHASCONDITIONALOUTPUTS();
    }

    public String getName() {
        return file.getParentFile().getName();
    }

    // if doesn't exist, return by default the name of the component.
    public String getShortName() {
        if (!StringUtils.isEmpty(compType.getHEADER().getSHORTNAME())) {
            return compType.getHEADER().getSHORTNAME();
        } else {
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
    }

    public String getLongName() {
        return getTranslatedValue(PROP_LONG_NAME);
    }

    public boolean canStart() {
        return compType.getHEADER().isSTARTABLE();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IComponent#createConnectors()
     */
    public List<NodeConnector> createConnectors(INode parentNode) {
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
            if (notShowIf != null && !("".equals(notShowIf))) {
                nodeConnector.setNotShowIf(notShowIf);
            }

            String showIf = connType.getSHOWIF();
            if (showIf != null && !("".equals(showIf))) {
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
                currentType = EConnectionType.FLOW_MERGE;

                if (connType.getCOLOR() == null) {
                    rgb = currentType.getRGB();
                }
                if (!connType.isSetLINESTYLE()) {
                    lineStyle = currentType.getDefaultLineStyle();
                }
                nodeConnector.addConnectionProperty(currentType, rgb, lineStyle);
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

    public String getPluginFullName() {
        String pluginFullName;

        pluginFullName = compType.getHEADER().getEXTENSION();
        if (pluginFullName == null) {
            pluginFullName = IComponentsFactory.COMPONENTS_LOCATION;
        }

        return pluginFullName;
    }

    public boolean isSchemaAutoPropagated() {
        return compType.getHEADER().isSCHEMAAUTOPROPAGATE();
    }

    public boolean isDataAutoPropagated() {
        return compType.getHEADER().isDATAAUTOPROPAGATE();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isVisible()
     */
    public boolean isVisible() {
        return isVisible(null);
    }

    public boolean isVisible(String family) {
        if (visible != null) {
            return visible;
        }
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();

        List<ComponentSetting> components = project.getEmfProject().getComponentsSettings();
        for (ComponentSetting componentSetting : components) {

            if (componentSetting.getFamily() != null && componentSetting.getFamily().equals(family)
                    && componentSetting.getName().equals(getName())) {
                return !componentSetting.isHidden();
            }

        }

        // return compType.getHEADER().isVISIBLE();
        return true;
    }

    public boolean isVisibleInComponentDefinition() {
        if (visible != null) {
            return visible;
        }
        return compType.getHEADER().isVISIBLE();
    }

    public String getVersion() {
        return String.valueOf(compType.getHEADER().getVERSION());
    }

    public List<ModuleNeeded> getModulesNeeded() {
        List<String> moduleNames = new ArrayList<String>();
        List<ModuleNeeded> componentImportNeedsList = new ArrayList<ModuleNeeded>();
        if (compType.getCODEGENERATION().getIMPORTS() != null) {
            EList emfImportList;
            emfImportList = compType.getCODEGENERATION().getIMPORTS().getIMPORT();
            for (int i = 0; i < emfImportList.size(); i++) {
                IMPORTType importType = (IMPORTType) emfImportList.get(i);

                String msg = getTranslatedValue(importType.getNAME() + ".INFO"); //$NON-NLS-1$
                if (msg.startsWith(Messages.KEY_NOT_FOUND_PREFIX)) {
                    msg = Messages.getString("modules.required"); //$NON-NLS-1$
                }
                List<String> list = getInstallURL(importType);
                ModuleNeeded componentImportNeeds = new ModuleNeeded(this.getName(), importType.getMODULE(), msg,
                        importType.isREQUIRED(), list);
                moduleNames.add(importType.getMODULE());
                componentImportNeeds.setShow(importType.isSHOW());
                componentImportNeedsList.add(componentImportNeeds);
            }
        }
        for (IMultipleComponentManager multipleComponentManager : multipleComponentManagers) {
            for (IMultipleComponentItem multipleComponentItem : multipleComponentManager.getItemList()) {
                IComponent component = ComponentsFactoryProvider.getInstance().get(multipleComponentItem.getComponent());
                if (component == null) {
                    continue;
                }
                for (ModuleNeeded moduleNeeded : component.getModulesNeeded()) {
                    if (!moduleNames.contains(moduleNeeded.getModuleName())) {
                        ModuleNeeded componentImportNeeds = new ModuleNeeded(this.getName(), moduleNeeded.getModuleName(),
                                moduleNeeded.getInformationMsg(), moduleNeeded.isRequired(), moduleNeeded.getInstallURL());
                        componentImportNeedsList.add(componentImportNeeds);
                    }
                }
            }
        }
        return componentImportNeedsList;
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

    public List<IMultipleComponentManager> getMultipleComponentManagers() {
        return this.multipleComponentManagers;
    }

    /**
     * DOC nrousseau Comment method "loadMultipleComponentManagerFromTemplates".
     */
    private void loadMultipleComponentManagerFromTemplates() {

        EList templatesTypes = compType.getCODEGENERATION().getTEMPLATES();
        if (templatesTypes == null) {
            return;
        }

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
                        target = "LOOKUP." + target;
                    }

                    multipleComponentManager.addParam(source, target);
                }
                if ((templateParamType.getTARGET() != null) && (templateParamType.getVALUE() != null)) {
                    String value, target;
                    value = templateParamType.getVALUE();
                    target = templateParamType.getTARGET();

                    if (lookupMode) {
                        target = "LOOKUP." + target;
                    }

                    multipleComponentManager.addValue(target, value);
                }
            }

            multipleComponentManager.validateItems();
            multipleComponentManagers.add(multipleComponentManager);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isLoaded()
     */
    public boolean isLoaded() {
        if (isLoaded == null) {
            return false;
        }
        return isLoaded;
    }

    /**
     * Getter for icon16.
     * 
     * @return the icon16
     */
    public ImageDescriptor getIcon16() {
        return this.icon16;
    }

    /**
     * Sets the icon16.
     * 
     * @param icon16 the icon16 to set
     */
    public void setIcon16(ImageDescriptor icon16) {
        this.icon16 = icon16;
    }

    /**
     * Getter for icon24.
     * 
     * @return the icon24
     */
    public ImageDescriptor getIcon24() {
        return this.icon24;
    }

    /**
     * Sets the icon24.
     * 
     * @param icon24 the icon24 to set
     */
    public void setIcon24(ImageDescriptor icon24) {
        this.icon24 = icon24;
    }

    /**
     * Getter for icon32.
     * 
     * @return the icon32
     */
    public ImageDescriptor getIcon32() {
        return this.icon32;
    }

    /**
     * Sets the icon32.
     * 
     * @param icon32 the icon32 to set
     */
    public void setIcon32(ImageDescriptor icon32) {
        this.icon32 = icon32;
    }

    public String getPathSource() {
        return this.pathSource;
    }

    public void setPathSource(String pathSource) {
        this.pathSource = pathSource;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IComponent other = (IComponent) obj;
        if (this.getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!this.getName().equals(other.getName())) {
            return false;
        }
        return true;
    }

    private void checkAvailableCodeParts() {
        codePartList = new ArrayList<ECodePart>();
        File dirFile = new File(file.getParent());
        final String extension = "." + LanguageManager.getCurrentLanguage().getName() + "jet"; //$NON-NLS-1$ //$NON-NLS-2$
        FilenameFilter fileNameFilter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(extension);
            }
        };

        String[] jetFiles = dirFile.list(fileNameFilter);

        for (int i = 0; i < jetFiles.length; i++) {
            String name = jetFiles[i];
            name = jetFiles[i].replace(getName() + "_", ""); //$NON-NLS-1$ //$NON-NLS-2$
            name = name.replace(extension, ""); //$NON-NLS-1$
            ECodePart part = ECodePart.getCodePartByName(name);
            if (part != null) {
                codePartList.add(part);
            }
        }
    }

    public List<ECodePart> getAvailableCodeParts() {
        return codePartList;
    }

    @SuppressWarnings("unchecked")
    public List<String> getPluginDependencies() {
        List<String> pluginDependencyList = new ArrayList<String>();
        if (this.compType.getPLUGINDEPENDENCIES() != null) {
            List<PLUGINDEPENDENCYTypeImpl> pti = this.compType.getPLUGINDEPENDENCIES().getPLUGINDEPENDENCY();
            for (PLUGINDEPENDENCYTypeImpl pt : pti) {
                pluginDependencyList.add(pt.getID());
            }
        }
        return pluginDependencyList;
    }

    public boolean useMerge() {
        if (useMerge == null) {
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

    public boolean isMultiplyingOutputs() {
        return compType.getHEADER().isISMULTIPLYINGOUTPUTS();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#getComponentType()
     */
    public boolean isMultipleOutput() {

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

    private boolean connectorUseInputLinkSelection(String name) {
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
    public boolean useLookup() {
        if (useLookup == null) {
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
    public boolean useImport() {
        if (useImport == null) {
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
    public EComponentType getComponentType() {
        return EComponentType.EMF;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isHashComponent()
     */
    public boolean isHashComponent() {
        return compType.getHEADER().isHASHCOMPONENT();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isTechnical()
     */
    public boolean isTechnical() {
        if (technical != null) {
            return technical;
        }

        return compType.getHEADER().isTECHNICAL();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isSingleton()
     */
    public boolean isSingleton() {
        return compType.getHEADER().isSINGLETON();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#isMainCodeCalled()
     */
    public boolean isMainCodeCalled() {
        return compType.getHEADER().isMAINCODECALLED();
    }

    /**
     * get this component's repository type <br>
     * see <PARAMETER NAME="PROPERTY" ...> in the component's xml definition.
     * 
     * @return
     */
    public String getRepositoryType() {
        for (PARAMETERType pType : (List<PARAMETERType>) compType.getPARAMETERS().getPARAMETER()) {
            if (pType.getFIELD().equals("PROPERTY_TYPE")) { //$NON-NLS-1$
                return pType.getREPOSITORYVALUE();
            }
        }
        return null;
    }

    public boolean canParallelize() {
        return compType.getHEADER().isPARALLELIZE();
    }

    /**
     * return the common ComponentResourceFactoryImpl to retreive component resource from URI
     * 
     * @return factoryImpl
     */
    // here we are using weak references so that whenever the GC runs it can collect the ComponentResourceFactoryImpl
    private static ComponentResourceFactoryImpl getComponentResourceFactoryImpl() {
        ComponentResourceFactoryImpl factoryImpl = compResFactoryWeakRef == null ? null : compResFactoryWeakRef.get();
        if (factoryImpl == null) {// if weak ref has not been created or if referenced factory has been GCed then create
            // a new one
            factoryImpl = new ComponentResourceFactoryImpl();
            compResFactoryWeakRef = new WeakReference<ComponentResourceFactoryImpl>(factoryImpl);
        }
        return factoryImpl;
    }

    /**
     * return the common ComponentResourceFactoryImpl to retreive component resource from URI
     * 
     * @return factoryImpl
     */
    // here we are using weak references so that whenever the GC runs it can collect the ComponentResourceFactoryImpl
    private static Map getLoadingOptionMap() {
        Map optionMap = optionMapWeakRef == null ? null : optionMapWeakRef.get();
        if (optionMap == null) {// if weak ref has not been created or if referenced factory has been GCed then create
            // a new one
            optionMap = new HashMap();
            optionMap.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
            optionMap.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
            optionMap.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap());
            optionMapWeakRef = new WeakReference<Map>(optionMap);
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
}
