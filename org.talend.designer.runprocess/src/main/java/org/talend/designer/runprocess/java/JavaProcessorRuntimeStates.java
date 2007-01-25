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
package org.talend.designer.runprocess.java;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.designer.runprocess.IJavaProcessorStates;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: RuntimeJavaStatus.java RuntimeJavaStatus 2007-1-23 下午05:17:59 +0000 (下午05:17:59, 2007-1-23 2007) yzhang $
 * 
 */
public class JavaProcessorRuntimeStates implements IJavaProcessorStates {

    private JavaProcessor javaProcessor;

    /**
     * DOC yzhang RuntimeJavaStatus constructor comment.
     */
    public JavaProcessorRuntimeStates(JavaProcessor javaProcessors) {
        this.javaProcessor = javaProcessors;
        this.javaProcessor.setStatus(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.java.IJavaStatus#getCodePath()
     */
    public IPath getCodePath() {
        return this.javaProcessor.getCompliedCodePath();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.java.IJavaStatus#getContextPath()
     */
    public IPath getContextPath() {
        return this.javaProcessor.getCompliedContextPath();
        // return null;
    }

}
