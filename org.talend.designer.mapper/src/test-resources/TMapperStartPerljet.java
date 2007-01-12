// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.AbstractExternalNode;
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
        if (node != null) {
            data = (ExternalMapperData) node.getExternalData();
        } else {
            org.talend.designer.mapper.MapperMain.setStandAloneMode(true);
            MapperDataTestGenerator testGenerator = new MapperDataTestGenerator(LanguageProvider.getCurrentLanguage(), false);
            data = (ExternalMapperData) testGenerator.getExternalData();
        }

        List<ExternalMapperTable> inputTables = data.getInputTables();
        List<ExternalMapperTable> varsTables = data.getVarsTables();
        List<ExternalMapperTable> outputTables = data.getOutputTables();

        String cr = "\n";

        int indent = 1;

        ILanguage currentLanguage = LanguageProvider.getJavaLanguage();

        PerlGenerationManager gm = (PerlGenerationManager) GenerationManagerFactory.getInstance().getGenerationManager(currentLanguage);

        StringBuilder sb = new StringBuilder();

        gm.setInputTables(inputTables);
        gm.setVarsTables(varsTables);

        List<ExternalMapperTable> allTablesList = new ArrayList<ExternalMapperTable>(inputTables);
        allTablesList.addAll(varsTables);
        allTablesList.addAll(outputTables);

        // constants
        for (ExternalMapperTable table : allTablesList) {
            List<ExternalMapperTableEntry> tableEntries = table.getMetadataTableEntries();
            if (tableEntries == null) {
                continue;
            }
            String tableName = table.getName();
            sb.append(cr);
            int lstSize = tableEntries.size();
            for (int i = 0; i < lstSize; i++) {
                ExternalMapperTableEntry tableEntry = (ExternalMapperTableEntry) tableEntries.get(i);
                sb.append(cr + gm.indent(indent) + "use constant " + tableName + "__" + tableEntry.getName() + " => " + i + ";");
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
