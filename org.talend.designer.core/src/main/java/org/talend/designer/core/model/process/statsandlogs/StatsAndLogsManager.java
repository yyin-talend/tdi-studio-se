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
import org.talend.designer.core.model.process.DataNode;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class StatsAndLogsManager {

    private static final String LOG_UNIQUE_NAME = "talendLogs"; //$NON-NLS-1$

    private static final String STAT_UNIQUE_NAME = "talendStats"; //$NON-NLS-1$

    public static List<DataNode> getStatsAndLogsNodes(Process process) {
        List<DataNode> nodeList = new ArrayList<DataNode>();

        String dbOutput = null;

        boolean dbFlag = (Boolean) process.getElementParameter("ON_DATABASE_FLAG").getValue(); //$NON-NLS-1$
        if (!dbFlag) {
            dbOutput = null;
        } else {
            dbOutput = (String) process.getElementParameter("DB_TYPE").getValue(); //$NON-NLS-1$
        }
        boolean file = (Boolean) process.getElementParameter("ON_FILE_FLAG").getValue(); //$NON-NLS-1$

        boolean console = (Boolean) process.getElementParameter("ON_CONSOLE_FLAG").getValue(); //$NON-NLS-1$

        if (!file && !dbFlag && !console) {
            return nodeList;
        }

        String basePath = (String) process.getElementParameter("FILE_PATH").getValue(); //$NON-NLS-1$
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
            basePath = basePath.replace("\\", "/") + ". '/' ."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } else {
            basePath = basePath.replace("\\", "/") + "+ \"/\" +"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        DataNode logsNode = createLogsNode(file, console, dbOutput);
        logsNode.getElementParameter("FILENAME").setValue( //$NON-NLS-1$
                basePath + process.getElementParameter("FILENAME_LOGS").getValue()); //$NON-NLS-1$
        logsNode.getElementParameter("HOST").setValue(process.getElementParameter("HOST").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        logsNode.getElementParameter("PORT").setValue(process.getElementParameter("PORT").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        logsNode.getElementParameter("SCHEMA_DB").setValue(process.getElementParameter("SCHEMA_DB").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        logsNode.getElementParameter("DBNAME").setValue(process.getElementParameter("DBNAME").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        logsNode.getElementParameter("USER").setValue(process.getElementParameter("USER").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        logsNode.getElementParameter("PASS").setValue(process.getElementParameter("PASS").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        logsNode.getElementParameter("TABLE").setValue(process.getElementParameter("TABLE_LOGS").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        logsNode.getElementParameter("CATCH_RUNTIME_ERRORS").setValue( //$NON-NLS-1$
                process.getElementParameter("CATCH_RUNTIME_ERRORS").getValue()); //$NON-NLS-1$
        logsNode.getElementParameter("CATCH_USER_ERRORS").setValue( //$NON-NLS-1$
                process.getElementParameter("CATCH_USER_ERRORS").getValue()); //$NON-NLS-1$
        logsNode.getElementParameter("CATCH_USER_WARNING").setValue( //$NON-NLS-1$
                process.getElementParameter("CATCH_USER_WARNING").getValue()); //$NON-NLS-1$

        logsNode.setProcess(process);
        nodeList.add(logsNode);

        DataNode statsNode = createStatsNode(file, console, dbOutput);
        statsNode.getElementParameter("FILENAME").setValue( //$NON-NLS-1$
                basePath + process.getElementParameter("FILENAME_STATS").getValue()); //$NON-NLS-1$
        statsNode.getElementParameter("HOST").setValue(process.getElementParameter("HOST").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        statsNode.getElementParameter("PORT").setValue(process.getElementParameter("PORT").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        statsNode.getElementParameter("SCHEMA_DB").setValue(process.getElementParameter("SCHEMA_DB").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        statsNode.getElementParameter("DBNAME").setValue(process.getElementParameter("DBNAME").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        statsNode.getElementParameter("USER").setValue(process.getElementParameter("USER").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        statsNode.getElementParameter("PASS").setValue(process.getElementParameter("PASS").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
        statsNode.getElementParameter("TABLE").setValue(process.getElementParameter("TABLE_STATS").getValue()); //$NON-NLS-1$ //$NON-NLS-2$

        statsNode.setProcess(process);
        nodeList.add(statsNode);

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

        // load the tLogCatcher to get the schema.
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
}
