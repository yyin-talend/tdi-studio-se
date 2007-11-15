package org.talend.repository.plugin.integration;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.talend.core.CorePlugin;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryView;

public class BootTalendAction implements IWorkbenchWindowActionDelegate {

    public final static String TALEND_PERSPECTIVE_ID = "org.talend.rcp.perspective";

    public void dispose() {
        // TODO Auto-generated method stub

    }

    public void init(IWorkbenchWindow window) {
        // TODO Auto-generated method stub

    }

    public void run(IAction action) {
        if (CorePlugin.getDefault().getRepositoryService().isRCPMode()) {
            PlatformUI.getWorkbench().restart();
            return;
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

        return;
    }

    public void selectionChanged(IAction action, ISelection selection) {

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
