package org.talend.expressionbuilder.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.expressionbuilder.model.Category;
import org.talend.expressionbuilder.model.CategoryManager;

public class CategoryComposite extends Composite {

    private List categoryList;

    private List functionList;

    CategoryManager manager = null;

    ExpressionController expressController = null;

    private static Function selectedFunction;

    /**
     * Getter for selectedFunction.
     * 
     * @return the selectedFunction
     */
    public static Function getSelectedFunction() {
        return selectedFunction;
    }

    /**
     * Create the composite
     * 
     * @param parent
     * @param style
     */
    public CategoryComposite(Composite parent, int style, CategoryManager manager) {
        super(parent, style);
        this.manager = manager;

        setLayout(new FillLayout());

        final SashForm sashForm = new SashForm(this, SWT.NONE);

        final SashForm sashForm_1 = new SashForm(sashForm, SWT.HORIZONTAL);

        final Composite composite = new Composite(sashForm_1, SWT.BORDER);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        composite.setLayout(gridLayout);

        final CLabel categoryLabel = new CLabel(composite, SWT.SHADOW_OUT);
        categoryLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        categoryLabel.setText("Category");

        final ListViewer categoryViewer = new ListViewer(composite, SWT.V_SCROLL | SWT.H_SCROLL);
        categoryViewer.setLabelProvider(new LabelProvider() {

            public String getText(Object element) {
                return ((Category) element).getName();
            }
        });
        categoryViewer.setContentProvider(new ArrayContentProvider());
        categoryList = categoryViewer.getList();
        categoryList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Composite composite_1 = new Composite(sashForm_1, SWT.BORDER);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.verticalSpacing = 0;
        gridLayout_1.horizontalSpacing = 0;
        gridLayout_1.marginWidth = 0;
        gridLayout_1.marginHeight = 0;
        composite_1.setLayout(gridLayout_1);

        final CLabel functionLabel = new CLabel(composite_1, SWT.SHADOW_OUT);
        functionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        functionLabel.setText("Functions");

        final ListViewer functionViewer = new ListViewer(composite_1, SWT.V_SCROLL | SWT.H_SCROLL);
        functionViewer.setContentProvider(new ArrayContentProvider());
        functionViewer.setLabelProvider(new LabelProvider() {

            public String getText(Object element) {
                return ((Function) element).getName();
            }
        });
        functionList = functionViewer.getList();
        functionList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        sashForm_1.setWeights(new int[] { 1, 1 });

        final Browser docDisplayer = new Browser(sashForm, SWT.BORDER);
        sashForm.setWeights(new int[] { 1, 1 });

        new UIRelationShipLinker(categoryViewer, functionViewer, docDisplayer);
        initializeData(categoryViewer);
    }

    /**
     * DOC bqian Comment method "initializeData".
     * 
     * @param categoryViewer
     */
    private void initializeData(ListViewer categoryViewer) {
        categoryViewer.setInput(manager.getInputCategory());
    }

    /**
     * Getter for expressController.
     * 
     * @return the expressController
     */
    public ExpressionController getExpressController() {
        return this.expressController;
    }

    /**
     * Sets the expressController.
     * 
     * @param expressController the expressController to set
     */
    public void setExpressController(ExpressionController expressController) {
        this.expressController = expressController;
    }

    class UIRelationShipLinker {

        UIRelationShipLinker(ListViewer categoryViewer, final ListViewer functionViewer, final Browser docDisplayer) {
            categoryViewer.addSelectionChangedListener(new ISelectionChangedListener() {

                public void selectionChanged(SelectionChangedEvent event) {

                    try {
                        Category category = (Category) ((IStructuredSelection) event.getSelection()).getFirstElement();
                        functionViewer.setInput(category.getFunctions());
                        if (category.getFunctions().isEmpty()) {
                            docDisplayer.setText("");
                            return;
                        }
                        functionViewer.setSelection(new StructuredSelection(category.getFunctions().get(0)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            functionViewer.addSelectionChangedListener(new ISelectionChangedListener() {

                public void selectionChanged(SelectionChangedEvent event) {
                    try {
                        Function function = (Function) ((IStructuredSelection) event.getSelection()).getFirstElement();
                        selectedFunction = function;
                        if (function != null && function.getDescription() != null) {
                            docDisplayer.setText(function.getDescription());
                            expressController.setExpression(function);
                        } else {
                            docDisplayer.setText("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
