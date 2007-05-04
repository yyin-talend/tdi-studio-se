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
package org.talend.designer.core.ui.editor.properties.controllers.generator;

import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ControllerGenerator.java 1 2006-12-22 下午03:13:13 +0000 (下午03:13:13) yzhang $
 * 
 */
public interface IControllerGenerator {

    public AbstractElementPropertySectionController generate();

    public void setDynamicTabbedPropertySection(DynamicTabbedPropertySection dtp);

}
