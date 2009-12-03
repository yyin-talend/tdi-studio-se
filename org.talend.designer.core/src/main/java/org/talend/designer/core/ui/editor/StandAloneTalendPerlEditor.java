// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.WorkbenchJob;
import org.epic.perleditor.editors.PerlEditor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.FileItem;
import org.talend.core.model.properties.Information;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.ui.ILastVersionChecker;
import org.talend.core.ui.IUIRefresher;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * Stand alone Perl editor.<br/>
 * 
 */
public class StandAloneTalendPerlEditor extends PerlEditor implements IUIRefresher, ILastVersionChecker {

    public static final String ID = "org.talend.designer.core.ui.editor.StandAloneTalendPerlEditor"; //$NON-NLS-1$

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
        return !rEditorInput.isReadOnly() && getRepositoryFactory().getStatus(item).isEditable() && isLastVersion(item);
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
        if (rEditorInput.getRepositoryNode() == null) {
            rEditorInput.setRepositoryNode(null); // retrieve node
        }
        super.doSetInput(rEditorInput);
        try {
            item = (FileItem) rEditorInput.getItem();
            item.getProperty().eAdapters().add(dirtyListener);
            if (!rEditorInput.isReadOnly()) {
                repFactory.lock(item);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        setName();

        RepositoryManager.getRepositoryView().refresh(rEditorInput.getRepositoryNode());

    }

    private void setName() {
        IRepositoryView viewPart = (IRepositoryView) getSite().getPage().findView(IRepositoryView.VIEW_ID);
        ILabelProvider labelProvider = (ILabelProvider) viewPart.getViewer().getLabelProvider();
        setTitleImage(labelProvider.getImage(item.getProperty()));
        setPartName(labelProvider.getText(item.getProperty()));
    }

    @Override
    public void dispose() {
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
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
        RepositoryNode repositoryNode = rEditorInput.getRepositoryNode();
        if (repositoryNode != null) {
            if (repFactory.getStatus(item) == ERepositoryStatus.DELETED) {
                RepositoryManager.refreshDeletedNode(null);
            } else {
                RepositoryManager.refresh(repositoryNode.getObjectType());
            }
        }
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
            byteArray.setInnerContentFromFile(((IFileEditorInput) getEditorInput()).getFile());
            IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
            IProxyRepositoryFactory repFactory = service.getProxyRepositoryFactory();
            repFactory.save(item);

            ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                    ICodeGeneratorService.class);
            if (item instanceof RoutineItem) {
                codeGenService.createPerlRoutineSynchronizer().syncRoutine((RoutineItem) item, false);
            }
            startRefreshJob(repFactory);
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
    }

    private void startRefreshJob(final IProxyRepositoryFactory repFactory) {
        Job refreshJob = new WorkbenchJob("") {//$NON-NLS-1$

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
             */
            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {
                // check syntax error
                addProblems();

                try {
                    // cause it to update MaxInformationLevel
                    repFactory.save(item.getProperty());
                } catch (Exception e) {

                }

                // add dirtyListener
                propertyIsDirty = false;
                EList adapters = item.getProperty().eAdapters();
                // if (!(item instanceof RoutineItem)) {
                adapters.add(dirtyListener);
                firePropertyChange(IEditorPart.PROP_DIRTY);
                // }

                // update image in repository
                RepositoryManager.refreshSavedNode(rEditorInput.getRepositoryNode());

                // update editor image
                setTitleImage(getTitleImage());
                return Status.OK_STATUS;
            }
        };
        refreshJob.setSystem(true);

        refreshJob.schedule(300);
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

    /**
     * add routine compilation errors into problems view.
     */
    private void addProblems() {
        List<Information> informations = Problems.addRoutineFile(rEditorInput.getFile(), item.getProperty());
        item.getProperty().getInformations().clear();
        item.getProperty().getInformations().addAll(informations);
        Problems.refreshProblemTreeView();
    }

    private FileItem item;

    private boolean propertyIsDirty;

    private AdapterImpl dirtyListener = new AdapterImpl() {

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
     * @see org.talend.core.ui.IUIRefresher#refreshName()
     */
    public void refreshName() {
        doSave(null);
        setName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.IUIRefresher#isLastVersion(org.talend.core.model.properties.Item)
     */
    public boolean isLastVersion(Item item) {
        if (item.getProperty() != null) {
            try {
                List<IRepositoryObject> allVersion = ProxyRepositoryFactory.getInstance().getAllVersion(
                        item.getProperty().getId());
                if (allVersion != null && !allVersion.isEmpty()) {
                    if (allVersion.get(allVersion.size() - 1).getVersion().equals(item.getProperty().getVersion())) {
                        return true;
                    }
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }
}
