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
package org.talend.designer.core.ui.editor.outline;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeReturn;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.nodes.NodeEditPolicy;

/**
 * This class uses the Node as model and will show a part of its atributes in the Outline tree. <br/>
 * 
 * $Id$
 * 
 */
public class NodeReturnsTreeEditPart extends AbstractTreeEditPart {

    @Override
    public Object getAdapter(Class key) {
        return null;
        /*
         * if (key == ITabbedPropertySheetPageContributor.class) { return null; } if (key == IResource.class) { return
         * null; } return super.getAdapter(key);
         */
    }

    static EditPart currentEditPart;

    @Override
    public void setSelected(int value) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        IEditorPart editorPart = page.getActiveEditor();
        if (editorPart instanceof MultiPageTalendEditor) {
            MultiPageTalendEditor multiPageTalendEditor = ((MultiPageTalendEditor) editorPart);
            EditPart editPart = multiPageTalendEditor.getOldSelection();
            if (editPart != null) {
                ISelection selection = multiPageTalendEditor.getTalendEditor().getViewer().getSelection();
                if (selection instanceof StructuredSelection) {
                    StructuredSelection structSel = (StructuredSelection) selection;
                    if (!structSel.getFirstElement().equals(editPart)) {
                        multiPageTalendEditor.getTalendEditor().getViewer().setSelection(selection);
                    }
                }
            }
        }
        currentEditPart = this;
        super.setSelected(0);
    }

    TransferDragSourceListener dragDropListener = new TransferDragSourceListener() {

        TextTransfer transfer;

        public Transfer getTransfer() {
            transfer = TextTransfer.getInstance();
            return transfer;
        }

        public void dragFinished(final DragSourceEvent event) {
        }

        public void dragSetData(final DragSourceEvent event) {
            INode node = (INode) currentEditPart.getParent().getModel();
            String value = ElementParameterParser.parse(node, ((INodeReturn) currentEditPart.getModel()).getVarName());
            event.data = value;
        }

        public void dragStart(final DragSourceEvent event) {
            event.doit = true;
        }

    };

    @Override
    public void activate() {
        super.activate();
        getViewer().addDragSourceListener(dragDropListener);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        getViewer().removeDragSourceListener(dragDropListener);
    }

    public NodeReturnsTreeEditPart(Object model) {
        super(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
     */
    protected List getModelChildren() {
        return Collections.EMPTY_LIST;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     *//*
         * public void propertyChange(PropertyChangeEvent change) { refreshVisuals(); }
         */

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractTreeEditPart#createEditPolicies()
     */
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeEditPolicy());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractTreeEditPart#refreshVisuals()
     */
    protected void refreshVisuals() {
        INodeReturn nr = ((INodeReturn) getModel());
        setWidgetText(nr.getDisplayName() + " - " + nr.getName() + " (" + nr.getAvailability() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
