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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.cmd.ChangeValuesFromRepository;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.cmd.QueryGuessCommand;
import org.talend.designer.core.ui.editor.cmd.RepositoryChangeQueryCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ComboController.java 1 2006-12-12 涓嬪崍01:58:48 +0000 (涓嬪崍01:58:48) yzhang $
 * 
 */
public class ComboController extends AbstractElementPropertySectionController {

    private Map<String, IMetadataTable> repositoryTableMap;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    private Map<String, List<String>> tablesmap;

    private Map<String, List<String>> queriesmap;

    private Map<IElementParameter, Button> queryButton = new HashMap<IElementParameter, Button>();;

    private static final String GUESS_QUERY_NAME = "Guess Query";

    /**
     * DOC dev ColumnListController constructor comment.
     * 
     * @param parameterBean
     */
    public ComboController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createCommand()
     */
    public Command createComboCommand(SelectionEvent event) {
        repositoryTableMap = dynamicTabbedPropertySection.getRepositoryTableMap();
        repositoryConnectionItemMap = dynamicTabbedPropertySection.getRepositoryConnectionItemMap();
        tablesmap = dynamicTabbedPropertySection.getTablesMap();
        queriesmap = dynamicTabbedPropertySection.getQueriesMap();
        Set<String> elementsName;
        Control ctrl;
        Connection repositoryConnection = null;
        IElementParameter switchParam = elem.getElementParameter(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName());
        elementsName = hashCurControls.keySet();
        for (String name : elementsName) {
            Object o = hashCurControls.get(name);
            if (o instanceof Control) {
                ctrl = (Control) o;
                if (ctrl == null) {
                    hashCurControls.remove(name);
                    return null;
                }
                CCombo combo = (CCombo) event.getSource();

                Object data = ctrl.getData(PARAMETER_NAME);
                if (!(ctrl instanceof CCombo)) {
                    continue;
                }
                boolean isDisposed = ((CCombo) ctrl).isDisposed() || combo.isDisposed();
                if (isDisposed) {
                    continue;
                }
                if (data != null && data.equals(combo.getData(PARAMETER_NAME))) {
                    if (!elem.getPropertyValue(name).equals(((CCombo) ctrl).getText())) {

                        String value = new String(""); //$NON-NLS-1$
                        for (int i = 0; i < elem.getElementParameters().size(); i++) {
                            IElementParameter param = elem.getElementParameters().get(i);
                            if (param.getName().equals(name)) {
                                for (int j = 0; j < param.getListItemsValue().length; j++) {
                                    if (((CCombo) ctrl).getText().equals(param.getListItemsDisplayName()[j])) {
                                        value = (String) param.getListItemsValue()[j];
                                    }
                                }
                            }
                        }
                        if (value.equals(elem.getPropertyValue(name))) { // same value so no need to do anything
                            return null;
                        }

                        if (name.equals(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName())) {
                            this.dynamicTabbedPropertySection.updateRepositoryList();
                            if (elem instanceof Node) {
                                Map<String, Query> repositoryQueryStoreMap = this.dynamicTabbedPropertySection
                                        .getRepositoryQueryStoreMap();
                                if (repositoryQueryStoreMap.containsKey(value)) {
                                    Query query = repositoryQueryStoreMap.get(value);
                                    IElementParameter queryText = getQueryTextElementParameter(elem);
                                    if (queryText != null) {
                                        return new RepositoryChangeQueryCommand(elem, query, name, value);
                                    }
                                }
                            }

                        } else {
                            ChangeValuesFromRepository changeValuesFromRepository;

                            if (name.equals(EParameterName.REPOSITORY_PROPERTY_TYPE.getName())) {
                                if (repositoryConnectionItemMap.containsKey(value)) {
                                    repositoryConnection = repositoryConnectionItemMap.get(value).getConnection();
                                } else {
                                    repositoryConnection = null;
                                }

                                if (repositoryConnection != null) {
                                    changeValuesFromRepository = new ChangeValuesFromRepository(elem,
                                            repositoryConnection, name, value);
                                    changeValuesFromRepository.setMaps(tablesmap, queriesmap, repositoryTableMap);
                                    return changeValuesFromRepository;
                                }
                            }

                            else if (name.equals(EParameterName.PROPERTY_TYPE.getName())) {
                                String connectionSelected;
                                connectionSelected = (String) elem
                                        .getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());

                                if (repositoryConnectionItemMap.containsKey(connectionSelected)) {
                                    repositoryConnection = (org.talend.core.model.metadata.builder.connection.Connection) repositoryConnectionItemMap
                                            .get(connectionSelected).getConnection();
                                } else {
                                    repositoryConnection = null;
                                }

                                if (repositoryConnection != null) {
                                    changeValuesFromRepository = new ChangeValuesFromRepository(elem,
                                            repositoryConnection, name, value);

                                    changeValuesFromRepository.setMaps(tablesmap, queriesmap, repositoryTableMap);
                                    return changeValuesFromRepository;
                                } else {
                                    return new PropertyChangeCommand(elem, name, value);
                                }
                            }

                            else if (name.equals(EParameterName.QUERYSTORE_TYPE.getName())) {
                                if (elem instanceof Node) {
                                    this.dynamicTabbedPropertySection.updateRepositoryList();
                                    String querySelected;
                                    Query repositoryQuery = null;
                                    querySelected = (String) elem
                                            .getPropertyValue(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName());

                                    Map<String, Query> repositoryQueryStoreMap = this.dynamicTabbedPropertySection
                                            .getRepositoryQueryStoreMap();
                                    if (repositoryQueryStoreMap.containsKey(querySelected)) {
                                        repositoryQuery = repositoryQueryStoreMap.get(querySelected);
                                    }
                                    if (switchParam != null) {
                                        switchParam.setValue(Boolean.FALSE);
                                    }

                                    if (repositoryQuery != null) {
                                        Command cmd = new RepositoryChangeQueryCommand(elem, repositoryQuery, name,
                                                value);
                                        getCommandStack().execute(cmd);
                                    } else {
                                        Command cmd = new PropertyChangeCommand(elem, name, value);
                                        getCommandStack().execute(cmd);
                                    }

                                }

                            } else {
                                return new PropertyChangeCommand(elem, name, value);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    IControlCreator cbCtrl = new IControlCreator() {

        public Control createControl(final Composite parent, final int style) {
            CCombo cb = new CCombo(parent, style);
            return cb;
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

        DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, cbCtrl);
        if (param.isRequired()) {
            FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                    FieldDecorationRegistry.DEC_REQUIRED);
            dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.TOP, false);
        }
        if (param.isRepositoryValueUsed()) {
            FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                    FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
            decoration.setDescription(Messages.getString("ComboController.valueFromRepository")); //$NON-NLS-1$
            dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.BOTTOM, false);
        }

        Control cLayout = dField.getLayoutControl();
        CCombo combo = (CCombo) dField.getControl();
        FormData data;
        combo.setItems(getListToDisplay(param));
        combo.setEditable(false);
        cLayout.setBackground(subComposite.getBackground());
        combo.setEnabled(!param.isReadOnly());
        combo.addSelectionListener(listenerSelection);
        combo.setData(PARAMETER_NAME, param.getName());
        if (param.getName().equals(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
            combo.setVisibleItemCount(10);
        }
        if (elem instanceof Node) {
            combo.setToolTipText(VARIABLE_TOOLTIP + param.getVariableName());
        }

        CLabel labelLabel = getWidgetFactory().createCLabel(subComposite, param.getDisplayName());
        data = new FormData();
        if (lastControl != null) {
            data.left = new FormAttachment(lastControl, 0);
        } else {
            data.left = new FormAttachment((((numInRow - 1) * MAX_PERCENT) / nbInRow), 0);
        }

        IElementParameter queryStoreTypeParameter = elem.getElementParameter(EParameterName.QUERYSTORE_TYPE.getName());
        if (queryStoreTypeParameter != null) {
            String queryStoreType = (String) queryStoreTypeParameter.getValue();
            if (param.getName().equals(EParameterName.QUERYSTORE_TYPE.getName()) && queryStoreType != null
                    && queryStoreType.equals(EmfComponent.BUILTIN)) {
                Control lastUsedControl = combo;
                queryButton.put(param, null);
                addGuessQueryButton(subComposite, param, lastUsedControl, numInRow, top);
            }
        }
        data.top = new FormAttachment(0, top);
        labelLabel.setLayoutData(data);
        if (numInRow != 1) {
            labelLabel.setAlignment(SWT.RIGHT);
        }
        // *********************
        data = new FormData();
        int currentLabelWidth = STANDARD_LABEL_WIDTH;
        GC gc = new GC(labelLabel);
        Point labelSize = gc.stringExtent(param.getDisplayName());
        gc.dispose();

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
        data.top = new FormAttachment(0, top);
        cLayout.setLayoutData(data);
        // **********************
        hashCurControls.put(param.getName(), combo);

        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        dynamicTabbedPropertySection.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);
        return cLayout;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#estimateRowSize(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter)
     */
    @Override
    public int estimateRowSize(Composite subComposite, IElementParameter param) {
        DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, cbCtrl);
        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        dField.getLayoutControl().dispose();

        return initialSize.y + ITabbedPropertyConstants.VSPACE;
    }

    /**
     * This method is used for creating a Button named "Guess Query".
     * 
     * @param subComposite
     * @param lastControl
     * @param numInRow
     * @param top
     */
    private void addGuessQueryButton(Composite subComposite, IElementParameter param, Control lastControl,
            int numInRow, int top) {
        final DecoratedField dField1 = new DecoratedField(subComposite, SWT.PUSH, new IControlCreator() {

            public Control createControl(Composite parent, int style) {
                return new Button(parent, style);
            }
        });
        Button guessQueryButton = null;
        Control buttonControl = dField1.getLayoutControl();
        guessQueryButton = (Button) dField1.getControl();
        guessQueryButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        buttonControl.setBackground(subComposite.getBackground());
        guessQueryButton.setEnabled(true);
        guessQueryButton.setData(NAME, GUESS_QUERY_NAME);
        guessQueryButton.setData(PARAMETER_NAME, param.getName());
        guessQueryButton.setText(GUESS_QUERY_NAME);

        FormData data1 = new FormData();
        data1.left = new FormAttachment(lastControl, 210);
        data1.top = new FormAttachment(0, top);

        buttonControl.setLayoutData(data1);
        guessQueryButton.addSelectionListener(listenerSelection);
        queryButton.put(param, guessQueryButton);

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub
    }

    SelectionListener listenerSelection = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            dynamicTabbedPropertySection.updateRepositoryList();
            Command cmd = createCommand(event);
            if (cmd != null) {
                getCommandStack().execute(cmd);
            }
        }
    };

    /**
     * This method is used for getting command base on component type.
     * 
     * @param e
     * @return
     */
    private Command createCommand(SelectionEvent event) {
        Command cmd = null;
        if (event.getSource() instanceof CCombo) {
            cmd = createComboCommand(event);
        } else if (event.getSource() instanceof Button) {
            cmd = createButtonCommand();
        }
        return cmd;
    }

    /**
     * This method is used for "Guess Query" button.
     * 
     * @return
     */
    private Command createButtonCommand() {
        IMetadataTable repositoryMetadata = null;
        IMetadataTable newRepositoryMetadata = null;
        String realTableName = null;
        String realTableId = null;

        // Only for getting the real table name.
        if (elem.getPropertyValue(EParameterName.SCHEMA_TYPE.getName()).equals(EmfComponent.REPOSITORY)) {
            Map<String, IMetadataTable> repositoryTableMap = dynamicTabbedPropertySection.getRepositoryTableMap();
            String paramName;
            IElementParameter repositorySchemaTypeParameter = elem
                    .getElementParameter(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
            Object repositoryControl = hashCurControls.get(repositorySchemaTypeParameter.getName());

            paramName = EParameterName.REPOSITORY_SCHEMA_TYPE.getName();

            if (repositoryControl != null) {

                String selectedComboItem = ((CCombo) repositoryControl).getText();
                if (selectedComboItem != null && selectedComboItem.length() > 0) {
                    String value = new String(""); //$NON-NLS-1$
                    for (int i = 0; i < elem.getElementParameters().size(); i++) {
                        IElementParameter param = elem.getElementParameters().get(i);
                        if (param.getName().equals(paramName)) {
                            for (int j = 0; j < param.getListItemsValue().length; j++) {
                                if (selectedComboItem.equals(param.getListItemsDisplayName()[j])) {
                                    value = (String) param.getListItemsValue()[j];
                                }
                            }
                        }
                    }
                    if (elem instanceof Node) {
                        this.dynamicTabbedPropertySection.updateRepositoryList();
                        if (repositoryTableMap.containsKey(value)) {
                            repositoryMetadata = repositoryTableMap.get(value);
                            realTableName = repositoryMetadata.getTableName();
                            realTableId = repositoryMetadata.getId();
                        } else {
                            repositoryMetadata = new MetadataTable();
                        }
                    }
                }
            }
        }// Ends

        QueryGuessCommand cmd = null;
        Node node = null;
        if (elem instanceof Node) {
            node = (Node) elem;
        } else { // else instanceof Connection
            node = ((org.talend.designer.core.ui.editor.connections.Connection) elem).getSource();
        }

        newRepositoryMetadata = node.getMetadataList().get(0);

        if (newRepositoryMetadata == null) {
            String schemaSelected = (String) node.getPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
            if (repositoryTableMap != null && schemaSelected != null && repositoryTableMap.containsKey(schemaSelected)) {
                repositoryMetadata = repositoryTableMap.get(schemaSelected);
            } else if (newRepositoryMetadata == null) {
                MessageDialog.openWarning(new Shell(), "Alert", "Nothing to guess.");
                return cmd;
            }
        }
        cmd = new QueryGuessCommand(node, newRepositoryMetadata);

        cmd.setMaps(dynamicTabbedPropertySection.getTableIdAndDbTypeMap(), dynamicTabbedPropertySection
                .getTableIdAndDbSchemaMap(), repositoryTableMap);
        String type = getValueFromRepositoryName("TYPE");
        cmd.setParameters(realTableId, realTableName, type);

        return cmd;
    }

    /**
     * DOC ftang Comment method "getQueryTextElementParameter".
     * 
     * @param elem
     * @return
     */
    private IElementParameter getQueryTextElementParameter(Element elem) {
        for (IElementParameter param : (List<IElementParameter>) elem.getElementParameters()) {
            if (param.getField() == EParameterFieldType.MEMO_SQL) {
                return param;
            }
        }
        return null;
    }

    @Override
    public void refresh(IElementParameter param, boolean check) {
        CCombo combo = (CCombo) hashCurControls.get(param.getName());

        if (combo == null) {
            return;
        }
        Object value = param.getValue();

        Button button = queryButton.get(param);
        if (button != null && (!button.isDisposed())) {
            boolean hasDbRepository = false;
            boolean hasDbTableField = false;
            IElementParameter schemaParam = elem.getElementParameter("SCHEMA_TYPE");
            if (schemaParam != null) {
                String schemaType = (String) schemaParam.getValue();
                if (schemaType.equals("REPOSITORY")) {
                    // repository mode
                    String metaRepositoryName = (String) elem.getElementParameter("REPOSITORY_SCHEMA_TYPE").getValue();
                    Connection connection = MetadataTool.getConnectionFromRepository(metaRepositoryName);
                    if (connection instanceof DatabaseConnection) {
                        hasDbRepository = true;
                    }
                }
            }
            if (!hasDbRepository) {
                IElementParameter dbTableParam = elem.getElementParameterFromField(EParameterFieldType.DBTABLE);
                if (dbTableParam != null && dbTableParam.isShow(elem.getElementParameters())) {
                    hasDbTableField = true;
                }
            }
            button.setEnabled(hasDbRepository | hasDbTableField);
        }

        if (value instanceof String) {
            String strValue = ""; //$NON-NLS-1$
            int nbInList = 0, nbMax = param.getListItemsValue().length;
            String name = (String) value;
            while (strValue.equals(new String("")) && nbInList < nbMax) { //$NON-NLS-1$
                if (name.equals(param.getListItemsValue()[nbInList])) {
                    strValue = param.getListItemsDisplayName()[nbInList];
                }
                nbInList++;
            }
            String[] paramItems = getListToDisplay(param);
            String[] comboItems = combo.getItems();

            if (!Arrays.equals(paramItems, comboItems)) {
                combo.setItems(paramItems);
            }
            combo.setText(strValue);
        }
    }
    
    private String[] getListToDisplay(IElementParameter param) {
        String[] originalList = param.getListItemsDisplayName();
        List<String> stringToDisplay = new ArrayList<String>();
        String[] itemsShowIf = param.getListItemsShowIf();
        if (itemsShowIf != null) {
            String[] itemsNotShowIf = param.getListItemsNotShowIf();
            for (int i = 0; i < originalList.length; i++) {
                if (param.isShow(itemsShowIf[i], itemsNotShowIf[i], elem.getElementParameters())) {
                    stringToDisplay.add(originalList[i]);
                }
            }
        } else {
            for (int i = 0; i < originalList.length; i++) {
                stringToDisplay.add(originalList[i]);
            }
        }
        return stringToDisplay.toArray(new String[0]);
    }
}
