// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.model.EEncryptionMethod;
import org.talend.repository.ui.swt.utils.AbstractForm;

/**
 * The class is used for LDAP schema on Repository View. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPSchemaStep1Form extends AbstractForm {

    // /** The connection name text widget */
    // private Text nameText;

    /** The host name combo with the history of recently used host names */
    private Combo hostCombo;

    /** The host combo with the history of recently used ports */
    private Combo portCombo;

    /** The combo to select the encryption method */
    private Combo encryptionMethodCombo;

    /** The button to check the connection parameters */
    private Button checkConnectionButton;

    private ConnectionItem connectionItem;

    private MetadataTable metadataTable;

    /**
     * LDAPSchemaStep2Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     * @param metadataTable
     * @param tableNames
     */
    public LDAPSchemaStep1Form(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] tableNames) {
        super(parent, SWT.NONE, tableNames);
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        setupForm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {

        Composite composite = BaseWidgetUtils.createColumnContainer(this, 1, 1);

        // Composite nameComposite = BaseWidgetUtils.createColumnContainer(composite, 2, 1);
        // BaseWidgetUtils.createLabel(nameComposite, "Connection name:", 1);
        // nameText = BaseWidgetUtils.createText(nameComposite, "", 1);

        BaseWidgetUtils.createSpacer(composite, 1);

        Group group = BaseWidgetUtils.createGroup(composite, "Network Parameter", 1);

        Composite groupComposite = BaseWidgetUtils.createColumnContainer(group, 3, 1);
        BaseWidgetUtils.createLabel(groupComposite, "Hostname:", 1);
        String[] hostHistory = new String[] {};
        hostCombo = BaseWidgetUtils.createCombo(groupComposite, hostHistory, -1, 2);

        BaseWidgetUtils.createLabel(groupComposite, "Port:", 1);
        String[] portHistory = new String[] {};
        portCombo = BaseWidgetUtils.createCombo(groupComposite, portHistory, -1, 2);

        String[] encMethods = new String[] { EEncryptionMethod.NO_ENCRYPTION_METHOD.getName(),
                EEncryptionMethod.SSL_ENCRYPTION_METHOD.getName(),
                EEncryptionMethod.STARTTSL_EXTENSION_METHOD.getName() };
        int index = 0;
        BaseWidgetUtils.createLabel(groupComposite, "Encryption method:", 1);
        encryptionMethodCombo = BaseWidgetUtils.createReadonlyCombo(groupComposite, encMethods, index, 2);
        // BaseWidgetUtils.createSpacer(groupComposite, 1);
        BaseWidgetUtils.createLabel(groupComposite, "                    Cilck the button to check connection status.",
                2);

        BaseWidgetUtils.createSpacer(groupComposite, 2);
        checkConnectionButton = new Button(groupComposite, SWT.PUSH);
        GridData gd = new GridData();
        gd.horizontalAlignment = SWT.RIGHT;
        gd.verticalAlignment = SWT.BOTTOM;
        checkConnectionButton.setLayoutData(gd);
        checkConnectionButton.setText("Check Network Parameter");

        checkFieldsValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {

        final LDAPSchemaConnection connection = (LDAPSchemaConnection) connectionItem.getConnection();

        hostCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                checkFieldsValue();
                connection.setHost(hostCombo.getText().trim());

            }
        });

        portCombo.addVerifyListener(new VerifyListener() {

            public void verifyText(VerifyEvent event) {
                if (!event.text.matches("[0-9]*")) {
                    event.doit = false;
                }
                if (portCombo.getText().length() > 4 && event.text.length() > 0) {
                    event.doit = false;
                }
                checkFieldsValue();
                connection.setPort(portCombo.getText().trim());
            }
        });
        portCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                checkFieldsValue();
                connection.setPort(portCombo.getText().trim());
            }
        });

        encryptionMethodCombo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                checkFieldsValue();

                connection.setEncryptionMethodName(encryptionMethodCombo.getText().trim());
            }

        });

        checkConnectionButton.addSelectionListener(new SelectionAdapter() {

            boolean isOK = false;

            public void widgetSelected(SelectionEvent event) {

                try {
                    IRunnableWithProgress op = new IRunnableWithProgress() {

                        public void run(IProgressMonitor monitor) {
                            isOK = LDAPConnectionUtils.checkParam(connection);
                        }
                    };
                    new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, op);
                } catch (InvocationTargetException e) {
                    MessageBoxExceptionHandler.process(e);
                } catch (InterruptedException e) {
                    MessageBoxExceptionHandler.process(e);
                }

                if (isOK) {
                    saveDialogSettings();
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Check Network Parameter",
                            "The connection was established successfully.");
                    updateStatus(IStatus.OK, null);
                } else {
                    MessageDialog.openError(Display.getDefault().getActiveShell(), "Check Network Parameter",
                            "The connection was established failed.");
                    updateStatus(IStatus.ERROR, null);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {

        if (hostCombo.getText() == null || hostCombo.getText().equals("")) {
            this.checkConnectionButton.setEnabled(false);
            updateStatus(IStatus.ERROR, "Host name must be specified."); //$NON-NLS-1$
            return false;
        } else if (portCombo.getText() == null || portCombo.getText().equals("")) {
            this.checkConnectionButton.setEnabled(false);
            updateStatus(IStatus.ERROR, "Port must be specified."); //$NON-NLS-1$
            return false;
        } else {
            this.checkConnectionButton.setEnabled(true);
            updateStatus(IStatus.ERROR, null);
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        LDAPSchemaConnection connection = (LDAPSchemaConnection) this.connectionItem.getConnection();
        String host = connection.getHost();
        this.hostCombo.setItems(HistoryUtils.load(ConnectionUIConstants.DIALOGSETTING_KEY_HOST_HISTORY));
        this.hostCombo.setText(host == null ? "" : host);
        String port = connection.getPort();
        this.portCombo.setItems(HistoryUtils.load(ConnectionUIConstants.DIALOGSETTING_KEY_PORT_HISTORY));
        this.portCombo.setText(port == null ? "" : port);
        String encryptionMethodName = connection.getEncryptionMethodName();
        this.encryptionMethodCombo.setText(encryptionMethodName == null ? "" : encryptionMethodName);

        if (connection.getEncryptionMethodName() == null) {
            connection.setEncryptionMethodName(EEncryptionMethod.NO_ENCRYPTION_METHOD.getName());
        } else {
            updateStatus(IStatus.OK, null);
        }

        boolean flag = (host == null || port.equals("") || encryptionMethodName == null);
        this.checkConnectionButton.setEnabled(false);
        checkFieldsValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        // TODO Auto-generated method stub

    }

    /**
     * Storing the history of combo.
     */
    public void saveDialogSettings() {
        HistoryUtils.save(ConnectionUIConstants.DIALOGSETTING_KEY_HOST_HISTORY, hostCombo.getText());
        HistoryUtils.save(ConnectionUIConstants.DIALOGSETTING_KEY_PORT_HISTORY, portCombo.getText());
    }
}
