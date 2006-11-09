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

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.swt.actions.AbstractShowViewAction;
import org.talend.designer.runprocess.ui.views.ProcessView;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: ShowRunProcessViewAction.java 219 2006-10-24 13:45:54 +0000 (mar., 24 oct. 2006) smallet $
 * 
 */
public class RunProcessAction extends AbstractShowViewAction {

    @Override
    public String getDefinitionId() {
        return "showAndRunProcess";
    }

    @Override
    public String getViewId() {
        return "org.talend.designer.runprocess.ui.views.processview";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.rcp.actions.AbstractShowViewAction#run()
     */
    @Override
    public void run() {
        super.run();
        // TODO SML Optimize
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        ProcessView view = (ProcessView) page.getActivePart();
        view.runAction.run();
    }

}
