// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.core.ui.editor.properties.macrowidgets.tableeditor;

import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ExportPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ImportPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButtonForExtendedTable;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.designer.core.ui.editor.cmd.PropertyTablePasteCommand;

/**
 * $Id$
 * 
 */
public class PropertiesTableToolbarEditorView extends ExtendedToolbarView {

    /**
     * DOC amaumont MetadataToolbarEditorView constructor comment.
     * 
     * @param parent
     * @param style
     * @param extendedTableViewer
     */
    public PropertiesTableToolbarEditorView(Composite parent, int style, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, style, extendedTableViewer);
    }

    
    
    /* (non-Javadoc)
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createComponents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createComponents(Composite parent) {
        super.createComponents(parent);
        
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
                PropertiesTableEditorModel tableEditorModel = (PropertiesTableEditorModel) getExtendedTableViewer()
                        .getExtendedControlModel();
                return tableEditorModel.createNewEntry();
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastButton()
     */
    @Override
    public PastePushButton createPastePushButton() {
        return new PastePushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, Integer indexWhereInsert) {
                 return new PropertyTablePasteCommand<Map<String,Object>>(extendedTableModel, indexWhereInsert);
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createExportPushButton()
     */
    @Override
    protected ExportPushButton createExportPushButton() {
//        return new ExportPushButtonForExtendedTable(toolbar, extendedTableViewer) {
//
//            @Override
//            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, File file) {
//                // return new MetadataExportXmlCommand((MetadataTableEditor) extendedTableModel, file);
//                return null;
//            }
//
//        };
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastButton()
     */
    @Override
    public ImportPushButton createImportPushButton() {
//        return new ImportPushButtonForExtendedTable(toolbar, extendedTableViewer) {
//
//            @Override
//            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, File file) {
//                // return new MetadataImportXmlCommand(extendedTableModel, file);
//                return null;
//            }
//
//        };
        return null;
    }

}
