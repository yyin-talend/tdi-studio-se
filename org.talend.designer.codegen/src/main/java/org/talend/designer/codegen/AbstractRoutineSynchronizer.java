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
package org.talend.designer.codegen;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/***/
public abstract class AbstractRoutineSynchronizer implements ITalendSynchronizer {

    private static Map<String, Date> id2date = new HashMap<String, Date>();

    protected List<IRepositoryObject> getRoutines() throws SystemException {
        List<IRepositoryObject> routineList = getMainProjectRoutine();
        // list.addAll(getReferencedProjectRoutine());

        // remove routine with same name in reference project
        Set<String> routineNames = new HashSet<String>();
        for (IRepositoryObject obj : routineList) {
            routineNames.add(obj.getProperty().getLabel());
        }

        List<IRepositoryObject> refRoutines = new ArrayList<IRepositoryObject>();
        getReferencedProjectRoutine(refRoutines, ProjectManager.getInstance().getReferencedProjects());
        for (IRepositoryObject obj : refRoutines) {
            String name = obj.getProperty().getLabel();
            // it does not have a routine with same name
            if (!routineNames.contains(name)) {
                routineNames.add(name);
                routineList.add(obj);
            }
        }
        return routineList;
    }

    private List<IRepositoryObject> getMainProjectRoutine() throws SystemException {
        IProxyRepositoryFactory repositoryFactory = CodeGeneratorActivator.getDefault().getRepositoryService()
                .getProxyRepositoryFactory();

        List<IRepositoryObject> routines;
        try {
            routines = repositoryFactory.getAll(ERepositoryObjectType.ROUTINES);
        } catch (PersistenceException e) {
            throw new SystemException(e);
        }
        return routines;
    }

    private void getReferencedProjectRoutine(List<IRepositoryObject> routines, List projects) throws SystemException {
        if (projects == null || projects.isEmpty()) {
            return;
        }
        IProxyRepositoryFactory repositoryFactory = CodeGeneratorActivator.getDefault().getRepositoryService()
                .getProxyRepositoryFactory();
        for (Object obj : projects) {
            Project project = null;
            if (obj instanceof Project) {
                project = (Project) obj;
            } else if (obj instanceof ProjectReference) {
                project = new Project(((ProjectReference) obj).getReferencedProject());
            }
            if (project != null) {
                try {
                    routines.addAll(repositoryFactory.getAll(project, ERepositoryObjectType.ROUTINES));
                } catch (PersistenceException e) {
                    throw new SystemException(e);
                }
                getReferencedProjectRoutine(routines, project.getEmfProject().getReferencedProjects());
            }
        }

    }

    public void syncRoutine(RoutineItem routineItem, boolean copyToTemp) throws SystemException {
        if (!isRoutineUptodate(routineItem) || !getFile(routineItem).exists()) {
            doSyncRoutine(routineItem, copyToTemp);
            setRoutineAsUptodate(routineItem);
        }
    }

    protected abstract void doSyncRoutine(RoutineItem routineItem, boolean copyToTemp) throws SystemException;

    protected boolean isRoutineUptodate(RoutineItem routineItem) {
        Date refDate = getRefDate(routineItem);
        if (refDate == null) {
            return false;
        }
        Date date = id2date.get(routineItem.getProperty().getId());
        return refDate.equals(date);
    }

    protected void setRoutineAsUptodate(RoutineItem routineItem) {
        Date refDate = getRefDate(routineItem);
        if (refDate == null) {
            return;
        }
        id2date.put(routineItem.getProperty().getId(), refDate);
    }

    private Date getRefDate(RoutineItem routineItem) {
        if (routineItem.isBuiltIn()) {
            // FIXME mhelleboid for now, routines are deleted and recreated on
            // project logon
            // change this code, if one day routines are updated
            return routineItem.getProperty().getCreationDate();
        } else {
            return routineItem.getProperty().getModificationDate();
        }

    }

    public void forceSyncRoutine(RoutineItem routineItem) {
        id2date.remove(routineItem.getProperty().getId());
        try {
            getFile(routineItem).delete(true, new NullProgressMonitor());
        } catch (Exception e) {
            // ignore me
        }
    }

    // qli modified to fix the bug 5400 and 6185.
    public abstract void renameRoutineClass(RoutineItem routineItem);

}
