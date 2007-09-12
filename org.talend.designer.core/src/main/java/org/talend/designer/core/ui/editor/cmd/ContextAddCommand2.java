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
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.ui.context.JobContextComposite;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.views.contexts.ContextsView;

/**
 * Command that will add a new Context and copy all parameters from the selected context.
 * 
 * $Id: ContextAddCommand.java 1777 2007-02-03 06:42:25 +0000 (sam., 03 févr. 2007) bqian $
 * 
 */
public class ContextAddCommand2 extends Command {

    List<IContext> listContext;

    IContext context;

    Composite composite;

    CCombo combo;

    CTabFolder tabFolder;

    int tabItemPosition;

    IContextManager contextManager;

    Map<IContext, TableViewerCreator> tableViewerCreatorMap;

    public ContextAddCommand2(JobContextComposite composite, IContext newContext, CCombo combo) {
        this.context = newContext;
        this.composite = composite;
        this.combo = combo;
        this.tabFolder = composite.getTabFolder();
        this.tableViewerCreatorMap = composite.getTableViewerCreatorMap();

        contextManager = composite.getContextManager();
        listContext = contextManager.getListContext();
        setLabel(Messages.getString("ContextAddCommand.label")); //$NON-NLS-1$
    }

    /**
     * qzhang Comment method "refreshContextView".
     */
    private void refreshContextView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view2 = page.findView("org.talend.designer.core.ui.views.ContextsView"); //$NON-NLS-1$
        if (view2 instanceof ContextsView) {
            ((ContextsView) view2).updateContextView(true);
        }
    }

    public void execute() {
        listContext.add(context);
        if (composite instanceof JobContextComposite) {
            ((JobContextComposite) composite).addContext(context);
        }

        String[] stringList = new String[listContext.size()];
        for (int i = 0; i < listContext.size(); i++) {
            stringList[i] = listContext.get(i).getName();
        }

        combo.setItems(stringList);
        contextManager.fireContextsChangedEvent();
        refreshContextView();
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
        refreshContextView();
    }
}
