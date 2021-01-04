// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dbmap.language.generation;

import java.util.List;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.Dbms;
import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.dbmap.DbMapComponent;
import org.talend.designer.dbmap.language.GenericDbLanguage;

/**
 * wzhang class global comment. Detailled comment
 */
public class GenericDbGenerationManager extends DbGenerationManager {

    public GenericDbGenerationManager() {
        super(new GenericDbLanguage());
    }

    @Override
    public String buildSqlSelect(DbMapComponent component, String outputTableName) {
        return super.buildSqlSelect(component, outputTableName);
    }

    @Override
    protected String getQuote(DbMapComponent component) {
        String quote = TalendQuoteUtils.QUOTATION_MARK;
        IElementParameter mappingPara = component.getElementParameter(EParameterName.MAPPING.getName());
        if (mappingPara == null) {
            return quote;
        }
        String mapping = (String) mappingPara.getValue();
        if (mapping == null) {
            return quote;
        }
        MappingTypeRetriever mappingTypeRetriever = MetadataTalendType.getMappingTypeRetriever(mapping);
        if (mappingTypeRetriever == null) {
            return quote;
        }
        Dbms dbms = mappingTypeRetriever.getDbms();
        String product = dbms.getProduct();
        if (isUseDelimitedIdentifiers() && ExtractMetaDataUtils.SNOWFLAKE.equalsIgnoreCase(product)) {
            return quote;
        }
        EDatabaseTypeName type = EDatabaseTypeName.getTypeFromProductName(product);
        return TalendQuoteUtils.getQuoteByDBType(type);
    }

    @Override
    protected String getHandledTableName(DbMapComponent component, String tableName, String alias) {
        if (alias == null) {
            return replaceVariablesForExpression(component, tableName);
        } else {
            List<IConnection> inputConnections = (List<IConnection>) component.getIncomingConnections();
            IConnection iconn = this.getConnectonByName(inputConnections, tableName);
            if (iconn != null) {
                String handledTableName = "";
                boolean inputIsELTDBMap = false;
                INode source = iconn.getSource();
                String schemaValue = "";
                String tableValue = "";
                if (source != null) {
                    inputIsELTDBMap = isELTDBMap(source);
                    if (inputIsELTDBMap) {
                        tableValue = iconn.getName();
                    } else {
                        IElementParameter schemaParam = source.getElementParameter("ELT_SCHEMA_NAME");
                        IElementParameter tableParam = source.getElementParameter("ELT_TABLE_NAME");
                        if (schemaParam != null && schemaParam.getValue() != null) {
                            schemaValue = schemaParam.getValue().toString();
                        }
                        if (tableParam != null && tableParam.getValue() != null) {
                            tableValue = tableParam.getValue().toString();
                        }
                    }
                }

                String schemaNoQuote = TalendTextUtils.removeQuotes(schemaValue);
                boolean hasSchema = !"".equals(schemaNoQuote);
                if (hasSchema) {
                    handledTableName = schemaValue + "+\".\"+";
                }
                handledTableName = handledTableName + tableValue;
                if (isUseDelimitedIdentifiers() && ExtractMetaDataUtils.SNOWFLAKE.equalsIgnoreCase(getDbType(component))) {
                    handledTableName = TalendTextUtils.removeQuotes(handledTableName);
                    return "\" +\"\\" + "\"" + handledTableName + "\\" + "\"\"+ \"";
                }
                return "\" +" + handledTableName + "+ \"";
            }
        }
        return tableName;
    }

    private String getDbType(DbMapComponent component) {
        IElementParameter mappingPara = component.getElementParameter(EParameterName.MAPPING.getName());
        if (mappingPara == null) {
            return null;
        }
        String mapping = (String) mappingPara.getValue();
        if (mapping == null) {
            return null;
        }
        MappingTypeRetriever mappingTypeRetriever = MetadataTalendType.getMappingTypeRetriever(mapping);
        if (mappingTypeRetriever == null) {
            return null;
        }
        Dbms dbms = mappingTypeRetriever.getDbms();
        if (dbms == null) {
            return null;
        }
        return dbms.getProduct();
    }
}
