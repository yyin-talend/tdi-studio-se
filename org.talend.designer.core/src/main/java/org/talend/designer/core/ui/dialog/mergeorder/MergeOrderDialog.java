// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.dialog.mergeorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.process.EConnectionType;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class MergeOrderDialog extends Dialog {

    private Node mergeNode;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setSize(new Point(300, 400));
        newShell.setText(mergeNode.getUniqueName() + Messages.getString("MergeOrderDialog.ModifyMergeOrder")); //$NON-NLS-1$
    }

    private List<Connection> connectionList;

    /**
     * DOC nrousseau MergeOrderDialog constructor comment.
     * 
     * @param parentShell
     */
    public MergeOrderDialog(Shell parentShell, Node mergeNode) {
        super(parentShell);
        this.mergeNode = mergeNode;
        List<Connection> currentList = (List<Connection>) mergeNode.getIncomingConnections();
        this.connectionList = new ArrayList<Connection>(currentList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        Composite tableComposite = new Composite(composite, SWT.None);
        tableComposite.setLayout(new GridLayout());
        GridData gridData = new GridData(GridData.FILL_BOTH);
        // gridData.minimumWidth = minimumWidth;
        // gridData.minimumHeight = minimumHeight;
        tableComposite.setLayoutData(gridData);

        final TableViewerCreator tableViewerCreator = new TableViewerCreator(tableComposite);
        tableViewerCreator.setBorderVisible(true);
        tableViewerCreator.setCheckboxInFirstColumn(false);
        tableViewerCreator.setColumnsResizableByDefault(true);
        tableViewerCreator.setColumnsSortableByDefault(true);
        tableViewerCreator.setLayoutMode(LAYOUT_MODE.FILL_HORIZONTAL);

        Table table = tableViewerCreator.createTable();
        table.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MergeOrderDialog.Order")); //$NON-NLS-1$
        column.setModifiable(true);
        column.setWidth(50);
        column.setToolTipHeader(Messages.getString("MergeOrderDialog.CurrentOrderConnection")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Connection, String>() {

            public String get(Connection bean) {
                return String.valueOf(connectionList.indexOf(bean) + 1);
            }

            public void set(Connection bean, String value) {
                // bean.setName(value);
            }
        });
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Connection Name"); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Connection, String>() {

            public String get(Connection bean) {
                return bean.getUniqueName();
            }

            public void set(Connection bean, String value) {
                // bean.setName(value);
            }
        });
        column.setModifiable(false);
        column.setWidth(200);

        tableViewerCreator.init(connectionList);
        Composite buttonComposite = new Composite(composite, SWT.None);
        buttonComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

        Button moveUp = new Button(buttonComposite, SWT.PUSH);
        moveUp.setToolTipText(Messages.getString("MergeOrderDialog.MoveUp")); //$NON-NLS-1$
        moveUp.setImage(ImageProvider.getImage(EImage.UP_ICON));

        moveUp.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                IStructuredSelection selection = (IStructuredSelection) tableViewerCreator.getTableViewer()
                        .getSelection();
                Connection connection = (Connection) selection.getFirstElement();
                int connId = connectionList.indexOf(connection);
                if (connId > 0) {
                    Collections.swap(connectionList, connId - 1, connId);
                    tableViewerCreator.getTableViewer().refresh();
                }
            }

        });

        Button moveDown = new Button(buttonComposite, SWT.PUSH);
        moveDown.setToolTipText(Messages.getString("MergeOrderDialog.MoveDown")); //$NON-NLS-1$
        moveDown.setImage(ImageProvider.getImage(EImage.DOWN_ICON));

        final int nbConn = mergeNode.getIncomingConnections(EConnectionType.FLOW_MERGE).size();
        moveDown.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                IStructuredSelection selection = (IStructuredSelection) tableViewerCreator.getTableViewer()
                        .getSelection();
                Connection connection = (Connection) selection.getFirstElement();
                int connId = connectionList.indexOf(connection);
                if (connId < (nbConn - 1)) {
                    Collections.swap(connectionList, connId + 1, connId);
                    tableViewerCreator.getTableViewer().refresh();
                }
            }

        });
        return composite;
    }

    /**
     * Getter for connectionList.
     * 
     * @return the connectionList
     */
    public List<Connection> getConnectionList() {
        return connectionList;
    }

}
