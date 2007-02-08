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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
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
import org.talend.designer.rowgenerator.i18n.Messages;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: MetadataToolbarEditorViewExt.java,v 1.9 2007/01/31 07:33:40 pub Exp $
 * 
 */
public class MetadataToolbarEditorViewExt extends MetadataToolbarEditorView {

    private static String[] items = new String[] { Messages.getString("RowGenTableEditor2.Key.TitleText"),
            Messages.getString("RowGenTableEditor2.Type.TitleText"), Messages.getString("RowGenTableEditor2.Length.TitleText"),
            Messages.getString("RowGenTableEditor2.Precision.TitleText"),
            Messages.getString("RowGenTableEditor2.Nullable.TitleText"),
            Messages.getString("RowGenTableEditor2.Comment.TitleText"),
            Messages.getString("RowGenTableEditor2.Fuctions.TitleText"),
            Messages.getString("RowGenTableEditor2.Parameters.TitleText"),
            Messages.getString("RowGenTableEditor2.Preview.TitleText"), };

    private static String[] ids = new String[] { RowGenTableEditor2.KEY_ID_COLUMN, RowGenTableEditor2.TYPE_ID_COLUMN,
            RowGenTableEditor2.LENGTH_ID_COLUMN, RowGenTableEditor2.PRECISION_ID_COLUMN, RowGenTableEditor2.NULLABLE_ID_COLUMN,
            RowGenTableEditor2.COMMENT_ID_COLUMN, RowGenTableEditor2.FUNCTION_ID_COLUMN, RowGenTableEditor2.PARAMETER_ID_COLUMN,
            RowGenTableEditor2.PREVIEW_ID_COLUMN };

    private RowGenTableEditor2 genTableEditor2;

    /**
     * qzhang MetadataToolbarEditorViewExt constructor comment.
     * 
     * @param parent
     * @param style
     * @param extendedTableViewer
     */
    public MetadataToolbarEditorViewExt(Composite parent, int style, AbstractExtendedTableViewer extendedTableViewer,
            RowGenTableEditor2 editor2) {
        super(parent, style, extendedTableViewer);
        this.genTableEditor2 = editor2;
        createColumns();
    }

    /**
     * qzhang Comment method "createColumns".
     */
    private void createColumns() {
        final ToolBar toolBar2 = new ToolBar(toolbar, SWT.HORIZONTAL);
        final ToolItem columns = new ToolItem(toolBar2, SWT.DROP_DOWN);
        columns.setText(Messages.getString("MetadataToolbarEditorViewExt.Columns.Text")); //$NON-NLS-1$
        final Menu menu = createMenu(columns);
        columns.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent event) {
                // If they clicked the arrow, we show the list
                // if (event.detail == SWT.ARROW) {
                // Determine where to put the dropdown list
                ToolItem item = (ToolItem) event.widget;
                Rectangle rect = item.getBounds();
                Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
                menu.setLocation(pt.x, pt.y + rect.height);
                menu.setVisible(true);
                // }
            }
        });
    }

    /**
     * qzhang Comment method "notuse".
     */
    private Menu createMenu(final ToolItem columns) {
        Menu menu = new Menu(columns.getParent().getShell());
        for (int i = 0; i < items.length; i++) {
            MenuItem item = new MenuItem(menu, SWT.CHECK);
            item.setText(items[i]);
            item.setSelection(true);
            final int j = i;
            item.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    MenuItem item = (MenuItem) e.widget;
                    genTableEditor2.updateHeader(ids[j], items[j], !item.getSelection());

                }
            });
        }
        return menu;
    }

    /*
     * (non-Java)
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
                // refreshPreview();
            }

        };
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView#createRemovePushButton()
     */
    protected RemovePushButton createRemovePushButton() {
        RemovePushButtonForExtendedTable removeButton2 = new RemovePushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected void afterCommandExecution(Command executedCommand) {
                super.afterCommandExecution(executedCommand);
                // refreshPreview();
            }
        };
        return removeButton2;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastButton()
     */
    @Override
    public PastePushButton createPastePushButton() {
        PastePushButton pastePushButton2 = super.createPastePushButton();
        return pastePushButton2;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createExportPushButton()
     */
    @Override
    protected ExportPushButton createExportPushButton() {
        // ExportPushButton exportPushButton2 = super.createExportPushButton();
        return null;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastButton()
     */
    @Override
    public ImportPushButton createImportPushButton() {
        // ImportPushButton importPushButton2 = super.createImportPushButton();
        return null;
    }

    /**
     * qzhang Comment method "refreshPreview".
     */
    protected void refreshPreview() {
        MetadataTableEditorExt editorExt = (MetadataTableEditorExt) this.getExtendedTableViewer().getExtendedControlModel();
        editorExt.refreshPreview(editorExt.getMetadataColumnList());
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView#createMoveUpPushButton()
     */
    @Override
    protected MoveUpPushButton createMoveUpPushButton() {
        return null;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView#createMoveDownPushButton()
     */
    @Override
    protected MoveDownPushButton createMoveDownPushButton() {
        return null;
    }
}
