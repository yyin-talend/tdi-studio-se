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

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class RenametMysqlOutputBulkExecMigrationTask extends AbstractJobMigrationTask {

    public ExecutionResult executeOnProcess(ProcessItem item) {
        try {
            ModifyComponentsAction.searchAndRename(item, "tMysqlOutputBulkExec", "tMysqlBulkExec"); //$NON-NLS-1$ //$NON-NLS-2$
            ModifyComponentsAction.searchAndRename(item, "tMysqlOutputBulk", "tMysqlOutputBulkExec"); //$NON-NLS-1$ //$NON-NLS-2$
            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

}
