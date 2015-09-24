/**
 * DescribeFlexiPageResult.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * DescribeFlexiPageResult bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class DescribeFlexiPageResult implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = DescribeFlexiPageResult Namespace URI = urn:partner.soap.sforce.com
     * Namespace Prefix = ns1
     */

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
     * field for QuickActionList
     */

    protected com.salesforce.soap.partner.DescribeQuickActionListResult localQuickActionList;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localQuickActionListTracker = false;

    public boolean isQuickActionListSpecified() {
        return localQuickActionListTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeQuickActionListResult
     */
    public com.salesforce.soap.partner.DescribeQuickActionListResult getQuickActionList() {
        return localQuickActionList;
    }

    /**
     * Auto generated setter method
     * 
     * @param param QuickActionList
     */
    public void setQuickActionList(com.salesforce.soap.partner.DescribeQuickActionListResult param) {
        localQuickActionListTracker = param != null;

        this.localQuickActionList = param;

    }

    /**
     * field for Regions This was an Array!
     */

    protected com.salesforce.soap.partner.DescribeFlexiPageRegion[] localRegions;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localRegionsTracker = false;

    public boolean isRegionsSpecified() {
        return localRegionsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeFlexiPageRegion[]
     */
    public com.salesforce.soap.partner.DescribeFlexiPageRegion[] getRegions() {
        return localRegions;
    }

    /**
     * validate the array for Regions
     */
    protected void validateRegions(com.salesforce.soap.partner.DescribeFlexiPageRegion[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param Regions
     */
    public void setRegions(com.salesforce.soap.partner.DescribeFlexiPageRegion[] param) {

        validateRegions(param);

        localRegionsTracker = param != null;

        this.localRegions = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.DescribeFlexiPageRegion
     */
    public void addRegions(com.salesforce.soap.partner.DescribeFlexiPageRegion param) {
        if (localRegions == null) {
            localRegions = new com.salesforce.soap.partner.DescribeFlexiPageRegion[] {};
        }

        // update the setting tracker
        localRegionsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localRegions);
        list.add(param);
        this.localRegions = (com.salesforce.soap.partner.DescribeFlexiPageRegion[]) list
                .toArray(new com.salesforce.soap.partner.DescribeFlexiPageRegion[list.size()]);

    }

    /**
     * field for SobjectType
     */

    protected java.lang.String localSobjectType;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localSobjectTypeTracker = false;

    public boolean isSobjectTypeSpecified() {
        return localSobjectTypeTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getSobjectType() {
        return localSobjectType;
    }

    /**
     * Auto generated setter method
     * 
     * @param param SobjectType
     */
    public void setSobjectType(java.lang.String param) {
        localSobjectTypeTracker = true;

        this.localSobjectType = param;

    }

    /**
     * field for Template
     */

    protected java.lang.String localTemplate;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getTemplate() {
        return localTemplate;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Template
     */
    public void setTemplate(java.lang.String param) {

        this.localTemplate = param;

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
                        + ":DescribeFlexiPageResult", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "DescribeFlexiPageResult", xmlWriter);
            }

        }

        if (localId == null) {
            throw new org.apache.axis2.databinding.ADBException("id cannot be null!!");
        }
        localId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "id"), xmlWriter);

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
        if (localQuickActionListTracker) {
            if (localQuickActionList == null) {
                throw new org.apache.axis2.databinding.ADBException("quickActionList cannot be null!!");
            }
            localQuickActionList.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "quickActionList"),
                    xmlWriter);
        }
        if (localRegionsTracker) {
            if (localRegions != null) {
                for (int i = 0; i < localRegions.length; i++) {
                    if (localRegions[i] != null) {
                        localRegions[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "regions"),
                                xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("regions cannot be null!!");

            }
        }
        if (localSobjectTypeTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "sobjectType", xmlWriter);

            if (localSobjectType == null) {
                // write the nil attribute

                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

            } else {

                xmlWriter.writeCharacters(localSobjectType);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "template", xmlWriter);

        if (localTemplate == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localTemplate);

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

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "id"));

        if (localId == null) {
            throw new org.apache.axis2.databinding.ADBException("id cannot be null!!");
        }
        elementList.add(localId);

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
        if (localQuickActionListTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "quickActionList"));

            if (localQuickActionList == null) {
                throw new org.apache.axis2.databinding.ADBException("quickActionList cannot be null!!");
            }
            elementList.add(localQuickActionList);
        }
        if (localRegionsTracker) {
            if (localRegions != null) {
                for (int i = 0; i < localRegions.length; i++) {

                    if (localRegions[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "regions"));
                        elementList.add(localRegions[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("regions cannot be null!!");

            }

        }
        if (localSobjectTypeTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sobjectType"));

            elementList.add(localSobjectType == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localSobjectType));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "template"));

        elementList.add(localTemplate == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localTemplate));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type"));

        if (localType != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localType));
        } else {
            throw new org.apache.axis2.databinding.ADBException("type cannot be null!!");
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
        public static DescribeFlexiPageResult parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            DescribeFlexiPageResult object = new DescribeFlexiPageResult();

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

                        if (!"DescribeFlexiPageResult".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (DescribeFlexiPageResult) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(
                                    nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list5 = new java.util.ArrayList();

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "quickActionList").equals(reader
                                .getName())) {

                    object.setQuickActionList(com.salesforce.soap.partner.DescribeQuickActionListResult.Factory.parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "regions").equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list5.add(com.salesforce.soap.partner.DescribeFlexiPageRegion.Factory.parse(reader));

                    // loop until we find a start element that is not part of this array
                    boolean loopDone5 = false;
                    while (!loopDone5) {
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
                            loopDone5 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "regions").equals(reader.getName())) {
                                list5.add(com.salesforce.soap.partner.DescribeFlexiPageRegion.Factory.parse(reader));

                            } else {
                                loopDone5 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setRegions((com.salesforce.soap.partner.DescribeFlexiPageRegion[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeFlexiPageRegion.class, list5));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sobjectType").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setSobjectType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "template").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setTemplate(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
