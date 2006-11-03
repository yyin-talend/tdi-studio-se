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
package org.talend.sqlbuilder.util;

/*
 * Copyright (C) 2002-2004 Andrea Mazzolini
 * andreamazzolini@users.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.talend.sqlbuilder.SqlBuilderPlugin;
/**
 * 
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: URLUtil.java,v 1.2 2006/10/26 21:39:06 qiang.zhang Exp $
 *
 */
public class URLUtil {

    private URLUtil() {
    }


    public static URL getResourceURL(String s) {
        if (!initialized)
            init();
        URL url = null;
        try {
            url = new URL(baseURL, s);
        } catch (Throwable e) {
        }
        return url;
    }

    static private boolean initialized = false;


    static private void init() {
        SqlBuilderPlugin defaultPlugin = SqlBuilderPlugin.getDefault();

        baseURL = defaultPlugin.getBundle().getEntry("/");
        initialized = true;
    }

    private static URL baseURL;



    /**
     * Return a URL to a file located in your plugin fragment
     * @param yourPluginId e.g net.sourceforge.sqlexplorer.oracle
     * @param filePath path to file within your fragment e.g. icons/test.gif
     * @return URL to the file.
     */
    public static URL getFragmentResourceURL(String yourPluginId, String filePath) {
        
        URL url = null;
        
        try {
            URL baseURL = Platform.getBundle(yourPluginId).getEntry("/");
            url = new URL(baseURL, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
     
        return url;
    }
    
}
