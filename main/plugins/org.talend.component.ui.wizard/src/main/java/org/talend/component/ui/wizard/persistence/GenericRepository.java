// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.component.ui.wizard.persistence;

import java.util.List;

import org.talend.component.ui.model.genericMetadata.GenericConnection;
import org.talend.component.ui.model.genericMetadata.GenericConnectionItem;
import org.talend.component.ui.model.genericMetadata.GenericMetadataFactory;
import org.talend.component.ui.model.genericMetadata.SubContainer;
import org.talend.component.ui.wizard.constants.IGenericConstants;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.properties.Repository;
import org.talend.components.api.schema.Schema;
import org.talend.components.api.schema.SchemaElement;
import org.talend.components.api.schema.SchemaElement.Type;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.seeker.RepositorySeekerManager;
import org.talend.cwm.helper.PackageHelper;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * created by ycbai on 2015年9月29日 Detailled comment
 *
 */
public class GenericRepository implements Repository {

    @Override
    public String storeComponentProperties(ComponentProperties properties, String name, String repositoryLocation, Schema schema) {
        String serializedProperties = properties.toSerialized();
        if (repositoryLocation.contains(IGenericConstants.REPOSITORY_LOCATION_SEPARATOR)) {// nested properties to be
                                                                                           // set.
            // the following search takes too much time especially when there are a lot of items in the filesystem
            // whe should find another way of locating the item ask ggu ?.
            GenericConnectionItem item = getGenericConnectionItem(repositoryLocation.substring(0,
                    repositoryLocation.indexOf(IGenericConstants.REPOSITORY_LOCATION_SEPARATOR)));
            if (item == null) {
                throw new RuntimeException("Failed to find the GenericConnectionItem for location:" + repositoryLocation); //$NON-NLS-1$
            }
            GenericConnection connection = (GenericConnection) item.getConnection();
            SubContainer subContainer = createContainer(name, serializedProperties);
            if (repositoryLocation.endsWith(IGenericConstants.REPOSITORY_LOCATION_SEPARATOR)) {// first nested property
                if (item != null) {
                    connection.getOwnedElement().add(subContainer);
                }
            } else {
                SubContainer parentContainer = getContainer(connection, repositoryLocation);
                parentContainer.getOwnedElement().add(subContainer);
            }
            // if there is a schema then creates a Schema element
            if (schema != null) {
                MetadataTable metadataTable = createSchema(subContainer, name, serializedProperties);
                convertComponentSchemaIntoTalendSchema(schema, metadataTable);
            }
            return repositoryLocation + IGenericConstants.REPOSITORY_LOCATION_SEPARATOR + name;
        } else {// simple properties to be set
            GenericConnectionItem item = getGenericConnectionItem(repositoryLocation);
            if (item != null) {
                GenericConnection connection = (GenericConnection) item.getConnection();
                connection.setCompProperties(serializedProperties);
                return repositoryLocation + IGenericConstants.REPOSITORY_LOCATION_SEPARATOR;
            } else {
                throw new RuntimeException("Failed to find the GenericConnectionItem for location:" + repositoryLocation); //$NON-NLS-1$
            }
        }
    }

    private SubContainer createContainer(String containerName, String serializedProperties) {
        SubContainer subContainer = GenericMetadataFactory.eINSTANCE.createSubContainer();
        subContainer.setName(containerName);
        subContainer.setCompProperties(serializedProperties);
        return subContainer;
    }

    private MetadataTable createSchema(SubContainer container, String name, String serializedProperties) {
        MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        metadataTable.setName(name);
        metadataTable.setLabel(name);
        TaggedValue serializedProps = CoreFactory.eINSTANCE.createTaggedValue();
        metadataTable.getTaggedValue().add(serializedProps);
        serializedProps.setTag(IGenericConstants.COMPONENT_PROPERTIES_TAG);
        serializedProps.setValue(serializedProperties);
        container.getOwnedElement().add(metadataTable);
        return metadataTable;
    }

    private SubContainer getContainer(GenericConnection connection, String repositoryLocation) {
        SubContainer theContainer = null;
        String containers = repositoryLocation;
        if (containers.indexOf(IGenericConstants.REPOSITORY_LOCATION_SEPARATOR) != -1) {
            containers = containers.substring(repositoryLocation.indexOf(IGenericConstants.REPOSITORY_LOCATION_SEPARATOR) + 1);
            String[] containersArray = containers.split(IGenericConstants.REPOSITORY_LOCATION_SEPARATOR);
            for (String container : containersArray) {
                if (theContainer == null) {
                    theContainer = getTheContainer(connection, container);
                } else {
                    theContainer = getTheContainer(theContainer, container);
                    if (theContainer == null) {
                        throw new RuntimeException("Failed to find the SubContainer named:" + container); //$NON-NLS-1$
                    }
                }
            }
        }
        return theContainer;
    }

    private SubContainer getTheContainer(orgomg.cwm.objectmodel.core.Package parentPackage, String containerName) {
        List<SubContainer> subContainers = PackageHelper.getOwnedElements(parentPackage, SubContainer.class);
        for (SubContainer subContainer : subContainers) {
            if (containerName != null && containerName.equals(subContainer.getName())) {
                return subContainer;
            }
        }
        return null;
    }

    /**
     * DOC sgandon Comment method "convertComponentSchemaIntoTalendSchema".
     * 
     * @param schema
     * @param metadataTable
     */
    private void convertComponentSchemaIntoTalendSchema(Schema schema, MetadataTable metadataTable) {
        SchemaElement root = schema.getRoot();
        if (root != null) {
            List<SchemaElement> schemaElements = root.getChildren();
            for (SchemaElement schemaElement : schemaElements) {
                MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                setupMetadataColumn(metadataColumn, schemaElement);
                metadataTable.getColumns().add(metadataColumn);
            }
        }
    }

    private void setupMetadataColumn(MetadataColumn metadataColumn, SchemaElement schemaElement) {
        String talendType = JavaTypesManager.STRING.getId();
        Type type = schemaElement.getType();
        switch (type) {
        case BOOLEAN:
            talendType = JavaTypesManager.BOOLEAN.getId();
            break;
        case INT:
            talendType = JavaTypesManager.INTEGER.getId();
            break;
        case DATE:
            talendType = JavaTypesManager.DATE.getId();
            break;
        case DATETIME:
            talendType = JavaTypesManager.DATE.getId();
            break;
        case DOUBLE:
            talendType = JavaTypesManager.DOUBLE.getId();
            break;
        case DECIMAL:
            talendType = JavaTypesManager.BIGDECIMAL.getId();
            break;
        default:
            talendType = JavaTypesManager.STRING.getId();
            break;
        }
        metadataColumn.setTalendType(talendType);
        metadataColumn.setName(schemaElement.getName());
        metadataColumn.setLabel(metadataColumn.getName());
        metadataColumn.setPattern(schemaElement.getPattern());
        metadataColumn.setNullable(schemaElement.isNullable());
        metadataColumn.setLength(schemaElement.getSize());
        metadataColumn.setPrecision(schemaElement.getPrecision());
        metadataColumn.setDefaultValue(schemaElement.getDefaultValue());
    }

    /**
     * DOC sgandon Comment method "getGenericConnectionItem".
     * 
     * @param repositoryLocation
     * @return
     */
    private GenericConnectionItem getGenericConnectionItem(String repositoryLocation) {
        IRepositoryNode nodeUnderWichStoreTheProperties = RepositorySeekerManager.getInstance().searchRepoViewNode(
                repositoryLocation);
        if (nodeUnderWichStoreTheProperties == null) {
            throw new RuntimeException("Failed to find correct node for storing the components properties");
        } // else we found it
        Item item = nodeUnderWichStoreTheProperties.getObject().getProperty().getItem();
        return (GenericConnectionItem) item;
    }

    @Override
    public ComponentProperties getPropertiesForComponent(String componentId) {
        return null;// FIXME
    }

}
