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
package org.talend.repository;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.DisableLanguageActions;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.plugin.integration.BindingActions;
import org.talend.repository.plugin.integration.SwitchProjectAction;
import org.talend.repository.ui.login.LoginDialog;
import org.talend.repository.ui.utils.ColumnNameValidator;
import org.talend.repository.ui.views.RepositoryView;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaWizard;
import org.talend.repository.utils.RepositoryPathProvider;

;

/**
 * DOC qian class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public class RepositoryService implements IRepositoryService {

    private GenericSchemaWizard genericSchemaWizard = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getComponentsFactory()
     */
    public IComponentsFactory getComponentsFactory() {
        return ComponentsFactoryProvider.getInstance();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getPathFileName(java.lang.String, java.lang.String)
     */
    public IPath getPathFileName(String folderName, String fileName) {
        return RepositoryPathProvider.getPathFileName(folderName, fileName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getProxyRepositoryFactory()
     */
    public IProxyRepositoryFactory getProxyRepositoryFactory() {
        return ProxyRepositoryFactory.getInstance();
    }

    public IPath getRepositoryPath(RepositoryNode node) {
        return RepositoryNodeUtilities.getPath(node);
    }

    ChangeProcessor changeProcessor = new ChangeProcessor();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#registerRepositoryChangedListener(org.talend.repository.IRepositoryChangedListener)
     */
    public void registerRepositoryChangedListener(IRepositoryChangedListener listener) {
        changeProcessor.addRepositoryChangedListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#registerRepositoryChangedListenerAsFirst(org.talend.repository.IRepositoryChangedListener)
     */
    public void registerRepositoryChangedListenerAsFirst(IRepositoryChangedListener listener) {
        changeProcessor.registerRepositoryChangedListenerAsFirst(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#removeRepositoryChangedListener(org.talend.repository.IRepositoryChangedListener)
     */
    public void removeRepositoryChangedListener(IRepositoryChangedListener listener) {
        changeProcessor.removeRepositoryChangedListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#repositoryChanged(org.talend.repository.RepositoryElementDelta)
     */
    public void repositoryChanged(RepositoryElementDelta delta) {
        changeProcessor.repositoryChanged(delta);
    }

    // This method is used for the Action in RepositoryView to synchronize the sqlBuilder.
    // see DataBaseWizard, DatabaseTableWizard, AContextualAction
    public void notifySQLBuilder(List<IRepositoryObject> list) {
        IRepositoryChangedListener listener = (IRepositoryChangedListener) RepositoryView.show();
        removeRepositoryChangedListener(listener);
        for (Iterator<IRepositoryObject> iter = list.iterator(); iter.hasNext();) {
            IRepositoryObject element = iter.next();
            repositoryChanged(new RepositoryElementDelta(element));
        }
        registerRepositoryChangedListenerAsFirst(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#validateColumnName(java.lang.String, int)
     */
    public String validateColumnName(String columnName, int index) {
        return ColumnNameValidator.validateColumnNameFormat(columnName, index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getGenericSchemaWizardDialog(org.eclipse.swt.widgets.Shell,
     * org.eclipse.ui.IWorkbench, boolean, org.eclipse.jface.viewers.ISelection, java.lang.String[], boolean)
     */
    public WizardDialog getGenericSchemaWizardDialog(Shell shell, IWorkbench workbench, boolean creation, ISelection selection,
            String[] existingNames, boolean isSinglePageOnly) {

        genericSchemaWizard = new GenericSchemaWizard(workbench, creation, selection, existingNames, isSinglePageOnly);
        return new WizardDialog(shell, genericSchemaWizard);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getPropertyFromWizardDialog()
     */
    public Property getPropertyFromWizardDialog() {
        if (this.genericSchemaWizard != null) {
            return this.genericSchemaWizard.getConnectionProperty();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getPathForSaveAsGenericSchema()
     */
    public IPath getPathForSaveAsGenericSchema() {
        if (this.genericSchemaWizard != null) {
            return this.genericSchemaWizard.getPathForSaveAsGenericSchema();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#openLoginDialog()
     */
    public void openLoginDialog() {

        if (CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY) != null) {
            return;
        }

        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        boolean logged = false;
        LoginDialog loginDialog = new LoginDialog(shell);
        // PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(true);
        logged = loginDialog.open() == LoginDialog.OK;
        if (logged) {

            // addCommand();
            new DisableLanguageActions().earlyStartup();

            new BindingActions().bind();

            IMigrationToolService toolService = CorePlugin.getDefault().getMigrationToolService();
            toolService.executeMigration(SwitchProjectAction.PLUGIN_MODEL);

            IRunProcessService runService = CorePlugin.getDefault().getRunProcessService();
            runService.deleteAllJobs(SwitchProjectAction.PLUGIN_MODEL);

            CorePlugin.getDefault().getCodeGeneratorService().generationInit();
            ComponentUtilities.isComponentPaletteNeedRefresh = true;
            CorePlugin.getDefault().getDesignerCoreService().refreshDesignerPalette();

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#initializeForTalendStartupJob()
     */
    public void initializeForTalendStartupJob() {
        // do nothing now.

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#initializeTalend()
     */
    public void initializeTalend() {

        if (CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY) != null) {
            return;
        }
        openLoginDialog();
    }

    boolean rcpMode = false;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#isRCPMode()
     */
    public boolean isRCPMode() {
        return rcpMode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#setRCPMode()
     */
    public void setRCPMode() {
        rcpMode = true;
    }

    boolean needSetPartListener;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#setPartListener(boolean)
     */
    public void setPartListener(boolean isReuqired) {
        this.needSetPartListener = isReuqired;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#needSetPartListener()
     */
    public boolean needSetPartListener() {
        return needSetPartListener;
    }
}
