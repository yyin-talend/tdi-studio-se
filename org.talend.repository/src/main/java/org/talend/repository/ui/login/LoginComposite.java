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
package org.talend.repository.ui.login;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.User;
import org.talend.core.prefs.PreferenceManipulator;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.ui.ERepositoryImages;
import org.talend.repository.ui.actions.ImportProjectsAction;
import org.talend.repository.ui.wizards.newproject.NewProjectWizard;

/**
 * Composite login.<br/>
 * 
 * $Id$
 * 
 */
public class LoginComposite extends Composite {

    /**
     * Colors used for Remote Object background when enabled.
     */
    private static final Color WHITE_COLOR = new Color(null, 255, 255, 255);

    private static final Color GREY_COLOR = new Color(null, 200, 200, 200);

    private FormToolkit toolkit;

    private LoginDialog dialog;

    private ComboViewer repositoryCombo;

    private Combo serverCombo;

    private Combo contextCombo;

    private Combo portCombo;

    private Combo userCombo;

    private Text passwordText;

    private ComboViewer projectViewer;

    private Image checkImage;

    private Image fillProjectsImage;

    private Button fillProjectsBtn;

    private Button checkBtn;

    private Button newProjectButton;

    private Button importProjectsButton;

    public static final Project NEW_PROJECT = new Project(Messages.getString("LoginComposite.newProject"));

    public static final Project IMPORT_PROJECTS = new Project("<Import projects>");

    /**
     * Constructs a new LoginComposite.
     * 
     * @param parent Parent component.
     * @param style Style bits.
     */
    public LoginComposite(Composite parent, int style, LoginDialog dialog) {
        super(parent, style);

        this.dialog = dialog;

        toolkit = new FormToolkit(this.getDisplay());
        Form form = toolkit.createForm(this);
        Composite formBody = form.getBody();

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        form.setLayoutData(new GridData(GridData.FILL_BOTH));

        layout = new GridLayout(9, false);
        formBody.setLayout(layout);

        // Blank
        Label blankLabel = toolkit.createLabel(formBody, null);
        GridData blankGrid = new GridData(GridData.FILL_HORIZONTAL);
        blankGrid.horizontalSpan = 9;
        blankLabel.setLayoutData(blankGrid);

        // Line 1
        // Repository
        toolkit.createLabel(formBody, Messages.getString("LoginComposite.repository"));
        repositoryCombo = new ComboViewer(formBody, SWT.BORDER | SWT.READ_ONLY);
        toolkit.adapt(repositoryCombo.getCombo());
        repositoryCombo.setContentProvider(new ArrayContentProvider());
        repositoryCombo.setLabelProvider(new RepositoryFactoryLabelProvider());

        GridData repositoryGrid = new GridData(GridData.FILL_HORIZONTAL);
        repositoryGrid.horizontalSpan = 7;
        repositoryCombo.getControl().setLayoutData(repositoryGrid);

        // Blank
        toolkit.createLabel(formBody, null);

        // Line 2
        // Server
        toolkit.createLabel(formBody, Messages.getString("LoginComposite.server"));
        serverCombo = new Combo(formBody, SWT.BORDER);
        toolkit.adapt(serverCombo);
        GridData serverGrid = new GridData(GridData.FILL_HORIZONTAL);
        serverGrid.horizontalSpan = 4;
        serverCombo.setLayoutData(serverGrid);

        // Port
        toolkit.createLabel(formBody, Messages.getString("LoginComposite.port"));
        portCombo = new Combo(formBody, SWT.FILL);
        toolkit.adapt(portCombo);
        GridData portGrid = new GridData(50, 20);
        portGrid.horizontalSpan = 2;
        portCombo.setLayoutData(portGrid);

        // Check Server Availability Button
        checkBtn = toolkit.createButton(formBody, null, SWT.PUSH);
        checkBtn.setToolTipText(Messages.getString("LoginComposite.checkServerHint"));
        ImageDescriptor imgDesc = RepositoryPlugin.imageDescriptorFromPlugin(RepositoryPlugin.PLUGIN_ID, "icons/checkserver.gif");
        if (imgDesc != null) {
            checkImage = imgDesc.createImage();
            checkBtn.setImage(checkImage);
        }
        GridData checkGrid = new GridData(SWT.BORDER | SWT.RIGHT);
        checkGrid.verticalSpan = 2;
        checkBtn.setLayoutData(checkGrid);

        // Line 3
        // Context
        toolkit.createLabel(formBody, Messages.getString("LoginComposite.context"));
        contextCombo = new Combo(formBody, SWT.BORDER);
        toolkit.adapt(contextCombo);
        GridData contextGrid = new GridData(GridData.FILL_HORIZONTAL);
        contextGrid.horizontalSpan = 7;
        contextCombo.setLayoutData(contextGrid);

        // Line 4
        // User
        toolkit.createLabel(formBody, Messages.getString("LoginComposite.username"));
        userCombo = new Combo(formBody, SWT.BORDER);
        toolkit.adapt(userCombo);
        GridData userGrid = new GridData(GridData.FILL_HORIZONTAL);
        userGrid.horizontalSpan = 7;
        userCombo.setLayoutData(userGrid);

        // Blank
        toolkit.createLabel(formBody, null);

        // Line 5
        // Password
        toolkit.createLabel(formBody, Messages.getString("LoginComposite.password"));
        passwordText = toolkit.createText(formBody, "", SWT.PASSWORD | SWT.BORDER);
        GridData passwordGrid = new GridData(GridData.FILL_HORIZONTAL);
        passwordGrid.horizontalSpan = 7;
        passwordText.setLayoutData(passwordGrid);

        // Blank
        toolkit.createLabel(formBody, null);

        // Line 6
        // Project
        toolkit.createLabel(formBody, Messages.getString("LoginComposite.project"));
        projectViewer = new ComboViewer(formBody, SWT.BORDER | SWT.READ_ONLY);
        toolkit.adapt(projectViewer.getCombo());
        GridData projectGrid = new GridData(GridData.FILL_HORIZONTAL);
        projectGrid.horizontalSpan = 7;
        projectViewer.getControl().setLayoutData(projectGrid);
        projectViewer.setContentProvider(new ArrayContentProvider());
        projectViewer.setLabelProvider(new ProjectLabelProvider());

        // Fill projects
        fillProjectsBtn = toolkit.createButton(formBody, null, SWT.PUSH);
        fillProjectsBtn.setToolTipText(Messages.getString("LoginComposite.refresh"));
        if (imgDesc != null) {
            fillProjectsImage = ImageProvider.getImage(EImage.REFRESH_ICON);
            fillProjectsBtn.setImage(fillProjectsImage);
        }
        GridData fillGrid = new GridData();
        fillGrid.horizontalSpan = 1;
        fillProjectsBtn.setLayoutData(fillGrid);

        // Blank
        // toolkit.createLabel(formBody, null);

        Composite bottomButtons = toolkit.createComposite(formBody);
        // bottomButtons.setBackground(new Color(null,255,0,0));
        GridData fillGrid2 = new GridData(GridData.FILL_HORIZONTAL);
        fillGrid2.horizontalSpan = 9;
        bottomButtons.setLayoutData(fillGrid2);
        bottomButtons.setLayout(new FormLayout());

        newProjectButton = toolkit.createButton(bottomButtons, null, SWT.PUSH);
        newProjectButton.setText(Messages.getString("LoginComposite.newProject"));
        newProjectButton.setToolTipText("Create a new project");
        newProjectButton.setImage(ImageProvider.getImage(ERepositoryImages.NEW_PROJECT_ACTION));
        FormData formData = new FormData();
        formData.left = new FormAttachment(0);
        newProjectButton.setLayoutData(formData);

        importProjectsButton = toolkit.createButton(bottomButtons, null, SWT.PUSH);
        ImportProjectsAction ipa = ImportProjectsAction.getInstance();
        importProjectsButton.setText(ipa.getText());
        importProjectsButton.setToolTipText(ipa.getToolTipText());
        importProjectsButton.setImage(ImageProvider.getImage(ipa.getImageDescriptor()));
        formData = new FormData();
        formData.left = new FormAttachment(newProjectButton, 5);
        importProjectsButton.setLayoutData(formData);

        fillContents();
        addListeners();
    }

    /**
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        if (checkImage != null) {
            checkImage.dispose();
            checkImage = null;
        }

        toolkit.dispose();

        super.dispose();
    }

    private void fillContents() {
        PreferenceManipulator prefManipulator = new PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());

        List<IRepositoryFactory> availableRepositories = RepositoryFactoryProvider.getAvailableRepositories();
        repositoryCombo.setInput(availableRepositories);

        // Check number of repository available.
        if (availableRepositories.size() == 1) {
            // disable combo box and select it automatically
            repositoryCombo.setSelection(new StructuredSelection(new Object[] { availableRepositories.get(0) }));
            repositoryCombo.getControl().setEnabled(false);
            repositoryCombo.getControl().setBackground(GREY_COLOR);
        } else {
            // select last repository used
            String className = prefManipulator.getLastRepository();
            boolean selected = false;
            for (IRepositoryFactory curent : availableRepositories) {
                if (curent.getClass().getName().equals(className)) {
                    selectLast(curent.toString(), repositoryCombo.getCombo());
                    selected = true;
                }
            }
            if (!selected) {
                repositoryCombo.setSelection(new StructuredSelection(new Object[] { availableRepositories.get(0) }));
            }
        }
        ProxyRepositoryFactory.getInstance().setRepositoryFactoryFromProvider(getRepository());

        String[] servers = prefManipulator.readServers();
        serverCombo.setItems(servers);
        selectLast(prefManipulator.getLastServer(), serverCombo);

        String[] contexts = prefManipulator.readContexts();
        contextCombo.setItems(contexts);
        selectLast(prefManipulator.getLastContext(), contextCombo);

        String[] ports = prefManipulator.readPorts();
        portCombo.setItems(ports);
        selectLast(prefManipulator.getLastPort(), portCombo);

        projectViewer.getControl().setEnabled(false);

        String[] users = prefManipulator.readUsers();
        if (users.length < 1) {
            users = new String[] { "" };
        }
        userCombo.setItems(users);

        selectLast(prefManipulator.getLastUser(), userCombo);

        if (!isAuthenticationNeeded()) {
            unpopulateRemoteLoginElements();
        }

        setRepositoryContextInContext();
    }

    /**
     * If setted, Select last ? used in PreferenceStore.
     * 
     * @param prefManipulator
     */
    private void selectLast(String lastObjectSelected, Combo comboToSelect) {
        if (lastObjectSelected != null) {
            int userIndex = -1;
            String[] items = comboToSelect.getItems();
            for (int i = 0; userIndex == -1 && i < items.length; i++) {
                if (lastObjectSelected.equals(items[i])) {
                    userIndex = i;
                }
            }
            if (userIndex != -1) {
                comboToSelect.select(userIndex);
            } else {
                comboToSelect.select(0);
            }
        }
    }

    /**
     * 
     * @return
     */
    protected boolean isAuthenticationNeeded() {
        IRepositoryFactory repository = getRepository();
        if (repository == null) {
            return false;
        }
        return repository.isAuthenticationNeeded();
    }

    private void addListeners() {
        ModifyListener modifyListener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                unpopulateProjectList();
                dialog.updateButtons();
            }
        };

        ModifyListener modifyListenerServerCombo = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                updateServerFields();
            }

        };

        repositoryCombo.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                updateServerFields();
            }
        });
        serverCombo.addModifyListener(modifyListenerServerCombo);
        portCombo.addModifyListener(modifyListenerServerCombo);
        contextCombo.addModifyListener(modifyListenerServerCombo);
        userCombo.addModifyListener(modifyListenerServerCombo);
        passwordText.addModifyListener(modifyListener);

        fillProjectsBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // Validate data
                if (validateFields()) {
                    populateProjectList();
                    validateProject();
                }
            }
        });

        projectViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                PreferenceManipulator prefManipulator = new PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
                prefManipulator.setLastProject(getProject().getLabel());
                dialog.updateButtons();
                setRepositoryContextInContext();
            }
        });

        checkBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

                if (validateFields()) {
                    MessageDialog.openInformation(getShell(), Messages.getString("LoginComposite.remoteRepositoryCheck"),
                            repositoryFactory.isServerValid());
                }
            }
        });

        newProjectButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Project project = null;
                NewProjectWizard newPrjWiz = new NewProjectWizard();
                WizardDialog newProjectDialog = new WizardDialog(getShell(), newPrjWiz);
                newProjectDialog.setTitle(Messages.getString("LoginDialog.newProjectTitle")); //$NON-NLS-1$
                if (newProjectDialog.open() == WizardDialog.OK) {
                    project = newPrjWiz.getProject();
                    populateProjectList();
                    selectProject(project);
                }
            }
        });

        importProjectsButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ImportProjectsAction.getInstance().run();
                populateProjectList();
            }
        });
    }

    /**
     * DOC smallet Comment method "updateServerFields".
     */
    private void updateServerFields() {
        ProxyRepositoryFactory.getInstance().setRepositoryFactoryFromProvider(getRepository());

        setRepositoryContextInContext();

        validateFields();

        if (isAuthenticationNeeded()) {
            unpopulateProjectList();
            populateRemoteLoginElements();
        } else {
            unpopulateRemoteLoginElements();
        }
        dialog.updateButtons();
    }

    /**
     * DOC mhirt Comment method "checkPortIsANumber".
     * 
     * @return
     */
    private boolean checkPortIsNumber() {
        if (getPortAsNumber() != null) {
            return true;
        }
        return false;
    }

    private void unpopulateProjectList() {
        projectViewer.setInput(null);
        projectViewer.getControl().setEnabled(false);
    }

    public RepositoryContext getRepositoryContext() {
        String server = getServer();
        Integer portAsNumber = getPortAsNumber();
        String context = getContext();
        User user = getUser();
        RepositoryContext repositoryContext = new RepositoryContext(server, portAsNumber, context, user);
        repositoryContext.setProject(getProject());
        return repositoryContext;
    }

    private void setRepositoryContextInContext() {
        Context ctx = CorePlugin.getContext();
        ctx.putProperty(Context.REPOSITORY_CONTEXT_KEY, getRepositoryContext());
    }

    protected void populateProjectList() {
        ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        repositoryFactory.setRepositoryFactoryFromProvider(getRepository());
        repositoryFactory.initialize();

        Project[] projects;
        try {
            projects = repositoryFactory.readProject();
        } catch (PersistenceException e) {
            projects = new Project[0];

            dialog.setErrorMessage(Messages.getString("LoginComposite.refreshFailure1") + e.getMessage()
                    + Messages.getString("LoginComposite.refreshFailure2"));
        }
        projectViewer.setInput(projects);

        // Try to select the last recently used project
        selectLastUsedProject();

        projectViewer.getControl().setEnabled(true);
    }

    /**
     * DOC smallet Comment method "selectLastUsedProject".
     * 
     * @param projects
     */
    private void selectLastUsedProject() {
        Project[] projects = (Project[]) projectViewer.getInput();
        PreferenceManipulator prefManipulator = new PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
        String lastProject = prefManipulator.getLastProject();
        if (lastProject != null) {
            Project goodProject = null;
            for (int i = 0; goodProject == null && i < projects.length; i++) {
                if (lastProject.equals(projects[i].getLabel())) {
                    goodProject = projects[i];
                }
            }

            if (goodProject == null && projects.length > 0) {
                goodProject = projects[0];
            }

            if (goodProject != null) {
                selectProject(goodProject);
            }
        }
    }

    /**
     * DOC smallet Comment method "selectProject".
     * 
     * @param goodProject
     */
    private void selectProject(Project goodProject) {
        projectViewer.setSelection(new StructuredSelection(new Object[] { goodProject }));
        setRepositoryContextInContext();
    }

    public IRepositoryFactory getRepository() {
        IRepositoryFactory repositoryFactory = null;
        IStructuredSelection sel = (IStructuredSelection) repositoryCombo.getSelection();
        repositoryFactory = (IRepositoryFactory) sel.getFirstElement();
        return repositoryFactory;
    }

    public String getServer() {
        return serverCombo.getText();
    }

    public String getContext() {
        return contextCombo.getText();
    }

    public String getPort() {
        return portCombo.getText();
    }

    /**
     * DOC mhirt Comment method "getPortAsNumber".
     * 
     * @return
     */
    public Integer getPortAsNumber() {
        try {
            return Integer.parseInt(portCombo.getText());
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public User getUser() {
        User toReturn = PropertiesFactory.eINSTANCE.createUser();
        toReturn.setId(1);
        toReturn.setLogin(userCombo.getText());
        toReturn.setPassword(passwordText.getText());
        return toReturn;
    }

    public Project getProject() {
        Project project = null;
        if (!projectViewer.getSelection().isEmpty()) {
            IStructuredSelection sel = (IStructuredSelection) projectViewer.getSelection();
            project = (Project) sel.getFirstElement();
        }
        return project;
    }

    public boolean canFinish() {
        return validateFields() && validateProject();
    }

    /**
     * Label provider for Projects. <br/>
     * 
     * $Id$
     * 
     */
    private class ProjectLabelProvider extends LabelProvider {

        /**
         * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
         */
        @Override
        public String getText(Object element) {
            Project prj = (Project) element;
            return prj.getLabel();
        }
    }

    /**
     * DOC smallet LoginComposite class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private class RepositoryFactoryLabelProvider extends LabelProvider {

        /**
         * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
         */
        @Override
        public String getText(Object element) {
            IRepositoryFactory prj = (IRepositoryFactory) element;
            return prj.getName();
        }
    }

    /**
     * clear login values.
     */
    private void unpopulateRemoteLoginElements() {
        serverCombo.setEnabled(false);
        serverCombo.setBackground(GREY_COLOR);
        contextCombo.setEnabled(false);
        contextCombo.setBackground(GREY_COLOR);
        portCombo.setEnabled(false);
        portCombo.setBackground(GREY_COLOR);
        passwordText.setText("");
        passwordText.setEnabled(false);
        passwordText.setEditable(false);
        passwordText.setBackground(GREY_COLOR);
        checkBtn.setEnabled(false);
        populateProjectList();
    }

    /**
     * fill login valueswith default elements.
     */
    private void populateRemoteLoginElements() {
        serverCombo.setEnabled(true);
        serverCombo.setBackground(WHITE_COLOR);
        contextCombo.setEnabled(true);
        contextCombo.setBackground(WHITE_COLOR);
        portCombo.setEnabled(true);
        portCombo.setBackground(WHITE_COLOR);
        passwordText.setEnabled(true);
        passwordText.setEditable(true);
        passwordText.setBackground(WHITE_COLOR);
        checkBtn.setEnabled(true);

        if (userCombo.getText().length() == 0) {
            PreferenceManipulator prefManipulator = new PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
            selectLast(prefManipulator.getLastUser(), userCombo);
        }
        unpopulateProjectList();
    }

    private boolean validateFields() {
        String errorMsg = null;
        boolean valid = true;
        boolean serverIsLocal = !isAuthenticationNeeded();
        if (valid && !serverIsLocal && getServer().length() == 0) {
            valid = false;
            errorMsg = Messages.getString("LoginComposite.serverEmpty");
        }
        if (valid && !serverIsLocal && userCombo.getText().length() == 0) {
            valid = false;
            errorMsg = Messages.getString("LoginComposite.usernameEmpty");
        } else if (valid && !serverIsLocal && getPort().length() == 0) {
            valid = false;
            errorMsg = Messages.getString("LoginComposite.portEmpty");
        } else if (valid && !serverIsLocal && !checkPortIsNumber()) {
            valid = false;
            errorMsg = Messages.getString("LoginComposite.portNotANumber");
        } else if (valid && !Pattern.matches(RepositoryConstants.MAIL_PATTERN, getUser().getLogin())) {
            valid = false;
            errorMsg = Messages.getString("LoginComposite.usernameMail");
        }
        if (valid && !serverIsLocal && passwordText.getText().length() == 0) {
            valid = false;
            errorMsg = Messages.getString("LoginComposite.passwordEmpty");
        }

        if (!valid) {
            dialog.setErrorMessage(errorMsg);
            checkBtn.setEnabled(false);
        } else {
            dialog.setErrorMessage(null);
            if (isAuthenticationNeeded()) {
                checkBtn.setEnabled(true);
            }
        }
        return valid;
    }

    private boolean validateProject() {
        String errorMsg = null;
        boolean valid = true;
        if (projectViewer.getCombo().getText().length() == 0) {
            valid = false;
            errorMsg = Messages.getString("LoginComposite.projectEmpty");
        }

        if (!valid) {
            dialog.setErrorMessage(errorMsg);
        } else {
            dialog.setErrorMessage(null);
        }
        return valid;
    }
}
