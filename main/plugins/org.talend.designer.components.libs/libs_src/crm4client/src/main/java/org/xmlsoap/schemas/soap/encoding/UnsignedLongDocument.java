/*
 * An XML document type.
 * Localname: unsignedLong
 * Namespace: http://schemas.xmlsoap.org/soap/encoding/
 * Java type: org.xmlsoap.schemas.soap.encoding.UnsignedLongDocument
 *
 * Automatically generated - do not modify.
 */
package org.xmlsoap.schemas.soap.encoding;


/**
 * A document containing one unsignedLong(@http://schemas.xmlsoap.org/soap/encoding/) element.
 *
 * This is a complex type.
 */
public interface UnsignedLongDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(UnsignedLongDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sE3DFDC56E75679F2AF264CA469AD5996").resolveHandle("unsignedlong12e7doctype");
    
    /**
     * Gets the "unsignedLong" element
     */
    UnsignedLong getUnsignedLong();
    
    /**
     * Sets the "unsignedLong" element
     */
    void setUnsignedLong(UnsignedLong unsignedLong);
    
    /**
     * Appends and returns a new empty "unsignedLong" element
     */
    UnsignedLong addNewUnsignedLong();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    static final class StaticFactory
    {
        public static UnsignedLongDocument newInstance() {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static UnsignedLongDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static UnsignedLongDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static UnsignedLongDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static UnsignedLongDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static UnsignedLongDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static UnsignedLongDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static UnsignedLongDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static UnsignedLongDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static UnsignedLongDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static UnsignedLongDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static UnsignedLongDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static UnsignedLongDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static UnsignedLongDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static UnsignedLongDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static UnsignedLongDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (UnsignedLongDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        private StaticFactory() { } // No instance of this class allowed
    }
}
