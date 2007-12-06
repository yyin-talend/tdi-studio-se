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
package org.talend.repository.ui.actions.metadata.importing.bean;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class DelimitedTableForDelimitedBean extends TablesForDelimitedBean {

    private static final int TOTAL = 13;

    private String filePath;

    private String format;

    private String encoding;

    private String fieldSeparatorValue;

    private String rowSeparatorValue;

    private String escapeType;

    private String escapeChar;

    private String textEnclosure;

    private boolean firstLineCaption;

    private int headerValue;

    private int footerValue;

    private boolean removeEmptyRow;

    private int limitValue;

    public DelimitedTableForDelimitedBean() {
        super();
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getFieldSeparatorValue() {
        return this.fieldSeparatorValue;
    }

    public void setFieldSeparatorValue(String fieldSeparatorValue) {
        this.fieldSeparatorValue = fieldSeparatorValue;
    }

    public String getRowSeparatorValue() {
        return this.rowSeparatorValue;
    }

    public void setRowSeparatorValue(String rowSeparatorValue) {
        this.rowSeparatorValue = rowSeparatorValue;
    }

    public String getEscapeType() {
        return this.escapeType;
    }

    public void setEscapeType(String escapeType) {
        this.escapeType = escapeType;
    }

    public String getEscapeChar() {
        return this.escapeChar;
    }

    public void setEscapeChar(String escapeChar) {
        this.escapeChar = escapeChar;
    }

    public String getTextEnclosure() {
        return this.textEnclosure;
    }

    public void setTextEnclosure(String textEnclosure) {
        this.textEnclosure = textEnclosure;
    }

    public boolean isFirstLineCaption() {
        return this.firstLineCaption;
    }

    public void setFirstLineCaption(boolean firstLineCaption) {
        this.firstLineCaption = firstLineCaption;
    }

    public int getHeaderValue() {
        return this.headerValue;
    }

    public void setHeaderValue(int headerValue) {
        this.headerValue = headerValue;
    }

    public int getFooterValue() {
        return this.footerValue;
    }

    public void setFooterValue(int footerValue) {
        this.footerValue = footerValue;
    }

    public boolean isRemoveEmptyRow() {
        return this.removeEmptyRow;
    }

    public void setRemoveEmptyRow(boolean removeEmptyRow) {
        this.removeEmptyRow = removeEmptyRow;
    }

    public int getLimitValue() {
        return this.limitValue;
    }

    public void setLimitValue(int limitValue) {
        this.limitValue = limitValue;
    }

    @Override
    public int getVarNum() {
        return TOTAL + super.getVarNum();
    }

    public int getIndexFilePath() {
        return 5;
    }

    @Override
    public int getIndexTableName() {
        return 18;
    }

    @Override
    public int getIndexLabel() {
        return 19;
    }

    @Override
    public int getIndexLengh() {
        return 23;
    }
}
