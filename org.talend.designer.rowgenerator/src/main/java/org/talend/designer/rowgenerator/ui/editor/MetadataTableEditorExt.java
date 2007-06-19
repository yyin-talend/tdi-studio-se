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
package org.talend.designer.rowgenerator.ui.editor;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.editor.MetadataTableEditor;
import org.talend.designer.rowgenerator.data.FunctionManagerExt;
import org.talend.designer.rowgenerator.ui.RowGeneratorUI;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: MetadataTableEditorExt.java,v 1.3 2007/01/31 05:20:52 pub Exp $
 * 
 */
public class MetadataTableEditorExt extends MetadataTableEditor {

    private RowGeneratorUI ui;

    /**
     * qzhang MetadataTableEditorExt constructor comment.
     */
    public MetadataTableEditorExt(IMetadataTable metadataTable, String titleName) {
        super(metadataTable, titleName);
    }

    public IMetadataColumn createNewMetadataColumn() {
        final MetadataColumnExt metadataColumnExt = new MetadataColumnExt((MetadataColumn) super.createNewMetadataColumn());
        metadataColumnExt.setFunction((new FunctionManagerExt()).getDefaultFunction(metadataColumnExt, metadataColumnExt
                .getTalendType()));
        return metadataColumnExt;
    }

    public void setRowGenUI(RowGeneratorUI ui) {
        this.ui = ui;
    }

    public RowGeneratorUI getRowGenUI() {
        return this.ui;
    }

}
