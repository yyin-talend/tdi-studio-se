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
package org.talend.designer.runprocess.shadow;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import com.csvreader.CsvReader;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class FileInputCSVNode extends FileInputNode {

    /**
     * Constructs a new FileInputCSVNode.
     */
    public FileInputCSVNode(String filename, String rowSep, String fieldSep, int limitRows, int headerRows,
            int footerRows, String escapeChar, String textEnclosure, boolean removeEmptyRow, String encoding) {
        super("tFileInputDelimited"); //$NON-NLS-1$

        CsvReader cr = null;
        try {
            cr = new CsvReader(new BufferedReader(new InputStreamReader(new FileInputStream(trimParameter(filename)),
                    trimParameter(encoding))), trimParameter(fieldSep).charAt(0));
            if (!rowSep.equals("\"\\n\"") && !rowSep.equals("\"\\r\"")) {
                cr.setRecordDelimiter(trimParameter(rowSep).charAt(0));
            }
            cr.setSkipEmptyRecords(true);
            String en = trimParameter(textEnclosure);
            if (en.length() > 0) {
                cr.setTextQualifier(en.charAt(0));
            } else {
                cr.setUseTextQualifier(false);
            }
            if (escapeChar.equals("\"\\\\\"") || escapeChar.equals("\"\"")) {
                cr.setEscapeMode(CsvReader.ESCAPE_MODE_BACKSLASH);
            } else {
                cr.setEscapeMode(CsvReader.ESCAPE_MODE_DOUBLED);
            }
            int columnCount = 0;
            for (int i = 0; i < limitRows && cr.readRecord(); i++) {
                int temp = cr.getColumnCount();
                if (temp > columnCount) {
                    columnCount = temp;
                }
            }
            if (columnCount > 0) {
                this.setColumnNumber(columnCount);
            }
        } catch (UnsupportedEncodingException e) {
            // e.printStackTrace();
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            // e.printStackTrace();
        } finally {
            if (cr != null) {
                cr.close();
            }
        }

        String[] paramNames = new String[] { "FILENAME", "ROWSEPARATOR", "FIELDSEPARATOR", "LIMIT", "HEADER", "FOOTER", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                "ESCAPE_CHAR", "TEXT_ENCLOSURE", "REMOVE_EMPTY_ROW", "ENCODING", "CSV_OPTION" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String[] paramValues = new String[] { filename, rowSep, fieldSep, Integer.toString(limitRows),
                Integer.toString(headerRows), Integer.toString(footerRows), escapeChar, textEnclosure,
                Boolean.toString(removeEmptyRow), encoding, "true" };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }
    }

}
