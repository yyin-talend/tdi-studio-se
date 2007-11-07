// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.components.ui;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.designer.core.ui.editor.TalendEditorPaletteFactory;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class PaletteContentDialog extends Dialog {

    private static final String DIALOG_TITLE = "Select the itmes in palette";

    private TreeViewer viewer;

    private PaletteRoot paletteRoot;

    private final ComponentsFormatPreferencePage page;

    /**
     * DOC yzhang PaletteContentDialog constructor comment.
     * 
     * @param parentShell
     */
    protected PaletteContentDialog(Shell parentShell, ComponentsFormatPreferencePage page) {
        super(parentShell);
        setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.RESIZE);
        this.page = page;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(DIALOG_TITLE);
        newShell.setSize(400, 600);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        addTreeViewer(composite);
        // addFileds(composite);
        return composite;
    }

    /**
     * yzhang Comment method "addViewer".
     * 
     * @param parent
     */
    private void addTreeViewer(Composite parent) {
        viewer = new TreeViewer(parent);
        viewer.setContentProvider(new TalendPaletteTreeProvider());
        viewer.setLabelProvider(new TalendPaletteLabelProvider());
        viewer.setInput(getPaletteRoot());
        viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    /**
     * DC yzhang Comment method "getPaletteRoot".
     * 
     * @return
     */
    private PaletteRoot getPaletteRoot() {

        if (paletteRoot == null || ComponentUtilities.isComponentPaletteNeedRefresh) {
            paletteRoot = TalendEditorPaletteFactory.createPalette(ComponentsFactoryProvider.getInstance());
            PaletteEntry entry = null;
            for (Object element : paletteRoot.getChildren()) {
                if (((PaletteContainer) element).getLabel().equals("Tools")) {
                    entry = (PaletteEntry) element;
                    break;
                }
            }
            if (entry != null) {
                paletteRoot.remove(entry);
            }
        }

        return paletteRoot;
    }

    /**
     * yzhang Comment method "addFileds".
     * 
     * @param parent
     */
    private void addFileds(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new GridLayout(2, false));
        panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label labelHint = new Label(panel, SWT.NONE);
        labelHint.setText("Format hint default:");
        Text textHint = new Text(panel, SWT.BORDER);
        textHint.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label labelLabel = new Label(panel, SWT.NONE);
        labelLabel.setText("Format label default:");
        Text textLabel = new Text(panel, SWT.BORDER);
        textLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label labelConnection = new Label(panel, SWT.NONE);
        labelConnection.setText("Format connection default:");
        Text textConnection = new Text(panel, SWT.BORDER);
        textConnection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    }

    /**
     * yzhang Comment method "getViewer".
     * 
     * @return
     */
    public TreeViewer getViewer() {
        return this.viewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        page.setViewerInput(viewer.getSelection());
        super.okPressed();
    }

}
