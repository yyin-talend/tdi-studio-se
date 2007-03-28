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
package org.talend.repository.model;

import org.talend.commons.ui.image.IImage;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class QueryEMFRepositoryNode extends RepositoryNode {

    private Query query;

    /**
     * DOC smallet EMFObjectRepositoryNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public QueryEMFRepositoryNode(Query query, RepositoryNode parent) {
        super(null, parent, ENodeType.REPOSITORY_ELEMENT);
        this.query = query;
    }

    public IImage getIcon() {
        return ECoreImage.METADATA_QUERY_ICON;
    }

    @Override
    public String getLabel() {
        return query.getLabel();
    }

    @Override
    public ENodeType getType() {
        return type;
    }

    public ERepositoryObjectType getObjectType() {
       return ERepositoryObjectType.METADATA_CON_QUERY;
    }
}
