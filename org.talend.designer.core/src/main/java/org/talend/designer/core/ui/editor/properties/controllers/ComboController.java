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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.commands.Command;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.cmd.ChangeValuesFromRepository;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.cmd.RepositoryChangeQueryCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ComboController.java 1 2006-12-12 下午01:58:48 +0000 (下午01:58:48) yzhang $
 * 
 */
public class ComboController extends AbstractElementPropertySectionController {

    private boolean updateColumnListFlag;

    private SelectionEvent e;

    private Map<String, IMetadataTable> repositoryTableMap;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    private Map<String, List<String>> tablesmap;

    private Map<String, List<String>> queriesmap;

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
    public Command createCommand() {
        repositoryTableMap = dynamicTabbedPropertySection.getRepositoryTableMap();
        repositoryConnectionItemMap = dynamicTabbedPropertySection.getRepositoryConnectionItemMap();
        tablesmap = dynamicTabbedPropertySection.getTablesMap();
        queriesmap = dynamicTabbedPropertySection.getQueriesMap();
        Set<String> elementsName;
        Control ctrl;
        IMetadataTable repositoryMetadata = new MetadataTable();
        Connection repositoryConnection = null;

        elementsName = hashCurControls.keySet();
        for (String name : elementsName) {
            Object o = hashCurControls.get(name);
            if (o instanceof Control) {
                ctrl = (Control) o;
                if (ctrl == null) {
                    hashCurControls.remove(name);
                    return null;
                }

                if (ctrl.equals(e.getSource()) && ctrl instanceof CCombo) {
                    if (name.equals(EParameterName.PROPERTY_TYPE.getName())) {
                        Object obj = elem.getPropertyValue(name);
                    }

                    boolean isDisposed = ((CCombo) ctrl).isDisposed();
                    if (!isDisposed && (!elem.getPropertyValue(name).equals(((CCombo) ctrl).getText()))) {

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

                            // Repository Property Type combo was selected.
                            if (name.equals(EParameterName.REPOSITORY_PROPERTY_TYPE.getName())) {

                                // Updates the Query text.
                                this.dynamicTabbedPropertySection.updateRepositoryList();
                                String schemaSelected = (String) elem
                                        .getPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
                                if (repositoryTableMap.containsKey(schemaSelected)) {
                                    repositoryMetadata = repositoryTableMap.get(schemaSelected);
                                } else {
                                    repositoryMetadata = new MetadataTable();
                                }

                                String newQuery = generateNewQuery(repositoryMetadata);

                                Command queryChangeCommand = new PropertyChangeCommand(elem, "QUERY", newQuery);
                                queryChangeCommand.execute();
                                // Ends.

                                if (repositoryConnectionItemMap.containsKey(value)) {
                                    repositoryConnection = repositoryConnectionItemMap.get(value).getConnection();
                                } else {
                                    repositoryConnection = null;
                                }

                                if (repositoryConnection != null) {
                                    changeValuesFromRepository = new ChangeValuesFromRepository(elem,
                                            repositoryConnection, name, value);
                                    changeValuesFromRepository.setMaps(tablesmap, queriesmap);
                                    return changeValuesFromRepository;
                                }
                                // Property Type combo was selected.
                            } else if (name.equals(EParameterName.PROPERTY_TYPE.getName())) {
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
                                    // Updates the Query text.
                                    this.dynamicTabbedPropertySection.updateRepositoryList();
                                    String schemaSelected = (String) elem
                                            .getPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
                                    if (repositoryTableMap.containsKey(schemaSelected)) {
                                        repositoryMetadata = repositoryTableMap.get(schemaSelected);
                                    } else {
                                        repositoryMetadata = new MetadataTable();
                                    }
                                    String newQuery = "";
                                    if (!EmfComponent.BUILTIN.equals(value)) {
                                        newQuery = generateNewQuery(repositoryMetadata);
                                    } else {
                                        newQuery = TalendTextUtils.addQuotes("");
                                    }

                                    Command queryChangeCommand = new PropertyChangeCommand(elem, "QUERY", newQuery);
                                    queryChangeCommand.execute();
                                    // Ends.

                                    changeValuesFromRepository = new ChangeValuesFromRepository(elem,
                                            repositoryConnection, name, value);
                                    changeValuesFromRepository.setMaps(tablesmap, queriesmap);
                                    return changeValuesFromRepository;
                                } else {
                                    return new PropertyChangeCommand(elem, name, value);
                                }
                            } else if (name.equals(EParameterName.QUERYSTORE_TYPE.getName())) {
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createControl()
     */
    @Override
    public Control createControl(final Composite subComposite, final IElementParameter param, final int numInRow,
            final int nbInRow, final int top, final Control lastControl) {
        IControlCreator cbCtrl = new IControlCreator() {

            public Control createControl(final Composite parent, final int style) {
                CCombo cb = new CCombo(parent, style);
                return cb;
            }
        };
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
        combo.setItems(stringToDisplay.toArray(new String[0]));
        combo.setEditable(false);
        cLayout.setBackground(subComposite.getBackground());
        combo.setEnabled(!param.isReadOnly());
        combo.addSelectionListener(listenerSelection);
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
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub
    }

    SelectionListener listenerSelection = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            e = event;
            dynamicTabbedPropertySection.updateRepositoryList();
            Command cmd = createCommand();
            if (cmd != null) {
                getCommandStack().execute(cmd);
                if (updateColumnListFlag) {
                    ComboController.this.dynamicTabbedPropertySection.updateColumnList(null);
                }
            }
        }
    };

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
            String[] paramItems = param.getListItemsDisplayName();
            String[] comboItems = combo.getItems();
            if (!paramItems.equals(comboItems)) {
                combo.setItems(paramItems);
            }
            combo.setText(strValue);
        }
    }
}
