// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.SystemException;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IMultipleComponentItem;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.metadata.EMetadataType;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.process.INode;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.codegen.perlmodule.ModuleNeeded;
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
import org.talend.designer.core.model.utils.emf.component.TEMPLATEPARAMType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATESType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATEType;
import org.talend.designer.core.model.utils.emf.component.util.ComponentResourceFactoryImpl;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * 
 * Component manager that read each information in a xml file with Emf. <br/> $Id: EmfComponent.java,v 1.12 2006/06/08
 * 14:12:27 nrousseau Exp $
 */
public class EmfComponent implements IComponent {

    private File file;

    private static final long serialVersionUID = 1L;

    private Boolean isLoaded = null;

    private COMPONENTType compType;

    private ImageDescriptor image32;

    private ImageDescriptor image24;

    private ImageDescriptor image16;

    private ECodeLanguage codeLanguage;

    public static final String BUILTIN = "BUILT_IN"; //$NON-NLS-1$

    public static final String REPOSITORY = "REPOSITORY"; //$NON-NLS-1$

    private static final String TEXT_BUILTIN = "Built-In";

    private static final String TEXT_REPOSITORY = "Repository";

    private static final String TSTATCATCHER_NAME = "tStatCatcher";

    private IMultipleComponentManager multipleComponentManager;

    public EmfComponent(File file) throws SystemException {
        this.file = file;
        load();
        codeLanguage = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getProject().getLanguage();
    }

    private String getTranslatedValue(final String nameValue) {

        String returnValue = nameValue;
        String compName = getName();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("components." + compName + ".component");
        returnValue = Messages.getString(nameValue, resourceBundle);
        return returnValue;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void load() throws SystemException {
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

                isLoaded = true;
            } catch (Exception e) {
                isLoaded = false;
                throw new SystemException("Cannot load component " + file.getName(), e);
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
        nodeRet.setAvailability("AFTER");
        nodeRet.setVarName("ERROR_MESSAGE", codeLanguage);
        nodeRet.setDisplayName("Error Message");
        nodeRet.setName("ERROR_MESSAGE");
        nodeRet.setType(EMetadataType.getTypeByName("STRING"));
        listReturn.add(nodeRet);

        if (codeLanguage.equals(ECodeLanguage.PERL)) {
            nodeRet = new NodeReturn();
            nodeRet.setAvailability("AFTER");
            nodeRet.setVarName("PERL_ERROR_MESSAGE", codeLanguage);
            nodeRet.setDisplayName("Perl Error Message");
            nodeRet.setName("PERL_ERROR_MESSAGE");
            nodeRet.setType(EMetadataType.getTypeByName("STRING"));
            listReturn.add(nodeRet);

            nodeRet = new NodeReturn();
            nodeRet.setAvailability("AFTER");
            nodeRet.setVarName("PERL_ERROR_CODE", codeLanguage);
            nodeRet.setDisplayName("Perl Error Code");
            nodeRet.setName("PERL_ERROR_CODE");
            nodeRet.setType(EMetadataType.getTypeByName("STRING"));
            listReturn.add(nodeRet);
        }
        // ****************** end of standard returns ******************
        returnList = compType.getRETURNS().getRETURN();

        for (int i = 0; i < returnList.size(); i++) {
            retType = (RETURNType) returnList.get(i);
            nodeRet = new NodeReturn();
            nodeRet.setAvailability(retType.getAVAILABILITY());
            nodeRet.setVarName(retType.getNAME(), codeLanguage);
            nodeRet.setDisplayName(getTranslatedValue(retType.getNAME() + "." + PROP_NAME));
            nodeRet.setName(retType.getNAME());
            nodeRet.setType(EMetadataType.getTypeByName(retType.getTYPE()));
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
        param.setValue("");
        param.setDisplayName(EParameterName.UNIQUE_NAME.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(true);
        listParam.add(param);

        // param = new ElementParameter(node);
        // param.setName(EParameterName.TRANSLATED_UNIQUE_NAME.getName());
        // param.setValue("");
        // param.setDisplayName(EParameterName.TRANSLATED_UNIQUE_NAME.getDisplayName());
        // param.setField(EParameterFieldType.TEXT);
        // param.setCategory(EComponentCategory.MAIN);
        // param.setNumRow(1);
        // param.setReadOnly(true);
        // param.setShow(!getName().equals(getTranslatedName()));
        // listParam.add(param);

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

        param = new ElementParameter(node);
        param.setName(EParameterName.LOG.getName());
        param.setValue(""); //$NON-NLS-1$
        param.setDisplayName(EParameterName.LOG.getDisplayName());
        param.setListItemsValue(new String[] { "NONE", "COUNT", "TIME", "PERF" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        param.setListItemsDisplayName(new String[] {
                Messages.getString("EmfComponent.10"), Messages.getString("EmfComponent.11"), //$NON-NLS-1$ //$NON-NLS-2$
                Messages.getString("EmfComponent.12"), Messages.getString("EmfComponent.13") }); //$NON-NLS-1$ //$NON-NLS-2$
        param.setField(EParameterFieldType.CLOSED_LIST);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(4);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(false);

        listParam.add(param);

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

    @SuppressWarnings("unchecked")
    private void addPropertyParameters(final List<ElementParameter> listParam, INode node) {
        EList listXmlParam;
        PARAMETERType xmlParam;
        ElementParameter param;

        listXmlParam = compType.getPARAMETERS().getPARAMETER();
        for (int i = 0; i < listXmlParam.size(); i++) {
            xmlParam = (PARAMETERType) listXmlParam.get(i);
            EParameterFieldType type = EParameterFieldType.getFieldTypeByName(xmlParam.getFIELD());
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
                listParam.add(newParam);

                newParam = new ElementParameter(node);
                newParam.setCategory(EComponentCategory.PROPERTY);
                newParam.setName(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
                newParam.setDisplayName(EParameterName.REPOSITORY_PROPERTY_TYPE.getDisplayName());
                newParam.setListItemsDisplayName(new String[] {});
                newParam.setListItemsValue(new String[] {});
                newParam.setNumRow(xmlParam.getNUMROW());
                newParam.setField(EParameterFieldType.CLOSED_LIST);
                newParam.setValue("");
                newParam.setShow(false);
                newParam.setRequired(true);
                listParam.add(newParam);
            }
            if (type == EParameterFieldType.SCHEMA_TYPE) {
                ElementParameter newParam = new ElementParameter(node);
                newParam.setCategory(EComponentCategory.PROPERTY);
                newParam.setName(EParameterName.SCHEMA_TYPE.getName());
                newParam.setDisplayName(EParameterName.SCHEMA_TYPE.getDisplayName());
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
                listParam.add(newParam);

                newParam = new ElementParameter(node);
                newParam.setCategory(EComponentCategory.PROPERTY);
                newParam.setName(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
                newParam.setDisplayName(EParameterName.REPOSITORY_SCHEMA_TYPE.getDisplayName());
                newParam.setListItemsDisplayName(new String[] {});
                newParam.setListItemsValue(new String[] {});
                newParam.setNumRow(xmlParam.getNUMROW());
                newParam.setField(EParameterFieldType.CLOSED_LIST);
                newParam.setValue("");
                newParam.setShow(false);
                newParam.setRequired(true);
                newParam.setShowIf(xmlParam.getSHOWIF());
                newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
                listParam.add(newParam);
            }
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
                listParam.add(newParam);

                newParam = new ElementParameter(node);
                newParam.setCategory(EComponentCategory.PROPERTY);
                newParam.setName(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName());
                newParam.setDisplayName(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getDisplayName());
                newParam.setListItemsDisplayName(new String[] {});
                newParam.setListItemsValue(new String[] {});
                newParam.setNumRow(xmlParam.getNUMROW());
                newParam.setField(EParameterFieldType.CLOSED_LIST);
                newParam.setValue("");
                newParam.setShow(false);
                newParam.setRequired(true);
                newParam.setShowIf(xmlParam.getSHOWIF());
                newParam.setNotShowIf(xmlParam.getNOTSHOWIF());
                listParam.add(newParam);
            }

            if (type == EParameterFieldType.PROCESS_TYPE) {
                ElementParameter newParam = new ElementParameter(node);
                newParam.setCategory(EComponentCategory.PROPERTY);
                newParam.setName(EParameterName.PROCESS_TYPE_PROCESS.getName());
                newParam.setDisplayName(EParameterName.PROCESS_TYPE_PROCESS.getDisplayName());
                newParam.setListItemsDisplayName(new String[] {});
                newParam.setListItemsValue(new String[] {});
                newParam.setValue("");
                newParam.setNumRow(xmlParam.getNUMROW());
                newParam.setField(EParameterFieldType.CLOSED_LIST);
                if (xmlParam.isSetSHOW()) {
                    newParam.setShow(xmlParam.isSHOW());
                }
                newParam.setRequired(true);
                listParam.add(newParam);

                newParam = new ElementParameter(node);
                newParam.setCategory(EComponentCategory.PROPERTY);
                newParam.setName(EParameterName.PROCESS_TYPE_CONTEXT.getName());
                newParam.setDisplayName(EParameterName.PROCESS_TYPE_CONTEXT.getDisplayName());
                newParam.setListItemsDisplayName(new String[] {});
                newParam.setListItemsValue(new String[] {});
                newParam.setNumRow(xmlParam.getNUMROW());
                newParam.setField(EParameterFieldType.CLOSED_LIST);
                newParam.setValue("");
                if (xmlParam.isSetSHOW()) {
                    newParam.setShow(xmlParam.isSHOW());
                }
                newParam.setRequired(true);
                listParam.add(newParam);
            }
            param = new ElementParameter(node);
            param.setName(xmlParam.getNAME());
            param.setDisplayName(getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME));
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

            param.setShowIf(xmlParam.getSHOWIF());
            param.setNotShowIf(xmlParam.getNOTSHOWIF());
            param.setRepositoryValue(xmlParam.getREPOSITORYVALUE());

            switch (type) {
            case CHECK:
                param.setValue(new Boolean(false));
                break;
            case TABLE:
                param.setValue(new ArrayList<Map<String, Object>>());
                break;
            case SCHEMA_TYPE:
                if (node.getMetadataList().size() > 0) {
                    IMetadataTable defaultTable = node.getMetadataList().get(0);
                    initializeTableFromXml(defaultTable, xmlParam);
                    param.setValue(defaultTable);
                }
                break;
            default:
                param.setValue("");
            }

            if (!param.getField().equals(EParameterFieldType.TABLE)
                    && !param.getField().equals(EParameterFieldType.CLOSED_LIST)) {
                List<DEFAULTType> listDefault = xmlParam.getDEFAULT();
                for (DEFAULTType defaultType : listDefault) {
                    IElementParameterDefaultValue defaultValue = new ElementParameterDefaultValue();
                    defaultValue.setDefaultValue(defaultType.getValue());
                    defaultValue.setIfCondition(defaultType.getIF());
                    defaultValue.setNotIfCondition(defaultType.getNOTIF());
                    param.getDefaultValues().add(defaultValue);
                }
            }

            if (xmlParam.getITEMS() != null) {
                addItemsPropertyParameters(xmlParam.getNAME(), xmlParam.getITEMS(), param, type, node);
            }

            param.setCategory(EComponentCategory.PROPERTY);
            listParam.add(param);
        }
    }

    private void initializeTableFromXml(IMetadataTable defaultTable, PARAMETERType xmlParam) {
        if (xmlParam.getTABLE() == null) {
            return;
        }
        EList xmlColumnList = xmlParam.getTABLE().getCOLUMN();
        COLUMNType xmlColumn;
        List<IMetadataColumn> talendColumnList = new ArrayList<IMetadataColumn>();
        IMetadataColumn talendColumn;

        for (int i = 0; i < xmlColumnList.size(); i++) {
            xmlColumn = (COLUMNType) xmlColumnList.get(i);
            talendColumn = new MetadataColumn();
            talendColumn.setLabel(xmlColumn.getNAME());
            talendColumn.setTalendType(xmlColumn.getTYPE());
            talendColumn.setPrecision(new Integer(xmlColumn.getPRECISION()));
            talendColumn.setLength(new Integer(xmlColumn.getLENGTH()));
            talendColumn.setNullable(xmlColumn.isNULLABLE());
            talendColumn.setKey(xmlColumn.isKEY());
            talendColumnList.add(talendColumn);
        }

        defaultTable.setListColumns(talendColumnList);
    }

    /**
     * DOC nrousseau Comment method "initializePropertyParameters".
     * 
     * @param listParam
     */
    private void initializePropertyParameters(List<ElementParameter> listParam) {
        for (ElementParameter param : listParam) {
            if (param.getDefaultValues().size() > 0) {
                for (IElementParameterDefaultValue defaultValue : param.getDefaultValues()) {
                    String conditionIf = defaultValue.getIfCondition();
                    String conditionNotIf = defaultValue.getNotIfCondition();

                    if (param.isShow(conditionIf, conditionNotIf, listParam)) {
                        if (param.getField().equals(EParameterFieldType.CHECK)) {
                            param.setValue(new Boolean(defaultValue.getDefaultValue()));
                        } else {
                            param.setValue(defaultValue.getDefaultValue());
                        }
                    }
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
                    listItemsDisplayCodeValue[k] = "SCHEMA_COLUMN";
                    listItemsDisplayValue[k] = "Column";
                    listField[k] = "";
                    listRepositoryItem[k] = "";
                    listItemsShowIf[k] = null;
                    listItemsNotShowIf[k] = null;
                    newParam = new ElementParameter(node);
                    newParam.setName("SCHEMA_COLUMN");
                    newParam.setDisplayName("");
                    newParam.setField(EParameterFieldType.TEXT);
                    newParam.setValue("");
                    listItemsValue[k] = newParam;
                    continue;
                } else {
                    currentItem = k - 1;
                }
            }
            item = (ITEMType) items.getITEM().get(currentItem);
            listItemsDisplayCodeValue[k] = item.getNAME();
            listItemsDisplayValue[k] = getTranslatedValue(paramName + ".ITEM." + item.getNAME());
            if (type != EParameterFieldType.TABLE) {
                listItemsValue[k] = item.getVALUE();
            } else {
                EParameterFieldType currentField = EParameterFieldType.getFieldTypeByName(item.getFIELD());
                newParam = new ElementParameter(node);
                newParam.setName(item.getNAME());
                newParam.setDisplayName("");
                newParam.setField(currentField);
                switch (currentField) {
                case CLOSED_LIST:
                case COLUMN_LIST:
                case PREV_COLUMN_LIST:
                    addItemsPropertyParameters(paramName + ".ITEM." + item.getNAME(), item.getITEMS(), newParam,
                            currentField, node);
                    break;
                case CHECK:
                    newParam.setValue(new Boolean(item.getVALUE()));
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
            Object defaultValue = "";
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

    public Boolean isMultipleMethods(ECodeLanguage language) {
        // language is not used anymore for the moment.

        Boolean multiple = null;
        TEMPLATEType tempType;
        EList listTempType = compType.getCODEGENERATION().getTEMPLATES().getTEMPLATE();

        if (listTempType.size() == 1) {
            tempType = (TEMPLATEType) listTempType.get(0);
            if (tempType.isSetMULTIPLEMETHODS()) {
                multiple = new Boolean(tempType.isMULTIPLEMETHODS());
            }
        }
        return multiple;
    }

    private ImageDescriptor getImage(String name) {
        URL url;
        try {
            url = new URL(file.getParentFile().toURL() + name);
            return ImageDescriptor.createFromURL(url);
        } catch (MalformedURLException e) {
            ExceptionHandler.process(new SystemException("Cannot load component icon " + name, e));
            return ImageProvider.getImageDesc(EImage.DEFAULT_IMAGE);
        }
    }

    public ImageDescriptor getIcon32() {
        if (image32 == null) {
            image32 = getImage(compType.getHEADER().getICON32());
        }
        return image32;
    }

    public ImageDescriptor getIcon24() {
        if (image24 == null) {
            if (compType.getHEADER().getICON24() != null && compType.getHEADER().getICON24().length() > 0) {
                image24 = getImage(compType.getHEADER().getICON24());
            } else {
                image24 = getIcon32(); // Temporary while 24 pix components icons aren't available
                // image24 = ImageDescriptor.createFromImageData(getIcon32().getImageData().scaledTo(24, 24));
            }
        }

        return image24;
    }

    public ImageDescriptor getIcon16() {
        if (image16 == null) {
            if (compType.getHEADER().getICON16() != null && compType.getHEADER().getICON16().length() > 0) {
                image16 = getImage(compType.getHEADER().getICON16());
            } else {
                image16 = ImageDescriptor.createFromImageData(getIcon32().getImageData().scaledTo(16, 16));
            }
        }

        return image16;
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
            nodeConnector = new NodeConnector();
            nodeConnector.setConnectionType(EConnectionType.getTypeFromName(connType.getCTYPE()));
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
            listConnector.add(nodeConnector);
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

                String msg = getTranslatedValue(importType.getNAME() + ".INFO");
                if (msg.startsWith(Messages.KEY_NOT_FOUND_PREFIX)) {
                    msg = Messages.getString("modules.required");
                }

                ModuleNeeded componentImportNeeds = new ModuleNeeded(this, importType.getMODULE(), msg, importType
                        .isREQUIRED());

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
}
