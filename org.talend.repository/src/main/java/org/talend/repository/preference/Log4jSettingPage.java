package org.talend.repository.preference;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.utils.Log4jUtil;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.repository.model.ResourceModelUtils;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.constants.Log4jPrefsConstants;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.utils.Log4jPrefsSettingManager;

public class Log4jSettingPage extends ProjectSettingPage {

    private StyledText templateTxt;

    private boolean reset = false;

    Button log4jBtn;

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Control createContents(Composite parent) {

        Group log4jActivateGroup = (Group) createLog4jActivateGroup(parent);
        if (Log4jUtil.isEnable()) {
            log4jActivateGroup.setVisible(true);
        } else {
            log4jActivateGroup.setVisible(false);
        }

        Label headLabel = new Label(parent, SWT.NONE);
        headLabel.setText("Log4j template"); //$NON-NLS-1$

        templateTxt = new StyledText(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData layoutData = new GridData(GridData.FILL_BOTH);
        templateTxt.setLayoutData(layoutData);

        initLog4jStatus();

        return parent;
    }

    private void initLog4jStatus() {
        if (Log4jPrefsSettingManager.getInstance().isLog4jPrefsExist()) {
            // if already exist,to get its value from prefs
            log4jBtn.setSelection(Boolean.valueOf(Log4jPrefsSettingManager.getInstance().getValueOfPreNode(
                    Log4jPrefsConstants.LOG4J_ENABLE_NODE)));
            templateTxt.setText(Log4jPrefsSettingManager.getInstance().getValueOfPreNode(Log4jPrefsConstants.LOG4J_CONTENT_NODE));

        }

    }

    protected Composite createLog4jActivateGroup(Composite parent) {

        Group group = new Group(parent, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setLayout(new GridLayout(3, false));
        Composite composite = new Composite(group, SWT.NONE);
        GridLayout gridLayout = new GridLayout(3, false);
        composite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalSpan = 3;
        composite.setLayoutData(gridData);
        group.setText(Messages.getString("Log4jSettingPage.Log4jEnabled")); //$NON-NLS-1$

        log4jBtn = new Button(composite, SWT.CHECK);
        log4jBtn.setText(Messages.getString("Log4jSettingPage.ActivateLog4j"));//$NON-NLS-1$

        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 8;
        composite.setLayout(layout);

        return group;
    }

    @Override
    public boolean performOk() {
        boolean ok = super.performOk();
        if (templateTxt != null && !templateTxt.isDisposed()) {
            IRunProcessService service = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
            }
            if (reset) {
                if (service != null) {
                    Log4jPrefsSettingManager.getInstance().saveLog4jNodeIntoPref(Log4jPrefsConstants.LOG4J_ENABLE_NODE,
                            String.valueOf(false));
                    Log4jPrefsSettingManager.getInstance().saveLog4jNodeIntoPref(Log4jPrefsConstants.LOG4J_CONTENT_NODE,
                            service.getLogTemplate(Log4jPrefsConstants.LOG4JFILEPATH));
                }
            } else {
                Log4jPrefsSettingManager.getInstance().saveLog4jNodeIntoPref(Log4jPrefsConstants.LOG4J_ENABLE_NODE,
                        String.valueOf(log4jBtn.getSelection()));
                Log4jPrefsSettingManager.getInstance().saveLog4jNodeIntoPref(Log4jPrefsConstants.LOG4J_CONTENT_NODE,
                        templateTxt.getText());

            }
            reset = false;
            updateLogFiles();
        }

        return ok;
    }

    private void updateLogFiles() {
        IRunProcessService service = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        }
        if (service != null) {
            IProject project;
            try {
                project = ResourceModelUtils.getProject(ProjectManager.getInstance().getCurrentProject());
                service.updateLogFiles(project, false);
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void performDefaults() {
        super.performDefaults();
        if (templateTxt != null && !templateTxt.isDisposed()) {
            reset = true;
            log4jBtn.setSelection(false);
            templateTxt.setText(Log4jPrefsSettingManager.getInstance()
                    .getDefaultTemplateString(Log4jPrefsConstants.LOG4JFILEPATH));
        }
    }
}
