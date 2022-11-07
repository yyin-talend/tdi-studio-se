/*
 * XML Type:  ArrayOfRolePrivilege
 * Namespace: http://schemas.microsoft.com/crm/2007/WebServices
 * Java type: com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege
 *
 * Automatically generated - do not modify.
 */
package com.microsoft.schemas.crm._2007.webservices;


/**
 * An XML ArrayOfRolePrivilege(@http://schemas.microsoft.com/crm/2007/WebServices).
 *
 * This is a complex type.
 */
public interface ArrayOfRolePrivilege extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ArrayOfRolePrivilege.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sE3DFDC56E75679F2AF264CA469AD5996").resolveHandle("arrayofroleprivilege97c9type");
    
    /**
     * Gets array of all "RolePrivilege" elements
     */
    com.microsoft.schemas.crm._2006.coretypes.RolePrivilege[] getRolePrivilegeArray();
    
    /**
     * Gets ith "RolePrivilege" element
     */
    com.microsoft.schemas.crm._2006.coretypes.RolePrivilege getRolePrivilegeArray(int i);
    
    /**
     * Tests for nil ith "RolePrivilege" element
     */
    boolean isNilRolePrivilegeArray(int i);
    
    /**
     * Returns number of "RolePrivilege" element
     */
    int sizeOfRolePrivilegeArray();
    
    /**
     * Sets array of all "RolePrivilege" element
     */
    void setRolePrivilegeArray(com.microsoft.schemas.crm._2006.coretypes.RolePrivilege[] rolePrivilegeArray);
    
    /**
     * Sets ith "RolePrivilege" element
     */
    void setRolePrivilegeArray(int i, com.microsoft.schemas.crm._2006.coretypes.RolePrivilege rolePrivilege);
    
    /**
     * Nils the ith "RolePrivilege" element
     */
    void setNilRolePrivilegeArray(int i);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "RolePrivilege" element
     */
    com.microsoft.schemas.crm._2006.coretypes.RolePrivilege insertNewRolePrivilege(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "RolePrivilege" element
     */
    com.microsoft.schemas.crm._2006.coretypes.RolePrivilege addNewRolePrivilege();
    
    /**
     * Removes the ith "RolePrivilege" element
     */
    void removeRolePrivilege(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    static final class StaticFactory
    {
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege newInstance() {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (com.microsoft.schemas.crm._2007.webservices.ArrayOfRolePrivilege) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        private StaticFactory() { } // No instance of this class allowed
    }
}
