// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.exchange.ui.dialog;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.download.DownloadHelper;
import org.talend.designer.components.exchange.ExchangePlugin;
import org.talend.designer.components.exchange.i18n.Messages;
import org.talend.designer.components.exchange.jobs.ComponentSearcher;
import org.talend.designer.components.exchange.model.Category;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.model.VersionRevision;
import org.talend.designer.components.exchange.util.ExchangeUtils;
import org.talend.designer.components.exchange.util.ExchangeWebService;

/**
 * hcyi A dialog for user to select filters that will be used to search components.
 * 
 * This dialog will open when Browse Talend Exchange button push.
 */
public class ImportExchangeDialog extends Dialog {

    private static String category;

    private static String version;

    public Combo categoryCombo;

    public Combo versionCombo;

    public static final String TOS_VERSION_FILTER = "TOS_VERSION_FILTER"; //$NON-NLS-1$

    private List<Category> fCategorys = new ArrayList<Category>();

    private List<VersionRevision> fVersionRevisions = new ArrayList<VersionRevision>();

    private List<ComponentExtension> compatible;

    public ScrolledComposite scrolledCompositeFileViewer;

    private ImportExchangeProperty downloadproperty;

    private String selectFile;

    private String progressBarMessage;

    private URL url;

    private File tempFile;

    public Table table;

    protected ImportExchangeDialog(Shell shell) {
        super(shell);
        this.setShellStyle(this.getShellStyle() | SWT.MIN | SWT.MAX | SWT.RESIZE);
        // init
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                fCategorys.clear();
                fCategorys = ExchangeWebService.searchCategoryExtensionJSONArray(ExchangeUtils.TYPEEXTENSION);
                fVersionRevisions.clear();
                fVersionRevisions = ExchangeWebService.searchVersionRevisionJSONArray(ExchangeUtils.TYPEEXTENSION);
            }
        });
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ImportExchangeDialog.dialogTitle")); //$NON-NLS-1$
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite exchangeDialogCom = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.makeColumnsEqualWidth = false;
        layout.marginWidth = 0;
        exchangeDialogCom.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        exchangeDialogCom.setLayoutData(gridData);
        new ImportCompatibleEcoComponentsComposite(exchangeDialogCom, exchangeDialogCom.getShell(), false);
        return parent;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.FINISH_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected void okPressed() {
        IPath tempPath = new Path(System.getProperty("user.dir")).append("temp"); //$NON-NLS-1$ //$NON-NLS-2$
        File pathFile = tempPath.toFile();
        if (downloadproperty.getFileName() == null || downloadproperty.getFileName() == null) {
            MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING | SWT.OK);
            box.setText(Messages.getString("ImportExchangeDialog.WARNING")); //$NON-NLS-1$
            box.setMessage(Messages.getString("ImportExchangeDialog.NOTSELECTWARNING")); //$NON-NLS-1$
            box.open();
            return;
        }
        tempFile = new File(pathFile, downloadproperty.getFileName());
        try {
            url = new URL(downloadproperty.getDownloadUrl());
        } catch (MalformedURLException e1) {
            ExceptionHandler.process(e1);
        }
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()
                .getShell());
        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) {
                monitor.beginTask(Messages.getString("ImportExchangeDialog.downloadProgressBar"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                Display.getDefault().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            new DownloadHelper().download(url, tempFile);
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                });
                monitor.done();
            }
        };
        try {
            progressDialog.run(true, true, runnable);
        } catch (InvocationTargetException e1) {
            ExceptionHandler.process(e1);
        } catch (InterruptedException e1) {
            ExceptionHandler.process(e1);
        }

        selectFile = tempFile.toString();
        super.okPressed();
    }

    /**
     * 
     * DOC hcyi ImportCompatibleEcoComponentsComposite class global comment. Detailled comment
     */
    class ImportCompatibleEcoComponentsComposite extends Composite {

        private static final int HEIGHT = 350;

        public ImportCompatibleEcoComponentsComposite(Composite parent, Shell shell, boolean init) {
            super(parent, SWT.NONE);
            createControls(parent);
        }

        public void createControls(Composite parent) {
            setLayout(new GridLayout());
            setLayoutData(new GridData(GridData.FILL_BOTH));
            creatOptions(parent);
            Composite tableComposite = new Composite(parent, SWT.NONE);
            tableComposite.setLayout(new GridLayout());

            GridData layoutData = new GridData(GridData.FILL_BOTH);
            layoutData.heightHint = HEIGHT;
            layoutData.minimumHeight = HEIGHT;
            tableComposite.setLayoutData(layoutData);

            scrolledCompositeFileViewer = new ScrolledComposite(tableComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
            scrolledCompositeFileViewer.setExpandHorizontal(true);
            scrolledCompositeFileViewer.setExpandVertical(true);
            GridData gridData1 = new GridData(GridData.FILL_BOTH);
            gridData1.widthHint = 300;
            gridData1.heightHint = 325;
            gridData1.horizontalSpan = 2;
            scrolledCompositeFileViewer.setLayoutData(gridData1);
            createTable();
        }

        public void createTable() {
            // List Table
            table = new Table(scrolledCompositeFileViewer, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
            table.setLayoutData(new GridData(GridData.FILL_BOTH));
            table.setHeaderVisible(true);
            table.setLinesVisible(true);

            TableColumn tableName = new TableColumn(table, SWT.NONE);
            tableName.setText(Messages.getString("ImportExchangeDialog.EXTENSION_NAME")); //$NON-NLS-1$
            tableName.setWidth(250);

            TableColumn nbColumns = new TableColumn(table, SWT.NONE);
            nbColumns.setText(Messages.getString("ImportExchangeDialog.AUTHOR_NAME")); //$NON-NLS-1$
            nbColumns.setWidth(100);

            TableColumn lastRe = new TableColumn(table, SWT.NONE);
            lastRe.setText(Messages.getString("ImportExchangeDialog.LATEST_REVISION")); //$NON-NLS-1$
            lastRe.setWidth(100);

            TableColumn desc = new TableColumn(table, SWT.NONE);
            desc.setText(Messages.getString("ImportExchangeDialog.EXTENSION_DESCRIPTION")); //$NON-NLS-1$
            desc.setWidth(250);

            scrolledCompositeFileViewer.setContent(table);
            scrolledCompositeFileViewer.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));

            downloadproperty = new ImportExchangeProperty();
            table.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    TableItem[] itemList = table.getItems();
                    for (int i = 0; i < itemList.length; i++) {
                        if (table.isSelected(i)) {
                            downloadproperty.setDownloadUrl(compatible.get(i).getLinkDownload());
                            downloadproperty.setFileName(compatible.get(i).getFilename());
                        }
                    }
                }
            });
        }

        /**
         * 
         * There will create three radio button.
         */
        private void creatOptions(Composite exchangeDialogCom) {
            Group group = new Group(exchangeDialogCom, SWT.NONE);
            GridLayout layout = new GridLayout();
            layout.numColumns = 5;
            layout.makeColumnsEqualWidth = false;
            layout.marginWidth = 5;
            group.setLayout(layout);
            group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            // Category
            creatCategory(group);
            category = categoryCombo.getText();
            // Tos Version
            creatTosVersionFilter(group, true);
            version = versionCombo.getText();
            // Refresh
            Button refresh = new Button(group, SWT.PUSH);
            refresh.setImage(ImageProvider.getImage(EImage.REFRESH_ICON));
            refresh.setToolTipText(Messages.getString("ImportExchangeDialog.REFRESH_BUTTON"));//$NON-NLS-1$
            refresh.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    progressBarMessage = Messages.getString("ImportExchangeDialog.REFRESHING_PROGRESSBAR"); //$NON-NLS-1$
                    findChoiceExchange();
                }
            });
        }

    }

    public void creatCategory(Composite parent) {
        Composite categoryComposite = new Composite(parent, SWT.NONE);
        categoryComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        categoryComposite.setLayout(new GridLayout(2, false));
        Label categoryLabel = new Label(categoryComposite, SWT.NONE);
        categoryLabel.setText("Category");
        GridData gridData = new GridData(SWT.Resize);
        gridData.widthHint = 160;
        categoryCombo = new Combo(categoryComposite, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.RESIZE);
        categoryCombo.setLayoutData(gridData);
        initCategoryCombo();
        categoryCombo.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                progressBarMessage = Messages.getString("ImportExchangeDialog.download.extensions"); //$NON-NLS-1$
                findChoiceExchange();
            }
        });
    }

    public void initCategoryCombo() {
        if (categoryCombo.getItemCount() > 0) {
            return;
        }
        Iterator<Category> cListIterator = fCategorys.iterator();
        while (cListIterator.hasNext()) {
            Category category = cListIterator.next();
            // now hide the Category : Component ,Hadoop Configuration
            if ("Component".equals(category.getCategoryName()) || "Hadoop Configuration".equals(category.getCategoryName())) {
                cListIterator.remove();
                continue;
            }
            categoryCombo.add(category.getCategoryName());
        }
        categoryCombo.select(0);
        categoryCombo.pack();
    }

    public void creatTosVersionFilter(Composite parent, boolean isInitTosVersion) {
        Composite tosVersionFilterComposite = new Composite(parent, SWT.NONE);
        tosVersionFilterComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        tosVersionFilterComposite.setLayout(new GridLayout(2, false));
        Label versionFilterLable = new Label(tosVersionFilterComposite, SWT.NONE);
        versionFilterLable.setText("TOS_VERSION_FILTER");
        GridData gridData = new GridData(SWT.Resize);
        gridData.widthHint = 60;
        versionCombo = new Combo(tosVersionFilterComposite, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.RESIZE);
        versionCombo.setLayoutData(gridData);

        if (isInitTosVersion) {
            initTosVersionFilter();
        }
        versionCombo.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                Combo comboControl = (Combo) e.getSource();
                String value = comboControl.getText();
                IPreferenceStore preferenceStore = ExchangePlugin.getDefault().getPreferenceStore();
                preferenceStore.setValue("TOS_IMPORT_VERSION_FILTER", value);
                version = value;
                progressBarMessage = Messages.getString("ImportExchangeDialog.download.extensions"); //$NON-NLS-1$
                findChoiceExchange();
            }
        });
    }

    public void initTosVersionFilter() {
        if (versionCombo.getItemCount() > 0) {
            return;
        }
        String currentVersion = ExchangePlugin.getDefault().getPreferenceStore().getString(TOS_VERSION_FILTER);
        currentVersion = ExchangeUtils.getMainVersion(currentVersion);
        if (fVersionRevisions != null) {
            String versions[] = ExchangeUtils.getVersionList(fVersionRevisions);
            int stringIndex = 0;
            for (int i = 0; i < versions.length; i++) {
                versionCombo.add(versions[i]);
                if (versions[i].equals(currentVersion)) {
                    stringIndex = i;
                }
            }
            versionCombo.select(stringIndex);
            versionCombo.pack();
        }
    }

    /**
     * 
     * This is a progressBar.when this progressBar run it will download some jobs/templates/routines from net.
     */
    public void findChoiceExchange() {
        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()
                .getShell());
        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) {
                monitor.beginTask(progressBarMessage, IProgressMonitor.UNKNOWN);
                Display.getDefault().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        compatible = ComponentSearcher.getImportComponentExtensions(version, ExchangeUtils.getCurrentLanguage(),
                                categoryCombo.getSelectionIndex() >= 0 ? fCategorys.get(categoryCombo.getSelectionIndex())
                                        .getCategoryId() : "");
                        updateTable(compatible);
                    }
                });
                monitor.done();
                if (monitor.isCanceled()) {
                    try {
                        throw new InterruptedException(Messages.getString("ImportExchangeDialog.OPERATION_CANCELLED")); //$NON-NLS-1$
                    } catch (InterruptedException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        };
        try {
            progressDialog.run(true, true, runnable);
        } catch (InvocationTargetException e1) {
            ExceptionHandler.process(e1);
        } catch (InterruptedException e1) {
            ExceptionHandler.process(e1);
        }

    }

    public void updateTable(List<ComponentExtension> extensions) {
        removeItemElements(extensions);
        addItemElements(extensions);
    }

    public void addItemElements(List<ComponentExtension> extensions) {
        if (extensions == null || extensions.isEmpty()) {
            return;
        }
        table.setRedraw(false);
        for (final ComponentExtension object : extensions) {
            final TableItem tableItem = new TableItem(table, SWT.NONE);
            tableItem.setData(object);
            tableItem.setText(0, object.getLabel());
            tableItem.setText(1, object.getAuthor());
            tableItem.setText(2, object.getVersionExtension());
            tableItem.setText(3, object.getDescription());
        }
        table.setRedraw(true);
    }

    private void removeItemElements(List<ComponentExtension> objects) {
        table.setRedraw(false);
        TableItem[] items = table.getItems();
        for (TableItem item : items) {
            removeTableItem(item);
        }
        table.setRedraw(true);
    }

    private void removeTableItem(TableItem item) {
        if (item == null) {
            return;
        }
        item.dispose();
    }

    public String getSelectFile() {
        return selectFile;
    }

    public void setSelectFile(String selectFile) {
        this.selectFile = selectFile;
    }
}
