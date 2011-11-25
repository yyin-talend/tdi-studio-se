/**
 * MalformedQueryFault.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.5.2 Built on : Sep 06, 2010 (09:42:01 CEST)
 */

package com.salesforce.soap.partner;

public class MalformedQueryFault extends java.lang.Exception {

    private com.salesforce.soap.partner.fault.MalformedQueryFaultE faultMessage;

    public MalformedQueryFault() {
        super("MalformedQueryFault");
    }

    public MalformedQueryFault(java.lang.String s) {
        super(s);
    }

    public MalformedQueryFault(java.lang.String s, java.lang.Throwable ex) {
        super(s, ex);
    }

    public MalformedQueryFault(java.lang.Throwable cause) {
        super(cause);
    }

    public void setFaultMessage(com.salesforce.soap.partner.fault.MalformedQueryFaultE msg) {
        faultMessage = msg;
    }

    public com.salesforce.soap.partner.fault.MalformedQueryFaultE getFaultMessage() {
        return faultMessage;
    }
}
