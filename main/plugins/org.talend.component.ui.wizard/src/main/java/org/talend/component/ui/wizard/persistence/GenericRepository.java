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

import org.talend.component.ui.model.genericMetadata.GenericConnection;
import org.talend.component.ui.model.genericMetadata.GenericConnectionItem;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.properties.Repository;
import org.talend.components.api.schema.Schema;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.seeker.RepositorySeekerManager;
import org.talend.repository.model.IRepositoryNode;

import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * created by ycbai on 2015年9月29日 Detailled comment
 *
 */
public class GenericRepository implements Repository {

    public GenericRepository(GenericConnection connection) {
    }

    @Override
    public String storeComponentProperties(ComponentProperties properties, String name, String repositoryLocation,
            Schema schema) {
        if (repositoryLocation.contains("#")) {// nested properties to be set. //$NON-NLS-1$
            // the following search takes too much time especially when there are a lot of items in the filesystem
            // whe should find another way of locating the item ask ggu ?.
            GenericConnectionItem item = getGenericConnectionItem(
                    repositoryLocation.substring(0, repositoryLocation.indexOf('#')));
            GenericConnection connection = (GenericConnection) item.getConnection();
            if (repositoryLocation.endsWith("#")) {// first nested property
                if (item != null) {
                    // create a generic package
                    GenericPackage genericPackage = ConnectionFactory.eINSTANCE.createGenericPackage();
                    connection.getDataPackage().add(genericPackage);
                    genericPackage.setName(name);

                    // if there is a schema then creates a Schema element
                    if (schema != null) {
                        // creates a catalog
                        MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                        genericPackage.getOwnedElement().add(metadataTable);
                        metadataTable.setName(name);
                        TaggedValue serializedProps = CoreFactory.eINSTANCE.createTaggedValue();
                        metadataTable.getTaggedValue().add(serializedProps);
                        serializedProps.setTag("component.json.serialized");
                        serializedProps.setValue(properties.toSerialized());
                        convertComponentSchemaIntoTalendSchema(schema, metadataTable);
                    } // else only pGeneric Package is created so that subsequent call can create sub package and tables
                    return repositoryLocation + "#" + name;
                } else {
                    throw new RuntimeException("Failed to find the GenericConnectionItem for location:" + repositoryLocation);
                }
            } else {// sub nested property FIXME to be handled too.
                // we need to find the proper GEnericPackage according to the name from the repositoryLocation
                // name1#name2#name3 should lead to
                // GenericPackage.getOwnedElement("name1").getOwnedElement("name2").getOwnedElement("name2")
                // this is a simplify explanation of course getOwnedElement() return a list that needs to be searched
                // for the proper name.

                throw new RuntimeException("Not implemented yet.");
            }
        } else {// simple properties to be set
            GenericConnectionItem item = getGenericConnectionItem(repositoryLocation);
            if (item != null) {
                GenericConnection connection = (GenericConnection) item.getConnection();
                connection.setCompProperties(properties.toSerialized());
                return repositoryLocation + "#";
            } else {
                throw new RuntimeException("Failed to find the GenericConnectionItem for location:" + repositoryLocation);
            }
        }
    }

    /**
     * DOC sgandon Comment method "convertComponentSchemaIntoTalendSchema".
     * 
     * @param schema
     * @param metadataTable
     */
    private void convertComponentSchemaIntoTalendSchema(Schema schema, MetadataTable metadataTable) {
        // FIXME to be implmented

    }

    /**
     * DOC sgandon Comment method "getGenericConnectionItem".
     * 
     * @param repositoryLocation
     * @return
     */
    private GenericConnectionItem getGenericConnectionItem(String repositoryLocation) {
        IRepositoryNode nodeUnderWichStoreTheProperties = RepositorySeekerManager.getInstance()
                .searchRepoViewNode(repositoryLocation);
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
