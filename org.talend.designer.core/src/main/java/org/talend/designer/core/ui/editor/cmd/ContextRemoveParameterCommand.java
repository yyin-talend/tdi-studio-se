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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;

/**
 * Command that will remove a parameter in all contexts. <br/>
 * 
 * $Id$
 * 
 */
public class ContextRemoveParameterCommand extends Command {

    private IContextManager contextManager;

    private String contextParamName;

    private Map<String, IContextParameter> mapParams = new HashMap<String, IContextParameter>();

    public ContextRemoveParameterCommand(IContextManager contextManager, String contextParamName) {
        this.contextManager = contextManager;
        this.contextParamName = contextParamName;
        this.setLabel("Remove Parameter");
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet");
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        tabbedPropertySheetPage.refresh();
    }

    @Override
    public void execute() {
        boolean found;
        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            List<IContextParameter> listParams = contextManager.getListContext().get(i).getContextParameterList();
            found = false;
            for (int j = 0; j < listParams.size() && !found; j++) {
                if (listParams.get(j).getName().equals(contextParamName)) {
                    mapParams.put(contextManager.getListContext().get(i).getName(), listParams.get(j));
                    listParams.remove(j);
                    found = true;
                }
            }
        }
        contextManager.fireContextsChangedEvent();
        refreshPropertyView();
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            List<IContextParameter> listParams = contextManager.getListContext().get(i).getContextParameterList();
            listParams.add(mapParams.get(contextManager.getListContext().get(i).getName()));
        }
        contextManager.fireContextsChangedEvent();
        refreshPropertyView();
    }
}
