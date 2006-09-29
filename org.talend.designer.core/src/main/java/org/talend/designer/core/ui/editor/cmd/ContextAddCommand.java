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

import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.designer.core.ui.editor.properties.process.ContextProcessSection;

/**
 * Command that will add a new Context and copy all parameters from the selected context.
 * 
 * $Id$
 * 
 */
public class ContextAddCommand extends Command {

    List<IContext> listContext;

    IContext context;

    ContextProcessSection section;

    CCombo combo;

    CTabFolder tabFolder;

    int tabItemPosition;

    IContextManager contextManager;

    Map<IContext, TableViewerCreator> tableViewerCreatorMap;

    public ContextAddCommand(ContextProcessSection section, IContext newContext, CCombo combo) {
        this.context = newContext;
        this.section = section;
        this.combo = combo;
        this.tabFolder = section.getTabFolder();
        this.tableViewerCreatorMap = section.getTableViewerCreatorMap();

        contextManager = section.getProcess().getContextManager();
        listContext = contextManager.getListContext();
        setLabel("Add new Context");
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet");
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        tabbedPropertySheetPage.refresh();
    }

    public void execute() {
        listContext.add(context);
        section.addContext(context);

        String[] stringList = new String[listContext.size()];
        for (int i = 0; i < listContext.size(); i++) {
            stringList[i] = listContext.get(i).getName();
        }

        combo.setItems(stringList);
        contextManager.fireContextsChangedEvent();
        refreshPropertyView();
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        boolean found = false;
        for (int i = 0; i < tabFolder.getItemCount() && !found; i++) {
            if (tabFolder.getItem(i).getText().equals(context.getName())) {
                tabFolder.getItem(i).dispose();
                tabItemPosition = i;
                found = true;
            }
        }

        listContext.remove(context);
        tableViewerCreatorMap.remove(context);

        String[] stringList = new String[listContext.size()];
        for (int i = 0; i < listContext.size(); i++) {
            stringList[i] = listContext.get(i).getName();
        }
        combo.setItems(stringList);
        contextManager.fireContextsChangedEvent();
        refreshPropertyView();
    }
}
