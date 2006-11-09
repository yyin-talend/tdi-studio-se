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
 * Copyright (C) 2001-2002-2004 Colin Bell
 * colbell@users.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.sourceforge.squirrel_sql.fw.util.EnumerationIterator;
import net.sourceforge.squirrel_sql.fw.util.Utilities;
/**
 * 
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: MyURLClassLoader.java,v 1.2 2006/10/26 21:45:29 qiang.zhang Exp $
 *
 */
public class MyURLClassLoader extends URLClassLoader {

    private Map pclasses = new HashMap();


    public MyURLClassLoader(String fileName) throws IOException {
        this(new File(fileName).toURL());
    }


    public MyURLClassLoader(URL url) {
        this(new URL[] {url});
    }


    public MyURLClassLoader(URL[] urls) {
        super(urls, URLUtil.class.getClassLoader());
    }


    public Class[] getAssignableClasses(Class type) throws IOException {
        List classes = new ArrayList();
        URL[] urls = getURLs();
        for (int i = 0; i < urls.length; ++i) {
            URL url = urls[i];
            File file = new File(url.getFile());
            if (!file.isDirectory() && file.exists() && file.canRead()) {
                ZipFile zipFile = null;
                try {
                    zipFile = new ZipFile(file);
                } catch (IOException ex) {
                }
                for (Iterator it = new EnumerationIterator(zipFile.entries()); it.hasNext();) {
                    Class cls = null;
                    String entryName = ((ZipEntry) it.next()).getName();
                    String className = Utilities.changeFileNameToClassName(entryName);
                    if (className != null) {

                        try {
                            cls = loadClass(className);
                        } catch (Throwable th) {

                        }
                        if (cls != null) {
                            if (type.isAssignableFrom(cls)) {
                                classes.add(cls);
                            }
                        }
                    }
                }
            }
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }


    protected synchronized Class findClass(String className) throws ClassNotFoundException {
        Class cls = (Class) pclasses.get(className);
        if (cls == null) {
            cls = super.findClass(className);
            pclasses.put(className, cls);
        }
        return cls;
    }


    protected void classHasBeenLoaded(Class cls) {
    }
}
