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
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.behavior.ColumnCellModifier;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class OutputXmlTreeSchemaTableView extends XmlTreeSchemaTableView {

    public OutputXmlTreeSchemaTableView(ExtendedTableModel<TreeSchemaTableEntry> extendedTableModel, Composite parent) {
        super(extendedTableModel, parent, false, false);

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

    protected String validateNameSpace(String newValue) {
        if ((newValue.indexOf("(") != -1 || newValue.indexOf(")") != -1)
                && !newValue.equals(XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX)) {
            isValidName = false;
            return "Namespace Prefix is invalid";
        }
        return null;

    }

}
