/**
 * LoginFault.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.5.2 Built on : Sep 06, 2010 (09:42:01 CEST)
 */

package com.salesforce.soap.partner;

public class LoginFault extends java.lang.Exception {

    private com.salesforce.soap.partner.fault.LoginFaultE faultMessage;

    public LoginFault() {
        super("LoginFault");
    }

    public LoginFault(java.lang.String s) {
        super(s);
    }

    public LoginFault(java.lang.String s, java.lang.Throwable ex) {
        super(s, ex);
    }

    public LoginFault(java.lang.Throwable cause) {
        super(cause);
    }

    public void setFaultMessage(com.salesforce.soap.partner.fault.LoginFaultE msg) {
        faultMessage = msg;
    }

    public com.salesforce.soap.partner.fault.LoginFaultE getFaultMessage() {
        return faultMessage;
    }
}
