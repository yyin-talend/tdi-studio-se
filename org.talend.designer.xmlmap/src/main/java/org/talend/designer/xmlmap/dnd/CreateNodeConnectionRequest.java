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
package org.talend.designer.xmlmap.dnd;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.CreateRequest;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;

/**
 * wchen class global comment. Detailled comment
 */
public class CreateNodeConnectionRequest extends CreateRequest {

    private EditPart targetEditPart;

    public CreateNodeConnectionRequest(EditPart targetEditPart) {
        this.targetEditPart = targetEditPart;
    }

    public EditPart getTargetEditPart() {
        return this.targetEditPart;
    }

    @Override
    public AbstractNode getNewObject() {
        if (getFactory() instanceof NewNodeCreationFactory) {
            return ((NewNodeCreationFactory) getFactory()).getNewObject();
        }
        return null;
    }

    public DropType getNewObjectType() {
        if (getFactory() instanceof NewNodeCreationFactory) {
            return ((NewNodeCreationFactory) getFactory()).getObjectType();
        }
        return null;
    }
}
