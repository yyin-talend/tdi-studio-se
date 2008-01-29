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

import org.apache.log4j.Level;
import org.eclipse.core.runtime.Path;
import org.talend.commons.utils.StringUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.designer.runprocess.ProcessorException;
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
                    int max = getColumnCount(filename, rowSep, fieldSep, limitRows, escapeChar, textEnclosure,
                            EShadowProcessType.FILE_DELIMITED);
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
                if (languageName.equals("perl")) {
                    int max = getColumnCount(filename, rowSep, fieldSep, limitRows, escapeChar, textEnclosure,
                            EShadowProcessType.FILE_CSV);
                    this.setColumnNumber(max);

                } else {
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
            String textEnclosure, EShadowProcessType fileType) {

        File config = new File(CorePlugin.getDefault().getPreferenceStore().getString(ITalendCorePrefConstants.FILE_PATH_TEMP)
                + "/conf.pl");
        if (config.exists()) {
            config.delete();
        }
        String modulepath = PreferencesUtilities.getLibrariesPath(ECodeLanguage.PERL);
        FileWriter filewriter;
        String str = "0";
        File resultFile = new File(CorePlugin.getDefault().getPreferenceStore()
                .getString(ITalendCorePrefConstants.FILE_PATH_TEMP)
                + "/result.txt");

        if (resultFile.exists()) {
            resultFile.delete();
        }

        try {
            filewriter = new FileWriter(config, true);
            switch (fileType) {
            case FILE_DELIMITED:
                filewriter.write("$conf{filename} = " + filename + ";");
                filewriter.write("$conf{row_separator} = " + rowSep + ";");
                filewriter.write("$conf{field_separator} = " + fieldSep + ";");
                filewriter.write("$conf{limit} = " + limitRows + ";");
                filewriter.write("$conf{result_file} =\'" + resultFile.toString() + "\';");
                filewriter.write("$conf{type} = \'delimited\';");
                break;
            case FILE_CSV:
                filewriter.write("$conf{filename} = " + filename + ";");
                filewriter.write("$conf{row_separator} = " + rowSep + ";");
                filewriter.write("$conf{field_separator} = " + fieldSep + ";");
                filewriter.write("$conf{escape_char} = " + escapeChar + ";");
                filewriter.write("$conf{text_enclosure} = " + textEnclosure + ";");
                filewriter.write("$conf{limit} = " + limitRows + ";");
                filewriter.write("$conf{result_file} =\'" + resultFile.toString() + "\';");
                filewriter.write("$conf{type} = \'CSV\';");
                break;
            default:
                break;
            }

            filewriter.close();
            modulepath = modulepath + "/column_counter_delimited.pl";
            StringBuffer out = new StringBuffer();
            StringBuffer err = new StringBuffer();

            IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            service.perlExec(out, err, new Path(modulepath), null, Level.DEBUG, "", null, -1, -1, new String[] { "--conf="
                    + config });

            FileReader filereader = new FileReader(resultFile);
            char[] chars = new char[1];
            filereader.read(chars);
            str = new String(chars);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProcessorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Integer.parseInt(str);
    }
}
