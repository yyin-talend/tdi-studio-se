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
package org.talend.repository.model.migration;

import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class ChangeRunIfOkConnectionToOnSubJobOkTask extends AbstractJobMigrationTask {

    private static final String TARGET_CONNECTION_NAME = "RUN_OK";

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.ProcessItem)
     */
    @Override
    public ExecutionResult executeOnProcess(ProcessItem item) {
        try {
            renameConnections(item);
            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    /**
     * yzhang Comment method "renameConnections".
     * 
     * @param item
     */
    private void renameConnections(ProcessItem item) throws PersistenceException {

        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        boolean modified = false;

        for (Object o : item.getProcess().getConnection()) {

            ConnectionType currentConnection = (ConnectionType) o;

            if (currentConnection.getConnectorName().equals(TARGET_CONNECTION_NAME)) { //$NON-NLS-1$

                setBeginningOfSubjob(currentConnection.getSource(), item);

                currentConnection.setConnectorName(EConnectionType.ON_SUBJOB_OK.getName());
                currentConnection.setLabel(EConnectionType.ON_SUBJOB_OK.getDefaultLinkName());
                currentConnection.setLineStyle(EConnectionType.ON_SUBJOB_OK.getDefaultLineStyle());
                currentConnection.setSource(beginningOfSubJob);

                modified = true;
            }

        }
        if (modified) {
            factory.save(item);
        }

    }

    private String beginningOfSubJob;

    private void setBeginningOfSubjob(String source, ProcessItem item) {

        List<ConnectionType> connections = item.getProcess().getConnection();
        for (ConnectionType connection : connections) {
            if (source.equals(connection.getTarget())
                    && (isMainTypeConnection(connection.getConnectorName()) || connection.getConnectorName().equals(
                            TARGET_CONNECTION_NAME))) {
                setBeginningOfSubjob(connection.getSource(), item);
                return;
            }
        }
        beginningOfSubJob = source;

    }

    private boolean isMainTypeConnection(String connectionName) {
        EConnectionType connectionType = EConnectionType.getTypeFromName(connectionName);
        return connectionType.hasConnectionCategory(IConnectionCategory.MAIN);
    }
}
