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
package org.talend.repository.ui.actions.metadata;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ReadTableAction extends AbstractCreateTableAction {

    protected static final String LABEL = Messages.getString("CreateTableAction.action.readTitle"); //$NON-NLS-1$

    public ReadTableAction() {
        super();
        this.setText(LABEL);
        this.setToolTipText(LABEL);
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.READ_ICON));
    }

    protected void init(RepositoryNode node) {
        setEnabled(false);

        if (ENodeType.REPOSITORY_ELEMENT.equals(node.getType())) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
                setEnabled(true);
                return;
            }

            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            if (factory.getStatus(node.getObject()) == ERepositoryStatus.DELETED) {
                return;
            }
        }
    }

    public void run() {
        RepositoryNode metadataNode = getViewPart().getRoot().getChildren().get(6);

        // Force focus to the repositoryView and open Metadata and DbConnection nodes
        getViewPart().setFocus();
        getViewPart().expand(metadataNode, true);

        IStructuredSelection selection = (IStructuredSelection) getSelection();
        RepositoryNode node = (RepositoryNode) selection.getFirstElement();

        // Init the content of the Wizard
        // init(node);

        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);

        if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
            ConnectionItem connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            nodeType = ERepositoryObjectType.getItemType(connectionItem);
        }

        if (ERepositoryObjectType.METADATA_FILE_POSITIONAL.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(2), true);
            createFilePositionalTableWizard(selection, true);

        } else if (ERepositoryObjectType.METADATA_FILE_DELIMITED.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(1), true);
            createFileDelimitedTableWizard(selection, true);

        } else if (ERepositoryObjectType.METADATA_CONNECTIONS.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(0), true);
            createDatabaseTableWizard(selection, true);

        } else if (ERepositoryObjectType.METADATA_FILE_REGEXP.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(0), true);
            createFileRegexpTableWizard(selection, true);

        } else if (ERepositoryObjectType.METADATA_FILE_XML.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(0), true);
            createFileXmlTableWizard(selection, true);

        } else if (ERepositoryObjectType.METADATA_FILE_LDIF.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(0), true);
            createFileLdifTableWizard(selection, true);
        }
    }
}
