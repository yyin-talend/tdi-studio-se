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
package org.talend.designer.runprocess.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextListener;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess;
import org.talend.designer.runprocess.i18n.Messages;

/**
 * Manages the context of a process. <br/>
 * 
 * $Id$
 * 
 */
public class ProcessContextComposite extends Composite {

    /**
     * 
     */
    private static final int COLUMN_WIDTH = 120;

    /**
     * 
     */
    private static final int HINT_HEIGHT = 250;

    /** Context combo viewer. */
    private static ComboViewer contextComboViewer;

    /** Context table viewer. */
    private static TableViewer contextTableViewer;

    /**
     * Constructs a new ProcessContextComposite.
     * 
     * @param parent Parent composite.
     * @param style Style bits.
     */
    public ProcessContextComposite(Composite parent, int style) {
        super(parent, style);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);

        // Group contextGroup = new Group(this, SWT.NONE);
        // contextGroup.setText(Messages.getString("ProcessComposite.contextGroup")); //$NON-NLS-1$
        Composite contextGroup = this;

        layout = new GridLayout();
        contextGroup.setLayout(layout);
        contextGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

        contextComboViewer = new ComboViewer(contextGroup, SWT.BORDER | SWT.READ_ONLY);
        contextComboViewer.setContentProvider(new ArrayContentProvider());
        contextComboViewer.setLabelProvider(new ContextNameLabelProvider());
        contextComboViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        contextComboViewer.getControl().setEnabled(false);

        Table contextTable = new Table(contextGroup, SWT.BORDER);
        contextTable.setLinesVisible(true);
        contextTable.setHeaderVisible(true);

        TableColumn colName = new TableColumn(contextTable, SWT.NONE);
        colName.setText(Messages.getString("ProcessComposite.nameCol")); //$NON-NLS-1$
        colName.setWidth(COLUMN_WIDTH);
        TableColumn colValue = new TableColumn(contextTable, SWT.NONE);
        colValue.setText(Messages.getString("ProcessComposite.valueCol")); //$NON-NLS-1$
        colValue.setWidth(COLUMN_WIDTH);

        contextTableViewer = new TableViewer(contextTable);
        contextTableViewer.setContentProvider(new ArrayContentProvider());
        contextTableViewer.setLabelProvider(new ContextParameterLabelProvider());
        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = HINT_HEIGHT;
        contextTableViewer.getControl().setLayoutData(data);

        // Add listeners
        contextComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(final SelectionChangedEvent event) {
                Object input = null;
                if (!event.getSelection().isEmpty()) {
                    IContext selectedContext = (IContext) ((IStructuredSelection) event.getSelection()).getFirstElement();
                    input = selectedContext.getContextParameterList();
                }
                contextTableViewer.setInput(input);
            }
        });
    }

    /**
     * Set the process on wich we are selecting context.
     * 
     * @param process The process.
     */
    public void setProcess(final IProcess process) {
        // Select the first context
        if (process != null) {
            contextComboViewer.getControl().setEnabled(true);

            getInformationsFromContextManager(process.getContextManager());

            process.getContextManager().addContextListener(new IContextListener() {

                public void contextsChanged() {
                    getInformationsFromContextManager(process.getContextManager());
                    // contextComboViewer.refresh();

                }
            });
        } else {
            contextComboViewer.getControl().setEnabled(false);
            contextComboViewer.setInput(null);
            contextTableViewer.setInput(null);
        }
    }

    protected void getInformationsFromContextManager(IContextManager contextManager) {
        List<IContext> internalContextList = new ArrayList<IContext>();
        IContext newSelectedCopiedContext = null;

        // if (!contextComboViewer.getSelection().isEmpty()) {
        // oldSelectedCopiedContext = (IContext) ((StructuredSelection)
        // contextComboViewer.getSelection()).getFirstElement();
        // }

        for (IContext context : contextManager.getListContext()) {
            IContext copiedContext = context.clone();
            internalContextList.add(copiedContext);
            if (contextManager.getDefaultContext().equals(context)) {
                newSelectedCopiedContext = copiedContext;
            }

        }
        contextComboViewer.setInput(internalContextList);

        if (newSelectedCopiedContext != null) {
            contextComboViewer.setSelection(new StructuredSelection(newSelectedCopiedContext));
            contextTableViewer.setInput(newSelectedCopiedContext.getContextParameterList());
        }

    }

    protected IContext getSelectedContext() {
        return (IContext) ((IStructuredSelection) contextComboViewer.getSelection()).getFirstElement();
    }

    protected boolean promptConfirmLauch() {
        return promptConfirmLauch(getShell(), getSelectedContext());
    }

    public static boolean promptConfirmLauch(Shell shell, IContext context) {
        boolean continueLaunch = true;

        int nbValues = 0;
        if (context == null) {
            throw new IllegalArgumentException("Context is null"); //$NON-NLS-1$
        }
        // Prompt for context values ?
        for (IContextParameter parameter : context.getContextParameterList()) {
            if (parameter.isPromptNeeded()) {
                nbValues++;
            }
        }
        if (nbValues > 0) {
            IContext contextCopy = context.clone();
            PromptDialog promptDialog = new PromptDialog(shell, contextCopy);
            if (promptDialog.open() == PromptDialog.OK) {
                for (IContextParameter param : context.getContextParameterList()) {
                    boolean found = false;
                    IContextParameter paramCopy = null;
                    for (int i = 0; i < contextCopy.getContextParameterList().size() & !found; i++) {
                        paramCopy = contextCopy.getContextParameterList().get(i);
                        if (param.getName().equals(paramCopy.getName())) {
                            // param.setValueList(paramCopy.getValueList());
                            param.setInternalValue(paramCopy.getValue());
                            found = true;
                        }
                    }
                }
                contextComboViewer.refresh();
                contextTableViewer.refresh();
            } else {
                continueLaunch = false;
            }
        } else {
            if (context.isConfirmationNeeded()) {
                continueLaunch = MessageDialog.openQuestion(shell, Messages.getString("ProcessComposite.confirmTitle"), //$NON-NLS-1$
                        Messages.getString("ProcessComposite.confirmText", context.getName())); //$NON-NLS-1$ //$NON-NLS-2$
            }

            updateDefaultValueForListTypeParameter(context.getContextParameterList());
        }
        return continueLaunch;
    }

    /**
     * Set the first item of value list for the default value if this parameter does not need to prompt.
     * 
     * @param contextParameterList
     */
    private static void updateDefaultValueForListTypeParameter(List<IContextParameter> contextParameterList) {
        for (IContextParameter contextParameter : contextParameterList) {
            String[] list = contextParameter.getValueList();
            if (list == null) {
                continue;
            }
            if (list.length == 0) {
                contextParameter.setInternalValue("");
            } else {
                contextParameter.setInternalValue(list[0]);
            }

        }
    }

    /**
     * LabelProvider for a context combo. <br/>
     * 
     * $Id$
     * 
     */
    private static class ContextNameLabelProvider extends LabelProvider {

        /**
         * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
         */
        @Override
        public String getText(final Object element) {
            IContext context = (IContext) element;
            return context.getName();
        }
    }

    /**
     * LabelProvider for a context table. <br/>
     * 
     * $Id$
     * 
     */
    private static class ContextParameterLabelProvider extends LabelProvider implements ITableLabelProvider {

        /**
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(final Object element, final int columnIndex) {
            String text;
            IContextParameter parameter = (IContextParameter) element;
            switch (columnIndex) {
            case 0:
                text = parameter.getName();
                break;
            case 1:
                text = parameter.getValue();
                break;
            default:
                text = super.getText(element);
            }
            return text;
        }

        /**
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(final Object element, final int columnIndex) {
            return null;
        }
    }

}
