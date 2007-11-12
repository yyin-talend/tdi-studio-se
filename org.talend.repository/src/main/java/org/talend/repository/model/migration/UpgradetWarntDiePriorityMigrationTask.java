// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.model.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.conversions.UpdatePropertyComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * Use to rename tDB(Input|Output|SQLRow) into tMysql(Input|Output|Row). Related bug 540.
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class UpgradetWarntDiePriorityMigrationTask extends AbstractJobMigrationTask {

    @Override
    public List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.PROCESS);
        return toReturn;
    }

    public ExecutionResult executeOnProcess(ProcessItem item) {
        try {
            // 1. tWarn:
            IComponentFilter filter1 = new NameComponentFilter("tWarn"); //$NON-NLS-1$
            IComponentConversion setPriorityProperty = new UpdatePropertyComponentConversion("PRIORITY", "4"); //$NON-NLS-1$
            ModifyComponentsAction.searchAndModify(item, filter1, Arrays.<IComponentConversion> asList(setPriorityProperty));

            // 1. tDie:
            IComponentFilter filter2 = new NameComponentFilter("tDie"); //$NON-NLS-1$
            setPriorityProperty = new UpdatePropertyComponentConversion("PRIORITY", "5"); //$NON-NLS-1$
            ModifyComponentsAction.searchAndModify(item, filter2, Arrays.<IComponentConversion> asList(setPriorityProperty));

            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }
}
