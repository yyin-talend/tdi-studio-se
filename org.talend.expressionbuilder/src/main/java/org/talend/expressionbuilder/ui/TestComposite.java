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
package org.talend.expressionbuilder.ui;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.talend.expressionbuilder.test.JavaTestShadow;
import org.talend.expressionbuilder.test.Variable;
import org.talend.expressionbuilder.test.shadow.ExpressionTestMain;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: TestComposite.java 上午10:14:03 2007-7-24 +0000 (2007-7-24) yzhang $
 * 
 */
public class TestComposite extends Composite {

    private Table table;

    private Button buttonAddVar, buttonRemoveVar;

    private final TableViewer tableViewer;

    private final Button testButton;

    private final Button clearButton;

    private final Text testResultText;

    private static final String NAME_PROPERTY = "Name";

    private static final String VALUE_PROPERTY = "Value";

    private JavaTestShadow shadow;

    /**
     * Create the composite
     * 
     * @param parent
     * @param style
     */
    public TestComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new FillLayout());

        final Group group = new Group(this, SWT.NONE);
        group.setLayout(new GridLayout());
        group.setText("Test");

        final Composite composite = new Composite(group, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        final FillLayout fillLayout = new FillLayout();
        fillLayout.spacing = 8;
        composite.setLayout(fillLayout);

        testButton = new Button(composite, SWT.NONE);
        testButton.setText("Test!");

        clearButton = new Button(composite, SWT.NONE);
        clearButton.setText("Clear");

        final SashForm sashForm = new SashForm(group, SWT.NONE);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Composite tablePart = new Composite(sashForm, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.verticalSpacing = 0;
        layout.marginTop = 0;
        layout.marginHeight = 0;
        tablePart.setLayout(layout);

        tableViewer = new TableViewer(tablePart, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        VariableTableViewerProvider provider = new VariableTableViewerProvider();
        tableViewer.setContentProvider(provider);
        tableViewer.setLabelProvider(provider);
        tableViewer.setCellModifier(new ICellModifier() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
             */
            public boolean canModify(Object element, String property) {
                return true;
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
             */
            public Object getValue(Object element, String property) {
                if (NAME_PROPERTY.equals(property)) {
                    return ((Variable) element).getName();
                } else if (VALUE_PROPERTY.equals(property)) {
                    return ((Variable) element).getValue();
                }
                return "";
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
             */
            public void modify(Object element, String property, Object value) {
                Variable var = (Variable) ((TableItem) element).getData();
                if (NAME_PROPERTY.equals(property)) {
                    var.setName((String) value);
                } else if (VALUE_PROPERTY.equals(property)) {
                    var.setValue((String) value);
                }
                tableViewer.refresh();
            }

        });
        table = tableViewer.getTable();
        tableViewer.setCellEditors(new CellEditor[] { new TextCellEditor(table), new TextCellEditor(table) });
        tableViewer.setColumnProperties(new String[] { NAME_PROPERTY, VALUE_PROPERTY });
        tableViewer.setInput(new LinkedList<Variable>());

        table.setHeaderVisible(true);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        table.setLayoutData(gd);

        final TableColumn varColumn = new TableColumn(table, SWT.NONE);
        varColumn.setWidth(100);
        varColumn.setText("Var");

        final TableColumn valueColumn = new TableColumn(table, SWT.NONE);
        valueColumn.setWidth(100);
        valueColumn.setText("Value");

        testResultText = new Text(sashForm, SWT.MULTI | SWT.BORDER | SWT.WRAP);

        Composite buttonPart = new Composite(tablePart, SWT.NONE);
        buttonPart.setLayout(new GridLayout(2, false));
        buttonPart.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

        buttonAddVar = new Button(buttonPart, SWT.NONE);
        buttonAddVar.setText("Add");
        buttonAddVar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

        buttonRemoveVar = new Button(buttonPart, SWT.NONE);
        buttonRemoveVar.setText("Remove");
        buttonRemoveVar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        sashForm.setWeights(new int[] { 1, 1 });

        shadow = new JavaTestShadow();

        installListener();
        //
    }

    private void installListener() {
        buttonAddVar.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                List<Variable> input = (List<Variable>) tableViewer.getInput();
                input.add(new Variable(input.size()));
                tableViewer.refresh();
            }
        });

        buttonRemoveVar.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                ISelection selection = tableViewer.getSelection();
                List<Variable> list = ((List<Variable>) tableViewer.getInput());

                if (!selection.isEmpty() && selection instanceof StructuredSelection) {
                    Object[] vars = ((StructuredSelection) selection).toArray();
                    for (Object var : vars) {
                        list.remove((Variable) var);
                        tableViewer.refresh();
                    }
                } else if (!list.isEmpty()) {
                    list.remove(list.size() - 1);
                    tableViewer.refresh();
                }
            }
        });

        testButton.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {

                // shadow.process(testResultText, tableViewer);
                new ExpressionTestMain(CategoryComposite.getSelectedFunction(), testResultText);

            }
        });

        clearButton.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                testResultText.setText("");
            }
        });

    }

    public void dispose() {
        super.dispose();
    }

    /**
     * Stop the server socket.
     * 
     * yzhang Comment method "stopServerThread".
     */
    public void stopServerThread() {
        shadow.stop();
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * yzhang Comment method "getVariableList".
     */
    public List<Variable> getVariableList() {
        return (List<Variable>) tableViewer.getInput();
    }

}
