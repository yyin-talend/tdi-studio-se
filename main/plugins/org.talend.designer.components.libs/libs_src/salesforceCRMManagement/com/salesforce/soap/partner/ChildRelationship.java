/**
 * ChildRelationship.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * ChildRelationship bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class ChildRelationship implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = ChildRelationship Namespace URI = urn:partner.soap.sforce.com
     * Namespace Prefix = ns1
     */

    /**
     * field for CascadeDelete
     */

    protected boolean localCascadeDelete;

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

        this.localCascadeDelete = param;

    }

    /**
     * field for ChildSObject
     */

    protected java.lang.String localChildSObject;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getChildSObject() {
        return localChildSObject;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ChildSObject
     */
    public void setChildSObject(java.lang.String param) {

        this.localChildSObject = param;

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
     * field for Field
     */

    protected java.lang.String localField;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getField() {
        return localField;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Field
     */
    public void setField(java.lang.String param) {

        this.localField = param;

    }

    /**
     * field for JunctionIdListName
     */

    protected java.lang.String localJunctionIdListName;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localJunctionIdListNameTracker = false;

    public boolean isJunctionIdListNameSpecified() {
        return localJunctionIdListNameTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getJunctionIdListName() {
        return localJunctionIdListName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param JunctionIdListName
     */
    public void setJunctionIdListName(java.lang.String param) {
        localJunctionIdListNameTracker = param != null;

        this.localJunctionIdListName = param;

    }

    /**
     * field for JunctionReferenceTo This was an Array!
     */

    protected java.lang.String[] localJunctionReferenceTo;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localJunctionReferenceToTracker = false;

    public boolean isJunctionReferenceToSpecified() {
        return localJunctionReferenceToTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String[]
     */
    public java.lang.String[] getJunctionReferenceTo() {
        return localJunctionReferenceTo;
    }

    /**
     * validate the array for JunctionReferenceTo
     */
    protected void validateJunctionReferenceTo(java.lang.String[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param JunctionReferenceTo
     */
    public void setJunctionReferenceTo(java.lang.String[] param) {

        validateJunctionReferenceTo(param);

        localJunctionReferenceToTracker = true;

        this.localJunctionReferenceTo = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param java.lang.String
     */
    public void addJunctionReferenceTo(java.lang.String param) {
        if (localJunctionReferenceTo == null) {
            localJunctionReferenceTo = new java.lang.String[] {};
        }

        // update the setting tracker
        localJunctionReferenceToTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localJunctionReferenceTo);
        list.add(param);
        this.localJunctionReferenceTo = (java.lang.String[]) list.toArray(new java.lang.String[list.size()]);

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
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        namespacePrefix + ":ChildRelationship", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "ChildRelationship", xmlWriter);
            }

        }

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "cascadeDelete", xmlWriter);

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("cascadeDelete cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCascadeDelete));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        writeStartElement(null, namespace, "childSObject", xmlWriter);

        if (localChildSObject == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("childSObject cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localChildSObject);

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
        writeStartElement(null, namespace, "field", xmlWriter);

        if (localField == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("field cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localField);

        }

        xmlWriter.writeEndElement();
        if (localJunctionIdListNameTracker) {
            namespace = "urn:partner.soap.sforce.com";
            writeStartElement(null, namespace, "junctionIdListName", xmlWriter);

            if (localJunctionIdListName == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("junctionIdListName cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localJunctionIdListName);

            }

            xmlWriter.writeEndElement();
        }
        if (localJunctionReferenceToTracker) {
            if (localJunctionReferenceTo != null) {
                namespace = "urn:partner.soap.sforce.com";
                for (int i = 0; i < localJunctionReferenceTo.length; i++) {

                    if (localJunctionReferenceTo[i] != null) {

                        writeStartElement(null, namespace, "junctionReferenceTo", xmlWriter);

                        xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(localJunctionReferenceTo[i]));

                        xmlWriter.writeEndElement();

                    } else {

                        // write null attribute
                        namespace = "urn:partner.soap.sforce.com";
                        writeStartElement(null, namespace, "junctionReferenceTo", xmlWriter);
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();

                    }

                }
            } else {

                // write the null attribute
                // write null attribute
                writeStartElement(null, "urn:partner.soap.sforce.com", "junctionReferenceTo", xmlWriter);

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

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "cascadeDelete"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCascadeDelete));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "childSObject"));

        if (localChildSObject != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localChildSObject));
        } else {
            throw new org.apache.axis2.databinding.ADBException("childSObject cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deprecatedAndHidden"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeprecatedAndHidden));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "field"));

        if (localField != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localField));
        } else {
            throw new org.apache.axis2.databinding.ADBException("field cannot be null!!");
        }
        if (localJunctionIdListNameTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "junctionIdListName"));

            if (localJunctionIdListName != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localJunctionIdListName));
            } else {
                throw new org.apache.axis2.databinding.ADBException("junctionIdListName cannot be null!!");
            }
        }
        if (localJunctionReferenceToTracker) {
            if (localJunctionReferenceTo != null) {
                for (int i = 0; i < localJunctionReferenceTo.length; i++) {

                    if (localJunctionReferenceTo[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "junctionReferenceTo"));
                        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(localJunctionReferenceTo[i]));
                    } else {

                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "junctionReferenceTo"));
                        elementList.add(null);

                    }

                }
            } else {

                elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "junctionReferenceTo"));
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
        if (localRestrictedDeleteTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "restrictedDelete"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRestrictedDelete));
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
        public static ChildRelationship parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            ChildRelationship object = new ChildRelationship();

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

                        if (!"ChildRelationship".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (ChildRelationship) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(nsUri,
                                    type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list6 = new java.util.ArrayList();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "cascadeDelete").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "cascadeDelete"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setCascadeDelete(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "childSObject").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "childSObject" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setChildSObject(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "field").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "field" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setField(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "junctionIdListName").equals(reader
                                .getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException("The element: " + "junctionIdListName"
                                + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setJunctionIdListName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "junctionReferenceTo").equals(reader
                                .getName())) {

                    // Process the array and step past its final element's end.

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        list6.add(null);

                        reader.next();
                    } else {
                        list6.add(reader.getElementText());
                    }
                    // loop until we find a start element that is not part of this array
                    boolean loopDone6 = false;
                    while (!loopDone6) {
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
                            loopDone6 = true;
                        } else {
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "junctionReferenceTo").equals(reader
                                    .getName())) {

                                nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                                if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                                    list6.add(null);

                                    reader.next();
                                } else {
                                    list6.add(reader.getElementText());
                                }
                            } else {
                                loopDone6 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setJunctionReferenceTo((java.lang.String[]) list6.toArray(new java.lang.String[list6.size()]));

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
