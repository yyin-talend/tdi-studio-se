// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.componentdesigner.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The class define for the amc perspective. <br/>
 * 
 * $Id: Perspective.java,v 1.9 2007/03/23 07:48:54 pub Exp $
 * 
 */
public class Perspective implements IPerspectiveFactory {

    public static final String ID = "org.talend.componentdesigner.perspective"; //$NON-NLS-1$

    public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(true);
    }

}
