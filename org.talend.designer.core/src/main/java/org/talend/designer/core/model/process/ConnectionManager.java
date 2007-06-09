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
package org.talend.designer.core.model.process;

import java.util.Iterator;
import java.util.List;

import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.INode;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class ConnectionManager {

    private static EConnectionType newlineStyle;

    /**
     * 
     * Will return true if the connection can connect or not between source & target.
     * 
     * @param source
     * @param target
     * @param connType
     * @param connectionName
     * @return
     */
    private static boolean canConnect(Node source, Node target, EConnectionType connType, String connectionName) {
        if (source.equals(target)) {
            return false;
        }

        if (!target.isActivate() || !source.isActivate()) {
            return false;
        }

        if (source.sameProcessAs(target, false)) {
            return false;
        }
        int nbMerge = countNbMerge(source, target);
        if (nbMerge > 1) {
            return false;
        }

        // Check existing connections to avoid to have more than one link
        // no matter the type of the connection and the direction
        List connections = source.getOutgoingConnections();
        for (int i = 0; i < connections.size(); i++) {
            if (((Connection) connections.get(i)).getTarget().equals(target)) {
                return false;
            }
        }
        connections = source.getIncomingConnections();
        for (int i = 0; i < connections.size(); i++) {
            if (((Connection) connections.get(i)).getSource().equals(target)) {
                return false;
            }
        }

        if (connType.hasConnectionCategory(IConnectionCategory.DEPENDENCY)) {
            if (!(Boolean) target.getPropertyValue(EParameterName.STARTABLE.getName())) {
                return false;
            }
            if (!target.isSubProcessStart()) {
                return false;
            }
        }

        connections = target.getIncomingConnections();
        for (int i = 0; i < connections.size(); i++) {
            if (connType.equals(EConnectionType.TABLE)) {
                if (((Connection) connections.get(i)).isActivate()) {
                    if (((Connection) connections.get(i)).getName().equals(connectionName)) {
                        return false;
                    }
                }
            }
        }
        boolean targetHasHashLinks = ((Process) target.getProcess()).isThereLinkWithHash(target)
                | newlineStyle.hasConnectionCategory(IConnectionCategory.USE_HASH);
        if (connType.hasConnectionCategory(IConnectionCategory.CONDITION)) {
            if (targetHasHashLinks) {
                return false;
            }
        }
        if (targetHasHashLinks && source.hasRunIfLink()) {
            return false;
        }
        return true;
    }

    private static int countNbMergeOutgoing(INode source, int nb) {
        int curNb = nb;

        for (IConnection curConnec : source.getOutgoingConnections()) {
            if (curConnec.getLineStyle().equals(EConnectionType.FLOW_MERGE)) {
                curNb++;
            } else if (curConnec.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                // if main, then test the next component to check if there is a merge
                curNb += countNbMergeOutgoing(curConnec.getTarget(), curNb);
            }
        }
        return curNb;
    }

    private static int countNbMergeIncoming(INode source, int nb) {
        int curNb = nb;
        if (source.getComponent().useMerge()) {
            // if the component use merge even if there is no connection, then add one merge.
            curNb++;
        }
        for (IConnection curConnec : source.getIncomingConnections()) {
            if (curConnec.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                // if main, then test the next component to check if there is a merge
                curNb += countNbMergeIncoming(curConnec.getSource(), curNb);
            }
        }
        return curNb;
    }

    private static int countNbMerge(Node source, Node target) {
        return countNbMergeOutgoing(source, 0) + countNbMergeIncoming(source, 0) + countNbMergeOutgoing(target, 0)
                + countNbMergeIncoming(target, 0);
    }

    /**
     * Will return true if the connection can connect or not between source & target.
     * 
     * @param oldSource
     * @param newSource
     * @param target
     * @param connType
     * @param connectionName
     * @return
     */
    public static boolean canConnectToSource(Node oldSource, Node newSource, Node target, EConnectionType lineStyle,
            String connectorName, String connectionName) {
        if (newSource.getConnectorFromName(connectorName) == null) {
            // if the new source don't contain the kind of link, then we can't connect the link.
            return false;
        }
        int maxOutput = newSource.getConnectorFromName(connectorName).getMaxLinkOutput();
        if (maxOutput != -1 && (newSource.getConnectorFromName(connectorName).getCurLinkNbOutput() >= maxOutput)) {
            return false;
        }

        newlineStyle = lineStyle;
        if (!canConnect(newSource, target, lineStyle, connectionName)) {
            return false;
        }

        // if (!newConnectionType.equals(EConnectionType.LOOKUP)) {
        // INodeConnector nodeConnectorSource;
        // nodeConnectorSource = newSource.getConnectorFromType(newConnectionType);
        // if (nodeConnectorSource.getMaxLinkOutput() != -1) {
        // if (nodeConnectorSource.getCurLinkNbOutput() >= nodeConnectorSource.getMaxLinkOutput()) {
        // return false;
        // }
        // }
        // }
        return true;
    }

    /**
     * Will return true if the connection can connect or not between source & target.
     * 
     * @param source
     * @param oldTarget
     * @param newTarget
     * @param connType
     * @param connectionName
     * @return
     */
    public static boolean canConnectToTarget(Node source, Node oldTarget, Node newTarget, EConnectionType lineStyle,
            String connectorName, String connectionName) {
        newlineStyle = lineStyle;

        INode processStartNode = source.getProcessStartNode(true);
        // if the target is the start of the (source) process, then can't connect.
        if (processStartNode.equals(newTarget)) {
            return false;
        }

        // Modify Connection Type depending old and new target.
        if (newlineStyle.hasConnectionCategory(IConnectionCategory.FLOW)) {
            // if the connection type is not the default one, then we don't change automatically.
            // && newlineStyle.getName().equals(newConnectionType)) {
            EConnectionType oldConnectionType = newlineStyle;
            if (oldTarget != null) {

                if (oldTarget.getComponent().useMerge() && oldConnectionType.equals(EConnectionType.FLOW_MERGE)) {
                    // if the previous connection was a merge, then we just change it to a main, then the rules below
                    // will apply the same.
                    oldConnectionType = EConnectionType.FLOW_MAIN;
                }
            }
            if (!newTarget.getComponent().useMerge()) {
                int nbMain = 0;
                for (IConnection connec : newTarget.getIncomingConnections()) {
                    if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                        nbMain++;
                    }
                }
                if (nbMain > 0) {
                    newlineStyle = EConnectionType.FLOW_REF;
                } else {
                    newlineStyle = EConnectionType.FLOW_MAIN;
                }
            } else {
                newlineStyle = EConnectionType.FLOW_MERGE;
            }
        }

        int maxInput = newTarget.getConnectorFromType(newlineStyle).getMaxLinkInput();
        if (maxInput != -1 && (newTarget.getConnectorFromType(newlineStyle).getCurLinkNbInput() >= maxInput)) {
            return false;
        }

        if (!canConnect(source, newTarget, lineStyle, connectionName)) {
            return false;
        }

        // if (!newConnectionType.equals(EConnectionType.LOOKUP)) {
        // INodeConnector nodeConnectorTarget;
        // nodeConnectorTarget = newTarget.getConnectorFromType(newConnectionType);
        // if (nodeConnectorTarget.getMaxLinkInput() != -1) {
        // if (nodeConnectorTarget.getCurLinkNbInput() >= nodeConnectorTarget.getMaxLinkInput()) {
        // return false;
        // }
        // }
        // }

        return true;
    }

    /**
     * To call after the canConnect only, this will give the new connection type after connect.
     * 
     * @return the newConnectionType
     */
    public static EConnectionType getNewConnectionType() {
        return newlineStyle;
    }

    /**
     * 
     * Not used yet.
     * 
     * @param source
     * @param target
     * @param connType
     * @param connectionName
     * @return
     */
    public static boolean canRename(Node source, Node target, EConnectionType connType, String connectionName) {
        boolean canRename = true;
        if (connType.hasConnectionCategory(IConnectionCategory.UNIQUE_NAME)) {
            if (!(source.getProcess().checkValidConnectionName(connectionName))) {
                canRename = false;
            }
        } else if (connType.equals(EConnectionType.TABLE)) {
            if (connectionName.equals("")) { //$NON-NLS-1$
                canRename = false;
            } else {
                List<? extends IConnection> cons = target.getIncomingConnections();
                for (Iterator iter = cons.iterator(); iter.hasNext();) {
                    Connection conn = (Connection) iter.next();
                    if (conn.getName().equals(connectionName)) {
                        canRename = false;
                        break;
                    }
                }
            }
        }
        return canRename;
    }
}
