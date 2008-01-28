// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.shadow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.talend.commons.exception.RuntimeExceptionHandler;
import org.talend.commons.utils.StringUtils;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.runprocess.shadow.ShadowProcess.EShadowProcessType;
import org.talend.fileprocess.delimited.DelimitedDataReader;
import org.talend.fileprocess.delimited.DelimitedDataReaderFactory;
import org.talend.librariesmanager.prefs.PreferencesUtilities;

import com.csvreader.CsvReader;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class FileInputDelimitedNode extends FileInputNode {

    /**
     * Constructs a new FileInputNode.
     */
    public FileInputDelimitedNode(String filename, String rowSep, String fieldSep, int limitRows, int headerRows, int footerRows,
            String escapeChar, String textEnclosure, boolean removeEmptyRow, String encoding, EShadowProcessType fileType) {
        super("tFileInputDelimited"); //$NON-NLS-1$

        boolean csvoption = false;
        String languageName = LanguageManager.getCurrentLanguage().getName();
        switch (fileType) {

        case FILE_DELIMITED:
            csvoption = false;
            DelimitedDataReader dr = null;
            try {

                dr = DelimitedDataReaderFactory.createDelimitedDataReader(trimParameter(filename), trimParameter(encoding),
                        trimParameter(StringUtils.loadConvert(fieldSep, languageName)), trimParameter(StringUtils.loadConvert(
                                rowSep, languageName)), true);
                dr.skipHeaders(0);
                if (languageName.equals("perl")) {
                    int max = getColumnCount(filename, rowSep, fieldSep, limitRows, escapeChar, textEnclosure);
                    this.setColumnNumber(max);

                } else {
                    int max = dr.getMaxColumnCount(limitRows);
                    if (max > 0) {
                        this.setColumnNumber(max);
                    }
                }

            } catch (IOException e) {
                // e.printStackTrace();
            } finally {
                if (dr != null) {
                    dr.close();
                }
            }
            break;

        case FILE_CSV:
            csvoption = true;
            CsvReader cr = null;
            try {
                cr = new CsvReader(new BufferedReader(new InputStreamReader(new FileInputStream(trimParameter(filename)),
                        trimParameter(encoding))), trimParameter(StringUtils.loadConvert(fieldSep, languageName)).charAt(0));
                if (!rowSep.equals("\"\\n\"") && !rowSep.equals("\"\\r\"")) {
                    cr.setRecordDelimiter(trimParameter(StringUtils.loadConvert(rowSep, languageName)).charAt(0));
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
            break;

        default:
            break;
        }

        String[] paramNames = new String[] { "FILENAME", "ROWSEPARATOR", "FIELDSEPARATOR", "LIMIT", "HEADER", "FOOTER", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                "ESCAPE_CHAR", "TEXT_ENCLOSURE", "REMOVE_EMPTY_ROW", "ENCODING", "CSV_OPTION" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String[] paramValues = new String[] { filename, rowSep, fieldSep, Integer.toString(limitRows),
                Integer.toString(headerRows), Integer.toString(footerRows), escapeChar, textEnclosure,
                Boolean.toString(removeEmptyRow), encoding, Boolean.toString(csvoption) };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }
    }

    private int getColumnCount(String filename, String rowSep, String fieldSep, int limitRows, String escapeChar,
            String textEnclosure) {

        File config = new File(CorePlugin.getDefault().getPreferenceStore().getString(ITalendCorePrefConstants.FILE_PATH_TEMP)
                + "/conf.pl");
        if (config.exists()) {
            config.delete();
        }
        String path = CorePlugin.getDefault().getPreferenceStore().getString(ITalendCorePrefConstants.PERL_INTERPRETER);
        String modulepath = PreferencesUtilities.getLibrariesPath(ECodeLanguage.PERL);
        FileWriter filewriter;
        Runtime runTime = Runtime.getRuntime();
        Process process = null;
        String str = "0";
        File resultFile = new File(CorePlugin.getDefault().getPreferenceStore()
                .getString(ITalendCorePrefConstants.FILE_PATH_TEMP)
                + "/result.txt");

        if (resultFile.exists()) {
            resultFile.delete();
        }

        try {
            filewriter = new FileWriter(config, true);
            filewriter.write("$conf{filename} = " + filename + ";");
            filewriter.write("$conf{row_separator} = " + rowSep + ";");
            filewriter.write("$conf{field_separator} = " + fieldSep + ";");
            filewriter.write("$conf{escape_char} = " + escapeChar + ";");
            filewriter.write("$conf{text_enclosure} = " + textEnclosure + ";");
            filewriter.write("$conf{limit} = " + limitRows + ";");
            filewriter.write("$conf{result_file} =\'" + resultFile.toString() + "\';");
            filewriter.write("$conf{type} = \'delimited\';");
            filewriter.close();
            if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0) {
                path = path.substring(0, path.lastIndexOf("\\"));
                modulepath = modulepath.substring(1, modulepath.length());
                process = runTime.exec("cmd /c start /D" + path + "\\" + " perl " + modulepath
                        + "/column_counter_delimited.pl --conf=" + config + "");
            } else {
                // String terminal = System.getenv("TERM");
                String command = "perl" + modulepath + "column_counter_delimited.pl --conf=" + config + "";
                // process = Runtime.getRuntime().exec(new String[] { terminal, "-e", command + "; $SHELL" });
                process = Runtime.getRuntime().exec(command);
            }

            int counter = 0;
            while (!resultFile.exists() && counter <= 6) {
                try {
                    Thread.sleep(900);
                    counter++;
                } catch (InterruptedException e) {
                    RuntimeExceptionHandler.process(e);
                }
            }

            FileReader filereader = new FileReader(resultFile);
            char[] chars = new char[1];
            filereader.read(chars);
            str = new String(chars);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Integer.parseInt(str);
    }
}
