// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.routines.RoutinesUtil;
import org.talend.designer.core.model.utils.emf.talendfile.ItemInforType;

/**
 * ggu class global comment. Detailled comment
 */
public class AddRoutineDependenciesMigrationTask extends AbstractJobMigrationTask {

    public List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.PROCESS);
        return toReturn;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ExecutionResult execute(Item item) {
        if (!(item instanceof ProcessItem)) {
            return ExecutionResult.NOTHING_TO_DO;
        }
        try {
            ProcessItem item2 = (ProcessItem) item;
            List<ItemInforType> routinesDependencies = (List<ItemInforType>) item2.getProcess().getRoutinesDependencies();

            List<ItemInforType> neededRoutineDependencies = new ArrayList<ItemInforType>();
            neededRoutineDependencies.addAll(RoutinesUtil.createJobRoutineDependencies(true));
            neededRoutineDependencies.addAll(RoutinesUtil.createJobRoutineDependencies(false));
            boolean modified = false;

            for (ItemInforType added : neededRoutineDependencies) {
                boolean found = false;
                for (ItemInforType type : routinesDependencies) {
                    if (added.isSystem() == type.isSystem() && added.getIdOrName().equals(type.getIdOrName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    routinesDependencies.add(added);
                    modified = true;
                }
            }
            if (modified) {
                CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().save(item, true);

                return ExecutionResult.SUCCESS_WITH_ALERT;
            }

        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
        return ExecutionResult.NOTHING_TO_DO;
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2010, 7, 22, 12, 0, 0);
        return gc.getTime();
    }

}
