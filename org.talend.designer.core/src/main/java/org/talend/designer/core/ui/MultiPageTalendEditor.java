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
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.process.IProcess;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ISyntaxCheckableEditor;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.TalendJavaEditor;
import org.talend.designer.core.ui.editor.TalendPerlEditor;
import org.talend.designer.core.ui.editor.TalendTabbedPropertySheetPage;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;
import org.talend.designer.core.ui.editor.nodes.NodeLabelEditPart;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.outline.NodeTreeEditPart;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.repository.job.deletion.JobResourceManager;

/**
 * This class is the main editor, the differents pages in it are: <br/><b>1)</b> {@link TalendEditor} <br/><b>2)</b>
 * {@link Text Editor on the generated code} <br/><br/> This class uses the interface ISelectionListener, it allows to
 * propage the Delete evenement to the designer. <br/>
 * 
 * $Id$
 * 
 */
public class MultiPageTalendEditor extends AbstractMultiPageTalendEditor {

    public static final String ID = "org.talend.designer.core.ui.MultiPageTalendEditor"; //$NON-NLS-1$

    private final TalendEditor designerEditor = new TalendEditor();;

    public MultiPageTalendEditor() {
        super();
    }

    public TalendEditor getTalendEditor() {
        return designerEditor;
    }

    /**
     * Creates the pages of the multi-page editor.
     */
    @Override
    protected void createPages() {
        super.createPages();
        setTitleImage(ImageProvider.getImage(ECoreImage.PROCESS_ICON));
    }

    /**
     * The <code>MultiPageEditorExample</code> implementation of this method checks that the input is an instance of
     * <code>IFileEditorInput</code>.
     */
    @Override
    public void init(final IEditorSite site, final IEditorInput editorInput) throws PartInitException {
        if (!(editorInput instanceof IFileEditorInput) && !(editorInput instanceof ProcessEditorInput)) {
            throw new PartInitException(Messages.getString("MultiPageTalendEditor.InvalidInput")); //$NON-NLS-1$
        }
        super.init(site, editorInput);
    }

    @Override
    public boolean isDirty() {
        return propertyIsDirty || super.isDirty();
    }

    /**
     * Calculates the contents of page 2 when the it is activated.
     */
    @Override
    protected void pageChange(final int newPageIndex) {
        super.pageChange(newPageIndex);
        setName();
        if (newPageIndex == 1) {
            if (codeEditor instanceof ISyntaxCheckableEditor) {
                moveCursorToSelectedComponent();

                /*
                 * Belowing method had been called at line 331 within the generateCode method, as soon as code
                 * generated.
                 */
                // ((ISyntaxCheckableEditor) codeEditor).validateSyntax();
            }
            codeSync();
        }
    }

    /**
     * DOC smallet Comment method "setName".
     * 
     * @param label
     */
    public void setName() {
        super.setName();
        String label = getEditorInput().getName();
        // if (getActivePage() == 1) {
        setPartName(Messages.getString("MultiPageTalendEditor.Job", label)); //$NON-NLS-1$
        // } else {
        // setPartName(Messages.getString("other Label??", label));
        // //$NON-NLS-1$
        // }
    }

    /**
     * Move Cursor to Selected Node.
     * 
     * @param processor
     */
    private void moveCursorToSelectedComponent() {
        String nodeName = getSelectedNodeName();
        if (nodeName != null) {
            if (codeEditor instanceof TalendJavaEditor) {
                ((TalendJavaEditor) codeEditor).placeCursorTo(nodeName); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                ((TalendPerlEditor) codeEditor).placeCursorTo(nodeName); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
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
                    node = ((NodeLabel) editPart.getModel()).getNode();
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.editor.INameRefresher#refreshName()
     */
    public void refreshName() {
        try {
            JobResourceManager jobResourceManager = JobResourceManager.getInstance();
            jobResourceManager.removeProtection(designerEditor);
            for (String id : designerEditor.getProtectedIds()) {
                if (designerEditor.getJobResource(id).getJobName().equalsIgnoreCase(oldJobName)) {
                    // delete only the job renamed
                    jobResourceManager.deleteResource(designerEditor.getJobResource(id));
                }
            }
            designerEditor.resetJobResources();

            setName();
            designerEditor.getCurrentJobResource().setJobName(getEditorInput().getName());
            jobResourceManager.addProtection(designerEditor);

            processor.initPath();
            processor.setProcessorStates(IProcessor.STATES_EDIT);

            FileEditorInput input = createFileEditorInput();
            codeEditor.setInput(input);

            IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (activeWorkbenchWindow != null) {
                if (activeWorkbenchWindow.getActivePage().isPartVisible(this)) {
                    new ActiveProcessTracker().partBroughtToTop(this);
                    DesignerPlugin.getDefault().getRunProcessService().refreshView();
                }
            }

        } catch (Exception e) {
            MessageBoxExceptionHandler.process(e);
        }
    }

    /**
     * DOC bqian Comment method "selectNode".
     * 
     * @param node
     */
    public void selectNode(Node node) {
        GraphicalViewer viewer = getTalendEditor().getViewer();
        Object object = viewer.getRootEditPart().getChildren().get(0);
        if (object instanceof ProcessPart) {
            for (EditPart editPart : (List<EditPart>) ((ProcessPart) object).getChildren()) {
                if (editPart instanceof NodePart) {
                    if (((NodePart) editPart).getModel().equals(node)) {
                        viewer.select(editPart);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.AbstractMultiPageTalendEditor#getEditorId()
     */
    @Override
    public String getEditorId() {
        return ID;
    }

}
