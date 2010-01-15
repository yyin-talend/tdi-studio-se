// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */
public class TalendPaletteViewerProvider extends PaletteViewerProvider {

    public TalendPaletteViewerProvider(EditDomain graphicalViewerDomain) {
        super(graphicalViewerDomain);
    }

    @Override
    public PaletteViewer createPaletteViewer(Composite parent) {
        // removed by 10304
        // if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC) {
        // // PTDO need check it later and fix the bug on MacOS.
        // return super.createPaletteViewer(parent);
        // }
        PaletteViewer pViewer = new TalendPaletteViewer(this.getEditDomain());
        pViewer.createControl(parent);
        configurePaletteViewer(pViewer);
        hookPaletteViewer(pViewer);
        return pViewer;
    }

    @Override
    protected void configurePaletteViewer(PaletteViewer pViewer) {
        pViewer.setContextMenu(new TalendPaletteContextMenuProvider(pViewer));
        pViewer.addDragSourceListener(new TalendPaletteDragSourceListener(pViewer));
    }

}
