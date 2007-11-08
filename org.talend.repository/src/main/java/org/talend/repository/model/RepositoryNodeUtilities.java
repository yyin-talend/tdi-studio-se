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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * Utility class to manage RepositoryNode.<br/>
 * 
 * $Id: RepositoryNodeUtilities.java 1368 2007-01-10 09:50:53Z smallet $
 * 
 */
public class RepositoryNodeUtilities {

    public static IPath getPath(RepositoryNode node) {
        if (node == null) {
            return null;
        }
        if (node.isBin()) {
            return new Path("bin"); //$NON-NLS-1$
        }
        if ((node.getType() == ENodeType.STABLE_SYSTEM_FOLDER && node.getContentType() != ERepositoryObjectType.JOBS)
                || node.getType() == ENodeType.SYSTEM_FOLDER) {
            return new Path(""); //$NON-NLS-1$
        }
        if (node.getType() == ENodeType.SIMPLE_FOLDER) {
            String label = node.getObject().getProperty().getLabel();
            return getPath(node.getParent()).append(label);
        }

        String label = node.getLabel();
        // checks if node is under Documentations/Generatated/Jobs
        if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER && node.getContentType() == ERepositoryObjectType.JOBS) {
            String nodeLabel = (String) node.getProperties(EProperties.LABEL);
            if (nodeLabel.equalsIgnoreCase(ERepositoryObjectType.JOBS.toString())) {
                return new Path("");
            } else
            {
                return getPath(node.getParent()).append(label);
            }
        }
        else {
                return getPath(node.getParent()).append(label);
            }
    }
}
