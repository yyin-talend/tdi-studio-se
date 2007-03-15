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
package org.talend.repository.ui.views;

import org.eclipse.jface.viewers.ViewerSorter;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * Name sorter for the repository view.<br/>
 * 
 * $Id$
 * 
 */
public class RepositoryNameSorter extends ViewerSorter {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerComparator#category(java.lang.Object)
     */
    @Override
    public int category(Object element) {
        RepositoryNode node = (RepositoryNode) element;

        if (node.isBin()) {
            return 50;
        }

        if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER || node.getType() == ENodeType.SYSTEM_FOLDER) {
            ERepositoryObjectType contentType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (contentType == null) {
                return 99;
            }

            switch (contentType) {
            case BUSINESS_PROCESS:
                return 0;
            case PROCESS:
                return 1;
            case CONTEXT:
                return 2;
            case ROUTINES:
                return 3;
            case METADATA:
                return 4;
            case METADATA_CONNECTIONS:
                return 5;
            case METADATA_FILE_DELIMITED:
                return 6;
            case METADATA_FILE_POSITIONAL:
                return 7;
            case METADATA_FILE_REGEXP:
                return 8;
            case METADATA_FILE_XML:
                return 9;
            case METADATA_FILE_LDIF:
                return 10;
            case DOCUMENTATION:
                return 11;
            default:
                return 99;
            }
        } else if (node.getType() == ENodeType.SIMPLE_FOLDER) {
            return 20;
        } else {
            return 30;
        }

    }

}
