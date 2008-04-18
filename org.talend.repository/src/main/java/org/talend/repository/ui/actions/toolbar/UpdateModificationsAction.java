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
package org.talend.repository.ui.actions.toolbar;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.update.IUpdateManager;
import org.talend.core.model.update.UpdateResult;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.repository.RepositoryPlugin;

/**
 * ggu class global comment. Detailled comment
 */
public class UpdateModificationsAction extends Action implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
     */
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        IDesignerCoreService designerCoreService = RepositoryPlugin.getDefault().getDesignerCoreService();
        if (designerCoreService == null) {
            return;
        }
        List<UpdateResult> updateResults = new ArrayList<UpdateResult>();
        IEditorReference[] reference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
        List<IProcess> processes = designerCoreService.getOpenedProcess(reference);
        for (IProcess process : processes) {
            if (process instanceof IProcess2) {
                IProcess2 process2 = (IProcess2) process;
                IUpdateManager updateManager = process2.getUpdateManager();
                if (updateManager != null) {
                    updateManager.checkAllModification();
                    List<UpdateResult> updatesNeeded = updateManager.getUpdatesNeeded();
                    if (updatesNeeded != null) {
                        updateResults.addAll(updatesNeeded);

                    }
                }
            }
        }
        designerCoreService.executeUpdatesManager(updateResults);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
     * org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {

    }

}
