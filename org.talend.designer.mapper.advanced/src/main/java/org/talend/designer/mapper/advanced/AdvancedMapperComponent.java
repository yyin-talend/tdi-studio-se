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
package org.talend.designer.mapper.advanced;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.core.model.process.BlockCode;
import org.talend.core.model.process.IHashConfiguration;
import org.talend.core.model.process.IHashableInputConnections;
import org.talend.core.model.process.IMatchingMode;
import org.talend.designer.components.commons.AdvancedLookup.MATCHING_MODE;
import org.talend.designer.mapper.MapperComponent;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: MapperComponent.java 3351 2007-05-04 12:14:00Z plegall $
 * 
 */
public class AdvancedMapperComponent extends MapperComponent implements IHashableInputConnections {

    /**
     * DOC amaumont MapperComponent constructor comment.
     */
    public AdvancedMapperComponent() {
        super();
    }

    @Override
    public List<BlockCode> getBlocksCodeToClose() {
        // ArrayList<BlockCode> list = new ArrayList<BlockCode>();
        // for (int i = 0; i < 2; i++) {
        // list.add(new BlockCode("lookup " + (i + 1) + " tAdvancedMap"));
        // }
        // return list;

        return getGenerationManager().getBlocksCodeToClose();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IHashableInputConnections#getHashConfiguration(java.lang.String)
     */
    public IHashConfiguration getHashConfiguration(String connectionName) {

        HashConfigurationForMapper hashConfigurationForMapper = null;
        ExternalMapperData externalData = (ExternalMapperData) getExternalData();
        List<ExternalMapperTable> inputTables = externalData.getInputTables();
        Set<String> hashableColumns = new HashSet<String>();
        for (ExternalMapperTable inputTable : inputTables) {
            if (inputTable.getName().equals(connectionName)) {
                List<ExternalMapperTableEntry> metadataTableEntries = inputTable.getMetadataTableEntries();
                for (ExternalMapperTableEntry entry : metadataTableEntries) {
                    if (entry.getExpression() != null && !entry.getExpression().trim().equals("")) {
                        hashableColumns.add(entry.getName());
                    }
                }

                IMatchingMode matchingMode = MATCHING_MODE
                        .parse(inputTable.getMatchingMode());
                if (matchingMode == null) {
                    matchingMode = MATCHING_MODE.ALL_MATCHES;
                }
                hashConfigurationForMapper = new HashConfigurationForMapper(hashableColumns, matchingMode);
                break;
            }
        }

        return hashConfigurationForMapper;
    }

    /**
     * 
     * DOC amaumont AdvancedMapperComponent class global comment. Detailled comment <br/>
     * 
     */
    public class HashConfigurationForMapper implements IHashConfiguration {

        private Set<String> hashableColumns;

        private IMatchingMode matchingMode;

        /**
         * DOC amaumont HashConfigurationForMapper constructor comment.
         * 
         * @param hashableColumns
         * @param matchingMode
         * @param uniqueMatch
         */
        public HashConfigurationForMapper(Set<String> hashableColumns, IMatchingMode matchingMode) {
            super();
            this.hashableColumns = hashableColumns;
            this.matchingMode = matchingMode;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IHashConfiguration#getHashableColumns(java.lang.String)
         */
        public Set<String> getHashableColumns() {
            return this.hashableColumns;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IHashConfiguration#getMatchingMode()
         */
        public IMatchingMode getMatchingMode() {
            return this.matchingMode;
        }

    }

}
