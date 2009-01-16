// ============================================================================
//
// Copyright (C) 2006-2008 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.filemultischemas.data;

import java.util.ArrayList;
import java.util.List;

/**
 * cLi class global comment. Detailled comment
 * 
 * for schema tree node.
 */
public class SchemasKeyData {

    private SchemasKeyData parent;

    private String keyName;

    private List<SchemasKeyData> children = new ArrayList<SchemasKeyData>();

    private List<MultiSchemaRowData> rowsData = new ArrayList<MultiSchemaRowData>();

    private List<MultiMetadataColumn> metadataColumns = new ArrayList<MultiMetadataColumn>();;

    public SchemasKeyData(String key) {
        super();
        this.keyName = key;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public void setParent(SchemasKeyData parent) {
        this.parent = parent;
    }

    public SchemasKeyData getParent() {
        return this.parent;
    }

    public void addRowsData(MultiSchemaRowData rowData) {
        if (rowData != null) {
            getRowsData().add(rowData);
            rowData.setParent(this);
        }
    }

    public List<MultiSchemaRowData> getRowsData() {
        return this.rowsData;
    }

    public void addChild(SchemasKeyData child) {
        if (child != null && child != this) {
            // have contain in current
            if (getChildren().contains(child)) {
                return;
            }
            // delete from parent
            SchemasKeyData p = child.getParent();
            if (p != null) {
                p.getChildren().remove(child);
            }
            // avoid to recycle, child is parent of current before.
            if (isParentOfParent(this.getParent(), child)) {
                updateRecycleChildren(child);

            }
            // add to current
            getChildren().add(child);
            child.setParent(this);
        }
    }

    private void updateRecycleChildren(SchemasKeyData current) {
        SchemasKeyData p = current.getParent();
        for (SchemasKeyData tmpChild : current.getChildren()) {
            tmpChild.setParent(p);
            if (p != null) {
                p.getChildren().add(tmpChild);
            }
        }
        // not child for current.
        current.getChildren().clear();

    }

    private boolean isParentOfParent(SchemasKeyData parent, SchemasKeyData child) {
        if (parent == null) {
            return false;
        }
        if (parent == child) {
            return true;
        }
        return isParentOfParent(parent.getParent(), child);
    }

    public List<SchemasKeyData> getChildren() {
        return this.children;
    }

    public int getTagLevel() {
        int level = -1;
        SchemasKeyData p = this;
        while ((p = p.getParent()) != null) {
            level++;
        }
        return level;
    }

    public List<MultiMetadataColumn> getMetadataColumns() {
        return this.metadataColumns;
    }

    public void setMetadataColumns(List<MultiMetadataColumn> metadataColumns) {
        this.metadataColumns.clear();
        if (metadataColumns != null) {
            this.metadataColumns = metadataColumns;
        }
    }

}
