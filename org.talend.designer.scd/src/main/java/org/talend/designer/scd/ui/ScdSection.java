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
package org.talend.designer.scd.ui;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Label;
import org.talend.designer.scd.util.SWTResourceManager;

/**
 * DOC hcw class global comment. Detailled comment
 */
public class ScdSection {

    private Label title;

    protected int width;

    protected int height;

    private Decorations composite;

    public ScdSection(Composite parent, int width, int height) {
        this.width = width;
        this.height = height;

        composite = new Decorations(parent, SWT.ON_TOP);
        init(composite);

    }

    /**
     * DOC hcw Comment method "init".
     * 
     * @param width
     * @param composite
     */
    private void init(Composite composite) {
        GridDataFactory.swtDefaults().hint(this.width, height).applyTo(composite);
        GridLayoutFactory.swtDefaults().margins(0, 0).spacing(0, 0).applyTo(composite);
        title = new Label(composite, SWT.NONE);
        title.setAlignment(SWT.CENTER);
        title.setFont(SWTResourceManager.getFont("Times New Roman", 12, SWT.BOLD));
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(title);

        createContents(composite);
    }

    public Composite getControl() {
        return composite;
    }

    /**
     * DOC hcw Comment method "createContents".
     * 
     * @param composite
     */
    protected void createContents(Composite composite) {
        // TODO Auto-generated method stub

    }

    public void setTitle(String text, Color background) {
        title.setText(text);
        title.setBackground(background);
    }

}
