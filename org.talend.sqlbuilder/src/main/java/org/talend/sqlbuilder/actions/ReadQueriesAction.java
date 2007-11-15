// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
import org.talend.core.model.utils.TalendTextUtils;
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
import org.talend.sqlbuilder.util.TextUtil;

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
            connParameters.setQuery("");
        } else if (node.getObjectType() == ERepositoryObjectType.METADATA_CON_QUERY) {
            QueryRepositoryObject queryRepositoryObject = (QueryRepositoryObject) node.getObject();
            DatabaseConnectionItem parent = (DatabaseConnectionItem) queryRepositoryObject.getProperty().getItem();
            connParameters.setRepositoryName(parent.getProperty().getLabel());
            connParameters.setQueryObject(queryRepositoryObject.getQuery());
            connParameters.setQuery(queryRepositoryObject.getQuery().getValue());
        }

        Shell parentShell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
                IRepositoryView.VIEW_ID).getSite().getShell().getDisplay());
        TextUtil.setDialogTitle(TalendTextUtils.SQL_BUILDER_TITLE_REP);
        SQLBuilderDialog dial = new SQLBuilderDialog(parentShell);
        
        connParameters.setNodeReadOnly(true);
        connParameters.setFromRepository(true);
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
