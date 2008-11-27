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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.Query;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.ICDCProviderService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.MetadataTableRepositoryObject;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.SAPFunctionRepositoryObject;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryContentProvider;
import org.talend.repository.ui.views.RepositoryView;

/**
 * bqian check the content of the repository view. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class RepositoryReviewDialog extends Dialog {

    ERepositoryObjectType type;

    String repositoryType;

    private FakeRepositoryView repositoryView;

    private RepositoryNode result;

    ITypeProcessor typeProcessor;

    private String selectedNodeName;

    ViewerTextFilter textFilter = new ViewerTextFilter();

    /**
     * DOC bqian RepositoryReviewDialog constructor comment.
     * 
     * @param parentShell
     * @param type support ERepositoryObjectType.PROCESS -> process <br>
     * ERepositoryObjectType.METADATA --> Repository <br>
     * ERepositoryObjectType.METADATA_CON_TABLE --> Schema <br>
     * ERepositoryObjectType.METADATA_CON_QUERY --> Query <br>
     * 
     * @param repositoryType String repositoryType = elem.getElementParameter(paramName).getRepositoryValue();<br>
     * see DynamicComposite.updateRepositoryListExtra().<br>
     * 
     * 
     */
    public RepositoryReviewDialog(Shell parentShell, ERepositoryObjectType type, String repositoryType) {
        super(parentShell);
        setShellStyle(SWT.SHELL_TRIM | SWT.APPLICATION_MODAL | getDefaultOrientation());
        this.type = type;
        /*
         * avoid select self repository node for Process Type.
         * 
         * borrow the repositoryType to set the current process id here.
         */
        this.repositoryType = repositoryType;
        typeProcessor = createTypeProcessor();
    }

    public RepositoryReviewDialog(Shell parentShell, ERepositoryObjectType type) {
        this(parentShell, type, null);
    }

    /**
     * bqian create the correct TypeProcessor according to the type.
     * 
     * @return
     */
    private ITypeProcessor createTypeProcessor() {
        if (type == ERepositoryObjectType.PROCESS) {
            return new JobTypeProcessor(repositoryType);
        }
        if (type == ERepositoryObjectType.METADATA) {
            return new RepositoryTypeProcessor(repositoryType);
        }

        if (type == ERepositoryObjectType.METADATA_CON_TABLE) {
            return new SchemaTypeProcessor(repositoryType);
        }

        if (type == ERepositoryObjectType.METADATA_CON_QUERY) {
            return new QueryTypeProcessor(repositoryType);
        }

        if (type == ERepositoryObjectType.METADATA_SAP_FUNCTION) {
            return new SAPFunctionProcessor(repositoryType);
        }

        throw new IllegalArgumentException("illegal argument:" + type);
    }

    /**
     * Configures the shell
     * 
     * @param shell the shell
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        // Set the title bar text and the size
        shell.setText("Repository Content");
        // shell.setSize(500, 400);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);

        GridData data = (GridData) container.getLayoutData();
        data.minimumHeight = 400;
        data.heightHint = 400;
        data.minimumWidth = 500;
        data.widthHint = 500;
        container.setLayoutData(data);

        // create text filter
        Label label = new Label(container, SWT.NONE);
        label.setText("Type job name prefix or pattern(*, ?, or camel case):");
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Text text = new Text(container, SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String pattern = text.getText();
                pattern = pattern.replace("*", ".*");
                pattern = pattern.replace("?", ".");
                pattern = "(?i)" + pattern + ".*";
                textFilter.setText(pattern);
                repositoryView.refresh();
                repositoryView.selectFirstOne((RepositoryNode) repositoryView.getViewer().getInput());
            }
        });

        Composite viewContainer = new Composite(container, SWT.BORDER);
        viewContainer.setLayout(new GridLayout());
        viewContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

        IRepositoryView view = RepositoryView.show();
        repositoryView = new FakeRepositoryView(typeProcessor, type, repositoryType);
        try {
            repositoryView.init(view.getViewSite());
        } catch (PartInitException e) {
            e.printStackTrace();
        }

        repositoryView.createPartControl(viewContainer);
        repositoryView.addFilter(textFilter);
        repositoryView.refresh();

        // see feature 0003664: tRunJob: When opening the tree dialog to select the job target, it could be useful to
        // open it on previous selected job if exists
        if (selectedNodeName != null) {
            repositoryView.selectNode((RepositoryNode) repositoryView.getViewer().getInput(), selectedNodeName);
        }

        repositoryView.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                boolean highlightOKButton = true;
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (selection == null || selection.size() != 1) {
                    highlightOKButton = false;
                } else {
                    RepositoryNode node = (RepositoryNode) selection.getFirstElement();
                    ERepositoryObjectType t = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                    if (node.getType() != ENodeType.REPOSITORY_ELEMENT) {
                        highlightOKButton = false;
                    } else if (!typeProcessor.isSelectionValid(node)) {
                        highlightOKButton = false;
                    }
                }
                getButton(IDialogConstants.OK_ID).setEnabled(highlightOKButton);
            }
        });
        repositoryView.getViewer().addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                if (getButton(IDialogConstants.OK_ID).isEnabled()) {
                    okPressed();
                }
            }
        });

        return container;
    }

    public void setSelectedNodeName(String selectionNode) {
        this.selectedNodeName = selectionNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        IStructuredSelection selection = (IStructuredSelection) repositoryView.getViewer().getSelection();
        result = (RepositoryNode) selection.getFirstElement();
        super.okPressed();
    }

    public RepositoryNode getResult() {
        return result;
    }

}

/**
 * bqian class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class FakeRepositoryView extends RepositoryView {

    ERepositoryObjectType type;

    private String repositoryType;

    ITypeProcessor typeProcessor;

    /**
     * DOC bqian SnippetsDialogTrayView constructor comment.
     * 
     * @param typeProcessor
     * 
     * @param type
     * @param type
     */
    public FakeRepositoryView(ITypeProcessor typeProcessor, ERepositoryObjectType type, String repositoryValue) {
        super();
        this.typeProcessor = typeProcessor;
        this.type = type;
        this.repositoryType = repositoryValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.RepositoryView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        ViewerFilter filter = typeProcessor.makeFilter();
        addFilter(filter);
        CorePlugin.getDefault().getRepositoryService().removeRepositoryChangedListener(this);
    }

    public void addFilter(ViewerFilter filter) {
        if (filter != null) {
            getViewer().addFilter(filter);
        }
    }

    /**
     * see feature 0003664: tRunJob: When opening the tree dialog to select the job target, it could be useful to open
     * it on previous selected job if exists.
     * 
     * @param root The root node of the sub tree that we are searching.
     * @param label The label that we are looking for.
     */
    public void selectNode(RepositoryNode root, String label) {
        if (root.getProperties(EProperties.LABEL).equals(label)) {
            getViewer().setSelection(new StructuredSelection(root), true);
        } else if (root.hasChildren()) {
            for (RepositoryNode child : root.getChildren()) {
                selectNode(child, label);
            }
        }
    }

    public boolean selectFirstOne(RepositoryNode root) {
        ENodeType nodeType = root.getType();
        if (nodeType == ENodeType.REPOSITORY_ELEMENT) {
            getViewer().setSelection(new StructuredSelection(root), true);
            getViewer().expandToLevel(root, AbstractTreeViewer.ALL_LEVELS);
            return true;
        } else if (root.hasChildren()) {
            for (RepositoryNode child : root.getChildren()) {
                if (selectFirstOne(child)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.RepositoryView#refresh(java.lang.Object)
     */
    @Override
    public void refresh(Object object) {
        refresh();
        // viewer.refresh(object);
        if (object != null) {
            // getViewer().setExpandedState(object, true);
            getViewer().expandToLevel(object, AbstractTreeViewer.ALL_LEVELS);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.RepositoryView#refresh()
     */
    @Override
    public void refresh() {
        super.refresh();
        // getViewer().setInput(this.getViewSite());
        getViewer().setInput(getInput());
    }

    private RepositoryNode getInput() {
        RepositoryContentProvider contentProvider = (RepositoryContentProvider) getViewer().getContentProvider();
        return typeProcessor.getInputRoot(contentProvider);
    }

    @Override
    protected void makeActions() {
    }

    @Override
    protected void hookContextMenu() {
    }

    @Override
    protected void contributeToActionBars() {
    }

    @Override
    protected void initDragAndDrop() {
    }

    @Override
    protected void hookDoubleClickAction() {
    }

}

/**
 * bqian decouple the process logic of JOB, REPOSITORY, SCHEMA, QUERY <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
interface ITypeProcessor {

    boolean isSelectionValid(RepositoryNode node);

    RepositoryNode getInputRoot(RepositoryContentProvider contentProvider);

    ViewerFilter makeFilter();

}

/**
 * bqian TypeProcessor for Job. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class JobTypeProcessor implements ITypeProcessor {

    private String curJobId;

    /**
     * ggu JobTypeProcessor constructor comment.
     */
    public JobTypeProcessor(String curJobId) {
        this.curJobId = curJobId;
    }

    public RepositoryNode getInputRoot(RepositoryContentProvider contentProvider) {
        List<RepositoryNode> refProjects = null;
        if (contentProvider.getReferenceProjectNode() != null) {
            refProjects = contentProvider.getReferenceProjectNode().getChildren();
        } else {
            refProjects = Collections.EMPTY_LIST;
        }

        RepositoryNode mainJobs = contentProvider.getProcessNode();
        if (!refProjects.isEmpty()) {
            List<RepositoryNode> list = new ArrayList<RepositoryNode>();

            for (RepositoryNode repositoryNode : refProjects) {
                ProjectRepositoryNode refProject = (ProjectRepositoryNode) repositoryNode;

                ProjectRepositoryNode newProject = new ProjectRepositoryNode(refProject);
                newProject.getChildren().add(refProject.getProcessNode());
                list.add(newProject);
            }

            // add the referenced projects' jobs
            mainJobs.getChildren().addAll(list);
        }
        return mainJobs;
    }

    public boolean isSelectionValid(RepositoryNode node) {
        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.PROCESS) {
            return true;
        }
        return false;
        // else {
        // if (node.getProperties(EProperties.CONTENT_TYPE) != ERepositoryObjectType.METADATA_CON_TABLE) {
        // highlightOKButton = false;
        // }
        // }

    }

    public ViewerFilter makeFilter() {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                RepositoryNode node = (RepositoryNode) element;
                if (curJobId != null && node.getObject() != null) {
                    if (node.getObject().getId().equals(curJobId)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }
}

/**
 * bqian TypeProcessor for Repository. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class RepositoryTypeProcessor implements ITypeProcessor {

    String repositoryType;

    /**
     * DOC bqian RepositoryTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public RepositoryTypeProcessor(String repositoryType) {
        this.repositoryType = repositoryType;
    }

    public RepositoryNode getInputRoot(RepositoryContentProvider contentProvider) {
        RepositoryNode metadataNode = getMetadataNode(contentProvider);
        addReferencedProjectNodes(contentProvider, metadataNode);
        return metadataNode;
    }

    /**
     * 
     * ggu Comment method "addReferencedProjectNodes".
     * 
     */
    private void addReferencedProjectNodes(RepositoryContentProvider contentProvider, RepositoryNode metadataNode) {
        if (contentProvider == null || metadataNode == null) {
            return;
        }
        // referenced project.
        if (contentProvider.getReferenceProjectNode() != null) {
            List<RepositoryNode> refProjects = contentProvider.getReferenceProjectNode().getChildren();
            if (refProjects != null && !refProjects.isEmpty()) {

                List<RepositoryNode> nodesList = new ArrayList<RepositoryNode>();
                for (RepositoryNode repositoryNode : refProjects) {
                    ProjectRepositoryNode refProject = (ProjectRepositoryNode) repositoryNode;

                    ProjectRepositoryNode newProject = new ProjectRepositoryNode(refProject);

                    RepositoryNode refMetadataNode = getMetadataNode(refProject);

                    if (refMetadataNode != null) {
                        newProject.getChildren().add(refMetadataNode);
                        nodesList.add(newProject);
                    }
                }
                metadataNode.getChildren().addAll(nodesList);
            }
        }
    }

    private RepositoryNode getMetadataNode(Object provider) {
        RepositoryNode metadataNode = null;
        if (provider != null) {
            if (repositoryType == null) { // all
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataNode();
                }
            }
            if (repositoryType.equals("DELIMITED")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataFileNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataFileNode();
                }
            }
            if (repositoryType.equals("POSITIONAL")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataFilePositionalNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataFilePositionalNode();
                }
            }
            if (repositoryType.equals("REGEX")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataFileRegexpNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataFileRegexpNode();
                }
            }
            if (repositoryType.equals("XML")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataFileXmlNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataFileXmlNode();
                }
            }
            if (repositoryType.equals("LDIF")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataFileLdifNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataFileLdifNode();
                }
            }
            if (repositoryType.equals("EXCEL")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataFileExcelNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataFileExcelNode();
                }
            }
            if (repositoryType.equals("GENERIC")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataGenericSchemaNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataGenericSchemaNode();
                }
            }
            if (repositoryType.equals("LDAP")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataLDAPSchemaNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataLDAPSchemaNode();
                }
            }
            if (repositoryType.equals("WSDL")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataWSDLSchemaNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataWSDLSchemaNode();
                }
            }
            if (repositoryType.equals("SALESFORCE")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataSalesforceSchemaNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataSalesforceSchemaNode();
                }
            }

            if (repositoryType.startsWith("DATABASE")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataConNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataConNode();
                }
            }
            if (repositoryType.startsWith("SAP")) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider).getMetadataSAPConnectionNode();
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getMetadataSAPConnectionNode();
                }
            }
        }
        return metadataNode;
    }

    public boolean isSelectionValid(RepositoryNode node) {
        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.PROCESS) {
            return true;
        }
        return true;
    }

    public ViewerFilter makeFilter() {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                // if (repositoryType.startsWith("DATABASE") && repositoryType.contains(":")) {
                RepositoryNode node = (RepositoryNode) element;
                if (node.getContentType() == ERepositoryObjectType.REFERENCED_PROJECTS) {
                    return true;
                }
                ProjectManager pManager = ProjectManager.getInstance();
                if (!pManager.isInCurrentMainProject(node)) {
                    // for sub folders
                    if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER) {
                        return false;
                    }
                    // for Db Connections
                    if (node.getType() == ENodeType.SYSTEM_FOLDER) {
                        return true;
                    }
                }
                if (node.getObject() == null || node.getObject().getProperty().getItem() == null) {
                    return false;
                }
                if (node.getObject() instanceof MetadataTable) {
                    return false;
                }
                Item item = node.getObject().getProperty().getItem();
                if (item instanceof FolderItem) {
                    return true;
                }

                if (repositoryType.startsWith("DATABASE")) { //$NON-NLS-1$
                    ConnectionItem connectionItem = (ConnectionItem) item;
                    Connection connection = connectionItem.getConnection();
                    String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, "TYPE"); //$NON-NLS-1$
                    if (repositoryType.contains(":")) { // database
                        // is
                        // specified
                        // //$NON-NLS-1$
                        String neededDbType = repositoryType.substring(repositoryType.indexOf(":") + 1); //$NON-NLS-1$
                        if (!MetadataTalendType.sameDBProductType(neededDbType, currentDbType)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        };
    }
}

/**
 * bqian TypeProcessor for Schema. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class SchemaTypeProcessor implements ITypeProcessor {

    String repositoryType;

    /**
     * DOC bqian RepositoryTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public SchemaTypeProcessor(String repositoryType) {
        this.repositoryType = repositoryType;
    }

    public RepositoryNode getInputRoot(RepositoryContentProvider contentProvider) {
        List<RepositoryNode> container = new NoNullList<RepositoryNode>();
        if (repositoryType != null && repositoryType.startsWith("DATABASE")) {
            container.add(contentProvider.getMetadataConNode());
        } else {
            container.add(contentProvider.getMetadataFileNode());
            container.add(contentProvider.getMetadataFilePositionalNode());
            container.add(contentProvider.getMetadataFileRegexpNode());
            container.add(contentProvider.getMetadataFileXmlNode());
            container.add(contentProvider.getMetadataFileLdifNode());
            container.add(contentProvider.getMetadataFileExcelNode());
            container.add(contentProvider.getMetadataGenericSchemaNode());
            container.add(contentProvider.getMetadataLDAPSchemaNode());
            container.add(contentProvider.getMetadataWSDLSchemaNode());
            container.add(contentProvider.getMetadataSalesforceSchemaNode());
            container.add(contentProvider.getMetadataSAPConnectionNode());

            container.add(contentProvider.getMetadataConNode());

        }
        addReferencedProjectNodes(contentProvider, container);
        RepositoryNode node = new RepositoryNode(null, null, null);
        node.getChildren().addAll(container);

        return node;
    }

    private void addReferencedProjectNodes(RepositoryContentProvider contentProvider, List<RepositoryNode> container) {
        if (contentProvider == null || container == null) {
            return;
        }
        // referenced project.
        if (contentProvider.getReferenceProjectNode() != null) {
            List<RepositoryNode> refProjects = contentProvider.getReferenceProjectNode().getChildren();
            if (refProjects != null && !refProjects.isEmpty()) {

                List<RepositoryNode> nodesList = new NoNullList<RepositoryNode>();

                for (RepositoryNode repositoryNode : refProjects) {
                    ProjectRepositoryNode refProject = (ProjectRepositoryNode) repositoryNode;

                    ProjectRepositoryNode newProject = new ProjectRepositoryNode(refProject);

                    List<RepositoryNode> refContainer = new ArrayList<RepositoryNode>();
                    if (repositoryType != null && repositoryType.startsWith("DATABASE")) {
                        refContainer.add(refProject.getMetadataConNode());
                    } else {
                        refContainer.add(refProject.getMetadataFileNode());
                        refContainer.add(refProject.getMetadataFilePositionalNode());
                        refContainer.add(refProject.getMetadataFileRegexpNode());
                        refContainer.add(refProject.getMetadataFileXmlNode());
                        refContainer.add(refProject.getMetadataFileLdifNode());
                        refContainer.add(refProject.getMetadataFileExcelNode());
                        refContainer.add(refProject.getMetadataGenericSchemaNode());
                        refContainer.add(refProject.getMetadataLDAPSchemaNode());
                        refContainer.add(refProject.getMetadataWSDLSchemaNode());
                        refContainer.add(refProject.getMetadataSalesforceSchemaNode());
                        refContainer.add(contentProvider.getMetadataSAPConnectionNode());

                        refContainer.add(refProject.getMetadataConNode());

                    }
                    refContainer.remove(null); // Not allow null element
                    newProject.getChildren().addAll(refContainer);

                    nodesList.add(newProject);
                }
                container.addAll(nodesList);
            }
        }
    }

    /**
     * 
     * DOC YeXiaowei SchemaTypeProcessor class global comment. Detailled comment
     */
    private static class NoNullList<T> extends ArrayList<T> {

        private static final long serialVersionUID = 4564909079208559374L;

        @Override
        public boolean add(T t) {
            if (t == null) {
                return false;
            }
            return super.add(t);
        }

    }

    public boolean isSelectionValid(RepositoryNode node) {
        if (node.getObject() instanceof MetadataTable || node.getObject() instanceof SAPFunctionRepositoryObject) {
            return true;
        }
        return false;
    }

    public ViewerFilter makeFilter() {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {

                RepositoryNode node = (RepositoryNode) element;
                if (node.getObject() != null && (node.getObject() instanceof Query)) {
                    return false;
                }
                // cdc
                ICDCProviderService cdcService = null;
                if (node.getObjectType() == ERepositoryObjectType.METADATA_CON_CDC) {
                    return false;
                }
                if (PluginChecker.isCDCPluginLoaded()) {
                    cdcService = (ICDCProviderService) GlobalServiceRegister.getDefault().getService(ICDCProviderService.class);
                    if (cdcService != null && cdcService.isSubscriberTableNode(node)) {
                        return false;
                    }
                }

                if ("DATABASE:CDC".equals(repositoryType) && (node.getObject() != null)) {
                    if (node.getObject().getType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
                        DatabaseConnectionItem item = (DatabaseConnectionItem) node.getObject().getProperty().getItem();
                        DatabaseConnection connection = (DatabaseConnection) item.getConnection();

                        if (cdcService != null && cdcService.canCreateCDCConnection(connection)) {
                            return true;
                        }
                        return false;
                    }
                    if (node.getObject() instanceof MetadataTable) {
                        return ((MetadataTableRepositoryObject) node.getObject()).getTable().isActivatedCDC();
                    }
                }
                return true;
            }
        };
    }
}

/**
 * xye TypeProcessor for Query. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class SAPFunctionProcessor implements ITypeProcessor {

    String repositoryType;

    /**
     * bqian RepositoryTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public SAPFunctionProcessor(String repositoryType) {
        this.repositoryType = repositoryType;
    }

    public RepositoryNode getInputRoot(RepositoryContentProvider contentProvider) {
        RepositoryNode metadataConNode = contentProvider.getMetadataSAPConnectionNode();
        // referenced project.
        if (contentProvider.getReferenceProjectNode() != null) {
            List<RepositoryNode> refProjects = contentProvider.getReferenceProjectNode().getChildren();
            if (refProjects != null && !refProjects.isEmpty()) {

                List<RepositoryNode> nodesList = new ArrayList<RepositoryNode>();

                for (RepositoryNode repositoryNode : refProjects) {
                    ProjectRepositoryNode refProject = (ProjectRepositoryNode) repositoryNode;

                    ProjectRepositoryNode newProject = new ProjectRepositoryNode(refProject);

                    newProject.getChildren().add(refProject.getMetadataSAPConnectionNode());

                    nodesList.add(newProject);
                }
                metadataConNode.getChildren().addAll(nodesList);
            }
        }
        return metadataConNode;
    }

    public boolean isSelectionValid(RepositoryNode node) {
        if (node.getObject().getType() == ERepositoryObjectType.METADATA_SAP_FUNCTION) {
            return true;
        }
        return false;
    }

    public ViewerFilter makeFilter() {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                RepositoryNode node = (RepositoryNode) element;
                if (node.getObject() != null && (node.getObject() instanceof MetadataTable)) {
                    return false;
                }
                return true;
            }
        };
    }

}

/**
 * bqian TypeProcessor for Query. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class QueryTypeProcessor implements ITypeProcessor {

    String repositoryType;

    /**
     * bqian RepositoryTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public QueryTypeProcessor(String repositoryType) {
        this.repositoryType = repositoryType;
    }

    public RepositoryNode getInputRoot(RepositoryContentProvider contentProvider) {
        RepositoryNode metadataConNode = contentProvider.getMetadataConNode();
        // referenced project.
        if (contentProvider.getReferenceProjectNode() != null) {
            List<RepositoryNode> refProjects = contentProvider.getReferenceProjectNode().getChildren();
            if (refProjects != null && !refProjects.isEmpty()) {

                List<RepositoryNode> nodesList = new ArrayList<RepositoryNode>();

                for (RepositoryNode repositoryNode : refProjects) {
                    ProjectRepositoryNode refProject = (ProjectRepositoryNode) repositoryNode;

                    ProjectRepositoryNode newProject = new ProjectRepositoryNode(refProject);

                    newProject.getChildren().add(refProject.getMetadataConNode());

                    nodesList.add(newProject);
                }
                metadataConNode.getChildren().addAll(nodesList);
            }
        }
        return metadataConNode;
    }

    public boolean isSelectionValid(RepositoryNode node) {
        if (node.getObject() instanceof Query) {
            return true;
        }
        return false;
    }

    public ViewerFilter makeFilter() {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                // if (repositoryType.startsWith("DATABASE") && repositoryType.contains(":")) {
                RepositoryNode node = (RepositoryNode) element;
                if (node.getObject() != null && (node.getObject() instanceof MetadataTable)) {
                    return false;
                }
                return true;
            }
        };
    }

}

/**
 * bqian class global comment. Detailled comment
 */
class ViewerTextFilter extends ViewerFilter {

    private String text = null;

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (text == null || text.equals("")) {
            return true;
        }
        RepositoryNode node = (RepositoryNode) element;
        ERepositoryObjectType type = node.getContentType();
        ENodeType nodeType = node.getType();
        if (nodeType != ENodeType.REPOSITORY_ELEMENT) {
            List<RepositoryNode> children = node.getChildren();
            if (children.isEmpty()) {
                return false;
            }
            for (RepositoryNode child : children) {
                return select(viewer, null, child);
            }

            return true;
        }

        String name = node.getObject().getProperty().getLabel();
        return name.matches(text);
    }
};
