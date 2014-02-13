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
package org.talend.designer.core.ui.editor.outline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * This class gets all node to add them in the tree in the Outline It doesn't give any detail, it just adds the roots
 * objects. <br/>
 * 
 * $Id$
 * 
 */
public class ProcessTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener {

    private NodeTransferDragSourceListener nodeTransferDragSourceListener = NodeTransferDragSourceListener.getInstance();

    public ProcessTreeEditPart(Object model) {
        super(model);
    }

    @Override
    public void setSelected(int value) {
        nodeTransferDragSourceListener.setEditPart(this);
        super.setSelected(value);
    }

    // TransferDragSourceListener dragDropListener = new TransferDragSourceListener() {
    //
    // TextTransfer transfer;
    //
    // public Transfer getTransfer() {
    // transfer = TextTransfer.getInstance();
    // return transfer;
    // }
    //
    // public void dragFinished(final DragSourceEvent event) {
    // }
    //
    // public void dragSetData(final DragSourceEvent event) {
    // INode node = (INode) currentEditPart.getParent().getModel();
    // String value = ElementParameterParser.parse(node, ((INodeReturn) currentEditPart.getModel()).getVarName());
    // event.data = value;
    // }
    //
    // public void dragStart(final DragSourceEvent event) {
    // event.doit = true;
    // }
    //
    // };

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#activate()
     */
    @Override
    public void activate() {
        super.activate();
        ((Process) getModel()).addPropertyChangeListener(this);
        nodeTransferDragSourceListener.setEditPart(this);
        getViewer().addDragSourceListener(nodeTransferDragSourceListener.getNodeTransferDragSourceListener());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void refreshChildren() {
        Map modelToEditPart = new HashMap();
        List children = getChildren();
        int i;
        for (i = 0; i < children.size(); i++) {
            EditPart editPart = (EditPart) children.get(i);
            modelToEditPart.put(editPart.getModel(), editPart);
        }

        List modelObjects = getModelChildren();
        // sort model for Outline
        Collections.sort(modelObjects, new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                String s1 = o1.getUniqueName().toLowerCase();
                String s2 = o2.getUniqueName().toLowerCase();
                return s1.compareToIgnoreCase(s2);
                // if (s1.charAt(0) > s2.charAt(0)) {
                // return 1;
                // } else if (s1.charAt(0) < s2.charAt(0)) {
                // return -1;
                // } else { // The alphanumeric section
                // String alphabet1 = s1.replaceAll("\\d+$", "");
                // String alphabet2 = s2.replaceAll("\\d+$", "");
                // // If you don't want to case-sensitive, or
                // // compareTo
                // int cmpAlphabet = alphabet1.compareToIgnoreCase(alphabet2);
                // if (cmpAlphabet != 0) {
                // return cmpAlphabet;
                // }
                // // Digital section
                // String numeric1 = s1.replaceAll("^[a-zA-Z_]+", "");
                // String numeric2 = s2.replaceAll("^[a-zA-Z_]+", "");
                // if ("".equals(numeric1)) {
                // /*
                // * Even if the numeric2 is the empty string also does not matter, of course, if the comparison
                // * is not String ( or other immutable objects ) is another matter
                // */
                // return -1;
                // }
                // if ("".equals(numeric2)) {
                // return 1;
                // }
                // int result = Integer.parseInt(numeric1) - Integer.parseInt(numeric2);
                // return result;
                // }
            }

        });

        for (i = 0; i < modelObjects.size(); i++) {
            Object model = modelObjects.get(i);
            if (i >= children.size() || ((EditPart) children.get(i)).getModel() != model) {
                EditPart editPart = (EditPart) modelToEditPart.get(model);
                if (editPart != null) {
                    reorderChild(editPart, i);
                } else {
                    editPart = createChild(model);
                    addChild(editPart, i);
                }
            }
        }

        List trash = new ArrayList();
        for (; i < children.size(); i++)
            trash.add(children.get(i));

        for (i = 0; i < trash.size(); i++) {
            EditPart ep = (EditPart) trash.get(i);
            removeChild(ep);
        }
    }

    /*
     * ss (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#deactivate()
     */
    @Override
    public void deactivate() {
        ((Process) getModel()).removePropertyChangeListener(this);
        nodeTransferDragSourceListener.setEditPart(null);
        getViewer().removeDragSourceListener(nodeTransferDragSourceListener.getNodeTransferDragSourceListener());
        super.deactivate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
     */
    @Override
    protected List getModelChildren() {
        return ((Process) getModel()).getGraphicalNodes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent change) {
        refreshChildren();

    }
}
