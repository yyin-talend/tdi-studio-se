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

import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.properties.tab.IDynamicProperty;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.cmd.ChangeValuesFromRepository;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.process.EDatabaseComponentName;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.dialog.RepositoryReviewDialog;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class PropertyTypeController extends AbstractRepositoryController {

    public PropertyTypeController(IDynamicProperty dp) {
        super(dp);
    }

    /*
     * add by wzhang
     * 
     * @see
     * org.talend.designer.core.ui.editor.properties.controllers.AbstractRepositoryController#createControl(org.eclipse
     * .swt.widgets.Composite, org.talend.core.model.process.IElementParameter, int, int, int,
     * org.eclipse.swt.widgets.Control)
     */
    @Override
    public Control createControl(Composite subComposite, IElementParameter param, int numInRow, int nbInRow, int top,
            Control lastControl) {

        Control lastControlUsed = lastControl;
        lastControlUsed = super.createControl(subComposite, param, numInRow, nbInRow, top, lastControl);
        // add a button if the value is Built-In
        if (EmfComponent.BUILTIN.equals(param.getChildParameters().get("PROPERTY_TYPE").getValue())) { //$NON-NLS-1$
            if (param.getElement() instanceof INode) {
                if (canSaveProperty(param)) {
                    lastControlUsed = addButton(subComposite, param, lastControlUsed, numInRow, top);
                }
            }
        }
        return lastControlUsed;
    }

    /**
     * DOC wzhang Comment method "canSaveProperty".
     * 
     * @param param
     * @return
     */
    private boolean canSaveProperty(IElementParameter param) {
        INode node = (INode) param.getElement();
        //
        boolean canSaved = false;
        String componentName = node.getComponent().getName();
        for (EDatabaseComponentName eComponent : EDatabaseComponentName.values()) {
            if (componentName.equals(eComponent.getInputComponentName())
                    || componentName.equals(eComponent.getOutPutComponentName())) {
                canSaved = true;
                break;
            }
            // Teradata
            /**
             * @author wzhang. For the property in EdatabaseComponentName class is "tELTTeradataInput" and
             * "tELTTeradataOutput". So define the String variable custom.
             */
            if (componentName.equals("tTeradataInput") || componentName.equals("tTeradataOutput")) { //$NON-NLS-1$ //$NON-NLS-2$
                canSaved = true;
            }
        }
        return canSaved;
    }

    /**
     * 
     * DOC wzhang Comment method "addButton".
     * 
     * @param subComposite
     * @param param
     * @param lastControl
     * @param numInRow
     * @param top
     * @return
     */
    private Control addButton(Composite subComposite, final IElementParameter param, Control lastControl, int numInRow, int top) {

        Button button;
        Button resetBtn = null;
        Control lastControlUsed = lastControl;
        Point buttonSize;
        FormData data;

        button = getWidgetFactory().createButton(subComposite, "", SWT.PUSH); //$NON-NLS-1$
        buttonSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        button.setImage(ImageProvider.getImage(EImage.SAVE_ICON));
        button.setToolTipText(Messages.getString("PropertyTypeController.saveToMetadata")); //$NON-NLS-1$
        button.setData(PARAMETER_NAME, param.getName());

        lastControlUsed = button;

        button.addSelectionListener(listenerSelection);

        CLabel labelLabel = getWidgetFactory().createCLabel(subComposite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(lastControl, 0);
        data.right = new FormAttachment(lastControl, labelLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT).x
                + (ITabbedPropertyConstants.HSPACE * 2), SWT.RIGHT);
        if (resetBtn != null) {
            data.top = new FormAttachment(resetBtn, 0, SWT.CENTER);
        } else {
            data.top = new FormAttachment(0, top);
        }
        labelLabel.setLayoutData(data);
        if (numInRow != 1) {
            labelLabel.setAlignment(SWT.RIGHT);
        }

        data = new FormData();
        data.left = new FormAttachment(labelLabel, -1);
        data.right = new FormAttachment(labelLabel, STANDARD_BUTTON_WIDTH, SWT.RIGHT);

        if (resetBtn != null) {
            data.top = new FormAttachment(resetBtn, 0, SWT.CENTER);
        } else {
            data.top = new FormAttachment(0, top);
        }
        data.height = STANDARD_HEIGHT - 2;
        button.setLayoutData(data);

        dynamicProperty.setCurRowSize(buttonSize.y + ITabbedPropertyConstants.VSPACE);
        return lastControlUsed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.ui.editor.properties.controllers.AbstractRepositoryController#createControl(org.eclipse
     * .swt.widgets.Composite, org.talend.core.model.process.IElementParameter, int, int, int,
     * org.eclipse.swt.widgets.Control)
     */
    @Override
    protected Command createButtonCommand(Button button) {
        String paramName = (String) button.getData(PARAMETER_NAME);
        IElementParameter param = elem.getElementParameter(paramName);
        Object data = button.getData(NAME);
        if (data != null && data.equals(REPOSITORY_CHOICE)) {
            RepositoryReviewDialog dialog = new RepositoryReviewDialog(Display.getCurrent().getActiveShell(),
                    ERepositoryObjectType.METADATA, param.getRepositoryValue());
            if (dialog.open() == RepositoryReviewDialog.OK) {
                String id = dialog.getResult().getObject().getId();

                IElementParameter repositoryParam = param.getChildParameters().get(
                        EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
                if (repositoryParam != null) {
                    repositoryParam.setLinkedRepositoryItem(dialog.getResult().getObject().getProperty().getItem());
                }
                String fullParamName = paramName + ":" + getRepositoryChoiceParamName(); //$NON-NLS-1$

                Connection repositoryConnection = null;
                Map<String, ConnectionItem> repositoryConnectionItemMap = dynamicProperty.getRepositoryConnectionItemMap();

                if (repositoryConnectionItemMap.containsKey(id)) {
                    repositoryConnection = repositoryConnectionItemMap.get(id).getConnection();
                } else {
                    repositoryConnection = null;
                    if (repositoryParam != null) {
                        Item item = repositoryParam.getLinkedRepositoryItem();
                        if (item instanceof ConnectionItem) {
                            repositoryConnection = ((ConnectionItem) item).getConnection();
                        }
                    }
                }

                if (repositoryConnection != null) {
                    CompoundCommand compoundCommand = new CompoundCommand();

                    ChangeValuesFromRepository changeValuesFromRepository = new ChangeValuesFromRepository(elem,
                            repositoryConnection, fullParamName, id);
                    changeValuesFromRepository.setMaps(dynamicProperty.getRepositoryTableMap());

                    compoundCommand.add(changeValuesFromRepository);

                    updateDBType(compoundCommand, repositoryConnection);
                    return compoundCommand;
                }

            }
        } else {
            /**
             * add by wzhang. When click the icon at the right side of Built-In. The corresponding wizard open.
             */
            // 1. open wizard
            if (elem instanceof INode) {
                INode node = (INode) elem;
                final IRepositoryService repositoryService = CorePlugin.getDefault().getRepositoryService();
                if (param != null) {
                    RepositoryNode realNode = null;
                    String repositoryValue = param.getRepositoryValue();
                    if (repositoryValue != null && repositoryValue.startsWith("DATABASE")) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_CONNECTIONS);
                    } else

                    // file delimited
                    if ("DELIMITED".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_DELIMITED);
                    }
                    // file positional
                    if ("POSITIONAL".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_POSITIONAL);
                    }
                    // file regexp
                    if ("REGEX".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_REGEXP);
                    }
                    // file xml
                    if ("XML".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_XML);
                    }
                    // file ldif
                    if ("LDIF".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_LDIF);
                    }
                    // excel
                    if ("EXCEL".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_EXCEL);
                    }
                    // generic schema
                    if ("GENERIC".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_GENERIC_SCHEMA);
                    }
                    // ldap
                    if ("LDAP".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_LDAP_SCHEMA);
                    }
                    // wsdl
                    if ("WSDL".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_WSDL_SCHEMA);
                    }
                    // salesforce
                    if ("SALESFORCE".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA);
                    }
                    // ebcdic
                    if ("EBCDIC".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_FILE_EBCDIC);
                    }
                    // sap
                    if ("SAP".equals(repositoryValue)) { //$NON-NLS-1$
                        realNode = repositoryService.getRootRepositoryNode(ERepositoryObjectType.METADATA_SAPCONNECTIONS);
                    }

                    if (realNode != null) {
                        ConnectionItem connItem = repositoryService.openMetadataConnection(true, realNode, node);
                        if (connItem != null) {
                            // refresh
                            RepositoryManager.refreshCreatedNode(ERepositoryObjectType.METADATA_CONNECTIONS);

                            IElementParameter propertyParam = elem
                                    .getElementParameterFromField(EParameterFieldType.PROPERTY_TYPE);
                            propertyParam.getChildParameters().get(EParameterName.PROPERTY_TYPE.getName()).setValue(
                                    EmfComponent.REPOSITORY);

                            // 2. commnd
                            Command cmd = new ChangeValuesFromRepository((Element) node, connItem.getConnection(), propertyParam
                                    .getName()
                                    + ":" + EParameterName.REPOSITORY_PROPERTY_TYPE.getName(), connItem.getProperty().getId()); //$NON-NLS-1$
                            executeCommand(cmd);
                        }
                    }

                }
            }
        }
        return null;
    }

    // see bug 0004305
    private void updateDBType(CompoundCommand compoundCommand, Connection repositoryConnection) {

        if (repositoryConnection == null) {
            return;
        }
        if (!(repositoryConnection instanceof DatabaseConnection)) {
            return;
        }
        final String property = "DBTYPE"; //$NON-NLS-1$
        if (elem.getElementParameter(property) == null) {
            return;
        }

        String currentDbType = ((DatabaseConnection) repositoryConnection).getDatabaseType();
        EDatabaseTypeName typeName = EDatabaseTypeName.getTypeFromDbType(currentDbType);

        Command command = new PropertyChangeCommand(elem, property, typeName.getXMLType());
        compoundCommand.add(command);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.ui.editor.properties.controllers.AbstractRepositoryController#createComboCommand(org
     * .eclipse.swt.custom.CCombo)
     */
    @Override
    protected Command createComboCommand(CCombo combo) {
        Connection repositoryConnection = null;
        ConnectionItem repositoryConnectionItem = null;

        String paramName = (String) combo.getData(PARAMETER_NAME);

        IElementParameter param = elem.getElementParameter(paramName);

        String value = combo.getText();

        for (int j = 0; j < param.getListItemsValue().length; j++) {
            if (combo.getText().equals(param.getListItemsDisplayName()[j])) {
                value = (String) param.getListItemsValue()[j];
            }
        }
        if (value.equals(param.getValue())) {
            return null;
        }
        Map<String, ConnectionItem> repositoryConnectionItemMap = null;
        IElementParameter repositoryParam = null;

        if (value.equals(EmfComponent.REPOSITORY)) {
            repositoryConnectionItemMap = dynamicProperty.getRepositoryConnectionItemMap();

            repositoryParam = param.getParentParameter().getChildParameters().get(
                    EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
            String connectionSelected = (String) repositoryParam.getValue();

            if (repositoryConnectionItemMap.containsKey(connectionSelected)) {
                repositoryConnectionItem = repositoryConnectionItemMap.get(connectionSelected);
                repositoryConnection = repositoryConnectionItem.getConnection();
            } else {
                if (!repositoryConnectionItemMap.isEmpty()) {
                    repositoryConnectionItem = repositoryConnectionItemMap.values().iterator().next();
                    repositoryConnection = repositoryConnectionItem.getConnection();
                } else {
                    repositoryConnection = null;
                }
            }

        }
        CompoundCommand cc = new CompoundCommand();
        ChangeValuesFromRepository changeValuesFromRepository1 = new ChangeValuesFromRepository(elem, repositoryConnection,
                paramName, value);
        changeValuesFromRepository1.setMaps(dynamicProperty.getRepositoryTableMap());
        cc.add(changeValuesFromRepository1);
        if (repositoryConnection != null) {
            ChangeValuesFromRepository changeValuesFromRepository2 = new ChangeValuesFromRepository(elem, repositoryConnection,
                    repositoryParam.getParentParameter().getName() + ":" + repositoryParam.getName(), repositoryConnectionItem //$NON-NLS-1$
                            .getProperty().getId());
            changeValuesFromRepository2.setMaps(dynamicProperty.getRepositoryTableMap());
            cc.add(changeValuesFromRepository2);
        }
        if (value.equals(EmfComponent.REPOSITORY)) {
            updateDBType(cc, repositoryConnection);
        }

        return cc;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.ui.editor.properties.controllers.AbstractRepositoryController#getRepositoryChoiceParamName
     * ()
     */
    @Override
    protected String getRepositoryChoiceParamName() {
        return EParameterName.REPOSITORY_PROPERTY_TYPE.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.ui.editor.properties.controllers.AbstractRepositoryController#getRepositoryTypeParamName
     * ()
     */
    @Override
    protected String getRepositoryTypeParamName() {
        return EParameterName.PROPERTY_TYPE.getName();
    }

    // @Override
    // protected String getDisplayNameFromValue(IElementParameter param, String value) {
    // if (param == null || value == null || value.equals("")) { //$NON-NLS-1$
    // return null;
    // }
    // if (!param.getName().equals(getRepositoryChoiceParamName())) {
    // return null;
    // }
    // Item item = param.getLinkedRepositoryItem();
    // if (item == null || (item != null && !item.getProperty().getId().equals(value))) {
    // Map<String, ConnectionItem> itemMap = dynamicProperty.getRepositoryConnectionItemMap();
    // item = itemMap.get(value);
    // if (item == null) {
    // item = UpdateRepositoryUtils.getConnectionItemByItemId(value);
    // if (item != null) {
    // // set in map
    // itemMap.put(value, (ConnectionItem) item);
    // }
    // }
    // }
    // if (item != null && item instanceof ConnectionItem) {
    // return dynamicProperty.getRepositoryAliasName((ConnectionItem) item) + ":" + item.getProperty().getLabel();
    // //$NON-NLS-1$
    // }
    // return null;
    // }

}
