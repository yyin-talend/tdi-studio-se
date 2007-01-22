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
package org.talend.designer.core.ui.editor;

/**
 * 
 * If the syntax of the editor used in the multiple page Talend editor need to be checked, this 
 * interface should be implemented.
 * 
 * For example the condition below:
 * When switch the tab from the designer to code in the multiple page Talend editor, the syntax of the 
 * code need to be validated by calling the method validateSyntax.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * 
 * $Id: ICheckable.java 1 2007-1-18 下午03:52:46 +0000 (下午03:52:46, 2007-1-18 2007) yzhang $
 * 
 */
public interface ISyntaxCheckable {

    /**
     * Validate the syntax of the code.
     * 
     * DOC yzhang Comment method "checkCode".
     */
    public void validateSyntax();

}
