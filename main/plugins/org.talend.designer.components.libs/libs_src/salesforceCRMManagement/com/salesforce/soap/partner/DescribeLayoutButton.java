/**
 * DescribeLayoutButton.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * DescribeLayoutButton bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class DescribeLayoutButton implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = DescribeLayoutButton Namespace URI = urn:partner.soap.sforce.com
     * Namespace Prefix = ns1
     */

    /**
     * field for Behavior
     */

    protected com.salesforce.soap.partner.WebLinkWindowType localBehavior;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localBehaviorTracker = false;

    public boolean isBehaviorSpecified() {
        return localBehaviorTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.WebLinkWindowType
     */
    public com.salesforce.soap.partner.WebLinkWindowType getBehavior() {
        return localBehavior;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Behavior
     */
    public void setBehavior(com.salesforce.soap.partner.WebLinkWindowType param) {
        localBehaviorTracker = true;

        this.localBehavior = param;

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
     * field for Content
     */

    protected java.lang.String localContent;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localContentTracker = false;

    public boolean isContentSpecified() {
        return localContentTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getContent() {
        return localContent;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Content
     */
    public void setContent(java.lang.String param) {
        localContentTracker = true;

        this.localContent = param;

    }

    /**
     * field for ContentSource
     */

    protected com.salesforce.soap.partner.WebLinkType localContentSource;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localContentSourceTracker = false;

    public boolean isContentSourceSpecified() {
        return localContentSourceTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.WebLinkType
     */
    public com.salesforce.soap.partner.WebLinkType getContentSource() {
        return localContentSource;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ContentSource
     */
    public void setContentSource(com.salesforce.soap.partner.WebLinkType param) {
        localContentSourceTracker = true;

        this.localContentSource = param;

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
     * field for Encoding
     */

    protected java.lang.String localEncoding;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localEncodingTracker = false;

    public boolean isEncodingSpecified() {
        return localEncodingTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getEncoding() {
        return localEncoding;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Encoding
     */
    public void setEncoding(java.lang.String param) {
        localEncodingTracker = true;

        this.localEncoding = param;

    }

    /**
     * field for Height
     */

    protected int localHeight;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localHeightTracker = false;

    public boolean isHeightSpecified() {
        return localHeightTracker;
    }

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
        localHeightTracker = true;

        this.localHeight = param;

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
     * field for Menubar
     */

    protected boolean localMenubar;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getMenubar() {
        return localMenubar;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Menubar
     */
    public void setMenubar(boolean param) {

        this.localMenubar = param;

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
     * field for Overridden
     */

    protected boolean localOverridden;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getOverridden() {
        return localOverridden;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Overridden
     */
    public void setOverridden(boolean param) {

        this.localOverridden = param;

    }

    /**
     * field for Resizeable
     */

    protected boolean localResizeable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getResizeable() {
        return localResizeable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Resizeable
     */
    public void setResizeable(boolean param) {

        this.localResizeable = param;

    }

    /**
     * field for Scrollbars
     */

    protected boolean localScrollbars;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getScrollbars() {
        return localScrollbars;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Scrollbars
     */
    public void setScrollbars(boolean param) {

        this.localScrollbars = param;

    }

    /**
     * field for ShowsLocation
     */

    protected boolean localShowsLocation;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getShowsLocation() {
        return localShowsLocation;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ShowsLocation
     */
    public void setShowsLocation(boolean param) {

        this.localShowsLocation = param;

    }

    /**
     * field for ShowsStatus
     */

    protected boolean localShowsStatus;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getShowsStatus() {
        return localShowsStatus;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ShowsStatus
     */
    public void setShowsStatus(boolean param) {

        this.localShowsStatus = param;

    }

    /**
     * field for Toolbar
     */

    protected boolean localToolbar;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getToolbar() {
        return localToolbar;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Toolbar
     */
    public void setToolbar(boolean param) {

        this.localToolbar = param;

    }

    /**
     * field for Url
     */

    protected java.lang.String localUrl;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localUrlTracker = false;

    public boolean isUrlSpecified() {
        return localUrlTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getUrl() {
        return localUrl;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Url
     */
    public void setUrl(java.lang.String param) {
        localUrlTracker = true;

        this.localUrl = param;

    }

    /**
     * field for Width
     */

    protected int localWidth;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localWidthTracker = false;

    public boolean isWidthSpecified() {
        return localWidthTracker;
    }

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
        localWidthTracker = true;

        this.localWidth = param;

    }

    /**
     * field for WindowPosition
     */

    protected com.salesforce.soap.partner.WebLinkPosition localWindowPosition;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localWindowPositionTracker = false;

    public boolean isWindowPositionSpecified() {
        return localWindowPositionTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.WebLinkPosition
     */
    public com.salesforce.soap.partner.WebLinkPosition getWindowPosition() {
        return localWindowPosition;
    }

    /**
     * Auto generated setter method
     * 
     * @param param WindowPosition
     */
    public void setWindowPosition(com.salesforce.soap.partner.WebLinkPosition param) {
        localWindowPositionTracker = true;

        this.localWindowPosition = param;

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
                        + ":DescribeLayoutButton", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "DescribeLayoutButton", xmlWriter);
            }

        }
        if (localBehaviorTracker) {
            if (localBehavior == null) {

                writeStartElement(null, "urn:partner.soap.sforce.com", "behavior", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localBehavior.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "behavior"), xmlWriter);
            }
        }
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
        if (localContentTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "content", xmlWriter);

            if (localContent == null) {
                // write the nil attribute

                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

            } else {

                xmlWriter.writeCharacters(localContent);

            }

            xmlWriter.writeEndElement();
        }
        if (localContentSourceTracker) {
            if (localContentSource == null) {

                writeStartElement(null, "urn:partner.soap.sforce.com", "contentSource", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localContentSource.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contentSource"),
                        xmlWriter);
            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "custom", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("custom cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustom));
        }

        xmlWriter.writeEndElement();
        if (localEncodingTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "encoding", xmlWriter);

            if (localEncoding == null) {
                // write the nil attribute

                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

            } else {

                xmlWriter.writeCharacters(localEncoding);

            }

            xmlWriter.writeEndElement();
        }
        if (localHeightTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "height", xmlWriter);

            if (localHeight == java.lang.Integer.MIN_VALUE) {

                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHeight));
            }

            xmlWriter.writeEndElement();
        }
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

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localLabel);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "menubar", xmlWriter);

        if (false) {

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMenubar));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "name", xmlWriter);

        if (localName == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localName);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "overridden", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("overridden cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOverridden));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "resizeable", xmlWriter);

        if (false) {

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResizeable));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "scrollbars", xmlWriter);

        if (false) {

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localScrollbars));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "showsLocation", xmlWriter);

        if (false) {

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShowsLocation));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "showsStatus", xmlWriter);

        if (false) {

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShowsStatus));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "toolbar", xmlWriter);

        if (false) {

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localToolbar));
        }

        xmlWriter.writeEndElement();
        if (localUrlTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "url", xmlWriter);

            if (localUrl == null) {
                // write the nil attribute

                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

            } else {

                xmlWriter.writeCharacters(localUrl);

            }

            xmlWriter.writeEndElement();
        }
        if (localWidthTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "width", xmlWriter);

            if (localWidth == java.lang.Integer.MIN_VALUE) {

                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWidth));
            }

            xmlWriter.writeEndElement();
        }
        if (localWindowPositionTracker) {
            if (localWindowPosition == null) {

                writeStartElement(null, "urn:partner.soap.sforce.com", "windowPosition", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localWindowPosition.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "windowPosition"),
                        xmlWriter);
            }
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

        if (localBehaviorTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "behavior"));

            elementList.add(localBehavior == null ? null : localBehavior);
        }
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
        if (localContentTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "content"));

            elementList.add(localContent == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localContent));
        }
        if (localContentSourceTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contentSource"));

            elementList.add(localContentSource == null ? null : localContentSource);
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "custom"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustom));
        if (localEncodingTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "encoding"));

            elementList.add(localEncoding == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localEncoding));
        }
        if (localHeightTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "height"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHeight));
        }
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

        elementList.add(localLabel == null ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLabel));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "menubar"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMenubar));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name"));

        elementList.add(localName == null ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localName));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "overridden"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOverridden));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "resizeable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResizeable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "scrollbars"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localScrollbars));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "showsLocation"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShowsLocation));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "showsStatus"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localShowsStatus));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "toolbar"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localToolbar));
        if (localUrlTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "url"));

            elementList.add(localUrl == null ? null : org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUrl));
        }
        if (localWidthTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "width"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWidth));
        }
        if (localWindowPositionTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "windowPosition"));

            elementList.add(localWindowPosition == null ? null : localWindowPosition);
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
        public static DescribeLayoutButton parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            DescribeLayoutButton object = new DescribeLayoutButton();

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

                        if (!"DescribeLayoutButton".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (DescribeLayoutButton) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(
                                    nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list2 = new java.util.ArrayList();

                java.util.ArrayList list8 = new java.util.ArrayList();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "behavior").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setBehavior(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setBehavior(com.salesforce.soap.partner.WebLinkWindowType.Factory.parse(reader));

                        reader.next();
                    }
                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "colors").equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list2.add(com.salesforce.soap.partner.DescribeColor.Factory.parse(reader));

                    // loop until we find a start element that is not part of this array
                    boolean loopDone2 = false;
                    while (!loopDone2) {
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
                            loopDone2 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "colors").equals(reader.getName())) {
                                list2.add(com.salesforce.soap.partner.DescribeColor.Factory.parse(reader));

                            } else {
                                loopDone2 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setColors((com.salesforce.soap.partner.DescribeColor[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeColor.class, list2));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "content").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setContent(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    } else {

                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contentSource").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setContentSource(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setContentSource(com.salesforce.soap.partner.WebLinkType.Factory.parse(reader));

                        reader.next();
                    }
                }  // End of if for expected property start element

                else {

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "encoding").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setEncoding(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    } else {

                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();

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

                    object.setHeight(java.lang.Integer.MIN_VALUE);

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "icons").equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list8.add(com.salesforce.soap.partner.DescribeIcon.Factory.parse(reader));

                    // loop until we find a start element that is not part of this array
                    boolean loopDone8 = false;
                    while (!loopDone8) {
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
                            loopDone8 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "icons").equals(reader.getName())) {
                                list8.add(com.salesforce.soap.partner.DescribeIcon.Factory.parse(reader));

                            } else {
                                loopDone8 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setIcons((com.salesforce.soap.partner.DescribeIcon[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeIcon.class, list8));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "label").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setLabel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "menubar").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setMenubar(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "overridden").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "overridden" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOverridden(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "resizeable").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setResizeable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "scrollbars").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setScrollbars(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "showsLocation").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setShowsLocation(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "showsStatus").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setShowsStatus(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "toolbar").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setToolbar(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "url").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setUrl(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    } else {

                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();

                }  // End of if for expected property start element

                else {

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

                    object.setWidth(java.lang.Integer.MIN_VALUE);

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "windowPosition")
                                .equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setWindowPosition(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setWindowPosition(com.salesforce.soap.partner.WebLinkPosition.Factory.parse(reader));

                        reader.next();
                    }
                }  // End of if for expected property start element

                else {

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
