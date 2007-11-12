// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
