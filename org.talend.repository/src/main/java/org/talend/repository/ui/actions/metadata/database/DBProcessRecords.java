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
package org.talend.repository.ui.actions.metadata.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ggu class global comment. Detailled comment <br/>
 * 
 */
public class DBProcessRecords {

    /**
     * 
     */
    public enum RecordsType {
        CONNECTION,
        TABLE,
        FIELD
    }

    /**
     * 
     */
    public enum ProcessType {
        REJECT,
        IMPORT
    }

    private Map<ProcessType, Map<RecordsType, Set<String>>> recordsMap = new HashMap<ProcessType, Map<RecordsType, Set<String>>>();

    public DBProcessRecords() {
        super();
        initNum();
    }

    private void initNum() {

        initProcess(ProcessType.IMPORT, recordsMap);

        initProcess(ProcessType.REJECT, recordsMap);
    }

    private void initProcess(ProcessType pType, Map<ProcessType, Map<RecordsType, Set<String>>> process) {
        HashMap<RecordsType, Set<String>> records = new HashMap<RecordsType, Set<String>>();
        initRecords(RecordsType.CONNECTION, records);
        initRecords(RecordsType.TABLE, records);
        initRecords(RecordsType.FIELD, records);
        process.put(pType, records);

    }

    private void initRecords(RecordsType rType, Map<RecordsType, Set<String>> records) {
        records.put(rType, new HashSet<String>());
    }

    public int getRecord(ProcessType pType, RecordsType rType) {
        if (!isValid(pType) || !isValid(rType)) {
            return -1;
        }
        switch (pType) {
        case REJECT:
        case IMPORT:
            Map<RecordsType, Set<String>> processMap = recordsMap.get(pType);
            if (processMap == null) {
                initProcess(pType, recordsMap);
                return 0;
            } else {
                Set<String> recordSet = processMap.get(rType);
                if (recordSet == null) {
                    initRecords(rType, processMap);
                    return 0;
                } else {
                    return recordSet.size();
                }
            }
        default:
            break;
        }
        return -1;
    }

    public void addRecord(ProcessType pType, RecordsType rType, String name) {
        if (!isValid(name) || !isValid(pType) || !isValid(rType)) {
            return;
        }
        switch (pType) {
        case REJECT:
        case IMPORT:
            Map<RecordsType, Set<String>> processMap = recordsMap.get(pType);
            if (processMap == null) {
                initProcess(pType, recordsMap);
                return;
            } else {
                Set<String> recordSet = processMap.get(rType);
                if (recordSet == null) {
                    initRecords(rType, processMap);
                    return;
                } else {
                    recordSet.add(name);
                }
            }
        default:
            break;
        }

    }

    private boolean isValid(Object name) {
        if (name == null) {
            return false;
        }
        if (name instanceof String) {
            if (name.toString().trim() == "") {
                return false;
            }
        }
        return true;
    }
}
