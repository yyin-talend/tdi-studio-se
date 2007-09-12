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
package org.talend.repository.ui.wizards.context;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextComposite;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ContextRepositoryComposite extends ContextComposite {

    private IContextManager contextManager;

    public ContextRepositoryComposite(Composite parent, IContextManager contextManager) {
        super(parent);
        this.contextManager = contextManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.ContextComposite#getContextManager()
     */
    @Override
    public IContextManager getContextManager() {
        return contextManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.JobContextComposite#onContextAddParameter(org.talend.core.model.process.IContextManager,
     * org.talend.core.model.process.IContextParameter)
     */
    public void onContextAddParameter(IContextManager contextManager, IContextParameter contextParam) {
        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            IContext context = contextManager.getListContext().get(i);
            context.getContextParameterList().add(contextParam.clone());
        }
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.JobContextComposite#onContextRenameParameter(org.talend.core.model.process.IContextManager,
     * java.lang.String, java.lang.String)
     */
    public void onContextRenameParameter(IContextManager contextManager, String oldName, String newName) {
        boolean found;
        List<IContextParameter> listParams;

        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            listParams = contextManager.getListContext().get(i).getContextParameterList();
            found = false;
            for (int j = 0; j < listParams.size() && !found; j++) {
                if (listParams.get(j).getName().equals(oldName)) {
                    listParams.get(j).setName(newName);
                    found = true;
                }
            }
        }
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.JobContextComposite#onContextRemoveParameter(org.talend.core.model.process.IContextManager,
     * java.lang.String)
     */
    public void onContextRemoveParameter(IContextManager contextManager, String contextParamName) {
        boolean found;
        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            List<IContextParameter> listParams = contextManager.getListContext().get(i).getContextParameterList();
            found = false;
            for (int j = 0; j < listParams.size() && !found; j++) {
                if (listParams.get(j).getName().equals(contextParamName)) {
                    listParams.remove(j);
                    found = true;
                }
            }
        }
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.JobContextComposite#onContextChangeDefault(org.talend.core.model.process.IContextManager,
     * org.talend.core.model.process.IContext)
     */
    public void onContextChangeDefault(IContextManager contextManager, IContext newDefault) {
        contextManager.setDefaultContext(newDefault);
        refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.ContextComposite#onContextModify(org.talend.core.model.process.IContextManager,
     * org.talend.core.model.process.IContextParameter)
     */
    public void onContextModify(IContextManager contextManager, IContextParameter parameter) {
        propagateType(contextManager, parameter);
        refresh();
    }

    private void propagateType(IContextManager contextManager, IContextParameter param) {
        for (IContext context : contextManager.getListContext()) {
            IContextParameter paramToModify = context.getContextParameter(param.getName());
            paramToModify.setType(param.getType());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.JobContextComposite#onContextAdd(org.talend.core.ui.context.JobContextComposite,
     * org.talend.core.model.process.IContext, org.eclipse.swt.custom.CCombo)
     */
    // public void onContextAdd(JobContextComposite composite, IContext newContext, CCombo combo) {
    // IContextManager contextManager = composite.getContextManager();
    // List<IContext> listContext = contextManager.getListContext();
    // listContext.add(newContext);
    // composite.addContext(newContext);
    //
    // String[] stringList = new String[listContext.size()];
    // for (int i = 0; i < listContext.size(); i++) {
    // stringList[i] = listContext.get(i).getName();
    // }
    //
    // combo.setItems(stringList);
    // contextManager.fireContextsChangedEvent();
    // }
    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // org.talend.core.ui.context.JobContextComposite#onContextRemove(org.talend.core.ui.context.JobContextComposite,
    // * java.lang.String, org.eclipse.swt.custom.CCombo)
    // */
    // public void onContextRemove(JobContextComposite composite, String contextName, CCombo combo) {
    // IContextManager contextManager = composite.getContextManager();
    // List<IContext> listContext = contextManager.getListContext();
    // Map<IContext, TableViewerCreator> tableViewerCreatorMap = composite.getTableViewerCreatorMap();
    // CTabFolder tabFolder = composite.getTabFolder();
    // IContext context = null;
    //
    // boolean found = false;
    // for (int i = 0; i < listContext.size() && !found; i++) {
    // if (listContext.get(i).getName().equals(contextName)) {
    // context = listContext.get(i);
    // found = true;
    // }
    // }
    // found = false;
    // for (int i = 0; i < tabFolder.getItemCount() && !found; i++) {
    // if (tabFolder.getItem(i).getText().equals(contextName)) {
    // tabFolder.getItem(i).dispose();
    // found = true;
    // }
    // }
    //
    // listContext.remove(context);
    // tableViewerCreatorMap.remove(context);
    //
    // String[] stringList = new String[listContext.size()];
    // for (int i = 0; i < listContext.size(); i++) {
    // stringList[i] = listContext.get(i).getName();
    // }
    // combo.setItems(stringList);
    // contextManager.fireContextsChangedEvent();
    // }
}
