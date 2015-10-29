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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.talend.component.ui.model.genericMetadata.SubContainer;
import org.talend.components.api.schema.Schema;
import org.talend.components.api.schema.SchemaElement;
import org.talend.components.api.schema.SchemaElement.Type;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;

/**
 * created by ycbai on 2015年10月28日 Detailled comment
 *
 */
public class SchemaUtils {

    public static List<MetadataTable> getMetadataTables(orgomg.cwm.objectmodel.core.Package parentPackage) {
        List<MetadataTable> metadataTables = new ArrayList<>();
        List<SubContainer> subContainers = PackageHelper.getOwnedElements(parentPackage, SubContainer.class);
        for (SubContainer subContainer : subContainers) {
            List<MetadataTable> tables = PackageHelper.getOwnedElements(subContainer, MetadataTable.class);
            if (tables.size() > 0) {
                metadataTables.addAll(tables);
            }
            metadataTables.addAll(getMetadataTables(subContainer));
        }
        return metadataTables;
    }

    public static MetadataTable getMetadataTable(Connection connection, String tabLabel) {
        List<MetadataTable> tables = getMetadataTables(connection);
        for (MetadataTable table : tables) {
            if (tabLabel != null && tabLabel.equals(table.getLabel())) {
                return table;
            }
        }
        return null;
    }

    public static MetadataTable findByLabel(Connection connection, String label) {
        if (connection == null) {
            throw new IllegalArgumentException("null connection"); //$NON-NLS-1$
        }
        if (label == null || "".equals(label)) {
            throw new IllegalArgumentException("null/empty label"); //$NON-NLS-1$
        }
        Set<MetadataTable> tables = ConnectionHelper.getTables(connection);
        for (MetadataTable table : tables) {
            if (label.equals(table.getLabel())) {
                return table;
            }
        }
        return null;
    }

    public static void convertComponentSchemaIntoTalendSchema(Schema schema, MetadataTable metadataTable) {
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

    private static void setupMetadataColumn(MetadataColumn metadataColumn, SchemaElement schemaElement) {
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

}
