package org.talend.designer.mapper.model.tableentry;

import org.talend.designer.abstractmap.model.table.IDataMapTable;

public class ColumnNameFilterEntry extends DataMapTableEntry{

    public ColumnNameFilterEntry(String cloumnNameFilter) {
        super(cloumnNameFilter);
        // TODO Auto-generated constructor stub
    }

    public ColumnNameFilterEntry(IDataMapTable abstractDataMapTable) {
        super(abstractDataMapTable);
    }


    public boolean isTableEntry() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isColumnEntry() {
        // TODO Auto-generated method stub
        return false;
    }

   
}
