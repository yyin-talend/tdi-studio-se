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
package org.talend.designer.core.ui.editor.palette;

import org.eclipse.gef.palette.PaletteDrawer;

/**
 * 
 */
public class TalendPaletteDrawer extends PaletteDrawer {

    public TalendPaletteDrawer(String label) {
        super(label);
    }

    @Override
    public boolean acceptsType(Object type) {
        return true;
    }
}
