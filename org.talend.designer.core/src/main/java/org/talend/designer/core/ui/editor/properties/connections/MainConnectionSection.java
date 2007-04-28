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
package org.talend.designer.core.ui.editor.properties.connections;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.editor.MetadataTableEditor;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.ui.metadata.dialog.CustomTableManager;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController;

/**
 * Main Section of the property for the connections. <br/>
 * 
 * $Id$
 * 
 */
public class MainConnectionSection extends DynamicTabbedPropertySection {

    private MetadataTableEditorView metadataTableEditorView;

    private MetadataTableEditor metadataTableEditor;

    public MainConnectionSection() {
        super(EComponentCategory.MAIN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection#refresh()
     */
    @Override
    public void refresh() {
        if (conIf()) {
            super.refresh();
            return;
        }

        if (conSchema()) {
            IMetadataTable outputMetaTable = ((Connection) elem).getMetadataTable();
            metadataTableEditor.setMetadataTable(outputMetaTable);
            metadataTableEditorView.setMetadataTableEditor(metadataTableEditor);
            CustomTableManager.addCustomManagementToTable(metadataTableEditorView.getTableViewerCreator(), true);
            metadataTableEditorView.getTableViewerCreator().getTableViewer().refresh();

            composite.getParent().layout();
        }
    }

    @Override
    public void addComponents() {

        if (conSchema()) {
            disposeChildren();
            curRowSize = 0;

            List<? extends IElementParameter> listParam = ((Connection) elem).getSource().getElementParameters();

            for (IElementParameter cur : listParam) {
                if (cur.getField() == EParameterFieldType.SCHEMA_TYPE) {
                    generator.initController(this);
                    AbstractElementPropertySectionController contorller = generator.getController(
                            EParameterFieldType.SCHEMA_TYPE, this);
                    contorller.createControl(composite, cur, 0, 0, 0, null);
                }
            }

            Connection connection = (Connection) elem;
            if (connection.getLineStyle().equals(EConnectionType.FLOW_MERGE)) {
                addChangeOrder();
            }

            FormData data = new FormData();
            data.left = new FormAttachment(0, ITabbedPropertyConstants.HSPACE);
            data.right = new FormAttachment(100, -ITabbedPropertyConstants.HSPACE);
            data.top = new FormAttachment(0, curRowSize + ITabbedPropertyConstants.VSPACE);
            data.width = 300; // to correct bug of table growing indefinitly

            IMetadataTable outputMetaTable = ((Connection) elem).getMetadataTable();
            metadataTableEditor = new MetadataTableEditor(outputMetaTable, "Schema from "
                    + ((Connection) elem).getSource().getElementName() + " output ");
            metadataTableEditorView = new MetadataTableEditorView(composite, SWT.NONE, metadataTableEditor, true, false);

            Composite compositeEditorView = metadataTableEditorView.getMainComposite();
            compositeEditorView.setLayoutData(data);

            // composite.getParent().layout();
            composite.layout();
        } else if (conIf()) {
            super.addComponents();
        } else {
            disposeChildren();
        }
    }

    private void disposeChildren() {
        // Empty the composite before use (kinda refresh) :
        Control[] ct = composite.getChildren();
        for (int i = 0; i < ct.length; i++) {
            ct[i].dispose();
        }
    }

    private boolean conIf() {
        Connection connection = (Connection) elem;
        return connection.getLineStyle() == EConnectionType.RUN_IF;
    }

    private boolean conSchema() {
        Connection connection = (Connection) elem;
        return connection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA);
    }

    CLabel currentOrderLabel;

    private void addChangeOrder() {
        final Connection connection = (Connection) elem;
        CLabel labelLabel = getWidgetFactory().createCLabel(composite, "Merge order");
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.top = new FormAttachment(0, curRowSize);
        labelLabel.setLayoutData(data);
        currentOrderLabel = getWidgetFactory().createCLabel(composite, "");

        Button btnUp;
        Point btnSize;
        btnUp = getWidgetFactory().createButton(composite, "Lower", SWT.PUSH);
        btnSize = btnUp.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        btnUp.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                connectionOrderUp((List<IConnection>) connection.getTarget().getIncomingConnections(), connection);
            }

        });

        data = new FormData();
        data.left = new FormAttachment(labelLabel, 0);
        data.top = new FormAttachment(0, curRowSize);
        btnUp.setLayoutData(data);

        Button btnDown;
        btnDown = getWidgetFactory().createButton(composite, "Higher", SWT.PUSH);
        btnSize = btnDown.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        btnDown.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                connectionOrderDown((List<IConnection>) connection.getTarget().getIncomingConnections(), connection);
            }

        });

        data = new FormData();
        data.left = new FormAttachment(btnUp, 0);
        data.top = new FormAttachment(0, curRowSize);
        btnDown.setLayoutData(data);

        data = new FormData();
        data.left = new FormAttachment(btnDown, 0);
        data.top = new FormAttachment(0, curRowSize);
        currentOrderLabel.setLayoutData(data);
        updateCurrentOrder();

        curRowSize += btnSize.y;
    }

    private void connectionOrderUp(List<IConnection> connectionList, Connection connection) {
        int order = connection.getInputId() - 1;
        if (order <= 0) {
            return;
        }
        IConnection connectionToSwitch = connectionList.get(order - 1);
        connectionList.set(order, connectionToSwitch);
        connectionList.set(order - 1, connection);
        connection.updateAllId();
        updateCurrentOrder();
    }

    private void connectionOrderDown(List<IConnection> connectionList, Connection connection) {
        int order = connection.getInputId();
        if (order >= connectionList.size()) {
            return;
        }
        order--;
        IConnection connectionToSwitch = connectionList.get(order + 1);
        connectionList.set(order, connectionToSwitch);
        connectionList.set(order + 1, connection);
        connection.updateAllId();
        updateCurrentOrder();
    }

    private void updateCurrentOrder() {
        currentOrderLabel.setText("CurrentOrder: " + ((Connection) elem).getInputId());
    }

}
