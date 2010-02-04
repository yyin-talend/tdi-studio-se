// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.hl7.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.designer.hl7.managers.HL7Manager;
import org.talend.designer.hl7.model.IModel;
import org.talend.designer.hl7.model.SegmentModel;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class HL7MultiSchemaUI extends HL7UI {

    private ComboViewer metaTableViewer;

    private Label schemaLabel;

    private static String LABEL_NAME = "Segment(As Schema)"; //$NON-NLS-N$

    public HL7MultiSchemaUI(Composite parent, HL7Manager hl7Manager) {
        super(parent, hl7Manager);
    }

    @Override
    protected void createCombo(Composite parent) {
        createLabel(parent);
        metaTableViewer = new ComboViewer(parent, SWT.NONE);
        metaTableViewer.setContentProvider(new ArrayContentProvider());
        metaTableViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                if (element instanceof SegmentModel) {
                    return ((SegmentModel) element).getSeg().getName();
                }
                return super.getText(element);
            }

        });

        final Combo combo = metaTableViewer.getCombo();
        GridData data = new GridData();
        data.widthHint = 60;
        combo.setLayoutData(data);
        combo.addSelectionListener(new SelectionAdapter() {

            @SuppressWarnings("unchecked")
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection selection = (IStructuredSelection) metaTableViewer.getSelection();
                Object selectedObj = selection.getFirstElement();
                String key = ((IModel) selectedObj).getDisplayName();
                Map m = hl7Manager.getSchemaRelationMap();
                List<MetadataColumn> beans = (List<MetadataColumn>) m.get(key);
                hl7SchemaEditorView.getMetadataEditor().removeAll();
                hl7SchemaEditorView.getMetadataEditor().addAll(beans);
                linker.removeAllLinks();
                linker.getMainui().redrawLinkers();
                linker.getBackgroundRefresher().refreshBackground();
            }
        });

    }

    public void initSchemaCombo() {
        List<SegmentModel> segments = this.contentProvider.getAllSegmentsForMessage();
        metaTableViewer.setInput(segments);
        initMappingMap(segments);
        final Combo combo = metaTableViewer.getCombo();
        combo.select(0);
    }

    private void initMappingMap(List<SegmentModel> segments) {
        for (SegmentModel sm : segments) {
            List<MetadataColumn> list = new ArrayList<MetadataColumn>();
            hl7Manager.getSchemaRelationMap().put(sm.getDisplayName(), list);
        }
    }

    private void createLabel(Composite parent) {
        schemaLabel = new Label(parent, SWT.NONE);
        schemaLabel.setText(LABEL_NAME);
    }

    public ComboViewer getMetaTableViewer() {
        return this.metaTableViewer;
    }

}
