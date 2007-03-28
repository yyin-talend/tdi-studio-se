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
package org.talend.designer.runprocess;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ProcessorException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOC chuger ProcessorException constructor comment.
     */
    public ProcessorException() {
    }

    /**
     * DOC chuger ProcessorException constructor comment.
     * 
     * @param arg0
     */
    public ProcessorException(String arg0) {
        super(arg0);
    }

    /**
     * DOC chuger ProcessorException constructor comment.
     * 
     * @param arg0
     */
    public ProcessorException(Throwable arg0) {
        super(arg0);
    }

    /**
     * DOC chuger ProcessorException constructor comment.
     * 
     * @param arg0
     * @param arg1
     */
    public ProcessorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
