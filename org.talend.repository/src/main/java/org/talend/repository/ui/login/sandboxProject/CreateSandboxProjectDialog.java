// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.login.sandboxProject;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.utils.PasswordHelper;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.User;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.ui.wizards.newproject.NewProjectWizardPage;

/**
 * ggu class global comment. Detailled comment
 */
public class CreateSandboxProjectDialog extends TitleAreaDialog {

    private Group projectGroup;

    private LabelledCombo languageCombo;

    private LabelledText projectLabelText;

    private Group userGroup;

    private LabelledText userLoginText, userPassText, userFirstNameText, userLastNameText;

    private Project[] projects = null;

    public CreateSandboxProjectDialog(Shell parentShell) {
        super(parentShell);
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        ImageDescriptor imgDesc = brandingService.getLoginHImage();
        if (imgDesc != null) {
            setTitleImage(ImageProvider.getImage(imgDesc));
        }

        initProjectList();
    }

    private void initProjectList() {
        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

        try {
            projects = repositoryFactory.readProject();
        } catch (Exception e) {
            //
        }
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        String fullProductName = brandingService.getFullProductName();
        newShell.setText(Messages.getString("CreateSandboxProjectDialog.Title", fullProductName)); //$NON-NLS-1$
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        createProjectInfors(composite);
        addProjectListeners();
        addUserListeners();
        return composite;
    }

    @Override
    protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        checkFields();
        if (userLoginText != null) {
            userLoginText.getTextControl().forceFocus(); // focus this first
        }
        return contents;
    }

    private void createProjectInfors(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        projectGroup = new Group(composite, SWT.NONE);
        projectGroup.setText(Messages.getString("CreateSandboxProjectDialog.Settings")); //$NON-NLS-1$
        projectGroup.setLayout(new GridLayout(2, false));
        projectGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

        projectLabelText = new LabelledText(projectGroup, Messages.getString("CreateSandboxProjectDialog.ProjectLabel")); //$NON-NLS-1$
        GridData layoutData = new GridData();
        layoutData.widthHint = 180;
        layoutData.minimumWidth = 180;
        projectLabelText.setLayoutData(layoutData);
        projectLabelText.getTextControl().setEditable(false);

        languageCombo = new LabelledCombo(projectGroup, Messages.getString("NewProjectWizardPage.language"), null, new String[] { //$NON-NLS-1$
                ECodeLanguage.JAVA.getName(), ECodeLanguage.PERL.getName() });
        layoutData = new GridData();
        layoutData.widthHint = 100;
        layoutData.minimumWidth = 100;
        languageCombo.getCombo().setLayoutData(layoutData);
        languageCombo.select(0); // default for java

        // user
        createUserInfors(projectGroup);

    }

    private void addProjectListeners() {

        ModifyListener listener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFields();
            }
        };
        projectLabelText.addModifyListener(listener);
    }

    private void createUserInfors(Composite parent) {
        userGroup = new Group(parent, SWT.NULL);
        userGroup.setLayout(new GridLayout(4, false));
        GridData userLayoutData = new GridData(GridData.FILL_HORIZONTAL);
        userLayoutData.horizontalSpan = 2;
        userGroup.setLayoutData(userLayoutData);

        userLoginText = new LabelledText(userGroup, Messages.getString("CreateSandboxProjectDialog.Login")); //$NON-NLS-1$
        userPassText = new LabelledText(userGroup, Messages.getString("CreateSandboxProjectDialog.Password")); //$NON-NLS-1$
        userPassText.getTextControl().setEchoChar('*');

        userFirstNameText = new LabelledText(userGroup, Messages.getString("CreateSandboxProjectDialog.userFirstname")); //$NON-NLS-1$
        userLastNameText = new LabelledText(userGroup, Messages.getString("CreateSandboxProjectDialog.userLastname")); //$NON-NLS-1$

    }

    private void addUserListeners() {
        ModifyListener listener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (e.widget == userLoginText.getTextControl()) {
                    String generateProjectName = generateProjectName();
                    projectLabelText.setText(generateProjectName);
                }
                checkFields();
            }
        };
        userLoginText.addModifyListener(listener);
        userPassText.addModifyListener(listener);
    }

    private void checkFields() {

        // check field
        String projectLabel = projectLabelText.getText();
        if ((userLoginText.getText().length() == 0 || !Pattern.matches(RepositoryConstants.MAIL_PATTERN, userLoginText.getText()))) {
            setErrorMessage(Messages.getString("CreateSandboxProjectDialog.userLoginValidMessage")); //$NON-NLS-1$
        } else if (!Pattern.matches(RepositoryConstants.PROJECT_PATTERN, projectLabel)
                || NewProjectWizardPage.isKeywords(projectLabel.toLowerCase())
                || ECodeLanguage.JAVA.getName().equalsIgnoreCase(projectLabel)) {
            setErrorMessage(Messages.getString("NewProjectWizardPage.illegalCharacter")); //$NON-NLS-1$
            checkProjectLabel(true);
        } else if (isProjectNameAlreadyUsed(projectLabel)) {
            setErrorMessage(Messages.getString("NewProjectWizardPage.projectNameAlredyExists")); //$NON-NLS-1$
            checkProjectLabel(true);
        } else if (userPassText.getText().length() == 0) {
            setErrorMessage(Messages.getString("CreateSandboxProjectDialog.userPasswordEmptyMessage")); //$NON-NLS-1$
        } else {
            setErrorMessage(null);
            checkProjectLabel(false);
        }
    }

    public void setErrorMessage(String newErrorMessage) {
        super.setErrorMessage(newErrorMessage);
        Button button = getButton(IDialogConstants.OK_ID);
        if (button != null && !button.isDisposed()) {
            button.setEnabled(newErrorMessage == null);
        }

    }

    private String generateProjectName() {
        String text = userLoginText.getText();

        if (Pattern.matches(RepositoryConstants.MAIL_PATTERN, text)) {
            int at = text.indexOf('@');
            if (at > -1) {
                String mailName = text.substring(0, at);
                if (mailName.length() > 0) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("Sandbox_"); //$NON-NLS-1$
                    sb.append(mailName);
                    sb.append("_project"); //$NON-NLS-1$
                    return sb.toString();
                }
            }
        }
        return null;
    }

    private void checkProjectLabel(boolean editable) {
        if (editable) {
            projectLabelText.getTextControl().setEditable(true);
        } else {
            projectLabelText.getTextControl().setEditable(false);
        }
    }

    private boolean isProjectNameAlreadyUsed(String newProjectName) {

        if (projects == null) { // need check later.
            return true;
        }
        for (Project project : projects) {
            if (Project.createTechnicalName(newProjectName).compareTo(project.getTechnicalLabel()) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void okPressed() {
        //
        Project newProject = new Project();
        newProject.setLabel(projectLabelText.getText());
        newProject.setTechnicalLabel(Project.createTechnicalName(newProject.getLabel()));
        newProject.setLanguage(ECodeLanguage.getCodeLanguage(languageCombo.getText()));
        newProject.setDescription(newProject.getLabel());

        User newUser = PropertiesFactory.eINSTANCE.createUser();
        newUser.setLogin(userLoginText.getText());
        newUser.setFirstName(userFirstNameText.getText());
        newUser.setLastName(userLastNameText.getText());
        try {
            newUser.setPassword(PasswordHelper.encryptPasswd(userPassText.getText()));
        } catch (NoSuchAlgorithmException e) {
            ExceptionHandler.process(e);
        }

        newProject.setAuthor(newUser); // set in project to record

        boolean ok = false;
        try {
            ok = CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().createSandboxProject(newProject);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            MessageDialog
                    .openError(
                            getShell(),
                            Messages.getString("CreateSandboxProjectDialog.Failure"), Messages.getString("CreateSandboxProjectDialog.failureMessage") //$NON-NLS-1$ //$NON-NLS-2$
                                    + "\n\n" + e.getMessage()); //$NON-NLS-1$
        }
        if (ok) { // if not created, will don't close the dialog
            MessageDialog
                    .openInformation(
                            getShell(),
                            Messages.getString("CreateSandboxProjectDialog.successTitile"), Messages.getString("CreateSandboxProjectDialog.successMessage")); //$NON-NLS-1$ //$NON-NLS-2$
            super.okPressed();
        }
    }

}
