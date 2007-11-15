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
package org.talend.repository.ui.login.connections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.talend.commons.ui.swt.formtools.LabelText;
import org.talend.core.model.general.ConnectionBean;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.DynamicButtonBean;
import org.talend.repository.model.DynamicFieldBean;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.ui.login.LoginComposite;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ConnectionFormComposite extends Composite {

    private ConnectionsDialog dialog;

    private FormToolkit toolkit;

    private ComboViewer repositoryCombo;

    private Text nameText;

    private Text descriptionText;

    private Text userText;

    private Text passwordText;

    private ConnectionBean connection;

    private ConnectionsListComposite connectionsListComposite;

    private Map<IRepositoryFactory, Map<String, LabelText>> dynamicControls = new HashMap<IRepositoryFactory, Map<String, LabelText>>();

    private Map<IRepositoryFactory, Map<String, LabelText>> dynamicRequiredControls = new HashMap<IRepositoryFactory, Map<String, LabelText>>();

    private Map<IRepositoryFactory, Map<String, Button>> dynamicButtons = new HashMap<IRepositoryFactory, Map<String, Button>>();

    /**
     * DOC smallet ConnectionsComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ConnectionFormComposite(Composite parent, int style, ConnectionsListComposite connectionsListComposite,
            ConnectionsDialog dialog) {
        super(parent, style);
        this.dialog = dialog;
        this.connectionsListComposite = connectionsListComposite;

        toolkit = new FormToolkit(this.getDisplay());
        ScrolledForm form = toolkit.createScrolledForm(this);
        // Form form = toolkit.createForm(this);
        Composite formBody = form.getBody();

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        form.setLayoutData(new GridData(GridData.FILL_BOTH));

        FormLayout formLayout = new FormLayout();
        formBody.setLayout(formLayout);

        // Use by all widgets:
        FormData data;

        // Repository
        repositoryCombo = new ComboViewer(formBody, SWT.BORDER | SWT.READ_ONLY);
        repositoryCombo.setContentProvider(new ArrayContentProvider());
        repositoryCombo.setLabelProvider(new RepositoryFactoryLabelProvider());
        data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, -ConnectionsDialog.HSPACE);
        data.top = new FormAttachment(0, ConnectionsDialog.VSPACE);
        repositoryCombo.getControl().setLayoutData(data);

        Label repositoryLabel = toolkit.createLabel(formBody, Messages.getString("connections.form.field.repository")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.HSPACE);
        data.bottom = new FormAttachment(repositoryCombo.getControl(), 0, SWT.BOTTOM);
        repositoryLabel.setLayoutData(data);

        // Name
        nameText = toolkit.createText(formBody, "", SWT.BORDER); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, -ConnectionsDialog.HSPACE);
        data.top = new FormAttachment(repositoryCombo.getControl(), ConnectionsDialog.VSPACE);
        nameText.setLayoutData(data);

        Label nameLabel = toolkit.createLabel(formBody, Messages.getString("connections.form.field.name")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.HSPACE);
        data.bottom = new FormAttachment(nameText, 0, SWT.BOTTOM);
        nameLabel.setLayoutData(data);

        // Comment
        descriptionText = toolkit.createText(formBody, "", SWT.BORDER); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, -ConnectionsDialog.HSPACE);
        data.top = new FormAttachment(nameText, ConnectionsDialog.VSPACE);
        descriptionText.setLayoutData(data);

        Label descriptionLabel = toolkit
                .createLabel(formBody, Messages.getString("connections.form.field.description")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.HSPACE);
        data.bottom = new FormAttachment(descriptionText, 0, SWT.BOTTOM);
        descriptionLabel.setLayoutData(data);

        // User
        userText = toolkit.createText(formBody, "", SWT.BORDER); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, -ConnectionsDialog.HSPACE);
        data.top = new FormAttachment(descriptionText, ConnectionsDialog.VSPACE);
        userText.setLayoutData(data);

        Label userLabel = toolkit.createLabel(formBody, Messages.getString("connections.form.field.username")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.HSPACE);
        data.bottom = new FormAttachment(userText, 0, SWT.BOTTOM);
        userLabel.setLayoutData(data);

        // Password
        passwordText = toolkit.createText(formBody, "", SWT.PASSWORD | SWT.BORDER); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, -ConnectionsDialog.HSPACE);
        data.top = new FormAttachment(userText, ConnectionsDialog.VSPACE);
        passwordText.setLayoutData(data);

        Label passwordLabel = toolkit.createLabel(formBody, Messages.getString("connections.form.field.password")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, ConnectionsDialog.HSPACE);
        data.bottom = new FormAttachment(passwordText, 0, SWT.BOTTOM);
        passwordLabel.setLayoutData(data);

        List<IRepositoryFactory> availableRepositories = getUsableRepositoryProvider();
        for (IRepositoryFactory current : availableRepositories) {
            Map<String, LabelText> list = new HashMap<String, LabelText>();
            Map<String, LabelText> listRequired = new HashMap<String, LabelText>();
            Map<String, Button> listButtons = new HashMap<String, Button>();
            dynamicControls.put(current, list);
            dynamicRequiredControls.put(current, listRequired);
            dynamicButtons.put(current, listButtons);
            Control baseControl = passwordLabel;
            for (DynamicFieldBean currentField : current.getFields()) {
                int textStyle = SWT.BORDER;
                if (currentField.isPassword()) {
                    textStyle = textStyle | SWT.PASSWORD;
                }
                Text text = toolkit.createText(formBody, "", textStyle); //$NON-NLS-1$
                data = new FormData();
                data.left = new FormAttachment(0, ConnectionsDialog.STANDARD_LABEL_WIDTH);
                data.right = new FormAttachment(100, -ConnectionsDialog.HSPACE);
                data.top = new FormAttachment(baseControl, ConnectionsDialog.VSPACE);
                text.setLayoutData(data);

                Label label = toolkit.createLabel(formBody, currentField.getName());
                data = new FormData();
                data.left = new FormAttachment(0, ConnectionsDialog.HSPACE);
                data.bottom = new FormAttachment(text, 0, SWT.BOTTOM);
                label.setLayoutData(data);

                baseControl = text;

                LabelText labelText = new LabelText(label, text);
                if (currentField.isRequired()) {
                    listRequired.put(currentField.getId(), labelText);
                }
                list.put(currentField.getId(), labelText);
            }

            for (final DynamicButtonBean currentButtonBean : current.getButtons()) {
                Button button = new Button(formBody, SWT.PUSH);
                button.setText(currentButtonBean.getName());
                button.addSelectionListener(new DelegateSelectionListener(currentButtonBean));

                data = new FormData();
                data.left = new FormAttachment(0, ConnectionsDialog.STANDARD_LABEL_WIDTH);
                data.right = new FormAttachment(100, -ConnectionsDialog.HSPACE);
                data.top = new FormAttachment(baseControl, ConnectionsDialog.VSPACE);
                button.setLayoutData(data);

                listButtons.put(currentButtonBean.getId(), button);
            }

        }

        addListeners();
        fillLists();
        showHideDynamicsControls();
        showHideTexts();
        // validateFields();
    }

    public boolean canFinish() {
        return validateFields();
    }

    private boolean validateFields() {
        String errorMsg = null;
        boolean valid = true;
        if (valid && getRepository() == null) {
            valid = false;
            errorMsg = Messages.getString("connections.form.emptyField.repository"); //$NON-NLS-1$
        } else if (valid && getName().length() == 0) {
            valid = false;
            errorMsg = Messages.getString("connections.form.emptyField.connname"); //$NON-NLS-1$
        } else if (valid && getUser().length() == 0) {
            valid = false;
            errorMsg = Messages.getString("connections.form.emptyField.username"); //$NON-NLS-1$
        } else if (valid && !Pattern.matches(RepositoryConstants.MAIL_PATTERN, getUser())) {
            valid = false;
            errorMsg = Messages.getString("connections.form.malformedField.username"); //$NON-NLS-1$
        } else {
            for (LabelText current : dynamicRequiredControls.get(getRepository()).values()) {
                if (valid && current.getText().length() == 0) {
                    valid = false;
                    errorMsg = Messages.getString("connections.form.dynamicFieldEmpty", current.getLabel()); //$NON-NLS-1$
                }
            }
        }

        if (!valid) {
            dialog.setErrorMessage(errorMsg);
        } else {
            dialog.setErrorMessage(null);
        }

        if (connection != null) {
            connection.setComplete(valid);
        }
        return valid;
    }

    private void showHideDynamicsControls() {
        // PTODO SML Optimize
        // 1. Hide all controls:
        for (IRepositoryFactory f : dynamicControls.keySet()) {
            for (LabelText control : dynamicControls.get(f).values()) {
                control.setVisible(false);
            }

            for (Button control : dynamicButtons.get(f).values()) {
                control.setVisible(false);
            }
        }

        // 2. Show active repository controls:
        if (getRepository() != null) {
            for (LabelText control : dynamicControls.get(getRepository()).values()) {
                control.setVisible(true);
            }

            for (Button control : dynamicButtons.get(getRepository()).values()) {
                control.setVisible(true);
            }
        }
    }

    private void showHideTexts() {
        if (connection != null) {
            IRepositoryFactory factory = RepositoryFactoryProvider.getRepositoriyById(connection.getRepositoryId());
            if (factory != null) {
                boolean authenticationNeeded = factory.isAuthenticationNeeded();
                if (authenticationNeeded) {
                    passwordText.setEnabled(true);
                    passwordText.setEditable(true);
                    passwordText.setBackground(LoginComposite.WHITE_COLOR);
                } else {
                    passwordText.setText(""); //$NON-NLS-1$
                    passwordText.setEnabled(false);
                    passwordText.setEditable(false);
                    passwordText.setBackground(LoginComposite.GREY_COLOR);
                }
            }
        }
    }

    public IRepositoryFactory getRepository() {
        IRepositoryFactory repositoryFactory = null;
        IStructuredSelection sel = (IStructuredSelection) repositoryCombo.getSelection();
        repositoryFactory = (IRepositoryFactory) sel.getFirstElement();
        return repositoryFactory;
    }

    private String getName() {
        return nameText.getText();
    }

    private String getUser() {
        return userText.getText();
    }

    ModifyListener standardTextListener = new ModifyListener() {

        public void modifyText(ModifyEvent e) {
            validateFields();
            fillBean();
        }
    };

    ISelectionChangedListener repositoryListener = new ISelectionChangedListener() {

        public void selectionChanged(SelectionChangedEvent e) {
            showHideDynamicsControls();
            validateFields();
            fillBean();
            showHideTexts();

        }

    };

    private void addListeners() {
        repositoryCombo.addPostSelectionChangedListener(repositoryListener);
        nameText.addModifyListener(standardTextListener);
        descriptionText.addModifyListener(standardTextListener);
        userText.addModifyListener(standardTextListener);
        passwordText.addModifyListener(standardTextListener);

        for (IRepositoryFactory f : dynamicControls.keySet()) {
            for (LabelText control : dynamicControls.get(f).values()) {
                control.addModifyListener(standardTextListener);
            }
        }
    }

    private void removeListeners() {
        repositoryCombo.removePostSelectionChangedListener(repositoryListener);
        nameText.removeModifyListener(standardTextListener);
        descriptionText.removeModifyListener(standardTextListener);
        userText.removeModifyListener(standardTextListener);
        passwordText.removeModifyListener(standardTextListener);

        for (IRepositoryFactory f : dynamicControls.keySet()) {
            for (LabelText control : dynamicControls.get(f).values()) {
                control.removeModifyListener(standardTextListener);
            }
        }
    }

    private void fillBean() {
        if (connection != null) {
            if (getRepository() != null) {
                connection.setRepositoryId(getRepository().getId());

                Map<String, LabelText> map = dynamicControls.get(getRepository());
                Map<String, String> connFields = new HashMap<String, String>();
                for (String fieldKey : map.keySet()) {
                    connFields.put(fieldKey, map.get(fieldKey).getText());
                }
                connection.setDynamicFields(connFields);
            }
            connection.setName(nameText.getText());
            connection.setDescription(descriptionText.getText());
            connection.setUser(userText.getText());
            connection.setPassword(passwordText.getText());

            connectionsListComposite.refresh(connection);
        }
    }

    private void fillLists() {
        List<IRepositoryFactory> availableRepositories = getUsableRepositoryProvider();
        repositoryCombo.setInput(availableRepositories);
        fillFields();
    }

    private List<IRepositoryFactory> getUsableRepositoryProvider() {
        List<IRepositoryFactory> availableRepositories = RepositoryFactoryProvider.getAvailableRepositories();

        List<IRepositoryFactory> result = new ArrayList<IRepositoryFactory>();
        for (IRepositoryFactory repositoryFactory : availableRepositories) {
            if (repositoryFactory.isDisplayToUser()) {
                result.add(repositoryFactory);
            }
        }
        return result;
    }

    private void fillFields() {
        if (connection != null) {
            removeListeners();
            String repositoryId = connection.getRepositoryId();
            IRepositoryFactory repositoriyById = RepositoryFactoryProvider.getRepositoriyById(repositoryId);
            repositoryCombo.setSelection(new StructuredSelection(new Object[] { repositoriyById }));

            if (getRepository() != null) {
                Map<String, LabelText> map = dynamicControls.get(getRepository());

                for (String fieldKey : map.keySet()) {
                    LabelText current = map.get(fieldKey);
                    String string = connection.getDynamicFields().get(fieldKey);
                    current.setText(string == null ? "" : string); //$NON-NLS-1$
                }
            }
            nameText.setText((connection.getName() == null ? "" : connection.getName())); //$NON-NLS-1$
            descriptionText.setText((connection.getDescription() == null ? "" : connection.getDescription())); //$NON-NLS-1$
            userText.setText((connection.getUser() == null ? "" : connection.getUser())); //$NON-NLS-1$
            passwordText.setText((connection.getPassword() == null ? "" : connection.getPassword())); //$NON-NLS-1$
            addListeners();
        }
    }

    /**
     * DOC smallet Comment method "setConnection".
     * 
     * @param selected
     */
    public void setConnection(ConnectionBean selected) {
        this.connection = selected;
        fillFields();
        showHideDynamicsControls();
        validateFields();
        showHideTexts();
    }

    /**
     */
    private final class DelegateSelectionListener implements SelectionListener {

        private DynamicButtonBean bean;

        private DelegateSelectionListener(DynamicButtonBean bean) {
            this.bean = bean;
        }

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            e.data = connection;
            bean.getListener().widgetSelected(e);
        }
    }

    /**
     * DOC smallet LoginComposite class global comment. Detailled comment <br/>
     * 
     * $Id: LoginComposite.java 1380 2007-01-10 11:18:55Z smallet $
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
}
