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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;
import org.talend.core.CorePlugin;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: BoundingActions.java 下午04:56:43 2007-10-24 +0000 (2007-10-24) yzhang $
 * 
 */
public class BoundingActions implements IStartup {

    private final List<IAction> actions = new ArrayList<IAction>();

    public static final IExtensionPointLimiter GLOBAL_ACTIONS = new ExtensionPointLimiterImpl(
            "org.talend.core.global_actions", //$NON-NLS-1$
            "GlobalAction"); //$NON-NLS-1$

    public void earlyStartup() {

        boolean isRCPModel = CorePlugin.getDefault().getRepositoryService().isRCPMode();

        if (!isRCPModel) {
            createActions();
            registerActions();
        }

    }

    /**
     * DOC smallet Comment method "registerActions".
     */
    private void registerActions() {

        IWorkbench workbench = PlatformUI.getWorkbench();
        IHandlerService handlerService = (IHandlerService) workbench.getService(IHandlerService.class);

        IHandler handler;
        for (IAction action : actions) {
            handler = new ActionHandler(action);
            handlerService.activateHandler(action.getActionDefinitionId(), handler);
        }
    }

    /**
     * DOC smallet Comment method "createActions".
     */
    private void createActions() {

        List<IAction> list = ExtensionImplementationProvider.getInstance(GLOBAL_ACTIONS);
        actions.addAll(list);

    }

}
