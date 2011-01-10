// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components;

import java.util.Locale;
import java.util.ResourceBundle;

import org.talend.designer.core.ILocalProviderService;

/**
 * DOC guanglong.du class global comment. Detailled comment
 */
public class LocalProviderService implements ILocalProviderService {

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.designer.core.ILocalProviderService#getResourceBundle(java.lang.String)
     */
    public ResourceBundle getResourceBundle(String label) {
        // TODO Auto-generated method stub
        return ResourceBundle.getBundle(label, Locale.getDefault(), new ResClassLoader(getClass().getClassLoader()));
    }

    /**
     * 
     * 
     * Needs to create our own class loader in order to clear the cache for a ResourceBundle. Without using a new class
     * loader each time the values would not be reread from the .properties file
     * 
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4212439
     * 
     * yzhang ComponentsFactory class global comment. Detailled comment <br/>
     * 
     * $Id: ComponentsFactory.java 52892 2010-12-20 05:52:17Z nrousseau $
     * 
     */
    public static class ResClassLoader extends ClassLoader {

        public ResClassLoader(ClassLoader parent) {
            super(parent);
        }
    }
}
