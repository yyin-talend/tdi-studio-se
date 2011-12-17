package org.talend.salesforce.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.salesforce.soap.partner.DescribeGlobalResult;
import com.salesforce.soap.partner.DescribeGlobalSObjectResult;
import com.salesforce.soap.partner.DescribeSObjectResult;
import com.salesforce.soap.partner.Field;
import com.salesforce.soap.partner.FieldType;
import com.salesforce.soap.partner.LoginResult;
import com.salesforce.soap.partner.SessionHeader;
import com.salesforce.soap.partner.SforceServiceLocator;
import com.salesforce.soap.partner.SoapBindingStub;
import com.salesforce.soap.partner.fault.ExceptionCode;
import com.salesforce.soap.partner.fault.InvalidSObjectFault;
import com.salesforce.soap.partner.fault.LoginFault;
import com.salesforce.soap.partner.fault.UnexpectedErrorFault;

public class SObjectsToSchema {

    private final static String INSERT = "insert";

    private final static String UPDATE = "update";

    private final static String UPSERT = "upsert";

    private final static String KEY = "Id";

    private SoapBindingStub binding;

    boolean login(String userName, String password) throws ServiceException {

        /**
         * Next, the sample client application initializes the binding stub. This is our main interface to the API
         * through which all calls are made. The getSoap method takes an optional parameter, (a java.net.URL) which is
         * the endpoint. For the login call, the parameter always starts with http(s)://login.salesforce.com. After
         * logging in, the sample client application changes the endpoint to the one specified in the returned
         * loginResult object.
         */

        binding = (SoapBindingStub) new SforceServiceLocator().getSoap();

        // Time out after a minute

        binding.setTimeout(60000);
        // Test operation

        LoginResult loginResult;
        try {
            System.out.println("LOGGING IN NOW....");
            loginResult = binding.login(userName, password);
        } catch (LoginFault ex) {
            // The LoginFault derives from AxisFault

            ExceptionCode exCode = ex.getExceptionCode();
            if (exCode == ExceptionCode.FUNCTIONALITY_NOT_ENABLED || exCode == ExceptionCode.INVALID_CLIENT
                    || exCode == ExceptionCode.INVALID_LOGIN || exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_DOMAIN
                    || exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_TIME || exCode == ExceptionCode.ORG_LOCKED
                    || exCode == ExceptionCode.PASSWORD_LOCKOUT || exCode == ExceptionCode.SERVER_UNAVAILABLE
                    || exCode == ExceptionCode.TRIAL_EXPIRED || exCode == ExceptionCode.UNSUPPORTED_CLIENT) {
                System.out.println("Please be sure that you have a valid username " + "and password.");
            } else {
                // Write the fault code to the console

                System.out.println(ex.getExceptionCode());
                // Write the fault message to the console

                System.out.println("An unexpected error has occurred." + ex.getMessage());
            }
            return false;
        } catch (Exception ex) {
            System.out.println("An unexpected error has occurred: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        // Check if the password has expired

        if (loginResult.isPasswordExpired()) {
            System.out.println("An error has occurred. Your password has expired.");
            return false;
        }
        /**
         * Once the client application has logged in successfully, it will use the results of the login call to reset
         * the endpoint of the service to the virtual server instance that is servicing your organization. To do this,
         * the client application sets the ENDPOINT_ADDRESS_PROPERTY of the binding object using the URL returned from
         * the LoginResult.
         */

        binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY, loginResult.getServerUrl());
        /**
         * The sample client application now has an instance of the SoapBindingStub that is pointing to the correct
         * endpoint. Next, the sample client application sets a persistent SOAP header (to be included on all subsequent
         * calls that are made with the SoapBindingStub) that contains the valid sessionId for our login credentials. To
         * do this, the sample client application creates a new SessionHeader object and set its sessionId property to
         * the sessionId property from the LoginResult object.
         */

        // Create a new session header object and add the session id

        // from the login return object

        SessionHeader sh = new SessionHeader();
        sh.setSessionId(loginResult.getSessionId());
        /**
         * Next, the sample client application calls the setHeader method of the SoapBindingStub to add the header to
         * all subsequent method calls. This header will persist until the SoapBindingStub is destroyed until the header
         * is explicitly removed. The "SessionHeader" parameter is the name of the header to be added.
         */

        // set the session header for subsequent call authentication

        binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(), "SessionHeader", sh);
        // return true to indicate that we are logged in, pointed

        // at the right url and have our security token in place.

        return true;
    }

    private void generateModuleName(String fileName) throws IOException {
        File f = new File(fileName);
        StringBuffer content = new StringBuffer();

        DescribeGlobalResult describeGlobalResult = null;
        describeGlobalResult = binding.describeGlobal();
        DescribeGlobalSObjectResult[] sobjectResults = describeGlobalResult.getSobjects();
        for (int i = 0; i < sobjectResults.length; i++) {
            if (!sobjectResults[i].isCustom()) {
                String moduleName = sobjectResults[i].getName();
                // System.out.println(sobjectResults[i].getName());
                content.append("<ITEM NAME=\"");
                content.append(moduleName);
                content.append("\" VALUE=\"");
                content.append(moduleName);
                content.append("\" />");
                content.append("\r\n");
            }
        }

        BufferedWriter output = new BufferedWriter(new FileWriter(f));
        output.write(content.toString());
        output.close();

    }

    void generateSchema(String inputFileName, String outputFileName) throws IOException {
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        StringBuffer inputContent = new StringBuffer();
        // StringBuffer deleteContent = new StringBuffer();
        StringBuffer insertContent = new StringBuffer();
        StringBuffer updateContent = new StringBuffer();
        StringBuffer upsertContent = new StringBuffer();

        DescribeGlobalResult describeGlobalResult = null;
        describeGlobalResult = binding.describeGlobal();
        DescribeGlobalSObjectResult[] sobjectResults = describeGlobalResult.getSobjects();
        for (int i = 0; i < sobjectResults.length; i++) {
            String moduleName = sobjectResults[i].getName();

            DescribeSObjectResult descSObjectRslt;
            descSObjectRslt = binding.describeSObject(moduleName);
            if (descSObjectRslt != null) {
                // Report object level information
                if (!descSObjectRslt.isCustom()) {
                    // if (descSObjectRslt.isQueryable()) {
                    inputContent.append(generateInputTable(moduleName, descSObjectRslt));//mark
                    // }
                    // if (descSObjectRslt.isCreateable()) {
                    insertContent.append(generateOutputTable(moduleName, descSObjectRslt, INSERT));
                    // }
                    // if (descSObjectRslt.isUpdateable()) {
                    updateContent.append(generateOutputTable(moduleName, descSObjectRslt, UPDATE));
                    // }
                    // if(descSObjectRslt.isUpdateable()&&descSObjectRslt.isCreateable()){
                    upsertContent.append(generateOutputTable(moduleName, descSObjectRslt, UPSERT));
                    // }
                    // if (descSObjectRslt.isDeletable()) {
                    //
                    // }
                }
            }
        }

        BufferedWriter inOutput = new BufferedWriter(new FileWriter(inputFile));
        inOutput.write(inputContent.toString());
        inOutput.close();

        String sepLint = "<!-- ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->";
        BufferedWriter OuOutput = new BufferedWriter(new FileWriter(outputFile));
        OuOutput.write(insertContent.toString() + "\r\n" + sepLint + "\r\n" + updateContent.toString() + "\r\n" + sepLint
                + "\r\n" + upsertContent.toString());
        OuOutput.close();

    }

    private String generateInputTable(String moduleName, DescribeSObjectResult descSObjectRslt) throws InvalidSObjectFault,
            UnexpectedErrorFault, RemoteException {
        StringBuffer content = new StringBuffer();
        content.append("<TABLE IF=\"");
        content.append("MODULENAME=='" + moduleName + "' ");
        content.append("\">");
        content.append("\r\n");

        // DescribeSObjectResult descSObjectRslt;
        // descSObjectRslt = binding.describeSObject(moduleName);

        Field[] fields = descSObjectRslt.getFields();
        if (fields != null) {
            for (Field field : fields) {
                if (!field.isCustom()) {
                    String colName = field.getName();
                    FieldType type = field.getType();
                    String typeName = ConvertSFTypeToTOSType.getTOSValue(type.getValue());

                    if (typeName.equals("id_Date")) {
                        String pattern = ConvertSFTypeToTOSType.getPattern(type.getValue());
                        if (pattern == null) {
                            System.out.println(moduleName + ":" + colName + " is not have pattern!");
                        }
                        content.append(generateInputColumn(colName.equals(KEY), colName, typeName, pattern, field.getLength()));
                    } else {
                        content.append(generateInputColumn(colName.equals(KEY), colName, typeName, field.getLength()));
                    }

                }
            }
        }
        content.append("</TABLE>");
        content.append("\r\n");
        return content.toString();
    }

    public String generateOutputTable(String moduleName, DescribeSObjectResult descSObjectRslt, String operation)
            throws InvalidSObjectFault, UnexpectedErrorFault, RemoteException {
        StringBuffer content = new StringBuffer();
        content.append("<TABLE IF=\"");
        content.append("(MODULENAME=='" + moduleName + "') ");

        content.append("and (ACTION=='" + operation + "') ");

        content.append("and (PROPERTY=='BUILT_IN')\">");
        content.append("\r\n");

        // DescribeSObjectResult descSObjectRslt;
        // descSObjectRslt = binding.describeSObject(moduleName);

        Field[] fields = descSObjectRslt.getFields();
        if (fields != null) {
            for (Field field : fields) {
                if (!field.isCustom()) {
                    String colName = field.getName();
                    FieldType type = field.getType();
                    String typeName = ConvertSFTypeToTOSType.getTOSValue(type.getValue());

                    boolean readOnly = field.isNillable() == false && field.isAutoNumber() == false
                            && field.isDefaultedOnCreate() == false;
                    boolean nullable = field.isNillable();

                    if (operation.equals(INSERT) && !field.isCreateable()) {
                        continue;
                    }
                    if (operation.equals(UPDATE) && !field.isUpdateable()) {
                        if (type.getValue().equals("id")) {
                            content.append(generateOutputColumn(colName.equals(KEY), colName, typeName, field.getLength(), true,
                                    false));
                        }
                        continue;
                    }
                    if (operation.equals(UPSERT) && !field.isCreateable() && !field.isUpdateable()) {
                        if (type.getValue().equals("id")) {
                            content.append(generateOutputColumn(colName.equals(KEY), colName, typeName, field.getLength(), true,
                                    false));
                        }
                        continue;
                    }
                    if (typeName.equals("id_Date")) {
                        String pattern = ConvertSFTypeToTOSType.getPattern(type.getValue());
                        if (pattern == null) {
                            System.out.println(moduleName + ":" + colName + " is not have pattern!");
                        }
                        content.append(generateOutputColumn(colName.equals(KEY), colName, typeName, pattern, field.getLength(),
                                readOnly, nullable));
                    } else {
                        content.append(generateOutputColumn(colName.equals(KEY), colName, typeName, field.getLength(), readOnly,
                                nullable));
                    }
                }
            }
        }
        content.append("</TABLE>");
        content.append("\r\n");
        return content.toString();
    }

    private String generateInputColumn(boolean isKey, String name, String type, int length) {
        return generateColumn(isKey, name, type, null, length, false, true);
    }

    private String generateInputColumn(boolean isKey, String name, String type, String pattern, int length) {
        return generateColumn(isKey, name, type, pattern, length, false, true);
    }

    private String generateOutputColumn(boolean isKey, String name, String type, int length, boolean readOnly, boolean nullable) {
        return generateColumn(isKey, name, type, null, length, readOnly, nullable);
    }

    private String generateOutputColumn(boolean isKey, String name, String type, String pattern, int length, boolean readOnly,
            boolean nullable) {
        return generateColumn(isKey, name, type, pattern, length, readOnly, nullable);
    }

    private String generateColumn(boolean isKey, String name, String type, String pattern, int length, boolean readOnly,
            boolean nullable) {
        StringBuffer column = new StringBuffer();
        column.append("\t");
        column.append("<COLUMN KEY=\"" + isKey + "\" ");
        column.append("LENGTH=\"" + length + "\" ");
        column.append("NAME=\"" + name + "\" ");
        column.append("TYPE=\"" + type + "\" ");

        if (pattern != null) {
            column.append("PATTERN=\"&quot;" + pattern + "&quot;\" ");
        }
        if (readOnly) {
            column.append("READONLY=\"true\" ");
        }
        if (!nullable) {
            column.append("NULLABLE=\"false\" ");
        }
        column.append("/>");
        column.append("\r\n");
        return column.toString();
    }

    public static void main(String[] args) {
        SObjectsToSchema sforceCLient = new SObjectsToSchema();
        boolean result = false;
        try {
            result = sforceCLient.login("bchen.bj@gmail.com", "qscesz#1115");
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (result) {
            try {
                sforceCLient.generateModuleName("d://sforceModule.txt");
                sforceCLient.generateSchema("d://sforceInputSchema.txt", "d://sforceOutputSchema.txt");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //if it cant generate the schema in one time,(connect reset error), please pay attention about line:174,177,180,183 in generateSchema method
    }
}
