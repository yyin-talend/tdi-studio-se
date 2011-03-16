// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * Migration task use to rename component name tELTXXX to tSQLTemplateXXX.
 */
public class RenametELTMigrationTask extends AbstractJobMigrationTask {

    public ExecutionResult execute(Item item) {
        String[] source = { "tELT", "tELTAggregate", "tELTCommit", "tELTFilterColumns", "tELTFilterRows", "tELTMerge",
                "tELTRollback", };
        String[] target = { "tSQLTemplate", "tSQLTemplateAggregate", "tSQLTemplateCommit", "tSQLTemplateFilterColumns",
                "tSQLTemplateFilterRows", "tSQLTemplateMerge", "tSQLTemplateRollback" };
        ProcessType processType = getProcessType(item);
        if (getProject().getLanguage() != ECodeLanguage.JAVA || processType == null) {
            return ExecutionResult.NOTHING_TO_DO;
        }
        try {
            for (int i = 0; i < source.length; i++) {
                ModifyComponentsAction.searchAndRename(item, processType, source[i], target[i]); //$NON-NLS-1$ //$NON-NLS-2$           
            }
            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2011, 3, 14, 12, 0, 0);
        return gc.getTime();
    }
}
