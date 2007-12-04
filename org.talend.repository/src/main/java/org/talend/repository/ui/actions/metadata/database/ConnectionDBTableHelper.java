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
package org.talend.repository.ui.actions.metadata.database;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.talend.commons.ui.utils.PathUtils;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.RejectedType;
import org.talend.repository.ui.actions.metadata.importing.AbstractImportingTablesHelper;
import org.talend.repository.ui.utils.DataStringConnection;

/**
 * ggu class global comment. Detailled comment <br/>
 * 
 */
public final class ConnectionDBTableHelper extends AbstractImportingTablesHelper {

    public ConnectionDBTableHelper(File file) {
        super(ERepositoryObjectType.METADATA_CONNECTIONS, file);
    }

    @Override
    protected ConnectionItem createConnectionItem(DBTableForDelimitedBean bean) {
        if (isNullable(bean.getDatabaseType())) {
            processRecords.addRejectedRecords(RejectedType.DATABASETYPE, bean.getDatabaseType(), bean.getName());
            return null;
        }
        DatabaseConnection conn = createDBConnection(bean);
        if (conn == null) {
            return null;
        }
        DatabaseConnectionItem connItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
        createProperty(connItem, bean);
        connItem.setConnection(conn);

        setConnectionCreated(true);

        return connItem;

    }

    private DatabaseConnection createDBConnection(DBTableForDelimitedBean bean) {

        final String product = EDatabaseTypeName.getTypeFromDisplayName(bean.getDatabaseType()).getProduct();
        if (product == null) {
            // not suppored database
            processRecords.addRejectedRecords(RejectedType.DATABASETYPE, bean.getDatabaseType(), bean.getName());
            return null;
        }
        DatabaseConnection connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();

        connection.setDatabaseType(bean.getDatabaseType());
        connection.setDatasourceName(bean.getDataSource());

        connection.setDBRootPath(bean.getDbRootPath());
        connection.setFileFieldName(PathUtils.getPortablePath(bean.getFile()));

        connection.setPassword(bean.getPassword());
        connection.setPort(bean.getPort());

        if (EDatabaseTypeName.ORACLEFORSID.getProduct().equals(connection.getProductId()) && bean.getDbSchema() != null) {
            connection.setSchema(bean.getDbSchema().toUpperCase());
        } else {
            connection.setSchema(bean.getDbSchema());
        }

        connection.setServerName(bean.getServer());
        connection.setURL(bean.getConnectionStr());
        connection.setUsername(bean.getLogin());

        connection.setProductId(product);

        final String mapping = MetadataTalendType.getDefaultDbmsFromProduct(product).getId();
        connection.setDbmsId(mapping);
        if (!checkDBConnectionURL(bean)) {
            return null;
        }

        return connection;

    }

    private boolean checkDBConnectionURL(DBTableForDelimitedBean bean) {
        if (isNullable(bean.getDatabaseType())) {
            processRecords.addRejectedRecords(RejectedType.DATABASETYPE, bean.getDatabaseType(), bean.getName());
            return false;
        }
        // check the url.
        DataStringConnection urlDBStr = new DataStringConnection();
        int index = urlDBStr.getIndexOfLabel(bean.getDatabaseType());
        if (index == -1) {
            // not existed Database.
            // add rejected dbtype
            processRecords.addRejectedRecords(RejectedType.DATABASETYPE, bean.getDatabaseType(), bean.getName());
            return false;
        }

        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.PERL) {
            Collection<String> databasePerl = new ArrayList<String>(Arrays.asList(urlDBStr.getItem()));
            databasePerl.remove("Microsoft SQL Server"); //$NON-NLS-1$
            databasePerl.remove("Ingres"); //$NON-NLS-1$
            databasePerl.remove("Interbase"); //$NON-NLS-1$
            databasePerl.remove("FireBird"); //$NON-NLS-1$
            databasePerl.remove("Informix"); //$NON-NLS-1$
            databasePerl.remove("Access"); //$NON-NLS-1$
            databasePerl.remove("Teradata"); //$NON-NLS-1$
            databasePerl.remove("AS400"); //$NON-NLS-1$

            databasePerl.remove("JavaDB Embeded"); //$NON-NLS-1$
            databasePerl.remove("JavaDB JCCJDBC"); //$NON-NLS-1$
            databasePerl.remove("JavaDB DerbyClient"); //$NON-NLS-1$

            databasePerl.remove("HSQLDB Server"); //$NON-NLS-1$
            databasePerl.remove("HSQLDB WebServer"); //$NON-NLS-1$
            databasePerl.remove("HSQLDB In-Process"); //$NON-NLS-1$
            if (!databasePerl.contains(bean.getDatabaseType())) {
                // not supported by perl
                // add rejected dbtype
                processRecords.setUnknownDBForPerl(bean.getDatabaseType(), bean.getName());
                return false;
            }
        }
        // urlDBStr.setSelectionIndex(index);
        // check the schema needed
        // if (urlDBStr.isSchemaNeeded() && isNullable(bean.getDbSchema())) {
        // return false;
        // }

        // need check the database url.

        return true;

    }

}
