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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.process.ProcessPart;

/**
 * DOC Talend class global comment. Detailled comment
 */
public class TalendCreateConnectionTool extends TalendConnectionCreationTool implements DragTracker {

    private NodePart nodePart;

    private boolean avoidDeactivation = false;

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
            if (getTargetEditPart() == null) {
                EditPart processPart = getProcessPart(this.nodePart);
                if (processPart != null) {
                    this.setTargetEditPart(getProcessPart(this.nodePart));
                    this.getTargetRequest().setType(RequestConstants.REQ_CONNECTION_END);
                }
            }
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

    private EditPart getProcessPart(EditPart editPart) {
        EditPart parent = editPart.getParent();
        if (parent == null) {
            return null;
        }
        if (parent instanceof ProcessPart) {
            return parent;
        }
        return getProcessPart(parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.action.TalendConnectionCreationTool#handleCreateConnection()
     */
    @Override
    protected boolean handleCreateConnection() {
        setAvoidDeactivation(true);

        EditPartViewer viewer = getCurrentViewer();
        Command endCommand = getCommand();
        if (endCommand == null) {
            if (isInState(STATE_TERMINAL)) {
                // Fake a drag to cause feedback to be displayed immediately on mouse down.
                setState(STATE_CONNECTION_STARTED);
                handleDrag();
            }
            return true;
        }
        nodePart.getViewer().getSelectionManager().deselect(nodePart);

        setCurrentCommand(endCommand);

        executeCurrentCommand();

        selectAddedObject(viewer, getReturnValues(endCommand));

        setAvoidDeactivation(false);
        eraseSourceFeedback();
        deactivate();

        return true;
    }

    public Collection getReturnValues(Command c) {
        if (c instanceof CompoundCommand) {
            CompoundCommand cc = (CompoundCommand) c;
            List l = new ArrayList(cc.size());
            for (Iterator i = cc.getCommands().iterator(); i.hasNext();) {
                l.addAll(getReturnValues((Command) i.next()));
            }
            return l;

        }
        return Collections.EMPTY_LIST;
    }

    protected void selectAddedObject(EditPartViewer viewer, Collection objects) {
        final List editparts = new ArrayList();
        final EditPart[] primaryEP = new EditPart[1];
        for (Iterator i = objects.iterator(); i.hasNext();) {
            Object object = i.next();
            if (object instanceof IAdaptable) {
                Object editPart = viewer.getEditPartRegistry().get(((IAdaptable) object).getAdapter(View.class));

                if (editPart instanceof GraphicalEditPart) {
                    editparts.add(editPart);
                }
            }
        }

        if (!editparts.isEmpty()) {
            viewer.setSelection(new StructuredSelection(editparts));

            // automatically put the first shape into edit-mode
            Display.getCurrent().asyncExec(new Runnable() {

                @Override
                public void run() {
                    if (primaryEP[0] == null) {
                        primaryEP[0] = (EditPart) editparts.get(0);
                    }
                    //
                    // add active test since test scripts are failing on this
                    // basically, the editpart has been deleted when this
                    // code is being executed. (see RATLC00527114)
                    if (primaryEP[0].isActive()) {
                        primaryEP[0].performRequest(new Request("direct edit"));
                    }
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.TargetingTool#getCommand()
     */
    @Override
    protected Command getCommand() {
        if (getTargetEditPart() == null) {
            return null;
        }
        return getTargetEditPart().getCommand(getTargetRequest());
    }

    protected void setAvoidDeactivation(boolean avoidDeactivation) {
        this.avoidDeactivation = avoidDeactivation;
    }

    protected boolean avoidDeactivation() {
        return avoidDeactivation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#deactivate()
     */
    @Override
    public void deactivate() {
        if (!avoidDeactivation()) {
            super.deactivate();
            setTargetRequest(null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.AbstractConnectionCreationTool#eraseSourceFeedback()
     */
    @Override
    protected void eraseSourceFeedback() {
        if (!avoidDeactivation()) {
            super.eraseSourceFeedback();
        }
    }

}
