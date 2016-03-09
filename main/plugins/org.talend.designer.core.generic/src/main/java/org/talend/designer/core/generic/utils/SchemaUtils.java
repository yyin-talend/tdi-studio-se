// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.generic.utils;

import java.util.Map;

import org.apache.avro.Schema;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.daikon.properties.Properties.Deserialized;
import org.talend.designer.core.generic.constants.IGenericConstants;
import org.talend.repository.model.IProxyRepositoryFactory;

import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.TaggedValue;


public class SchemaUtils {

    public static MetadataTable createSchema(String name, String serializedProperties) {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        metadataTable.setId(factory.getNextId());
        metadataTable.setName(name);
        metadataTable.setLabel(name);
        metadataTable.setSourceName(name);
        TaggedValue serializedProps = CoreFactory.eINSTANCE.createTaggedValue();
        metadataTable.getTaggedValue().add(serializedProps);
        serializedProps.setTag(IGenericConstants.COMPONENT_PROPERTIES_TAG);
        serializedProps.setValue(serializedProperties);
        return metadataTable;
    }

    public static void convertComponentSchemaIntoTalendSchema(Schema schema, MetadataTable metadataTable) {
    	for (Schema.Field field : schema.getFields()) {
            MetadataColumn metadataColumn = MetadataToolHelper.convertFromAvro(field);
            metadataTable.getColumns().add(metadataColumn);
        }
    }

    public static ComponentProperties getCurrentComponentProperties(IMetadataTable table) {
        if (table != null) {
            String serializedProperties = null;
            if (table instanceof MetadataTableRepositoryObject) {
                MetadataTableRepositoryObject metaTableRepObj = (MetadataTableRepositoryObject) table;
                MetadataTable metadataTable = metaTableRepObj.getTable();
                if (metadataTable != null && metadataTable.getTaggedValue() != null) {
                    for (TaggedValue serializedProps : metadataTable.getTaggedValue()) {
                        if (IGenericConstants.COMPONENT_PROPERTIES_TAG.equals(serializedProps.getTag())) {
                            serializedProperties = serializedProps.getValue();
                            break;
                        }
                    }
                }
            } else if (table instanceof org.talend.core.model.metadata.MetadataTable) {
                org.talend.core.model.metadata.MetadataTable metaTable = (org.talend.core.model.metadata.MetadataTable) table;
                Map<String, String> additionalProperties = metaTable.getAdditionalProperties();
                serializedProperties = additionalProperties.get(IGenericConstants.COMPONENT_PROPERTIES_TAG);
            }
            if (serializedProperties != null) {
                Deserialized<ComponentProperties> fromSerializedProperties = ComponentProperties
                        .fromSerialized(serializedProperties, ComponentProperties.class);
                if (fromSerializedProperties != null) {
                    return fromSerializedProperties.properties;
                }
            }
        }
        return null;
    }
}
