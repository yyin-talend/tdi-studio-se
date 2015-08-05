// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
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

import org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.ui.images.CoreImageProvider;

/**
 * DOC talend class global comment. Detailled comment
 */
public class TalendEntryEditPart extends ToolEntryEditPart {

    /**
     * DOC talend TalendEntryEditPart constructor comment.
     * 
     * @param paletteEntry
     */
    public TalendEntryEditPart(PaletteEntry paletteEntry) {
        super(paletteEntry);
    }

    @Override
    protected void setImageDescriptor(ImageDescriptor desc) {
        setImageInFigure(CoreImageProvider.getComponentImageFromDesc(desc));
    }

}
