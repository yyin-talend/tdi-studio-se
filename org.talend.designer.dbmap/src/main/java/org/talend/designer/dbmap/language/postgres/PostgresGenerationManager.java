// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dbmap.language.postgres;

import java.util.regex.Pattern;

import org.talend.designer.dbmap.DbMapComponent;
import org.talend.designer.dbmap.language.GenericDbLanguage;
import org.talend.designer.dbmap.language.generation.DbGenerationManager;

/**
 * 
 * add for bug TDI-20733,user elt,the query is generated wrong in tPostgresqlMap
 */
public class PostgresGenerationManager extends DbGenerationManager {

    public PostgresGenerationManager() {
        super(new GenericDbLanguage());
    }

    @Override
    public String buildSqlSelect(DbMapComponent component, String outputTableName) {
        return super.buildSqlSelect(component, outputTableName);
    }

    @Override
    protected String addQuit(String exp) {
        if ("".equals(exp) || exp == null) {
            return exp;
        }
        exp = exp.trim();
        StringBuilder sb = new StringBuilder();
        if (!exp.contains(".")) {
            return sb.append("\\\"").append(exp).append("\\\"").toString();
        }
        boolean flag = Pattern.compile("\\s+").matcher(exp).find();
        if (flag) {
            String[] spl = exp.split("\\s+");
            for (String elt : spl) {
                sb.append(getNewStr(elt)).append("  ");
            }
        } else {
            sb.append(getNewStr(exp));
        }
        return sb.toString().trim();
    }

    private String getNewStr(String newStr) {
        StringBuilder sb = new StringBuilder();
        String[] word = newStr.split("\\.");
        for (String elt : word) {
            sb.append("\\\"").append(elt).append("\\\"").append(".");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    protected boolean isPostgres() {
        return true;
    }

}
