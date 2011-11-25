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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.MoveHandleLocator;
import org.talend.designer.xmlmap.parts.AbstractInOutTreeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

/**
 * WCHEN class global comment. Detailled comment
 */
public class RowSelectionEditPolicy extends NonResizableEditPolicy {

    @Override
    protected List createSelectionHandles() {
        List list = new ArrayList();
        list.add(new RowMoveHandler((GraphicalEditPart) getHost()));
        return list;
    }

    class RowMoveHandler extends MoveHandle {

        public RowMoveHandler(GraphicalEditPart owner) {
            super(owner, null);
        }

        @Override
        public Locator getLocator() {
            setLocator(new MoveHandleLocator(getLocatorFigure(getOwner())));
            return super.getLocator();
        }

        private IFigure getLocatorFigure(GraphicalEditPart owner) {
            Figure figure = null;
            if (owner instanceof TreeNodeEditPart) {
                TreeNodeEditPart treeNodePart = (TreeNodeEditPart) owner;
                AbstractInOutTreeEditPart inOutTreeEditPart = treeNodePart.getInOutTreeEditPart(treeNodePart);
                if (inOutTreeEditPart != null) {
                    Rectangle treeBounds = inOutTreeEditPart.getFigure().getBounds();
                    Rectangle treeNodeBounds = owner.getFigure().getBounds();
                    if (treeBounds.x + 1 != treeNodeBounds.x || treeNodeBounds.width > treeBounds.width) {
                        figure = new Figure();
                        figure.setBounds(new Rectangle(treeBounds.x + 1, treeNodeBounds.y, treeBounds.width - 2,
                                treeNodeBounds.height));
                        return figure;
                    }
                }

            }
            return owner.getFigure();

        }

    }

}
