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
package org.talend.designer.core.ui.editor.properties.connections;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.editor.MetadataTableEditor;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.connections.ConnLabelEditPart;
import org.talend.designer.core.ui.editor.connections.ConnectionPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * Main Section of the property for the connections. <br/>
 * 
 * $Id$
 * 
 */
public class MainConnectionSection extends DynamicTabbedPropertySection {

    private Composite parent;

    private MetadataTableEditorView metadataTableEditorView;

    private MetadataTableEditor metadataTableEditor;

    private boolean built = false;

    public MainConnectionSection() {
        super(EComponentCategory.MAIN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection#refresh()
     */
    @Override
    public void refresh() {
        if (!built) {
            addComponents();
        }
        IMetadataTable outputMetaTable = ((Node) elem).getMetadataList().get(0);
        metadataTableEditor.setMetadataTable(outputMetaTable);
        metadataTableEditorView.setMetadataTableEditor(metadataTableEditor);
        metadataTableEditorView.getTableViewerCreator().getTableViewer().refresh();

        composite.pack();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection#setInput(org.eclipse.ui.IWorkbenchPart,
     * org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void setInput(IWorkbenchPart workbenchPart, ISelection selection) {
        if (!(selection instanceof IStructuredSelection)) {
            return;
        }

        if (workbenchPart instanceof MultiPageTalendEditor) {
            part = (MultiPageTalendEditor) workbenchPart;
        } else {
            part = (MultiPageTalendEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        }

        Object input = ((IStructuredSelection) selection).getFirstElement();
        if (input instanceof ConnectionPart) {
            elem = (Element) ((ConnectionPart) input).getSource().getModel();
        }
        if (input instanceof ConnLabelEditPart) {
            elem = (Element) ((ConnectionPart) ((ConnLabelEditPart) input).getParent()).getSource().getModel();
        }
    }

    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        this.parent = parent;
    }

    @Override
    public void addComponents() {
        List<? extends IElementParameter> listParam = elem.getElementParameters();

        for (IElementParameter cur : listParam) {
            if (cur.getField() == EParameterFieldType.SCHEMA_TYPE) {
                addSchemaType(parent, cur, 0, 0, 0, null);
            }
        }

        FormData data = new FormData();

        Composite container = new Composite(composite, SWT.BORDER);
        container.setLayout(new FillLayout());
        container.setBackground(new Color(null, 181, 220, 17));
        data = new FormData();
        data.left = new FormAttachment(0, ITabbedPropertyConstants.HSPACE);
        // TODO SML Put 100 instead of 95 when parent composite bug is resolved
        data.right = new FormAttachment(95, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(0, curRowSize + ITabbedPropertyConstants.VSPACE);
        container.setLayoutData(data);

        IMetadataTable outputMetaTable = ((Node) elem).getMetadataList().get(0);
        metadataTableEditor = new MetadataTableEditor(outputMetaTable, "Schema from " + outputMetaTable.getTableName()
                + " output ");
        metadataTableEditorView = new MetadataTableEditorView(container, SWT.NONE, metadataTableEditor, false);
        metadataTableEditorView.setReadOnly(true);

        composite.pack();

        built = true;
    }

}
