/*
 * XML Type:  CheckIncomingEmailRequest
 * Namespace: http://schemas.microsoft.com/crm/2007/WebServices
 * Java type: com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest
 *
 * Automatically generated - do not modify.
 */
package com.microsoft.schemas.crm._2007.webservices;


/**
 * An XML CheckIncomingEmailRequest(@http://schemas.microsoft.com/crm/2007/WebServices).
 *
 * This is a complex type.
 */
public interface CheckIncomingEmailRequest extends com.microsoft.schemas.crm._2007.webservices.Request
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CheckIncomingEmailRequest.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sE3DFDC56E75679F2AF264CA469AD5996").resolveHandle("checkincomingemailrequest296dtype");
    
    /**
     * Gets the "MessageId" element
     */
    java.lang.String getMessageId();
    
    /**
     * Gets (as xml) the "MessageId" element
     */
    org.apache.xmlbeans.XmlString xgetMessageId();
    
    /**
     * Sets the "MessageId" element
     */
    void setMessageId(java.lang.String messageId);
    
    /**
     * Sets (as xml) the "MessageId" element
     */
    void xsetMessageId(org.apache.xmlbeans.XmlString messageId);
    
    /**
     * Gets the "Subject" element
     */
    java.lang.String getSubject();
    
    /**
     * Gets (as xml) the "Subject" element
     */
    org.apache.xmlbeans.XmlString xgetSubject();
    
    /**
     * Sets the "Subject" element
     */
    void setSubject(java.lang.String subject);
    
    /**
     * Sets (as xml) the "Subject" element
     */
    void xsetSubject(org.apache.xmlbeans.XmlString subject);
    
    /**
     * Gets the "From" element
     */
    java.lang.String getFrom();
    
    /**
     * Gets (as xml) the "From" element
     */
    org.apache.xmlbeans.XmlString xgetFrom();
    
    /**
     * Sets the "From" element
     */
    void setFrom(java.lang.String from);
    
    /**
     * Sets (as xml) the "From" element
     */
    void xsetFrom(org.apache.xmlbeans.XmlString from);
    
    /**
     * Gets the "To" element
     */
    java.lang.String getTo();
    
    /**
     * Gets (as xml) the "To" element
     */
    org.apache.xmlbeans.XmlString xgetTo();
    
    /**
     * Sets the "To" element
     */
    void setTo(java.lang.String to);
    
    /**
     * Sets (as xml) the "To" element
     */
    void xsetTo(org.apache.xmlbeans.XmlString to);
    
    /**
     * Gets the "Cc" element
     */
    java.lang.String getCc();
    
    /**
     * Gets (as xml) the "Cc" element
     */
    org.apache.xmlbeans.XmlString xgetCc();
    
    /**
     * Sets the "Cc" element
     */
    void setCc(java.lang.String cc);
    
    /**
     * Sets (as xml) the "Cc" element
     */
    void xsetCc(org.apache.xmlbeans.XmlString cc);
    
    /**
     * Gets the "Bcc" element
     */
    java.lang.String getBcc();
    
    /**
     * Gets (as xml) the "Bcc" element
     */
    org.apache.xmlbeans.XmlString xgetBcc();
    
    /**
     * Sets the "Bcc" element
     */
    void setBcc(java.lang.String bcc);
    
    /**
     * Sets (as xml) the "Bcc" element
     */
    void xsetBcc(org.apache.xmlbeans.XmlString bcc);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    static final class StaticFactory
    {
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest newInstance() {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.CheckIncomingEmailRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        private StaticFactory() { } // No instance of this class allowed
    }
}
