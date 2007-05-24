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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.UniqueNodeNameGenerator;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.metadata.MetadataEmfFactory;
import org.talend.designer.core.model.metadata.MetadataUtils;
import org.talend.designer.core.model.process.DataProcess;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.DocumentRoot;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.JobType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.NoteType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.RequiredType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.core.model.utils.emf.talendfile.util.TalendFileResourceImpl;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.Node.Data;
import org.talend.designer.core.ui.editor.notes.Note;
import org.talend.designer.core.ui.preferences.StatsAndLogsConstants;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * The diagram will contain all elements (nodes, connections) The xml that describes the diagram will be saved from the
 * information of this class. <br/>
 * 
 * $Id$
 * 
 */
public class Process extends Element implements IProcess {

    // properties
    public static final String NODES = "nodes"; //$NON-NLS-1$

    public static final String NOTES = "notes"; //$NON-NLS-1$

    public static final String DEFAULT_CONNECTION_NAME = "row"; //$NON-NLS-1$

    protected List<Node> nodes = new ArrayList<Node>();

    protected List<Element> elem = new ArrayList<Element>();

    protected List<Note> notes = new ArrayList<Note>();

    private String name = new String(Messages.getString("Process.Job")); //$NON-NLS-1$

    private boolean activate = true;

    // list where is stored each unique name for the connections
    private List<String> uniqueConnectionNameList = new ArrayList<String>();

    // list where is stored each unique name for the nodes
    private List<String> uniqueNodeNameList = new ArrayList<String>();

    private boolean readOnly;

    private GraphicalViewer viewer = null;

    public static final Color READ_ONLY_COLOR = new Color(null, new RGB(0xE7, 0xE7, 0xE7));

    public static final Color READ_WRITE_COLOR = new Color(null, new RGB(255, 255, 255));

    private IContextManager contextManager;

    public static final int BREAKPOINT_STATUS = 1;

    public static final int ERROR_STATUS = 2;

    public static final int WARNING_STATUS = 4;

    private Property property;

    private boolean initDone = false;

    private IProcessor processor;

    public Process() {
        contextManager = new JobContextManager();
        createProcessParameters();
    }

    public Process(Property property) {
        this();
        this.property = property;
        init();
    }

    private void init() {
        if (!initDone) {
            setId(property.getId());
            setLabel(property.getLabel());
            setVersion(property.getVersion());
            setAuthor(property.getAuthor());
            setStatusCode(property.getStatusCode());
            if (getStatusCode() == null) {
                setStatusCode(""); //$NON-NLS-1$
            }
            initDone = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Process other = (Process) obj;
        if (!this.getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

    /**
     * Add all parameters for a process.
     */
    private void createProcessParameters() {
        createMainParameters();
        createStatsAndLogsParameters();
    }

    /**
     * This function will add quotes only if necessary for the stats & logs.
     * 
     * @param str
     * @return
     */
    private String addQuotes(String str) {
        // function?
        if (str.contains("(") && str.contains(")")) {
            return str;
        }

        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            // if the user already added quotes (anywhere) then don't add.
            if (str.contains("\"")) {
                return str;
            }
            break;
        default: // PERL
            // if the user already added quotes (anywhere) then don't add.
            if (str.contains("'")) {
                return str;
            }
        }
        return TalendTextUtils.addQuotes(str);
    }

    /**
     * crate parameters for tabbed page 'Stats & Logs'.
     */
    private void createStatsAndLogsParameters() {
        ElementParameter param;
        IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();

        param = new ElementParameter(this);
        param.setName(EParameterName.UPDATE_COMPONENTS.getName());
        param.setValue(Boolean.FALSE);
        param.setDisplayName(EParameterName.UPDATE_COMPONENTS.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        addElementParameter(param);

        // checks current language, if it is perl, set languageType to 0(default value), otherwise to 1.
        int languageType = 0;
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
            languageType = 1;
        }

        // on console
        param = new ElementParameter(this);
        param.setName("ON_CONSOLE_FLAG");
        param.setValue(Boolean.FALSE);
        param.setDisplayName(EParameterName.ON_CONSOLE_FLAG.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(1);
        addElementParameter(param);

        // on files
        param = new ElementParameter(this);
        param.setName("ON_FILE_FLAG"); // On files
        param.setValue(preferenceStore.getBoolean(StatsAndLogsConstants.ON_FILE_FLAG[languageType].getName()));
        param.setDisplayName(StatsAndLogsConstants.ON_FILE_FLAG[languageType].getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(2);
        addElementParameter(param);

        // file path
        param = new ElementParameter(this);
        param.setName("FILE_PATH"); // File path
        param.setValue(addQuotes(replaceSlash(preferenceStore.getString(StatsAndLogsConstants.FILE_PATH[languageType]
                .getName()))));
        param.setDisplayName(StatsAndLogsConstants.FILE_PATH[languageType].getDisplayName());
        param.setField(EParameterFieldType.DIRECTORY);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(3);
        addElementParameter(param);

        // stats file name
        param = new ElementParameter(this);
        param.setName("FILENAME_STATS"); // Stats file name
        param.setValue(addQuotes(preferenceStore
                .getString(StatsAndLogsConstants.FILENAME_STATS[languageType].getName())));
        param.setDisplayName(StatsAndLogsConstants.FILENAME_STATS[languageType].getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(4);

        addElementParameter(param);

        // split every ** rows
        /*
         * param = new ElementParameter(this); param.setName("ON_FIlES_START_FILE_SPLIT_ROWS");
         * param.setValue(Boolean.FALSE); param.setDisplayName("Split Rows"); param.setField(EParameterFieldType.CHECK);
         * param.setCategory(EComponentCategory.STATSANDLOGS); param.setNumRow(5); addElementParameter(param);
         */
        // rows
        // param = new ElementParameter(this);
        // param.setName("ON_FIlES_START_FILE_SPLIT_ROWS");
        // param.setValue(new String());
        // param.setField(EParameterFieldType);
        // param.setCategory(EComponentCategory.STATSANDLOGS);
        // param.setNumRow(5);
        // addElementParameter(param);
        // Log file name
        param = new ElementParameter(this);
        param.setName("FILENAME_LOGS");
        param
                .setValue(addQuotes(preferenceStore.getString(StatsAndLogsConstants.FILENAME_LOGS[languageType]
                        .getName())));
        param.setDisplayName(StatsAndLogsConstants.FILENAME_LOGS[languageType].getDisplayName()); // "Logs File Name"
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(6);
        addElementParameter(param);

        // split every ** rows
        /*
         * param = new ElementParameter(this); param.setName("ON_FIlES_LOG_FILE_SPLIT_ROWS_FLAG");
         * param.setValue(Boolean.FALSE); param.setDisplayName("Split Rows"); param.setField(EParameterFieldType.CHECK);
         * param.setCategory(EComponentCategory.STATSANDLOGS); param.setNumRow(7); addElementParameter(param);
         */
        // rows
        // param = new ElementParameter(this);
        // param.setName("ON_FIlES_LOG_FILE_SPLIT_ROWS");
        // param.setValue(new String());
        // param.setField(EParameterFieldType.TEXT);
        // param.setCategory(EComponentCategory.STATSANDLOGS);
        // param.setNumRow(8);
        // addElementParameter(param);
        // on database
        param = new ElementParameter(this);
        param.setName("ON_DATABASE_FLAG");
        param.setValue(preferenceStore.getBoolean(StatsAndLogsConstants.ON_DATABASE_FLAG[languageType].getName()));
        param.setDisplayName(StatsAndLogsConstants.ON_DATABASE_FLAG[languageType].getDisplayName()); // On Database
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(9);
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setName("PROPERTY_TYPE");
        param.setDisplayName(StatsAndLogsConstants.PROPERTY_TYPE[languageType].getDisplayName());
        param.setListItemsDisplayName(new String[] { EmfComponent.TEXT_BUILTIN, EmfComponent.TEXT_REPOSITORY });
        param.setListItemsDisplayCodeName(new String[] { EmfComponent.BUILTIN, EmfComponent.REPOSITORY });
        param.setListItemsValue(new String[] { EmfComponent.BUILTIN, EmfComponent.REPOSITORY });
        param.setValue(preferenceStore.getString(StatsAndLogsConstants.PROPERTY_TYPE[languageType].getName()));
        param.setNumRow(10);
        param.setField(EParameterFieldType.CLOSED_LIST);
        param.setRepositoryValue("DATABASE");
        addElementParameter(param);

        param = new ElementParameter(this);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setName(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
        param.setDisplayName(EParameterName.REPOSITORY_PROPERTY_TYPE.getDisplayName());
        param.setListItemsDisplayName(new String[] {});
        param.setListItemsValue(new String[] {});
        param.setNumRow(10);
        param.setField(EParameterFieldType.CLOSED_LIST);
        param.setValue(preferenceStore
                .getString(StatsAndLogsConstants.REPOSITORY_PROPERTY_TYPE[languageType].getName())); //$NON-NLS-1$
        param.setShow(false);
        param.setRequired(true);
        addElementParameter(param);

        // dbType
        param = new ElementParameter(this);
        param.setName("DB_TYPE");
        param.setValue(preferenceStore.getString(StatsAndLogsConstants.DB_TYPE[languageType].getName()));
        param.setDisplayName(EParameterName.PERL_DB_TYPE.getDisplayName()); // "DB Type");
        param.setField(EParameterFieldType.CLOSED_LIST);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        String[] strDisplay = null, strValue = null, strItems = null, strCodes = null;
        if (languageType == 0) {
            strDisplay = new String[] { "Generic ODBC", "MySQL", "Microsoft SQL Server (Odbc driver)", "Oracle",
                    "PostgreSQL", "IBM DB2", "Sybase", "Ingres" };
            strValue = new String[] { "tDBOutput", "tMysqlOutput", "tDBOutput", "tOracleOutput", "tPostgresqlOutput",
                    "tDB2Output", "tSybaseOutput", "tIngresOutput" };
            strItems = new String[] { RepositoryToComponentProperty.ODBC, RepositoryToComponentProperty.MYSQL,
                    RepositoryToComponentProperty.ODBC, RepositoryToComponentProperty.ORACLE,
                    RepositoryToComponentProperty.POSTGRESQL, RepositoryToComponentProperty.IBM_DB2,
                    RepositoryToComponentProperty.SYBASE, RepositoryToComponentProperty.INGRES };
            strCodes = new String[] { RepositoryToComponentProperty.ODBC, RepositoryToComponentProperty.MYSQL,
                    RepositoryToComponentProperty.ODBC, "OCLE", RepositoryToComponentProperty.POSTGRESQL,
                    RepositoryToComponentProperty.IBM_DB2, RepositoryToComponentProperty.SYBASE,
                    RepositoryToComponentProperty.INGRES };
        } else if (languageType == 1) {
            strDisplay = new String[] { "Generic ODBC", "MySQL", "Microsoft SQL Server", "Oracle", "PostgreSQL",
                    "IBM DB2", "Sybase", "Ingres" };
            strValue = new String[] { "tDBOutput", "tMysqlOutput", "tMSSqlOutput", "tOracleOutput",
                    "tPostgresqlOutput", "tDB2Output", "tSybaseOutput", "tIngresOutput" };
            strItems = new String[] { RepositoryToComponentProperty.ODBC, RepositoryToComponentProperty.MYSQL,
                    RepositoryToComponentProperty.SQL_SERVER, RepositoryToComponentProperty.ORACLE,
                    RepositoryToComponentProperty.POSTGRESQL, RepositoryToComponentProperty.IBM_DB2,
                    RepositoryToComponentProperty.SYBASE, RepositoryToComponentProperty.INGRES };
            strCodes = new String[] { RepositoryToComponentProperty.ODBC, RepositoryToComponentProperty.MYSQL,
                    RepositoryToComponentProperty.SQL_SERVER, "OCLE", RepositoryToComponentProperty.POSTGRESQL,
                    RepositoryToComponentProperty.IBM_DB2, RepositoryToComponentProperty.SYBASE,
                    RepositoryToComponentProperty.INGRES };
        } else {
            strDisplay = new String[0];
            strValue = new String[0];
            strItems = new String[0];
            strCodes = new String[0];
        }

        param.setListItemsDisplayName(strDisplay);
        param.setListItemsValue(strValue);
        param.setListRepositoryItems(strItems);
        param.setListItemsDisplayCodeName(strCodes);
        param.setNumRow(11);
        param.setRepositoryValue("TYPE");
        param.setRequired(true);
        addElementParameter(param);

        // host
        param = new ElementParameter(this);
        param.setName("HOST");
        param.setValue(addQuotes(preferenceStore.getString(StatsAndLogsConstants.HOST[languageType].getName())));
        param.setDisplayName(StatsAndLogsConstants.HOST[languageType].getDisplayName()); // Host
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(11);
        param.setRepositoryValue("SERVER_NAME");
        addElementParameter(param);

        // port
        param = new ElementParameter(this);
        param.setName("PORT");
        param.setValue(addQuotes(preferenceStore.getString(StatsAndLogsConstants.PORT[languageType].getName())));
        param.setDisplayName(StatsAndLogsConstants.PORT[languageType].getDisplayName()); // Port
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(11);
        param.setRepositoryValue("PORT");
        addElementParameter(param);

        // dbName
        param = new ElementParameter(this);
        param.setName("DBNAME");// DBNAME
        param.setValue(addQuotes(preferenceStore.getString(StatsAndLogsConstants.DBNAME[languageType].getName())));
        param.setDisplayName(StatsAndLogsConstants.DBNAME[languageType].getDisplayName()); // "DB Name"
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(12);
        param.setRepositoryValue("SID");
        addElementParameter(param);

        // schema
        param = new ElementParameter(this);
        param.setName("SCHEMA_DB"); // SCHEMA_DB
        param.setValue(addQuotes(preferenceStore.getString(StatsAndLogsConstants.SCHEMA_DB[languageType].getName())));
        param.setDisplayName(StatsAndLogsConstants.SCHEMA_DB[languageType].getDisplayName());// "Schema"
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(12);
        String showIfStr = "(DB_TYPE=='" + "OCLE" + "') or (DB_TYPE=='" + "POSTGRESQL" + "')";
        param.setShowIf(showIfStr);

        param.setRepositoryValue("SCHEMA");
        addElementParameter(param);

        // username
        param = new ElementParameter(this);
        param.setName("USER");
        param.setValue(addQuotes(preferenceStore.getString(StatsAndLogsConstants.USER[languageType].getName())));
        param.setDisplayName(StatsAndLogsConstants.USER[languageType].getDisplayName()); // User
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(13);
        param.setRequired(true);
        param.setRepositoryValue("USERNAME");
        addElementParameter(param);

        // password
        param = new ElementParameter(this);
        param.setName("PASS"); // Pass
        param.setValue(addQuotes(preferenceStore.getString(StatsAndLogsConstants.PASS[languageType].getName())));
        param.setDisplayName(StatsAndLogsConstants.PASS[languageType].getDisplayName()); // "Password"
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(13);
        param.setRequired(true);
        param.setRepositoryValue("PASSWORD");
        addElementParameter(param);

        // Stats table
        param = new ElementParameter(this);
        param.setName("TABLE_STATS"); // "TABLE_STATS"
        param.setValue(addQuotes(preferenceStore.getString(StatsAndLogsConstants.TABLE_STATS[languageType].getName())));
        param.setDisplayName(StatsAndLogsConstants.TABLE_STATS[languageType].getDisplayName());// "Stats Table");
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(13);
        addElementParameter(param);

        /*
         * // Stats table button param = new ElementParameter(this); param.setName("OPEN_STATS_TABLE");
         * param.setField(EParameterFieldType.QUERYSTORE_TYPE); param.setCategory(EComponentCategory.STATSANDLOGS);
         * 
         * String[] str3 = new String[] { "Built-in", "repository" }; param.setListItemsDisplayName(str3);
         * 
         * param.setNumRow(13); addElementParameter(param);
         */
        // Log table
        param = new ElementParameter(this);
        param.setName("TABLE_LOGS");
        param.setValue(addQuotes(preferenceStore.getString(StatsAndLogsConstants.TABLE_LOGS[languageType].getName())));
        param.setDisplayName(StatsAndLogsConstants.TABLE_LOGS[languageType].getDisplayName()); // "Log Table");
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(13);
        addElementParameter(param);

        // Catch runtime errors
        param = new ElementParameter(this);
        param.setName("CATCH_RUNTIME_ERRORS");
        param.setValue(preferenceStore.getBoolean(StatsAndLogsConstants.CATCH_RUNTIME_ERRORS[languageType].getName()));
        param.setDisplayName(StatsAndLogsConstants.CATCH_RUNTIME_ERRORS[languageType].getDisplayName());// "Catch
        // runtime
        // errors");
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(15);
        addElementParameter(param);

        // Catch user errors
        param = new ElementParameter(this);
        param.setName("CATCH_USER_ERRORS");
        param.setValue(preferenceStore.getBoolean(StatsAndLogsConstants.CATCH_USER_ERRORS[languageType].getName()));
        param.setDisplayName(StatsAndLogsConstants.CATCH_USER_ERRORS[languageType].getDisplayName());// "Catch user
        // errors");
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(15);
        addElementParameter(param);

        // Catch user warning
        param = new ElementParameter(this);
        param.setName("CATCH_USER_WARNING");
        param.setValue(preferenceStore.getBoolean(StatsAndLogsConstants.CATCH_USER_WARNING[languageType].getName()));
        param.setDisplayName(StatsAndLogsConstants.CATCH_USER_WARNING[languageType].getDisplayName());// "Catch user
        // warning");
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(15);
        addElementParameter(param);

        // Catch realtime statistics
        param = new ElementParameter(this);
        param.setName("CATCH_REALTIME_STATS");
        param.setValue(preferenceStore.getBoolean(StatsAndLogsConstants.CATCH_REALTIME_STATS[languageType].getName()));
        param.setDisplayName(StatsAndLogsConstants.CATCH_REALTIME_STATS[languageType].getDisplayName()); // "Catch
        // realtime
        // statistics");
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(15);
        addElementParameter(param);
    }

    /**
     * Creates parameters for tabbed page 'Main'.
     */
    private void createMainParameters() {
        ElementParameter param;

        param = new ElementParameter(this);
        param.setName(EParameterName.COMP_DEFAULT_FILE_DIR.getName());
        param.setCategory(EComponentCategory.MAIN);
        param.setField(EParameterFieldType.DIRECTORY);
        param.setDisplayName(EParameterName.COMP_DEFAULT_FILE_DIR.getDisplayName());
        param.setNumRow(99);
        param.setShow(false);
        param.setValue(DesignerPlugin.getDefault().getPluginPreferences().getString(
                TalendDesignerPrefConstants.COMP_DEFAULT_FILE_DIR));
        addElementParameter(param);
    }

    /**
     * Add a new node to the diagram.
     * 
     * @param node
     */
    public void addNodeContainer(final NodeContainer nodeContainer) {
        elem.add(nodeContainer);
        elem.add(nodeContainer.getNode());
        nodes.add(nodeContainer.getNode());
        fireStructureChange(NODES, elem);
    }

    /**
     * Remove a node to the diagram.
     * 
     * @param node
     */
    public void removeNodeContainer(final NodeContainer nodeContainer) {
        removeUniqueNodeName(nodeContainer.getNode().getUniqueName());
        nodes.remove(nodeContainer.getNode());
        elem.remove(nodeContainer.getNode());
        elem.remove(nodeContainer);
        fireStructureChange(NODES, elem);
    }

    /**
     * Get the list of all elements, Node and Connection.
     * 
     * @return
     */
    public List getElements() {
        return this.elem;
    }

    public List<? extends INode> getGraphicalNodes() {
        return this.nodes;
    }

    public List<? extends INode> getGeneratingNodes() {
        DataProcess.buildFromGraphicalProcess(this, nodes);
        return DataProcess.getNodeList();
    }

    /*
     * public double getZoom() { return zoom; }
     */
    private void retrieveAttachedViewer() {
        IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editorPart instanceof MultiPageTalendEditor) {
            viewer = ((MultiPageTalendEditor) editorPart).getTalendEditor().getViewer();
        }
    }

    public void setViewer(GraphicalViewer viewer) {
        this.viewer = viewer;
    }

    /**
     * Returns true if the grid is enabled.
     * 
     * @return
     */
    public boolean isGridEnabled() {
        if (viewer == null) {
            retrieveAttachedViewer();
            if (viewer == null) {
                return (Boolean) viewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
            }
        } else {
            return (Boolean) viewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
        }
        return false;
    }

    /**
     * Returns true if the SnapToGeometry is enabled.
     * 
     * @return
     */
    public boolean isSnapToGeometryEnabled() {
        if (viewer == null) {
            retrieveAttachedViewer();
            if (viewer == null) {
                return (Boolean) viewer.getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
            }
        } else {
            return (Boolean) viewer.getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
        }
        return false;
    }

    public static IMetadataTable getMetadataFromRepository(String metaRepositoryName) {
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        List<ConnectionItem> metadataConnectionsItem = null;
        List<String> repositoryList = new ArrayList<String>();
        IMetadataTable metaToReturn = null;
        try {
            metadataConnectionsItem = factory.getMetadataConnectionsItem();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        if (metadataConnectionsItem != null) {
            for (ConnectionItem connectionItem : metadataConnectionsItem) {
                org.talend.core.model.metadata.builder.connection.Connection connection;
                connection = (org.talend.core.model.metadata.builder.connection.Connection) connectionItem
                        .getConnection();
                for (Object tableObj : connection.getTables()) {
                    MetadataTable table = (MetadataTable) tableObj;
                    if (!factory.isDeleted(table)) {
                        String name = connectionItem.getProperty().getId() + " - " + table.getLabel(); //$NON-NLS-1$
                        if (name.equals(metaRepositoryName)) {
                            metaToReturn = ConvertionHelper.convert(table);
                        }
                        repositoryList.add(name);
                    }
                }
            }
        }
        return metaToReturn;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void saveElementParameters(TalendFileFactory fileFact, List<? extends IElementParameter> paramList,
            EList listParamType, ProcessType process) {
        IElementParameter param;
        ElementParameterType pType;

        for (int j = 0; j < paramList.size(); j++) {
            param = paramList.get(j);
            if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                continue;
            }
            if (param.getName().equals(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
                // if this parameter is defined in a component, then we add a dependancy to this job.
                RequiredType rType = process.getRequired();
                if (rType == null) {
                    rType = fileFact.createRequiredType();
                    process.setRequired(rType);
                }
                JobType jType = fileFact.createJobType();
                String jobName;
                jobName = ((String) param.getValue()).replace("'", ""); //$NON-NLS-1$ //$NON-NLS-2$
                jType.setName(jobName);
                String contextName = ""; //$NON-NLS-1$
                boolean found = false;
                for (int i = 0; i < paramList.size() && !found; i++) {
                    IElementParameter contextParam = paramList.get(i);
                    if (contextParam.getName().equals(EParameterName.PROCESS_TYPE_CONTEXT.getName())) {
                        contextName = ((String) contextParam.getValue()).replace("'", ""); //$NON-NLS-1$ //$NON-NLS-2$
                        found = true;
                    }
                }
                jType.setContext(contextName);
                rType.getJob().add(jType);
            }
            if ((!param.isReadOnly()) || param.getName().equals(EParameterName.UNIQUE_NAME.getName())
                    || param.getName().equals(EParameterName.VERSION.getName())) {
                pType = fileFact.createElementParameterType();
                pType.setName(param.getName());
                pType.setField(param.getField().getName());
                Object value = param.getValue();
                if (param.getField().equals(EParameterFieldType.TABLE)) {
                    List<Map<String, Object>> tableValues = (List<Map<String, Object>>) value;
                    for (Map<String, Object> currentLine : tableValues) {
                        for (int i = 0; i < currentLine.size(); i++) {
                            ElementValueType elementValue = fileFact.createElementValueType();
                            elementValue.setElementRef(param.getListItemsDisplayCodeName()[i]);
                            Object o = currentLine.get(param.getListItemsDisplayCodeName()[i]);
                            String strValue = ""; //$NON-NLS-1$
                            if (o instanceof Integer) {
                                IElementParameter tmpParam = (IElementParameter) param.getListItemsValue()[i];
                                if (tmpParam.getListItemsValue().length == 0) {
                                    strValue = ""; //$NON-NLS-1$
                                } else {
                                    strValue = (String) tmpParam.getListItemsValue()[(Integer) o];
                                }
                            } else {
                                if (o instanceof String) {
                                    strValue = (String) o;
                                } else {
                                    if (o instanceof Boolean) {
                                        strValue = (String) ((Boolean) o).toString();
                                    }
                                }
                            }
                            elementValue.setValue(strValue);
                            pType.getElementValue().add(elementValue);
                        }
                    }
                } else {
                    if (value == null) {
                        pType.setValue(""); //$NON-NLS-1$
                    } else {
                        if (value instanceof Boolean) {
                            pType.setValue((String) ((Boolean) value).toString());
                        } else {
                            if (value instanceof String) {
                                pType.setValue((String) value);
                            }
                        }
                    }
                }
                listParamType.add(pType);
            }
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void loadElementParameters(Element elemParam, EList listParamType) {
        ElementParameterType pType;

        for (int j = 0; j < listParamType.size(); j++) {
            pType = (ElementParameterType) listParamType.get(j);

            if (pType != null) {
                IElementParameter param = elemParam.getElementParameter(pType.getName());
                if (param != null) {
                    if (param.getField().equals(EParameterFieldType.CHECK)) {
                        elemParam.setPropertyValue(pType.getName(), new Boolean(pType.getValue()));
                    } else if (param.getField().equals(EParameterFieldType.TABLE)) {
                        List<Map<String, Object>> tableValues = new ArrayList<Map<String, Object>>();
                        String[] codeList = param.getListItemsDisplayCodeName();
                        Map<String, Object> lineValues = null;
                        for (ElementValueType elementValue : (List<ElementValueType>) pType.getElementValue()) {
                            boolean found = false;
                            for (int i = 0; i < codeList.length && !found; i++) {
                                if (codeList[i].equals(elementValue.getElementRef())) {
                                    found = true;
                                }
                            }
                            if (found) {
                                if ((lineValues == null) || (lineValues.get(elementValue.getElementRef()) != null)) {
                                    lineValues = new HashMap<String, Object>();
                                    tableValues.add(lineValues);
                                }
                                lineValues.put(elementValue.getElementRef(), elementValue.getValue());
                            }
                        }
                        elemParam.setPropertyValue(pType.getName(), tableValues);
                    } else if (!param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                        elemParam.setPropertyValue(pType.getName(), pType.getValue());
                    }
                }
            }
        }
    }

    /**
     * Save the diagram in a Xml File.
     * 
     * @param file
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ProcessType saveXmlFile(final IFile file) throws IOException {
        String fileName;

        init();
        fileName = file.getLocationURI().toString();
        URI uri = URI.createURI(fileName);
        Resource res = new TalendFileResourceImpl(uri);

        TalendFileFactory fileFact = TalendFileFactory.eINSTANCE;
        DocumentRoot xmlDoc;
        xmlDoc = fileFact.createDocumentRoot();

        ProcessType process = fileFact.createProcessType();
        xmlDoc.setProcess(process);

        ParametersType params = fileFact.createParametersType();
        process.setParameters(params);

        saveElementParameters(fileFact, this.getElementParameters(), process.getParameters().getElementParameter(),
                process);

        EList nList = process.getNode();
        EList cList = process.getConnection();
        MetadataEmfFactory factory = new MetadataEmfFactory();

        // save according to elem order to keep zorder (children insertion) in diagram
        for (Element element : elem) {
            if (element instanceof Node) {
                saveNode(fileFact, process, nList, cList, (Node) element, factory);
            } else if (element instanceof Note) {
                saveNote(fileFact, process, (Note) element);
            }
        }

        /**
         * Save the contexts informations
         */
        process.setDefaultContext(contextManager.getDefaultContext().getName());
        if (repositoryId != null) {
            process.setRepositoryContextId(repositoryId);
        }

        contextManager.saveToEmf(process.getContext());

        res.getContents().add(xmlDoc);

        HashMap options = new HashMap(2);
        options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
        options.put(XMLResource.OPTION_XML_VERSION, "1.1");
        res.save(options);
        return process;
    }

    private void saveNote(TalendFileFactory fileFact, ProcessType process, Note note) {
        NoteType noteType = fileFact.createNoteType();
        noteType.setPosX(note.getLocation().x);
        noteType.setPosY(note.getLocation().y);
        noteType.setSizeWidth(note.getSize().width);
        noteType.setSizeHeight(note.getSize().height);
        noteType.setOpaque(note.isOpaque());
        noteType.setText(note.getText());

        process.getNote().add(noteType);
    }

    private void saveNode(TalendFileFactory fileFact, ProcessType process, EList nList, EList cList, Node node,
            MetadataEmfFactory factory) {
        NodeType nType;
        List<Connection> connList;
        Connection connec;
        ConnectionType cType;
        List<? extends IElementParameter> paramList;
        List<IMetadataTable> listMetaData;
        EList listParamType;
        EList listMetaType;
        IMetadataTable metaData;
        nType = fileFact.createNodeType();
        nType.setComponentVersion(node.getComponent().getVersion());
        nType.setComponentName(node.getComponent().getName());
        nType.setPosX(node.getLocation().x);
        nType.setPosY(node.getLocation().y);
        nType.setOffsetLabelX(node.getNodeLabel().getOffset().x);
        nType.setOffsetLabelY(node.getNodeLabel().getOffset().y);
        if (node.getExternalNode() != null) {
            if (node.getExternalData() != null) {
                Data data = node.getExternalBytesData();
                nType.setBinaryData(data.getBytesData());
                nType.setStringData(data.getStringData());
            }
        }
        listParamType = nType.getElementParameter();
        paramList = node.getElementParameters();

        // Added a condition to check if it is Stats and Logs.
        // saveElementParameters(fileFact, paramList, listParamType, process);
        {
            List<? extends IElementParameter> paramListForStatsAndLogs = node.getNodeContainer().getElementParameters();

        }

        saveElementParameters(fileFact, paramList, listParamType, process);
        listMetaType = nType.getMetadata();
        listMetaData = node.getMetadataList();
        for (int j = 0; j < listMetaData.size(); j++) {
            metaData = listMetaData.get(j);
            factory.setMetadataTable(metaData);
            listMetaType.add(factory.getMetadataType());
        }

        connList = (List<Connection>) node.getOutgoingConnections();
        for (int j = 0; j < connList.size(); j++) {
            connec = connList.get(j);
            cType = fileFact.createConnectionType();
            cType.setSource(node.getUniqueName());
            cType.setTarget(connec.getTarget().getUniqueName());
            cType.setLabel(connec.getName());
            cType.setLineStyle(connec.getLineStyleId());
            cType.setConnectorName(connec.getConnectorName());
            cType.setOffsetLabelX(connec.getConnectionLabel().getOffset().x); //$NON-NLS-1$
            cType.setOffsetLabelY(connec.getConnectionLabel().getOffset().y); //$NON-NLS-1$
            cType.setMetaname(connec.getMetaName());
            int id = connec.getOutputId(connec.getLineStyle());
            if (id >= 0) {
                cType.setOutputId(id);
            }
            listParamType = cType.getElementParameter();
            paramList = connec.getElementParameters();
            saveElementParameters(fileFact, paramList, listParamType, process);
            cList.add(cType);
        }
        nList.add(nType);
    }

    /**
     * DOC mhelleboid Comment method "loadXmlFile".
     * 
     * @param process
     */
    public void loadXmlFile(ProcessType process) {
        init();
        Hashtable<String, Node> nodesHashtable = new Hashtable<String, Node>();

        setActivate(false);

        if (process.getParameters() != null) {
            loadElementParameters(this, process.getParameters().getElementParameter());
        }

        try {
            loadNodes(process, nodesHashtable);
        } catch (PersistenceException e) {
            // there are some components unloaded.
            return;
        }

        repositoryId = process.getRepositoryContextId();

        loadConnections(process, nodesHashtable);
        loadContexts(process);
        loadNotes(process);
        initExternalComponents();
        setActivate(true);
        checkStartNodes();
        // checkProcess();
    }

    private void loadNotes(ProcessType process) {
        for (Iterator iter = process.getNote().iterator(); iter.hasNext();) {
            NoteType noteType = (NoteType) iter.next();

            Note note = new Note();
            note.setLocation(new Point(noteType.getPosX(), noteType.getPosY()));
            note.setSize(new Dimension(noteType.getSizeWidth(), noteType.getSizeHeight()));
            note.setOpaque(noteType.isOpaque());
            note.setText(noteType.getText());

            addNote(note);
        }
    }

    private void initExternalComponents() {
        for (Node node : nodes) {
            if (node.isExternalNode()) {
                node.getExternalNode().initialize();
            }
        }
    }

    private List<String> uploadedNodeNames = null;

    private void loadNodes(ProcessType process, Hashtable<String, Node> nodesHashtable) throws PersistenceException {
        EList nodeList;
        NodeType nType;
        nodeList = process.getNode();
        Node nc;

        EList listParamType;
        uploadedNodeNames = new ArrayList<String>();
        for (int i = 0; i < nodeList.size(); i++) {
            nType = (NodeType) nodeList.get(i);
            listParamType = nType.getElementParameter();
            IComponent component = ComponentsFactoryProvider.getInstance().get(nType.getComponentName());
            if (component == null) {
                uploadedNodeNames.add(nType.getComponentName());
                continue;
            }
            nc = new Node(component, this);
            nc.setLocation(new Point(nType.getPosX(), nType.getPosY()));
            Point offset = new Point(nType.getOffsetLabelX(), nType.getOffsetLabelY());
            nc.getNodeLabel().setOffset(offset);

            loadElementParameters(nc, listParamType);

            nc.setData(nType.getBinaryData(), nType.getStringData());

            loadSchema(nc, nType);

            addNodeContainer(new NodeContainer(nc));
            nodesHashtable.put(nc.getUniqueName(), nc);
            updateAllMappingTypes();
        }

        if (!uploadedNodeNames.isEmpty()) {
            throw new PersistenceException(Messages.getString("Process.componentsUnloaded")); //$NON-NLS-1$
        }
    }

    /**
     * to optimize.
     */
    private void updateAllMappingTypes() {
        for (INode node : this.getGraphicalNodes()) {
            for (IElementParameter param : node.getElementParameters()) {
                if (param.getField().equals(EParameterFieldType.MAPPING_TYPE)) {
                    for (IMetadataTable table : node.getMetadataList()) {
                        table.setDbms((String) param.getValue());
                    }
                }
            }
        }
    }

    /**
     * Checks if there are unloaded nodes.If there are some nodes unloaded, throws PersistenceException.
     * 
     * @throws PersistenceException PersistenceException
     */
    public void checkLoadNodes() throws PersistenceException {
        if (uploadedNodeNames == null || uploadedNodeNames.isEmpty()) {
            return;
        }
        String errorMessage = null;
        if (uploadedNodeNames.size() == 1) {
            errorMessage = Messages.getString("Process.component.notloaded", uploadedNodeNames.get(0)); //$NON-NLS-1$
        } else {
            StringBuilder curentName = new StringBuilder();
            for (String componentName : uploadedNodeNames) {
                curentName.append(componentName).append(",");
            }
            curentName.deleteCharAt(curentName.length() - 1);

            errorMessage = Messages.getString("Process.components.notloaded", curentName.toString()); //$NON-NLS-1$

        }
        PersistenceException ex = new PersistenceException(errorMessage);
        throw ex;
    }

    private void loadSchema(Node nc, NodeType nType) {
        MetadataEmfFactory factory = new MetadataEmfFactory();
        MetadataType mType;
        EList listMetaType;

        List<IMetadataTable> listMetaData;
        listMetaType = nType.getMetadata();
        IMetadataTable metadataTable;
        listMetaData = new ArrayList<IMetadataTable>();
        for (int j = 0; j < listMetaType.size(); j++) {
            mType = (MetadataType) listMetaType.get(j);
            factory.setMetadataType(mType);
            metadataTable = factory.getMetadataTable();
            listMetaData.add(metadataTable);
            if (nc.getConnectorFromType(EConnectionType.FLOW_MAIN).isBuiltIn()) {
                addUniqueConnectionName(metadataTable.getTableName());
            }
            MetadataUtils.initilializeSchemaFromElementParameters(metadataTable, (List<IElementParameter>) nc
                    .getElementParameters());
        }
        nc.setMetadataList(listMetaData);
    }

    /**
     * 
     * DOC nrousseau Comment method "checkNodeSchemaFromRepository".
     * 
     * @param nc
     * @param metadataTable
     * @return true if the data have been modified
     */
    private boolean checkNodeSchemaFromRepository(final Node node, final List<MetadataUpdateCheckResult> resultList) {
        boolean modified = false;

        final IMetadataTable metadataTable = node.getMetadataTable(node.getUniqueName());

        final String uniqueName = node.getUniqueName();

        // check the metadata from the repository to see if it's up to date.
        String schemaType = (String) node.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
        if (schemaType != null) {
            if (schemaType.equals(EmfComponent.REPOSITORY)) {
                String metaRepositoryName = (String) node.getPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE
                        .getName());
                IMetadataTable repositoryMetadata = getMetadataFromRepository(metaRepositoryName);

                MetadataUpdateCheckResult result = new MetadataUpdateCheckResult(node);

                if (repositoryMetadata != null) {
                    repositoryMetadata = repositoryMetadata.clone();
                    final IMetadataTable copyOfrepositoryMetadata = repositoryMetadata;
                    copyOfrepositoryMetadata.setTableName(uniqueName);

                    if (!copyOfrepositoryMetadata.sameMetadataAs(metadataTable)) {

                        result.setResult(MetadataUpdateCheckResult.RepositoryType.schema,
                                MetadataUpdateCheckResult.ResultType.change, copyOfrepositoryMetadata);

                        modified = true;
                    }
                } else {

                    result.setResult(MetadataUpdateCheckResult.RepositoryType.schema,
                            MetadataUpdateCheckResult.ResultType.delete, null);
                    // if the repository connection doesn't exists then set to built-in
                    modified = true;
                }

                // add the check result to resultList, hold the value.
                if (result.getResultType() != null) {
                    resultList.add(result);
                }
            }
        }
        return modified;
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        if (sheet.getCurrentPage() instanceof TabbedPropertySheetPage) {
            TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
            if (tabbedPropertySheetPage.getCurrentTab() != null) {
                tabbedPropertySheetPage.refresh();
            }
        }
    }

    /**
     * 
     * DOC nrousseau Comment method "checkNodePropertiesFromRepository".
     * 
     * @param node
     * @return true if the data have been modified
     */
    private boolean checkNodePropertiesFromRepository(final Node node, final List<MetadataUpdateCheckResult> resultList) {
        boolean modified = false;

        String propertyType = (String) node.getPropertyValue(EParameterName.PROPERTY_TYPE.getName());
        if (propertyType != null) {
            if (propertyType.equals(EmfComponent.REPOSITORY)) {
                IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
                List<ConnectionItem> metadataConnectionsItem = null;
                try {
                    metadataConnectionsItem = factory.getMetadataConnectionsItem();
                } catch (PersistenceException e) {
                    throw new RuntimeException(e);
                }
                org.talend.core.model.metadata.builder.connection.Connection tmpRepositoryConnection = null;
                if (metadataConnectionsItem != null) {
                    for (ConnectionItem connectionItem : metadataConnectionsItem) {
                        String value = connectionItem.getProperty().getId() + ""; //$NON-NLS-1$
                        if (value.equals((String) node.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE
                                .getName()))) {
                            tmpRepositoryConnection = (org.talend.core.model.metadata.builder.connection.Connection) connectionItem
                                    .getConnection();
                        }
                    }
                }
                final org.talend.core.model.metadata.builder.connection.Connection repositoryConnection = tmpRepositoryConnection;

                MetadataUpdateCheckResult result = new MetadataUpdateCheckResult(node);

                if (repositoryConnection != null) {
                    boolean sameValues = true;
                    // if the repository connection exists then test the values
                    for (IElementParameter param : node.getElementParameters()) {
                        String repositoryValue = param.getRepositoryValue();
                        if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
                            Object objectValue = (Object) RepositoryToComponentProperty.getValue(repositoryConnection,
                                    repositoryValue);

                            if (objectValue != null) {
                                if (param.getField().equals(EParameterFieldType.CLOSED_LIST)
                                        && param.getRepositoryValue().equals("TYPE")) { //$NON-NLS-1$
                                    boolean found = false;
                                    String[] list = param.getListRepositoryItems();
                                    for (int i = 0; (i < list.length) && (!found); i++) {
                                        if (objectValue.equals(list[i])) {
                                            found = true;
                                        }
                                    }
                                    if (!found) {
                                        sameValues = false;
                                    }
                                } else {
                                    // check the value
                                    if (!param.getValue().equals(objectValue)) {
                                        sameValues = false;
                                    }
                                }
                            }
                        }
                    }
                    if (!sameValues) {

                        result.setResult(MetadataUpdateCheckResult.RepositoryType.property,
                                MetadataUpdateCheckResult.ResultType.change, repositoryConnection);

                        modified = true;
                    } else {
                        for (IElementParameter param : node.getElementParameters()) {
                            String repositoryValue = param.getRepositoryValue();
                            if (param.isShow(node.getElementParameters()) && (repositoryValue != null)
                                    && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
                                param.setRepositoryValueUsed(true);
                            }
                        }
                    }
                } else {

                    result.setResult(MetadataUpdateCheckResult.RepositoryType.property,
                            MetadataUpdateCheckResult.ResultType.delete, null);

                    modified = true;
                }

                // add the check result to resultList, hold the value.
                if (result.getResultType() != null) {
                    resultList.add(result);
                }
            }
        }
        return modified;
    }

    /**
     * 
     * DOC nrousseau Comment method "checkDifferenceWithRepository".
     * 
     * @return true if a difference has been detected
     */
    public boolean checkDifferenceWithRepository() {
        List<MetadataUpdateCheckResult> resultList = new ArrayList<MetadataUpdateCheckResult>();
        boolean modified = false;
        for (Node node : nodes) {
            if (checkNodePropertiesFromRepository(node, resultList)) {
                modified = true;
            }
            if (checkNodeSchemaFromRepository(node, resultList)) {
                modified = true;
            }
        }

        // when modified == true, then resultList.size() > 0
        if (resultList.size() > 0) {
            MetadataUpdateCheckDialog checkDlg = new MetadataUpdateCheckDialog(PlatformUI.getWorkbench().getDisplay()
                    .getActiveShell(), resultList, Messages.getString("Process.IfToUpgradeMetadata")); //$NON-NLS-1$
            checkDlg.setTitle(Messages.getString("Process.metadataModificationDetected")); //$NON-NLS-1$

            checkDlg.setInputElement(resultList);
            int ret = checkDlg.open();
            if (ret == IDialogConstants.OK_ID) {
                List<Object> selectResult = Arrays.asList(checkDlg.getResult());

                updateNodeswithMetadata(selectResult);

                refreshPropertyView();

                modified = true;

            } else { // IDialogConstants.CANCEL_ID
                modified = false;
            }
        }

        return modified;
    }

    private void updateNodeswithMetadata(final List<Object> list) {

        for (int k = 0; k < list.size(); k++) {

            MetadataUpdateCheckResult result = (MetadataUpdateCheckResult) list.get(k);

            Node node = result.getNode();

            if (result.getRepositoryType() == MetadataUpdateCheckResult.RepositoryType.property) {

                if (result.getResultType() == MetadataUpdateCheckResult.ResultType.change) {

                    // upgrade from repository
                    if (result.isChecked()) {
                        for (IElementParameter param : node.getElementParameters()) {
                            String repositoryValue = param.getRepositoryValue();
                            if (param.isShow(node.getElementParameters()) && (repositoryValue != null)
                                    && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
                                Object objectValue = (Object) RepositoryToComponentProperty.getValue(
                                        (org.talend.core.model.metadata.builder.connection.Connection) result
                                                .getParameter(), repositoryValue);
                                if (objectValue != null) {
                                    if (param.getField().equals(EParameterFieldType.CLOSED_LIST)
                                            && param.getRepositoryValue().equals("TYPE")) { //$NON-NLS-1$
                                        boolean found = false;
                                        String[] items = param.getListRepositoryItems();
                                        for (int i = 0; (i < items.length) && (!found); i++) {
                                            if (objectValue.equals(items[i])) {
                                                found = true;
                                                node.setPropertyValue(param.getName(), param.getListItemsValue()[i]);
                                            }
                                        }
                                    } else {
                                        node.setPropertyValue(param.getName(), objectValue);
                                    }
                                    param.setRepositoryValueUsed(true);
                                }
                            }
                        }
                    } else { // result.isChecked() == false
                        // don't upgrade so set to builtin
                        node.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
                        for (IElementParameter param : node.getElementParameters()) {
                            String repositoryValue = param.getRepositoryValue();
                            if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
                                param.setRepositoryValueUsed(false);
                            }
                        }
                    }
                } else { // MetadataUpdateCheckResult.ResultType.delete

                    node.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
                }

            } else if (result.getRepositoryType() == MetadataUpdateCheckResult.RepositoryType.schema) {

                if (result.getResultType() == MetadataUpdateCheckResult.ResultType.change) {

                    if (result.isChecked()) {
                        node.getMetadataTable(node.getUniqueName()).setListColumns(
                                ((IMetadataTable) result.getParameter()).getListColumns());
                    } else { // result.isChecked()==false
                        node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                    }
                } else { // MetadataUpdateCheckResult.ResultType.delete

                    node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);

                }

            } else if (result.getRepositoryType() == MetadataUpdateCheckResult.RepositoryType.query) {
                // here need to add the code the do the "query"
            }
        }

    }

    private void loadConnections(ProcessType process, Hashtable<String, Node> nodesHashtable) {
        EList listParamType;
        EList connecList;
        ConnectionType cType;
        connecList = process.getConnection();
        Connection connec;
        Node source, target;

        for (int i = 0; i < connecList.size(); i++) {
            cType = (ConnectionType) connecList.get(i);
            source = (Node) nodesHashtable.get(cType.getSource());
            target = (Node) nodesHashtable.get(cType.getTarget());
            Integer lineStyleId = new Integer(cType.getLineStyle());
            String connectorName = cType.getConnectorName();
            boolean connectionTypeFound = false;
            if (connectorName != null) {
                // check if the connector exists and if the line style is correct
                // (used for automatic component upgrade, to avoid migration each time)
                if (source.getConnectorFromName(connectorName) != null
                        && (source.getConnectorFromName(connectorName).getDefaultConnectionType().getId() == lineStyleId)) {
                    connectionTypeFound = true;
                }
            }

            if (connectionTypeFound) {
                connec = new Connection(source, target, EConnectionType.getTypeFromId(lineStyleId), connectorName,
                        cType.getMetaname(), cType.getLabel(), cType.getMetaname());
            } else {
                EConnectionType type = EConnectionType.getTypeFromId(lineStyleId);
                connec = new Connection(source, target, type, source.getConnectorFromType(type).getName(), cType
                        .getMetaname(), cType.getLabel(), cType.getMetaname());
            }
            if ((!source.isActivate()) || (!target.isActivate())) {
                connec.setActivate(false);
            }
            listParamType = cType.getElementParameter();
            loadElementParameters(connec, listParamType);

            Point offset = new Point(cType.getOffsetLabelX(), cType.getOffsetLabelY());
            INodeConnector nodeConnectorSource = connec.getSourceNodeConnector();
            nodeConnectorSource.setCurLinkNbOutput(nodeConnectorSource.getCurLinkNbOutput() + 1);
            INodeConnector nodeConnectorTarget = connec.getTargetNodeConnector();
            nodeConnectorTarget.setCurLinkNbInput(nodeConnectorTarget.getCurLinkNbInput() + 1);
            connec.getConnectionLabel().setOffset(offset);
        }
    }

    private void loadContexts(ProcessType process) {
        /**
         * Load the contexts informations
         */
        String defaultContextToLoad;
        defaultContextToLoad = process.getDefaultContext();

        contextManager = new JobContextManager(process.getContext(), defaultContextToLoad);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean checkReadOnly() {
        IProxyRepositoryFactory repFactory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        boolean readOnlyLocal = !repFactory.isEditableAndLockIfPossible(property.getItem());
        this.setReadOnly(readOnlyLocal);
        return readOnlyLocal;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;

        for (Node node : nodes) {
            node.setReadOnly(readOnly);
            for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
                connec.setReadOnly(readOnly);
            }
        }

        for (Note note : notes) {
            note.setReadOnly(readOnly);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#getElementName()
     */
    @Override
    public String getElementName() {
        return name;
    }

    public String getName() {
        return this.getProperty().getLabel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#getAuthor()
     */
    public User getAuthor() {
        return getProperty().getAuthor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#getId()
     */
    public String getId() {
        return getProperty().getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#getLabel()
     */
    public String getLabel() {
        return getProperty().getLabel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#getStatus()
     */
    public String getStatusCode() {
        return getProperty().getStatusCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#getVersion()
     */
    public String getVersion() {
        return getProperty().getVersion();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setAuthor(org.talend.core.model.temp.User)
     */
    public void setAuthor(User author) {
        if (!getProperty().getAuthor().equals(author)) {
            getProperty().setAuthor(author);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setId(int)
     */
    public void setId(String id) {
        if (!getProperty().getId().equals(id)) {
            getProperty().setId(id);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setLabel(java.lang.String)
     */
    public void setLabel(String label) {
        if (!getProperty().getLabel().equals(label)) {
            getProperty().setLabel(label);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setStatus(org.talend.core.model.process.EProcessStatus)
     */
    public void setStatusCode(String statusCode) {
        setPropertyValue(EParameterName.STATUS.getName(), statusCode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IRepositoryProcess#setVersion(int)
     */
    public void setVersion(String version) {
        if (!getProperty().getVersion().equals(version)) {
            getProperty().setVersion(version);
        }
    }

    // private InputStream content;
    private byte[] content;

    private String repositoryId;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.temp.IXmlSerializable#getXmlStream()
     */
    public InputStream getXmlStream() {
        return new ByteArrayInputStream(content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.temp.IXmlSerializable#setXmlStream(java.io.InputStream)
     */
    public void setXmlStream(InputStream xmlStream) {
        ByteArrayOutputStream st = new ByteArrayOutputStream();

        int byteLu;
        try {
            while ((byteLu = xmlStream.read()) != -1) {
                st.write(byteLu);
            }
        } catch (IOException e) {
            // TODO SML Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                xmlStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.content = st.toByteArray();
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    /**
     * Check if the given name will be unique in the process. If another link already exists with that name, false will
     * be returned.
     * 
     * @param uniqueName
     * @param checkEsists
     * @return true if the name is unique
     */
    public boolean checkValidConnectionName(String connectionName, boolean checkExists) {
        if (checkExists && uniqueConnectionNameList.contains(connectionName)) {
            return false;
        }
        Perl5Matcher matcher = new Perl5Matcher();
        Perl5Compiler compiler = new Perl5Compiler();
        Pattern pattern;

        try {
            pattern = compiler.compile("^[A-Za-z_][A-Za-z0-9_]*$"); //$NON-NLS-1$
            if (!matcher.matches(connectionName, pattern)) {
                return false;
            }
        } catch (MalformedPatternException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    /**
     * Check if the given name will be unique in the process. If another link already exists with that name, false will
     * be returned.
     * 
     * @param uniqueName
     * @return true if the name is unique
     */
    public boolean checkValidConnectionName(String connectionName) {
        return checkValidConnectionName(connectionName, true);
    }

    /**
     * Manage to find a unique name with the given name.
     * 
     * @param titleName
     */
    public String generateUniqueConnectionName(String baseName) {
        if (baseName == null) {
            throw new IllegalArgumentException("baseName can't be null"); //$NON-NLS-1$
        }
        String uniqueName = baseName + 1;

        int counter = 1;
        boolean exists = true;
        while (exists) {
            exists = !checkValidConnectionName(uniqueName);
            if (!exists) {
                break;
            }
            uniqueName = baseName + counter++;
        }
        return uniqueName;
    }

    public void addUniqueConnectionName(String uniqueConnectionName) {
        if (uniqueConnectionName != null) {
            if (checkValidConnectionName(uniqueConnectionName)) {
                uniqueConnectionNameList.add(uniqueConnectionName);
            } else {
                throw new IllegalArgumentException("The name of the connection is not valid: " + uniqueConnectionName); //$NON-NLS-1$
            }
        }
    }

    public void removeUniqueConnectionName(String uniqueConnectionName) {
        if (uniqueConnectionName != null) {
            uniqueConnectionNameList.remove(uniqueConnectionName);
        }
    }

    public String generateUniqueNodeName(INode node) {
        String baseName = node.getComponent().getName();
        return UniqueNodeNameGenerator.generateUniqueNodeName(baseName, uniqueNodeNameList);
    }

    /**
     * This function will take a unique name and update the list with the given name. This function should be private
     * only and should be called only when the xml file is loaded.
     * 
     * @param uniqueName
     */
    public void addUniqueNodeName(final String uniqueName) {
        if (!uniqueNodeNameList.contains(uniqueName)) {
            uniqueNodeNameList.add(uniqueName);
        }
    }

    public void removeUniqueNodeName(final String uniqueName) {
        if (!uniqueName.equals("")) { //$NON-NLS-1$
            uniqueNodeNameList.remove(uniqueName);
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void setActivate(Node node, boolean active, Node activateNode) {
        Node mainSubProcess = node.getSubProcessStartNode(false);

        // if the selected node is the start node, then everything will be
        // desacticated
        if (activateNode.isStart()) {
            for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                if (connec.getSource().isActivate() != active) {
                    if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_HASH)) {
                        if (connec.getSource().getSubProcessStartNode(false).isActivate() != active) {
                            setActivate(connec.getSource().getSubProcessStartNode(false), active, activateNode);
                        }
                    }
                }
            }
            node.setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
            for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
                if (connec.getTarget().isActivate() != active) {
                    setActivate(connec.getTarget(), active, activateNode);
                }
            }
        } else {
            if (node.getSubProcessStartNode(false).equals(mainSubProcess)) {
                node.setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
                for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                    if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                            || connec.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_HASH)) {
                        if (connec.getSource().isActivate() != active) {
                            setActivate(connec.getSource(), active, activateNode);
                        }
                    }
                }
                for (Connection connec : (List<Connection>) node.getOutgoingConnections()) {
                    if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                            || connec.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_HASH)) {
                        if (connec.getTarget().isActivate() != active) {
                            setActivate(connec.getTarget(), active, activateNode);
                        }
                    }
                }
            }
            node.setPropertyValue(EParameterName.ACTIVATE.getName(), new Boolean(active));
        }
    }

    public void setActivate(Node node, boolean active) {
        // desactive first the process to avoid to check the process during the
        // activation / desactivation
        setActivate(false);
        setActivate(node, active, node);
        // now that everything is set, reactivate the process
        setActivate(true);
    }

    public void checkStartNodes() {
        for (Node node : nodes) {
            if ((Boolean) node.getPropertyValue(EParameterName.STARTABLE.getName())) {
                if (node.isELTComponent()) {
                    if (node.isActivate()) {
                        node.setStart(true);
                    }
                } else {
                    if (node.isActivate()) {
                        node.setStart(false);
                        boolean isActivatedConnection = false;
                        for (int j = 0; j < node.getIncomingConnections().size() && !isActivatedConnection; j++) {
                            Connection connection = (Connection) node.getIncomingConnections().get(j);
                            // connection that will generate a hash file are not considered as activated for this test.
                            if (connection.isActivate()
                                    && !connection.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_HASH)) {
                                isActivatedConnection = true;
                            }
                        }
                        if (!isActivatedConnection) {
                            if (!isThereLinkWithHash(node)) {
                                node.setStart(true);
                            }
                        } else {
                            if (node.getIncomingConnections().size() == 0) {
                                if (!isThereLinkWithHash(node)) {
                                    node.setStart(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This function check if in this subprocess there should be a start or not depends on the ref links. If in this
     * subprocess there is only one main flow and one ref then this function will return true. If there is several flow
     * in the output of one component in this subprocess,it'll return false.
     * 
     * @param node
     * @return
     */
    public boolean isThereLinkWithHash(final INode node) {
        boolean refLink = false;

        for (int i = 0; i < node.getOutgoingConnections().size() && !refLink; i++) {
            IConnection connec = node.getOutgoingConnections().get(i);
            if (connec.isActivate()) {
                if (connec.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_HASH)) {
                    refLink = true;
                } else {
                    if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                            || connec.getLineStyle().equals(EConnectionType.ITERATE)
                            || connec.getLineStyle().hasConnectionCategory(IConnectionCategory.EXECUTION_ORDER)) {
                        refLink = isThereLinkWithHash(connec.getTarget());
                    }
                }
            }
        }
        return refLink;
    }

    /**
     * DOC nrousseau Comment method "checkProcess".
     * 
     * @param propagate
     */
    public void checkProcess() {
        if (isActivate()) {
            checkProblems();
        }
    }

    private void checkProblems() {
        Problems.clearAll();

        for (Node node : nodes) {
            if (node.isActivate()) {
                node.checkNode();
            }
        }
        Problems.refreshView();
    }

    /**
     * 
     * DOC check the problems of node.compare with the checkProblems(),this method can't refresh problems view.
     */
    public void checkNodeProblems() {
        if (isActivate()) {
            Problems.clearAll();

            for (Node node : nodes) {
                if (node.isActivate()) {
                    node.checkNode();
                }
            }
        }
    }

    public String toString() {
        return "Process:" + getLabel(); //$NON-NLS-1$
    }

    public ERepositoryObjectType getType() {
        return ERepositoryObjectType.PROCESS;
    }

    public IContextManager getContextManager() {
        return contextManager;
    }

    // PTODO mhelleboid remove
    public Date getCreationDate() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public Date getModificationDate() {
        return null;
    }

    public String getPurpose() {
        return null;
    }

    public void setCreationDate(Date value) {
    }

    public void setDescription(String value) {
    }

    public void setModificationDate(Date value) {
    }

    public void setPurpose(String value) {
    }

    @Override
    public void setPropertyValue(String id, Object value) {
        if (id.equals(EParameterName.SCHEMA_TYPE.getName()) || id.equals(EParameterName.QUERYSTORE_TYPE.getName())
                || id.equals(EParameterName.PROPERTY_TYPE.getName())
                || id.equals(EParameterName.PROCESS_TYPE_PROCESS.getName())
                || id.equals(EParameterName.ENCODING_TYPE.getName())) {
            setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), Boolean.TRUE);
        }

        super.setPropertyValue(id, value);
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getChildren()
     */
    public List<IRepositoryObject> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Return all Nodes of Component type componentName.
     * 
     * @param componentName the component name
     * @return all the activated matching nodes in the process
     */
    public List<INode> getNodesOfType(String componentName) {
        List<INode> matchingNodes = new ArrayList<INode>();
        for (INode node : getGeneratingNodes()) {
            if (node.isActivate()) {
                if (componentName == null) { // means all nodes will be returned
                    matchingNodes.add(node);
                } else if ((node.getComponent().getName() != null)
                        && (node.getComponent().getName().compareTo(componentName)) == 0) {
                    matchingNodes.add(node);
                }
            }
        }
        return matchingNodes;
    }

    public Project getProject() {
        return ((org.talend.core.context.RepositoryContext) CorePlugin.getContext().getProperty(
                org.talend.core.context.Context.REPOSITORY_CONTEXT_KEY)).getProject();
    }

    public void setAsMasterJob() {
        getProject().setMasterJobId(this.getId());
    }

    public ProcessItem getMasterJob() {
        ProcessItem item = null;
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();

        try {
            IRepositoryObject repositoryObject = factory.getProcess().getMember(getProject().getMasterJobId());
            if (repositoryObject.getType() == ERepositoryObjectType.PROCESS) {
                item = (ProcessItem) repositoryObject.getProperty().getItem();
            }
        } catch (PersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return item;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void addNote(Note note) {
        elem.add(note);
        notes.add(note);
        fireStructureChange(NOTES, elem);
    }

    public void removeNote(Note note) {
        elem.remove(note);
        notes.remove(note);
        fireStructureChange(NOTES, elem);
    }

    /**
     * Getter for processor.
     * 
     * @return the processor
     */
    public IProcessor getProcessor() {
        return processor;
    }

    /**
     * Sets the processor.
     * 
     * @param processor the processor to set
     */
    public void setProcessor(IProcessor processor) {
        this.processor = processor;
    }

    /**
     * DOC Administrator Comment method "change".
     * 
     * @param str
     */
    private String replaceSlash(String str) {
        String tempStr = str.replaceAll("\\\\", "/");
        return tempStr;
    }
}
