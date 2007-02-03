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
package org.talend.designer.core.ui.editor.cmd;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that will change the default context.
 * 
 * $Id$
 * 
 */
public class ContextChangeDefaultCommand extends Command {

    IContext newDefault;

    IContext oldDefault;

    IContextManager contextManager;

    public ContextChangeDefaultCommand(Process process, IContext newDefault) {
        this.newDefault = newDefault;
        contextManager = process.getContextManager();
        this.oldDefault = contextManager.getDefaultContext();
        this.setLabel(Messages.getString("ContextChangeDefaultCommand.label")); //$NON-NLS-1$
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        tabbedPropertySheetPage.refresh();
    }

    @Override
    public void execute() {
        contextManager.setDefaultContext(newDefault);
        contextManager.fireContextsChangedEvent();
        refreshPropertyView();
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        contextManager.setDefaultContext(oldDefault);
        contextManager.fireContextsChangedEvent();
        refreshPropertyView();
    }
}
