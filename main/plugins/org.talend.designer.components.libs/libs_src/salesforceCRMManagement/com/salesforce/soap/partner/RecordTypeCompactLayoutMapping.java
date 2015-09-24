/**
 * RecordTypeCompactLayoutMapping.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * RecordTypeCompactLayoutMapping bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class RecordTypeCompactLayoutMapping implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = RecordTypeCompactLayoutMapping Namespace URI =
     * urn:partner.soap.sforce.com Namespace Prefix = ns1
     */

    /**
     * field for Available
     */

    protected boolean localAvailable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getAvailable() {
        return localAvailable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Available
     */
    public void setAvailable(boolean param) {

        this.localAvailable = param;

    }

    /**
     * field for CompactLayoutId
     */

    protected com.salesforce.soap.partner.ID localCompactLayoutId;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getCompactLayoutId() {
        return localCompactLayoutId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param CompactLayoutId
     */
    public void setCompactLayoutId(com.salesforce.soap.partner.ID param) {

        this.localCompactLayoutId = param;

    }

    /**
     * field for CompactLayoutName
     */

    protected java.lang.String localCompactLayoutName;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getCompactLayoutName() {
        return localCompactLayoutName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param CompactLayoutName
     */
    public void setCompactLayoutName(java.lang.String param) {

        this.localCompactLayoutName = param;

    }

    /**
     * field for RecordTypeId
     */

    protected com.salesforce.soap.partner.ID localRecordTypeId;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getRecordTypeId() {
        return localRecordTypeId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param RecordTypeId
     */
    public void setRecordTypeId(com.salesforce.soap.partner.ID param) {

        this.localRecordTypeId = param;

    }

    /**
     * field for RecordTypeName
     */

    protected java.lang.String localRecordTypeName;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getRecordTypeName() {
        return localRecordTypeName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param RecordTypeName
     */
    public void setRecordTypeName(java.lang.String param) {

        this.localRecordTypeName = param;

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
                        + ":RecordTypeCompactLayoutMapping", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "RecordTypeCompactLayoutMapping",
                        xmlWriter);
            }

        }

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "available", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("available cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAvailable));
        }

        xmlWriter.writeEndElement();

        if (localCompactLayoutId == null) {

            writeStartElement(null, "urn:partner.soap.sforce.com", "compactLayoutId", xmlWriter);

            // write the nil attribute
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            xmlWriter.writeEndElement();
        } else {
            localCompactLayoutId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "compactLayoutId"),
                    xmlWriter);
        }

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "compactLayoutName", xmlWriter);

        if (localCompactLayoutName == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("compactLayoutName cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localCompactLayoutName);

        }

        xmlWriter.writeEndElement();

        if (localRecordTypeId == null) {
            throw new org.apache.axis2.databinding.ADBException("recordTypeId cannot be null!!");
        }
        localRecordTypeId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeId"), xmlWriter);

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "recordTypeName", xmlWriter);

        if (localRecordTypeName == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("recordTypeName cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localRecordTypeName);

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

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "available"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAvailable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "compactLayoutId"));

        elementList.add(localCompactLayoutId == null ? null : localCompactLayoutId);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "compactLayoutName"));

        if (localCompactLayoutName != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCompactLayoutName));
        } else {
            throw new org.apache.axis2.databinding.ADBException("compactLayoutName cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeId"));

        if (localRecordTypeId == null) {
            throw new org.apache.axis2.databinding.ADBException("recordTypeId cannot be null!!");
        }
        elementList.add(localRecordTypeId);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeName"));

        if (localRecordTypeName != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRecordTypeName));
        } else {
            throw new org.apache.axis2.databinding.ADBException("recordTypeName cannot be null!!");
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
        public static RecordTypeCompactLayoutMapping parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            RecordTypeCompactLayoutMapping object = new RecordTypeCompactLayoutMapping();

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

                        if (!"RecordTypeCompactLayoutMapping".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (RecordTypeCompactLayoutMapping) com.salesforce.soap.partner.sobject.ExtensionMapper
                                    .getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "available").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "available" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setAvailable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "compactLayoutId").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setCompactLayoutId(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setCompactLayoutId(com.salesforce.soap.partner.ID.Factory.parse(reader));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "compactLayoutName").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "compactLayoutName"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCompactLayoutName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeId").equals(reader.getName())) {

                    object.setRecordTypeId(com.salesforce.soap.partner.ID.Factory.parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeName")
                                .equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "recordTypeName"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setRecordTypeName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
