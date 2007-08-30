// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.model.migration;

import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.AbstractMigrationTask;
import org.talend.core.model.migration.IProjectMigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * Task replace run before and after with then run connection.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ChangeRunBeforeAfterToThenRunMigrationTask.java 下午04:41:56 2007-5-17
 * +0000 (2007-5-17) yzhang $
 * 
 */
public class ChangeUniqRowLinksMigrationTask extends AbstractMigrationTask
		implements IProjectMigrationTask {

	private static final String STANDARD_CONNECTOR_NAME = "FLOW";

	private static final String NEW_CONNECTOR_NAME = "UNIQUE";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.talend.core.model.migration.IProjectMigrationTask#execute(org.talend.core.model.general.Project)
	 */
	public ExecutionResult execute(Project project) {
		try {
			boolean modified = replaceConnections();
			if (modified) {
				return ExecutionResult.SUCCESS_WITH_ALERT;
			} else {
				return ExecutionResult.SUCCESS_NO_ALERT;
			}
		} catch (Exception e) {
			ExceptionHandler.process(e);
			return ExecutionResult.FAILURE;
		}
	}

	public boolean replaceConnections() throws PersistenceException {
		ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
		List<IRepositoryObject> list = factory.getAll(
				ERepositoryObjectType.PROCESS, true);

		boolean replaceDone = false;
		for (IRepositoryObject mainObject : list) {

			List<IRepositoryObject> allVersion = factory
					.getAllVersion(mainObject.getId());

			for (IRepositoryObject object : allVersion) {

				ProcessItem item = (ProcessItem) object.getProperty().getItem();

				boolean modified = false;

				for (Object o : item.getProcess().getConnection()) {

					ConnectionType currentConnection = (ConnectionType) o;

					// if no connector name or if the connector is FLOW on the
					// output of the tUniqRow, set the connector name to UNIQUE.
					if ((currentConnection.getConnectorName() == null)
							|| STANDARD_CONNECTOR_NAME.equals(currentConnection
									.getConnectorName())
							&& sourceComeFromtUniqRow(item, currentConnection
									.getSource())) {
						currentConnection.setConnectorName(NEW_CONNECTOR_NAME);
						modified = true;
					}

				}
				if (modified) {
					factory.save(item);
					replaceDone = true;
				}
			}
		}
		return replaceDone;
	}

	private boolean sourceComeFromtUniqRow(ProcessItem item, String sourceName) {
		boolean tUniqRow = false;
		for (Object o : item.getProcess().getNode()) {
			NodeType currentNode = (NodeType) o;

			if (currentNode.getComponentName().equals("tUniqRow")) {
				if (sourceName.equals(ComponentUtilities
						.getNodeUniqueName(currentNode))) {
					tUniqRow = true;
				}
			}
		}
		return tUniqRow;
	}
}
