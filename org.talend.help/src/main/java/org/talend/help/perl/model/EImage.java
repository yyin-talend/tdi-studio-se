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
package org.talend.help.perl.model;

import org.talend.commons.ui.image.IImage;
import org.talend.help.Activator;


/**
 * DOC Administrator  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ÐÇÆÚÎå, 29 ¾ÅÔÂ 2006) nrousseau $
 *
 */
public enum EImage implements IImage {
    
    DEFAULT_IMAGE,
    ENTER_BACKIMAGE("/icons/e_back2.gif"), //$NON-NLS-1$
    OUT_BACKIMAGE("/icons/e_back.gif"), //$NON-NLS-1$
    DISABLE_BACKIMAGE("/icons/back.gif"), //$NON-NLS-1$
    ENTER_FORWARDIMAGE("/icons/e_forward2.gif"), //$NON-NLS-1$
    OUT_FORWARDIMAGE("/icons/e_forward.gif"), //$NON-NLS-1$
    DISABLE_FORWARDIMAGE("/icons/forward.gif"), //$NON-NLS-1$
    
    WARNING_ICON("/icons/warning.gif"); //$NON-NLS-1$
    
    private String path;

    EImage() {
        this.path = "/icons/unknow.gif"; //$NON-NLS-1$
    }

    EImage(String path) {
        this.path = path;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for clazz.
     * 
     * @return the clazz
     */
    public Class getLocation() {
        return Activator.class;
    }

}

