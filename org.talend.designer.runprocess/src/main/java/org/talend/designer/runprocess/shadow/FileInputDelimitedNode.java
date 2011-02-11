// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.StringUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.shadow.ShadowProcess.EShadowProcessType;
import org.talend.fileprocess.FileInputDelimited;
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
            String escapeChar, String textEnclosure, boolean removeEmptyRow, boolean spitRecord, String encoding,
            EShadowProcessType fileType) {
        super("tFileInputDelimited"); //$NON-NLS-1$

        boolean csvoption = false;
        String languageName = LanguageManager.getCurrentLanguage().getName();
        switch (fileType) {

        case FILE_DELIMITED:
            csvoption = false;

            if (languageName.equals("perl")) { //$NON-NLS-1$
                int max = getColumnCount(filename, rowSep, fieldSep, limitRows, headerRows, escapeChar, textEnclosure,
                        EShadowProcessType.FILE_DELIMITED);
                this.setColumnNumber(max);

            } else {

                int max = 0;
                try {

                    max = FileInputDelimited.getMaxColumnCount(trimParameter(filename), trimParameter(encoding),
                            trimParameter(StringUtils.loadConvert(fieldSep, languageName)), trimParameter(StringUtils
                                    .loadConvert(rowSep, languageName)), true, spitRecord, headerRows, limitRows);

                } catch (IOException e) {
                    // e.printStackTrace();
                    ExceptionHandler.process(e);
                }
                if (max > 0) {
                    this.setColumnNumber(max);
                }

            }
            break;

        case FILE_CSV:
            csvoption = true;
            if (languageName.equals("perl")) { //$NON-NLS-1$
                int max = getColumnCount(filename, rowSep, fieldSep, limitRows, headerRows, escapeChar, textEnclosure,
                        EShadowProcessType.FILE_CSV);
                this.setColumnNumber(max);
            } else {
                CsvReader cr = null;
                try {
                    cr = new CsvReader(new BufferedReader(new InputStreamReader(new FileInputStream(trimParameter(filename)),
                            trimParameter(encoding))), trimParameter(StringUtils.loadConvert(fieldSep, languageName)).charAt(0));
                    if (!rowSep.equals("\"\\n\"") && !rowSep.equals("\"\\r\"")) { //$NON-NLS-1$ //$NON-NLS-2$
                        cr.setRecordDelimiter(trimParameter(StringUtils.loadConvert(rowSep, languageName)).charAt(0));
                    }
                    cr.setSkipEmptyRecords(true);
                    String en = trimParameter(textEnclosure);
                    if (en.length() > 0) {
                        cr.setTextQualifier(en.charAt(0));
                    } else {
                        cr.setUseTextQualifier(false);
                    }
                    if (escapeChar.equals("\"\\\\\"") || escapeChar.equals("\"\"")) { //$NON-NLS-1$ //$NON-NLS-2$
                        cr.setEscapeMode(CsvReader.ESCAPE_MODE_BACKSLASH);
                    } else {
                        cr.setEscapeMode(CsvReader.ESCAPE_MODE_DOUBLED);
                    }

                    for (int i = 0; i < headerRows && cr.readRecord(); i++) {
                        // do nothing, just ignore the header part
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
                    ExceptionHandler.process(e);
                } catch (FileNotFoundException e) {
                    // e.printStackTrace();
                    ExceptionHandler.process(e);
                } catch (IOException e) {
                    // e.printStackTrace();
                    ExceptionHandler.process(e);
                } finally {
                    if (cr != null) {
                        cr.close();
                    }
                }
            }
            break;

        default:
            break;
        }

        String[] paramNames = new String[] { "FILENAME", "ROWSEPARATOR", "FIELDSEPARATOR", "LIMIT", "HEADER", "FOOTER", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                "ESCAPE_CHAR", "TEXT_ENCLOSURE", "REMOVE_EMPTY_ROW", "ENCODING", "CSV_OPTION", "SPLITRECORD" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        String[] paramValues = new String[] { filename, rowSep, fieldSep, Integer.toString(limitRows),
                Integer.toString(headerRows), Integer.toString(footerRows), escapeChar, textEnclosure,
                Boolean.toString(removeEmptyRow), encoding, Boolean.toString(csvoption), Boolean.toString(spitRecord) };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }
    }

    private int getColumnCount(String filename, String rowSep, String fieldSep, int limitRows, int headerRows, String escapeChar,
            String textEnclosure, EShadowProcessType fileType) {

        File config = new File(CorePlugin.getDefault().getPreferenceStore().getString(ITalendCorePrefConstants.FILE_PATH_TEMP)
                + "/conf.pl"); //$NON-NLS-1$
        if (config.exists()) {
            config.delete();
        }
        String modulepath = PreferencesUtilities.getLibrariesPath(ECodeLanguage.PERL);
        FileWriter filewriter;
        String str = "0"; //$NON-NLS-1$
        File resultFile = new File(CorePlugin.getDefault().getPreferenceStore()
                .getString(ITalendCorePrefConstants.FILE_PATH_TEMP)
                + "/result.txt"); //$NON-NLS-1$

        if (resultFile.exists()) {
            resultFile.delete();
        }

        try {
            filewriter = new FileWriter(config, true);
            switch (fileType) {
            case FILE_DELIMITED:
                filewriter.write("$conf{filename} = " + filename + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{row_separator} = " + rowSep + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{field_separator} = " + fieldSep + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{header} = " + headerRows + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{limit} = " + limitRows + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{result_file} =\'" + resultFile.toString() + "\';"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{type} = \'delimited\';"); //$NON-NLS-1$
                break;
            case FILE_CSV:
                filewriter.write("$conf{filename} = " + filename + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{row_separator} = " + rowSep + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{field_separator} = " + fieldSep + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{escape_char} = " + escapeChar + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{text_enclosure} = " + textEnclosure + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{header} = " + headerRows + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{limit} = " + limitRows + ";"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{result_file} =\'" + resultFile.toString() + "\';"); //$NON-NLS-1$ //$NON-NLS-2$
                filewriter.write("$conf{type} = \'CSV\';"); //$NON-NLS-1$
                break;
            default:
                break;
            }

            filewriter.close();
            modulepath = modulepath + "/column_counter_delimited.pl"; //$NON-NLS-1$
            StringBuffer out = new StringBuffer();
            StringBuffer err = new StringBuffer();

            IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            service.perlExec(out, err, new Path(modulepath), null, Level.DEBUG, "", null, -1, -1, new String[] { "--conf=" //$NON-NLS-1$ //$NON-NLS-2$
                    + config });

            FileReader filereader = new FileReader(resultFile);
            BufferedReader reader = new BufferedReader(filereader);
            str = reader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            ExceptionHandler.process(e);
        } catch (ProcessorException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
        return Integer.parseInt(str);
    }
}
