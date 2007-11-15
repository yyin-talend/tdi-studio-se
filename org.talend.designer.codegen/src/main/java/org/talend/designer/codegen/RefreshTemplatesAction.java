// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.codegen;

import org.eclipse.jface.action.Action;
import org.talend.core.CorePlugin;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.designer.codegen.model.CodeGeneratorEmittersPoolFactory;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RefreshTemplatesAction extends Action {

    public RefreshTemplatesAction() {
        super();
        this.setActionDefinitionId("refreshTemplates");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        CodeGeneratorEmittersPoolFactory.initialize();
        CorePlugin.getDefault().getLibrariesService().syncLibraries();
        ComponentUtilities.isComponentPaletteNeedRefresh = true;
    }

}
