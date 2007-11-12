// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.registeruser.proxy;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 */
public class UserRegistration implements java.io.Serializable {

    private int id;

    private java.lang.String email;

    private java.lang.String country;

    private java.lang.String designerVersion;

    private java.lang.String productname;

    private java.lang.String registrationDate;

    public UserRegistration() {
    }

    public UserRegistration(int id, java.lang.String email, java.lang.String country, java.lang.String designerVersion,
            java.lang.String productname, java.lang.String registrationDate) {
        this.id = id;
        this.email = email;
        this.country = country;
        this.designerVersion = designerVersion;
        this.productname = productname;
        this.registrationDate = registrationDate;
    }

    /**
     * Gets the id value for this UserRegistration.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id value for this UserRegistration.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the email value for this UserRegistration.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }

    /**
     * Sets the email value for this UserRegistration.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    /**
     * Gets the country value for this UserRegistration.
     * 
     * @return country
     */
    public java.lang.String getCountry() {
        return country;
    }

    /**
     * Sets the country value for this UserRegistration.
     * 
     * @param country
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }

    /**
     * Gets the designer_version value for this UserRegistration.
     * 
     * @return designer_version
     */
    public java.lang.String getDesignerVersion() {
        return designerVersion;
    }

    /**
     * Sets the designer_version value for this UserRegistration.
     * 
     * @param designer_version
     */
    public void setDesignerVersion(java.lang.String designerVersion) {
        this.designerVersion = designerVersion;
    }

    /**
     * Gets the productname value for this UserRegistration.
     * 
     * @return productname
     */
    public java.lang.String getProductname() {
        return productname;
    }

    /**
     * Sets the productname value for this UserRegistration.
     * 
     * @param productname
     */
    public void setProductname(java.lang.String productname) {
        this.productname = productname;
    }

    /**
     * Gets the registration_date value for this UserRegistration.
     * 
     * @return registration_date
     */
    public java.lang.String getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the registration_date value for this UserRegistration.
     * 
     * @param registration_date
     */
    public void setRegistrationDate(java.lang.String registrationDate) {
        this.registrationDate = registrationDate;
    }

    private java.lang.Object equalsCalc = null;

    @Override
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserRegistration))
            return false;
        UserRegistration other = (UserRegistration) obj;
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (equalsCalc != null) {
            return (equalsCalc == obj);
        }
        equalsCalc = obj;
        boolean isEquals;
        isEquals = true
                && this.id == other.getId()
                && ((this.email == null && other.getEmail() == null) || (this.email != null && this.email.equals(other
                        .getEmail())))
                && ((this.country == null && other.getCountry() == null) || (this.country != null && this.country
                        .equals(other.getCountry())))
                && ((this.designerVersion == null && other.getDesignerVersion() == null) || (this.designerVersion != null && this.designerVersion
                        .equals(other.getDesignerVersion())))
                && ((this.productname == null && other.getProductname() == null) || (this.productname != null && this.productname
                        .equals(other.getProductname())))
                && ((this.registrationDate == null && other.getRegistrationDate() == null) || (this.registrationDate != null && this.registrationDate
                        .equals(other.getRegistrationDate())));
        equalsCalc = null;
        return isEquals;
    }

    private boolean hashCodeCalc = false;

    @Override
    public synchronized int hashCode() {
        if (hashCodeCalc) {
            return 0;
        }
        hashCodeCalc = true;
        int hashCode = 1;
        hashCode += getId();
        if (getEmail() != null) {
            hashCode += getEmail().hashCode();
        }
        if (getCountry() != null) {
            hashCode += getCountry().hashCode();
        }
        if (getDesignerVersion() != null) {
            hashCode += getDesignerVersion().hashCode();
        }
        if (getProductname() != null) {
            hashCode += getProductname().hashCode();
        }
        if (getRegistrationDate() != null) {
            hashCode += getRegistrationDate().hashCode();
        }
        hashCodeCalc = false;
        return hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
            UserRegistration.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.talend.com/TalendRegisterWS/wsdl",
                "UserRegistration"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("country");
        elemField.setXmlName(new javax.xml.namespace.QName("", "country"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("designer_version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "designer_version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productname");
        elemField.setXmlName(new javax.xml.namespace.QName("", "productname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registration_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registration_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
            java.lang.Class javaType, javax.xml.namespace.QName xmlType) {
        return new org.apache.axis.encoding.ser.BeanSerializer(javaType, xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
            java.lang.Class javaType, javax.xml.namespace.QName xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(javaType, xmlType, typeDesc);
    }
}
