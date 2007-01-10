// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.CorePlugin;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.ui.metadata.dialog.MetadataDialog;
import org.talend.designer.core.ui.editor.cmd.ChangeMetadataCommand;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: SchemaController.java 1 2006-12-12 上午11:21:25 +0000 (上午11:21:25) yzhang $
 * 
 */
public class SchemaController extends AbstractElementPropertySectionController {

    private static final String RESET_COLUMNS = "RESET_COLUMNS"; //$NON-NLS-1$

    private static final String SCHEMA = "SCHEMA"; //$NON-NLS-1$

    private Button btn;

    private Button resetBtn = null;

    /**
     * DOC yzhang SchemaController constructor comment.
     * 
     * @param parameterBean
     */
    public SchemaController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createCommand()
     */
    @Override
    public Command createCommand() {

        if (btn.getData(NAME).equals(SCHEMA)) {

            Node node;
            if (elem instanceof Node) {
                node = (Node) elem;
            } else { // else instanceof Connection
                node = ((Connection) elem).getSource();
            }
            IMetadataTable inputMetadata = null, inputMetaCopy = null;
            Connection inputConec = null;

            boolean inputReadOnly = false, outputReadOnly = false;
            for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                if (connec.isActivate() && connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                    inputMetadata = connec.getMetadataTable();
                    inputMetaCopy = inputMetadata.clone();
                    inputConec = connec;

                    if (connec.getSource().isReadOnly()) {
                        inputReadOnly = true;
                    } else {
                        for (IElementParameter param : connec.getSource().getElementParameters()) {
                            if (param.getField() == EParameterFieldType.SCHEMA_TYPE) {
                                if (param.isReadOnly()) {
                                    inputReadOnly = true;
                                }
                            }
                        }
                    }

                }
            }

            String propertyName = (String) btn.getData(PROPERTY);
            IElementParameter param = node.getElementParameter(propertyName);
            if (param.isReadOnly() || node.isReadOnly()) {
                outputReadOnly = true;
            }
            IMetadataTable originaleOutputTable = (IMetadataTable) node.getMetadataList().get(0);
            IMetadataTable outputMetaCopy = originaleOutputTable.clone();

            MetadataDialog metaDialog;
            if (inputMetadata != null) {
                metaDialog = new MetadataDialog(composite.getShell(), inputMetaCopy, inputMetadata.getTableName(),
                        outputMetaCopy, node.getUniqueName(), getCommandStack());
            } else {
                metaDialog = new MetadataDialog(composite.getShell(), outputMetaCopy, node.getUniqueName(),
                        getCommandStack());
            }
            metaDialog.setText("Schema of " + node.getLabel());
            metaDialog.setInputReadOnly(inputReadOnly);
            metaDialog.setOutputReadOnly(outputReadOnly);

            if (metaDialog.open() == MetadataDialog.OK) {
                inputMetaCopy = metaDialog.getInputMetaData();
                outputMetaCopy = metaDialog.getOutputMetaData();
                boolean modified = false;
                if (!outputMetaCopy.sameMetadataAs(originaleOutputTable)) {
                    modified = true;
                } else {
                    if (inputMetadata != null) {
                        if (!inputMetaCopy.sameMetadataAs(inputMetadata)) {
                            modified = true;
                        }
                    }
                }

                if (modified) {
                    Node inputNode = null;
                    if (inputConec != null) {
                        inputNode = inputConec.getSource();
                    }
                    return new ChangeMetadataCommand(node, inputNode, inputMetadata, inputMetaCopy,
                            originaleOutputTable, outputMetaCopy);

                }
            }

        } else if (btn.getData(NAME).equals(RESET_COLUMNS)) {
            Node node = (Node) elem;
            IMetadataTable meta = (IMetadataTable) node.getMetadataList().get(0);
            IMetadataTable metaCopy = meta.clone();
            metaCopy.setListColumns(new ArrayList<IMetadataColumn>());
            
            for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                if (connec.isActivate() && connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                    metaCopy = connec.getMetadataTable().clone();
                }
            }


            return new ChangeMetadataCommand(node, meta, metaCopy);
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

        FormData data;
        Control lastControlUsed;

        Point btnSize;

        btn = getWidgetFactory().createButton(subComposite, "", SWT.PUSH); //$NON-NLS-1$
        btnSize = btn.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        btn.setImage(CorePlugin.getImageDescriptor(DOTS_BUTTON).createImage());

        btn.addSelectionListener(listenerSelection);
        btn.setData(NAME, SCHEMA);
        btn.setData(PROPERTY, param.getName());

        lastControlUsed = btn;

        if (elem instanceof Node) {
            Node node = (Node) elem;
            boolean flowMainInput = false;
            for (IConnection connec : node.getIncomingConnections()) {
                if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)) {
                    flowMainInput = true;
                }
            }
            if (flowMainInput) {
                resetBtn = getWidgetFactory().createButton(subComposite, "Sync columns", SWT.PUSH);
                resetBtn.setToolTipText("This will take automatically the columns of the previous component");

                Point resetBtnSize = resetBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT);

                resetBtn.addSelectionListener(listenerSelection);
                data = new FormData();
                data.left = new FormAttachment(btn, 0);
                data.right = new FormAttachment(btn, resetBtnSize.x + ITabbedPropertyConstants.HSPACE, SWT.RIGHT);
                data.top = new FormAttachment(0, top);
                data.height = resetBtnSize.y;
                resetBtn.setLayoutData(data);
                resetBtn.setData(NAME, RESET_COLUMNS);
                resetBtn.setData(PROPERTY, param.getName());
                resetBtn.setEnabled(!param.isReadOnly());

                if (resetBtnSize.y > btnSize.y) {
                    btnSize.y = resetBtnSize.y;
                }

                lastControlUsed = btn;
            }
        }

        CLabel labelLabel = getWidgetFactory().createCLabel(subComposite, "Edit schema");
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
        data.left = new FormAttachment(labelLabel, 0);
        data.right = new FormAttachment(labelLabel, STANDARD_BUTTON_WIDTH, SWT.RIGHT);
        if (resetBtn != null) {
            data.top = new FormAttachment(resetBtn, 0, SWT.CENTER);
        } else {
            data.top = new FormAttachment(0, top);
        }
        data.height = STANDARD_HEIGHT - 2;
        btn.setLayoutData(data);

        // curRowSize = btnSize.y + ITabbedPropertyConstants.VSPACE;
        dynamicTabbedPropertySection.setCurRowSize(btnSize.y + ITabbedPropertyConstants.VSPACE);
        return lastControlUsed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub

    }

    SelectionListener listenerSelection = new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent e) {

        }

        public void widgetSelected(SelectionEvent e) {
            Command cmd = createCommand();
            if (cmd != null) {
                getCommandStack().execute(cmd);
            }
        }

    };

}
