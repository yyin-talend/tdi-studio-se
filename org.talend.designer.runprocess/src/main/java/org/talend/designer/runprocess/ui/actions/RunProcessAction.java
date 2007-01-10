// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.runprocess.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.ui.ERunprocessImages;
import org.talend.designer.runprocess.ui.views.ProcessView;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: ShowRunProcessViewAction.java 219 2006-10-24 13:45:54 +0000 (mar., 24 oct. 2006) smallet $
 * 
 */
public class RunProcessAction extends Action implements IWorkbenchWindowActionDelegate {

    public RunProcessAction() {
        super();
        this.setText(Messages.getString("ProcessComposite.exec"));
        this.setToolTipText(Messages.getString("ProcessComposite.execHint"));
        this.setImageDescriptor(ImageProvider.getImageDesc(ERunprocessImages.RUN_PROCESS_ACTION));
        this.setActionDefinitionId("showAndRunProcess");
    }

    @Override
    public void run() {
        // TODO SML Use getInstance
        ShowRunProcessViewAction action = new ShowRunProcessViewAction();
        action.run();

        // TODO SML Optimize
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        ProcessView view = (ProcessView) page.getActivePart();
        view.runAction.run();
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
        run();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

}
