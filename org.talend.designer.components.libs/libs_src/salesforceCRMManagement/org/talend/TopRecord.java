// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.message.MessageElement;

import com.sforce16.soap.partner.QueryResult;
import com.sforce16.soap.partner.sobject.SObject;

public class TopRecord {

    // hold the rootType for coverting the column "Name" to "Account_Name" when
    // try to get the result in Map
    private String rootType = null;

    // Each TopQueryResult has itself columnName List, because sometimes they
    // are
    // differen't, it depend on the return XML, for the Node "xsi:nil=true",
    // there will
    // miss some columnName
    List columnNameList = new ArrayList();

    Map valueMap = new HashMap();

    public TopRecord(String rootType) {
        this.rootType = rootType;
    }

    private void processQueryResult(QueryResult qr, String prefixName) throws Exception {

        for (int i = 0; i < qr.getRecords().length; i++) {

            SObject record = qr.getRecords()[i];

            processSObject(record, prefixName + TopConfig.COLUMNNAME_DELIMTER + record.getType());

        }
    }

    public void processSObject(SObject record, String prefixName) throws Exception {
        MessageElement[] fields = record.get_any();

        for (int j = 0; j < fields.length; j++) {
            // check getValue() and getObjectValue()

            if (fields[j].getValue() == null) {

                Object objectValue = fields[j].getObjectValue();// Here check
                // Here check the ObjectValue again, maybe this is a NODE, not a
                // COMMON FIELD

                if (objectValue != null) {
                    // if (objectValue != null),
                    // it is xsi:type="sf:sObject" OR xsi:type="QueryResult"
                    // if ((value=null)&&(objectValue=null)), the FIELD/NODE is
                    // xsi:nil="true"

                    if (objectValue instanceof SObject) {
                        SObject sobject = (SObject) objectValue;
                        processSObject(sobject, prefixName + TopConfig.COLUMNNAME_DELIMTER + sobject.getType());

                    } else if (objectValue instanceof QueryResult) {
                        QueryResult queryResult = (QueryResult) objectValue;
                        processQueryResult(queryResult, prefixName);
                    } else {
                        throw new Exception("Unexcepted case happend...");
                    }
                }

            } else {
                String newPrefixName = prefixName + TopConfig.COLUMNNAME_DELIMTER + fields[j].getName();

                // add the columnName to List one by one(order is important)
                if (!columnNameList.contains(newPrefixName)) {
                    columnNameList.add(newPrefixName);
                }

                Object value = valueMap.get(newPrefixName);

                if (value != null) {

                    valueMap.put(newPrefixName, value + TopConfig.VALUE_DELIMITER + fields[j].getValue());
                } else {
                    valueMap.put(newPrefixName, fields[j].getValue());
                }

            }

        }

    }

    public Object getValue(String key) {
        Object value = null;

        // try directly: Name
        value = valueMap.get(key);

        if (value == null) {

            if (rootType != null) {
                // try again: Account_Name
                String fullKey = rootType + TopConfig.COLUMNNAME_DELIMTER + key;

                value = valueMap.get(fullKey);
            }
        }

        return value;
    }

    public List getColumnNameList() {
        return columnNameList;
    }

    public Map getValueMap() {
        return valueMap;
    }

}
