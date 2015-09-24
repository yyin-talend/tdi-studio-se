/**
 * DescribeCompactLayout.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * DescribeCompactLayout bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class DescribeCompactLayout implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = DescribeCompactLayout Namespace URI = urn:partner.soap.sforce.com
     * Namespace Prefix = ns1
     */

    /**
     * field for Actions This was an Array!
     */

    protected com.salesforce.soap.partner.DescribeLayoutButton[] localActions;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localActionsTracker = false;

    public boolean isActionsSpecified() {
        return localActionsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeLayoutButton[]
     */
    public com.salesforce.soap.partner.DescribeLayoutButton[] getActions() {
        return localActions;
    }

    /**
     * validate the array for Actions
     */
    protected void validateActions(com.salesforce.soap.partner.DescribeLayoutButton[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param Actions
     */
    public void setActions(com.salesforce.soap.partner.DescribeLayoutButton[] param) {

        validateActions(param);

        localActionsTracker = param != null;

        this.localActions = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.DescribeLayoutButton
     */
    public void addActions(com.salesforce.soap.partner.DescribeLayoutButton param) {
        if (localActions == null) {
            localActions = new com.salesforce.soap.partner.DescribeLayoutButton[] {};
        }

        // update the setting tracker
        localActionsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localActions);
        list.add(param);
        this.localActions = (com.salesforce.soap.partner.DescribeLayoutButton[]) list
                .toArray(new com.salesforce.soap.partner.DescribeLayoutButton[list.size()]);

    }

    /**
     * field for FieldItems This was an Array!
     */

    protected com.salesforce.soap.partner.DescribeLayoutItem[] localFieldItems;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localFieldItemsTracker = false;

    public boolean isFieldItemsSpecified() {
        return localFieldItemsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeLayoutItem[]
     */
    public com.salesforce.soap.partner.DescribeLayoutItem[] getFieldItems() {
        return localFieldItems;
    }

    /**
     * validate the array for FieldItems
     */
    protected void validateFieldItems(com.salesforce.soap.partner.DescribeLayoutItem[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param FieldItems
     */
    public void setFieldItems(com.salesforce.soap.partner.DescribeLayoutItem[] param) {

        validateFieldItems(param);

        localFieldItemsTracker = param != null;

        this.localFieldItems = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.DescribeLayoutItem
     */
    public void addFieldItems(com.salesforce.soap.partner.DescribeLayoutItem param) {
        if (localFieldItems == null) {
            localFieldItems = new com.salesforce.soap.partner.DescribeLayoutItem[] {};
        }

        // update the setting tracker
        localFieldItemsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localFieldItems);
        list.add(param);
        this.localFieldItems = (com.salesforce.soap.partner.DescribeLayoutItem[]) list
                .toArray(new com.salesforce.soap.partner.DescribeLayoutItem[list.size()]);

    }

    /**
     * field for Id
     */

    protected com.salesforce.soap.partner.ID localId;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getId() {
        return localId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Id
     */
    public void setId(com.salesforce.soap.partner.ID param) {

        this.localId = param;

    }

    /**
     * field for ImageItems This was an Array!
     */

    protected com.salesforce.soap.partner.DescribeLayoutItem[] localImageItems;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localImageItemsTracker = false;

    public boolean isImageItemsSpecified() {
        return localImageItemsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeLayoutItem[]
     */
    public com.salesforce.soap.partner.DescribeLayoutItem[] getImageItems() {
        return localImageItems;
    }

    /**
     * validate the array for ImageItems
     */
    protected void validateImageItems(com.salesforce.soap.partner.DescribeLayoutItem[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param ImageItems
     */
    public void setImageItems(com.salesforce.soap.partner.DescribeLayoutItem[] param) {

        validateImageItems(param);

        localImageItemsTracker = param != null;

        this.localImageItems = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.DescribeLayoutItem
     */
    public void addImageItems(com.salesforce.soap.partner.DescribeLayoutItem param) {
        if (localImageItems == null) {
            localImageItems = new com.salesforce.soap.partner.DescribeLayoutItem[] {};
        }

        // update the setting tracker
        localImageItemsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localImageItems);
        list.add(param);
        this.localImageItems = (com.salesforce.soap.partner.DescribeLayoutItem[]) list
                .toArray(new com.salesforce.soap.partner.DescribeLayoutItem[list.size()]);

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
     * field for ObjectType
     */

    protected java.lang.String localObjectType;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getObjectType() {
        return localObjectType;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ObjectType
     */
    public void setObjectType(java.lang.String param) {

        this.localObjectType = param;

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
                        + ":DescribeCompactLayout", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "DescribeCompactLayout", xmlWriter);
            }

        }
        if (localActionsTracker) {
            if (localActions != null) {
                for (int i = 0; i < localActions.length; i++) {
                    if (localActions[i] != null) {
                        localActions[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actions"),
                                xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("actions cannot be null!!");

            }
        }
        if (localFieldItemsTracker) {
            if (localFieldItems != null) {
                for (int i = 0; i < localFieldItems.length; i++) {
                    if (localFieldItems[i] != null) {
                        localFieldItems[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fieldItems"),
                                xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("fieldItems cannot be null!!");

            }
        }
        if (localId == null) {
            throw new org.apache.axis2.databinding.ADBException("id cannot be null!!");
        }
        localId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "id"), xmlWriter);
        if (localImageItemsTracker) {
            if (localImageItems != null) {
                for (int i = 0; i < localImageItems.length; i++) {
                    if (localImageItems[i] != null) {
                        localImageItems[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "imageItems"),
                                xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("imageItems cannot be null!!");

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
        writeStartElement(null, namespace, "objectType", xmlWriter);

        if (localObjectType == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("objectType cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localObjectType);

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

        if (localActionsTracker) {
            if (localActions != null) {
                for (int i = 0; i < localActions.length; i++) {

                    if (localActions[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actions"));
                        elementList.add(localActions[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("actions cannot be null!!");

            }

        }
        if (localFieldItemsTracker) {
            if (localFieldItems != null) {
                for (int i = 0; i < localFieldItems.length; i++) {

                    if (localFieldItems[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fieldItems"));
                        elementList.add(localFieldItems[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("fieldItems cannot be null!!");

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "id"));

        if (localId == null) {
            throw new org.apache.axis2.databinding.ADBException("id cannot be null!!");
        }
        elementList.add(localId);
        if (localImageItemsTracker) {
            if (localImageItems != null) {
                for (int i = 0; i < localImageItems.length; i++) {

                    if (localImageItems[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "imageItems"));
                        elementList.add(localImageItems[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("imageItems cannot be null!!");

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "label"));

        if (localLabel != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLabel));
        } else {
            throw new org.apache.axis2.databinding.ADBException("label cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name"));

        if (localName != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localName));
        } else {
            throw new org.apache.axis2.databinding.ADBException("name cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "objectType"));

        if (localObjectType != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localObjectType));
        } else {
            throw new org.apache.axis2.databinding.ADBException("objectType cannot be null!!");
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
        public static DescribeCompactLayout parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            DescribeCompactLayout object = new DescribeCompactLayout();

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

                        if (!"DescribeCompactLayout".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (DescribeCompactLayout) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(
                                    nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list1 = new java.util.ArrayList();

                java.util.ArrayList list2 = new java.util.ArrayList();

                java.util.ArrayList list4 = new java.util.ArrayList();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actions").equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list1.add(com.salesforce.soap.partner.DescribeLayoutButton.Factory.parse(reader));

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
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actions").equals(reader.getName())) {
                                list1.add(com.salesforce.soap.partner.DescribeLayoutButton.Factory.parse(reader));

                            } else {
                                loopDone1 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setActions((com.salesforce.soap.partner.DescribeLayoutButton[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeLayoutButton.class, list1));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fieldItems").equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list2.add(com.salesforce.soap.partner.DescribeLayoutItem.Factory.parse(reader));

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
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fieldItems").equals(reader
                                    .getName())) {
                                list2.add(com.salesforce.soap.partner.DescribeLayoutItem.Factory.parse(reader));

                            } else {
                                loopDone2 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setFieldItems((com.salesforce.soap.partner.DescribeLayoutItem[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeLayoutItem.class, list2));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "id").equals(reader.getName())) {

                    object.setId(com.salesforce.soap.partner.ID.Factory.parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "imageItems").equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list4.add(com.salesforce.soap.partner.DescribeLayoutItem.Factory.parse(reader));

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
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "imageItems").equals(reader
                                    .getName())) {
                                list4.add(com.salesforce.soap.partner.DescribeLayoutItem.Factory.parse(reader));

                            } else {
                                loopDone4 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setImageItems((com.salesforce.soap.partner.DescribeLayoutItem[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeLayoutItem.class, list4));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "objectType").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "objectType" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setObjectType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
