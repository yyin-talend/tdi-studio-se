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
package org.talend.repository.ui.login.connections;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractExtendedToolbar;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.selection.ILineSelectionListener;
import org.talend.commons.ui.swt.tableviewer.selection.LineSelectionEvent;
import org.talend.commons.ui.swt.tableviewer.tableeditor.CheckboxTableEditorContent;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.CorePlugin;
import org.talend.core.model.general.ConnectionBean;
import org.talend.core.prefs.PreferenceManipulator;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ConnectionsListComposite extends Composite {

    private FormToolkit toolkit;

    private AbstractDataTableEditorView<ConnectionBean> table;

    private ConnectionFormComposite connectionsFormComposite;

    private List<ConnectionBean> list;

    private ExtendedTableModel<ConnectionBean> model;

    /**
     * DOC smallet ConnectionsComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ConnectionsListComposite(Composite parent, int style) {
        super(parent, style);

        PreferenceManipulator prefManipulator = new PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
        this.list = prefManipulator.readConnections();

        if (list.isEmpty()) {
            list.add(ConnectionBean.getDefaultConnectionBean());
        }

        this.model = new ExtendedTableModel<ConnectionBean>(null, list);

        toolkit = new FormToolkit(this.getDisplay());
        Form form = toolkit.createForm(this);
        Composite formBody = form.getBody();

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        form.setLayoutData(new GridData(GridData.FILL_BOTH));

        FormLayout formLayout = new FormLayout();
        formBody.setLayout(formLayout);

        Group group = new Group(formBody, SWT.NONE);
        group.setText("Connections");
        group.setBackground(new Color(null, 255, 255, 255));
        FormData data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.HSPACE);
        data.right = new FormAttachment(100, -ConnectionsDialog.HSPACE);
        data.top = new FormAttachment(0, ConnectionsDialog.VSPACE);
        data.bottom = new FormAttachment(100, -ConnectionsDialog.VSPACE);
        group.setLayoutData(data);

        group.setLayout(new FillLayout());

        table = new AbstractDataTableEditorView<ConnectionBean>(group, SWT.NONE, model, false, true, false) {

            @Override
            protected void setTableViewerCreatorOptions(TableViewerCreator<ConnectionBean> newTableViewerCreator) {
                super.setTableViewerCreatorOptions(newTableViewerCreator);
                newTableViewerCreator.setHeaderVisible(false);
                newTableViewerCreator.setVerticalScroll(true);
            }

            @Override
            protected AbstractExtendedToolbar initToolBar() {
                return new ConnectionsListButtonsToolBar(getMainComposite(), SWT.NONE, this.getExtendedTableViewer());
            }

            @Override
            protected void createColumns(TableViewerCreator<ConnectionBean> tableViewerCreator, Table table) {
                TableViewerCreatorColumn nameColumn = new TableViewerCreatorColumn(tableViewerCreator);
                nameColumn.setTitle("Name");
                nameColumn.setBeanPropertyAccessors(new IBeanPropertyAccessors<ConnectionBean, String>() {

                    public String get(ConnectionBean bean) {
                        return bean.getName();
                    }

                    public void set(ConnectionBean bean, String value) {
                        bean.setName(value);
                    }

                });
                nameColumn.setWeight(20);
                nameColumn.setModifiable(!isReadOnly());
                nameColumn.setMinimumWidth(20);
                nameColumn.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));

                TableViewerCreatorColumn comleteColumn = new TableViewerCreatorColumn(tableViewerCreator);
                comleteColumn.setTitle("Complete");
                comleteColumn.setBeanPropertyAccessors(new IBeanPropertyAccessors<ConnectionBean, Boolean>() {

                    public Boolean get(ConnectionBean bean) {
                        return bean.isComplete();
                    }

                    public void set(ConnectionBean bean, Boolean value) {
                        bean.setComplete(value);
                    }

                });
                comleteColumn.setWeight(5);
                comleteColumn.setModifiable(false);
                comleteColumn.setMinimumWidth(5);
                comleteColumn.setDisplayedValue("");
                CheckboxTableEditorContent checkboxTableEditorContent = new CheckboxTableEditorContent(true);
                comleteColumn.setTableEditorContent(checkboxTableEditorContent);
            }

        };

        addListeners();
    }

    private void addListeners() {
    }

    public ConnectionFormComposite getConnectionsFormComposite() {
        return this.connectionsFormComposite;
    }

    public void setConnectionsFormComposite(ConnectionFormComposite connectionsFormComposite) {
        this.connectionsFormComposite = connectionsFormComposite;

        table.getTableViewerCreator().getSelectionHelper().addAfterSelectionListener(new ILineSelectionListener() {

            public void handle(LineSelectionEvent e) {
                ISelection sel = e.source.getTableViewer().getSelection();
                IStructuredSelection sel2 = (IStructuredSelection) sel;
                ConnectionBean selected = (ConnectionBean) sel2.getFirstElement();
                setSelectedConnection(selected);
            }

        });

        if (!list.isEmpty()) {
            setSelectedConnection(list.get(0));
        }
    }

    private void setSelectedConnection(ConnectionBean selected) {
        connectionsFormComposite.setConnection(selected);
    }

    public void refresh(ConnectionBean toRefresh) {
        table.getTableViewerCreator().getTableViewer().refresh(toRefresh);
        table.getTableViewerCreator().refreshTableEditorControls();
    }

    public List<ConnectionBean> getList() {
        return this.list;
    }

}
