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
package org.talend.designer.core.ui.editor.properties.controllers;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IControlCreator;
import org.eclipse.jface.fieldassist.TextControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.database.ConnectionStatus;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.ui.metadata.dialog.DbTableSelectorDialog;
import org.talend.core.ui.metadata.dialog.DbTableSelectorObject;
import org.talend.core.ui.metadata.dialog.DbTableSelectorObject.ObjectType;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;
import org.talend.sqlbuilder.util.ConnectionParameters;
import org.talend.sqlbuilder.util.EConnectionParameterName;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: TextController.java 1 2006-12-12 下午01:53:53 +0000 (下午01:53:53) yzhang $
 * 
 */
public class DbTableController extends AbstractElementPropertySectionController {

    private Button openListTable;

    private Button openSQLEditorButton;

    private static Logger log = Logger.getLogger(DbTableController.class);

    /**
     * DOC yzhang TextController constructor comment.
     * 
     * @param dtp
     */
    public DbTableController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    SelectionListener openTablesListener = new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent e) {

        }

        public void widgetSelected(SelectionEvent e) {
            createListTablesCommand();
        }
    };

    SelectionListener openSQLListener = new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent e) {

        }

        public void widgetSelected(SelectionEvent e) {
            createOpenSQLCommand();
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createControl()
     */
    @Override
    public Control createControl(final Composite subComposite, final IElementParameter param, final int numInRow,
            final int nbInRow, final int top, final Control lastControl) {
        FormData data;

        Control buttonControl = addListTablesButton(subComposite, param, top, numInRow, nbInRow);

        Control openSqlBuilder = null;
        if (!isContainSqlMemo()) {
            openSqlBuilder = addOpenSqlBulderButton(subComposite, param, top, numInRow, nbInRow);
            FormData data1 = new FormData();
            data1.right = new FormAttachment(100, -ITabbedPropertyConstants.HSPACE);
            data1.left = new FormAttachment(100, -(ITabbedPropertyConstants.HSPACE + STANDARD_BUTTON_WIDTH));
            data1.top = new FormAttachment(0, top);
            openSqlBuilder.setLayoutData(data1);
        }
        data = new FormData();
        if (openSqlBuilder != null) {
            data.right = new FormAttachment(openSqlBuilder, -5, SWT.LEFT);
            data.left = new FormAttachment(openSqlBuilder, -(15 + STANDARD_BUTTON_WIDTH), SWT.LEFT);
        } else {
            data.right = new FormAttachment(((numInRow * MAX_PERCENT) / nbInRow), 0);
            data.left = new FormAttachment(((numInRow * MAX_PERCENT) / nbInRow), -STANDARD_BUTTON_WIDTH);
        }
        data.top = new FormAttachment(0, top);
        data.height = STANDARD_HEIGHT - 2;
        openListTable.setLayoutData(data);
        openListTable.setData(PARAMETER_NAME, param.getName());
        openListTable.setEnabled(!param.isReadOnly());
        openListTable.addSelectionListener(openTablesListener);
        Text labelText;

        final DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, new TextControlCreator());
        if (param.isRequired()) {
            FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                    FieldDecorationRegistry.DEC_REQUIRED);
            dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.TOP, false);
        }
        if (param.isRepositoryValueUsed()) {
            FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                    FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
            decoration.setDescription(Messages.getString("TextController.decoration.description")); //$NON-NLS-1$
            dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.BOTTOM, false);
        }
        Control cLayout = dField.getLayoutControl();
        labelText = (Text) dField.getControl();

        labelText.setData(PARAMETER_NAME, param.getName());

        editionControlHelper.register(param.getName(), labelText, true);

        cLayout.setBackground(subComposite.getBackground());
        labelText.setEditable(!param.isReadOnly());
        if (elem instanceof Node) {
            labelText.setToolTipText(VARIABLE_TOOLTIP + param.getVariableName());
        }
        addDragAndDropTarget(labelText);

        CLabel labelLabel = getWidgetFactory().createCLabel(subComposite, param.getDisplayName());
        data = new FormData();
        if (lastControl != null) {
            data.left = new FormAttachment(lastControl, 0);
        } else {
            data.left = new FormAttachment((((numInRow - 1) * MAX_PERCENT) / nbInRow), 0);
        }
        data.top = new FormAttachment(0, top);
        labelLabel.setLayoutData(data);
        if (numInRow != 1) {
            labelLabel.setAlignment(SWT.RIGHT);
        }
        // *********************
        data = new FormData();
        int currentLabelWidth = STANDARD_LABEL_WIDTH;
        Point labelSize = new GC(labelLabel).stringExtent(param.getDisplayName());

        if ((labelSize.x + ITabbedPropertyConstants.HSPACE) > currentLabelWidth) {
            currentLabelWidth = labelSize.x + ITabbedPropertyConstants.HSPACE;
        }

        if (numInRow == 1) {
            if (lastControl != null) {
                data.left = new FormAttachment(lastControl, currentLabelWidth);
            } else {
                data.left = new FormAttachment(0, currentLabelWidth);
            }

        } else {
            data.left = new FormAttachment(labelLabel, 0, SWT.RIGHT);
        }
        data.right = new FormAttachment(buttonControl, -5, SWT.LEFT);
        // data.right = new FormAttachment((numInRow * MAX_PERCENT) / nbInRow, 0);
        data.top = new FormAttachment(0, top);
        cLayout.setLayoutData(data);
        // **********************

        hashCurControls.put(param.getName(), labelText);

        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        // curRowSize = initialSize.y + ITabbedPropertyConstants.VSPACE;
        dynamicTabbedPropertySection.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);
        return null;
    }

    /**
     * qzhang Comment method "createOpenSQLCommand".
     */
    protected void createOpenSQLCommand() {
        initConnectionParameters();
        openSqlBuilderBuildIn(connParameters, (String) openSQLEditorButton.getData(PARAMETER_NAME));
    }

    /**
     * qzhang Comment method "isContainSqlMemo".
     * 
     * @return
     */
    private boolean isContainSqlMemo() {
        IElementParameter elementParameterFromField = elem.getElementParameterFromField(EParameterFieldType.MEMO_SQL);
        return elementParameterFromField != null;
    }

    /**
     * qzhang Comment method "addOpenSqlBulderButton".
     * 
     * @param subComposite
     * @param param
     * @param top
     * @param numInRow
     * @param nbInRow
     * @return
     */
    private Control addOpenSqlBulderButton(Composite subComposite, IElementParameter param, int top, int numInRow, int nbInRow) {
        final DecoratedField dField1 = new DecoratedField(subComposite, SWT.PUSH, new IControlCreator() {

            public Control createControl(Composite parent, int style) {
                return new Button(parent, style);
            }
        });

        Control buttonControl = dField1.getLayoutControl();
        openSQLEditorButton = (Button) dField1.getControl();
        openSQLEditorButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        openSQLEditorButton.setImage(CorePlugin.getImageDescriptor(DOTS_BUTTON).createImage());
        buttonControl.setBackground(subComposite.getBackground());
        openSQLEditorButton.setEnabled(true);
        openSQLEditorButton.setData(NAME, SQLEDITOR);
        openSQLEditorButton.setData(PARAMETER_NAME, param.getName());

        openSQLEditorButton.addSelectionListener(openSQLListener);

        return buttonControl;
    }

    /**
     * qzhang Comment method "addListTablesButton".
     * 
     * @param subComposite
     * @param param
     * @param top
     * @return
     */
    private Control addListTablesButton(final Composite subComposite, final IElementParameter param, final int top, int numInRow,
            final int nbInRow) {

        openListTable = getWidgetFactory().createButton(subComposite, "", SWT.PUSH); //$NON-NLS-1$
        openListTable.setImage(CorePlugin.getImageDescriptor(DOTS_BUTTON).createImage());
        return openListTable;
    }

    private ConnectionParameters connParameters;

    /**
     * qzhang Comment method "createCommand".
     * 
     * @return
     */
    protected void createListTablesCommand() {
        initConnectionParameters();
        openDbTableSelectorJob();
    }

    /**
     * qzhang Comment method "initConnectionParameters".
     */
    private void initConnectionParameters() {
        connParameters = new ConnectionParameters();

        String type = getValueFromRepositoryName("TYPE"); //$NON-NLS-1$
        connParameters.setDbType(type);
        String schema = setConnectionParameter(connParameters, EConnectionParameterName.SCHEMA.getName());
        connParameters.setSchema(schema);
        String realTableName = null;
        if (elem.getPropertyValue(EParameterName.SCHEMA_TYPE.getName()).equals(EmfComponent.REPOSITORY)) {
            realTableName = dynamicTabbedPropertySection.getRepositoryTableMap().get(
                    elem.getPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName())).getTableName();
        }
        connParameters.setSchemaName(MetadataTool.getTableName(elem, connParameters.getMetadataTable(), schema, type,
                realTableName));

        String userName = setConnectionParameter(connParameters, EConnectionParameterName.USERNAME.getName());
        connParameters.setUserName(userName);

        String password = setConnectionParameter(connParameters, EConnectionParameterName.PASSWORD.getName());
        connParameters.setPassword(password);

        String host = setConnectionParameter(connParameters, EConnectionParameterName.SERVER_NAME.getName());
        connParameters.setHost(host);

        String port = setConnectionParameter(connParameters, EConnectionParameterName.PORT.getName());
        connParameters.setPort(port);

        String dbName = setConnectionParameter(connParameters, EConnectionParameterName.SID.getName());
        connParameters.setDbName(dbName);

    }

    private String setConnectionParameter(ConnectionParameters connParameters, String repositoryName) {
        String userName = getValueFromRepositoryName(repositoryName); //$NON-NLS-1$
        final String paraNameFromRepositoryName = getParaNameFromRepositoryName(repositoryName);
        if (paraNameFromRepositoryName != null) {
            connParameters.getRepositoryNameParaName().put(repositoryName, paraNameFromRepositoryName);
        }
        return userName;
    }

    /**
     * qzhang Comment method "openDbTableSelectorJob".
     */
    private void openDbTableSelectorJob() {
        Job job = new Job("Open Database Table Selector Dialog") {

            @Override
            protected IStatus run(final IProgressMonitor monitor) {
                monitor.beginTask("Waiting for opening Database Table Selector Dialog...", IProgressMonitor.UNKNOWN);
                SQLBuilderRepositoryNodeManager manager = new SQLBuilderRepositoryNodeManager();
                final IMetadataConnection iMetadataConnection = ConvertionHelper
                        .convert(manager.createConnection(connParameters));
                final List<String> returnTablesFormConnection = ExtractMetaDataFromDataBase
                        .returnTablesFormConnection(iMetadataConnection);
                if (connParameters == null) {
                    initConnectionParameters();
                }
                boolean isStatus = checkConnection(iMetadataConnection);
                if (!monitor.isCanceled()) {
                    try {
                        if (isStatus) {
                            Display.getDefault().asyncExec(new Runnable() {

                                public void run() {
                                    final DbTableSelectorObject object = new DbTableSelectorObject();
                                    DbTableSelectorObject connO = new DbTableSelectorObject();
                                    connO.setLabel(connParameters.getDbName());
                                    connO.setType(ObjectType.DB);

                                    if (monitor.isCanceled()) {
                                        monitor.done();
                                        return;
                                    }
                                    for (String string : returnTablesFormConnection) {

                                        DbTableSelectorObject tableO = new DbTableSelectorObject();
                                        tableO.setLabel(string);
                                        tableO.setType(ObjectType.TABLE);
                                        // List<MetadataColumn> cols =
                                        // ExtractMetaDataFromDataBase.returnMetadataColumnsFormTable(
                                        // iMetadataConnection, string);
                                        // if (monitor.isCanceled()) {
                                        // monitor.done();
                                        // return;
                                        // }
                                        // for (MetadataColumn column : cols) {
                                        // DbTableSelectorObject columnO = new DbTableSelectorObject();
                                        // columnO.setLabel(column.getLabel());
                                        // columnO.setType(ObjectType.COLUMN);
                                        // tableO.addChildren(columnO);
                                        // }
                                        connO.addChildren(tableO);
                                    }
                                    object.addChildren(connO);
                                    String propertyName = (String) openListTable.getData(PARAMETER_NAME);
                                    DbTableSelectorDialog selectorDialog = new DbTableSelectorDialog(composite.getShell(), object);
                                    if (selectorDialog.open() == DbTableSelectorDialog.OK) {
                                        String name = selectorDialog.getSelectName();
                                        if (name != null) {
                                            Command dbSelectorCommand = new PropertyChangeCommand(elem, propertyName,
                                                    TalendTextUtils.addQuotes(name));
                                            getCommandStack().execute(dbSelectorCommand);
                                        }
                                    }

                                }
                            });
                        } else {
                            Display.getDefault().asyncExec(new Runnable() {

                                public void run() {
                                    String pid = SqlBuilderPlugin.PLUGIN_ID;
                                    String mainMsg = "Database connection is failed."; //$NON-NLS-1$
                                    new ErrorDialogWidthDetailArea(composite.getShell(), pid, mainMsg, connParameters
                                            .getConnectionComment());

                                }
                            });
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
                monitor.done();
                return Status.OK_STATUS;
            }

        };
        IWorkbenchSiteProgressService siteps = (IWorkbenchSiteProgressService) part.getSite().getAdapter(
                IWorkbenchSiteProgressService.class);
        siteps.showInDialog(composite.getShell(), job);
        job.setUser(true);
        job.schedule();
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
                    metadataConnection.getSchema());
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
        DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, new TextControlCreator());
        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        dField.getLayoutControl().dispose();
        return initialSize.y + ITabbedPropertyConstants.VSPACE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh(IElementParameter param, boolean checkErrorsWhenViewRefreshed) {
        Text labelText = (Text) hashCurControls.get(param.getName());
        if (labelText == null) {
            return;
        }
        Object value = param.getValue();
        boolean valueChanged = false;
        if (value == null) {
            labelText.setText(""); //$NON-NLS-1$
        } else {
            if (!value.equals(labelText.getText())) {
                labelText.setText((String) value);
                valueChanged = true;
            }
        }
        if (checkErrorsWhenViewRefreshed || valueChanged) {
            checkErrorsForPropertiesOnly(labelText);
        }
        fixedCursorPosition(param, labelText, value, valueChanged);
    }

}
