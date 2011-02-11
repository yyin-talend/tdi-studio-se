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
package org.talend.designer.xmlmap.policy;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.talend.commons.ui.swt.geftree.figure.ExpandCollapseFigure;
import org.talend.designer.xmlmap.figures.OutputTreeNodeFigure;
import org.talend.designer.xmlmap.figures.XmlTreeBranch;
import org.talend.designer.xmlmap.figures.TreeNodeFigure;
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
                if (((ExpandCollapseFigure) me.getSource()).getParent() instanceof XmlTreeBranch) {
                    XmlTreeBranch treeBranch = (XmlTreeBranch) ((ExpandCollapseFigure) me.getSource()).getParent();
                    expanded = !treeBranch.isExpanded();
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

        // public void mouseDoubleClicked(MouseEvent me) {
        // expand();
        // }

        public void expand() {
            TreeNodeEditPart host = (TreeNodeEditPart) getHost();
            host.expand(expanded = !expanded);
            AbstractGraphicalEditPart parent = (AbstractGraphicalEditPart) host.getParent();
            EditPolicy editPolicy = parent.getEditPolicy(LAYOUT_ROLE);
            if (editPolicy != null) {
                Request request = new Request(SIZE_CHANGED);
                Map hashMap = new HashMap();
                hashMap.put(SIZE_CHANGED, host);
                request.setExtendedData(hashMap);
                editPolicy.showSourceFeedback(request);
            }

        }

    }

    // unused
    // private void notifyParent() {
    // TreeExpandSupportEditPolicy treeExpandSupportEditPolicy = ((TreeExpandSupportEditPolicy) getHost().getParent()
    // .getEditPolicy("Expand support"));
    // if (treeExpandSupportEditPolicy != null)
    // treeExpandSupportEditPolicy.sizeChanged();
    // }

    @Override
    public void activate() {
        super.activate();
        TreeNodeEditPart host = (TreeNodeEditPart) getHost();
        IFigure figure = null;
        if (host.getFigure() instanceof TreeNodeFigure) {
            figure = ((TreeNodeFigure) host.getFigure()).getTreeBranch();
        } else if (host.getFigure() instanceof OutputTreeNodeFigure) {
            figure = ((OutputTreeNodeFigure) host.getFigure()).getTreeBranch();
        } else {
            figure = host.getFigure();
        }

        if (figure instanceof XmlTreeBranch) {
            XmlTreeBranch treeBranch = (XmlTreeBranch) figure;
            if (treeBranch.getExpandCollapseFigure() != null) {
                treeBranch.getExpandCollapseFigure().addMouseListener(l);
            }
        }
        // host.getFigure().addMouseListener(l);

    }

    @Override
    public void deactivate() {
        // TreeNodeEditPart host = (TreeNodeEditPart) getHost();
        // host.getFigure().removeMouseListener(l);
        TreeNodeEditPart host = (TreeNodeEditPart) getHost();
        IFigure figure = null;
        if (host.getFigure() instanceof TreeNodeFigure) {
            figure = ((TreeNodeFigure) host.getFigure()).getTreeBranch();
        } else if (host.getFigure() instanceof OutputTreeNodeFigure) {
            figure = ((OutputTreeNodeFigure) host.getFigure()).getTreeBranch();
        } else {
            figure = host.getFigure();
        }

        if (figure instanceof XmlTreeBranch) {
            XmlTreeBranch treeBranch = (XmlTreeBranch) figure;
            if (treeBranch.getExpandCollapseFigure() != null) {
                treeBranch.getExpandCollapseFigure().removeMouseListener(l);
            }
        }
        super.deactivate();
    }

    // public Command getCommand(Request request) {
    //
    // if (REQ_CREATE.equals(request.getType())) {
    // return new Command("expand") {
    //
    // public void execute() {
    // TreeNodeEditPart host = (TreeNodeEditPart) getHost();
    // host.expand(true);
    // }
    // };
    // }
    //
    // return super.getCommand(request);
    // }
}
