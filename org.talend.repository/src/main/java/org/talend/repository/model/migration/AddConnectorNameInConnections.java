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

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class AddConnectorNameInConnections extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractJobMigrationTask#executeOnProcess(org.talend.core.model.properties.ProcessItem)
     */
    @Override
    public ExecutionResult executeOnProcess(ProcessItem item) {
        try {
            boolean modified = addConnectorName(item);
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

    /**
     * DOC nrousseau Comment method "addConnectorName".
     * 
     * @param item
     * @return
     */
    private boolean addConnectorName(ProcessItem item) throws PersistenceException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

        boolean modified = false;

        for (Object o : item.getProcess().getConnection()) {

            ConnectionType currentConnection = (ConnectionType) o;

            if (currentConnection.getConnectorName() == null) {
                currentConnection.setConnectorName(EConnectionType.getTypeFromId(currentConnection.getLineStyle()).getName());
                modified = true;
            }

        }
        if (modified) {
            factory.save(item,true);
        }
        return modified;
    }
}
