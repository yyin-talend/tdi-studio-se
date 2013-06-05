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

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.designer.core.ui.editor.connections.ConnLabelEditPart;

/**
 * DOC Talend class global comment. Detailled comment
 */
public class GEFUndoAction extends SelectionAction {

    /**
     * DOC Talend GEFUndoAction constructor comment.
     * 
     * @param abstractTalendEditor
     */
    public GEFUndoAction(IWorkbenchPart part) {
        super(part);
        setId(ActionFactory.UNDO.getId());
        setText("Undo"); //$NON-NLS-1$
        ISharedImages sharedImages = part.getSite().getWorkbenchWindow().getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO_DISABLED));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
     */
    @Override
    protected boolean calculateEnabled() {
        List objects = getSelectedObjects();
        if (!objects.isEmpty()) {
            for (Object o : objects) {
                if (o instanceof ConnLabelEditPart) {
                    return true;
                }
            }
        }
        return getCommandStack().canUndo();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.actions.UndoAction#run()
     */
    @Override
    public void run() {
        List objects = getSelectedObjects();
        boolean connectionTextActived = false;
        if (!objects.isEmpty()) {
            ConnLabelEditPart connLabelEditPart = null;
            Text text = null;
            if (objects.size() == 1) {
                for (Object o : objects) {
                    if (o instanceof ConnLabelEditPart) {
                        connLabelEditPart = (ConnLabelEditPart) objects.get(0);
                        if (connLabelEditPart.getDirectEditManager() != null
                                && connLabelEditPart.getDirectEditManager().getTextControl() != null) {
                            connectionTextActived = true;
                        }
                    }
                }
            }
            if (connectionTextActived && connLabelEditPart != null) {
                text = connLabelEditPart.getDirectEditManager().getTextControl();
                text.setText(connLabelEditPart.getDirectEditManager().getOldName());
                text.selectAll();
                return;
            }
        }
        getCommandStack().undo();
    }
}
