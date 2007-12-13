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
package org.talend.componentdesigner.ui.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.model.enumtype.LanguageType;

/**
 * @author rli
 * 
 */
public class WizardComponentProjectPage extends WizardNewProjectCreationPage {

    private Button useJavaLangButton;

    private Button usePerlLangButton;

    private PropertyChangeBean propertyChangeBean;

    /**
     * @param pageName
     */
    public WizardComponentProjectPage(String pageName) {
        super(pageName);
        propertyChangeBean = new PropertyChangeBean();
    }

    PropertyChangeBean getPropertyChangeBean() {
        return propertyChangeBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.WizardNewProjectCreationPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        this.createProjectLangGroup((Composite) this.getControl());
    }

    private void createProjectLangGroup(Composite parent) {
        // project specification group
        Composite langCheckGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        langCheckGroup.setLayout(layout);
        langCheckGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // create the language selection check button
        useJavaLangButton = new Button(langCheckGroup, SWT.CHECK | SWT.RIGHT);
        useJavaLangButton.setText("Use JAVA");
        useJavaLangButton.setSelection(true);
        usePerlLangButton = new Button(langCheckGroup, SWT.CHECK | SWT.RIGHT);
        usePerlLangButton.setText("Use Perl");

        SelectionListener listener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setPageComplete(validatePage());
                LanguageType type = LanguageType.find(useJavaLangButton.getSelection(), usePerlLangButton.getSelection());
                propertyChangeBean.firePropertyChange(PluginConstant.LANGUAGE_PROPERTY, null, type);
            }
        };
        useJavaLangButton.addSelectionListener(listener);
        usePerlLangButton.addSelectionListener(listener);
        propertyChangeBean
                .firePropertyChange(PluginConstant.LANGUAGE_PROPERTY, null, LanguageType.find(PluginConstant.JAVA_LANG));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.WizardNewProjectCreationPage#validatePage()
     */
    @Override
    protected boolean validatePage() {
        if (useJavaLangButton != null && usePerlLangButton != null) {
            if (!(useJavaLangButton.getSelection() || usePerlLangButton.getSelection())) {
                setErrorMessage("The component language haven't select");
                return false;
            }
        }
        return super.validatePage();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.WizardPage#setPageComplete(boolean)
     */
    @Override
    public void setPageComplete(boolean complete) {
        super.setPageComplete(complete);
        if (propertyChangeBean != null) {
            this.propertyChangeBean.firePropertyChange(PluginConstant.NAME_PROPERTY, null, this.getProjectName());
        }
    }

}
