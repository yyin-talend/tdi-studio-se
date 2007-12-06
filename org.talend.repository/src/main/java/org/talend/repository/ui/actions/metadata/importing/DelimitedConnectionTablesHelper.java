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
package org.talend.repository.ui.actions.metadata.importing;

import java.io.File;

import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.connection.FieldSeparator;
import org.talend.core.model.metadata.builder.connection.FileFormat;
import org.talend.core.model.metadata.builder.connection.RowSeparator;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.RejectedType;
import org.talend.repository.ui.actions.metadata.importing.bean.DelimitedTableForDelimitedBean;
import org.talend.repository.ui.actions.metadata.importing.bean.TablesForDelimitedBean;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class DelimitedConnectionTablesHelper extends AbstractImportingTablesHelper {

    /**
     * DOC ggu DelimitedConnectionTablesHelper constructor comment.
     * 
     * @param file
     */
    public DelimitedConnectionTablesHelper(File file) {
        super(ERepositoryObjectType.METADATA_FILE_DELIMITED, file);
    }

    @Override
    protected ConnectionItem createConnectionItem(TablesForDelimitedBean bean) {
        DelimitedTableForDelimitedBean theBean = (DelimitedTableForDelimitedBean) bean;
        if (isNullable(theBean.getFilePath())) {
            processRecords.addRejectedRecords(RejectedType.FILE, theBean.getFilePath(), bean.getName());
            return null;
        }
        DelimitedFileConnection conn = createConnection(theBean);
        if (conn == null) {
            return null;
        }

        DelimitedFileConnectionItem connItem = PropertiesFactory.eINSTANCE.createDelimitedFileConnectionItem();
        createProperty(connItem, bean);
        connItem.setConnection(conn);

        setConnectionCreated(true);

        return connItem;

    }

    private DelimitedFileConnection createConnection(DelimitedTableForDelimitedBean theBean) {
        DelimitedFileConnection conn = ConnectionFactory.eINSTANCE.createDelimitedFileConnection();
        conn.setFilePath(theBean.getFilePath());

        FileFormat format = FileFormat.UNIX_LITERAL;
        if (!isNullable(theBean.getFormat())) {
            format = FileFormat.getByName(theBean.getFormat());
            if (format == null) {
                // not surport
                processRecords.addRejectedRecords(RejectedType.FORMAT, theBean.getFormat(), theBean.getName());
                return null;
            }
        }

        conn.setFormat(format);

        EMetadataEncoding encoding = EMetadataEncoding.getMetadataEncoding(theBean.getEncoding());
        conn.setEncoding(encoding.getName());

        // fieldSeparatorType
        conn.setFieldSeparatorValue(theBean.getFieldSeparatorValue());
        FieldSeparator fieldSeparatorType = getFieldSeparator(theBean.getFieldSeparatorValue());
        conn.setFieldSeparatorType(fieldSeparatorType);

        // RowSeparator
        conn.setRowSeparatorValue(theBean.getRowSeparatorValue());
        RowSeparator rowSeparatorType = getRowSeparator(theBean.getRowSeparatorValue());
        conn.setRowSeparatorType(rowSeparatorType);

        // Escape
        Escape escapeType = Escape.DELIMITED_LITERAL;
        if (!isNullable(theBean.getEscapeType())) {
            escapeType = Escape.getByName(theBean.getEscapeType());
            if (escapeType == null) {
                // not surport
                processRecords.addRejectedRecords(RejectedType.ESCAPE, theBean.getEscapeType(), theBean.getName());
                return null;
            }
        }
        conn.setEscapeType(escapeType);
        conn.setEscapeChar(theBean.getEscapeChar());
        conn.setTextEnclosure(theBean.getTextEnclosure());

        conn.setFirstLineCaption(theBean.isFirstLineCaption());

        conn.setHeaderValue(theBean.getHeaderValue());
        if (theBean.getHeaderValue() > 0) {
            conn.setUseHeader(true);
        } else {
            conn.setUseHeader(false);
        }

        conn.setFooterValue(theBean.getFooterValue());
        if (theBean.getFooterValue() > 0) {
            conn.setUseFooter(true);
        } else {
            conn.setUseFooter(false);
        }
        conn.setRemoveEmptyRow(theBean.isRemoveEmptyRow());

        conn.setLimitValue(theBean.getLimitValue());
        if (theBean.getLimitValue() > 0) {
            conn.setUseLimit(true);
        } else {
            conn.setUseLimit(false);
        }

        return conn;
    }

    private FieldSeparator getFieldSeparator(String fieldSeparatorValue) {
        if (fieldSeparatorValue == null) {
            return FieldSeparator.CUSTOM_ANSI_LITERAL;
        }
        String mark = TalendTextUtils.removeQuotes(fieldSeparatorValue);
        if (";".equals(mark)) { //$NON-NLS-1$
            return FieldSeparator.SEMICOLON_LITERAL;
        } else if (",".equals(mark)) { //$NON-NLS-1$
            return FieldSeparator.COMMA_LITERAL;
        } else if ("\\t".equals(mark)) { //$NON-NLS-1$
            return FieldSeparator.TABULATION_LITERAL;
        } else if (" ".equals(mark)) { //$NON-NLS-1$
            return FieldSeparator.SPACE_LITERAL;
        } else if ("''".equals(mark)) { //$NON-NLS-1$
            return FieldSeparator.ALT_65_LITERAL;
        } else {
            return FieldSeparator.CUSTOM_ANSI_LITERAL;
        }

    }

    private RowSeparator getRowSeparator(String rowSeparatorValue) {
        if (rowSeparatorValue == null) {
            return RowSeparator.CUSTOM_STRING_LITERAL;
        }
        if (rowSeparatorValue.indexOf("\\n") > 0) { //$NON-NLS-1$
            return RowSeparator.STANDART_EOL_LITERAL;
        }
        return RowSeparator.CUSTOM_STRING_LITERAL;
    }
}
