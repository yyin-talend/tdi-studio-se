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
package org.talend.designer.core.ui.editor;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.corext.refactoring.rename.JavaRenameProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.JavaRenameRefactoring;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameCompilationUnitProcessor;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.RefactoringStatusEntry;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.ui.IUIRefresher;
import org.talend.designer.core.DesignerPlugin;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * Stand alone Perl editor.<br/>
 * 
 */
public class StandAloneTalendJavaEditor extends CompilationUnitEditor implements IUIRefresher {

    public static final String ID = "org.talend.designer.core.ui.editor.StandAloneTalendJavaEditor";
    private RepositoryEditorInput rEditorInput;

    /**
     * DOC smallet Comment method "getRepositoryFactory".
     */
    private IProxyRepositoryFactory getRepositoryFactory() {
        return DesignerPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
    }

    @Override
    public boolean isSaveAsAllowed() {
        return getRepositoryFactory().getStatus(item).isEditable();
    }

    @Override
    public boolean isEditable() {
        return !rEditorInput.isReadOnly() && getRepositoryFactory().getStatus(item).isEditable();
    }

    @Override
    public void doSetInput(IEditorInput input) throws CoreException {
        // Lock the process :
        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
        IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();

        if (input instanceof RepositoryEditorInput) {
            rEditorInput = (RepositoryEditorInput) input;
        } else {
            FileEditorInput fileInput = (FileEditorInput) input;
            rEditorInput = new RepositoryEditorInput(fileInput.getFile(), rEditorInput.getItem());
        }
        try {
            // see bug 1321
            item = (RoutineItem) rEditorInput.getItem();
            item.getProperty().eAdapters().add(dirtyListener);
            if (!rEditorInput.isReadOnly()) {
                repFactory.lock(item);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        rEditorInput.getFile().refreshLocal(IResource.DEPTH_ONE, null);
        super.doSetInput(rEditorInput);
        setName();
    }

    private void setName() {
        IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(IRepositoryView.VIEW_ID);
        ILabelProvider labelProvider = (ILabelProvider) viewPart.getViewer().getLabelProvider();
        setTitleImage(labelProvider.getImage(item.getProperty()));
        setPartName(labelProvider.getText(item.getProperty()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#getTitleImage()
     */
    @Override
    public Image getTitleImage() {
        if (item != null) {
            IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(IRepositoryView.VIEW_ID);
            ILabelProvider labelProvider = (ILabelProvider) viewPart.getViewer().getLabelProvider();
            return labelProvider.getImage(item.getProperty());
        }
        return super.getTitleImage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#getPartName()
     */
    @Override
    public String getPartName() {
        if (item != null) {
            IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(IRepositoryView.VIEW_ID);
            ILabelProvider labelProvider = (ILabelProvider) viewPart.getViewer().getLabelProvider();
            return labelProvider.getText(item.getProperty());
        }
        return super.getPartName();
    }

    @Override
    public void dispose() {
        // remove the Runtines .java file in the .Java Project.
        // try {
        // rEditorInput.getFile().delete(true, null);
        // } catch (CoreException e1) {
        // RuntimeExceptionHandler.process(e1);
        // }
        super.dispose();
        // Unlock the process :
        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
        IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
        try {
            item.getProperty().eAdapters().remove(dirtyListener);
            Property property = repFactory.reload(item.getProperty());
            item = (RoutineItem) property.getItem();
            repFactory.unlock(item);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(IRepositoryView.VIEW_ID);
        viewPart.refresh();
    }

    @Override
    public boolean isDirty() {
        return propertyIsDirty || super.isDirty();
    }

    @Override
    protected void editorSaved() {

    }

    @Override
    public void doSave(final IProgressMonitor monitor) {
        EList adapters = item.getProperty().eAdapters();
        adapters.remove(dirtyListener);
        super.doSave(monitor);

        try {
            ByteArray byteArray = item.getContent();
            byteArray.setInnerContentFromFile(((FileEditorInput) getEditorInput()).getFile());
            IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
            IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
            repFactory.save(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        propertyIsDirty = false;
        adapters.add(dirtyListener);
        firePropertyChange(IEditorPart.PROP_DIRTY);
        IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(IRepositoryView.VIEW_ID);
        viewPart.refresh();
    }

    private RoutineItem item;

    private boolean propertyIsDirty;

    private final AdapterImpl dirtyListener = new AdapterImpl() {

        @Override
        public void notifyChanged(Notification notification) {
            if (notification.getEventType() != Notification.REMOVING_ADAPTER) {
                propertyIsDirty = true;
                firePropertyChange(IEditorPart.PROP_DIRTY);
            }
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.javaeditor.JavaEditor#getCorrespondingElement(org.eclipse.jdt.core.IJavaElement)
     */
    @Override
    protected IJavaElement getCorrespondingElement(IJavaElement element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.internal.ui.javaeditor.JavaEditor#getElementAt(int)
     */
    @Override
    protected IJavaElement getElementAt(int offset) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.IUIRefresher#refreshName()
     */
    public void refreshName() {

        ICompilationUnit unit = (ICompilationUnit) this.getInputJavaElement();
        String newName = item.getProperty().getLabel();
        propertyIsDirty = false;
        try {
            JavaRenameProcessor processor = new RenameCompilationUnitProcessor(unit);
            processor.setNewElementName(newName + SuffixConstants.SUFFIX_STRING_java);
            RenameRefactoring ref = new JavaRenameRefactoring(processor);
            final PerformRefactoringOperation operation = new PerformRefactoringOperation(ref,
                    CheckConditionsOperation.ALL_CONDITIONS);

            IRunnableWithProgress r = new IRunnableWithProgress() {

                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            try {
                                operation.run(monitor);
                            } catch (CoreException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    });

                }
            };

            PlatformUI.getWorkbench().getProgressService().run(true, true, r);
            RefactoringStatus conditionStatus = operation.getConditionStatus();
            if (conditionStatus.hasError()) {
                String errorMessage = "Rename " + unit.getElementName() + " to " + newName + " has errors!";
                RefactoringStatusEntry[] entries = conditionStatus.getEntries();
                for (int i = 0; i < entries.length; i++) {
                    RefactoringStatusEntry entry = entries[i];
                    errorMessage += "\n>>>" + entry.getMessage();
                }
                MessageDialog.openError(this.getSite().getShell(), "Warning", errorMessage);
            } else {
                doSave(null);
            }
            setName();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }
}
