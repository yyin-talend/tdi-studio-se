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
package org.talend.designer.core.ui.editor.properties.controllers;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.editor.properties.macrowidgets.tableeditor.PropertiesTableEditorModel;
import org.talend.designer.core.ui.editor.properties.macrowidgets.tableeditor.PropertiesTableEditorView;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: TableController.java 1 2006-12-14 下午05:44:30 +0000 (下午05:44:30) yzhang $
 * 
 */
public class TableController extends AbstractElementPropertySectionController {

    /**
     * 
     */
    private static final int MIN_NUMBER_ROWS = 8;

    /**
     * DOC yzhang TableController constructor comment.
     * 
     * @param dtp
     */
    public TableController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createCommand()
     */
    @Override
    public Command createCommand() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#
     * createControl(org.eclipse.swt.widgets.Composite, org.talend.core.model.process.IElementParameter, int, int, int,
     * org.eclipse.swt.widgets.Control)
     */
    @Override
    public Control createControl(final Composite parentComposite, final IElementParameter param, final int numInRow,
            final int nbInRow, int top, final Control lastControlPrm) {

        final Composite container = parentComposite;

        PropertiesTableEditorModel<Map<String, Object>> tableEditorModel = new PropertiesTableEditorModel<Map<String, Object>>();

        dynamicTabbedPropertySection.updateColumnList(null);
        tableEditorModel.setData(elem, param, part.getTalendEditor().getProcess());
        PropertiesTableEditorView<Map<String, Object>> tableEditorView = new PropertiesTableEditorView<Map<String, Object>>(
                parentComposite, SWT.NONE, tableEditorModel, !param.isBasedOnSchema(), false);
        tableEditorView.getExtendedTableViewer().setCommandStack(getCommandStack());
        tableEditorView.setReadOnly(param.isReadOnly());

        final Table table = tableEditorView.getTable();

        table.setToolTipText(VARIABLE_TOOLTIP + param.getVariableName());

        // add listener to tableMetadata (listen the event of the toolbars)
        tableEditorView.getExtendedTableModel().addAfterOperationListListener(new IListenableListListener() {

            public void handleEvent(ListenableListEvent event) {
                Node node = (Node) elem;
                node.checkAndRefreshNode();
            }
        });
        final Composite mainComposite = tableEditorView.getMainComposite();

        CLabel labelLabel2 = getWidgetFactory().createCLabel(container, param.getDisplayName());
        FormData formData = new FormData();
        if (lastControlPrm != null) {
            formData.left = new FormAttachment(lastControlPrm, 0);
        } else {
            formData.left = new FormAttachment((((numInRow - 1) * MAX_PERCENT) / nbInRow), 0);
        }
        formData.top = new FormAttachment(0, top);
        labelLabel2.setLayoutData(formData);
        if (numInRow != 1) {
            labelLabel2.setAlignment(SWT.RIGHT);
        }
        // *********************
        formData = new FormData();
        int currentLabelWidth2 = STANDARD_LABEL_WIDTH;
        GC gc2 = new GC(labelLabel2);
        Point labelSize2 = gc2.stringExtent(param.getDisplayName());
        gc2.dispose();

        if ((labelSize2.x + ITabbedPropertyConstants.HSPACE) > currentLabelWidth2) {
            currentLabelWidth2 = labelSize2.x + ITabbedPropertyConstants.HSPACE;
        }

        int tableHorizontalOffset = -5;
        if (numInRow == 1) {
            if (lastControlPrm != null) {
                formData.left = new FormAttachment(lastControlPrm, currentLabelWidth2 + tableHorizontalOffset);
            } else {
                formData.left = new FormAttachment(0, currentLabelWidth2 + tableHorizontalOffset);
            }
        } else {
            formData.left = new FormAttachment(labelLabel2, 0 + tableHorizontalOffset, SWT.RIGHT);
        }
        formData.right = new FormAttachment((numInRow * MAX_PERCENT) / nbInRow, 0);
        formData.top = new FormAttachment(0, top);

        int currentHeightEditor = table.getHeaderHeight() + ((List) param.getValue()).size() * table.getItemHeight()
                + table.getItemHeight() + 50;
        int minHeightEditor = table.getHeaderHeight() + MIN_NUMBER_ROWS * table.getItemHeight() + table.getItemHeight()
                + 50;
        int ySize2 = Math.max(currentHeightEditor, minHeightEditor);

        formData.bottom = new FormAttachment(0, top + ySize2);
        mainComposite.setLayoutData(formData);

        hashCurControls.put(param.getName(), tableEditorView.getExtendedTableViewer().getTableViewerCreator());

        this.dynamicTabbedPropertySection.setCurRowSize(ySize2 + ITabbedPropertyConstants.VSPACE);

        top += this.dynamicTabbedPropertySection.getCurRowSize();

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh(IElementParameter param, boolean check) {
        TableViewerCreator tableViewerCreator = (TableViewerCreator) hashCurControls.get(param.getName());
        Object value = param.getValue();
        if (value instanceof List) {
            dynamicTabbedPropertySection.updateColumnList(null);
            if (tableViewerCreator != null) {
                if (!tableViewerCreator.getInputList().equals(value)) {
                    tableViewerCreator.init((List) value);
                }
                tableViewerCreator.getTableViewer().refresh();
            }
        }
    }
}
