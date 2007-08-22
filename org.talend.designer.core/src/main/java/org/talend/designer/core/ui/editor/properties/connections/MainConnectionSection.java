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
package org.talend.designer.core.ui.editor.properties.connections;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.editor.MetadataTableEditor;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.ui.metadata.dialog.CustomTableManager;
import org.talend.core.ui.metadata.dialog.MetadataDialog;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController;

/**
 * Main Section of the property for the connections. <br/>
 * 
 * $Id$
 * 
 */
public class MainConnectionSection extends DynamicTabbedPropertySection {

    private MetadataTableEditorView metadataTableEditorView;

    private MetadataTableEditor metadataTableEditor;

    public MainConnectionSection() {
        super(EComponentCategory.MAIN);
    }

    @Override
    public void addComponents(boolean forceRedraw) {

        if (conSchema()) {
            disposeChildren();
            curRowSize = 0;

            List<? extends IElementParameter> listParam = ((Connection) elem).getSource().getElementParameters();

            generator.initController(this);
            for (IElementParameter cur : listParam) {
                if ((cur.getField() == EParameterFieldType.SCHEMA_TYPE) && (cur.getContext().equals(((Connection) elem).getConnectorName()))) {
                    AbstractElementPropertySectionController contorller = generator.getController(
                            EParameterFieldType.SCHEMA_TYPE, this);
                    contorller.createControl(composite, cur, 0, 0, 0, null);
                }
            }

            FormData data = new FormData();
            data.left = new FormAttachment(0, ITabbedPropertyConstants.HSPACE);
            data.right = new FormAttachment(100, -ITabbedPropertyConstants.HSPACE);
            data.top = new FormAttachment(0, curRowSize + ITabbedPropertyConstants.VSPACE);
            data.width = 300; // to correct bug of table growing indefinitly

            IMetadataTable outputMetaTable = ((Connection) elem).getMetadataTable();
            if (outputMetaTable != null) {
                // Composite compositeEditorView = new Composite(composite, SWT.BORDER);
                // compositeEditorView.setLayoutData(data);
                
                metadataTableEditor = new MetadataTableEditor(outputMetaTable, "Schema from "
                        + ((Connection) elem).getSource().getElementName() + " output ");
                metadataTableEditorView = new MetadataTableEditorView(composite, SWT.NONE, metadataTableEditor, true,
                        false, true, false);

                MetadataDialog.initializeMetadataTableView(metadataTableEditorView, ((Connection) elem).getSource(),
                        outputMetaTable);
                
                metadataTableEditorView.initGraphicComponents();
                CustomTableManager.addCustomManagementToTable(metadataTableEditorView, true);
                Composite compositeEditorView = metadataTableEditorView.getMainComposite();
                compositeEditorView.setLayoutData(data);
                compositeEditorView.getParent().layout();
            }
            super.addComponents(forceRedraw, false);
            if (forceRedraw) {
                refresh();
            }
        } else if (conIf()) {
            super.addComponents(forceRedraw);
            if (forceRedraw) {
                refresh();
            }
        } else {
            disposeChildren();
        }
    }

    private void disposeChildren() {
        // Empty the composite before use (kinda refresh) :
        Control[] ct = composite.getChildren();
        for (int i = 0; i < ct.length; i++) {
            ct[i].dispose();
        }
    }

    private boolean conIf() {
        Connection connection = (Connection) elem;
        return connection.getLineStyle() == EConnectionType.RUN_IF;
    }

    private boolean conSchema() {
        Connection connection = (Connection) elem;
        return connection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA);
    }
}
