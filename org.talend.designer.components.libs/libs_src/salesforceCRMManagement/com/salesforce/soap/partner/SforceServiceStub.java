
/**
 * SforceServiceStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.2  Built on : Sep 06, 2010 (09:42:01 CEST)
 */
        package com.salesforce.soap.partner;

        

        /*
        *  SforceServiceStub java implementation
        */

        
        public class SforceServiceStub extends org.apache.axis2.client.Stub
        implements SforceService{
        protected org.apache.axis2.description.AxisOperation[] _operations;

        //hashmaps to keep the fault mapping
        private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
        private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
        private java.util.HashMap faultMessageMap = new java.util.HashMap();

        private static int counter = 0;

        private static synchronized java.lang.String getUniqueSuffix(){
            // reset the counter if it is greater than 99999
            if (counter > 99999){
                counter = 0;
            }
            counter = counter + 1; 
            return java.lang.Long.toString(System.currentTimeMillis()) + "_" + counter;
        }

    
    private void populateAxisService() throws org.apache.axis2.AxisFault {

     //creating the Service with a unique name
     _service = new org.apache.axis2.description.AxisService("SforceService" + getUniqueSuffix());
     addAnonymousOperations();

        //creating the operations
        org.apache.axis2.description.AxisOperation __operation;

        _operations = new org.apache.axis2.description.AxisOperation[32];
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[0]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUserInfo"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[1]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSoftphoneLayout"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[2]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[3]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "setPassword"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[4]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "logout"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[5]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[6]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryAll"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[7]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUpdated"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[8]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[9]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[10]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sendEmail"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[11]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "search"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[12]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[13]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getDeleted"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[14]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[15]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroupStructures"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[16]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "resetPassword"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[17]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeGlobal"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[18]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeLayout"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[19]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeTabs"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[20]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroups"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[21]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getServerTimestamp"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[22]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "invalidateSessions"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[23]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObject"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[24]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "login"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[25]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryMore"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[26]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObjects"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[27]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "emptyRecycleBin"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[28]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[29]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[30]=__operation;
            
        
                   __operation = new org.apache.axis2.description.OutInAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[31]=__operation;
            
        
        }

    //populates the faults
    private void populateFaults(){
         
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.fault.InvalidFieldFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.fault.InvalidFieldFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidNewPasswordFault"),"com.salesforce.soap.partner.InvalidNewPasswordFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidNewPasswordFault"),"com.salesforce.soap.partner.InvalidNewPasswordFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidNewPasswordFault"),"com.salesforce.soap.partner.fault.InvalidNewPasswordFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedQueryFault"),"com.salesforce.soap.partner.MalformedQueryFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedQueryFault"),"com.salesforce.soap.partner.MalformedQueryFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedQueryFault"),"com.salesforce.soap.partner.fault.MalformedQueryFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.fault.InvalidFieldFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedQueryFault"),"com.salesforce.soap.partner.MalformedQueryFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedQueryFault"),"com.salesforce.soap.partner.MalformedQueryFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedQueryFault"),"com.salesforce.soap.partner.fault.MalformedQueryFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.fault.InvalidFieldFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidQueryLocatorFault"),"com.salesforce.soap.partner.InvalidQueryLocatorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidQueryLocatorFault"),"com.salesforce.soap.partner.InvalidQueryLocatorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidQueryLocatorFault"),"com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.fault.InvalidFieldFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedSearchFault"),"com.salesforce.soap.partner.MalformedSearchFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedSearchFault"),"com.salesforce.soap.partner.MalformedSearchFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedSearchFault"),"com.salesforce.soap.partner.fault.MalformedSearchFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.fault.InvalidFieldFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedQueryFault"),"com.salesforce.soap.partner.MalformedQueryFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedQueryFault"),"com.salesforce.soap.partner.MalformedQueryFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","MalformedQueryFault"),"com.salesforce.soap.partner.fault.MalformedQueryFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.fault.InvalidFieldFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidQueryLocatorFault"),"com.salesforce.soap.partner.InvalidQueryLocatorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidQueryLocatorFault"),"com.salesforce.soap.partner.InvalidQueryLocatorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidQueryLocatorFault"),"com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","LoginFault"),"com.salesforce.soap.partner.LoginFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","LoginFault"),"com.salesforce.soap.partner.LoginFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","LoginFault"),"com.salesforce.soap.partner.fault.LoginFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.fault.InvalidFieldFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidQueryLocatorFault"),"com.salesforce.soap.partner.InvalidQueryLocatorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidQueryLocatorFault"),"com.salesforce.soap.partner.InvalidQueryLocatorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidQueryLocatorFault"),"com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.InvalidSObjectFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidSObjectFault"),"com.salesforce.soap.partner.fault.InvalidSObjectFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.InvalidIdFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidIdFault"),"com.salesforce.soap.partner.fault.InvalidIdFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.InvalidFieldFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","InvalidFieldFault"),"com.salesforce.soap.partner.fault.InvalidFieldFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           
              faultExceptionNameMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultExceptionClassNameMap.put(new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.UnexpectedErrorFault");
              faultMessageMap.put( new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com","UnexpectedErrorFault"),"com.salesforce.soap.partner.fault.UnexpectedErrorFaultE");
           


    }

    /**
      *Constructor that takes in a configContext
      */

    public SforceServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
       java.lang.String targetEndpoint)
       throws org.apache.axis2.AxisFault {
         this(configurationContext,targetEndpoint,false);
   }


   /**
     * Constructor that takes in a configContext  and useseperate listner
     */
   public SforceServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
        java.lang.String targetEndpoint, boolean useSeparateListener)
        throws org.apache.axis2.AxisFault {
         //To populate AxisService
         populateAxisService();
         populateFaults();

        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext,_service);
        
	
        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(
                targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
        
    
    }

    /**
     * Default Constructor
     */
    public SforceServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {
        
                    this(configurationContext,"https://login.salesforce.com/services/Soap/u/20.0" );
                
    }

    /**
     * Default Constructor
     */
    public SforceServiceStub() throws org.apache.axis2.AxisFault {
        
                    this("https://login.salesforce.com/services/Soap/u/20.0" );
                
    }

    /**
     * Constructor taking the target endpoint
     */
    public SforceServiceStub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null,targetEndpoint);
    }



        
                    /**
                     * Auto generated method signature
                     * Merge and update a set of sObjects based on object id
                     * @see com.salesforce.soap.partner.SforceService#merge
                     * @param merge320
                    
                     * @param sessionHeader321
                    
                     * @param callOptions322
                    
                     * @param assignmentRuleHeader323
                    
                     * @param mruHeader324
                    
                     * @param allowFieldTruncationHeader325
                    
                     * @param disableFeedTrackingHeader326
                    
                     * @param debuggingHeader327
                    
                     * @param packageVersionHeader328
                    
                     * @param emailHeader329
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.InvalidFieldFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.MergeResponse merge(

                            com.salesforce.soap.partner.Merge merge320,com.salesforce.soap.partner.SessionHeader sessionHeader321,com.salesforce.soap.partner.CallOptions callOptions322,com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader323,com.salesforce.soap.partner.MruHeader mruHeader324,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader325,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader326,com.salesforce.soap.partner.DebuggingHeader debuggingHeader327,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader328,com.salesforce.soap.partner.EmailHeader emailHeader329)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.InvalidFieldFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:mergeRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    merge320,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "merge")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader321!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader321 = toOM(sessionHeader321, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementsessionHeader321,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions322!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions322 = toOM(callOptions322, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementcallOptions322,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (assignmentRuleHeader323!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementassignmentRuleHeader323 = toOM(assignmentRuleHeader323, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementassignmentRuleHeader323,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (mruHeader324!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementmruHeader324 = toOM(mruHeader324, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementmruHeader324,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allowFieldTruncationHeader325!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader325 = toOM(allowFieldTruncationHeader325, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementallowFieldTruncationHeader325,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (disableFeedTrackingHeader326!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader326 = toOM(disableFeedTrackingHeader326, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementdisableFeedTrackingHeader326,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (debuggingHeader327!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader327 = toOM(debuggingHeader327, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementdebuggingHeader327,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader328!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader328 = toOM(packageVersionHeader328, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementpackageVersionHeader328,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (emailHeader329!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementemailHeader329 = toOM(emailHeader329, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementemailHeader329,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.MergeResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.MergeResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
                          throw (com.salesforce.soap.partner.InvalidFieldFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Merge and update a set of sObjects based on object id
                * @see com.salesforce.soap.partner.SforceService#startmerge
                    * @param merge320
                
                    * @param sessionHeader321
                
                    * @param callOptions322
                
                    * @param assignmentRuleHeader323
                
                    * @param mruHeader324
                
                    * @param allowFieldTruncationHeader325
                
                    * @param disableFeedTrackingHeader326
                
                    * @param debuggingHeader327
                
                    * @param packageVersionHeader328
                
                    * @param emailHeader329
                
                */
                public  void startmerge(

                 com.salesforce.soap.partner.Merge merge320,com.salesforce.soap.partner.SessionHeader sessionHeader321,
                    com.salesforce.soap.partner.CallOptions callOptions322,
                    com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader323,
                    com.salesforce.soap.partner.MruHeader mruHeader324,
                    com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader325,
                    com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader326,
                    com.salesforce.soap.partner.DebuggingHeader debuggingHeader327,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader328,
                    com.salesforce.soap.partner.EmailHeader emailHeader329,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:mergeRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    merge320,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "merge")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader321!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader321 = toOM(sessionHeader321, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementsessionHeader321,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions322!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions322 = toOM(callOptions322, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementcallOptions322,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (assignmentRuleHeader323!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementassignmentRuleHeader323 = toOM(assignmentRuleHeader323, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementassignmentRuleHeader323,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (mruHeader324!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementmruHeader324 = toOM(mruHeader324, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementmruHeader324,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allowFieldTruncationHeader325!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader325 = toOM(allowFieldTruncationHeader325, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementallowFieldTruncationHeader325,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (disableFeedTrackingHeader326!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader326 = toOM(disableFeedTrackingHeader326, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementdisableFeedTrackingHeader326,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (debuggingHeader327!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader327 = toOM(debuggingHeader327, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementdebuggingHeader327,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader328!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader328 = toOM(packageVersionHeader328, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementpackageVersionHeader328,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (emailHeader329!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementemailHeader329 = toOM(emailHeader329, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge")));
                                                    addHeader(omElementemailHeader329,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.MergeResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultmerge(
                                        (com.salesforce.soap.partner.MergeResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrormerge(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrormerge((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrormerge((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
														callback.receiveErrormerge((com.salesforce.soap.partner.InvalidFieldFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrormerge((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrormerge(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrormerge(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrormerge(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrormerge(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrormerge(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrormerge(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrormerge(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrormerge(f);
                                            }
									    } else {
										    callback.receiveErrormerge(f);
									    }
									} else {
									    callback.receiveErrormerge(f);
									}
								} else {
								    callback.receiveErrormerge(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrormerge(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[0].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[0].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Returns standard information relevant to the current user
                     * @see com.salesforce.soap.partner.SforceService#getUserInfo
                     * @param getUserInfo331
                    
                     * @param sessionHeader332
                    
                     * @param callOptions333
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.GetUserInfoResponse getUserInfo(

                            com.salesforce.soap.partner.GetUserInfo getUserInfo331,com.salesforce.soap.partner.SessionHeader sessionHeader332,com.salesforce.soap.partner.CallOptions callOptions333)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:getUserInfoRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    getUserInfo331,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "getUserInfo")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader332!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader332 = toOM(sessionHeader332, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUserInfo")));
                                                    addHeader(omElementsessionHeader332,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions333!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions333 = toOM(callOptions333, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUserInfo")));
                                                    addHeader(omElementcallOptions333,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.GetUserInfoResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.GetUserInfoResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Returns standard information relevant to the current user
                * @see com.salesforce.soap.partner.SforceService#startgetUserInfo
                    * @param getUserInfo331
                
                    * @param sessionHeader332
                
                    * @param callOptions333
                
                */
                public  void startgetUserInfo(

                 com.salesforce.soap.partner.GetUserInfo getUserInfo331,com.salesforce.soap.partner.SessionHeader sessionHeader332,
                    com.salesforce.soap.partner.CallOptions callOptions333,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:getUserInfoRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    getUserInfo331,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "getUserInfo")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader332!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader332 = toOM(sessionHeader332, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUserInfo")));
                                                    addHeader(omElementsessionHeader332,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions333!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions333 = toOM(callOptions333, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUserInfo")));
                                                    addHeader(omElementcallOptions333,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.GetUserInfoResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultgetUserInfo(
                                        (com.salesforce.soap.partner.GetUserInfoResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorgetUserInfo(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorgetUserInfo((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorgetUserInfo(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUserInfo(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUserInfo(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUserInfo(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUserInfo(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUserInfo(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUserInfo(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUserInfo(f);
                                            }
									    } else {
										    callback.receiveErrorgetUserInfo(f);
									    }
									} else {
									    callback.receiveErrorgetUserInfo(f);
									}
								} else {
								    callback.receiveErrorgetUserInfo(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorgetUserInfo(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[1].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[1].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Describe the layout of the SoftPhone
                     * @see com.salesforce.soap.partner.SforceService#describeSoftphoneLayout
                     * @param describeSoftphoneLayout335
                    
                     * @param sessionHeader336
                    
                     * @param callOptions337
                    
                     * @param packageVersionHeader338
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse describeSoftphoneLayout(

                            com.salesforce.soap.partner.DescribeSoftphoneLayout describeSoftphoneLayout335,com.salesforce.soap.partner.SessionHeader sessionHeader336,com.salesforce.soap.partner.CallOptions callOptions337,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader338)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeSoftphoneLayoutRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeSoftphoneLayout335,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeSoftphoneLayout")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader336!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader336 = toOM(sessionHeader336, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSoftphoneLayout")));
                                                    addHeader(omElementsessionHeader336,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions337!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions337 = toOM(callOptions337, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSoftphoneLayout")));
                                                    addHeader(omElementcallOptions337,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader338!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader338 = toOM(packageVersionHeader338, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSoftphoneLayout")));
                                                    addHeader(omElementpackageVersionHeader338,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Describe the layout of the SoftPhone
                * @see com.salesforce.soap.partner.SforceService#startdescribeSoftphoneLayout
                    * @param describeSoftphoneLayout335
                
                    * @param sessionHeader336
                
                    * @param callOptions337
                
                    * @param packageVersionHeader338
                
                */
                public  void startdescribeSoftphoneLayout(

                 com.salesforce.soap.partner.DescribeSoftphoneLayout describeSoftphoneLayout335,com.salesforce.soap.partner.SessionHeader sessionHeader336,
                    com.salesforce.soap.partner.CallOptions callOptions337,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader338,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeSoftphoneLayoutRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeSoftphoneLayout335,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeSoftphoneLayout")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader336!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader336 = toOM(sessionHeader336, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSoftphoneLayout")));
                                                    addHeader(omElementsessionHeader336,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions337!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions337 = toOM(callOptions337, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSoftphoneLayout")));
                                                    addHeader(omElementcallOptions337,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader338!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader338 = toOM(packageVersionHeader338, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSoftphoneLayout")));
                                                    addHeader(omElementpackageVersionHeader338,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultdescribeSoftphoneLayout(
                                        (com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrordescribeSoftphoneLayout(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrordescribeSoftphoneLayout((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrordescribeSoftphoneLayout(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSoftphoneLayout(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSoftphoneLayout(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSoftphoneLayout(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSoftphoneLayout(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSoftphoneLayout(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSoftphoneLayout(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSoftphoneLayout(f);
                                            }
									    } else {
										    callback.receiveErrordescribeSoftphoneLayout(f);
									    }
									} else {
									    callback.receiveErrordescribeSoftphoneLayout(f);
									}
								} else {
								    callback.receiveErrordescribeSoftphoneLayout(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrordescribeSoftphoneLayout(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[2].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[2].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Update a set of sObjects
                     * @see com.salesforce.soap.partner.SforceService#update
                     * @param update340
                    
                     * @param sessionHeader341
                    
                     * @param callOptions342
                    
                     * @param assignmentRuleHeader343
                    
                     * @param mruHeader344
                    
                     * @param allowFieldTruncationHeader345
                    
                     * @param disableFeedTrackingHeader346
                    
                     * @param allOrNoneHeader347
                    
                     * @param debuggingHeader348
                    
                     * @param packageVersionHeader349
                    
                     * @param emailHeader350
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.InvalidFieldFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.UpdateResponse update(

                            com.salesforce.soap.partner.Update update340,com.salesforce.soap.partner.SessionHeader sessionHeader341,com.salesforce.soap.partner.CallOptions callOptions342,com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader343,com.salesforce.soap.partner.MruHeader mruHeader344,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader345,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader346,com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader347,com.salesforce.soap.partner.DebuggingHeader debuggingHeader348,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader349,com.salesforce.soap.partner.EmailHeader emailHeader350)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.InvalidFieldFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:updateRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    update340,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "update")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader341!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader341 = toOM(sessionHeader341, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementsessionHeader341,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions342!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions342 = toOM(callOptions342, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementcallOptions342,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (assignmentRuleHeader343!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementassignmentRuleHeader343 = toOM(assignmentRuleHeader343, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementassignmentRuleHeader343,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (mruHeader344!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementmruHeader344 = toOM(mruHeader344, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementmruHeader344,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allowFieldTruncationHeader345!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader345 = toOM(allowFieldTruncationHeader345, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementallowFieldTruncationHeader345,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (disableFeedTrackingHeader346!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader346 = toOM(disableFeedTrackingHeader346, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementdisableFeedTrackingHeader346,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allOrNoneHeader347!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallOrNoneHeader347 = toOM(allOrNoneHeader347, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementallOrNoneHeader347,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (debuggingHeader348!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader348 = toOM(debuggingHeader348, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementdebuggingHeader348,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader349!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader349 = toOM(packageVersionHeader349, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementpackageVersionHeader349,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (emailHeader350!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementemailHeader350 = toOM(emailHeader350, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementemailHeader350,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.UpdateResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.UpdateResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
                          throw (com.salesforce.soap.partner.InvalidFieldFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Update a set of sObjects
                * @see com.salesforce.soap.partner.SforceService#startupdate
                    * @param update340
                
                    * @param sessionHeader341
                
                    * @param callOptions342
                
                    * @param assignmentRuleHeader343
                
                    * @param mruHeader344
                
                    * @param allowFieldTruncationHeader345
                
                    * @param disableFeedTrackingHeader346
                
                    * @param allOrNoneHeader347
                
                    * @param debuggingHeader348
                
                    * @param packageVersionHeader349
                
                    * @param emailHeader350
                
                */
                public  void startupdate(

                 com.salesforce.soap.partner.Update update340,com.salesforce.soap.partner.SessionHeader sessionHeader341,
                    com.salesforce.soap.partner.CallOptions callOptions342,
                    com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader343,
                    com.salesforce.soap.partner.MruHeader mruHeader344,
                    com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader345,
                    com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader346,
                    com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader347,
                    com.salesforce.soap.partner.DebuggingHeader debuggingHeader348,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader349,
                    com.salesforce.soap.partner.EmailHeader emailHeader350,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:updateRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    update340,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "update")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader341!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader341 = toOM(sessionHeader341, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementsessionHeader341,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions342!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions342 = toOM(callOptions342, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementcallOptions342,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (assignmentRuleHeader343!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementassignmentRuleHeader343 = toOM(assignmentRuleHeader343, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementassignmentRuleHeader343,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (mruHeader344!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementmruHeader344 = toOM(mruHeader344, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementmruHeader344,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allowFieldTruncationHeader345!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader345 = toOM(allowFieldTruncationHeader345, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementallowFieldTruncationHeader345,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (disableFeedTrackingHeader346!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader346 = toOM(disableFeedTrackingHeader346, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementdisableFeedTrackingHeader346,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allOrNoneHeader347!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallOrNoneHeader347 = toOM(allOrNoneHeader347, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementallOrNoneHeader347,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (debuggingHeader348!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader348 = toOM(debuggingHeader348, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementdebuggingHeader348,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader349!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader349 = toOM(packageVersionHeader349, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementpackageVersionHeader349,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (emailHeader350!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementemailHeader350 = toOM(emailHeader350, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update")));
                                                    addHeader(omElementemailHeader350,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.UpdateResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultupdate(
                                        (com.salesforce.soap.partner.UpdateResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorupdate(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrorupdate((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrorupdate((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
														callback.receiveErrorupdate((com.salesforce.soap.partner.InvalidFieldFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorupdate((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorupdate(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupdate(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupdate(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupdate(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupdate(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupdate(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupdate(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupdate(f);
                                            }
									    } else {
										    callback.receiveErrorupdate(f);
									    }
									} else {
									    callback.receiveErrorupdate(f);
									}
								} else {
								    callback.receiveErrorupdate(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorupdate(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[3].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[3].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Set a user's password
                     * @see com.salesforce.soap.partner.SforceService#setPassword
                     * @param setPassword352
                    
                     * @param sessionHeader353
                    
                     * @param callOptions354
                    
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     * @throws com.salesforce.soap.partner.InvalidNewPasswordFault : 
                     */

                    

                            public  com.salesforce.soap.partner.SetPasswordResponse setPassword(

                            com.salesforce.soap.partner.SetPassword setPassword352,com.salesforce.soap.partner.SessionHeader sessionHeader353,com.salesforce.soap.partner.CallOptions callOptions354)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault
                        ,com.salesforce.soap.partner.InvalidNewPasswordFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[4].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:setPasswordRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    setPassword352,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "setPassword")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader353!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader353 = toOM(sessionHeader353, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "setPassword")));
                                                    addHeader(omElementsessionHeader353,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions354!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions354 = toOM(callOptions354, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "setPassword")));
                                                    addHeader(omElementcallOptions354,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.SetPasswordResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.SetPasswordResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidNewPasswordFault){
                          throw (com.salesforce.soap.partner.InvalidNewPasswordFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Set a user's password
                * @see com.salesforce.soap.partner.SforceService#startsetPassword
                    * @param setPassword352
                
                    * @param sessionHeader353
                
                    * @param callOptions354
                
                */
                public  void startsetPassword(

                 com.salesforce.soap.partner.SetPassword setPassword352,com.salesforce.soap.partner.SessionHeader sessionHeader353,
                    com.salesforce.soap.partner.CallOptions callOptions354,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[4].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:setPasswordRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    setPassword352,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "setPassword")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader353!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader353 = toOM(sessionHeader353, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "setPassword")));
                                                    addHeader(omElementsessionHeader353,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions354!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions354 = toOM(callOptions354, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "setPassword")));
                                                    addHeader(omElementcallOptions354,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.SetPasswordResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultsetPassword(
                                        (com.salesforce.soap.partner.SetPasswordResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorsetPassword(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrorsetPassword((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorsetPassword((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidNewPasswordFault){
														callback.receiveErrorsetPassword((com.salesforce.soap.partner.InvalidNewPasswordFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorsetPassword(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsetPassword(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsetPassword(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsetPassword(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsetPassword(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsetPassword(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsetPassword(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsetPassword(f);
                                            }
									    } else {
										    callback.receiveErrorsetPassword(f);
									    }
									} else {
									    callback.receiveErrorsetPassword(f);
									}
								} else {
								    callback.receiveErrorsetPassword(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorsetPassword(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[4].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[4].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Logout the current user, invalidating the current session.
                     * @see com.salesforce.soap.partner.SforceService#logout
                     * @param logout356
                    
                     * @param sessionHeader357
                    
                     * @param callOptions358
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.LogoutResponse logout(

                            com.salesforce.soap.partner.Logout logout356,com.salesforce.soap.partner.SessionHeader sessionHeader357,com.salesforce.soap.partner.CallOptions callOptions358)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[5].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:logoutRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    logout356,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "logout")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader357!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader357 = toOM(sessionHeader357, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "logout")));
                                                    addHeader(omElementsessionHeader357,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions358!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions358 = toOM(callOptions358, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "logout")));
                                                    addHeader(omElementcallOptions358,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.LogoutResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.LogoutResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Logout the current user, invalidating the current session.
                * @see com.salesforce.soap.partner.SforceService#startlogout
                    * @param logout356
                
                    * @param sessionHeader357
                
                    * @param callOptions358
                
                */
                public  void startlogout(

                 com.salesforce.soap.partner.Logout logout356,com.salesforce.soap.partner.SessionHeader sessionHeader357,
                    com.salesforce.soap.partner.CallOptions callOptions358,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[5].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:logoutRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    logout356,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "logout")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader357!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader357 = toOM(sessionHeader357, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "logout")));
                                                    addHeader(omElementsessionHeader357,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions358!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions358 = toOM(callOptions358, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "logout")));
                                                    addHeader(omElementcallOptions358,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.LogoutResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultlogout(
                                        (com.salesforce.soap.partner.LogoutResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorlogout(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorlogout((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorlogout(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogout(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogout(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogout(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogout(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogout(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogout(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogout(f);
                                            }
									    } else {
										    callback.receiveErrorlogout(f);
									    }
									} else {
									    callback.receiveErrorlogout(f);
									}
								} else {
								    callback.receiveErrorlogout(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorlogout(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[5].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[5].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Get a set of sObjects
                     * @see com.salesforce.soap.partner.SforceService#retrieve
                     * @param retrieve360
                    
                     * @param sessionHeader361
                    
                     * @param callOptions362
                    
                     * @param queryOptions363
                    
                     * @param mruHeader364
                    
                     * @param packageVersionHeader365
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.MalformedQueryFault : 
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.InvalidFieldFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.RetrieveResponse retrieve(

                            com.salesforce.soap.partner.Retrieve retrieve360,com.salesforce.soap.partner.SessionHeader sessionHeader361,com.salesforce.soap.partner.CallOptions callOptions362,com.salesforce.soap.partner.QueryOptions queryOptions363,com.salesforce.soap.partner.MruHeader mruHeader364,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader365)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.MalformedQueryFault
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.InvalidFieldFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[6].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:retrieveRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    retrieve360,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "retrieve")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader361!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader361 = toOM(sessionHeader361, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve")));
                                                    addHeader(omElementsessionHeader361,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions362!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions362 = toOM(callOptions362, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve")));
                                                    addHeader(omElementcallOptions362,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (queryOptions363!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementqueryOptions363 = toOM(queryOptions363, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve")));
                                                    addHeader(omElementqueryOptions363,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (mruHeader364!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementmruHeader364 = toOM(mruHeader364, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve")));
                                                    addHeader(omElementmruHeader364,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader365!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader365 = toOM(packageVersionHeader365, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve")));
                                                    addHeader(omElementpackageVersionHeader365,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.RetrieveResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.RetrieveResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.MalformedQueryFault){
                          throw (com.salesforce.soap.partner.MalformedQueryFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
                          throw (com.salesforce.soap.partner.InvalidFieldFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Get a set of sObjects
                * @see com.salesforce.soap.partner.SforceService#startretrieve
                    * @param retrieve360
                
                    * @param sessionHeader361
                
                    * @param callOptions362
                
                    * @param queryOptions363
                
                    * @param mruHeader364
                
                    * @param packageVersionHeader365
                
                */
                public  void startretrieve(

                 com.salesforce.soap.partner.Retrieve retrieve360,com.salesforce.soap.partner.SessionHeader sessionHeader361,
                    com.salesforce.soap.partner.CallOptions callOptions362,
                    com.salesforce.soap.partner.QueryOptions queryOptions363,
                    com.salesforce.soap.partner.MruHeader mruHeader364,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader365,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[6].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:retrieveRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    retrieve360,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "retrieve")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader361!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader361 = toOM(sessionHeader361, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve")));
                                                    addHeader(omElementsessionHeader361,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions362!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions362 = toOM(callOptions362, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve")));
                                                    addHeader(omElementcallOptions362,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (queryOptions363!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementqueryOptions363 = toOM(queryOptions363, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve")));
                                                    addHeader(omElementqueryOptions363,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (mruHeader364!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementmruHeader364 = toOM(mruHeader364, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve")));
                                                    addHeader(omElementmruHeader364,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader365!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader365 = toOM(packageVersionHeader365, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve")));
                                                    addHeader(omElementpackageVersionHeader365,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.RetrieveResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultretrieve(
                                        (com.salesforce.soap.partner.RetrieveResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorretrieve(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrorretrieve((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.MalformedQueryFault){
														callback.receiveErrorretrieve((com.salesforce.soap.partner.MalformedQueryFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrorretrieve((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
														callback.receiveErrorretrieve((com.salesforce.soap.partner.InvalidFieldFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorretrieve((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorretrieve(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorretrieve(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorretrieve(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorretrieve(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorretrieve(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorretrieve(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorretrieve(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorretrieve(f);
                                            }
									    } else {
										    callback.receiveErrorretrieve(f);
									    }
									} else {
									    callback.receiveErrorretrieve(f);
									}
								} else {
								    callback.receiveErrorretrieve(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorretrieve(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[6].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[6].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Create a Query Cursor, including deleted sObjects
                     * @see com.salesforce.soap.partner.SforceService#queryAll
                     * @param queryAll367
                    
                     * @param sessionHeader368
                    
                     * @param callOptions369
                    
                     * @param queryOptions370
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.MalformedQueryFault : 
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.InvalidFieldFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.QueryAllResponse queryAll(

                            com.salesforce.soap.partner.QueryAll queryAll367,com.salesforce.soap.partner.SessionHeader sessionHeader368,com.salesforce.soap.partner.CallOptions callOptions369,com.salesforce.soap.partner.QueryOptions queryOptions370)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.MalformedQueryFault
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.InvalidFieldFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault
                        ,com.salesforce.soap.partner.InvalidQueryLocatorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[7].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:queryAllRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    queryAll367,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "queryAll")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader368!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader368 = toOM(sessionHeader368, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryAll")));
                                                    addHeader(omElementsessionHeader368,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions369!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions369 = toOM(callOptions369, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryAll")));
                                                    addHeader(omElementcallOptions369,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (queryOptions370!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementqueryOptions370 = toOM(queryOptions370, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryAll")));
                                                    addHeader(omElementqueryOptions370,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.QueryAllResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.QueryAllResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.MalformedQueryFault){
                          throw (com.salesforce.soap.partner.MalformedQueryFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
                          throw (com.salesforce.soap.partner.InvalidFieldFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidQueryLocatorFault){
                          throw (com.salesforce.soap.partner.InvalidQueryLocatorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Create a Query Cursor, including deleted sObjects
                * @see com.salesforce.soap.partner.SforceService#startqueryAll
                    * @param queryAll367
                
                    * @param sessionHeader368
                
                    * @param callOptions369
                
                    * @param queryOptions370
                
                */
                public  void startqueryAll(

                 com.salesforce.soap.partner.QueryAll queryAll367,com.salesforce.soap.partner.SessionHeader sessionHeader368,
                    com.salesforce.soap.partner.CallOptions callOptions369,
                    com.salesforce.soap.partner.QueryOptions queryOptions370,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[7].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:queryAllRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    queryAll367,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "queryAll")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader368!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader368 = toOM(sessionHeader368, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryAll")));
                                                    addHeader(omElementsessionHeader368,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions369!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions369 = toOM(callOptions369, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryAll")));
                                                    addHeader(omElementcallOptions369,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (queryOptions370!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementqueryOptions370 = toOM(queryOptions370, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryAll")));
                                                    addHeader(omElementqueryOptions370,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.QueryAllResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultqueryAll(
                                        (com.salesforce.soap.partner.QueryAllResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorqueryAll(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrorqueryAll((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.MalformedQueryFault){
														callback.receiveErrorqueryAll((com.salesforce.soap.partner.MalformedQueryFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrorqueryAll((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
														callback.receiveErrorqueryAll((com.salesforce.soap.partner.InvalidFieldFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorqueryAll((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidQueryLocatorFault){
														callback.receiveErrorqueryAll((com.salesforce.soap.partner.InvalidQueryLocatorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorqueryAll(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryAll(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryAll(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryAll(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryAll(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryAll(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryAll(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryAll(f);
                                            }
									    } else {
										    callback.receiveErrorqueryAll(f);
									    }
									} else {
									    callback.receiveErrorqueryAll(f);
									}
								} else {
								    callback.receiveErrorqueryAll(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorqueryAll(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[7].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[7].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Get the IDs for updated sObjects
                     * @see com.salesforce.soap.partner.SforceService#getUpdated
                     * @param getUpdated372
                    
                     * @param sessionHeader373
                    
                     * @param callOptions374
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.GetUpdatedResponse getUpdated(

                            com.salesforce.soap.partner.GetUpdated getUpdated372,com.salesforce.soap.partner.SessionHeader sessionHeader373,com.salesforce.soap.partner.CallOptions callOptions374)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[8].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:getUpdatedRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    getUpdated372,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "getUpdated")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader373!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader373 = toOM(sessionHeader373, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUpdated")));
                                                    addHeader(omElementsessionHeader373,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions374!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions374 = toOM(callOptions374, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUpdated")));
                                                    addHeader(omElementcallOptions374,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.GetUpdatedResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.GetUpdatedResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Get the IDs for updated sObjects
                * @see com.salesforce.soap.partner.SforceService#startgetUpdated
                    * @param getUpdated372
                
                    * @param sessionHeader373
                
                    * @param callOptions374
                
                */
                public  void startgetUpdated(

                 com.salesforce.soap.partner.GetUpdated getUpdated372,com.salesforce.soap.partner.SessionHeader sessionHeader373,
                    com.salesforce.soap.partner.CallOptions callOptions374,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[8].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:getUpdatedRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    getUpdated372,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "getUpdated")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader373!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader373 = toOM(sessionHeader373, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUpdated")));
                                                    addHeader(omElementsessionHeader373,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions374!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions374 = toOM(callOptions374, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUpdated")));
                                                    addHeader(omElementcallOptions374,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.GetUpdatedResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultgetUpdated(
                                        (com.salesforce.soap.partner.GetUpdatedResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorgetUpdated(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrorgetUpdated((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorgetUpdated((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorgetUpdated(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUpdated(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUpdated(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUpdated(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUpdated(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUpdated(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUpdated(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetUpdated(f);
                                            }
									    } else {
										    callback.receiveErrorgetUpdated(f);
									    }
									} else {
									    callback.receiveErrorgetUpdated(f);
									}
								} else {
								    callback.receiveErrorgetUpdated(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorgetUpdated(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[8].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[8].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Undelete a set of sObjects
                     * @see com.salesforce.soap.partner.SforceService#undelete
                     * @param undelete376
                    
                     * @param sessionHeader377
                    
                     * @param callOptions378
                    
                     * @param allowFieldTruncationHeader379
                    
                     * @param disableFeedTrackingHeader380
                    
                     * @param allOrNoneHeader381
                    
                     * @param debuggingHeader382
                    
                     * @param packageVersionHeader383
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.UndeleteResponse undelete(

                            com.salesforce.soap.partner.Undelete undelete376,com.salesforce.soap.partner.SessionHeader sessionHeader377,com.salesforce.soap.partner.CallOptions callOptions378,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader379,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader380,com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader381,com.salesforce.soap.partner.DebuggingHeader debuggingHeader382,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader383)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[9].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:undeleteRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    undelete376,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "undelete")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader377!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader377 = toOM(sessionHeader377, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementsessionHeader377,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions378!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions378 = toOM(callOptions378, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementcallOptions378,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allowFieldTruncationHeader379!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader379 = toOM(allowFieldTruncationHeader379, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementallowFieldTruncationHeader379,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (disableFeedTrackingHeader380!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader380 = toOM(disableFeedTrackingHeader380, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementdisableFeedTrackingHeader380,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allOrNoneHeader381!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallOrNoneHeader381 = toOM(allOrNoneHeader381, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementallOrNoneHeader381,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (debuggingHeader382!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader382 = toOM(debuggingHeader382, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementdebuggingHeader382,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader383!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader383 = toOM(packageVersionHeader383, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementpackageVersionHeader383,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.UndeleteResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.UndeleteResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Undelete a set of sObjects
                * @see com.salesforce.soap.partner.SforceService#startundelete
                    * @param undelete376
                
                    * @param sessionHeader377
                
                    * @param callOptions378
                
                    * @param allowFieldTruncationHeader379
                
                    * @param disableFeedTrackingHeader380
                
                    * @param allOrNoneHeader381
                
                    * @param debuggingHeader382
                
                    * @param packageVersionHeader383
                
                */
                public  void startundelete(

                 com.salesforce.soap.partner.Undelete undelete376,com.salesforce.soap.partner.SessionHeader sessionHeader377,
                    com.salesforce.soap.partner.CallOptions callOptions378,
                    com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader379,
                    com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader380,
                    com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader381,
                    com.salesforce.soap.partner.DebuggingHeader debuggingHeader382,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader383,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[9].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:undeleteRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    undelete376,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "undelete")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader377!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader377 = toOM(sessionHeader377, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementsessionHeader377,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions378!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions378 = toOM(callOptions378, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementcallOptions378,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allowFieldTruncationHeader379!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader379 = toOM(allowFieldTruncationHeader379, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementallowFieldTruncationHeader379,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (disableFeedTrackingHeader380!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader380 = toOM(disableFeedTrackingHeader380, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementdisableFeedTrackingHeader380,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allOrNoneHeader381!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallOrNoneHeader381 = toOM(allOrNoneHeader381, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementallOrNoneHeader381,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (debuggingHeader382!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader382 = toOM(debuggingHeader382, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementdebuggingHeader382,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader383!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader383 = toOM(packageVersionHeader383, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete")));
                                                    addHeader(omElementpackageVersionHeader383,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.UndeleteResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultundelete(
                                        (com.salesforce.soap.partner.UndeleteResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorundelete(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorundelete((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorundelete(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorundelete(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorundelete(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorundelete(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorundelete(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorundelete(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorundelete(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorundelete(f);
                                            }
									    } else {
										    callback.receiveErrorundelete(f);
									    }
									} else {
									    callback.receiveErrorundelete(f);
									}
								} else {
								    callback.receiveErrorundelete(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorundelete(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[9].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[9].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Create a set of new sObjects
                     * @see com.salesforce.soap.partner.SforceService#create
                     * @param create385
                    
                     * @param sessionHeader386
                    
                     * @param callOptions387
                    
                     * @param assignmentRuleHeader388
                    
                     * @param mruHeader389
                    
                     * @param allowFieldTruncationHeader390
                    
                     * @param disableFeedTrackingHeader391
                    
                     * @param allOrNoneHeader392
                    
                     * @param debuggingHeader393
                    
                     * @param packageVersionHeader394
                    
                     * @param emailHeader395
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.InvalidFieldFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.CreateResponse create(

                            com.salesforce.soap.partner.Create create385,com.salesforce.soap.partner.SessionHeader sessionHeader386,com.salesforce.soap.partner.CallOptions callOptions387,com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader388,com.salesforce.soap.partner.MruHeader mruHeader389,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader390,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader391,com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader392,com.salesforce.soap.partner.DebuggingHeader debuggingHeader393,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader394,com.salesforce.soap.partner.EmailHeader emailHeader395)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.InvalidFieldFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[10].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:createRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    create385,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "create")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader386!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader386 = toOM(sessionHeader386, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementsessionHeader386,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions387!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions387 = toOM(callOptions387, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementcallOptions387,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (assignmentRuleHeader388!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementassignmentRuleHeader388 = toOM(assignmentRuleHeader388, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementassignmentRuleHeader388,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (mruHeader389!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementmruHeader389 = toOM(mruHeader389, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementmruHeader389,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allowFieldTruncationHeader390!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader390 = toOM(allowFieldTruncationHeader390, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementallowFieldTruncationHeader390,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (disableFeedTrackingHeader391!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader391 = toOM(disableFeedTrackingHeader391, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementdisableFeedTrackingHeader391,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allOrNoneHeader392!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallOrNoneHeader392 = toOM(allOrNoneHeader392, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementallOrNoneHeader392,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (debuggingHeader393!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader393 = toOM(debuggingHeader393, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementdebuggingHeader393,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader394!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader394 = toOM(packageVersionHeader394, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementpackageVersionHeader394,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (emailHeader395!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementemailHeader395 = toOM(emailHeader395, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementemailHeader395,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.CreateResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.CreateResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
                          throw (com.salesforce.soap.partner.InvalidFieldFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Create a set of new sObjects
                * @see com.salesforce.soap.partner.SforceService#startcreate
                    * @param create385
                
                    * @param sessionHeader386
                
                    * @param callOptions387
                
                    * @param assignmentRuleHeader388
                
                    * @param mruHeader389
                
                    * @param allowFieldTruncationHeader390
                
                    * @param disableFeedTrackingHeader391
                
                    * @param allOrNoneHeader392
                
                    * @param debuggingHeader393
                
                    * @param packageVersionHeader394
                
                    * @param emailHeader395
                
                */
                public  void startcreate(

                 com.salesforce.soap.partner.Create create385,com.salesforce.soap.partner.SessionHeader sessionHeader386,
                    com.salesforce.soap.partner.CallOptions callOptions387,
                    com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader388,
                    com.salesforce.soap.partner.MruHeader mruHeader389,
                    com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader390,
                    com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader391,
                    com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader392,
                    com.salesforce.soap.partner.DebuggingHeader debuggingHeader393,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader394,
                    com.salesforce.soap.partner.EmailHeader emailHeader395,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[10].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:createRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    create385,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "create")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader386!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader386 = toOM(sessionHeader386, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementsessionHeader386,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions387!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions387 = toOM(callOptions387, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementcallOptions387,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (assignmentRuleHeader388!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementassignmentRuleHeader388 = toOM(assignmentRuleHeader388, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementassignmentRuleHeader388,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (mruHeader389!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementmruHeader389 = toOM(mruHeader389, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementmruHeader389,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allowFieldTruncationHeader390!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader390 = toOM(allowFieldTruncationHeader390, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementallowFieldTruncationHeader390,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (disableFeedTrackingHeader391!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader391 = toOM(disableFeedTrackingHeader391, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementdisableFeedTrackingHeader391,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allOrNoneHeader392!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallOrNoneHeader392 = toOM(allOrNoneHeader392, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementallOrNoneHeader392,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (debuggingHeader393!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader393 = toOM(debuggingHeader393, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementdebuggingHeader393,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader394!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader394 = toOM(packageVersionHeader394, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementpackageVersionHeader394,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (emailHeader395!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementemailHeader395 = toOM(emailHeader395, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create")));
                                                    addHeader(omElementemailHeader395,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.CreateResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultcreate(
                                        (com.salesforce.soap.partner.CreateResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorcreate(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrorcreate((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrorcreate((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
														callback.receiveErrorcreate((com.salesforce.soap.partner.InvalidFieldFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorcreate((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorcreate(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorcreate(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorcreate(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorcreate(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorcreate(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorcreate(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorcreate(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorcreate(f);
                                            }
									    } else {
										    callback.receiveErrorcreate(f);
									    }
									} else {
									    callback.receiveErrorcreate(f);
									}
								} else {
								    callback.receiveErrorcreate(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorcreate(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[10].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[10].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Send outbound email
                     * @see com.salesforce.soap.partner.SforceService#sendEmail
                     * @param sendEmail397
                    
                     * @param sessionHeader398
                    
                     * @param callOptions399
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.SendEmailResponse sendEmail(

                            com.salesforce.soap.partner.SendEmail sendEmail397,com.salesforce.soap.partner.SessionHeader sessionHeader398,com.salesforce.soap.partner.CallOptions callOptions399)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[11].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:sendEmailRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    sendEmail397,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "sendEmail")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader398!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader398 = toOM(sessionHeader398, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sendEmail")));
                                                    addHeader(omElementsessionHeader398,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions399!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions399 = toOM(callOptions399, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sendEmail")));
                                                    addHeader(omElementcallOptions399,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.SendEmailResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.SendEmailResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Send outbound email
                * @see com.salesforce.soap.partner.SforceService#startsendEmail
                    * @param sendEmail397
                
                    * @param sessionHeader398
                
                    * @param callOptions399
                
                */
                public  void startsendEmail(

                 com.salesforce.soap.partner.SendEmail sendEmail397,com.salesforce.soap.partner.SessionHeader sessionHeader398,
                    com.salesforce.soap.partner.CallOptions callOptions399,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[11].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:sendEmailRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    sendEmail397,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "sendEmail")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader398!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader398 = toOM(sessionHeader398, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sendEmail")));
                                                    addHeader(omElementsessionHeader398,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions399!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions399 = toOM(callOptions399, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sendEmail")));
                                                    addHeader(omElementcallOptions399,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.SendEmailResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultsendEmail(
                                        (com.salesforce.soap.partner.SendEmailResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorsendEmail(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorsendEmail((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorsendEmail(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsendEmail(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsendEmail(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsendEmail(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsendEmail(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsendEmail(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsendEmail(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsendEmail(f);
                                            }
									    } else {
										    callback.receiveErrorsendEmail(f);
									    }
									} else {
									    callback.receiveErrorsendEmail(f);
									}
								} else {
								    callback.receiveErrorsendEmail(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorsendEmail(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[11].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[11].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Search for sObjects
                     * @see com.salesforce.soap.partner.SforceService#search
                     * @param search401
                    
                     * @param sessionHeader402
                    
                     * @param callOptions403
                    
                     * @param packageVersionHeader404
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.MalformedSearchFault : 
                     * @throws com.salesforce.soap.partner.InvalidFieldFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.SearchResponse search(

                            com.salesforce.soap.partner.Search search401,com.salesforce.soap.partner.SessionHeader sessionHeader402,com.salesforce.soap.partner.CallOptions callOptions403,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader404)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.MalformedSearchFault
                        ,com.salesforce.soap.partner.InvalidFieldFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[12].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:searchRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    search401,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "search")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader402!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader402 = toOM(sessionHeader402, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "search")));
                                                    addHeader(omElementsessionHeader402,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions403!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions403 = toOM(callOptions403, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "search")));
                                                    addHeader(omElementcallOptions403,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader404!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader404 = toOM(packageVersionHeader404, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "search")));
                                                    addHeader(omElementpackageVersionHeader404,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.SearchResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.SearchResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.MalformedSearchFault){
                          throw (com.salesforce.soap.partner.MalformedSearchFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
                          throw (com.salesforce.soap.partner.InvalidFieldFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Search for sObjects
                * @see com.salesforce.soap.partner.SforceService#startsearch
                    * @param search401
                
                    * @param sessionHeader402
                
                    * @param callOptions403
                
                    * @param packageVersionHeader404
                
                */
                public  void startsearch(

                 com.salesforce.soap.partner.Search search401,com.salesforce.soap.partner.SessionHeader sessionHeader402,
                    com.salesforce.soap.partner.CallOptions callOptions403,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader404,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[12].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:searchRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    search401,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "search")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader402!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader402 = toOM(sessionHeader402, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "search")));
                                                    addHeader(omElementsessionHeader402,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions403!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions403 = toOM(callOptions403, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "search")));
                                                    addHeader(omElementcallOptions403,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader404!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader404 = toOM(packageVersionHeader404, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "search")));
                                                    addHeader(omElementpackageVersionHeader404,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.SearchResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultsearch(
                                        (com.salesforce.soap.partner.SearchResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorsearch(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrorsearch((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.MalformedSearchFault){
														callback.receiveErrorsearch((com.salesforce.soap.partner.MalformedSearchFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
														callback.receiveErrorsearch((com.salesforce.soap.partner.InvalidFieldFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorsearch((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorsearch(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsearch(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsearch(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsearch(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsearch(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsearch(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsearch(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorsearch(f);
                                            }
									    } else {
										    callback.receiveErrorsearch(f);
									    }
									} else {
									    callback.receiveErrorsearch(f);
									}
								} else {
								    callback.receiveErrorsearch(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorsearch(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[12].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[12].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Create a Query Cursor
                     * @see com.salesforce.soap.partner.SforceService#query
                     * @param query406
                    
                     * @param sessionHeader407
                    
                     * @param callOptions408
                    
                     * @param queryOptions409
                    
                     * @param mruHeader410
                    
                     * @param packageVersionHeader411
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.MalformedQueryFault : 
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.InvalidFieldFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.QueryResponse query(

                            com.salesforce.soap.partner.Query query406,com.salesforce.soap.partner.SessionHeader sessionHeader407,com.salesforce.soap.partner.CallOptions callOptions408,com.salesforce.soap.partner.QueryOptions queryOptions409,com.salesforce.soap.partner.MruHeader mruHeader410,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader411)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.MalformedQueryFault
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.InvalidFieldFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault
                        ,com.salesforce.soap.partner.InvalidQueryLocatorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[13].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:queryRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    query406,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "query")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader407!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader407 = toOM(sessionHeader407, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query")));
                                                    addHeader(omElementsessionHeader407,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions408!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions408 = toOM(callOptions408, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query")));
                                                    addHeader(omElementcallOptions408,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (queryOptions409!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementqueryOptions409 = toOM(queryOptions409, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query")));
                                                    addHeader(omElementqueryOptions409,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (mruHeader410!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementmruHeader410 = toOM(mruHeader410, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query")));
                                                    addHeader(omElementmruHeader410,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader411!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader411 = toOM(packageVersionHeader411, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query")));
                                                    addHeader(omElementpackageVersionHeader411,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.QueryResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.QueryResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.MalformedQueryFault){
                          throw (com.salesforce.soap.partner.MalformedQueryFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
                          throw (com.salesforce.soap.partner.InvalidFieldFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidQueryLocatorFault){
                          throw (com.salesforce.soap.partner.InvalidQueryLocatorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Create a Query Cursor
                * @see com.salesforce.soap.partner.SforceService#startquery
                    * @param query406
                
                    * @param sessionHeader407
                
                    * @param callOptions408
                
                    * @param queryOptions409
                
                    * @param mruHeader410
                
                    * @param packageVersionHeader411
                
                */
                public  void startquery(

                 com.salesforce.soap.partner.Query query406,com.salesforce.soap.partner.SessionHeader sessionHeader407,
                    com.salesforce.soap.partner.CallOptions callOptions408,
                    com.salesforce.soap.partner.QueryOptions queryOptions409,
                    com.salesforce.soap.partner.MruHeader mruHeader410,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader411,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[13].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:queryRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    query406,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "query")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader407!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader407 = toOM(sessionHeader407, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query")));
                                                    addHeader(omElementsessionHeader407,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions408!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions408 = toOM(callOptions408, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query")));
                                                    addHeader(omElementcallOptions408,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (queryOptions409!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementqueryOptions409 = toOM(queryOptions409, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query")));
                                                    addHeader(omElementqueryOptions409,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (mruHeader410!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementmruHeader410 = toOM(mruHeader410, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query")));
                                                    addHeader(omElementmruHeader410,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader411!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader411 = toOM(packageVersionHeader411, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query")));
                                                    addHeader(omElementpackageVersionHeader411,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.QueryResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultquery(
                                        (com.salesforce.soap.partner.QueryResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorquery(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrorquery((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.MalformedQueryFault){
														callback.receiveErrorquery((com.salesforce.soap.partner.MalformedQueryFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrorquery((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
														callback.receiveErrorquery((com.salesforce.soap.partner.InvalidFieldFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorquery((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidQueryLocatorFault){
														callback.receiveErrorquery((com.salesforce.soap.partner.InvalidQueryLocatorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorquery(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorquery(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorquery(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorquery(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorquery(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorquery(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorquery(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorquery(f);
                                            }
									    } else {
										    callback.receiveErrorquery(f);
									    }
									} else {
									    callback.receiveErrorquery(f);
									}
								} else {
								    callback.receiveErrorquery(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorquery(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[13].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[13].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Get the IDs for deleted sObjects
                     * @see com.salesforce.soap.partner.SforceService#getDeleted
                     * @param getDeleted413
                    
                     * @param sessionHeader414
                    
                     * @param callOptions415
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.GetDeletedResponse getDeleted(

                            com.salesforce.soap.partner.GetDeleted getDeleted413,com.salesforce.soap.partner.SessionHeader sessionHeader414,com.salesforce.soap.partner.CallOptions callOptions415)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[14].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:getDeletedRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    getDeleted413,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "getDeleted")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader414!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader414 = toOM(sessionHeader414, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getDeleted")));
                                                    addHeader(omElementsessionHeader414,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions415!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions415 = toOM(callOptions415, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getDeleted")));
                                                    addHeader(omElementcallOptions415,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.GetDeletedResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.GetDeletedResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Get the IDs for deleted sObjects
                * @see com.salesforce.soap.partner.SforceService#startgetDeleted
                    * @param getDeleted413
                
                    * @param sessionHeader414
                
                    * @param callOptions415
                
                */
                public  void startgetDeleted(

                 com.salesforce.soap.partner.GetDeleted getDeleted413,com.salesforce.soap.partner.SessionHeader sessionHeader414,
                    com.salesforce.soap.partner.CallOptions callOptions415,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[14].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:getDeletedRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    getDeleted413,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "getDeleted")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader414!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader414 = toOM(sessionHeader414, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getDeleted")));
                                                    addHeader(omElementsessionHeader414,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions415!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions415 = toOM(callOptions415, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getDeleted")));
                                                    addHeader(omElementcallOptions415,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.GetDeletedResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultgetDeleted(
                                        (com.salesforce.soap.partner.GetDeletedResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorgetDeleted(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrorgetDeleted((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorgetDeleted((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorgetDeleted(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetDeleted(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetDeleted(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetDeleted(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetDeleted(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetDeleted(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetDeleted(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetDeleted(f);
                                            }
									    } else {
										    callback.receiveErrorgetDeleted(f);
									    }
									} else {
									    callback.receiveErrorgetDeleted(f);
									}
								} else {
								    callback.receiveErrorgetDeleted(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorgetDeleted(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[14].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[14].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Submit an entity to a workflow process or process a workitem
                     * @see com.salesforce.soap.partner.SforceService#process
                     * @param process417
                    
                     * @param sessionHeader418
                    
                     * @param callOptions419
                    
                     * @param allowFieldTruncationHeader420
                    
                     * @param disableFeedTrackingHeader421
                    
                     * @param debuggingHeader422
                    
                     * @param packageVersionHeader423
                    
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.ProcessResponse process(

                            com.salesforce.soap.partner.Process process417,com.salesforce.soap.partner.SessionHeader sessionHeader418,com.salesforce.soap.partner.CallOptions callOptions419,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader420,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader421,com.salesforce.soap.partner.DebuggingHeader debuggingHeader422,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader423)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[15].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:processRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    process417,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "process")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader418!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader418 = toOM(sessionHeader418, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementsessionHeader418,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions419!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions419 = toOM(callOptions419, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementcallOptions419,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allowFieldTruncationHeader420!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader420 = toOM(allowFieldTruncationHeader420, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementallowFieldTruncationHeader420,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (disableFeedTrackingHeader421!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader421 = toOM(disableFeedTrackingHeader421, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementdisableFeedTrackingHeader421,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (debuggingHeader422!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader422 = toOM(debuggingHeader422, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementdebuggingHeader422,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader423!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader423 = toOM(packageVersionHeader423, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementpackageVersionHeader423,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.ProcessResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.ProcessResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Submit an entity to a workflow process or process a workitem
                * @see com.salesforce.soap.partner.SforceService#startprocess
                    * @param process417
                
                    * @param sessionHeader418
                
                    * @param callOptions419
                
                    * @param allowFieldTruncationHeader420
                
                    * @param disableFeedTrackingHeader421
                
                    * @param debuggingHeader422
                
                    * @param packageVersionHeader423
                
                */
                public  void startprocess(

                 com.salesforce.soap.partner.Process process417,com.salesforce.soap.partner.SessionHeader sessionHeader418,
                    com.salesforce.soap.partner.CallOptions callOptions419,
                    com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader420,
                    com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader421,
                    com.salesforce.soap.partner.DebuggingHeader debuggingHeader422,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader423,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[15].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:processRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    process417,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "process")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader418!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader418 = toOM(sessionHeader418, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementsessionHeader418,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions419!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions419 = toOM(callOptions419, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementcallOptions419,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allowFieldTruncationHeader420!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader420 = toOM(allowFieldTruncationHeader420, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementallowFieldTruncationHeader420,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (disableFeedTrackingHeader421!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader421 = toOM(disableFeedTrackingHeader421, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementdisableFeedTrackingHeader421,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (debuggingHeader422!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader422 = toOM(debuggingHeader422, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementdebuggingHeader422,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader423!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader423 = toOM(packageVersionHeader423, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process")));
                                                    addHeader(omElementpackageVersionHeader423,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.ProcessResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultprocess(
                                        (com.salesforce.soap.partner.ProcessResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorprocess(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrorprocess((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorprocess((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorprocess(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorprocess(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorprocess(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorprocess(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorprocess(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorprocess(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorprocess(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorprocess(f);
                                            }
									    } else {
										    callback.receiveErrorprocess(f);
									    }
									} else {
									    callback.receiveErrorprocess(f);
									}
								} else {
								    callback.receiveErrorprocess(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorprocess(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[15].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[15].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Describe the data category group structures for a given set of pair of types and data category group name
                     * @see com.salesforce.soap.partner.SforceService#describeDataCategoryGroupStructures
                     * @param describeDataCategoryGroupStructures425
                    
                     * @param sessionHeader426
                    
                     * @param callOptions427
                    
                     * @param packageVersionHeader428
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse describeDataCategoryGroupStructures(

                            com.salesforce.soap.partner.DescribeDataCategoryGroupStructures describeDataCategoryGroupStructures425,com.salesforce.soap.partner.SessionHeader sessionHeader426,com.salesforce.soap.partner.CallOptions callOptions427,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader428)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[16].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeDataCategoryGroupStructuresRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeDataCategoryGroupStructures425,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeDataCategoryGroupStructures")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader426!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader426 = toOM(sessionHeader426, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroupStructures")));
                                                    addHeader(omElementsessionHeader426,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions427!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions427 = toOM(callOptions427, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroupStructures")));
                                                    addHeader(omElementcallOptions427,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader428!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader428 = toOM(packageVersionHeader428, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroupStructures")));
                                                    addHeader(omElementpackageVersionHeader428,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Describe the data category group structures for a given set of pair of types and data category group name
                * @see com.salesforce.soap.partner.SforceService#startdescribeDataCategoryGroupStructures
                    * @param describeDataCategoryGroupStructures425
                
                    * @param sessionHeader426
                
                    * @param callOptions427
                
                    * @param packageVersionHeader428
                
                */
                public  void startdescribeDataCategoryGroupStructures(

                 com.salesforce.soap.partner.DescribeDataCategoryGroupStructures describeDataCategoryGroupStructures425,com.salesforce.soap.partner.SessionHeader sessionHeader426,
                    com.salesforce.soap.partner.CallOptions callOptions427,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader428,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[16].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeDataCategoryGroupStructuresRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeDataCategoryGroupStructures425,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeDataCategoryGroupStructures")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader426!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader426 = toOM(sessionHeader426, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroupStructures")));
                                                    addHeader(omElementsessionHeader426,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions427!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions427 = toOM(callOptions427, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroupStructures")));
                                                    addHeader(omElementcallOptions427,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader428!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader428 = toOM(packageVersionHeader428, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroupStructures")));
                                                    addHeader(omElementpackageVersionHeader428,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultdescribeDataCategoryGroupStructures(
                                        (com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrordescribeDataCategoryGroupStructures(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrordescribeDataCategoryGroupStructures((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrordescribeDataCategoryGroupStructures((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrordescribeDataCategoryGroupStructures(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroupStructures(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroupStructures(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroupStructures(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroupStructures(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroupStructures(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroupStructures(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroupStructures(f);
                                            }
									    } else {
										    callback.receiveErrordescribeDataCategoryGroupStructures(f);
									    }
									} else {
									    callback.receiveErrordescribeDataCategoryGroupStructures(f);
									}
								} else {
								    callback.receiveErrordescribeDataCategoryGroupStructures(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrordescribeDataCategoryGroupStructures(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[16].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[16].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Reset a user's password
                     * @see com.salesforce.soap.partner.SforceService#resetPassword
                     * @param resetPassword430
                    
                     * @param sessionHeader431
                    
                     * @param callOptions432
                    
                     * @param emailHeader433
                    
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.ResetPasswordResponse resetPassword(

                            com.salesforce.soap.partner.ResetPassword resetPassword430,com.salesforce.soap.partner.SessionHeader sessionHeader431,com.salesforce.soap.partner.CallOptions callOptions432,com.salesforce.soap.partner.EmailHeader emailHeader433)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[17].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:resetPasswordRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    resetPassword430,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "resetPassword")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader431!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader431 = toOM(sessionHeader431, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "resetPassword")));
                                                    addHeader(omElementsessionHeader431,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions432!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions432 = toOM(callOptions432, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "resetPassword")));
                                                    addHeader(omElementcallOptions432,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (emailHeader433!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementemailHeader433 = toOM(emailHeader433, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "resetPassword")));
                                                    addHeader(omElementemailHeader433,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.ResetPasswordResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.ResetPasswordResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Reset a user's password
                * @see com.salesforce.soap.partner.SforceService#startresetPassword
                    * @param resetPassword430
                
                    * @param sessionHeader431
                
                    * @param callOptions432
                
                    * @param emailHeader433
                
                */
                public  void startresetPassword(

                 com.salesforce.soap.partner.ResetPassword resetPassword430,com.salesforce.soap.partner.SessionHeader sessionHeader431,
                    com.salesforce.soap.partner.CallOptions callOptions432,
                    com.salesforce.soap.partner.EmailHeader emailHeader433,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[17].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:resetPasswordRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    resetPassword430,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "resetPassword")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader431!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader431 = toOM(sessionHeader431, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "resetPassword")));
                                                    addHeader(omElementsessionHeader431,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions432!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions432 = toOM(callOptions432, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "resetPassword")));
                                                    addHeader(omElementcallOptions432,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (emailHeader433!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementemailHeader433 = toOM(emailHeader433, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "resetPassword")));
                                                    addHeader(omElementemailHeader433,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.ResetPasswordResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultresetPassword(
                                        (com.salesforce.soap.partner.ResetPasswordResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorresetPassword(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrorresetPassword((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorresetPassword((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorresetPassword(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorresetPassword(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorresetPassword(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorresetPassword(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorresetPassword(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorresetPassword(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorresetPassword(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorresetPassword(f);
                                            }
									    } else {
										    callback.receiveErrorresetPassword(f);
									    }
									} else {
									    callback.receiveErrorresetPassword(f);
									}
								} else {
								    callback.receiveErrorresetPassword(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorresetPassword(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[17].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[17].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Describe the Global state
                     * @see com.salesforce.soap.partner.SforceService#describeGlobal
                     * @param describeGlobal435
                    
                     * @param sessionHeader436
                    
                     * @param callOptions437
                    
                     * @param packageVersionHeader438
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.DescribeGlobalResponse describeGlobal(

                            com.salesforce.soap.partner.DescribeGlobal describeGlobal435,com.salesforce.soap.partner.SessionHeader sessionHeader436,com.salesforce.soap.partner.CallOptions callOptions437,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader438)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[18].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeGlobalRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeGlobal435,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeGlobal")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader436!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader436 = toOM(sessionHeader436, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeGlobal")));
                                                    addHeader(omElementsessionHeader436,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions437!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions437 = toOM(callOptions437, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeGlobal")));
                                                    addHeader(omElementcallOptions437,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader438!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader438 = toOM(packageVersionHeader438, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeGlobal")));
                                                    addHeader(omElementpackageVersionHeader438,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.DescribeGlobalResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.DescribeGlobalResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Describe the Global state
                * @see com.salesforce.soap.partner.SforceService#startdescribeGlobal
                    * @param describeGlobal435
                
                    * @param sessionHeader436
                
                    * @param callOptions437
                
                    * @param packageVersionHeader438
                
                */
                public  void startdescribeGlobal(

                 com.salesforce.soap.partner.DescribeGlobal describeGlobal435,com.salesforce.soap.partner.SessionHeader sessionHeader436,
                    com.salesforce.soap.partner.CallOptions callOptions437,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader438,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[18].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeGlobalRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeGlobal435,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeGlobal")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader436!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader436 = toOM(sessionHeader436, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeGlobal")));
                                                    addHeader(omElementsessionHeader436,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions437!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions437 = toOM(callOptions437, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeGlobal")));
                                                    addHeader(omElementcallOptions437,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader438!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader438 = toOM(packageVersionHeader438, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeGlobal")));
                                                    addHeader(omElementpackageVersionHeader438,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.DescribeGlobalResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultdescribeGlobal(
                                        (com.salesforce.soap.partner.DescribeGlobalResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrordescribeGlobal(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrordescribeGlobal((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrordescribeGlobal(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeGlobal(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeGlobal(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeGlobal(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeGlobal(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeGlobal(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeGlobal(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeGlobal(f);
                                            }
									    } else {
										    callback.receiveErrordescribeGlobal(f);
									    }
									} else {
									    callback.receiveErrordescribeGlobal(f);
									}
								} else {
								    callback.receiveErrordescribeGlobal(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrordescribeGlobal(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[18].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[18].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Describe the layout of an sObject
                     * @see com.salesforce.soap.partner.SforceService#describeLayout
                     * @param describeLayout440
                    
                     * @param sessionHeader441
                    
                     * @param callOptions442
                    
                     * @param packageVersionHeader443
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.DescribeLayoutResponse describeLayout(

                            com.salesforce.soap.partner.DescribeLayoutE describeLayout440,com.salesforce.soap.partner.SessionHeader sessionHeader441,com.salesforce.soap.partner.CallOptions callOptions442,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader443)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[19].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeLayoutRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeLayout440,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeLayout")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader441!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader441 = toOM(sessionHeader441, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeLayout")));
                                                    addHeader(omElementsessionHeader441,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions442!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions442 = toOM(callOptions442, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeLayout")));
                                                    addHeader(omElementcallOptions442,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader443!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader443 = toOM(packageVersionHeader443, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeLayout")));
                                                    addHeader(omElementpackageVersionHeader443,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.DescribeLayoutResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.DescribeLayoutResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Describe the layout of an sObject
                * @see com.salesforce.soap.partner.SforceService#startdescribeLayout
                    * @param describeLayout440
                
                    * @param sessionHeader441
                
                    * @param callOptions442
                
                    * @param packageVersionHeader443
                
                */
                public  void startdescribeLayout(

                 com.salesforce.soap.partner.DescribeLayoutE describeLayout440,com.salesforce.soap.partner.SessionHeader sessionHeader441,
                    com.salesforce.soap.partner.CallOptions callOptions442,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader443,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[19].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeLayoutRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeLayout440,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeLayout")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader441!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader441 = toOM(sessionHeader441, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeLayout")));
                                                    addHeader(omElementsessionHeader441,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions442!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions442 = toOM(callOptions442, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeLayout")));
                                                    addHeader(omElementcallOptions442,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader443!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader443 = toOM(packageVersionHeader443, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeLayout")));
                                                    addHeader(omElementpackageVersionHeader443,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.DescribeLayoutResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultdescribeLayout(
                                        (com.salesforce.soap.partner.DescribeLayoutResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrordescribeLayout(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrordescribeLayout((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrordescribeLayout((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrordescribeLayout((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrordescribeLayout(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeLayout(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeLayout(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeLayout(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeLayout(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeLayout(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeLayout(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeLayout(f);
                                            }
									    } else {
										    callback.receiveErrordescribeLayout(f);
									    }
									} else {
									    callback.receiveErrordescribeLayout(f);
									}
								} else {
								    callback.receiveErrordescribeLayout(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrordescribeLayout(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[19].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[19].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Describe the tabs that appear on a users page
                     * @see com.salesforce.soap.partner.SforceService#describeTabs
                     * @param describeTabs445
                    
                     * @param sessionHeader446
                    
                     * @param callOptions447
                    
                     * @param packageVersionHeader448
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.DescribeTabsResponse describeTabs(

                            com.salesforce.soap.partner.DescribeTabs describeTabs445,com.salesforce.soap.partner.SessionHeader sessionHeader446,com.salesforce.soap.partner.CallOptions callOptions447,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader448)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[20].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeTabsRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeTabs445,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeTabs")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader446!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader446 = toOM(sessionHeader446, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeTabs")));
                                                    addHeader(omElementsessionHeader446,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions447!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions447 = toOM(callOptions447, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeTabs")));
                                                    addHeader(omElementcallOptions447,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader448!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader448 = toOM(packageVersionHeader448, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeTabs")));
                                                    addHeader(omElementpackageVersionHeader448,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.DescribeTabsResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.DescribeTabsResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Describe the tabs that appear on a users page
                * @see com.salesforce.soap.partner.SforceService#startdescribeTabs
                    * @param describeTabs445
                
                    * @param sessionHeader446
                
                    * @param callOptions447
                
                    * @param packageVersionHeader448
                
                */
                public  void startdescribeTabs(

                 com.salesforce.soap.partner.DescribeTabs describeTabs445,com.salesforce.soap.partner.SessionHeader sessionHeader446,
                    com.salesforce.soap.partner.CallOptions callOptions447,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader448,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[20].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeTabsRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeTabs445,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeTabs")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader446!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader446 = toOM(sessionHeader446, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeTabs")));
                                                    addHeader(omElementsessionHeader446,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions447!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions447 = toOM(callOptions447, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeTabs")));
                                                    addHeader(omElementcallOptions447,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader448!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader448 = toOM(packageVersionHeader448, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeTabs")));
                                                    addHeader(omElementpackageVersionHeader448,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.DescribeTabsResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultdescribeTabs(
                                        (com.salesforce.soap.partner.DescribeTabsResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrordescribeTabs(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrordescribeTabs((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrordescribeTabs(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeTabs(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeTabs(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeTabs(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeTabs(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeTabs(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeTabs(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeTabs(f);
                                            }
									    } else {
										    callback.receiveErrordescribeTabs(f);
									    }
									} else {
									    callback.receiveErrordescribeTabs(f);
									}
								} else {
								    callback.receiveErrordescribeTabs(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrordescribeTabs(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[20].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[20].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Describe all the data category groups available for a given set of types
                     * @see com.salesforce.soap.partner.SforceService#describeDataCategoryGroups
                     * @param describeDataCategoryGroups450
                    
                     * @param sessionHeader451
                    
                     * @param callOptions452
                    
                     * @param packageVersionHeader453
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse describeDataCategoryGroups(

                            com.salesforce.soap.partner.DescribeDataCategoryGroups describeDataCategoryGroups450,com.salesforce.soap.partner.SessionHeader sessionHeader451,com.salesforce.soap.partner.CallOptions callOptions452,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader453)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[21].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeDataCategoryGroupsRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeDataCategoryGroups450,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeDataCategoryGroups")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader451!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader451 = toOM(sessionHeader451, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroups")));
                                                    addHeader(omElementsessionHeader451,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions452!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions452 = toOM(callOptions452, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroups")));
                                                    addHeader(omElementcallOptions452,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader453!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader453 = toOM(packageVersionHeader453, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroups")));
                                                    addHeader(omElementpackageVersionHeader453,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Describe all the data category groups available for a given set of types
                * @see com.salesforce.soap.partner.SforceService#startdescribeDataCategoryGroups
                    * @param describeDataCategoryGroups450
                
                    * @param sessionHeader451
                
                    * @param callOptions452
                
                    * @param packageVersionHeader453
                
                */
                public  void startdescribeDataCategoryGroups(

                 com.salesforce.soap.partner.DescribeDataCategoryGroups describeDataCategoryGroups450,com.salesforce.soap.partner.SessionHeader sessionHeader451,
                    com.salesforce.soap.partner.CallOptions callOptions452,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader453,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[21].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeDataCategoryGroupsRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeDataCategoryGroups450,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeDataCategoryGroups")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader451!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader451 = toOM(sessionHeader451, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroups")));
                                                    addHeader(omElementsessionHeader451,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions452!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions452 = toOM(callOptions452, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroups")));
                                                    addHeader(omElementcallOptions452,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader453!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader453 = toOM(packageVersionHeader453, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeDataCategoryGroups")));
                                                    addHeader(omElementpackageVersionHeader453,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultdescribeDataCategoryGroups(
                                        (com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrordescribeDataCategoryGroups(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrordescribeDataCategoryGroups((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrordescribeDataCategoryGroups((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrordescribeDataCategoryGroups(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroups(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroups(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroups(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroups(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroups(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroups(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeDataCategoryGroups(f);
                                            }
									    } else {
										    callback.receiveErrordescribeDataCategoryGroups(f);
									    }
									} else {
									    callback.receiveErrordescribeDataCategoryGroups(f);
									}
								} else {
								    callback.receiveErrordescribeDataCategoryGroups(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrordescribeDataCategoryGroups(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[21].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[21].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Gets server timestamp
                     * @see com.salesforce.soap.partner.SforceService#getServerTimestamp
                     * @param getServerTimestamp455
                    
                     * @param sessionHeader456
                    
                     * @param callOptions457
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.GetServerTimestampResponse getServerTimestamp(

                            com.salesforce.soap.partner.GetServerTimestamp getServerTimestamp455,com.salesforce.soap.partner.SessionHeader sessionHeader456,com.salesforce.soap.partner.CallOptions callOptions457)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[22].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:getServerTimestampRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    getServerTimestamp455,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "getServerTimestamp")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader456!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader456 = toOM(sessionHeader456, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getServerTimestamp")));
                                                    addHeader(omElementsessionHeader456,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions457!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions457 = toOM(callOptions457, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getServerTimestamp")));
                                                    addHeader(omElementcallOptions457,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.GetServerTimestampResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.GetServerTimestampResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Gets server timestamp
                * @see com.salesforce.soap.partner.SforceService#startgetServerTimestamp
                    * @param getServerTimestamp455
                
                    * @param sessionHeader456
                
                    * @param callOptions457
                
                */
                public  void startgetServerTimestamp(

                 com.salesforce.soap.partner.GetServerTimestamp getServerTimestamp455,com.salesforce.soap.partner.SessionHeader sessionHeader456,
                    com.salesforce.soap.partner.CallOptions callOptions457,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[22].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:getServerTimestampRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    getServerTimestamp455,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "getServerTimestamp")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader456!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader456 = toOM(sessionHeader456, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getServerTimestamp")));
                                                    addHeader(omElementsessionHeader456,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions457!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions457 = toOM(callOptions457, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getServerTimestamp")));
                                                    addHeader(omElementcallOptions457,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.GetServerTimestampResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultgetServerTimestamp(
                                        (com.salesforce.soap.partner.GetServerTimestampResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorgetServerTimestamp(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorgetServerTimestamp((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorgetServerTimestamp(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetServerTimestamp(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetServerTimestamp(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetServerTimestamp(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetServerTimestamp(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetServerTimestamp(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetServerTimestamp(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorgetServerTimestamp(f);
                                            }
									    } else {
										    callback.receiveErrorgetServerTimestamp(f);
									    }
									} else {
									    callback.receiveErrorgetServerTimestamp(f);
									}
								} else {
								    callback.receiveErrorgetServerTimestamp(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorgetServerTimestamp(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[22].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[22].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Logs out and invalidates session ids
                     * @see com.salesforce.soap.partner.SforceService#invalidateSessions
                     * @param invalidateSessions459
                    
                     * @param sessionHeader460
                    
                     * @param callOptions461
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.InvalidateSessionsResponse invalidateSessions(

                            com.salesforce.soap.partner.InvalidateSessions invalidateSessions459,com.salesforce.soap.partner.SessionHeader sessionHeader460,com.salesforce.soap.partner.CallOptions callOptions461)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[23].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:invalidateSessionsRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    invalidateSessions459,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "invalidateSessions")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader460!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader460 = toOM(sessionHeader460, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "invalidateSessions")));
                                                    addHeader(omElementsessionHeader460,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions461!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions461 = toOM(callOptions461, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "invalidateSessions")));
                                                    addHeader(omElementcallOptions461,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.InvalidateSessionsResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.InvalidateSessionsResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Logs out and invalidates session ids
                * @see com.salesforce.soap.partner.SforceService#startinvalidateSessions
                    * @param invalidateSessions459
                
                    * @param sessionHeader460
                
                    * @param callOptions461
                
                */
                public  void startinvalidateSessions(

                 com.salesforce.soap.partner.InvalidateSessions invalidateSessions459,com.salesforce.soap.partner.SessionHeader sessionHeader460,
                    com.salesforce.soap.partner.CallOptions callOptions461,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[23].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:invalidateSessionsRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    invalidateSessions459,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "invalidateSessions")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader460!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader460 = toOM(sessionHeader460, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "invalidateSessions")));
                                                    addHeader(omElementsessionHeader460,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions461!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions461 = toOM(callOptions461, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "invalidateSessions")));
                                                    addHeader(omElementcallOptions461,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.InvalidateSessionsResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultinvalidateSessions(
                                        (com.salesforce.soap.partner.InvalidateSessionsResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorinvalidateSessions(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorinvalidateSessions((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorinvalidateSessions(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorinvalidateSessions(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorinvalidateSessions(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorinvalidateSessions(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorinvalidateSessions(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorinvalidateSessions(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorinvalidateSessions(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorinvalidateSessions(f);
                                            }
									    } else {
										    callback.receiveErrorinvalidateSessions(f);
									    }
									} else {
									    callback.receiveErrorinvalidateSessions(f);
									}
								} else {
								    callback.receiveErrorinvalidateSessions(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorinvalidateSessions(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[23].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[23].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Describe an sObject
                     * @see com.salesforce.soap.partner.SforceService#describeSObject
                     * @param describeSObject463
                    
                     * @param sessionHeader464
                    
                     * @param callOptions465
                    
                     * @param packageVersionHeader466
                    
                     * @param localeOptions467
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.DescribeSObjectResponse describeSObject(

                            com.salesforce.soap.partner.DescribeSObject describeSObject463,com.salesforce.soap.partner.SessionHeader sessionHeader464,com.salesforce.soap.partner.CallOptions callOptions465,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader466,com.salesforce.soap.partner.LocaleOptions localeOptions467)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[24].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeSObjectRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeSObject463,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeSObject")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader464!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader464 = toOM(sessionHeader464, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObject")));
                                                    addHeader(omElementsessionHeader464,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions465!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions465 = toOM(callOptions465, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObject")));
                                                    addHeader(omElementcallOptions465,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader466!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader466 = toOM(packageVersionHeader466, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObject")));
                                                    addHeader(omElementpackageVersionHeader466,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (localeOptions467!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementlocaleOptions467 = toOM(localeOptions467, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObject")));
                                                    addHeader(omElementlocaleOptions467,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.DescribeSObjectResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.DescribeSObjectResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Describe an sObject
                * @see com.salesforce.soap.partner.SforceService#startdescribeSObject
                    * @param describeSObject463
                
                    * @param sessionHeader464
                
                    * @param callOptions465
                
                    * @param packageVersionHeader466
                
                    * @param localeOptions467
                
                */
                public  void startdescribeSObject(

                 com.salesforce.soap.partner.DescribeSObject describeSObject463,com.salesforce.soap.partner.SessionHeader sessionHeader464,
                    com.salesforce.soap.partner.CallOptions callOptions465,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader466,
                    com.salesforce.soap.partner.LocaleOptions localeOptions467,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[24].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeSObjectRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeSObject463,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeSObject")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader464!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader464 = toOM(sessionHeader464, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObject")));
                                                    addHeader(omElementsessionHeader464,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions465!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions465 = toOM(callOptions465, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObject")));
                                                    addHeader(omElementcallOptions465,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader466!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader466 = toOM(packageVersionHeader466, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObject")));
                                                    addHeader(omElementpackageVersionHeader466,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (localeOptions467!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementlocaleOptions467 = toOM(localeOptions467, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObject")));
                                                    addHeader(omElementlocaleOptions467,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.DescribeSObjectResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultdescribeSObject(
                                        (com.salesforce.soap.partner.DescribeSObjectResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrordescribeSObject(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrordescribeSObject((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrordescribeSObject((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrordescribeSObject(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObject(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObject(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObject(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObject(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObject(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObject(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObject(f);
                                            }
									    } else {
										    callback.receiveErrordescribeSObject(f);
									    }
									} else {
									    callback.receiveErrordescribeSObject(f);
									}
								} else {
								    callback.receiveErrordescribeSObject(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrordescribeSObject(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[24].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[24].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Login to the Salesforce.com SOAP Api
                     * @see com.salesforce.soap.partner.SforceService#login
                     * @param login469
                    
                     * @param loginScopeHeader470
                    
                     * @param callOptions471
                    
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     * @throws com.salesforce.soap.partner.LoginFault : 
                     */

                    

                            public  com.salesforce.soap.partner.LoginResponse login(

                            com.salesforce.soap.partner.Login login469,com.salesforce.soap.partner.LoginScopeHeader loginScopeHeader470,com.salesforce.soap.partner.CallOptions callOptions471)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault
                        ,com.salesforce.soap.partner.LoginFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[25].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:loginRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    login469,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "login")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (loginScopeHeader470!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementloginScopeHeader470 = toOM(loginScopeHeader470, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "login")));
                                                    addHeader(omElementloginScopeHeader470,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions471!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions471 = toOM(callOptions471, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "login")));
                                                    addHeader(omElementcallOptions471,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.LoginResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.LoginResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.LoginFault){
                          throw (com.salesforce.soap.partner.LoginFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Login to the Salesforce.com SOAP Api
                * @see com.salesforce.soap.partner.SforceService#startlogin
                    * @param login469
                
                    * @param loginScopeHeader470
                
                    * @param callOptions471
                
                */
                public  void startlogin(

                 com.salesforce.soap.partner.Login login469,com.salesforce.soap.partner.LoginScopeHeader loginScopeHeader470,
                    com.salesforce.soap.partner.CallOptions callOptions471,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[25].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:loginRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    login469,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "login")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (loginScopeHeader470!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementloginScopeHeader470 = toOM(loginScopeHeader470, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "login")));
                                                    addHeader(omElementloginScopeHeader470,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions471!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions471 = toOM(callOptions471, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "login")));
                                                    addHeader(omElementcallOptions471,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.LoginResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultlogin(
                                        (com.salesforce.soap.partner.LoginResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorlogin(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrorlogin((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorlogin((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.LoginFault){
														callback.receiveErrorlogin((com.salesforce.soap.partner.LoginFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorlogin(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogin(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogin(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogin(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogin(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogin(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogin(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorlogin(f);
                                            }
									    } else {
										    callback.receiveErrorlogin(f);
									    }
									} else {
									    callback.receiveErrorlogin(f);
									}
								} else {
								    callback.receiveErrorlogin(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorlogin(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[25].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[25].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Gets the next batch of sObjects from a query
                     * @see com.salesforce.soap.partner.SforceService#queryMore
                     * @param queryMore473
                    
                     * @param sessionHeader474
                    
                     * @param callOptions475
                    
                     * @param queryOptions476
                    
                     * @throws com.salesforce.soap.partner.InvalidFieldFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.QueryMoreResponse queryMore(

                            com.salesforce.soap.partner.QueryMore queryMore473,com.salesforce.soap.partner.SessionHeader sessionHeader474,com.salesforce.soap.partner.CallOptions callOptions475,com.salesforce.soap.partner.QueryOptions queryOptions476)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidFieldFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault
                        ,com.salesforce.soap.partner.InvalidQueryLocatorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[26].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:queryMoreRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    queryMore473,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "queryMore")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader474!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader474 = toOM(sessionHeader474, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryMore")));
                                                    addHeader(omElementsessionHeader474,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions475!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions475 = toOM(callOptions475, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryMore")));
                                                    addHeader(omElementcallOptions475,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (queryOptions476!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementqueryOptions476 = toOM(queryOptions476, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryMore")));
                                                    addHeader(omElementqueryOptions476,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.QueryMoreResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.QueryMoreResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
                          throw (com.salesforce.soap.partner.InvalidFieldFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidQueryLocatorFault){
                          throw (com.salesforce.soap.partner.InvalidQueryLocatorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Gets the next batch of sObjects from a query
                * @see com.salesforce.soap.partner.SforceService#startqueryMore
                    * @param queryMore473
                
                    * @param sessionHeader474
                
                    * @param callOptions475
                
                    * @param queryOptions476
                
                */
                public  void startqueryMore(

                 com.salesforce.soap.partner.QueryMore queryMore473,com.salesforce.soap.partner.SessionHeader sessionHeader474,
                    com.salesforce.soap.partner.CallOptions callOptions475,
                    com.salesforce.soap.partner.QueryOptions queryOptions476,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[26].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:queryMoreRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    queryMore473,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "queryMore")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader474!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader474 = toOM(sessionHeader474, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryMore")));
                                                    addHeader(omElementsessionHeader474,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions475!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions475 = toOM(callOptions475, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryMore")));
                                                    addHeader(omElementcallOptions475,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (queryOptions476!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementqueryOptions476 = toOM(queryOptions476, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryMore")));
                                                    addHeader(omElementqueryOptions476,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.QueryMoreResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultqueryMore(
                                        (com.salesforce.soap.partner.QueryMoreResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorqueryMore(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
														callback.receiveErrorqueryMore((com.salesforce.soap.partner.InvalidFieldFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorqueryMore((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidQueryLocatorFault){
														callback.receiveErrorqueryMore((com.salesforce.soap.partner.InvalidQueryLocatorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorqueryMore(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryMore(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryMore(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryMore(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryMore(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryMore(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryMore(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorqueryMore(f);
                                            }
									    } else {
										    callback.receiveErrorqueryMore(f);
									    }
									} else {
									    callback.receiveErrorqueryMore(f);
									}
								} else {
								    callback.receiveErrorqueryMore(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorqueryMore(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[26].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[26].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Describe a number sObjects
                     * @see com.salesforce.soap.partner.SforceService#describeSObjects
                     * @param describeSObjects478
                    
                     * @param sessionHeader479
                    
                     * @param callOptions480
                    
                     * @param packageVersionHeader481
                    
                     * @param localeOptions482
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.DescribeSObjectsResponse describeSObjects(

                            com.salesforce.soap.partner.DescribeSObjects describeSObjects478,com.salesforce.soap.partner.SessionHeader sessionHeader479,com.salesforce.soap.partner.CallOptions callOptions480,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader481,com.salesforce.soap.partner.LocaleOptions localeOptions482)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[27].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeSObjectsRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeSObjects478,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeSObjects")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader479!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader479 = toOM(sessionHeader479, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObjects")));
                                                    addHeader(omElementsessionHeader479,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions480!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions480 = toOM(callOptions480, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObjects")));
                                                    addHeader(omElementcallOptions480,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader481!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader481 = toOM(packageVersionHeader481, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObjects")));
                                                    addHeader(omElementpackageVersionHeader481,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (localeOptions482!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementlocaleOptions482 = toOM(localeOptions482, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObjects")));
                                                    addHeader(omElementlocaleOptions482,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.DescribeSObjectsResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.DescribeSObjectsResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Describe a number sObjects
                * @see com.salesforce.soap.partner.SforceService#startdescribeSObjects
                    * @param describeSObjects478
                
                    * @param sessionHeader479
                
                    * @param callOptions480
                
                    * @param packageVersionHeader481
                
                    * @param localeOptions482
                
                */
                public  void startdescribeSObjects(

                 com.salesforce.soap.partner.DescribeSObjects describeSObjects478,com.salesforce.soap.partner.SessionHeader sessionHeader479,
                    com.salesforce.soap.partner.CallOptions callOptions480,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader481,
                    com.salesforce.soap.partner.LocaleOptions localeOptions482,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[27].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:describeSObjectsRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    describeSObjects478,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "describeSObjects")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader479!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader479 = toOM(sessionHeader479, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObjects")));
                                                    addHeader(omElementsessionHeader479,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions480!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions480 = toOM(callOptions480, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObjects")));
                                                    addHeader(omElementcallOptions480,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader481!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader481 = toOM(packageVersionHeader481, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObjects")));
                                                    addHeader(omElementpackageVersionHeader481,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (localeOptions482!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementlocaleOptions482 = toOM(localeOptions482, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObjects")));
                                                    addHeader(omElementlocaleOptions482,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.DescribeSObjectsResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultdescribeSObjects(
                                        (com.salesforce.soap.partner.DescribeSObjectsResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrordescribeSObjects(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrordescribeSObjects((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrordescribeSObjects((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrordescribeSObjects(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObjects(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObjects(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObjects(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObjects(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObjects(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObjects(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordescribeSObjects(f);
                                            }
									    } else {
										    callback.receiveErrordescribeSObjects(f);
									    }
									} else {
									    callback.receiveErrordescribeSObjects(f);
									}
								} else {
								    callback.receiveErrordescribeSObjects(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrordescribeSObjects(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[27].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[27].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Empty a set of sObjects from the recycle bin
                     * @see com.salesforce.soap.partner.SforceService#emptyRecycleBin
                     * @param emptyRecycleBin484
                    
                     * @param sessionHeader485
                    
                     * @param callOptions486
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.EmptyRecycleBinResponse emptyRecycleBin(

                            com.salesforce.soap.partner.EmptyRecycleBin emptyRecycleBin484,com.salesforce.soap.partner.SessionHeader sessionHeader485,com.salesforce.soap.partner.CallOptions callOptions486)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[28].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:emptyRecycleBinRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    emptyRecycleBin484,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "emptyRecycleBin")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader485!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader485 = toOM(sessionHeader485, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "emptyRecycleBin")));
                                                    addHeader(omElementsessionHeader485,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions486!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions486 = toOM(callOptions486, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "emptyRecycleBin")));
                                                    addHeader(omElementcallOptions486,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.EmptyRecycleBinResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.EmptyRecycleBinResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Empty a set of sObjects from the recycle bin
                * @see com.salesforce.soap.partner.SforceService#startemptyRecycleBin
                    * @param emptyRecycleBin484
                
                    * @param sessionHeader485
                
                    * @param callOptions486
                
                */
                public  void startemptyRecycleBin(

                 com.salesforce.soap.partner.EmptyRecycleBin emptyRecycleBin484,com.salesforce.soap.partner.SessionHeader sessionHeader485,
                    com.salesforce.soap.partner.CallOptions callOptions486,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[28].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:emptyRecycleBinRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    emptyRecycleBin484,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "emptyRecycleBin")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader485!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader485 = toOM(sessionHeader485, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "emptyRecycleBin")));
                                                    addHeader(omElementsessionHeader485,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions486!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions486 = toOM(callOptions486, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "emptyRecycleBin")));
                                                    addHeader(omElementcallOptions486,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.EmptyRecycleBinResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultemptyRecycleBin(
                                        (com.salesforce.soap.partner.EmptyRecycleBinResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErroremptyRecycleBin(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErroremptyRecycleBin((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErroremptyRecycleBin(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErroremptyRecycleBin(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErroremptyRecycleBin(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErroremptyRecycleBin(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErroremptyRecycleBin(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErroremptyRecycleBin(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErroremptyRecycleBin(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErroremptyRecycleBin(f);
                                            }
									    } else {
										    callback.receiveErroremptyRecycleBin(f);
									    }
									} else {
									    callback.receiveErroremptyRecycleBin(f);
									}
								} else {
								    callback.receiveErroremptyRecycleBin(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErroremptyRecycleBin(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[28].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[28].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Update or insert a set of sObjects based on object id
                     * @see com.salesforce.soap.partner.SforceService#upsert
                     * @param upsert488
                    
                     * @param sessionHeader489
                    
                     * @param callOptions490
                    
                     * @param assignmentRuleHeader491
                    
                     * @param mruHeader492
                    
                     * @param allowFieldTruncationHeader493
                    
                     * @param disableFeedTrackingHeader494
                    
                     * @param allOrNoneHeader495
                    
                     * @param debuggingHeader496
                    
                     * @param packageVersionHeader497
                    
                     * @param emailHeader498
                    
                     * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
                     * @throws com.salesforce.soap.partner.InvalidIdFault : 
                     * @throws com.salesforce.soap.partner.InvalidFieldFault : 
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.UpsertResponse upsert(

                            com.salesforce.soap.partner.Upsert upsert488,com.salesforce.soap.partner.SessionHeader sessionHeader489,com.salesforce.soap.partner.CallOptions callOptions490,com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader491,com.salesforce.soap.partner.MruHeader mruHeader492,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader493,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader494,com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader495,com.salesforce.soap.partner.DebuggingHeader debuggingHeader496,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader497,com.salesforce.soap.partner.EmailHeader emailHeader498)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.InvalidSObjectFault
                        ,com.salesforce.soap.partner.InvalidIdFault
                        ,com.salesforce.soap.partner.InvalidFieldFault
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[29].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:upsertRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    upsert488,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "upsert")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader489!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader489 = toOM(sessionHeader489, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementsessionHeader489,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions490!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions490 = toOM(callOptions490, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementcallOptions490,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (assignmentRuleHeader491!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementassignmentRuleHeader491 = toOM(assignmentRuleHeader491, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementassignmentRuleHeader491,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (mruHeader492!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementmruHeader492 = toOM(mruHeader492, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementmruHeader492,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allowFieldTruncationHeader493!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader493 = toOM(allowFieldTruncationHeader493, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementallowFieldTruncationHeader493,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (disableFeedTrackingHeader494!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader494 = toOM(disableFeedTrackingHeader494, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementdisableFeedTrackingHeader494,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allOrNoneHeader495!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallOrNoneHeader495 = toOM(allOrNoneHeader495, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementallOrNoneHeader495,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (debuggingHeader496!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader496 = toOM(debuggingHeader496, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementdebuggingHeader496,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader497!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader497 = toOM(packageVersionHeader497, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementpackageVersionHeader497,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (emailHeader498!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementemailHeader498 = toOM(emailHeader498, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementemailHeader498,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.UpsertResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.UpsertResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
                          throw (com.salesforce.soap.partner.InvalidSObjectFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
                          throw (com.salesforce.soap.partner.InvalidIdFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
                          throw (com.salesforce.soap.partner.InvalidFieldFault)ex;
                        }
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Update or insert a set of sObjects based on object id
                * @see com.salesforce.soap.partner.SforceService#startupsert
                    * @param upsert488
                
                    * @param sessionHeader489
                
                    * @param callOptions490
                
                    * @param assignmentRuleHeader491
                
                    * @param mruHeader492
                
                    * @param allowFieldTruncationHeader493
                
                    * @param disableFeedTrackingHeader494
                
                    * @param allOrNoneHeader495
                
                    * @param debuggingHeader496
                
                    * @param packageVersionHeader497
                
                    * @param emailHeader498
                
                */
                public  void startupsert(

                 com.salesforce.soap.partner.Upsert upsert488,com.salesforce.soap.partner.SessionHeader sessionHeader489,
                    com.salesforce.soap.partner.CallOptions callOptions490,
                    com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader491,
                    com.salesforce.soap.partner.MruHeader mruHeader492,
                    com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader493,
                    com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader494,
                    com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader495,
                    com.salesforce.soap.partner.DebuggingHeader debuggingHeader496,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader497,
                    com.salesforce.soap.partner.EmailHeader emailHeader498,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[29].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:upsertRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    upsert488,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "upsert")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader489!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader489 = toOM(sessionHeader489, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementsessionHeader489,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions490!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions490 = toOM(callOptions490, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementcallOptions490,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (assignmentRuleHeader491!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementassignmentRuleHeader491 = toOM(assignmentRuleHeader491, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementassignmentRuleHeader491,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (mruHeader492!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementmruHeader492 = toOM(mruHeader492, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementmruHeader492,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allowFieldTruncationHeader493!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader493 = toOM(allowFieldTruncationHeader493, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementallowFieldTruncationHeader493,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (disableFeedTrackingHeader494!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader494 = toOM(disableFeedTrackingHeader494, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementdisableFeedTrackingHeader494,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allOrNoneHeader495!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallOrNoneHeader495 = toOM(allOrNoneHeader495, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementallOrNoneHeader495,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (debuggingHeader496!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader496 = toOM(debuggingHeader496, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementdebuggingHeader496,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader497!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader497 = toOM(packageVersionHeader497, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementpackageVersionHeader497,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (emailHeader498!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementemailHeader498 = toOM(emailHeader498, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert")));
                                                    addHeader(omElementemailHeader498,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.UpsertResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultupsert(
                                        (com.salesforce.soap.partner.UpsertResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorupsert(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.InvalidSObjectFault){
														callback.receiveErrorupsert((com.salesforce.soap.partner.InvalidSObjectFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidIdFault){
														callback.receiveErrorupsert((com.salesforce.soap.partner.InvalidIdFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.InvalidFieldFault){
														callback.receiveErrorupsert((com.salesforce.soap.partner.InvalidFieldFault)ex);
											            return;
										            }
										            
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorupsert((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorupsert(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupsert(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupsert(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupsert(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupsert(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupsert(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupsert(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorupsert(f);
                                            }
									    } else {
										    callback.receiveErrorupsert(f);
									    }
									} else {
									    callback.receiveErrorupsert(f);
									}
								} else {
								    callback.receiveErrorupsert(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorupsert(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[29].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[29].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * convert a set of leads
                     * @see com.salesforce.soap.partner.SforceService#convertLead
                     * @param convertLead500
                    
                     * @param sessionHeader501
                    
                     * @param callOptions502
                    
                     * @param allowFieldTruncationHeader503
                    
                     * @param disableFeedTrackingHeader504
                    
                     * @param debuggingHeader505
                    
                     * @param packageVersionHeader506
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.ConvertLeadResponse convertLead(

                            com.salesforce.soap.partner.ConvertLead convertLead500,com.salesforce.soap.partner.SessionHeader sessionHeader501,com.salesforce.soap.partner.CallOptions callOptions502,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader503,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader504,com.salesforce.soap.partner.DebuggingHeader debuggingHeader505,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader506)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[30].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:convertLeadRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    convertLead500,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "convertLead")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader501!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader501 = toOM(sessionHeader501, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementsessionHeader501,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions502!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions502 = toOM(callOptions502, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementcallOptions502,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allowFieldTruncationHeader503!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader503 = toOM(allowFieldTruncationHeader503, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementallowFieldTruncationHeader503,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (disableFeedTrackingHeader504!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader504 = toOM(disableFeedTrackingHeader504, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementdisableFeedTrackingHeader504,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (debuggingHeader505!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader505 = toOM(debuggingHeader505, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementdebuggingHeader505,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader506!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader506 = toOM(packageVersionHeader506, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementpackageVersionHeader506,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.ConvertLeadResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.ConvertLeadResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * convert a set of leads
                * @see com.salesforce.soap.partner.SforceService#startconvertLead
                    * @param convertLead500
                
                    * @param sessionHeader501
                
                    * @param callOptions502
                
                    * @param allowFieldTruncationHeader503
                
                    * @param disableFeedTrackingHeader504
                
                    * @param debuggingHeader505
                
                    * @param packageVersionHeader506
                
                */
                public  void startconvertLead(

                 com.salesforce.soap.partner.ConvertLead convertLead500,com.salesforce.soap.partner.SessionHeader sessionHeader501,
                    com.salesforce.soap.partner.CallOptions callOptions502,
                    com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader503,
                    com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader504,
                    com.salesforce.soap.partner.DebuggingHeader debuggingHeader505,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader506,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[30].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:convertLeadRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    convertLead500,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "convertLead")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader501!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader501 = toOM(sessionHeader501, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementsessionHeader501,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions502!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions502 = toOM(callOptions502, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementcallOptions502,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allowFieldTruncationHeader503!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader503 = toOM(allowFieldTruncationHeader503, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementallowFieldTruncationHeader503,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (disableFeedTrackingHeader504!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader504 = toOM(disableFeedTrackingHeader504, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementdisableFeedTrackingHeader504,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (debuggingHeader505!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader505 = toOM(debuggingHeader505, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementdebuggingHeader505,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader506!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader506 = toOM(packageVersionHeader506, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead")));
                                                    addHeader(omElementpackageVersionHeader506,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.ConvertLeadResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultconvertLead(
                                        (com.salesforce.soap.partner.ConvertLeadResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrorconvertLead(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrorconvertLead((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrorconvertLead(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorconvertLead(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorconvertLead(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorconvertLead(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorconvertLead(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorconvertLead(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorconvertLead(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrorconvertLead(f);
                                            }
									    } else {
										    callback.receiveErrorconvertLead(f);
									    }
									} else {
									    callback.receiveErrorconvertLead(f);
									}
								} else {
								    callback.receiveErrorconvertLead(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrorconvertLead(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[30].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[30].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                
                    /**
                     * Auto generated method signature
                     * Delete a set of sObjects
                     * @see com.salesforce.soap.partner.SforceService#delete
                     * @param delete508
                    
                     * @param sessionHeader509
                    
                     * @param callOptions510
                    
                     * @param packageVersionHeader511
                    
                     * @param userTerritoryDeleteHeader512
                    
                     * @param emailHeader513
                    
                     * @param allowFieldTruncationHeader514
                    
                     * @param disableFeedTrackingHeader515
                    
                     * @param allOrNoneHeader516
                    
                     * @param debuggingHeader517
                    
                     * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
                     */

                    

                            public  com.salesforce.soap.partner.DeleteResponse delete(

                            com.salesforce.soap.partner.Delete delete508,com.salesforce.soap.partner.SessionHeader sessionHeader509,com.salesforce.soap.partner.CallOptions callOptions510,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader511,com.salesforce.soap.partner.UserTerritoryDeleteHeader userTerritoryDeleteHeader512,com.salesforce.soap.partner.EmailHeader emailHeader513,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader514,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader515,com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader516,com.salesforce.soap.partner.DebuggingHeader debuggingHeader517)
                        

                    throws java.rmi.RemoteException
                    
                    
                        ,com.salesforce.soap.partner.UnexpectedErrorFault{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[31].getName());
              _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:deleteRequest");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              

              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();

              

              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;
                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    delete508,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "delete")));
                                                
                                               env.build();
                                    
                                        // add the children only if the parameter is not null
                                        if (sessionHeader509!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementsessionHeader509 = toOM(sessionHeader509, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementsessionHeader509,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (callOptions510!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementcallOptions510 = toOM(callOptions510, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementcallOptions510,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (packageVersionHeader511!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader511 = toOM(packageVersionHeader511, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementpackageVersionHeader511,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (userTerritoryDeleteHeader512!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementuserTerritoryDeleteHeader512 = toOM(userTerritoryDeleteHeader512, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementuserTerritoryDeleteHeader512,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (emailHeader513!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementemailHeader513 = toOM(emailHeader513, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementemailHeader513,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allowFieldTruncationHeader514!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader514 = toOM(allowFieldTruncationHeader514, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementallowFieldTruncationHeader514,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (disableFeedTrackingHeader515!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader515 = toOM(disableFeedTrackingHeader515, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementdisableFeedTrackingHeader515,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (allOrNoneHeader516!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementallOrNoneHeader516 = toOM(allOrNoneHeader516, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementallOrNoneHeader516,env);
                                                
                                        }
                                    
                                        // add the children only if the parameter is not null
                                        if (debuggingHeader517!=null){
                                            
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader517 = toOM(debuggingHeader517, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementdebuggingHeader517,env);
                                                
                                        }
                                    
        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);

         
               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
                
                
                                java.lang.Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             com.salesforce.soap.partner.DeleteResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));

                               
                                        return (com.salesforce.soap.partner.DeleteResponse)object;
                                   
         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(faultElt.getQName())){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex=
                                (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});
                        
                        if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
                          throw (com.salesforce.soap.partner.UnexpectedErrorFault)ex;
                        }
                        

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
            
                /**
                * Auto generated method signature for Asynchronous Invocations
                * Delete a set of sObjects
                * @see com.salesforce.soap.partner.SforceService#startdelete
                    * @param delete508
                
                    * @param sessionHeader509
                
                    * @param callOptions510
                
                    * @param packageVersionHeader511
                
                    * @param userTerritoryDeleteHeader512
                
                    * @param emailHeader513
                
                    * @param allowFieldTruncationHeader514
                
                    * @param disableFeedTrackingHeader515
                
                    * @param allOrNoneHeader516
                
                    * @param debuggingHeader517
                
                */
                public  void startdelete(

                 com.salesforce.soap.partner.Delete delete508,com.salesforce.soap.partner.SessionHeader sessionHeader509,
                    com.salesforce.soap.partner.CallOptions callOptions510,
                    com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader511,
                    com.salesforce.soap.partner.UserTerritoryDeleteHeader userTerritoryDeleteHeader512,
                    com.salesforce.soap.partner.EmailHeader emailHeader513,
                    com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader514,
                    com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader515,
                    com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader516,
                    com.salesforce.soap.partner.DebuggingHeader debuggingHeader517,
                    

                  final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

                throws java.rmi.RemoteException{

              org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[31].getName());
             _operationClient.getOptions().setAction("urn:partner.soap.sforce.com:Soap:deleteRequest");
             _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

              
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              


              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env=null;
              final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

                    
                                    //Style is Doc.
                                    
                                                    
                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    delete508,
                                                    optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                                                    "delete")));
                                                
                                         // add the soap_headers only if they are not null
                                        if (sessionHeader509!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementsessionHeader509 = toOM(sessionHeader509, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementsessionHeader509,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (callOptions510!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementcallOptions510 = toOM(callOptions510, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementcallOptions510,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (packageVersionHeader511!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementpackageVersionHeader511 = toOM(packageVersionHeader511, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementpackageVersionHeader511,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (userTerritoryDeleteHeader512!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementuserTerritoryDeleteHeader512 = toOM(userTerritoryDeleteHeader512, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementuserTerritoryDeleteHeader512,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (emailHeader513!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementemailHeader513 = toOM(emailHeader513, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementemailHeader513,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allowFieldTruncationHeader514!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallowFieldTruncationHeader514 = toOM(allowFieldTruncationHeader514, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementallowFieldTruncationHeader514,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (disableFeedTrackingHeader515!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdisableFeedTrackingHeader515 = toOM(disableFeedTrackingHeader515, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementdisableFeedTrackingHeader515,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (allOrNoneHeader516!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementallOrNoneHeader516 = toOM(allOrNoneHeader516, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementallOrNoneHeader516,env);
                                                
                                        }
                                    
                                         // add the soap_headers only if they are not null
                                        if (debuggingHeader517!=null){
                                           
                                                    org.apache.axiom.om.OMElement omElementdebuggingHeader517 = toOM(debuggingHeader517, optimizeContent(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete")));
                                                    addHeader(omElementdebuggingHeader517,env);
                                                
                                        }
                                    
        // adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);


                    
                        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
                            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                            try {
                                org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();
                                
                                        java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
                                                                         com.salesforce.soap.partner.DeleteResponse.class,
                                                                         getEnvelopeNamespaces(resultEnv));
                                        callback.receiveResultdelete(
                                        (com.salesforce.soap.partner.DeleteResponse)object);
                                        
                            } catch (org.apache.axis2.AxisFault e) {
                                callback.receiveErrordelete(e);
                            }
                            }

                            public void onError(java.lang.Exception error) {
								if (error instanceof org.apache.axis2.AxisFault) {
									org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
									org.apache.axiom.om.OMElement faultElt = f.getDetail();
									if (faultElt!=null){
										if (faultExceptionNameMap.containsKey(faultElt.getQName())){
											//make the fault by reflection
											try{
													java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(faultElt.getQName());
													java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
													java.lang.Exception ex=
														(java.lang.Exception) exceptionClass.newInstance();
													//message class
													java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(faultElt.getQName());
														java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
													java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
													java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
															new java.lang.Class[]{messageClass});
													m.invoke(ex,new java.lang.Object[]{messageObject});
													
													if (ex instanceof com.salesforce.soap.partner.UnexpectedErrorFault){
														callback.receiveErrordelete((com.salesforce.soap.partner.UnexpectedErrorFault)ex);
											            return;
										            }
										            
					
										            callback.receiveErrordelete(new java.rmi.RemoteException(ex.getMessage(), ex));
                                            } catch(java.lang.ClassCastException e){
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordelete(f);
                                            } catch (java.lang.ClassNotFoundException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordelete(f);
                                            } catch (java.lang.NoSuchMethodException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordelete(f);
                                            } catch (java.lang.reflect.InvocationTargetException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordelete(f);
                                            } catch (java.lang.IllegalAccessException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordelete(f);
                                            } catch (java.lang.InstantiationException e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordelete(f);
                                            } catch (org.apache.axis2.AxisFault e) {
                                                // we cannot intantiate the class - throw the original Axis fault
                                                callback.receiveErrordelete(f);
                                            }
									    } else {
										    callback.receiveErrordelete(f);
									    }
									} else {
									    callback.receiveErrordelete(f);
									}
								} else {
								    callback.receiveErrordelete(error);
								}
                            }

                            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
                                onError(fault);
                            }

                            public void onComplete() {
                                try {
                                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                                } catch (org.apache.axis2.AxisFault axisFault) {
                                    callback.receiveErrordelete(axisFault);
                                }
                            }
                });
                        

          org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
        if ( _operations[31].getMessageReceiver()==null &&  _operationClient.getOptions().isUseSeparateListener()) {
           _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
          _operations[31].setMessageReceiver(
                    _callbackReceiver);
        }

           //execute the operation client
           _operationClient.execute(false);

                    }
                


       /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
       private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
            org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
       return returnMap;
    }

    
    
    private javax.xml.namespace.QName[] opNameArray = null;
    private boolean optimizeContent(javax.xml.namespace.QName opName) {
        

        if (opNameArray == null) {
            return false;
        }
        for (int i = 0; i < opNameArray.length; i++) {
            if (opName.equals(opNameArray[i])) {
                return true;   
            }
        }
        return false;
    }
     //https://login.salesforce.com/services/Soap/u/20.0
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Merge param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Merge.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.MergeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.MergeResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.fault.InvalidSObjectFaultE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.fault.InvalidSObjectFaultE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.fault.InvalidIdFaultE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.fault.InvalidIdFaultE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.fault.InvalidFieldFaultE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.fault.InvalidFieldFaultE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.fault.UnexpectedErrorFaultE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.SessionHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.SessionHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.CallOptions param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.CallOptions.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.AssignmentRuleHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.AssignmentRuleHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.MruHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.MruHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.AllowFieldTruncationHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.AllowFieldTruncationHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DisableFeedTrackingHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DisableFeedTrackingHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DebuggingHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DebuggingHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.PackageVersionHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.PackageVersionHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.EmailHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.EmailHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DebuggingInfo param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DebuggingInfo.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.GetUserInfo param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.GetUserInfo.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.GetUserInfoResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.GetUserInfoResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeSoftphoneLayout param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeSoftphoneLayout.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Update param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Update.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.UpdateResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.UpdateResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.AllOrNoneHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.AllOrNoneHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.SetPassword param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.SetPassword.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.SetPasswordResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.SetPasswordResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.fault.InvalidNewPasswordFaultE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.fault.InvalidNewPasswordFaultE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Logout param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Logout.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.LogoutResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.LogoutResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Retrieve param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Retrieve.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.RetrieveResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.RetrieveResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.fault.MalformedQueryFaultE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.fault.MalformedQueryFaultE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.QueryOptions param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.QueryOptions.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.QueryAll param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.QueryAll.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.QueryAllResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.QueryAllResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.GetUpdated param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.GetUpdated.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.GetUpdatedResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.GetUpdatedResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Undelete param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Undelete.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.UndeleteResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.UndeleteResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Create param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Create.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.CreateResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.CreateResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.SendEmail param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.SendEmail.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.SendEmailResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.SendEmailResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Search param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Search.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.SearchResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.SearchResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.fault.MalformedSearchFaultE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.fault.MalformedSearchFaultE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Query param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Query.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.QueryResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.QueryResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.GetDeleted param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.GetDeleted.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.GetDeletedResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.GetDeletedResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Process param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Process.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.ProcessResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.ProcessResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeDataCategoryGroupStructures param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeDataCategoryGroupStructures.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.ResetPassword param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.ResetPassword.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.ResetPasswordResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.ResetPasswordResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeGlobal param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeGlobal.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeGlobalResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeGlobalResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeLayoutE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeLayoutE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeLayoutResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeLayoutResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeTabs param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeTabs.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeTabsResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeTabsResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeDataCategoryGroups param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeDataCategoryGroups.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.GetServerTimestamp param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.GetServerTimestamp.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.GetServerTimestampResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.GetServerTimestampResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.InvalidateSessions param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.InvalidateSessions.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.InvalidateSessionsResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.InvalidateSessionsResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeSObject param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeSObject.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeSObjectResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeSObjectResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.LocaleOptions param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.LocaleOptions.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Login param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Login.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.LoginResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.LoginResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.fault.LoginFaultE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.fault.LoginFaultE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.LoginScopeHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.LoginScopeHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.QueryMore param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.QueryMore.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.QueryMoreResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.QueryMoreResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeSObjects param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeSObjects.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DescribeSObjectsResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DescribeSObjectsResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.EmptyRecycleBin param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.EmptyRecycleBin.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.EmptyRecycleBinResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.EmptyRecycleBinResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Upsert param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Upsert.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.UpsertResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.UpsertResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.ConvertLead param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.ConvertLead.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.ConvertLeadResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.ConvertLeadResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.Delete param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.Delete.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.DeleteResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.DeleteResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.salesforce.soap.partner.UserTerritoryDeleteHeader param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.salesforce.soap.partner.UserTerritoryDeleteHeader.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Merge param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Merge.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.GetUserInfo param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.GetUserInfo.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.DescribeSoftphoneLayout param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.DescribeSoftphoneLayout.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Update param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Update.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.SetPassword param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.SetPassword.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Logout param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Logout.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Retrieve param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Retrieve.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.QueryAll param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.QueryAll.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.GetUpdated param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.GetUpdated.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Undelete param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Undelete.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Create param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Create.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.SendEmail param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.SendEmail.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Search param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Search.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Query param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Query.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.GetDeleted param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.GetDeleted.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Process param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Process.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.DescribeDataCategoryGroupStructures param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.DescribeDataCategoryGroupStructures.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.ResetPassword param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.ResetPassword.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.DescribeGlobal param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.DescribeGlobal.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.DescribeLayoutE param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.DescribeLayoutE.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.DescribeTabs param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.DescribeTabs.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.DescribeDataCategoryGroups param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.DescribeDataCategoryGroups.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.GetServerTimestamp param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.GetServerTimestamp.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.InvalidateSessions param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.InvalidateSessions.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.DescribeSObject param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.DescribeSObject.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Login param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Login.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.QueryMore param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.QueryMore.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.DescribeSObjects param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.DescribeSObjects.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.EmptyRecycleBin param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.EmptyRecycleBin.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Upsert param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Upsert.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.ConvertLead param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.ConvertLead.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.salesforce.soap.partner.Delete param, boolean optimizeContent)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(com.salesforce.soap.partner.Delete.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (com.salesforce.soap.partner.Merge.class.equals(type)){
                
                           return com.salesforce.soap.partner.Merge.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.MergeResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.MergeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidFieldFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidFieldFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AssignmentRuleHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AssignmentRuleHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.MruHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.MruHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllowFieldTruncationHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllowFieldTruncationHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DisableFeedTrackingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DisableFeedTrackingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.EmailHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.EmailHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingInfo.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.GetUserInfo.class.equals(type)){
                
                           return com.salesforce.soap.partner.GetUserInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.GetUserInfoResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.GetUserInfoResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeSoftphoneLayout.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeSoftphoneLayout.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Update.class.equals(type)){
                
                           return com.salesforce.soap.partner.Update.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.UpdateResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.UpdateResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidFieldFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidFieldFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AssignmentRuleHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AssignmentRuleHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.MruHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.MruHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllowFieldTruncationHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllowFieldTruncationHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DisableFeedTrackingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DisableFeedTrackingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllOrNoneHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllOrNoneHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.EmailHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.EmailHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingInfo.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SetPassword.class.equals(type)){
                
                           return com.salesforce.soap.partner.SetPassword.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SetPasswordResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.SetPasswordResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidNewPasswordFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidNewPasswordFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Logout.class.equals(type)){
                
                           return com.salesforce.soap.partner.Logout.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.LogoutResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.LogoutResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Retrieve.class.equals(type)){
                
                           return com.salesforce.soap.partner.Retrieve.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.RetrieveResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.RetrieveResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.MalformedQueryFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.MalformedQueryFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidFieldFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidFieldFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.QueryOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.QueryOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.MruHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.MruHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.QueryAll.class.equals(type)){
                
                           return com.salesforce.soap.partner.QueryAll.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.QueryAllResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.QueryAllResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.MalformedQueryFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.MalformedQueryFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidFieldFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidFieldFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.QueryOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.QueryOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.GetUpdated.class.equals(type)){
                
                           return com.salesforce.soap.partner.GetUpdated.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.GetUpdatedResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.GetUpdatedResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Undelete.class.equals(type)){
                
                           return com.salesforce.soap.partner.Undelete.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.UndeleteResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.UndeleteResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllowFieldTruncationHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllowFieldTruncationHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DisableFeedTrackingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DisableFeedTrackingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllOrNoneHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllOrNoneHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingInfo.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Create.class.equals(type)){
                
                           return com.salesforce.soap.partner.Create.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CreateResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.CreateResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidFieldFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidFieldFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AssignmentRuleHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AssignmentRuleHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.MruHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.MruHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllowFieldTruncationHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllowFieldTruncationHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DisableFeedTrackingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DisableFeedTrackingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllOrNoneHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllOrNoneHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.EmailHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.EmailHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingInfo.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SendEmail.class.equals(type)){
                
                           return com.salesforce.soap.partner.SendEmail.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SendEmailResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.SendEmailResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Search.class.equals(type)){
                
                           return com.salesforce.soap.partner.Search.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SearchResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.SearchResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.MalformedSearchFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.MalformedSearchFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidFieldFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidFieldFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Query.class.equals(type)){
                
                           return com.salesforce.soap.partner.Query.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.QueryResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.QueryResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.MalformedQueryFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.MalformedQueryFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidFieldFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidFieldFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.QueryOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.QueryOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.MruHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.MruHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.GetDeleted.class.equals(type)){
                
                           return com.salesforce.soap.partner.GetDeleted.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.GetDeletedResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.GetDeletedResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Process.class.equals(type)){
                
                           return com.salesforce.soap.partner.Process.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.ProcessResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.ProcessResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllowFieldTruncationHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllowFieldTruncationHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DisableFeedTrackingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DisableFeedTrackingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingInfo.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeDataCategoryGroupStructures.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeDataCategoryGroupStructures.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.ResetPassword.class.equals(type)){
                
                           return com.salesforce.soap.partner.ResetPassword.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.ResetPasswordResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.ResetPasswordResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.EmailHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.EmailHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeGlobal.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeGlobal.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeGlobalResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeGlobalResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeLayoutE.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeLayoutE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeLayoutResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeLayoutResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeTabs.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeTabs.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeTabsResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeTabsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeDataCategoryGroups.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeDataCategoryGroups.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.GetServerTimestamp.class.equals(type)){
                
                           return com.salesforce.soap.partner.GetServerTimestamp.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.GetServerTimestampResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.GetServerTimestampResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.InvalidateSessions.class.equals(type)){
                
                           return com.salesforce.soap.partner.InvalidateSessions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.InvalidateSessionsResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.InvalidateSessionsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeSObject.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeSObject.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeSObjectResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeSObjectResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.LocaleOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.LocaleOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Login.class.equals(type)){
                
                           return com.salesforce.soap.partner.Login.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.LoginResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.LoginResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.LoginFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.LoginFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.LoginScopeHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.LoginScopeHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.QueryMore.class.equals(type)){
                
                           return com.salesforce.soap.partner.QueryMore.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.QueryMoreResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.QueryMoreResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidFieldFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidFieldFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidQueryLocatorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.QueryOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.QueryOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeSObjects.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeSObjects.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DescribeSObjectsResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.DescribeSObjectsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.LocaleOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.LocaleOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.EmptyRecycleBin.class.equals(type)){
                
                           return com.salesforce.soap.partner.EmptyRecycleBin.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.EmptyRecycleBinResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.EmptyRecycleBinResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Upsert.class.equals(type)){
                
                           return com.salesforce.soap.partner.Upsert.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.UpsertResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.UpsertResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidSObjectFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidSObjectFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidIdFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidIdFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.InvalidFieldFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.InvalidFieldFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AssignmentRuleHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AssignmentRuleHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.MruHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.MruHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllowFieldTruncationHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllowFieldTruncationHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DisableFeedTrackingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DisableFeedTrackingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllOrNoneHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllOrNoneHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.EmailHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.EmailHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingInfo.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.ConvertLead.class.equals(type)){
                
                           return com.salesforce.soap.partner.ConvertLead.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.ConvertLeadResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.ConvertLeadResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllowFieldTruncationHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllowFieldTruncationHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DisableFeedTrackingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DisableFeedTrackingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingInfo.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.Delete.class.equals(type)){
                
                           return com.salesforce.soap.partner.Delete.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DeleteResponse.class.equals(type)){
                
                           return com.salesforce.soap.partner.DeleteResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.class.equals(type)){
                
                           return com.salesforce.soap.partner.fault.UnexpectedErrorFaultE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.SessionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.SessionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.CallOptions.class.equals(type)){
                
                           return com.salesforce.soap.partner.CallOptions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.PackageVersionHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.PackageVersionHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.UserTerritoryDeleteHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.UserTerritoryDeleteHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.EmailHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.EmailHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllowFieldTruncationHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllowFieldTruncationHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DisableFeedTrackingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DisableFeedTrackingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.AllOrNoneHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.AllOrNoneHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingHeader.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingHeader.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.salesforce.soap.partner.DebuggingInfo.class.equals(type)){
                
                           return com.salesforce.soap.partner.DebuggingInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    
   }
   