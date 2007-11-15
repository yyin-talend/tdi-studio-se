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
package org.talend.designer.dbmap.model.tableentry;

import org.talend.designer.abstractmap.model.table.IDataMapTable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: FilterTableEntry.java 898 2006-12-07 11:06:17Z amaumont $
 * 
 */
public class FilterTableEntry extends TableEntry {

    public FilterTableEntry(IDataMapTable abstractDataMapTable, String name, String expression) {
        super(abstractDataMapTable, name, expression);
    }

}
