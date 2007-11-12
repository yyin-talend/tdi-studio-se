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

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * Task replace run before and after with then run connection.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ChangeRunBeforeAfterToThenRunMigrationTask.java 下午04:41:56 2007-5-17 +0000 (2007-5-17) yzhang $
 * 
 */
public class ChangeUniqRowLinksMigrationTask extends AbstractJobMigrationTask {

    private static final String STANDARD_CONNECTOR_NAME = "FLOW";

    private static final String NEW_CONNECTOR_NAME = "UNIQUE";

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#execute(org.talend.core.model.general.Project)
     */
    public ExecutionResult executeOnProcess(ProcessItem item) {
        try {
            boolean modified = replaceConnections(item);
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

    public boolean replaceConnections(ProcessItem item) throws PersistenceException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        boolean replaceDone = false;
        boolean modified = false;

        for (Object o : item.getProcess().getConnection()) {

            ConnectionType currentConnection = (ConnectionType) o;

            // if no connector name or if the connector is FLOW on the
            // output of the tUniqRow, set the connector name to UNIQUE.
            if ((currentConnection.getConnectorName() == null)
                    || STANDARD_CONNECTOR_NAME.equals(currentConnection.getConnectorName())
                    && sourceComeFromtUniqRow(item, currentConnection.getSource())) {
                currentConnection.setConnectorName(NEW_CONNECTOR_NAME);
                modified = true;
            }

        }
        if (modified) {
            factory.save(item);
            replaceDone = true;
        }
        return replaceDone;
    }

    private boolean sourceComeFromtUniqRow(ProcessItem item, String sourceName) {
        boolean tUniqRow = false;
        for (Object o : item.getProcess().getNode()) {
            NodeType currentNode = (NodeType) o;

            if (currentNode.getComponentName().equals("tUniqRow")) {
                if (sourceName.equals(ComponentUtilities.getNodeUniqueName(currentNode))) {
                    tUniqRow = true;
                }
            }
        }
        return tUniqRow;
    }
}
