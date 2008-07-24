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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledDirectoryField;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.core.CorePlugin;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.swt.utils.AbstractForm;
import org.talend.repository.ui.utils.DBConnectionContextUtils;
import org.talend.repository.ui.utils.DataStringConnection;
import org.talend.repository.ui.utils.ManagerConnection;
import org.talend.repository.ui.utils.DBConnectionContextUtils.EDBParamName;

/**
 * @author ocarbone
 * 
 */
public class DatabaseForm extends AbstractForm {

    /**
     * The number of items that can be visible in database type combo.
     */
    private static final int VISIBLE_DATABASE_COUNT = 20;

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

    private LabelledText additionParamText;

    private Button standardButton;

    private Button systemButton;

    /**
     * Fields for general jdbc
     */

    private LabelledText generalJdbcUrlText = null;

    private LabelledText generalJdbcUserText = null;

    private LabelledText generalJdbcPasswordText = null;

    private LabelledText generalJdbcSchemaText = null;

    private LabelledText generalJdbcClassNameText = null;

    private LabelledText generalJdbcDriverjarText = null;

    private Button browseJarFilesButton = null;

    /**
     * Anothers Fields.
     */
    private UtilsButton checkButton;

    private Group databaseSettingGroup;

    private Composite typeDbCompositeParent;

    private Composite generalDbCompositeParent;

    private Composite compositeGroupDbSettings;

    private LabelledText generalMappingFileText;

    private Button mappingSelectButton;

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
        setConnectionItem(connectionItem); // must be first.
        setupForm(true);
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

            if (isGeneralJDBC()) {
                switchBetweenTypeandGeneralDB(false);
                initializeGeneralJDBC();
            }
        }
        if (isContextMode()) {
            adaptFormToEditable();
            urlConnectionStringText.setText(getStringConnection());
        } else {
            urlConnectionStringText.setText(getConnection().getURL());

        }
        usernameText.setText(getConnection().getUsername());
        passwordText.setText(getConnection().getPassword());
        serverText.setText(getConnection().getServerName());
        portText.setText(getConnection().getPort());
        datasourceText.setText(getConnection().getDatasourceName());
        additionParamText.setText(getConnection().getAdditionalParams());
        sidOrDatabaseText.setText(getConnection().getSID());
        schemaText.setText(getConnection().getSchema());
        fileField.setText(getConnection().getFileFieldName());
        stringQuoteText.setText(getConnection().getStringQuote());
        nullCharText.setText(getConnection().getNullChar());
        directoryField.setText(getConnection().getDBRootPath());

        checkAS400SpecificCase();
        // PTODO !StandBy! (use width SQL Editor): to define the values of SQL Syntax (need by SQL Editor)
        getConnection().setSqlSynthax(Messages.getString("DatabaseForm.sqlSyntax")); //$NON-NLS-1$
        sqlSyntaxCombo.select(getSqlSyntaxIndex(getConnection().getSqlSynthax()));
        updateStatus(IStatus.OK, ""); //$NON-NLS-1$
    }

    /**
     * DOC YeXiaowei Comment method "initializeGeneralJDBC".
     */
    private void initializeGeneralJDBC() {
        generalJdbcUrlText.setText(getConnection().getURL());
        generalJdbcClassNameText.setText(getConnection().getDriverClass());
        generalJdbcUserText.setText(getConnection().getUsername());
        generalJdbcPasswordText.setText(getConnection().getPassword());
        generalJdbcDriverjarText.setText(getConnection().getDriverJarPath());
        generalMappingFileText.setText(getConnection().getDbmsId());
        generalJdbcSchemaText.setText(getConnection().getSchema() == null ? "" : getConnection().getSchema());
    }

    private void checkAS400SpecificCase() {
        if (getConnection().isStandardSQL() == getConnection().isSystemSQL()) { // create connection
            boolean b = CorePlugin.getDefault().getPreferenceStore().getBoolean(ITalendCorePrefConstants.AS400_SQL_SEG);
            standardButton.setSelection(b);
            systemButton.setSelection(!b);
            getConnection().setStandardSQL(b);
            getConnection().setSystemSQL(!b);
        } else {
            standardButton.setSelection(getConnection().isStandardSQL());
            systemButton.setSelection(getConnection().isSystemSQL());
        }
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
        additionParamText.setReadOnly(isReadOnly());
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
        databaseSettingGroup = Form.createGroup(this, 1, Messages.getString("DatabaseForm.groupDatabaseSettings"), 310);
        compositeGroupDbSettings = Form.startNewGridLayout(databaseSettingGroup, 1);
        layout2 = (GridLayout) compositeGroupDbSettings.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;
        layout2.marginLeft = 0;
        layout2.marginRight = 0;
        layout2.marginWidth = 0;

        compositeDbSettings = new Composite(compositeGroupDbSettings, SWT.NULL);

        compositeDbSettings.setLayout(new GridLayout(3, false));
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.minimumWidth = width;
        // gridData.minimumHeight = 50;
        gridData.widthHint = width;
        // gridData.heightHint = 50;
        compositeDbSettings.setLayoutData(gridData);

        layout2 = (GridLayout) compositeDbSettings.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;

        // Main Fields

        // Database Type Combo
        urlDataStringConnection = new DataStringConnection();

        addDBSelectCombo();

        Label label = new Label(compositeDbSettings, SWT.SEPARATOR | SWT.H_SCROLL);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 3;
        label.setLayoutData(data);

        addFieldsForTypeDB(compositeGroupDbSettings);
        addFieldsForGeneralDB(compositeGroupDbSettings);

        switchBetweenTypeandGeneralDB(true);

        addCheckAndStandardButtons(width, compositeGroupDbSettings);

        checkDBTypeAS400();

    }

    /**
     * DOC YeXiaowei Comment method "addFieldForTypeDB".
     * 
     * @param width
     * @param compositeGroupDbSettings
     */
    private void addFieldsForTypeDB(Composite compositeGroupDbSettings) {

        typeDbCompositeParent = new Composite(compositeGroupDbSettings, SWT.NULL);
        typeDbCompositeParent.setLayout(new GridLayout(3, false));

        GridLayout layout2 = (GridLayout) typeDbCompositeParent.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;

        // Field connectionString
        urlDataStringConnection.setSelectionIndex(dbTypeCombo.getSelectionIndex());
        urlConnectionStringText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.stringConnection"), 2); //$NON-NLS-1$
        urlConnectionStringText.setEditable(false);

        // Field login & password
        usernameText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.login"), 2); //$NON-NLS-1$
        passwordText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.password"), 2, SWT.BORDER //$NON-NLS-1$
                | SWT.SINGLE | SWT.PASSWORD);

        // Another fields
        serverText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.server"), 2); //$NON-NLS-1$
        portText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.port"), 2); //$NON-NLS-1$
        // portText.setTextLimit(5);
        sidOrDatabaseText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.database"), 2); //$NON-NLS-1$
        schemaText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.schema"), 2); //$NON-NLS-1$
        datasourceText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.dataSource"), 2); //$NON-NLS-1$
        additionParamText = new LabelledText(typeDbCompositeParent, Messages.getString("DatabaseForm.AddParams"), 2); //$NON-NLS-1$

        String[] extensions = { "*.*" }; //$NON-NLS-1$
        fileField = new LabelledFileField(typeDbCompositeParent, Messages.getString("DatabaseForm.mdbFile"), extensions); //$NON-NLS-1$
        directoryField = new LabelledDirectoryField(typeDbCompositeParent, "DB Root Path"); //$NON-NLS-1$
    }

    private void addFieldsForGeneralDB(Composite parent) {

        generalDbCompositeParent = new Composite(parent, SWT.NULL);

        generalDbCompositeParent.setLayout(new GridLayout(3, false));

        GridLayout layout2 = (GridLayout) generalDbCompositeParent.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;

        generalJdbcUrlText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.general.url"), 2); //$NON-NLS-1$

        generalJdbcDriverjarText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.general.jarfile"),
                1);

        browseJarFilesButton = new Button(generalDbCompositeParent, SWT.NONE);
        browseJarFilesButton.setText("...");
        browseJarFilesButton.setToolTipText("Select jar file");

        generalJdbcClassNameText = new LabelledText(generalDbCompositeParent, Messages
                .getString("DatabaseForm.general.classname"), 2); //$NON-NLS-1$

        generalJdbcUserText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.general.user"), 2); //$NON-NLS-1$

        generalJdbcPasswordText = new LabelledText(generalDbCompositeParent,
                Messages.getString("DatabaseForm.general.password"), 2); //$NON-NLS-1$

        generalJdbcSchemaText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.schema"), 2); //$NON-NLS-1$ 

        generalMappingFileText = new LabelledText(generalDbCompositeParent, Messages.getString("DatabaseForm.general.mapping"), 1);
        // generalMappingFileText.setEditable(false);

        mappingSelectButton = new Button(generalDbCompositeParent, SWT.NONE);
        mappingSelectButton.setText("...");
        mappingSelectButton.setToolTipText("Select mapping rule");

    }

    /**
     * Hidden one composite and display another one
     * <p>
     * DOC YeXiaowei Comment method "switchBetweenTypeandGeneralDB".
     * 
     * @param hiddenGeneral
     */
    private void switchBetweenTypeandGeneralDB(boolean hiddenGeneral) {

        GridData dataGeneralDb = null;
        GridData dataTypeDb = null;

        if (hiddenGeneral) {
            dataGeneralDb = new GridData(GridData.FILL_HORIZONTAL);
            dataGeneralDb.heightHint = 0;

            dataTypeDb = new GridData(GridData.FILL_BOTH);
        } else {
            dataGeneralDb = new GridData(GridData.FILL_BOTH);

            dataTypeDb = new GridData(GridData.FILL_HORIZONTAL);
            dataTypeDb.heightHint = 0;
        }

        generalDbCompositeParent.setLayoutData(dataGeneralDb);
        typeDbCompositeParent.setLayoutData(dataTypeDb);

        compositeGroupDbSettings.layout();
    }

    /**
     * DOC YeXiaowei Comment method "addCheckAndStandardButtons".
     * 
     * @param width
     * @param compositeGroupDbSettings
     */
    private void addCheckAndStandardButtons(int width, Composite compositeGroupDbSettings) {

        GridLayout layout2 = null;

        fileField.hide();
        directoryField.hide();
        // Button Check
        Composite compositeCheckButton = Form.startNewGridLayout(compositeGroupDbSettings, 1, false, SWT.CENTER, SWT.TOP);
        layout2 = (GridLayout) compositeCheckButton.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;
        checkButton = new UtilsButton(compositeCheckButton, Messages.getString("DatabaseForm.check"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                HEIGHT_BUTTON_PIXEL);
        checkButton.setEnabled(false);

        // Group Database Properties
        Group group1 = Form.createGroup(this, 1, Messages.getString("DatabaseForm.groupDatabaseProperties")); //$NON-NLS-1$
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.minimumHeight = 80;
        gridData.heightHint = 80;
        group1.setLayoutData(gridData);
        // Composite compositeGroupDbProperties = Form.startNewGridLayout(group1, 4, false, SWT.LEFT, SWT.CENTER);
        Composite compositeGroupDbProperties = Form.startNewDimensionnedGridLayout(group1, 4, width, 70);

        // PTODO !StandBy! (use width SQL Editor): to define the values of SQL Syntax (need by SQL Editor)
        String[] item = { "SQL 92" }; //$NON-NLS-1$
        sqlSyntaxCombo = new LabelledCombo(compositeGroupDbProperties, Messages.getString("DatabaseForm.sqlSyntax"), null, item, //$NON-NLS-1$
                3);

        stringQuoteText = new LabelledText(compositeGroupDbProperties, Messages.getString("DatabaseForm.stringQuote"), false); //$NON-NLS-1$
        nullCharText = new LabelledText(compositeGroupDbProperties, Messages.getString("DatabaseForm.nullChar"), false); //$NON-NLS-1$
        gridData = new GridData();
        gridData.horizontalSpan = 2;
        standardButton = new Button(compositeGroupDbProperties, SWT.RADIO);
        standardButton.setText(Messages.getString("DatabaseForm.StandardSQL")); //$NON-NLS-1$
        standardButton.setLayoutData(gridData);
        systemButton = new Button(compositeGroupDbProperties, SWT.RADIO);
        systemButton.setText(Messages.getString("DatabaseForm.SystemSQL")); //$NON-NLS-1$
        systemButton.setLayoutData(gridData);
    }

    /**
     * DOC YeXiaowei Comment method "addDBSelectCombo". Extract method form addFields()
     */
    private void addDBSelectCombo() {
        // PTODO cantoine : HIDDEN some Database connection in function of project MODE (Perl/Java).
        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.PERL) {
            Collection<String> databasePerl = new ArrayList<String>(Arrays.asList(urlDataStringConnection.getItem()));
            databasePerl.remove("Microsoft SQL Server"); //$NON-NLS-1$
            databasePerl.remove("Ingres"); //$NON-NLS-1$
            databasePerl.remove("Interbase"); //$NON-NLS-1$
            databasePerl.remove("FireBird"); //$NON-NLS-1$
            databasePerl.remove("Informix"); //$NON-NLS-1$
            databasePerl.remove("Access"); //$NON-NLS-1$
            databasePerl.remove("Teradata"); //$NON-NLS-1$
            databasePerl.remove("AS400"); //$NON-NLS-1$

            databasePerl.remove("JavaDB Embeded"); //$NON-NLS-1$
            databasePerl.remove("JavaDB JCCJDBC"); //$NON-NLS-1$
            databasePerl.remove(Messages.getString("DatabaseForm.10")); //$NON-NLS-1$

            databasePerl.remove("HSQLDB Server"); //$NON-NLS-1$
            databasePerl.remove("HSQLDB WebServer"); //$NON-NLS-1$
            databasePerl.remove("HSQLDB In-Process"); //$NON-NLS-1$

            String[] dbPerl = databasePerl.toArray(new String[databasePerl.size()]);
            dbTypeCombo = new LabelledCombo(compositeDbSettings, Messages.getString("DatabaseForm.dbType"), Messages //$NON-NLS-1$
                    .getString("DatabaseForm.dbTypeTip"), dbPerl, 2, true); //$NON-NLS-1$
        } else {
            dbTypeCombo = new LabelledCombo(compositeDbSettings, Messages.getString("DatabaseForm.dbType"), Messages //$NON-NLS-1$
                    .getString("DatabaseForm.dbTypeTip"), urlDataStringConnection.getItem(), 2, true); //$NON-NLS-1$
        }

        // configure the visible item of database combo
        int visibleItemCount = dbTypeCombo.getCombo().getItemCount();
        if (visibleItemCount > VISIBLE_DATABASE_COUNT) {
            visibleItemCount = VISIBLE_DATABASE_COUNT;
        }
        dbTypeCombo.getCombo().setVisibleItemCount(visibleItemCount);
    }

    /**
     * Check DBType is AS400,set systemButton and stardardButton visible.a
     */
    private void checkDBTypeAS400() {
        if (dbTypeCombo.getSelectionIndex() == 16 || dbTypeCombo.getSelectionIndex() == 25 || dbTypeCombo.getSelectionIndex() == 26) {
            standardButton.setVisible(true);
            systemButton.setVisible(true);
        } else {
            standardButton.setVisible(false);
            systemButton.setVisible(false);
        }

    }

    /**
     * Check data connection.
     */
    private void checkConnection() {

        checkButton.setEnabled(false);
        if (connectionItem.getConnection() instanceof DatabaseConnection) {
            DatabaseConnection c = (DatabaseConnection) connectionItem.getConnection();
            if (c.getProductId().equals(EDatabaseTypeName.ORACLEFORSID.getProduct())) {
                if (!isContextMode()) {
                    schemaText.setText(schemaText.getText().toUpperCase());
                }
            }
        }
        ManagerConnection managerConnection = new ManagerConnection();

        if (isContextMode()) { // context mode

            String urlStr = DBConnectionContextUtils.setManagerConnectionValues(managerConnection, connectionItem, dbTypeCombo
                    .getItem(dbTypeCombo.getSelectionIndex()), dbTypeCombo.getSelectionIndex());
            if (urlStr == null) {
                urlStr = getStringConnection();
            }
            urlConnectionStringText.setText(urlStr);
        } else {
            // set the value
            managerConnection.setValue(0, dbTypeCombo.getItem(dbTypeCombo.getSelectionIndex()),
                    isGeneralJDBC() ? generalJdbcUrlText.getText() : urlConnectionStringText.getText(), serverText.getText(),
                    isGeneralJDBC() ? generalJdbcUserText.getText() : usernameText.getText(),
                    isGeneralJDBC() ? generalJdbcPasswordText.getText() : passwordText.getText(), sidOrDatabaseText.getText(),
                    portText.getText(), fileField.getText(), datasourceText.getText(), isGeneralJDBC() ? generalJdbcSchemaText
                            .getText() : schemaText.getText(), additionParamText.getText(), generalJdbcClassNameText.getText(),
                    generalJdbcDriverjarText.getText());

            managerConnection.setDbRootPath(directoryField.getText());

        }
        managerConnection.setValueProperties(sqlSyntaxCombo.getItem(sqlSyntaxCombo.getSelectionIndex()), stringQuoteText
                .getText(), nullCharText.getText());
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
                if (isContextMode()) {
                    adaptFormToEditable();
                } else {
                    setPropertiesFormEditable(true);
                }
            }

        } else {
            String mainMsg = Messages.getString("DatabaseForm.checkFailure") + " " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("DatabaseForm.checkFailureTip"); //$NON-NLS-1$
            if (!isReadOnly()) {
                updateStatus(IStatus.WARNING, mainMsg);
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
                if (!isContextMode()) {
                    if (dbTypeCombo.getSelectionIndex() >= 0) {
                        setPropertiesFormEditable(false);
                        urlConnectionStringText.setEditable(true);
                    } else {
                        updateStatus(IStatus.ERROR, Messages.getString("DatabaseForm.alert", dbTypeCombo.getLabel())); //$NON-NLS-1$
                    }
                }
            }
        });

        urlConnectionStringText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (urlConnectionStringText.getEditable()) {
                        if (!databaseSettingIsValide) {
                            updateStatus(IStatus.INFO, Messages.getString("DatabaseForm.checkInformation")); //$NON-NLS-1$
                        }
                        databaseSettingIsValide = false;
                        checkButton.setEnabled(true);
                        String[] s = urlDataStringConnection.getAnalyse(urlConnectionStringText.getText());
                        // if the ConnectionString write manually don't correspond width selectedIndex of combo DbType
                        // we search if another regex corresponding at this string
                        int selection = new Integer(s[0]);
                        if (selection != dbTypeCombo.getSelectionIndex()) {
                            dbTypeCombo.select(new Integer(s[0]));
                            dbTypeCombo.forceFocus();
                        }

                        int index = 1;
                        if (s[index] != "") {//$NON-NLS-1$
                            if (selection == DataStringConnection.DBTYPE_GENERIC_ODBC
                                    || selection == DataStringConnection.DBTYPE_MICORSOFT_SQL_SERVER_ODBC_DRIVER) {
                                datasourceText.setText(s[index]);
                                getConnection().setDatasourceName(s[index]);
                            } else if (selection == DataStringConnection.DBTYPE_SQLITE
                                    || selection == DataStringConnection.DBTYPE_ACCESS) {
                                fileField.setText(s[index]);
                                getConnection().setFileFieldName(s[index]);
                            } else if (selection == DataStringConnection.DBTYPE_JAVADB_EMBEDED) {
                                sidOrDatabaseText.setText(s[index]);
                                getConnection().setSID(s[index]);
                            } else if (selection == DataStringConnection.DBTYPE_HSQLDB_IN_PROCESS) {
                                directoryField.setText(s[index]);
                                getConnection().setDBRootPath(s[index]);
                            } else {
                                serverText.setText(s[index]);
                                getConnection().setServerName(s[index]);
                            }
                        }

                        index = 2;
                        if (s[index] != "") { //$NON-NLS-1$
                            if (selection == DataStringConnection.DBTYPE_INTERBASE
                                    || selection == DataStringConnection.DBTYPE_TERDATA
                                    || selection == DataStringConnection.DBTYPE_AS400
                                    || selection == DataStringConnection.DBTYPE_HSQLDB_IN_PROCESS) {
                                sidOrDatabaseText.setText(s[index]);
                                getConnection().setSID(s[index]);
                            } else if (selection == DataStringConnection.DBTYPE_FIREBIRD) {
                                fileField.setText(s[index]);
                                getConnection().setFileFieldName(s[index]);
                            } else {
                                portText.setText(s[index]);
                                getConnection().setPort(s[index]);
                            }
                        }

                        index = 3;
                        if (s[index] != "") { //$NON-NLS-1$
                            if (selection != DataStringConnection.DBTYPE_AS400) {
                                sidOrDatabaseText.setText(s[index]);
                                getConnection().setSID(s[index]);
                            }
                        }

                        index = 4;
                        if (s[index] != "") { //$NON-NLS-1$
                            if (selection == DataStringConnection.DBTYPE_INFORMIX) {
                                datasourceText.setText(s[index]);
                                getConnection().setDatasourceName(s[index]);
                            } else {
                                additionParamText.setText(s[index]);
                                getConnection().setAdditionalParams(s[index]);
                            }
                        }

                        index = 5;
                        if (s[index] != "") {
                            additionParamText.setText(s[index]);
                            getConnection().setAdditionalParams(s[index]);
                        }
                    }
                    checkDBTypeAS400();
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
                if (isContextMode()) {
                    //
                } else {
                    if (dbTypeCombo.getSelectionIndex() == -1) {
                        dbTypeCombo.forceFocus();
                    }
                    setPropertiesFormEditable(dbTypeCombo.getSelectionIndex() > -1);
                    urlConnectionStringText.setEditable(false);
                }
            }
        };

        usernameText.addListener(SWT.FocusIn, listener);
        passwordText.addListener(SWT.FocusIn, listener);
        serverText.addListener(SWT.FocusIn, listener);
        portText.addListener(SWT.FocusIn, listener);
        sidOrDatabaseText.addListener(SWT.FocusIn, listener);
        datasourceText.addListener(SWT.FocusIn, listener);
        schemaText.addListener(SWT.FocusIn, listener);
        additionParamText.addListener(SWT.FocusIn, listener);
        urlConnectionStringText.addListener(SWT.FocusIn, listener);

        // serverText : Event modifyText
        serverText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setServerName(serverText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        // portText : Event modifyText
        portText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setPort(portText.getText());
                        checkFieldsValue();
                    }
                    // Check port
                    boolean b = true;
                    if (getConnection().getDatabaseType().equals("Ingres")) { //$NON-NLS-1$
                        b = Pattern.matches(Messages.getString("DatabaseForm.ingresDBRegex"), portText.getText()); //$NON-NLS-1$
                    } else {
                        b = Pattern.matches(Messages.getString("DatabaseForm.otherDBRegex"), portText.getText()); //$NON-NLS-1$
                    }
                    if (b) {
                        b = portText.getText().length() <= 5;
                    }
                    evaluateTextField(b);
                }
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
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setUsername(usernameText.getText());
                    }
                }
            }
        });

        // passwordText : Event modifyText
        passwordText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setPassword(passwordText.getText());
                    }
                }
            }
        });

        // sidOrDatabaseText : Event modifyText
        sidOrDatabaseText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setSID(sidOrDatabaseText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        // datasourceText : Event modifyText
        datasourceText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setDatasourceName(datasourceText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        // schemaText : Event modifyText
        schemaText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setSchema(schemaText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });
        // additional parameters: Event modifyText
        additionParamText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setAdditionalParams(additionParamText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });
        // standardButton parameters: Event modifyText
        standardButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!isContextMode()) {
                    getConnection().setStandardSQL(standardButton.getSelection());
                    getConnection().setSystemSQL(systemButton.getSelection());
                }
            }

        });
        // systemButton parameters: Event modifyText
        systemButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!isContextMode()) {
                    getConnection().setStandardSQL(standardButton.getSelection());
                    getConnection().setSystemSQL(systemButton.getSelection());
                }
            }

        });
        // Event dbTypeCombo
        dbTypeCombo.addModifyListener(new ModifyListener() {

            // Event Modify
            public void modifyText(final ModifyEvent e) {

                boolean hiddenGeneral = !isGeneralJDBC();
                // change controls
                switchBetweenTypeandGeneralDB(hiddenGeneral);

                if (!isContextMode()) {
                    urlDataStringConnection.setSelectionIndex(dbTypeCombo.getSelectionIndex());
                    setPropertiesFormEditable(true);
                    getConnection().setDatabaseType(dbTypeCombo.getText());
                    portText.setText(urlDataStringConnection.getDefaultPort());

                    final String product = EDatabaseTypeName.getTypeFromDisplayName(getConnection().getDatabaseType())
                            .getProduct();
                    getConnection().setProductId(product);

                    String mapping = null;

                    if (product == null || product.equals("General JDBC")) {
                        mapping = generalMappingFileText.getText();
                    } else {
                        if (MetadataTalendType.getDefaultDbmsFromProduct(filterProductName(product)) != null) {
                            mapping = MetadataTalendType.getDefaultDbmsFromProduct(filterProductName(product)).getId();
                        }
                    }
                    if (mapping == null) {
                        mapping = "mysql_id";// default value
                    }

                    getConnection().setDbmsId(mapping);
                    if (dbTypeCombo.getSelectionIndex() == 0) {
                        // additionParamText.setText(DataStringConnection.mySQlDefaultValue);
                        additionParamText.setText(DataStringConnection.mySQlDefaultValue);
                    }
                    if (dbTypeCombo.getSelectionIndex() == 16) {
                        additionParamText.setText(DataStringConnection.as400DefaultValue);
                    }
                    checkAS400SpecificCase();
                    checkFieldsValue();
                }
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
                if (!isContextMode()) {
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
            }
        });
        // Event Modify
        fileField.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        getConnection().setFileFieldName(fileField.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        directoryField.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (!urlConnectionStringText.getEditable()) {
                        String text = directoryField.getText();
                        getConnection().setDBRootPath(text);
                        checkFieldsValue();
                    }
                }
            }
        });
        // sqlSyntaxText : Event modifyText
        sqlSyntaxCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().setSqlSynthax(sqlSyntaxCombo.getText());
                }
            }
        });

        // nullCharText : Event modifyText
        nullCharText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().setNullChar(nullCharText.getText());
                }
            }
        });

        // stringQuoteText : Event modifyText
        stringQuoteText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    getConnection().setStringQuote(stringQuoteText.getText());
                }
            }
        });

        addGeneralDbFieldsListeners();

        // urlConnectionStringText : Event modifyText
        // urlConnectionStringText.addModifyListener(new ModifyListener() {
        //
        // public void modifyText(final ModifyEvent e) {
        // if (!isContextMode()) {
        // getConnection().setURL(urlConnectionStringText.getText());
        // getConnection().setServerName(serverText.getText());
        // getConnection().setPort(portText.getText());
        // getConnection().setUsername(usernameText.getText());
        // getConnection().setPassword(passwordText.getText());
        // getConnection().setSID(sidOrDatabaseText.getText());
        // getConnection().setDatasourceName(datasourceText.getText());
        // getConnection().setSchema(schemaText.getText());
        // getConnection().setDatabaseType(dbTypeCombo.getText());
        // getConnection().setFileFieldName(fileField.getText());
        // getConnection().setDBRootPath(directoryField.getText());
        // getConnection().setAdditionalParams(additionParamText.getText());
        // }
        // }
        // });

    }

    private String filterProductName(final String product) {
        if (product.startsWith("ORACLE")) {
            return "ORACLE";
        }
        return product;
    }

    /**
     * DOC YeXiaowei Comment method "addGeneralDbFieldsListeners".
     */
    private void addGeneralDbFieldsListeners() {

        generalJdbcClassNameText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalJdbcClassNameText.getText())) {
                        getConnection().setDriverClass(generalJdbcClassNameText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        generalJdbcDriverjarText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalJdbcDriverjarText.getText())) {
                        getConnection().setDriverJarPath(generalJdbcDriverjarText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        generalJdbcUrlText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalJdbcUrlText.getText())) {
                        getConnection().setURL(generalJdbcUrlText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        generalJdbcPasswordText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalJdbcPasswordText.getText())) {
                        getConnection().setPassword(generalJdbcPasswordText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        generalJdbcSchemaText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalJdbcSchemaText.getText())) {
                        getConnection().setSchema(generalJdbcSchemaText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        generalJdbcUserText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalJdbcUserText.getText())) {
                        getConnection().setUsername(generalJdbcUserText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        generalMappingFileText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (validText(generalMappingFileText.getText())) {
                        getConnection().setDbmsId(generalMappingFileText.getText());
                        checkFieldsValue();
                    }
                }
            }
        });

        browseJarFilesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell());
                dialog.setFilterExtensions(new String[] { "*.jar", "*.zip", "*.*", "*" });
                String userDir = System.getProperty("user.dir");
                String pathSeparator = System.getProperty("path.separator");
                dialog.setFilterPath(userDir + pathSeparator + "lib" + pathSeparator + "java");
                String path = dialog.open();
                if (path == null) {
                    return;
                }
                generalJdbcDriverjarText.setText(path);
            }

        });

        mappingSelectButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                MappingFileSelectDialog dialog = new MappingFileSelectDialog(getShell());
                dialog.open();
                generalMappingFileText.setText(dialog.getSelectId());
            }
        });
    }

    /**
     * 
     * DOC YeXiaowei Comment method "isGeneralJDBC".
     * 
     * @return
     */
    private boolean isGeneralJDBC() {
        return dbTypeCombo.getText().equals(DataStringConnection.GENERAL_JDBC);
    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkConnection().
     */
    @Override
    public boolean checkFieldsValue() {
        if (isContextMode()) {
            return true;
        }
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

        boolean checkGeneralDB = isGeneralJDBC();

        if (!checkGeneralDB) {
            if (!checkTypeDBFieldValues()) {
                return false;
            }
        } else {
            if (!checkGeneralDBFieldValues()) {
                return false;
            }
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

    private boolean checkGeneralDBFieldValues() {
        String value = generalJdbcUrlText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", generalJdbcUrlText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        value = generalJdbcDriverjarText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", generalJdbcDriverjarText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        value = generalJdbcClassNameText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", generalJdbcClassNameText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        value = generalJdbcUserText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", generalJdbcUserText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        value = generalJdbcPasswordText.getText();
        if (!validText(value)) {
            updateStatus(IStatus.WARNING, Messages.getString("DatabaseForm.alert", generalJdbcPasswordText.getLabelText())); //$NON-NLS-1$
            return false;
        }

        updateStatus(IStatus.OK, null);
        return true;
    }

    private boolean validText(final String value) {
        return value != null && !value.equals("");
    }

    private boolean checkTypeDBFieldValues() {

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
        updateStatus(IStatus.OK, null);
        return true;

    }

    private String getStringConnection() {
        String s = null;
        if (isContextMode()) {
            s = DBConnectionContextUtils.getUrlConnectionString(dbTypeCombo.getSelectionIndex(), connectionItem, true)
                    .getUrlConnectionStr();
        } else {
            s = urlDataStringConnection.getString(dbTypeCombo.getSelectionIndex(), serverText.getText(), usernameText.getText(),
                    passwordText.getText(), portText.getText(), sidOrDatabaseText.getText(), fileField.getText().toLowerCase(),
                    datasourceText.getText(), directoryField.getText(), additionParamText.getText());
        }

        return s;
    }

    private void updateCheckButton() {
        // update checkEnable
        if (isContextMode()) {
            checkButton.setEnabled(true);
        } else {
            checkButton.setEnabled((dbTypeCombo.getSelectionIndex() >= 0)
                    && (getStringConnection() != urlDataStringConnection.getStringConnectionTemplate()));
            if (isGeneralJDBC()) {
                checkButton.setEnabled(true);
            }
        }
    }

    /**
     * SetEditable fields.
     * 
     * @param boolean
     */
    private void setPropertiesFormEditable(boolean visible) {
        clearContextParams();
        EDBParamName sidOrDatabase = null;
        // update the UI label
        if (urlDataStringConnection.isSchemaNeeded()) {
            if (urlDataStringConnection.getStringConnectionTemplate().contains("(description=(address=(protocol=tcp)")) { //$NON-NLS-1$
                sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.service_name")); //$NON-NLS-1$
                sidOrDatabaseText.setLabelWidth(65);
                sidOrDatabase = EDBParamName.ServiceName;
            } else {
                sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.sid")); //$NON-NLS-1$
                sidOrDatabase = EDBParamName.Sid;
            }
        } else {
            sidOrDatabaseText.setLabelText(Messages.getString("DatabaseForm.database")); //$NON-NLS-1$
            sidOrDatabase = EDBParamName.Database;
        }

        addContextParams(EDBParamName.Login, visible);
        addContextParams(EDBParamName.Password, visible);
        // update the UI Fields
        usernameText.setEditable(visible);
        passwordText.setEditable(visible);
        serverText.setEditable(false);
        portText.setEditable(false);
        sidOrDatabaseText.setEditable(false);
        datasourceText.setEditable(false);
        additionParamText.setEditable(false);
        schemaText.setEditable(false);
        fileField.setEditable(false);
        directoryField.setEditable(false);

        if (dbTypeCombo.getSelectionIndex() < 0) {
            urlConnectionStringText.setEditable(false);
        } else {
            String s = urlDataStringConnection.getStringConnectionTemplate();
            urlConnectionStringText.setEditable(!visible);

            if (s != null && s.startsWith("jdbc:jtds:sqlserver:")) { //$NON-NLS-1$
                schemaText.setEditable(true);
                addContextParams(EDBParamName.Schema, true);
                if (schemaText.getText().equals("")) { //$NON-NLS-1$
                    schemaText.setText("dbo"); //$NON-NLS-1$
                }
            }

            if (s.contains("<host>")) { //$NON-NLS-1$
                serverText.setEditable(visible);
                addContextParams(EDBParamName.Server, visible);
            }
            if (s.contains("<port>")) { //$NON-NLS-1$
                portText.setEditable(visible);
                addContextParams(EDBParamName.Port, visible);
            }
            if (s.contains("<sid>") || s.contains("<service_name>")) { //$NON-NLS-1$ //$NON-NLS-2$
                sidOrDatabaseText.setEditable(visible);
                addContextParams(sidOrDatabase, visible);
            }
            if (s.contains("<filename>")) { // && //$NON-NLS-1$
                // urlDataStringConnection.getStringConnectionTemplate().contains("jdbc:sqlite")
                fileField.show();
                fileField.setEditable(visible);
                addContextParams(EDBParamName.File, visible);
                boolean isSqlLite = false;
                if (EDatabaseTypeName.getTypeFromDisplayName(getConnection().getDatabaseType()).equals(EDatabaseTypeName.SQLITE)) {
                    isSqlLite = true;
                } else {
                    isSqlLite = false;
                }
                usernameText.setEditable(!isSqlLite);
                passwordText.setEditable(!isSqlLite);
                addContextParams(EDBParamName.Login, !isSqlLite);
                addContextParams(EDBParamName.Password, !isSqlLite);
            } else {
                fileField.hide();
                addContextParams(EDBParamName.File, false);
            }
            // if (s.contains("<filename>")) { //$NON-NLS-1$
            // fileField.setEditable(visible);
            // }
            if (s.contains("<datasource>")) { //$NON-NLS-1$
                datasourceText.show();
                datasourceText.setEditable(visible);
                addContextParams(EDBParamName.Datasource, visible);
            } else {
                datasourceText.hide();
                addContextParams(EDBParamName.Datasource, false);
            }

            if (s.contains("<dbRootPath>")) { //$NON-NLS-1$
                directoryField.show();
                directoryField.setEditable(visible);
                sidOrDatabaseText.setEditable(visible);
                addContextParams(EDBParamName.DBRootPath, visible);
                addContextParams(sidOrDatabase, visible);
            } else {
                directoryField.hide();
                addContextParams(EDBParamName.DBRootPath, false);
            }

            if (urlDataStringConnection.isSchemaNeeded()) {
                schemaText.setEditable(visible);
                addContextParams(EDBParamName.Schema, visible);
            }
            if (urlDataStringConnection.isAddtionParamsNeeded()) {
                additionParamText.setEditable(visible);
                addContextParams(EDBParamName.AdditionalParams, visible);
            }
        }
        compositeDbSettings.layout();
        databaseSettingGroup.layout();
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
        if (isContextMode()) {
            adaptFormToEditable();
        } else {
            setPropertiesFormEditable((urlDataStringConnection.getIndexOfLabel(getConnection().getDatabaseType()) > -1));
        }
        if (isReadOnly() != readOnly) {
            adaptFormToReadOnly();
        }
    }

    protected DatabaseConnection getConnection() {
        return (DatabaseConnection) connectionItem.getConnection();
    }

    protected void evaluateTextField(boolean b) {

        if (b) {
            updateStatus(IStatus.OK, null);
        } else {
            updateStatus(IStatus.ERROR, Messages.getString("DatabaseForm.portError")); //$NON-NLS-1$

        }
    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        dbTypeCombo.setReadOnly(isContextMode());

        urlConnectionStringText.setEditable(!isContextMode());
        usernameText.setEditable(!isContextMode());
        passwordText.setEditable(!isContextMode());
        serverText.setEditable(!isContextMode());
        portText.setEditable(!isContextMode());
        sidOrDatabaseText.setEditable(!isContextMode());
        schemaText.setEditable(!isContextMode());
        datasourceText.setEditable(!isContextMode());
        additionParamText.setEditable(!isContextMode());
        fileField.setEditable(!isContextMode());
        directoryField.setEditable(!isContextMode());

        sqlSyntaxCombo.setReadOnly(isContextMode());
        stringQuoteText.setEditable(!isContextMode());
        nullCharText.setEditable(!isContextMode());
        if (isContextMode()) {
            passwordText.getTextControl().setEchoChar('\0');
        } else {
            passwordText.getTextControl().setEchoChar('*');
        }
    }

}
