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
package org.talend.designer.xmlmap.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.talend.designer.xmlmap.figures.routers.LookupConnectionRouter;
import org.talend.designer.xmlmap.model.emf.xmlmap.IConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class LookupConnectionEditPart extends BaseConnectionEditPart {

    private LookupConnectionRouter cr;

    @Override
    protected void createEditPolicies() {

    }

    @Override
    protected IFigure createFigure() {
        PolylineConnection connection = new PolylineConnection();
        connection.setTargetDecoration(new PolygonDecoration());
        // connection.setBackgroundColor(ColorConstants.yellow);
        connection.setForegroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_UNSELECTED_LOOKUP_LINKS));
        connection.setLineWidth(2);
        cr = new LookupConnectionRouter();
        connection.setConnectionRouter(cr);
        return connection;
    }

    private int calculateConnOffset() {
        LookupConnection model = (LookupConnection) getModel();

        if (model.getSource() == null) {
            return 0;
        }
        TreeNode sourceTreeNode = (TreeNode) model.getSource();
        List<IConnection> outConns = new ArrayList<IConnection>();
        TreeNode inputTreeNodeRoot = XmlMapUtil.getTreeNodeRoot(sourceTreeNode);
        if (inputTreeNodeRoot != null) {
            InputXmlTree inputTree = (InputXmlTree) inputTreeNodeRoot.eContainer();
            outConns.addAll(XmlMapUtil.getAllNodeLookConnections(inputTree));
        }
        int indexOf = outConns.indexOf(model);
        if (indexOf != -1) {
            return -(indexOf + 1) * XmlMapUtil.DEFAULT_OFFSET;
        }
        return 0;
    }

    @Override
    public IFigure getFigure() {
        IFigure figure = super.getFigure();
        if (cr != null) {// && cr.getOffset() == 0
            cr.setOffset(calculateConnOffset());
        }
        return figure;
    }

    public void updateForegroundColor(boolean selected) {
        if (selected) {
            getFigure().setForegroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_SELECTED_LOOKUP_LINKS));
        } else {
            getFigure().setForegroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_UNSELECTED_LOOKUP_LINKS));
        }
    }

}
