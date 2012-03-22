// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.parts;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.talend.designer.xmlmap.figures.OutputXmlTreeFigure;
import org.talend.designer.xmlmap.figures.treeNode.RootTreeNodeFigure;
import org.talend.designer.xmlmap.figures.treeNode.RowFigure;
import org.talend.designer.xmlmap.figures.treeNode.TreeNodeFigure;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapPackage;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class OutputTreeNodeEditPart extends TreeNodeEditPart {

    @Override
    protected IFigure createFigure() {
        OutputTreeNode model = (OutputTreeNode) getModel();
        boolean isRoot = false;
        if (model.eContainer() instanceof AbstractInOutTree) {
            isRoot = true;
        }

        final RowFigure testRow = new RowFigure(this, !isRoot);
        TreeNodeFigure treeNodeFigure = null;
        if (isRoot) {
            treeNodeFigure = new RootTreeNodeFigure(testRow);
        } else {
            treeNodeFigure = new TreeNodeFigure(testRow);
        }

        return treeNodeFigure;
    }

    @Override
    public List getModelChildren() {
        TreeNode model = (TreeNode) getModel();
        return model.getChildren();
    }

    @Override
    protected List getModelTargetConnections() {
        OutputTreeNode model = (OutputTreeNode) getModel();
        return model.getIncomingConnections();
    }

    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        super.addChildVisual(childEditPart, index);
    }

    @Override
    public void notifyChanged(Notification notification) {
        super.notifyChanged(notification);
        int type = notification.getEventType();
        int featureId = notification.getFeatureID(XmlmapPackage.class);
        switch (type) {
        case Notification.SET:
            switch (featureId) {
            case XmlmapPackage.TREE_NODE__TYPE:
                if (XmlMapUtil.DOCUMENT.equals(notification.getOldValue())
                        || !XmlMapUtil.DOCUMENT.equals(notification.getOldValue())
                        && XmlMapUtil.DOCUMENT.equals(notification.getNewValue())) {
                    AbstractInOutTreeEditPart inOutTreeEditPart = getInOutTreeEditPart(this);
                    if (inOutTreeEditPart.getFigure() instanceof OutputXmlTreeFigure) {
                        ((OutputXmlTreeFigure) inOutTreeEditPart.getFigure()).update(XmlmapPackage.TREE_NODE__TYPE);
                    }
                }
            }
            break;
        case Notification.ADD:
        case Notification.ADD_MANY:
        case Notification.REMOVE:
        case Notification.REMOVE_MANY:
            switch (featureId) {
            case XmlmapPackage.INPUT_LOOP_NODES_TABLE__INPUTLOOPNODES:
            case XmlmapPackage.OUTPUT_TREE_NODE__INPUT_LOOP_NODES_TABLE:
                if (getFigure() instanceof TreeNodeFigure) {
                    TreeNodeFigure outputFigure = (TreeNodeFigure) getFigure();
                    if (outputFigure.getElement() != null) {
                        outputFigure.getElement().getBranchContent().updateLoopButtonFigure();
                    }
                }
                break;
            default:
                break;
            }
        }
    }

    @Override
    public void activate() {
        super.activate();
        OutputTreeNode outputNode = (OutputTreeNode) getModel();
        if (outputNode.getInputLoopNodesTable() != null) {
            outputNode.getInputLoopNodesTable().eAdapters().add(this);
        }
    }

    @Override
    public void deactivate() {
        super.deactivate();
        OutputTreeNode outputNode = (OutputTreeNode) getModel();
        if (outputNode.getInputLoopNodesTable() != null) {
            outputNode.getInputLoopNodesTable().eAdapters().remove(this);
        }
    }
}
