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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
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
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.general.User;
import org.talend.core.model.repository.ERepositoryType;
import org.talend.core.prefs.PreferenceManipulator;
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryFactoryProvider;

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

    private Combo repositoryCombo;

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

    public static final Project NEW_PROJECT = new Project(Messages.getString("LoginComposite.newProject"));

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
        repositoryCombo = new Combo(formBody, SWT.BORDER);
        toolkit.adapt(repositoryCombo);
        GridData repositoryGrid = new GridData(GridData.FILL_HORIZONTAL);
        repositoryGrid.horizontalSpan = 7;
        repositoryCombo.setLayoutData(repositoryGrid);

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

        repositoryCombo.setItems(RepositoryFactoryProvider.getAvailableRepositories());

        // Check number of repository available.
        if (repositoryCombo.getItemCount() == 1) {
            // disable combo box and select it automatically
            repositoryCombo.select(0);
            repositoryCombo.setEnabled(false);
            repositoryCombo.setBackground(GREY_COLOR);
        } else {
            // select last repository used
            selectLast(prefManipulator.getLastRepository(), repositoryCombo);
        }

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
        // PTODO CCA devmode <start>To delete
        if (users.length < 1) {
            users = new String[] { "" };
        }
        // CCA <end>To delete
        userCombo.setItems(users);

        selectLast(prefManipulator.getLastUser(), userCombo);

        if (isSelectedRepositoryLocal()) {
            unpopulateRemoteLoginElements();
        }
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
    private boolean isSelectedRepositoryLocal() {
        return (getRepository().compareTo(ERepositoryType.LOCAL.getLabel()) == 0);
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
                unpopulateProjectList();

                validateFields();

                if (isSelectedRepositoryLocal()) {
                    unpopulateRemoteLoginElements();
                } else {
                    populateRemoteLoginElements();
                }
                dialog.updateButtons();
            }
        };

        repositoryCombo.addModifyListener(modifyListenerServerCombo);
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
                dialog.updateButtons();
            }
        });

        checkBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // Remote Repository mandatory
                ERepositoryType currentRepository = ERepositoryType.getRepository(getRepository());

                RepositoryContext repositoryContext = new RepositoryContext(getServer(), getPortAsNumber(), getContext(),
                        getUser(), currentRepository);

                IRepositoryFactory repositoryFactory = RepositoryFactoryProvider.getInstance(repositoryContext);

                if (validateFields()) {
                    MessageDialog.openInformation(getShell(), Messages.getString("LoginComposite.remoteRepositoryCheck"),
                            repositoryFactory.isServerValid());
                }
            }
        });
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
        ERepositoryType currentRepository = ERepositoryType.getRepository(this.getRepository());
        String server = getServer();
        Integer portAsNumber = getPortAsNumber();
        String context = getContext();
        User user = getUser();
        RepositoryContext repositoryContext = new RepositoryContext(server, portAsNumber, context, user, currentRepository);
        return repositoryContext;
    }

    private void populateProjectList() {

        RepositoryContext repositoryContext = getRepositoryContext();
        IRepositoryFactory repositoryFactory = RepositoryFactoryProvider.getInstance(repositoryContext);

        Project[] projects;
        try {
            projects = repositoryFactory.readProject();
        } catch (PersistenceException e) {
            projects = new Project[0];

            dialog.setErrorMessage(Messages.getString("LoginComposite.refreshFailure1") + e.getMessage()
                    + Messages.getString("LoginComposite.refreshFailure2"));
        }
        if (isSelectedRepositoryLocal()) {
            Project[] prjs = new Project[projects.length + 1];
            System.arraycopy(projects, 0, prjs, 0, projects.length);
            prjs[prjs.length - 1] = NEW_PROJECT;
            projects = prjs;
        }
        projectViewer.setInput(projects);

        // Try to select the last recently used project
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
                projectViewer.setSelection(new StructuredSelection(new Object[] { goodProject }));
            }
        }

        projectViewer.getControl().setEnabled(true);
    }

    public String getRepository() {
        return repositoryCombo.getText();
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
        return new User(userCombo.getText(), passwordText.getText());
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
        boolean serverIsLocal = isSelectedRepositoryLocal();
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
            if (!isSelectedRepositoryLocal()) {
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
