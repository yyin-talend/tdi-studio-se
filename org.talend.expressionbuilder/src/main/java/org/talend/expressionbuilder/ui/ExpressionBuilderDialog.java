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
package org.talend.expressionbuilder.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.expressionbuilder.model.CategoryManager;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionBuilderDialog.java 上午10:12:13 2007-7-24 +0000 (2007-7-24) yzhang $
 * 
 */
public class ExpressionBuilderDialog extends Dialog {

    private static final int APPLY_ID = IDialogConstants.CLIENT_ID + 23;

    private static final int EXPORT_ID = IDialogConstants.CLIENT_ID + 22;

    private static final int IMPORT_ID = IDialogConstants.CLIENT_ID + 21;

    private static TestComposite testComposite;

    private static IExpressionController expressionComposite;

    private static CategoryComposite categoryComposite;

    CategoryManager manager = new CategoryManager();

    /**
     * Create the dialog
     * 
     * @param parentShell
     */
    public ExpressionBuilderDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(this.getShellStyle() | SWT.RESIZE);
    }

    /**
     * Create contents of the dialog
     * 
     * @param parent
     */
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.makeColumnsEqualWidth = true;
        container.setLayout(gridLayout);

        final SashForm sashForm = new SashForm(container, SWT.NONE);
        sashForm.setOrientation(SWT.VERTICAL);

        final Composite upperComposite = new Composite(sashForm, SWT.NONE);
        upperComposite.setLayout(new FillLayout());

        final SashForm upperSashform = new SashForm(upperComposite, SWT.NONE);

        expressionComposite = new ExpressionComposite(upperSashform, SWT.NONE);

        testComposite = new TestComposite(upperSashform, SWT.NONE);
        upperSashform.setWeights(new int[] { 1, 1 });

        final Composite lowerComposite = new Composite(sashForm, SWT.NONE);
        lowerComposite.setLayout(new FillLayout());

        categoryComposite = new CategoryComposite(lowerComposite, SWT.NONE, manager);
        categoryComposite.setExpressController(expressionComposite);

        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        // gridData.heightHint = 291;
        // gridData.widthHint = 483;
        sashForm.setLayoutData(gridData);
        sashForm.setWeights(new int[] { 1, 1 });

        expressionComposite.configProposal();

        return container;
    }

    /**
     * Create contents of the button bar
     * 
     * @param parent
     */
    protected void createButtonsForButtonBar(Composite parent) {
        final Button importButton = createButton(parent, IMPORT_ID, "Import", false);
        importButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
        importButton.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
        createButton(parent, EXPORT_ID, "Export", false);
        createButton(parent, APPLY_ID, "Apply", false);
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Expression Builder");
    }

    protected Point getInitialSize() {
        return new Point(1024, 768);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#close()
     */
    @Override
    public boolean close() {
        testComposite.stopServerThread();
        return super.close();
    }

    /**
     * Getter for testComposite.
     * 
     * @return the testComposite
     */
    public static TestComposite getTestComposite() {
        return testComposite;
    }

    /**
     * Getter for expressionComposite.
     * 
     * @return the expressionComposite
     */
    public static ExpressionComposite getExpressionComposite() {
        return (ExpressionComposite) expressionComposite;
    }

    /**
     * Getter for categoryComposite.
     * 
     * @return the categoryComposite
     */
    public static CategoryComposite getCategoryComposite() {
        return categoryComposite;
    }
}
