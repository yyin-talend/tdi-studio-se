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
package org.talend.designer.core.ui.action;

import org.talend.commons.ui.swt.actions.AbstractShowViewAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ShowPropertiesViewAction extends AbstractShowViewAction {

    @Override
    public String getDefinitionId() {
        return "showPropertiesView"; //$NON-NLS-1$
    }

    @Override
    public String getViewId() {
        return "org.eclipse.ui.views.PropertySheet"; //$NON-NLS-1$
    }

}
