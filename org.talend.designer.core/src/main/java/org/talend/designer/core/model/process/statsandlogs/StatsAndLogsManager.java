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
package org.talend.designer.core.model.process.statsandlogs;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.model.process.DataNode;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class StatsAndLogsManager {

    private static final String LOG_UNIQUE_NAME = "talendLogs";

    private static final String STAT_UNIQUE_NAME = "talendStats";

    public static List<DataNode> getStatsAndLogsNodes(Process process) {
        List<DataNode> nodeList = new ArrayList<DataNode>();

        // if (1 == 1) {
        // return nodeList;
        // }
        String dbOutput = null;
        // dbOutput = "tMysqlOutput";
        // dbOutput = "tPostgresqlOutput";
        // dbOutput = "tOracleOutput";
        // dbOutput = "tMSSqlOutput";
        // dbOutput = "tDBOutput";
        // dbOutput = "tDB2Output";
        // dbOutput = "tSybaseOutput";

        // these two functions are for test only.
        // createTempParams(process);
        // fillParamsFor(process, dbOutput);

        // process.getElementParameter("DB_TYPE").setValue("tMysqlOutput");

        boolean dbFlag = (Boolean) process.getElementParameter("ON_DATABASE_FLAG").getValue();
        if (!dbFlag) {
            dbOutput = null;
        } else {
            dbOutput = (String) process.getElementParameter("DB_TYPE").getValue();
        }
        boolean file = (Boolean) process.getElementParameter("ON_FILES_FLAG").getValue();

        if (!file && !dbFlag) {
            return nodeList;
        }

        String basePath = (String) process.getElementParameter("FILE_PATH").getValue();
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
            basePath += ". '/' .";
        } else {
            basePath += "+ \"/\" +";
        }
        DataNode logsNode = createLogsNode(file, dbOutput);
        logsNode.getElementParameter("FILENAME").setValue(
                basePath + process.getElementParameter("FILENAME_LOGS").getValue());
        logsNode.getElementParameter("HOST").setValue(process.getElementParameter("HOST").getValue());
        logsNode.getElementParameter("PORT").setValue(process.getElementParameter("PORT").getValue());
        logsNode.getElementParameter("SCHEMA_DB").setValue(process.getElementParameter("SCHEMA_DB").getValue());
        logsNode.getElementParameter("DBNAME").setValue(process.getElementParameter("DBNAME").getValue());
        logsNode.getElementParameter("USER").setValue(process.getElementParameter("USER").getValue());
        logsNode.getElementParameter("PASS").setValue(process.getElementParameter("PASS").getValue());
        logsNode.getElementParameter("TABLE").setValue(process.getElementParameter("TABLE_LOGS").getValue());
        logsNode.setProcess(process);
        nodeList.add(logsNode);

        DataNode statsNode = createStatsNode(file, dbOutput);
        statsNode.getElementParameter("FILENAME").setValue(
                basePath + process.getElementParameter("FILENAME_STATS").getValue());
        statsNode.getElementParameter("HOST").setValue(process.getElementParameter("HOST").getValue());
        statsNode.getElementParameter("PORT").setValue(process.getElementParameter("PORT").getValue());
        statsNode.getElementParameter("SCHEMA_DB").setValue(process.getElementParameter("SCHEMA_DB").getValue());
        statsNode.getElementParameter("DBNAME").setValue(process.getElementParameter("DBNAME").getValue());
        statsNode.getElementParameter("USER").setValue(process.getElementParameter("USER").getValue());
        statsNode.getElementParameter("PASS").setValue(process.getElementParameter("PASS").getValue());
        statsNode.getElementParameter("TABLE").setValue(process.getElementParameter("TABLE_STATS").getValue());
        statsNode.setProcess(process);
        nodeList.add(statsNode);

        return nodeList;
    }
//
//    private static void fillParamsFor(Process process, String dbOutput) {
//        process.getElementParameter("FILENAME_LOGS").setValue(TalendTextUtils.addQuotes("c:/tmp/logs.csv"));
//        process.getElementParameter("FILENAME_STATS").setValue(TalendTextUtils.addQuotes("c:/tmp/stats.csv"));
//        if (dbOutput == null) {
//            return;
//        }
//        process.getElementParameter("HOST").setValue(TalendTextUtils.addQuotes("talend-dbms"));
//        process.getElementParameter("USER").setValue(TalendTextUtils.addQuotes("root"));
//        process.getElementParameter("PASS").setValue(TalendTextUtils.addQuotes("toor"));
//        process.getElementParameter("TABLE_LOGS").setValue(TalendTextUtils.addQuotes(LOG_UNIQUE_NAME + "_test1"));
//        process.getElementParameter("TABLE_STATS").setValue(TalendTextUtils.addQuotes(STAT_UNIQUE_NAME + "_test1"));
//        process.getElementParameter("DBNAME").setValue(TalendTextUtils.addQuotes("Talend"));
//        if (dbOutput.equals("tMysqlOutput")) {
//            process.getElementParameter("PORT").setValue(TalendTextUtils.addQuotes("3306"));
//        } else if (dbOutput.equals("tPostgresqlOutput")) {
//            process.getElementParameter("PORT").setValue(TalendTextUtils.addQuotes("5432"));
//            process.getElementParameter("SCHEMA_DB").setValue(TalendTextUtils.addQuotes("\"mypostgres\""));
//            process.getElementParameter("TABLE_LOGS").setValue(
//                    TalendTextUtils.addQuotes("\"" + LOG_UNIQUE_NAME + "_test1\""));
//            process.getElementParameter("TABLE_STATS").setValue(
//                    TalendTextUtils.addQuotes("\"" + STAT_UNIQUE_NAME + "_test1\""));
//        } else if (dbOutput.equals("tOracleOutput")) {
//            process.getElementParameter("PORT").setValue(TalendTextUtils.addQuotes("1521"));
//            process.getElementParameter("DBNAME").setValue(TalendTextUtils.addQuotes("TALEND"));
//        } else if (dbOutput.equals("tMSSqlOutput")) {
//            process.getElementParameter("PORT").setValue(TalendTextUtils.addQuotes("1433"));
//        } else if (dbOutput.equals("tDBOutput")) {
//            process.getElementParameter("DBNAME").setValue(TalendTextUtils.addQuotes("msServer"));
//        } else if (dbOutput.equals("tDB2Output")) {
//            process.getElementParameter("PORT").setValue(TalendTextUtils.addQuotes("50000"));
//            process.getElementParameter("DBNAME").setValue(TalendTextUtils.addQuotes("TALEND"));
//        } else if (dbOutput.equals("tSybaseOutput")) {
//            process.getElementParameter("HOST").setValue(TalendTextUtils.addQuotes("SOYA104"));
//            process.getElementParameter("USER").setValue(TalendTextUtils.addQuotes("sa"));
//            process.getElementParameter("PASS").setValue(TalendTextUtils.addQuotes(""));
//            process.getElementParameter("PORT").setValue(TalendTextUtils.addQuotes("5001"));
//            process.getElementParameter("DBNAME").setValue(TalendTextUtils.addQuotes("soyatec_test"));
//        }
//    }
//
//    private static void createTempParams(Process process) {
//        List<IElementParameter> params = (List<IElementParameter>) process.getElementParameters();
//
//        IElementParameter newParam = new ElementParameter(process);
//        newParam.setName("FILENAME_LOGS");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("USE_FILE");
//        newParam.setField(EParameterFieldType.CHECK);
//        newParam.setValue(Boolean.FALSE);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("USE_DB");
//        newParam.setField(EParameterFieldType.CHECK);
//        newParam.setValue(Boolean.FALSE);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("DB_TYPE");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("FILENAME_STATS");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("HOST");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("PORT");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("DBNAME");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("SCHEMA_DB");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("USER");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("PASS");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("TABLE_LOGS");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//
//        newParam = new ElementParameter(process);
//        newParam.setName("TABLE_STATS");
//        newParam.setField(EParameterFieldType.TEXT);
//        params.add(newParam);
//    }

    private static DataNode createLogsNode(boolean useFile, String dbOutput) {
        JobLogsComponent logsComponent = new JobLogsComponent(useFile, dbOutput);
        DataNode logsNode = new DataNode(logsComponent, LOG_UNIQUE_NAME);
        logsNode.setStart(true);
        logsNode.setSubProcessStart(true);
        logsNode.setActivate(true);
        logsNode.getMetadataList().clear();

        // load the tLogCatcher to get the schema.
        IComponent tmpComponent = ComponentsFactoryProvider.getInstance().get("tLogCatcher");
        DataNode tmpNode = new DataNode(tmpComponent, "tmp");
        boolean found = false;
        for (int k = 0; k < tmpNode.getElementParameters().size() && !found; k++) {
            IElementParameter currentParam = tmpNode.getElementParameters().get(k);
            if (currentParam.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                Object value = currentParam.getValue();
                if (value instanceof IMetadataTable) {
                    IMetadataTable table = ((IMetadataTable) value).clone();
                    table.setTableName(LOG_UNIQUE_NAME);
                    logsNode.getMetadataList().add(table);
                }
                found = true;
            }
        }
        return logsNode;
    }

    private static DataNode createStatsNode(boolean useFile, String dbOutput) {
        JobStatsComponent statsComponent = new JobStatsComponent(useFile, dbOutput);
        DataNode statsNode = new DataNode(statsComponent, STAT_UNIQUE_NAME);
        statsNode.setStart(true);
        statsNode.setSubProcessStart(true);
        statsNode.setActivate(true);

        statsNode.getMetadataList().clear();

        // load the tLogCatcher to get the schema.
        IComponent tmpComponent = ComponentsFactoryProvider.getInstance().get("tStatCatcher");
        DataNode tmpNode = new DataNode(tmpComponent, "tmp");
        boolean found = false;
        for (int k = 0; k < tmpNode.getElementParameters().size() && !found; k++) {
            IElementParameter currentParam = tmpNode.getElementParameters().get(k);
            if (currentParam.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                Object value = currentParam.getValue();
                if (value instanceof IMetadataTable) {
                    IMetadataTable table = ((IMetadataTable) value).clone();
                    table.setTableName(STAT_UNIQUE_NAME);
                    statsNode.getMetadataList().add(table);
                }
                found = true;
            }
        }
        return statsNode;
    }
}
