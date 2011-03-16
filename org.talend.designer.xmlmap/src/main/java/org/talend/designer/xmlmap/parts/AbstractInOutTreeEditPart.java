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
package org.talend.designer.xmlmap.parts;

import java.util.ArrayList;
import java.util.List;

/**
 * DOC talend class global comment. Detailled comment
 */
public class AbstractInOutTreeEditPart extends BaseEditPart {

    private boolean treeOrChildSelected = false;

    public boolean isTreeOrChildSelected() {
        return treeOrChildSelected;
    }

    public void setTreeOrChildSelected(boolean treeOrChildSelected) {
        this.treeOrChildSelected = treeOrChildSelected;
    }

    public void updateChildrenConnections(List treeNodeParts, boolean selected) {
        for (Object obj : treeNodeParts) {
            if (obj instanceof TreeNodeEditPart) {
                List connections = new ArrayList();
                TreeNodeEditPart treeNodePart = (TreeNodeEditPart) obj;
                connections.addAll(treeNodePart.getSourceConnections());
                connections.addAll(treeNodePart.getTargetConnections());
                for (Object connObj : connections) {
                    if (connObj instanceof BaseConnectionEditPart) {
                        ((BaseConnectionEditPart) connObj).updateForegroundColor(selected);
                    }
                }
                if (!treeNodePart.getChildren().isEmpty()) {
                    updateChildrenConnections(treeNodePart.getChildren(), selected);
                }
            }
        }
    }

}
