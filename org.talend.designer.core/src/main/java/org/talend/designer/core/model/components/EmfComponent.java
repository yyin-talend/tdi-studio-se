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
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.metadata.EMetadataType;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.utils.emf.component.COMPONENTType;
import org.talend.designer.core.model.utils.emf.component.CONNECTORType;
import org.talend.designer.core.model.utils.emf.component.DEFAULTType;
import org.talend.designer.core.model.utils.emf.component.DocumentRoot;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.component.ITEMSType;
import org.talend.designer.core.model.utils.emf.component.ITEMType;
import org.talend.designer.core.model.utils.emf.component.PARAMETERType;
import org.talend.designer.core.model.utils.emf.component.RETURNType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATEPARAMType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATESType;
import org.talend.designer.core.model.utils.emf.component.TEMPLATEType;
import org.talend.designer.core.model.utils.emf.component.util.ComponentResourceFactoryImpl;

/**
 * 
 * Component manager that read each information in a xml file with Emf. <br/> $Id: EmfComponent.java,v 1.12 2006/06/08
 * 14:12:27 nrousseau Exp $
 */
public class EmfComponent implements IComponent {

    private File file;

    private static final long serialVersionUID = 1L;

    private boolean isLoaded = false;

    private COMPONENTType compType;

    private ImageDescriptor image;

    private ECodeLanguage codeLanguage;

    public static final String BUILTIN = "BUILT_IN"; //$NON-NLS-1$

    public static final String REPOSITORY = "REPOSITORY"; //$NON-NLS-1$

    private static final String TEXT_BUILTIN = "Built-In";

    private static final String TEXT_REPOSITORY = "Repository";
    
    private IMultipleComponentManager multipleComponentManager;

    public EmfComponent(File file) {
        this.file = file;
        load();
        codeLanguage = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getProject().getLanguage();
    }

    public boolean isPropagateSchema() {
        if (getName().equals("tFileOutputDelimited")) {
            return true;
        }
        return false;
    }

    private String getTranslatedValue(final String nameValue) {

        String returnValue = nameValue;
        String compName = file.getParentFile().getName();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("components." + compName + ".component");
        returnValue = Messages.getString(nameValue, resourceBundle);
        return returnValue;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void load() {
        ResourceSet resourceSet = new ResourceSetImpl();
        ComponentResourceFactoryImpl compFact;
        compFact = new ComponentResourceFactoryImpl();
        compFact.createResource(URI.createURI(file.toURI().toString()));
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
                Resource.Factory.Registry.DEFAULT_EXTENSION, compFact);

        Resource res = resourceSet.getResource(URI.createURI(file.toURI().toString()), true);

        DocumentRoot xmlDoc;
        xmlDoc = (DocumentRoot) res.getContents().get(0);

        compType = (COMPONENTType) xmlDoc.eContents().get(0);
        
        loadMultipleComponentManagerFromTemplates();
        
        isLoaded = true;
    }

    public List<ElementParameter> createParameters() {
        List<ElementParameter> listParam;

        if (!isLoaded) {
            load();
        }
        listParam = new ArrayList<ElementParameter>();
        addMainParameters(listParam);
        addPropertyParameters(listParam);
        initializePropertyParameters(listParam);
        addViewParameters(listParam);
        addDocParameters(listParam);
        return listParam;
    }

    public List<NodeReturn> createReturns() {
        List<NodeReturn> listReturn;
        RETURNType retType;
        EList returnList;
        NodeReturn nodeRet;

        if (!isLoaded) {
            load();
        }
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

    private void addDocParameters(final List<ElementParameter> listParam) {
        ElementParameter param;

        param = new ElementParameter();
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

    private void addViewParameters(final List<ElementParameter> listParam) {
        ElementParameter param;

        param = new ElementParameter();
        param.setName(EParameterName.LABEL.getName());
        param.setDisplayName(EParameterName.LABEL.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.VIEW);
        param.setNumRow(1);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        listParam.add(param);

        param = new ElementParameter();
        param.setName(EParameterName.HINT.getName());
        param.setDisplayName(EParameterName.HINT.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.VIEW);
        param.setNumRow(2);
        param.setReadOnly(false);
        param.setRequired(false);
        param.setShow(true);
        listParam.add(param);

        param = new ElementParameter();
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

    public void addMainParameters(final List<ElementParameter> listParam) {
        ElementParameter param;

        param = new ElementParameter();
        param.setName(EParameterName.UNIQUE_NAME.getName());
        param.setValue("");
        param.setDisplayName(EParameterName.UNIQUE_NAME.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(true);
        listParam.add(param);

        param = new ElementParameter();
        param.setName(EParameterName.COMPONENT_NAME.getName());
        param.setValue(getTranslatedValue(PROP_NAME));
        param.setDisplayName(EParameterName.COMPONENT_NAME.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setShow(false);
        listParam.add(param);

        param = new ElementParameter();
        param.setName(EParameterName.VERSION.getName());
        param.setValue(compType.getHEADER().getVERSION() + " (" + compType.getHEADER().getSTATUS() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        param.setDisplayName(EParameterName.VERSION.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.MAIN);
        param.setNumRow(2);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(true);
        listParam.add(param);

        param = new ElementParameter();
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

        param = new ElementParameter();
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
            param = new ElementParameter();
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

        param = new ElementParameter();
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

        param = new ElementParameter();
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

        param = new ElementParameter();
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

        param = new ElementParameter();
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

        // commented -> see tMap.xml
        // param = new ElementParameter();
        // param.setName(EParameterName.PREVIEW.getName());
        // param.setValue("");
        // param.setDisplayName(EParameterName.PREVIEW.getDisplayName());
        // param.setField(EParameterFieldType.IMAGE);
        // param.setCategory(EComponentCategory.PROPERTY);
        // param.setNumRow(2);
        // param.setReadOnly(false);
        // param.setRequired(false);
        // param.setShow(true);
        // listParam.add(param);

    }

    @SuppressWarnings("unchecked")
    public void addPropertyParameters(final List<ElementParameter> listParam) {
        EList listXmlParam;
        PARAMETERType xmlParam;
        ElementParameter param;

        listXmlParam = compType.getPARAMETERS().getPARAMETER();
        for (int i = 0; i < listXmlParam.size(); i++) {
            xmlParam = (PARAMETERType) listXmlParam.get(i);
            EParameterFieldType type = EParameterFieldType.getFieldTypeByName(xmlParam.getFIELD());
            if (type == EParameterFieldType.PROPERTY_TYPE) {
                ElementParameter newParam = new ElementParameter();
                newParam.setCategory(EComponentCategory.PROPERTY);
                newParam.setName(EParameterName.PROPERTY_TYPE.getName());
                newParam.setDisplayName(EParameterName.PROPERTY_TYPE.getDisplayName());
                newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
                newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
                newParam.setValue(BUILTIN);
                newParam.setNumRow(xmlParam.getNUMROW());
                newParam.setField(EParameterFieldType.CLOSED_LIST);
                newParam.setRepositoryValue(xmlParam.getREPOSITORYVALUE());
                if (xmlParam.isSetSHOW()) {
                    newParam.setShow(xmlParam.isSHOW());
                }
                listParam.add(newParam);

                newParam = new ElementParameter();
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
                ElementParameter newParam = new ElementParameter();
                newParam.setCategory(EComponentCategory.PROPERTY);
                newParam.setName(EParameterName.SCHEMA_TYPE.getName());
                newParam.setDisplayName(EParameterName.SCHEMA_TYPE.getDisplayName());
                newParam.setListItemsDisplayName(new String[] { TEXT_BUILTIN, TEXT_REPOSITORY });
                newParam.setListItemsValue(new String[] { BUILTIN, REPOSITORY });
                newParam.setValue(BUILTIN);
                newParam.setNumRow(xmlParam.getNUMROW());
                newParam.setField(EParameterFieldType.CLOSED_LIST);
                if (xmlParam.isSetSHOW()) {
                    newParam.setShow(xmlParam.isSHOW());
                }
                listParam.add(newParam);

                newParam = new ElementParameter();
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
                listParam.add(newParam);
            }
            if (type == EParameterFieldType.PROCESS_TYPE) {
                ElementParameter newParam = new ElementParameter();
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

                newParam = new ElementParameter();
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
            param = new ElementParameter();
            param.setName(xmlParam.getNAME());
            param.setDisplayName(getTranslatedValue(xmlParam.getNAME() + "." + PROP_NAME));
            param.setField(type);
            if (type.equals(EParameterFieldType.CHECK)) {
                param.setValue(new Boolean(false));
            } else {
                if (type.equals(EParameterFieldType.TABLE)) {
                    param.setValue(new ArrayList<Map<String, String>>());
                } else {
                    param.setValue("");
                }
            }
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

            addItemsPropertyParameters(listParam, xmlParam, param, type);

            param.setCategory(EComponentCategory.PROPERTY);
            listParam.add(param);
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

    public void addItemsPropertyParameters(final List<ElementParameter> listParam, PARAMETERType xmlParam,
            ElementParameter param, EParameterFieldType type) {
        ITEMType item;

        if (xmlParam.getITEMS() != null) {
            EList listItems = xmlParam.getITEMS();
            for (int j = 0; j < listItems.size(); j++) {
                ITEMSType items = (ITEMSType) listItems.get(0);
                ECodeLanguage language = ECodeLanguage.getCodeLanguage(items.getCODELANGUAGE());

                int nbItems = items.getITEM().size();
                String[] listRepositoryItem = new String[nbItems];
                String[] listItemsDisplayValue = new String[nbItems];
                String[] listItemsDisplayCodeValue = new String[nbItems];
                String[] listItemsValue = new String[nbItems];
                String[] listItemsShowIf = new String[nbItems];
                String[] listItemsNotShowIf = new String[nbItems];
                for (int k = 0; k < nbItems; k++) {
                    item = (ITEMType) items.getITEM().get(k);
                    listItemsDisplayCodeValue[k] = item.getNAME();
                    listItemsDisplayValue[k] = getTranslatedValue(xmlParam.getNAME() + "." + language.getName()
                            + ".ITEM." + item.getNAME());
                    if (!type.equals(EParameterFieldType.TABLE)) {
                        listItemsValue[k] = item.getVALUE();
                    } else {
                        listItemsValue[k] = item.getNAME();
                    }
                    listRepositoryItem[k] = item.getREPOSITORYITEM();
                    listItemsShowIf[k] = item.getSHOWIF();
                    listItemsNotShowIf[k] = item.getNOTSHOWIF();
                }

                param.setListItemsDisplayName(language, listItemsDisplayValue);
                param.setListItemsDisplayCodeName(language, listItemsDisplayCodeValue);
                param.setListItemsValue(language, listItemsValue);
                param.setListRepositoryItems(language, listRepositoryItem);
                param.setListItemsShowIf(language, listItemsShowIf);
                param.setListItemsNotShowIf(language, listItemsNotShowIf);
                if (!type.equals(EParameterFieldType.TABLE)) {
                    Object defaultValue;
                    if (items.getDEFAULT() != null) {
                        defaultValue = new String(items.getDEFAULT());
                    } else {
                        defaultValue = "";
                    }
                    param.setDefaultClosedListValue(language, defaultValue);
                    if (language == codeLanguage) {
                        param.setValue(defaultValue);
                    }
                }
            }
        }
    }

    public String getFamily() {
        if (!isLoaded) {
            load();
        }
        return getTranslatedValue(PROP_FAMILY);
    }

    public boolean isMultipleMethods(ECodeLanguage language) {
        // language is not used anymore for the moment.

        boolean multiple = false;
        if (!isLoaded) {
            load();
        }
        TEMPLATEType tempType;
        EList listTempType = compType.getCODEGENERATION().getTEMPLATES().getTEMPLATE();
        for (int i = 0; i < listTempType.size(); i++) {
            tempType = (TEMPLATEType) listTempType.get(i);

            if (tempType.getNAME().equals(getName())) {
                if (tempType.isSetMULTIPLEMETHODS()) {
                    multiple = tempType.isMULTIPLEMETHODS();
                }
            }
        }
        return multiple;
    }

    public ImageDescriptor getImageDescriptor() {
        if (!isLoaded) {
            load();
        }
        if (image == null) {
            URL url;
            try {
                url = new URL(file.getParentFile().toURL() + compType.getHEADER().getICON());
                image = ImageDescriptor.createFromURL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public String getName() {
        if (!isLoaded) {
            load();
        }
        return getTranslatedValue(PROP_NAME);
    }

    public String getLongName() {
        if (!isLoaded) {
            load();
        }
        return getTranslatedValue(PROP_LONG_NAME);
    }

    public boolean canStart() {
        if (!isLoaded) {
            load();
        }
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
        if (!isLoaded) {
            load();
        }
        if (compType.getHEADER().isSetSCHEMAAUTOPROPAGATE()) {
            return compType.getHEADER().isSCHEMAAUTOPROPAGATE();
        } else {
            return false;
        }
    }

    public boolean isDataAutoPropagated() {
        if (!isLoaded) {
            load();
        }
        if (compType.getHEADER().isSetDATAAUTOPROPAGATE()) {
            return compType.getHEADER().isDATAAUTOPROPAGATE();
        } else {
            return false;
        }
    }

    public boolean isCheckColumns() {
        if (!isLoaded) {
            load();
        }
        if (compType.getHEADER().isSetCOLUMNCHECK()) {
            return compType.getHEADER().isCOLUMNCHECK();
        } else {
            return true;
        }
    }

    public String getVersion() {
        if (!isLoaded) {
            load();
        }
        return String.valueOf(compType.getHEADER().getVERSION());
    }

    public List<ComponentImportNeeds> getImportsNeeded() {
        List<ComponentImportNeeds> componentImportNeedsList = new ArrayList<ComponentImportNeeds>();
        if (compType.getCODEGENERATION().getIMPORTS() != null) {
            EList emfImportList;
            emfImportList = compType.getCODEGENERATION().getIMPORTS().getIMPORT();
            for (int i = 0; i < emfImportList.size(); i++) {
                IMPORTType importType = (IMPORTType) emfImportList.get(i);
                ComponentImportNeeds componentImportNeeds = new ComponentImportNeeds();

                componentImportNeeds.setName(importType.getNAME());
                componentImportNeeds.setComponentName(getName());
                componentImportNeeds.setModuleName(importType.getMODULE());
                componentImportNeeds.setRequired(importType.isREQUIRED());

                String msg = getTranslatedValue(importType.getNAME() + ".INFO");
                if (msg.startsWith(Messages.KEY_NOT_FOUND_PREFIX)) {
                    msg = Messages.getString("modules.required");
                }
                componentImportNeeds.setInformationMsg(msg);

                componentImportNeedsList.add(componentImportNeeds);
            }
        }
        return componentImportNeedsList;
    }
    
    public IMultipleComponentManager getMultipleComponentManager() {
        if (!isLoaded) {
            load();
        }
        return this.multipleComponentManager;
    }
    
    /**
     * DOC nrousseau Comment method "loadMultipleComponentManagerFromTemplates".
     */
    private void loadMultipleComponentManagerFromTemplates() {
        TEMPLATEType templateType;
        TEMPLATEPARAMType templateParamType;
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
            templateType = (TEMPLATEType) listTempType.get(i);
            
            String name, connection, linkTo;
            name = templateType.getNAME();
            connection = templateType.getCTYPE();
            linkTo = templateType.getLINKTO();
            
            multipleComponentManager.addItem(name, connection, linkTo);
        }
        
        EList listTempParamType = templatesType.getTEMPLATEPARAM();
        for (int i = 0; i < listTempParamType.size(); i++) {
            templateParamType = (TEMPLATEPARAMType) listTempParamType.get(i);
            
            String source, target;
            source = templateParamType.getSOURCE();
            target = templateParamType.getTARGET();
            
            multipleComponentManager.addParam(source, target);
        }
        
        multipleComponentManager.validateItems();
    }
}
