// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.dialog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * DOC wchen class global comment. Detailled comment
 */
public class PastSelectorDialog extends Dialog {

    private List<IRepositoryObject> versions;

    private Set<IRepositoryObject> selectedVersionItems = new HashSet<IRepositoryObject>();

    private Table table;

    private RepositoryNode sourceNode;

    private Button selectAll;

    private Button selectNone;

    /**
     * DOC talend PastSelectorDialog constructor comment.
     * 
     * @param parentShell
     */
    public PastSelectorDialog(Shell parentShell, List<IRepositoryObject> versions, RepositoryNode sourceNode) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
        this.versions = versions;
        this.sourceNode = sourceNode;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(sourceNode.getProperties(EProperties.LABEL) + " " + sourceNode.getObject().getVersion()
                + " - Selecte old versions to be past");
        newShell.setSize(600, 300);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        table = new Table(composite, SWT.NONE | SWT.CHECK);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn version = new TableColumn(table, SWT.NONE);
        version.setWidth(100);
        version.setText("Version");

        TableColumn cretationTime = new TableColumn(table, SWT.NONE);
        cretationTime.setWidth(200);
        cretationTime.setText("Creation Time");

        TableColumn modificationTime = new TableColumn(table, SWT.NONE);
        modificationTime.setWidth(200);
        modificationTime.setText("Modification Time");

        for (IRepositoryObject object : versions) {
            if (object.getVersion().equals(sourceNode.getObject().getVersion())) {
                continue;
            }
            TableItem item = new TableItem(table, SWT.NONE);
            item.setData(object);
            item.setText(0, object.getVersion());
            item.setText(1, object.getCreationDate().toString());
            if (object.getModificationDate() != null) {
                item.setText(2, object.getModificationDate().toString());
            }
        }

        Composite buttonContainer = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        buttonContainer.setLayout(layout);
        buttonContainer.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

        selectAll = new Button(buttonContainer, SWT.PUSH);
        selectAll.setText("Select All");
        selectNone = new Button(buttonContainer, SWT.PUSH);
        selectNone.setText("Select None");

        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.detail == SWT.CHECK) {
                    TableItem tableItem = (TableItem) e.item;
                    Property property = ((IRepositoryObject) tableItem.getData()).getProperty();
                    if (property != null && property.getItem() != null) {
                        if (tableItem.getChecked()) {
                            // selectedVersionItems.add(property.getItem());
                            selectedVersionItems.add((IRepositoryObject) tableItem.getData());
                        } else {
                            // selectedVersionItems.remove(property.getItem());
                            selectedVersionItems.remove(tableItem.getData());
                        }
                    }
                }
            }

        });
        selectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                for (int i = 0; i < table.getItemCount(); i++) {
                    table.getItem(i).setChecked(true);
                    IRepositoryObject data = (IRepositoryObject) table.getItem(i).getData();
                    selectedVersionItems.add(data);
                }
            }

        });

        selectNone.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                for (int i = 0; i < table.getItemCount(); i++) {
                    table.getItem(i).setChecked(false);
                    selectedVersionItems.remove(table.getItem(i).getData());
                }
            }

        });

        return composite;
    }

    public Set<IRepositoryObject> getSelectedVersionItems() {
        return this.selectedVersionItems;
    }

}
