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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class URIHelper {

    private static final String RESOURCE = "resource"; //$NON-NLS-1$

    private static final String PLATFORM = "platform"; //$NON-NLS-1$

    public static IFile getFile(URI uri) {
        IPath path = convert(uri);
        if (path != null) {
            return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        }
        return null;
    }

    public static IPath convert(URI uri) {
        if (PLATFORM.equals(uri.scheme()) && uri.segmentCount() > 1 && RESOURCE.equals(uri.segment(0))) {
            StringBuffer platformResourcePath = new StringBuffer();
            for (int i = 1, size = uri.segmentCount(); i < size; ++i) {
                platformResourcePath.append('/');
                platformResourcePath.append(URI.decode(uri.segment(i)));
            }

            return new Path(platformResourcePath.toString());
        }

        return null;
    }

    public static URI convert(IPath path) {
        return URI.createPlatformResourceURI(path.toString());
    }
}
