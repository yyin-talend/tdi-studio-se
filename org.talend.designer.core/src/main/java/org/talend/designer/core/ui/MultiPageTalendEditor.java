// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
import org.epic.perleditor.PerlEditorPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.TalendPerlEditor;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;
import org.talend.designer.core.ui.editor.nodes.NodeLabelEditPart;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.outline.NodeTreeEditPart;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.runprocess.IPerlProcessor;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.designer.runprocess.ProcessorException;
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

    private TalendPerlEditor perlEditor;

    public MultiPageTalendEditor() {
        super();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        // MultieditPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(
        // (org.eclipse.jface.util.IPropertyChangeListener)this);
    }

    /**
     * Creates page 0 of the multi-page editor, which contains a text editor.
     */
    void createPage0() {
        try {
            int index = addPage(designerEditor, getEditorInput());
            setPageText(index, Messages.getString("MultiPageTalendEditor.2")); //$NON-NLS-1$
        } catch (PartInitException e) {
            ErrorDialog.openError(getSite().getShell(), Messages.getString("MultiPageTalendEditor.3"), //$NON-NLS-1$
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
        perlEditor = new TalendPerlEditor();
        IProcess process = designerEditor.getProcess();
        IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
        IPerlProcessor plProcessor = service.createPerlProcessor(process, false);
        try {
            plProcessor.initPaths(process.getContextManager().getDefaultContext());
            IFile codeFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
                    plProcessor.getPerlProject().getFullPath().append(plProcessor.getCodePath()));
            if (!codeFile.exists()) {
                // Create empty one
                try {
                    codeFile.create(new ByteArrayInputStream("".getBytes()), true, null);
                } catch (CoreException e) {
                    // Do nothing.
                }
            }

            int index = addPage(perlEditor, new FileEditorInput(codeFile));

            // init Syntax Validation.
            PerlEditorPlugin.getDefault().setSyntaxValidationPreference(true);

            setPageText(index, Messages.getString("MultiPageTalendEditor.4")); //$NON-NLS-1$
        } catch (PartInitException pie) {
            ErrorDialog.openError(getSite().getShell(), Messages.getString("MultiPageTalendEditor.3"), //$NON-NLS-1$
                    null, pie.getStatus());
        } catch (ProcessorException pe) {
            ErrorDialog.openError(getSite().getShell(), Messages.getString("MultiPageTalendEditor.3"), //$NON-NLS-1$
                    pe.getMessage(), null);
        }
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
        getTalendEditor().getProperty().eAdapters().remove(dirtyListener);
        getEditor(0).doSave(monitor);
        getTalendEditor().getProperty().eAdapters().add(dirtyListener);
        propertyIsDirty = false;
        firePropertyChange(IEditorPart.PROP_DIRTY);

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
            throw new PartInitException(Messages.getString("MultiPageTalendEditor.6")); //$NON-NLS-1$
        }
        setSite(site);
        setInput(editorInput);
        site.setSelectionProvider(new MultiPageTalendSelectionProvider(this));
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);

        // Lock the process :
        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
        IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
        ProcessEditorInput processEditorInput = (ProcessEditorInput) editorInput;
        Process process = (processEditorInput).getLoadedProcess();
        if (!process.isReadOnly()) {
            try {
                processEditorInput.getItem().getProperty().eAdapters().add(dirtyListener);
                repFactory.lock(process);
            } catch (PersistenceException e) {
                e.printStackTrace();
            } catch (BusinessException e) {
                // Nothing to do
            }
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
            IProcess process = designerEditor.getProcess();

            IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
            IPerlProcessor plProcessor = service.createPerlProcessor(process, false);

            try {
                // plProcessor.generateCode(process.getContextManager().getDefaultContext(), false, false, true,
                // true);//Old
                plProcessor.generateCode(process.getContextManager().getDefaultContext(), false, false, true);

            } catch (ProcessorException pe) {
                MessageBoxExceptionHandler.process(pe);
                // ErrorDialog.openError(getSite().getShell(), Messages.getString("MultiPageTalendEditor.3"),
                // //$NON-NLS-1$
                // pe.getMessage(), null);
            }

            moveCursorToSelectedComponent(plProcessor);

            perlEditor.revalidateSyntax();
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
        if (getActivePage() == 1) {
            setPartName(Messages.getString("MultiPageTalendEditor.0", label)); //$NON-NLS-1$
        } else {
            setPartName(Messages.getString("MultiPageTalendEditor.1", label)); //$NON-NLS-1$
        }
    }

    /**
     * Move Cursor to Selected Node.
     * 
     * @param plProcessor
     */
    private void moveCursorToSelectedComponent(IPerlProcessor plProcessor) {
        String nodeName = getSelectedNode();

        if (nodeName.compareTo("") != 0) {
            int lineNumber = plProcessor.getLineNumber(nodeName) - 1;
            IDocument document = perlEditor.getDocumentProvider().getDocument(perlEditor.getEditorInput());
            try {
                int start = document.getLineOffset(lineNumber);
                int end = start + document.getLineLength(lineNumber) - 1;
                perlEditor.selectAndReveal(start, end - start);
            } catch (BadLocationException e) {
                perlEditor.selectAndReveal(0, 0);
            }
        }
    }

    /**
     * Get the selected Node if any.
     * 
     * @return the component selected name or "" if component is not found or is not activated
     */
    private String getSelectedNode() {
        Node node = null;
        String nodeName = "";
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
        if (node != null) {
            if (node.isActivate()) {
                nodeName = node.getUniqueName();
            } else {
                nodeName = "";
            }
            if (node.getComponent().getMultipleComponentManager() != null) {
                nodeName += "_" + node.getComponent().getMultipleComponentManager().getInput().getName();
            }
        }
        return nodeName;
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

}
