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
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.conversions.UpdateAttributesFortFilterRow;
import org.talend.core.model.components.conversions.UpdatePropertyComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class UpgradeAttributestFilterRow extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.ProcessItem)
     */
    @Override
    public ExecutionResult executeOnProcess(ProcessItem item) {
        try {
            // tFilterRow:
            IComponentFilter filter1 = new NameComponentFilter("tFilterRow"); //$NON-NLS-1$
            // Change the value of LOGICAL_OP
            IComponentConversion setPriorityProperty = new UpdatePropertyComponentConversion("LOGICAL_OP", "||"); //$NON-NLS-1$
            ModifyComponentsAction.searchAndModify(item, filter1, Arrays.<IComponentConversion> asList(setPriorityProperty));
            // Change the value of the column "FUNCTION" of table "CONDITIONS"
            IComponentConversion setPriorityProperty2 = new UpdateAttributesFortFilterRow("CONDITIONS", "FUNCTION", "");
            ModifyComponentsAction.searchAndModify(item, filter1, Arrays.<IComponentConversion> asList(setPriorityProperty2));
            // Change the value of the column "OPERATOR" of table "CONDITIONS"
            IComponentConversion setPriorityProperty3 = new UpdateAttributesFortFilterRow("CONDITIONS", "OPERATOR", "==");
            ModifyComponentsAction.searchAndModify(item, filter1, Arrays.<IComponentConversion> asList(setPriorityProperty3));

            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }
}
