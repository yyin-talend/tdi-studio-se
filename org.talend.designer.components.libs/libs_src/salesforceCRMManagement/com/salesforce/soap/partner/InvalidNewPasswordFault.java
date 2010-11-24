
/**
 * InvalidNewPasswordFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.2  Built on : Sep 06, 2010 (09:42:01 CEST)
 */

package com.salesforce.soap.partner;

public class InvalidNewPasswordFault extends java.lang.Exception{
    
    private com.salesforce.soap.partner.fault.InvalidNewPasswordFaultE faultMessage;

    
        public InvalidNewPasswordFault() {
            super("InvalidNewPasswordFault");
        }

        public InvalidNewPasswordFault(java.lang.String s) {
           super(s);
        }

        public InvalidNewPasswordFault(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public InvalidNewPasswordFault(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.salesforce.soap.partner.fault.InvalidNewPasswordFaultE msg){
       faultMessage = msg;
    }
    
    public com.salesforce.soap.partner.fault.InvalidNewPasswordFaultE getFaultMessage(){
       return faultMessage;
    }
}
    