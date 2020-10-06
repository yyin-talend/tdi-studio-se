// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
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

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * created by wwang on 2012-9-27 Detailled comment
 *
 */
public class TalendXSSFSheetXMLHandler extends XSSFSheetXMLHandler {

    private DefaultTalendSheetContentsHandler output = null;
    private final StylesTable styles;

    public TalendXSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings,
            TalendSheetContentsHandler sheetContentsHandler, DataFormatter dataFormatter, boolean formulasNotResults) {
        super(styles, strings, sheetContentsHandler, dataFormatter, formulasNotResults);
        this.output = (DefaultTalendSheetContentsHandler) sheetContentsHandler;
        this.styles = styles;
    }

    public interface TalendSheetContentsHandler extends SheetContentsHandler {

        public void endSheet();

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        // Update Date Format value
        if ("c".equals(localName) && "n".equals(attributes.getValue("t"))) {
            String cellStyleStr = attributes.getValue("s");
            // Number, but almost certainly with a special style or format
            XSSFCellStyle style = styles.getStyleAt(Integer.parseInt(cellStyleStr));

            if (style != null) {
                if (DateUtil.isADateFormat(style.getDataFormat(), style.getDataFormatString())) {
                    output.setCellDateType(true);
                    output.setExcelDateFormat(style.getDataFormatString());
                }
            }
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endDocument() throws SAXException {
        this.output.endSheet();
    }

}