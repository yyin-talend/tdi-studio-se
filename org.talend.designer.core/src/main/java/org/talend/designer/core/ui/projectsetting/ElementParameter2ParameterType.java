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

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.IEbcdicConstant;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.core.ui.editor.process.Process;
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

        List<IElementParameter> paramList = (List<IElementParameter>) elem.getElementParameters();
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
    public static void loadElementParameters(Element elemParam, ParametersType paType) {
        EList listParamType = paType.getElementParameter();
        for (int j = 0; j < listParamType.size(); j++) {
            ElementParameterType pType = (ElementParameterType) listParamType.get(j);
            if (pType != null) {
                IElementParameter param = elemParam.getElementParameter(pType.getName());
                if (param != null) {
                    param.setContextMode(pType.isContextMode());
                    if (param.isReadOnly()
                            && !(param.getName().equals(EParameterName.UNIQUE_NAME.getName()) || param.getName().equals(
                                    EParameterName.VERSION.getName()))) {
                        continue; // if the parameter is read only, don't load
                        // it (this will prevent to overwrite the
                        // value)
                    }
                    String value = pType.getValue();
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

}
