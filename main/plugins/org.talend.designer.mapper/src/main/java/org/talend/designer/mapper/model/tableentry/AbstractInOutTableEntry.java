// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper.model.tableentry;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.abstractmap.model.table.IDataMapTable;
import org.talend.designer.abstractmap.model.tableentry.IColumnEntry;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class AbstractInOutTableEntry extends DataMapTableEntry implements IColumnEntry {

    private IMetadataColumn metadataColumn;

    private String parentName;

    public AbstractInOutTableEntry(IMetadataColumn metadataColumn) {

        this.metadataColumn = metadataColumn;
    }

    public AbstractInOutTableEntry(IDataMapTable abstractDataMapTable, IMetadataColumn metadataColumn, String expression) {
        super(abstractDataMapTable, expression);
        this.metadataColumn = metadataColumn;
    }

    public AbstractInOutTableEntry(IDataMapTable abstractDataMapTable, IMetadataColumn metadataColumn) {
        super(abstractDataMapTable);
        this.metadataColumn = metadataColumn;
    }

    public AbstractInOutTableEntry(IDataMapTable abstractDataMapTable, String name, String expression) {
        super(abstractDataMapTable, name, expression);
    }

    public IMetadataColumn getMetadataColumn() {
        return this.metadataColumn;
    }

    public String getName() {
        return this.metadataColumn.getLabel();
    }

    public void setName(String name) {
        this.metadataColumn.setLabel(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.tableentry.IDataMapTableEntry#isTableEntry()
     */
    public boolean isTableEntry() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.tableentry.IDataMapTableEntry#isColumnEntry()
     */
    public boolean isColumnEntry() {
        return true;
    }

    /**
     * Getter for parentName.
     * 
     * @return the parentName
     */


    /**
     * Sets the parentName.
     * 
     * @param parentName the parentName to set
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getTableName() {
        return this.parentName;
    }

}
