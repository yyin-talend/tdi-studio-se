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
package org.talend.repository.plugin.integration;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;
import org.talend.core.CorePlugin;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryView;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class LaunchTalendHandler extends AbstractHandler {

    public final static String TALEND_PERSPECTIVE_ID = "org.talend.rcp.perspective";

    /**
     * The constructor.
     */
    public LaunchTalendHandler() {
    }

    /**
     * the command has been executed, so extract extract the needed information from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (CorePlugin.getDefault().getRepositoryService().isRCPMode()) {
            PlatformUI.getWorkbench().restart();
            return null;
        }

        IPerspectiveDescriptor pDescriptor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getPerspective();

        if (!pDescriptor.getId().equals(TALEND_PERSPECTIVE_ID)) {
            pDescriptor = getPerspective(TALEND_PERSPECTIVE_ID);
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(pDescriptor);

        }

        SwitchProjectAction switchAction = new SwitchProjectAction();
        switchAction.run();
        IRepositoryView view = RepositoryView.show();
        if (view != null) {
            view.refresh();
        }

        // 
        //
        // 

        return null;
    }

    /**
     * DOC bqian Comment method "getPerspective".
     * 
     * @param id
     * @return
     */
    private IPerspectiveDescriptor getPerspective(String id) {
        IPerspectiveDescriptor[] pers = PlatformUI.getWorkbench().getPerspectiveRegistry().getPerspectives();
        for (IPerspectiveDescriptor perspectiveDescriptor : pers) {
            if (perspectiveDescriptor.getId().equals(id)) {
                return perspectiveDescriptor;
            }
        }
        throw new RuntimeException("plugin org.talend.rcp is not loaded.");
    }
}
