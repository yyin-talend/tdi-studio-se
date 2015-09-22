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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.component.ui.wizard.util.GenericWizardServiceFactory;
import org.talend.core.model.properties.ConnectionItem;

/**
 * 
 * created by ycbai on 2015年9月21日 Detailled comment
 *
 */
public class GenericConnWizardPage extends WizardPage {

    private static final int VISIBLE_DB_TYPE_COUNT = 5;

    private final ConnectionItem connectionItem;

    private final String[] existingNames;

    private final boolean isRepositoryObjectEditable;

    private Composite parentComp;

    private LabelledCombo dbTypeCombo;

    private final NoSQLRepositoryFactory repFactory;

    private final NoSQLRepositoryTranslator repositoryTranslator;

    private IWizardPageProvider wizPageProvider;

    private AbstractNoSQLConnForm connectionForm;

    public GenericConnWizardPage(ConnectionItem connectionItem, boolean isRepositoryObjectEditable, String[] existingNames,
            boolean creation) {
        super("GenericConnWizardPage"); //$NON-NLS-1$
        this.connectionItem = connectionItem;
        this.existingNames = existingNames;
        this.isRepositoryObjectEditable = isRepositoryObjectEditable;
        repFactory = NoSQLRepositoryFactory.getInstance();
        repositoryTranslator = NoSQLRepositoryTranslator.getInstance();
    }

    @Override
    public void createControl(final Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout());
        parentComp = container;
        setControl(parentComp);

        GenericWizardServiceFactory.getGenericWizardService()
        new DynamicComposite(parentComp, null);

        Composite dbTypePartComposite = new Composite(parentComp, SWT.NONE);
        GridLayout dbTypePartLayout = new GridLayout(2, false);
        dbTypePartLayout.marginWidth = 10;
        dbTypePartLayout.marginHeight = 5;
        dbTypePartComposite.setLayout(dbTypePartLayout);
        dbTypePartComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        addFieldsListeners();
    }

    protected void addFieldsListeners() {
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        setPageComplete(visible);
    }

}
