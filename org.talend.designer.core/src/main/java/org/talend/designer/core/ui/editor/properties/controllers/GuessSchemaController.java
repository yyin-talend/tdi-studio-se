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
package org.talend.designer.core.ui.editor.properties.controllers;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWithDetailAreaAndContinueButton;
import org.talend.commons.utils.data.list.UniqueStringGenerator;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ConnectionStatus;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.types.JavaDataTypeHelper;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.PerlDataTypeHelper;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.properties.tab.IDynamicProperty;
import org.talend.core.ui.metadata.dialog.MetadataDialog;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.ChangeMetadataCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.ConfigureConnParamDialog;
import org.talend.repository.ui.utils.ColumnNameValidator;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class GuessSchemaController extends AbstractElementPropertySectionController {

    private static final String GUESS_SCHEMA_NAME = "Guess schema";

    private static final String SCHEMA = "SCHEMA";

    private static Logger log = Logger.getLogger(GuessSchemaController.class);

    private Map<IElementParameter, Button> queryButton = new HashMap<IElementParameter, Button>();

    /**
     * DOC zqin GuessSchemaController constructor comment.
     * 
     * @param dp
     */
    public GuessSchemaController(IDynamicProperty dp) {
        super(dp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#createControl(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter, int, int, int, org.eclipse.swt.widgets.Control)
     */
    @Override
    public Control createControl(Composite subComposite, IElementParameter param, int numInRow, int nbInRow, int top,
            Control lastControl) {
        this.curParameter = param;
        this.paramFieldType = param.getField();
        FormData data;

        final Button btnCmd = new Button(subComposite, SWT.NONE);
        btnCmd.setText(GUESS_SCHEMA_NAME);

        data = new FormData();

        GC gc = new GC(btnCmd);
        Point labelSize = gc.stringExtent(GUESS_SCHEMA_NAME);
        gc.dispose();
        int currentLabelWidth = STANDARD_BUTTON_WIDTH;
        if ((labelSize.x + ITabbedPropertyConstants.HSPACE * 2) > STANDARD_BUTTON_WIDTH) {
            currentLabelWidth = labelSize.x + ITabbedPropertyConstants.HSPACE * 2;
        }

        data.left = new FormAttachment(lastControl, 0);
        data.top = new FormAttachment(0, top);
        data.height = STANDARD_HEIGHT + 2;
        btnCmd.setLayoutData(data);
        btnCmd.setData(PARAMETER_NAME, param.getName());
        btnCmd.setData(NAME, SCHEMA);
        btnCmd.setData(SCHEMA, checkQuotes((String) param.getValue()));
        btnCmd.setEnabled(!param.isReadOnly());
        btnCmd.addSelectionListener(listenerSelection);

        return btnCmd;
    }

    private String checkQuotes(final String str) {
        if (str == null || "".equals(str)) { //$NON-NLS-1$
            return TalendTextUtils.addQuotes(str);
        }

        return str;
    }

    SelectionListener listenerSelection = new SelectionAdapter() {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
         */
        @Override
        public void widgetSelected(SelectionEvent e) {

            Command cmd = null;

            if (part == null) {
                cmd = createButtonCommand((Button) e.getSource(), new EmptyContextManager());
            } else {
                cmd = createButtonCommand((Button) e.getSource(), part.getTalendEditor().getProcess().getContextManager());
            }

            executeCommand(cmd);
        }

    };

    /**
     * This method is used for "Guess Query" button.
     * 
     * @return
     */
    private Command createButtonCommand(Button btn, IContextManager manager) {

        ArrayList<String> metaParameter = null;
        IElementParameter elementParameterFromField = elem.getElementParameterFromField(EParameterFieldType.MEMO_SQL);
        IElementParameter switchParam = elem.getElementParameter(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName());
        memoSQL = (String) elementParameterFromField.getValue();

        memoSQL = memoSQL.substring(1, memoSQL.length() - 1).trim();

        initConnectionParameters();
        if (this.connParameters != null && memoSQL != null) {
            initConnectionParametersWithContext(elem, manager.getDefaultContext());
            runShadowProcess();
        }

        if (columns != null && columns.size() > 0) {
            Node node = (Node) elem;
            IMetadataTable tempMetatable = new MetadataTable();
            IMetadataTable inputMetaCopy, inputMetadata, outputMetaCopy, originaleOutputTable;

            String propertyName = (String) btn.getData(PARAMETER_NAME);
            IElementParameter param = node.getElementParameter(propertyName);
            for (IElementParameter eParam : elem.getElementParameters()) {
                if (eParam.getContext() != null) {
                    param = eParam;
                }
            }
            originaleOutputTable = node.getMetadataFromConnector(param.getContext());
            if (originaleOutputTable != null) {
                outputMetaCopy = originaleOutputTable.clone();
            }

            tempMetatable.setListColumns(columns);

            MetadataDialog metaDialog = new MetadataDialog(composite.getShell(), tempMetatable, node, getCommandStack());
            if (metaDialog != null) {
                metaDialog.setText(Messages.getString("SchemaController.schemaOf") + node.getLabel());

                if (metaDialog.open() == MetadataDialog.OK) {
                    outputMetaCopy = metaDialog.getOutputMetaData();
                    boolean modified = false;
                    if (!outputMetaCopy.sameMetadataAs(originaleOutputTable, IMetadataColumn.OPTIONS_NONE)) {
                        modified = true;
                    }

                    if (modified) {
                        if (switchParam != null) {
                            switchParam.setValue(Boolean.FALSE);
                        }

                        ChangeMetadataCommand changeMetadataCommand = new ChangeMetadataCommand(node, param,
                                originaleOutputTable, outputMetaCopy);

                        return changeMetadataCommand;

                    }
                }
            }
        }

        return null;
    }

    private String memoSQL = null;

    private List<IMetadataColumn> columns = new ArrayList<IMetadataColumn>();

    private void runShadowProcess() {

        final ProgressMonitorDialog pmd = new ProgressMonitorDialog(this.composite.getShell());

        try {
            pmd.run(true, true, new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                    if (columns != null) {
                        columns.clear();
                    }
                    monitor.beginTask("Waiting for opening Database ...", IProgressMonitor.UNKNOWN);
                    SQLBuilderRepositoryNodeManager manager = new SQLBuilderRepositoryNodeManager();
                    if (connParameters == null) {
                        initConnectionParameters();
                    }

                    DatabaseConnection connt = manager.createConnection(connParameters);
                    IMetadataConnection iMetadataConnection = null;
                    boolean isStatus = false;
                    if (connt != null) {
                        iMetadataConnection = ConvertionHelper.convert(connt);
                        isStatus = checkConnection(iMetadataConnection);
                    }
                    if (!monitor.isCanceled()) {
                        try {
                            if (isStatus) {
                                ExtractMetaDataUtils.getConnection(iMetadataConnection.getDbType(), iMetadataConnection.getUrl(),
                                        iMetadataConnection.getUsername(), iMetadataConnection.getPassword(), iMetadataConnection
                                                .getDatabase(), iMetadataConnection.getSchema(), iMetadataConnection
                                                .getDriverClass(), iMetadataConnection.getDriverJarPath());
                                if (ExtractMetaDataUtils.conn != null) {
                                    Statement smst = ExtractMetaDataUtils.conn.createStatement();

                                    ResultSet rs = smst.executeQuery(memoSQL);
                                    ResultSetMetaData rsmd = rs.getMetaData();
                                    int numbOfColumn = rsmd.getColumnCount();

                                    int count = 0;
                                    List<String[]> cvsArrays = new ArrayList<String[]>();
                                    while (rs.next() && count < 50) {

                                        String[] dataOneRow = new String[numbOfColumn];
                                        for (int i = 1; i <= numbOfColumn; i++) {
                                            String tempStr = rs.getString(i);
                                            dataOneRow[i - 1] = tempStr;
                                        }

                                        cvsArrays.add(dataOneRow);
                                        count++;
                                    }

                                    refreshMetaDataTable(rsmd, cvsArrays);

                                    ExtractMetaDataUtils.closeConnection();
                                }
                            } else {
                                Display.getDefault().asyncExec(new Runnable() {

                                    public void run() {
                                        String pid = SqlBuilderPlugin.PLUGIN_ID;
                                        String mainMsg = "Database connection is failed. "; //$NON-NLS-1$
                                        ErrorDialogWithDetailAreaAndContinueButton dialog = new ErrorDialogWithDetailAreaAndContinueButton(
                                                composite.getShell(), pid, mainMsg, connParameters.getConnectionComment());
                                        if (dialog.getCodeOfButton() == Window.OK) {
                                            openParamemerDialog(composite.getShell(), part.getTalendEditor().getProcess()
                                                    .getContextManager());
                                        }
                                    }
                                });
                            }
                        } catch (Exception e) {
                            ExtractMetaDataUtils.closeConnection();

                            final String strExcepton = "Connect to DB error ,or some errors in SQL query string, or 'Guess Schema' not compatible with current SQL query string."
                                    + System.getProperty("line.separator");
                            Display.getDefault().asyncExec(new Runnable() {

                                public void run() {
                                    MessageDialog.openWarning(composite.getShell(), "Connection error", strExcepton);
                                }
                            });
                        }
                    }

                }

            });
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * DOC ocarbone Comment method "refreshMetaData".
     * 
     * @param csvArray
     */
    public void refreshMetaDataTable(ResultSetMetaData rsmd, final List<String[]> csvRows) throws SQLException {

        if (csvRows == null) {
            return;
        } else {

            List<String> allNames = new ArrayList<String>();

            if (csvRows.isEmpty()) {
                int numbOfColumn = rsmd.getColumnCount();
                for (int i = 1; i <= numbOfColumn; i++) {
                    IMetadataColumn oneColum = new MetadataColumn();
                    String labelName = rsmd.getColumnLabel(i);
                    labelName = ColumnNameValidator.validateColumnNameFormat(labelName, i);
                    oneColum.setLabel(getNextGeneratedColumnName(labelName, allNames));
                    oneColum.setOriginalDbColumnName(rsmd.getColumnName(i));
                    oneColum.setNullable(rsmd.isNullable(i) == 0 ? false : true);
                    oneColum.setTalendType(JavaTypesManager.STRING.getId());

                    columns.add(oneColum);
                }
                return;
            }
            String[] fields = csvRows.get(0);

            Integer numberOfCol = getRightFirstRow(csvRows);

            // define the label to the metadata width the content of the first row
            int firstRowToExtractMetadata = 0;

            for (int i = 0; i < numberOfCol.intValue(); i++) {
                // define the first currentType and assimile it to globalType
                String globalType = null;
                int lengthValue = 0;
                int precisionValue = 0;

                int current = firstRowToExtractMetadata;
                while (globalType == null) {
                    if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                        if (i >= csvRows.get(current).length) {
                            globalType = "id_String"; //$NON-NLS-1$
                        } else {
                            globalType = JavaDataTypeHelper.getTalendTypeOfValue(csvRows.get(current)[i]);
                            current++;

                        }
                    } else {
                        if (i >= csvRows.get(current).length) {
                            globalType = "String"; //$NON-NLS-1$
                        } else {
                            globalType = PerlDataTypeHelper.getTalendTypeOfValue(csvRows.get(current)[i]);
                            current++;

                        }
                    }
                }

                // for another lines
                for (int f = firstRowToExtractMetadata; f < csvRows.size(); f++) {
                    fields = csvRows.get(f);
                    if (fields.length > i) {
                        String value = fields[i];
                        if (!value.equals("")) { //$NON-NLS-1$
                            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                                if (!JavaDataTypeHelper.getTalendTypeOfValue(value).equals(globalType)) {
                                    globalType = JavaDataTypeHelper.getCommonType(globalType, JavaDataTypeHelper
                                            .getTalendTypeOfValue(value));
                                }
                            } else {
                                if (!PerlDataTypeHelper.getTalendTypeOfValue(value).equals(globalType)) {
                                    globalType = PerlDataTypeHelper.getCommonType(globalType, PerlDataTypeHelper
                                            .getTalendTypeOfValue(value));
                                }
                            }
                            if (lengthValue < value.length()) {
                                lengthValue = value.length();
                            }
                            int positionDecimal = 0;
                            if (value.indexOf(',') > -1) {
                                positionDecimal = value.lastIndexOf(',');
                                precisionValue = lengthValue - positionDecimal;
                            } else if (value.indexOf('.') > -1) {
                                positionDecimal = value.lastIndexOf('.');
                                precisionValue = lengthValue - positionDecimal;
                            }
                        }
                    }
                }

                IMetadataColumn oneColum = new MetadataColumn();
                // Convert javaType to TalendType
                String talendType = null;
                if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                    talendType = globalType;
                    if (globalType.equals(JavaTypesManager.FLOAT.getId()) || globalType.equals(JavaTypesManager.DOUBLE.getId())) {
                        oneColum.setPrecision(precisionValue);
                    } else {
                        oneColum.setPrecision(0);
                    }
                } else {
                    talendType = PerlTypesManager.getNewTypeName(MetadataTalendType.loadTalendType(globalType,
                            "TALENDDEFAULT", false)); //$NON-NLS-1$
                    if (globalType.equals("FLOAT") || globalType.equals("DOUBLE")) { //$NON-NLS-1$ //$NON-NLS-2$
                        oneColum.setPrecision(precisionValue);
                    } else {
                        oneColum.setPrecision(0);
                    }
                }

                String labelName = rsmd.getColumnLabel(i + 1);
                labelName = ColumnNameValidator.validateColumnNameFormat(labelName, i);

                oneColum.setTalendType(talendType);
                oneColum.setLength(lengthValue);

                oneColum.setLabel(getNextGeneratedColumnName(labelName, allNames));
                oneColum.setOriginalDbColumnName(rsmd.getColumnName(i + 1));
                oneColum.setNullable(rsmd.isNullable(i + 1) == 0 ? false : true);

                columns.add(oneColum);

            }
        }
    }

    private String getNextGeneratedColumnName(String oldName, List<String> allNames) {

        String uniqueString;

        UniqueStringGenerator<String> uniqueStringGenerator = new UniqueStringGenerator<String>(oldName, allNames) {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.commons.utils.data.list.UniqueStringGenerator#getBeanString(java.lang.Object)
             */
            @Override
            protected String getBeanString(String bean) {
                return bean;
            }

        };

        uniqueString = uniqueStringGenerator.getUniqueString();

        allNames.add(oldName);

        return uniqueString;
    }

    // CALCULATE THE NULBER OF COLUMNS IN THE PREVIEW
    private Integer getRightFirstRow(List<String[]> csvRows) {

        Integer numbersOfColumns = null;
        int parserLine = csvRows.size();
        if (parserLine > 50) {
            parserLine = 50;
        }
        for (int i = 0; i < parserLine; i++) {
            if (csvRows.get(i) != null) {
                String[] nbRow = csvRows.get(i);
                // List<XmlField> nbRowFields = nbRow.getFields();
                if (numbersOfColumns == null || nbRow.length >= numbersOfColumns) {
                    numbersOfColumns = nbRow.length;
                }
            }
        }
        return numbersOfColumns;
    }

    private void openParamemerDialog(Shell shell, IContextManager manager) {
        initConnectionParameters();
        if (connParameters != null) {
            ConfigureConnParamDialog paramDialog = new ConfigureConnParamDialog(shell, connParameters, manager, elem);
            if (paramDialog.open() == Window.OK) {
                runShadowProcess();
            }
        } else {
            MessageDialog.openWarning(shell, "Connection error", "Please set connection parameters");
        }

    }

    /**
     * qzhang Comment method "checkConnection".
     * 
     * @param metadataConnection
     */
    protected boolean checkConnection(IMetadataConnection metadataConnection) {
        try {
            ConnectionStatus testConnection = ExtractMetaDataFromDataBase.testConnection(metadataConnection.getDbType(),
                    metadataConnection.getUrl(), metadataConnection.getUsername(), metadataConnection.getPassword(),
                    metadataConnection.getSchema(), metadataConnection.getDriverClass(), metadataConnection.getDriverJarPath());
            connParameters.setConnectionComment(testConnection.getMessageException());
            return testConnection.getResult();
        } catch (Exception e) {
            log.error(Messages.getString("CommonWizard.exception") + "\n" + e.toString());
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#estimateRowSize(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter)
     */
    @Override
    public int estimateRowSize(Composite subComposite, IElementParameter param) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#refresh(org.talend.core.model.process.IElementParameter,
     * boolean)
     */
    @Override
    public void refresh(IElementParameter param, boolean check) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub

    }

}
