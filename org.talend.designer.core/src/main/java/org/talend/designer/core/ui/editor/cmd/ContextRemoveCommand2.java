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
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IProcess;
import org.talend.core.ui.context.JobContextComposite;
import org.talend.core.ui.context.JobContextCompositeForView;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.views.contexts.ContextsView;

/**
 * Command that will remove a context.
 * 
 * $Id: ContextRemoveCommand.java 1777 2007-02-03 06:42:25 +0000 (sam., 03 févr. 2007) bqian $
 * 
 */
public class ContextRemoveCommand2 extends Command {

    List<IContext> listContext;

    IContext context;

    int tabItemPosition;

    Map<IContext, TableViewerCreator> tableViewerCreatorMap;

    CTabFolder tabFolder;

    String contextName;

    Composite composite;

    CCombo combo;

    IContextManager contextManager;

    IProcess process = null;

    public ContextRemoveCommand2(IProcess process, JobContextComposite composite, String contextName, CCombo combo) {
        this.composite = composite;
        this.contextName = contextName;
        this.process = process;
        boolean found = false;
        this.combo = combo;
        this.tableViewerCreatorMap = composite.getTableViewerCreatorMap();
        this.tabFolder = composite.getTabFolder();

        contextManager = composite.getContextManager();
        listContext = contextManager.getListContext();

        for (int i = 0; i < listContext.size() && !found; i++) {
            if (listContext.get(i).getName().equals(contextName)) {
                context = listContext.get(i);
                found = true;
            }
        }

        found = false;
        setLabel(Messages.getString("ContextRemoveCommand.label")); //$NON-NLS-1$
    }

    public ContextRemoveCommand2(IProcess process, JobContextCompositeForView composite, String contextName, CCombo combo) {
        this.composite = composite;
        this.contextName = contextName;
        this.process = process;
        boolean found = false;
        this.combo = combo;
        this.tableViewerCreatorMap = composite.getTableViewerCreatorMap();
        this.tabFolder = composite.getTabFolder();

        contextManager = composite.getContextManager();
        listContext = contextManager.getListContext();

        for (int i = 0; i < listContext.size() && !found; i++) {
            if (listContext.get(i).getName().equals(contextName)) {
                context = listContext.get(i);
                found = true;
            }
        }

        found = false;
        setLabel(Messages.getString("ContextRemoveCommand.label")); //$NON-NLS-1$
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        final IPage currentPage = sheet.getCurrentPage();
        if (currentPage instanceof TabbedPropertySheetPage) {
            TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) currentPage;
            tabbedPropertySheetPage.refresh();
        }
        IViewPart view2 = page.findView("org.talend.designer.core.ui.views.ContextsView"); //$NON-NLS-1$
        if (view2 instanceof ContextsView) {
            ((ContextsView) view2).refresh();
        }
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
        boolean found = false;
        for (int i = 0; i < tabFolder.getItemCount() && !found; i++) {
            if (tabFolder.getItem(i).getText().equals(contextName)) {
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

        // Removes the attached context files
        try {
            CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().removeContextFiles(process, context);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        listContext.add(context);
        if (composite instanceof JobContextCompositeForView) {
            ((JobContextCompositeForView) composite).addContext(context);
        } else if (composite instanceof JobContextComposite) {
            ((JobContextComposite) composite).addContext(context);
        }

        String[] stringList = new String[listContext.size()];
        for (int i = 0; i < listContext.size(); i++) {
            stringList[i] = listContext.get(i).getName();
        }

        if (combo != null && !combo.isDisposed()) {
            combo.setItems(stringList);
        }
        contextManager.fireContextsChangedEvent();
        refreshContextView();
    }
}
