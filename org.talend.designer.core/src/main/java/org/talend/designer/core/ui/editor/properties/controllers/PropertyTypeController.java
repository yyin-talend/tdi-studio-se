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

import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.cmd.ChangeValuesFromRepository;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty;
import org.talend.repository.ui.dialog.RepositoryReviewDialog;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class PropertyTypeController extends AbstractRepositoryController {

    public PropertyTypeController(IDynamicProperty dp) {
        super(dp);
    }

    /*
     * (non-Javadoc)
     * 
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractRepositoryController#createButtonCommand(org.eclipse.swt.widgets.Button)
     */
    @Override
    protected Command createButtonCommand(Button button) {
        if (button.getData(NAME).equals(REPOSITORY_CHOICE)) {
            String paramName = (String) button.getData(PARAMETER_NAME);
            IElementParameter param = elem.getElementParameter(paramName);
            RepositoryReviewDialog dialog = new RepositoryReviewDialog(button.getShell(), ERepositoryObjectType.METADATA, param
                    .getRepositoryValue());
            if (dialog.open() == RepositoryReviewDialog.OK) {
                String id = dialog.getResult().getObject().getId();
                // String name = dialog.getResult().getObject().getLabel();
                // IElementParameter repositoryChoiceParameter =
                // param.getChildParameters().get(getRepositoryChoiceParamName());
                // repositoryChoiceParameter.setValue(id);
                String fullParamName = paramName + ":" + getRepositoryChoiceParamName();
                // Text text = (Text) hashCurControls.get(fullParamName);
                // text.setText(getDisplayNameFromValue(repositoryChoiceParameter, id));
                Connection repositoryConnection = null;
                Map<String, ConnectionItem> repositoryConnectionItemMap = dynamicProperty.getRepositoryConnectionItemMap();
                Map<String, IMetadataTable> repositoryTableMap = dynamicProperty.getRepositoryTableMap();
                Map<String, List<String>> tablesmap = dynamicProperty.getTablesMap();
                Map<String, List<String>> queriesmap = dynamicProperty.getQueriesMap();

                if (repositoryConnectionItemMap.containsKey(id)) {
                    repositoryConnection = repositoryConnectionItemMap.get(id).getConnection();
                } else {
                    repositoryConnection = null;
                }

                if (repositoryConnection != null) {
                    ChangeValuesFromRepository changeValuesFromRepository = new ChangeValuesFromRepository(elem,
                            repositoryConnection, fullParamName, id);
                    changeValuesFromRepository.setMaps(tablesmap, queriesmap, repositoryTableMap, dynamicProperty
                            .getRepositoryQueryStoreMap());
                    return changeValuesFromRepository;
                }

            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractRepositoryController#createComboCommand(org.eclipse.swt.custom.CCombo)
     */
    @Override
    protected Command createComboCommand(CCombo combo) {
        Connection repositoryConnection = null;

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
        Map<String, IMetadataTable> repositoryTableMap = null;
        Map<String, ConnectionItem> repositoryConnectionItemMap = null;
        Map<String, List<String>> tablesMap = null;
        Map<String, List<String>> queriesMap = null;
        Map<String, Query> queryStoreMap = null;

        if (value.equals(EmfComponent.REPOSITORY)) {
            repositoryTableMap = dynamicProperty.getRepositoryTableMap();
            repositoryConnectionItemMap = dynamicProperty.getRepositoryConnectionItemMap();
            tablesMap = dynamicProperty.getTablesMap();
            queriesMap = dynamicProperty.getQueriesMap();
            queryStoreMap = dynamicProperty.getRepositoryQueryStoreMap();

            IElementParameter repositoryParam = param.getParentParameter().getChildParameters().get(
                    EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
            String connectionSelected = (String) repositoryParam.getValue();

            if (repositoryConnectionItemMap.containsKey(connectionSelected)) {
                repositoryConnection = (org.talend.core.model.metadata.builder.connection.Connection) repositoryConnectionItemMap
                        .get(connectionSelected).getConnection();
            } else {
                repositoryConnection = null;
            }
        }

        ChangeValuesFromRepository changeValuesFromRepository = new ChangeValuesFromRepository(elem, repositoryConnection,
                paramName, value);

        changeValuesFromRepository.setMaps(tablesMap, queriesMap, repositoryTableMap, queryStoreMap);
        return changeValuesFromRepository;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractRepositoryController#getRepositoryChoiceParamName()
     */
    @Override
    protected String getRepositoryChoiceParamName() {
        return EParameterName.REPOSITORY_PROPERTY_TYPE.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractRepositoryController#getRepositoryTypeParamName()
     */
    @Override
    protected String getRepositoryTypeParamName() {
        return EParameterName.PROPERTY_TYPE.getName();
    }

}
