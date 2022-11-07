/*
 * XML Type:  AccessRights
 * Namespace: http://schemas.microsoft.com/crm/2006/CoreTypes
 * Java type: com.microsoft.schemas.crm._2006.coretypes.AccessRights
 *
 * Automatically generated - do not modify.
 */
package com.microsoft.schemas.crm._2006.coretypes.impl;

import java.util.List;
import org.apache.xmlbeans.StringEnumAbstractBase;

/**
 * An XML AccessRights(@http://schemas.microsoft.com/crm/2006/CoreTypes).
 *
 * This is a list type whose items are com.microsoft.schemas.crm._2006.coretypes.AccessRights$Item.
 */
public class AccessRightsImpl extends org.apache.xmlbeans.impl.values.XmlListImpl implements com.microsoft.schemas.crm._2006.coretypes.AccessRights
{
    
    public AccessRightsImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, false);
    }
    
    protected AccessRightsImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
    {
        super(sType, b);
    }

    @Override
    public List<?> listValue() {
        return getListValue();
    }

    @Override
    public List<?> xlistValue() {
        return xgetListValue();
    }

    @Override
    public void set(List<?> list) {
        setListValue(list);
    }

    /**
     * An anonymous inner XML type.
     *
     * This is an atomic type that is a restriction of com.microsoft.schemas.crm._2006.coretypes.AccessRights$Item.
     */
    public static class ItemImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements com.microsoft.schemas.crm._2006.coretypes.AccessRights.Item
    {
        
        public ItemImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected ItemImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
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
}
