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
 * Represents a condition composed by an {@link Expression} and another {@link ComponentCondition}, linked by a
 * {@link BooleanOperator} operator.
 *
 */
public class MultiComponentCondition extends SimpleComponentCondition {

    private ComponentCondition mOtherCondition;

    private BooleanOperator mBooleanOperator;

    public MultiComponentCondition(Expression expression, ComponentCondition otherCondition, BooleanOperator booleanOperator) {
        super(expression);
        this.mOtherCondition = otherCondition;
        this.mBooleanOperator = booleanOperator;
    }

    @Override
    public String getConditionString() {
        return this.mExpression.getExpressionString()
                + " " + mBooleanOperator.name() + " " + this.mOtherCondition.getConditionString(); //$NON-NLS-1$ //$NON-NLS-2$ 
    }
}
