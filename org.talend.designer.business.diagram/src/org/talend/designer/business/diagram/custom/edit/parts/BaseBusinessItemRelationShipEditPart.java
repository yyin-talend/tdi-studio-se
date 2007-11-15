// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.business.diagram.custom.edit.parts;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;
import org.talend.designer.business.diagram.custom.edit.policies.BusinessItemDragDropEditPolicy;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id: BusinessItemShapeEditPart.java 1 2006-09-29 17:06:40 +0000 (ven, 29 sep 2006) nrousseau $
 * 
 */
public abstract class BaseBusinessItemRelationShipEditPart extends ConnectionNodeEditPart {

    /**
     * DOC mhelleboid BusinessItemShapeEditPart constructor comment.
     * 
     * @param view
     */
    public BaseBusinessItemRelationShipEditPart(View view) {
        super(view);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart#createDefaultEditPolicies()
     */
    @Override
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new BusinessItemDragDropEditPolicy());
    }
}
