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
package org.talend.repository.ui.actions.metadata;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * Administrator class global comment. Detailed comment <br/>
 * 
 */
public class CopyToGenericSchemaHelper {

    private static final String DEFAULT_TABLE_NAME = "metadata";
    
    private static IProxyRepositoryFactory repositoryFactory; 

    /**
     * Administrator Comment method "moveToGenericSchema".
     * 
     * @param factory
     * @param ve
     * @param isConnectionTableSchema
     * @throws PersistenceException
     */
    public static void copyToGenericSchema(IProxyRepositoryFactory factory, IRepositoryObject objectToMove)
            throws PersistenceException {
        
        repositoryFactory = factory;
        
        String dbmsId = null;

        GenericSchemaConnectionItem connectionItem = PropertiesFactory.eINSTANCE.createGenericSchemaConnectionItem();
        GenericSchemaConnection connection = ConnectionFactory.eINSTANCE.createGenericSchemaConnection();

        if (dbmsId != null && dbmsId.length() > 0) {
            connection.setMappingTypeId(dbmsId);
            connection.setMappingTypeUsed(true);
        }

        MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        metadataTable.setConnection(connection);

        ConnectionItem itemToMove = (ConnectionItem) objectToMove.getProperty().getItem();
        EList tables = itemToMove.getConnection().getTables();
        EList listColumns = null;

        MetadataTable table = null;
        for (Object object : tables) {
            table = (MetadataTable) object;
            listColumns = table.getColumns();
        }

        boolean isConnectionTableSchema = checkIsConnectionTableSchema(objectToMove);
        if (isConnectionTableSchema) {
            metadataTable.setLabel(DEFAULT_TABLE_NAME);
        } else {
            metadataTable.setLabel(table.getLabel() == null ? DEFAULT_TABLE_NAME : table.getLabel());
        }
        for (Object temp : listColumns) {
            MetadataColumn column = (MetadataColumnImpl) temp;
            MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
            metadataColumn.setComment(column.getComment());
            metadataColumn.setLabel(column.getLabel());
            metadataColumn.setDefaultValue(column.getDefaultValue());
            metadataColumn.setKey(column.isKey());

            metadataColumn.setLength(column.getLength());

            metadataColumn.setPrecision(column.getPrecision());

            metadataColumn.setPattern(column.getPattern());
            metadataColumn.setNullable(column.isNullable());
            metadataColumn.setOriginalField(column.getLabel());
            metadataColumn.setTalendType(column.getTalendType());
            metadataColumn.setSourceType(column.getSourceType());

            metadataTable.getColumns().add(metadataColumn);
        }

        Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        connectionProperty.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY)).getUser());
        connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        connectionProperty.setStatusCode(""); //$NON-NLS-1$
        connectionProperty.setLabel(objectToMove.getLabel());

        connection.getTables().add(metadataTable);
        connectionItem.setConnection(connection);
        connectionProperty.setItem(connectionItem);
        connectionProperty.setId(factory.getNextId());   

        if (!repositoryFactory.isNameAvailable(connectionItem, objectToMove.getLabel()))// name is existing in generic schema.
            try {
                setPropNewName(connectionProperty);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            
            repositoryFactory.create(connectionItem, new Path(""));
    }

    /**
     * Comment method "checkIsConnectionTableSchema".
     * 
     * @param objectToM m isConnectionTableSchema
     * @return
     */
    private static boolean checkIsConnectionTableSchema(IRepositoryObject objectToMove) {
        if (objectToMove != null && objectToMove.getType() != null) {
            switch (objectToMove.getType()) {
            case METADATA_CON_TABLE:
            case METADATA_CON_SYNONYM:
            case METADATA_CON_VIEW:
                return true;
            default:
                return false;
            }
        }
        return false;
    }

    /**
     * Comment method "getCopiedLabel".
     * 
     * @param copiedProperty
     * @return
     * @throws PersistenceException
     * @throws BusinessException
     */
    private static void setPropNewName(Property copiedProperty) throws PersistenceException, BusinessException {
        String originalLabel = copiedProperty.getLabel();
        String add1 = "Copy_of_"; //$NON-NLS-1$
        String initialTry = add1 + originalLabel;
        copiedProperty.setLabel(initialTry);
        if (repositoryFactory.isNameAvailable(copiedProperty.getItem(), null)) {
            return;
        } else {
            char j = 'a';
            while (!repositoryFactory.isNameAvailable(copiedProperty.getItem(), null)) {
                if (j > 'z') {
                    throw new BusinessException("Cannot generate pasted item label.");
                }
                String nextTry = initialTry + "_" + (j++) + ""; //$NON-NLS-1$ //$NON-NLS-2$
                copiedProperty.setLabel(nextTry);
            }
        }
    }
}
