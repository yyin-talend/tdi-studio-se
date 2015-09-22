// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dataprep.component.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.ui.properties.tab.IDynamicProperty;
import org.talend.designer.core.ui.editor.cmd.ChangeMetadataCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.controllers.AbstractButtonController;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

/**
 * created by wchen on 2015年9月17日 Detailled comment
 *
 */
public class FetchSchemaController extends AbstractButtonController {

    /**
     * DOC wchen FetchSchemaController constructor comment.
     * 
     * @param dp
     */
    public FetchSchemaController(IDynamicProperty dp) {
        super(dp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.core.ui.editor.properties.controllers.AbstractButtonController#createButton(org.talend.core
     * .model.process.IElementParameter, org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Button createButton(IElementParameter param, Composite subComposite) {
        // subComposite.setBackground(subComposite.getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
        Button btnCmd = new Button(subComposite, SWT.NONE);
        btnCmd.setText("Fetch Schema.....");
        return btnCmd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractButtonController#createButtonCommand()
     */
    @Override
    protected void executeButtonCommand(Button button) {
        if (elem instanceof Node) {

            boolean doit = MessageDialog.openConfirm(button.getShell(), "Confirm changes ?",
                    "Fetch schema will overwrite all current schema. Continue anyway ?");
            if (doit) {
                final Node node = (Node) elem;
                final IElementParameter urlParam = node.getElementParameter("URL"); //$NON-NLS-1$
                final IElementParameter preparationId = node.getElementParameter("PREPARATION_ID"); //$NON-NLS-1$
                if (urlParam != null && preparationId != null) {
                    ProgressMonitorDialog dialog = new ProgressMonitorDialog(button.getShell());
                    IRunnableWithProgress runnable = new IRunnableWithProgress() {

                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            try {
                                IElementParameter schemaType = node.getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE);
                                if (schemaType != null) {
                                    IMetadataTable metadataTable = null;
                                    if (!node.getMetadataList().isEmpty()) {
                                        metadataTable = node.getMetadataList().get(0);
                                    } else {
                                        metadataTable = new MetadataTable();
                                        node.getMetadataList().add(metadataTable);
                                    }
                                    IMetadataTable newMetadata = metadataTable.clone();
                                    newMetadata.getListColumns().clear();
                                    Resty resty = new Resty();
                                    JSONResource resource = resty.json(TalendTextUtils.removeQuotes(urlParam.getValue()
                                            .toString())
                                            + ":8888/api/preparations/"
                                            + TalendTextUtils.removeQuotes(preparationId.getValue().toString()) + "/content");
                                    if (resource != null) {
                                        final JSONArray jsonArray = resource.object().getJSONArray("columns");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String name = jsonObject.getString("name");
                                            String prepColumnId = jsonObject.getString("id");
                                            // String typeName = jsonObject.getString("type");
                                            IMetadataColumn column = new MetadataColumn();
                                            column.setLabel(name);
                                            column.setOriginalDbColumnName(prepColumnId);
                                            column.setType("String");
                                            column.setTalendType("id_String");
                                            newMetadata.getListColumns().add(column);
                                        }

                                        ChangeMetadataCommand command = new ChangeMetadataCommand(node, schemaType,
                                                metadataTable, newMetadata);
                                        executeCommand(command);

                                    }
                                }
                            } catch (IOException e) {
                                ExceptionHandler.process(e);
                            } catch (JSONException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    };

                    try {
                        dialog.run(false, true, runnable);
                    } catch (InvocationTargetException e) {
                        ExceptionHandler.process(e);
                    } catch (InterruptedException e) {
                        ExceptionHandler.process(e);
                    }

                } else {
                    throw new RuntimeException("The dataprep component url parameter or id is not set!");
                }
            }

        }

    }
}
