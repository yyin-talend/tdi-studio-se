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
package org.talend.sqlbuilder.erdiagram.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.CheckBox;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.ui.swt.colorstyledtext.ColorManager;
import org.talend.commons.ui.swt.colorstyledtext.ColorStyledText;
import org.talend.core.CorePlugin;
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
import org.talend.sqlbuilder.ui.ISQLBuilderDialog;
import org.talend.sqlbuilder.util.UIUtils;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ErDiagramComposite extends SashForm {

    private List<RepositoryNode> nodes;

    private ErdiagramDiagramEditor editor;

    private StyledText sqlText;

    private final String language = "tsql"; //$NON-NLS-1$

    /**
     * admin ErDiagramComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ErDiagramComposite(Composite parent, int style) {
        super(parent, style);
        nodes = new ArrayList<RepositoryNode>();
    }

    /**
     * admin Comment method "addErDiagramEditor".
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void addErDiagramEditor() {
        GridData gridData = new GridData(GridData.FILL_BOTH);
        this.setLayoutData(gridData);

        GridLayout layout = new GridLayout();
        layout.verticalSpacing = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginBottom = 0;
        layout.marginTop = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 1;

        this.setLayout(layout);

        editor = new ErdiagramDiagramEditor();
        editor.createPartControl(this);
        editor.getViewer().setContents(createErDiagram());
        Control control = editor.getGraphicalControl();
        if (control != null) {
            control.setParent(this);
            control.setLayoutData(gridData);
        }
        layout = new GridLayout();
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginBottom = 0;
        layout.marginTop = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 30;
        ColorManager colorManager = new ColorManager(CorePlugin.getDefault().getPreferenceStore());
        int textstyle = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
        sqlText = new ColorStyledText(this, textstyle, colorManager, language);
        sqlText.setLayoutData(gridData);
        sqlText.setText("");
        sqlText.setBackground(getBackground());
        // sqlText.addMouseListener(new MouseAdapter() {
        //
        // /*
        // * (non-Javadoc)
        // *
        // * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
        // */
        // @Override
        // public void mouseDown(MouseEvent e) {
        // super.mouseDown(e);
        // sqlText.setText(getSqlStatement());
        // isModified = true;
        // }
        //
        // });

    }

    /**
     * Getter for sqlText.
     * 
     * @return the sqlText
     */
    public String getSqlText() {
        String text = sqlText.getText();
        if ("".equals(text)) {
            if (!"".equals(getSqlStatement())) {
                return getSqlStatement();
            }
        }
        return text;
    }

    /**
     * Sets the sqlText.
     * 
     * @param sqlText the sqlText to set
     */
    public void setSqlText(String sqlText) {
        if (sqlText != null) {
            this.sqlText.setText(sqlText);
            this.sqlText.setVisible(true);

        }
    }

    private ErDiagram erDiagram;

    /**
     * admin Comment method "createErDiagram".
     * 
     * @return
     */
    private ErDiagram createErDiagram() {
        erDiagram = new ErDiagram();
        erDiagram.setErDiagramComposite(this);
        IRunnableWithProgress progress = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask("", IProgressMonitor.UNKNOWN);
                try {
                    List<MetadataColumn> selectedColumns = new ArrayList<MetadataColumn>();
                    List<MetadataTable> tables = EMFRepositoryNodeManager.getInstance().getTables(getNodes(), selectedColumns);

                    erDiagram.setMetadataTables(tables);
                    List<String[]> fks = EMFRepositoryNodeManager.getInstance().getPKFromTables(tables);
                    for (MetadataTable metadataTable : tables) {
                        Table table = new Table();
                        table.setMetadataTable(metadataTable, selectedColumns);
                        table.setErDiagram(erDiagram);
                        erDiagram.addTable(table);
                    }
                    erDiagram.setRelations(fks);

                } finally {
                    monitor.done();
                }
            }
        };
        UIUtils.runWithProgress(progress, true, dialog.getProgressMonitor(), dialog.getShell());
        return erDiagram;
    }

    private ISQLBuilderDialog dialog;

    public void setDialog(ISQLBuilderDialog dialog) {
        this.dialog = dialog;
    }

    private String getCurrentDbType() {
        DatabaseConnection connection = (DatabaseConnection) ((ConnectionItem) rootNode.getObject().getProperty().getItem())
                .getConnection();
        return connection.getDatabaseType();
    }

    private String getSchema() {
        DatabaseConnection connection = (DatabaseConnection) ((ConnectionItem) rootNode.getObject().getProperty().getItem())
                .getConnection();
        return connection.getSchema();
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private String getSqlStatement() {
        String sql = ""; //$NON-NLS-1$
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
                        if (getCurrentDbType().equals("PostgreSQL")) { //$NON-NLS-1$
                            if (!"".equals(getSchema())) {
                                tables.add("\"" + getSchema() + "\".\"" + table.getElementName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
                            } else {
                                tables.add("\"" + table.getElementName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                        } else {
                            tables.add(table.getElementName());
                        }
                        for (Object obj : tablePart.getChildren()) {
                            if (obj instanceof ColumnPart) {
                                ColumnPart columnPart = (ColumnPart) obj;
                                Column column = (Column) columnPart.getModel();
                                CheckBox isSelected = columnPart.getPrimativeFigure().getFigureCustomColumnIsSelectedFigure();
                                if (isSelected != null && isSelected.isSelected() && !column.getElementName().equals("*")) { //$NON-NLS-1$
                                    if (getCurrentDbType().equals("PostgreSQL")) { //$NON-NLS-1$
                                        columns.add("\"" + table.getElementName() + "\".\"" + column.getElementName() + "\"");
                                    } else {
                                        columns.add(table.getElementName() + "." + column.getElementName()); //$NON-NLS-1$
                                    }
                                }
                                for (Relation rel : (List<Relation>) column.getOutputs()) {
                                    Column source = rel.getSource();
                                    Column target = rel.getTarget();
                                    if (getCurrentDbType().equals("PostgreSQL")) {
                                        String where1 = "\"" + source.getTable().getElementName() + "\".\""
                                                + source.getElementName() + "\"=\"" + target.getTable().getElementName()
                                                + "\".\"" + target.getElementName() + "\"";
                                        if (!wheres.contains(where1)) {
                                            wheres.add(where1);
                                        }
                                    } else {
                                        String where1 = source.getTable().getElementName() + "." + source.getElementName() + "="
                                                + target.getTable().getElementName() + "." + target.getElementName();
                                        if (!wheres.contains(where1)) {
                                            wheres.add(where1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        sql = getSelectStatement(tables, columns, wheres);
        if (sql.endsWith(",")) { //$NON-NLS-1$
            return sql.substring(0, sql.length() - 1);
        } else if (sql.endsWith(" and ")) { //$NON-NLS-1$
            return sql.substring(0, sql.length() - 5);
        }
        return ""; //$NON-NLS-1$

    }

    /**
     * admin Comment method "getSelectStatement".
     * 
     * @param tables
     * @param columns
     * @param wheres
     * @return
     */
    private String getSelectStatement(List<String> tables, List<String> columns, List<String> wheres) {
        String sql = ""; //$NON-NLS-1$
        if (tables.isEmpty() || columns.isEmpty()) {
            return sql;
        }
        sql = "select "; //$NON-NLS-1$
        for (String string : columns) {
            sql = sql + string + ","; //$NON-NLS-1$
        }
        sql = sql.substring(0, sql.length() - 1) + " \nfrom "; //$NON-NLS-1$
        for (String string : tables) {
            sql = sql + string + ","; //$NON-NLS-1$
        }
        if (!wheres.isEmpty()) {
            sql = sql.substring(0, sql.length() - 1) + " \nwhere "; //$NON-NLS-1$
            for (String string : wheres) {
                sql = sql + string + " and "; //$NON-NLS-1$
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

    private RepositoryNode rootNode;

    public void setRootNode(RepositoryNode root) {
        if (root == null && !getNodes().isEmpty()) {
            this.rootNode = SQLBuilderRepositoryNodeManager.getRoot(getNodes().get(0));

        } else {
            this.rootNode = root;
        }
    }

    public void updateNodes(List<RepositoryNode> nodes, String sqlText) {
        this.nodes = nodes;
        this.sqlText.setText(sqlText);
        editor.getViewer().setContents(createErDiagram());
    }

    private boolean isModified = false;

    /**
     * Getter for isModified.
     * 
     * @return the isModified
     */
    public boolean isModified() {
        return this.isModified;
    }

    /**
     * Sets the isModified.
     * 
     * @param isModified the isModified to set
     */
    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    public ErdiagramDiagramEditor getEditor() {
        return this.editor;
    }

    public ErDiagram getErDiagram() {
        return this.erDiagram;
    }

    public void clearAll() {
        List<Table> tables = new ArrayList<Table>();
        tables.addAll(erDiagram.getTables());
        for (Table table : tables) {
            erDiagram.removeTable(table);
        }
        this.sqlText.setText("");
    }

    /**
     * qzhang Comment method "updateSql".
     */
    public void updateSql() {
        setSqlText(getSqlStatement());
    }

    
    public ISQLBuilderDialog getDialog() {
        return this.dialog;
    }
    
}
