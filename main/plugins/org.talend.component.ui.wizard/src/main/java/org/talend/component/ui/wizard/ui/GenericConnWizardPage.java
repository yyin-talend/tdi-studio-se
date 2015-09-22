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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.components.api.properties.presentation.Form;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.core.ui.views.properties.MultipleThreadDynamicComposite;

/**
 * 
 * created by ycbai on 2015年9月21日 Detailled comment
 *
 */
public class GenericConnWizardPage extends WizardPage {

    private final ConnectionItem connectionItem;

    private final String[] existingNames;

    private final boolean isRepositoryObjectEditable;

    private Composite parentComp;

    private Form form;

    public GenericConnWizardPage(ConnectionItem connectionItem, boolean isRepositoryObjectEditable, String[] existingNames,
            boolean creation, Form form) {
        super("GenericConnWizardPage"); //$NON-NLS-1$
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        this.form = form;
    }

    @Override
    public void createControl(final Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout());
        parentComp = container;
        setControl(parentComp);

        Element element = new Element() {

            @Override
            public void setReadOnly(boolean readOnly) {
                return;
            }

            @Override
            public boolean isReadOnly() {
                return false;
            }

            @Override
            public String getElementName() {
                return "";
            }

        };
        // TODO: add element parameters to element.
        new MultipleThreadDynamicComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS, EComponentCategory.MAIN, element,
                true);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        setPageComplete(visible);
    }

}
