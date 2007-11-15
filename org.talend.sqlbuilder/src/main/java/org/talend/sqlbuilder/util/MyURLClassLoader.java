// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.sqlbuilder.util;

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

import org.talend.sqlbuilder.SqlBuilderPlugin;

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


    @SuppressWarnings("unchecked") //$NON-NLS-1$
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
                	SqlBuilderPlugin.log(ex.getMessage(), ex);
                }
                for (Iterator it = new EnumerationIterator(zipFile.entries()); it.hasNext();) {
                    Class cls = null;
                    String entryName = ((ZipEntry) it.next()).getName();
                    String className = Utilities.changeFileNameToClassName(entryName);
                    if (className != null) {

                        try {
                            cls = loadClass(className);
                        } catch (Throwable th) {
                        	SqlBuilderPlugin.log(th.getMessage(), th);
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


    /* (non-Javadoc)
     * @see java.net.URLClassLoader#findClass(java.lang.String)
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
	protected synchronized Class findCslass(String className) throws ClassNotFoundException {
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
