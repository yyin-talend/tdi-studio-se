/**
 * DescribeSObjectResult.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * DescribeSObjectResult bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class DescribeSObjectResult implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = DescribeSObjectResult Namespace URI = urn:partner.soap.sforce.com
     * Namespace Prefix = ns1
     */

    /**
     * field for ActionOverrides This was an Array!
     */

    protected com.salesforce.soap.partner.ActionOverride[] localActionOverrides;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localActionOverridesTracker = false;

    public boolean isActionOverridesSpecified() {
        return localActionOverridesTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ActionOverride[]
     */
    public com.salesforce.soap.partner.ActionOverride[] getActionOverrides() {
        return localActionOverrides;
    }

    /**
     * validate the array for ActionOverrides
     */
    protected void validateActionOverrides(com.salesforce.soap.partner.ActionOverride[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param ActionOverrides
     */
    public void setActionOverrides(com.salesforce.soap.partner.ActionOverride[] param) {

        validateActionOverrides(param);

        localActionOverridesTracker = true;

        this.localActionOverrides = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.ActionOverride
     */
    public void addActionOverrides(com.salesforce.soap.partner.ActionOverride param) {
        if (localActionOverrides == null) {
            localActionOverrides = new com.salesforce.soap.partner.ActionOverride[] {};
        }

        // update the setting tracker
        localActionOverridesTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localActionOverrides);
        list.add(param);
        this.localActionOverrides = (com.salesforce.soap.partner.ActionOverride[]) list
                .toArray(new com.salesforce.soap.partner.ActionOverride[list.size()]);

    }

    /**
     * field for Activateable
     */

    protected boolean localActivateable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getActivateable() {
        return localActivateable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Activateable
     */
    public void setActivateable(boolean param) {

        this.localActivateable = param;

    }

    /**
     * field for ChildRelationships This was an Array!
     */

    protected com.salesforce.soap.partner.ChildRelationship[] localChildRelationships;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localChildRelationshipsTracker = false;

    public boolean isChildRelationshipsSpecified() {
        return localChildRelationshipsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ChildRelationship[]
     */
    public com.salesforce.soap.partner.ChildRelationship[] getChildRelationships() {
        return localChildRelationships;
    }

    /**
     * validate the array for ChildRelationships
     */
    protected void validateChildRelationships(com.salesforce.soap.partner.ChildRelationship[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param ChildRelationships
     */
    public void setChildRelationships(com.salesforce.soap.partner.ChildRelationship[] param) {

        validateChildRelationships(param);

        localChildRelationshipsTracker = param != null;

        this.localChildRelationships = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.ChildRelationship
     */
    public void addChildRelationships(com.salesforce.soap.partner.ChildRelationship param) {
        if (localChildRelationships == null) {
            localChildRelationships = new com.salesforce.soap.partner.ChildRelationship[] {};
        }

        // update the setting tracker
        localChildRelationshipsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localChildRelationships);
        list.add(param);
        this.localChildRelationships = (com.salesforce.soap.partner.ChildRelationship[]) list
                .toArray(new com.salesforce.soap.partner.ChildRelationship[list.size()]);

    }

    /**
     * field for CompactLayoutable
     */

    protected boolean localCompactLayoutable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getCompactLayoutable() {
        return localCompactLayoutable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param CompactLayoutable
     */
    public void setCompactLayoutable(boolean param) {

        this.localCompactLayoutable = param;

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
     * field for CustomSetting
     */

    protected boolean localCustomSetting;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getCustomSetting() {
        return localCustomSetting;
    }

    /**
     * Auto generated setter method
     * 
     * @param param CustomSetting
     */
    public void setCustomSetting(boolean param) {

        this.localCustomSetting = param;

    }

    /**
     * field for Deletable
     */

    protected boolean localDeletable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getDeletable() {
        return localDeletable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Deletable
     */
    public void setDeletable(boolean param) {

        this.localDeletable = param;

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
     * field for FeedEnabled
     */

    protected boolean localFeedEnabled;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getFeedEnabled() {
        return localFeedEnabled;
    }

    /**
     * Auto generated setter method
     * 
     * @param param FeedEnabled
     */
    public void setFeedEnabled(boolean param) {

        this.localFeedEnabled = param;

    }

    /**
     * field for Fields This was an Array!
     */

    protected com.salesforce.soap.partner.Field[] localFields;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localFieldsTracker = false;

    public boolean isFieldsSpecified() {
        return localFieldsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.Field[]
     */
    public com.salesforce.soap.partner.Field[] getFields() {
        return localFields;
    }

    /**
     * validate the array for Fields
     */
    protected void validateFields(com.salesforce.soap.partner.Field[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param Fields
     */
    public void setFields(com.salesforce.soap.partner.Field[] param) {

        validateFields(param);

        localFieldsTracker = true;

        this.localFields = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.Field
     */
    public void addFields(com.salesforce.soap.partner.Field param) {
        if (localFields == null) {
            localFields = new com.salesforce.soap.partner.Field[] {};
        }

        // update the setting tracker
        localFieldsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localFields);
        list.add(param);
        this.localFields = (com.salesforce.soap.partner.Field[]) list.toArray(new com.salesforce.soap.partner.Field[list.size()]);

    }

    /**
     * field for KeyPrefix
     */

    protected java.lang.String localKeyPrefix;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getKeyPrefix() {
        return localKeyPrefix;
    }

    /**
     * Auto generated setter method
     * 
     * @param param KeyPrefix
     */
    public void setKeyPrefix(java.lang.String param) {

        this.localKeyPrefix = param;

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
     * field for LabelPlural
     */

    protected java.lang.String localLabelPlural;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getLabelPlural() {
        return localLabelPlural;
    }

    /**
     * Auto generated setter method
     * 
     * @param param LabelPlural
     */
    public void setLabelPlural(java.lang.String param) {

        this.localLabelPlural = param;

    }

    /**
     * field for Layoutable
     */

    protected boolean localLayoutable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getLayoutable() {
        return localLayoutable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Layoutable
     */
    public void setLayoutable(boolean param) {

        this.localLayoutable = param;

    }

    /**
     * field for Mergeable
     */

    protected boolean localMergeable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getMergeable() {
        return localMergeable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Mergeable
     */
    public void setMergeable(boolean param) {

        this.localMergeable = param;

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
     * field for NamedLayoutInfos This was an Array!
     */

    protected com.salesforce.soap.partner.NamedLayoutInfo[] localNamedLayoutInfos;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localNamedLayoutInfosTracker = false;

    public boolean isNamedLayoutInfosSpecified() {
        return localNamedLayoutInfosTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.NamedLayoutInfo[]
     */
    public com.salesforce.soap.partner.NamedLayoutInfo[] getNamedLayoutInfos() {
        return localNamedLayoutInfos;
    }

    /**
     * validate the array for NamedLayoutInfos
     */
    protected void validateNamedLayoutInfos(com.salesforce.soap.partner.NamedLayoutInfo[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param NamedLayoutInfos
     */
    public void setNamedLayoutInfos(com.salesforce.soap.partner.NamedLayoutInfo[] param) {

        validateNamedLayoutInfos(param);

        localNamedLayoutInfosTracker = param != null;

        this.localNamedLayoutInfos = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.NamedLayoutInfo
     */
    public void addNamedLayoutInfos(com.salesforce.soap.partner.NamedLayoutInfo param) {
        if (localNamedLayoutInfos == null) {
            localNamedLayoutInfos = new com.salesforce.soap.partner.NamedLayoutInfo[] {};
        }

        // update the setting tracker
        localNamedLayoutInfosTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localNamedLayoutInfos);
        list.add(param);
        this.localNamedLayoutInfos = (com.salesforce.soap.partner.NamedLayoutInfo[]) list
                .toArray(new com.salesforce.soap.partner.NamedLayoutInfo[list.size()]);

    }

    /**
     * field for Queryable
     */

    protected boolean localQueryable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getQueryable() {
        return localQueryable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Queryable
     */
    public void setQueryable(boolean param) {

        this.localQueryable = param;

    }

    /**
     * field for RecordTypeInfos This was an Array!
     */

    protected com.salesforce.soap.partner.RecordTypeInfo[] localRecordTypeInfos;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localRecordTypeInfosTracker = false;

    public boolean isRecordTypeInfosSpecified() {
        return localRecordTypeInfosTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.RecordTypeInfo[]
     */
    public com.salesforce.soap.partner.RecordTypeInfo[] getRecordTypeInfos() {
        return localRecordTypeInfos;
    }

    /**
     * validate the array for RecordTypeInfos
     */
    protected void validateRecordTypeInfos(com.salesforce.soap.partner.RecordTypeInfo[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param RecordTypeInfos
     */
    public void setRecordTypeInfos(com.salesforce.soap.partner.RecordTypeInfo[] param) {

        validateRecordTypeInfos(param);

        localRecordTypeInfosTracker = param != null;

        this.localRecordTypeInfos = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.RecordTypeInfo
     */
    public void addRecordTypeInfos(com.salesforce.soap.partner.RecordTypeInfo param) {
        if (localRecordTypeInfos == null) {
            localRecordTypeInfos = new com.salesforce.soap.partner.RecordTypeInfo[] {};
        }

        // update the setting tracker
        localRecordTypeInfosTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localRecordTypeInfos);
        list.add(param);
        this.localRecordTypeInfos = (com.salesforce.soap.partner.RecordTypeInfo[]) list
                .toArray(new com.salesforce.soap.partner.RecordTypeInfo[list.size()]);

    }

    /**
     * field for Replicateable
     */

    protected boolean localReplicateable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getReplicateable() {
        return localReplicateable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Replicateable
     */
    public void setReplicateable(boolean param) {

        this.localReplicateable = param;

    }

    /**
     * field for Retrieveable
     */

    protected boolean localRetrieveable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getRetrieveable() {
        return localRetrieveable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Retrieveable
     */
    public void setRetrieveable(boolean param) {

        this.localRetrieveable = param;

    }

    /**
     * field for SearchLayoutable
     */

    protected boolean localSearchLayoutable;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localSearchLayoutableTracker = false;

    public boolean isSearchLayoutableSpecified() {
        return localSearchLayoutableTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getSearchLayoutable() {
        return localSearchLayoutable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param SearchLayoutable
     */
    public void setSearchLayoutable(boolean param) {

        // setting primitive attribute tracker to true
        localSearchLayoutableTracker = true;

        this.localSearchLayoutable = param;

    }

    /**
     * field for Searchable
     */

    protected boolean localSearchable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getSearchable() {
        return localSearchable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Searchable
     */
    public void setSearchable(boolean param) {

        this.localSearchable = param;

    }

    /**
     * field for Triggerable
     */

    protected boolean localTriggerable;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localTriggerableTracker = false;

    public boolean isTriggerableSpecified() {
        return localTriggerableTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getTriggerable() {
        return localTriggerable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Triggerable
     */
    public void setTriggerable(boolean param) {

        // setting primitive attribute tracker to true
        localTriggerableTracker = true;

        this.localTriggerable = param;

    }

    /**
     * field for Undeletable
     */

    protected boolean localUndeletable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getUndeletable() {
        return localUndeletable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Undeletable
     */
    public void setUndeletable(boolean param) {

        this.localUndeletable = param;

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
     * field for UrlDetail
     */

    protected java.lang.String localUrlDetail;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getUrlDetail() {
        return localUrlDetail;
    }

    /**
     * Auto generated setter method
     * 
     * @param param UrlDetail
     */
    public void setUrlDetail(java.lang.String param) {

        this.localUrlDetail = param;

    }

    /**
     * field for UrlEdit
     */

    protected java.lang.String localUrlEdit;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getUrlEdit() {
        return localUrlEdit;
    }

    /**
     * Auto generated setter method
     * 
     * @param param UrlEdit
     */
    public void setUrlEdit(java.lang.String param) {

        this.localUrlEdit = param;

    }

    /**
     * field for UrlNew
     */

    protected java.lang.String localUrlNew;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getUrlNew() {
        return localUrlNew;
    }

    /**
     * Auto generated setter method
     * 
     * @param param UrlNew
     */
    public void setUrlNew(java.lang.String param) {

        this.localUrlNew = param;

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
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":DescribeSObjectResult", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "DescribeSObjectResult", xmlWriter);
            }

        }
        if (localActionOverridesTracker) {
            if (localActionOverrides != null) {
                for (int i = 0; i < localActionOverrides.length; i++) {
                    if (localActionOverrides[i] != null) {
                        localActionOverrides[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                "actionOverrides"), xmlWriter);
                    } else {

                        writeStartElement(null, "urn:partner.soap.sforce.com", "actionOverrides", xmlWriter);

                        // write the nil attribute
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();

                    }

                }
            } else {

                writeStartElement(null, "urn:partner.soap.sforce.com", "actionOverrides", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();

            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "activateable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("activateable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActivateable));
        }

        xmlWriter.writeEndElement();
        if (localChildRelationshipsTracker) {
            if (localChildRelationships != null) {
                for (int i = 0; i < localChildRelationships.length; i++) {
                    if (localChildRelationships[i] != null) {
                        localChildRelationships[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                "childRelationships"), xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("childRelationships cannot be null!!");

            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "compactLayoutable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("compactLayoutable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCompactLayoutable));
        }

        xmlWriter.writeEndElement();

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

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "customSetting", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("customSetting cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustomSetting));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "deletable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("deletable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeletable));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "deprecatedAndHidden", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("deprecatedAndHidden cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeprecatedAndHidden));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "feedEnabled", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("feedEnabled cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFeedEnabled));
        }

        xmlWriter.writeEndElement();
        if (localFieldsTracker) {
            if (localFields != null) {
                for (int i = 0; i < localFields.length; i++) {
                    if (localFields[i] != null) {
                        localFields[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields"),
                                xmlWriter);
                    } else {

                        writeStartElement(null, "urn:partner.soap.sforce.com", "fields", xmlWriter);

                        // write the nil attribute
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();

                    }

                }
            } else {

                writeStartElement(null, "urn:partner.soap.sforce.com", "fields", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();

            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "keyPrefix", xmlWriter);

        if (localKeyPrefix == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localKeyPrefix);

        }

        xmlWriter.writeEndElement();

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
        writeStartElement(null, namespace, "labelPlural", xmlWriter);

        if (localLabelPlural == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("labelPlural cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localLabelPlural);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "layoutable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("layoutable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLayoutable));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "mergeable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("mergeable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMergeable));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "name", xmlWriter);

        if (localName == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("name cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localName);

        }

        xmlWriter.writeEndElement();
        if (localNamedLayoutInfosTracker) {
            if (localNamedLayoutInfos != null) {
                for (int i = 0; i < localNamedLayoutInfos.length; i++) {
                    if (localNamedLayoutInfos[i] != null) {
                        localNamedLayoutInfos[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                "namedLayoutInfos"), xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("namedLayoutInfos cannot be null!!");

            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "queryable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("queryable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localQueryable));
        }

        xmlWriter.writeEndElement();
        if (localRecordTypeInfosTracker) {
            if (localRecordTypeInfos != null) {
                for (int i = 0; i < localRecordTypeInfos.length; i++) {
                    if (localRecordTypeInfos[i] != null) {
                        localRecordTypeInfos[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                "recordTypeInfos"), xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("recordTypeInfos cannot be null!!");

            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "replicateable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("replicateable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReplicateable));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "retrieveable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("retrieveable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRetrieveable));
        }

        xmlWriter.writeEndElement();
        if (localSearchLayoutableTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "searchLayoutable", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("searchLayoutable cannot be null!!");

            } else {
                xmlWriter
                        .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSearchLayoutable));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "searchable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("searchable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSearchable));
        }

        xmlWriter.writeEndElement();
        if (localTriggerableTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "triggerable", xmlWriter);

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("triggerable cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTriggerable));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "undeletable", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("undeletable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUndeletable));
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

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "urlDetail", xmlWriter);

        if (localUrlDetail == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localUrlDetail);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "urlEdit", xmlWriter);

        if (localUrlEdit == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localUrlEdit);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "urlNew", xmlWriter);

        if (localUrlNew == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localUrlNew);

        }

        xmlWriter.writeEndElement();

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

        if (localActionOverridesTracker) {
            if (localActionOverrides != null) {
                for (int i = 0; i < localActionOverrides.length; i++) {

                    if (localActionOverrides[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actionOverrides"));
                        elementList.add(localActionOverrides[i]);
                    } else {

                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actionOverrides"));
                        elementList.add(null);

                    }

                }
            } else {

                elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actionOverrides"));
                elementList.add(localActionOverrides);

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "activateable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localActivateable));
        if (localChildRelationshipsTracker) {
            if (localChildRelationships != null) {
                for (int i = 0; i < localChildRelationships.length; i++) {

                    if (localChildRelationships[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "childRelationships"));
                        elementList.add(localChildRelationships[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("childRelationships cannot be null!!");

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "compactLayoutable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCompactLayoutable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "createable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreateable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "custom"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustom));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "customSetting"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustomSetting));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deletable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeletable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deprecatedAndHidden"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeprecatedAndHidden));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedEnabled"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFeedEnabled));
        if (localFieldsTracker) {
            if (localFields != null) {
                for (int i = 0; i < localFields.length; i++) {

                    if (localFields[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields"));
                        elementList.add(localFields[i]);
                    } else {

                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields"));
                        elementList.add(null);

                    }

                }
            } else {

                elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields"));
                elementList.add(localFields);

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "keyPrefix"));

        elementList.add(localKeyPrefix == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localKeyPrefix));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "label"));

        if (localLabel != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLabel));
        } else {
            throw new org.apache.axis2.databinding.ADBException("label cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "labelPlural"));

        if (localLabelPlural != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLabelPlural));
        } else {
            throw new org.apache.axis2.databinding.ADBException("labelPlural cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "layoutable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLayoutable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "mergeable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMergeable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name"));

        if (localName != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localName));
        } else {
            throw new org.apache.axis2.databinding.ADBException("name cannot be null!!");
        }
        if (localNamedLayoutInfosTracker) {
            if (localNamedLayoutInfos != null) {
                for (int i = 0; i < localNamedLayoutInfos.length; i++) {

                    if (localNamedLayoutInfos[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "namedLayoutInfos"));
                        elementList.add(localNamedLayoutInfos[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("namedLayoutInfos cannot be null!!");

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localQueryable));
        if (localRecordTypeInfosTracker) {
            if (localRecordTypeInfos != null) {
                for (int i = 0; i < localRecordTypeInfos.length; i++) {

                    if (localRecordTypeInfos[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeInfos"));
                        elementList.add(localRecordTypeInfos[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("recordTypeInfos cannot be null!!");

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "replicateable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReplicateable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieveable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRetrieveable));
        if (localSearchLayoutableTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "searchLayoutable"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSearchLayoutable));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "searchable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSearchable));
        if (localTriggerableTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "triggerable"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTriggerable));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undeletable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUndeletable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "updateable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUpdateable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "urlDetail"));

        elementList.add(localUrlDetail == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localUrlDetail));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "urlEdit"));

        elementList.add(localUrlEdit == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localUrlEdit));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "urlNew"));

        elementList.add(localUrlNew == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localUrlNew));

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
        public static DescribeSObjectResult parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            DescribeSObjectResult object = new DescribeSObjectResult();

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

                        if (!"DescribeSObjectResult".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (DescribeSObjectResult) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(
                                    nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list1 = new java.util.ArrayList();

                java.util.ArrayList list3 = new java.util.ArrayList();

                java.util.ArrayList list11 = new java.util.ArrayList();

                java.util.ArrayList list18 = new java.util.ArrayList();

                java.util.ArrayList list20 = new java.util.ArrayList();

                // code by bchen
                while (!reader.isEndElement()
                        || !new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result").equals(reader.getName())) {
                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields").equals(reader.getName())) {

                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                            list11.add(null);
                            reader.next();
                        } else {
                            list11.add(com.salesforce.soap.partner.Field.Factory.parse(reader));
                        }
                        // loop until we find a start element that is not part of this array
                        boolean loopDone9 = false;
                        while (!loopDone9) {
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
                                loopDone9 = true;
                            } else {
                                if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields").equals(reader
                                        .getName())) {

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                        list11.add(null);
                                        reader.next();
                                    } else {
                                        list11.add(com.salesforce.soap.partner.Field.Factory.parse(reader));
                                    }
                                } else {
                                    loopDone9 = true;
                                }
                            }
                        }
                        // call the converter utility to convert and set the array

                        object.setFields((com.salesforce.soap.partner.Field[]) org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToArray(com.salesforce.soap.partner.Field.class, list11));

                    } // End of if for expected property start element

                    else {
                        reader.next();
                    }

                }
                
                
                // end code by bchen

                /*
                 
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actionOverrides").equals(reader
                                .getName())) {

                    // Process the array and step past its final element's end.

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list1.add(null);
                        reader.next();
                    } else {
                        list1.add(com.salesforce.soap.partner.ActionOverride.Factory.parse(reader));
                    }
                    // loop until we find a start element that is not part of this array
                    boolean loopDone1 = false;
                    while (!loopDone1) {
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
                            loopDone1 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actionOverrides").equals(reader
                                    .getName())) {

                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list1.add(null);
                                    reader.next();
                                } else {
                                    list1.add(com.salesforce.soap.partner.ActionOverride.Factory.parse(reader));
                                }
                            } else {
                                loopDone1 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setActionOverrides((com.salesforce.soap.partner.ActionOverride[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.ActionOverride.class, list1));

                }  // End of if for expected property start element

                else {

                }
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "activateable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "activateable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setActivateable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "childRelationships").equals(reader
                                .getName())) {

                    // Process the array and step past its final element's end.
                    list3.add(com.salesforce.soap.partner.ChildRelationship.Factory.parse(reader));

                    // loop until we find a start element that is not part of this array
                    boolean loopDone3 = false;
                    while (!loopDone3) {
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
                            loopDone3 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "childRelationships").equals(reader
                                    .getName())) {
                                list3.add(com.salesforce.soap.partner.ChildRelationship.Factory.parse(reader));

                            } else {
                                loopDone3 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setChildRelationships((com.salesforce.soap.partner.ChildRelationship[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.ChildRelationship.class, list3));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "compactLayoutable").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "compactLayoutable"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCompactLayoutable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "customSetting").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "customSetting"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCustomSetting(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deletable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "deletable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDeletable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedEnabled").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "feedEnabled" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setFeedEnabled(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields").equals(reader.getName())) {

                    // Process the array and step past its final element's end.

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list11.add(null);
                        reader.next();
                    } else {
                        list11.add(com.salesforce.soap.partner.Field.Factory.parse(reader));
                    }
                    // loop until we find a start element that is not part of this array
                    boolean loopDone11 = false;
                    while (!loopDone11) {
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
                            loopDone11 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields").equals(reader.getName())) {

                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list11.add(null);
                                    reader.next();
                                } else {
                                    list11.add(com.salesforce.soap.partner.Field.Factory.parse(reader));
                                }
                            } else {
                                loopDone11 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setFields((com.salesforce.soap.partner.Field[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.Field.class, list11));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "keyPrefix").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setKeyPrefix(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    } else {

                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "labelPlural").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "labelPlural" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setLabelPlural(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "layoutable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "layoutable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setLayoutable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "mergeable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "mergeable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setMergeable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "namedLayoutInfos").equals(reader
                                .getName())) {

                    // Process the array and step past its final element's end.
                    list18.add(com.salesforce.soap.partner.NamedLayoutInfo.Factory.parse(reader));

                    // loop until we find a start element that is not part of this array
                    boolean loopDone18 = false;
                    while (!loopDone18) {
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
                            loopDone18 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "namedLayoutInfos").equals(reader
                                    .getName())) {
                                list18.add(com.salesforce.soap.partner.NamedLayoutInfo.Factory.parse(reader));

                            } else {
                                loopDone18 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setNamedLayoutInfos((com.salesforce.soap.partner.NamedLayoutInfo[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.NamedLayoutInfo.class, list18));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "queryable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setQueryable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeInfos").equals(reader
                                .getName())) {

                    // Process the array and step past its final element's end.
                    list20.add(com.salesforce.soap.partner.RecordTypeInfo.Factory.parse(reader));

                    // loop until we find a start element that is not part of this array
                    boolean loopDone20 = false;
                    while (!loopDone20) {
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
                            loopDone20 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeInfos").equals(reader
                                    .getName())) {
                                list20.add(com.salesforce.soap.partner.RecordTypeInfo.Factory.parse(reader));

                            } else {
                                loopDone20 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setRecordTypeInfos((com.salesforce.soap.partner.RecordTypeInfo[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.RecordTypeInfo.class, list20));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "replicateable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "replicateable"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setReplicateable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieveable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "retrieveable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRetrieveable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "searchLayoutable").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "searchLayoutable"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSearchLayoutable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "searchable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "searchable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSearchable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "triggerable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "triggerable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setTriggerable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undeletable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "undeletable" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setUndeletable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "urlDetail").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setUrlDetail(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    } else {

                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "urlEdit").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setUrlEdit(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    } else {

                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "urlNew").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setUrlNew(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    } else {

                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
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
