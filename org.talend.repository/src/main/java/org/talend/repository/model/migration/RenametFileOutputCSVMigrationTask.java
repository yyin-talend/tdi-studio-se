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
import org.talend.core.model.components.conversions.AddPropertyCSVOptionConversion;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.conversions.RenameComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;

/**
 * Migration task use to rename component name tFileOutputCSV to tFileOutputDelimited.
 */
public class RenametFileOutputCSVMigrationTask extends AbstractJobMigrationTask {

    public ExecutionResult executeOnProcess(ProcessItem item) {

        try {
            IComponentFilter filter1 = new NameComponentFilter("tFileOutputCSV"); //$NON-NLS-1$

            IComponentConversion addProperty = new AddPropertyCSVOptionConversion();
            IComponentConversion renameComponent = new RenameComponentConversion("tFileOutputDelimited");

            ModifyComponentsAction.searchAndModify(item, filter1, Arrays.<IComponentConversion> asList(addProperty,
                    renameComponent));

            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }
}
