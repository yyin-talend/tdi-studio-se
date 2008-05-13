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
package org.talend.designer.core.ui.views.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty;

/**
 * yzhang class global comment. Detailled comment
 */
public class AdvancedContextComposite extends ScrolledComposite implements IDynamicProperty {

    private static final String CODE_PROPERTY = "codeProperty";

    private static final String NAME_PROPERTY = "nameProperty";

    private static final int ADD_COLUMN = 0;

    private static final int CODE_COLUMN = 1;

    private List<IElementParameter> comboContent;

    private DynamicComboBoxCellEditor dynamicComboBoxCellEditor;

    /**
     * yzhang AdvancedContextComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public AdvancedContextComposite(Composite parent, int style, final Element element) {
        super(parent, style);

        setExpandHorizontal(true);
        setExpandVertical(true);

        FormData layouData = new FormData();
        layouData.left = new FormAttachment(0, 0);
        layouData.right = new FormAttachment(100, 0);
        layouData.top = new FormAttachment(0, 0);
        layouData.bottom = new FormAttachment(100, 0);
        setLayoutData(layouData);

        final Composite panel = new Composite(this, SWT.NONE);
        setContent(panel);
        panel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

        FormLayout layout = new FormLayout();
        layout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
        layout.marginHeight = ITabbedPropertyConstants.VSPACE;
        layout.spacing = ITabbedPropertyConstants.VMARGIN + 1;
        panel.setLayout(layout);

        final TableViewer tableViewer = new TableViewer(panel, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        FormData tableLayoutData = new FormData();
        tableLayoutData.left = new FormAttachment(10, 0);
        tableLayoutData.right = new FormAttachment(90, 0);
        tableLayoutData.top = new FormAttachment(10, 0);
        tableLayoutData.bottom = new FormAttachment(50, 0);

        final Table table = tableViewer.getTable();

        table.setLayoutData(tableLayoutData);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn columnName = new TableColumn(table, SWT.NONE, ADD_COLUMN);
        columnName.setText("Name");
        columnName.setWidth(200);
        columnName.setResizable(true);
        columnName.setMoveable(true);

        TableColumn columnCode = new TableColumn(table, SWT.NONE, CODE_COLUMN);
        columnCode.setText("Code");
        columnCode.setWidth(200);
        columnCode.setResizable(true);
        columnCode.setMoveable(true);

        Image addImage = ImageProvider.getImage(EImage.ADD_ICON);
        Image removeImage = ImageProvider.getImage(EImage.DELETE_ICON);

        final Button buttonAdd = new Button(panel, SWT.NONE);
        buttonAdd.setText("Add");
        buttonAdd.setImage(addImage);

        FormData buttonAddData = new FormData();
        buttonAddData.left = new FormAttachment(table, 10, SWT.LEFT);
        buttonAddData.top = new FormAttachment(table, 2);
        buttonAdd.setLayoutData(buttonAddData);

        final Button buttonRemove = new Button(panel, SWT.NONE);
        buttonRemove.setText("Delete");
        buttonRemove.setImage(removeImage);

        FormData buttonRemoveData = new FormData();
        buttonRemoveData.left = new FormAttachment(buttonAdd, 1, SWT.RIGHT);
        buttonRemoveData.top = new FormAttachment(table, 2);
        buttonRemove.setLayoutData(buttonRemoveData);

        setMinSize(panel.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        final List<IElementParameter> legalParameters = new ArrayList<IElementParameter>();
        for (IElementParameter parameter : element.getElementParameters()) {
            if (parameter.isShow(element.getElementParameters())
                    && parameter.getCategory() != EComponentCategory.TECHNICAL
                    && (parameter.getField() == EParameterFieldType.CHECK || parameter.getField() == EParameterFieldType.CLOSED_LIST)
                    || parameter.getField() == EParameterFieldType.MODULE_LIST) {
                legalParameters.add(parameter);
            }
        }

        TableViewerProvider provider = new TableViewerProvider();
        tableViewer.setContentProvider(provider);
        tableViewer.setLabelProvider(provider);
        tableViewer.setCellModifier(new ICellModifier() {

            public boolean canModify(Object element, String property) {
                return comboContent.size() > 0 || property.equals(CODE_PROPERTY);
            }

            public Object getValue(Object element, String property) {

                if (property.equals(NAME_PROPERTY)) {
                    comboContent.add((IElementParameter) element);
                    dynamicComboBoxCellEditor.refresh();
                    return element;
                } else if (property.equals(CODE_PROPERTY)) {
                    Object value = ((IElementParameter) element).getValue();
                    if (value instanceof String) {
                        return value;
                    } else {
                        return String.valueOf(value);
                    }
                }
                return "";
            }

            public void modify(Object elem, String property, Object value) {

                TableItem item = null;
                if (elem instanceof Table) {
                    item = ((Table) elem).getItem(0);
                } else if (elem instanceof TableItem) {
                    item = (TableItem) elem;
                }

                if (item != null && value != null) {
                    IElementParameter parameter = (IElementParameter) item.getData();
                    if (property.equals(NAME_PROPERTY)) {

                        List<IElementParameter> tableInput = (List<IElementParameter>) tableViewer.getInput();
                        int i = tableInput.indexOf(parameter);
                        tableInput.remove(i);
                        parameter.setContextMode(false);
                        tableInput.add(i, (IElementParameter) value);
                        ((IElementParameter) value).setContextMode(true);

                        refreshComboContent(tableViewer, legalParameters);

                    } else if (property.equals(CODE_PROPERTY)) {

                        Command cmd = new PropertyChangeCommand(element, parameter.getName(), value);
                        executeCommand(cmd);

                    }
                    tableViewer.refresh();
                }
            }

        });

        List<IElementParameter> tableContent = new ArrayList<IElementParameter>();
        for (IElementParameter parameter : element.getElementParameters()) {
            if (parameter.isContextMode()) {
                tableContent.add(parameter);
            }
        }
        tableViewer.setInput(tableContent);

        comboContent = new ArrayList<IElementParameter>(calculateComboContent(tableViewer, legalParameters));
        dynamicComboBoxCellEditor = new DynamicComboBoxCellEditor(table, comboContent, comboboxCellEditorLabelProvider);
        tableViewer.setCellEditors(new CellEditor[] { dynamicComboBoxCellEditor, new TextCellEditor(table) });
        tableViewer.setColumnProperties(new String[] { NAME_PROPERTY, CODE_PROPERTY });

        if (tableContent.size() == legalParameters.size()) {
            buttonAdd.setEnabled(false);
        }

        buttonAdd.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {

            }

            public void widgetSelected(SelectionEvent e) {
                if (comboContent.size() > 0) {
                    List<IElementParameter> tableInput = (List<IElementParameter>) tableViewer.getInput();
                    IElementParameter parameter = comboContent.get(0);
                    tableInput.add(parameter);
                    ((IElementParameter) parameter).setContextMode(true);
                    refreshComboContent(tableViewer, legalParameters);
                    if (comboContent.size() == 0) {
                        buttonAdd.setEnabled(false);
                    }
                    tableViewer.refresh();
                    if (!buttonRemove.isEnabled()) {
                        buttonRemove.setEnabled(true);
                    }
                    executeCommand(new PropertyChangeCommand(element, parameter.getName(), parameter.getValue()));
                }
            }

        });
        if (tableContent.size() == 0) {
            buttonRemove.setEnabled(false);
        }
        buttonRemove.addSelectionListener(new SelectionListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetDefaultSelected(SelectionEvent e) {

            }

            public void widgetSelected(SelectionEvent e) {
                ISelection selection = tableViewer.getSelection();
                List<IElementParameter> tableViewerInput = ((List<IElementParameter>) tableViewer.getInput());

                boolean needRefresh = false;
                if (!selection.isEmpty() && selection instanceof StructuredSelection) {
                    Object[] elements = ((StructuredSelection) selection).toArray();
                    for (Object element : elements) {
                        tableViewerInput.remove(element);
                        ((IElementParameter) element).setContextMode(false);
                    }
                    needRefresh = true;
                } else if (!tableViewerInput.isEmpty()) {
                    int index = tableViewerInput.size() - 1;
                    ((IElementParameter) tableViewerInput.get(index)).setContextMode(false);
                    tableViewerInput.remove(index);
                    needRefresh = true;
                }

                if (needRefresh) {
                    refreshComboContent(tableViewer, legalParameters);
                    tableViewer.refresh();
                    if (!buttonAdd.isEnabled()) {
                        buttonAdd.setEnabled(true);
                    }
                    if (((List) tableViewer.getInput()).size() == 0) {
                        buttonRemove.setEnabled(false);
                    }
                }
            }
        });

    }

    /**
     * yzhang AdvancedContextComposite class global comment. Detailled comment
     */
    private class DynamicComboBoxCellEditor extends ExtendedComboBoxCellEditor {

        /**
         * yzhang ComboBoxCellEditor constructor comment.
         * 
         * @param composite
         * @param list
         * @param labelProvider
         */
        public DynamicComboBoxCellEditor(Composite composite, List<?> list, ILabelProvider labelProvider) {
            super(composite, list, labelProvider);
        }

        public void refresh() {
            refreshItems("");
        }

    }

    /**
     * yzhang Comment method "executeCommand".
     * 
     * @param cmd
     */
    private void executeCommand(Command cmd) {
        if (commandStack == null) {
            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            if (part instanceof AbstractMultiPageTalendEditor) {
                AbstractTalendEditor editor = ((AbstractMultiPageTalendEditor) part).getTalendEditor();
                commandStack = (CommandStack) editor.getAdapter(CommandStack.class);
            }
        }

        if (commandStack != null) {
            commandStack.execute(cmd);
        } else {
            // Execute self
            cmd.execute();
        }

    }

    /**
     * yzhang Comment method "refreshComboContent".
     * 
     * @param tableViewer
     * @param legalParameters
     */
    private void refreshComboContent(TableViewer tableViewer, List<IElementParameter> legalParameters) {
        comboContent.clear();
        comboContent.addAll(calculateComboContent(tableViewer, legalParameters));
    }

    /**
     * yzhang Comment method "calculateComboContent".
     * 
     * @param tableViewer
     * @param legalParameters
     * @return
     */
    private List<IElementParameter> calculateComboContent(TableViewer tableViewer, List<IElementParameter> legalParameters) {
        List<IElementParameter> tableInput = (List<IElementParameter>) tableViewer.getInput();
        List<IElementParameter> content = new ArrayList<IElementParameter>(legalParameters);
        content.removeAll(tableInput);
        return content;
    }

    private ILabelProvider comboboxCellEditorLabelProvider = new ILabelProvider() {

        public Image getImage(Object element) {
            // TODO Auto-generated method stub
            return null;
        }

        public String getText(Object element) {
            return ((IElementParameter) element).getDisplayName();
        }

        public void addListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

        public void dispose() {
            // TODO Auto-generated method stub

        }

        public boolean isLabelProperty(Object element, String property) {
            // TODO Auto-generated method stub
            return false;
        }

        public void removeListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

    };

    private CommandStack commandStack;

    /**
     * yzhang AdvancedContextComposite class global comment. Detailled comment
     */
    private class TableViewerProvider implements IStructuredContentProvider, ITableLabelProvider {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof List) {
                return ((List) inputElement).toArray();
            }
            return new Object[0];
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose() {

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
         * java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            if (element instanceof IElementParameter) {
                IElementParameter ep = (IElementParameter) element;
                if (columnIndex == ADD_COLUMN) {
                    return ep.getDisplayName();
                } else if (columnIndex == CODE_COLUMN) {
                    return ep.getValue() != null ? ep.getValue().toString() : "";
                }
            }
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void addListener(ILabelProviderListener listener) {

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
         */
        public boolean isLabelProperty(Object element, String property) {
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void removeListener(ILabelProviderListener listener) {

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getComposite()
     */
    public Composite getComposite() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getCurRowSize()
     */
    public int getCurRowSize() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getElement()
     */
    public Element getElement() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getHashCurControls()
     */
    public BidiMap getHashCurControls() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getPart()
     */
    public AbstractMultiPageTalendEditor getPart() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getQueriesMap()
     */
    public Map<String, List<String>> getQueriesMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getRepositoryAliasName(org.talend.core.model.properties.ConnectionItem)
     */
    public String getRepositoryAliasName(ConnectionItem connectionItem) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getRepositoryConnectionItemMap()
     */
    public Map<String, ConnectionItem> getRepositoryConnectionItemMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getRepositoryQueryStoreMap()
     */
    public Map<String, Query> getRepositoryQueryStoreMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getRepositoryTableMap()
     */
    public Map<String, IMetadataTable> getRepositoryTableMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getSection()
     */
    public EComponentCategory getSection() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getTableIdAndDbSchemaMap()
     */
    public Map<String, String> getTableIdAndDbSchemaMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getTableIdAndDbTypeMap()
     */
    public Map<String, String> getTableIdAndDbTypeMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getTablesMap()
     */
    public Map<String, List<String>> getTablesMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#refresh()
     */
    public void refresh() {
        getParent().layout();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#setCurRowSize(int)
     */
    public void setCurRowSize(int i) {

    }

}
