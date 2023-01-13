package com.talend.excel.xssf.event;

import java.util.Calendar;
import java.util.Date;

public class ColumnUtil {

    /**
     * get index of column,like "AB12" return 27
     */
    public static int calculateIndexOfColumn(String cellReference) {
        int columnIndex = -1;
        // get First Digit index ,like cellReferecnce is "AA13" ,firstDigitIndex return 2
        int firstDigitIndex = -1;
        for (int c = 0; c < cellReference.length(); ++c) {
            if (Character.isDigit(cellReference.charAt(c))) {
                firstDigitIndex = c;
                break;
            }
        }
        // get String part for cellReference,like "AB2" ,return "AB"
        String cellStringPart = cellReference.substring(0, firstDigitIndex);
        for (int i = 0; i < cellStringPart.length(); ++i) {
            int c = cellStringPart.charAt(i);
            columnIndex = (columnIndex + 1) * 26 + c - 'A';
        }
        return columnIndex;
    }
    
    
    public static Date convert_Date1904(java.util.Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1462);
        return c.getTime();
}

}
