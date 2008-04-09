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
package org.talend.designer.core.ui.preferences;

import org.eclipse.gmf.runtime.common.ui.preferences.CheckBoxFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.commons.utils.workbench.preferences.ComboFieldEditor;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.views.statsandlogs.StatsAndLogsViewHelper;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.dialog.RepositoryReviewDialog;
import org.talend.repository.ui.utils.DataStringConnection;
import org.talend.repository.ui.views.RepositoryContentProvider;
import org.talend.repository.ui.views.RepositoryView;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public abstract class StatsAndLogsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /**
     * 
     */
    public static final String CONNECTION_ITEM_LABEL = "_CONNECTION_ITEM_LABEL";

    /**
     * 
     */
    private static final String REPOSITORY = "Repository";

    /**
     * 
     */
    private static final String BUILD_IN = "Build-in";

    private ECodeLanguage language;

    private String languagePrefix;

    /** controls. * */
    private CheckBoxFieldEditor onStatCatcherField;

    private CheckBoxFieldEditor onLogCatcherField;

    private CheckBoxFieldEditor onMetterCatcherField;

    private CheckBoxFieldEditor onFilesField;

    private DirectoryFieldEditor filePathField;

    private StringFieldEditor statsFileNameField;

    private StringFieldEditor logsFileNameField;

    private StringFieldEditor metterFileNameField;

    private CheckBoxFieldEditor onDatabaseField;

    private ComboFieldEditor dbTypeField;

    private StringFieldEditor hostField;

    private StringFieldEditor additionParamField;

    private FileFieldEditor dabasePathField;

    private StringFieldEditor portField;

    private StringFieldEditor dbNameField;

    private StringFieldEditor schemaField;

    private StringFieldEditor userField;

    private StringFieldEditor passwordField;

    private StringFieldEditor logsTableField;

    private StringFieldEditor statsTableField;

    private StringFieldEditor metterTableField;

    private CheckBoxFieldEditor catchRuntimeErrorsField;

    private CheckBoxFieldEditor catchUserErrorsField;

    private CheckBoxFieldEditor catchUserWarningField;

    private CheckBoxFieldEditor catchRealtimeStatsField;

    private Composite parent;

    private Composite dbTypeComposite;

    private Composite finalPart;

    private Combo comboRepositoryType;

    private ConnectionItem connectionItem;

    /**
     * Default constructor.
     */
    public StatsAndLogsPreferencePage(ECodeLanguage language) {
        super(GRID);
        this.language = language;
        setPreferenceStore(DesignerPlugin.getDefault().getPreferenceStore());
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        languagePrefix = language.toString() + "_";

        createFields();
        updateEnableStateFromPreferences();
        addListeners();
    }

    private void updateFileContent(ConnectionItem connectionItem) {
        DatabaseConnection conn = (DatabaseConnection) connectionItem.getConnection();
        DataStringConnection url = new DataStringConnection();
        String[] connectionTypeLabels = url.getItem();
        int selectionIndex = 0;
        for (; selectionIndex < connectionTypeLabels.length; selectionIndex++) {
            if (connectionTypeLabels[selectionIndex].equals(conn.getDatabaseType())) {
                break;
            }
        }
        url.setSelectionIndex(selectionIndex);
        String stringConnection = url.getStringConnectionTemplate();

        filePathField.getTextControl(parent).setText(conn.getFileFieldName());
        hostField.getTextControl(parent).setText(conn.getServerName());
        portField.getTextControl(parent).setText(conn.getPort());
        dbNameField.getTextControl(parent).setText(conn.getDatasourceName());
        schemaField.getTextControl(parent).setText(conn.getSchema());
        userField.getTextControl(parent).setText(conn.getUsername());
        passwordField.getTextControl(parent).setText(conn.getPassword());
        dabasePathField.getTextControl(parent).setText(conn.getDBRootPath() == null ? "" : conn.getDBRootPath());
        additionParamField.getTextControl(parent).setText(conn.getAdditionalParams() == null ? "" : conn.getAdditionalParams());

        if (stringConnection != null && stringConnection.startsWith("jdbc:jtds:sqlserver:")) {
            schemaField.getTextControl(parent).setEditable(true);
            if (schemaField.getTextControl(parent).getText().equals("")) {
                schemaField.getTextControl(parent).setText("dbo");
            }
        }

        disableAllDbFields();

        userField.setEnabled(true, parent);
        passwordField.setEnabled(true, parent);

        boolean visible = true;

        if (stringConnection.contains("<host>")) { //$NON-NLS-1$
            hostField.setEnabled(visible, parent);
        }
        if (stringConnection.contains("<port>")) { //$NON-NLS-1$
            portField.setEnabled(visible, parent);
        }
        if (stringConnection.contains("<sid>") || stringConnection.contains("<service_name>")) { //$NON-NLS-1$ //$NON-NLS-2$
            // sidOrDatabaseText.setEditable(visible);
        }
        if (stringConnection.contains("<filename>")) { // &&
            if (EDatabaseTypeName.getTypeFromDisplayName(conn.getDatabaseType()).equals(EDatabaseTypeName.SQLITE)) {
                userField.setEnabled(false, parent);
                passwordField.setEnabled(false, parent);
            } else {
                userField.setEnabled(true, parent);
                passwordField.setEnabled(true, parent);
            }
            dabasePathField.setEnabled(visible, parent);
        }

        if (stringConnection.contains("<datasource>")) { //$NON-NLS-1$
            // datasourceText.setEditable(visible);
        }
        if (stringConnection.contains("<dbRootPath>")) {
            // directoryField.setEditable(visible);
            // sidOrDatabaseText.setEditable(visible);
        }
        if (url.isSchemaNeeded()) {
            schemaField.getTextControl(parent).setEditable(visible);
        }
        if (url.isAddtionParamsNeeded()) {
            additionParamField.getTextControl(parent).setEditable(visible);
        }

    }

    public void disableAllDbFields() {
        hostField.setEnabled(false, parent);
        portField.setEnabled(false, parent);
        dbNameField.setEnabled(false, parent);
        additionParamField.setEnabled(false, parent);
        schemaField.setEnabled(false, parent);
        userField.setEnabled(false, parent);
        passwordField.setEnabled(false, parent);
        dabasePathField.setEnabled(false, parent);
        statsTableField.setEnabled(false, parent);
        logsTableField.setEnabled(false, parent);
        metterTableField.setEnabled(false, parent);
    }

    /**
     * yzhang Comment method "formRepositoryTypeText".
     * 
     * @param dialog
     * @return
     */
    private String formRepositoryTypeText(ConnectionItem connectionItem) {
        if (connectionItem == null) {
            return "";
        }
        ERepositoryObjectType repositoryObjectType = ERepositoryObjectType.getItemType(connectionItem);
        String aliasName = repositoryObjectType.getAlias();
        org.talend.core.model.metadata.builder.connection.Connection connection = connectionItem.getConnection();
        if (connection instanceof DatabaseConnection) {
            String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, "TYPE"); //$NON-NLS-1$
            aliasName += " (" + currentDbType + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return aliasName + ":" + connectionItem.getProperty().getLabel();
    }

    private void createFields() {
        parent = getFieldEditorParent();
        int languageType = language == ECodeLanguage.JAVA ? 1 : 0;

        Composite titlePart = new Composite(parent, SWT.None);
        titlePart.setLayout(new GridLayout());

        onStatCatcherField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_STATCATCHER_FLAG.getName(),
                EParameterName.ON_STATCATCHER_FLAG.getDisplayName(), titlePart);
        onLogCatcherField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_LOGCATCHER_FLAG.getName(),
                EParameterName.ON_LOGCATCHER_FLAG.getDisplayName(), titlePart);
        onMetterCatcherField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_METERCATCHER_FLAG.getName(),
                EParameterName.ON_METERCATCHER_FLAG.getDisplayName(), titlePart);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 4;
        titlePart.setLayoutData(gridData);

        GridLayout gridLayout = (GridLayout) titlePart.getLayout();
        gridLayout.numColumns = 12;
        titlePart.setLayout(gridLayout);

        onFilesField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_FILES_FLAG.getName(),
                EParameterName.ON_FILES_FLAG.getDisplayName(), parent);
        filePathField = new DirectoryFieldEditor(languagePrefix + EParameterName.FILE_PATH.getName(), EParameterName.FILE_PATH
                .getDisplayName(), parent);

        statsFileNameField = new StringFieldEditor(languagePrefix + EParameterName.FILENAME_STATS.getName(),
                EParameterName.FILENAME_STATS.getDisplayName(), parent);

        statsFileNameField.getTextControl(parent).setText(
                TalendTextUtils.addQuotes(PreferenceInitializer.DEFAULT_STATS_FILE_NAME));

        logsFileNameField = new StringFieldEditor(languagePrefix + EParameterName.FILENAME_LOGS.getName(),
                EParameterName.FILENAME_LOGS.getDisplayName(), parent);

        logsFileNameField.getTextControl(parent).setText(TalendTextUtils.addQuotes(PreferenceInitializer.DEFAULT_LOGS_FILE_NAME));

        metterFileNameField = new StringFieldEditor(languagePrefix + EParameterName.FILENAME_METTER.getName(),
                EParameterName.FILENAME_METTER.getDisplayName(), parent);

        metterFileNameField.getTextControl(parent).setText(
                TalendTextUtils.addQuotes(PreferenceInitializer.DEFAULT_METER_FILE_NAME));

        onDatabaseField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_DATABASE_FLAG.getName(),
                EParameterName.ON_DATABASE_FLAG.getDisplayName(), parent);

        Composite comboTypePanel = new Composite(parent, SWT.NONE);
        GridData layout = new GridData(GridData.FILL_HORIZONTAL);
        layout.horizontalSpan = 3;

        comboTypePanel.setLayoutData(layout);
        comboTypePanel.setLayout(new RowLayout());
        Label labelRepositoryType = new Label(comboTypePanel, SWT.NONE);
        labelRepositoryType.setText("Repository Type ");

        comboRepositoryType = new Combo(comboTypePanel, SWT.READ_ONLY);

        String[] repositoryTypes = new String[] { BUILD_IN, REPOSITORY };
        comboRepositoryType.setItems(repositoryTypes);

        String currentType = getPreferenceStore().getString(languagePrefix + EParameterName.PROPERTY_TYPE.getName());
        currentType = currentType.equals(EmfComponent.REPOSITORY) ? REPOSITORY : BUILD_IN;
        int currentTypeIndex = 0;
        for (; currentTypeIndex < repositoryTypes.length; currentTypeIndex++) {
            if (repositoryTypes[currentTypeIndex].equals(currentType)) {
                break;
            }

        }
        comboRepositoryType.select(currentTypeIndex);

        String dbTypeLabel = getPreferenceStore().getString(
                languagePrefix + EParameterName.REPOSITORY_PROPERTY_TYPE.getName()
                        + StatsAndLogsPreferencePage.CONNECTION_ITEM_LABEL);

        RepositoryContentProvider contentProvider = (RepositoryContentProvider) RepositoryView.show().getViewer()
                .getContentProvider();
        RepositoryNode repositoryNode = contentProvider.getMetadataConNode();
        connectionItem = StatsAndLogsViewHelper.findConnectionItem(contentProvider, repositoryNode, dbTypeLabel);

        textRepositoryType = new Text(comboTypePanel, SWT.SINGLE | SWT.BORDER);
        textRepositoryType.setVisible(currentType.equals(REPOSITORY));
        textRepositoryType.setEditable(false);
        textRepositoryType.setText("                                         ");
        textRepositoryType.setText(formRepositoryTypeText(connectionItem));

        buttonShowRepository = new Button(comboTypePanel, SWT.NONE);
        buttonShowRepository.setText("...");
        buttonShowRepository.setVisible(currentType.equals(REPOSITORY));

        buttonShowRepository.addSelectionListener(new SelectionListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetDefaultSelected(SelectionEvent e) {

            }

            public void widgetSelected(SelectionEvent e) {
                RepositoryReviewDialog dialog = new RepositoryReviewDialog(Display.getCurrent().getActiveShell(),
                        ERepositoryObjectType.METADATA, "DATABASE");

                if (dialog.open() == RepositoryReviewDialog.OK) {
                    setDbId(dialog.getResult().getObject().getId());
                    connectionItem = (ConnectionItem) dialog.getResult().getObject().getProperty().getItem();
                    String repositoryType = formRepositoryTypeText(connectionItem);
                    textRepositoryType.setText(repositoryType);
                    updateFileContent(connectionItem);
                }
            }

        });

        comboRepositoryType.addSelectionListener(new SelectionListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetDefaultSelected(SelectionEvent e) {

            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetSelected(SelectionEvent e) {
                if (comboRepositoryType.getText().equals(BUILD_IN)) {
                    textRepositoryType.setVisible(false);
                    buttonShowRepository.setVisible(false);
                    dbTypeField.getControl().setEnabled(true);
                    updateEnableStateFromDisplay();
                } else {
                    textRepositoryType.setVisible(true);
                    buttonShowRepository.setVisible(true);
                    dbTypeField.setEnabled(false, dbTypeComposite);
                    dbTypeField.getComboBoxControl(dbTypeComposite).setEnabled(false);
                }
            }
        });

        dbTypeComposite = new Composite(parent, SWT.NONE);

        String[] strDisplay, strValue;
        strDisplay = StatsAndLogsConstants.DISPLAY_DBNAMES[languageType];
        strValue = StatsAndLogsConstants.DB_COMPONENTS[languageType];
        String[][] strsForDBType = new String[strDisplay.length][2];

        for (int i = 0; i < strDisplay.length; i++) {
            strsForDBType[i][0] = strDisplay[i];
            strsForDBType[i][1] = strValue[i];
        }

        dbTypeField = new ComboFieldEditor(languagePrefix + EParameterName.DB_TYPE.getName(), EParameterName.DB_TYPE
                .getDisplayName(), strsForDBType, dbTypeComposite);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 4;
        dbTypeComposite.setLayoutData(gridData);

        gridLayout = (GridLayout) dbTypeComposite.getLayout();
        gridLayout.numColumns = 6;
        dbTypeComposite.setLayout(gridLayout);

        hostField = new StringFieldEditor(languagePrefix + EParameterName.HOST.getName(), EParameterName.HOST.getDisplayName(),
                parent);

        portField = new StringFieldEditor(languagePrefix + EParameterName.PORT.getName(), EParameterName.PORT.getDisplayName(),
                parent);

        dbNameField = new StringFieldEditor(languagePrefix + EParameterName.DBNAME.getName(), EParameterName.DBNAME
                .getDisplayName(), parent);
        if (language == ECodeLanguage.JAVA) {
            additionParamField = new StringFieldEditor(languagePrefix + EParameterName.PROPERTIES.getName(),
                    EParameterName.PROPERTIES.getDisplayName(), parent);
        }
        schemaField = new StringFieldEditor(languagePrefix + EParameterName.SCHEMA_DB.getName(), EParameterName.SCHEMA_DB
                .getDisplayName(), parent);

        userField = new StringFieldEditor(languagePrefix + EParameterName.USER.getName(), EParameterName.USER.getDisplayName(),
                parent);

        passwordField = new StringFieldEditor(languagePrefix + EParameterName.PASS.getName(), EParameterName.PASS
                .getDisplayName(), parent);

        dabasePathField = new FileFieldEditor(languagePrefix + EParameterName.DBFILE.getName(), EParameterName.DBFILE
                .getDisplayName(), parent);

        statsTableField = new StringFieldEditor(languagePrefix + EParameterName.TABLE_STATS.getName(), EParameterName.TABLE_STATS
                .getDisplayName(), parent);

        logsTableField = new StringFieldEditor(languagePrefix + EParameterName.TABLE_LOGS.getName(), EParameterName.TABLE_LOGS
                .getDisplayName(), parent);

        metterTableField = new StringFieldEditor(languagePrefix + EParameterName.TABLE_METER.getName(),
                EParameterName.TABLE_METER.getDisplayName(), parent);

        finalPart = new Composite(parent, SWT.None);
        finalPart.setLayout(new GridLayout());

        catchRuntimeErrorsField = new CheckBoxFieldEditor(languagePrefix + EParameterName.CATCH_RUNTIME_ERRORS.getName(),
                EParameterName.CATCH_RUNTIME_ERRORS.getDisplayName(), finalPart);
        catchUserErrorsField = new CheckBoxFieldEditor(languagePrefix + EParameterName.CATCH_USER_ERRORS.getName(),
                EParameterName.CATCH_USER_ERRORS.getDisplayName(), finalPart);
        catchUserWarningField = new CheckBoxFieldEditor(languagePrefix + EParameterName.CATCH_USER_WARNING.getName(),
                EParameterName.CATCH_USER_WARNING.getDisplayName(), finalPart);
        catchRealtimeStatsField = new CheckBoxFieldEditor(languagePrefix + EParameterName.CATCH_REALTIME_STATS.getName(),
                EParameterName.CATCH_REALTIME_STATS.getDisplayName(), finalPart);

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 4;
        finalPart.setLayoutData(gridData);

        gridLayout = (GridLayout) finalPart.getLayout();
        gridLayout.numColumns = 12;
        finalPart.setLayout(gridLayout);

        addField(onStatCatcherField);
        addField(onLogCatcherField);
        addField(onMetterCatcherField);

        addField(onFilesField);
        addField(filePathField);
        addField(statsFileNameField);
        addField(logsFileNameField);
        addField(metterFileNameField);
        addField(onDatabaseField);

        addField(dbTypeField);
        addField(hostField);

        addField(portField);
        addField(dbNameField);
        if (language == ECodeLanguage.JAVA) {
            addField(additionParamField);
        }

        addField(schemaField);
        addField(userField);
        addField(passwordField);
        addField(dabasePathField);
        addField(statsTableField);
        addField(logsTableField);
        addField(metterTableField);

        addField(catchRuntimeErrorsField);
        addField(catchUserErrorsField);
        addField(catchUserWarningField);
        addField(catchRealtimeStatsField);
    }

    private void updateEnableStateFromPreferences() {
        IPreferenceStore preferenceStore = getPreferenceStore();

        boolean onStatCatcher = preferenceStore.getBoolean(languagePrefix + EParameterName.ON_STATCATCHER_FLAG.getName());
        boolean onLogCatcher = preferenceStore.getBoolean(languagePrefix + EParameterName.ON_LOGCATCHER_FLAG.getName());
        boolean onMetterCatcher = preferenceStore.getBoolean(languagePrefix + EParameterName.ON_METERCATCHER_FLAG.getName());
        boolean onFiles = preferenceStore.getBoolean(languagePrefix + EParameterName.ON_FILES_FLAG.getName());
        boolean onDatabase = preferenceStore.getBoolean(languagePrefix + EParameterName.ON_DATABASE_FLAG.getName());
        String dbValue = preferenceStore.getString(languagePrefix + EParameterName.DB_TYPE.getName());
        boolean isBuildin = comboRepositoryType.getText().equals(BUILD_IN);
        updateEnableState(onStatCatcher, onLogCatcher, onMetterCatcher, onFiles, onDatabase, dbValue, isBuildin);
    }

    private void updateEnableStateFromDisplay() {
        boolean onStatCatcher = onStatCatcherField.getBooleanValue();
        boolean onLogCatcher = onLogCatcherField.getBooleanValue();
        boolean onMetterCatcher = onMetterCatcherField.getBooleanValue();
        boolean onFiles = onFilesField.getBooleanValue();
        boolean onDatabase = onDatabaseField.getBooleanValue();
        String dbValue = dbTypeField.getFieldValue();
        boolean isBuildin = comboRepositoryType.getText().equals(BUILD_IN);
        updateEnableState(onStatCatcher, onLogCatcher, onMetterCatcher, onFiles, onDatabase, dbValue, isBuildin);
    }

    private void updateEnableState(boolean onStatCatcher, boolean onLogCatcher, boolean onMetterCatcher, boolean onFiles,
            boolean onDatabase, String dbValue, boolean isBuildin) {
        onFilesField.setEnabled(onStatCatcher || onLogCatcher || onMetterCatcher, parent);
        filePathField.setEnabled(onFiles && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        statsFileNameField.setEnabled(onFiles && onStatCatcher, parent);
        logsFileNameField.setEnabled(onFiles && onLogCatcher, parent);
        metterFileNameField.setEnabled(onFiles && onMetterCatcher, parent);

        comboRepositoryType.setEnabled(onLogCatcher && onDatabase);
        textRepositoryType.setEnabled(onLogCatcher && onDatabase);
        buttonShowRepository.setEnabled(onLogCatcher && onDatabase);

        onDatabaseField.setEnabled(onStatCatcher || onLogCatcher || onMetterCatcher, parent);
        dbTypeField.getComboBoxControl(dbTypeComposite).setEnabled(
                isBuildin && onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher));
        dbTypeField.setEnabled(isBuildin && onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), dbTypeComposite);
        hostField.setEnabled((!dbValue.equals("tAccessOutput") && !dbValue.equals("tSQLiteOutput")) && onDatabase
                && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        portField.setEnabled((!dbValue.equals("tAccessOutput") && !dbValue.equals("tSQLiteOutput") && !dbValue
                .equals("tFirebirdOutput"))
                && onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);

        dbNameField.setEnabled((!dbValue.equals("tAccessOutput") && !dbValue.equals("tSQLiteOutput") && !dbValue
                .equals("tFirebirdOutput"))
                && onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        if (language == ECodeLanguage.JAVA) {
            additionParamField.setEnabled((dbValue.equals("tMSSqlOutput") || dbValue.equals("tInformixOutput") || dbValue
                    .equals("tMysqlOutput"))
                    && onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        }
        schemaField.setEnabled((dbValue.equals("tOracleOutput") || dbValue.equals("tPostgresqlOutput")) && onDatabase
                && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        userField.setEnabled((!dbValue.equals("tSQLiteOutput")) && onDatabase
                && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        passwordField.setEnabled((!dbValue.equals("tSQLiteOutput")) && onDatabase
                && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        dabasePathField.setEnabled((dbValue.equals("tAccessOutput") || dbValue.equals("tSQLiteOutput") || dbValue
                .equals("tFirebirdOutput"))
                && onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        statsTableField.setEnabled(onDatabase && onStatCatcher, parent);
        logsTableField.setEnabled(onDatabase && onLogCatcher, parent);
        metterTableField.setEnabled(onDatabase && onMetterCatcher, parent);

        catchRuntimeErrorsField.setEnabled(onLogCatcher, finalPart);
        catchUserErrorsField.setEnabled(onLogCatcher, finalPart);
        catchUserWarningField.setEnabled(onLogCatcher, finalPart);
        catchRealtimeStatsField.setEnabled(onStatCatcher, finalPart);
    }

    private void addListeners() {
        SelectionListener listener = new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                updateEnableStateFromDisplay();
            }
        };
        onStatCatcherField.getCheckbox().addSelectionListener(listener);
        onLogCatcherField.getCheckbox().addSelectionListener(listener);
        onMetterCatcherField.getCheckbox().addSelectionListener(listener);
        onFilesField.getCheckbox().addSelectionListener(listener);
        onDatabaseField.getCheckbox().addSelectionListener(listener);
        dbTypeField.getComboBoxControl(dbTypeComposite).addSelectionListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        super.performDefaults();
        updateEnableStateFromDisplay();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {

    }

    private String id;

    private Text textRepositoryType;

    private Button buttonShowRepository;

    private void setDbId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        boolean isRepositoryType = comboRepositoryType.getText().equals(REPOSITORY);

        String key = languagePrefix + EParameterName.PROPERTY_TYPE.getName();
        if (isRepositoryType) {
            getPreferenceStore().setValue(key, EmfComponent.REPOSITORY);
        } else {
            getPreferenceStore().setValue(key, EmfComponent.BUILTIN);
        }

        getPreferenceStore().setValue(languagePrefix + EParameterName.REPOSITORY_PROPERTY_TYPE.getName(),
                this.id == null ? "" : this.id);
        String itemLabel = "";
        if (connectionItem != null) {
            itemLabel = connectionItem.getProperty().getLabel();
        }
        getPreferenceStore().setValue(languagePrefix + EParameterName.REPOSITORY_PROPERTY_TYPE.getName() + CONNECTION_ITEM_LABEL,
                itemLabel);
        return super.performOk();
    }
}
