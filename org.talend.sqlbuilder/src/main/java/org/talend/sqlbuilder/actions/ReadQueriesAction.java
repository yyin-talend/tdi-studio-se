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
package org.talend.sqlbuilder.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryContentProvider.QueryRepositoryObject;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.ui.SQLBuilderDialog;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ReadQueriesAction extends AContextualAction {

    public ReadQueriesAction() {
        super();
        setText(Messages.getString("EditQueriesAction.textOpenQueries")); //$NON-NLS-1$
        setImageDescriptor(ImageProvider.getImageDesc(EImage.READ_ICON));
    }

    public void run() {
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        RepositoryNode node = (RepositoryNode) selection.getFirstElement();

        ConnectionParameters connParameters = new ConnectionParameters();
        if (node.getObjectType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
            connParameters.setRepositoryName(node.getObject().getLabel());
        } else if (node.getObjectType() == ERepositoryObjectType.METADATA_CON_QUERY) {
            QueryRepositoryObject queryRepositoryObject = (QueryRepositoryObject) node.getObject();
            DatabaseConnectionItem parent = (DatabaseConnectionItem) queryRepositoryObject.getProperty().getItem();
            connParameters.setRepositoryName(parent.getProperty().getLabel());
        }

        Shell parentShell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
                IRepositoryView.VIEW_ID).getSite().getShell().getDisplay());
        SQLBuilderDialog dial = new SQLBuilderDialog(parentShell);
        connParameters.setQuery(""); //$NON-NLS-1$
        connParameters.setNodeReadOnly(true);
        dial.setConnParameters(connParameters);
        dial.open();
        refresh(node);
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        if (canWork) {
            Object o = selection.getFirstElement();
            RepositoryNode node = (RepositoryNode) o;
            
            switch (node.getType()) {
            case REPOSITORY_ELEMENT:
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                if (factory.getStatus(node.getObject()) == ERepositoryStatus.DELETED) {
                    canWork = false;
                }
                if (node.getObjectType() != ERepositoryObjectType.METADATA_CONNECTIONS
                        && node.getObjectType() != ERepositoryObjectType.METADATA_CON_QUERY) {
                    canWork = false;
                }
                break;
            default:
                canWork = false;
            }
        }
        setEnabled(canWork);
    }
}
