// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.utils;

import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;

/**
 * DOC YeXiaowei class global comment. Detailled comment
 */
public final class SAPParametersUtils {

    /**
     * DOC xye Comment method "retrieveSAPParams".
     * 
     * @param param
     */
    @SuppressWarnings("unchecked")
    public static void retrieveSAPParams(final Element elem, final Connection connection, final IElementParameter param,
            final String sapFunctionName) {
        if (param.getRepositoryValue() == null) {
            return;
        }
        if (param.getField().equals(EParameterFieldType.TEXT) && param.getRepositoryValue().equals("SAP_FUNCTION")) {
            if (connection != null && sapFunctionName != null) {
                param.setValue(TalendTextUtils.addQuotes(sapFunctionName));
                param.setRepositoryValueUsed(true);
                param.setReadOnly(true);
            } else {
                param.setRepositoryValueUsed(false);
                param.setReadOnly(false);
            }
        } else if (param.getField().equals(EParameterFieldType.TABLE) && param.getRepositoryValue().equals("INPUT_PARAMS")) {
            if (connection != null && sapFunctionName != null) {
                List<Map<String, Object>> table = (List<Map<String, Object>>) elem.getPropertyValue(param.getName());
                RepositoryToComponentProperty.getSAPInputAndOutputValue((SAPConnection) connection, table, sapFunctionName, true);
                param.setRepositoryValueUsed(true);
                param.setReadOnly(true);
            } else {
                param.setRepositoryValueUsed(false);
                param.setReadOnly(false);
            }
        } else if (param.getField().equals(EParameterFieldType.TABLE) && param.getRepositoryValue().equals("OUTPUT_PARAMS")) {
            if (connection != null && sapFunctionName != null) {
                List<Map<String, Object>> table = (List<Map<String, Object>>) elem.getPropertyValue(param.getName());
                RepositoryToComponentProperty
                        .getSAPInputAndOutputValue((SAPConnection) connection, table, sapFunctionName, false);
                param.setRepositoryValueUsed(true);
                param.setReadOnly(true);
            } else {
                param.setRepositoryValueUsed(false);
                param.setReadOnly(false);
            }
        } else if (param.getRepositoryValue().equals("SAP_ITERATE_OUT_TYPE")) {
            if (connection != null && sapFunctionName != null) {
                param.setValue(RepositoryToComponentProperty.getSAPValuesForFunction((SAPConnection) connection, sapFunctionName,
                        "SAP_ITERATE_OUT_TYPE"));
                param.setRepositoryValueUsed(true);
                param.setReadOnly(true);
            } else {
                param.setRepositoryValueUsed(false);
                param.setReadOnly(false);
            }
        } else if (param.getRepositoryValue().equals("SAP_ITERATE_OUT_TABLENAME")) {
            if (connection != null && sapFunctionName != null) {
                param.setValue(RepositoryToComponentProperty.getSAPValuesForFunction((SAPConnection) connection, sapFunctionName,
                        "SAP_ITERATE_OUT_TABLENAME"));
                param.setRepositoryValueUsed(true);
                param.setReadOnly(true);
            } else {
                param.setRepositoryValueUsed(false);
                param.setReadOnly(false);
            }
        }
    }

    public static void setNoRepositoryParams(final IElementParameter param) {
        if (param == null) {
            return;
        }
        if ((param.getField().equals(EParameterFieldType.TABLE) && param.getName().equals("MAPPING_INPUT"))
                || (param.getField().equals(EParameterFieldType.TABLE) && param.getName().equals("MAPPING_OUTPUT"))
                || (param.getName().equals("SAP_ITERATE_OUT_TYPE")) || param.getName().equals("SAP_ITERATE_OUT_TABLENAME")
                || param.getName().equals("SAP_FUNCTION")) {
            param.setRepositoryValueUsed(false);
            param.setReadOnly(false);
        }
    }
}
