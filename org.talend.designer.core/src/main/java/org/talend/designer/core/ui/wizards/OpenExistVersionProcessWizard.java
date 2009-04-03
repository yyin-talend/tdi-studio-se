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
package org.talend.designer.core.ui.wizards;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.expressionbuilder.ExpressionPersistance;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC xye class global comment. Detailled comment
 */
public class OpenExistVersionProcessWizard extends Wizard {

    OpenExistVersionProcessPage mainPage = null;

    private final IRepositoryObject processObject;

    private boolean alreadyLockedByUser = false;

    private String originaleObjectLabel = null;

    private String originalVersion = null;

    public OpenExistVersionProcessWizard(IRepositoryObject processObject) {
        this.processObject = processObject;
        originaleObjectLabel = processObject.getProperty().getLabel();
        originalVersion = processObject.getProperty().getVersion();
        lockObject();
    }

    @Override
    public void addPages() {
        mainPage = new OpenExistVersionProcessPage(alreadyLockedByUser, processObject);
        addPage(mainPage);
        setWindowTitle(Messages.getString("NewProcessWizard.windowTitle")); //$NON-NLS-1$
    }

    /**
     * Returns the currently active page for this workbench window.
     * 
     * @return the active page, or <code>null</code> if none
     */
    public IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }

    @Override
    public boolean performCancel() {
        restoreVersion();
        unlockObject();
        return super.performCancel();
    }

    private void lockObject() {
        IProxyRepositoryFactory repositoryFactory = CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
        try {
            if (repositoryFactory.getStatus(processObject).equals(ERepositoryStatus.LOCK_BY_USER)) {
                alreadyLockedByUser = true;
            } else {
                repositoryFactory.lock(processObject);
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        } catch (BusinessException e) {
            ExceptionHandler.process(e);
        }
    }

    private void unlockObject() {
        IProxyRepositoryFactory repositoryFactory = CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
        try {
            if (repositoryFactory.getStatus(processObject).equals(ERepositoryStatus.LOCK_BY_USER)) {
                repositoryFactory.unlock(processObject);
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        RepositoryManager.refreshCreatedNode(ERepositoryObjectType.PROCESS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (mainPage.isCreateNewVersionJob()) {
            refreshNewJob();
            openJob(processObject.getRepositoryNode(), false);
        } else {
            StructuredSelection selection = (StructuredSelection) mainPage.getSelection();
            RepositoryNode node = (RepositoryNode) selection.getFirstElement();
            // Only latest version can be editted
            openJob(node, !node.getObject().getProperty().getVersion().equals(processObject.getProperty().getVersion()));
        }
        return true;
    }

    private boolean refreshNewJob() {
        if (alreadyLockedByUser) {
            return false;
        }
        IProxyRepositoryFactory repositoryFactory = CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
        try {
            repositoryFactory.save(getProperty(), this.originaleObjectLabel, this.originalVersion);
            ExpressionPersistance.getInstance().jobNameChanged(originaleObjectLabel, processObject.getLabel());
            return true;
        } catch (PersistenceException e) {
            MessageBoxExceptionHandler.process(e);
            return false;
        }
    }

    private void openJob(final RepositoryNode node, final boolean readonly) {
        ProcessItem processItem = (ProcessItem) node.getObject().getProperty().getItem();

        IWorkbenchPage page = getActivePage();

        try {
            ProcessEditorInput fileEditorInput = new ProcessEditorInput(processItem, true, readonly);
            IEditorPart editorPart = page.findEditor(fileEditorInput);

            if (editorPart == null) {
                // fileEditorInput.setView(getViewPart());
                fileEditorInput.setRepositoryNode(node);
                page.openEditor(fileEditorInput, MultiPageTalendEditor.ID, readonly);
            } else {
                page.activate(editorPart);
            }
        } catch (PartInitException e) {
            MessageBoxExceptionHandler.process(e);
        } catch (PersistenceException e) {
            MessageBoxExceptionHandler.process(e);
        }
    }

    private Property getProperty() {
        return processObject.getProperty();
    }

    public String getOriginVersion() {
        return this.originalVersion;
    }

    public void restoreVersion() {
        getProperty().setVersion(getOriginVersion());
    }

}
