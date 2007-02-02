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
package org.talend.designer.rowgenerator.ui.editor;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ExportPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ImportPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.MoveDownPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.MoveUpPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.RemovePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.RemovePushButtonForExtendedTable;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.core.ui.metadata.editor.MetadataToolbarEditorView;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: MetadataToolbarEditorViewExt.java,v 1.9 2007/01/31 07:33:40 pub Exp $
 * 
 */
public class MetadataToolbarEditorViewExt extends MetadataToolbarEditorView {

    /**
     * qzhang MetadataToolbarEditorViewExt constructor comment.
     * 
     * @param parent
     * @param style
     * @param extendedTableViewer
     */
    public MetadataToolbarEditorViewExt(Composite parent, int style, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, style, extendedTableViewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createAddPushButton()
     */
    @Override
    protected AddPushButton createAddPushButton() {
        return new AddPushButtonForExtendedTable(this.toolbar, getExtendedTableViewer()) {

            @Override
            protected Object getObjectToAdd() {
                MetadataTableEditorExt tableEditorModel = (MetadataTableEditorExt) getExtendedTableViewer()
                        .getExtendedControlModel();
                return tableEditorModel.createNewMetadataColumn();
            }

            @Override
            protected void afterCommandExecution(Command executedCommand) {
                super.afterCommandExecution(executedCommand);
//                refreshPreview();
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView#createRemovePushButton()
     */
    protected RemovePushButton createRemovePushButton() {
        RemovePushButtonForExtendedTable removeButton2 = new RemovePushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected void afterCommandExecution(Command executedCommand) {
                super.afterCommandExecution(executedCommand);
//                refreshPreview();
            }
        };
        return removeButton2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastButton()
     */
    @Override
    public PastePushButton createPastePushButton() {
        PastePushButton pastePushButton2 = super.createPastePushButton();
        return pastePushButton2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createExportPushButton()
     */
    @Override
    protected ExportPushButton createExportPushButton() {
//        ExportPushButton exportPushButton2 = super.createExportPushButton();
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastButton()
     */
    @Override
    public ImportPushButton createImportPushButton() {
//        ImportPushButton importPushButton2 = super.createImportPushButton();
        return null;
    }

    /**
     * qzhang Comment method "refreshPreview".
     */
    protected void refreshPreview() {
        MetadataTableEditorExt editorExt = (MetadataTableEditorExt) this.getExtendedTableViewer()
                .getExtendedControlModel();
        editorExt.refreshPreview(editorExt.getMetadataColumnList());
    }

    /* (non-Javadoc)
     * @see org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView#createMoveUpPushButton()
     */
    @Override
    protected MoveUpPushButton createMoveUpPushButton() {
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView#createMoveDownPushButton()
     */
    @Override
    protected MoveDownPushButton createMoveDownPushButton() {
        return null;
    }
}
