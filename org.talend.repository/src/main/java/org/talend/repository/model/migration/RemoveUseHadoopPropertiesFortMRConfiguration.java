// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
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

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * cli class global comment. Detailled comment
 */
public class RemoveUseHadoopPropertiesFortMRConfiguration extends
		AbstractJobMigrationTask {

	@Override
	public ExecutionResult execute(Item item) {
		ProcessType processType = getProcessType(item);
		if (processType != null) {
			boolean modified = false;
			final ParametersType parameters = processType.getParameters();
			if (parameters != null) {
				EList elementParameters = parameters.getElementParameter();
				for (int i = 0; i < elementParameters.size(); i++) {
					ElementParameterType param = (ElementParameterType) elementParameters
							.get(i);
					if ("USE_HADOOP_PROPERTIES".equals(param.getName()) && "true".equalsIgnoreCase(param.getValue())) { //$NON-NLS-1$ //$NON-NLS-2$

						for (int j = 0; i < elementParameters.size(); i++) {
							ElementParameterType hadoopProps = (ElementParameterType) elementParameters
									.get(j);
							if ("HADOOP_ADVANCED_PROPERTIES"
									.equalsIgnoreCase(hadoopProps.getName())) {
								hadoopProps.getElementValue().clear();
								modified = true;
							}
						}
						elementParameters.remove(param);
						break;
					}
				}
			}
			if (modified) {
				try {
					ProxyRepositoryFactory factory = ProxyRepositoryFactory
							.getInstance();
					factory.save(item, true);
				} catch (PersistenceException e) {
					ExceptionHandler.process(e);
					return ExecutionResult.FAILURE;
				}
				return ExecutionResult.SUCCESS_NO_ALERT;
			}
		}
		return ExecutionResult.NOTHING_TO_DO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.talend.core.model.migration.IProjectMigrationTask#getOrder()
	 */
	public Date getOrder() {
		GregorianCalendar gc = new GregorianCalendar(2013, 10, 19, 12, 0, 0);
		return gc.getTime();
	}

}
