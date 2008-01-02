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
package org.talend.designer.core.ui;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.epic.perleditor.PerlEditorPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.context.UpdateRunJobComponentContextHelper;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.IUIRefresher;
import org.talend.designer.core.ISyntaxCheckableEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.editor.CodeEditorFactory;
import org.talend.designer.core.ui.editor.TalendJavaEditor;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.job.deletion.JobResourceManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC qzhang class global comment. Detailled comment
 */
public abstract class AbstractMultiPageTalendEditor extends MultiPageEditorPart implements IResourceChangeListener,
        ISelectionListener, IUIRefresher {

    protected final AdapterImpl dirtyListener = new AdapterImpl() {

        @Override
        public void notifyChanged(Notification notification) {
            if (notification.getEventType() != Notification.REMOVING_ADAPTER) {
                propertyIsDirty = true;
                getTalendEditor().getProperty().eAdapters().remove(dirtyListener);
                process.updateProperties();
                getTalendEditor().getProperty().eAdapters().add(dirtyListener);
                firePropertyChange(IEditorPart.PROP_DIRTY);
            }
        }
    };

    protected boolean propertyIsDirty = false;

    protected AbstractDecoratedTextEditor codeEditor;

    protected IProcess2 process;

    protected IProcessor processor;

    protected String oldJobName;

    protected boolean keepPropertyLocked; // used only if the user try to open more than one editor at a time.

    protected boolean codeSync = false;

    protected boolean needSetPartListener = true;

    public static final String JOBLET_ID = "org.talend.designer.joblet.multieditor";

    public static final String JOB_ID = "org.talend.designer.joblet.multieditor";

    public AbstractMultiPageTalendEditor() {
        super();

        if (needSetPartListener) {

            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().addPartListener(
                    CorePlugin.getDefault().getDesignerCoreService().getActiveProcessTracker());

            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    try {
                        CorePlugin.getDefault().getCodeGeneratorService().createRoutineSynchronizer().syncAllRoutines();
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            });
            needSetPartListener = false;
        }
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }

    /**
     * DOC qzhang Comment method "getAbstractTalendEditor".
     * 
     * @return
     */
    public abstract AbstractTalendEditor getTalendEditor();

    public void setReadOnly(boolean readonly) {
        getTalendEditor().setReadOnly(readonly);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    @Override
    public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
        setSite(site);
        setInput(editorInput);
        site.setSelectionProvider(new MultiPageTalendSelectionProvider(this));
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);

        // Lock the process :
        IRepositoryService service = CorePlugin.getDefault().getRepositoryService();
        IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
        RepositoryEditorInput processEditorInput = (RepositoryEditorInput) editorInput;
        IProcess2 currentProcess = (processEditorInput).getLoadedProcess();
        if (!currentProcess.isReadOnly()) {
            try {
                processEditorInput.getItem().getProperty().eAdapters().add(dirtyListener);
                repFactory.lock(currentProcess);
            } catch (PersistenceException e) {
                e.printStackTrace();
            } catch (BusinessException e) {
                // Nothing to do
            }
        } else {
            setReadOnly(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.editor.INameRefresher#refreshName()
     */
    public void refreshName() {
        try {
            JobResourceManager jobResourceManager = JobResourceManager.getInstance();
            jobResourceManager.removeProtection(getTalendEditor());
            for (String id : getTalendEditor().getProtectedIds()) {
                if (getTalendEditor().getJobResource(id).getJobName().equalsIgnoreCase(oldJobName)) {
                    // delete only the job renamed
                    jobResourceManager.deleteResource(getTalendEditor().getJobResource(id));
                }
            }
            getTalendEditor().resetJobResources();

            setName();
            getTalendEditor().getCurrentJobResource().setJobName(getEditorInput().getName());
            jobResourceManager.addProtection(getTalendEditor());

            processor.initPath();
            processor.setProcessorStates(IProcessor.STATES_EDIT);

            FileEditorInput input = createFileEditorInput();
            codeEditor.setInput(input);

            IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (activeWorkbenchWindow != null) {
                if (activeWorkbenchWindow.getActivePage().isPartVisible(this)) {
                    CorePlugin.getDefault().getDesignerCoreService().getActiveProcessTracker().partBroughtToTop(this);
                    CorePlugin.getDefault().getRunProcessService().refreshView();
                }
            }

        } catch (Exception e) {
            MessageBoxExceptionHandler.process(e);
        }
    }

    public void setName() {
        String label = getEditorInput().getName();
        oldJobName = label;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
     */
    @Override
    protected void createPages() {
        createPage0();
        createPage1();
    }

    protected void createPage0() {
        try {
            int index = addPage(getTalendEditor(), getEditorInput());
            setPageText(index, "Designer");
            getTalendEditor().setParent(this);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates page 1 of the multi-page editor, which allows you to change the font used in page 2.
     */
    protected void createPage1() {
        codeEditor = CodeEditorFactory.getInstance().getCodeEditor(getCurrentLang());
        process = getTalendEditor().getProcess();
        ((Process) process).setEditor(this);
        processor = ProcessorUtilities.getProcessor(process, process.getContextManager().getDefaultContext());

        process.setProcessor(processor);
        if (processor.getProcessorType().equals("javaProcessor")) { //$NON-NLS-1$
            processor.setProcessorStates(IProcessor.STATES_EDIT);
            if (codeEditor instanceof ISyntaxCheckableEditor) {
                processor.setSyntaxCheckableEditor((ISyntaxCheckableEditor) codeEditor);
            }
        }
        if (codeEditor instanceof TalendJavaEditor) {
            ((TalendJavaEditor) codeEditor).addEditorPart(this);
        }

        try {
            int index = addPage(codeEditor, createFileEditorInput());

            // init Syntax Validation.
            if (getCurrentLang() == ECodeLanguage.PERL) {
                PerlEditorPlugin.getDefault().setSyntaxValidationPreference(true);
            }

            setPageText(index, "Code");
        } catch (PartInitException pie) {
            pie.printStackTrace();
        }
        if (process.getGeneratingNodes().size() != 0) {
            Job job = new Job("Generating code") {

                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    ProcessorUtilities.generateCode(process, process.getContextManager().getDefaultContext(), false, false, true,
                            ProcessorUtilities.GENERATE_WITH_FIRST_CHILD);

                    return Status.OK_STATUS;
                }
            };
            job.setUser(true);
            job.setPriority(Job.BUILD);
            job.schedule(); // start as soon as possible
            codeSync = true;
        }

        CommandStackEventListener commandStackEventListener = new CommandStackEventListener() {

            public void stackChanged(CommandStackEvent event) {
                codeSync = false;
            }
        };
        CommandStack commandStack = (CommandStack) getTalendEditor().getAdapter(CommandStack.class);
        commandStack.addCommandStackEventListener(commandStackEventListener);

    }

    /**
     * Creates page 1 of the multi-page editor, which allows you to change the font used in page 2.
     */

    /**
     * get the current project generating code language.
     * 
     * @return the current generating code language
     */
    private ECodeLanguage getCurrentLang() {
        return ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject()
                .getLanguage();
    }

    /**
     * Saves the multi-page editor's document.
     */
    @Override
    public void doSave(final IProgressMonitor monitor) {
        if (!isDirty()) {
            return;
        }
        updateRunJobContext();
        getTalendEditor().getProperty().eAdapters().remove(dirtyListener);
        getEditor(0).doSave(monitor);
        getTalendEditor().getProperty().eAdapters().add(dirtyListener);
        codeSync();

        propertyIsDirty = false;
        firePropertyChange(IEditorPart.PROP_DIRTY);

    }

    protected void updateRunJobContext() {
        JobContextManager manager = (JobContextManager) getProcess().getContextManager();
        Map<String, String> nameMap = manager.getNameMap();
        if (manager.isModified()) {
            try {
                IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();

                Set<String> curContextVars = getCurrentContextVariables(manager);
                String jobName = getProcess().getLabel();

                UpdateRunJobComponentContextHelper.updateItemRunJobComponentReference(factory, nameMap, jobName, curContextVars);

                IEditorReference[] reference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getEditorReferences();
                List<IProcess> processes = CorePlugin.getDefault().getDesignerCoreService().getOpenedProcess(reference);

                UpdateRunJobComponentContextHelper.updateOpenedJobRunJobComponentReference(processes, nameMap, jobName,
                        curContextVars);
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
            nameMap.clear();
            manager.setModified(false);
        }
    }

    private Set<String> getCurrentContextVariables(IContextManager manager) {
        Set<String> varNameSet = new HashSet<String>();
        if (manager != null) {
            for (IContextParameter param : manager.getDefaultContext().getContextParameterList()) {
                varNameSet.add(param.getName());
            }
        }
        return varNameSet;
    }

    public void codeSync() {
        if (!codeSync && process.getGeneratingNodes().size() != 0) {
            try {
                processor.generateCode(false, false, true);

            } catch (ProcessorException pe) {
                MessageBoxExceptionHandler.process(pe);
            }
            codeSync = true;
        }
    }

    /**
     * Saves the multi-page editor's document as another file. Also updates the text for page 0's tab, and updates this
     * multi-page editor's input to correspond to the nested editor's.
     */
    @Override
    public void doSaveAs() {
        IEditorPart editor = getEditor(0);
        editor.doSaveAs();
        setPageText(0, editor.getTitle());
        setInput(editor.getEditorInput());
    }

    /*
     * (non-Javadoc) Method declared on IEditorPart
     */
    public void gotoMarker(final IMarker marker) {
        setActivePage(0);
    }

    /*
     * (non-Javadoc) Method declared on IEditorPart.
     */
    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    public void showDesignerPage() {
        setActivePage(0);
    }

    public void showCodePage() {
        setActivePage(1);
    }

    /**
     * Move Cursor to Selected Node.
     * 
     * @param processor
     */

    /**
     * Get the selected Node if any.
     * 
     * @return the component selected name or null if component is not found or is not activated
     */
    public String getSelectedNodeName() {
        String nodeName = null;
        Node node = getSelectedGraphicNode();
        if (node != null) {
            if (node.isActivate() || node.isDummy()) {
                nodeName = node.getUniqueName();
            } else {
                nodeName = null;

            }
            if (node.getComponent().getMultipleComponentManager() != null) {
                nodeName += "_" + node.getComponent().getMultipleComponentManager().getInput().getName(); //$NON-NLS-1$
            }
        }
        return nodeName;
    }

    /**
     * DOC amaumont Comment method "getSelectedNode".
     * 
     * @return
     */
    public abstract Node getSelectedGraphicNode();

    /**
     * Closes all project files on project close.
     */

    public void resourceChanged(final IResourceChangeEvent event) {
        if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
                    for (int i = 0; i < pages.length; i++) {
                        if (((FileEditorInput) getTalendEditor().getEditorInput()).getFile().getProject().equals(
                                event.getResource())) {
                            IEditorPart editorPart = pages[i].findEditor(getTalendEditor().getEditorInput());
                            pages[i].closeEditor(editorPart, true);
                        }
                    }
                }
            });
        }
    }

    @Override
    public Object getAdapter(final Class adapter) {
        if (getTalendEditor().equals(getActiveEditor())) {
            return this.getActiveEditor().getAdapter(adapter);
        }
        /*
         * if (textEditor.equals(getActiveEditor())) { if (adapter == IPropertySheetPage.class) { return null; } return
         * this.getActiveEditor().getAdapter(adapter); }
         */
        return super.getAdapter(adapter);
    }

    /**
     * Will allow to propagate the Delete evenement in the designer.
     */
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        if (this.equals(getSite().getPage().getActiveEditor())) {
            if (selection instanceof StructuredSelection) {
                StructuredSelection structSel = (StructuredSelection) selection;
                if (structSel.getFirstElement() instanceof EditPart) {
                    if (getTalendEditor().equals(getActiveEditor())) {
                        getTalendEditor().selectionChanged(getActiveEditor(), selection);

                    }
                }
            }
        }
    }

    protected FileEditorInput createFileEditorInput() {

        IPath codePath = processor.getCodePath();

        if (codePath.isEmpty()) {
            // reinitialize the processor if there was any problem during the initialization.
            // (should not happen)
            try {
                processor.initPath();
            } catch (ProcessorException e) {
                MessageBoxExceptionHandler.process(e);
            }
            codePath = processor.getCodePath();
        }

        IFile codeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
                processor.getCodeProject().getFullPath().append(codePath));
        if (!codeFile.exists()) {
            // Create empty one
            try {
                codeFile.create(new ByteArrayInputStream("".getBytes()), true, null); //$NON-NLS-1$
            } catch (CoreException e) {
                // Do nothing.
            }
        }
        return new FileEditorInput(codeFile);
    }

    /**
     * Getter for process.
     * 
     * @return the process
     */
    public IProcess getProcess() {
        return this.process;
    }

    public void updateChildrens() {
        // just call the method add protection will update new childrens and
        // keep old ones (keep to delete automatically
        // when closing job)
        JobResourceManager jobResourceManager = JobResourceManager.getInstance();
        jobResourceManager.addProtection(getTalendEditor());
    }

    /**
     * DOC bqian Comment method "selectNode".
     * 
     * @param node
     */
    public void selectNode(INode node) {
        GraphicalViewer viewer = getTalendEditor().getViewer();
        Object object = viewer.getRootEditPart().getChildren().get(0);
        if (object instanceof ProcessPart) {
            for (EditPart editPart : (List<EditPart>) ((ProcessPart) object).getChildren()) {
                if (editPart instanceof NodePart) {
                    if (((INodeEditPart) editPart).getModel().equals(node)) {
                        viewer.select(editPart);
                    }
                }
            }
        }
    }

    public boolean isJobAlreadyOpened() {
        return foundExistEditor(this.getEditorInput());
    }

    private boolean foundExistEditor(final IEditorInput editorInput) {
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow != null) {

            WorkbenchPage page = (WorkbenchPage) activeWorkbenchWindow.getActivePage();
            if (page != null) {
                int i = 0;
                if (editorInput instanceof RepositoryEditorInput) {
                    RepositoryEditorInput curEditorInput = (RepositoryEditorInput) editorInput;

                    IEditorReference[] ref = page.findEditors(curEditorInput, getEditorId(), IWorkbenchPage.MATCH_INPUT);
                    boolean exist = ref.length > 1;
                    if (exist) {
                        // MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        // "New Editor Error!", " It's not possible to open another editor of current job. ");
                        IEditorPart activePart = page.getActiveEditor();
                        // activePart.removePropertyListener(listener);
                        // page.getEditorPresentation().closeEditor(activePart);

                    }
                    return exist;
                }
            }

        }
        return false;
    }

    /**
     * DOC qzhang Comment method "getEditorId".
     * 
     * @return
     */
    public abstract String getEditorId();

    /**
     * Getter for keepPropertyLocked.
     * 
     * @return the keepPropertyLocked
     */
    public boolean isKeepPropertyLocked() {
        return this.keepPropertyLocked;
    }

    /**
     * Sets the keepPropertyLocked.
     * 
     * @param keepPropertyLocked the keepPropertyLocked to set
     */
    public void setKeepPropertyLocked(boolean keepPropertyLocked) {
        this.keepPropertyLocked = keepPropertyLocked;
    }

    /**
     * Getter for codeEditor.
     * 
     * @return the codeEditor
     */
    public TalendJavaEditor getCodeEditor() {
        return (TalendJavaEditor) this.codeEditor;
    }

    /**
     * The <code>MultiPageEditorPart</code> implementation of this <code>IWorkbenchPart</code> method disposes all
     * nested editors. Subclasses may extend.
     */
    @Override
    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        if (this.codeEditor instanceof TalendJavaEditor) {
            ((TalendJavaEditor) codeEditor).removeEditorPart(this);
        }

        // MultieditPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(
        // (org.eclipse.jface.util.IPropertyChangeListener) this);
        super.dispose();

        if (isKeepPropertyLocked()) {
            return;
        }

        // Unlock the process :
        IRepositoryService service = CorePlugin.getDefault().getRepositoryService();
        IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
        try {
            getTalendEditor().getProperty().eAdapters().remove(dirtyListener);
            Property property = repFactory.reload(getTalendEditor().getProperty());
            getTalendEditor().setProperty(property);
            repFactory.unlock(property.getItem());
        } catch (PersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(IRepositoryView.VIEW_ID);
        if (viewPart != null) {
            viewPart.refresh();
        }
        if (process != null) {
            ((Process) process).setEditor(null);
        }
    }
}
