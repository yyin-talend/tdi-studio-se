// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.ui.tabs.table;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.behavior.ColumnCellModifier;
import org.talend.commons.ui.swt.tableviewer.behavior.DefaultCellModifier;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.commons.ui.swt.tableviewer.data.AccessorUtils;
import org.talend.commons.ui.swt.tableviewer.data.ModifiedObjectInfo;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.ui.proposal.JavaSimpleDateFormatProposalProvider;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class OutputXmlTreeSchemaTableView extends AbstractExtendedTableViewer<TreeSchemaTableEntry> {

    private static final String ID_COLUMN_XPATH = "xpath";

    private boolean isValidName = true;

    public OutputXmlTreeSchemaTableView(ExtendedTableModel<TreeSchemaTableEntry> extendedTableModel, Composite parent) {
        super(extendedTableModel, parent);
    }

    @Override
    protected void createColumns(TableViewerCreator<TreeSchemaTableEntry> tableViewerCreator, Table table) {
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("XPath");
        column.setId(ID_COLUMN_XPATH);
        column.setWeight(20);
        column.setModifiable(true);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<TreeSchemaTableEntry, Object>() {

            public Object get(TreeSchemaTableEntry bean) {
                return bean.getXPath();
            }

            public void set(TreeSchemaTableEntry bean, Object value) {
                if (isValidName) {
                    bean.setName((String) value);

                    XmlMapData mapperData = XmlMapUtil.getXmlMapData(bean.getTreeNode());
                    XmlMapUtil.updateXPathAndExpression(mapperData, bean.getTreeNode(), bean.getName(),
                            XmlMapUtil.getXPathLength(bean.getXPath()), true);
                    if (!bean.getTreeNode().getChildren().isEmpty()) {
                        refresh();
                    }

                }
            }
        });
        final TextCellEditor cellEditor = new TextCellEditor(tableViewerCreator.getTable());
        cellEditor.addListener(new DialogErrorForCellEditorListener(cellEditor, column) {

            @Override
            public void newValidValueTyped(int itemIndex, Object previousValue, Object newValue, CELL_EDITOR_STATE state) {
            }

            @Override
            public String validateValue(String newValue, int beanPosition) {
                return validateXPath(newValue, beanPosition);
            }

        });
        column.setCellEditor(cellEditor);
        column.setColumnCellModifier(new ColumnCellModifier(column) {

            @Override
            public Object getValue(Object bean) {
                TreeSchemaTableEntry entry = (TreeSchemaTableEntry) bean;
                String name = entry.getName();
                if (name == null || "".equals(name)) {
                    TreeNode treeNode = entry.getTreeNode();
                    if (NodeType.NAME_SPACE.equals(treeNode.getNodeType())) {
                        name = XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX;
                    }
                }
                return name;
            }

            @Override
            public boolean modify(Object bean, Object value) {
                return false;
            }
        });

        tableViewerCreator.setCellModifier(new XmlCellModifier(tableViewerCreator));
    }

    public void refresh() {
        this.getTableViewerCreator().getTableViewer().refresh();
    }

    private boolean currentBeanHasJavaDateType(Object element) {
        String talendType = getTalendTypeAccessor().get((TreeSchemaTableEntry) element);
        boolean typeIsDate = JavaTypesManager.DATE.getId().equals(talendType);
        return typeIsDate;
    }

    private IBeanPropertyAccessors<TreeSchemaTableEntry, String> getTalendTypeAccessor() {
        return new IBeanPropertyAccessors<TreeSchemaTableEntry, String>() {

            public String get(TreeSchemaTableEntry bean) {
                return bean.getType();
            }

            public void set(TreeSchemaTableEntry bean, String value) {
                bean.setType(value);
                if (currentBeanHasJavaDateType(bean)) {
                    bean.setPattern(new JavaSimpleDateFormatProposalProvider().getProposals(null, 0)[0].getContent());
                }
            }
        };
    }

    private IBeanPropertyAccessors<TreeSchemaTableEntry, Boolean> getNullableAccessor() {
        return new IBeanPropertyAccessors<TreeSchemaTableEntry, Boolean>() {

            public Boolean get(TreeSchemaTableEntry bean) {
                return bean.isNullable();
            }

            public void set(TreeSchemaTableEntry bean, Boolean value) {
                bean.setNullable(value);
            }
        };
    }

    public String validateXPath(String newValue, int beanPosition) {
        if (beanPosition == -1) {
            return null;
        }
        isValidName = true;
        if (newValue == null || "".equals(newValue)) {
            isValidName = false;
            return "Name can't be null";
        }

        if ((newValue.indexOf("(") != -1 || newValue.indexOf(")") != -1)
                && !newValue.equals(XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX)) {
            isValidName = false;
            return "Namespace Prefix is invalid";
        }

        TreeSchemaTableEntry bean = getExtendedTableModel().getBeansList().get(beanPosition);

        int xPathLength = XmlMapUtil.getXPathLength(bean.getXPath());

        if (getExtendedTableModel() != null) {
            for (int i = 0; i < getExtendedTableModel().getBeansList().size(); i++) {
                if (i == beanPosition) {
                    continue;
                }
                TreeSchemaTableEntry entry = getExtendedTableModel().getBeansList().get(i);
                int pathLength = XmlMapUtil.getXPathLength(entry.getXPath());
                if (entry.getName() != null && entry.getName().equals(newValue) && xPathLength == pathLength) {
                    isValidName = false;
                    return "Name alrady existed";
                }
            }
        }
        return null;
    }

    class XmlCellModifier extends DefaultCellModifier {

        private TableViewerCreator tableViewerCreator;

        public XmlCellModifier(TableViewerCreator tableViewerCreator) {
            super(tableViewerCreator);
            this.tableViewerCreator = tableViewerCreator;
        }

        @Override
        public Object getValue(Object bean, String idColumn) {
            if (!ID_COLUMN_XPATH.equals(idColumn)) {
                return super.getValue(bean, idColumn);
            } else {
                TableViewerCreatorColumn column = tableViewerCreator.getColumn(idColumn);
                ModifiedObjectInfo modifiedObjectInfo = this.tableViewerCreator.getModifiedObjectInfo();
                modifiedObjectInfo.setCurrentModifiedBean(bean);
                modifiedObjectInfo.setCurrentModifiedColumn(column);
                modifiedObjectInfo.setCurrentModifiedIndex(this.tableViewerCreator.getInputList().indexOf(bean));

                Object returnValue = null;
                if (column.getColumnCellModifier() != null) {
                    returnValue = column.getColumnCellModifier().getValue(bean);
                }
                if (returnValue == null) {
                    Object value = AccessorUtils.get(bean, column);

                    if (column.getCellEditorValueAdapter() != null) {
                        returnValue = column.getCellEditorValueAdapter().getCellEditorTypedValue(column.getCellEditor(), value);
                    } else {
                        returnValue = value;
                    }
                    if (returnValue == null && column.getDefaultInternalValue() != null) {
                        returnValue = column.getDefaultInternalValue();
                    }
                }
                modifiedObjectInfo.setOriginalPropertyBeanValue(returnValue);
                modifiedObjectInfo.setPreviousPropertyBeanValue(returnValue);
                return returnValue;
            }
        }
    }
}
