// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ItemSaveTriggerProjectMigrationTask extends AbstractJobMigrationTask {

    public List<ERepositoryObjectType> getTypes() {
        // all items
        return Arrays.asList((ERepositoryObjectType[]) ERepositoryObjectType.values());
    }

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2017, 11, 2, 12, 0, 0);
        return gc.getTime();
    }

    public ExecutionResult execute(Project project) {
        if (PluginChecker.isTIS()) {
            return super.execute(project);
        }
        return ExecutionResult.NOTHING_TO_DO;
    }

    @Override
    public ExecutionResult execute(Item item) {
        final ProcessType processType = getProcessType(item);
        try {
            if (processType != null) { // for all jobs
                IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                        IRepositoryService.class);
                IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();

                factory.save(item, true); // only need re-save the item to trigger action
                return ExecutionResult.SUCCESS_NO_ALERT;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
        return ExecutionResult.NOTHING_TO_DO;
    }

}
