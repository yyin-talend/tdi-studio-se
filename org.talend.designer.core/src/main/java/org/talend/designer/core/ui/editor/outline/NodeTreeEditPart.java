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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeEditPolicy;

/**
 * This class uses the Node as model and will show a part of its atributes in the Outline tree. <br/>
 * 
 * $Id$
 * 
 */
public class NodeTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener {

    public NodeTreeEditPart(Object model) {
        super(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#activate()
     */
    public void activate() {
        super.activate();
        ((Node) getModel()).addPropertyChangeListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#deactivate()
     */
    public void deactivate() {
        ((Node) getModel()).removePropertyChangeListener(this);
        super.deactivate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
     */
    protected List getModelChildren() {
        return ((Node) getModel()).getReturns(); // Collections.EMPTY_LIST;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent change) {
        refreshVisuals();
    }

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
        Node node = (Node) getModel();
        if (node.getLabel().equals(node.getUniqueName())) {
            setWidgetText(node.getUniqueName());
        } else {
            setWidgetText(node.getUniqueName() + " (" + node.getLabel() + ")");
        }
    }
}
