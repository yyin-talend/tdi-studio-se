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
package org.talend.designer.codegen;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.IProxyRepositoryFactory;

/***/
public abstract class AbstractRoutineSynchronizer implements
		IRoutineSynchronizer {

	private static Map<String, Date> id2date = new HashMap<String, Date>();

	protected List<IRepositoryObject> getRoutines() throws SystemException {
		IProxyRepositoryFactory repositoryFactory = CodeGeneratorActivator
				.getDefault().getRepositoryService()
				.getProxyRepositoryFactory();

		List<IRepositoryObject> routines;
		try {
			routines = repositoryFactory.getAll(ERepositoryObjectType.ROUTINES);
		} catch (PersistenceException e) {
			throw new SystemException(e);
		}
		return routines;
	}

	public void syncRoutine(RoutineItem routineItem, boolean copyToTemp)
			throws SystemException {
        if (!isRoutineUptodate(routineItem) || !getRoutineFile(routineItem).exists()) {
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
			//FIXME mhelleboid for now, routines are deleted and recreated on project logon
			//change this code, if one day routines are updated
			return routineItem.getProperty().getCreationDate();
		} else {
			return routineItem.getProperty().getModificationDate();
		}

	}

}
