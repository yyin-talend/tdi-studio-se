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
package org.talend.designer.hl7.model;

import org.talend.core.model.utils.TalendTextUtils;

import ca.uhn.hl7v2.model.Primitive;

public class PrimitiveModel extends AbstractStructureModel {

    private int componentindex;

    private int subcomponentindex;

    private Primitive primitive;

    private IModel parent;

    private String asColumnName;

    public PrimitiveModel(IModel parent, Primitive primitive, int componentindex, int subcomponentindex) {
        this.parent = parent;
        this.primitive = primitive;
        this.componentindex = componentindex;
        this.subcomponentindex = subcomponentindex;
    }

    @Override
    protected void generateDisplayName() {
        String parentName = "";
        if (parent instanceof TypeModel) {
            TypeModel tm = (TypeModel) parent;
            String extensionName = tm.getType().getName();
            String parentDisplayName = tm.getDisplayName();
            int extensionIndex = parentDisplayName.indexOf(extensionName);
            parentName = parentDisplayName.substring(0, extensionIndex - 1);
        }

        this.displayName = parentName + "-" + this.componentindex + "-" + this.subcomponentindex + TalendTextUtils.LBRACKET
                + this.primitive.getName() + TalendTextUtils.RBRACKET;
    }

}
