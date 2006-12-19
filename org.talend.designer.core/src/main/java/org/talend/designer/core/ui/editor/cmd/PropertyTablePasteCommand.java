// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.ui.editor.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTablePasteCommand;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <B> bean
 */
public class PropertyTablePasteCommand<B> extends ExtendedTablePasteCommand {

    /**
     * DOC amaumont MetadataPasteCommand constructor comment.
     * @param extendedTable
     * @param validAssignableType
     * @param indexStartAdd
     */
    public PropertyTablePasteCommand(ExtendedTableModel extendedTable, Integer indexStartAdd) {
        super(extendedTable, indexStartAdd);
    }

    /**
     * DOC amaumont MetadataPasteCommand constructor comment.
     * @param extendedTable
     * @param instanceOfType
     */
    public PropertyTablePasteCommand(ExtendedTableModel extendedTable) {
        super(extendedTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTablePasteCommand#createPastableBeansList(java.util.List)
     */
    @Override
    public List createPastableBeansList(ExtendedTableModel extendedTable, List copiedObjectsList) {
        ArrayList list = new ArrayList();
        
        for (Object current : copiedObjectsList) {
            if (current instanceof Map) {
                // create a new column as a copy of this column
                HashMap<String, Object> row = new HashMap<String, Object>();
                Map<String, Object> clonedRow = (Map<String, Object>) row.clone();
//                newColumnCopy.setLabel(nextGeneratedColumnName);
                list.add(clonedRow);
            }
        }
        return list;
    }

//    private void getUniqueStringValue(String baseStringValue, List<B> beansList) {
//        UniqueStringGenerator<B> uniqueStringGenerator = new UniqueStringGenerator<B>(baseStringValue,
//                beansList) {
//
//            /*
//             * (non-Javadoc)
//             * 
//             * @see org.talend.commons.utils.data.list.UniqueStringGenerator#getBeanString(java.lang.Object)
//             */
//            @Override
//            protected String getBeanString(B bean) {
//                return bean.getLabel();
//            }
//
//        };
//
//        return uniqueStringGenerator.getUniqueString();
//
//    }
    
    
}
