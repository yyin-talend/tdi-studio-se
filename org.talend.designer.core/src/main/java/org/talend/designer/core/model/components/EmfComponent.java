// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.model.components;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.talend.commons.exception.BusinessException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IMultipleComponentItem;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.temp.ECodePart;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.utils.emf.component.COLUMNType;
import org.talend.designer.core.model.utils.emf.component.COMPONENTType;
import org.talend.designer.core.model.utils.emf.component.CONNECTORType;
import org.talend.designer.core.model.utils.emf.component.DEFAULTType;
import org.talend.designer.core.model.utils.emf.component.DocumentRoot;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.component.ITEMSType;
import org.talend.designer.core.model.utils.emf.component.ITEMType;
import org.talend.designer.core.model.utils.emf.component.LINKTOType;
import org.talend.designer.core.model.utils.emf.component.PARAMETERType;
import org.talend.designer.core.model.utils.emf.component.RETURNType;
import org.talend.designer.core.model.utils.emf.component.TABLEType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATEPARAMType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATESType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATEType;
import org.talend.designer.core.model.utils.emf.component.impl.PLUGINDEPENDENCYTypeImpl;
import org.talend.designer.core.model.utils.emf.component.util.ComponentResourceFactoryImpl;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.ExternalNodesFactory;

/**
 * 
 * Component manager that read each information in a xml file with Emf. <br/> $Id: EmfComponent.java 5524 2007-09-10
 * 02:23:53Z nrousseau $
 */
public class EmfComponent implements IComponent {

    private static final String DEFAULT_COLOR = "255;255;255"; //$NON-NLS-1$

    private File file;

    private static final long serialVersionUID = 1L;

    private Boolean isLoaded = null;

    private COMPONENTType compType;

    private ImageDescriptor icon32;

    private ImageDescriptor icon24;

    private ImageDescriptor icon16;

    private ECodeLanguage codeLanguage;

    public static final String BUILTIN = "BUILT_IN"; //$NON-NLS-1$

    public static final String REPOSITORY = "REPOSITORY"; //$NON-NLS-1$

    public static final String TEXT_BUILTIN = Messages.getString("EmfComponent.builtIn"); //$NON-NLS-1$

    public static final String TEXT_REPOSITORY = Messages.getString("EmfComponent.repository"); //$NON-NLS-1$

    private static final String TSTATCATCHER_NAME = "tStatCatcher"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_UTF_8 = "UTF-8"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_ISO_8859_15 = "ISO-8859-15"; //$NON-NLS-1$

    public static final String ENCODING_TYPE_CUSTOM = "CUSTOM"; //$NON-NLS-1$

    private static final String STRING_TYPE = "String"; //$NON-NLS-1$

    private IMultipleComponentManager multipleComponentManager;

    private ResourceBundle resourceBundle;

    private String pathSource;

    private List<ECodePart> codePartList;

    public EmfComponent(File file, String pathSource) throws BusinessException {
        this.file = file;
        this.pathSource = pathSource;
        load();
        codeLanguage = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getProject().getLanguage();
    }

    public ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    private String getTranslatedValue(final String nameValue) {
        String returnValue = nameValue;
        returnValue = Messages.getString(nameValue, getResourceBundle());
        return returnValue;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void load() throws BusinessException {
        if (isLoaded == null) {
            try {
                ResourceSet resourceSet = new ResourceSetImpl();
                ComponentResourceFactoryImpl compFact;
                compFact = new ComponentResourceFactoryImpl();
                URI createURI = URI.createURI(file.toURI().toString());
                compFact.createResource(createURI);
                resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
                        Resource.Factory.Registry.DEFAULT_EXTENSION, compFact);

                Resource res = resourceSet.getResource(createURI, true);

                DocumentRoot xmlDoc;
                xmlDoc = (DocumentRoot) res.getContents().get(0);

                compType = (COMPONENTType) xmlDoc.eContents().get(0);

                loadMultipleComponentManagerFromTemplates();

                // just load the externalNode plugin to check if the plugin
                // exists.
                if (compType.getHEADER().getEXTENSION() != null) {
                    ExternalNodesFactory.getInstance(this.getPluginFullName());
                }

                checkAvailableCodeParts();

                isLoaded = true;
            } catch (Exception e) {
                isLoaded = false;
                throw new BusinessException("Cannot load component " + file.getName(), e); //$NON-NLS-1$
            }
        }
    }

    public List<ElementParameter> createElementParameters(INode node) {
        List<ElementParameter> listParam;
        listParam = new ArrayList<ElementParameter>();
        addMainParameters(listParam, node);
        addPropertyParameters(listParam, node);
        initializePropertyParameters(listParam);
        addViewParameters(listParam, node);
        addDocParameters(listParam, node);
        return listParam;
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
        nodeRet.setType(STRING_TYPE); //$NON-NLS-1$
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
            nodeRet.setType(STRING_TYPE); //$NON-NLS-1$
            listReturn.add(nodeRet);

            nodeRet = new NodeReturn();
            nodeRet.setAvailability("AFTER"); //$NON-NLS-1$
            nodeRet.setVarName("PERL_ERROR_CODE"); //$NON-NLS-1$
            nodeRet.setDisplayName("Perl Error Code"); //$NON-NLS-1$
            nodeRet.setName("PERL_ERROR_CODE"); //$NON-NLS-1$
            nodeRet.setType(STRING_TYPE); //$NON-NLS-1$
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
        param.setName(EParameterName.COMMENT.getName());
        param.setValue(""); //$NON-NLS-1$
        param.setDisplayName(EParameterName.COMMENT.getDisplayName());
        param.setField(EParameterFieldType.MEMO);
        param.setNbLines(10);
        param.setCategory(EComponentCategory.DOC);
        param.setNumRow(1);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        listParam.add(param);
    }

    private void addViewParameters(final List<ElementParameter> listParam, INode node) {
        ElementParameter param;

        param = new ElementParameter(node);
        param.setName(EParameterName.LABEL.getName());
        param.setDisplayName(EParameterName.LABEL.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.VIEW);
        param.setNumRow(1);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
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
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.SHOW_HINT.getName());
        param.setValue(new Boolean(false));
        param.setDisplayName(EParameterName.SHOW_HINT.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.VIEW);
        param.setNumRow(3);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        listParam.add(param);
    }

    private void addMainParameters(final List<ElementParameter> listParam, INode node) {
        ElementParameter param;
        param = new ElementParameter(node);
        param.setName(EParameterName.UNIQUE_NAME.getName());
        param.setValue(""); //$NON-NLS-1$
        param.setDisplayName(EParameterName.UNIQUE_NAME.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(true);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.COMPONENT_NAME.getName());
        param.setValue(getName());
        param.setDisplayName(EParameterName.COMPONENT_NAME.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.TRANSLATED_COMPONENT_NAME.getName());
        param.setValue(getTranslatedName());
        param.setDisplayName(EParameterName.TRANSLATED_COMPONENT_NAME.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.VERSION.getName());
        param.setValue(compType.getHEADER().getVERSION() + " (" + compType.getHEADER().getSTATUS() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        param.setDisplayName(EParameterName.VERSION.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(2);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.FAMILY.getName());
        param.setValue(getFamily());
        param.setDisplayName(EParameterName.FAMILY.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(3);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(true);
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
            param.setCategory(EComponentCategory.MAIN);
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
        param.setCategory(EComponentCategory.MAIN);
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
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(5);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        listParam.add(param);

        boolean tStatCatcherAvailable = ComponentsFactoryProvider.getInstance().get(TSTATCATCHER_NAME) != null;
        param = new ElementParameter(node);
        param.setName(EParameterName.TSTATCATCHER_STATS.getName());
        param.setValue(new Boolean(compType.getHEADER().isTSTATCATCHERSTATS()));
        param.setDisplayName(EParameterName.TSTATCATCHER_STATS.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(6);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(tStatCatcherAvailable);
        listParam.add(param);

        param = new ElementParameter(node);
        param.setName(EParameterName.HELP.getName());
        param.setValue(getTranslatedValue(PROP_HELP));
        param.setDisplayName(EParameterName.HELP.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.MAIN);
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
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(5);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        listParam.add(param);
    }

    private void createSpecificParametersFromType(final List<ElementParameter> listParam, final PARAMETERType xmlParam,
            final INode node, final EParameterFieldType type, ElementParameter parentParam) {
        if (type == EParameterFieldType.PROPERTY_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.PROPERTY);
            newParam.setName(EParameterName.PROPERTY_TYPE.getName());
            newParam.setDisplayName(EParameterName.PROPERTY_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
            newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY });
            newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
            newParam.setValue(BUILTIN);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setField(EParameterFieldType.CLOSED_LIST);
            newParam.setRepositoryValue(xmlParam.getREPOSITORYVALUE());
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            // newParam.setParentParameter(parentParam);
            listParam.add(newParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.PROPERTY);
            newParam.setName(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
            newParam.setDisplayName(EParameterName.REPOSITORY_PROPERTY_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setField(EParameterFieldType.CLOSED_LIST);
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setShow(false);
            newParam.setRequired(true);
            // newParam.setParentParameter(parentParam);
            listParam.add(newParam);
        }
        if (type == EParameterFieldType.SCHEMA_TYPE) {
            String context = xmlParam.getCONTEXT();
            if (context == null) {
                // by default the schema will be set to the "FLOW" connector.
                context = EConnectionType.FLOW_MAIN.getName();
                if (getFamily().startsWith("ELT")) {
                    context = EConnectionType.TABLE.getName();
                }
            }

            String displayName = getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME);
            if (displayName.startsWith("!!")) { //$NON-NLS-1$ //$NON-NLS-2$
                displayName = EParameterName.SCHEMA_TYPE.getDisplayName();
            }

            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.PROPERTY);
            newParam.setName(EParameterName.SCHEMA_TYPE.getName());
            newParam.setDisplayName(displayName);
            newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
            newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY });
            newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
            newParam.setValue(BUILTIN);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setField(EParameterFieldType.TECHNICAL);
            newParam.setShow(xmlParam.isSHOW());
            newParam.setShowIf(parentParam.getName() + " =='" + REPOSITORY + "'");
            newParam.setReadOnly(xmlParam.isREADONLY());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());

            newParam.setContext(context);
            newParam.setParentParameter(parentParam);
            // listParam.add(newParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.PROPERTY);
            newParam.setName(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
            newParam.setDisplayName(EParameterName.REPOSITORY_SCHEMA_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setField(EParameterFieldType.TECHNICAL);
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setShow(false);
            newParam.setRequired(true);
            newParam.setReadOnly(xmlParam.isREADONLY());
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            newParam.setContext(context);
            newParam.setParentParameter(parentParam);
            // listParam.add(newParam);
        }
        if (type == EParameterFieldType.ENCODING_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.PROPERTY);
            newParam.setName(EParameterName.ENCODING_TYPE.getName());
            newParam.setDisplayName(EParameterName.ENCODING_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] { ENCODING_TYPE_ISO_8859_15, ENCODING_TYPE_UTF_8,
                    ENCODING_TYPE_CUSTOM });
            newParam.setListItemsDisplayCodeName(new String[] { ENCODING_TYPE_ISO_8859_15, ENCODING_TYPE_UTF_8,
                    ENCODING_TYPE_CUSTOM });
            newParam.setListItemsValue(new String[] { ENCODING_TYPE_ISO_8859_15, ENCODING_TYPE_UTF_8,
                    ENCODING_TYPE_CUSTOM });
            newParam.setValue(ENCODING_TYPE_ISO_8859_15);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setField(EParameterFieldType.TECHNICAL);
            newParam.setShow(true);
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            // newParam.setParentParameter(parentParam);
            listParam.add(newParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.PROPERTY);
            newParam.setName(EParameterName.REPOSITORY_ENCODING_TYPE.getName());
            newParam.setDisplayName(EParameterName.REPOSITORY_ENCODING_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setField(EParameterFieldType.TECHNICAL);
            newParam.setValue(TalendTextUtils.addQuotes("")); //$NON-NLS-1$
            newParam.setShow(false);
            newParam.setRequired(true);
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            // newParam.setParentParameter(parentParam);
            listParam.add(newParam);
        }// Ends
        if (type == EParameterFieldType.QUERYSTORE_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.PROPERTY);
            newParam.setName(EParameterName.QUERYSTORE_TYPE.getName());
            newParam.setDisplayName(EParameterName.QUERYSTORE_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
            newParam.setListItemsDisplayCodeName(new String[] { BUILTIN, REPOSITORY });
            newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
            newParam.setValue(BUILTIN);
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setField(EParameterFieldType.CLOSED_LIST);
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            // newParam.setParentParameter(parentParam);
            listParam.add(newParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.PROPERTY);
            newParam.setName(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName());
            newParam.setDisplayName(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getDisplayName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setField(EParameterFieldType.CLOSED_LIST);
            newParam.setValue(""); //$NON-NLS-1$
            newParam.setShow(false);
            newParam.setRequired(true);
            newParam.setShowIf(xmlParam.getSHOWIF());
            newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
            // newParam.setParentParameter(parentParam);
            listParam.add(newParam);
        }

        if (type == EParameterFieldType.PROCESS_TYPE) {
            ElementParameter newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.PROPERTY);
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
            newParam.setField(EParameterFieldType.CLOSED_LIST);
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setRequired(true);
            // newParam.setParentParameter(parentParam);
            listParam.add(newParam);

            newParam = new ElementParameter(node);
            newParam.setCategory(EComponentCategory.PROPERTY);
            newParam.setName(EParameterName.PROCESS_TYPE_CONTEXT.getName());
            newParam.setDisplayName(EParameterName.PROCESS_TYPE_CONTEXT.getDisplayName());
            newParam.setListItemsDisplayName(new String[] {});
            newParam.setListItemsValue(new String[] {});
            newParam.setNumRow(xmlParam.getNUMROW());
            newParam.setField(EParameterFieldType.CLOSED_LIST);
            newParam.setValue(""); //$NON-NLS-1$
            if (xmlParam.isSetSHOW()) {
                newParam.setShow(xmlParam.isSHOW());
            }
            newParam.setRequired(true);
            // newParam.setParentParameter(parentParam);
            listParam.add(newParam);
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void addPropertyParameters(final List<ElementParameter> listParam, final INode node) {
        EList listXmlParam;
        PARAMETERType xmlParam;
        ElementParameter param;

        boolean autoSwitchAdded = false;

        listXmlParam = compType.getPARAMETERS().getPARAMETER();
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

            param.setFilter(xmlParam.getFILTER());
            param.setShowIf(xmlParam.getSHOWIF());
            param.setNotShowIf(xmlParam.getNOTSHOWIF());
            param.setRepositoryValue(xmlParam.getREPOSITORYVALUE());
            param.setContext(xmlParam.getCONTEXT());

            switch (type) {
            case COLOR:
                param.setValue(DEFAULT_COLOR);
                break;
            case CHECK:
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
                    if (getFamily().startsWith("ELT")) {
                        param.setContext(EConnectionType.TABLE.getName());
                    }
                }
                List<NodeConnector> list = createConnectors();
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
                param.setShow(false);
                param.setRequired(false);
                if (getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME).startsWith("!!")) { //$NON-NLS-1$ //$NON-NLS-2$
                    param.setDisplayName(EParameterName.PROCESS_TYPE.getDisplayName());
                }
                param.setValue(""); // TODO to change ? //$NON-NLS-1$
                break;
            case PROPERTY_TYPE:
                param.setShow(false);
                param.setValue(""); //$NON-NLS-1$
                break;
            default:
                param.setValue(""); //$NON-NLS-1$
            }

            if (!param.getField().equals(EParameterFieldType.TABLE)
                    && !param.getField().equals(EParameterFieldType.CLOSED_LIST)
                    && !param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                List<DEFAULTType> listDefault = xmlParam.getDEFAULT();
                for (DEFAULTType defaultType : listDefault) {
                    IElementParameterDefaultValue defaultValue = new ElementParameterDefaultValue();

                    if (node.getProcess() != null) {
                        defaultValue.setDefaultValue(ElementParameterParser.parse(node.getProcess(), defaultType
                                .getValue()));
                        if (param.getField() == EParameterFieldType.FILE
                                || param.getField() == EParameterFieldType.DIRECTORY) {
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
            }

            param.setCategory(EComponentCategory.PROPERTY);

            createSpecificParametersFromType(listParam, xmlParam, node, type, param);
            listParam.add(param);
        }
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
                }
                talendColumn.setCustomId(i);
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
                for (IElementParameterDefaultValue defaultValue : param.getDefaultValues()) {
                    String conditionIf = defaultValue.getIfCondition();
                    String conditionNotIf = defaultValue.getNotIfCondition();

                    if (param.isShow(conditionIf, conditionNotIf, listParam)) {
                        isSet = true;
                        if (param.getField().equals(EParameterFieldType.CHECK)) {
                            param.setValue(new Boolean(defaultValue.getDefaultValue().toString()));
                        } else {
                            param.setValue(defaultValue.getDefaultValue());
                        }
                    }
                }
                if (!isSet) {
                    if (param.getField().equals(EParameterFieldType.CHECK)) {
                        param.setValue(new Boolean(param.getDefaultValues().get(0).getDefaultValue().toString()));
                    } else {
                        param.setValue(param.getDefaultValues().get(0).getDefaultValue());
                    }
                }
            }
        }

        initializePropertyParametersForSchema(listParam);
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

    public void addItemsPropertyParameters(String paramName, ITEMSType items, ElementParameter param,
            EParameterFieldType type, INode node) {
        ITEMType item;
        ElementParameter newParam;

        int nbItems = 0;
        if (items != null) {
            if (items.isSetBASEDONSCHEMA()) {
                param.setBasedOnSchema(items.isBASEDONSCHEMA());
            }
            nbItems = items.getITEM().size();
            if (param.isBasedOnSchema()) {
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

        for (int k = 0; k < nbItems; k++) {
            int currentItem = k;
            if (param.isBasedOnSchema()) {
                if (k == 0) {
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
                } else {
                    currentItem = k - 1;
                }
            }
            item = (ITEMType) items.getITEM().get(currentItem);
            listItemsDisplayCodeValue[k] = item.getNAME();
            listItemsDisplayValue[k] = getTranslatedValue(paramName + ".ITEM." + item.getNAME()); //$NON-NLS-1$
            if (type != EParameterFieldType.TABLE) {
                listItemsValue[k] = item.getVALUE();
            } else {
                EParameterFieldType currentField = EParameterFieldType.getFieldTypeByName(item.getFIELD());
                newParam = new ElementParameter(node);
                newParam.setName(item.getNAME());
                newParam.setFilter(item.getFILTER());
                newParam.setDisplayName(""); //$NON-NLS-1$
                newParam.setField(currentField);
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
                    addItemsPropertyParameters(
                            paramName + ".ITEM." + item.getNAME(), item.getITEMS(), newParam, currentField, //$NON-NLS-1$
                            node);
                    break;
                case CHECK:
                    newParam.setValue(new Boolean(item.getVALUE()));
                    break;
                case COLOR:
                    newParam.setValue(DEFAULT_COLOR);
                    break;
                default: // TEXT by default
                    newParam.setField(EParameterFieldType.TEXT);
                    newParam.setValue(item.getVALUE());
                }
                listItemsValue[k] = newParam;
            }
            listField[k] = item.getFIELD();
            listRepositoryItem[k] = item.getREPOSITORYITEM();
            listItemsShowIf[k] = item.getSHOWIF();
            listItemsNotShowIf[k] = item.getNOTSHOWIF();
        }

        param.setListItemsDisplayName(listItemsDisplayValue);
        param.setListItemsDisplayCodeName(listItemsDisplayCodeValue);
        param.setListItemsValue(listItemsValue);
        param.setListRepositoryItems(listRepositoryItem);
        param.setListItemsShowIf(listItemsShowIf);
        param.setListItemsNotShowIf(listItemsNotShowIf);
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

    public String getFamily() {
        return getTranslatedValue(PROP_FAMILY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.IComponent#hasConditionalOutputs()
     */
    public Boolean hasConditionalOutputs() {
        return compType.getHEADER().isHASCONDITIONALOUTPUTS();
    }

    public String getName() {
        return file.getParentFile().getName();
    }

    public String getTranslatedName() {
        return getTranslatedValue(PROP_NAME);
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
    public List<NodeConnector> createConnectors() {
        List<NodeConnector> listConnector;
        EList listConnType;
        CONNECTORType connType;
        NodeConnector nodeConnector;

        listConnector = new ArrayList<NodeConnector>();

        listConnType = compType.getCONNECTORS().getCONNECTOR();
        for (int i = 0; i < listConnType.size(); i++) {
            connType = (CONNECTORType) listConnType.get(i);
            // EConnectionType currentType = EConnectionType.values()[i];
            EConnectionType currentType = EConnectionType.getTypeFromName(connType.getCTYPE());
            if (currentType == null) {
                continue;
            }
            nodeConnector = new NodeConnector();
            nodeConnector.setDefaultConnectionType(currentType);
            // set the default values
            nodeConnector.setLinkName(currentType.getDefaultLinkName());
            nodeConnector.setMenuName(currentType.getDefaultMenuName());
            Color color = currentType.getDefaultColor();
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
                RGB rgb = new RGB(r, g, b);

                color = new Color(null, rgb);
            }
            nodeConnector.addConnectionProperty(currentType, color, lineStyle);
            String baseSchema = connType.getBASESCHEMA();
            if (baseSchema != null && (!baseSchema.equals(""))) {
                nodeConnector.setBaseSchema(baseSchema);
            } else {
                nodeConnector.setBaseSchema(nodeConnector.getName());
            }

            listConnector.add(nodeConnector);
            if (connType.getCTYPE().equals("FLOW")) { // if kind is "flow"
                // (main type), then add
                // the same for the
                // //$NON-NLS-1$
                // lookup.
                currentType = EConnectionType.FLOW_REF;

                if (connType.getCOLOR() == null) {
                    color = currentType.getDefaultColor();
                }
                if (!connType.isSetLINESTYLE()) {
                    lineStyle = currentType.getDefaultLineStyle();
                }
                nodeConnector.addConnectionProperty(currentType, color, lineStyle);
                currentType = EConnectionType.FLOW_MERGE;

                if (connType.getCOLOR() == null) {
                    color = currentType.getDefaultColor();
                }
                if (!connType.isSetLINESTYLE()) {
                    lineStyle = currentType.getDefaultLineStyle();
                }
                nodeConnector.addConnectionProperty(currentType, color, lineStyle);
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
                nodeConnector = new NodeConnector();
                nodeConnector.setDefaultConnectionType(currentType);
                nodeConnector.setName(currentType.getName());
                nodeConnector.setBaseSchema(currentType.getName());
                nodeConnector.addConnectionProperty(currentType, currentType.getDefaultColor(), currentType
                        .getDefaultLineStyle());
                nodeConnector.setLinkName(currentType.getDefaultLinkName());
                nodeConnector.setMenuName(currentType.getDefaultMenuName());
                nodeConnector.setMaxLinkInput(0);
                nodeConnector.setMinLinkInput(0);
                nodeConnector.setMaxLinkOutput(0);
                nodeConnector.setMinLinkOutput(0);
                if (currentType == EConnectionType.FLOW_MAIN) {
                    nodeConnector.addConnectionProperty(EConnectionType.FLOW_REF, EConnectionType.FLOW_REF
                            .getDefaultColor(), EConnectionType.FLOW_REF.getDefaultLineStyle());
                    nodeConnector.addConnectionProperty(EConnectionType.FLOW_MERGE, EConnectionType.FLOW_MERGE
                            .getDefaultColor(), EConnectionType.FLOW_MERGE.getDefaultLineStyle());
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

    public boolean isVisible() {
        return compType.getHEADER().isVISIBLE();
    }

    public String getVersion() {
        return String.valueOf(compType.getHEADER().getVERSION());
    }

    public List<ModuleNeeded> getModulesNeeded() {
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

                ModuleNeeded componentImportNeeds = new ModuleNeeded(this.getName(), importType.getMODULE(), msg,
                        importType.isREQUIRED());
                componentImportNeeds.setShow(importType.isSHOW());
                componentImportNeedsList.add(componentImportNeeds);
            }
        }
        return componentImportNeedsList;
    }

    public IMultipleComponentManager getMultipleComponentManager() {
        return this.multipleComponentManager;
    }

    /**
     * DOC nrousseau Comment method "loadMultipleComponentManagerFromTemplates".
     */
    private void loadMultipleComponentManagerFromTemplates() {
        TEMPLATESType templatesType;

        templatesType = compType.getCODEGENERATION().getTEMPLATES();

        if (templatesType == null) {
            return;
        }
        String input, output;

        input = templatesType.getINPUT();
        output = templatesType.getOUTPUT();

        if (input == null || output == null) {
            return;
        }

        multipleComponentManager = new MultipleComponentManager(input, output);

        EList listTempType = templatesType.getTEMPLATE();
        for (int i = 0; i < listTempType.size(); i++) {
            TEMPLATEType templateType = (TEMPLATEType) listTempType.get(i);

            String name, component;
            name = templateType.getNAME();
            component = templateType.getCOMPONENT();

            IMultipleComponentItem currentItem = multipleComponentManager.addItem(name, component);
            EList listLinkTo = templateType.getLINKTO();
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

                multipleComponentManager.addParam(source, target);
            }
            if ((templateParamType.getTARGET() != null) && (templateParamType.getVALUE() != null)) {
                String value, target;
                value = templateParamType.getVALUE();
                target = templateParamType.getTARGET();

                multipleComponentManager.addValue(target, value);
            }
        }

        multipleComponentManager.validateItems();
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
        final EmfComponent other = (EmfComponent) obj;
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

    @SuppressWarnings("unchecked")//$NON-NLS-1$
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
        return compType.getHEADER().isUSEMERGE();
    }

    public Boolean isMultiplyingOutputs() {
        return compType.getHEADER().isISMULTIPLYINGOUTPUTS();
    }
}
