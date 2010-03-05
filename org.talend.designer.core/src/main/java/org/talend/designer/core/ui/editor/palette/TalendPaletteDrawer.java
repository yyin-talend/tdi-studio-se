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
package org.talend.designer.core.ui.editor.palette;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.talend.designer.core.IFilter;

/**
 * 
 */
public class TalendPaletteDrawer extends PaletteDrawer implements IFilter {

    private boolean filtered;

    /**
     * Getter for filtered.
     * 
     * @return the filtered
     */
    public boolean isFiltered() {
        return this.filtered;
    }

    /**
     * Sets the filtered.
     * 
     * @param filtered the filtered to set
     */
    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
        PaletteContainer parentContainer = getParent();
        if (parentContainer instanceof IFilter) {
            ((IFilter) parentContainer).setFiltered(filtered);
        }
    }

    public TalendPaletteDrawer(String label) {
        super(label);
        filtered = true;
    }

    @Override
    public boolean acceptsType(Object type) {
        return true;
    }

}
