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
package org.talend.sqlbuilder.erdiagram.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.CheckBox;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.erdiagram.ui.editor.ErdiagramDiagramEditor;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Column;
import org.talend.sqlbuilder.erdiagram.ui.nodes.ErDiagram;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Relation;
import org.talend.sqlbuilder.erdiagram.ui.nodes.Table;
import org.talend.sqlbuilder.erdiagram.ui.parts.ColumnPart;
import org.talend.sqlbuilder.erdiagram.ui.parts.ErDiagramPart;
import org.talend.sqlbuilder.erdiagram.ui.parts.TablePart;
import org.talend.sqlbuilder.repository.utility.EMFRepositoryNodeManager;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ErDiagramComposite extends Composite {

    private List<RepositoryNode> nodes;

    private ErdiagramDiagramEditor editor;

    private EMFRepositoryNodeManager nodeManager = new EMFRepositoryNodeManager();

    /**
     * DOC admin ErDiagramComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ErDiagramComposite(Composite parent, int style) {
        super(parent, style);
        nodes = new ArrayList<RepositoryNode>();
    }

    /**
     * DOC admin Comment method "addErDiagramEditor".
     */
    @SuppressWarnings("unchecked")
    private void addErDiagramEditor() {
        GridData gridData = new GridData(GridData.FILL_BOTH);
        this.setLayoutData(gridData);

        GridLayout layout = new GridLayout();
        this.setLayout(layout);

        editor = new ErdiagramDiagramEditor();
        editor.createPartControl(this);
        editor.getViewer().setContents(createErDiagram());
        Control control = editor.getGraphicalControl();
        if (control != null) {
            control.setParent(this);
        }
    }

    /**
     * DOC admin Comment method "createErDiagram".
     * 
     * @return
     */
    private ErDiagram createErDiagram() {
        ErDiagram erDiagram = new ErDiagram();
        erDiagram.setNodeManager(nodeManager);
        List<MetadataColumn> selectedColumns = new ArrayList<MetadataColumn>();
        List<MetadataTable> tables = nodeManager.getTables(getNodes(),selectedColumns);

        erDiagram.setMetadataTables(tables);
        List<String[]> fks = nodeManager.getPKFromTables(tables);
        for (MetadataTable metadataTable : tables) {
            Table table = new Table();
            table.setMetadataTable(metadataTable,selectedColumns);
            table.setErDiagram(erDiagram);
            erDiagram.addTable(table);
        }
        erDiagram.setRelations(fks);
        return erDiagram;
    }

    private String getCurrentDbType() {
        DatabaseConnection connection = (DatabaseConnection) ((ConnectionItem) SQLBuilderRepositoryNodeManager.getRoot(
                getNodes().get(0)).getObject().getProperty().getItem()).getConnection();
        return connection.getDatabaseType();
    }

    @SuppressWarnings("unchecked")
    public String getSqlStatement() {
        String sql = "";
        List<String> tables = new ArrayList<String>();
        List<String> columns = new ArrayList<String>();
        List<String> wheres = new ArrayList<String>();
        if (editor != null) {
            if (editor.getViewer().getContents() instanceof ErDiagramPart) {
                ErDiagramPart er = (ErDiagramPart) editor.getViewer().getContents();
                for (Object object : er.getChildren()) {
                    if (object instanceof TablePart) {
                        TablePart tablePart = (TablePart) object;
                        Table table = (Table) tablePart.getModel();
                        if (getCurrentDbType().equals("PostgreSQL")) {
                            tables.add("\"" + table.getElementName() + "\"");
                        } else {
                            tables.add(table.getElementName());
                        }
                        for (Object obj : tablePart.getChildren()) {
                            if (obj instanceof ColumnPart) {
                                ColumnPart columnPart = (ColumnPart) obj;
                                Column column = (Column) columnPart.getModel();
                                CheckBox isSelected = columnPart.getPrimativeFigure().getFigureCustomColumnIsSelectedFigure();
                                if (isSelected != null && isSelected.isSelected() && !column.getElementName().equals("*")) {
                                    if (getCurrentDbType().equals("PostgreSQL")) {
                                        columns.add("\"" + table.getElementName() + "\".\"" + column.getElementName() + "\"");
                                    } else {
                                        columns.add(table.getElementName() + "." + column.getElementName());
                                    }
                                }
                                for (Relation rel : (List<Relation>) column.getOutputs()) {
                                    Column source = rel.getSource();
                                    Column target = rel.getTarget();
                                    if (getCurrentDbType().equals("PostgreSQL")) {
                                        wheres.add("\"" + source.getTable().getElementName() + "\".\"" + source.getElementName()
                                                + "\"=\"" + target.getTable().getElementName() + "\".\""
                                                + target.getElementName() + "\"");
                                    } else {
                                        wheres.add(source.getTable().getElementName() + "." + source.getElementName() + "="
                                                + target.getTable().getElementName() + "." + target.getElementName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        sql = getSelectStatement(tables, columns, wheres);
        if (sql.endsWith(",")) {
            return sql.substring(0, sql.length() - 1) + ";";
        } else if (sql.endsWith("and ")) {
            return sql.substring(0, sql.length() - 4) + ";";
        }
        return "";

    }

    /**
     * DOC admin Comment method "getSelectStatement".
     * 
     * @param tables
     * @param columns
     * @param wheres
     * @return
     */
    private String getSelectStatement(List<String> tables, List<String> columns, List<String> wheres) {
        String sql = "";
        if (tables.isEmpty() || columns.isEmpty()) {
            return sql;
        }
        sql = "select ";
        for (String string : columns) {
            sql = sql + string + ",";
        }
        sql = sql.substring(0, sql.length() - 1) + " \nfrom ";
        for (String string : tables) {
            sql = sql + string + ",";
        }
        if (!wheres.isEmpty()) {
            sql = sql.substring(0, sql.length() - 1) + " \nwhere ";
            for (String string : wheres) {
                sql = sql + string + " and ";
            }
        }

        return sql;
    }

    public List<RepositoryNode> getNodes() {
        return this.nodes;
    }

    public void setNodes(List<RepositoryNode> nodes) {
        this.nodes = nodes;
        addErDiagramEditor();
    }

}
