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
package org.talend.repository.ui.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PartInitException;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IESBService;
import org.talend.core.PluginChecker;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.MetadataColumnRepositoryObject;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.Query;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.HeaderFooterConnection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.param.ERepositoryCategoryType;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.HeaderFooterConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.core.ui.ICDCProviderService;
import org.talend.repository.ProjectManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.SAPFunctionRepositoryObject;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryContentProvider;
import org.talend.repository.ui.views.RepositoryLabelProvider;
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

    private String[] repositoryTypes;

    protected FakeRepositoryView repositoryView;

    public FakeRepositoryView getRepositoryView() {
        return this.repositoryView;
    }

    private RepositoryNode result;

    ITypeProcessor typeProcessor;

    private String selectedNodeName;

    private boolean hidenTypeSelection;

    private boolean isHeaderButton;

    private ViewerFilter[] additionalFilters;

    private DatabaseTypeFilter dbSupportFilter;

    ViewerTextFilter textFilter = new ViewerTextFilter();

    private boolean needInitialize = true;

    private List<String> jobIDList;

    private IRepositoryView view;

    protected RepositoryReviewDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(SWT.SHELL_TRIM | SWT.APPLICATION_MODAL | getDefaultOrientation());

        boolean debugMode = CommonsPlugin.isDebugMode();
        // debugMode = true;
        TimeMeasure.display = debugMode;
        TimeMeasure.displaySteps = debugMode;
        TimeMeasure.measureActive = debugMode;

        TimeMeasure.begin("RepositoryReviewDialog"); //$NON-NLS-1$
    }

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
        this(parentShell);
        this.type = type;
        /*
         * avoid select self repository node for Process Type.
         * 
         * borrow the repositoryType to set the current process id here.
         */
        this.repositoryType = repositoryType;
        typeProcessor = createTypeProcessor();
    }

    public RepositoryReviewDialog(Shell parentShell, String[] repositoryTypes) {
        this(parentShell);
        this.repositoryTypes = repositoryTypes;
        typeProcessor = createTypeProcessor();
    }

    public RepositoryReviewDialog(Shell parentShell, ERepositoryObjectType type, String repositoryType, String[] itemFilter) {
        this(parentShell, type, repositoryType);
        this.dbSupportFilter = new DatabaseTypeFilter(itemFilter);
    }

    /**
     * DOC ycbai RepositoryReviewDialog constructor comment.
     * 
     * @param parentShell
     * @param type
     * @param repositoryType
     * @param additionalFilter
     */
    public RepositoryReviewDialog(Shell parentShell, ERepositoryObjectType type, String repositoryType,
            ViewerFilter[] additionalFilters) {
        this(parentShell, type, repositoryType);
        this.additionalFilters = additionalFilters;
    }

    public RepositoryReviewDialog(Shell parentShell, ERepositoryObjectType type, Boolean isHeaderButton, String repositoryType) {
        this(parentShell, type, repositoryType);

        this.isHeaderButton = isHeaderButton;
        // setHeaderButton(isHeaderButton);
        if (typeProcessor instanceof RepositoryTypeProcessor) {
            ((RepositoryTypeProcessor) typeProcessor).setHeaderButton(isHeaderButton);
        }
    }

    public RepositoryReviewDialog(Shell parentShell, ERepositoryObjectType type, String repositoryType,
            boolean hidenTypeSelection, boolean needInitialize) {
        this(parentShell, type, repositoryType);

        this.needInitialize = needInitialize;
        this.hidenTypeSelection = hidenTypeSelection;
        if (hidenTypeSelection && (typeProcessor instanceof RepositoryTypeProcessor)) {
            ((RepositoryTypeProcessor) typeProcessor).setHidenTypeSelection(hidenTypeSelection);
        }

    }

    public RepositoryReviewDialog(Shell parentShell, ERepositoryObjectType type) {
        this(parentShell, type, null);
    }

    public RepositoryReviewDialog(Shell parentShell, ITypeProcessor typeProcessor, ERepositoryObjectType type) {
        this(parentShell, type);
        this.typeProcessor = typeProcessor;
    }

    protected IRepositoryView getView() {
        if (view == null) {
            view = RepositoryView.show();
        }
        return view;
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

        if (type == ERepositoryObjectType.CONTEXT) {
            return new ContextTypeProcessor(repositoryType);
        }
        if (type == ERepositoryObjectType.METADATA_HEADER_FOOTER) {
            return new HeaderFooterTypeProcessor(repositoryType);
        }

        if (type == ERepositoryObjectType.METADATA_VALIDATION_RULES) {
            return new ValidationRuleTypeProcessor(repositoryType);
        }

        if (repositoryTypes != null) {
            return new MetadataMultiTypeProcessor(repositoryTypes);
        }

        throw new IllegalArgumentException(Messages.getString("RepositoryReviewDialog.0", type)); //$NON-NLS-1$
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
        if (typeProcessor.getDialogTitle() == null) {
            shell.setText(Messages.getString("RepositoryReviewDialog.repositoryContent")); //$NON-NLS-1$
        } else {
            shell.setText(typeProcessor.getDialogTitle());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        TimeMeasure.step("RepositoryReviewDialog", "before createDialogArea..."); //$NON-NLS-1$  //$NON-NLS-2$
        GridData data = (GridData) container.getLayoutData();
        data.minimumHeight = 400;
        data.heightHint = 400;
        data.minimumWidth = 500;
        data.widthHint = 500;
        container.setLayoutData(data);

        createFilterField(container);

        Composite viewContainer = new Composite(container, SWT.BORDER);
        viewContainer.setLayout(new GridLayout());
        viewContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

        TimeMeasure.step("RepositoryReviewDialog", "show RepositoryView"); //$NON-NLS-1$  //$NON-NLS-2$
        repositoryView = new FakeRepositoryView(typeProcessor, type, jobIDList);
        try {
            repositoryView.init(getView().getViewSite());
            TimeMeasure.step("RepositoryReviewDialog", "init for FakeRepositoryView"); //$NON-NLS-1$  //$NON-NLS-2$
        } catch (PartInitException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }

        repositoryView.createPartControl(viewContainer);
        TimeMeasure.step("RepositoryReviewDialog", "createPartControl for FakeRepositoryView"); //$NON-NLS-1$  //$NON-NLS-2$
        repositoryView.addFilter(textFilter);
        if (dbSupportFilter != null) {
            repositoryView.addFilter(dbSupportFilter);
        }
        if (additionalFilters != null) {
            repositoryView.addFilter(additionalFilters);
        }
        // no need refresh.
        // refreshView();

        // see feature 0003664: tRunJob: When opening the tree dialog to select the job target, it could be useful to
        // open it on previous selected job if exists
        if (selectedNodeName != null) {
            repositoryView.selectNode((RepositoryNode) repositoryView.getViewer().getInput(), selectedNodeName);
            TimeMeasure.step("RepositoryReviewDialog", "selectNode for FakeRepositoryView"); //$NON-NLS-1$  //$NON-NLS-2$
        }

        repositoryView.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                boolean highlightOKButton = isSelectionValid(event);
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

        TimeMeasure.step("RepositoryReviewDialog", "finished createDialogArea..."); //$NON-NLS-1$  //$NON-NLS-2$
        TimeMeasure.end("RepositoryReviewDialog"); //$NON-NLS-1$
        TimeMeasure.display = false;
        TimeMeasure.displaySteps = false;
        TimeMeasure.measureActive = false;

        return container;
    }

    protected boolean isSelectionValid(SelectionChangedEvent event) {
        boolean highlightOKButton = true;
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        if (selection == null || selection.size() != 1) {
            highlightOKButton = false;
        } else {
            RepositoryNode node = (RepositoryNode) selection.getFirstElement();
            ERepositoryObjectType t = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            List<String> idList = this.getJobIDList();
            if (idList != null && t == ERepositoryObjectType.PROCESS) {
                if (idList.contains(node.getObject().getId())) {
                    highlightOKButton = false;
                    MessageDialog.openWarning(getParentShell(), "warning", "This Operation is used in other job!");
                    return highlightOKButton;
                }

            }

            if (node.getType() != ENodeType.REPOSITORY_ELEMENT) {
                highlightOKButton = false;
            }
            // else if (t == ERepositoryObjectType.SERVICESOPERATION) {
            // return highlightOKButton;
            // }
            else if (!typeProcessor.isSelectionValid(node)) {
                highlightOKButton = false;
            }
        }
        return highlightOKButton;
    }

    /**
     * DOC bqian Comment method "createFilterField".
     * 
     * @param container
     */
    private void createFilterField(Composite container) {

        if (type != ERepositoryObjectType.PROCESS) {
            return;
        }

        // create text filter
        Label label = new Label(container, SWT.NONE);
        label.setText(Messages.getString("RepositoryReviewDialog.jobNameFormat")); //$NON-NLS-1$
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Text text = new Text(container, SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String pattern = text.getText();
                pattern = pattern.replace("*", ".*"); //$NON-NLS-1$ //$NON-NLS-2$
                pattern = pattern.replace("?", "."); //$NON-NLS-1$ //$NON-NLS-2$
                pattern = "(?i)" + pattern + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
                textFilter.setText(pattern);
                repositoryView.refresh();
                repositoryView.selectFirstOne();
            }
        });
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

    protected void refreshView() {
        TimeMeasure.step("RepositoryReviewDialog", "before refresh FakeRepositoryView"); //$NON-NLS-1$  //$NON-NLS-2$
        ProjectRepositoryNode.refProjectBool = false;
        repositoryView.refresh(needInitialize);
        TimeMeasure.step("RepositoryReviewDialog", "refresh FakeRepositoryView"); //$NON-NLS-1$  //$NON-NLS-2$
        getView().refresh();
        TimeMeasure.step("RepositoryReviewDialog", "refresh RepositoryView"); //$NON-NLS-1$  //$NON-NLS-2$
        ProjectRepositoryNode.refProjectBool = true;
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
        repositoryView.dispose();
    }

    public RepositoryNode getResult() {
        return result;
    }

    public List<String> getJobIDList() {
        return this.jobIDList;
    }

    public void setJobIDList(List<String> jobIDList) {
        this.jobIDList = jobIDList;
    }

}

/**
 * 
 * DOC talend class global comment. Detailled comment
 */
class MetadataMultiTypeProcessor implements ITypeProcessor {

    String[] repositoryTypes;

    public MetadataMultiTypeProcessor(String[] repositoryTypes) {
        this.repositoryTypes = repositoryTypes;
    }

    public boolean isSelectionValid(RepositoryNode node) {
        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_FILE_XML
                || node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_MDMCONNECTION) {
            return true;
        }
        return false;

    }

    public RepositoryNode getInputRoot(RepositoryContentProvider contentProvider) {
        List<IRepositoryNode> refProjects = null;
        RepositoryNode referenceProjectNode = contentProvider.getRootRepositoryNode(ERepositoryObjectType.REFERENCED_PROJECTS);
        if (referenceProjectNode != null) {
            refProjects = referenceProjectNode.getChildren();
        } else {
            refProjects = Collections.EMPTY_LIST;
        }
        List<RepositoryNode> container = new NoNullList<RepositoryNode>();
        container.addAll(getMetadataNode(contentProvider.getRoot()));
        getReferencedInputRoot(container, refProjects);

        RepositoryNode node = new RepositoryNode(null, null, null);
        node.getChildren().addAll(container);

        return node;
    }

    private void getReferencedInputRoot(List<RepositoryNode> container, List<IRepositoryNode> refProjects) {
        if (!refProjects.isEmpty()) {
            for (IRepositoryNode repositoryNode : refProjects) {
                List<RepositoryNode> refContainer = new NoNullList<RepositoryNode>();
                ProjectRepositoryNode refProject = (ProjectRepositoryNode) repositoryNode;
                ProjectRepositoryNode newProject = new ProjectRepositoryNode(refProject);
                refContainer.addAll(getMetadataNode(refProject));
                RepositoryNode referenceProjectNode = refProject.getRootRepositoryNode(ERepositoryObjectType.REFERENCED_PROJECTS);
                if (referenceProjectNode != null && !referenceProjectNode.getChildren().isEmpty()) {
                    getReferencedInputRoot(refContainer, referenceProjectNode.getChildren());
                }
                newProject.getChildren().addAll(refContainer);
                container.add(newProject);
            }
        }
    }

    /**
     * 
     * DOC need add other types when needed
     * 
     * @param projectNode
     * @return
     */
    private List<RepositoryNode> getMetadataNode(ProjectRepositoryNode projectNode) {
        List<RepositoryNode> nodes = new ArrayList<RepositoryNode>();
        if (repositoryTypes != null && repositoryTypes.length != 0) {
            for (int i = 0; i < repositoryTypes.length; i++) {
                if (ERepositoryCategoryType.XML.getName().equals(repositoryTypes[i])) {
                    nodes.add(projectNode.getMetadataFileXmlNode());
                } else if (ERepositoryCategoryType.MDM.getName().equals(repositoryTypes[i])) {
                    nodes.add(projectNode.getMetadataMDMConnectionNode());
                }
            }
        }
        return nodes;
    }

    public ViewerFilter makeFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDialogTitle() {
        // TODO Auto-generated method stub
        return "Metadatas";
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

    ITypeProcessor typeProcessor;

    RepositoryNode input;

    private List<String> jobIDList = null;

    // private IRepositoryView repositoryview;

    /**
     * DOC bqian SnippetsDialogTrayView constructor comment.
     * 
     * @param typeProcessor
     * 
     * @param type
     * @param type
     */
    public FakeRepositoryView(ITypeProcessor typeProcessor, ERepositoryObjectType type, List<String> jobIDList) {
        super();
        this.typeProcessor = typeProcessor;
        this.type = type;
        this.jobIDList = jobIDList;
        // this.repositoryview = RepositoryView.show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.RepositoryView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        super.setFromFake(false);
        TimeMeasure.step("RepositoryReviewDialog", "before createPartControl in FakeRepositoryView"); //$NON-NLS-1$  //$NON-NLS-2$
        super.createPartControl(parent);
        TimeMeasure.step("RepositoryReviewDialog", "end parent createPartControl in FakeRepositoryView"); //$NON-NLS-1$  //$NON-NLS-2$
        ViewerFilter filter = typeProcessor.makeFilter();
        addFilter(filter);
        CorePlugin.getDefault().getRepositoryService().removeRepositoryChangedListener(this);
    }

    @Override
    protected void setContentProviderForView() {
        viewer.setContentProvider(new RepositoryContentProvider(this) {

            @Override
            public Object[] getElements(Object parent) {
                if (parent.equals(getView().getViewSite())) {
                    return new RepositoryNode[0];
                }
                return getChildren(parent);
            }

        });
    }

    @Override
    protected void setLabelProviderForView() {
        viewer.setLabelProvider(new RepositoryLabelProvider(this) {

            public Color getForeground(Object element) {
                if (jobIDList != null && !jobIDList.isEmpty()) {
                    RepositoryNode node = (RepositoryNode) element;
                    if (node.getObject() != null && jobIDList.contains(node.getObject().getId())) {
                        return INACTIVE_ENTRY_COLOR;
                    }
                }
                return super.getForeground(element);
            }
        });

    }

    protected void setupInput() {
        TimeMeasure.step("RepositoryReviewDialog", "before set input"); //$NON-NLS-1$  //$NON-NLS-2$     
        viewer.setInput(getInput());
        TimeMeasure.step("RepositoryReviewDialog", "finished set input"); //$NON-NLS-1$  //$NON-NLS-2$        
    }

    public void addFilters() {
        super.addFilters();
        TimeMeasure.step("RepositoryReviewDialog", "finished addFilters"); //$NON-NLS-1$  //$NON-NLS-2$    
    }

    protected void expandCollapseAll() {
        // nothing to do here
    }

    protected void createTreeTooltip(Tree tree) {
        // no need for fake repository
    }

    protected void expandFirstLevel() {
        // no need for fake repository
        if (type == ERepositoryObjectType.PROCESS) {
            getViewer().expandAll();
        }
    }

    public void addFilter(ViewerFilter filter) {
        if (filter != null) {
            getViewer().addFilter(filter);
        }
    }

    public void addFilter(ViewerFilter[] filters) {
        if (filters != null) {
            for (ViewerFilter filter : filters) {
                addFilter(filter);
            }
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
            for (IRepositoryNode child : root.getChildren()) {
                selectNode((RepositoryNode) child, label);
            }
        }
    }

    public void printItem(TreeItem[] items) {
        for (TreeItem treeItem : items) {
            Object o = treeItem.getData();

            getViewer().setExpandedState(o, true);

            printItem(treeItem.getItems());
        }
    }

    private TreeItem getFirstMatchingItem(TreeItem[] items) {
        for (int i = 0; i < items.length; i++) {
            RepositoryNode node = (RepositoryNode) items[i].getData();
            ENodeType nodeType = node.getType();
            if (nodeType == ENodeType.REPOSITORY_ELEMENT) {
                return items[i];
            }
            getViewer().setExpandedState(node, true);

            TreeItem item = getFirstMatchingItem(items[i].getItems());
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    public void selectFirstOne() {
        TreeItem item = getFirstMatchingItem(getViewer().getTree().getItems());

        if (item != null) {
            getViewer().getTree().setSelection(new TreeItem[] { item });
            ISelection sel = getViewer().getSelection();
            getViewer().setSelection(sel, true);
        }

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
        getViewer().expandAll();
    }

    @Override
    public void refresh(boolean needInitialize) {
        super.refresh(needInitialize);
        // getViewer().setInput(this.getViewSite());
        getViewer().setInput(getInput());
        getViewer().expandAll();
    }

    private RepositoryNode getInput() {
        if (input == null) {
            // getViewer().expandAll();

            // RepositoryContentProvider contentProvider = (RepositoryContentProvider) this.repositoryview.getViewer()
            // .getContentProvider();
            RepositoryContentProvider contentProvider = (RepositoryContentProvider) getViewer().getContentProvider();
            input = typeProcessor.getInputRoot(contentProvider);
        }
        return input;
    }

    @Override
    protected void makeActions() {
    }

    @Override
    protected void hookContextMenu() {
    }

    @Override
    protected void contributeToActionBars() {
        // not used
    }

    @Override
    protected void initDragAndDrop() {
    }

    @Override
    protected void hookDoubleClickAction() {
    }

    @Override
    public void createActionComposite(Composite parent) {
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

    String getDialogTitle();
}

/**
 * 
 * ggu class global comment. Detailled comment
 */
abstract class SingleTypeProcessor implements ITypeProcessor {

    private String repositoryType;

    public SingleTypeProcessor(String repositoryType) {
        super();
        this.repositoryType = repositoryType;
    }

    protected String getRepositoryType() {
        return repositoryType;
    }

    protected abstract ERepositoryObjectType getType();

    public RepositoryNode getInputRoot(RepositoryContentProvider contentProvider) {
        TimeMeasure.step("RepositoryReviewDialog", "before getInputRoot, in SingleTypeProcessor"); //$NON-NLS-1$  //$NON-NLS-2$
        RepositoryNode contextNode = contentProvider.getRootRepositoryNode(getType());
        TimeMeasure.step("RepositoryReviewDialog", "finished main project, in SingleTypeProcessor"); //$NON-NLS-1$  //$NON-NLS-2$
        // referenced project.
        addSubReferencedProjectNodes(contextNode, contentProvider.getRoot());
        TimeMeasure.step("RepositoryReviewDialog", "finished ref-projects, in SingleTypeProcessor"); //$NON-NLS-1$  //$NON-NLS-2$
        return contextNode;
    }

    protected void addSubReferencedProjectNodes(RepositoryNode contextNode, ProjectRepositoryNode parentRefProject) {

        RepositoryNode referenceProjectNode = parentRefProject.getRootRepositoryNode(ERepositoryObjectType.REFERENCED_PROJECTS);
        if (referenceProjectNode != null) {
            if (!referenceProjectNode.isInitialized()) {
                if (referenceProjectNode.getParent() instanceof ProjectRepositoryNode) {
                    ((ProjectRepositoryNode) referenceProjectNode.getParent()).initializeChildren(referenceProjectNode);
                }
                referenceProjectNode.setInitialized(true);
            }

            List<IRepositoryNode> refProjects = referenceProjectNode.getChildren();
            if (refProjects != null && !refProjects.isEmpty()) {
                List<IRepositoryNode> nodesList = new ArrayList<IRepositoryNode>();

                for (IRepositoryNode repositoryNode : refProjects) {
                    ProjectRepositoryNode refProject = (ProjectRepositoryNode) repositoryNode;
                    ProjectRepositoryNode newProject = new ProjectRepositoryNode(refProject);

                    RepositoryNode rootRepositoryNode = refProject.getRootRepositoryNode(getType());
                    if (rootRepositoryNode != null) {
                        newProject.getChildren().add(rootRepositoryNode);
                    }
                    nodesList.add(newProject);

                    // sub ref-project
                    addSubReferencedProjectNodes(rootRepositoryNode, newProject);
                }
                contextNode.getChildren().addAll(nodesList);
            }
        }
    }

    public boolean isSelectionValid(RepositoryNode node) {
        if (node.getObjectType() == getType()) {
            return true;
        }
        return false;
    }

    public ViewerFilter makeFilter() {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                RepositoryNode node = (RepositoryNode) element;
                if (node.getContentType() == getType()) {
                    return false;
                }
                return true;
            }
        };
    }

    public String getDialogTitle() {
        return null;
    }
}

/**
 * bqian TypeProcessor for Job. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class JobTypeProcessor extends SingleTypeProcessor {

    /**
     * ggu JobTypeProcessor constructor comment.
     */
    public JobTypeProcessor(String curJobId) {
        super(curJobId);
    }

    @Override
    protected ERepositoryObjectType getType() {
        return ERepositoryObjectType.PROCESS;
    }

    public boolean isSelectionValid(RepositoryNode node) {
        if (node.getProperties(EProperties.CONTENT_TYPE) == getType()) {
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
                if (getRepositoryType() != null && node.getObject() != null) {
                    if (node.getObject().getId() == null || node.getObject().getId().equals(getRepositoryType())) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.dialog.ITypeProcessor#getDialogTitle()
     */
    public String getDialogTitle() {
        return Messages.getString("OpenJobSelectionDialog.findJob"); //$NON-NLS-1$
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

    boolean hidenTypeSelection;

    boolean isHeaderButton;

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

        RepositoryNode referenceProjectNode = contentProvider.getRootRepositoryNode(ERepositoryObjectType.REFERENCED_PROJECTS);
        ;
        addSubRefProjects(metadataNode, referenceProjectNode);

    }

    private void addSubRefProjectNodes(ProjectRepositoryNode subRefProject, RepositoryNode metadataNode) {
        if (subRefProject.getReferenceProjectNode() == null)
            return;
        RepositoryNode referenceProjectNode = subRefProject.getRootRepositoryNode(ERepositoryObjectType.REFERENCED_PROJECTS);

        addSubRefProjects(metadataNode, referenceProjectNode);
    }

    protected void addSubRefProjects(RepositoryNode metadataNode, RepositoryNode referenceProjectNode) {
        if (referenceProjectNode != null) {
            List<RepositoryNode> nodesList = new ArrayList<RepositoryNode>();
            if (!referenceProjectNode.isInitialized()) {
                if (referenceProjectNode.getParent() instanceof ProjectRepositoryNode) {
                    ((ProjectRepositoryNode) referenceProjectNode.getParent()).initializeChildren(referenceProjectNode);
                }
                referenceProjectNode.setInitialized(true);

            }
            List<IRepositoryNode> refProjects = referenceProjectNode.getChildren();
            if (refProjects != null && !refProjects.isEmpty()) {
                for (IRepositoryNode repositoryNode : refProjects) {
                    ProjectRepositoryNode refProject = (ProjectRepositoryNode) repositoryNode;
                    ProjectRepositoryNode newProject = new ProjectRepositoryNode(refProject);

                    RepositoryNode refMetadataNode = getMetadataNode(refProject);

                    if (refMetadataNode != null) {
                        newProject.getChildren().add(refMetadataNode);
                        nodesList.add(newProject);

                        addSubRefProjectNodes(refProject, refMetadataNode);
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
                    metadataNode = ((RepositoryContentProvider) provider).getRootRepositoryNode(ERepositoryObjectType.METADATA);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider).getRootRepositoryNode(ERepositoryObjectType.METADATA);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.DELIMITED.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_DELIMITED);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_DELIMITED);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.POSITIONAL.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_POSITIONAL);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_POSITIONAL);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.REGEX.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_REGEXP);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_REGEXP);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.XML.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_XML);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_XML);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.LDIF.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_LDIF);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_LDIF);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.EXCEL.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_EXCEL);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_EXCEL);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.GENERIC.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_GENERIC_SCHEMA);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_GENERIC_SCHEMA);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.LDAP.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_LDAP_SCHEMA);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_LDAP_SCHEMA);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.WSDL.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_WSDL_SCHEMA);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_WSDL_SCHEMA);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.SALESFORCE.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA);
                }
            }

            if (repositoryType.startsWith(ERepositoryCategoryType.DATABASE.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_CONNECTIONS);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_CONNECTIONS);
                }
            }

            if (repositoryType.startsWith(ERepositoryCategoryType.SAP.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_SAPCONNECTIONS);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_SAPCONNECTIONS);
                }
            }
            if (repositoryType.startsWith(ERepositoryCategoryType.HEADERFOOTER.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_HEADER_FOOTER);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_HEADER_FOOTER);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.EBCDIC.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_EBCDIC);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_EBCDIC);
                }
            }

            if (repositoryType.equals("SERVICES:OPERATION")) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
                    IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
                    ERepositoryObjectType servicesType = service.getServicesType();
                    if (provider instanceof RepositoryContentProvider) {
                        metadataNode = ((RepositoryContentProvider) provider).getRootRepositoryNode(servicesType);
                    }
                    if (provider instanceof ProjectRepositoryNode) {
                        metadataNode = ((ProjectRepositoryNode) provider).getRootRepositoryNode(servicesType);
                    }
                }

            }

            if (repositoryType.equals(ERepositoryCategoryType.MDM.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_MDMCONNECTION);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_MDMCONNECTION);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.FTP.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_FTP);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_FTP);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.BRMS.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_BRMS);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_BRMS);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.HL7.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_HL7);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_HL7);
                }
            }
            // added by hyWang
            if (repositoryType.equals(ERepositoryCategoryType.RULE.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_RULES);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_RULES);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.VALIDATIONRULES.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_VALIDATION_RULES);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_VALIDATION_RULES);
                }
            }
            if (repositoryType.equals(ERepositoryCategoryType.EDIFACT.getName())) {
                if (provider instanceof RepositoryContentProvider) {
                    metadataNode = ((RepositoryContentProvider) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_EDIFACT);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    metadataNode = ((ProjectRepositoryNode) provider)
                            .getRootRepositoryNode(ERepositoryObjectType.METADATA_EDIFACT);
                }
            }
        }
        return metadataNode;
    }

    public boolean isSelectionValid(RepositoryNode node) {
        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.PROCESS) {
            return true;
        }
        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.SERVICESPORT) {
            return false;
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
            IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
            ERepositoryObjectType servicesType = service.getServicesType();
            if (node.getProperties(EProperties.CONTENT_TYPE) == servicesType) {
                return false;
            }
        }

        return true;
    }

    public boolean isHidenTypeSelection() {
        return this.hidenTypeSelection;
    }

    public void setHidenTypeSelection(boolean hidenTypeSelection) {
        this.hidenTypeSelection = hidenTypeSelection;
    }

    public boolean isHeaderButton() {
        return this.isHeaderButton;
    }

    public void setHeaderButton(boolean isHeaderButton) {
        this.isHeaderButton = isHeaderButton;
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

                if (repositoryType.startsWith(ERepositoryCategoryType.DATABASE.getName())) {
                    if (item instanceof ConnectionItem) {
                        ConnectionItem connectionItem = (ConnectionItem) item;
                        Connection connection = connectionItem.getConnection();
                        String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, "TYPE", null); //$NON-NLS-1$
                        if (repositoryType.contains(":")) { // database //$NON-NLS-1$
                            // is
                            // specified
                            // //$NON-NLS-1$
                            String neededDbType = repositoryType.substring(repositoryType.indexOf(":") + 1); //$NON-NLS-1$
                            if (hidenTypeSelection) {
                                return true;
                            }
                            if (!MetadataTalendType.sameDBProductType(neededDbType, currentDbType)) {
                                return false;
                            }
                        }
                    }
                }

                if (repositoryType.equals("SERVICES:OPERATION")) {
                    if (item instanceof ConnectionItem) {
                        ConnectionItem connectionItem = (ConnectionItem) item;
                        Connection connection = connectionItem.getConnection();
                        String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, "TYPE", null); //$NON-NLS-1$
                        if (repositoryType.contains(":")) { // database //$NON-NLS-1$
                            // is
                            // specified
                            // //$NON-NLS-1$
                            String neededDbType = repositoryType.substring(repositoryType.indexOf(":") + 1); //$NON-NLS-1$
                            if (hidenTypeSelection) {
                                return true;
                            }
                            // if (node.getObjectType() == ERepositoryObjectType.SERVICESPORT) {
                            // return false;
                            // }
                            // if (node.getObjectType() == ERepositoryObjectType.SERVICESOPERATION) {
                            // return false;
                            // }
                            // if (!MetadataTalendType.sameDBProductType(neededDbType, currentDbType)) {
                            // return false;
                            // }
                        }
                    }
                }

                if (repositoryType.startsWith(ERepositoryCategoryType.HEADERFOOTER.getName())) {
                    if (item instanceof HeaderFooterConnectionItem) {
                        HeaderFooterConnectionItem connectionItem = (HeaderFooterConnectionItem) item;
                        HeaderFooterConnection connection = (HeaderFooterConnection) connectionItem.getConnection();
                        boolean isHeader = connection.isIsHeader();

                        if ((isHeader && isHeaderButton) || (!isHeader && !isHeaderButton)) {
                            return true;
                        } else {
                            return false;
                        }

                    }
                }
                return true;
            }
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.dialog.ITypeProcessor#getDialogTitle()
     */
    public String getDialogTitle() {
        // TODO Auto-generated method stub
        return null;
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

    private List<ERepositoryObjectType> needCheckedTypes() {
        List<ERepositoryObjectType> list = new ArrayList<ERepositoryObjectType>(50);
        if (repositoryType != null && repositoryType.startsWith(ERepositoryCategoryType.DATABASE.getName())) {
            list.add(ERepositoryObjectType.METADATA_CONNECTIONS);
        } else {
            list.add(ERepositoryObjectType.METADATA_FILE_DELIMITED);
            list.add(ERepositoryObjectType.METADATA_FILE_POSITIONAL);
            list.add(ERepositoryObjectType.METADATA_FILE_REGEXP);
            list.add(ERepositoryObjectType.METADATA_FILE_XML);
            list.add(ERepositoryObjectType.METADATA_FILE_LDIF);
            list.add(ERepositoryObjectType.METADATA_FILE_EXCEL);
            list.add(ERepositoryObjectType.METADATA_GENERIC_SCHEMA);
            list.add(ERepositoryObjectType.METADATA_LDAP_SCHEMA);
            list.add(ERepositoryObjectType.METADATA_WSDL_SCHEMA);
            list.add(ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA);
            list.add(ERepositoryObjectType.METADATA_SAPCONNECTIONS);
            list.add(ERepositoryObjectType.METADATA_FILE_HL7);
            list.add(ERepositoryObjectType.METADATA_FILE_EBCDIC);
            list.add(ERepositoryObjectType.METADATA_RULES_MANAGEMENT);
            list.add(ERepositoryObjectType.METADATA_MDMCONNECTION);
            list.add(ERepositoryObjectType.METADATA_FILE_FTP);
            list.add(ERepositoryObjectType.METADATA_EDIFACT);
            list.add(ERepositoryObjectType.METADATA_CONNECTIONS);
            list.add(ERepositoryObjectType.METADATA_FILE_BRMS);
        }
        return list;
    }

    public RepositoryNode getInputRoot(RepositoryContentProvider contentProvider) {
        List<RepositoryNode> container = new NoNullList<RepositoryNode>();

        for (ERepositoryObjectType type : needCheckedTypes()) {
            RepositoryNode rootRepositoryNode = contentProvider.getRootRepositoryNode(type);
            if (rootRepositoryNode != null) {
                container.add(rootRepositoryNode);
            }
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

        RepositoryNode refRepositoryNode = contentProvider.getRootRepositoryNode(ERepositoryObjectType.REFERENCED_PROJECTS);
        if (refRepositoryNode != null) {
            if (!refRepositoryNode.isInitialized()) {
                if (refRepositoryNode.getParent() instanceof ProjectRepositoryNode) {
                    ((ProjectRepositoryNode) refRepositoryNode.getParent()).initializeChildren(refRepositoryNode);
                }
                refRepositoryNode.setInitialized(true);
            }
            List<IRepositoryNode> refProjects = refRepositoryNode.getChildren();

            addSubReferenceProjects(container, refProjects);
        }
    }

    private void addSubReferencedProjectNodes(ProjectRepositoryNode subRefProject, List<RepositoryNode> container) {
        RepositoryNode refRepositoryNode = subRefProject.getRootRepositoryNode(ERepositoryObjectType.REFERENCED_PROJECTS);
        if (refRepositoryNode == null)
            return;
        if (!refRepositoryNode.isInitialized()) {
            if (refRepositoryNode.getParent() instanceof ProjectRepositoryNode) {
                ((ProjectRepositoryNode) refRepositoryNode.getParent()).initializeChildren(refRepositoryNode);
            }
            refRepositoryNode.setInitialized(true);
        }
        List<IRepositoryNode> refProjects = refRepositoryNode.getChildren();

        addSubReferenceProjects(container, refProjects);
    }

    protected void addSubReferenceProjects(List<RepositoryNode> container, List<IRepositoryNode> refProjects) {
        if (refProjects != null && !refProjects.isEmpty()) {
            List<RepositoryNode> nodesList = new NoNullList<RepositoryNode>();
            for (IRepositoryNode repositoryNode : refProjects) {
                ProjectRepositoryNode refProject = (ProjectRepositoryNode) repositoryNode;

                ProjectRepositoryNode newProject = new ProjectRepositoryNode(refProject);

                List<RepositoryNode> refContainer = new ArrayList<RepositoryNode>();
                for (ERepositoryObjectType type : needCheckedTypes()) {
                    RepositoryNode rootRepositoryNode = refProject.getRootRepositoryNode(type);
                    if (rootRepositoryNode != null) {
                        refContainer.add(rootRepositoryNode);
                    }
                }

                newProject.getChildren().addAll(refContainer);
                nodesList.add(newProject);

                addSubReferencedProjectNodes(refProject, refContainer);
            }
            container.addAll(nodesList);
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
                if (node == null)
                    return false;
                IRepositoryViewObject object = node.getObject();
                if (object != null) {
                    // query
                    if (object instanceof Query) {
                        return false;
                    }
                    // column
                    if (object instanceof MetadataColumnRepositoryObject) {
                        return false;
                    }
                }
                // hide the column folder
                if (object == null && node.getParent() != null && node.getParent().getObject() != null
                        && node.getParent().getObjectType() == ERepositoryObjectType.METADATA_CON_TABLE) {
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

                if (ERepositoryCategoryType.CDC.getName().equals(repositoryType) && (object != null)) { //$NON-NLS-1$
                    if (object.getRepositoryObjectType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
                        DatabaseConnectionItem item = (DatabaseConnectionItem) object.getProperty().getItem();
                        DatabaseConnection connection = (DatabaseConnection) item.getConnection();

                        if (cdcService != null && cdcService.canCreateCDCConnection(connection)) {
                            return true;
                        }
                        return false;
                    }
                    if (object instanceof MetadataTable) {
                        return ((MetadataTableRepositoryObject) object).getTable().isActivatedCDC();
                    }
                }
                return true;
            }
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.dialog.ITypeProcessor#getDialogTitle()
     */
    public String getDialogTitle() {
        // TODO Auto-generated method stub
        return null;
    }
}

/**
 * 
 * DOC talend class global comment. Detailled comment
 * 
 * @param <T> on null object
 */
class NoNullList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 4564909079208559374L;

    @Override
    public boolean add(T t) {
        if (t == null) {
            return false;
        }
        return super.add(t);
    }

}

/**
 * xye TypeProcessor for Query. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class SAPFunctionProcessor extends SingleTypeProcessor {

    /**
     * bqian RepositoryTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public SAPFunctionProcessor(String repositoryType) {
        super(repositoryType);
    }

    @Override
    protected ERepositoryObjectType getType() {
        return ERepositoryObjectType.METADATA_SAPCONNECTIONS;
    }

    public boolean isSelectionValid(RepositoryNode node) {
        if (node.getObject().getRepositoryObjectType() == ERepositoryObjectType.METADATA_SAP_FUNCTION) {
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

// //

/**
 * DOC zli class global comment. Detailled comment
 */
class HeaderFooterTypeProcessor extends SingleTypeProcessor {

    /**
     * DOC zli HeaderFooterTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public HeaderFooterTypeProcessor(String repositoryType) {
        super(repositoryType);
    }

    @Override
    protected ERepositoryObjectType getType() {
        return ERepositoryObjectType.METADATA_HEADER_FOOTER;
    }

}

// ////////////
// //

/**
 * xye class global comment. Detailled comment
 */
class ContextTypeProcessor extends SingleTypeProcessor {

    /**
     * xye RepositoryTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public ContextTypeProcessor(String repositoryType) {
        super(repositoryType);
    }

    @Override
    protected ERepositoryObjectType getType() {
        return ERepositoryObjectType.CONTEXT;
    }

}

/**
 * bqian TypeProcessor for Query. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class QueryTypeProcessor extends SingleTypeProcessor {

    /**
     * bqian RepositoryTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public QueryTypeProcessor(String repositoryType) {
        super(repositoryType);
    }

    @Override
    protected ERepositoryObjectType getType() {
        return ERepositoryObjectType.METADATA_CONNECTIONS;
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
 * DOC ycbai class global comment. Detailled comment
 */
class ValidationRuleTypeProcessor extends SingleTypeProcessor {

    /**
     * DOC ycbai ValidationRuleTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public ValidationRuleTypeProcessor(String repositoryType) {
        super(repositoryType);
    }

    @Override
    protected ERepositoryObjectType getType() {
        return ERepositoryObjectType.METADATA_VALIDATION_RULES;
    }

    public ViewerFilter makeFilter() {
        return new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                RepositoryNode node = (RepositoryNode) element;
                if (node.getContentType() == ERepositoryObjectType.REFERENCED_PROJECTS) {
                    return true;
                }
                ProjectManager pManager = ProjectManager.getInstance();
                if (!pManager.isInCurrentMainProject(node)) {
                    if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER) {
                        return false;
                    }
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
                if (node.getObjectType() == getType()) {
                    return true;
                }
                return false;
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
        if (text == null || text.equals("")) { //$NON-NLS-1$
            return true;
        }
        RepositoryNode node = (RepositoryNode) element;
        ERepositoryObjectType type = node.getContentType();
        ENodeType nodeType = node.getType();
        if (nodeType != ENodeType.REPOSITORY_ELEMENT) {
            List<IRepositoryNode> children = node.getChildren();
            if (children.isEmpty()) {
                return false;
            }
            for (IRepositoryNode child : children) {
                if (select(viewer, null, child)) {
                    return true;
                }
            }

            return false;
        }

        String name = node.getObject().getProperty().getLabel();
        return name.matches(text);
    }
}

/**
 * wchen class global comment. Detailled comment
 */
class DatabaseTypeFilter extends ViewerFilter {

    private String[] filterItems;

    public DatabaseTypeFilter(String[] filterItems) {
        this.filterItems = filterItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        return isSupportNode((RepositoryNode) element, filterItems);
    }

    private boolean isSupportNode(IRepositoryNode node, String[] items) {
        if (filterItems == null) {
            return true;
        }
        List<String> asList = Arrays.asList(items);
        if (node.getObject() == null) {
            if (node.getType() == ENodeType.REFERENCED_PROJECT || node.getType() == ENodeType.SYSTEM_FOLDER) {
                return true;
            }
        } else {
            Item item = node.getObject().getProperty().getItem();
            if (item instanceof DatabaseConnectionItem) {
                DatabaseConnectionItem connItem = (DatabaseConnectionItem) item;
                DatabaseConnection connection = (DatabaseConnection) connItem.getConnection();
                if (connection != null) {
                    String databaseType = connection.getDatabaseType();
                    if (databaseType.equals(EDatabaseTypeName.ORACLEFORSID.getDisplayName())) {
                        databaseType = EDatabaseTypeName.ORACLEFORSID.getXmlName();
                    } else if (databaseType.equals(EDatabaseTypeName.ORACLESN.getDisplayName())) {
                        databaseType = EDatabaseTypeName.ORACLESN.getXmlName();
                    } else if (databaseType.equals(EDatabaseTypeName.ORACLE_OCI.getDisplayName())) {
                        databaseType = EDatabaseTypeName.ORACLE_OCI.getXmlName();
                    } else if (databaseType.equals(EDatabaseTypeName.MSSQL.getDisplayName())) {
                        databaseType = EDatabaseTypeName.MSSQL.getXmlName(); // for component
                    } else {
                        databaseType = EDatabaseTypeName.getTypeFromDbType(databaseType).getProduct();
                    }

                    if (asList.contains(databaseType)) {
                        return true;
                    }
                }
            } else if (item instanceof FolderItem) {
                return true;
            }
        }
        return false;
    }

}
