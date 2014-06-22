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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.swt.SWT;
import org.talend.designer.xmlmap.parts.AbstractInOutTreeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

/**
 * DOC talend class global comment. Detailled comment
 */
public class XmlSelectEditPartTracker extends SelectEditPartTracker {

    static EditPart lastSelectedShiftPart = null;

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

    @Override
    protected void performSelection() {
        if (hasSelectionOccurred())
            return;
        setFlag(FLAG_SELECTION_PERFORMED, true);
        EditPartViewer viewer = getCurrentViewer();
        List selectedObjects = viewer.getSelectedEditParts();

        if (getCurrentInput().isModKeyDown(SWT.MOD1)) {
            lastSelectedShiftPart = null;
            if (selectedObjects.contains(getSourceEditPart()))
                viewer.deselect(getSourceEditPart());
            else
                viewer.appendSelection(getSourceEditPart());
        } else if (getCurrentInput().isShiftKeyDown()) {
            if (lastSelectedShiftPart == null) {
                if (!selectedObjects.isEmpty()) {
                    Object object = selectedObjects.get(selectedObjects.size() - 1);
                    if (object instanceof TreeNodeEditPart) {
                        lastSelectedShiftPart = (EditPart) object;
                    }
                } else {
                    lastSelectedShiftPart = getSourceEditPart();
                }
            }
            List<EditPart> toselect = new ArrayList<EditPart>();
            List<EditPart> deSelect = new ArrayList<EditPart>();
            getShiftAffactedEditPart(toselect, deSelect);
            for (EditPart part : deSelect) {
                viewer.deselect(part);
            }
            for (EditPart part : toselect) {
                if (!viewer.getSelectedEditParts().contains(part)) {
                    viewer.appendSelection(part);
                }
            }
        } else {
            lastSelectedShiftPart = null;
            viewer.select(getSourceEditPart());
        }
    }

    private void getShiftAffactedEditPart(List toSelect, List deSelect) {
        if (lastSelectedShiftPart == null || lastSelectedShiftPart == getSourceEditPart()) {
            toSelect.add(getSourceEditPart());
            return;
        }

        if (lastSelectedShiftPart instanceof TreeNodeEditPart) {
            AbstractInOutTreeEditPart treePart = getTreeNodeNodePart((TreeNodeEditPart) lastSelectedShiftPart);
            if (treePart != null) {
                List<TreeNodeEditPart> partList = new ArrayList<TreeNodeEditPart>();
                getChildNodeList(partList, treePart.getChildren());

                int index = partList.indexOf(lastSelectedShiftPart);
                int index2 = partList.indexOf(getSourceEditPart());
                if (index2 != -1) {
                    for (int i = Math.min(index, index2); i < Math.max(index, index2) + 1; i++) {
                        if (!toSelect.contains(partList.get(i))) {
                            toSelect.add(partList.get(i));
                        }
                    }

                    List selectedEditParts = getCurrentViewer().getSelectedEditParts();
                    for (int i = 0; i < selectedEditParts.size(); i++) {
                        if (partList.contains(selectedEditParts.get(i)) && !toSelect.contains(selectedEditParts.get(i))) {
                            deSelect.add(selectedEditParts.get(i));
                        }
                    }
                }
            }
        }
    }

    private AbstractInOutTreeEditPart getTreeNodeNodePart(TreeNodeEditPart nodepart) {
        if (nodepart.getParent() instanceof AbstractInOutTreeEditPart) {
            return (AbstractInOutTreeEditPart) nodepart.getParent();
        } else if (nodepart.getParent() instanceof TreeNodeEditPart) {
            return getTreeNodeNodePart((TreeNodeEditPart) nodepart.getParent());
        }
        return null;
    }

    private void getChildNodeList(List<TreeNodeEditPart> childParts, List children) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) instanceof TreeNodeEditPart) {
                TreeNodeEditPart child = (TreeNodeEditPart) children.get(i);
                childParts.add(child);
                if (!child.getChildren().isEmpty()) {
                    getChildNodeList(childParts, child.getChildren());
                }
            }
        }

    }
}
