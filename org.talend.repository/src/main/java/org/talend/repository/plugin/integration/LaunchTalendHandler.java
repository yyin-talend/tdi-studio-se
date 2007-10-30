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
