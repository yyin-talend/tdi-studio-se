/**
 * LeadConvert.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * LeadConvert bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class LeadConvert implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = LeadConvert Namespace URI = urn:partner.soap.sforce.com Namespace
     * Prefix = ns1
     */

    /**
     * field for AccountId
     */

    protected com.salesforce.soap.partner.ID localAccountId;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getAccountId() {
        return localAccountId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param AccountId
     */
    public void setAccountId(com.salesforce.soap.partner.ID param) {

        this.localAccountId = param;

    }

    /**
     * field for ContactId
     */

    protected com.salesforce.soap.partner.ID localContactId;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getContactId() {
        return localContactId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ContactId
     */
    public void setContactId(com.salesforce.soap.partner.ID param) {

        this.localContactId = param;

    }

    /**
     * field for ConvertedStatus
     */

    protected java.lang.String localConvertedStatus;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getConvertedStatus() {
        return localConvertedStatus;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ConvertedStatus
     */
    public void setConvertedStatus(java.lang.String param) {

        this.localConvertedStatus = param;

    }

    /**
     * field for DoNotCreateOpportunity
     */

    protected boolean localDoNotCreateOpportunity;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getDoNotCreateOpportunity() {
        return localDoNotCreateOpportunity;
    }

    /**
     * Auto generated setter method
     * 
     * @param param DoNotCreateOpportunity
     */
    public void setDoNotCreateOpportunity(boolean param) {

        this.localDoNotCreateOpportunity = param;

    }

    /**
     * field for LeadId
     */

    protected com.salesforce.soap.partner.ID localLeadId;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getLeadId() {
        return localLeadId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param LeadId
     */
    public void setLeadId(com.salesforce.soap.partner.ID param) {

        this.localLeadId = param;

    }

    /**
     * field for OpportunityName
     */

    protected java.lang.String localOpportunityName;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getOpportunityName() {
        return localOpportunityName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param OpportunityName
     */
    public void setOpportunityName(java.lang.String param) {

        this.localOpportunityName = param;

    }

    /**
     * field for OverwriteLeadSource
     */

    protected boolean localOverwriteLeadSource;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getOverwriteLeadSource() {
        return localOverwriteLeadSource;
    }

    /**
     * Auto generated setter method
     * 
     * @param param OverwriteLeadSource
     */
    public void setOverwriteLeadSource(boolean param) {

        this.localOverwriteLeadSource = param;

    }

    /**
     * field for OwnerId
     */

    protected com.salesforce.soap.partner.ID localOwnerId;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getOwnerId() {
        return localOwnerId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param OwnerId
     */
    public void setOwnerId(com.salesforce.soap.partner.ID param) {

        this.localOwnerId = param;

    }

    /**
     * field for SendNotificationEmail
     */

    protected boolean localSendNotificationEmail;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getSendNotificationEmail() {
        return localSendNotificationEmail;
    }

    /**
     * Auto generated setter method
     * 
     * @param param SendNotificationEmail
     */
    public void setSendNotificationEmail(boolean param) {

        this.localSendNotificationEmail = param;

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
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":LeadConvert",
                        xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "LeadConvert", xmlWriter);
            }

        }

        if (localAccountId == null) {

            writeStartElement(null, "urn:partner.soap.sforce.com", "accountId", xmlWriter);

            // write the nil attribute
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            xmlWriter.writeEndElement();
        } else {
            localAccountId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "accountId"), xmlWriter);
        }

        if (localContactId == null) {

            writeStartElement(null, "urn:partner.soap.sforce.com", "contactId", xmlWriter);

            // write the nil attribute
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            xmlWriter.writeEndElement();
        } else {
            localContactId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contactId"), xmlWriter);
        }

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "convertedStatus", xmlWriter);

        if (localConvertedStatus == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("convertedStatus cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localConvertedStatus);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "doNotCreateOpportunity", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("doNotCreateOpportunity cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localDoNotCreateOpportunity));
        }

        xmlWriter.writeEndElement();

        if (localLeadId == null) {
            throw new org.apache.axis2.databinding.ADBException("leadId cannot be null!!");
        }
        localLeadId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "leadId"), xmlWriter);

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "opportunityName", xmlWriter);

        if (localOpportunityName == null) {
            // write the nil attribute

            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);

        } else {

            xmlWriter.writeCharacters(localOpportunityName);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "overwriteLeadSource", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("overwriteLeadSource cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOverwriteLeadSource));
        }

        xmlWriter.writeEndElement();

        if (localOwnerId == null) {

            writeStartElement(null, "urn:partner.soap.sforce.com", "ownerId", xmlWriter);

            // write the nil attribute
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            xmlWriter.writeEndElement();
        } else {
            localOwnerId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ownerId"), xmlWriter);
        }

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "sendNotificationEmail", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("sendNotificationEmail cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localSendNotificationEmail));
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

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "accountId"));

        elementList.add(localAccountId == null ? null : localAccountId);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contactId"));

        elementList.add(localContactId == null ? null : localContactId);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertedStatus"));

        if (localConvertedStatus != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localConvertedStatus));
        } else {
            throw new org.apache.axis2.databinding.ADBException("convertedStatus cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "doNotCreateOpportunity"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDoNotCreateOpportunity));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "leadId"));

        if (localLeadId == null) {
            throw new org.apache.axis2.databinding.ADBException("leadId cannot be null!!");
        }
        elementList.add(localLeadId);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "opportunityName"));

        elementList.add(localOpportunityName == null ? null : org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localOpportunityName));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "overwriteLeadSource"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOverwriteLeadSource));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ownerId"));

        elementList.add(localOwnerId == null ? null : localOwnerId);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sendNotificationEmail"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSendNotificationEmail));

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
        public static LeadConvert parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            LeadConvert object = new LeadConvert();

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

                        if (!"LeadConvert".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (LeadConvert) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(nsUri, type,
                                    reader);
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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "accountId").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setAccountId(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setAccountId(com.salesforce.soap.partner.ID.Factory.parse(reader));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contactId").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setContactId(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setContactId(com.salesforce.soap.partner.ID.Factory.parse(reader));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertedStatus").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "convertedStatus"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setConvertedStatus(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "doNotCreateOpportunity").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "doNotCreateOpportunity"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDoNotCreateOpportunity(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "leadId").equals(reader.getName())) {

                    object.setLeadId(com.salesforce.soap.partner.ID.Factory.parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "opportunityName").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {

                        java.lang.String content = reader.getElementText();

                        object.setOpportunityName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "overwriteLeadSource").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "overwriteLeadSource"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setOverwriteLeadSource(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ownerId").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setOwnerId(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setOwnerId(com.salesforce.soap.partner.ID.Factory.parse(reader));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sendNotificationEmail").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "sendNotificationEmail"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSendNotificationEmail(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
