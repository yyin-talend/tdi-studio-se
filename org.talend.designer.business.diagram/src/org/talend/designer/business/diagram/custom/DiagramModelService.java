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
package org.talend.designer.business.diagram.custom;

import org.eclipse.jface.action.IAction;
import org.talend.designer.business.diagram.custom.actions.CreateDiagramAction;

/**
 * DOC qian class global comment. An implementation of the IRunProcessService. <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (星期�? 29 九月 2006) nrousseau $
 * 
 */

public class DiagramModelService implements IDiagramModelService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.business.diagram.custom.IDiagramModelService#getCreateDiagramAction(boolean)
     */
    public IAction getCreateDiagramAction(boolean isToolbar) {
        // TODO Auto-generated method stub
        return new CreateDiagramAction(isToolbar);
    }

}
