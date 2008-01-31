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
package org.talend.repository.model.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.conversions.UpdatePropertyComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class UpdateDbtypeOfCreateTableTask extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.ProcessItem)
     */
    @Override
    public ExecutionResult executeOnProcess(ProcessItem item) {

        final String dbtypes[] = { "ACCESS", "AS400", "DB2", "FIREBIRD", "HSQLDB", "INFORMIX", "INGRES", "INTERBASE", "JAVADB",
                "MSSQL", "DBORACLE", "POSTGRE", "SQLITE", "SYBASE", "ODBC" };

        final String name = "tCreateTable";
        final String property = "DBTYPE";

        IComponentFilter filert = new IComponentFilter() {

            public boolean accept(NodeType node) {

                boolean toReturn = (name == null ? true : acceptS(node));
                if (toReturn) {
                    String pValue = ComponentUtilities.getNodePropertyValue(node, property);
                    toReturn = !Arrays.asList(dbtypes).contains(pValue);
                }
                return toReturn;
            }

            public boolean acceptS(NodeType node) {
                return node.getComponentName().equals(name);
            }

        };

        IComponentConversion conversion = new UpdatePropertyComponentConversion("DBTYPE", "MYSQL");
        List<IComponentConversion> cons = new ArrayList<IComponentConversion>();
        cons.add(conversion);

        try {

            ModifyComponentsAction.searchAndModify(filert, cons);
            return ExecutionResult.SUCCESS_WITH_ALERT;

        } catch (Exception e) {

            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;

        }

    }
}
