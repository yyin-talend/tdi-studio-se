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
package org.talend.designer.xmlmap.figures.varnode;

import org.talend.designer.gefabstractmap.figures.var.VarEntityFigure;

/**
 * created by Administrator on 2013-1-11 Detailled comment
 * 
 */
public class VarNodeFigure extends VarEntityFigure {

    public VarNodeFigure(VarEntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected VarEntityManager getEntityManager() {
        return (VarEntityManager) super.getEntityManager();
    }

    @Override
    public void setNullable(boolean isCheck) {
        getEntityManager().getModel().setNullable(isCheck);
    }

    @Override
    public boolean isNullable() {
        return getEntityManager().getModel().isNullable();
    }

    @Override
    public String getVarName() {
        return getEntityManager().getModel().getName();
    }

    @Override
    public String getExpressionText() {
        return getEntityManager().getModel().getExpression();
    }

    @Override
    public String getType() {
        return getEntityManager().getModel().getType();
    }

}
