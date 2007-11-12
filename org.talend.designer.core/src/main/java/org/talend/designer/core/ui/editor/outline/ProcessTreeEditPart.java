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

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * This class gets all node to add them in the tree in the Outline It doesn't give any detail, it just adds the roots
 * objects. <br/>
 * 
 * $Id$
 * 
 */
public class ProcessTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener {

    public ProcessTreeEditPart(Object model) {
        super(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#activate()
     */
    public void activate() {
        super.activate();
        ((Process) getModel()).addPropertyChangeListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#deactivate()
     */
    public void deactivate() {
        ((Process) getModel()).removePropertyChangeListener(this);
        super.deactivate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
     */
    protected List getModelChildren() {
        return ((Process) getModel()).getGraphicalNodes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent change) {
        refreshChildren();
    }
}
