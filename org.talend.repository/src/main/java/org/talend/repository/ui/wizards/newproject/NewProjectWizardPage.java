// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.repository.ui.wizards.newproject;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.User;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryFactoryProvider;

/**
 * Page for new project details. <br/>
 * 
 * $Id$
 * 
 */
public class NewProjectWizardPage extends WizardPage {

    /** Name field. */
    private Text nameText;

    /** Technical Name. */
    private Text technicalNameText;

    /** Description field. */
    private Text descriptionText;

    /** Language combo. */
    private Combo languageCombo;

    private IStatus nameStatus;

    private IStatus descriptionStatus;

    private IStatus languageStatus;

    private User author;

    /**
     * Constructs a new NewProjectWizardPage.
     * 
     * @param server
     * @param password
     * @param author
     */
    public NewProjectWizardPage(User author) {
        super("WizardPage"); //$NON-NLS-1$

        setTitle(Messages.getString("NewProjectWizardPage.title")); //$NON-NLS-1$
        setDescription(Messages.getString("NewProjectWizardPage.description")); //$NON-NLS-1$

        this.author = author;

        nameStatus = createOkStatus();
        descriptionStatus = createOkStatus();
        languageStatus = createOkStatus();
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        // Name
        Label nameLab = new Label(container, SWT.NONE);
        nameLab.setText(Messages.getString("NewProjectWizardPage.name")); //$NON-NLS-1$

        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // TechnicalName (for information only)
        Label technicalNameLab = new Label(container, SWT.NONE);
        technicalNameLab.setText(Messages.getString("NewProjectWizardPage.technicalName")); //$NON-NLS-1$

        technicalNameText = new Text(container, SWT.BORDER);
        technicalNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        technicalNameText.setEnabled(false);

        // Description
        Label descriptionLab = new Label(container, SWT.NONE);
        descriptionLab.setText(Messages.getString("NewProjectWizardPage.comment")); //$NON-NLS-1$
        descriptionLab.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        descriptionText = new Text(container, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 60;
        descriptionText.setLayoutData(data);

        // Language
        Label languageLab = new Label(container, SWT.NONE);
        languageLab.setText(Messages.getString("NewProjectWizardPage.language")); //$NON-NLS-1$

        languageCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        languageCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        languageCombo.setItems(new String[] { ECodeLanguage.PERL.getName(), ECodeLanguage.JAVA.getName() });
        languageCombo.select(0);

        setControl(container);
        addListeners();
        setPageComplete(false);
    }

    Project[] projects;

    private boolean isProjectNameAlreadyUsed(String newProjectName) {
        IRepositoryFactory repositoryFactory = RepositoryFactoryProvider.getInstance(((NewProjectWizard)getWizard()).getRepositoryContext());
        if (projects == null) {
            try {
                projects = repositoryFactory.readProject();
            } catch (PersistenceException e) {
                projects = new Project[0];
            }
        }
        for (Project project : projects) {
            if (Project.createTechnicalName(newProjectName).compareTo(project.getTechnicalLabel()) == 0) {
                return true;
            }
        }
        return false;
    }

    private void addListeners() {
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
            }
        });

        descriptionText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
            }
        });

        languageCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkFieldsValue();
            }
        });
    }

    /**
     * DOC ocarbone Comment method "checkField".
     */
    protected void checkFieldsValue() {
        // Field Name
        if (nameText.getText().length() == 0) {
            nameStatus = new Status(IStatus.ERROR, RepositoryPlugin.PLUGIN_ID, IStatus.OK, Messages
                    .getString("NewProjectWizardPage.nameEmpty"), null); //$NON-NLS-1$
        } else {
            technicalNameText.setText(Project.createTechnicalName(nameText.getText()));
            if (!Pattern.matches(RepositoryConstants.PROJECT_PATTERN, nameText.getText())) {
                nameStatus = new Status(IStatus.ERROR, RepositoryPlugin.PLUGIN_ID, IStatus.OK, Messages
                        .getString("NewProjectWizardPage.illegalCharacter"), null); //$NON-NLS-1$
            } else {

                if (isProjectNameAlreadyUsed(nameText.getText())) {
                    nameStatus = new Status(IStatus.ERROR, RepositoryPlugin.PLUGIN_ID, IStatus.OK, Messages
                            .getString("NewProjectWizardPage.projectNameAlredyExists"), null); //$NON-NLS-1$
                } else {
                    nameStatus = createOkStatus();

                    // Field description
                    descriptionStatus = createOkStatus();

                    // Combo language
                    if (languageCombo.getSelectionIndex() == -1) {
                        languageStatus = new Status(IStatus.ERROR, RepositoryPlugin.PLUGIN_ID, IStatus.OK, Messages
                                .getString("NewProjectWizardPage.languageEmpty"), //$NON-NLS-1$
                                null);
                    } else if (!ECodeLanguage.PERL.getName().equals(getLanguage())) {
                        languageStatus = new Status(IStatus.ERROR, RepositoryPlugin.PLUGIN_ID, IStatus.OK, Messages
                                .getString("NewProjectWizardPage.perlOnly"), //$NON-NLS-1$
                                null);
                    } else {
                        languageStatus = createOkStatus();
                    }
                }
            }
        }
        updatePageStatus();
    }

    private void updatePageStatus() {
        IStatus findMostSevere = findMostSevere();
        setMessage(findMostSevere);
        setPageComplete(findMostSevere.getSeverity() != IStatus.ERROR);
    }

    private IStatus findMostSevere() {
        IStatus status;
        if (nameStatus.getSeverity() == IStatus.ERROR) {
            status = nameStatus;
        } else if (descriptionStatus.getSeverity() == IStatus.ERROR) {
            status = descriptionStatus;
        } else if (languageStatus.getSeverity() == IStatus.ERROR) {
            status = languageStatus;
        } else {
            status = nameStatus.getSeverity() > descriptionStatus.getSeverity() ? nameStatus : descriptionStatus;
            status = status.getSeverity() > languageStatus.getSeverity() ? status : languageStatus;
        }
        return status;
    }

    private void setMessage(IStatus status) {
        if (IStatus.ERROR == status.getSeverity()) {
            setErrorMessage(status.getMessage());
            setMessage(""); //$NON-NLS-1$
        } else {
            setMessage(status.getMessage());
            setErrorMessage(null);
        }
    }

    public String getName() {
        return nameText.getText();
    }

    public String getDescription() {
        return descriptionText.getText();
    }

    public String getLanguage() {
        return languageCombo.getSelectionIndex() != -1 ? languageCombo.getItem(languageCombo.getSelectionIndex())
                : null;
    }

    private static IStatus createOkStatus() {
        return new Status(IStatus.OK, RepositoryPlugin.PLUGIN_ID, IStatus.OK, "", null); //$NON-NLS-1$
    }
}
