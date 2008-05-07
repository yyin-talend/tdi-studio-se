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
package org.talend.repository.ui.swt.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceModuleParseAPI;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public abstract class AbstractSalesforceStepForm extends AbstractForm {

    protected int maximumRowsToPreview = CorePlugin.getDefault().getPreferenceStore().getInt(
            ITalendCorePrefConstants.PREVIEW_LIMIT);

    protected ConnectionItem connectionItem;

    protected SalesforceSchemaConnection connection;

    protected AbstractNode fakeSalesforceNode = null;

    private final String tSalesforceUniqueName = "tSalesforceInput";

    private SalesforceModuleParseAPI salesforceAPI = null;

    private IMetadataContextModeManager contextModeManager;

    public AbstractSalesforceStepForm(Composite parent, ConnectionItem connectionItem, String[] existingNames,
            SalesforceModuleParseAPI salesforceAPI) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
        this.salesforceAPI = salesforceAPI;
    }

    public AbstractSalesforceStepForm(Composite parent, ConnectionItem connectionItem, SalesforceModuleParseAPI salesforceAPI) {
        this(parent, connectionItem, null, salesforceAPI);
    }

    public AbstractSalesforceStepForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames, SalesforceModuleParseAPI salesforceAPI) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
        this.salesforceAPI = salesforceAPI;
    }

    protected SalesforceSchemaConnection getConnection() {
        return (SalesforceSchemaConnection) connectionItem.getConnection();
    }

    public boolean isPerlProject() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        return (codeLanguage == ECodeLanguage.PERL);
    }

    /**
     * 
     * DOC YeXiaowei Comment method "getSalesforceComponent".
     * 
     * @return Always not null
     */
    public INode getSalesforceNode() {
        return RepositoryPlugin.getDefault().getDesignerCoreService().getRefrenceNode(tSalesforceUniqueName);
    }

    public IMetadataTable getMetadatasForSalesforce(String endPoint, String user, String pass, String moduleName, boolean update) {
        IMetadataTable result = null;

        if (!moduleName.equals(salesforceAPI.getCurrentModuleName())) {
            result = getMetadataTableBySalesforceServerAPI(endPoint, user, pass, moduleName);
            if (result == null) {
                result = getMetadataTableFromConfigFile(moduleName);
            }
            return result;
        } else {
            if (update) {
                result = getMetadataTableBySalesforceServerAPI(endPoint, user, pass, moduleName);
                if (result == null) {
                    result = getMetadataTableFromConfigFile(moduleName);
                }
                return result;
            } else {
                IMetadataTable metadataTable = new org.talend.core.model.metadata.MetadataTable();
                metadataTable.setListColumns(salesforceAPI.getCurrentMetadataColumns());
                return metadataTable;
            }
        }

    }

    private IMetadataTable getMetadataTableBySalesforceServerAPI(final String endPoint, final String user, final String pass,
            final String moduleName) {
        IMetadataTable metadataTable = new org.talend.core.model.metadata.MetadataTable();

        if (user == null || pass == null || user.equals("") || pass.equals("") || moduleName == null || moduleName.equals("")) {
            return null;
        }

        ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
        try {
            dialog.run(true, false, new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                    monitor.beginTask("Connection to Salesforce service to fetch module '" + moduleName + "'data column",
                            IProgressMonitor.UNKNOWN);

                    try {
                        salesforceAPI.login(endPoint, user, pass);
                    } catch (Throwable e) {
                        ExceptionHandler.process(e);
                    }
                    salesforceAPI.fetchMetaDataColumns(moduleName);
                    monitor.done();
                }
            });
        } catch (InvocationTargetException e1) {
            ExceptionHandler.process(e1);
        } catch (InterruptedException e2) {
            ExceptionHandler.process(e2);
        }

        if (salesforceAPI.getCurrentMetadataColumns() == null) {
            return null;
        }

        metadataTable.setListColumns(salesforceAPI.getCurrentMetadataColumns());
        return metadataTable;
    }

    protected boolean checkSalesfoceLogin(final String endPoint, final String username, final String password) {

        salesforceAPI.setLogin(false);

        ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
        try {
            dialog.run(true, false, new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                    monitor.beginTask("Connection to Salesforce server and try to login ...", IProgressMonitor.UNKNOWN);

                    if (salesforceAPI == null) {
                        try {
                            salesforceAPI = new SalesforceModuleParseAPI();
                        } catch (Throwable e) {
                            ExceptionHandler.process(e);
                        }
                    }

                    try {
                        salesforceAPI.login(endPoint, username, password);
                        salesforceAPI.setLogin(true);
                    } catch (Throwable e) {
                        ExceptionHandler.process(e);
                    }
                    monitor.done();
                }
            });
        } catch (InvocationTargetException e1) {
            ExceptionHandler.process(e1);
        } catch (InterruptedException e2) {
            ExceptionHandler.process(e2);
        }

        return salesforceAPI.isLogin();
    }

    private IMetadataTable getMetadataTableFromConfigFile(String moduleName) {

        INode node = getSalesforceNode();

        IElementParameter currentModuleNameParam = node.getElementParameter("MODULENAME");
        currentModuleNameParam.setValue(moduleName);

        node.getComponent().createElementParameters(node);

        IElementParameter schemaParam = node.getElementParameter("SCHEMA");

        if (schemaParam == null) {
            return null;
        }

        schemaParam.setValueToDefault(node.getElementParameters()); // Call this method to recompute some parameters
        // value.

        IMetadataTable metadataTable = (IMetadataTable) schemaParam.getValue();

        return metadataTable;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        // TODO Auto-generated method stub
    }

    /**
     * Getter for salesforceAPI.
     * 
     * @return the salesforceAPI
     */
    public SalesforceModuleParseAPI getSalesforceAPI() {
        return this.salesforceAPI;
    }

    /**
     * Sets the salesforceAPI.
     * 
     * @param salesforceAPI the salesforceAPI to set
     */
    public void setSalesforceAPI(SalesforceModuleParseAPI salesforceAPI) {
        this.salesforceAPI = salesforceAPI;
    }

    public IMetadataContextModeManager getContextModeManager() {
        return this.contextModeManager;
    }

    public void setContextModeManager(IMetadataContextModeManager contextModeManager) {
        this.contextModeManager = contextModeManager;
    }
}
