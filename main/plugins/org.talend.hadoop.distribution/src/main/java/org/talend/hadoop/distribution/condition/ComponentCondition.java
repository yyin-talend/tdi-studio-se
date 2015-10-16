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
package org.talend.hadoop.distribution.condition;

/**
 * created by rdubois on 16 oct. 2015 Detailled comment
 *
 */
public class ComponentCondition {

    protected Expression mExpression;

    public ComponentCondition(Expression expression) {
        this.mExpression = expression;
    }

    public String getConditionString() {
        return this.mExpression.getExpressionString();
    }

}
