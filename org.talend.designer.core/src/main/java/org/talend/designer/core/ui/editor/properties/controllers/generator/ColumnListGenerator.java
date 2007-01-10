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
package org.talend.designer.core.ui.editor.properties.controllers.generator;

import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController;
import org.talend.designer.core.ui.editor.properties.controllers.ColumnListController;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ColumnListGenerator.java 1 2006-12-22 下午04:38:44 +0000 (下午04:38:44) yzhang $
 * 
 */
public class ColumnListGenerator implements IControllerGenerator {

    private DynamicTabbedPropertySection dtp;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IControllerGenerator#generate()
     */
    public AbstractElementPropertySectionController generate() {
        return new ColumnListController(dtp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IControllerGenerator#setDynamicTabbedPropertySection(org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection)
     */
    public void setDynamicTabbedPropertySection(DynamicTabbedPropertySection dtp) {
        this.dtp = dtp;

    }

}
