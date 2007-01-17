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
import org.talend.designer.core.performance.RowStruct;
import org.talend.designer.mapper.MapperMain;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.language.generation.GenerationManagerFactory;
import org.talend.designer.mapper.language.generation.JavaGenerationManager;
import org.talend.designer.mapper.model.metadata.MapperDataTestGenerator;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: TMapperStartPerljet.java 1383 2007-01-10 14:40:52Z amaumont $
 * 
 */
public class TMapperStartJavajet {

    public static void main(String[] args) {
        AbstractExternalNode argument = null;

        AbstractExternalNode node = (AbstractExternalNode) argument;

        // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ExternalMapperData data;
        if (node != null) {
            data = (ExternalMapperData) node.getExternalData();
        } else {
            MapperMain.setStandAloneMode(true);
            MapperDataTestGenerator testGenerator = new MapperDataTestGenerator(LanguageProvider.getCurrentLanguage(), false);
            data = (ExternalMapperData) testGenerator.getExternalData();
        }

        List<ExternalMapperTable> varsTables = data.getVarsTables();

        String cr = "\n";

        int indent = 1;

        ILanguage currentLanguage = LanguageProvider.getJavaLanguage();

        JavaGenerationManager gm = (JavaGenerationManager) GenerationManagerFactory.getInstance().getGenerationManager(currentLanguage);

        StringBuilder sb = new StringBuilder();


        sb.append(cr + className + " " + instanceVarName + " = new " + className + "();");
        Struct rowStructKey = new RowStruct();

        /////////////////////////////////////////////////////////////////////////
        gm.setVarsTables(varsTables);
        // Classes of var tables
        List<ExternalMapperTable> varTablesList = new ArrayList<ExternalMapperTable>(varsTables);
        for (ExternalMapperTable table : varTablesList) {
            List<ExternalMapperTableEntry> tableEntries = table.getMetadataTableEntries();
            if (tableEntries == null) {
                continue;
            }
            String tableName = table.getName();

            String instanceVarName = tableName + "__" + node.getUniqueName();
            String className = instanceVarName + "__Struct";

            sb.append(cr + "class " + className + " {");
            int lstSize = tableEntries.size();
            for (int i = 0; i < lstSize; i++) {
                ExternalMapperTableEntry varTableEntry = (ExternalMapperTableEntry) tableEntries.get(i);
                String javaType = varTableEntry.getType();
                if (javaType == null) {
                    javaType = "String";
                }
                sb.append(cr + gm.indent(indent) + "public " + javaType + " " + varTableEntry.getName() + ";");
            }
            sb.append(cr + "}");
            sb.append(cr + className + " " + instanceVarName + " = new " + className + "();");
        }
        /////////////////////////////////////////////////////////////////////////


        
        
        // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println(sb);
    }


}
