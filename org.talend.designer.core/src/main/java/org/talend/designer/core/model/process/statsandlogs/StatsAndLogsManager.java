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

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.process.DataNode;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.preferences.StatsAndLogsConstants;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class StatsAndLogsManager {

    private static final String LOG_UNIQUE_NAME = "talendLogs"; //$NON-NLS-1$

    private static final String STAT_UNIQUE_NAME = "talendStats"; //$NON-NLS-1$

    private static final String METER_UNIQUE_NAME = "talendMeter"; //$NON-NLS-1$

    public static boolean isStatsAndLogsActivated(IProcess process) {
        String dbOutput = null;
        boolean dbFlag = ((Boolean) process.getElementParameter(EParameterName.ON_DATABASE_FLAG.getName()).getValue())
                && process.getElementParameter(EParameterName.ON_DATABASE_FLAG.getName()).isShow(
                        process.getElementParameters());
        if (!dbFlag) {
            dbOutput = null;
        } else {
            dbOutput = (String) process.getElementParameter(EParameterName.DB_TYPE.getName()).getValue(); //$NON-NLS-1$
            if (dbOutput == null || dbOutput.equals("")) {
                dbOutput = null;
                dbFlag = false;
            }
        }
        boolean file = ((Boolean) process.getElementParameter(EParameterName.ON_FILES_FLAG.getName()).getValue())
                && process.getElementParameter(EParameterName.ON_FILES_FLAG.getName()).isShow(
                        process.getElementParameters());

        boolean console = ((Boolean) process.getElementParameter(EParameterName.ON_CONSOLE_FLAG.getName()).getValue())
                && process.getElementParameter(EParameterName.ON_CONSOLE_FLAG.getName()).isShow(
                        process.getElementParameters());

        if (!file && !dbFlag && !console) {
            return false;
        }
        return true;
    }

    public static List<DataNode> getStatsAndLogsNodes(Process process) {
        List<DataNode> nodeList = new ArrayList<DataNode>();

        String dbOutput = null;

        boolean dbFlag = ((Boolean) process.getElementParameter(EParameterName.ON_DATABASE_FLAG.getName()).getValue())
                && process.getElementParameter(EParameterName.ON_DATABASE_FLAG.getName()).isShow(
                        process.getElementParameters());
        if (!dbFlag) {
            dbOutput = null;
        } else {
            dbOutput = (String) process.getElementParameter(EParameterName.DB_TYPE.getName()).getValue(); //$NON-NLS-1$
            if (dbOutput == null || dbOutput.equals("")) {
                dbOutput = null;
                dbFlag = false;
            }
        }
        boolean file = ((Boolean) process.getElementParameter(EParameterName.ON_FILES_FLAG.getName()).getValue())
                && process.getElementParameter(EParameterName.ON_FILES_FLAG.getName()).isShow(
                        process.getElementParameters());

        boolean console = ((Boolean) process.getElementParameter(EParameterName.ON_CONSOLE_FLAG.getName()).getValue())
                && process.getElementParameter(EParameterName.ON_CONSOLE_FLAG.getName()).isShow(
                        process.getElementParameters());

        if (!file && !dbFlag && !console) {
            return nodeList;
        }

        boolean useStats = ((Boolean) process.getElementParameter(EParameterName.ON_STATCATCHER_FLAG.getName())
                .getValue())
                && process.getElementParameter(EParameterName.ON_STATCATCHER_FLAG.getName()).isShow(
                        process.getElementParameters());

        boolean useLogs = ((Boolean) process.getElementParameter(EParameterName.ON_LOGCATCHER_FLAG.getName())
                .getValue())
                && process.getElementParameter(EParameterName.ON_LOGCATCHER_FLAG.getName()).isShow(
                        process.getElementParameters());

        boolean useMetter = ((Boolean) process.getElementParameter(EParameterName.ON_METERCATCHER_FLAG.getName())
                .getValue()) //$NON-NLS-1$
                && process.getElementParameter(EParameterName.ON_METERCATCHER_FLAG.getName()).isShow(
                        process.getElementParameters());

        String basePath = (String) process.getElementParameter(EParameterName.FILE_PATH.getName()).getValue();
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
            basePath = basePath.replace("\\", "/") + ". '/' ."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } else {
            basePath = basePath.replace("\\", "/") + "+ \"/\" +"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        if (useLogs) {
            DataNode logsNode = createLogsNode(file, console, dbOutput);
            logsNode.getElementParameter("FILENAME").setValue(//$NON-NLS-1$
                    basePath + process.getElementParameter(EParameterName.FILENAME_LOGS.getName()).getValue());
            logsNode.getElementParameter(EParameterName.HOST.getName()).setValue(
                    process.getElementParameter(EParameterName.HOST.getName()).getValue());
            logsNode.getElementParameter(EParameterName.PORT.getName()).setValue(
                    process.getElementParameter(EParameterName.PORT.getName()).getValue());
            logsNode.getElementParameter(EParameterName.SCHEMA_DB.getName()).setValue(
                    process.getElementParameter(EParameterName.SCHEMA_DB.getName()).getValue());
            logsNode.getElementParameter(EParameterName.DBNAME.getName()).setValue(
                    process.getElementParameter(EParameterName.DBNAME.getName()).getValue());
            logsNode.getElementParameter(EParameterName.USER.getName()).setValue(
                    process.getElementParameter(EParameterName.USER.getName()).getValue());
            logsNode.getElementParameter(EParameterName.PASS.getName()).setValue(
                    process.getElementParameter(EParameterName.PASS.getName()).getValue());
            logsNode.getElementParameter("TABLE").setValue(//$NON-NLS-1$
                    process.getElementParameter(EParameterName.TABLE_LOGS.getName()).getValue());
            logsNode.getElementParameter(EParameterName.CATCH_RUNTIME_ERRORS.getName()).setValue(
                    process.getElementParameter(EParameterName.CATCH_RUNTIME_ERRORS.getName()).getValue());
            logsNode.getElementParameter(EParameterName.CATCH_USER_ERRORS.getName()).setValue(
                    process.getElementParameter(EParameterName.CATCH_USER_ERRORS.getName()).getValue());
            logsNode.getElementParameter(EParameterName.CATCH_USER_WARNING.getName()).setValue(
                    process.getElementParameter(EParameterName.CATCH_USER_WARNING.getName()).getValue());

            logsNode.setProcess(process);
            nodeList.add(logsNode);
        }

        if (useStats) {
            DataNode statsNode = createStatsNode(file, console, dbOutput);
            statsNode.getElementParameter("FILENAME").setValue(//$NON-NLS-1$
                    basePath + process.getElementParameter(EParameterName.FILENAME_STATS.getName()).getValue()); //$NON-NLS-1$
            statsNode.getElementParameter(EParameterName.HOST.getName()).setValue(
                    process.getElementParameter(EParameterName.HOST.getName()).getValue());
            statsNode.getElementParameter(EParameterName.PORT.getName()).setValue(
                    process.getElementParameter(EParameterName.PORT.getName()).getValue());
            statsNode.getElementParameter(EParameterName.SCHEMA_DB.getName()).setValue(
                    process.getElementParameter(EParameterName.SCHEMA_DB.getName()).getValue());
            statsNode.getElementParameter(EParameterName.DBNAME.getName()).setValue(
                    process.getElementParameter(EParameterName.DBNAME.getName()).getValue());
            statsNode.getElementParameter(EParameterName.USER.getName()).setValue(
                    process.getElementParameter(EParameterName.USER.getName()).getValue());
            statsNode.getElementParameter(EParameterName.PASS.getName()).setValue(
                    process.getElementParameter(EParameterName.PASS.getName()).getValue());
            statsNode.getElementParameter("TABLE").setValue(//$NON-NLS-1$
                    process.getElementParameter(EParameterName.TABLE_STATS.getName()).getValue());

            statsNode.setProcess(process);
            nodeList.add(statsNode);
        }

        if (useMetter) {
            DataNode statsNode = createMetterNode(file, console, dbOutput);
            statsNode.getElementParameter("FILENAME").setValue(//$NON-NLS-1$
                    basePath + process.getElementParameter(EParameterName.FILENAME_METTER.getName()).getValue());
            statsNode.getElementParameter(EParameterName.HOST.getName()).setValue(
                    process.getElementParameter(EParameterName.HOST.getName()).getValue());
            statsNode.getElementParameter(EParameterName.PORT.getName()).setValue(
                    process.getElementParameter(EParameterName.PORT.getName()).getValue());
            statsNode.getElementParameter(EParameterName.SCHEMA_DB.getName()).setValue(
                    process.getElementParameter(EParameterName.SCHEMA_DB.getName()).getValue());
            statsNode.getElementParameter(EParameterName.DBNAME.getName()).setValue(
                    process.getElementParameter(EParameterName.DBNAME.getName()).getValue());
            statsNode.getElementParameter(EParameterName.USER.getName()).setValue(
                    process.getElementParameter(EParameterName.USER.getName()).getValue());
            statsNode.getElementParameter(EParameterName.PASS.getName()).setValue(
                    process.getElementParameter(EParameterName.PASS.getName()).getValue());
            statsNode.getElementParameter("TABLE").setValue(//$NON-NLS-1$
                    process.getElementParameter(EParameterName.TABLE_METER.getName()).getValue());

            statsNode.setProcess(process);
            nodeList.add(statsNode);
        }

        return nodeList;
    }

    private static DataNode createLogsNode(boolean useFile, boolean console, String dbOutput) {
        JobLogsComponent logsComponent = new JobLogsComponent(useFile, console, dbOutput);
        DataNode logsNode = new DataNode(logsComponent, LOG_UNIQUE_NAME);
        logsNode.setStart(true);
        logsNode.setSubProcessStart(true);
        logsNode.setActivate(true);
        logsNode.getMetadataList().clear();

        // load the tLogCatcher to get the schema.
        IComponent tmpComponent = ComponentsFactoryProvider.getInstance().get("tLogCatcher"); //$NON-NLS-1$
        DataNode tmpNode = new DataNode(tmpComponent, "tmp"); //$NON-NLS-1$
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

    private static DataNode createStatsNode(boolean useFile, boolean console, String dbOutput) {
        JobStatsComponent statsComponent = new JobStatsComponent(useFile, console, dbOutput);
        DataNode statsNode = new DataNode(statsComponent, STAT_UNIQUE_NAME);
        statsNode.setStart(true);
        statsNode.setSubProcessStart(true);
        statsNode.setActivate(true);

        statsNode.getMetadataList().clear();

        // load the tStatCatcher to get the schema.
        IComponent tmpComponent = ComponentsFactoryProvider.getInstance().get("tStatCatcher"); //$NON-NLS-1$
        DataNode tmpNode = new DataNode(tmpComponent, "tmp"); //$NON-NLS-1$
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

    private static DataNode createMetterNode(boolean useFile, boolean console, String dbOutput) {
        JobMetterComponent statsComponent = new JobMetterComponent(useFile, console, dbOutput);
        DataNode statsNode = new DataNode(statsComponent, METER_UNIQUE_NAME);
        statsNode.setStart(true);
        statsNode.setSubProcessStart(true);
        statsNode.setActivate(true);

        statsNode.getMetadataList().clear();

        // load the tFlowMeterCatcher to get the schema.
        IComponent tmpComponent = ComponentsFactoryProvider.getInstance().get("tFlowMeterCatcher"); //$NON-NLS-1$
        DataNode tmpNode = new DataNode(tmpComponent, "tmp"); //$NON-NLS-1$
        boolean found = false;
        for (int k = 0; k < tmpNode.getElementParameters().size() && !found; k++) {
            IElementParameter currentParam = tmpNode.getElementParameters().get(k);
            if (currentParam.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                Object value = currentParam.getValue();
                if (value instanceof IMetadataTable) {
                    IMetadataTable table = ((IMetadataTable) value).clone();
                    table.setTableName(METER_UNIQUE_NAME);
                    statsNode.getMetadataList().add(table);
                }
                found = true;
            }
        }
        return statsNode;
    }

    public static void createStatsAndLogsParameters(IProcess process) {
        statsAndLogsParametersTitlePart(process);
        statsAndLogsParametersFilePart(process);
        statsAndLogsParametersDBPart(process);
        statsAndLogsParametersFinalPart(process);

    }

    private static void statsAndLogsParametersTitlePart(IProcess process) {
        ElementParameter param;
        IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();

        List<IElementParameter> paramList = (List<IElementParameter>) process.getElementParameters();

        String languagePrefix = LanguageManager.getCurrentLanguage().toString() + "_";

        param = new ElementParameter(process);
        param.setName(EParameterName.UPDATE_COMPONENTS.getName());
        param.setValue(Boolean.FALSE);
        param.setDisplayName(EParameterName.UPDATE_COMPONENTS.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(1);
        param.setReadOnly(true);
        param.setRequired(false);
        param.setShow(false);
        paramList.add(param);

        param = new ElementParameter(process);
        param.setName(EParameterName.ON_STATCATCHER_FLAG.getName());
        param.setValue(preferenceStore.getBoolean(languagePrefix + EParameterName.ON_STATCATCHER_FLAG.getName()));
        param.setDisplayName(EParameterName.ON_STATCATCHER_FLAG.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(1);
        paramList.add(param);

        param = new ElementParameter(process);
        param.setName(EParameterName.ON_LOGCATCHER_FLAG.getName());
        param.setValue(preferenceStore.getBoolean(languagePrefix + EParameterName.ON_LOGCATCHER_FLAG.getName()));
        param.setDisplayName(EParameterName.ON_LOGCATCHER_FLAG.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(1);
        paramList.add(param);

        param = new ElementParameter(process);
        param.setName(EParameterName.ON_METERCATCHER_FLAG.getName());
        param.setValue(preferenceStore.getBoolean(languagePrefix + EParameterName.ON_METERCATCHER_FLAG.getName()));
        param.setDisplayName(EParameterName.ON_METERCATCHER_FLAG.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(1);
        paramList.add(param);

        // on console
        param = new ElementParameter(process);
        param.setName(EParameterName.ON_CONSOLE_FLAG.getName());
        param.setValue(Boolean.FALSE);
        param.setDisplayName(EParameterName.ON_CONSOLE_FLAG.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(2);
        param
                .setShowIf("(ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);
    }

    private static void statsAndLogsParametersFilePart(IProcess process) {
        ElementParameter param;
        IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();

        List<IElementParameter> paramList = (List<IElementParameter>) process.getElementParameters();

        String languagePrefix = LanguageManager.getCurrentLanguage().toString() + "_";
        // on files
        param = new ElementParameter(process);
        param.setName(EParameterName.ON_FILES_FLAG.getName());
        param.setValue(preferenceStore.getBoolean(languagePrefix + EParameterName.ON_FILES_FLAG.getName()));
        param.setDisplayName(EParameterName.ON_FILES_FLAG.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(10);
        param
                .setShowIf("(ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);

        // file path
        param = new ElementParameter(process);
        param.setName(EParameterName.FILE_PATH.getName());
        param.setValue(addQuotes(replaceSlash(preferenceStore.getString(languagePrefix
                + EParameterName.FILE_PATH.getName()))));
        param.setDisplayName(EParameterName.FILE_PATH.getDisplayName());
        param.setField(EParameterFieldType.DIRECTORY);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param
                .setShowIf("(ON_FILES_FLAG == 'true') and (ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        param.setNumRow(11);
        paramList.add(param);

        // stats file name
        param = new ElementParameter(process);
        param.setName(EParameterName.FILENAME_STATS.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.FILENAME_STATS.getName())));
        param.setDisplayName(EParameterName.FILENAME_STATS.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setShowIf("(ON_FILES_FLAG == 'true' and ON_STATCATCHER_FLAG == 'true')");
        param.setNumRow(12);

        paramList.add(param);

        param = new ElementParameter(process);
        param.setName(EParameterName.FILENAME_LOGS.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.FILENAME_LOGS.getName())));
        param.setDisplayName(EParameterName.FILENAME_LOGS.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setShowIf("(ON_FILES_FLAG == 'true' and ON_LOGCATCHER_FLAG == 'true')");
        param.setNumRow(13);
        paramList.add(param);

        param = new ElementParameter(process);
        param.setName(EParameterName.FILENAME_METTER.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.FILENAME_METTER.getName())));
        param.setDisplayName(EParameterName.FILENAME_METTER.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setShowIf("(ON_FILES_FLAG == 'true' and ON_METERCATCHER_FLAG == 'true')");
        param.setNumRow(14);
        paramList.add(param);
    }

    private static void statsAndLogsParametersDBPart(IProcess process) {
        ElementParameter param;
        IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();

        List<IElementParameter> paramList = (List<IElementParameter>) process.getElementParameters();

        // checks current language, if it is perl, set languageType to 0(default value), otherwise to 1.
        int languageType = 0;
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
            languageType = 1;
        }

        String languagePrefix = LanguageManager.getCurrentLanguage().toString() + "_";

        // on database
        param = new ElementParameter(process);
        param.setName(EParameterName.ON_DATABASE_FLAG.getName());
        param.setValue(preferenceStore.getBoolean(languagePrefix + EParameterName.ON_DATABASE_FLAG.getName()));
        param.setDisplayName(EParameterName.ON_DATABASE_FLAG.getDisplayName()); // On Database
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(50);
        param
                .setShowIf("(ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);

        param = new ElementParameter(process);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setName(EParameterName.PROPERTY_TYPE.getName());
        param.setDisplayName(EParameterName.PROPERTY_TYPE.getDisplayName());
        param.setListItemsDisplayName(new String[] { EmfComponent.TEXT_BUILTIN, EmfComponent.TEXT_REPOSITORY });
        param.setListItemsDisplayCodeName(new String[] { EmfComponent.BUILTIN, EmfComponent.REPOSITORY });
        param.setListItemsValue(new String[] { EmfComponent.BUILTIN, EmfComponent.REPOSITORY });
        param.setValue(preferenceStore.getString(languagePrefix + EParameterName.PROPERTY_TYPE.getName()));
        param.setNumRow(51);
        param.setField(EParameterFieldType.CLOSED_LIST);
        param.setRepositoryValue("DATABASE"); //$NON-NLS-1$
        param
                .setShowIf("(ON_DATABASE_FLAG == 'true') and (ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");

        paramList.add(param);

        param = new ElementParameter(process);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setName(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
        param.setDisplayName(EParameterName.REPOSITORY_PROPERTY_TYPE.getDisplayName());
        param.setListItemsDisplayName(new String[] {});
        param.setListItemsValue(new String[] {});
        param.setNumRow(51);
        param.setField(EParameterFieldType.CLOSED_LIST);
        param.setValue(preferenceStore.getString(languagePrefix + EParameterName.REPOSITORY_PROPERTY_TYPE.getName())); //$NON-NLS-1$
        param.setShow(false);
        param.setRequired(true);
        param
                .setShowIf("(ON_DATABASE_FLAG == 'true') and (ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");

        paramList.add(param);

        // dbType
        param = new ElementParameter(process);
        param.setName(EParameterName.DB_TYPE.getName());
        param.setValue(preferenceStore.getString(languagePrefix + EParameterName.DB_TYPE.getName()));
        param.setDisplayName(EParameterName.DB_TYPE.getDisplayName());
        param.setField(EParameterFieldType.CLOSED_LIST);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setListItemsDisplayName(StatsAndLogsConstants.DISPLAY_DBNAMES[languageType]);
        param.setListItemsValue(StatsAndLogsConstants.DB_COMPONENTS[languageType]);
        param.setListRepositoryItems(StatsAndLogsConstants.REPOSITORY_ITEMS[languageType]);
        param.setListItemsDisplayCodeName(StatsAndLogsConstants.CODE_LIST[languageType]);
        param.setNumRow(52);
        param.setRepositoryValue("TYPE"); //$NON-NLS-1$
        param.setRequired(true);
        param
                .setShowIf("(ON_DATABASE_FLAG == 'true') and (ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);

        // host
        param = new ElementParameter(process);
        param.setName(EParameterName.HOST.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.HOST.getName())));
        param.setDisplayName(EParameterName.HOST.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(52);
        param.setRepositoryValue("SERVER_NAME"); //$NON-NLS-1$
        param
                .setShowIf("(ON_DATABASE_FLAG == 'true') and (ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);

        // port
        param = new ElementParameter(process);
        param.setName(EParameterName.PORT.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.PORT.getName())));
        param.setDisplayName(EParameterName.PORT.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(52);
        param.setRepositoryValue("PORT"); //$NON-NLS-1$
        param
                .setShowIf("(ON_DATABASE_FLAG == 'true') and (ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);

        // dbName
        param = new ElementParameter(process);
        param.setName(EParameterName.DBNAME.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.DBNAME.getName())));
        param.setDisplayName(EParameterName.DBNAME.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(53);
        param.setRepositoryValue("SID"); //$NON-NLS-1$
        param
                .setShowIf("(ON_DATABASE_FLAG == 'true') and (ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);

        // schema
        param = new ElementParameter(process);
        param.setName(EParameterName.SCHEMA_DB.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.SCHEMA_DB.getName())));
        param.setDisplayName(EParameterName.SCHEMA_DB.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(53);
        param.setRepositoryValue("SCHEMA"); //$NON-NLS-1$
        param
                .setShowIf("(DB_TYPE=='OCLE' or DB_TYPE=='POSTGRESQL') and (ON_DATABASE_FLAG == 'true') and (ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);

        // username
        param = new ElementParameter(process);
        param.setName(EParameterName.USER.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.USER.getName())));
        param.setDisplayName(EParameterName.USER.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(54);
        param.setRequired(true);
        param.setRepositoryValue("USERNAME"); //$NON-NLS-1$
        param
                .setShowIf("(ON_DATABASE_FLAG == 'true') and (ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);

        // password
        param = new ElementParameter(process);
        param.setName(EParameterName.PASS.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.PASS.getName())));
        param.setDisplayName(EParameterName.PASS.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(54);
        param.setRequired(true);
        param.setRepositoryValue("PASSWORD"); //$NON-NLS-1$
        param
                .setShowIf("(ON_DATABASE_FLAG == 'true') and (ON_STATCATCHER_FLAG == 'true' or ON_LOGCATCHER_FLAG == 'true' or ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);

        // Stats table
        param = new ElementParameter(process);
        param.setName(EParameterName.TABLE_STATS.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.TABLE_STATS.getName())));
        param.setDisplayName(EParameterName.TABLE_STATS.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(55);
        param.setShowIf("(ON_DATABASE_FLAG == 'true' and ON_STATCATCHER_FLAG == 'true')");
        paramList.add(param);

        // Log table
        param = new ElementParameter(process);
        param.setName(EParameterName.TABLE_LOGS.getName());
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.TABLE_LOGS.getName())));
        param.setDisplayName(EParameterName.TABLE_LOGS.getDisplayName());
        param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(55);
        param.setShowIf("(ON_DATABASE_FLAG == 'true' and ON_LOGCATCHER_FLAG == 'true')");
        paramList.add(param);

        // Metter table
        param = new ElementParameter(process);
        param.setName(EParameterName.TABLE_METER.getName()); //$NON-NLS-1$
        param.setValue(addQuotes(preferenceStore.getString(languagePrefix + EParameterName.TABLE_METER.getName())));
        param.setDisplayName(EParameterName.TABLE_METER.getDisplayName());
       param.setField(EParameterFieldType.TEXT);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(55);
        param.setShowIf("(ON_DATABASE_FLAG == 'true' and ON_METERCATCHER_FLAG == 'true')");
        paramList.add(param);
    }

    private static void statsAndLogsParametersFinalPart(IProcess process) {
        ElementParameter param;
        IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();

        List<IElementParameter> paramList = (List<IElementParameter>) process.getElementParameters();

        String languagePrefix = LanguageManager.getCurrentLanguage().toString() + "_";

        // Catch runtime errors
        param = new ElementParameter(process);
        param.setName("CATCH_RUNTIME_ERRORS"); //$NON-NLS-1$
        param.setValue(preferenceStore.getBoolean(languagePrefix + EParameterName.CATCH_RUNTIME_ERRORS.getName()));
        param.setDisplayName(EParameterName.CATCH_RUNTIME_ERRORS.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(90);
        param.setShowIf("ON_LOGCATCHER_FLAG == 'true'");
        paramList.add(param);

        // Catch user errors
        param = new ElementParameter(process);
        param.setName("CATCH_USER_ERRORS"); //$NON-NLS-1$
        param.setValue(preferenceStore.getBoolean(languagePrefix + EParameterName.CATCH_USER_ERRORS.getName()));
        param.setDisplayName(EParameterName.CATCH_USER_ERRORS.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(90);
        param.setShowIf("ON_LOGCATCHER_FLAG == 'true'");
        paramList.add(param);

        // Catch user warning
        param = new ElementParameter(process);
        param.setName("CATCH_USER_WARNING"); //$NON-NLS-1$
        param.setValue(preferenceStore.getBoolean(languagePrefix + EParameterName.CATCH_USER_WARNING.getName()));
        param.setDisplayName(EParameterName.CATCH_USER_WARNING.getDisplayName());
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(90);
        param.setShowIf("ON_LOGCATCHER_FLAG == 'true'");
        paramList.add(param);

        // Catch realtime statistics
        param = new ElementParameter(process);
        param.setName("CATCH_REALTIME_STATS"); //$NON-NLS-1$
        param.setValue(preferenceStore.getBoolean(languagePrefix + EParameterName.CATCH_REALTIME_STATS.getName()));
        param.setDisplayName(EParameterName.CATCH_REALTIME_STATS.getDisplayName() + " ("
                + EParameterName.TSTATCATCHER_STATS.getDisplayName() + ")");
        param.setField(EParameterFieldType.CHECK);
        param.setCategory(EComponentCategory.STATSANDLOGS);
        param.setNumRow(90);
        param.setShowIf("ON_STATCATCHER_FLAG == 'true'");
        paramList.add(param);
    }

    /**
     * This function will add quotes only if necessary for the stats & logs.
     * 
     * @param str
     * @return
     */
    private static String addQuotes(String str) {
        // function?
        if (str.contains("(") && str.contains(")")) { //$NON-NLS-1$ //$NON-NLS-2$
            return str;
        }

        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            // if the user already added quotes (anywhere) then don't add.
            if (str.contains("\"")) { //$NON-NLS-1$
                return str;
            }
            break;
        default: // PERL
            // if the user already added quotes (anywhere) then don't add.
            if (str.contains("'")) { //$NON-NLS-1$
                return str;
            }
        }
        return TalendTextUtils.addQuotes(str);
    }

    private static String replaceSlash(String str) {
        String tempStr = str.replaceAll("\\\\", "/"); //$NON-NLS-1$ //$NON-NLS-2$
        return tempStr;
    }
}
