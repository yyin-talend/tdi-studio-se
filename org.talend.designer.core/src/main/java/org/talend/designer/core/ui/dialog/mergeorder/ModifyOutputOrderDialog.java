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
package org.talend.designer.core.ui.dialog.mergeorder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * yzhang class global comment. Detailled comment
 */
public class ModifyOutputOrderDialog extends MergeOrderDialog {

    private Node multipleOutputNode;

    List<Connection> notDataList;

    private boolean isIterate = false;

    /**
     * yzhang ModifyOutputOrderDialog constructor comment.
     * 
     * @param parentShell
     * @param mergeNode
     */
    public ModifyOutputOrderDialog(Shell parentShell, Node multipleOutputNode) {
        super(parentShell);
        this.multipleOutputNode = multipleOutputNode;
        List<Connection> fullList = (List<Connection>) multipleOutputNode.getOutgoingConnections();
        notDataList = new ArrayList<Connection>();
        this.connectionList = new ArrayList<Connection>();
        for (Connection connection : fullList) {
            if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                connectionList.add(connection);
            } else if (connection.getLineStyle() == EConnectionType.ITERATE) {
                // feature 4505
                isIterate = true;
                connectionList.add(connection);
            } else {
                notDataList.add(connection);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.dialog.mergeorder.MergeOrderDialog#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.ui.dialog.mergeorder.MergeOrderDialog#configureSizeAndTitle(org.eclipse.swt.widgets.
     * Shell)
     */
    @Override
    protected void configureSizeAndTitle(Shell shell) {
        // shell.setSize(new Point(300, 400));
        String midStr = "output"; //$NON-NLS-1$
        if (isIterate) {
            midStr = "iterate"; //$NON-NLS-1$
        }
        shell.setText(multipleOutputNode.getUniqueName() + " Modify " + midStr + " orders"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.dialog.mergeorder.MergeOrderDialog#getConnectionQty()
     */
    @Override
    protected int getConnectionQty() {
        int nb = 0;
        for (Connection connection : (List<Connection>) multipleOutputNode.getOutgoingConnections()) {
            if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                nb++;
            } else if (connection.getLineStyle().equals(EConnectionType.ITERATE)) {
                nb++;
            }
        }
        return nb;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.dialog.mergeorder.MergeOrderDialog#getConnectionList()
     */
    @Override
    public List<Connection> getConnectionList() {
        connectionList.addAll(notDataList);
        return connectionList;
    }
}
