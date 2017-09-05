// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dbmap.service;

import java.util.List;

import org.talend.core.model.process.IExternalNode;
import org.talend.core.service.IDbMapService;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.dbmap.DbMapComponent;
import org.talend.designer.dbmap.model.emf.dbmap.DBMapData;
import org.talend.designer.dbmap.model.emf.dbmap.DBMapperTableEntry;
import org.talend.designer.dbmap.model.emf.dbmap.InputTable;
import org.talend.designer.dbmap.model.emf.dbmap.OutputTable;

/**
 * DOC talend class global comment. Detailled comment
 */
public class DbMapService implements IDbMapService {

    @Override
    public boolean isDbMapComponent(IExternalNode node) {
        if (node instanceof DbMapComponent) {
            return true;
        }
        return false;
    }

    @Override
    public void updateEMFDBMapData(NodeType nodeType, String oldValue, String newValue) {
        AbstractExternalData nodeData = nodeType.getNodeData();
        if (nodeData instanceof DBMapData) {
            DBMapData dbMapData = (DBMapData) nodeData;
            for (InputTable input : dbMapData.getInputTables()) {
                if (input.getName().equals(oldValue) || input.getTableName().equals(oldValue)) {
                    input.setName(newValue);
                    input.setTableName(newValue);
                }
            }
            for (OutputTable output : dbMapData.getOutputTables()) {
                List<DBMapperTableEntry> entries = output.getDBMapperTableEntries();
                for (DBMapperTableEntry entry : entries) {
                    String expression = entry.getExpression();
                    if (expression != null && !"".equals(expression.trim())) { //$NON-NLS-1$
                        int index = expression.lastIndexOf("."); //$NON-NLS-1$
                        // at least "a.b"
                        if (index > 0) {
                            String connectionName = expression.substring(0, index);
                            if (oldValue.equals(connectionName)) {
                                entry.setExpression(newValue + expression.substring(index, expression.length()));
                            }
                        }
                    }
                }
            }
        }
        
    }

}
