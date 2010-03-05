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
package org.talend.designer.mapper.utils;

import java.util.HashMap;
import java.util.List;

import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INode;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class MapperHelper {

    /**
     * 
     * DOC YeXiaowei Comment method "isGeneratedAsVirtualComponent".
     * 
     * @param mapperNode
     * @return
     */
    public static boolean isGeneratedAsVirtualComponent(final INode mapperNode) {

        boolean hasPersistentSortedLookup = false;

        List<IConnection> incomingConnections = (List<IConnection>) mapperNode.getIncomingConnections();

        ExternalMapperData internalExternalData = (ExternalMapperData) mapperNode.getExternalNode().getExternalData();

        if (internalExternalData != null && incomingConnections.size() > 0) {
            List<ExternalMapperTable> inputTables = internalExternalData.getInputTables();

            int sizeInputTables = inputTables.size();

            HashMap<String, IConnection> hNameToConnection = new HashMap<String, IConnection>();
            for (IConnection connection : incomingConnections) {
                hNameToConnection.put(connection.getName(), connection);
            }

            for (int iInputTable = 1; iInputTable < sizeInputTables; iInputTable++) { // T_TM_M_241
                ExternalMapperTable inputTable = inputTables.get(iInputTable);
                String tableName = inputTable.getName();
                IConnection connection = hNameToConnection.get(tableName);
                if (connection == null) {
                    continue;
                }

                if (inputTable != null) { // T_TM_M_245
                    List<ExternalMapperTableEntry> metadataTableEntries = inputTable.getMetadataTableEntries();
                    if (metadataTableEntries == null) {
                        continue;
                    }

                    if (inputTable.isPersistent()
                            && !"ALL_ROWS".equals(inputTable.getMatchingMode()) && !"DYNAMIC".equals(inputTable.getLookupMode())) { //$NON-NLS-1$  //$NON-NLS-2$
                        hasPersistentSortedLookup = true;
                    }

                } // T_TM_M_245
            } // T_TM_M_241
        }
        return hasPersistentSortedLookup;
    }
}
