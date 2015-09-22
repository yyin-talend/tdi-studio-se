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
package org.talend.component.ui.wizard.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.component.ui.wizard.ui.factory.GenericUIBuilder;
import org.talend.components.api.properties.presentation.Form;

/**
 * created by ycbai on 2015年1月23日 Detailled comment
 *
 */
public class DynamicComposite extends Composite {

    public DynamicComposite(Composite parent, Form form) {
        super(parent, SWT.NONE);
        this.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);
        createControl(form);
        addListener();
    }

    private void createControl(Form form) {
        new GenericUIBuilder(this).build(form);
    }

    private void addListener() {
    }

}
