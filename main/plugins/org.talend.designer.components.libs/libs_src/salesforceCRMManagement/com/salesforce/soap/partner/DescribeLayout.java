/**
 * DescribeLayout.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:34:40 IST)
 */

package com.salesforce.soap.partner;

/**
 * DescribeLayout bean class
 */
@SuppressWarnings({ "unchecked", "unused" })
public class DescribeLayout implements org.apache.axis2.databinding.ADBBean {
    /*
     * This type was generated from the piece of schema that had name = DescribeLayout Namespace URI = urn:partner.soap.sforce.com Namespace
     * Prefix = ns1
     */

    /**
     * field for ButtonLayoutSection
     */

    protected com.salesforce.soap.partner.DescribeLayoutButtonSection localButtonLayoutSection;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localButtonLayoutSectionTracker = false;

    public boolean isButtonLayoutSectionSpecified() {
        return localButtonLayoutSectionTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeLayoutButtonSection
     */
    public com.salesforce.soap.partner.DescribeLayoutButtonSection getButtonLayoutSection() {
        return localButtonLayoutSection;
    }

    /**
     * Auto generated setter method
     * 
     * @param param ButtonLayoutSection
     */
    public void setButtonLayoutSection(com.salesforce.soap.partner.DescribeLayoutButtonSection param) {
        localButtonLayoutSectionTracker = param != null;

        this.localButtonLayoutSection = param;

    }

    /**
     * field for DetailLayoutSections This was an Array!
     */

    protected com.salesforce.soap.partner.DescribeLayoutSection[] localDetailLayoutSections;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localDetailLayoutSectionsTracker = false;

    public boolean isDetailLayoutSectionsSpecified() {
        return localDetailLayoutSectionsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeLayoutSection[]
     */
    public com.salesforce.soap.partner.DescribeLayoutSection[] getDetailLayoutSections() {
        return localDetailLayoutSections;
    }

    /**
     * validate the array for DetailLayoutSections
     */
    protected void validateDetailLayoutSections(com.salesforce.soap.partner.DescribeLayoutSection[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param DetailLayoutSections
     */
    public void setDetailLayoutSections(com.salesforce.soap.partner.DescribeLayoutSection[] param) {

        validateDetailLayoutSections(param);

        localDetailLayoutSectionsTracker = param != null;

        this.localDetailLayoutSections = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.DescribeLayoutSection
     */
    public void addDetailLayoutSections(com.salesforce.soap.partner.DescribeLayoutSection param) {
        if (localDetailLayoutSections == null) {
            localDetailLayoutSections = new com.salesforce.soap.partner.DescribeLayoutSection[] {};
        }

        // update the setting tracker
        localDetailLayoutSectionsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localDetailLayoutSections);
        list.add(param);
        this.localDetailLayoutSections = (com.salesforce.soap.partner.DescribeLayoutSection[]) list
                .toArray(new com.salesforce.soap.partner.DescribeLayoutSection[list.size()]);

    }

    /**
     * field for EditLayoutSections This was an Array!
     */

    protected com.salesforce.soap.partner.DescribeLayoutSection[] localEditLayoutSections;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localEditLayoutSectionsTracker = false;

    public boolean isEditLayoutSectionsSpecified() {
        return localEditLayoutSectionsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeLayoutSection[]
     */
    public com.salesforce.soap.partner.DescribeLayoutSection[] getEditLayoutSections() {
        return localEditLayoutSections;
    }

    /**
     * validate the array for EditLayoutSections
     */
    protected void validateEditLayoutSections(com.salesforce.soap.partner.DescribeLayoutSection[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param EditLayoutSections
     */
    public void setEditLayoutSections(com.salesforce.soap.partner.DescribeLayoutSection[] param) {

        validateEditLayoutSections(param);

        localEditLayoutSectionsTracker = param != null;

        this.localEditLayoutSections = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.DescribeLayoutSection
     */
    public void addEditLayoutSections(com.salesforce.soap.partner.DescribeLayoutSection param) {
        if (localEditLayoutSections == null) {
            localEditLayoutSections = new com.salesforce.soap.partner.DescribeLayoutSection[] {};
        }

        // update the setting tracker
        localEditLayoutSectionsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localEditLayoutSections);
        list.add(param);
        this.localEditLayoutSections = (com.salesforce.soap.partner.DescribeLayoutSection[]) list
                .toArray(new com.salesforce.soap.partner.DescribeLayoutSection[list.size()]);

    }

    /**
     * field for FeedView
     */

    protected com.salesforce.soap.partner.DescribeLayoutFeedView localFeedView;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localFeedViewTracker = false;

    public boolean isFeedViewSpecified() {
        return localFeedViewTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeLayoutFeedView
     */
    public com.salesforce.soap.partner.DescribeLayoutFeedView getFeedView() {
        return localFeedView;
    }

    /**
     * Auto generated setter method
     * 
     * @param param FeedView
     */
    public void setFeedView(com.salesforce.soap.partner.DescribeLayoutFeedView param) {
        localFeedViewTracker = param != null;

        this.localFeedView = param;

    }

    /**
     * field for HighlightsPanelLayoutSection
     */

    protected com.salesforce.soap.partner.DescribeLayoutSection localHighlightsPanelLayoutSection;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localHighlightsPanelLayoutSectionTracker = false;

    public boolean isHighlightsPanelLayoutSectionSpecified() {
        return localHighlightsPanelLayoutSectionTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.DescribeLayoutSection
     */
    public com.salesforce.soap.partner.DescribeLayoutSection getHighlightsPanelLayoutSection() {
        return localHighlightsPanelLayoutSection;
    }

    /**
     * Auto generated setter method
     * 
     * @param param HighlightsPanelLayoutSection
     */
    public void setHighlightsPanelLayoutSection(com.salesforce.soap.partner.DescribeLayoutSection param) {
        localHighlightsPanelLayoutSectionTracker = param != null;

        this.localHighlightsPanelLayoutSection = param;

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
     * field for RelatedContent
     */

    protected com.salesforce.soap.partner.RelatedContent localRelatedContent;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localRelatedContentTracker = false;

    public boolean isRelatedContentSpecified() {
        return localRelatedContentTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.RelatedContent
     */
    public com.salesforce.soap.partner.RelatedContent getRelatedContent() {
        return localRelatedContent;
    }

    /**
     * Auto generated setter method
     * 
     * @param param RelatedContent
     */
    public void setRelatedContent(com.salesforce.soap.partner.RelatedContent param) {
        localRelatedContentTracker = param != null;

        this.localRelatedContent = param;

    }

    /**
     * field for RelatedLists This was an Array!
     */

    protected com.salesforce.soap.partner.RelatedList[] localRelatedLists;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be used to determine
     * whether to include this field in the serialized XML
     */
    protected boolean localRelatedListsTracker = false;

    public boolean isRelatedListsSpecified() {
        return localRelatedListsTracker;
    }

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.RelatedList[]
     */
    public com.salesforce.soap.partner.RelatedList[] getRelatedLists() {
        return localRelatedLists;
    }

    /**
     * validate the array for RelatedLists
     */
    protected void validateRelatedLists(com.salesforce.soap.partner.RelatedList[] param) {

    }

    /**
     * Auto generated setter method
     * 
     * @param param RelatedLists
     */
    public void setRelatedLists(com.salesforce.soap.partner.RelatedList[] param) {

        validateRelatedLists(param);

        localRelatedListsTracker = param != null;

        this.localRelatedLists = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param com.salesforce.soap.partner.RelatedList
     */
    public void addRelatedLists(com.salesforce.soap.partner.RelatedList param) {
        if (localRelatedLists == null) {
            localRelatedLists = new com.salesforce.soap.partner.RelatedList[] {};
        }

        // update the setting tracker
        localRelatedListsTracker = true;

        java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localRelatedLists);
        list.add(param);
        this.localRelatedLists = (com.salesforce.soap.partner.RelatedList[]) list
                .toArray(new com.salesforce.soap.partner.RelatedList[list.size()]);

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
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":DescribeLayout",
                        xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "DescribeLayout", xmlWriter);
            }

        }
        if (localButtonLayoutSectionTracker) {
            if (localButtonLayoutSection == null) {
                throw new org.apache.axis2.databinding.ADBException("buttonLayoutSection cannot be null!!");
            }
            localButtonLayoutSection.serialize(
                    new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "buttonLayoutSection"), xmlWriter);
        }
        if (localDetailLayoutSectionsTracker) {
            if (localDetailLayoutSections != null) {
                for (int i = 0; i < localDetailLayoutSections.length; i++) {
                    if (localDetailLayoutSections[i] != null) {
                        localDetailLayoutSections[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                "detailLayoutSections"), xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("detailLayoutSections cannot be null!!");

            }
        }
        if (localEditLayoutSectionsTracker) {
            if (localEditLayoutSections != null) {
                for (int i = 0; i < localEditLayoutSections.length; i++) {
                    if (localEditLayoutSections[i] != null) {
                        localEditLayoutSections[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                "editLayoutSections"), xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("editLayoutSections cannot be null!!");

            }
        }
        if (localFeedViewTracker) {
            if (localFeedView == null) {
                throw new org.apache.axis2.databinding.ADBException("feedView cannot be null!!");
            }
            localFeedView.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedView"), xmlWriter);
        }
        if (localHighlightsPanelLayoutSectionTracker) {
            if (localHighlightsPanelLayoutSection == null) {
                throw new org.apache.axis2.databinding.ADBException("highlightsPanelLayoutSection cannot be null!!");
            }
            localHighlightsPanelLayoutSection.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                    "highlightsPanelLayoutSection"), xmlWriter);
        }
        if (localId == null) {

            writeStartElement(null, "urn:partner.soap.sforce.com", "id", xmlWriter);

            // write the nil attribute
            writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            xmlWriter.writeEndElement();
        } else {
            localId.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "id"), xmlWriter);
        }
        if (localQuickActionListTracker) {
            if (localQuickActionList == null) {
                throw new org.apache.axis2.databinding.ADBException("quickActionList cannot be null!!");
            }
            localQuickActionList.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "quickActionList"),
                    xmlWriter);
        }
        if (localRelatedContentTracker) {
            if (localRelatedContent == null) {
                throw new org.apache.axis2.databinding.ADBException("relatedContent cannot be null!!");
            }
            localRelatedContent.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relatedContent"),
                    xmlWriter);
        }
        if (localRelatedListsTracker) {
            if (localRelatedLists != null) {
                for (int i = 0; i < localRelatedLists.length; i++) {
                    if (localRelatedLists[i] != null) {
                        localRelatedLists[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                "relatedLists"), xmlWriter);
                    } else {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("relatedLists cannot be null!!");

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

        if (localButtonLayoutSectionTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "buttonLayoutSection"));

            if (localButtonLayoutSection == null) {
                throw new org.apache.axis2.databinding.ADBException("buttonLayoutSection cannot be null!!");
            }
            elementList.add(localButtonLayoutSection);
        }
        if (localDetailLayoutSectionsTracker) {
            if (localDetailLayoutSections != null) {
                for (int i = 0; i < localDetailLayoutSections.length; i++) {

                    if (localDetailLayoutSections[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "detailLayoutSections"));
                        elementList.add(localDetailLayoutSections[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("detailLayoutSections cannot be null!!");

            }

        }
        if (localEditLayoutSectionsTracker) {
            if (localEditLayoutSections != null) {
                for (int i = 0; i < localEditLayoutSections.length; i++) {

                    if (localEditLayoutSections[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "editLayoutSections"));
                        elementList.add(localEditLayoutSections[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("editLayoutSections cannot be null!!");

            }

        }
        if (localFeedViewTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedView"));

            if (localFeedView == null) {
                throw new org.apache.axis2.databinding.ADBException("feedView cannot be null!!");
            }
            elementList.add(localFeedView);
        }
        if (localHighlightsPanelLayoutSectionTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "highlightsPanelLayoutSection"));

            if (localHighlightsPanelLayoutSection == null) {
                throw new org.apache.axis2.databinding.ADBException("highlightsPanelLayoutSection cannot be null!!");
            }
            elementList.add(localHighlightsPanelLayoutSection);
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "id"));

        elementList.add(localId == null ? null : localId);
        if (localQuickActionListTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "quickActionList"));

            if (localQuickActionList == null) {
                throw new org.apache.axis2.databinding.ADBException("quickActionList cannot be null!!");
            }
            elementList.add(localQuickActionList);
        }
        if (localRelatedContentTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relatedContent"));

            if (localRelatedContent == null) {
                throw new org.apache.axis2.databinding.ADBException("relatedContent cannot be null!!");
            }
            elementList.add(localRelatedContent);
        }
        if (localRelatedListsTracker) {
            if (localRelatedLists != null) {
                for (int i = 0; i < localRelatedLists.length; i++) {

                    if (localRelatedLists[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relatedLists"));
                        elementList.add(localRelatedLists[i]);
                    } else {

                        // nothing to do

                    }

                }
            } else {

                throw new org.apache.axis2.databinding.ADBException("relatedLists cannot be null!!");

            }

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
        public static DescribeLayout parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            DescribeLayout object = new DescribeLayout();

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

                        if (!"DescribeLayout".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (DescribeLayout) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(nsUri,
                                    type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list2 = new java.util.ArrayList();

                java.util.ArrayList list3 = new java.util.ArrayList();

                java.util.ArrayList list9 = new java.util.ArrayList();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "buttonLayoutSection").equals(reader
                                .getName())) {

                    object.setButtonLayoutSection(com.salesforce.soap.partner.DescribeLayoutButtonSection.Factory.parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "detailLayoutSections").equals(reader
                                .getName())) {

                    // Process the array and step past its final element's end.
                    list2.add(com.salesforce.soap.partner.DescribeLayoutSection.Factory.parse(reader));

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
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "detailLayoutSections")
                                    .equals(reader.getName())) {
                                list2.add(com.salesforce.soap.partner.DescribeLayoutSection.Factory.parse(reader));

                            } else {
                                loopDone2 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setDetailLayoutSections((com.salesforce.soap.partner.DescribeLayoutSection[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeLayoutSection.class, list2));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "editLayoutSections").equals(reader
                                .getName())) {

                    // Process the array and step past its final element's end.
                    list3.add(com.salesforce.soap.partner.DescribeLayoutSection.Factory.parse(reader));

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
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "editLayoutSections").equals(reader
                                    .getName())) {
                                list3.add(com.salesforce.soap.partner.DescribeLayoutSection.Factory.parse(reader));

                            } else {
                                loopDone3 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setEditLayoutSections((com.salesforce.soap.partner.DescribeLayoutSection[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.DescribeLayoutSection.class, list3));

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "feedView").equals(reader.getName())) {

                    object.setFeedView(com.salesforce.soap.partner.DescribeLayoutFeedView.Factory.parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "highlightsPanelLayoutSection")
                                .equals(reader.getName())) {

                    object.setHighlightsPanelLayoutSection(com.salesforce.soap.partner.DescribeLayoutSection.Factory
                            .parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "id").equals(reader.getName())) {

                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                        object.setId(null);
                        reader.next();

                        reader.next();

                    } else {

                        object.setId(com.salesforce.soap.partner.ID.Factory.parse(reader));

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
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relatedContent")
                                .equals(reader.getName())) {

                    object.setRelatedContent(com.salesforce.soap.partner.RelatedContent.Factory.parse(reader));

                    reader.next();

                }  // End of if for expected property start element

                else {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relatedLists").equals(reader.getName())) {

                    // Process the array and step past its final element's end.
                    list9.add(com.salesforce.soap.partner.RelatedList.Factory.parse(reader));

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
                            if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relatedLists").equals(reader
                                    .getName())) {
                                list9.add(com.salesforce.soap.partner.RelatedList.Factory.parse(reader));

                            } else {
                                loopDone9 = true;
                            }
                        }
                    }
                    // call the converter utility to convert and set the array

                    object.setRelatedLists((com.salesforce.soap.partner.RelatedList[]) org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToArray(com.salesforce.soap.partner.RelatedList.class, list9));

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
