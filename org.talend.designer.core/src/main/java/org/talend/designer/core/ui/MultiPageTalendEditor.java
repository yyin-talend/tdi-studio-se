// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.core.ui;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.epic.perleditor.PerlEditorPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ISyntaxCheckableEditor;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.CodeEditorFactory;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.TalendJavaEditor;
import org.talend.designer.core.ui.editor.TalendTabbedPropertySheetPage;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;
import org.talend.designer.core.ui.editor.nodes.NodeLabelEditPart;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.outline.NodeTreeEditPart;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * This class is the main editor, the differents pages in it are: <br/><b>1)</b> {@link TalendEditor} <br/><b>2)</b>
 * {@link Text Editor on the generated code} <br/><br/> This class uses the interface ISelectionListener, it allows to
 * propage the Delete evenement to the designer. <br/>
 * 
 * $Id$
 * 
 */
public class MultiPageTalendEditor extends MultiPageEditorPart implements IResourceChangeListener, ISelectionListener {

    private AdapterImpl dirtyListener = new AdapterImpl() {

        public void notifyChanged(Notification notification) {
            if (notification.getEventType() != Notification.REMOVING_ADAPTER) {
                propertyIsDirty = true;
                firePropertyChange(IEditorPart.PROP_DIRTY);
            }
        }
    };

    private boolean propertyIsDirty = false;

    public static final String ID = "org.talend.designer.core.ui.MultiPageTalendEditor"; //$NON-NLS-1$

    private TalendEditor designerEditor = new TalendEditor();;

    private AbstractDecoratedTextEditor codeEditor;

    private boolean alreadyGenerated = false;

    private IProcess process;

    private IProcessor processor;

    public MultiPageTalendEditor() {
        super();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        // MultieditPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(
        // (org.eclipse.jface.util.IPropertyChangeListener)this);
    }

    public void setReadOnly(boolean readonly) {
        designerEditor.setReadOnly(readonly);
    }

    /**
     * Creates page 0 of the multi-page editor, which contains a text editor.
     */
    void createPage0() {
        try {
            int index = addPage(designerEditor, getEditorInput());
            setPageText(index, Messages.getString("MultiPageTalendEditor.Designer")); //$NON-NLS-1$
        } catch (PartInitException e) {
            ErrorDialog.openError(getSite().getShell(), Messages.getString("MultiPageTalendEditor.Designer.Error"), //$NON-NLS-1$
                    null, e.getStatus());
        }
    }

    public TalendEditor getTalendEditor() {
        return designerEditor;
    }

    /**
     * Creates page 1 of the multi-page editor, which allows you to change the font used in page 2.
     */
    void createPage1() {
        codeEditor = CodeEditorFactory.getInstance().getCodeEditor(getCurrentLang());
        process = designerEditor.getProcess();
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
            IFile codeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
                    processor.getCodeProject().getFullPath().append(processor.getCodePath()));
            if (!codeFile.exists()) {
                // Create empty one
                try {
                    codeFile.create(new ByteArrayInputStream("".getBytes()), true, null); //$NON-NLS-1$
                } catch (CoreException e) {
                    // Do nothing.
                }
            }

            int index = addPage(codeEditor, new FileEditorInput(codeFile));

            // init Syntax Validation.
            if (getCurrentLang() == ECodeLanguage.PERL) {
                PerlEditorPlugin.getDefault().setSyntaxValidationPreference(true);
            }

            setPageText(index, Messages.getString("MultiPageTalendEditor.Code")); //$NON-NLS-1$
        } catch (PartInitException pie) {
            ErrorDialog.openError(getSite().getShell(), Messages.getString("MultiPageTalendEditor.Designer.Error"), //$NON-NLS-1$
                    null, pie.getStatus());
        }
    }

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
     * Creates the pages of the multi-page editor.
     */
    protected void createPages() {
        setTitleImage(ImageProvider.getImage(ECoreImage.PROCESS_ICON));
        createPage0();
        createPage1();
    }

    /**
     * The <code>MultiPageEditorPart</code> implementation of this <code>IWorkbenchPart</code> method disposes all
     * nested editors. Subclasses may extend.
     */
    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        if (this.codeEditor instanceof TalendJavaEditor) {
            ((TalendJavaEditor) codeEditor).removeEditorPart(this);
        }

        // MultieditPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(
        // (org.eclipse.jface.util.IPropertyChangeListener) this);
        super.dispose();

        // Unlock the process :
        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
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
    }

    /**
     * Saves the multi-page editor's document.
     */
    public void doSave(final IProgressMonitor monitor) {
        if (!isDirty()) {
            return;
        }
        getTalendEditor().getProperty().eAdapters().remove(dirtyListener);
        getEditor(0).doSave(monitor);
    

        alreadyGenerated = false;
        codeSync();

        propertyIsDirty = false;
        firePropertyChange(IEditorPart.PROP_DIRTY);
        getTalendEditor().getProperty().eAdapters().add(dirtyListener);
    }

    public void codeSync() {
        if (alreadyGenerated) {
            return;
        }
        try {
            processor.generateCode(process.getContextManager().getDefaultContext(), false, false, true);

        } catch (ProcessorException pe) {
            MessageBoxExceptionHandler.process(pe);
        }
        alreadyGenerated = true;
    }

    /**
     * Saves the multi-page editor's document as another file. Also updates the text for page 0's tab, and updates this
     * multi-page editor's input to correspond to the nested editor's.
     */
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

    /**
     * The <code>MultiPageEditorExample</code> implementation of this method checks that the input is an instance of
     * <code>IFileEditorInput</code>.
     */
    public void init(final IEditorSite site, final IEditorInput editorInput) throws PartInitException {
        if (!(editorInput instanceof IFileEditorInput) && !(editorInput instanceof ProcessEditorInput)) {
            throw new PartInitException(Messages.getString("MultiPageTalendEditor.InvalidInput")); //$NON-NLS-1$
        }
        setSite(site);
        setInput(editorInput);
        site.setSelectionProvider(new MultiPageTalendSelectionProvider(this));
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);

        // Lock the process :
        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
        IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
        ProcessEditorInput processEditorInput = (ProcessEditorInput) editorInput;
        Process currentProcess = (processEditorInput).getLoadedProcess();
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

    @Override
    public boolean isDirty() {
        return propertyIsDirty || super.isDirty();
    }

    /*
     * (non-Javadoc) Method declared on IEditorPart.
     */
    public boolean isSaveAsAllowed() {
        return true;
    }

    /**
     * Calculates the contents of page 2 when the it is activated.
     */
    protected void pageChange(final int newPageIndex) {
        super.pageChange(newPageIndex);
        setName();
        if (newPageIndex == 1) {
            if (isDirty()) {
                alreadyGenerated = false;
            }
            codeSync();

            if (codeEditor instanceof ISyntaxCheckableEditor) {
                moveCursorToSelectedComponent(processor);

                /*
                 * Belowing method had been called at line 331 within the generateCode method, as soon as code
                 * generated.
                 */
                // ((ISyntaxCheckableEditor) codeEditor).validateSyntax();
            }
        }
    }

    public void showDesignerPage() {
        setActivePage(0);
    }

    public void showCodePage() {
        setActivePage(1);
    }

    /**
     * DOC smallet Comment method "setName".
     * 
     * @param label
     */
    public void setName() {
        String label = getEditorInput().getName();
        // if (getActivePage() == 1) {
        setPartName(Messages.getString("MultiPageTalendEditor.Job", label)); //$NON-NLS-1$
        // } else {
        // setPartName(Messages.getString("other Label??", label)); //$NON-NLS-1$
        // }
    }

    /**
     * Move Cursor to Selected Node.
     * 
     * @param processor
     */
    private void moveCursorToSelectedComponent(IProcessor processor) {
        String nodeName = getSelectedNodeName();
        int shift = 0;
        if (processor.getProcessorType().equals(ECodeLanguage.JAVA.getName())) {
            shift = 2;
        } else if (processor.getProcessorType().equals(ECodeLanguage.PERL.getName())) {
            shift = 1;
        }

        if (nodeName != null) {
            if (codeEditor instanceof TalendJavaEditor) {
                ((TalendJavaEditor) codeEditor).validateSyntax();
            }
            int lineNumber = processor.getLineNumber("[" + nodeName + " main ] start") - shift; //$NON-NLS-1$ //$NON-NLS-2$
            IDocument document = codeEditor.getDocumentProvider().getDocument(codeEditor.getEditorInput());
            try {
                int start = document.getLineOffset(lineNumber);
                int end = start + document.getLineLength(lineNumber) - shift;
                codeEditor.selectAndReveal(start, end - start);
            } catch (BadLocationException e) {
                codeEditor.selectAndReveal(0, 0);
            }
        }
    }

    /**
     * Get the selected Node if any.
     * 
     * @return the component selected name or null if component is not found or is not activated
     */
    public String getSelectedNodeName() {
        String nodeName = null;
        Node node = getSelectedGraphicNode();
        if (node != null) {
            if (node.isActivate()) {
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
    public Node getSelectedGraphicNode() {
        Node node = null;
        List selections = designerEditor.getViewer().getSelectedEditParts();
        if (selections.size() == 1) {
            Object selection = selections.get(0);

            if (selection instanceof NodeTreeEditPart) {
                NodeTreeEditPart nTreePart = (NodeTreeEditPart) selection;
                node = (Node) nTreePart.getModel();
            } else {
                if (selection instanceof NodePart) {
                    NodePart editPart = (NodePart) selection;
                    node = (Node) editPart.getModel();
                } else if (selection instanceof NodeLabelEditPart) {
                    NodeLabelEditPart editPart = (NodeLabelEditPart) selection;
                    node = (Node) ((NodeLabel) editPart.getModel()).getNode();
                }
            }
        }
        return node;
    }

    public EditPart getOldSelection() {
        IPropertySheetPage propertyPage = (IPropertySheetPage) designerEditor.getAdapter(IPropertySheetPage.class);
        if (propertyPage instanceof TalendTabbedPropertySheetPage) {
            StructuredSelection selections = ((TalendTabbedPropertySheetPage) propertyPage).getOldSelection();
            if (selections != null) {
                Object selection = selections.getFirstElement();
                if (selection instanceof EditPart) {
                    return (EditPart) selection;
                }
            }
        }
        return null;
    }

    /**
     * Closes all project files on project close.
     */

    public void resourceChanged(final IResourceChangeEvent event) {
        if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
                    for (int i = 0; i < pages.length; i++) {
                        if (((FileEditorInput) designerEditor.getEditorInput()).getFile().getProject()
                                .equals(event.getResource())) {
                            IEditorPart editorPart = pages[i].findEditor(designerEditor.getEditorInput());
                            pages[i].closeEditor(editorPart, true);
                        }
                    }
                }
            });
        }
    }

    @Override
    public Object getAdapter(final Class adapter) {
        if (designerEditor.equals(getActiveEditor())) {
            return this.getActiveEditor().getAdapter(adapter);
        }
        /*
         * if (textEditor.equals(getActiveEditor())) { if (adapter == IPropertySheetPage.class) { return null; } return
         * this.getActiveEditor().getAdapter(adapter); }
         */
        return super.getAdapter(adapter);
    }

    /**
     * Will allow to propage the Delete evenement in the designer.
     */
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        if (this.equals(getSite().getPage().getActiveEditor())) {
            if (selection instanceof StructuredSelection) {
                StructuredSelection structSel = (StructuredSelection) selection;
                if (structSel.getFirstElement() instanceof EditPart) {
                    if (designerEditor.equals(getActiveEditor())) {
                        designerEditor.selectionChanged(getActiveEditor(), selection);

                    }
                }
            }
        }
    }

    /**
     * Getter for codeEditor.
     * 
     * @return the codeEditor
     */
    public TalendJavaEditor getCodeEditor() {
        return (TalendJavaEditor) this.codeEditor;
    }

}
