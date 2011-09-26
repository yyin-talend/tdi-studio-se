// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.system.EnvironmentUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.model.general.ConnectionBean;
import org.talend.core.model.general.Project;
import org.talend.core.prefs.PreferenceManipulator;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.TalendBrowserLaunchHelper;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.actions.importproject.ImportDemoProjectAction;
import org.talend.repository.ui.actions.importproject.ImportProjectAsAction;
import org.talend.repository.ui.actions.importproject.SelectDeleteProjectDialog;
import org.talend.repository.ui.login.connections.ConnectionUserPerReader;
import org.talend.repository.ui.wizards.newproject.NewProjectWizard;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class TOSLoginComposite extends Composite {

    private FormToolkit toolkit;

    private LoginComposite loginComposite;

    public static final Color WHITE_COLOR = new Color(null, 255, 255, 255);

    public static final Color GREY_COLOR = new Color(null, 215, 215, 215);

    public static final Color YELLOW_GREEN_COLOR = new Color(null, 159, 181, 38);// 143, 163, 35

    public static final Color YELLOW_COLOR = new Color(null, 255, 173, 37);// 254, 182, 84

    private static final Color RED_COLOR = new Color(null, new RGB(240, 0, 0));// 255

    private Composite formBody;

    private Composite repositoryComposite;

    private Composite separatorComposite;

    private Composite tosActionComposite;

    private List projectList;

    private Button openButton;

    private Button deleteButton;

    private Button createButton;

    private Button importButton;

    private Button demoProjectButton;

    private Composite tosWorkspaceComposite;

    private Button changeButton;

    private Hyperlink repositoryHyperlink;

    private Map projectsMap = new HashMap();

    private Button restartBut;

    private LoginDialog dialog;

    private Text workspaceText;

    private ConnectionUserPerReader perReader = null;

    private String oldPath;

    /**
     * DOC Administrator TOSLoginComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public TOSLoginComposite(Composite parent, int style, LoginComposite loginComposite, LoginDialog dialog) {
        super(parent, style);
        this.loginComposite = loginComposite;
        this.dialog = dialog;

        perReader = ConnectionUserPerReader.getInstance();

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        toolkit = new FormToolkit(this.getDisplay());
        toolkit = new FormToolkit(this.getDisplay());
        Form form = toolkit.createForm(this);
        formBody = form.getBody();
        formBody.setBackgroundMode(SWT.INHERIT_DEFAULT);
        GridData formBodyGd = new GridData(GridData.FILL_BOTH);
        form.setLayoutData(formBodyGd);
        GridLayout createLayout = createLayout(1);
        createLayout.verticalSpacing = 0;
        createLayout.horizontalSpacing = 0;
        createLayout.marginHeight = 0;
        createLayout.marginWidth = 0;
        formBody.setLayout(createLayout);
        createTosRepositoryArea(formBody);
        createSeparator(formBody);
        createTosActionArea(formBody);
        createTosWorkspaceArea(formBody);
        addListener();
    }

    private GridLayout createLayout(int numColumns) {
        GridLayout layout = new GridLayout(numColumns, false);
        if (!EnvironmentUtils.isWindowsSystem()) {
            layout.marginHeight = 0;
            layout.verticalSpacing = 0;
        }
        return layout;
    }

    private void createTosRepositoryArea(Composite parent) {
        repositoryComposite = toolkit.createComposite(parent);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 40;
        repositoryComposite.setLayoutData(gd);
        repositoryComposite.setLayout(new FormLayout());
        repositoryComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
        repositoryComposite.setBackground(GREY_COLOR);

        Label welcomeLabel = toolkit.createLabel(repositoryComposite, Messages.getString("TOSLoginComposite.welcomeTitle")); //$NON-NLS-1$
        welcomeLabel.setBackground(repositoryComposite.getBackground());
        FormData welcomeLabelFormData = new FormData();
        welcomeLabelFormData.top = new FormAttachment(0, 7);
        welcomeLabelFormData.left = new FormAttachment(0, 10);
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            welcomeLabelFormData.right = new FormAttachment(0, 350);
        } else if (Platform.getOS().equals(Platform.OS_LINUX)) {
            welcomeLabelFormData.right = new FormAttachment(0, 370);
        } else {
            welcomeLabelFormData.right = new FormAttachment(0, 370);
        }
        welcomeLabelFormData.bottom = new FormAttachment(100, 0);
        welcomeLabel.setLayoutData(welcomeLabelFormData);
    }

    private void createSeparator(Composite parent) {
        separatorComposite = toolkit.createComposite(parent);
        separatorComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
        separatorComposite.setBackground(GREY_COLOR); // parent.getBackground()
        separatorComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout();
        layout.marginTop = 10;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.marginBottom = 0;
        separatorComposite.setLayout(layout);
        Label separatorLabel = toolkit.createSeparator(separatorComposite, SWT.HORIZONTAL | SWT.COLOR_DARK_GRAY);
        separatorLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        separatorLabel.setBackground(GREY_COLOR);
    }

    private void createTosActionArea(Composite parent) {
        tosActionComposite = toolkit.createComposite(parent);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 140;
        tosActionComposite.setLayoutData(gd);
        tosActionComposite.setLayout(new FormLayout());
        tosActionComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
        tosActionComposite.setBackground(GREY_COLOR);

        FormData data;

        Label projectLabel = toolkit.createLabel(tosActionComposite, Messages.getString("TOSLoginComposite.projectLabel"));
        GC gc = new GC(projectLabel);
        Point labelSize = gc.stringExtent(Messages.getString("TOSLoginComposite.projectLabel"));
        gc.dispose();
        data = new FormData();
        data.top = new FormAttachment(0, 10);
        data.left = new FormAttachment(0, 20);
        data.right = new FormAttachment(0, 20 + labelSize.x);
        data.bottom = new FormAttachment(0, 30);
        projectLabel.setBackground(tosActionComposite.getBackground());
        projectLabel.setLayoutData(data);

        openButton = toolkit.createButton(tosActionComposite, null, SWT.PUSH);
        data = new FormData();
        data.top = new FormAttachment(projectLabel, 0, SWT.TOP);
        data.left = new FormAttachment(100, -75);
        data.right = new FormAttachment(100, -10);
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            data.bottom = new FormAttachment(projectLabel, 0, SWT.BOTTOM);
        } else if (Platform.getOS().equals(Platform.OS_LINUX)) {
            data.bottom = new FormAttachment(projectLabel, 5, SWT.BOTTOM);
        } else {
            data.bottom = new FormAttachment(projectLabel, 5, SWT.BOTTOM);
        }
        openButton.setText(Messages.getString("TOSLoginComposite.openButton"));
        openButton.setLayoutData(data);

        projectList = new List(tosActionComposite, SWT.BORDER | SWT.V_SCROLL);
        data = new FormData();
        data.top = new FormAttachment(projectLabel, 0, SWT.TOP);
        data.left = new FormAttachment(projectLabel, 10, SWT.RIGHT);
        data.right = new FormAttachment(openButton, -10, SWT.LEFT);
        data.bottom = new FormAttachment(0, 100);
        projectList.setLayoutData(data);

        deleteButton = toolkit.createButton(tosActionComposite, null, SWT.PUSH);
        data = new FormData();
        data.top = new FormAttachment(openButton, 5, SWT.BOTTOM);
        data.left = new FormAttachment(openButton, 0, SWT.LEFT);
        data.right = new FormAttachment(openButton, 0, SWT.RIGHT);
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            data.height = 20;
        } else if (Platform.getOS().equals(Platform.OS_LINUX)) {
            data.height = 25;
        } else {
            data.height = 25;
        }
        deleteButton.setText(Messages.getString("TOSLoginComposite.deleteButton"));
        deleteButton.setLayoutData(data);

        createButton = toolkit.createButton(tosActionComposite, null, SWT.PUSH);
        data = new FormData();
        data.top = new FormAttachment(projectList, 10, SWT.BOTTOM);
        data.left = new FormAttachment(projectList, 0, SWT.LEFT);
        data.right = new FormAttachment(projectList, 65, SWT.LEFT);
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            data.height = 20;
        } else if (Platform.getOS().equals(Platform.OS_LINUX)) {
            data.height = 25;
        } else {
            data.height = 25;
        }
        createButton.setText(Messages.getString("TOSLoginComposite.createButton"));
        createButton.setLayoutData(data);

        importButton = toolkit.createButton(tosActionComposite, null, SWT.PUSH);
        data = new FormData();
        data.top = new FormAttachment(createButton, 0, SWT.TOP);
        data.left = new FormAttachment(createButton, 10, SWT.RIGHT);
        data.right = new FormAttachment(createButton, 75, SWT.RIGHT);
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            data.height = 20;
        } else if (Platform.getOS().equals(Platform.OS_LINUX)) {
            data.height = 25;
        } else {
            data.height = 25;
        }
        importButton.setText(Messages.getString("TOSLoginComposite.importButton"));
        importButton.setLayoutData(data);

        demoProjectButton = toolkit.createButton(tosActionComposite, null, SWT.PUSH);
        data = new FormData();
        data.top = new FormAttachment(importButton, 0, SWT.TOP);
        data.left = new FormAttachment(importButton, 10, SWT.RIGHT);
        data.right = new FormAttachment(importButton, 125, SWT.RIGHT);
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            data.height = 20;
        } else if (Platform.getOS().equals(Platform.OS_LINUX)) {
            data.height = 25;
        } else {
            data.height = 25;
        }
        demoProjectButton.setText(Messages.getString("TOSLoginComposite.demoProjectButton"));
        demoProjectButton.setLayoutData(data);

    }

    private void createTosWorkspaceArea(Composite parent) {
        tosWorkspaceComposite = toolkit.createComposite(parent);
        tosWorkspaceComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        tosWorkspaceComposite.setLayout(new FormLayout());
        tosWorkspaceComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);

        FormData data;

        Label workSpaceLabel = toolkit.createLabel(tosWorkspaceComposite, Messages.getString("TOSLoginComposite.workspaceLabel"));
        GC gc = new GC(workSpaceLabel);
        Point labelSize = gc.stringExtent(Messages.getString("TOSLoginComposite.workspaceLabel"));
        gc.dispose();
        data = new FormData();
        data.top = new FormAttachment(0, 10);
        data.left = new FormAttachment(0, 10);
        data.right = new FormAttachment(0, 10 + labelSize.x);
        data.bottom = new FormAttachment(0, 30);
        workSpaceLabel.setLayoutData(data);

        changeButton = toolkit.createButton(tosWorkspaceComposite, null, SWT.PUSH);
        data = new FormData();
        data.top = new FormAttachment(workSpaceLabel, 0, SWT.TOP);
        data.left = new FormAttachment(100, -75);
        data.right = new FormAttachment(100, -10);
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            data.bottom = new FormAttachment(workSpaceLabel, 0, SWT.BOTTOM);
        } else if (Platform.getOS().equals(Platform.OS_LINUX)) {
            data.bottom = new FormAttachment(workSpaceLabel, 5, SWT.BOTTOM);
        } else {
            data.bottom = new FormAttachment(workSpaceLabel, 5, SWT.BOTTOM);
        }
        changeButton.setText(Messages.getString("TOSLoginComposite.changeButton"));
        changeButton.setLayoutData(data);

        workspaceText = toolkit.createText(tosWorkspaceComposite, null, SWT.READ_ONLY | SWT.BORDER);
        workspaceText.setText(loginComposite.getConnection().getWorkSpace());
        oldPath = loginComposite.getConnection().getWorkSpace();
        data = new FormData();
        data.width = 200;
        data.top = new FormAttachment(workSpaceLabel, 0, SWT.TOP);
        data.left = new FormAttachment(workSpaceLabel, 10, SWT.RIGHT);
        data.right = new FormAttachment(changeButton, -10, SWT.LEFT);
        data.bottom = new FormAttachment(changeButton, 0, SWT.BOTTOM);
        workspaceText.setLayoutData(data);

        repositoryHyperlink = toolkit.createHyperlink(tosWorkspaceComposite,
                Messages.getString("LoginComposite.sharedRepositoryMessage"), SWT.NONE);
        gc = new GC(repositoryHyperlink);
        labelSize = gc.stringExtent(Messages.getString("LoginComposite.sharedRepositoryMessage"));
        gc.dispose();
        data = new FormData();
        data.top = new FormAttachment(workspaceText, 10, SWT.BOTTOM);
        data.left = new FormAttachment(workspaceText, 0, SWT.LEFT);
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            data.right = new FormAttachment(workspaceText, 10 + labelSize.x, SWT.LEFT);
        } else if (Platform.getOS().equals(Platform.OS_LINUX)) {
            data.right = new FormAttachment(workspaceText, 20 + labelSize.x, SWT.LEFT);
        } else {
            data.right = new FormAttachment(workspaceText, 20 + labelSize.x, SWT.LEFT);
        }
        data.bottom = new FormAttachment(workspaceText, 30, SWT.BOTTOM);
        repositoryHyperlink.setLayoutData(data);
        repositoryHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                String url = "http://www.talend.com/products-data-integration/sharedRepository.php"; //$NON-NLS-1$
                TalendBrowserLaunchHelper.openURL(url);
            }
        });

        restartBut = toolkit.createButton(tosWorkspaceComposite, Messages.getString("LoginComposite.RESTART"), SWT.PUSH); //$NON-NLS-1$
        restartBut.setVisible(false);
        FormData formData = new FormData();
        formData.top = new FormAttachment(100, -20);
        formData.left = new FormAttachment(100, -60);
        formData.right = new FormAttachment(100, -5);
        formData.bottom = new FormAttachment(100, 0);
        restartBut.setLayoutData(formData);
        restartBut.setVisible(false);
    }

    public Project[] readProject() {
        ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        Project[] projects = null;
        try {
            projects = repositoryFactory.readProject();
        } catch (PersistenceException e1) {
            e1.printStackTrace();
        } catch (BusinessException e1) {
            e1.printStackTrace();
        }
        return projects;
    }

    private void addListener() {
        createButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Project project = null;
                ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
                Project[] projects = null;
                try {
                    projects = repositoryFactory.readProject();
                } catch (PersistenceException e1) {
                    e1.printStackTrace();
                } catch (BusinessException e1) {
                    e1.printStackTrace();
                }
                NewProjectWizard newPrjWiz = new NewProjectWizard((Project[]) projects);
                WizardDialog newProjectDialog = new WizardDialog(getShell(), newPrjWiz);
                newProjectDialog.setTitle(Messages.getString("LoginDialog.newProjectTitle")); //$NON-NLS-1$
                if (newProjectDialog.open() == Window.OK) {
                    project = newPrjWiz.getProject();
                    if (!projectsMap.containsKey(project.getLabel().toUpperCase())) {
                        projectsMap.put(project.getLabel().toUpperCase(), project);
                        projectList.add(project.getLabel().toUpperCase());
                    }
                }
            }
        });

        deleteButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Shell activeShell = Display.getCurrent().getActiveShell();
                SelectDeleteProjectDialog dialog = new SelectDeleteProjectDialog(activeShell, true);
                if (dialog.open() == Dialog.OK) {
                    CorePlugin.getDefault().getRepositoryLocalProviderService().resetXmiResourceSet();
                    java.util.List<Object> delList = dialog.getDelList();
                    if (delList.size() != 0) {
                        for (Object obj : delList) {
                            if (obj instanceof IProject) {
                                IProject p = (IProject) obj;
                                if (projectsMap.containsKey(p.getName())) {
                                    projectsMap.remove(p.getName());
                                    projectList.remove(p.getName());
                                }
                            }
                        }
                    }
                }
            }
        });

        importButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ImportProjectAsAction.getInstance().run();
                String newProject = ImportProjectAsAction.getInstance().getProjectName();
                if (newProject != null) {
                    Project project = null;
                    ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
                    Project[] projects = null;
                    try {
                        projects = repositoryFactory.readProject();
                    } catch (PersistenceException e1) {
                        e1.printStackTrace();
                    } catch (BusinessException e1) {
                        e1.printStackTrace();
                    }
                    if (!projectsMap.containsKey(newProject.toUpperCase())) {
                        for (int i = 0; i < projects.length; i++) {
                            if (projects[i].getLabel().toUpperCase().equals(newProject.toUpperCase())) {
                                projectsMap.put(newProject.toUpperCase(), project);
                                projectList.add(newProject.toUpperCase().toUpperCase());
                            }
                        }
                    }
                }
            }
        });
        changeButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dirDialog = new DirectoryDialog(dialog.getShell());
                String path = dirDialog.open();
                if (path == null || "".equals(path)) { //$NON-NLS-1$
                    workspaceText.setText(getRecentWorkSpace());
                    loginComposite.getConnection().setWorkSpace(getRecentWorkSpace());
                } else {
                    workspaceText.setText(path);
                    loginComposite.getConnection().setWorkSpace(path);
                    if (!path.equals(oldPath)) {
                        oldPath = path;
                        restartBut.setVisible(true);
                        openButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                        createButton.setEnabled(false);
                        importButton.setEnabled(false);
                        demoProjectButton.setEnabled(false);
                        changeButton.setEnabled(false);
                    }
                }
                PreferenceManipulator prefManipulator = new PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
                java.util.List<ConnectionBean> list = new ArrayList<ConnectionBean>();
                list.add(loginComposite.getConnection());
                prefManipulator.saveConnections(list);
                loginComposite.storedConnections = list;
                perReader.saveConnections(loginComposite.storedConnections);
            }
        });
        restartBut.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                LoginComposite.isRestart = true;
                perReader.saveLastConnectionBean(loginComposite.getConnection());
                dialog.okPressed();
            }
        });
        openButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Context ctx = CorePlugin.getContext();
                ctx.putProperty(Context.REPOSITORY_CONTEXT_KEY, loginComposite.getRepositoryContext());
                if (projectList.getSelectionCount() > 0) {
                    String selection = projectList.getSelection()[0];
                    if (selection != null && !selection.equals("")) {
                        Project project = (Project) projectsMap.get(selection);
                        dialog.logIn(project);
                        dialog.okPressed();
                    }
                }
            }
        });
        demoProjectButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ImportDemoProjectAction.getInstance().setShell(getShell());
                ImportDemoProjectAction.getInstance().run();
                String newProject = ImportDemoProjectAction.getInstance().getProjectName();
                if (newProject != null) {
                    Project[] listProject = readProject();
                    for (int i = 0; i < listProject.length; i++) {
                        if (listProject[i].getLabel().equalsIgnoreCase(newProject)) {
                            if (!projectsMap.containsKey(newProject.toUpperCase())) {
                                projectsMap.put(listProject[i].getLabel(), listProject[i]);
                                projectList.add(listProject[i].getLabel());
                            }
                        }
                    }
                }
            }
        });
    }

    private String getRecentWorkSpace() {
        String filePath = new Path(Platform.getInstanceLocation().getURL().getPath()).toFile().getPath();
        return filePath;
    }

    public List getProjectList() {
        return projectList;
    }

    public Map getProjectMap() {
        return projectsMap;
    }
}
