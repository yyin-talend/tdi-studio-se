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
package org.talend.componentdesigner.ui.wizard.creatcomponent;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.model.enumtype.LanguageType;
import org.talend.componentdesigner.ui.wizard.PropertyChangeBean;

/**
 * @author rli
 * 
 */
public class WizardComponentFolderPage extends WizardPage {

    private Button useJavaLangButton;

    private Button usePerlLangButton;

    private PropertyChangeBean propertyChangeBean;

	private Text componentFolderText;

    /**
     * @param pageName
     */
    public WizardComponentFolderPage(String pageName) {
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
    public void createControl(Composite parent) {
    	Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		GridData data = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(data);

		Label label = new Label(composite, SWT.NONE);
		GridData gridData = new GridData();
//		gridData.horizontalSpan = 1;
		label.setLayoutData(gridData);
		label.setText("Component Name:");

		componentFolderText = new Text(composite, SWT.BORDER | SWT.LEFT);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
//		gridData.horizontalSpan = 8;
		componentFolderText.setLayoutData(gridData);
		componentFolderText.addModifyListener(new ModifyListener() {
            
            /* (non-Javadoc)
             * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
             */
            public void modifyText(ModifyEvent e) {
                setPageComplete(validatePage());
                propertyChangeBean.firePropertyChange(PluginConstant.NAME_PROPERTY, null, componentFolderText.getText());
            }

        });
		
        this.createComponentLangGroup(composite);
        this.setControl(composite);
    }

    private void createComponentLangGroup(Composite parent) {
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
    protected boolean validatePage() {
        if (useJavaLangButton != null && usePerlLangButton != null) {
            if (!(useJavaLangButton.getSelection() || usePerlLangButton.getSelection())) {
                setErrorMessage("The component language haven't select");
                return false;
            }
        }
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				PluginConstant.PROJECTNAME_DEFAULT);
		IFolder componentFolder = project.getFolder(componentFolderText
				.getText());
		if (componentFolder.exists()) {
			 setErrorMessage("The component has been exsit");
             return false;
		}
		setErrorMessage(null);
        return true;
    }
    
    public String getComponentFolderName() {
    	
       return componentFolderText.getText();
	}

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.WizardPage#setPageComplete(boolean)
     */
    @Override
    public void setPageComplete(boolean complete) {
        super.setPageComplete(complete);
        if (propertyChangeBean != null) {
            this.propertyChangeBean.firePropertyChange(PluginConstant.NAME_PROPERTY, null, this.getComponentFolderName());
        }
    }

}
