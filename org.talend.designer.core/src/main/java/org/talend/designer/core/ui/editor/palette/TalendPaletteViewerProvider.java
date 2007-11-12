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
package org.talend.designer.core.ui.editor.palette;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */
public class TalendPaletteViewerProvider extends PaletteViewerProvider {

    EditDomain graphicalViewerDomain;

    public TalendPaletteViewerProvider(EditDomain graphicalViewerDomain) {
        super(graphicalViewerDomain);
        this.graphicalViewerDomain = graphicalViewerDomain;
    }

    @Override
    public PaletteViewer createPaletteViewer(Composite parent) {
        PaletteViewer pViewer = new TalendPaletteViewer(graphicalViewerDomain);
        pViewer.createControl(parent);
        configurePaletteViewer(pViewer);
        hookPaletteViewer(pViewer);
        return pViewer;
    }
}
