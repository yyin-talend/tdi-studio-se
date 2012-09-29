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
package com.talend.excel.xssf.event;

import java.util.ArrayList;
import java.util.List;

/**
 * created by wwang on 2012-9-27 Detailled comment
 * 
 */
public class DefaultTalendSheetContentsHandler implements TalendXSSFSheetXMLHandler.TalendSheetContentsHandler {

    private DataBufferCache cache = null;

    private List<String> row = null;

    private boolean stop = false;

    public DefaultTalendSheetContentsHandler(DataBufferCache cache) {
        this.cache = cache;
    }

    public void startRow(int rowNum) {
        row = new ArrayList<String>();
    }

    public void endRow() {
        cache.writeData(row);
        row = null;

        if (stop) {
            throw new EnoughDataException("Get enough data,now stop the xml parse action");
        }
    }

    public void cell(String cellReference, String formattedValue) {
        row.add(formattedValue);
    }

    public void headerFooter(String text, boolean isHeader, String tagName) {
    }

    public void endSheet() {

    }

    public void stop() {
        this.stop = true;
    }

}
