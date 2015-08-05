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
package org.talend.designer.dbmap.language.oracle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.process.IConnection;
import org.talend.designer.dbmap.DbMapComponent;
import org.talend.designer.dbmap.external.data.ExternalDbMapTable;
import org.talend.designer.dbmap.language.generation.DbGenerationManager;
import org.talend.designer.dbmap.language.generation.DbMapSqlConstants;

/**
 * 
 * wzhang class global comment. Detailled comment
 */
public class OracleGenerationManager extends DbGenerationManager {

    private static final String JOIN = "(+)";

    public OracleGenerationManager() {
        super(new OracleLanguage());
    }

    @Override
    public String buildSqlSelect(DbMapComponent component, String outputTableName) {
        return super.buildSqlSelect(component, outputTableName);
    }

    @Override
    protected String getSpecialRightJoin(ExternalDbMapTable table) {
        // when use oracle's right join (+)
        if (language.getJoin(table.getJoinType()) == OracleLanguage.ORACLEJOIN.RIGHT_OUTER_JOIN_ORACLE) {
            return JOIN;
        }
        return DbMapSqlConstants.EMPTY;
    }

    @Override
    protected String getSpecialLeftJoin(ExternalDbMapTable table) {
        // when use oracle's left join (+)
        if (language.getJoin(table.getJoinType()) == OracleLanguage.ORACLEJOIN.LEFT_OUTER_JOIN_ORACLE) {
            return JOIN + DbMapSqlConstants.SPACE;
        }
        return DbMapSqlConstants.EMPTY;
    }

    @Override
    protected String addQuoteForSpecialChar(String expression, DbMapComponent component) {
        List<String> specialList = new ArrayList<String>();
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<IConnection> inputConnections = (List<IConnection>) component.getIncomingConnections();
        if (inputConnections == null) {
            return expression;
        }
        for (IConnection iconn : inputConnections) {
            IMetadataTable metadataTable = iconn.getMetadataTable();
            if (metadataTable != null) {
                List<IMetadataColumn> lColumn = metadataTable.getListColumns();
                for (IMetadataColumn co : lColumn) {
                    String columnLabel = co.getOriginalDbColumnName();
                    String exp = MetadataToolHelper.validateValueNoLengthLimit(columnLabel);
                    if (!exp.equals(columnLabel)) {
                        specialList.add(columnLabel);
                    }
                }
            }
        }
        for (String specialColumn : specialList) {
            if (expression.contains(specialColumn)) {
                if (map.get(expression) == null) {
                    List<String> list = new ArrayList<String>();
                    list.add(specialColumn);
                    map.put(expression, list);
                } else {
                    List<String> list = map.get(expression);
                    list.add(specialColumn);
                }
            }
        }
        if (map.size() > 0) {
            List<String> list = map.get(expression);
            Collections.sort(list);
            String specialColumn = list.get(list.size() - 1);
            if (expression.contains(specialColumn)) {
                int begin = expression.indexOf(specialColumn);
                int length = specialColumn.length();
                int allLength = expression.length();
                expression = expression.substring(0, begin) + "\\\"" + expression.substring(begin, begin + length) + "\\\""
                        + expression.substring(begin + length, allLength);
                return expression;
            }
        }
        return expression;
    }

}
