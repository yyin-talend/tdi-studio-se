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
package org.talend.designer.core.ui.action;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreationFactory;
import org.talend.designer.core.ui.editor.nodes.NodePart;

/**
 * DOC Talend class global comment. Detailled comment
 */
public class TalendCreateConnectionTool extends TalendConnectionCreationTool implements DragTracker {

    private NodePart nodePart;

    public TalendCreateConnectionTool(CreationFactory factory, NodePart nodePart) {
        super(factory, false);
        setUnloadWhenFinished(true);
        this.nodePart = nodePart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#handleButtonUp(int)
     */
    @Override
    protected boolean handleButtonUp(int button) {
        if (button == 1 && stateTransition(STATE_CONNECTION_STARTED, STATE_TERMINAL)) {
            return handleCreateConnection();
        }

        super.handleButtonDown(button);
        if (isInState(STATE_CONNECTION_STARTED)) {
            // Fake a drag to cause feedback to be displayed immediately on
            // mouse down.
            handleDrag();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.action.TalendConnectionCreationTool#handleCreateConnection()
     */
    @Override
    protected boolean handleCreateConnection() {
        Command endCommand = getCommand();
        if (endCommand != null) {
            nodePart.getViewer().getSelectionManager().deselect(nodePart);
        }
        return super.handleCreateConnection();
    }
}
