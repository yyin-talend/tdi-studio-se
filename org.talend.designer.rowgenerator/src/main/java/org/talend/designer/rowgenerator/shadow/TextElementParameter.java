// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.rowgenerator.shadow;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.properties.Item;

/**
 * Simple Text implementation of IElementParameter. <br/>
 * 
 * $Id: TextElementParameter.java,v 1.1 2007/02/02 07:59:31 pub Exp $
 * 
 */
public class TextElementParameter implements IElementParameter {

    private String name;

    private String value;

    private boolean contextMode;

    private RGB color;

    private RGB backgroundColor;

    /**
     * Constructs a new TextElementParameter.
     */
    public TextElementParameter(String name, String value) {
        super();

        this.name = name;
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getCategory()
     */
    public EComponentCategory getCategory() {
        return EComponentCategory.MAIN;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getDisplayName()
     */
    public String getDisplayName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getExtension()
     */
    public String getExtension() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getField()
     */
    public EParameterFieldType getField() {
        return EParameterFieldType.TEXT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsID()
     */
    public String[] getListItemsValue() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsValue()
     */
    public String[] getListItemsDisplayName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getMetadataType()
     */
    public String getMetadataType() {
        return "String"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getName()
     */
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNbLines()
     */
    public int getNbLines() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNumRow()
     */
    public int getNumRow() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getValue()
     */
    public Object getValue() {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getVariableName()
     */
    public String getVariableName() {
        return "__" + name + "__"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isReadOnly()
     */
    public boolean isReadOnly() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isRequired()
     */
    public boolean isRequired() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setCategory(org.talend
     * .core.model.process.EComponentCategory)
     */
    public void setCategory(EComponentCategory cat) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setDisplayName(java.lang .String)
     */
    public void setDisplayName(String s) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setExtension(java.lang .String)
     */
    public void setExtension(String extension) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setField(org.talend.core .model.process.EParameterFieldType)
     */
    public void setField(EParameterFieldType type) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsID(java.lang .String[])
     */
    public void setListItemsValue(String[] list) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsValue(java .lang.String[])
     */
    public void setListItemsDisplayName(String[] list) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setMetadataType(org.talend
     * .core.model.metadata.EMetadataType)
     */
    public void setMetadataType(String metadataType) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setName(java.lang.String)
     */
    public void setName(String s) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNbLines(int)
     */
    public void setNbLines(int nbLines) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNumRow(int)
     */
    public void setNumRow(int numRow) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setReadOnly(boolean)
     */
    public void setReadOnly(boolean readOnly) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRequired(boolean)
     */
    public void setRequired(boolean required) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setShow(boolean)
     */
    public void setShow(boolean show) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setValue(java.lang.Object )
     */
    public void setValue(Object o) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getDefaultClosedListValue
     * (org.talend.core.model.temp.ECodeLanguage)
     */
    public Object getDefaultClosedListValue() {
        return null;
    }

    public void setDefaultClosedListValue(Object o) {
    }

    public void setListItemsValue(Object[] list) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getRepositoryValue()
     */
    public String getRepositoryValue() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRepositoryValue(java .lang.String)
     */
    public void setRepositoryValue(String repositoryValue) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isRepositoryValueUsed()
     */
    public boolean isRepositoryValueUsed() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setRepositoryValueUsed (boolean)
     */
    public void setRepositoryValueUsed(boolean repositoryUsed) {
    }

    public String[] getListRepositoryItems() {
        return null;
    }

    public void setListRepositoryItems(String[] list) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setShowIf(java.lang.String )
     */
    public void setShowIf(String showIf) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getShowIf()
     */
    public String getShowIf() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNotShowIf(java.lang .String)
     */
    public void setNotShowIf(String notShowIf) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNotShowIf()
     */
    public String getNotShowIf() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#testIfShow(java.util. List)
     */
    public boolean isShow(List<? extends IElementParameter> listParam) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsDisplayCodeName
     * (org.talend.core.model.temp.ECodeLanguage)
     */
    public String[] getListItemsDisplayCodeName() {
        return null;
    }

    public void setListItemsDisplayCodeName(String[] list) {
    }

    public String[] getListItemsNotShowIf() {
        return null;
    }

    public String[] getListItemsShowIf() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isShow(java.lang.String, java.lang.String, java.util.List)
     */
    public boolean isShow(String conditionShowIf, String conditionNotShowIf, List<? extends IElementParameter> listParam) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsNotShowIf (java.lang.String[])
     */
    public void setListItemsNotShowIf(String[] list) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsShowIf(java .lang.String[])
     */
    public void setListItemsShowIf(String[] list) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getDefaultValues()
     */
    public List<IElementParameterDefaultValue> getDefaultValues() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setDefaultValues(java .util.List)
     */
    public void setDefaultValues(List<IElementParameterDefaultValue> defaultValues) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setValueToDefault(java .util.List)
     */
    public void setValueToDefault(List<? extends IElementParameter> listParam) {
    }

    public int getIndexOfItemFromList(String item) {
        return 0;
    }

    public IElement getElement() {
        return null;
    }

    public void setElement(IElement element) {
    }

    public boolean isBasedOnSchema() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setBasedOnSchema(boolean basedOnSchema) {
        // TODO Auto-generated method stub

    }

    public String getFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setFilter(String filter) {
        // TODO Auto-generated method stub

    }

    public boolean isNoCheck() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setNoCheck(boolean noCheck) {
        // TODO Auto-generated method stub

    }

    public String getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setContext(String context) {
        // TODO Auto-generated method stub

    }

    public Map<String, IElementParameter> getChildParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    public IElementParameter getParentParameter() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setParentParameter(IElementParameter parentParameter) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getGroup()
     */
    public String getGroup() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setGroup(java.lang.String )
     */
    public void setGroup(String groupName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getGroupDisplayName()
     */
    public String getGroupDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setGroupDisplayName(java .lang.String)
     */
    public void setGroupDisplayName(String groupDisplayName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getLinkedRepositoryItem()
     */
    public Item getLinkedRepositoryItem() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setLinkedRepositoryItem
     * (org.talend.core.model.properties.Item)
     */
    public void setLinkedRepositoryItem(Item item) {
        // TODO Auto-generated method stub

    }

    /**
     * Getter for contextMode.
     * 
     * @return the contextMode
     */
    public boolean isContextMode() {
        return this.contextMode;
    }

    /**
     * Sets the contextMode.
     * 
     * @param contextMode the contextMode to set
     */
    public void setContextMode(boolean contextMode) {
        this.contextMode = contextMode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getLabelFromRepository()
     */
    public String getLabelFromRepository() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setLabelFromRepository (java.lang.String)
     */
    public void setLabelFromRepository(String label) {
        // TODO Auto-generated method stub

    }

    /**
     * Getter for color.
     * 
     * @return the color
     */
    public RGB getColor() {
        return this.color;
    }

    /**
     * Sets the color.
     * 
     * @param color the color to set
     */
    public void setColor(RGB color) {
        this.color = color;
    }

    /**
     * Getter for backgroundColor.
     * 
     * @return the backgroundColor
     */
    public RGB getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * Sets the backgroundColor.
     * 
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(RGB backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isBasedOnSubjobStarts()
     */
    public boolean isBasedOnSubjobStarts() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setBasedOnSubjobStarts (boolean)
     */
    public void setBasedOnSubjobStarts(boolean basedOnSubjobStarts) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isDynamicSettings()
     */
    public boolean isDynamicSettings() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setDynamicSettings(boolean )
     */
    public void setDynamicSettings(boolean dynamicSettings) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsNotReadOnlyIf()
     */
    public String[] getListItemsNotReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getListItemsReadOnlyIf()
     */
    public String[] getListItemsReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getNotReadOnlyIf()
     */
    public String getNotReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#getReadOnlyIf()
     */
    public String getReadOnlyIf() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isReadOnly(java.util.List)
     */
    public boolean isReadOnly(List<? extends IElementParameter> listParam) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isReadOnly(java.lang.String, java.lang.String,
     * java.util.List)
     */
    public boolean isReadOnly(String conditionReadOnlyIf, String conditionNotReadOnlyIf,
            List<? extends IElementParameter> listParams) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsNotReadOnlyIf(java.lang.String[])
     */
    public void setListItemsNotReadOnlyIf(String[] list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setListItemsReadOnlyIf(java.lang.String[])
     */
    public void setListItemsReadOnlyIf(String[] list) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setNotReadOnlyIf(java.lang.String)
     */
    public void setNotReadOnlyIf(String notShowIf) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setReadOnlyIf(java.lang.String)
     */
    public void setReadOnlyIf(String showIf) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#isColumnsBasedOnSchema()
     */
    public boolean isColumnsBasedOnSchema() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElementParameter#setColumnsBasedOnSchema(boolean)
     */
    public void setColumnsBasedOnSchema(boolean columnsBasedOnSchema) {

    }
}
