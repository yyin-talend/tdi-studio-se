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
package org.talend.designer.mapper.language.perl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.IConnection;
import org.talend.designer.mapper.MapperMain;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.language.generation.GenerationManagerFactory;
import org.talend.designer.mapper.language.generation.PerlGenerationManager;
import org.talend.designer.mapper.model.metadata.MapperDataTestGenerator;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class TMapperStartPerljet {

    public static void main(String[] args) {
        AbstractExternalNode argument = null;

        AbstractExternalNode node = (AbstractExternalNode) argument;

        // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ExternalMapperData data;
        List<IConnection> inputConnections = null;
        if (node != null) {
            data = (ExternalMapperData) node.getExternalData();
            inputConnections = (List<IConnection>) node.getIncomingConnections();
        } else {
            MapperMain.setStandAloneMode(true);
            MapperDataTestGenerator testGenerator = new MapperDataTestGenerator(LanguageProvider.getCurrentLanguage(),
                    false);
            inputConnections = testGenerator.getInputConnectionsList();
            data = (ExternalMapperData) testGenerator.getExternalData();
        }

        List<ExternalMapperTable> inputTables = data.getInputTables();
        List<ExternalMapperTable> varsTables = data.getVarsTables();
        List<ExternalMapperTable> outputTables = data.getOutputTables();

        String cr = "\n";

        int indent = 1;

        ILanguage currentLanguage = LanguageProvider.getPerlLanguage();

        PerlGenerationManager gm = (PerlGenerationManager) GenerationManagerFactory.getInstance().getGenerationManager(
                currentLanguage);

        StringBuilder sb = new StringBuilder();

        gm.setInputTables(inputTables);
        gm.setVarsTables(varsTables);

        // constants

        String useConstantPerlCode = "use constant ";
        
        List<ExternalMapperTable> inputsTablesList = new ArrayList<ExternalMapperTable>(inputTables);

        HashMap<String, IConnection> hNameToConnection = new HashMap<String, IConnection>();
        for (IConnection connection : inputConnections) {
            hNameToConnection.put(connection.getName(), connection);
        }
        HashMap<String, ExternalMapperTableEntry> hExternalInputTableEntries = new HashMap<String, ExternalMapperTableEntry>();
        for (ExternalMapperTable externalTable : inputsTablesList) {

            String tableName = externalTable.getName();
            sb.append(cr);

            IConnection connection = hNameToConnection.get(tableName);

            if (connection != null) {

                hExternalInputTableEntries.clear();
                List<ExternalMapperTableEntry> metadataTableEntries = externalTable.getMetadataTableEntries();
                if (metadataTableEntries == null) {
                    continue;
                }
                for (ExternalMapperTableEntry externalTableEntry : metadataTableEntries) {
                    hExternalInputTableEntries.put(externalTableEntry.getName(), externalTableEntry);
                }

                IMetadataTable metadataTable = connection.getMetadataTable();

                List<IMetadataColumn> listColumns = metadataTable.getListColumns();

                int lstSize = listColumns.size();
                for (int i = 0; i < lstSize; i++) {
                    IMetadataColumn column = (IMetadataColumn) listColumns.get(i);
                    String columnName = column.getLabel();
                    ExternalMapperTableEntry externalInputTableEntry = hExternalInputTableEntries.get(columnName);
                    if (externalInputTableEntry != null) {
                        sb.append(cr + gm.indent(indent) + useConstantPerlCode + tableName + "__" + columnName + " => " + i
                                + ";");
                    }
                }
            }

        }

        List<ExternalMapperTable> varsAndOutputsTablesList = new ArrayList<ExternalMapperTable>(varsTables);
        varsAndOutputsTablesList.addAll(outputTables);
        for (ExternalMapperTable table : varsAndOutputsTablesList) {
            List<ExternalMapperTableEntry> tableEntries = table.getMetadataTableEntries();
            if (tableEntries == null) {
                continue;
            }
            String tableName = table.getName();
            sb.append(cr);
            int lstSize = tableEntries.size();
            for (int i = 0; i < lstSize; i++) {
                ExternalMapperTableEntry tableEntry = (ExternalMapperTableEntry) tableEntries.get(i);
                sb.append(cr + gm.indent(indent) + useConstantPerlCode + tableName + "__" + tableEntry.getName() + " => "
                        + i + ";");
            }
        }

        // column names
        for (ExternalMapperTable table : outputTables) {
            List<ExternalMapperTableEntry> tableEntries = table.getMetadataTableEntries();
            String tableName = table.getName();
            sb.append(cr + cr + gm.indent(indent) + "my @colnames_" + tableName + " = (");
            int lstSize = tableEntries.size();
            indent++;
            for (int i = 0; i < lstSize; i++) {
                ExternalMapperTableEntry tableEntry = (ExternalMapperTableEntry) tableEntries.get(i);
                sb.append(cr + gm.indent(indent) + "'" + tableEntry.getName() + "',");
            }
            indent--;
            sb.append(cr + gm.indent(indent) + ");");
        }

        // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println(sb);
    }

}
