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
package org.talend.designer.codegen.model;

import java.util.ArrayList;
import java.util.List;

import org.talend.designer.codegen.config.EInternalTemplate;
import org.talend.designer.codegen.config.TemplateUtil;

/**
 * Create a list of Available templates in the application.
 * 
 * $Id$
 * 
 */
public class CodeGeneratorInternalTemplatesFactory {

    private List<TemplateUtil> templates;

    /**
     * Constructor.
     */
    public CodeGeneratorInternalTemplatesFactory() {
    }

    /**
     * init list of templates.
     */
    public void init() {
        templates = new ArrayList<TemplateUtil>();

        for (EInternalTemplate utilTemplate : EInternalTemplate.values()) {
            TemplateUtil template = new TemplateUtil(utilTemplate);
            templates.add(template);
        }
    }

    /**
     * Return list of available templates.
     * 
     * @return
     */
    public List<TemplateUtil> getTemplates() {
        return templates;
    }
}
