// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.exchange.ui;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.core.model.process.EComponentCategory;
import org.talend.designer.components.exchange.i18n.Messages;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.ui.actions.DownloadComponenentsAction;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class DownloadedExtensionsComposite extends ExchangeComposite {

    private ISelection selection;

    private IWizardPage wizardPage = null;

    private Table itemTable;

    private static final String ITEM_EDITOR_KEY = "ITEM_EDITOR_KEY"; //$NON-NLS-1$

    Button operateStatusBtn;

    /**
     * DOC hcyi DownloadedExtensionsComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public DownloadedExtensionsComposite(Composite parent, int styles, TabbedPropertySheetWidgetFactory factory,
            List<ComponentExtension> fExtensions) {
        super(parent, styles, factory);
        FormLayout layout = new FormLayout();
        setLayout(layout);
        FormData thisFormData = new FormData();
        thisFormData.left = new FormAttachment(0, 0);
        thisFormData.right = new FormAttachment(100, 0);
        thisFormData.top = new FormAttachment(0, 0);
        thisFormData.bottom = new FormAttachment(100, 0);
        setLayoutData(thisFormData);

        itemTable = new Table(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        TableLayout tableLayout = new TableLayout();
        itemTable.setLayout(tableLayout);
        itemTable.setHeaderVisible(true);
        itemTable.setLinesVisible(true);

        //
        TableColumn itemColumn = new TableColumn(itemTable, SWT.CENTER);
        itemColumn.setText(Messages.getString("DownloadedExtensionsComposite.ExtensionName")); //$NON-NLS-1$
        itemColumn.setWidth(220);

        TableColumn versionColumn = new TableColumn(itemTable, SWT.CENTER);
        versionColumn.setText(Messages.getString("DownloadedExtensionsComposite.DownloadedVersion")); //$NON-NLS-1$
        versionColumn.setWidth(180);

        TableColumn dateColumn = new TableColumn(itemTable, SWT.CENTER);
        dateColumn.setText(Messages.getString("DownloadedExtensionsComposite.DownloadDate")); //$NON-NLS-1$
        dateColumn.setWidth(180);

        final TableColumn operateColumn = new TableColumn(itemTable, SWT.CENTER);
        operateColumn.setText(""); //$NON-NLS-1$
        operateColumn.setWidth(150);
        operateColumn.setResizable(false);

        Object layoutData = parent.getLayoutData();
        if (layoutData instanceof GridData) {
            GridData gridData = (GridData) layoutData;
            gridData.grabExcessVerticalSpace = true;
            gridData.verticalAlignment = SWT.FILL;
        }

        FormData formData = new FormData();
        formData.left = new FormAttachment(0);
        formData.top = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        formData.bottom = new FormAttachment(100);
        itemTable.setLayoutData(formData);

        operateColumn.addControlListener(new ControlListener() {

            public void controlMoved(ControlEvent e) {
                //
            }

            public void controlResized(ControlEvent e) {
            }
        });
        itemTable.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });

        //
        if (fExtensions != null && !fExtensions.isEmpty()) {
            updateInstalledExtensions(fExtensions);
        }
    }

    public void updateInstalledExtensions(List<ComponentExtension> extensions) {
        for (ComponentExtension ext : extensions) {
            fInstalledExtensions.put(ext.getIdExtension(), ext);
        }
        loadDownloadedExtensionsFromFile();
        removeItemElements(fInstalledExtensions);
        addItemElements(fInstalledExtensions);
        refresh();
    }

    private void addItemElements(Map<String, ComponentExtension> elements) {
        if (elements == null || elements.isEmpty()) {
            return;
        }
        itemTable.setRedraw(false);

        Iterator ite = elements.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, ComponentExtension> entry = (Entry<String, ComponentExtension>) ite.next();
            ComponentExtension object = entry.getValue();

            final TableItem tableItem = new TableItem(itemTable, SWT.NONE);
            tableItem.setData(object);

            // Extension Name
            tableItem.setText(0, object.getLabel());
            tableItem.setText(1, object.getDownloadedVersion());
            tableItem.setText(2, formatter.format(object.getDateDownload()));

            Composite operateComposit = new Composite(itemTable, SWT.CENTER);
            GridLayout layout = new GridLayout(3, false);
            layout.horizontalSpacing = 2;
            layout.verticalSpacing = 1;
            layout.marginHeight = 0;
            layout.marginWidth = 5;
            operateComposit.setLayout(layout);

            operateStatusBtn = new Button(operateComposit, SWT.CENTER);
            GridData data = new GridData(GridData.FILL_HORIZONTAL);
            data.horizontalAlignment = SWT.CENTER;
            operateStatusBtn.setLayoutData(data);
            operateStatusBtn.setText(getOperateStatus(object));
            operateStatusBtn.setToolTipText(Messages.getString("DownloadedExtensionsComposite.OperateStatus")); //$NON-NLS-1$
            operateStatusBtn.setData(tableItem);
            operateStatusBtn.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    //
                    if ((Button) event.widget != null && ((Button) event.widget).getData() != null
                            && ((Button) event.widget).getData() instanceof TableItem) {
                        TableItem tableItem = (TableItem) ((Button) event.widget).getData();
                        if (tableItem != null && tableItem.getData() != null && tableItem.getData() instanceof ComponentExtension) {
                            setSelectedExtension((ComponentExtension) tableItem.getData());
                            //
                            String operStatusText = ((Button) event.widget).getText();
                            if (operStatusText.trim().equals(
                                    Messages.getString("DownloadedExtensionsComposite.installOperateStatus"))
                                    || operStatusText.trim().equals(
                                            Messages.getString("DownloadedExtensionsComposite.updateOperateStatus"))) {
                                DownloadComponenentsAction downloadAction = new DownloadComponenentsAction();
                                if (downloadAction != null) {
                                    downloadAction.run();
                                }
                                operateStatusBtn.setEnabled(false);
                                itemTable.layout();
                                refresh();
                            } else if (operStatusText.trim().equals(
                                    Messages.getString("DownloadedExtensionsComposite.updateOperateStatus"))) {
                                DownloadComponenentsAction downloadAction = new DownloadComponenentsAction();
                                if (downloadAction != null) {
                                    downloadAction.run();
                                }
                                operateStatusBtn.setEnabled(false);
                                itemTable.layout();
                                refresh();
                            }
                        }
                    }
                }
            });
            TableEditor versionEditor = new TableEditor(itemTable);
            versionEditor.minimumWidth = itemTable.getColumn(3).getWidth();
            versionEditor.setEditor(operateComposit, tableItem, 3);
            tableItem.setData(ITEM_EDITOR_KEY, new TableEditor[] { versionEditor });
        }
        itemTable.setRedraw(true);
    }

    private String getOperateStatus(ComponentExtension object) {
        //
        if (object != null && fDownloadedExtensions.containsKey(object.getIdExtension())) {
            if (object.getVersionExtension().equals(object.getDownloadedVersion())) {
                operateStatusBtn.setEnabled(false);
                return Messages.getString("DownloadedExtensionsComposite.installedOperateStatus");
            } else {
                return "  " + Messages.getString("DownloadedExtensionsComposite.updateOperateStatus") + "  ";
            }
        }
        return "  " + Messages.getString("DownloadedExtensionsComposite.installOperateStatus") + "  ";
    }

    private void removeItemElements(Map<String, ComponentExtension> elements) {
        if (elements == null || elements.isEmpty()) {
            return;
        }
        itemTable.setRedraw(false);
        TableItem[] items = itemTable.getItems();
        for (TableItem item : items) {
            if (item.getData() != null && item.getData() instanceof ComponentExtension) {
                ComponentExtension itemObject = (ComponentExtension) item.getData();
                if (elements.containsKey(itemObject.getIdExtension())) {
                    removeTableItem(item);
                }
            }
        }
        itemTable.setRedraw(true);
    }

    private void removeTableItem(TableItem item) {
        if (item == null) {
            return;
        }
        TableEditor[] editors = (TableEditor[]) item.getData(ITEM_EDITOR_KEY);
        if (editors != null) {
            for (int j = 0; j < editors.length; j++) {
                editors[j].getEditor().dispose();
                editors[j].dispose();
            }
        }
        item.dispose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getSection()
     */
    public EComponentCategory getSection() {
        return EComponentCategory.DOWNLOADEDEXTENSIONS;
    }

    public ISelection getSelection() {
        return this.selection;
    }

    public IWizardPage getParentWizard() {
        return this.wizardPage;
    }

    public void setParentWizard(IWizardPage parentWizard) {
        this.wizardPage = parentWizard;
    }

}
