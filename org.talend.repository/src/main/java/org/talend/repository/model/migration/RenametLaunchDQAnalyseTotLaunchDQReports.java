package org.talend.repository.model.migration;

import java.util.Date;
import java.util.GregorianCalendar;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.migration.IProjectMigrationTask.ExecutionResult;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

public class RenametLaunchDQAnalyseTotLaunchDQReports extends
		AbstractJobMigrationTask {

	@Override
	public ExecutionResult execute(Item item) {
    	ProcessType processType = getProcessType(item);
		if (processType == null) {
			return ExecutionResult.NOTHING_TO_DO;
		}	
        try {
            ModifyComponentsAction.searchAndRename(item,processType, "tLaunchDQAnalyse", "tLaunchDQReports"); //$NON-NLS-1$ //$NON-NLS-2$
            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2009, 5, 31, 16, 0, 0);
        return gc.getTime();
    }

}
