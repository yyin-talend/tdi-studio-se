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
package org.talend.repository.ui.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.FieldSeparator;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;

/**
 * ggu class global comment. Detailled comment
 */
public final class FileConnectionContextUtils {

    private static final ECodeLanguage LANGUAGE = LanguageManager.getCurrentLanguage();

    /**
     * 
     */
    enum EFileParamName {
        // Server,
        FilePath,
        RowSeparatorValue,
        FieldSeparatorValue,
        Encoding,
        //
        LimitValue,
        HeaderValue,
        FooterValue,
        // 
        EscapeChar,
        TextEnclosure,
        // excel
        ThousandSeparator,
        DecimalSeparator,
        LastColumn,
        FirstColumn,
    }

    static List<IContextParameter> getFileVariables(String prefixName, FileConnection conn) {
        if (conn == null || prefixName == null) {
            return Collections.emptyList();
        }
        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        // paramName = prefixName + EFileParamName.Server;
        // ConnectionContextHelper.createParameters(varList, paramName, conn.getServer());
        // conn.setServer(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EFileParamName.FilePath;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getFilePath(), JavaTypesManager.FILE);

        paramName = prefixName + EFileParamName.Encoding;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getEncoding());

        paramName = prefixName + EFileParamName.LimitValue;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getLimitValue(), JavaTypesManager.INTEGER);

        paramName = prefixName + EFileParamName.HeaderValue;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getHeaderValue(), JavaTypesManager.INTEGER);

        paramName = prefixName + EFileParamName.FooterValue;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getFooterValue(), JavaTypesManager.INTEGER);

        if (conn instanceof DelimitedFileConnection || conn instanceof PositionalFileConnection) {
            // paramName = prefixName + EFileParamName.EscapeChar;
            // ConnectionContextHelper.createParameters(varList, paramName, conn.getEscapeChar());
            //            
            // paramName = prefixName + EFileParamName.TextEnclosure;
            // ConnectionContextHelper.createParameters(varList, paramName, conn.getTextEnclosure());
        }

        if (conn instanceof DelimitedFileConnection || conn instanceof PositionalFileConnection
                || conn instanceof RegexpFileConnection) {
            paramName = prefixName + EFileParamName.RowSeparatorValue;
            ConnectionContextHelper.createParameters(varList, paramName, conn.getRowSeparatorValue());

            paramName = prefixName + EFileParamName.FieldSeparatorValue;
            ConnectionContextHelper.createParameters(varList, paramName, conn.getFieldSeparatorValue());

        }

        if (conn instanceof FileExcelConnection) {
            FileExcelConnection excelConn = (FileExcelConnection) conn;

            switch (LanguageManager.getCurrentLanguage()) {
            case JAVA:
                paramName = prefixName + EFileParamName.ThousandSeparator;
                ConnectionContextHelper.createParameters(varList, paramName, excelConn.getThousandSeparator());

                paramName = prefixName + EFileParamName.DecimalSeparator;
                ConnectionContextHelper.createParameters(varList, paramName, excelConn.getDecimalSeparator());
            }
            paramName = prefixName + EFileParamName.FirstColumn;
            ConnectionContextHelper.createParameters(varList, paramName, excelConn.getFirstColumn(), JavaTypesManager.INTEGER);

            paramName = prefixName + EFileParamName.LastColumn;
            ConnectionContextHelper.createParameters(varList, paramName, excelConn.getLastColumn(), JavaTypesManager.INTEGER);
        }

        return varList;
    }

    static void setPropertiesForContextMode(String prefixName, FileConnection conn) {
        if (conn == null || prefixName == null) {
            return;
        }
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        paramName = prefixName + EFileParamName.FilePath;
        conn.setFilePath(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EFileParamName.Encoding;
        conn.setEncoding(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EFileParamName.LimitValue;
        conn.setLimitValue(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EFileParamName.HeaderValue;
        conn.setHeaderValue(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EFileParamName.FooterValue;
        conn.setFooterValue(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        if (conn instanceof DelimitedFileConnection || conn instanceof PositionalFileConnection) {
            // paramName = prefixName + EFileParamName.EscapeChar;
            // conn.setEscapeChar(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
            //            
            // paramName = prefixName + EFileParamName.TextEnclosure;
            // conn.setTextEnclosure(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
        }

        if (conn instanceof DelimitedFileConnection || conn instanceof PositionalFileConnection
                || conn instanceof RegexpFileConnection) {
            paramName = prefixName + EFileParamName.RowSeparatorValue;
            conn.setRowSeparatorValue(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

            paramName = prefixName + EFileParamName.FieldSeparatorValue;
            conn.setFieldSeparatorValue(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

            // conn.setRowSeparatorType(RowSeparator.CUSTOM_STRING_LITERAL);

            if (conn instanceof DelimitedFileConnection) {
                ((DelimitedFileConnection) conn).setFieldSeparatorType(FieldSeparator.CUSTOM_UTF8_LITERAL);
            }
        }
        if (conn instanceof FileExcelConnection) {
            FileExcelConnection excelConn = (FileExcelConnection) conn;
            switch (LanguageManager.getCurrentLanguage()) {
            case JAVA:
                paramName = prefixName + EFileParamName.ThousandSeparator;
                excelConn.setThousandSeparator(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

                paramName = prefixName + EFileParamName.DecimalSeparator;
                excelConn.setDecimalSeparator(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
            }
            paramName = prefixName + EFileParamName.FirstColumn;
            excelConn.setFirstColumn(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

            paramName = prefixName + EFileParamName.LastColumn;
            excelConn.setLastColumn(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        }

    }

    /**
     * 
     * ggu Comment method "cloneOriginalValueConnection".
     * 
     * perhaps, if connection is in context mode, will open dialog to choose context sets.
     */
    public static FileConnection cloneOriginalValueConnection(FileConnection fileConn) {
        return cloneOriginalValueConnection(null, fileConn, false);
    }

    public static FileConnection cloneOriginalValueConnection(Shell shell, FileConnection fileConn, boolean defaultContext) {
        if (fileConn == null) {
            return null;
        }
        ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(shell, fileConn, null, defaultContext);
        return cloneOriginalValueConnection(fileConn, contextType);
    }

    /**
     * 
     * ggu Comment method "cloneOriginalValueConnection".
     * 
     * 
     */
    @SuppressWarnings("unchecked")
    public static FileConnection cloneOriginalValueConnection(FileConnection fileConn, ContextType contextType) {
        if (fileConn == null) {
            return null;
        }

        FileConnection cloneConn = null;
        if (fileConn instanceof DelimitedFileConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createDelimitedFileConnection();
        } else if (fileConn instanceof PositionalFileConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createPositionalFileConnection();
        } else if (fileConn instanceof RegexpFileConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createRegexpFileConnection();
        } else if (fileConn instanceof FileExcelConnection) {
            cloneConn = ConnectionFactory.eINSTANCE.createFileExcelConnection();

        }

        if (cloneConn != null) {
            String filePath = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getFilePath());
            String encoding = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getEncoding());
            String headValue = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getHeaderValue());
            String footerValue = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getFooterValue());
            String limitValue = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getLimitValue());

            filePath = TalendTextUtils.removeQuotes(filePath);
            cloneConn.setFilePath(filePath);
            cloneConn.setEncoding(encoding);
            cloneConn.setHeaderValue(headValue);
            cloneConn.setFooterValue(footerValue);
            cloneConn.setLimitValue(limitValue);

            //
            if (fileConn instanceof DelimitedFileConnection || fileConn instanceof PositionalFileConnection
                    || fileConn instanceof RegexpFileConnection) {
                String fieldSeparatorValue = ConnectionContextHelper.getOriginalValue(contextType, fileConn
                        .getFieldSeparatorValue());
                String rowSeparatorValue = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getRowSeparatorValue());

                cloneConn.setFieldSeparatorValue(fieldSeparatorValue);
                cloneConn.setRowSeparatorValue(rowSeparatorValue);

                if (fileConn instanceof DelimitedFileConnection) {
                    ((DelimitedFileConnection) cloneConn).setFieldSeparatorType(((DelimitedFileConnection) fileConn)
                            .getFieldSeparatorType());
                }
            }
            // excel
            if (fileConn instanceof FileExcelConnection) {
                FileExcelConnection excelConnection = (FileExcelConnection) fileConn;
                FileExcelConnection cloneExcelConnection = (FileExcelConnection) cloneConn;

                String thousandSeparator = ConnectionContextHelper.getOriginalValue(contextType, excelConnection
                        .getThousandSeparator());
                String decimalSeparator = ConnectionContextHelper.getOriginalValue(contextType, excelConnection
                        .getDecimalSeparator());
                String firstColumn = ConnectionContextHelper.getOriginalValue(contextType, excelConnection.getFirstColumn());
                String lastColumn = ConnectionContextHelper.getOriginalValue(contextType, excelConnection.getLastColumn());

                cloneExcelConnection.setThousandSeparator(thousandSeparator);
                cloneExcelConnection.setDecimalSeparator(decimalSeparator);
                cloneExcelConnection.setFirstColumn(firstColumn);
                cloneExcelConnection.setLastColumn(lastColumn);

                cloneExcelConnection.setSelectAllSheets(excelConnection.isSelectAllSheets());
                cloneExcelConnection.setSheetName(excelConnection.getSheetName());

                ArrayList sheetList = excelConnection.getSheetList();
                cloneExcelConnection.setSheetList((ArrayList) sheetList.clone());

                EList sheetColumns = excelConnection.getSheetColumns();
                if (sheetColumns != null && sheetColumns instanceof BasicEList) {
                    cloneExcelConnection.getSheetColumns().addAll((EList) ((BasicEList) sheetColumns).clone());
                }

                cloneExcelConnection.setAdvancedSpearator(excelConnection.isAdvancedSpearator());

                cloneConn.setFieldSeparatorValue(fileConn.getFieldSeparatorValue());
                cloneConn.setRowSeparatorType(fileConn.getRowSeparatorType());
                cloneConn.setRowSeparatorValue(fileConn.getRowSeparatorValue());
            }

            cloneConn.setRowSeparatorType(fileConn.getRowSeparatorType());

            cloneConn.setCsvOption(fileConn.isCsvOption());
            cloneConn.setEscapeChar(fileConn.getEscapeChar());
            cloneConn.setEscapeType(fileConn.getEscapeType());
            cloneConn.setFirstLineCaption(fileConn.isFirstLineCaption());
            cloneConn.setFormat(fileConn.getFormat());
            cloneConn.setRemoveEmptyRow(fileConn.isRemoveEmptyRow());
            cloneConn.setServer(fileConn.getServer());
            cloneConn.setTextEnclosure(fileConn.getTextEnclosure());
            cloneConn.setTextIdentifier(fileConn.getTextIdentifier());
            cloneConn.setUseFooter(fileConn.isUseFooter());
            cloneConn.setUseHeader(fileConn.isUseHeader());
            cloneConn.setUseLimit(fileConn.isUseLimit());

            ConnectionContextHelper.cloneConnectionProperties(fileConn, cloneConn);
        }
        return cloneConn;
    }

    static void revertPropertiesForContextMode(FileConnection fileConn, ContextType contextType) {
        if (fileConn == null || contextType == null) {
            return;
        }
        String filePath = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getFilePath());
        String encoding = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getEncoding());
        String headValue = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getHeaderValue());
        String footerValue = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getFooterValue());
        String limitValue = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getLimitValue());

        filePath = TalendTextUtils.removeQuotes(filePath);
        fileConn.setFilePath(filePath);
        fileConn.setEncoding(encoding);
        fileConn.setHeaderValue(headValue);
        fileConn.setFooterValue(footerValue);
        fileConn.setLimitValue(limitValue);

        //
        if (fileConn instanceof DelimitedFileConnection || fileConn instanceof PositionalFileConnection
                || fileConn instanceof RegexpFileConnection) {
            String fieldSeparatorValue = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getFieldSeparatorValue());
            String rowSeparatorValue = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getRowSeparatorValue());

            fileConn.setFieldSeparatorValue(fieldSeparatorValue);
            fileConn.setRowSeparatorValue(rowSeparatorValue);

        }
        // excel
        if (fileConn instanceof FileExcelConnection) {
            FileExcelConnection excelConnection = (FileExcelConnection) fileConn;

            String thousandSeparator = ConnectionContextHelper.getOriginalValue(contextType, excelConnection
                    .getThousandSeparator());
            String decimalSeparator = ConnectionContextHelper
                    .getOriginalValue(contextType, excelConnection.getDecimalSeparator());
            String firstColumn = ConnectionContextHelper.getOriginalValue(contextType, excelConnection.getFirstColumn());
            String lastColumn = ConnectionContextHelper.getOriginalValue(contextType, excelConnection.getLastColumn());

            excelConnection.setThousandSeparator(thousandSeparator);
            excelConnection.setDecimalSeparator(decimalSeparator);
            excelConnection.setFirstColumn(firstColumn);
            excelConnection.setLastColumn(lastColumn);
        }
    }
}
