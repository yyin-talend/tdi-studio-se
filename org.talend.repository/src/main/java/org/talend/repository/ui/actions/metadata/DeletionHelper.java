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
package org.talend.repository.ui.actions.metadata;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.RepositoryContentProvider.MetadataTableRepositoryObject;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DeletionHelper {

    /**
     * DOC tguiu Comment method "isDeleted".
     * 
     * @param repObj
     * @return
     * @throws PersistenceException
     */
    public static boolean isDeleted(IRepositoryFactory factory, RepositoryNode node) throws PersistenceException {
        RepositoryNode parent = node.getParent();
        ERepositoryObjectType nodeType = (ERepositoryObjectType) parent.getProperties(EProperties.CONTENT_TYPE);
        Connection connection = (Connection) ((ConnectionItem) parent.getObject().getProperty().getItem()).getConnection();
        if (connection == null) {
            return true;
        }
        MetadataTable table = ((MetadataTableRepositoryObject) node.getObject()).getTable();
        return TableHelper.isDeleted(table);
    }

    /**
     * DOC tguiu DeletionHelper constructor comment.
     */
    private DeletionHelper() {
        // TODO Auto-generated constructor stub
    }

}
