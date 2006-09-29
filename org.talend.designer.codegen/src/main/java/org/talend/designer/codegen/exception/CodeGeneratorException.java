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
package org.talend.designer.codegen.exception;

import org.talend.commons.exception.SystemException;

/**
 * CodeGenerator Typed Exception.
 * 
 * $Id$
 * 
 */
public class CodeGeneratorException extends SystemException {

    private static final long serialVersionUID = -8567088114593303753L;

    public CodeGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeGeneratorException(Throwable cause) {
        super(cause);
    }

    public CodeGeneratorException(String message) {
        super(message);
    }
}
