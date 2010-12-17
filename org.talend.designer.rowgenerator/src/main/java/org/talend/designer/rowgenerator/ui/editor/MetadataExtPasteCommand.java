// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.rowgenerator.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.editor.MetadataTableEditor;
import org.talend.core.ui.metadata.extended.command.MetadataPasteCommand;
import org.talend.designer.rowgenerator.data.FunctionManagerExt;

/**
 * chuang class global comment. Detailled comment
 */
public class MetadataExtPasteCommand extends MetadataPasteCommand {

    /**
     * chuang MetadataExtPasteCommand constructor comment.
     * 
     * @param extendedTable
     */
    public MetadataExtPasteCommand(ExtendedTableModel extendedTable) {
        super(extendedTable);
    }

    MetadataExtPasteCommand(ExtendedTableModel extendedTable, Integer indexStartAdd) {
        super(extendedTable, indexStartAdd);
    }

    @Override
    public List createPastableBeansList(ExtendedTableModel extendedTable, List copiedObjectsList) {
        ArrayList list = new ArrayList();
        for (Object current : copiedObjectsList) {
            if (current instanceof IMetadataColumn) {
                IMetadataColumn copy = ((IMetadataColumn) current).clone();
                copy.setLabel(((MetadataTableEditor) extendedTable).getNextGeneratedColumnName(copy.getLabel()));
                if (copy instanceof MetadataColumnExt) {
                    list.add(copy);
                } else {
                    MetadataColumnExt metadataColumnExt = new MetadataColumnExt((MetadataColumn) copy);
                    metadataColumnExt.setFunction((new FunctionManagerExt()).getDefaultFunction(metadataColumnExt,
                            metadataColumnExt.getTalendType()));
                    list.add(metadataColumnExt);
                }
            }
        }
        return list;
    }

}
