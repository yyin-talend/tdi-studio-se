/**
 * Field.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.5.2 Built on : Sep 06, 2010 (09:42:47 CEST)
 */

package com.salesforce.soap.partner;

/**
 * Field bean class
 */

public class Field implements org.apache.axis2.databinding.ADBBean {

    /*
     * This type was generated from the piece of schema that had name = Field Namespace URI =
     * urn:partner.soap.sforce.com Namespace Prefix = ns1
     */

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("urn:partner.soap.sforce.com")) {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for AutoNumber
     */

    protected boolean localAutoNumber;

    /**
     * field for ByteLength
     */

    protected int localByteLength;

    /**
     * field for Calculated
     */

    protected boolean localCalculated;

    /**
     * field for CalculatedFormula
     */

    protected java.lang.String localCalculatedFormula;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localCalculatedFormulaTracker = false;

    /**
     * field for CaseSensitive
     */

    protected boolean localCaseSensitive;

    /**
     * field for ControllerName
     */

    protected java.lang.String localControllerName;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localControllerNameTracker = false;

    /**
     * field for Createable
     */

    protected boolean localCreateable;

    /**
     * field for Custom
     */

    protected boolean localCustom;

    /**
     * field for DefaultValueFormula
     */

    protected java.lang.String localDefaultValueFormula;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localDefaultValueFormulaTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getDefaultValueFormula() {
        return localDefaultValueFormula;
    }

    /**
     * Auto generated setter method
     * 
     * @param param DefaultValueFormula
     */
    public void setDefaultValueFormula(java.lang.String param) {

        if (param != null) {
            // update the setting tracker
            localDefaultValueFormulaTracker = true;
        } else {
            localDefaultValueFormulaTracker = false;

        }

        this.localDefaultValueFormula = param;

    }

    /**
     * field for DefaultedOnCreate
     */

    protected boolean localDefaultedOnCreate;

    /**
     * field for DependentPicklist
     */

    protected boolean localDependentPicklist;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localDependentPicklistTracker = false;

    /**
     * field for DeprecatedAndHidden
     */

    protected boolean localDeprecatedAndHidden;

    /**
     * field for Digits
     */

    protected int localDigits;

    /**
     * field for ExternalId
     */

    protected boolean localExternalId;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localExternalIdTracker = false;

    /**
     * field for Filterable
     */

    protected boolean localFilterable;

    /**
     * field for Groupable
     */

    protected boolean localGroupable;

    /**
     * field for HtmlFormatted
     */

    protected boolean localHtmlFormatted;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localHtmlFormattedTracker = false;

    /**
     * field for IdLookup
     */

    protected boolean localIdLookup;

    /**
     * field for InlineHelpText
     */

    protected java.lang.String localInlineHelpText;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localInlineHelpTextTracker = false;

    /**
     * field for Label
     */

    protected java.lang.String localLabel;

    /**
     * field for Length
     */

    protected int localLength;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getLength() {
        return localLength;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Length
     */
    public void setLength(int param) {

        this.localLength = param;

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
     * field for NameField
     */

    protected boolean localNameField;

    /**
     * field for NamePointing
     */

    protected boolean localNamePointing;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localNamePointingTracker = false;

    /**
     * field for Nillable
     */

    protected boolean localNillable;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getNillable() {
        return localNillable;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Nillable
     */
    public void setNillable(boolean param) {

        this.localNillable = param;

    }

    /**
     * field for PicklistValues This was an Array!
     */

    protected com.salesforce.soap.partner.PicklistEntry[] localPicklistValues;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localPicklistValuesTracker = false;

    /**
     * validate the array for PicklistValues
     */
    protected void validatePicklistValues(com.salesforce.soap.partner.PicklistEntry[] param) {

    }

    /**
     * field for Precision
     */

    protected int localPrecision;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getPrecision() {
        return localPrecision;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Precision
     */
    public void setPrecision(int param) {

        this.localPrecision = param;

    }

    /**
     * field for ReferenceTo This was an Array!
     */

    protected java.lang.String[] localReferenceTo;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localReferenceToTracker = false;

    /**
     * validate the array for ReferenceTo
     */
    protected void validateReferenceTo(java.lang.String[] param) {

    }

    /**
     * field for RelationshipName
     */

    protected java.lang.String localRelationshipName;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localRelationshipNameTracker = false;

    /**
     * field for RelationshipOrder
     */

    protected int localRelationshipOrder;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localRelationshipOrderTracker = false;

    /**
     * field for RestrictedPicklist
     */

    protected boolean localRestrictedPicklist;

    /**
     * field for Scale
     */

    protected int localScale;

    /**
     * field for SoapType
     */

    protected com.salesforce.soap.partner.SoapType localSoapType;

    /**
     * field for Sortable
     */

    protected boolean localSortable;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localSortableTracker = false;

    /**
     * field for Type
     */

    protected com.salesforce.soap.partner.FieldType localType;

    /**
     * Auto generated getter method
     * 
     * @return com.salesforce.soap.partner.FieldType
     */
    public com.salesforce.soap.partner.FieldType getType() {
        return localType;
    }

    /**
     * Auto generated setter method
     * 
     * @param param Type
     */
    public void setType(com.salesforce.soap.partner.FieldType param) {

        this.localType = param;

    }

    /**
     * field for Unique
     */

    protected boolean localUnique;

    /**
     * field for Updateable
     */

    protected boolean localUpdateable;

    /**
     * field for WriteRequiresMasterRead
     */

    protected boolean localWriteRequiresMasterRead;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method for this attribute. It will be
     * used to determine whether to include this field in the serialized XML
     */
    protected boolean localWriteRequiresMasterReadTracker = false;

    /**
     * isReaderMTOMAware
     * 
     * @return true if the reader supports MTOM
     */
    public static boolean isReaderMTOMAware(javax.xml.stream.XMLStreamReader reader) {
        boolean isReaderMTOMAware = false;

        try {
            isReaderMTOMAware = java.lang.Boolean.TRUE.equals(reader
                    .getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        } catch (java.lang.IllegalArgumentException e) {
            isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
    }

    /**
     * 
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {

        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName) {

            public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                    throws javax.xml.stream.XMLStreamException {
                Field.this.serialize(parentQName, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(parentQName, factory, dataSource);

    }

    public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
            org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
        serialize(parentQName, factory, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
            org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter, boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {

        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();

        if ((namespace != null) && (namespace.trim().length() > 0)) {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
            } else {
                if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(), namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        } else {
            xmlWriter.writeStartElement(parentQName.getLocalPart());
        }

        if (serializeType) {

            java.lang.String namespacePrefix = registerPrefix(xmlWriter, "urn:partner.soap.sforce.com");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":Field", xmlWriter);
            } else {
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "Field", xmlWriter);
            }

        }

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "autoNumber", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "autoNumber");
            }

        } else {
            xmlWriter.writeStartElement("autoNumber");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("autoNumber cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAutoNumber));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "byteLength", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "byteLength");
            }

        } else {
            xmlWriter.writeStartElement("byteLength");
        }

        if (localByteLength == java.lang.Integer.MIN_VALUE) {

            throw new org.apache.axis2.databinding.ADBException("byteLength cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localByteLength));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "calculated", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "calculated");
            }

        } else {
            xmlWriter.writeStartElement("calculated");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("calculated cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCalculated));
        }

        xmlWriter.writeEndElement();
        if (localCalculatedFormulaTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "calculatedFormula", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "calculatedFormula");
                }

            } else {
                xmlWriter.writeStartElement("calculatedFormula");
            }

            if (localCalculatedFormula == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("calculatedFormula cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localCalculatedFormula);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "caseSensitive", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "caseSensitive");
            }

        } else {
            xmlWriter.writeStartElement("caseSensitive");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("caseSensitive cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCaseSensitive));
        }

        xmlWriter.writeEndElement();
        if (localControllerNameTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "controllerName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "controllerName");
                }

            } else {
                xmlWriter.writeStartElement("controllerName");
            }

            if (localControllerName == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("controllerName cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localControllerName);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "createable", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "createable");
            }

        } else {
            xmlWriter.writeStartElement("createable");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("createable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreateable));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "custom", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "custom");
            }

        } else {
            xmlWriter.writeStartElement("custom");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("custom cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustom));
        }

        xmlWriter.writeEndElement();
        if (localDefaultValueFormulaTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "defaultValueFormula", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "defaultValueFormula");
                }

            } else {
                xmlWriter.writeStartElement("defaultValueFormula");
            }

            if (localDefaultValueFormula == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("defaultValueFormula cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localDefaultValueFormula);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "defaultedOnCreate", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "defaultedOnCreate");
            }

        } else {
            xmlWriter.writeStartElement("defaultedOnCreate");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("defaultedOnCreate cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDefaultedOnCreate));
        }

        xmlWriter.writeEndElement();
        if (localDependentPicklistTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "dependentPicklist", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "dependentPicklist");
                }

            } else {
                xmlWriter.writeStartElement("dependentPicklist");
            }

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("dependentPicklist cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(localDependentPicklist));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "deprecatedAndHidden", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "deprecatedAndHidden");
            }

        } else {
            xmlWriter.writeStartElement("deprecatedAndHidden");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("deprecatedAndHidden cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeprecatedAndHidden));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "digits", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "digits");
            }

        } else {
            xmlWriter.writeStartElement("digits");
        }

        if (localDigits == java.lang.Integer.MIN_VALUE) {

            throw new org.apache.axis2.databinding.ADBException("digits cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDigits));
        }

        xmlWriter.writeEndElement();
        if (localExternalIdTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "externalId", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "externalId");
                }

            } else {
                xmlWriter.writeStartElement("externalId");
            }

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("externalId cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExternalId));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "filterable", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "filterable");
            }

        } else {
            xmlWriter.writeStartElement("filterable");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("filterable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFilterable));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "groupable", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "groupable");
            }

        } else {
            xmlWriter.writeStartElement("groupable");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("groupable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localGroupable));
        }

        xmlWriter.writeEndElement();
        if (localHtmlFormattedTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "htmlFormatted", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "htmlFormatted");
                }

            } else {
                xmlWriter.writeStartElement("htmlFormatted");
            }

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("htmlFormatted cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHtmlFormatted));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "idLookup", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "idLookup");
            }

        } else {
            xmlWriter.writeStartElement("idLookup");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("idLookup cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIdLookup));
        }

        xmlWriter.writeEndElement();
        if (localInlineHelpTextTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "inlineHelpText", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "inlineHelpText");
                }

            } else {
                xmlWriter.writeStartElement("inlineHelpText");
            }

            if (localInlineHelpText == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("inlineHelpText cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localInlineHelpText);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "label", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "label");
            }

        } else {
            xmlWriter.writeStartElement("label");
        }

        if (localLabel == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("label cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localLabel);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "length", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "length");
            }

        } else {
            xmlWriter.writeStartElement("length");
        }

        if (localLength == java.lang.Integer.MIN_VALUE) {

            throw new org.apache.axis2.databinding.ADBException("length cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLength));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "name", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "name");
            }

        } else {
            xmlWriter.writeStartElement("name");
        }

        if (localName == null) {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("name cannot be null!!");

        } else {

            xmlWriter.writeCharacters(localName);

        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "nameField", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "nameField");
            }

        } else {
            xmlWriter.writeStartElement("nameField");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("nameField cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNameField));
        }

        xmlWriter.writeEndElement();
        if (localNamePointingTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "namePointing", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "namePointing");
                }

            } else {
                xmlWriter.writeStartElement("namePointing");
            }

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("namePointing cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNamePointing));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "nillable", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "nillable");
            }

        } else {
            xmlWriter.writeStartElement("nillable");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("nillable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNillable));
        }

        xmlWriter.writeEndElement();
        if (localPicklistValuesTracker) {
            if (localPicklistValues != null) {
                for (int i = 0; i < localPicklistValues.length; i++) {
                    if (localPicklistValues[i] != null) {
                        localPicklistValues[i].serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                "picklistValues"), factory, xmlWriter);
                    } else {

                        // write null attribute
                        java.lang.String namespace2 = "urn:partner.soap.sforce.com";
                        if (!namespace2.equals("")) {
                            java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                            if (prefix2 == null) {
                                prefix2 = generatePrefix(namespace2);

                                xmlWriter.writeStartElement(prefix2, "picklistValues", namespace2);
                                xmlWriter.writeNamespace(prefix2, namespace2);
                                xmlWriter.setPrefix(prefix2, namespace2);

                            } else {
                                xmlWriter.writeStartElement(namespace2, "picklistValues");
                            }

                        } else {
                            xmlWriter.writeStartElement("picklistValues");
                        }

                        // write the nil attribute
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();

                    }

                }
            } else {

                // write null attribute
                java.lang.String namespace2 = "urn:partner.soap.sforce.com";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);

                        xmlWriter.writeStartElement(prefix2, "picklistValues", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);

                    } else {
                        xmlWriter.writeStartElement(namespace2, "picklistValues");
                    }

                } else {
                    xmlWriter.writeStartElement("picklistValues");
                }

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();

            }
        }
        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "precision", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "precision");
            }

        } else {
            xmlWriter.writeStartElement("precision");
        }

        if (localPrecision == java.lang.Integer.MIN_VALUE) {

            throw new org.apache.axis2.databinding.ADBException("precision cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPrecision));
        }

        xmlWriter.writeEndElement();
        if (localReferenceToTracker) {
            if (localReferenceTo != null) {
                namespace = "urn:partner.soap.sforce.com";
                boolean emptyNamespace = namespace == null || namespace.length() == 0;
                prefix = emptyNamespace ? null : xmlWriter.getPrefix(namespace);
                for (int i = 0; i < localReferenceTo.length; i++) {

                    if (localReferenceTo[i] != null) {

                        if (!emptyNamespace) {
                            if (prefix == null) {
                                java.lang.String prefix2 = generatePrefix(namespace);

                                xmlWriter.writeStartElement(prefix2, "referenceTo", namespace);
                                xmlWriter.writeNamespace(prefix2, namespace);
                                xmlWriter.setPrefix(prefix2, namespace);

                            } else {
                                xmlWriter.writeStartElement(namespace, "referenceTo");
                            }

                        } else {
                            xmlWriter.writeStartElement("referenceTo");
                        }

                        xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(localReferenceTo[i]));

                        xmlWriter.writeEndElement();

                    } else {

                        // write null attribute
                        namespace = "urn:partner.soap.sforce.com";
                        if (!namespace.equals("")) {
                            prefix = xmlWriter.getPrefix(namespace);

                            if (prefix == null) {
                                prefix = generatePrefix(namespace);

                                xmlWriter.writeStartElement(prefix, "referenceTo", namespace);
                                xmlWriter.writeNamespace(prefix, namespace);
                                xmlWriter.setPrefix(prefix, namespace);

                            } else {
                                xmlWriter.writeStartElement(namespace, "referenceTo");
                            }

                        } else {
                            xmlWriter.writeStartElement("referenceTo");
                        }
                        writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                        xmlWriter.writeEndElement();

                    }

                }
            } else {

                // write the null attribute
                // write null attribute
                java.lang.String namespace2 = "urn:partner.soap.sforce.com";
                if (!namespace2.equals("")) {
                    java.lang.String prefix2 = xmlWriter.getPrefix(namespace2);

                    if (prefix2 == null) {
                        prefix2 = generatePrefix(namespace2);

                        xmlWriter.writeStartElement(prefix2, "referenceTo", namespace2);
                        xmlWriter.writeNamespace(prefix2, namespace2);
                        xmlWriter.setPrefix(prefix2, namespace2);

                    } else {
                        xmlWriter.writeStartElement(namespace2, "referenceTo");
                    }

                } else {
                    xmlWriter.writeStartElement("referenceTo");
                }

                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
                xmlWriter.writeEndElement();

            }

        }
        if (localRelationshipNameTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "relationshipName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "relationshipName");
                }

            } else {
                xmlWriter.writeStartElement("relationshipName");
            }

            if (localRelationshipName == null) {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("relationshipName cannot be null!!");

            } else {

                xmlWriter.writeCharacters(localRelationshipName);

            }

            xmlWriter.writeEndElement();
        }
        if (localRelationshipOrderTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "relationshipOrder", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "relationshipOrder");
                }

            } else {
                xmlWriter.writeStartElement("relationshipOrder");
            }

            if (localRelationshipOrder == java.lang.Integer.MIN_VALUE) {

                throw new org.apache.axis2.databinding.ADBException("relationshipOrder cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(localRelationshipOrder));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "restrictedPicklist", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "restrictedPicklist");
            }

        } else {
            xmlWriter.writeStartElement("restrictedPicklist");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("restrictedPicklist cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRestrictedPicklist));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "scale", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "scale");
            }

        } else {
            xmlWriter.writeStartElement("scale");
        }

        if (localScale == java.lang.Integer.MIN_VALUE) {

            throw new org.apache.axis2.databinding.ADBException("scale cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localScale));
        }

        xmlWriter.writeEndElement();

        if (localSoapType == null) {
            throw new org.apache.axis2.databinding.ADBException("soapType cannot be null!!");
        }
        localSoapType.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "soapType"), factory, xmlWriter);
        if (localSortableTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "sortable", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "sortable");
                }

            } else {
                xmlWriter.writeStartElement("sortable");
            }

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("sortable cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSortable));
            }

            xmlWriter.writeEndElement();
        }
        if (localType == null) {
            throw new org.apache.axis2.databinding.ADBException("type cannot be null!!");
        }
        localType.serialize(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type"), factory, xmlWriter);

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "unique", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "unique");
            }

        } else {
            xmlWriter.writeStartElement("unique");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("unique cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUnique));
        }

        xmlWriter.writeEndElement();

        namespace = "urn:partner.soap.sforce.com";
        if (!namespace.equals("")) {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "updateable", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            } else {
                xmlWriter.writeStartElement(namespace, "updateable");
            }

        } else {
            xmlWriter.writeStartElement("updateable");
        }

        if (false) {

            throw new org.apache.axis2.databinding.ADBException("updateable cannot be null!!");

        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUpdateable));
        }

        xmlWriter.writeEndElement();
        if (localWriteRequiresMasterReadTracker) {
            namespace = "urn:partner.soap.sforce.com";
            if (!namespace.equals("")) {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "writeRequiresMasterRead", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                } else {
                    xmlWriter.writeStartElement(namespace, "writeRequiresMasterRead");
                }

            } else {
                xmlWriter.writeStartElement("writeRequiresMasterRead");
            }

            if (false) {

                throw new org.apache.axis2.databinding.ADBException("writeRequiresMasterRead cannot be null!!");

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(localWriteRequiresMasterRead));
            }

            xmlWriter.writeEndElement();
        }
        xmlWriter.writeEndElement();

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

            while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
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

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "autoNumber"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAutoNumber));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "byteLength"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localByteLength));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "calculated"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCalculated));
        if (localCalculatedFormulaTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "calculatedFormula"));

            if (localCalculatedFormula != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCalculatedFormula));
            } else {
                throw new org.apache.axis2.databinding.ADBException("calculatedFormula cannot be null!!");
            }
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "caseSensitive"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCaseSensitive));
        if (localControllerNameTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "controllerName"));

            if (localControllerName != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localControllerName));
            } else {
                throw new org.apache.axis2.databinding.ADBException("controllerName cannot be null!!");
            }
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "createable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCreateable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "custom"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustom));
        if (localDefaultValueFormulaTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValueFormula"));

            if (localDefaultValueFormula != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDefaultValueFormula));
            } else {
                throw new org.apache.axis2.databinding.ADBException("defaultValueFormula cannot be null!!");
            }
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultedOnCreate"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDefaultedOnCreate));
        if (localDependentPicklistTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "dependentPicklist"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDependentPicklist));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deprecatedAndHidden"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeprecatedAndHidden));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "digits"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDigits));
        if (localExternalIdTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "externalId"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExternalId));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "filterable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFilterable));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "groupable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localGroupable));
        if (localHtmlFormattedTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "htmlFormatted"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHtmlFormatted));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "idLookup"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIdLookup));
        if (localInlineHelpTextTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "inlineHelpText"));

            if (localInlineHelpText != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInlineHelpText));
            } else {
                throw new org.apache.axis2.databinding.ADBException("inlineHelpText cannot be null!!");
            }
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "label"));

        if (localLabel != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLabel));
        } else {
            throw new org.apache.axis2.databinding.ADBException("label cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "length"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLength));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name"));

        if (localName != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localName));
        } else {
            throw new org.apache.axis2.databinding.ADBException("name cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "nameField"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNameField));
        if (localNamePointingTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "namePointing"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNamePointing));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "nillable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNillable));
        if (localPicklistValuesTracker) {
            if (localPicklistValues != null) {
                for (int i = 0; i < localPicklistValues.length; i++) {

                    if (localPicklistValues[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "picklistValues"));
                        elementList.add(localPicklistValues[i]);
                    } else {

                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "picklistValues"));
                        elementList.add(null);

                    }

                }
            } else {

                elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "picklistValues"));
                elementList.add(localPicklistValues);

            }

        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "precision"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPrecision));
        if (localReferenceToTracker) {
            if (localReferenceTo != null) {
                for (int i = 0; i < localReferenceTo.length; i++) {

                    if (localReferenceTo[i] != null) {
                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTo"));
                        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReferenceTo[i]));
                    } else {

                        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTo"));
                        elementList.add(null);

                    }

                }
            } else {

                elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTo"));
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
        if (localRelationshipOrderTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relationshipOrder"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRelationshipOrder));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "restrictedPicklist"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRestrictedPicklist));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "scale"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localScale));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "soapType"));

        if (localSoapType == null) {
            throw new org.apache.axis2.databinding.ADBException("soapType cannot be null!!");
        }
        elementList.add(localSoapType);
        if (localSortableTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sortable"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSortable));
        }
        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type"));

        if (localType == null) {
            throw new org.apache.axis2.databinding.ADBException("type cannot be null!!");
        }
        elementList.add(localType);

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "unique"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUnique));

        elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "updateable"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUpdateable));
        if (localWriteRequiresMasterReadTracker) {
            elementList.add(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "writeRequiresMasterRead"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWriteRequiresMasterRead));
        }

        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(),
                attribList.toArray());

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory {

        /**
         * static method to create the object Precondition: If this object is an element, the current or next start
         * element starts this object and any intervening reader events are ignorable If this object is not an element,
         * it is a complex type and the reader is at the event just after the outer start element Postcondition: If this
         * object is an element, the reader is positioned at its end element If this object is a complex type, the
         * reader is positioned at the end element of its outer element
         */
        public static Field parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            Field object = new Field();

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

                        if (!"Field".equals(type)) {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (Field) com.salesforce.soap.partner.sobject.ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                java.util.ArrayList list26 = new java.util.ArrayList();

                java.util.ArrayList list28 = new java.util.ArrayList();

                // code by bchen
                while (!reader.isEndElement()
                        || !new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields").equals(reader.getName())) {
                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name").equals(reader.getName())) {

                        java.lang.String content = reader.getElementText();

                        object.setName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        reader.next();

                    } // End of if for expected property start element
                    else if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type").equals(reader.getName())) {

                        object.setType(com.salesforce.soap.partner.FieldType.Factory.parse(reader));

                        reader.next();

                    } // End of if for expected property start element
                    else if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "precision").equals(reader.getName())) {

                        java.lang.String content = reader.getElementText();

                        object.setPrecision(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                        reader.next();

                    } // End of if for expected property start element
                    else if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "nillable").equals(reader.getName())) {

                        java.lang.String content = reader.getElementText();

                        object.setNillable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    } // End of if for expected property start element
                    else if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "length").equals(reader.getName())) {

                        java.lang.String content = reader.getElementText();

                        object.setLength(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                        reader.next();

                    } // End of if for expected property start element
                    else if (reader.isStartElement()
                            && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValueFormula").equals(reader
                                    .getName())) {

                        java.lang.String content = reader.getElementText();

                        object.setDefaultValueFormula(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        reader.next();

                    } // End of if for expected property start element
                    else {
                        reader.next();
                    }
                }
                // end code by bchen

                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "autoNumber").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setAutoNumber(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "byteLength").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setByteLength(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "calculated").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setCalculated(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "calculatedFormula").equals(reader
                // .getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setCalculatedFormula(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "caseSensitive").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setCaseSensitive(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "controllerName")
                // .equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setControllerName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "createable").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setCreateable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "custom").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setCustom(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultValueFormula").equals(reader
                // .getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setDefaultValueFormula(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultedOnCreate").equals(reader
                // .getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setDefaultedOnCreate(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "dependentPicklist").equals(reader
                // .getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setDependentPicklist(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deprecatedAndHidden").equals(reader
                // .getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setDeprecatedAndHidden(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "digits").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setDigits(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "externalId").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setExternalId(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "filterable").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setFilterable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "groupable").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setGroupable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "htmlFormatted").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setHtmlFormatted(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "idLookup").equals(reader.getName()))
                // {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setIdLookup(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "inlineHelpText")
                // .equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setInlineHelpText(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "label").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setLabel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "length").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setLength(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "nameField").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setNameField(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "namePointing").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setNamePointing(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "nillable").equals(reader.getName()))
                // {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setNillable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "picklistValues")
                // .equals(reader.getName())) {
                //
                // // Process the array and step past its final element's end.
                //
                // nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                // if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                // list26.add(null);
                // reader.next();
                // } else {
                // list26.add(com.salesforce.soap.partner.PicklistEntry.Factory.parse(reader));
                // }
                // // loop until we find a start element that is not part of this array
                // boolean loopDone26 = false;
                // while (!loopDone26) {
                // // We should be at the end element, but make sure
                // while (!reader.isEndElement())
                // reader.next();
                // // Step out of this element
                // reader.next();
                // // Step to next element event.
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                // if (reader.isEndElement()) {
                // // two continuous end elements means we are exiting the xml structure
                // loopDone26 = true;
                // } else {
                // if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "picklistValues").equals(reader
                // .getName())) {
                //
                // nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                // if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                // list26.add(null);
                // reader.next();
                // } else {
                // list26.add(com.salesforce.soap.partner.PicklistEntry.Factory.parse(reader));
                // }
                // } else {
                // loopDone26 = true;
                // }
                // }
                // }
                // // call the converter utility to convert and set the array
                //
                // object.setPicklistValues((com.salesforce.soap.partner.PicklistEntry[])
                // org.apache.axis2.databinding.utils.ConverterUtil
                // .convertToArray(com.salesforce.soap.partner.PicklistEntry.class, list26));
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "precision").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setPrecision(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "referenceTo").equals(reader.getName())) {
                //
                // // Process the array and step past its final element's end.
                //
                // nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                // if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                // list28.add(null);
                //
                // reader.next();
                // } else {
                // list28.add(reader.getElementText());
                // }
                // // loop until we find a start element that is not part of this array
                // boolean loopDone28 = false;
                // while (!loopDone28) {
                // // Ensure we are at the EndElement
                // while (!reader.isEndElement()) {
                // reader.next();
                // }
                // // Step out of this element
                // reader.next();
                // // Step to next element event.
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                // if (reader.isEndElement()) {
                // // two continuous end elements means we are exiting the xml structure
                // loopDone28 = true;
                // } else {
                // if (new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "referenceTo").equals(reader
                // .getName())) {
                //
                // nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
                // if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
                // list28.add(null);
                //
                // reader.next();
                // } else {
                // list28.add(reader.getElementText());
                // }
                // } else {
                // loopDone28 = true;
                // }
                // }
                // }
                // // call the converter utility to convert and set the array
                //
                // object.setReferenceTo((java.lang.String[]) list28.toArray(new java.lang.String[list28.size()]));
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relationshipName").equals(reader
                // .getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setRelationshipName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relationshipOrder").equals(reader
                // .getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setRelationshipOrder(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // object.setRelationshipOrder(java.lang.Integer.MIN_VALUE);
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "restrictedPicklist").equals(reader
                // .getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setRestrictedPicklist(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "scale").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setScale(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "soapType").equals(reader.getName()))
                // {
                //
                // object.setSoapType(com.salesforce.soap.partner.SoapType.Factory.parse(reader));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sortable").equals(reader.getName()))
                // {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setSortable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type").equals(reader.getName())) {
                //
                // object.setType(com.salesforce.soap.partner.FieldType.Factory.parse(reader));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "unique").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setUnique(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "updateable").equals(reader.getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setUpdateable(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                // // A start element we are not expecting indicates an invalid parameter was passed
                // throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " +
                // reader.getLocalName());
                // }
                //
                // while (!reader.isStartElement() && !reader.isEndElement())
                // reader.next();
                //
                // if (reader.isStartElement()
                // && new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                // "writeRequiresMasterRead").equals(reader
                // .getName())) {
                //
                // java.lang.String content = reader.getElementText();
                //
                // object.setWriteRequiresMasterRead(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                //
                // reader.next();
                //
                // } // End of if for expected property start element
                //
                // else {
                //
                // }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement())
                    // A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());

            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }
    }// end of factory class

}
