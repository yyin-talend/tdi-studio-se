// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.editor;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.SelectEditPartTracker;

/**
 * DOC talend class global comment. Detailled comment
 */
public class XmlSelectEditPartTracker extends SelectEditPartTracker {

    public XmlSelectEditPartTracker(EditPart owner) {
        super(owner);
    }

    @Override
    protected void performConditionalSelection() {
        super.performConditionalSelection();
    }

    protected boolean handleButtonUp(int button) {
        if (isInState(STATE_DRAG)) {
            performSelection();
            if (hasSelectionOccurred())
                performDirectEdit();
            if (button == 1 && getSourceEditPart().getSelected() != EditPart.SELECTED_NONE)
                getCurrentViewer().reveal(getSourceEditPart());
            setState(STATE_TERMINAL);
            return true;
        }
        return false;
    }

    protected void performDirectEdit() {
        DirectEditRequest req = new DirectEditRequest();
        req.setLocation(getCurrentInput().getMouseLocation());
        new XmlDelayedDirectEditHelper(getSourceEditPart().getViewer(), req, getSourceEditPart());
    }
}
