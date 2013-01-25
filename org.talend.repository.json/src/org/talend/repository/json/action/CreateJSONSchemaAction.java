// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.json.action;

import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.cwm.helper.TableHelper;
import org.talend.repository.ProjectManager;
import org.talend.repository.json.i18n.Messages;
import org.talend.repository.json.node.JSONRepositoryNodeType;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.json.JSONFileConnection;
import org.talend.repository.model.json.JSONFileConnectionItem;

/**
 * DOC wanghong class global comment. Detailled comment
 */
public class CreateJSONSchemaAction extends AbstractCreateAction {

    private static final String CREATE_LABEL = Messages.CreateJSONSchemaAction_RETRIEVE_SCHEMA;

    private static final String EDIT_LABEL = Messages.CreateJSONSchemaAction_EDIT_SCHEMA;

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 500;

    public CreateJSONSchemaAction() {
        super();
        setText(CREATE_LABEL);
        setToolTipText(CREATE_LABEL);
        setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_TABLE_ICON));
    }

    @Override
    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(node)) {
            setEnabled(false);
        } else {
            if (ENodeType.REPOSITORY_ELEMENT.equals(node.getType())) {
                if (node.getObject().getRepositoryStatus() == ERepositoryStatus.DELETED
                        || node.getObject().getRepositoryStatus() == ERepositoryStatus.LOCK_BY_OTHER) {
                    setEnabled(false);
                    return;
                }
                if ((factory.getStatus(node.getObject()) == ERepositoryStatus.DELETED)
                        || (factory.getStatus(node.getObject()) == ERepositoryStatus.LOCK_BY_OTHER)) {
                    setEnabled(false);
                    return;
                }

                if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)
                        || ERepositoryObjectType.METADATA_CON_COLUMN.equals(nodeType)) {
                    RepositoryNode parent = node.getParent();
                    if (parent != null && JSONRepositoryNodeType.JSON.equals(parent.getProperties(EProperties.CONTENT_TYPE))) {
                        setText(EDIT_LABEL);
                        collectSiblingNames(node);
                        setEnabled(true);
                        return;
                    }
                } else if (JSONRepositoryNodeType.JSON.equals(nodeType)) {
                    setText(CREATE_LABEL);
                    collectChildNames(node);
                    setEnabled(true);
                    return;
                }
            }
        }
    }

    @Override
    public Class getClassForDoubleClick() {
        return JSONFileConnection.class;
    }

    @Override
    protected void doRun() {
        if (repositoryNode == null) {
            repositoryNode = getCurrentRepositoryNode();
        }
        JSONFileConnection connection = null;
        MetadataTable metadataTable = null;
        boolean creation = false;
        if (repositoryNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) repositoryNode.getProperties(EProperties.CONTENT_TYPE);
            String metadataTableLabel = (String) repositoryNode.getProperties(EProperties.LABEL);

            JSONFileConnectionItem item = null;
            if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
                item = (JSONFileConnectionItem) repositoryNode.getObject().getProperty().getItem();
                connection = (JSONFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, metadataTableLabel);
                creation = false;
            } else if (nodeType == JSONRepositoryNodeType.JSON) {
                item = (JSONFileConnectionItem) repositoryNode.getObject().getProperty().getItem();
                connection = (JSONFileConnection) item.getConnection();
                creation = true;
            } else {
                return;
            }

            boolean isOK = true;
            // if (creation) {
            // isOK = checkJSONConnection((JSONFileConnection) item.getConnection());
            // }
            if (isOK) {
                openJSONSchemaWizard(item, metadataTable, false, creation);
            }
        }
    }

    // private boolean checkJSONConnection(final JSONFileConnection connection) {
    // final boolean[] result = new boolean[] { true };
    // IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
    //
    // public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
    // monitor.beginTask(Messages.getString("CreateJSONSchemaAction.checkConnection"), IProgressMonitor.UNKNOWN);
    // Object dfs = null;
    // try {
    // JSONConnectionBean connectionBean = JSONModelUtil.convert2JSONConnectionBean(connection);
    // dfs = HadoopOperationManager.getInstance().getDFS(connectionBean);
    // } catch (Exception e) {
    // ExceptionHandler.process(e);
    // } finally {
    // monitor.done();
    // }
    // if (dfs == null) {
    // PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
    //
    // @Override
    // public void run() {
    //                            String mainMsg = Messages.getString("CreateJSONSchemaAction.connectionFailure.mainMsg"); //$NON-NLS-1$
    //                            String detailMsg = Messages.getString("CreateJSONSchemaAction.connectionFailure.detailMsg", //$NON-NLS-1$
    // connection.getNameNodeURI());
    // new ErrorDialogWidthDetailArea(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
    // Activator.PLUGIN_ID, mainMsg, detailMsg);
    // result[0] = false;
    // return;
    // }
    // });
    // }
    // }
    // };
    // ProgressMonitorDialog dialog = new
    // ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
    // try {
    // dialog.run(true, true, runnableWithProgress);
    // } catch (Exception e) {
    // result[0] = false;
    // ExceptionHandler.process(e);
    // }
    //
    // return result[0];
    // }

    private void openJSONSchemaWizard(final JSONFileConnectionItem item, final MetadataTable metadataTable,
            final boolean forceReadOnly, final boolean creation) {
        // WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(),
        // new JSONSchemaWizard(PlatformUI.getWorkbench(), creation, repositoryNode.getObject(), metadataTable,
        // getExistingNames(), forceReadOnly));
        // wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        // wizardDialog.create();
        // wizardDialog.open();
    }

}
