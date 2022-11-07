/*
 * XML Type:  SalesOrderState
 * Namespace: http://schemas.microsoft.com/crm/2007/WebServices
 * Java type: com.microsoft.schemas.crm._2007.webservices.SalesOrderState
 *
 * Automatically generated - do not modify.
 */
package com.microsoft.schemas.crm._2007.webservices.impl;

import org.apache.xmlbeans.StringEnumAbstractBase;

/**
 * An XML SalesOrderState(@http://schemas.microsoft.com/crm/2007/WebServices).
 *
 * This is an atomic type that is a restriction of com.microsoft.schemas.crm._2007.webservices.SalesOrderState.
 */
public class SalesOrderStateImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements com.microsoft.schemas.crm._2007.webservices.SalesOrderState
{
    
    public SalesOrderStateImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, false);
    }
    
    protected SalesOrderStateImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
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
