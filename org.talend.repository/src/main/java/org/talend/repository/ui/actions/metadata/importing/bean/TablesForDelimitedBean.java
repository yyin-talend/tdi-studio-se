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
public abstract class TablesForDelimitedBean {

    private static final int TOTAL = 15;

    private String name;

    private String purpose;

    private String description;

    private String version;

    private String status;

    private String tableName;

    private String label;

    private String comment;

    private String defaultValue;

    private boolean key;

    private int length;

    private boolean nullable;

    private String pattern;

    private int precision;

    private String talendType;

    public TablesForDelimitedBean() {
        super();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isKey() {
        return this.key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNullable() {
        return this.nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getPrecision() {
        return this.precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public String getTalendType() {
        return this.talendType;
    }

    public void setTalendType(String talendType) {
        this.talendType = talendType;
    }

    public int getVarNum() {
        return TOTAL;
    }

    public int getIndexName() {
        return 0;
    }

    public abstract int getIndexTableName();

    public abstract int getIndexLabel();

    public int getIndexKey() {
        return getIndexLengh() - 1;
    }

    public abstract int getIndexLengh();

}
