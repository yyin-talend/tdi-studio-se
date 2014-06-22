/**
 * ParamsRequestCampaign.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.marketo.www.mktows;

public class ParamsRequestCampaign  implements java.io.Serializable {
    private com.marketo.www.mktows.ReqCampSourceType source;

    private int campaignId;

    private com.marketo.www.mktows.LeadKey[] leadList;

    public ParamsRequestCampaign() {
    }

    public ParamsRequestCampaign(
           com.marketo.www.mktows.ReqCampSourceType source,
           int campaignId,
           com.marketo.www.mktows.LeadKey[] leadList) {
           this.source = source;
           this.campaignId = campaignId;
           this.leadList = leadList;
    }


    /**
     * Gets the source value for this ParamsRequestCampaign.
     * 
     * @return source
     */
    public com.marketo.www.mktows.ReqCampSourceType getSource() {
        return source;
    }


    /**
     * Sets the source value for this ParamsRequestCampaign.
     * 
     * @param source
     */
    public void setSource(com.marketo.www.mktows.ReqCampSourceType source) {
        this.source = source;
    }


    /**
     * Gets the campaignId value for this ParamsRequestCampaign.
     * 
     * @return campaignId
     */
    public int getCampaignId() {
        return campaignId;
    }


    /**
     * Sets the campaignId value for this ParamsRequestCampaign.
     * 
     * @param campaignId
     */
    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }


    /**
     * Gets the leadList value for this ParamsRequestCampaign.
     * 
     * @return leadList
     */
    public com.marketo.www.mktows.LeadKey[] getLeadList() {
        return leadList;
    }


    /**
     * Sets the leadList value for this ParamsRequestCampaign.
     * 
     * @param leadList
     */
    public void setLeadList(com.marketo.www.mktows.LeadKey[] leadList) {
        this.leadList = leadList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ParamsRequestCampaign)) return false;
        ParamsRequestCampaign other = (ParamsRequestCampaign) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.source==null && other.getSource()==null) || 
             (this.source!=null &&
              this.source.equals(other.getSource()))) &&
            this.campaignId == other.getCampaignId() &&
            ((this.leadList==null && other.getLeadList()==null) || 
             (this.leadList!=null &&
              java.util.Arrays.equals(this.leadList, other.getLeadList())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getSource() != null) {
            _hashCode += getSource().hashCode();
        }
        _hashCode += getCampaignId();
        if (getLeadList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLeadList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLeadList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ParamsRequestCampaign.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.marketo.com/mktows/", "ParamsRequestCampaign"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source");
        elemField.setXmlName(new javax.xml.namespace.QName("", "source"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.marketo.com/mktows/", "ReqCampSourceType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "campaignId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("leadList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "leadList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.marketo.com/mktows/", "LeadKey"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "leadKey"));
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
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
