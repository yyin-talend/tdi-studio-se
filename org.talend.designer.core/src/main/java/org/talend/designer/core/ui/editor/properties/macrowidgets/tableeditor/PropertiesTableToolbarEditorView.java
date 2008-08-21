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
package org.talend.designer.core.ui.editor.properties.macrowidgets.tableeditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddAllPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddAllPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ExportPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.ImportPushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.RemovePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.RemovePushButtonForExtendedTable;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.Element;
import org.talend.designer.core.ui.editor.cmd.PropertyTablePasteCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.controllers.TableController;

/**
 * $Id$
 * 
 */
public class PropertiesTableToolbarEditorView extends ExtendedToolbarView {

    private PropertiesTableEditorModel model;

    /**
     * DOC amaumont MetadataToolbarEditorView constructor comment.
     * 
     * @param parent
     * @param style
     * @param extendedTableViewer
     */
    public PropertiesTableToolbarEditorView(Composite parent, int style, AbstractExtendedTableViewer extendedTableViewer,
            PropertiesTableEditorModel model) {
        super(parent, style, extendedTableViewer);
        this.model = model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createComponents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createComponents(Composite parent) {
        super.createComponents(parent);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createAddPushButton()
     */
    @Override
    protected AddPushButton createAddPushButton() {
        return new AddPushButtonForExtendedTable(this.toolbar, getExtendedTableViewer()) {

            @Override
            public boolean getEnabledState() {
                return super.getEnabledState() && (model == null || !model.getElemParameter().isBasedOnSubjobStarts());
            }

            @Override
            protected Object getObjectToAdd() {
                PropertiesTableEditorModel tableEditorModel = (PropertiesTableEditorModel) getExtendedTableViewer()
                        .getExtendedControlModel();
                return tableEditorModel.createNewEntry();
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView#createAddAllPushButton()
     */
    @Override
    protected AddAllPushButton createAddAllPushButton() {

        final PropertiesTableEditorModel tableEditorModel = (PropertiesTableEditorModel) getExtendedTableViewer()
                .getExtendedControlModel();

        if (!TableController.isNeedAddAllButton(tableEditorModel.getElemParameter())) {
            return null;
        }

        return new AddAllPushButtonForExtendedTable(this.toolbar, getExtendedTableViewer()) {

            @Override
            public boolean getEnabledState() {
                return super.getEnabledState() && (model == null || !model.getElemParameter().isBasedOnSubjobStarts());
            }

            @Override
            protected List<Object> getObjectToAdd() {

                Element element = tableEditorModel.getElement();
                if (element != null && element instanceof Node) {
                    Node node = (Node) element;
                    if (node.getMetadataList() != null && !node.getMetadataList().isEmpty()) {
                        IMetadataTable metadata = node.getMetadataList().get(0);
                        if (metadata.getListColumns() != null && !metadata.getListColumns().isEmpty()) {

                            List<Object> objects = new ArrayList<Object>();
                            for (IMetadataColumn column : metadata.getListColumns()) {

                                Object entry = tableEditorModel.createNewEntry();
                                if (!(entry instanceof Map)) {
                                    continue;
                                }

                                Map mapObject = (Map) entry;
                                if (mapObject.containsKey("COLUMN")) {
                                    mapObject.put("COLUMN", column.getLabel());
                                }
                                if (mapObject.containsKey("SIZE")) {
                                    if (column.getLength() != null) {
                                        mapObject.put("SIZE", Integer.toString(column.getLength()));
                                    }
                                }
                                objects.add(entry);
                            }
                            return objects;
                        }
                    }
                }
                return Collections.EMPTY_LIST;
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastButton()
     */
    @Override
    public PastePushButton createPastePushButton() {
        return new PastePushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            public boolean getEnabledState() {
                return super.getEnabledState() && (model == null || !model.getElemParameter().isBasedOnSubjobStarts());
            }

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, Integer indexWhereInsert) {
                return new PropertyTablePasteCommand<Map<String, Object>>(extendedTableModel, indexWhereInsert);
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createExportPushButton()
     */
    @Override
    protected ExportPushButton createExportPushButton() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastButton()
     */
    @Override
    public ImportPushButton createImportPushButton() {
        return null;
    }

    @Override
    protected RemovePushButton createRemovePushButton() {
        return new RemovePushButtonForExtendedTable(this.toolbar, getExtendedTableViewer()) {

            @Override
            public boolean getEnabledState() {
                return super.getEnabledState() && (model == null || !model.getElemParameter().isBasedOnSubjobStarts());
            }
        };
    }
}
