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
package org.talend.repository.model;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.repository.model.RepositoryNode.ENodeType;

/**
 * Utility class to manage RepositoryNode.<br/>
 * 
 * $Id$
 * 
 */
public class RepositoryNodeUtilities {

    public static IPath getPath(RepositoryNode node) {
        if (node instanceof BinRepositoryNode) {
            // FIXME SML
            return new Path("bin");
        }
        if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER || node.getType() == ENodeType.SYSTEM_FOLDER) {
            return new Path("");
        }
        if (node.getType() == ENodeType.SIMPLE_FOLDER) {
            String label = node.getObject().getProperty().getLabel();
            return getPath(node.getParent()).append(label);
        } else {
            return getPath(node.getParent());
        }
    }
}
