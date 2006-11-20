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
package org.talend.designer.core.ui.editor.properties.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.SHOW_ROW_SELECTION;
import org.talend.commons.ui.swt.tableviewer.behavior.CellEditorValueAdapter;
import org.talend.commons.ui.swt.tableviewer.tableeditor.TableEditorManager;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.EMetadataType;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.context.Context;
import org.talend.designer.core.model.context.ContextParameter;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.cmd.ContextAddCommand;
import org.talend.designer.core.ui.editor.cmd.ContextAddParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextChangeDefaultCommand;
import org.talend.designer.core.ui.editor.cmd.ContextModifyCommand;
import org.talend.designer.core.ui.editor.cmd.ContextRemoveCommand;
import org.talend.designer.core.ui.editor.cmd.ContextRemoveParameterCommand;
import org.talend.designer.core.ui.editor.cmd.ContextRenameParameterCommand;
import org.talend.designer.core.ui.editor.outline.ProcessTreeEditPart;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;

/**
 * Section used for the contexts in a process.
 * 
 * $Id$
 * 
 */
public class ContextProcessSection extends AbstractPropertySection {

    private MultiPageTalendEditor part;

    private Process process;

    private Composite composite;

    private CTabFolder tabFolder;

    private static final String DEFAULT_CONTEXT = "defaultContext"; //$NON-NLS-1$

    private static final String MENU_TABLE = "menuTable";

    private static final String ASK_CONFIRMATION = Messages.getString("ContextProcessSection.2"); //$NON-NLS-1$

    private static final int FIRST_COLUMN_WIDTH = 50;

    private static final int NAME_COLUMN_WIDTH = 80;

    private static final int PROMPT_COLUMN_WIDTH = 120;

    private static final int TYPE_COLUMN_WIDTH = 80;

    private static final int VALUE_COLUMN_WIDTH = 140;

    private static final int COMMENT_COLUMN_WIDTH = 200;

    private static final int SCRIPT_COLUMN_WIDTH = 120;

    private Map<String, Object> hashCurControls;

    private static final String NEW_PARAM_NAME = "new"; //$NON-NLS-1$

    private Map<IContext, TableViewerCreator> tableViewerCreatorMap;

    private Map<IContext, Button> removeButtons;

    private boolean initialized = false;

    protected CommandStack getCommandStack() {
        return (CommandStack) part.getTalendEditor().getAdapter(CommandStack.class);
    }

    private SelectionListener listenerSelection = new SelectionListener() {

        public void widgetDefaultSelected(final SelectionEvent e) {
        }

        public void widgetSelected(final SelectionEvent e) {
            if (e.getSource() instanceof Button) {
                Button b = (Button) e.getSource();
                boolean selected = b.getSelection();
                IContext context = getSelectedContext();
                IContext oldContextCloned = context.clone();
                context.setConfirmationNeeded(selected);

                getCommandStack().execute(
                        new ContextModifyCommand(process.getContextManager(), oldContextCloned, context));

            } else {

                CCombo combo = (CCombo) e.getSource();
                for (int i = 0; i < process.getContextManager().getListContext().size(); i++) {
                    if (process.getContextManager().getListContext().get(i).getName().equals(combo.getText())) {
                        getCommandStack().execute(
                                new ContextChangeDefaultCommand(process, process.getContextManager().getListContext()
                                        .get(i)));
                    }
                }
                IContext context = getSelectedContext();
                if (context.getName().equals(process.getContextManager().getDefaultContext().getName())) {
                    removeButtons.get(context).setEnabled(false);
                } else {
                    removeButtons.get(context).setEnabled(true);
                }
            }
        }
    };

    private Listener menuTableListener = new Listener() {

        public void handleEvent(final Event event) {
            Menu menuTable = (Menu) hashCurControls.get(MENU_TABLE);
            MenuItem[] menuItems = menuTable.getItems();
            for (int i = 0; i < menuItems.length; i++) {
                menuItems[i].dispose();
            }

            final IContext selectedContext = getSelectedContext();
            TableViewerCreator tableViewerCreator = tableViewerCreatorMap.get(selectedContext);
            final Table table = tableViewerCreator.getTable();
            final TableItem[] tableItems = table.getSelection();

            MenuItem menuItem = new MenuItem(menuTable, SWT.PUSH);
            menuItem.setText(Messages.getString("ContextProcessSection.21")); //$NON-NLS-1$
            menuItem.addListener(SWT.Selection, new Listener() {

                public void handleEvent(final Event event) {
                    addNewParameter();
                }
            });

            if (tableItems.length == 1) {
                menuItem = new MenuItem(menuTable, SWT.PUSH);
                menuItem.setText(Messages.getString("ContextProcessSection.22") //$NON-NLS-1$ 
                        + tableItems[0].getText(1) + ")"); //$NON-NLS-2$
                menuItem.addListener(SWT.Selection, new Listener() {

                    public void handleEvent(final Event event) {
                        TableItem tableItem = tableItems[0];
                        String paramName = ((IContextParameter) tableItem.getData()).getName();
                        getCommandStack().execute(
                                new ContextRemoveParameterCommand(process.getContextManager(), paramName));
                        table.deselectAll();
                        removeButtons.get(selectedContext).setEnabled(false);
                    }
                });
            }
        }
    };

    CellEditorValueAdapter comboCellEditorValueAdapter = new CellEditorValueAdapter() {

        @Override
        public Object getCellEditorTypedValue(final CellEditor cellEditor, final Object originalTypedValue) {
            Integer intValue = null;
            oldCellEditorValue = originalTypedValue;
            oldContext = getSelectedContext().clone();
            EMetadataType[] values = EMetadataType.values();
            for (int j = 0; j < values.length && intValue == null; j++) {
                if (values[j].equals(originalTypedValue)) {
                    intValue = new Integer(j);
                }
            }
            return super.getCellEditorTypedValue(cellEditor, intValue);
        }

        @Override
        public Object getOriginalTypedValue(final CellEditor cellEditor, final Object cellEditorTypedValue) {
            newCellEditorValue = cellEditorTypedValue;
            EMetadataType value = EMetadataType.values()[(Integer) cellEditorTypedValue];
            return super.getOriginalTypedValue(cellEditor, value);
        }

        public String getColumnText(final CellEditor cellEditor, final Object cellEditorValue) {
            return ((EMetadataType) cellEditorValue).getDisplayName();
        }
    };

    CellEditorValueAdapter paramNameCellEditorValueAdapter = new CellEditorValueAdapter() {

        private String oldName;

        @Override
        public Object getCellEditorTypedValue(final CellEditor cellEditor, final Object originalTypedValue) {
            oldName = (String) originalTypedValue;
            return super.getCellEditorTypedValue(cellEditor, originalTypedValue);
        }

        @Override
        public Object getOriginalTypedValue(final CellEditor cellEditor, final Object cellEditorTypedValue) {
            if (!oldName.equals((String) cellEditorTypedValue)) {
                if (!renameParameter(oldName, (String) cellEditorTypedValue)) {
                    return super.getOriginalTypedValue(cellEditor, oldName);
                }
            }
            return super.getOriginalTypedValue(cellEditor, cellEditorTypedValue);
        }
    };

    // used when fields are modified in the table viewer
    private Object oldCellEditorValue, newCellEditorValue;

    private IContext oldContext;

    private CellEditorValueAdapter setDirtyValueAdapter = new CellEditorValueAdapter() {

        @Override
        public Object getCellEditorTypedValue(final CellEditor cellEditor, final Object originalTypedValue) {
            oldCellEditorValue = originalTypedValue;
            oldContext = getSelectedContext().clone();
            return super.getCellEditorTypedValue(cellEditor, originalTypedValue);
        }

        @Override
        public Object getOriginalTypedValue(final CellEditor cellEditor, final Object cellEditorTypedValue) {
            newCellEditorValue = cellEditorTypedValue;
            return super.getOriginalTypedValue(cellEditor, cellEditorTypedValue);
        }
    };

    private SelectionAdapter checkTableAdapter = new SelectionAdapter() {

        public void widgetSelected(final SelectionEvent e) {
            if (e.detail == SWT.CHECK) {
                TableItem tableItem = (TableItem) e.item;
                boolean promptNeeded = tableItem.getChecked();
                String paramName = ((IContextParameter) tableItem.getData()).getName();
                IContext context = getSelectedContext();
                IContext oldContextCloned = context.clone();
                List<IContextParameter> listParams = context.getContextParameterList();
                boolean paramNameFound = false;
                for (int i = 0; i < listParams.size() && !paramNameFound; i++) {
                    if (paramName.equals(listParams.get(i).getName())) {
                        listParams.get(i).setPromptNeeded(promptNeeded);
                        paramNameFound = true;
                        getCommandStack().execute(
                                new ContextModifyCommand(process.getContextManager(), oldContextCloned, context));
                    }
                }
            }
        }
    };

    private Listener removeContextListener = new Listener() {

        public void handleEvent(final Event event) {
            String contextName = tabFolder.getSelection().getText();
            boolean delete = MessageDialog.openQuestion(composite.getShell(), Messages
                    .getString("ContextProcessSection.18"), //$NON-NLS-1$
                    Messages.getString("ContextProcessSection.19") + contextName + ")"); //$NON-NLS-1$ //$NON-NLS-2$

            if (delete) {
                removeContext(contextName);
            }
        }
    };

    private Listener copyContextListener = new Listener() {

        public void handleEvent(final Event event) {
            InputDialog inputDial = new InputDialog(composite.getShell(),
                    Messages.getString("ContextProcessSection.6"), //$NON-NLS-1$
                    Messages.getString("ContextProcessSection.7"), "", null); //$NON-NLS-1$ //$NON-NLS-2$

            inputDial.open();
            String returnValue = inputDial.getValue();
            if (returnValue != null) {
                if (!returnValue.equals("")) { //$NON-NLS-1$
                    createContext(returnValue);
                }
            }
        }
    };

    private Listener renameContextListener = new Listener() {

        public void handleEvent(final Event event) {
            InputDialog inputDial = new InputDialog(composite.getShell(), Messages
                    .getString("ContextProcessSection.12"), //$NON-NLS-1$
                    Messages.getString("ContextProcessSection.13"), "", null); //$NON-NLS-1$ //$NON-NLS-2$
            inputDial.open();
            String newName = inputDial.getValue();
            if (newName != null) {
                if (!newName.equals("")) { //$NON-NLS-1$
                    String contextName = tabFolder.getSelection().getText();
                    renameContext(contextName, newName);
                }
            }
        }
    };

    private Listener removeParameterListener = new Listener() {

        public void handleEvent(final Event event) {
            final IContext selectedContext = getSelectedContext();
            TableViewerCreator tableViewerCreator = tableViewerCreatorMap.get(selectedContext);
            Table table = tableViewerCreator.getTable();
            final TableItem[] tableItems = table.getSelection();
            TableItem tableItem = tableItems[0];
            String paramName = ((IContextParameter) tableItem.getData()).getName();
            getCommandStack().execute(new ContextRemoveParameterCommand(process.getContextManager(), paramName));
            table.deselectAll();
            removeButtons.get(selectedContext).setEnabled(false);
        }
    };

    private void addNewParameter() {
        IContext context = getSelectedContext();
        List<IContextParameter> listParams = context.getContextParameterList();
        Integer numParam = new Integer(1);
        boolean paramNameFound;
        String paramName = null;
        do {
            paramNameFound = true;
            paramName = NEW_PARAM_NAME + numParam;
            for (int i = 0; i < listParams.size() && paramNameFound; i++) {
                if (paramName.equals(listParams.get(i).getName())) {
                    paramNameFound = false;
                }
            }
            if (!paramNameFound) {
                numParam++;
            }
        } while (!paramNameFound);

        ContextParameter contextParam = new ContextParameter();
        contextParam.setName(paramName);
        contextParam.setType(EMetadataType.STRING);
        contextParam.setPrompt(paramName + "?"); //$NON-NLS-1$
        contextParam.setValue("''"); //$NON-NLS-1$
        contextParam.setComment(""); //$NON-NLS-1$
        getCommandStack().execute(new ContextAddParameterCommand(process.getContextManager(), contextParam));
    }

    private boolean renameParameter(final String oldParamName, final String newParamName) {
        if (!process.getContextManager().checkValidParameterName(newParamName)) {
            MessageDialog.openError(composite.getShell(), "Error", "Parameter name is not valid.");
            return false;
        }

        getCommandStack().execute(
                new ContextRenameParameterCommand(process.getContextManager(), oldParamName, newParamName));
        return true;
    }

    private IContext getSelectedContext() {
        List<IContext> contexts = process.getContextManager().getListContext();
        CTabItem tabItem = tabFolder.getSelection();
        int numContext = 0;

        while (!tabItem.getText().equals(contexts.get(numContext).getName())) {
            numContext++;
        }
        return contexts.get(numContext);
    }

    public void createContext(final String name) {
        IContext context = this.getSelectedContext();

        for (int i = 0; i < process.getContextManager().getListContext().size(); i++) {
            if (process.getContextManager().getListContext().get(i).getName().equals(name)) {
                MessageBox mBox = new MessageBox(composite.getShell(), SWT.ICON_ERROR);
                mBox.setText(Messages.getString("ContextProcessSection.29")); //$NON-NLS-1$
                mBox.setMessage(Messages.getString("ContextProcessSection.30")); //$NON-NLS-1$
                mBox.open();
                return;
            }
        }
        Context newContext = new Context(name);

        List<IContextParameter> newParamList = new ArrayList<IContextParameter>();
        newContext.setContextParameterList(newParamList);
        ContextParameter param;
        for (int i = 0; i < context.getContextParameterList().size(); i++) {
            param = new ContextParameter();
            param.setName(context.getContextParameterList().get(i).getName());
            param.setPrompt(context.getContextParameterList().get(i).getPrompt());
            param.setType(context.getContextParameterList().get(i).getType());
            param.setValue(context.getContextParameterList().get(i).getValue());
            param.setComment(context.getContextParameterList().get(i).getComment());
            param.setPromptNeeded(context.getContextParameterList().get(i).isPromptNeeded());
            newParamList.add(param);
        }
        CCombo combo = (CCombo) hashCurControls.get(DEFAULT_CONTEXT);

        this.getCommandStack().execute(new ContextAddCommand(this, newContext, combo));

    }

    public void removeContext(final String contextName) {
        if (contextName.equals(process.getContextManager().getDefaultContext().getName())) {
            MessageBox mBox = new MessageBox(composite.getShell(), SWT.ICON_ERROR);
            mBox.setText(Messages.getString("ContextProcessSection.31")); //$NON-NLS-1$
            mBox.setMessage(Messages.getString("ContextProcessSection.32")); //$NON-NLS-1$
            mBox.open();
            return;
        }
        CCombo combo = (CCombo) hashCurControls.get(DEFAULT_CONTEXT);

        getCommandStack().execute(new ContextRemoveCommand(this, contextName, combo));
        composite.layout();
    }

    private void renameContext(final String contextName, final String newName) {
        IContext context = this.getSelectedContext();
        IContext oldContextCloned = context.clone();
        boolean found = false;

        for (int i = 0; i < process.getContextManager().getListContext().size() && !found; i++) {
            if (process.getContextManager().getListContext().get(i).getName().equals(newName)) {
                MessageBox mBox = new MessageBox(composite.getShell(), SWT.ICON_ERROR);
                mBox.setText(Messages.getString("ContextProcessSection.33")); //$NON-NLS-1$
                mBox.setMessage(Messages.getString("ContextProcessSection.34")); //$NON-NLS-1$
                mBox.open();
                return;
            }
        }
        context.setName(newName);
        found = false;
        for (int i = 0; i < tabFolder.getItemCount() && !found; i++) {
            if (tabFolder.getItem(i).getText().equals(contextName)) {
                tabFolder.getItem(i).setText(newName);
                found = true;
            }
        }
        tabFolder.update();

        CCombo combo = (CCombo) hashCurControls.get(DEFAULT_CONTEXT);
        String[] stringList = new String[process.getContextManager().getListContext().size()];
        for (int i = 0; i < process.getContextManager().getListContext().size(); i++) {
            stringList[i] = process.getContextManager().getListContext().get(i).getName();
        }

        combo.setItems(stringList);
        composite.layout();
        combo.setText(process.getContextManager().getDefaultContext().getName());
        combo.clearSelection();
        getCommandStack().execute(new ContextModifyCommand(process.getContextManager(), oldContextCloned, context));
    }

    public void addContext(final IContext context) {
        Composite contextComposite = getWidgetFactory().createComposite(tabFolder);
        contextComposite.setLayout(new GridLayout());
        contextComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

        Composite buttonComposite = getWidgetFactory().createComposite(contextComposite);
        buttonComposite.setLayout(new GridLayout(3, false));
        buttonComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

        final Button checkBtn;
        checkBtn = getWidgetFactory().createButton(buttonComposite, ASK_CONFIRMATION, SWT.CHECK);
        checkBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        checkBtn.addSelectionListener(listenerSelection);
        checkBtn.setEnabled(!process.isReadOnly());

        Composite buttonParameterComposite = getWidgetFactory().createComposite(buttonComposite);
        buttonParameterComposite.setLayout(new GridLayout(3, false));
        buttonParameterComposite
                .setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.GRAB_HORIZONTAL));
        CLabel label = getWidgetFactory().createCLabel(buttonParameterComposite, "Parameters:");
        label.setAlignment(SWT.RIGHT);
        Button addParameter = new Button(buttonParameterComposite, SWT.None);
        addParameter.setText("Add");
        addParameter.addListener(SWT.Selection, new Listener() {

            public void handleEvent(final Event event) {
                addNewParameter();
            }
        });
        addParameter.setEnabled(!process.isReadOnly());
        final Button removeParameter = new Button(buttonParameterComposite, SWT.None);
        removeParameter.setText("Remove");
        removeButtons.put(context, removeParameter);
        removeParameter.setEnabled(false);

        Composite buttonContextComposite = getWidgetFactory().createComposite(buttonComposite);
        buttonContextComposite.setLayout(new GridLayout(4, false));
        buttonContextComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.GRAB_HORIZONTAL));
        label = getWidgetFactory().createCLabel(buttonContextComposite, "Context:");
        label.setAlignment(SWT.RIGHT);
        Button copyContext = new Button(buttonContextComposite, SWT.None);
        copyContext.setText("Copy");
        copyContext.addListener(SWT.Selection, copyContextListener);
        copyContext.setEnabled(!process.isReadOnly());
        Button renameContext = new Button(buttonContextComposite, SWT.None);
        renameContext.setText("Rename");
        renameContext.addListener(SWT.Selection, renameContextListener);
        renameContext.setEnabled(!process.isReadOnly());
        final Button removeContext = new Button(buttonContextComposite, SWT.None);
        removeContext.setText("Remove");
        removeContext.addListener(SWT.Selection, removeContextListener);
        removeContext.setEnabled(!process.isReadOnly());

        tabFolder.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(final SelectionEvent e) {
            }

            public void widgetSelected(final SelectionEvent e) {
                IContext context = getSelectedContext();
                checkBtn.setSelection(context.isConfirmationNeeded());
                if (!process.isReadOnly()) {
                    if (context.getName().equals(process.getContextManager().getDefaultContext().getName())) {
                        removeContext.setEnabled(false);
                    } else {
                        removeContext.setEnabled(true);
                    }
                }
            }
        });
        CTabItem tabItem = getWidgetFactory().createTabItem(tabFolder, SWT.NONE);
        tabItem.setText(context.getName());

        final TableViewerCreator tableViewerCreator = new TableViewerCreator(contextComposite);
        tableViewerCreatorMap.put(context, tableViewerCreator);
        tableViewerCreator.setBorderVisible(true);
        tableViewerCreator.setCheckboxInFirstColumn(true);
        tableViewerCreator.setAllColumnsResizable(true);
        tableViewerCreator.setAllColumnsSortable(true);
        tableViewerCreator.setLayoutMode(LAYOUT_MODE.FILL_HORIZONTAL);

        final Table table = tableViewerCreator.createTable();
        table.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                removeParameter.setEnabled(true);
            }
        });
        removeParameter.addListener(SWT.Selection, removeParameterListener);
        table.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

        if (!process.isReadOnly()) {
            Menu menuTable;
            menuTable = (Menu) hashCurControls.get(MENU_TABLE);
            table.setMenu(menuTable);
            menuTable.addListener(SWT.Show, menuTableListener);
        }

        table.setEnabled(!process.isReadOnly());
        table.addSelectionListener(checkTableAdapter);

        addTableColumns(tableViewerCreator, table);

        List<IContextParameter> listContextParam = context.getContextParameterList();
        tableViewerCreator.init(listContextParam);

        TableItem tableItem;
        for (int i = 0; i < table.getItemCount(); i++) {
            tableItem = table.getItem(i);
            String paramName = ((IContextParameter) tableItem.getData()).getName();
            List<IContextParameter> listParams = context.getContextParameterList();
            boolean paramNameFound = false;
            for (int j = 0; j < listParams.size() && !paramNameFound; j++) {
                if (paramName.equals(listParams.get(j).getName())) {
                    tableItem.setChecked(listParams.get(i).isPromptNeeded());
                    paramNameFound = true;
                }
            }
        }

        TableEditorManager tableEditorManager = new TableEditorManager(tableViewerCreator);
        tableEditorManager.init();
        tabItem.setControl(contextComposite);
        if (context.getName().equals(process.getContextManager().getDefaultContext().getName())) {
            tabFolder.setSelection(tabItem);
            removeContext.setEnabled(false);
        }
    }

    private void addTableColumns(final TableViewerCreator tableViewerCreator, Table table) {
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Prompt"); //$NON-NLS-1$
        column.setModifiable(true);
        column.setWidth(FIRST_COLUMN_WIDTH);
        column.setToolTipHeader(Messages.getString("ContextProcessSection.38")); //$NON-NLS-1$

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ContextProcessSection.39")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IContextParameter, String>() {

            public String get(IContextParameter bean) {
                return bean.getName();
            }

            public void set(IContextParameter bean, String value) {
                bean.setName(value);
            }
        });
        column.setModifiable(true);
        column.setWidth(NAME_COLUMN_WIDTH);
        TextCellEditor textCellEditor = new TextCellEditor(table);
        column.setCellEditor(textCellEditor, paramNameCellEditorValueAdapter);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ContextProcessSection.41")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IContextParameter, String>() {

            public String get(IContextParameter bean) {
                return bean.getPrompt();
            }

            public void set(IContextParameter bean, String value) {
                bean.setPrompt(value);
                if (!oldCellEditorValue.equals(newCellEditorValue)) {
                    IContext context = getSelectedContext();
                    getCommandStack().execute(
                            new ContextModifyCommand(process.getContextManager(), oldContext, context));
                }
            }
        });
        column.setModifiable(true);
        column.setWidth(PROMPT_COLUMN_WIDTH);
        textCellEditor = new TextCellEditor(table);
        column.setCellEditor(textCellEditor, setDirtyValueAdapter);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ContextProcessSection.43")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IContextParameter, EMetadataType>() {

            public EMetadataType get(IContextParameter bean) {
                return bean.getType();
            }

            public void set(IContextParameter bean, EMetadataType value) {
                bean.setType(value);
                if (!oldCellEditorValue.equals(newCellEditorValue)) {
                    IContext context = getSelectedContext();
                    getCommandStack().execute(
                            new ContextModifyCommand(process.getContextManager(), oldContext, context));
                }
            }
        });
        column.setModifiable(true);
        column.setWidth(TYPE_COLUMN_WIDTH);
        EMetadataType[] values = EMetadataType.values();
        String[] stringValues = new String[values.length];
        for (int j = 0; j < values.length; j++) {
            stringValues[j] = values[j].getDisplayName();
        }

        ComboBoxCellEditor comboBoxCellEditor = new ComboBoxCellEditor(table, stringValues);
        ((CCombo) comboBoxCellEditor.getControl()).setEditable(false);
        column.setCellEditor(comboBoxCellEditor, comboCellEditorValueAdapter);
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ContextProcessSection.45")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IContextParameter, String>() {

            public String get(IContextParameter bean) {
                return bean.getValue();
            }

            public void set(IContextParameter bean, String value) {
                bean.setValue(value);
                if (!oldCellEditorValue.equals(newCellEditorValue)) {
                    IContext context = getSelectedContext();
                    getCommandStack().execute(
                            new ContextModifyCommand(process.getContextManager(), oldContext, context));
                }
            }
        });
        column.setModifiable(true);
        column.setWidth(VALUE_COLUMN_WIDTH);
        textCellEditor = new TextCellEditor(table);
        column.setCellEditor(textCellEditor, setDirtyValueAdapter);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ContextProcessSection.47")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IContextParameter, String>() {

            public String get(IContextParameter bean) {
                return bean.getComment();
            }

            public void set(IContextParameter bean, String value) {
                bean.setComment(value);
                if (!oldCellEditorValue.equals(newCellEditorValue)) {
                    IContext context = getSelectedContext();
                    getCommandStack().execute(
                            new ContextModifyCommand(process.getContextManager(), oldContext, context));
                }
            }
        });
        column.setModifiable(true);
        column.setWidth(COMMENT_COLUMN_WIDTH);
        column.setCellEditor(new TextCellEditor(table), setDirtyValueAdapter);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Script code");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IContextParameter, String>() {

            public String get(IContextParameter bean) {
                return ContextParameterUtils.getScriptCode(bean, ((RepositoryContext) org.talend.core.CorePlugin
                        .getContext().getProperty(org.talend.core.context.Context.REPOSITORY_CONTEXT_KEY)).getProject()
                        .getLanguage());
            }

            public void set(IContextParameter bean, String value) {
                // Immutable
            }
        });
        column.setModifiable(false);
        column.setWidth(SCRIPT_COLUMN_WIDTH);
    }

    private void addComponents() {
        FormData data;
        hashCurControls = new HashMap<String, Object>();

        Composite dataComp = getWidgetFactory().createComposite(composite, SWT.NONE);

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
        formLayout.marginHeight = ITabbedPropertyConstants.VSPACE;
        formLayout.spacing = ITabbedPropertyConstants.VMARGIN + 1;
        dataComp.setLayout(formLayout);

        CLabel label = getWidgetFactory().createCLabel(dataComp, Messages.getString("ContextProcessSection.49")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0);
        label.setLayoutData(data);
        label.setAlignment(SWT.RIGHT);

        CCombo combo;
        combo = getWidgetFactory().createCCombo(dataComp, SWT.BORDER);
        data = new FormData();
        data.left = new FormAttachment(label);
        combo.setLayoutData(data);
        combo.setEditable(false);
        hashCurControls.put(DEFAULT_CONTEXT, combo);
        String[] stringList = new String[process.getContextManager().getListContext().size()];
        for (int i = 0; i < process.getContextManager().getListContext().size(); i++) {
            stringList[i] = process.getContextManager().getListContext().get(i).getName();
        }
        combo.setItems(stringList);
        combo.addSelectionListener(listenerSelection);
        combo.setEnabled(!process.isReadOnly());

        tabFolder = getWidgetFactory().createTabFolder(composite, SWT.BORDER);
        tabFolder.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

        Menu menuTable = new Menu(tabFolder);
        hashCurControls.put(MENU_TABLE, menuTable);

        IContext context;

        tableViewerCreatorMap = new HashMap<IContext, TableViewerCreator>();
        removeButtons = new HashMap<IContext, Button>();

        for (int i = 0; i < process.getContextManager().getListContext().size(); i++) {
            context = process.getContextManager().getListContext().get(i);
            addContext(context);
        }
    }

    public void createControls(final Composite parent, final TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);

        composite = parent;

        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
        gridLayout.marginHeight = ITabbedPropertyConstants.VSPACE;
        composite.setLayout(gridLayout);

        composite.getParent().setLayout(new GridLayout());
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = SWT.FILL;
        gridData.horizontalAlignment = SWT.FILL;
        composite.setLayoutData(gridData);
    }

    public void setInput(final IWorkbenchPart workbenchPart, final ISelection selection) {
        if (!(selection instanceof IStructuredSelection)) {
            return;
        }
        if (workbenchPart instanceof MultiPageTalendEditor) {
            part = (MultiPageTalendEditor) workbenchPart;
        } else {
            part = (MultiPageTalendEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
        }
        super.setInput(part, selection);
        Object input = ((IStructuredSelection) selection).getFirstElement();
        if (input instanceof ProcessTreeEditPart) {
            process = (Process) ((ProcessTreeEditPart) input).getModel();
        } else {
            if (input instanceof ProcessPart) {
                process = (Process) ((ProcessPart) input).getModel();
            } else {
                return;
            }
        }

        if (!initialized) {
            addComponents();
            initialized = true;
        }
    }

    public void refresh() {
        if (hashCurControls == null) {
            return;
        }
        CCombo combo = (CCombo) hashCurControls.get(DEFAULT_CONTEXT);
        combo.setText(process.getContextManager().getDefaultContext().getName());
        combo.clearSelection();

        for (int i = 0; i < process.getContextManager().getListContext().size(); i++) {
            TableViewerCreator tableViewerCreator = tableViewerCreatorMap.get(process.getContextManager()
                    .getListContext().get(i));
            tableViewerCreator.init();
            tableViewerCreator.getTableViewer().refresh();
            // tableViewerCreatorMap.get(process.getContextManager().getListContext().get(i)).getTableViewer().refresh();

            Table table = tableViewerCreator.getTable();
            TableItem tableItem;
            for (int j = 0; j < table.getItemCount(); j++) {
                tableItem = table.getItem(j);
                String paramName = ((IContextParameter) tableItem.getData()).getName();
                List<IContextParameter> listParams = process.getContextManager().getListContext().get(i)
                        .getContextParameterList();
                boolean paramNameFound = false;
                for (int k = 0; k < listParams.size() && !paramNameFound; k++) {
                    if (paramName.equals(listParams.get(k).getName())) {
                        tableItem.setChecked(listParams.get(j).isPromptNeeded());
                        paramNameFound = true;
                    }
                }
            }
        }

        tabFolder.update();

        composite.layout();
    }

    public Process getProcess() {
        return this.process;
    }

    public void setProcess(final Process process) {
        this.process = process;
    }

    public CTabFolder getTabFolder() {
        return this.tabFolder;
    }

    public void setTabFolder(final CTabFolder tabFolder) {
        this.tabFolder = tabFolder;
    }

    public Map<IContext, TableViewerCreator> getTableViewerCreatorMap() {
        return this.tableViewerCreatorMap;
    }

    public void setTableViewerCreatorMap(final Map<IContext, TableViewerCreator> tableViewerCreatorMap) {
        this.tableViewerCreatorMap = tableViewerCreatorMap;
    }
}
