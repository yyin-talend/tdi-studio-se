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
package org.talend.repository.ui.actions.metadata.importing;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class PositionHelper {

    private int total;

    private int nameIndex;

    private int connectionIndex;

    private int originalTablenameIndex;

    private int originalLabelIndex;

    private int keyIndex;

    private int lengthIndex;

    public PositionHelper(int total, int nameIndex, int connectionIndex, int originalTablenameIndex, int originalLabelIndex,
            int keyIndex, int lengthIndex) {
        super();
        this.total = total;
        this.nameIndex = nameIndex;
        this.connectionIndex = connectionIndex;
        this.originalTablenameIndex = originalTablenameIndex;
        this.originalLabelIndex = originalLabelIndex;
        this.keyIndex = keyIndex;
        this.lengthIndex = lengthIndex;
    }

    public int getTotal() {
        return this.total;
    }

    public int getNameIndex() {
        return this.nameIndex;
    }

    public int getConnectionIndex() {
        return this.connectionIndex;
    }

    public int getOriginalTablenameIndex() {
        return this.originalTablenameIndex;
    }

    public int getOriginalLabelIndex() {
        return this.originalLabelIndex;
    }

    public int getKeyIndex() {
        return this.keyIndex;
    }

    public int getLengthIndex() {
        return this.lengthIndex;
    }

}
