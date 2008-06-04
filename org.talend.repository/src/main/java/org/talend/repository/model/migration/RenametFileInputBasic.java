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

import java.util.Date;
import java.util.GregorianCalendar;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;

/**
 * Migration task to rename tFileInputBasic into tFileInputFullRow.
 */
public class RenametFileInputBasic extends AbstractJobMigrationTask {

    public ExecutionResult executeOnProcess(ProcessItem item) {
        try {
            ModifyComponentsAction.searchAndRename(item, "tFileInputBasic", "tFileInputFullRow"); //$NON-NLS-1$ //$NON-NLS-2$
            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2008, 6, 4, 10, 39, 12);
        return gc.getTime();
    }
}
