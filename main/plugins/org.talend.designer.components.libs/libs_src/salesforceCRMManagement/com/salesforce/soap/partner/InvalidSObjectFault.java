/**
 * InvalidSObjectFault.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.salesforce.soap.partner;

public class InvalidSObjectFault extends java.lang.Exception {

    private static final long serialVersionUID = 1336706741062L;

    private com.salesforce.soap.partner.fault.InvalidSObjectFaultE faultMessage;

    public InvalidSObjectFault() {
        super("InvalidSObjectFault");
    }

    public InvalidSObjectFault(java.lang.String s) {
        super(s);
    }

    public InvalidSObjectFault(java.lang.String s, java.lang.Throwable ex) {
        super(s, ex);
    }

    public InvalidSObjectFault(java.lang.Throwable cause) {
        super(cause);
    }

    public void setFaultMessage(com.salesforce.soap.partner.fault.InvalidSObjectFaultE msg) {
        faultMessage = msg;
    }

    public com.salesforce.soap.partner.fault.InvalidSObjectFaultE getFaultMessage() {
        return faultMessage;
    }
}
