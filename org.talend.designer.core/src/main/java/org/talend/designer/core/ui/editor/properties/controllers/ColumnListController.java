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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.ChangeValuesFromRepository;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.cmd.SchemaPropertyChangeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ColumnListController.java 1 2006-12-12 下午02:04:32 +0000 (下午02:04:32) yzhang $
 * 
 */
public class ColumnListController extends AbstractElementPropertySectionController {

    private static final String REFRESH_BUTTON = "icons/refresh.gif"; //$NON-NLS-1$

    private boolean updateColumnListFlag;

    private SelectionEvent e;

    private Map<String, IMetadataTable> repositoryTableMap;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    /**
     * DOC dev ColumnListController constructor comment.
     * 
     * @param parameterBean
     */
    public ColumnListController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createCommand()
     */
    @Override
    public Command createCommand() {
        repositoryTableMap = dynamicTabbedPropertySection.getRepositoryTableMap();
        repositoryConnectionItemMap = dynamicTabbedPropertySection.getRepositoryConnectionItemMap();

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
                        if (name.equals(EParameterName.REPOSITORY_SCHEMA_TYPE.getName())) {
                            if (elem instanceof Node) {
                                if (repositoryTableMap.containsKey(value)) {
                                    repositoryMetadata = repositoryTableMap.get(value);
                                } else {
                                    repositoryMetadata = new MetadataTable();
                                }
                                return new SchemaPropertyChangeCommand((Node) elem, name, value, repositoryMetadata);
                            }
                        } else if (name.equals(EParameterName.REPOSITORY_PROPERTY_TYPE.getName())) {
                            if (repositoryConnectionItemMap.containsKey(value)) {
                                repositoryConnection = repositoryConnectionItemMap.get(value).getConnection();
                            } else {
                                repositoryConnection = null;
                            }

                            if (repositoryConnection != null) {
                                return new ChangeValuesFromRepository(elem, repositoryConnection, name, value);
                            }
                        } else if (name.equals(EParameterName.PROPERTY_TYPE.getName())) {
                            String connectionSelected;
                            connectionSelected = (String) elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE
                                    .getName());

                            if (repositoryConnectionItemMap.containsKey(connectionSelected)) {
                                repositoryConnection = (org.talend.core.model.metadata.builder.connection.Connection) repositoryConnectionItemMap
                                        .get(connectionSelected).getConnection();
                            } else {
                                repositoryConnection = null;
                            }

                            if (repositoryConnection != null) {
                                return new ChangeValuesFromRepository(elem, repositoryConnection, name, value);
                            } else {
                                return new PropertyChangeCommand(elem, name, value);
                            }
                        } else if (name.equals(EParameterName.SCHEMA_TYPE.getName())) {
                            if (elem instanceof Node) {
                                String schemaSelected;
                                schemaSelected = (String) elem.getPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE
                                        .getName());
                                if (repositoryTableMap.containsKey(schemaSelected)) {
                                    repositoryMetadata = repositoryTableMap.get(schemaSelected);
                                } else {
                                    repositoryMetadata = new MetadataTable();
                                }

                                updateColumnListFlag = true;
                                return new SchemaPropertyChangeCommand((Node) elem, name, value, repositoryMetadata);
                            }
                        } else {
                            return new PropertyChangeCommand(elem, name, value);
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

        if (param.getField() == EParameterFieldType.COLUMN_LIST) {
            param.setDisplayName(EParameterName.COLUMN_LIST.getDisplayName());
        } else {
            param.setDisplayName(EParameterName.PREV_COLUMN_LIST.getDisplayName());
        }

        Button refreshBtn;
        refreshBtn = getWidgetFactory().createButton(subComposite, "", SWT.PUSH); //$NON-NLS-1$
        refreshBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        refreshBtn.setImage(CorePlugin.getImageDescriptor(REFRESH_BUTTON).createImage());

        refreshBtn.addSelectionListener(listenerSelection);
        refreshBtn.setData(NAME, COLUMN);
        refreshBtn.setData(PROPERTY, param.getName());
        refreshBtn.setEnabled(!param.isReadOnly());

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

        Control cLayout = dField.getLayoutControl();
        CCombo combo = (CCombo) dField.getControl();
        FormData data;
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
        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);

        data = new FormData();
        data.left = new FormAttachment(cLayout, 0, SWT.RIGHT);
        data.top = new FormAttachment(0, top);
        data.height = initialSize.y;

        refreshBtn.setLayoutData(data);

        // **********************
        hashCurControls.put(param.getName(), combo);
        this.dynamicTabbedPropertySection.updateColumnList(null);

        dynamicTabbedPropertySection.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);
        return refreshBtn;
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
            // updateRepositoryList();
            dynamicTabbedPropertySection.updateRepositoryList();
            Command cmd = createCommand();
            if (cmd != null) {
                getCommandStack().execute(cmd);
                if (updateColumnListFlag) {
                    ColumnListController.this.dynamicTabbedPropertySection.updateColumnList(null);
                }
            }
        }
    };

    @Override
    public void refresh(IElementParameter param, boolean check) {
        CCombo combo = (CCombo) hashCurControls.get(param.getName());
        Object value = param.getValue();

        if (value instanceof String) {
            String strValue = (String) value; //$NON-NLS-1$
            int nbInList = 0, nbMax = param.getListItemsValue().length;
            String name = (String) elem.getPropertyValue(param.getName());
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
