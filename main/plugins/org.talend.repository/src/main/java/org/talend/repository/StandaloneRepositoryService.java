// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * Repository service for job runtime. Currently, the main purpose is to retrieve the job context.
 */
public class StandaloneRepositoryService implements IRepositoryService {

    private Properties contextProperties;

    public void setContextProperties(Properties contextProperties) {
        this.contextProperties = contextProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getComponentsFactory()
     */
    @Override
    public IComponentsFactory getComponentsFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getPathFileName(java.lang.String, java.lang.String)
     */
    @Override
    public IPath getPathFileName(String folderName, String fileName) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getProxyRepositoryFactory()
     */
    @Override
    public IProxyRepositoryFactory getProxyRepositoryFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#getRepositoryPath(org.talend.repository.model.IRepositoryNode)
     */
    @Override
    public IPath getRepositoryPath(IRepositoryNode node) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#registerRepositoryChangedListener(org.talend.repository.
     * IRepositoryChangedListener)
     */
    @Override
    public void registerRepositoryChangedListener(IRepositoryChangedListener listener) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#removeRepositoryChangedListener(org.talend.repository.
     * IRepositoryChangedListener)
     */
    @Override
    public void removeRepositoryChangedListener(IRepositoryChangedListener listener) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#repositoryChanged(org.talend.repository.IRepositoryElementDelta)
     */
    @Override
    public void repositoryChanged(IRepositoryElementDelta event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#notifySQLBuilder(java.util.List)
     */
    @Override
    public void notifySQLBuilder(List<IRepositoryViewObject> list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#validateColumnName(java.lang.String, int)
     */
    @Override
    public String validateColumnName(String columnName, int index) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#registerRepositoryChangedListenerAsFirst(org.talend.repository
     * .IRepositoryChangedListener)
     */
    @Override
    public void registerRepositoryChangedListenerAsFirst(IRepositoryChangedListener listener) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#openLoginDialog()
     */
    @Override
    public void openLoginDialog() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#openLoginDialog(org.eclipse.swt.widgets.Shell, boolean)
     */
    @Override
    public boolean openLoginDialog(Shell shell, boolean inuse) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#openReadOnlyDialog(org.eclipse.swt.widgets.Shell)
     */
    @Override
    public boolean openReadOnlyDialog(Shell shell) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#initializeForTalendStartupJob()
     */
    @Override
    public void initializeForTalendStartupJob() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#initializePluginMode()
     */
    @Override
    public void initializePluginMode() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#isRCPMode()
     */
    @Override
    public boolean isRCPMode() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#setRCPMode()
     */
    @Override
    public void setRCPMode() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#cloneOriginalValueConnection(org.talend.core.model.metadata.builder
     * .connection.DatabaseConnection)
     */
    @Override
    public DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn) {
        return StandaloneConnectionContextUtils.cloneOriginalValueConnection(dbConn, contextProperties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#cloneOriginalValueConnection(org.talend.core.model.metadata.builder
     * .connection.DatabaseConnection, boolean)
     */
    @Override
    public DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn, boolean defaultContext) {
        return StandaloneConnectionContextUtils.cloneOriginalValueConnection(dbConn, contextProperties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#cloneOriginalValueConnection(org.talend.core.model.metadata.builder
     * .connection.DatabaseConnection, boolean, java.lang.String)
     */
    @Override
    public DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn, boolean defaultContext,
            String selectedContext) {
        return StandaloneConnectionContextUtils.cloneOriginalValueConnection(dbConn, contextProperties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#setMetadataConnectionParameter(org.talend.core.model.metadata.
     * builder.connection.DatabaseConnection, org.talend.core.model.metadata.IMetadataConnection)
     */
    @Override
    public void setMetadataConnectionParameter(DatabaseConnection dbConn, IMetadataConnection metaConn) {
        StandaloneConnectionContextUtils.setMetadataConnectionParameter(dbConn, metaConn, contextProperties);
    }

    @Override
    public FileConnection cloneOriginalValueConnection(FileConnection fileConn) {
        return StandaloneConnectionContextUtils.cloneOriginalValueConnection(fileConn, contextProperties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#openSQLPatternEditor(org.talend.core.model.properties.SQLPatternItem
     * , boolean)
     */
    @Override
    public IEditorPart openSQLPatternEditor(SQLPatternItem item, boolean readOnly) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#createSqlpattern(java.lang.String, boolean)
     */
    @Override
    public void createSqlpattern(String path, boolean isFromSqlPatternComposite) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#addRepositoryTreeViewListener(org.eclipse.jface.viewers.
     * ISelectionChangedListener)
     */
    @Override
    public void addRepositoryTreeViewListener(ISelectionChangedListener listener) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#removeRepositoryTreeViewListener(org.eclipse.jface.viewers.
     * ISelectionChangedListener)
     */
    @Override
    public void removeRepositoryTreeViewListener(ISelectionChangedListener listener) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getRepositoryPreferenceStore()
     */
    @Override
    public IPreferenceStore getRepositoryPreferenceStore() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getRepositoryNode(java.lang.String, boolean)
     */
    @Override
    public IRepositoryNode getRepositoryNode(String id, boolean expanded) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#openRepositoryReviewDialog(org.talend.core.model.repository.
     * ERepositoryObjectType, java.lang.String, java.util.List, org.talend.core.model.process.IContextManager)
     */
    @Override
    public void openRepositoryReviewDialog(ERepositoryObjectType type, String repositoryType, List<IContextParameter> params,
            IContextManager contextManager) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getRootRepositoryNode(org.talend.core.model.repository.
     * ERepositoryObjectType)
     */
    @Override
    public IRepositoryNode getRootRepositoryNode(ERepositoryObjectType type) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#setInternalNodeHTMLMap(org.talend.core.model.process.INode,
     * java.util.Map)
     */
    @Override
    public void setInternalNodeHTMLMap(INode node, Map<String, Object> internalNodeHTMLMap) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getDialogSettings()
     */
    @Override
    public IDialogSettings getDialogSettings() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#getTablesFromSpecifiedDataPackage(org.talend.core.model.metadata
     * .builder.connection.DatabaseConnection)
     */
    @Override
    public Set<MetadataTable> getTablesFromSpecifiedDataPackage(DatabaseConnection dbconn) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getClassForSalesforceModule()
     */
    @Override
    public Class getClassForSalesforceModule() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#getCreateRoutineAction(org.talend.repository.ui.views.IRepositoryView
     * )
     */
    @Override
    public AContextualAction getCreateRoutineAction(IRepositoryView repositoryView) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#getRulesProviderPath(org.talend.core.model.properties.RulesItem)
     */
    @Override
    public String getRulesProviderPath(RulesItem currentRepositoryItem) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#exportPigudf(org.talend.designer.runprocess.IProcessor,
     * org.talend.core.model.properties.Property, boolean)
     */
    @Override
    public String exportPigudf(IProcessor processor, Property property, boolean isExport) throws ProcessorException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryService#getRepNodeFromRepReviewDialog(org.eclipse.swt.widgets.Shell,
     * org.talend.core.model.repository.ERepositoryObjectType, java.lang.String)
     */
    @Override
    public RepositoryNode getRepNodeFromRepReviewDialog(Shell parentShell, ERepositoryObjectType type, String repositoryType) {
        // TODO Auto-generated method stub
        return null;
    }

}
