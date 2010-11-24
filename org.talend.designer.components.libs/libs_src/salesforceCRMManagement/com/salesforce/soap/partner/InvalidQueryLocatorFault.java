
/**
 * InvalidQueryLocatorFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.2  Built on : Sep 06, 2010 (09:42:01 CEST)
 */

package com.salesforce.soap.partner;

public class InvalidQueryLocatorFault extends java.lang.Exception{
    
    private com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE faultMessage;

    
        public InvalidQueryLocatorFault() {
            super("InvalidQueryLocatorFault");
        }

        public InvalidQueryLocatorFault(java.lang.String s) {
           super(s);
        }

        public InvalidQueryLocatorFault(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public InvalidQueryLocatorFault(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE msg){
       faultMessage = msg;
    }
    
    public com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE getFaultMessage(){
       return faultMessage;
    }
}
    