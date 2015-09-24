/**
 * PerformQuickActionResult.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * PerformQuickActionResult bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class PerformQuickActionResult implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = PerformQuickActionResult Namespace URI = urn:partner.soap.sforce.com
     * Namespace Prefix = ns1
     */

    /**
     * field for ContextId
     */

    protected com.salesforce.soap.partner.ID localContextId;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localContextIdTracker = false;

    public boolean isContextIdSpecified() {
        return localContextIdTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID
     */
    public com.salesforce.soap.partner.ID getContextId() {
        return localContextId;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ContextId
     */
    public void setContextId(com.salesforce.soap.partner.ID param) {
        localContextIdTracker = true;

        this.localContextId = param;

    }

    /**
     * field for Created
     */

    protected boolean localCreated;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getCreated() {
        return localCreated;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Created
     */
    public void setCreated(boolean param) {

        this.localCreated = param;

    }

    /**
     * field for Errors This was an Array!
     */

    protected com.salesforce.soap.partner.Error[] localErrors;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localErrorsTracker = false;

    public boolean isErrorsSpecified() {
        return localErrorsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.Error[]
     */
    public com.salesforce.soap.partner.Error[] getErrors() {
        return localErrors;
    }

    /**
     * validate the array for Errors
     */
    protected void validateErrors(com.salesforce.soap.partner.Error[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param Errors
     */
    public void setErrors(com.salesforce.soap.partner.Error[] param) {

        validateErrors(param);

        localErrorsTracker = param != null;

        this.localErrors = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.Error
     */
    public void addErrors(com.salesforce.soap.partner.Error param) {
        if (localErrors == null) {
            localErrors = new com.salesforce.soap.partner.Error[] {};
        }

        // update the setting tracker
        localErrorsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localErrors);
        list.add(param);
        this.localErrors = (com.salesforce.soap.partner.Error[]) list.toArray(new com.salesforce.soap.partner.Error[list.size()]);

    }

    /**
     * field for FeedItemIds This was an Array!
     */

    protected com.salesforce.soap.partner.ID[] localFeedItemIds;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localFeedItemIdsTracker = false;

    public boolean isFeedItemIdsSpecified() {
        return localFeedItemIdsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID[]
     */
    public com.salesforce.soap.partner.ID[] getFeedItemIds() {
        return localFeedItemIds;
    }

    /**
     * validate the array for FeedItemIds
     */
    protected void validateFeedItemIds(com.salesforce.soap.partner.ID[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param FeedItemIds
     */
    public void setFeedItemIds(com.salesforce.soap.partner.ID[] param) {

        validateFeedItemIds(param);

        localFeedItemIdsTracker = true;

        this.localFeedItemIds = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.ID
     */
    public void addFeedItemIds(com.salesforce.soap.partner.ID param) {
        if (localFeedItemIds == null) {
            localFeedItemIds = new com.salesforce.soap.partner.ID[] {};
        }

        // update the setting tracker
        localFeedItemIdsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localFeedItemIds);
        list.add(param);
        this.localFeedItemIds = (com.salesforce.soap.partner.ID[]) list.toArray(new com.salesforce.soap.partner.ID[list.size()]);

    }

    /**
     * field for Ids This was an Array!
     */

    protected com.salesforce.soap.partner.ID[] localIds;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localIdsTracker = false;

    public boolean isIdsSpecified() {
        return localIdsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.ID[]
     */
    public com.salesforce.soap.partner.ID[] getIds() {
        return localIds;
    }

    /**
     * validate the array for Ids
     */
    protected void validateIds(com.salesforce.soap.partner.ID[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param Ids
     */
    public void setIds(com.salesforce.soap.partner.ID[] param) {

        validateIds(param);

        localIdsTracker = true;

        this.localIds = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.ID
     */
    public void addIds(com.salesforce.soap.partner.ID param) {
        if (localIds == null) {
            localIds = new com.salesforce.soap.partner.ID[] {};
        }

        // update the setting tracker
        localIdsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localIds);
        list.add(param);
        this.localIds = (com.salesforce.soap.partner.ID[]) list.toArray(new com.salesforce.soap.partner.ID[list.size()]);

    }

    /**
     * field for Success
     */

    protected boolean localSuccess;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getSuccess() {
        return localSuccess;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Success
     */
    public void setSuccess(boolean param) {

        this.localSuccess = param;

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
                        + ":PerformQuickActionResult", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "PerformQuickActionResult", xmlWriter);
            }

        }
        if (localContextIdTracker) {
            if (localContextId == null) {

                writeStartElement(null, "urn:partner.soap.sforce.com", "contextId", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localContextId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contextId"), xmlWriter);
            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "created", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("created cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreated));
        }

        xmlWriter.writeEndElement();
        if (localErrorsTracker) {
            if (localErrors != null) {
                for (int i = 0; i < localErrors.length; i++) {
                    if (localErrors[i] != null) {
                        localErrors[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "errors"),
                                xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("errors cannot be null!!");

            }
        }
        if (localFeedItemIdsTracker) {
            if (localFeedItemIds != null) {
                for (int i = 0; i < localFeedItemIds.length; i++) {
                    if (localFeedItemIds[i] != null) {
                        localFeedItemIds[i].serialize(
                                new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedItemIds"), xmlWriter);
                    } else {

                        writeStartElement(null, "urn:partner.soap.sforce.com", "feedItemIds", xmlWriter);

                        // write the nil attribute
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();

                    }

                }
            } else {

                writeStartElement(null, "urn:partner.soap.sforce.com", "feedItemIds", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();

            }
        }
        if (localIdsTracker) {
            if (localIds != null) {
                for (int i = 0; i < localIds.length; i++) {
                    if (localIds[i] != null) {
                        localIds[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids"), xmlWriter);
                    } else {

                        writeStartElement(null, "urn:partner.soap.sforce.com", "ids", xmlWriter);

                        // write the nil attribute
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();

                    }

                }
            } else {

                writeStartElement(null, "urn:partner.soap.sforce.com", "ids", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();

            }
        }
        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "success", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("success cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSuccess));
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

        if (localContextIdTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contextId"));

            elementList.add(localContextId == null ? null : localContextId);
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "created"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreated));
        if (localErrorsTracker) {
            if (localErrors != null) {
                for (int i = 0; i < localErrors.length; i++) {

                    if (localErrors[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "errors"));
                        elementList.add(localErrors[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("errors cannot be null!!");

            }

        }
        if (localFeedItemIdsTracker) {
            if (localFeedItemIds != null) {
                for (int i = 0; i < localFeedItemIds.length; i++) {

                    if (localFeedItemIds[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedItemIds"));
                        elementList.add(localFeedItemIds[i]);
                    } else {

                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedItemIds"));
                        elementList.add(null);

                    }

                }
            } else {

                elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedItemIds"));
                elementList.add(localFeedItemIds);

            }

        }
        if (localIdsTracker) {
            if (localIds != null) {
                for (int i = 0; i < localIds.length; i++) {

                    if (localIds[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids"));
                        elementList.add(localIds[i]);
                    } else {

                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids"));
                        elementList.add(null);

                    }

                }
            } else {

                elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids"));
                elementList.add(localIds);

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "success"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSuccess));

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
        public static PerformQuickActionResult parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            PerformQuickActionResult object = new PerformQuickActionResult();

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

                        if (!"PerformQuickActionResult".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (PerformQuickActionResult) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(
                                    nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list3 = new java.util.ArrayList();

                java.util.ArrayList list4 = new java.util.ArrayList();

                java.util.ArrayList list5 = new java.util.ArrayList();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contextId").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setContextId(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setContextId(com.salesforce.soap.partner.ID.Factory.parse(reader));

                        reader.next();
                    }
                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "created").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "created" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCreated(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "errors").equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list3.add(com.salesforce.soap.partner.Error.Factory.parse(reader));

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
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "errors").equals(reader.getName())) {
                                list3.add(com.salesforce.soap.partner.Error.Factory.parse(reader));

                            } else {
                                loopDone3 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setErrors((com.salesforce.soap.partner.Error[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.Error.class, list3));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedItemIds").equals(reader.getName())) {

                    // Process the array and step past its final element's end.

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list4.add(null);
                        reader.next();
                    } else {
                        list4.add(com.salesforce.soap.partner.ID.Factory.parse(reader));
                    }
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
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedItemIds").equals(reader
                                    .getName())) {

                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list4.add(null);
                                    reader.next();
                                } else {
                                    list4.add(com.salesforce.soap.partner.ID.Factory.parse(reader));
                                }
                            } else {
                                loopDone4 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setFeedItemIds((com.salesforce.soap.partner.ID[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.ID.class, list4));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids").equals(reader.getName())) {

                    // Process the array and step past its final element's end.

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list5.add(null);
                        reader.next();
                    } else {
                        list5.add(com.salesforce.soap.partner.ID.Factory.parse(reader));
                    }
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
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids").equals(reader.getName())) {

                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list5.add(null);
                                    reader.next();
                                } else {
                                    list5.add(com.salesforce.soap.partner.ID.Factory.parse(reader));
                                }
                            } else {
                                loopDone5 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setIds((com.salesforce.soap.partner.ID[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.ID.class, list5));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "success").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "success" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSuccess(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
