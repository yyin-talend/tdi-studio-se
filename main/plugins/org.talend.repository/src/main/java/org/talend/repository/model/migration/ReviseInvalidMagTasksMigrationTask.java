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

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.AbstractProjectMigrationTask;
import org.talend.core.model.properties.MigrationTask;
import org.talend.core.model.utils.MigrationUtil;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.migration.IProjectMigrationTask;
import org.talend.migrationtool.model.GetTasksHelper;

/**
 * 
 * created by ycbai on 2016年7月22日 Detailled comment
 *
 */
public class ReviseInvalidMagTasksMigrationTask extends AbstractProjectMigrationTask {

    @Override
    public ExecutionResult execute(Project project, boolean doSave) {
        try {
            boolean modified = updateMigrationTask(project, doSave);
            if (modified) {
                return ExecutionResult.SUCCESS_NO_ALERT;
            } else {
                return ExecutionResult.NOTHING_TO_DO;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }

    }

    @Override
    public ExecutionResult execute(Project project) {
        return execute(project, true);
    }

    private boolean updateMigrationTask(Project project, boolean doSave) throws PersistenceException, IllegalAccessException,
            InvocationTargetException {
        boolean modified = false;
        org.talend.core.model.properties.Project emfProject = project.getEmfProject();
        EList<MigrationTask> projectMigrationTasks = emfProject.getMigrationTask();
        for (MigrationTask projectMigrationTask : projectMigrationTasks) {
            IProjectMigrationTask productMigrationTask = GetTasksHelper.getInstance()
                    .getProjectTask(projectMigrationTask.getId());
            if (productMigrationTask != null) {
                MigrationTask convertedProductMigrationTask = MigrationUtil.convertMigrationTask(productMigrationTask);
                if (!isSameSettingsOfTasks(convertedProductMigrationTask, projectMigrationTask)) {
                    BeanUtils.copyProperties(projectMigrationTask, convertedProductMigrationTask);
                    modified = true;
                }
            }
        }
        if (doSave) {
            ProxyRepositoryFactory.getInstance().saveProject(project);
        }
        return modified;
    }

    private boolean isSameSettingsOfTasks(MigrationTask m1, MigrationTask m2) {
        if (m1 == null || m2 == null) {
            return false;
        }
        String version1 = m1.getVersion();
        String version2 = m2.getVersion();
        String breaks1 = m1.getBreaks();
        String breaks2 = m2.getBreaks();
        if (version1 == null || version2 == null || breaks1 == null || breaks2 == null) {
            return false;
        }
        return version1.equals(version2) && breaks1.equals(breaks2);
    }

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2016, 7, 22, 12, 0, 0);
        return gc.getTime();
    }

}
