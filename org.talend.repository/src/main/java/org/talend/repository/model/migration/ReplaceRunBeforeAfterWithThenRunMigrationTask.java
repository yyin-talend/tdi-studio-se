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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.migration.AbstractMigrationTask;
import org.talend.core.model.migration.IProjectMigrationTask;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * Task replace run before and after with then run connection.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ChangeRunBeforeAfterToThenRunMigrationTask.java 下午04:41:56 2007-5-17 +0000 (2007-5-17) yzhang $
 * 
 */
public class ReplaceRunBeforeAfterWithThenRunMigrationTask extends AbstractMigrationTask implements
        IProjectMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#execute(org.talend.core.model.general.Project)
     */
    public ExecutionResult execute(Project project) {
        try {
            replaceConnections();
            return ExecutionResult.SUCCESS_WITH_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    /**
     * Replace run before and after connection with then run.
     * 
     * yzhang Comment method "replaceConnections".
     * 
     * @throws PersistenceException
     */
    public void replaceConnections() throws PersistenceException {
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        List<IRepositoryObject> list = factory.getAll(ERepositoryObjectType.PROCESS, true);

        boolean modified;
        for (IRepositoryObject mainObject : list) {

            List<IRepositoryObject> allVersion = factory.getAllVersion(mainObject.getId());

            for (IRepositoryObject object : allVersion) {

                ProcessItem item = (ProcessItem) object.getProperty().getItem();

                modified = false;

                Map<String, List> runAfterMap = new TreeMap<String, List>();

                if (isMultiJob(item.getProcess().getConnection())) {
                    // TODO: if it's a job with muliti sub jobs an error mark need to be added in repository.                    
                }

                for (Object o : item.getProcess().getConnection()) {

                    ConnectionType currentConnection = (ConnectionType) o;

                    if (currentConnection.getLabel().equals("RunAfter")) {

                        currentConnection.setLabel("ThenRun");
                        currentConnection.setLineStyle(EConnectionType.THEN_RUN.getId());

                        String sourceKey = currentConnection.getSource();
                        if (!runAfterMap.containsKey(sourceKey)) {
                            List<ConnectionType> connectionList = new ArrayList<ConnectionType>();
                            connectionList.add(currentConnection);
                            runAfterMap.put(sourceKey, connectionList);
                        } else {
                            runAfterMap.get(sourceKey).add(currentConnection);
                        }

                        modified = true;

                    } else if (currentConnection.getLabel().equals("RunBefore")) {

                        currentConnection.setLabel("ThenRun");
                        String target = currentConnection.getTarget();
                        currentConnection.setTarget(currentConnection.getSource());
                        currentConnection.setSource(target);

                        String sourceKey = currentConnection.getSource();
                        if (!runAfterMap.containsKey(sourceKey)) {
                            List<ConnectionType> connectionList = new ArrayList<ConnectionType>();
                            connectionList.add(currentConnection);
                            runAfterMap.put(sourceKey, connectionList);
                        } else {
                            runAfterMap.get(sourceKey).add(currentConnection);
                        }

                        modified = true;
                    }

                }
                if (modified) {
                    resetDirectionOfConnections(runAfterMap);
                    factory.save(item);
                }
            }
        }
    }

    /**
     * Reset driection of the connections.
     * 
     * yzhang Comment method "resetRunAfterConnections".
     * 
     * @param connectionMap
     */
    private void resetDirectionOfConnections(Map<String, List> connectionMap) {
        if (connectionMap.isEmpty()) {
            return;
        }
        for (Iterator iter = connectionMap.keySet().iterator(); iter.hasNext();) {
            String sourceName = (String) iter.next();
            List<ConnectionType> connectionList = connectionMap.get(sourceName);
            ConnectionType oldConnection = null;
            for (ConnectionType connection : connectionList) {
                if (oldConnection != null) {
                    oldConnection.setTarget(connection.getTarget());
                }
                connection.setSource(connection.getTarget());
                oldConnection = connection;
            }
            if (oldConnection != null) {
                oldConnection.setTarget(sourceName);
            }
        }
    }

    /**
     * To see whether the job with sub jobs.
     * 
     * yzhang Comment method "isMulitiJob".
     * 
     * @return
     */
    private boolean isMultiJob(List<ConnectionType> connections) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (ConnectionType conn : connections) {

            if (conn.getLabel().equals("RunAfter") || conn.getLabel().equals("RunBefore")) {
                if (map.containsKey(conn.getTarget())) {
                    int i = map.get(conn.getTarget());
                    map.put(conn.getTarget(), ++i);
                } else {
                    map.put(conn.getTarget(), 1);
                }
            }
        }

        for (Integer flag : map.values()) {
            if (flag > 1) {

                return true;
            }
        }
        return false;

    }
}
