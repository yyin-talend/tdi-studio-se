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

import java.util.Arrays;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.AddPropertyDieOnErrorOptionConversion;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;

/**
 * 1. For new jobs : Die on Error should be unchecked by default for all components having this option (tDBOutput, and
 * tFileInput) 2. For old jobs, keep the ckeckbox checked with a migration task on the same components
 */
public class DieOnErrorDefaultValueMigrationTask extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.ProcessItem)
     */
    @Override
    public ExecutionResult executeOnProcess(ProcessItem item) {
        String[] componentsName = new String[] { "tAccessOutput", "tAccessRow", "tDB2Output", "tDB2Row", "tDBOutput",
                "tDBSQLRow", "tFileInputDelimited", "tFileInputExcel", "tFileInputLDIF", "tFileInputPositional",
                "tFileInputRegex", "tFirebirdOutput", "tFirebirdRow", "tHSQLDbOutput", "tHSQLDbRow", "tInformixOutput",
                "tInformixRow", "tIngresOutput", "tIngresRow", "tInterbaseOutput", "tInterbaseRow", "tJDBCOutput", "tJDBCRow",
                "tJavaDBOutput", "tJavaDBRow", "tMSSqlOutput", "tMSSqlRow", "tMysqlOutput", "tMysqlRow", "tOracleOutput",
                "tOracleRow", "tPostgresqlOutput", "tPostgresqlRow", "tSQLiteOutput", "tSQLiteRow", "tSybaseOutput",
                "tSybaseRow", "tTeradataFastLoad", "tTeradataOutput", "tFileInputCSV" };

        try {

            for (int i = 0; i < componentsName.length; i++) {
                IComponentFilter filter = new NameComponentFilter(componentsName[i]); //$NON-NLS-1$
                IComponentConversion addPropertyDieOnError = new AddPropertyDieOnErrorOptionConversion(); //$NON-NLS-1$
                ModifyComponentsAction.searchAndModify(item, filter, Arrays.<IComponentConversion> asList(addPropertyDieOnError));
            }

            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

}
