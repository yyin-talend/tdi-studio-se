package org.talend.salesforce.util;

public class Test_SFSchema {

    /**
     * DOC Administrator Comment method "main".
     * 
     * @param args
     */
    public static void main(String[] args) {
        SObjectsToSchema.update("d:/tSalesforceInput_java_10.xml", "d:/tSalesforceInput_java_16.xml",
                "https://www.salesforce.com/services/Soap/u/16.0", "musicatcher@gmail.com", "1qa2ws3ed");
        // SObjectsToSchema.update("d:/tSalesforceOutput_java_10.xml", "d:/tSalesforceOutput_java_16.xml",
        // "https://www.salesforce.com/services/Soap/u/16.0", "musicatcher@gmail.com", "1qa2ws3ed");
        // SObjectsToSchema.update("d:/tSalesforceGetUpdated_java_10.xml", "d:/tSalesforceGetUpdated_java_16.xml",
        // "https://www.salesforce.com/services/Soap/u/16.0", "musicatcher@gmail.com", "1qa2ws3ed");
        // SObjectsToSchema.update("d:/tSalesforceGetDeleted_java_10.xml", "d:/tSalesforceGetDeleted_java_16.xml",
        // "https://www.salesforce.com/services/Soap/u/16.0", "musicatcher@gmail.com", "1qa2ws3ed");
    }
}
