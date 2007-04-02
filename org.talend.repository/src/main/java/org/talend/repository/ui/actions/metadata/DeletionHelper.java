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

import org.talend.commons.exception.PersistenceException;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;

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
    public static boolean isDeleted(RepositoryNode node) throws PersistenceException {
        IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
        return (repFactory.getStatus(node.getObject()) == ERepositoryStatus.DELETED);
    }

    /**
     * DOC tguiu DeletionHelper constructor comment.
     */
    private DeletionHelper() {
        // TODO Auto-generated constructor stub
    }

}
