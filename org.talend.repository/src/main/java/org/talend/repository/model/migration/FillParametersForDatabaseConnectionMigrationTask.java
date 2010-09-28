// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model.migration;

import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC hywang class global comment. All the databaseConnection need have name because the shareing of metadata between
 * top and tos
 */
public class FillParametersForDatabaseConnectionMigrationTask extends AbstractItemMigrationTask {

    private static String sqlDataTypeDefaultName = "VARCHAR";

    private static int sqlDataTypeDefaultJavaType = 12;

    private static long sqlDataTypeDefaultPrecision = 10;

    private static long sqlDataTypeDefaultPrecisionRadix = 10;

    private IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    @Override
    public ExecutionResult execute(Item item) {
        if (item instanceof DatabaseConnectionItem) {
            DatabaseConnectionItem dbItem = (DatabaseConnectionItem) item;
            DatabaseConnection dbconn = (DatabaseConnection) dbItem.getConnection();
            EList<orgomg.cwm.objectmodel.core.Package> pkgs = dbconn.getDataPackage();
            fillParametersForColumns(pkgs); // get all tdtables and set sqldatatype
            dbconn.setName(dbItem.getProperty().getLabel());
            try {
                factory.save(dbItem, true);
                return ExecutionResult.SUCCESS_WITH_ALERT;
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                return ExecutionResult.FAILURE;
            }
        }
        return ExecutionResult.SUCCESS_NO_ALERT;
    }

    private void fillParametersForColumns(EList<orgomg.cwm.objectmodel.core.Package> pkgs) {
        for (orgomg.cwm.objectmodel.core.Package pkg : pkgs) {
            EList<ModelElement> allTables = pkg.getOwnedElement();
            for (ModelElement mo : allTables) {
                if (mo instanceof TdTable) {
                    TdTable ttable = (TdTable) mo;
                    for (MetadataColumn col : ttable.getColumns()) {
                        if (col instanceof TdColumn) {
                            TdColumn tcol = (TdColumn) col;
                            TdSqlDataType sqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
                            sqlDataType.setName(sqlDataTypeDefaultName);
                            sqlDataType.setJavaDataType(sqlDataTypeDefaultJavaType);
                            sqlDataType.setNumericPrecision(sqlDataTypeDefaultPrecision);
                            sqlDataType.setNumericPrecisionRadix(sqlDataTypeDefaultPrecisionRadix);
                            tcol.setSqlDataType(sqlDataType);
                        }
                    }

                }
            }
        }
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2010, 9, 25, 15, 30, 0);
        return gc.getTime();
    }

}
