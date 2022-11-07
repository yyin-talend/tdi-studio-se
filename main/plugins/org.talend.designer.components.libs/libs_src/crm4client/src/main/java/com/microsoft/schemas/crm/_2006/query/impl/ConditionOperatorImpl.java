/*
 * XML Type:  ConditionOperator
 * Namespace: http://schemas.microsoft.com/crm/2006/Query
 * Java type: com.microsoft.schemas.crm._2006.query.ConditionOperator
 *
 * Automatically generated - do not modify.
 */
package com.microsoft.schemas.crm._2006.query.impl;

import org.apache.xmlbeans.StringEnumAbstractBase;

/**
 * An XML ConditionOperator(@http://schemas.microsoft.com/crm/2006/Query).
 *
 * This is an atomic type that is a restriction of com.microsoft.schemas.crm._2006.query.ConditionOperator.
 */
public class ConditionOperatorImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements com.microsoft.schemas.crm._2006.query.ConditionOperator
{
    
    public ConditionOperatorImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, false);
    }
    
    protected ConditionOperatorImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
    {
        super(sType, b);
    }

    @Override
    public StringEnumAbstractBase enumValue() {
        return getEnumValue();
    }

    @Override
    public void set(StringEnumAbstractBase e) {
        setEnumValue(e);
    }
}
