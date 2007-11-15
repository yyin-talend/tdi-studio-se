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
package org.talend.repository.ui.wizards.metadata.connection.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledDirectoryField;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.swt.utils.AbstractForm;
import org.talend.repository.ui.utils.DataStringConnection;
import org.talend.repository.ui.utils.ManagerConnection;

/**
 * @author ocarbone
 * 
 */
public class DatabaseForm extends AbstractForm {

    /**
     * Composite.
     */
    private Composite compositeDbSettings;

    /**
     * Main Vars.
     */
    private final ConnectionItem connectionItem;

    private DataStringConnection urlDataStringConnection;

    /**
     * Flags.
     */
    private boolean databaseSettingIsValide = false;

    private boolean readOnly;

    /**
     * Main Fields.
     */
    private LabelledCombo dbTypeCombo;

    private LabelledCombo sqlSyntaxCombo;

    private LabelledText serverText;

    private LabelledText portText;

    private LabelledText usernameText;

    private LabelledText passwordText;

    private LabelledText sidOrDatabaseText;

    private LabelledText schemaText;

    private LabelledText datasourceText;

    private LabelledText stringQuoteText;

    private LabelledText nullCharText;

    private LabelledText urlConnectionStringText;

    private LabelledFileField fileField;

    private LabelledDirectoryField directoryField;

    /**
     * Anothers Fields.
     */
    private UtilsButton checkButton;

    /**
     * Constructor to use by a Wizard to create a new database connection.
     * 
     * @param existingNames
     * 
     * @param Composite
     * @param Wizard
     * @param ISelection
     */
    public DatabaseForm(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
        setupForm();
        addStringConnectionControls();
        GridLayout layout2 = (GridLayout) getLayout();
        layout2.marginHeight = 0;
        setLayout(layout2);
    }

    /**
     * initialize UI (button save & default settings or saved settings).
     */
    @Override
    public void initialize() {
        Integer indexOfCombo = urlDataStringConnection.getIndexOfLabel(getConnection().getDatabaseType());
        if (indexOfCombo > -1) {
            dbTypeCombo.select(indexOfCombo);
            urlDataStringConnection.setSelectionIndex(indexOfCombo);
        }
        urlConnectionStringText.setText(getConnection().getURL());
        usernameText.setText(getConnection().getUsername());
        passwordText.setText(getConnection().getPassword());
        serverText.setText(getConnection().getServerName());
        portText.setText(getConnection().getPort());
        datasourceText.setText(getConnection().getDatasourceName());
        sidOrDatabaseText.setText(getConnection().getSID());
        schemaText.setText(getConnection().getSchema());
        fileField.setText(getConnection().getFileFieldName());
        stringQuoteText.setText(getConnection().getStringQuote());
        nullCharText.setText(getConnection().getNullChar());
        directoryField.setText(getConnection().getDBRootPath());
        // PTODO !StandBy! (use width SQL Editor): to define the values of SQL Syntax (need by SQL Editor)
        getConnection().setSqlSynthax(Messages.getString("DatabaseForm.sqlSyntax")); //$NON-NLS-1$
        sqlSyntaxCombo.select(getSqlSyntaxIndex(getConnection().getSqlSynthax()));

        updateStatus(IStatus.OK, ""); //$NON-NLS-1$
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();

        dbTypeCombo.setReadOnly(isReadOnly());
        urlConnectionStringText.setReadOnly(isReadOnly());
        usernameText.setReadOnly(isReadOnly());
        passwordText.setReadOnly(isReadOnly());
        serverText.setReadOnly(isReadOnly());
        portText.setReadOnly(isReadOnly());
        sidOrDatabaseText.setReadOnly(isReadOnly());
        schemaText.setReadOnly(isReadOnly());
        datasourceText.setReadOnly(isReadOnly());
        fileField.setReadOnly(isReadOnly());
        sqlSyntaxCombo.setReadOnly(isReadOnly());
        stringQuoteText.setReadOnly(isReadOnly());
        nullCharText.setReadOnly(isReadOnly());
    }

    /**
     * Get the index of a sqlSyntax label or 0 if the label don't exist.
     * 
     * @param string label
     */
    public int getSqlSyntaxIndex(final String label) {
        for (int i = 0; i < sqlSyntaxCombo.getItemCount(); i++) {
            if (sqlSyntaxCombo.getItem(i).equals(label)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void addFields() {
        int width = getSize().x;
        GridLayout layout2;
        // Group Database Settings
        Group group = Form.createGroup(this, 1, Messages.getString("DatabaseForm.groupDatabaseSettings"), 365); //$NON-NLS-1$
        Composite compositeGroupDbSettings = Form.startNewGridLayout(group, 1);
        layout2 = (GridLayout) compositeGroupDbSettings.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;
        layout2.marginLeft = 0;
        layout2.marginRight = 0;
        layout2.marginWidth = 0;

        compositeDbSettings = Form.startNewDimensionnedGridLayout(compositeGroupDbSettings, 3, width, 330);
        layout2 = (GridLayout) compositeDbSettings.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;

        // Main Fields

        // Database Type Combo
        urlDataStringConnection = new DataStringConnection();

        // PTODO cantoine : HIDDEN some Database connection in function of project MODE (Perl/Java).
        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.PERL) {
            Collection<String> databasePerl = new ArrayList<String>(Arrays.asList(urlDataStringConnection.getItem()));
            databasePerl.remove("Microsoft SQL Server");
            databasePerl.remove("Ingres");
            databasePerl.remove("Interbase");
            databasePerl.remove("FireBird");
            databasePerl.remove("Informix");
            databasePerl.remove("Access");
            databasePerl.remove("Teradata");
            databasePerl.remove("AS400");

            databasePerl.remove("JavaDB Embeded");
            databasePerl.remove("JavaDB JCCJDBC");
            databasePerl.remove("JavaDB DerbyClient");

            databasePerl.remove("HSQLDB Server");
            databasePerl.remove("HSQLDB WebServer");
            databasePerl.remove("HSQLDB In-Process");

            String[] dbPerl = databasePerl.toArray(new String[databasePerl.size()]);
            dbTypeCombo = new LabelledCombo(compositeDbSettings, Messages.getString("DatabaseForm.dbType"), Messages //$NON-NLS-1$
                    .getString("DatabaseForm.dbTypeTip"), dbPerl, 2, true); //$NON-NLS-1$
        } else {
            dbTypeCombo = new LabelledCombo(compositeDbSettings, Messages.getString("DatabaseForm.dbType"), Messages //$NON-NLS-1$
                    .getString("DatabaseForm.dbTypeTip"), urlDataStringConnection.getItem(), 2, true); //$NON-NLS-1$
        }

        // Field connectionString
        urlDataStringConnection.setSelectionIndex(dbTypeCombo.getSelectionIndex());
        urlConnectionStringText = new LabelledText(compositeDbSettings, Messages
                .getString("DatabaseForm.stringConnection"), 2); //$NON-NLS-1$
        urlConnectionStringText.setEditable(false);

        // Field login & password
        usernameText = new LabelledText(compositeDbSettings, Messages.getString("DatabaseForm.login"), 2); //$NON-NLS-1$
        passwordText = new LabelledText(compositeDbSettings, Messages.getString("DatabaseForm.password"), 2, SWT.BORDER //$NON-NLS-1$
                | SWT.SINGLE | SWT.PASSWORD);

        // Another fields
        serverText = new LabelledText(compositeDbSettings, Messages.getString("DatabaseForm.server"), 2); //$NON-NLS-1$
        portText = new LabelledText(compositeDbSettings, Messages.getString("DatabaseForm.port"), 2); //$NON-NLS-1$
        portText.setTextLimit(5);
        sidOrDatabaseText = new LabelledText(compositeDbSettings, Messages.getString("DatabaseForm.database"), 2); //$NON-NLS-1$
        schemaText = new LabelledText(compositeDbSettings, Messages.getString("DatabaseForm.schema"), 2); //$NON-NLS-1$
        datasourceText = new LabelledText(compositeDbSettings, Messages.getString("DatabaseForm.dataSource"), 2); //$NON-NLS-1$

        String[] extensions = { "*.*" }; //$NON-NLS-1$
        fileField = new LabelledFileField(compositeDbSettings, Messages.getString("DatabaseForm.mdbFile"), extensions); //$NON-NLS-1$
        directoryField = new LabelledDirectoryField(compositeDbSettings, "DB Root Path"); //$NON-NLS-1$

        // Button Check
        Composite compositeCheckButton = Form.startNewGridLayout(compositeGroupDbSettings, 1, false, SWT.CENTER,
                SWT.BOTTOM);
        layout2 = (GridLayout) compositeCheckButton.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;
        checkButton = new UtilsButton(compositeCheckButton,
                Messages.getString("DatabaseForm.check"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                HEIGHT_BUTTON_PIXEL);
        checkButton.setEnabled(false);

        // Group Database Properties
        Group group1 = Form.createGroup(this, 1, Messages.getString("DatabaseForm.groupDatabaseProperties"), 60); //$NON-NLS-1$
        // Composite compositeGroupDbProperties = Form.startNewGridLayout(group1, 4, false, SWT.LEFT, SWT.CENTER);
        Composite compositeGroupDbProperties = Form.startNewDimensionnedGridLayout(group1, 4, width, 70);

        // PTODO !StandBy! (use width SQL Editor): to define the values of SQL Syntax (need by SQL Editor)
        String[] item = { "SQL 92" }; //$NON-NLS-1$
        sqlSyntaxCombo = new LabelledCombo(compositeGroupDbProperties,
                Messages.getString("DatabaseForm.sqlSyntax"), null, item, //$NON-NLS-1$
                3);

        stringQuoteText = new LabelledText(compositeGroupDbProperties,
                Messages.getString("DatabaseForm.stringQuote"), false); //$NON-NLS-1$
        nullCharText = new LabelledText(compositeGroupDbProperties, Messages.getString("DatabaseForm.nullChar"), false); //$NON-NLS-1$
    }

    /**
     * Check data connection.
     */
    private void checkConnection() {

        checkButton.setEnabled(false);
        if (connectionItem.getConnection() instanceof DatabaseConnection) {
            DatabaseConnection c = (DatabaseConnection) connectionItem.getConnection();
            if (c.getProductId().equals(EDatabaseTypeName.ORACLEFORSID.getProduct())) {
                schemaText.setText(schemaText.getText().toUpperCase());
            }
        }
        ManagerConnection managerConnection = new ManagerConnection();
        // set the value
        managerConnection.setValue(0, dbTypeCombo.getItem(dbTypeCombo.getSelectionIndex()), urlConnectionStringText
                .getText(), serverText.getText(), usernameText.getText(), passwordText.getText(), sidOrDatabaseText
                .getText(), portText.getText(), fileField.getText(), datasourceText.getText(), schemaText.getText());
        managerConnection.setDbRootPath(directoryField.getText());
        managerConnection.setValueProperties(sqlSyntaxCombo.getItem(sqlSyntaxCombo.getSelectionIndex()),
                stringQuoteText.getText(), nullCharText.getText());

        // check the connection
        databaseSettingIsValide = managerConnection.check();

        // update the button
        checkButton.setEnabled(true);

        // show the result of check connection
        if (databaseSettingIsValide) {
            if (!isReadOnly()) {
                updateStatus(IStatus.OK, null);
            }
            MessageDialog.openInformation(getShell(), Messages.getString("DatabaseForm.checkConnectionTitle"), "\"" //$NON-NLS-1$ //$NON-NLS-2$
                    + connectionItem.getProperty().getLabel() + "\" " + Messages.getString("DatabaseForm.checkIsDone")); //$NON-NLS-1$ //$NON-NLS-2$
            if (!isReadOnly()) {
                setPropertiesFormEditable(true);
            }
        } else {
            String mainMsg = Messages.getString("DatabaseForm.checkFailure") + " " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("DatabaseForm.checkFailureTip"); //$NON-NLS-1$
            if (!isReadOnly()) {
                updateStatus(IStatus.WARNING, mainMsg); //$NON-NLS-1$
            }
            new ErrorDialogWidthDetailArea(getShell(), PID, mainMsg, managerConnection.getMessageException());
        }
    }

    /**
     * add StringConnection Controls.
     */
    private void addStringConnectionControls() {
        // event FocusIn
        urlConnectionStringText.addListener(SWT.FocusIn, new Listener() {

            public void handleEvent(final Event e) {
                if (dbTypeCombo.getSelectionIndex() >= 0) {
                    setPropertiesFormEditable(false);
                    urlConnectionStringText.setEditable(true);
                } else {
                    updateStatus(IStatus.ERROR, Messages.getString("DatabaseForm.alert", dbTypeCombo.getLabel())); //$NON-NLS-1$
                }
            }
        });

        urlConnectionStringText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (urlConnectionStringText.getEditable()) {
                    if (!databaseSettingIsValide) {
                        updateStatus(IStatus.INFO, Messages.getString("DatabaseForm.checkInformation")); //$NON-NLS-1$
                    }
                    databaseSettingIsValide = false;
                    checkButton.setEnabled(true);
                    String[] s = urlDataStringConnection.getAnalyse(urlConnectionStringText.getText());
                    // if the ConnectionString write manually don't correspond width selectedIndex of combo DbType
                    // we search if another regex corresponding at this string
                    if (new Integer(s[0]) != dbTypeCombo.getSelectionIndex()) {
                        dbTypeCombo.select(new Integer(s[0]));
                        dbTypeCombo.forceFocus();
                    }

                    if (s[1] != "") { //$NON-NLS-1$
                        serverText.setText(s[1]);
                        getConnection().setServerName(s[1]);
                    }
                    if (s[2] != "") { //$NON-NLS-1$
                        portText.setText(s[2]);
                        getConnection().setPort(s[2]);
                    }
                    if (s[3] != "") { //$NON-NLS-1$
                        sidOrDatabaseText.setText(s[3]);
                        getConnection().setSID(s[3]);
                    }
                }
            }
        });

    }

    /**
     * addButtonControls.
     * 
     * @param cancelButton
     */
    @Override
    protected void addUtilsButtonListeners() {

        // Event CheckButton
        checkButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                checkConnection();
            }
        });
    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        // common Listener to force the choice of dbCombo
        Listener listener = new Listener() {

            public void handleEvent(final Event e) {
                if (dbTypeCombo.getSelectionIndex() == -1) {
                    dbTypeCombo.forceFocus();
                }
                setPropertiesFormEditable(dbTypeCombo.getSelectionIndex() > -1);
                urlConnectionStringText.setEditable(false);
            }
        };

        usernameText.addListener(SWT.FocusIn, listener);
        passwordText.addListener(SWT.FocusIn, listener);
        serverText.addListener(SWT.FocusIn, listener);
        portText.addListener(SWT.FocusIn, listener);
        sidOrDatabaseText.addListener(SWT.FocusIn, listener);
        datasourceText.addListener(SWT.FocusIn, listener);
        schemaText.addListener(SWT.FocusIn, listener);
        urlConnectionStringText.addListener(SWT.FocusIn, listener);

        // serverText : Event modifyText
        serverText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!urlConnectionStringText.getEditable()) {
                    getConnection().setServerName(serverText.getText());
                    checkFieldsValue();
                }
            }
        });

        // portText : Event modifyText
        portText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!urlConnectionStringText.getEditable()) {
                    getConnection().setPort(portText.getText());
                    checkFieldsValue();
                }
                // Check port
                boolean b = true;
                if (getConnection().getDatabaseType().equals("Ingres")) {
                    b = Pattern.matches(Messages.getString("DatabaseForm.ingresDBRegex"), portText.getText());
                } else {
                    b = Pattern.matches(Messages.getString("DatabaseForm.otherDBRegex"), portText.getText());
                }
                evaluateTextField(b);
            }
        });

        // portText : Event Key
        portText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (!Character.isLetterOrDigit(e.character) && !Character.isIdentifierIgnorable(e.character)) {
                    e.doit = false;
                }
            }
        });

        // usernameText : Event modifyText
        usernameText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!urlConnectionStringText.getEditable()) {
                    getConnection().setUsername(usernameText.getText());
                }
            }
        });

        // passwordText : Event modifyText
        passwordText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!urlConnectionStringText.getEditable()) {
                    getConnection().setPassword(passwordText.getText());
                }
            }
        });

        // sidOrDatabaseText : Event modifyText
        sidOrDatabaseText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!urlConnectionStringText.getEditable()) {
                    getConnection().setSID(sidOrDatabaseText.getText());
                    checkFieldsValue();
                }
            }
        });

        // datasourceText : Event modifyText
        datasourceText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!urlConnectionStringText.getEditable()) {
                    getConnection().setDatasourceName(datasourceText.getText());
                    checkFieldsValue();
                }
            }
        });

        // schemaText : Event modifyText
        schemaText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!urlConnectionStringText.getEditable()) {
                    getConnection().setSchema(schemaText.getText());
                    checkFieldsValue();
                }
            }
        });

        // Event dbTypeCombo
        dbTypeCombo.addModifyListener(new ModifyListener() {

            // Event Modify
            public void modifyText(final ModifyEvent e) {
                urlDataStringConnection.setSelectionIndex(dbTypeCombo.getSelectionIndex());
                setPropertiesFormEditable(true);
                getConnection().setDatabaseType(dbTypeCombo.getText());
                portText.setText(urlDataStringConnection.getDefaultPort());
                final String product = EDatabaseTypeName.getTypeFromDisplayName(getConnection().getDatabaseType())
                        .getProduct();
                getConnection().setProductId(product);
                final String mapping = MetadataTalendType.getDefaultDbmsFromProduct(product).getId();
                getConnection().setDbmsId(mapping);
                checkFieldsValue();
            }
        });

        // When the DbType is selected, disabled the action of keyboard's letter to modify the combo
        // utils when the user use the keyboard to write the connection string
        dbTypeCombo.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (dbTypeCombo.getSelectionIndex() > -1) {
                    if (Character.isLetterOrDigit(e.character)) {
                        e.doit = false;
                    }
                }
            }
        });

        // Event fileField
        fileField.addListener(SWT.FocusIn, new Listener() {

            // Event FocusIn
            public void handleEvent(final Event e) {
                if (dbTypeCombo.getSelectionIndex() == -1) {
                    dbTypeCombo.forceFocus();
                } else {
                    if (urlDataStringConnection.getStringConnectionTemplate().contains("<filename>")) { //$NON-NLS-1$
                        setPropertiesFormEditable(true);
                        urlConnectionStringText.setEditable(false);
                    }
                    getConnection().setFileFieldName(PathUtils.getPortablePath(fileField.getText()));
                }
            }
        });
        // Event Modify
        fileField.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!urlConnectionStringText.getEditable()) {
                    getConnection().setFileFieldName(fileField.getText());
                    checkFieldsValue();
                }
            }
        });

        directoryField.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!urlConnectionStringText.getEditable()) {
                    String text = directoryField.getText();
                    getConnection().setDBRootPath(text);
                    checkFieldsValue();
                }
            }
        });
        // sqlSyntaxText : Event modifyText
        sqlSyntaxCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                getConnection().setSqlSynthax(sqlSyntaxCombo.getText());
            }
        });

        // nullCharText : Event modifyText
        nullCharText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                getConnection().setNullChar(nullCharText.getText());
            }
        });

        // stringQuoteText : Event modifyText
        stringQuoteText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                getConnection().setStringQuote(stringQuoteText.getText());
            }
        });

        // urlConnectionStringText : Event modifyText
        urlConnectionStringText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                getConnection().setURL(urlConnectionStringText.getText());
                getConnection().setServerName(serverText.getText());
                getConnection().setPort(portText.getText());
                getConnection().setUsername(usernameText.getText());
                getConnection().setPassword(passwordText.getText());
                getConnection().setSID(sidOrDatabaseText.getText());
                getConnection().setDatasourceName(datasourceText.getText());
                getConnection().setSchema(schemaText.getText());
                getConnection().setDatabaseType(dbTypeCombo.getText());
                getConnection().setFileFieldName(fileField.getText());
                getConnection().setDBRootPath(directoryField.getText());
            }
        });

    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkConnection().
     */
    @Override
    public boolean checkFieldsValue() {

        databaseSettingIsValide = false;
        urlConnectionStringText.setText(getStringConnection());
        urlConnectionStringText.setToolTipText(urlDataStringConnection.getStringConnectionTemplate());
        updateCheckButton();
        setPropertiesFormEditable(false);

        if (dbTypeCombo.getSelectionIndex() < 0) {
            updateStatus(IStatus.ERROR, Messages.getString("DatabaseForm.alert", dbTypeCombo.getLabel())); //$NON-NLS-1$
            return false;
        }

        // Show Database Properties
        setPropertiesFormEditable(true);

        // Another fields depend on DbType
        String s = urlDataStringConnection.getStringConnectionTemplate();
        if (s.contains("<host>") && serverText.getCharCount() == 0) { //$NON-NLS-1$
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", serverText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if (s.contains("<port>") && portText.getCharCount() == 0) { //$NON-NLS-1$
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", portText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if ((s.contains("<sid>") || s.contains("<service_name>")) && sidOrDatabaseText.getCharCount() == 0) { //$NON-NLS-1$ //$NON-NLS-2$
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", sidOrDatabaseText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if (s.contains("<filename>") && fileField.getText() == "") { //$NON-NLS-1$ //$NON-NLS-2$
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", fileField.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if (s.contains("<datasource>") && datasourceText.getText() == "") { //$NON-NLS-1$ //$NON-NLS-2$
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", datasourceText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if (s.contains("<dbRootPath>") && directoryField.getText() == "") { //$NON-NLS-1$ //$NON-NLS-2$
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", directoryField.getLabelText())); //$NON-NLS-1$
            return false;
        }

        if (urlDataStringConnection.isSchemaNeeded() && schemaText.getCharCount() == 0) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", schemaText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        if (!databaseSettingIsValide) {
            updateStatus(IStatus.INFO, Messages.getString("DatabaseForm.checkInformation")); //$NON-NLS-1$
            return false;
        }
        if (sqlSyntaxCombo.getSelectionIndex() == -1) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", sqlSyntaxCombo.getLabel())); //$NON-NLS-1$
            return false;
        }
        if (nullCharText.getCharCount() == 0) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", nullCharText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        if (stringQuoteText.getCharCount() == 0) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", stringQuoteText.getLabelText())); //$NON-NLS-1$
            return false;
        }
        updateStatus(IStatus.OK, null);
        return true;
    }

    private String getStringConnection() {
        String s = urlDataStringConnection.getString(dbTypeCombo.getSelectionIndex(), serverText.getText(),
                usernameText.getText(), passwordText.getText(), portText.getText(), sidOrDatabaseText.getText(),
                fileField.getText().toLowerCase(), datasourceText.getText(), directoryField.getText());
        return s;
    }

    private void updateCheckButton() {
        // update checkEnable
        checkButton.setEnabled((dbTypeCombo.getSelectionIndex() >= 0)
                && (getStringConnection() != urlDataStringConnection.getStringConnectionTemplate()));
    }

    /**
     * SetEditable fields.
     * 
     * @param boolean
     */
    private void setPropertiesFormEditable(boolean visible) {
        // update the UI label
        if (urlDataStringConnection.isSchemaNeeded()) {
            if (urlDataStringConnection.getStringConnectionTemplate().contains("(description=(address=(protocol=tcp)")) { //$NON-NLS-1$
                sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.service_name")); //$NON-NLS-1$
                sidOrDatabaseText.setLabelWidth(65);
            } else {
                sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.sid")); //$NON-NLS-1$
            }
        } else {
            sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.database")); //$NON-NLS-1$
        }

        // update the UI Fields
        usernameText.setEditable(visible);
        passwordText.setEditable(visible);
        serverText.setEditable(false);
        portText.setEditable(false);
        sidOrDatabaseText.setEditable(false);
        datasourceText.setEditable(false);
        schemaText.setEditable(false);
        fileField.setEditable(false);
        directoryField.setEditable(false);

        if (dbTypeCombo.getSelectionIndex() < 0) {
            urlConnectionStringText.setEditable(false);
        } else {
            String s = urlDataStringConnection.getStringConnectionTemplate();
            urlConnectionStringText.setEditable(!visible);
            if (s.contains("<host>")) { //$NON-NLS-1$
                serverText.setEditable(visible);
            }
            if (s.contains("<port>")) { //$NON-NLS-1$
                portText.setEditable(visible);
            }
            if (s.contains("<sid>") || s.contains("<service_name>")) { //$NON-NLS-1$ //$NON-NLS-2$
                sidOrDatabaseText.setEditable(visible);
            }
            if (s.contains("<filename>")) { // &&
                // urlDataStringConnection.getStringConnectionTemplate().contains("jdbc:sqlite")
                fileField.setEditable(visible);
                if (EDatabaseTypeName.getTypeFromDisplayName(getConnection().getDatabaseType()).equals(
                        EDatabaseTypeName.SQLITE)) {
                    usernameText.setEditable(false);
                    passwordText.setEditable(false);
                } else {
                    usernameText.setEditable(true);
                    passwordText.setEditable(true);
                }
            }
            // if (s.contains("<filename>")) { //$NON-NLS-1$
            // fileField.setEditable(visible);
            // }
            if (s.contains("<datasource>")) { //$NON-NLS-1$
                datasourceText.setEditable(visible);
            }
            if (s.contains("<dbRootPath>")) {
                directoryField.setEditable(visible);
                sidOrDatabaseText.setEditable(visible);
            }
            if (urlDataStringConnection.isSchemaNeeded()) {
                schemaText.setEditable(visible);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     * 
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        updateCheckButton();
        if (urlConnectionStringText.getText().equals("")) { //$NON-NLS-1$
            urlConnectionStringText.setText(getStringConnection());
        }
        setPropertiesFormEditable((urlDataStringConnection.getIndexOfLabel(getConnection().getDatabaseType()) > -1));

        if (isReadOnly() != readOnly) {
            adaptFormToReadOnly();
        }
    }

    protected DatabaseConnection getConnection() {
        return (DatabaseConnection) connectionItem.getConnection();
    }

    protected void evaluateTextField(boolean b) {

        if (!b) {
            updateStatus(IStatus.ERROR, Messages.getString("DatabaseForm.portError"));
        }
    }

}
