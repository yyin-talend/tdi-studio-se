/**
 * DescribeQuickActionResult.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * DescribeQuickActionResult bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class DescribeQuickActionResult implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = DescribeQuickActionResult Namespace URI =
     * urn:partner.soap.sforce.com Namespace Prefix = ns1
     */

    /**
     * field for AccessLevelRequired
     */

    protected com.salesforce.soap.partner.ShareAccessLevel localAccessLevelRequired;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ShareAccessLevel
     */
    public com.salesforce.soap.partner.ShareAccessLevel getAccessLevelRequired() {
        return localAccessLevelRequired;
    }

    /**
     * Auto generated setter method
     * 
     * @param param AccessLevelRequired
     */
    public void setAccessLevelRequired(com.salesforce.soap.partner.ShareAccessLevel param) {

        this.localAccessLevelRequired = param;

    }

    /**
     * field for CanvasApplicationId
     */

    protected com.salesforce.soap.partner.ID localCanvasApplicationId;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getCanvasApplicationId() {
        return localCanvasApplicationId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param CanvasApplicationId
     */
    public void setCanvasApplicationId(com.salesforce.soap.partner.ID param) {

        this.localCanvasApplicationId = param;

    }

    /**
     * field for CanvasApplicationName
     */

    protected java.lang.String localCanvasApplicationName;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getCanvasApplicationName() {
        return localCanvasApplicationName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param CanvasApplicationName
     */
    public void setCanvasApplicationName(java.lang.String param) {

        this.localCanvasApplicationName = param;

    }

    /**
     * field for Colors This was an Array!
     */

    protected com.salesforce.soap.partner.DescribeColor[] localColors;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localColorsTracker = false;

    public boolean isColorsSpecified() {
        return localColorsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeColor[]
     */
    public com.salesforce.soap.partner.DescribeColor[] getColors() {
        return localColors;
    }

    /**
     * validate the array for Colors
     */
    protected void validateColors(com.salesforce.soap.partner.DescribeColor[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param Colors
     */
    public void setColors(com.salesforce.soap.partner.DescribeColor[] param) {

        validateColors(param);

        localColorsTracker = param != null;

        this.localColors = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.DescribeColor
     */
    public void addColors(com.salesforce.soap.partner.DescribeColor param) {
        if (localColors == null) {
            localColors = new com.salesforce.soap.partner.DescribeColor[] {};
        }

        // update the setting tracker
        localColorsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localColors);
        list.add(param);
        this.localColors = (com.salesforce.soap.partner.DescribeColor[]) list
                .toArray(new com.salesforce.soap.partner.DescribeColor[list.size()]);

    }

    /**
     * field for ContextSobjectType
     */

    protected java.lang.String localContextSobjectType;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getContextSobjectType() {
        return localContextSobjectType;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ContextSobjectType
     */
    public void setContextSobjectType(java.lang.String param) {

        this.localContextSobjectType = param;

    }

    /**
     * field for DefaultValues This was an Array!
     */

    protected com.salesforce.soap.partner.DescribeQuickActionDefaultValue[] localDefaultValues;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localDefaultValuesTracker = false;

    public boolean isDefaultValuesSpecified() {
        return localDefaultValuesTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeQuickActionDefaultValue[]
     */
    public com.salesforce.soap.partner.DescribeQuickActionDefaultValue[] getDefaultValues() {
        return localDefaultValues;
    }

    /**
     * validate the array for DefaultValues
     */
    protected void validateDefaultValues(com.salesforce.soap.partner.DescribeQuickActionDefaultValue[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param DefaultValues
     */
    public void setDefaultValues(com.salesforce.soap.partner.DescribeQuickActionDefaultValue[] param) {

        validateDefaultValues(param);

        localDefaultValuesTracker = true;

        this.localDefaultValues = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.DescribeQuickActionDefaultValue
     */
    public void addDefaultValues(com.salesforce.soap.partner.DescribeQuickActionDefaultValue param) {
        if (localDefaultValues == null) {
            localDefaultValues = new com.salesforce.soap.partner.DescribeQuickActionDefaultValue[] {};
        }

        // update the setting tracker
        localDefaultValuesTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localDefaultValues);
        list.add(param);
        this.localDefaultValues = (com.salesforce.soap.partner.DescribeQuickActionDefaultValue[]) list
                .toArray(new com.salesforce.soap.partner.DescribeQuickActionDefaultValue[list.size()]);

    }

    /**
     * field for Height
     */

    protected int localHeight;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getHeight() {
        return localHeight;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Height
     */
    public void setHeight(int param) {

        this.localHeight = param;

    }

    /**
     * field for IconName
     */

    protected java.lang.String localIconName;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getIconName() {
        return localIconName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param IconName
     */
    public void setIconName(java.lang.String param) {

        this.localIconName = param;

    }

    /**
     * field for IconUrl
     */

    protected java.lang.String localIconUrl;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getIconUrl() {
        return localIconUrl;
    }

    /**
     * Auto generated setter method
     * 
     * @param param IconUrl
     */
    public void setIconUrl(java.lang.String param) {

        this.localIconUrl = param;

    }

    /**
     * field for Icons This was an Array!
     */

    protected com.salesforce.soap.partner.DescribeIcon[] localIcons;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localIconsTracker = false;

    public boolean isIconsSpecified() {
        return localIconsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeIcon[]
     */
    public com.salesforce.soap.partner.DescribeIcon[] getIcons() {
        return localIcons;
    }

    /**
     * validate the array for Icons
     */
    protected void validateIcons(com.salesforce.soap.partner.DescribeIcon[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param Icons
     */
    public void setIcons(com.salesforce.soap.partner.DescribeIcon[] param) {

        validateIcons(param);

        localIconsTracker = param != null;

        this.localIcons = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.DescribeIcon
     */
    public void addIcons(com.salesforce.soap.partner.DescribeIcon param) {
        if (localIcons == null) {
            localIcons = new com.salesforce.soap.partner.DescribeIcon[] {};
        }

        // update the setting tracker
        localIconsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localIcons);
        list.add(param);
        this.localIcons = (com.salesforce.soap.partner.DescribeIcon[]) list
                .toArray(new com.salesforce.soap.partner.DescribeIcon[list.size()]);

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
     * field for Layout
     */

    protected com.salesforce.soap.partner.DescribeLayoutSection localLayout;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeLayoutSection
     */
    public com.salesforce.soap.partner.DescribeLayoutSection getLayout() {
        return localLayout;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Layout
     */
    public void setLayout(com.salesforce.soap.partner.DescribeLayoutSection param) {

        this.localLayout = param;

    }

    /**
     * field for MiniIconUrl
     */

    protected java.lang.String localMiniIconUrl;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getMiniIconUrl() {
        return localMiniIconUrl;
    }

    /**
     * Auto generated setter method
     * 
     * @param param MiniIconUrl
     */
    public void setMiniIconUrl(java.lang.String param) {

        this.localMiniIconUrl = param;

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
     * field for TargetParentField
     */

    protected java.lang.String localTargetParentField;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getTargetParentField() {
        return localTargetParentField;
    }

    /**
     * Auto generated setter method
     * 
     * @param param TargetParentField
     */
    public void setTargetParentField(java.lang.String param) {

        this.localTargetParentField = param;

    }

    /**
     * field for TargetRecordTypeId
     */

    protected com.salesforce.soap.partner.ID localTargetRecordTypeId;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getTargetRecordTypeId() {
        return localTargetRecordTypeId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param TargetRecordTypeId
     */
    public void setTargetRecordTypeId(com.salesforce.soap.partner.ID param) {

        this.localTargetRecordTypeId = param;

    }

    /**
     * field for TargetSobjectType
     */

    protected java.lang.String localTargetSobjectType;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getTargetSobjectType() {
        return localTargetSobjectType;
    }

    /**
     * Auto generated setter method
     * 
     * @param param TargetSobjectType
     */
    public void setTargetSobjectType(java.lang.String param) {

        this.localTargetSobjectType = param;

    }

    /**
     * field for Type
     */

    protected java.lang.String localType;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getType() {
        return localType;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Type
     */
    public void setType(java.lang.String param) {

        this.localType = param;

    }

    /**
     * field for VisualforcePageName
     */

    protected java.lang.String localVisualforcePageName;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getVisualforcePageName() {
        return localVisualforcePageName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param VisualforcePageName
     */
    public void setVisualforcePageName(java.lang.String param) {

        this.localVisualforcePageName = param;

    }

    /**
     * field for Width
     */

    protected int localWidth;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getWidth() {
        return localWidth;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Width
     */
    public void setWidth(int param) {

        this.localWidth = param;

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
                        + ":DescribeQuickActionResult", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "DescribeQuickActionResult", xmlWriter);
            }

        }

        if (localAccessLevelRequired == null) {

            writeStartElement(null, "urn:partner.soap.sforce.com", "accessLevelRequired", xmlWriter);

            // write the nil attribute
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            xmlWriter.writeEndElement();
        } else {
            localAccessLevelRequired.serialize(
                    new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "accessLevelRequired"), xmlWriter);
        }

        if (localCanvasApplicationId == null) {

            writeStartElement(null, "urn:partner.soap.sforce.com", "canvasApplicationId", xmlWriter);

            // write the nil attribute
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            xmlWriter.writeEndElement();
        } else {
            localCanvasApplicationId.serialize(
                    new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "canvasApplicationId"), xmlWriter);
        }

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "canvasApplicationName", xmlWriter);

        if (localCanvasApplicationName == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localCanvasApplicationName);

        }

        xmlWriter.writeEndElement();
        if (localColorsTracker) {
            if (localColors != null) {
                for (int i = 0; i < localColors.length; i++) {
                    if (localColors[i] != null) {
                        localColors[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "colors"),
                                xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("colors cannot be null!!");

            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "contextSobjectType", xmlWriter);

        if (localContextSobjectType == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localContextSobjectType);

        }

        xmlWriter.writeEndElement();
        if (localDefaultValuesTracker) {
            if (localDefaultValues != null) {
                for (int i = 0; i < localDefaultValues.length; i++) {
                    if (localDefaultValues[i] != null) {
                        localDefaultValues[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                "defaultValues"), xmlWriter);
                    } else {

                        writeStartElement(null, "urn:partner.soap.sforce.com", "defaultValues", xmlWriter);

                        // write the nil attribute
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();

                    }

                }
            } else {

                writeStartElement(null, "urn:partner.soap.sforce.com", "defaultValues", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();

            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "height", xmlWriter);

        if (localHeight == java.lang.Integer.MIN_VALUE) {

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHeight));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "iconName", xmlWriter);

        if (localIconName == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localIconName);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "iconUrl", xmlWriter);

        if (localIconUrl == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localIconUrl);

        }

        xmlWriter.writeEndElement();
        if (localIconsTracker) {
            if (localIcons != null) {
                for (int i = 0; i < localIcons.length; i++) {
                    if (localIcons[i] != null) {
                        localIcons[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "icons"), xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("icons cannot be null!!");

            }
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

        if (localLayout == null) {

            writeStartElement(null, "urn:partner.soap.sforce.com", "layout", xmlWriter);

            // write the nil attribute
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            xmlWriter.writeEndElement();
        } else {
            localLayout.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "layout"), xmlWriter);
        }

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "miniIconUrl", xmlWriter);

        if (localMiniIconUrl == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localMiniIconUrl);

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

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "targetParentField", xmlWriter);

        if (localTargetParentField == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localTargetParentField);

        }

        xmlWriter.writeEndElement();

        if (localTargetRecordTypeId == null) {

            writeStartElement(null, "urn:partner.soap.sforce.com", "targetRecordTypeId", xmlWriter);

            // write the nil attribute
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            xmlWriter.writeEndElement();
        } else {
            localTargetRecordTypeId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "targetRecordTypeId"),
                    xmlWriter);
        }

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "targetSobjectType", xmlWriter);

        if (localTargetSobjectType == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localTargetSobjectType);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "type", xmlWriter);

        if (localType == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("type cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localType);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "visualforcePageName", xmlWriter);

        if (localVisualforcePageName == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localVisualforcePageName);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "width", xmlWriter);

        if (localWidth == java.lang.Integer.MIN_VALUE) {

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWidth));
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

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "accessLevelRequired"));

        elementList.add(localAccessLevelRequired == null ? null : localAccessLevelRequired);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "canvasApplicationId"));

        elementList.add(localCanvasApplicationId == null ? null : localCanvasApplicationId);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "canvasApplicationName"));

        elementList.add(localCanvasApplicationName == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localCanvasApplicationName));
        if (localColorsTracker) {
            if (localColors != null) {
                for (int i = 0; i < localColors.length; i++) {

                    if (localColors[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "colors"));
                        elementList.add(localColors[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("colors cannot be null!!");

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contextSobjectType"));

        elementList.add(localContextSobjectType == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localContextSobjectType));
        if (localDefaultValuesTracker) {
            if (localDefaultValues != null) {
                for (int i = 0; i < localDefaultValues.length; i++) {

                    if (localDefaultValues[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValues"));
                        elementList.add(localDefaultValues[i]);
                    } else {

                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValues"));
                        elementList.add(null);

                    }

                }
            } else {

                elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValues"));
                elementList.add(localDefaultValues);

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "height"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHeight));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "iconName"));

        elementList.add(localIconName == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localIconName));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "iconUrl"));

        elementList.add(localIconUrl == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localIconUrl));
        if (localIconsTracker) {
            if (localIcons != null) {
                for (int i = 0; i < localIcons.length; i++) {

                    if (localIcons[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "icons"));
                        elementList.add(localIcons[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("icons cannot be null!!");

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "label"));

        if (localLabel != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLabel));
        } else {
            throw new org.apache.axis2.databinding.ADBException("label cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "layout"));

        elementList.add(localLayout == null ? null : localLayout);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "miniIconUrl"));

        elementList.add(localMiniIconUrl == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localMiniIconUrl));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name"));

        if (localName != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localName));
        } else {
            throw new org.apache.axis2.databinding.ADBException("name cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "targetParentField"));

        elementList.add(localTargetParentField == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localTargetParentField));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "targetRecordTypeId"));

        elementList.add(localTargetRecordTypeId == null ? null : localTargetRecordTypeId);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "targetSobjectType"));

        elementList.add(localTargetSobjectType == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localTargetSobjectType));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type"));

        if (localType != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localType));
        } else {
            throw new org.apache.axis2.databinding.ADBException("type cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "visualforcePageName"));

        elementList.add(localVisualforcePageName == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localVisualforcePageName));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "width"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWidth));

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
        public static DescribeQuickActionResult parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            DescribeQuickActionResult object = new DescribeQuickActionResult();

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

                        if (!"DescribeQuickActionResult".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (DescribeQuickActionResult) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(
                                    nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list4 = new java.util.ArrayList();

                java.util.ArrayList list6 = new java.util.ArrayList();

                java.util.ArrayList list10 = new java.util.ArrayList();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "accessLevelRequired").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setAccessLevelRequired(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setAccessLevelRequired(com.salesforce.soap.partner.ShareAccessLevel.Factory.parse(reader));

                        reader.next();
                    }
                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "canvasApplicationId").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setCanvasApplicationId(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setCanvasApplicationId(com.salesforce.soap.partner.ID.Factory.parse(reader));

                        reader.next();
                    }
                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "canvasApplicationName").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setCanvasApplicationName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "colors").equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list4.add(com.salesforce.soap.partner.DescribeColor.Factory.parse(reader));

                    // loop until we find a start element that is not part of this array
                    boolean loopDone4 = false;
                    while (!loopDone4) {
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
                            loopDone4 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "colors").equals(reader.getName())) {
                                list4.add(com.salesforce.soap.partner.DescribeColor.Factory.parse(reader));

                            } else {
                                loopDone4 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setColors((com.salesforce.soap.partner.DescribeColor[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeColor.class, list4));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contextSobjectType").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setContextSobjectType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValues").equals(reader.getName())) {

                    // Process the array and step past its final element's end.

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list6.add(null);
                        reader.next();
                    } else {
                        list6.add(com.salesforce.soap.partner.DescribeQuickActionDefaultValue.Factory.parse(reader));
                    }
                    // loop until we find a start element that is not part of this array
                    boolean loopDone6 = false;
                    while (!loopDone6) {
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
                            loopDone6 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValues").equals(reader
                                    .getName())) {

                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list6.add(null);
                                    reader.next();
                                } else {
                                    list6.add(com.salesforce.soap.partner.DescribeQuickActionDefaultValue.Factory.parse(reader));
                                }
                            } else {
                                loopDone6 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setDefaultValues((com.salesforce.soap.partner.DescribeQuickActionDefaultValue[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeQuickActionDefaultValue.class, list6));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "height").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setHeight(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    } else {

                        object.setHeight(java.lang.Integer.MIN_VALUE);

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "iconName").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setIconName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "iconUrl").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setIconUrl(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "icons").equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list10.add(com.salesforce.soap.partner.DescribeIcon.Factory.parse(reader));

                    // loop until we find a start element that is not part of this array
                    boolean loopDone10 = false;
                    while (!loopDone10) {
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
                            loopDone10 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "icons").equals(reader.getName())) {
                                list10.add(com.salesforce.soap.partner.DescribeIcon.Factory.parse(reader));

                            } else {
                                loopDone10 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setIcons((com.salesforce.soap.partner.DescribeIcon[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeIcon.class, list10));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "layout").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setLayout(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setLayout(com.salesforce.soap.partner.DescribeLayoutSection.Factory.parse(reader));

                        reader.next();
                    }
                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "miniIconUrl").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setMiniIconUrl(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "targetParentField").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setTargetParentField(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "targetRecordTypeId").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setTargetRecordTypeId(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setTargetRecordTypeId(com.salesforce.soap.partner.ID.Factory.parse(reader));

                        reader.next();
                    }
                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "targetSobjectType").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setTargetSobjectType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "type" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "visualforcePageName").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setVisualforcePageName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "width").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setWidth(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    } else {

                        object.setWidth(java.lang.Integer.MIN_VALUE);

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
