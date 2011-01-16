// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

/**
 * wchen class global comment. Detailled comment
 */
public class CreateNodeConnectionRequest extends CreateRequest {

    public static final int DROP_EXPRESSION = 0;

    public static final int DROP_TREE = 1;

    private EditPart targetEditPart;

    private int dropType;

    public CreateNodeConnectionRequest(EditPart targetEditPart) {
        this.targetEditPart = targetEditPart;
        setDropType(DROP_TREE);
    }

    public EditPart getTargetEditPart() {
        return this.targetEditPart;
    }

    public void setDropType(int dropType) {
        this.dropType = dropType;
    }

    public int getDropType() {
        return dropType;
    }

}
