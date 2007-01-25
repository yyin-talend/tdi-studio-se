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

/**
 * There are two kinds of java project status, one is editor status for the use of Talend java editor and another is run
 * time status for the use of run process.
 * 
 * 
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: JavaStatus.java JavaStatus 2007-1-23 下午05:12:45 +0000 (下午05:12:45, 2007-1-23 2007) yzhang $
 * 
 */
public interface IJavaProcessorStates {

    /**
     * Return the code path.
     * 
     * DOC yzhang Comment method "getCodePath".
     */
    public IPath getCodePath();

    /**
     * Return the context path.
     * 
     * DOC yzhang Comment method "getContextPath".
     */
    public IPath getContextPath();

}
