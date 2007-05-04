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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.properties.ContextItem;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.process.ContextProcessSection2;
import org.talend.designer.core.ui.views.contexts.ContextsView;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ContextRepositoryCommand extends Command {

    private IContextManager contextManager;

    private ContextItem contextItem;

    private List<IContext> oldContextList;

    private IContext oldDefaultContext;

    private String oldRepositoryId;

    private Process process;

    public ContextRepositoryCommand(Process process, ContextItem contextItem) {
        this.process = process;
        this.contextManager = process.getContextManager();
        this.contextItem = contextItem;
        oldContextList = new ArrayList<IContext>();
        this.setLabel(Messages.getString("ContextRepositoryCommand.modifyContext")); //$NON-NLS-1$
    }

    @Override
    public void execute() {
        if (contextItem != null) {
            oldDefaultContext = contextManager.getDefaultContext();
            oldContextList.addAll(contextManager.getListContext());
            contextManager.loadFromEmf(contextItem.getContext(), contextItem.getDefaultContext());
            oldRepositoryId = process.getRepositoryId();
            process.setRepositoryId(contextItem.getProperty().getId());
        } else {
            oldRepositoryId = process.getRepositoryId();
            process.setRepositoryId(null);
        }
        refreshContextView();
    }

    @Override
    public void undo() {
        if (contextItem != null) {
            contextManager.setListContext(oldContextList);
            contextManager.setDefaultContext(oldDefaultContext);
            process.setRepositoryId(oldRepositoryId);
        } else {
            if (loadContextFromId(oldRepositoryId)) {
                process.setRepositoryId(oldRepositoryId);
            }
        }
        refreshContextView();
    }

    private boolean loadContextFromId(String repositoryId) {
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        List<ContextItem> contextItemList = null;
        try {
            contextItemList = factory.getContextItem();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        if (contextItemList != null) {
            for (ContextItem item : contextItemList) {
                if (factory.getStatus(item) != ERepositoryStatus.DELETED) {
                    String id = item.getProperty().getId();
                    if (id.equals(repositoryId)) {
                        contextManager.loadFromEmf(item.getContext(), item.getDefaultContext());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void updateContextSection() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        final IPage currentPage = sheet.getCurrentPage();
        if (currentPage instanceof TabbedPropertySheetPage) {
            TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) currentPage;
            tabbedPropertySheetPage.refresh();
            ISection[] sections = tabbedPropertySheetPage.getCurrentTab().getSections();
            for (int i = 0; i < sections.length; i++) {
                if (sections[i] instanceof ContextProcessSection2) {
                    ContextProcessSection2 currentSection = (ContextProcessSection2) sections[i];
                    currentSection.updateContextView();
                }
            }
        } 

    }

    /**
     * qzhang Comment method "refreshContextView".
     */
    private void refreshContextView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view2 = page.findView("org.talend.designer.core.ui.views.ContextsView"); //$NON-NLS-1$
        if (view2 instanceof ContextsView) {
            ((ContextsView) view2).updateContextView(contextItem == null);
        }
    }

}
