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
package org.talend.repository.ui.dialog;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.core.util.Util;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.progress.ProgressMonitorJobsDialog;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.composite.ThreeCompositesSashForm;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.ui.views.CheckboxRepositoryTreeViewer;
import org.talend.repository.ui.views.RepositoryCheckBoxView;
import org.talend.repository.ui.views.RepositoryView;

/**
 * ggu class global comment. Detailled comment
 */
public class VersionManagementDialog extends Dialog {

    private static final String TITLE = Messages.getString("VersionManagementDialog.Title"); //$NON-NLS-1$

    private static final IProxyRepositoryFactory FACTORY = CorePlugin.getDefault().getProxyRepositoryFactory();

    private static final String ITEM_EDITOR_KEY = "ITEM_EDITOR_KEY"; //$NON-NLS-1$

    private CheckboxRepositoryTreeViewer treeViewer;

    private Table itemTable;

    private Button removeBtn;

    private List<ItemVersionObject> checkedObjects = new ArrayList<ItemVersionObject>();

    public VersionManagementDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(TITLE);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        ThreeCompositesSashForm compositesSachForm = new ThreeCompositesSashForm(composite, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;
        composite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 400;
        gridData.widthHint = 500;
        composite.setLayoutData(gridData);

        //
        addRepositoryTreeViewer(compositesSachForm.getLeftComposite());

        addButtons(compositesSachForm.getMidComposite());

        addItemTableViewer(compositesSachForm.getRightComposite());

        return composite;
    }

    /**
     * repository tree viewer.
     */
    private void addRepositoryTreeViewer(Composite leftComposite) {
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 250;
        leftComposite.setLayoutData(gridData);
        RepositoryCheckBoxView view = new RepositoryCheckBoxView();
        try {
            view.init(RepositoryView.show().getViewSite());
        } catch (PartInitException e) {
            ExceptionHandler.process(e);
        }
        view.createPartControl(leftComposite);
        treeViewer = (CheckboxRepositoryTreeViewer) view.getViewer();
        // filter
        treeViewer.addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                RepositoryNode node = (RepositoryNode) element;
                return filterRepositoryNode(node);
            }
        });
        // event
        treeViewer.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                RepositoryNode node = (RepositoryNode) event.getElement();
                List<ItemVersionObject> objects = new ArrayList<ItemVersionObject>();
                processItems(objects, node);
                if (!objects.isEmpty()) {
                    if (event.getChecked()) {
                        checkedObjects.addAll(objects);
                        addItemElements(objects);
                    } else {
                        checkedObjects.removeAll(objects);
                        removeItemElements(objects);
                    }
                    checkButtonsState();
                }
            }
        });
        treeViewer.addTreeListener(new ITreeViewerListener() {

            public void treeCollapsed(TreeExpansionEvent event) {
                //
            }

            public void treeExpanded(TreeExpansionEvent event) {
                refreshCheckedTreeView();
            }
        });

        expandSomeNodes(view);
    }

    private void expandSomeNodes(RepositoryCheckBoxView view) {
        if (view == null) {
            return;
        }
        final RepositoryNode root = view.getRoot();
        if (root instanceof IProjectRepositoryNode) {
            final IProjectRepositoryNode rootNode = (IProjectRepositoryNode) root;
            final TreeViewer viewer = view.getViewer();
            // metadata
            RepositoryNode metadataConNode = rootNode.getMetadataNode();
            if (metadataConNode != null) {
                viewer.expandToLevel(metadataConNode, 1);
            }
            // code
            RepositoryNode codeNode = rootNode.getCodeNode();
            if (codeNode != null) {
                viewer.expandToLevel(codeNode, 1);
            }
        }
    }

    private boolean filterRepositoryNode(RepositoryNode node) {
        if (node == null) {
            return false;
        }
        if (node.isBin()) {
            return false;
        }
        if (node.getObject() != null) {
            ERepositoryStatus status = FACTORY.getStatus(node.getObject());
            if (status == ERepositoryStatus.LOCK_BY_OTHER || status == ERepositoryStatus.LOCK_BY_USER) {
                return false;
            }
            // table
            if (node.getObject() instanceof org.talend.core.model.metadata.MetadataTable) {
                return false;
            }
            // opened items
            // FIXME

            ERepositoryObjectType objectType = node.getObjectType();
            switch (objectType) {
            // case SQLPATTERNS:
            case ROUTINES:
                RepositoryNode systemNode = node.getParent();
                if (systemNode != null) {
                    // for system folder
                    if (systemNode.getType() == ENodeType.STABLE_SYSTEM_FOLDER
                            && systemNode.getLabel().equalsIgnoreCase(RepositoryConstants.SYSTEM_DIRECTORY)) {
                        return false;
                    }
                }
            default:
            }
        }
        ERepositoryObjectType contentType = node.getContentType();
        if (contentType != null) {
            switch (contentType) {
            case REFERENCED_PROJECTS: // referenced project.
            case GENERATED: // generated documentation
            case SQLPATTERNS: // sql pattern
                return false;
                // case SQLPATTERNS:
            case ROUTINES:

                // for system folder
                if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER
                        && node.getLabel().equalsIgnoreCase(RepositoryConstants.SYSTEM_DIRECTORY)) {
                    return false;
                }
            default:
            }
        }

        // for sub folder in db connection
        if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER) {
            RepositoryNode parentNode = node.getParent();
            if (parentNode != null) {
                if (parentNode.getObjectType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
                    return false;
                }
            }
        }
        return true;
    }

    private void processItems(List<ItemVersionObject> objects, RepositoryNode node) {
        if (node == null) {
            return;
        }
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            if (node.getObject() != null) {
                Property property = node.getObject().getProperty();
                Item item = property.getItem();
                if (item != null && filterRepositoryNode(node)) { // must be item
                    ItemVersionObject object = new ItemVersionObject(node, property.getVersion());
                    objects.add(object);
                }
            }
        } else {
            for (RepositoryNode child : node.getChildren()) {
                processItems(objects, child);
            }
        }
    }

    private void addButtons(Composite middleComposite) {
        Composite btnComposite = new Composite(middleComposite, SWT.NONE);
        btnComposite.setLayout(new GridLayout(1, true));
        GridData data = new GridData(GridData.FILL_BOTH);
        data.verticalAlignment = GridData.CENTER;
        btnComposite.setLayoutData(data);

        removeBtn = new Button(btnComposite, SWT.NONE);
        removeBtn.setImage(ImageProvider.getImage(EImage.LEFT_ICON));
        removeBtn.setToolTipText("remove items");
        removeBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                TableItem[] selections = itemTable.getSelection();
                itemTable.setRedraw(false);
                for (TableItem item : selections) {
                    Object data = item.getData();
                    checkedObjects.remove(data);
                    removeTableItem(item);
                }
                itemTable.setRedraw(true);
                // some problem for table update. so disable it as follow
                refreshCheckedTreeView();
                checkButtonsState();
            }
        });

        removeBtn.setEnabled(false);
        // disable
        removeBtn.setVisible(false);
    }

    private void addItemTableViewer(Composite rightComposite) {

        itemTable = new Table(rightComposite, SWT.MULTI | SWT.BORDER);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(itemTable);
        itemTable.setHeaderVisible(true);
        itemTable.setLinesVisible(true);

        //
        TableColumn itemColumn = new TableColumn(itemTable, SWT.CENTER);
        itemColumn.setText(Messages.getString("VersionManagementDialog.Items")); //$NON-NLS-1$
        itemColumn.setWidth(110);

        TableColumn versionColumn = new TableColumn(itemTable, SWT.CENTER);
        versionColumn.setText(Messages.getString("VersionManagementDialog.Version")); //$NON-NLS-1$
        versionColumn.setWidth(82);

        final TableColumn delColumn = new TableColumn(itemTable, SWT.CENTER);
        delColumn.setText(""); //$NON-NLS-1$
        delColumn.setWidth(26);
        delColumn.setResizable(false);

        versionColumn.addControlListener(new ControlListener() {

            public void controlMoved(ControlEvent e) {
                //
            }

            public void controlResized(ControlEvent e) {
                removeItemElements(checkedObjects);
                addItemElements(checkedObjects);
            }
        });

        itemTable.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkButtonsState();
            }

        });
    }

    private void checkButtonsState() {
        TableItem[] selections = itemTable.getSelection();
        if (selections != null && selections.length > 0) {
            removeBtn.setEnabled(true);
        } else {
            removeBtn.setEnabled(false);
        }
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

    private void removeItemElements(List<ItemVersionObject> objects) {
        itemTable.setRedraw(false);
        TableItem[] items = itemTable.getItems();
        for (TableItem item : items) {
            if (objects.contains(item.getData())) {
                removeTableItem(item);
            }
        }
        itemTable.setRedraw(true);
    }

    private void refreshCheckedTreeView() {
        List<RepositoryNode> nodes = new ArrayList<RepositoryNode>();

        for (TableItem item : itemTable.getItems()) {
            Object data = item.getData();
            if (data instanceof ItemVersionObject) {
                nodes.add(((ItemVersionObject) data).getRepositoryNode());
            }
        }
        treeViewer.setCheckedElements(nodes.toArray());
        // treeViewer.refresh();
    }

    private void addItemElements(List<ItemVersionObject> elements) {
        itemTable.setRedraw(false);
        if (elements == null || elements.isEmpty()) {
            return;
        }
        for (final ItemVersionObject object : elements) {
            final TableItem tableItem = new TableItem(itemTable, SWT.NONE);
            tableItem.setData(object);
            Item item = object.getItem();

            ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
            tableItem.setImage(ImageProvider.getImage(CoreImageProvider.getIcon(itemType)));
            tableItem.setText(item.getProperty().getLabel());

            // version
            TableEditor versionEditor = new TableEditor(itemTable);
            Composite versionComposit = new Composite(itemTable, SWT.NONE);
            GridLayout layout = new GridLayout(3, false);
            layout.horizontalSpacing = 1;
            layout.verticalSpacing = 0;
            layout.marginHeight = 0;
            layout.marginWidth = 0;
            versionComposit.setLayout(layout);

            final Text text = new Text(versionComposit, SWT.BORDER | SWT.READ_ONLY);
            GridData data = new GridData(GridData.FILL_HORIZONTAL);
            text.setLayoutData(data);
            text.setEditable(false);
            text.setText(object.getOldVersion());
            text.setForeground(new Color(null, 0, 0, 255));

            Button majorBtn = new Button(versionComposit, SWT.NONE);
            majorBtn.setText("M"); //$NON-NLS-1$
            Button minorBtn = new Button(versionComposit, SWT.NONE);
            minorBtn.setText("m"); //$NON-NLS-1$

            majorBtn.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    String version = object.getNewVersion();
                    version = VersionUtils.upMajor(version);
                    text.setText(version);
                    object.setNewVersion(version);
                }
            });
            minorBtn.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    String version = object.getNewVersion();
                    version = VersionUtils.upMinor(version);
                    text.setText(version);
                    object.setNewVersion(version);
                }
            });

            versionEditor.minimumWidth = itemTable.getColumn(1).getWidth();
            versionEditor.setEditor(versionComposit, tableItem, 1);

            TableEditor delEditor = new TableEditor(itemTable);
            Label delLabel = new Label(itemTable, SWT.CENTER);
            delLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
            delLabel.setImage(ImageProvider.getImage(EImage.DELETE_ICON));
            delLabel.setToolTipText(Messages.getString("VersionManagementDialog.DeletedTip")); //$NON-NLS-1$
            delLabel.pack();
            delLabel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(MouseEvent e) {
                    checkedObjects.remove(object);
                    removeTableItem(tableItem);
                    refreshCheckedTreeView();
                    checkButtonsState();
                }

            });

            delEditor.minimumWidth = 25;
            delEditor.horizontalAlignment = SWT.CENTER;
            delEditor.setEditor(delLabel, tableItem, 2);

            tableItem.setData(ITEM_EDITOR_KEY, new TableEditor[] { versionEditor, delEditor });
            itemTable.setRedraw(true);
        }
    }

    /**
     * 
     */
    class ItemVersionObject {

        private RepositoryNode node;

        private String oldVersion;

        private String newVersion;

        public ItemVersionObject(RepositoryNode node, String oldVersion) {
            super();
            this.node = node;
            this.oldVersion = oldVersion;
            this.newVersion = oldVersion; // init
        }

        public String getNewVersion() {
            return this.newVersion;
        }

        public void setNewVersion(String newVersion) {
            this.newVersion = newVersion;
        }

        public Item getItem() {
            return this.node.getObject().getProperty().getItem();
        }

        public RepositoryNode getRepositoryNode() {
            return this.node;
        }

        public String getOldVersion() {
            return this.oldVersion;
        }

        @SuppressWarnings("restriction")
        @Override
        public int hashCode() {
            return Util.combineHashCodes(getRepositoryNode().getId().hashCode(), getOldVersion().hashCode());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof ItemVersionObject) {
                ItemVersionObject tObj = (ItemVersionObject) obj;
                if (tObj.getRepositoryNode() == getRepositoryNode() && tObj.getOldVersion().equals(getOldVersion())) {
                    return true;
                }
            }
            return false;
        }

    }

    public List<ItemVersionObject> getModifiedVersionItems() {
        return this.checkedObjects;
    }

    @SuppressWarnings("restriction")
    @Override
    protected void okPressed() {
        boolean modified = false;
        for (ItemVersionObject object : getModifiedVersionItems()) {
            if (!object.getOldVersion().equals(object.getNewVersion())) {
                modified = true;
                break;
            }
        }
        if (!modified) {
            if (!getModifiedVersionItems().isEmpty()) {
                MessageDialog.openWarning(getShell(), Messages.getString("VersionManagementDialog.WarningTitle"), //$NON-NLS-1$
                        Messages.getString("VersionManagementDialog.WarningMessages")); //$NON-NLS-1$
            }
            super.okPressed();
            return;
        }
        ItemsVersionConfirmDialog chanedDialog = new ItemsVersionConfirmDialog(getShell(), getModifiedVersionItems());
        if (chanedDialog.open() == Window.OK) {
            IRunnableWithProgress runnable = new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask("", getModifiedVersionItems().size() * 100); //$NON-NLS-1$
                    for (ItemVersionObject object : getModifiedVersionItems()) {
                        if (!object.getOldVersion().equals(object.getNewVersion())) {
                            final Item item = object.getItem();
                            item.getProperty().setVersion(object.getNewVersion());

                            try {
                                FACTORY.save(item);
                            } catch (PersistenceException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                        monitor.worked(100);
                    }
                    RepositoryView.show().refresh();
                    monitor.done();
                }
            };

            final ProgressMonitorJobsDialog dialog = new ProgressMonitorJobsDialog(null);
            try {
                dialog.run(false, false, runnable);
            } catch (InvocationTargetException e) {
                ExceptionHandler.process(e);
            } catch (InterruptedException e) {
                ExceptionHandler.process(e);
            }
            super.okPressed();
        }
    }

}
