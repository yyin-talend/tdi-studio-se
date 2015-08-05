// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.policy;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.talend.designer.xmlmap.figures.treeNode.ExpandCollapseFigure;
import org.talend.designer.xmlmap.figures.treeNode.RowFigure;
import org.talend.designer.xmlmap.figures.treeNode.TreeNodeFigure;
import org.talend.designer.xmlmap.figures.treeNode.TreeSelectManager;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeExpandSupportEditPolicy extends GraphicalEditPolicy {

    private boolean expanded = true;

    private Listener l = new Listener();

    public static final String EXPAND_SUPPORT = "EXPAND_SUPPORT";

    public static final String SIZE_CHANGED = "SIZE_CHANGED";

    public EditPart getTargetEditPart(Request request) {
        return super.getTargetEditPart(request);
    }

    private class Listener extends MouseListener.Stub {

        public void mousePressed(MouseEvent me) {
            if (me.getSource() instanceof ExpandCollapseFigure) {
                ExpandCollapseFigure ecFigure = (ExpandCollapseFigure) me.getSource();
                TreeSelectManager manager = TreeSelectManager.getManager();
                if (ecFigure.getParent().getParent() instanceof RowFigure) {
                    RowFigure rowFigure = (RowFigure) ecFigure.getParent().getParent();
                    manager.setSelection(rowFigure); // select first
                    TreeNodeFigure treeNodeFigure = ecFigure.getParent().getTreeNodeFigure();
                    if (treeNodeFigure != null) {
                        expanded = treeNodeFigure.isExpanded();
                        treeNodeFigure.doExpandCollapse();
                    }
                }
            }

            if (getHost() instanceof OutputTreeNodeEditPart) {
                OutputTreeNodeEditPart host = (OutputTreeNodeEditPart) getHost();
                host.refreshChildrenTargetConnections(host, expanded);
            } else if (getHost() instanceof TreeNodeEditPart) {
                TreeNodeEditPart host = (TreeNodeEditPart) getHost();
                host.refreshChildrenSourceConnections(host, expanded);
            }
        }

    }

    @Override
    public void activate() {
        super.activate();
        TreeNodeEditPart host = (TreeNodeEditPart) getHost();
        if (host.getFigure() instanceof TreeNodeFigure) {
            TreeNodeFigure treeNodeFigure = (TreeNodeFigure) host.getFigure();
            ExpandCollapseFigure ecFigure = treeNodeFigure.getElement().getTreeBranch().getExpandCollapseFigure();
            if (ecFigure != null) {
                ecFigure.addMouseListener(l);
            }

        }

    }

    @Override
    public void deactivate() {
        TreeNodeEditPart host = (TreeNodeEditPart) getHost();
        if (host.getFigure() instanceof TreeNodeFigure) {
            TreeNodeFigure treeNodeFigure = (TreeNodeFigure) host.getFigure();
            ExpandCollapseFigure ecFigure = treeNodeFigure.getElement().getTreeBranch().getExpandCollapseFigure();
            if (ecFigure != null) {
                ecFigure.removeMouseListener(l);
            }

        }
        super.deactivate();
    }

}
