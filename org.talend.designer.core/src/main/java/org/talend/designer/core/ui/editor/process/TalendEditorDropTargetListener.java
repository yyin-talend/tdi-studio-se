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
package org.talend.designer.core.ui.editor.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Canvas;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.RegExFileConnectionItem;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.cmd.ChangeValuesFromRepository;
import org.talend.designer.core.ui.editor.cmd.CreateNodeContainerCommand;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.cmd.RepositoryChangeMetadataCommand;
import org.talend.designer.core.ui.editor.cmd.RepositoryChangeQueryCommand;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;

/**
 * Performs a native Drop for the talendEditor. see feature
 * 
 * $Id: TalendEditorDropTargetListener.java 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class TalendEditorDropTargetListener implements TransferDropTargetListener {

    private TalendEditor editor;

    /**
     * TalendEditorDropTargetListener constructor comment.
     * 
     * @param editor
     */
    public TalendEditorDropTargetListener(TalendEditor editor) {
        this.editor = editor;
    }

    public Transfer getTransfer() {
        return LocalSelectionTransfer.getTransfer();
    }

    public boolean isEnabled(DropTargetEvent event) {
        return true;
    }

    public void dragEnter(DropTargetEvent event) {

    }

    public void dragLeave(DropTargetEvent event) {

    }

    public void dragOperationChanged(DropTargetEvent event) {

    }

    public void dragOver(DropTargetEvent event) {
        // when the job that selected is the same one in the current editor, the drag event should be disabled.
        IStructuredSelection selection = getSelection();
        if (selection.size() != 1) {
            return;
        }

        RepositoryNode sourceNode = (RepositoryNode) selection.getFirstElement();
        if (equalsJobInCurrentEditor(sourceNode)) {
            event.detail = DND.DROP_NONE;
        }
    }

    private boolean equalsJobInCurrentEditor(RepositoryNode sourceNode) {
        Item item = sourceNode.getObject().getProperty().getItem();
        if (item instanceof ProcessItem) {
            return editor.getProcess().getProperty().getItem().equals(item);
        }
        return false;
    }

    private IStructuredSelection getSelection() {
        LocalSelectionTransfer transfer = (LocalSelectionTransfer) getTransfer();
        IStructuredSelection selection = (IStructuredSelection) transfer.getSelection();
        return selection;
    }

    /**
     * Used to store data temporarily. <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
     * 
     */
    class TempStore {

        // This is the element that user select in the repositoryView.
        RepositoryNode seletetedNode = null;

        IComponent component;

    }

    public void drop(DropTargetEvent event1) {
        boolean isInput = event1.detail == DND.DROP_MOVE;
        List<TempStore> list = new ArrayList<TempStore>();

        IStructuredSelection selection = getSelection();
        for (Object obj : selection.toArray()) {
            RepositoryNode sourceNode = (RepositoryNode) obj;
            if (equalsJobInCurrentEditor(sourceNode)) {
                continue;
            }

            Item item = sourceNode.getObject().getProperty().getItem();
            if (!(item instanceof ConnectionItem) && !(item instanceof ProcessItem)) {
                continue;
            }

            IComponent component = getCorrespondingComponent(item, isInput);
            if (component != null) {
                TempStore store = new TempStore();
                store.seletetedNode = sourceNode;
                store.component = component;
                list.add(store);
            }
        }

        org.eclipse.swt.graphics.Point swtLocation = new org.eclipse.swt.graphics.Point(event1.x, event1.y);
        Canvas canvas = (Canvas) editor.getViewer().getControl();

        /*
         * translate to Canvas coordinate
         */
        swtLocation = canvas.toControl(swtLocation);
        org.eclipse.swt.graphics.Point size = canvas.getSize();
        /*
         * translate to Viewport coordinate with zoom
         */
        org.eclipse.draw2d.geometry.Point draw2dPosition = new org.eclipse.draw2d.geometry.Point(swtLocation.x, swtLocation.y);

        /*
         * calcule the view port position. Take into acounte the scroll position
         */
        ProcessPart part = (ProcessPart) editor.getViewer().getRootEditPart().getRoot().getChildren().get(0);

        IFigure targetFigure = part.getFigure();
        translateAbsolateToRelative(targetFigure, draw2dPosition);

        // creates every node
        for (Iterator<TempStore> iter = list.iterator(); iter.hasNext();) {
            TempStore store = iter.next();

            RepositoryNode selectedNode = store.seletetedNode;
            IComponent element = store.component;
            Node node = new Node(element);

            NodeContainer nc = new NodeContainer(node);

            // create the node on the design sheet
            new CreateNodeContainerCommand(editor.getProcess(), nc, draw2dPosition).execute();

            // initialize the propertiesView

            List<Command> commands = createRefreshingPropertiesCommand(selectedNode, node);
            for (Command command : commands) {
                command.execute();
            }

            draw2dPosition = draw2dPosition.getCopy();
            draw2dPosition.x += TalendEditor.GRID_SIZE;
            draw2dPosition.y += TalendEditor.GRID_SIZE;
        }

    }

    /**
     * DOC bqian Comment method "createRefreshingPropertiesCommand".
     * 
     * @param selectedNode
     * @param node
     */
    private List<Command> createRefreshingPropertiesCommand(RepositoryNode selectedNode, Node node) {
        List<Command> list = new ArrayList<Command>();
        if (selectedNode.getObject().getProperty().getItem() instanceof ConnectionItem) {
            node.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.REPOSITORY);
            node.setPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName(), selectedNode.getObject().getProperty()
                    .getId());

            ConnectionItem connectionItem = (ConnectionItem) selectedNode.getObject().getProperty().getItem();

            IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
            Map<String, List<String>> tablesMap = new HashMap<String, List<String>>();

            Map<String, List<String>> queriesMap = new HashMap<String, List<String>>();
            Map<String, IMetadataTable> repositoryTableMap = new HashMap<String, IMetadataTable>();

            List<String> tableNamesList = new ArrayList<String>();
            List<String> tableValuesList = new ArrayList<String>();
            List<String> queryStoreNameList = new ArrayList<String>();
            List<String> queryStoreValuesList = new ArrayList<String>();
            Connection connection = (Connection) connectionItem.getConnection();
            if (!connection.isReadOnly()) {
                for (Object tableObj : connection.getTables()) {
                    org.talend.core.model.metadata.builder.connection.MetadataTable table;

                    table = (org.talend.core.model.metadata.builder.connection.MetadataTable) tableObj;

                    if (factory.getStatus(connectionItem) != ERepositoryStatus.DELETED) {
                        if (!factory.isDeleted(table)) {
                            String name = getRepositoryAliasName(connectionItem) + ":" //$NON-NLS-1$
                                    + connectionItem.getProperty().getLabel() + " - " + table.getLabel(); //$NON-NLS-1$
                            String value = connectionItem.getProperty().getId() + " - " + table.getLabel(); //$NON-NLS-1$
                            IMetadataTable newTable = ConvertionHelper.convert(table);
                            repositoryTableMap.put(value, newTable);
                            addOrderDisplayNames(tableValuesList, tableNamesList, value, name);
                        }
                    }
                }
            }
            tablesMap.put(connectionItem.getProperty().getId(), tableValuesList);
            IElementParameter repositorySchemaTypeParameter = node.getElementParameter(EParameterName.REPOSITORY_SCHEMA_TYPE
                    .getName());
            repositorySchemaTypeParameter.setListItemsValue(tableValuesList.toArray(new String[0]));
            if (connection instanceof DatabaseConnection && !connection.isReadOnly()) {
                DatabaseConnection dbConnection = (DatabaseConnection) connection;
                QueriesConnection queriesConnection = dbConnection.getQueries();
                if (queriesConnection != null) {
                    List<Query> qs = queriesConnection.getQuery();
                    for (Query query : qs) {
                        String name = getRepositoryAliasName(connectionItem) + ":" //$NON-NLS-1$
                                + connectionItem.getProperty().getLabel() + " - " + query.getLabel(); //$NON-NLS-1$
                        String value = connectionItem.getProperty().getId() + " - " + query.getLabel(); //$NON-NLS-1$
                        addOrderDisplayNames(queryStoreValuesList, queryStoreNameList, value, name);
                    }
                }
            }
            queriesMap.put(connectionItem.getProperty().getId(), queryStoreValuesList);

            // command used to set property type
            ChangeValuesFromRepository command1 = new ChangeValuesFromRepository(node, connectionItem.getConnection(),
                    EParameterName.REPOSITORY_PROPERTY_TYPE.getName(), selectedNode.getObject().getProperty().getId());

            command1.setMaps(tablesMap, queriesMap, repositoryTableMap);
            list.add(command1);

            // command used to set metadata
            if (selectedNode.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_CON_TABLE) {
                RepositoryObject object = (RepositoryObject) selectedNode.getObject();
                MetadataTable table = (MetadataTable) object.getAdapter(MetadataTable.class);
                String value = connectionItem.getProperty().getId() + " - " + object.getLabel(); //$NON-NLS-1$
                RepositoryChangeMetadataCommand command2 = new RepositoryChangeMetadataCommand(node,
                        EParameterName.REPOSITORY_SCHEMA_TYPE.getName(), value, repositoryTableMap.get(value));
                list.add(command2);
            }

            // command used to set query
            if (selectedNode.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_CON_QUERY) {
                RepositoryObject object = (RepositoryObject) selectedNode.getObject();
                Query query = (Query) object.getAdapter(Query.class);
                String value = connectionItem.getProperty().getId() + " - " + object.getLabel(); //$NON-NLS-1$
                RepositoryChangeQueryCommand command3 = new RepositoryChangeQueryCommand(node, query,
                        EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName(), value);
                list.add(command3);
            }
        } else if (selectedNode.getObject().getProperty().getItem() instanceof ProcessItem) {
            ProcessItem processItem = (ProcessItem) selectedNode.getObject().getProperty().getItem();
            // command used to set job
            String value = processItem.getProperty().getLabel();
            PropertyChangeCommand command4 = new PropertyChangeCommand(node, EParameterName.PROCESS_TYPE_PROCESS.getName(), value);
            list.add(command4);
        }

        return list;
    }

    private String getRepositoryAliasName(ConnectionItem connectionItem) {
        ERepositoryObjectType repositoryObjectType = ERepositoryObjectType.getItemType(connectionItem);
        String aliasName = repositoryObjectType.getAlias();
        Connection connection = (Connection) connectionItem.getConnection();
        if (connection instanceof DatabaseConnection) {
            String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, "TYPE"); //$NON-NLS-1$
            aliasName += " (" + currentDbType + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return aliasName;
    }

    private void addOrderDisplayNames(List<String> connectionValuesList, List<String> connectionStringList, String key,
            String name) {
        int i = 0;

        for (; i < connectionStringList.size(); i++) {
            String string = connectionStringList.get(i);
            if (name.compareTo(string) < 0) {
                connectionStringList.add(i, name);
                connectionValuesList.add(i, key);
                break;
            }
        }
        if (connectionStringList.size() == 0 || i == connectionStringList.size()) {
            connectionStringList.add(name);
            connectionValuesList.add(key);
        }
    }

    public void dropAccept(DropTargetEvent event) {
    }

    private IComponent getCorrespondingComponent(Item item, boolean input) {
        EDatabaseComponentName name = EDatabaseComponentName.getCorrespondingComponentName(item);
        String componentName = null;
        if (input) {
            componentName = name.getInputComponentName();
        } else {
            componentName = name.getOutPutComponentName();
        }
        IComponent component = editor.getComponent(componentName);
        return component;
    }

    public void translateAbsolateToRelative(IFigure owner, Translatable t) {
        owner.translateToRelative(t);

        Rectangle bounds = owner.getBounds();
        t.performTranslate(-bounds.x, -bounds.y);
    }

    /**
     * DOC bqian TalendEditor class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
     * 
     */
    enum EDatabaseComponentName {
        // DATABASES
        DBMYSQL(DatabaseConnectionItem.class, "MYSQL", "tMysqlInput", "tMysqlOutput"),
        DBPSQL(DatabaseConnectionItem.class, "POSTGRESQL", "tPostgresqlInput", "tPostgresqlOutput"),
        DBORACLEFORSID(DatabaseConnectionItem.class, "ORACLE", "tOracleInput", "tOracleOutput"),

        DBORACLESN(DatabaseConnectionItem.class, "ORACLE", "tOracleInput", "tOracleOutput"),

        DBGODBC(DatabaseConnectionItem.class, "MSODBC", "tDBInput", "tDBOutput"),
        MSODBC(DatabaseConnectionItem.class, "MSODBC", "tDBInput", "tDBOutput"),
        IBMDB2(DatabaseConnectionItem.class, "IBMDB2", "tDB2Input", "tDB2Output"),
        SYBASEASE(DatabaseConnectionItem.class, "SYBASE", "tSybaseInput", "tSybaseOutput"),

        // this Sybase IQ not used.
        // SYBASEIQ(DatabaseConnectionItem.class,"SYBASE", "Sybase IQ", new Boolean(false), "SYBASE"),
        MSSQL(DatabaseConnectionItem.class, "MSODBC", "tMSSqlInput", "tMSSqlOutput"),
        // this don't use in Branch 2.0
        HSQL(DatabaseConnectionItem.class, "HSQLDB", "tHSQLDbInput", "tHSQLDbOutput"),
        JAVADB(DatabaseConnectionItem.class, "JAVADB", "tJavaDBInput", "tJavaDBOutput"),
        INGRES(DatabaseConnectionItem.class, "INGRES", "tIngresInput", "tIngresOutput"), // "INGRES"),
        INTERBASE(DatabaseConnectionItem.class, "Interbase", "tInterbaseInput", "tInterbaseOutput"), // "INTERBASE"),
        SQLITE(DatabaseConnectionItem.class, "SQLITE", "tSQLiteInput", "tSQLiteOutput"), // "SQLITE"),
        FIREBIRD(DatabaseConnectionItem.class, "FIREBIRD", "tFirebirdInput", "tFirebirdOutput"), // "FIREBIRD"),
        INFORMIX(DatabaseConnectionItem.class, "INFORMIX", "tInformixInput", "tInformixOutput"), // "INFORMIX");
        ACCESS(DatabaseConnectionItem.class, "ACCESS", "tAccessInput", "tAccessOutput"), // "ACCESS");
        TERADATA(DatabaseConnectionItem.class, "TERADATA", "tTeradataInput", "tTeradataOutput"), // "TERADATA");

        
        // FILES
        FILEDELIMITED(DelimitedFileConnectionItem.class, "tFileInputDelimited", "tFileOutputDelimited"),
        FILEPOSITIONAL(PositionalFileConnectionItem.class, "tFileInputPositional", "tFileOutputPositional"),
        FILEREGEX(RegExFileConnectionItem.class, "tFileInputRegex", null),
        FILEXML(XmlFileConnectionItem.class, "tFileInputXML", "tFileOutputXML"),

        // RunJob
        RunJob(ProcessItem.class, "tRunJob", "tRunJob");

        // DBORACLESN("ORACLE", "Oracle with service name", new Boolean(true), "ORACLE"),
        // DBGODBC(DatabaseConnectionItem.class,"MSODBC", "Generic ODBC", new Boolean(false), "MSODBC"),
        // DBMSODBC("Microsoft SQL (Odbc driver)", "Microsoft SQL Server (Odbc driver)", new Boolean(false), "MSODBC"),
        // DBIBMDB2("IBM DB2", "IBM DB2", new Boolean(false), "IBMDB2"),
        // DBSYBASEASE("SybaseASE", "Sybase ASE", new Boolean(false), "SYBASE"),
        //       
        // // this Sybase IQ not used.
        // DBSYBASEIQ("Sybase IQ", "Sybase IQ", new Boolean(false), "SYBASE"),
        // DBMSSQL("MSSQL", "Microsoft SQL Server", new Boolean(false), "MSODBC"),
        // // this don't use in Branch 2.0
        // DBHSQL("HSQL","HSQL",new Boolean(false),"HSQLDB"),
        // DBJAVADB("JavaDB", "JavaDB", new Boolean(false), "JAVADB"),
        // DBINGRES("Ingres", "Ingres", new Boolean(false), "INGRES"), // "INGRES"),
        // DBINTERBASE("Interbase", "Interbase", new Boolean(false), "Interbase"), // "INTERBASE"),
        // DBSQLITE("SQLite", "SQLite", new Boolean(false), "SQLITE"), // "SQLITE"),
        // DBFIREBIRD("FireBird", "FireBird", new Boolean(false), "FIREBIRD"), // "FIREBIRD"),
        // DBINFORMIX("Informix", "Informix", new Boolean(true), "INFORMIX"), // "INFORMIX");
        //
        // DBACCESS("Access", "Access", new Boolean(false), "ACCESS"); // "ACCESS");
        // JAVADB_EMBEDED("JavaDB Embeded", "JavaDB Embeded", new Boolean(false), "JAVADB"),
        // JAVADB_JCCJDBC("JavaDB JCCJDBC", "JavaDB JCCJDBC", new Boolean(false), "JAVADB"),
        // JAVADB_DERBYCLIENT("JavaDB DerbyClient", "JavaDB DerbyClient", new Boolean(false), "JAVADB");

        Class clazz;

        String inputComponentName;

        String outPutComponentName;

        String dbType;

        /**
         * Getter for clazz.
         * 
         * @return the clazz
         */
        public Class getMappingKey() {
            return this.clazz;
        }

        /**
         * Getter for dbType.
         * 
         * @return the dbType
         */
        public String getDbType() {
            return this.dbType;
        }

        /**
         * Getter for inputComponentName.
         * 
         * @return the inputComponentName
         */
        public String getInputComponentName() {
            return this.inputComponentName;
        }

        /**
         * Getter for outPutComponentName.
         * 
         * @return the outPutComponentName
         */
        public String getOutPutComponentName() {
            return this.outPutComponentName;
        }

        /**
         * Contructor for files.
         * 
         * @param clazz
         * @param inputComponentName
         * @param outPutComponentName
         */
        EDatabaseComponentName(Class clazz, String inputComponentName, String outPutComponentName) {
            this.clazz = clazz;
            this.inputComponentName = inputComponentName;
            this.outPutComponentName = outPutComponentName;
        }

        /**
         * Contructor for databases.
         * 
         * @param clazz
         * @param type
         * @param inputComponentName
         * @param outPutComponentName
         */
        EDatabaseComponentName(Class clazz, String dbType, String inputComponentName, String outPutComponentName) {
            this(clazz, inputComponentName, outPutComponentName);
            this.dbType = dbType;
        }

        public static EDatabaseComponentName getCorrespondingComponentName(Item item) {

            for (EDatabaseComponentName typeName : EDatabaseComponentName.values()) {
                if (typeName.getMappingKey().isAssignableFrom(item.getClass())) {

                    if (typeName.getMappingKey() == DatabaseConnectionItem.class) {
                        DatabaseConnectionItem dbItem = (DatabaseConnectionItem) item;
                        DatabaseConnection dbConnection = (DatabaseConnection) dbItem.getConnection();
                        if (typeName.getDbType().equals(dbConnection.getProductId())) {
                            return typeName;
                        }
                    } else {
                        return typeName;
                    }
                }
            }
            return DBMYSQL;
        }
    }
}
