// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.expressionbuilder.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.FunctionManagerExt;
import org.talend.expressionbuilder.i18n.Messages;
import org.talend.expressionbuilder.model.Category;
import org.talend.expressionbuilder.model.CategoryManager;
import org.talend.expressionbuilder.test.shadow.Variable;
import org.talend.expressionbuilder.test.shadow.VirtualMetadataColumn;
import org.talend.expressionbuilder.ui.proposal.ExpressionContentProposal;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: CategoryComposite.java 上午10:10:58 2007-7-24 +0000 (2007-7-24) yzhang $
 * 
 */
public class CategoryComposite extends Composite {

    private final List categoryList;

    private final List functionList;

    final ListViewer functionViewer;

    CategoryManager manager = null;

    private static Function selectedFunction = new Function();

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

        final SashForm sashForm1 = new SashForm(sashForm, SWT.HORIZONTAL);

        final Composite composite = new Composite(sashForm1, SWT.BORDER);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        composite.setLayout(gridLayout);

        final CLabel categoryLabel = new CLabel(composite, SWT.SHADOW_OUT);
        categoryLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        categoryLabel.setText(Messages.getString("CategoryComposite.category")); //$NON-NLS-1$

        final ListViewer categoryViewer = new ListViewer(composite, SWT.V_SCROLL | SWT.H_SCROLL);
        categoryViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                return ((Category) element).getName();
            }
        });
        categoryViewer.setContentProvider(new ArrayContentProvider());
        categoryList = categoryViewer.getList();
        categoryList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Composite composite1 = new Composite(sashForm1, SWT.BORDER);
        final GridLayout gridLayout1 = new GridLayout();
        gridLayout1.verticalSpacing = 0;
        gridLayout1.horizontalSpacing = 0;
        gridLayout1.marginWidth = 0;
        gridLayout1.marginHeight = 0;
        composite1.setLayout(gridLayout1);

        final CLabel functionLabel = new CLabel(composite1, SWT.SHADOW_OUT);
        functionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        functionLabel.setText(Messages.getString("CategoryComposite.functions")); //$NON-NLS-1$

        functionViewer = new ListViewer(composite1, SWT.V_SCROLL | SWT.H_SCROLL);
        functionViewer.setContentProvider(new ArrayContentProvider());
        functionViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                return ((Function) element).getName();
            }
        });
        functionList = functionViewer.getList();
        functionList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        sashForm1.setWeights(new int[] { 1, 1 });

        Composite docComposite = new Composite(sashForm, SWT.BORDER);
        GridLayout docLayout = new GridLayout();
        docLayout.marginTop = 0;
        docLayout.marginBottom = 0;
        docLayout.marginHeight = 0;
        docLayout.marginLeft = 0;
        docLayout.marginWidth = 0;
        docLayout.marginHeight = 0;
        docComposite.setLayout(docLayout);

        Label docLabel = new Label(docComposite, SWT.NONE);
        docLabel.setText(Messages.getString("CategoryComposite.Help")); //$NON-NLS-1$

        final Browser docDisplayer = new Browser(docComposite, SWT.BORDER);
        docDisplayer.setText(Messages.getString("CategoryComposite.SelectCategoryAndFunction")); //$NON-NLS-1$
        docDisplayer.setLayoutData(new GridData(GridData.FILL_BOTH));
        sashForm.setWeights(new int[] { 2, 1 });

        new UIRelationShipLinker(categoryViewer, functionViewer, docDisplayer);
        initializeData(categoryViewer);
    }

    /**
     * bqian Comment method "initializeData".
     * 
     * @param categoryViewer
     */
    private void initializeData(ListViewer categoryViewer) {
        categoryViewer.setInput(manager.getInputCategory());
//        if (manager.getInputCategory().size() > 0) {
//            categoryViewer.getList().select(0);
//            java.util.List<Function> functions = manager.getInputCategory().get(0).getFunctions();
//            if (functions != null && functions.size() > 0) {
//                functionViewer.setInput(functions);
//                functionList.select(0);
//            }
//        }
    }

    /**
     * yzhang CategoryComposite class global comment. Detailled comment <br/>
     * 
     * $Id: CategoryComposite.java 上午10:11:21 2007-7-24 +0000 (2007-7-24) yzhang $
     * 
     */
    class UIRelationShipLinker {

        UIRelationShipLinker(ListViewer categoryViewer, final ListViewer functionViewer, final Browser docDisplayer) {
            categoryViewer.addSelectionChangedListener(new ISelectionChangedListener() {

                public void selectionChanged(SelectionChangedEvent event) {

                    try {
                        Category category = (Category) ((IStructuredSelection) event.getSelection()).getFirstElement();
                        if (category == null) {
                            return;
                        }
                        functionViewer.setInput(category.getFunctions());
                        if (category.getFunctions().isEmpty()) {
                            docDisplayer.setText(""); //$NON-NLS-1$
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
                    Function function = (Function) ((IStructuredSelection) event.getSelection()).getFirstElement();
                    if (function != null && function.getDescription() != null) {
                        docDisplayer.setText(function.getDescription());
                    } else {
                        docDisplayer.setText(""); //$NON-NLS-1$
                    }
                }

            });

            functionViewer.addDoubleClickListener(new IDoubleClickListener() {

                public void doubleClick(DoubleClickEvent event) {

                    try {
                        Function function = (Function) ((IStructuredSelection) event.getSelection()).getFirstElement();
                        selectedFunction = function;
                        if (function != null && function.getDescription() != null) {
                            docDisplayer.setText(function.getDescription());
                            VirtualMetadataColumn column = new VirtualMetadataColumn();
                            column.setTalendType(function.getTalendType().getName());
                            column.setFunction(function);

                            ExpressionComposite expressionComposite = ExpressionBuilderDialog.getExpressionComposite();
                            expressionComposite.setExpression(FunctionManagerExt.getOneColData(column, false), true);

                        } else {
                            docDisplayer.setText(""); //$NON-NLS-1$
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    /**
     * yzhang Comment method "getProposals".
     * 
     * @return
     */
    public IContentProposal[] getProposals(String categoryFunction, int position) {

        String category = null;
        String function = null;

        boolean displayFunction = false;
        if (categoryFunction.indexOf(".") != -1) { //$NON-NLS-1$
            String[] cf = categoryFunction.split("\\."); //$NON-NLS-1$
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < cf.length - 1; i++) {
                buffer.append(cf[i]);
                if (i != cf.length - 2) {
                    buffer.append("."); //$NON-NLS-1$
                }
            }
            if (cf.length == 1) {
                category = cf[cf.length - 1];
            } else {
                function = cf[cf.length - 1];
                category = buffer.toString();
            }

            displayFunction = true;

        } else {
            category = categoryFunction;
        }

        java.util.List<IContentProposal> proposals = new LinkedList<IContentProposal>();
        java.util.List<Category> categories = manager.getInputCategory();

        boolean addAllCategory = category.equals("*C") ? true : false; //$NON-NLS-1$

        if (!displayFunction) {
            for (Category cg : categories) {
                if (!cg.getName().startsWith("*") && (addAllCategory || cg.getName().startsWith(category))) { //$NON-NLS-1$
                    proposals.add(new ExpressionContentProposal(cg.getName(), "", position)); //$NON-NLS-1$
                }
            }

            java.util.List<Variable> vars = ExpressionBuilderDialog.getTestComposite().getVariableList();
            for (Variable var : vars) {
                if (addAllCategory || var.getName().startsWith(category)) {
                    proposals.add(new ExpressionContentProposal(var.getName(), var.getValue(), position));
                }
            }

        } else {
            for (Category cg : categories) {
                if (cg.getName().equals(category)) {
                    java.util.List<Function> funs = cg.getFunctions();
                    boolean addAll = (function == null ? true : false);

                    for (Function fun : funs) {
                        if (addAll || fun.getName().startsWith(function)) {
                            proposals.add(new ExpressionContentProposal(fun.getName() + "()", fun.getDescription(), //$NON-NLS-1$
                                    position));
                        }
                    }
                }
            }
        }

        Collections.sort(proposals, new CategoryFunctionCompartor());

        String replaceString;
        if (displayFunction) {
            replaceString = function == null ? "" : function; //$NON-NLS-1$
        } else {
            replaceString = category;
        }
        ExpressionBuilderDialog.getExpressionComposite().setReplacedText(replaceString);

        return proposals.toArray(new IContentProposal[proposals.size()]);
    }

    /**
     * yzhang CategoryComposite class global comment. Detailled comment <br/>
     * 
     * $Id: CategoryComposite.java 下午01:57:54 2007-7-30 +0000 (2007-7-30) yzhang $
     * 
     */
    class CategoryFunctionCompartor implements Comparator<IContentProposal> {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(IContentProposal o1, IContentProposal o2) {
            return o1.getContent().compareToIgnoreCase(o2.getContent());
        }
    }
}
