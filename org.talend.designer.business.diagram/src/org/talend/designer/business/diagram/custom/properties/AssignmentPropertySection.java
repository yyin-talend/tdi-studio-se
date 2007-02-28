// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
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
package org.talend.designer.business.diagram.custom.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractModelerPropertySection;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.designer.business.diagram.custom.commands.UnassignTalendItemsFromBusinessAssignmentCommand;
import org.talend.designer.business.diagram.custom.util.EmfPropertyHelper;
import org.talend.designer.business.diagram.custom.util.KeyHelper;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.designer.business.model.business.BusinessAssignment;
import org.talend.designer.business.model.business.BusinessFactory;
import org.talend.designer.business.model.business.BusinessPackage;
import org.talend.designer.business.model.business.provider.BusinessItemProviderAdapterFactory;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class AssignmentPropertySection extends AbstractModelerPropertySection {

    private Composite composite;

    private BusinessItemProviderAdapterFactory adapterFactory;

    private TableViewer tableViewer;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractModelerPropertySection#createControls(org.eclipse.swt.widgets.Composite,
     * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
     */
    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);

        composite = getWidgetFactory().createFlatFormComposite(parent);

        // PTODO mhelleboid externalize tableviewer creation

        adapterFactory = new BusinessItemProviderAdapterFactory();

        tableViewer = new TableViewer(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        tableViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
        tableViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));

        Table table = tableViewer.getTable();
        TableLayout tableLayout = new TableLayout();
        table.setLayout(tableLayout);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        final String[] columnProperties = new String[] {
                Messages.getString("AssignmentPropertySection.Type"), Messages.getString("AssignmentPropertySection.Name"), Messages.getString("AssignmentPropertySection.Comment") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        TableColumn column1 = new TableColumn(table, SWT.NONE);
        tableLayout.addColumnData(new ColumnPixelData(125, true));
        column1.setText(columnProperties[0]);

        TableColumn column2 = new TableColumn(table, SWT.NONE);
        tableLayout.addColumnData(new ColumnPixelData(125, true));
        column2.setText(columnProperties[1]);

        TableColumn column3 = new TableColumn(table, SWT.NONE);
        tableLayout.addColumnData(new ColumnWeightData(1, 150, true));
        column3.setText(columnProperties[2]);

        tableViewer.setColumnProperties(columnProperties);

        final IItemPropertyDescriptor itemPropertyDescriptor = getItemPropertyDescriptor();

        tableViewer.setCellModifier(new ICellModifier() {

            public boolean canModify(Object element, String property) {
                return property.equals(columnProperties[2]);
            }

            public Object getValue(Object element, String property) {
                return EmfPropertyHelper.getValue(itemPropertyDescriptor, element);
            }

            public void modify(Object element, String property, Object value) {
                if (element instanceof TableItem) {
                    TableItem tableItem = (TableItem) element;
                    itemPropertyDescriptor.setPropertyValue(tableItem.getData(), value);
                }
            }

        });

        CellEditor[] cellEditors = new CellEditor[3];
        cellEditors[2] = new TextCellEditor(table);
        tableViewer.setCellEditors(cellEditors);

        createKeyListener(table);

        handleLayout(parent, table, column1, column2, column3);
    }

    private void handleLayout(Composite parent, Table table, TableColumn column1, TableColumn column2,
            TableColumn column3) {
        Object layoutData = parent.getLayoutData();
        if (layoutData instanceof GridData) {
            GridData gridData = (GridData) layoutData;
            gridData.grabExcessVerticalSpace = true;
            gridData.verticalAlignment = SWT.FILL;
        }

        FormData formData = new FormData();
        formData.left = new FormAttachment(0);
        formData.top = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        formData.bottom = new FormAttachment(100);
        table.setLayoutData(formData);
    }

    private IItemPropertyDescriptor getItemPropertyDescriptor() {
        // PTODO mhelleboid find another way to itempropertysource without an eobject
        BusinessAssignment sampleBusinessAssignment = BusinessFactory.eINSTANCE.createBusinessAssignment();
        EStructuralFeature businessAssignment_Comment = BusinessPackage.eINSTANCE.getBusinessAssignment_Comment();
        IItemPropertySource itemPropertySource = EmfPropertyHelper.getItemPropertySource(adapterFactory,
                sampleBusinessAssignment);
        return EmfPropertyHelper.getItemPropertyDescriptor(itemPropertySource, sampleBusinessAssignment,
                businessAssignment_Comment);
    }

    /**
     * DOC mhelleboid Comment method "createKeyListener".
     * 
     * @param table
     */
    private void createKeyListener(Table table) {
        table.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent event) {
                ISelection selection = tableViewer.getSelection();
                if (selection instanceof IStructuredSelection) {
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;

                    if (event.keyCode == SWT.DEL) {
                        executeDeleteCommand(structuredSelection);
                    } else if (new KeyHelper().anyModifierPressed(event.keyCode)) {
                        // do nothing for Ctrl + Y / Ctrl +Z
                    } else if (new KeyHelper().cursorPressed(event.keyCode)) {
                        // do nothing for UP / DOWN
                    } else {
                        // Edit third column
                        tableViewer.editElement(structuredSelection.getFirstElement(), 2);
                    }
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.properties.sections.AbstractModelerPropertySection#setInput(org.eclipse.ui.IWorkbenchPart,
     * org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        tableViewer.setInput(getEObject());
    }

    private void executeDeleteCommand(IStructuredSelection structuredSelection) {
        UnassignTalendItemsFromBusinessAssignmentCommand command = new UnassignTalendItemsFromBusinessAssignmentCommand(
                getEditingDomain(), true);
        for (Iterator iter = structuredSelection.iterator(); iter.hasNext();) {
            Object object = (Object) iter.next();
            if (object instanceof BusinessAssignment) {
                BusinessAssignment businessAssignment = (BusinessAssignment) object;
                command.addBusinessAssignment(businessAssignment);
            }
        }

        List commands = new ArrayList();
        commands.add(command);

        executeAsCompositeCommand(Messages.getString("AssignmentPropertySection.DeleteAssignment"), commands); //$NON-NLS-1$
    }
}
