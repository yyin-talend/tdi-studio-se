// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.projectsetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.IEbcdicConstant;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.migration.UpdateTheJobsActionsOnTable;

/**
 * Helper class to Convert ElementParameter and ParameterType
 */
public class ElementParameter2ParameterType {

    /**
     * save the EMF Model's parameters to Element
     * 
     * @param elem
     * @param pType
     */
    public static void saveElementParameters(Element elem, ParametersType pType) {
        EList listParamType = pType.getElementParameter();
        listParamType.clear();

        List<IElementParameter> paramList = (List<IElementParameter>) elem.getElementParametersWithChildrens();
        for (IElementParameter param : paramList) {
            ElementParameterType type = getElemeterParameterType(param);
            listParamType.add(type);
        }
    }

    public static void setParameterValue(Element elem, String paramName, Object value) {
        IElementParameter param = elem.getElementParameter(paramName);
        if (param != null) {
            param.setValue(value);
        }
    }

    public static String getParameterValue(ParametersType paType, String paramName) {
        if (paType == null)
            return null;
        EList listParamType = paType.getElementParameter();
        for (int j = 0; j < listParamType.size(); j++) {
            ElementParameterType pType = (ElementParameterType) listParamType.get(j);
            if (pType != null && paramName.equals(pType.getName())) {
                return pType.getValue();
            }
        }
        return null;
    }

    public static boolean isContains(ParametersType paType, String paramName) {
        EList listParamType = paType.getElementParameter();
        for (int j = 0; j < listParamType.size(); j++) {
            ElementParameterType pType = (ElementParameterType) listParamType.get(j);
            if (pType != null && paramName.equals(pType.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void setParameterValue(ParametersType paType, String paramName, Object value) {
        if (value == null)
            return;
        EList listParamType = paType.getElementParameter();
        if (!isContains(paType, paramName)) {
            ElementParameterType etype = TalendFileFactory.eINSTANCE.createElementParameterType();
            etype.setName(paramName);
            listParamType.add(etype);
        }
        for (int j = 0; j < listParamType.size(); j++) {
            ElementParameterType pType = (ElementParameterType) listParamType.get(j);
            if (pType != null && paramName.equals(pType.getName())) {
                pType.setValue(value.toString());
                return;
            }
        }
    }

    public static Object getParameterValue(Element elem, String paramName) {
        IElementParameter param = elem.getElementParameter(paramName);
        if (param != null) {
            return param.getValue();
        }
        return null;
    }

    /**
     * 
     * load the Element's parameters to EMF Model
     * 
     * @param elemParam
     * @param paType
     */
    public static void loadElementParameters(Element elemParam, ParametersType paType, String repParamName) {
        EList listParamType = paType.getElementParameter();
        ElementParameterType repositoryParam = null;

        if (repParamName != null && !repParamName.equals("")) { //$NON-NLS-N$
            repositoryParam = findElementParameterType(paType, repParamName);
        } else {
            repositoryParam = findElementParameterType(paType, EParameterName.PROPERTY_TYPE.getName() + ":"
                    + EParameterName.PROPERTY_TYPE.getName());
        }

        for (int j = 0; j < listParamType.size(); j++) {
            ElementParameterType pType = (ElementParameterType) listParamType.get(j);
            if (pType != null) {
                IElementParameter param = elemParam.getElementParameter(pType.getName());
                if (param != null) {
                    String name = param.getName();
                    param.setContextMode(pType.isContextMode());
                    if (param.isReadOnly()
                            && !(name.equals(EParameterName.UNIQUE_NAME.getName()) || name.equals(EParameterName.VERSION
                                    .getName()))) {
                        continue; // if the parameter is read only, don't load
                        // it (this will prevent to overwrite the
                        // value)
                    }
                    String value = null;
                    if (name.equals("STATANDLOG_USE_PROJECT_SETTINGS") //$NON-NLS-1$
                            || name.equals("IMPLICITCONTEXT_USE_PROJECT_SETTINGS")) { //$NON-NLS-1$
                        value = param.getValue().toString();
                    } else {
                        value = pType.getValue();
                    }
                    if (param.getField().equals(EParameterFieldType.CHECK) || param.getField().equals(EParameterFieldType.RADIO)) {
                        if ("false".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || !pType.isContextMode()) { //$NON-NLS-1$ //$NON-NLS-2$
                            Boolean boolean1 = new Boolean(value);
                            elemParam.setPropertyValue(pType.getName(), boolean1);
                        } else {
                            elemParam.setPropertyValue(pType.getName(), value);
                        }
                        // if (EParameterName.ACTIVATE.getName().equals(param.getName())) {
                        // if ((elemParam instanceof Node) && !boolean1) {
                        // ((Node) elemParam).setDummy(!boolean1);
                        // }
                        // }
                    } else if (param.getField().equals(EParameterFieldType.CLOSED_LIST)) {
                        boolean valueSet = false;
                        if (!ArrayUtils.contains(param.getListItemsValue(), value)) {
                            if (ArrayUtils.contains(param.getListItemsDisplayName(), value)) {
                                valueSet = true;
                                int index = ArrayUtils.indexOf(param.getListItemsDisplayName(), value);
                                elemParam.setPropertyValue(pType.getName(), param.getListItemsValue()[index]);
                            }
                        }
                        if (!valueSet) {                    
                            elemParam.setPropertyValue(pType.getName(), value);
                        }
                    } else if (param.getField().equals(EParameterFieldType.TABLE)) {
                        List<Map<String, Object>> tableValues = new ArrayList<Map<String, Object>>();
                        String[] codeList = param.getListItemsDisplayCodeName();
                        Map<String, Object> lineValues = null;
                        for (ElementValueType elementValue : (List<ElementValueType>) pType.getElementValue()) {
                            boolean found = false;
                            for (int i = 0; i < codeList.length && !found; i++) {
                                if (codeList[i].equals(elementValue.getElementRef())) {
                                    found = true;
                                }
                            }
                            if (found) {
                                if ((lineValues == null) || (lineValues.get(elementValue.getElementRef()) != null)) {
                                    lineValues = new HashMap<String, Object>();
                                    tableValues.add(lineValues);
                                }
                                lineValues.put(elementValue.getElementRef(), elementValue.getValue());
                                if (elementValue.getType() != null) {
                                    lineValues.put(elementValue.getElementRef() + IEbcdicConstant.REF_TYPE, elementValue
                                            .getType());
                                }
                            }
                        }
                        elemParam.setPropertyValue(pType.getName(), tableValues);
                    } else if (param.getField().equals(EParameterFieldType.ENCODING_TYPE)) {
                        // fix for bug 2193
                        boolean setToCustom = false;
                        if (EmfComponent.REPOSITORY.equals(elemParam.getPropertyValue(EParameterName.PROPERTY_TYPE.getName()))
                                && param.getRepositoryValue() != null && param.getRepositoryValue().equals("ENCODING")) { //$NON-NLS-1$
                            setToCustom = true;
                        }
                        String tempValue = (String) param.getChildParameters().get(EParameterName.ENCODING_TYPE.getName())
                                .getValue();
                        if (!tempValue.equals(EmfComponent.ENCODING_TYPE_CUSTOM)) {
                            tempValue = tempValue.replaceAll("'", ""); //$NON-NLS-1$ //$NON-NLS-2$
                            tempValue = tempValue.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
                            tempValue = TalendTextUtils.addQuotes(tempValue);
                            if (!tempValue.equals(value)) {
                                setToCustom = true;
                            }
                        }

                        if (setToCustom) {
                            param.getChildParameters().get(EParameterName.ENCODING_TYPE.getName()).setValue(
                                    EmfComponent.ENCODING_TYPE_CUSTOM);
                        }
                        elemParam.setPropertyValue(pType.getName(), value);
                        // end of fix for bug 2193
                    } else if (!param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                        if (param.getRepositoryValue() != null && !param.getField().equals(EParameterFieldType.PROPERTY_TYPE)) {
                            if (repositoryParam != null && EmfComponent.REPOSITORY.equals(repositoryParam.getValue())) {
                                param.setRepositoryValueUsed(true);
                            } else {
                                param.setRepositoryValueUsed(false);
                            }
                        }
                        elemParam.setPropertyValue(pType.getName(), value);
                    }
                } else if (UpdateTheJobsActionsOnTable.isClear && "CLEAR_TABLE".equals(pType.getName()) //$NON-NLS-1$
                        && "true".equals(pType.getValue()) //$NON-NLS-1$
                        && "NONE".equals(elemParam.getElementParameter(Process.TABLE_ACTION).getValue())) { //$NON-NLS-1$
                    elemParam.setPropertyValue(Process.TABLE_ACTION, "CLEAR"); //$NON-NLS-1$
                    UpdateTheJobsActionsOnTable.isClear = false;
                }
            }
        }
    }

    /**
     * 
     * load project settings to no-opened process
     * 
     * @param elemParam
     * @param projectPaType
     */
    public static void loadElementParameters(ParametersType processType, ParametersType projectPaType, EParameterName paramName) {
        EList listParamType = projectPaType.getElementParameter();
        for (int j = 0; j < listParamType.size(); j++) {
            ElementParameterType pType = (ElementParameterType) listParamType.get(j);
            EList processParameters = processType.getElementParameter();
            ElementParameterType processParam = null;
            for (int i = 0; i < processParameters.size(); i++) {
                ElementParameterType paramType = (ElementParameterType) processParameters.get(i);
                if (paramType.getName().equals(pType.getName()) && paramType.getField().equals(pType.getField())) {
                    processParam = paramType;
                } else if (pType.getName().contains(":")) {
                    StringTokenizer token = new StringTokenizer(pType.getName(), ":"); //$NON-NLS-1$
                    String parentId = token.nextToken();
                    String childId = token.nextToken();
                    String[] split = paramType.getName().split(":");
                    if (split.length != 2) {
                        continue;
                    }
                    String complexName = paramName + ":" + childId;
                    if (complexName.equals(paramType.getName())) {
                        processParam = paramType;
                    }

                }
                if (processParam != null) {
                    break;
                }
            }
            if (processParam != null) {
                processParam.setValue(pType.getValue());
            }

        }
    }

    public static ElementParameterType findElementParameterType(ParametersType paType, String name) {
        EList listParamType = paType.getElementParameter();
        for (int j = 0; j < listParamType.size(); j++) {
            ElementParameterType pType = (ElementParameterType) listParamType.get(j);
            if (pType.getName().equals(name)) {
                return pType;
            }
        }
        return null;
    }

    public static ElementParameterType getElemeterParameterType(IElementParameter param) {
        ElementParameterType pType;

        TalendFileFactory fileFact = TalendFileFactory.eINSTANCE;
        if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
            return null;
        }

        pType = fileFact.createElementParameterType();
        if (param.getParentParameter() != null) {
            pType.setName(param.getParentParameter().getName() + ":" + param.getName()); //$NON-NLS-1$
        } else {
            pType.setName(param.getName());
        }
        pType.setField(param.getField().getName());
        pType.setContextMode(param.isContextMode());
        Object value = param.getValue();
        if (param.getField().equals(EParameterFieldType.TABLE) && value != null) {
            List<Map<String, Object>> tableValues = (List<Map<String, Object>>) value;
            for (Map<String, Object> currentLine : tableValues) {
                for (int i = 0; i < param.getListItemsDisplayCodeName().length; i++) {
                    ElementValueType elementValue = fileFact.createElementValueType();
                    elementValue.setElementRef(param.getListItemsDisplayCodeName()[i]);
                    Object o = currentLine.get(param.getListItemsDisplayCodeName()[i]);
                    String strValue = ""; //$NON-NLS-1$
                    if (o instanceof Integer) {
                        IElementParameter tmpParam = (IElementParameter) param.getListItemsValue()[i];
                        if (tmpParam.getListItemsValue().length == 0) {
                            strValue = ""; //$NON-NLS-1$
                        } else {
                            strValue = (String) tmpParam.getListItemsValue()[(Integer) o];
                        }
                    } else {
                        if (o instanceof String) {
                            strValue = (String) o;
                        } else {
                            if (o instanceof Boolean) {
                                strValue = ((Boolean) o).toString();
                            }
                        }
                    }
                    elementValue.setValue(strValue);
                    //
                    Object object = currentLine.get(param.getListItemsDisplayCodeName()[i] + IEbcdicConstant.REF_TYPE);
                    if (object != null) {
                        elementValue.setType((String) object);
                    }
                    pType.getElementValue().add(elementValue);
                }
            }
        } else {
            if (value == null) {
                pType.setValue(""); //$NON-NLS-1$
            } else {
                if (value instanceof Boolean) {
                    pType.setValue(((Boolean) value).toString());
                } else {
                    if (value instanceof String) {
                        pType.setValue((String) value);
                    }
                }
            }
        }
        return pType;
    }

    /**
     * DOC zli Comment method "loadProjectsettingsParameters".
     * 
     * @param parameters
     */
    public static void loadProjectsettingsParameters(ParametersType parameters) {
        IEditorReference[] reference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
        IDesignerCoreService designerCoreService = CorePlugin.getDefault().getDesignerCoreService();
        designerCoreService.switchToCurJobSettingsView();
        List<IProcess> openedProcess = designerCoreService.getOpenedProcess(reference);

        for (IProcess process : openedProcess) {
            if (process instanceof Element) {
                Element processElem = (Element) process;
                ElementParameter2ParameterType.loadElementParameters(processElem, parameters, null);
            }
            process.setNeedRegenerateCode(true);
        }
        IProxyRepositoryFactory repositoryFactory = CorePlugin.getDefault().getProxyRepositoryFactory();
        IProcess process = null;

        try {
            List<IRepositoryObject> all = repositoryFactory.getAll(ERepositoryObjectType.PROCESS);
            for (IRepositoryObject object : all) {
                if (!openedProcess.contains(object)) {
                    Item item = object.getProperty().getItem();
                    if (item instanceof ProcessItem) {
                        process = designerCoreService.getProcessFromProcessItem((ProcessItem) item);
                        if (process != null && process instanceof IProcess2) {
                            IProcess2 process2 = (IProcess2) process;
                            if (process2 instanceof Element) {
                                Element processElem = (Element) process2;
                                ElementParameter2ParameterType.loadElementParameters(processElem, parameters, null);
                                ProcessType processType = process2.saveXmlFile();
                                if (processType != null) {
                                    ((ProcessItem) item).setProcess(processType);
                                }
                                repositoryFactory.save(item);
                            }

                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
