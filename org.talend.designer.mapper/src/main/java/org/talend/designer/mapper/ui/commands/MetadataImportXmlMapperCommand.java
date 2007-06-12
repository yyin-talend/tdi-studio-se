// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.mapper.ui.commands;

import java.io.File;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.ui.extended.command.MetadataImportXmlCommand;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 */
public class MetadataImportXmlMapperCommand extends MetadataImportXmlCommand {

    /**
     * DOC amaumont MetadataImportXmlMapperCommand constructor comment.
     * @param extendedTableModel
     * @param file
     */
    public MetadataImportXmlMapperCommand(ExtendedTableModel extendedTableModel, File file) {
        super(extendedTableModel, file);
    }

    
    
    /* (non-Javadoc)
     * @see org.talend.core.ui.extended.command.MetadataImportXmlCommand#execute()
     */
    @Override
    public void execute() {
        super.execute();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.commands.Command#redo()
     */
    @Override
    public void redo() {
        super.redo();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo() {
        super.undo();
    }

    
    
}
