/**
 * Field.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * Field bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class Field implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = Field Namespace URI = urn:partner.soap.sforce.com Namespace Prefix =
     * ns1
     */

    /**
     * field for AutoNumber
     */

    protected boolean localAutoNumber;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getAutoNumber() {
        return localAutoNumber;
    }

    /**
     * Auto generated setter method
     * 
     * @param param AutoNumber
     */
    public void setAutoNumber(boolean param) {

        this.localAutoNumber = param;

    }

    /**
     * field for ByteLength
     */

    protected int localByteLength;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getByteLength() {
        return localByteLength;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ByteLength
     */
    public void setByteLength(int param) {

        this.localByteLength = param;

    }

    /**
     * field for Calculated
     */

    protected boolean localCalculated;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getCalculated() {
        return localCalculated;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Calculated
     */
    public void setCalculated(boolean param) {

        this.localCalculated = param;

    }

    /**
     * field for CalculatedFormula
     */

    protected java.lang.String localCalculatedFormula;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localCalculatedFormulaTracker = false;

    public boolean isCalculatedFormulaSpecified() {
        return localCalculatedFormulaTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getCalculatedFormula() {
        return localCalculatedFormula;
    }

    /**
     * Auto generated setter method
     * 
     * @param param CalculatedFormula
     */
    public void setCalculatedFormula(java.lang.String param) {
        localCalculatedFormulaTracker = param != null;

        this.localCalculatedFormula = param;

    }

    /**
     * field for CascadeDelete
     */

    protected boolean localCascadeDelete;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localCascadeDeleteTracker = false;

    public boolean isCascadeDeleteSpecified() {
        return localCascadeDeleteTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getCascadeDelete() {
        return localCascadeDelete;
    }

    /**
     * Auto generated setter method
     * 
     * @param param CascadeDelete
     */
    public void setCascadeDelete(boolean param) {

        // setting primitive attribute tracker to true
        localCascadeDeleteTracker = true;

        this.localCascadeDelete = param;

    }

    /**
     * field for CaseSensitive
     */

    protected boolean localCaseSensitive;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getCaseSensitive() {
        return localCaseSensitive;
    }

    /**
     * Auto generated setter method
     * 
     * @param param CaseSensitive
     */
    public void setCaseSensitive(boolean param) {

        this.localCaseSensitive = param;

    }

    /**
     * field for ControllerName
     */

    protected java.lang.String localControllerName;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localControllerNameTracker = false;

    public boolean isControllerNameSpecified() {
        return localControllerNameTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getControllerName() {
        return localControllerName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ControllerName
     */
    public void setControllerName(java.lang.String param) {
        localControllerNameTracker = param != null;

        this.localControllerName = param;

    }

    /**
     * field for Createable
     */

    protected boolean localCreateable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getCreateable() {
        return localCreateable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Createable
     */
    public void setCreateable(boolean param) {

        this.localCreateable = param;

    }

    /**
     * field for Custom
     */

    protected boolean localCustom;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getCustom() {
        return localCustom;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Custom
     */
    public void setCustom(boolean param) {

        this.localCustom = param;

    }

    /**
     * field for DefaultValueFormula
     */

    protected java.lang.String localDefaultValueFormula;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localDefaultValueFormulaTracker = false;

    public boolean isDefaultValueFormulaSpecified() {
        return localDefaultValueFormulaTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getDefaultValueFormula() {
        return localDefaultValueFormula;
    }

    /**
     * Auto generated setter method
     * 
     * @param param DefaultValueFormula
     */
    public void setDefaultValueFormula(java.lang.String param) {
        localDefaultValueFormulaTracker = param != null;

        this.localDefaultValueFormula = param;

    }

    /**
     * field for DefaultedOnCreate
     */

    protected boolean localDefaultedOnCreate;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getDefaultedOnCreate() {
        return localDefaultedOnCreate;
    }

    /**
     * Auto generated setter method
     * 
     * @param param DefaultedOnCreate
     */
    public void setDefaultedOnCreate(boolean param) {

        this.localDefaultedOnCreate = param;

    }

    /**
     * field for DependentPicklist
     */

    protected boolean localDependentPicklist;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localDependentPicklistTracker = false;

    public boolean isDependentPicklistSpecified() {
        return localDependentPicklistTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getDependentPicklist() {
        return localDependentPicklist;
    }

    /**
     * Auto generated setter method
     * 
     * @param param DependentPicklist
     */
    public void setDependentPicklist(boolean param) {

        // setting primitive attribute tracker to true
        localDependentPicklistTracker = true;

        this.localDependentPicklist = param;

    }

    /**
     * field for DeprecatedAndHidden
     */

    protected boolean localDeprecatedAndHidden;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getDeprecatedAndHidden() {
        return localDeprecatedAndHidden;
    }

    /**
     * Auto generated setter method
     * 
     * @param param DeprecatedAndHidden
     */
    public void setDeprecatedAndHidden(boolean param) {

        this.localDeprecatedAndHidden = param;

    }

    /**
     * field for Digits
     */

    protected int localDigits;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getDigits() {
        return localDigits;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Digits
     */
    public void setDigits(int param) {

        this.localDigits = param;

    }

    /**
     * field for DisplayLocationInDecimal
     */

    protected boolean localDisplayLocationInDecimal;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localDisplayLocationInDecimalTracker = false;

    public boolean isDisplayLocationInDecimalSpecified() {
        return localDisplayLocationInDecimalTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getDisplayLocationInDecimal() {
        return localDisplayLocationInDecimal;
    }

    /**
     * Auto generated setter method
     * 
     * @param param DisplayLocationInDecimal
     */
    public void setDisplayLocationInDecimal(boolean param) {

        // setting primitive attribute tracker to true
        localDisplayLocationInDecimalTracker = true;

        this.localDisplayLocationInDecimal = param;

    }

    /**
     * field for Encrypted
     */

    protected boolean localEncrypted;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localEncryptedTracker = false;

    public boolean isEncryptedSpecified() {
        return localEncryptedTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getEncrypted() {
        return localEncrypted;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Encrypted
     */
    public void setEncrypted(boolean param) {

        // setting primitive attribute tracker to true
        localEncryptedTracker = true;

        this.localEncrypted = param;

    }

    /**
     * field for ExternalId
     */

    protected boolean localExternalId;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localExternalIdTracker = false;

    public boolean isExternalIdSpecified() {
        return localExternalIdTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getExternalId() {
        return localExternalId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ExternalId
     */
    public void setExternalId(boolean param) {

        // setting primitive attribute tracker to true
        localExternalIdTracker = true;

        this.localExternalId = param;

    }

    /**
     * field for ExtraTypeInfo
     */

    protected java.lang.String localExtraTypeInfo;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localExtraTypeInfoTracker = false;

    public boolean isExtraTypeInfoSpecified() {
        return localExtraTypeInfoTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getExtraTypeInfo() {
        return localExtraTypeInfo;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ExtraTypeInfo
     */
    public void setExtraTypeInfo(java.lang.String param) {
        localExtraTypeInfoTracker = param != null;

        this.localExtraTypeInfo = param;

    }

    /**
     * field for Filterable
     */

    protected boolean localFilterable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getFilterable() {
        return localFilterable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Filterable
     */
    public void setFilterable(boolean param) {

        this.localFilterable = param;

    }

    /**
     * field for FilteredLookupInfo
     */

    protected com.salesforce.soap.partner.FilteredLookupInfo localFilteredLookupInfo;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localFilteredLookupInfoTracker = false;

    public boolean isFilteredLookupInfoSpecified() {
        return localFilteredLookupInfoTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.FilteredLookupInfo
     */
    public com.salesforce.soap.partner.FilteredLookupInfo getFilteredLookupInfo() {
        return localFilteredLookupInfo;
    }

    /**
     * Auto generated setter method
     * 
     * @param param FilteredLookupInfo
     */
    public void setFilteredLookupInfo(com.salesforce.soap.partner.FilteredLookupInfo param) {
        localFilteredLookupInfoTracker = true;

        this.localFilteredLookupInfo = param;

    }

    /**
     * field for Groupable
     */

    protected boolean localGroupable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getGroupable() {
        return localGroupable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Groupable
     */
    public void setGroupable(boolean param) {

        this.localGroupable = param;

    }

    /**
     * field for HighScaleNumber
     */

    protected boolean localHighScaleNumber;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localHighScaleNumberTracker = false;

    public boolean isHighScaleNumberSpecified() {
        return localHighScaleNumberTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getHighScaleNumber() {
        return localHighScaleNumber;
    }

    /**
     * Auto generated setter method
     * 
     * @param param HighScaleNumber
     */
    public void setHighScaleNumber(boolean param) {

        // setting primitive attribute tracker to true
        localHighScaleNumberTracker = true;

        this.localHighScaleNumber = param;

    }

    /**
     * field for HtmlFormatted
     */

    protected boolean localHtmlFormatted;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localHtmlFormattedTracker = false;

    public boolean isHtmlFormattedSpecified() {
        return localHtmlFormattedTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getHtmlFormatted() {
        return localHtmlFormatted;
    }

    /**
     * Auto generated setter method
     * 
     * @param param HtmlFormatted
     */
    public void setHtmlFormatted(boolean param) {

        // setting primitive attribute tracker to true
        localHtmlFormattedTracker = true;

        this.localHtmlFormatted = param;

    }

    /**
     * field for IdLookup
     */

    protected boolean localIdLookup;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getIdLookup() {
        return localIdLookup;
    }

    /**
     * Auto generated setter method
     * 
     * @param param IdLookup
     */
    public void setIdLookup(boolean param) {

        this.localIdLookup = param;

    }

    /**
     * field for InlineHelpText
     */

    protected java.lang.String localInlineHelpText;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localInlineHelpTextTracker = false;

    public boolean isInlineHelpTextSpecified() {
        return localInlineHelpTextTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getInlineHelpText() {
        return localInlineHelpText;
    }

    /**
     * Auto generated setter method
     * 
     * @param param InlineHelpText
     */
    public void setInlineHelpText(java.lang.String param) {
        localInlineHelpTextTracker = param != null;

        this.localInlineHelpText = param;

    }

    /**
     * field for Label
     */

    protected java.lang.String localLabel;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getLabel() {
        return localLabel;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Label
     */
    public void setLabel(java.lang.String param) {

        this.localLabel = param;

    }

    /**
     * field for Length
     */

    protected int localLength;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getLength() {
        return localLength;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Length
     */
    public void setLength(int param) {

        this.localLength = param;

    }

    /**
     * field for Mask
     */

    protected java.lang.String localMask;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localMaskTracker = false;

    public boolean isMaskSpecified() {
        return localMaskTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getMask() {
        return localMask;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Mask
     */
    public void setMask(java.lang.String param) {
        localMaskTracker = param != null;

        this.localMask = param;

    }

    /**
     * field for MaskType
     */

    protected java.lang.String localMaskType;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localMaskTypeTracker = false;

    public boolean isMaskTypeSpecified() {
        return localMaskTypeTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getMaskType() {
        return localMaskType;
    }

    /**
     * Auto generated setter method
     * 
     * @param param MaskType
     */
    public void setMaskType(java.lang.String param) {
        localMaskTypeTracker = param != null;

        this.localMaskType = param;

    }

    /**
     * field for Name
     */

    protected java.lang.String localName;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getName() {
        return localName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Name
     */
    public void setName(java.lang.String param) {

        this.localName = param;

    }

    /**
     * field for NameField
     */

    protected boolean localNameField;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getNameField() {
        return localNameField;
    }

    /**
     * Auto generated setter method
     * 
     * @param param NameField
     */
    public void setNameField(boolean param) {

        this.localNameField = param;

    }

    /**
     * field for NamePointing
     */

    protected boolean localNamePointing;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localNamePointingTracker = false;

    public boolean isNamePointingSpecified() {
        return localNamePointingTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getNamePointing() {
        return localNamePointing;
    }

    /**
     * Auto generated setter method
     * 
     * @param param NamePointing
     */
    public void setNamePointing(boolean param) {

        // setting primitive attribute tracker to true
        localNamePointingTracker = true;

        this.localNamePointing = param;

    }

    /**
     * field for Nillable
     */

    protected boolean localNillable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getNillable() {
        return localNillable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Nillable
     */
    public void setNillable(boolean param) {

        this.localNillable = param;

    }

    /**
     * field for Permissionable
     */

    protected boolean localPermissionable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getPermissionable() {
        return localPermissionable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Permissionable
     */
    public void setPermissionable(boolean param) {

        this.localPermissionable = param;

    }

    /**
     * field for PicklistValues This was an Array!
     */

    protected com.salesforce.soap.partner.PicklistEntry[] localPicklistValues;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localPicklistValuesTracker = false;

    public boolean isPicklistValuesSpecified() {
        return localPicklistValuesTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.PicklistEntry[]
     */
    public com.salesforce.soap.partner.PicklistEntry[] getPicklistValues() {
        return localPicklistValues;
    }

    /**
     * validate the array for PicklistValues
     */
    protected void validatePicklistValues(com.salesforce.soap.partner.PicklistEntry[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param PicklistValues
     */
    public void setPicklistValues(com.salesforce.soap.partner.PicklistEntry[] param) {

        validatePicklistValues(param);

        localPicklistValuesTracker = true;

        this.localPicklistValues = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.PicklistEntry
     */
    public void addPicklistValues(com.salesforce.soap.partner.PicklistEntry param) {
        if (localPicklistValues == null) {
            localPicklistValues = new com.salesforce.soap.partner.PicklistEntry[] {};
        }

        // update the setting tracker
        localPicklistValuesTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localPicklistValues);
        list.add(param);
        this.localPicklistValues = (com.salesforce.soap.partner.PicklistEntry[]) list
                .toArray(new com.salesforce.soap.partner.PicklistEntry[list.size()]);

    }

    /**
     * field for Precision
     */

    protected int localPrecision;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getPrecision() {
        return localPrecision;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Precision
     */
    public void setPrecision(int param) {

        this.localPrecision = param;

    }

    /**
     * field for QueryByDistance
     */

    protected boolean localQueryByDistance;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getQueryByDistance() {
        return localQueryByDistance;
    }

    /**
     * Auto generated setter method
     * 
     * @param param QueryByDistance
     */
    public void setQueryByDistance(boolean param) {

        this.localQueryByDistance = param;

    }

    /**
     * field for ReferenceTargetField
     */

    protected java.lang.String localReferenceTargetField;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localReferenceTargetFieldTracker = false;

    public boolean isReferenceTargetFieldSpecified() {
        return localReferenceTargetFieldTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getReferenceTargetField() {
        return localReferenceTargetField;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ReferenceTargetField
     */
    public void setReferenceTargetField(java.lang.String param) {
        localReferenceTargetFieldTracker = param != null;

        this.localReferenceTargetField = param;

    }

    /**
     * field for ReferenceTo This was an Array!
     */

    protected java.lang.String[] localReferenceTo;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localReferenceToTracker = false;

    public boolean isReferenceToSpecified() {
        return localReferenceToTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String[]
     */
    public java.lang.String[] getReferenceTo() {
        return localReferenceTo;
    }

    /**
     * validate the array for ReferenceTo
     */
    protected void validateReferenceTo(java.lang.String[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param ReferenceTo
     */
    public void setReferenceTo(java.lang.String[] param) {

        validateReferenceTo(param);

        localReferenceToTracker = true;

        this.localReferenceTo = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param java.lang.String
     */
    public void addReferenceTo(java.lang.String param) {
        if (localReferenceTo == null) {
            localReferenceTo = new java.lang.String[] {};
        }

        // update the setting tracker
        localReferenceToTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localReferenceTo);
        list.add(param);
        this.localReferenceTo = (java.lang.String[]) list.toArray(new java.lang.String[list.size()]);

    }

    /**
     * field for RelationshipName
     */

    protected java.lang.String localRelationshipName;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localRelationshipNameTracker = false;

    public boolean isRelationshipNameSpecified() {
        return localRelationshipNameTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getRelationshipName() {
        return localRelationshipName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param RelationshipName
     */
    public void setRelationshipName(java.lang.String param) {
        localRelationshipNameTracker = param != null;

        this.localRelationshipName = param;

    }

    /**
     * field for RelationshipOrder
     */

    protected int localRelationshipOrder;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localRelationshipOrderTracker = false;

    public boolean isRelationshipOrderSpecified() {
        return localRelationshipOrderTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getRelationshipOrder() {
        return localRelationshipOrder;
    }

    /**
     * Auto generated setter method
     * 
     * @param param RelationshipOrder
     */
    public void setRelationshipOrder(int param) {

        // setting primitive attribute tracker to true
        localRelationshipOrderTracker = param != java.lang.Integer.MIN_VALUE;

        this.localRelationshipOrder = param;

    }

    /**
     * field for RestrictedDelete
     */

    protected boolean localRestrictedDelete;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localRestrictedDeleteTracker = false;

    public boolean isRestrictedDeleteSpecified() {
        return localRestrictedDeleteTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getRestrictedDelete() {
        return localRestrictedDelete;
    }

    /**
     * Auto generated setter method
     * 
     * @param param RestrictedDelete
     */
    public void setRestrictedDelete(boolean param) {

        // setting primitive attribute tracker to true
        localRestrictedDeleteTracker = true;

        this.localRestrictedDelete = param;

    }

    /**
     * field for RestrictedPicklist
     */

    protected boolean localRestrictedPicklist;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getRestrictedPicklist() {
        return localRestrictedPicklist;
    }

    /**
     * Auto generated setter method
     * 
     * @param param RestrictedPicklist
     */
    public void setRestrictedPicklist(boolean param) {

        this.localRestrictedPicklist = param;

    }

    /**
     * field for Scale
     */

    protected int localScale;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getScale() {
        return localScale;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Scale
     */
    public void setScale(int param) {

        this.localScale = param;

    }

    /**
     * field for SoapType
     */

    protected com.salesforce.soap.partner.SoapType localSoapType;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.SoapType
     */
    public com.salesforce.soap.partner.SoapType getSoapType() {
        return localSoapType;
    }

    /**
     * Auto generated setter method
     * 
     * @param param SoapType
     */
    public void setSoapType(com.salesforce.soap.partner.SoapType param) {

        this.localSoapType = param;

    }

    /**
     * field for Sortable
     */

    protected boolean localSortable;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localSortableTracker = false;

    public boolean isSortableSpecified() {
        return localSortableTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getSortable() {
        return localSortable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Sortable
     */
    public void setSortable(boolean param) {

        // setting primitive attribute tracker to true
        localSortableTracker = true;

        this.localSortable = param;

    }

    /**
     * field for Type
     */

    protected com.salesforce.soap.partner.FieldType localType;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.FieldType
     */
    public com.salesforce.soap.partner.FieldType getType() {
        return localType;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Type
     */
    public void setType(com.salesforce.soap.partner.FieldType param) {

        this.localType = param;

    }

    /**
     * field for Unique
     */

    protected boolean localUnique;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getUnique() {
        return localUnique;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Unique
     */
    public void setUnique(boolean param) {

        this.localUnique = param;

    }

    /**
     * field for Updateable
     */

    protected boolean localUpdateable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getUpdateable() {
        return localUpdateable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Updateable
     */
    public void setUpdateable(boolean param) {

        this.localUpdateable = param;

    }

    /**
     * field for WriteRequiresMasterRead
     */

    protected boolean localWriteRequiresMasterRead;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localWriteRequiresMasterReadTracker = false;

    public boolean isWriteRequiresMasterReadSpecified() {
        return localWriteRequiresMasterReadTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getWriteRequiresMasterRead() {
        return localWriteRequiresMasterRead;
    }

    /**
     * Auto generated setter method
     * 
     * @param param WriteRequiresMasterRead
     */
    public void setWriteRequiresMasterRead(boolean param) {

        // setting primitive attribute tracker to true
        localWriteRequiresMasterReadTracker = true;

        this.localWriteRequiresMasterRead = param;

    }

    /**
     *
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {

        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
        return factory.createOMElement(dataSource, parentQName);

    }

    public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
        serialize(parentQName, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
            boolean serializeType) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {

        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

        if (serializeType) {

            java.lang.String namespacePrefix = registerPrefix(xmlWriter, "urn:partner.soap.sforce.com");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":Field", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "Field", xmlWriter);
            }

        }

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "autoNumber", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("autoNumber cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAutoNumber));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "byteLength", xmlWriter);

        if (localByteLength == java.lang.Integer.MIN_VALUE) {

            throw new org.apache.axis2.databinding.ADBException("byteLength cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localByteLength));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "calculated", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("calculated cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCalculated));
        }

        xmlWriter.writeEndElement();
        if (localCalculatedFormulaTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "calculatedFormula", xmlWriter);

            if (localCalculatedFormula == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("calculatedFormula cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localCalculatedFormula);

            }

            xmlWriter.writeEndElement();
        }
        if (localCascadeDeleteTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "cascadeDelete", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("cascadeDelete cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCascadeDelete));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "caseSensitive", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("caseSensitive cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCaseSensitive));
        }

        xmlWriter.writeEndElement();
        if (localControllerNameTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "controllerName", xmlWriter);

            if (localControllerName == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("controllerName cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localControllerName);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "createable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("createable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreateable));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "custom", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("custom cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustom));
        }

        xmlWriter.writeEndElement();
        if (localDefaultValueFormulaTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "defaultValueFormula", xmlWriter);

            if (localDefaultValueFormula == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("defaultValueFormula cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localDefaultValueFormula);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "defaultedOnCreate", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("defaultedOnCreate cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDefaultedOnCreate));
        }

        xmlWriter.writeEndElement();
        if (localDependentPicklistTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "dependentPicklist", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("dependentPicklist cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(localDependentPicklist));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "deprecatedAndHidden", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("deprecatedAndHidden cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeprecatedAndHidden));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "digits", xmlWriter);

        if (localDigits == java.lang.Integer.MIN_VALUE) {

            throw new org.apache.axis2.databinding.ADBException("digits cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDigits));
        }

        xmlWriter.writeEndElement();
        if (localDisplayLocationInDecimalTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "displayLocationInDecimal", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("displayLocationInDecimal cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(localDisplayLocationInDecimal));
            }

            xmlWriter.writeEndElement();
        }
        if (localEncryptedTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "encrypted", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("encrypted cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEncrypted));
            }

            xmlWriter.writeEndElement();
        }
        if (localExternalIdTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "externalId", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("externalId cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExternalId));
            }

            xmlWriter.writeEndElement();
        }
        if (localExtraTypeInfoTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "extraTypeInfo", xmlWriter);

            if (localExtraTypeInfo == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("extraTypeInfo cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localExtraTypeInfo);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "filterable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("filterable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFilterable));
        }

        xmlWriter.writeEndElement();
        if (localFilteredLookupInfoTracker) {
            if (localFilteredLookupInfo == null) {

                writeStartElement(null, "urn:partner.soap.sforce.com", "filteredLookupInfo", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localFilteredLookupInfo.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                        "filteredLookupInfo"), xmlWriter);
            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "groupable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("groupable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localGroupable));
        }

        xmlWriter.writeEndElement();
        if (localHighScaleNumberTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "highScaleNumber", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("highScaleNumber cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHighScaleNumber));
            }

            xmlWriter.writeEndElement();
        }
        if (localHtmlFormattedTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "htmlFormatted", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("htmlFormatted cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHtmlFormatted));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "idLookup", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("idLookup cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIdLookup));
        }

        xmlWriter.writeEndElement();
        if (localInlineHelpTextTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "inlineHelpText", xmlWriter);

            if (localInlineHelpText == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("inlineHelpText cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localInlineHelpText);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "label", xmlWriter);

        if (localLabel == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("label cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localLabel);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "length", xmlWriter);

        if (localLength == java.lang.Integer.MIN_VALUE) {

            throw new org.apache.axis2.databinding.ADBException("length cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLength));
        }

        xmlWriter.writeEndElement();
        if (localMaskTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "mask", xmlWriter);

            if (localMask == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("mask cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localMask);

            }

            xmlWriter.writeEndElement();
        }
        if (localMaskTypeTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "maskType", xmlWriter);

            if (localMaskType == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("maskType cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localMaskType);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "name", xmlWriter);

        if (localName == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("name cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localName);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "nameField", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("nameField cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNameField));
        }

        xmlWriter.writeEndElement();
        if (localNamePointingTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "namePointing", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("namePointing cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNamePointing));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "nillable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("nillable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNillable));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "permissionable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("permissionable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPermissionable));
        }

        xmlWriter.writeEndElement();
        if (localPicklistValuesTracker) {
            if (localPicklistValues != null) {
                for (int i = 0; i < localPicklistValues.length; i++) {
                    if (localPicklistValues[i] != null) {
                        localPicklistValues[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                "picklistValues"), xmlWriter);
                    } else {

                        writeStartElement(null, "urn:partner.soap.sforce.com", "picklistValues", xmlWriter);

                        // write the nil attribute
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();

                    }

                }
            } else {

                writeStartElement(null, "urn:partner.soap.sforce.com", "picklistValues", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();

            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "precision", xmlWriter);

        if (localPrecision == java.lang.Integer.MIN_VALUE) {

            throw new org.apache.axis2.databinding.ADBException("precision cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPrecision));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "queryByDistance", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("queryByDistance cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localQueryByDistance));
        }

        xmlWriter.writeEndElement();
        if (localReferenceTargetFieldTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "referenceTargetField", xmlWriter);

            if (localReferenceTargetField == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("referenceTargetField cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localReferenceTargetField);

            }

            xmlWriter.writeEndElement();
        }
        if (localReferenceToTracker) {
            if (localReferenceTo != null) {
                namespace = "urn:partner.soap.sforce.com";
                for (int i = 0; i < localReferenceTo.length; i++) {

                    if (localReferenceTo[i] != null) {

                        writeStartElement(null, namespace, "referenceTo", xmlWriter);

                        xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(localReferenceTo[i]));

                        xmlWriter.writeEndElement();

                    } else {

                        // write null attribute
                        namespace = "urn:partner.soap.sforce.com";
                        writeStartElement(null, namespace, "referenceTo", xmlWriter);
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();

                    }

                }
            } else {

                // write the null attribute
                // write null attribute
                writeStartElement(null, "urn:partner.soap.sforce.com", "referenceTo", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();

            }

        }
        if (localRelationshipNameTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "relationshipName", xmlWriter);

            if (localRelationshipName == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("relationshipName cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localRelationshipName);

            }

            xmlWriter.writeEndElement();
        }
        if (localRelationshipOrderTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "relationshipOrder", xmlWriter);

            if (localRelationshipOrder == java.lang.Integer.MIN_VALUE) {

                throw new org.apache.axis2.databinding.ADBException("relationshipOrder cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(localRelationshipOrder));
            }

            xmlWriter.writeEndElement();
        }
        if (localRestrictedDeleteTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "restrictedDelete", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("restrictedDelete cannot be null!!");

            } else {
                xmlWriter
                        .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRestrictedDelete));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "restrictedPicklist", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("restrictedPicklist cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRestrictedPicklist));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "scale", xmlWriter);

        if (localScale == java.lang.Integer.MIN_VALUE) {

            throw new org.apache.axis2.databinding.ADBException("scale cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localScale));
        }

        xmlWriter.writeEndElement();

        if (localSoapType == null) {
            throw new org.apache.axis2.databinding.ADBException("soapType cannot be null!!");
        }
        localSoapType.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "soapType"), xmlWriter);
        if (localSortableTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "sortable", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("sortable cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSortable));
            }

            xmlWriter.writeEndElement();
        }
        if (localType == null) {
            throw new org.apache.axis2.databinding.ADBException("type cannot be null!!");
        }
        localType.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type"), xmlWriter);

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "unique", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("unique cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUnique));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "updateable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("updateable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUpdateable));
        }

        xmlWriter.writeEndElement();
        if (localWriteRequiresMasterReadTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "writeRequiresMasterRead", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("writeRequiresMasterRead cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(localWriteRequiresMasterRead));
            }

            xmlWriter.writeEndElement();
        }
        xmlWriter.writeEndElement();

    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("urn:partner.soap.sforce.com")) {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
            javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
        if (writerPrefix != null) {
            xmlWriter.writeStartElement(namespace, localPart);
        } else {
            if (namespace.length() == 0) {
                prefix = "";
            } else if (prefix == null) {
                prefix = generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(java.lang.String prefix, java.lang.String namespace, java.lang.String attName,
            java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        if (xmlWriter.getPrefix(namespace) == null) {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        xmlWriter.writeAttribute(namespace, attName, attValue);
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeAttribute(java.lang.String namespace, java.lang.String attName, java.lang.String attValue,
            javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName, javax.xml.namespace.QName qname,
            javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }
        java.lang.String attributeValue;
        if (attributePrefix.trim().length() > 0) {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        } else {
            attributeValue = qname.getLocalPart();
        }

        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attributeValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attributeValue);
        }
    }

    /**
     * method to handle Qnames
     */

    private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException {
        java.lang.String namespaceURI = qname.getNamespaceURI();
        if (namespaceURI != null) {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
            if (prefix == null) {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0) {
                xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            } else {
                // i.e this is the default namespace
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException {

        if (qnames != null) {
            // we have to store this data until last moment since it is not possible to write any
            // namespace data after writing the charactor data
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;

            for (int i = 0; i < qnames.length; i++) {
                if (i > 0) {
                    stringToWrite.append(" ");
                }
                namespaceURI = qnames[i].getNamespaceURI();
                if (namespaceURI != null) {
                    prefix = xmlWriter.getPrefix(namespaceURI);
                    if ((prefix == null) || (prefix.length() == 0)) {
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0) {
                        stringToWrite.append(prefix).append(":")
                                .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                } else {
                    stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                }
            }
            xmlWriter.writeCharacters(stringToWrite.toString());
        }

    }

    /**
     * Register a namespace prefix
     */
    private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace)
            throws javax.xml.stream.XMLStreamException {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);
        if (prefix == null) {
            prefix = generatePrefix(namespace);
            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
            while (true) {
                java.lang.String uri = nsContext.getNamespaceURI(prefix);
                if (uri == null || uri.length() == 0) {
                    break;
                }
                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     *
     */
    public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
            throws org.apache.axis2.databinding.ADBException {

        java.util.ArrayList elementList = new java.util.ArrayList();
        java.util.ArrayList attribList = new java.util.ArrayList();

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "autoNumber"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAutoNumber));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "byteLength"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localByteLength));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "calculated"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCalculated));
        if (localCalculatedFormulaTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "calculatedFormula"));

            if (localCalculatedFormula != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCalculatedFormula));
            } else {
                throw new org.apache.axis2.databinding.ADBException("calculatedFormula cannot be null!!");
            }
        }
        if (localCascadeDeleteTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "cascadeDelete"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCascadeDelete));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "caseSensitive"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCaseSensitive));
        if (localControllerNameTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "controllerName"));

            if (localControllerName != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localControllerName));
            } else {
                throw new org.apache.axis2.databinding.ADBException("controllerName cannot be null!!");
            }
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "createable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreateable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "custom"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustom));
        if (localDefaultValueFormulaTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValueFormula"));

            if (localDefaultValueFormula != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDefaultValueFormula));
            } else {
                throw new org.apache.axis2.databinding.ADBException("defaultValueFormula cannot be null!!");
            }
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultedOnCreate"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDefaultedOnCreate));
        if (localDependentPicklistTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "dependentPicklist"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDependentPicklist));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deprecatedAndHidden"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeprecatedAndHidden));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "digits"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDigits));
        if (localDisplayLocationInDecimalTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "displayLocationInDecimal"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDisplayLocationInDecimal));
        }
        if (localEncryptedTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "encrypted"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEncrypted));
        }
        if (localExternalIdTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "externalId"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExternalId));
        }
        if (localExtraTypeInfoTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "extraTypeInfo"));

            if (localExtraTypeInfo != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExtraTypeInfo));
            } else {
                throw new org.apache.axis2.databinding.ADBException("extraTypeInfo cannot be null!!");
            }
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "filterable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFilterable));
        if (localFilteredLookupInfoTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "filteredLookupInfo"));

            elementList.add(localFilteredLookupInfo == null ? null : localFilteredLookupInfo);
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "groupable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localGroupable));
        if (localHighScaleNumberTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "highScaleNumber"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHighScaleNumber));
        }
        if (localHtmlFormattedTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "htmlFormatted"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHtmlFormatted));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "idLookup"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIdLookup));
        if (localInlineHelpTextTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "inlineHelpText"));

            if (localInlineHelpText != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInlineHelpText));
            } else {
                throw new org.apache.axis2.databinding.ADBException("inlineHelpText cannot be null!!");
            }
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "label"));

        if (localLabel != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLabel));
        } else {
            throw new org.apache.axis2.databinding.ADBException("label cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "length"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLength));
        if (localMaskTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "mask"));

            if (localMask != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMask));
            } else {
                throw new org.apache.axis2.databinding.ADBException("mask cannot be null!!");
            }
        }
        if (localMaskTypeTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "maskType"));

            if (localMaskType != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaskType));
            } else {
                throw new org.apache.axis2.databinding.ADBException("maskType cannot be null!!");
            }
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name"));

        if (localName != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localName));
        } else {
            throw new org.apache.axis2.databinding.ADBException("name cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "nameField"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNameField));
        if (localNamePointingTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "namePointing"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNamePointing));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "nillable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNillable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "permissionable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPermissionable));
        if (localPicklistValuesTracker) {
            if (localPicklistValues != null) {
                for (int i = 0; i < localPicklistValues.length; i++) {

                    if (localPicklistValues[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "picklistValues"));
                        elementList.add(localPicklistValues[i]);
                    } else {

                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "picklistValues"));
                        elementList.add(null);

                    }

                }
            } else {

                elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "picklistValues"));
                elementList.add(localPicklistValues);

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "precision"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPrecision));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryByDistance"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localQueryByDistance));
        if (localReferenceTargetFieldTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTargetField"));

            if (localReferenceTargetField != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReferenceTargetField));
            } else {
                throw new org.apache.axis2.databinding.ADBException("referenceTargetField cannot be null!!");
            }
        }
        if (localReferenceToTracker) {
            if (localReferenceTo != null) {
                for (int i = 0; i < localReferenceTo.length; i++) {

                    if (localReferenceTo[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTo"));
                        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReferenceTo[i]));
                    } else {

                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTo"));
                        elementList.add(null);

                    }

                }
            } else {

                elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTo"));
                elementList.add(null);

            }

        }
        if (localRelationshipNameTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relationshipName"));

            if (localRelationshipName != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRelationshipName));
            } else {
                throw new org.apache.axis2.databinding.ADBException("relationshipName cannot be null!!");
            }
        }
        if (localRelationshipOrderTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relationshipOrder"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRelationshipOrder));
        }
        if (localRestrictedDeleteTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "restrictedDelete"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRestrictedDelete));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "restrictedPicklist"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRestrictedPicklist));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "scale"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localScale));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "soapType"));

        if (localSoapType == null) {
            throw new org.apache.axis2.databinding.ADBException("soapType cannot be null!!");
        }
        elementList.add(localSoapType);
        if (localSortableTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sortable"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSortable));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type"));

        if (localType == null) {
            throw new org.apache.axis2.databinding.ADBException("type cannot be null!!");
        }
        elementList.add(localType);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "unique"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUnique));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "updateable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUpdateable));
        if (localWriteRequiresMasterReadTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "writeRequiresMasterRead"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWriteRequiresMasterRead));
        }

        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(),
                attribList.toArray());

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory {

        /**
         * static method to create the object Precondition: If this object is an element, the current or next start element starts this
         * object and any intervening reader events are ignorable If this object is not an element, it is a complex type and the reader is
         * at the event just after the outer start element Postcondition: If this object is an element, the reader is positioned at its end
         * element If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static Field parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            Field object = new Field();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
                    if (fullTypeName != null) {
                        java.lang.String nsPrefix = null;
                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                        }
                        nsPrefix = nsPrefix == null ? "" : nsPrefix;

                        java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                        if (!"Field".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (Field) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list35 = new java.util.ArrayList();

                java.util.ArrayList list39 = new java.util.ArrayList();

                // code by talend
                while (!reader.isEndElement()
                        || !new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields").equals(reader.getName())) {
                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name").equals(reader.getName())) {

                        java.lang.String content = reader.getElementText();

                        object.setName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        reader.next();

                    } // End of if for expected property start element
                    else if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type").equals(reader.getName())) {

                        object.setType(com.salesforce.soap.partner.FieldType.Factory.parse(reader));

                        reader.next();

                    } // End of if for expected property start element
                    else if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "precision").equals(reader.getName())) {

                        java.lang.String content = reader.getElementText();

                        object.setPrecision(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                        reader.next();

                    } // End of if for expected property start element
                    else if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "nillable").equals(reader.getName())) {

                        java.lang.String content = reader.getElementText();

                        object.setNillable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    } // End of if for expected property start element
                    else if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "length").equals(reader.getName())) {

                        java.lang.String content = reader.getElementText();

                        object.setLength(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                        reader.next();

                    } // End of if for expected property start element
                    else if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValueFormula").equals(reader
                                    .getName())) {

                        java.lang.String content = reader.getElementText();

                        object.setDefaultValueFormula(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        reader.next();

                    } // End of if for expected property start element
                    else {
                        reader.next();
                    }
                }
				// end code by talend

				/*
				 * disable by talend
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "controllerName")
                                .equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "controllerName"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setControllerName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "createable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "createable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCreateable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "custom").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "custom" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustom(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValueFormula").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "defaultValueFormula"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDefaultValueFormula(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultedOnCreate").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "defaultedOnCreate"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDefaultedOnCreate(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "dependentPicklist").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "dependentPicklist"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDependentPicklist(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deprecatedAndHidden").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "deprecatedAndHidden"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDeprecatedAndHidden(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "digits").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "digits" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDigits(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "displayLocationInDecimal").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "displayLocationInDecimal"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDisplayLocationInDecimal(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "encrypted").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "encrypted" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setEncrypted(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "externalId").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "externalId" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setExternalId(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "extraTypeInfo").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "extraTypeInfo"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setExtraTypeInfo(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "filterable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "filterable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setFilterable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "filteredLookupInfo").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setFilteredLookupInfo(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setFilteredLookupInfo(com.salesforce.soap.partner.FilteredLookupInfo.Factory.parse(reader));

                        reader.next();
                    }
                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "groupable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "groupable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setGroupable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "highScaleNumber").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "highScaleNumber"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setHighScaleNumber(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "htmlFormatted").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "htmlFormatted"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setHtmlFormatted(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "idLookup").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "idLookup" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIdLookup(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "inlineHelpText")
                                .equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "inlineHelpText"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setInlineHelpText(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "label").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "label" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setLabel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "length").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "length" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setLength(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "mask").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "mask" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setMask(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "maskType").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "maskType" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setMaskType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "name" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "nameField").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "nameField" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setNameField(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "namePointing").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "namePointing" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setNamePointing(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "nillable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "nillable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setNillable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "permissionable")
                                .equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "permissionable"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPermissionable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "picklistValues")
                                .equals(reader.getName())) {

                    // Process the array and step past its final element's end.

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list35.add(null);
                        reader.next();
                    } else {
                        list35.add(com.salesforce.soap.partner.PicklistEntry.Factory.parse(reader));
                    }
                    // loop until we find a start element that is not part of this array
                    boolean loopDone35 = false;
                    while (!loopDone35) {
                        // We should be at the end element, but make sure
                        while (!reader.isEndElement())
                            reader.next();
                        // Step out of this element
                        reader.next();
                        // Step to next element event.
                        while (!reader.isStartElement() && !reader.isEndElement())
                            reader.next();
                        if (reader.isEndElement()) {
                            // two continuous end elements means we are exiting the xml structure
                            loopDone35 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "picklistValues").equals(reader
                                    .getName())) {

                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list35.add(null);
                                    reader.next();
                                } else {
                                    list35.add(com.salesforce.soap.partner.PicklistEntry.Factory.parse(reader));
                                }
                            } else {
                                loopDone35 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setPicklistValues((com.salesforce.soap.partner.PicklistEntry[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.PicklistEntry.class, list35));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "precision").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "precision" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPrecision(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryByDistance").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "queryByDistance"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setQueryByDistance(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTargetField").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "referenceTargetField"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setReferenceTargetField(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTo").equals(reader.getName())) {

                    // Process the array and step past its final element's end.

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list39.add(null);

                        reader.next();
                    } else {
                        list39.add(reader.getElementText());
                    }
                    // loop until we find a start element that is not part of this array
                    boolean loopDone39 = false;
                    while (!loopDone39) {
                        // Ensure we are at the EndElement
                        while (!reader.isEndElement()) {
                            reader.next();
                        }
                        // Step out of this element
                        reader.next();
                        // Step to next element event.
                        while (!reader.isStartElement() && !reader.isEndElement())
                            reader.next();
                        if (reader.isEndElement()) {
                            // two continuous end elements means we are exiting the xml structure
                            loopDone39 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTo").equals(reader
                                    .getName())) {

                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list39.add(null);

                                    reader.next();
                                } else {
                                    list39.add(reader.getElementText());
                                }
                            } else {
                                loopDone39 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setReferenceTo((java.lang.String[]) list39.toArray(new java.lang.String[list39.size()]));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relationshipName").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "relationshipName"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRelationshipName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relationshipOrder").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "relationshipOrder"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRelationshipOrder(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                    object.setRelationshipOrder(java.lang.Integer.MIN_VALUE);

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "restrictedDelete").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "restrictedDelete"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRestrictedDelete(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "restrictedPicklist").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "restrictedPicklist"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRestrictedPicklist(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "scale").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "scale" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setScale(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "soapType").equals(reader.getName())) {

                    object.setSoapType(com.salesforce.soap.partner.SoapType.Factory.parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sortable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "sortable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSortable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type").equals(reader.getName())) {

                    object.setType(com.salesforce.soap.partner.FieldType.Factory.parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "unique").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "unique" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setUnique(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "updateable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "updateable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setUpdateable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "writeRequiresMasterRead").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "writeRequiresMasterRead"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setWriteRequiresMasterRead(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }
				*/

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement())
                    // A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }// end of factory class

}
