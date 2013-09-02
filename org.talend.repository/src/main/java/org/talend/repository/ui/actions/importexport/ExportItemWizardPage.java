// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.actions.importexport;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.progress.ProgressMonitorJobsDialog;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.model.metadata.MetadataColumnRepositoryObject;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IExtendedNodeHandler;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.ui.advanced.composite.FilteredCheckboxTree;
import org.talend.repository.ProjectManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.local.ExportItemUtil;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.viewer.ui.provider.RepoCommonViewerProvider;

/**
 * Initialy copied from org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage.
 */
class ExportItemWizardPage extends WizardPage {

    private Button itemFromDirectoryRadio;

    private Text directoryPathField;

    private Button browseDirectoriesButton;

    private Button itemFromArchiveRadio;

    private Text archivePathField;

    private Button browseArchivesButton;

    private String previouslyBrowsedDirectory = ""; //$NON-NLS-1$

    private String previouslyBrowsedArchive = ""; //$NON-NLS-1$

    private static final String[] FILE_EXPORT_MASK = { "*.zip;*.tar;*.tar.gz", "*.*" }; //$NON-NLS-1$ //$NON-NLS-2$

    private static final String DESTINATION_FILE = "destinationFile"; //$NON-NLS-1$

    private static final String DIRECTORY_PATH = "directoryPath"; //$NON-NLS-1$

    private static final String ARCHIVE_PATH = "archivePath"; //$NON-NLS-1$

    private String lastPath;

    private IStructuredSelection selection;

    private FilteredCheckboxTree filteredCheckboxTree;

    private Button exportDependencies;

    Collection repositoryNodes = new ArrayList();

    Set checkedNodes = new HashSet();

    Set allNode = new HashSet();

    protected ExportItemWizardPage(String pageName, IStructuredSelection selection) {
        super(pageName);
        this.selection = selection;
        setDescription(Messages.getString("ExportItemWizardPage.description")); //$NON-NLS-1$
        setImageDescriptor(WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_WIZBAN_EXPORT_WIZ));

        TimeMeasure.display = CommonsPlugin.isDebugMode();
        TimeMeasure.displaySteps = CommonsPlugin.isDebugMode();
        TimeMeasure.measureActive = CommonsPlugin.isDebugMode();
    }

    private CheckboxTreeViewer getItemsTreeViewer() {
        return filteredCheckboxTree.getViewer();
    }

    @Override
    public void createControl(Composite parent) {

        TimeMeasure.begin(this.getClass().getSimpleName());

        Composite workArea = new Composite(parent, SWT.NONE);
        setControl(workArea);

        workArea.setLayout(new GridLayout());
        workArea.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

        createItemRoot(workArea);
        TimeMeasure.step(this.getClass().getSimpleName(), "finished to createItemRoot"); //$NON-NLS-1$

        createItemList(workArea);
        TimeMeasure.step(this.getClass().getSimpleName(), "finished to createItemList"); //$NON-NLS-1$

        TimeMeasure.end(this.getClass().getSimpleName());
        TimeMeasure.display = false;
        TimeMeasure.displaySteps = false;
        TimeMeasure.measureActive = false;
    }

    private String reloadExportPath(String pathType) {
        if (getDialogSettings() != null) {
            IDialogSettings section = getDialogSettings().getSection(DESTINATION_FILE);
            if (section == null) {
                section = getDialogSettings().addNewSection(DESTINATION_FILE);
            }
            return section.get(pathType);
        }
        return null;
    }

    /**
     * DOC hcw Comment method "createItemList".
     * 
     * @param workArea
     */
    private void createItemList(Composite workArea) {
        Composite itemComposite = new Composite(workArea, SWT.NONE);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(itemComposite);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).hint(500, 300).applyTo(itemComposite);

        Label label = new Label(itemComposite, SWT.NONE);
        label.setText(Messages.getString("ExportItemWizardPage.labelText")); //$NON-NLS-1$
        GridDataFactory.swtDefaults().span(2, 1).applyTo(label);

        createTreeViewer(itemComposite);
        TimeMeasure.step(this.getClass().getSimpleName(), "finished to createTreeViewer"); //$NON-NLS-1$

        createSelectionButton(itemComposite);
        CheckboxTreeViewer exportItemsTreeViewer = getItemsTreeViewer();
        // exportItemsTreeViewer.refresh();
        exportItemsTreeViewer.getTree().setRedraw(false);
        // force loading all nodes
        exportItemsTreeViewer.expandAll();
        TimeMeasure.step(this.getClass().getSimpleName(), "finished to expandAll"); //$NON-NLS-1$
        exportItemsTreeViewer.collapseAll();
        TimeMeasure.step(this.getClass().getSimpleName(), "finished to collapseAll"); //$NON-NLS-1$
        // expand to level of metadata connection
        exportItemsTreeViewer.expandToLevel(3);
        TimeMeasure.step(this.getClass().getSimpleName(), "finished to expandToLevel"); //$NON-NLS-1$
        exportItemsTreeViewer.getTree().setRedraw(true);
        TimeMeasure.step(this.getClass().getSimpleName(), "finished to redraw"); //$NON-NLS-1$

        addTreeCheckedSelection();
        // if user has select some items in repository view, mark them as checked
        if (selection != null && !selection.isEmpty()) {
            // for bug 10969
            Set newSelection = new HashSet();
            for (Object currentNode : selection.toList()) {
                // List<IRepositoryViewObject> objects = null;
                // if (currentNode.getContentType() != null && currentNode.getObject() != null
                // && currentNode.getObjectType() != ERepositoryObjectType.FOLDER) {
                // try {
                // objects = exportItemsTreeViewer.getAll(currentNode.getObjectType());
                // } catch (IllegalArgumentException e) {
                // // do nothing
                // objects = new ArrayList<IRepositoryViewObject>();
                // }
                //
                // for (IRepositoryViewObject nodeToSelect : objects) {
                // if (currentNode.getObject().getId().equals(nodeToSelect.getId())) {
                // newSelection.add((RepositoryNode) nodeToSelect.getRepositoryNode());
                // }
                // }
                // } else {
                newSelection.add(currentNode);
                // }
            }

            repositoryNodes.addAll(newSelection);
            repositoryNodes.addAll(checkedNodes);

            Set nodes = new HashSet();

            for (Object obj : repositoryNodes) {
                if (obj instanceof RepositoryNode) {
                    RepositoryNode node = (RepositoryNode) obj;
                    expandRoot(node);
                    expandParent(exportItemsTreeViewer, node);
                    checkElement(node, nodes);
                } else {
                    // for transform node
                    nodes.add(obj);
                }
            }
            TimeMeasure.step(this.getClass().getSimpleName(), "finished to collect nodes"); //$NON-NLS-1$
            exportItemsTreeViewer.setCheckedElements(nodes.toArray());
            TimeMeasure.step(this.getClass().getSimpleName(), "finished to check nodes"); //$NON-NLS-1$
        }
    }

    protected boolean selectRepositoryNode(Viewer viewer, RepositoryNode node) {
        if (node == null) {
            return false;
        }
        IRepositoryViewObject object = node.getObject();
        if (object != null) {
            // column
            if (object instanceof MetadataColumnRepositoryObject) {
                return false;
            }
            if (node.getObjectType() == ERepositoryObjectType.METADATA_CON_TABLE) {
                return false;
            }
        }
        // hide the conn folder
        if (object == null && node.getParent() != null && node.getParent().getObject() != null
                && node.getParent().getObjectType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
            return false;
        }
        // hide the generated documentation node, avoid to export .
        if (object == null && ERepositoryObjectType.GENERATED.equals(node.getContentType())) {
            return false;
        }

        return true;
    }

    private void refreshExportDependNodes() {
        checkedNodes.clear();
        CheckboxTreeViewer exportItemsTreeViewer = getItemsTreeViewer();
        Object[] checkedObj = exportItemsTreeViewer.getCheckedElements();
        for (Object element : checkedObj) {
            if (element instanceof RepositoryNode) {
                RepositoryNode checkedNode = (RepositoryNode) element;
                if (checkedNode != null && !RepositoryNode.NO_ID.equals(checkedNode.getId())) {
                    if (ENodeType.REPOSITORY_ELEMENT.equals(checkedNode.getType())) {
                        checkedNodes.add(checkedNode);
                    }
                }
            }
        }
        allNode.clear();
        allNode.addAll(repositoryNodes);
        allNode.addAll(checkedNodes);
    }

    private void checkElement(RepositoryNode node, Set nodes) {
        if (node == null) {
            return;
        }
        ERepositoryObjectType objectType = node.getObjectType();
        Property property = null;
        if (objectType != null) {
            if (objectType == ERepositoryObjectType.METADATA_CON_TABLE || objectType == ERepositoryObjectType.METADATA_CON_VIEW
                    || objectType == ERepositoryObjectType.METADATA_CON_SYNONYM
                    || objectType == ERepositoryObjectType.METADATA_CON_QUERY) {
                if (node.getObject() != null) {
                    property = node.getObject().getProperty();
                }
            }

        }
        if (property != null) {
            RepositoryNode repositoryNode = RepositoryNodeUtilities.getRepositoryNode(property.getId(), false);
            if (repositoryNode != null) {
                nodes.add(repositoryNode);
            }
        } else {
            nodes.add(node);
        }

    }

    private void expandParent(TreeViewer viewer, RepositoryNode node) {
        if (node.getContentType() == ERepositoryObjectType.SVN_ROOT) {
            viewer.setExpandedState(node, true);
        }
        RepositoryNode parent = node.getParent();
        if (parent != null) {
            expandParent(viewer, parent);
            if (ERepositoryObjectType.METADATA_CONNECTIONS != null
                    && ERepositoryObjectType.METADATA_CONNECTIONS.equals(node.getObjectType())) {
                viewer.expandToLevel(node, TreeViewer.ALL_LEVELS);
            } else {
                viewer.setExpandedState(node, true);
            }

        }
    }

    // expand root node for node in metadata , routines , documentation
    private void expandRoot(RepositoryNode node) {
        ERepositoryObjectType objectType = node.getObjectType();

        // for user folders in metadata , routines , documentation
        if (ERepositoryObjectType.FOLDER.equals(objectType)) {
            RepositoryNode rootNode = getParentNodeNotFolder(node);
            objectType = rootNode.getContentType();
        }
        if (objectType == null) {
            objectType = node.getContentType();
        }

        if (objectType != null) {
            if (objectType == ERepositoryObjectType.METADATA_CON_TABLE || objectType == ERepositoryObjectType.METADATA_CON_VIEW
                    || objectType == ERepositoryObjectType.METADATA_CON_SYNONYM
                    || objectType == ERepositoryObjectType.METADATA_CON_QUERY
                    || objectType == ERepositoryObjectType.METADATA_CONNECTIONS
                    || objectType == ERepositoryObjectType.METADATA_FILE_DELIMITED
                    || objectType == ERepositoryObjectType.METADATA_FILE_POSITIONAL
                    || objectType == ERepositoryObjectType.METADATA_FILE_REGEXP
                    || objectType == ERepositoryObjectType.METADATA_FILE_XML
                    || objectType == ERepositoryObjectType.METADATA_FILE_LDIF
                    || objectType == ERepositoryObjectType.METADATA_FILE_EXCEL
                    || objectType == ERepositoryObjectType.METADATA_GENERIC_SCHEMA
                    || objectType == ERepositoryObjectType.METADATA_LDAP_SCHEMA
                    || objectType == ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA
                    || objectType == ERepositoryObjectType.METADATA_WSDL_SCHEMA
                    || objectType == ERepositoryObjectType.METADATA_FILE_EBCDIC
                    || objectType == ERepositoryObjectType.METADATA_FILE_HL7
                    || objectType == ERepositoryObjectType.METADATA_MDMCONNECTION
                    || objectType == ERepositoryObjectType.METADATA_FILE_RULES
                    || objectType == ERepositoryObjectType.METADATA_SAPCONNECTIONS
                    || objectType == ERepositoryObjectType.METADATA_SAP_FUNCTION
                    || objectType == ERepositoryObjectType.METADATA_SAP_IDOC
                    || objectType == ERepositoryObjectType.METADATA_HEADER_FOOTER) {
                objectType = ERepositoryObjectType.METADATA;
            } else if (objectType == ERepositoryObjectType.ROUTINES || objectType == ERepositoryObjectType.SNIPPETS) {
                objectType = ERepositoryObjectType.ROUTINES;
            } else if (objectType == ERepositoryObjectType.DOCUMENTATION || objectType == ERepositoryObjectType.JOB_DOC
                    || objectType == ERepositoryObjectType.JOBLET_DOC) {
                objectType = ERepositoryObjectType.DOCUMENTATION;
            }
        }
        if (objectType != null) {
            CheckboxTreeViewer exportItemsTreeViewer = getItemsTreeViewer();
            if (objectType == ERepositoryObjectType.METADATA || objectType == ERepositoryObjectType.ROUTINES
                    || objectType == ERepositoryObjectType.DOCUMENTATION) {
                RepositoryNode rootRepositoryNode = ProjectRepositoryNode.getInstance().getRootRepositoryNode(objectType);
                if (rootRepositoryNode != null) {
                    exportItemsTreeViewer.expandToLevel(rootRepositoryNode, 2);
                }
            }

        }

    }

    private RepositoryNode getParentNodeNotFolder(RepositoryNode node) {
        if (ERepositoryObjectType.FOLDER.equals(node.getObjectType())) {
            return getParentNodeNotFolder(node.getParent());
        } else {
            return node;
        }
    }

    private void addTreeCheckedSelection() {
        CheckboxTreeViewer exportItemsTreeViewer = getItemsTreeViewer();
        exportItemsTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                refreshExportDependNodes();
            }

        });
    }

    private void createTreeViewer(Composite itemComposite) {
        filteredCheckboxTree = new FilteredCheckboxTree(itemComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI) {

            @Override
            protected CheckboxTreeViewer doCreateTreeViewer(Composite parent, int style) {
                RepoCommonViewerProvider provider = RepoCommonViewerProvider.CHECKBOX;

                return (CheckboxTreeViewer) provider.createViewer(parent);

            }

            @Override
            protected void refreshCompleted() {
                getViewer().expandToLevel(3);
                restoreCheckedElements();
            }

            @Override
            protected boolean isNodeCollectable(TreeItem item) {
                Object obj = item.getData();
                if (obj instanceof RepositoryNode) {
                    RepositoryNode node = (RepositoryNode) obj;
                    if (node.getObjectType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
                        return true;
                    }
                }
                return false;
            }
        };
        filteredCheckboxTree.getViewer().addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof RepositoryNode) {
                    return selectRepositoryNode(viewer, (RepositoryNode) element);
                } else {
                    for (IExtendedNodeHandler nodeHandler : RepositoryContentManager.getExtendedNodeHandler()) {
                        boolean exportFilter = nodeHandler.exportFilter(viewer, parentElement, element);
                        if (!exportFilter) {
                            return exportFilter;
                        }

                    }
                }
                return true;
            }
        });
    }

    /**
     * DOC hcw Comment method "createSelectionButton".
     * 
     * @param itemComposite
     */
    private void createSelectionButton(Composite itemComposite) {
        Composite buttonComposite = new Composite(itemComposite, SWT.NONE);
        GridLayoutFactory.swtDefaults().margins(0, 25).applyTo(buttonComposite);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).applyTo(buttonComposite);

        Button selectAll = new Button(buttonComposite, SWT.PUSH);
        selectAll.setText(DataTransferMessages.DataTransfer_selectAll);
        selectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                CheckboxTreeViewer exportItemsTreeViewer = getItemsTreeViewer();
                exportItemsTreeViewer.setAllChecked(true);
            }
        });

        setButtonLayoutData(selectAll);

        Button deselectAll = new Button(buttonComposite, SWT.PUSH);
        deselectAll.setText(DataTransferMessages.DataTransfer_deselectAll);
        deselectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                CheckboxTreeViewer exportItemsTreeViewer = getItemsTreeViewer();
                exportItemsTreeViewer.setAllChecked(false);
            }
        });

        setButtonLayoutData(deselectAll);

        Button expandBtn = new Button(buttonComposite, SWT.PUSH);
        expandBtn.setText(Messages.getString("ExportItemWizardPage.expandBtnText")); //$NON-NLS-1$
        expandBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                CheckboxTreeViewer exportItemsTreeViewer = getItemsTreeViewer();
                exportItemsTreeViewer.expandAll();
            }
        });
        setButtonLayoutData(expandBtn);

        Button collapseBtn = new Button(buttonComposite, SWT.PUSH);
        collapseBtn.setText(Messages.getString("ExportItemWizardPage.collapseBtnText")); //$NON-NLS-1$
        collapseBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                CheckboxTreeViewer exportItemsTreeViewer = getItemsTreeViewer();
                exportItemsTreeViewer.collapseAll();
            }
        });
        setButtonLayoutData(collapseBtn);
    }

    @SuppressWarnings("restriction")
    private void createItemRoot(Composite workArea) {
        Composite projectGroup = new Composite(workArea, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.makeColumnsEqualWidth = false;
        layout.marginWidth = 0;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        itemFromDirectoryRadio = new Button(projectGroup, SWT.RADIO);
        itemFromDirectoryRadio.setText(DataTransferMessages.WizardProjectsImportPage_RootSelectTitle);

        this.directoryPathField = new Text(projectGroup, SWT.BORDER);

        this.directoryPathField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        String exportDirPath = reloadExportPath(DIRECTORY_PATH);
        if (exportDirPath != null) {
            this.directoryPathField.setText(exportDirPath);
            this.lastPath = exportDirPath;
        }

        browseDirectoriesButton = new Button(projectGroup, SWT.PUSH);
        browseDirectoriesButton.setText(DataTransferMessages.DataTransfer_browse);
        setButtonLayoutData(browseDirectoriesButton);

        // new project from archive radio button
        itemFromArchiveRadio = new Button(projectGroup, SWT.RADIO);
        itemFromArchiveRadio.setText(DataTransferMessages.WizardProjectsImportPage_ArchiveSelectTitle);

        // project location entry field
        archivePathField = new Text(projectGroup, SWT.BORDER);

        archivePathField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

        if (selection != null && selection.getFirstElement() instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) selection.getFirstElement();
            String arcFileName;
            if (node.getObject() == null) {
                arcFileName = node.getLabel();
            } else {
                arcFileName = node.getObject().getLabel();
            }
            String exportArchivePath = reloadExportPath(ARCHIVE_PATH);
            // when first open the exportItem dialog ,the exportPath maybe empty,need judge
            if (exportArchivePath != null && exportArchivePath.trim().length() > 0) {
                File beforeExportArchiveFolder = new File(exportArchivePath).getParentFile();
                /*
                 * TDI-22791, when the exportArchivePath is only name without path. No need check the existed or not.
                 * Because will be create the parent folders, when export item.
                 */
                if (beforeExportArchiveFolder != null/* && beforeExportArchiveFolder.exists() */) {
                    String newPath = new File(beforeExportArchiveFolder, arcFileName + FileConstants.ZIP_FILE_SUFFIX)
                            .getAbsolutePath();
                    this.archivePathField.setText(newPath);
                }
            } else if (exportDirPath != null && exportDirPath.trim().length() > 0) {
                String newPath = exportDirPath + File.separator + arcFileName + FileConstants.ZIP_FILE_SUFFIX;
                this.archivePathField.setText(newPath);
            }
        }

        // browse button
        browseArchivesButton = new Button(projectGroup, SWT.PUSH);
        browseArchivesButton.setText(DataTransferMessages.DataTransfer_browse);
        setButtonLayoutData(browseArchivesButton);

        itemFromDirectoryRadio.setSelection(true);
        archivePathField.setEnabled(false);
        browseArchivesButton.setEnabled(false);

        browseDirectoriesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleLocationDirectoryButtonPressed();
            }
        });

        browseArchivesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleLocationArchiveButtonPressed();
            }
        });

        directoryPathField.addTraverseListener(new TraverseListener() {

            @Override
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                    lastPath = directoryPathField.getText().trim();
                }
            }

        });

        directoryPathField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(org.eclipse.swt.events.FocusEvent e) {
                lastPath = directoryPathField.getText().trim();
            }

        });

        directoryPathField.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                saveExportPath(DIRECTORY_PATH, directoryPathField.getText());
            }

        });

        archivePathField.addTraverseListener(new TraverseListener() {

            @Override
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                    lastPath = archivePathField.getText().trim();
                }
            }
        });

        archivePathField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(org.eclipse.swt.events.FocusEvent e) {
                lastPath = archivePathField.getText().trim();
            }
        });

        archivePathField.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                saveExportPath(ARCHIVE_PATH, archivePathField.getText());
            }

        });
        itemFromDirectoryRadio.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                directoryRadioSelected();
            }
        });

        itemFromArchiveRadio.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                archiveRadioSelected();
            }
        });

        exportDependencies = new Button(workArea, SWT.CHECK);
        exportDependencies.setText(Messages.getString("ExportItemWizardPage.exportDependenciesText")); //$NON-NLS-1$
        exportDependencies.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                refreshExportDependNodes();
                CheckboxTreeViewer exportItemsTreeViewer = getItemsTreeViewer();
                exportItemsTreeViewer.expandAll();
                exportItemsTreeViewer.collapseAll();
                exportItemsTreeViewer.expandToLevel(3);
                exportDependenciesSelected();
                allNode.clear();
                if (exportDependencies.getSelection()) {
                    allNode.addAll(checkedNodes);
                } else {
                    repositoryNodes.clear();
                    repositoryNodes.addAll(selection.toList());
                    allNode.addAll(repositoryNodes);
                }
                Set<RepositoryNode> nodes = new HashSet<RepositoryNode>();
                for (Object obj : allNode) {
                    if (obj instanceof RepositoryNode) {
                        RepositoryNode node = (RepositoryNode) obj;
                        expandRoot(node);
                        expandParent(exportItemsTreeViewer, node);
                        checkElement(node, nodes);
                    }
                }
                exportItemsTreeViewer.setCheckedElements(nodes.toArray());
            }
        });
    }

    /**
     * DOC qwei Comment method "exportDependenciesSelected".
     */
    private void exportDependenciesSelected() {
        final Collection<Item> selectedItems = getSelectedItems();

        // addTreeCheckedSelection();

        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask("Dependencies", 100);//$NON-NLS-1$
                monitor.setCanceled(false);
                //
                final List<IRepositoryViewObject> repositoryObjects = new ArrayList<IRepositoryViewObject>();

                ProcessUtils.clearFakeProcesses();

                // dependencies All
                Display.getDefault().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        for (Item item : selectedItems) {
                            RepositoryNodeUtilities.checkItemDependencies(item, repositoryObjects);
                        }

                    }
                });
                monitor.worked(60);
                Display.getDefault().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        if (exportDependencies.getSelection()) {
                            for (IRepositoryViewObject repositoryObject : repositoryObjects) {
                                RepositoryNode repositoryNode = RepositoryNodeUtilities
                                        .getRepositoryNode(repositoryObject, false);
                                if (repositoryNode != null && !repositoryNodes.contains(repositoryNode)) {
                                    repositoryNodes.add(repositoryNode);
                                    checkedNodes.add(repositoryNode);
                                }

                            }
                        } else {
                            for (IRepositoryViewObject repositoryObject : repositoryObjects) {
                                RepositoryNode repositoryNode = RepositoryNodeUtilities
                                        .getRepositoryNode(repositoryObject, false);
                                if (repositoryNode != null && repositoryNodes.contains(repositoryNode)) {
                                    repositoryNodes.remove(repositoryNode);
                                    checkedNodes.remove(repositoryNode);
                                }
                            }
                        }
                    }
                });
                monitor.worked(90);
                // selection
                Display.getDefault().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        CheckboxTreeViewer exportItemsTreeViewer = getItemsTreeViewer();
                        Set nodes = new HashSet();
                        nodes.addAll(repositoryNodes);
                        nodes.addAll(checkedNodes);
                        exportItemsTreeViewer.setCheckedElements(nodes.toArray());

                    }
                });
                ProcessUtils.clearFakeProcesses();
                monitor.done();
            }

        };
        final ProgressMonitorJobsDialog dialog = new ProgressMonitorJobsDialog(getShell());
        try {
            dialog.run(true, false, runnable);
        } catch (InvocationTargetException e) {
            //
        } catch (InterruptedException e) {
            //
        }

    }

    private void archiveRadioSelected() {
        if (itemFromArchiveRadio.getSelection()) {
            directoryPathField.setEnabled(false);
            browseDirectoriesButton.setEnabled(false);
            archivePathField.setEnabled(true);
            browseArchivesButton.setEnabled(true);
            lastPath = archivePathField.getText().trim();
            archivePathField.setFocus();
        }
    }

    @SuppressWarnings("restriction")
    protected void handleLocationDirectoryButtonPressed() {

        DirectoryDialog dialog = new DirectoryDialog(directoryPathField.getShell());
        dialog.setMessage(DataTransferMessages.FileExport_selectDestinationTitle);

        String dirName = directoryPathField.getText().trim();
        if (dirName.length() == 0) {
            dirName = previouslyBrowsedDirectory;
        }

        if (dirName.length() == 0) {
            dialog.setFilterPath(IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getLocation().toOSString());
        } else {
            File path = new File(dirName);
            if (path.exists()) {
                dialog.setFilterPath(new Path(dirName).toOSString());
            }
        }

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            previouslyBrowsedDirectory = selectedDirectory;
            directoryPathField.setText(previouslyBrowsedDirectory);
            lastPath = directoryPathField.getText().trim();
            saveExportPath(DIRECTORY_PATH, lastPath);

        }

    }

    /**
     * The browse button has been selected. Select the location.
     */
    @SuppressWarnings("restriction")
    protected void handleLocationArchiveButtonPressed() {

        FileDialog dialog = new FileDialog(archivePathField.getShell(), SWT.SAVE);
        dialog.setFilterExtensions(FILE_EXPORT_MASK);
        dialog.setText(DataTransferMessages.ArchiveExport_selectDestinationTitle);

        String fileName = archivePathField.getText().trim();
        if (fileName.length() == 0) {
            fileName = previouslyBrowsedArchive;
        }

        if (fileName.length() == 0) {
            dialog.setFilterPath(IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getLocation().toOSString());
        } else {
            File path = new File(fileName);
            if (path.exists()) {
                dialog.setFilterPath(new Path(fileName).toOSString());
            }
        }

        String selectedArchive = dialog.open();
        if (selectedArchive != null) {
            previouslyBrowsedArchive = selectedArchive;
            archivePathField.setText(previouslyBrowsedArchive);
            lastPath = archivePathField.getText().trim();
            saveExportPath(ARCHIVE_PATH, lastPath);

        }

    }

    private void saveExportPath(String pathType, String path) {
        if (getDialogSettings() != null) {
            IDialogSettings section = getDialogSettings().getSection(DESTINATION_FILE);
            if (section == null) {
                section = getDialogSettings().addNewSection(DESTINATION_FILE);
            }
            if (section != null) {
                section.put(pathType, path);
            }
        }
    }

    private void directoryRadioSelected() {
        if (itemFromDirectoryRadio.getSelection()) {
            directoryPathField.setEnabled(true);
            browseDirectoriesButton.setEnabled(true);
            archivePathField.setEnabled(false);
            browseArchivesButton.setEnabled(false);
            lastPath = directoryPathField.getText().trim();
            directoryPathField.setFocus();
        }
    }

    private void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file2 : files) {
                    this.deleteFile(file2);
                }
            }
            file.delete();
        }
    }

    public boolean performFinish() {
        if (!checkExportFile()) {
            return false;
        }
        ProjectManager pManager = ProjectManager.getInstance();
        Project project = pManager.getCurrentProject().getEmfProject();
        String projectPath = lastPath + "\\" + project.getTechnicalLabel(); //$NON-NLS-1$
        if (new File(projectPath).exists() || new File(archivePathField.getText()).exists()) {
            File oldFile = new File(projectPath).exists() ? new File(projectPath) : new File(archivePathField.getText());
            if (MessageDialogWithToggle
                    .openConfirm(
                            null,
                            Messages.getString("ExportItemWizardPage.waring"), Messages.getString("ExportItemWizardPage.fileAlreadyExist"))) { //$NON-NLS-1$ //$NON-NLS-2$
                deleteFile(oldFile);
            } else {
                return false;
            }
        }
        Collection<Item> selectedItems = getSelectedItems();
        try {
            ExportItemUtil exportItemUtil = new ExportItemUtil();
            if (itemFromArchiveRadio.getSelection()) {
                if (lastPath != null && !lastPath.endsWith(FileConstants.TAR_FILE_SUFFIX)
                        && !lastPath.endsWith(FileConstants.TAR_GZ_FILE_SUFFIX)
                        && !lastPath.endsWith(FileConstants.ZIP_FILE_SUFFIX)) {
                    // set zip as default
                    lastPath = lastPath + FileConstants.ZIP_FILE_SUFFIX;
                }
            }

            // MOD sgandon 31/03/2010 bug 12229: moved getAllVersion into ExportItemUtil.exportitems() method.
            exportItemUtil.exportItems(new File(lastPath), selectedItems, true, new NullProgressMonitor());

        } catch (Exception e) {
            MessageBoxExceptionHandler.process(e);
        }
        return true;
    }

    private boolean checkExportFile() {
        if (lastPath == null || "".equals(lastPath.trim())) {//$NON-NLS-1$
            MessageDialog.openError(getShell(), "Error", "Must input the export path or archive file.");//$NON-NLS-1$//$NON-NLS-2$
            return false;
        }
        return true;
    }

    private static boolean isRepositoryFolder(RepositoryNode node) {
        final ENodeType type = node.getType();
        if (type == ENodeType.SIMPLE_FOLDER || type == ENodeType.STABLE_SYSTEM_FOLDER || type == ENodeType.SYSTEM_FOLDER) {
            return true;
        }
        return false;
    }

    /**
     * Get all selected items to export.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    private Collection<Item> getSelectedItems() {
        // add this if user use filter
        Set checkedElements = new HashSet();
        for (Object obj : filteredCheckboxTree.getCheckedLeafNodes()) {
            // if (obj instanceof RepositoryNode) {
            checkedElements.add(obj);
            // }
        }

        // add this if user does not use filter
        for (Object obj : filteredCheckboxTree.getViewer().getCheckedElements()) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode repositoryNode = (RepositoryNode) obj;
                if (!isRepositoryFolder(repositoryNode) && !(repositoryNode instanceof ProjectRepositoryNode)) {
                    checkedElements.add(obj);
                }
            } else {
                for (IExtendedNodeHandler nodeHandler : RepositoryContentManager.getExtendedNodeHandler()) {
                    if (nodeHandler.isExportEnable(obj)) {
                        checkedElements.add(obj);
                        break;
                    }

                }
            }
        }

        Object[] elements = checkedElements.toArray();

        Map<String, Item> items = new HashMap<String, Item>();
        collectNodes(items, elements);
        return new ArrayList<Item>(items.values());
    }

    private void collectNodes(Map<String, Item> items, Object[] objects) {
        for (Object object : objects) {
            if (object instanceof RepositoryNode) {
                RepositoryNode repositoryNode = (RepositoryNode) object;
                IRepositoryViewObject repositoryObject = repositoryNode.getObject();
                if (repositoryObject != null) {
                    if (repositoryObject.getRepositoryObjectType().isResourceItem()) {
                        Item item = repositoryObject.getProperty().getItem();
                        items.put(item.getProperty().getId(), item);
                    }
                } else {
                    if (repositoryNode.getParent() != null && repositoryNode.getParent().getObject() != null) {
                        Item item = repositoryNode.getParent().getObject().getProperty().getItem();
                        items.put(item.getProperty().getId(), item);
                    }
                }
                if (filteredCheckboxTree != null && !isHadoopClusterNode(repositoryNode)) {
                    IContentProvider contentProvider = filteredCheckboxTree.getViewer().getContentProvider();
                    if (contentProvider instanceof ITreeContentProvider) {
                        Object[] children = ((ITreeContentProvider) contentProvider).getChildren(repositoryNode);
                        collectNodes(items, children);
                    }
                }
            } else {
                for (IExtendedNodeHandler nodeHandler : RepositoryContentManager.getExtendedNodeHandler()) {
                    Property property = nodeHandler.getProperty(object);
                    if (property != null) {
                        items.put(property.getId(), property.getItem());
                    }
                }
            }
        }
    }

    // private void collectNodes(Map<String, Item> items, RepositoryNode repositoryNode) {
    // IRepositoryViewObject repositoryObject = repositoryNode.getObject();
    // if (repositoryObject != null) {
    // if (repositoryObject.getRepositoryObjectType().isResourceItem()) {
    // Item item = repositoryObject.getProperty().getItem();
    // items.put(item.getProperty().getId(), item);
    // }
    // } else {
    // if (repositoryNode.getParent() != null && repositoryNode.getParent().getObject() != null) {
    // Item item = repositoryNode.getParent().getObject().getProperty().getItem();
    // items.put(item.getProperty().getId(), item);
    // }
    // }
    // if (filteredCheckboxTree != null && !isHadoopClusterNode(repositoryNode)) {
    // IContentProvider contentProvider = filteredCheckboxTree.getViewer().getContentProvider();
    // if (contentProvider instanceof ITreeContentProvider) {
    // Object[] children = ((ITreeContentProvider) contentProvider).getChildren(repositoryNode);
    // collectNodes(items, children);
    // }
    // }
    //
    // }

    public boolean performCancel() {
        return true;
    }

    private boolean isHadoopClusterNode(IRepositoryNode repositoryNode) {
        IHadoopClusterService hadoopClusterService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopClusterService.class)) {
            hadoopClusterService = (IHadoopClusterService) GlobalServiceRegister.getDefault().getService(
                    IHadoopClusterService.class);
        }
        if (hadoopClusterService != null) {
            return hadoopClusterService.isHadoopClusterNode(repositoryNode);
        }

        return false;
    }

}
