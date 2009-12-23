// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.salesforce;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.EngineConfigurationFactoryFinder;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.message.MessageElement;
import org.apache.axis.transport.http.CommonsHTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.w3c.dom.Element;

import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.QueryOptions;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.SessionHeader;
import com.sforce.soap.partner.SforceService;
import com.sforce.soap.partner.SforceServiceLocator;
import com.sforce.soap.partner.SoapBindingStub;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 * 
 * 
 */
public class SforceManagementImpl implements SforceManagement {

    private SoapBindingStub binding;

    public SoapBindingStub getBinding() {
        return binding;
    }

    private String endPoint;

    private LoginResult loginResult = null;

    private String username = "";

    private String password = "";

    /** number of records to buffer before submitting to the salesforce api */
    private boolean exceptionForErrors = false;

    private java.io.BufferedWriter logWriter = null;

    private int commitLevel = 0;

    private ArrayList<String> deleteItems;

    private ArrayList<SObject> insertItems;

    private ArrayList<SObject> upsertItems;

    private ArrayList<SObject> updateItems;

    // remember the upsert key for the logout method
    private String upsertKeyColumn;

    // TODO when timeout, reset it false, then login again.
    private boolean loggedIn = false;

    private SforceService sforceService;

    public SforceService getSforceService() {
        return sforceService;
    }

    public void setSforceService(SforceService sforceService) {
        this.sforceService = sforceService;
    }

    private SforceService createService() {
        if (this.needCompression) {
            EngineConfiguration defaultConfig = EngineConfigurationFactoryFinder.newFactory().getClientEngineConfig();
            SimpleProvider config = new SimpleProvider(defaultConfig);
            config.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, new CommonsHTTPSender());
            return new SforceCompressionServiceLocator(config);
        } else {
            return new SforceServiceLocator();
        }
    }

    private Boolean needCompression = false;

    public void setNeedCompression(Boolean needCompression) {
        this.needCompression = needCompression;
    }

    /**
     * login
     */
    public boolean login(String username, String password, String endPoint) throws Exception {
        if (username == null || username.trim().length() == 0)
            return false;
        if (password == null || password.trim().length() == 0)
            return false;
        if (endPoint == null || endPoint.trim().length() == 0)
            return false;

        // hold the values, when timeout loggin again.
        this.username = username;
        this.password = password;
        this.endPoint = endPoint;
        this.commitLevel = 1;

        this.deleteItems = new ArrayList<String>(commitLevel * 2);
        this.insertItems = new ArrayList<SObject>(commitLevel * 2);
        this.updateItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertKeyColumn = "";

        this.sforceService = createService();
        binding = (SoapBindingStub) sforceService.getSoap(new URL(endPoint));

        // 10 minutes
        binding.setTimeout(60000);

        loginResult = binding.login(username, password);

        binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY, loginResult.getServerUrl());

        SessionHeader sh = new SessionHeader();
        sh.setSessionId(loginResult.getSessionId());
        binding.setHeader(sforceService.getServiceName().getNamespaceURI(), "SessionHeader", sh);

        loggedIn = true;

        return true;
    }

    /**
     * login
     */
    public boolean login(String username, String password, String endPoint, int commitLevel, boolean exceptionForErrors,
            String errorLogFile) throws Exception {
        if (username == null || username.trim().length() == 0)
            return false;
        if (password == null || password.trim().length() == 0)
            return false;
        if (endPoint == null || endPoint.trim().length() == 0)
            return false;

        if (commitLevel < 0)
            commitLevel = 1;
        else if (commitLevel > 200)
            commitLevel = 200;

        // hold the values, when timeout loggin again.
        this.username = username;
        this.password = password;
        this.endPoint = endPoint;
        this.commitLevel = commitLevel;
        this.exceptionForErrors = exceptionForErrors;
        if (errorLogFile != null && errorLogFile.trim().length() > 0) {
            logWriter = new java.io.BufferedWriter(new java.io.FileWriter(errorLogFile));
        }

        this.deleteItems = new ArrayList<String>(commitLevel * 2);
        this.insertItems = new ArrayList<SObject>(commitLevel * 2);
        this.updateItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertKeyColumn = "";

        this.sforceService = createService();
        binding = (SoapBindingStub) sforceService.getSoap(new URL(endPoint));
        // 10 minutes
        binding.setTimeout(60000);

        loginResult = binding.login(username, password);

        binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY, loginResult.getServerUrl());

        SessionHeader sh = new SessionHeader();
        sh.setSessionId(loginResult.getSessionId());
        binding.setHeader(sforceService.getServiceName().getNamespaceURI(), "SessionHeader", sh);

        loggedIn = true;

        return true;
    }

    /**
     * login with binding
     */
    public boolean login(SoapBindingStub binding) throws Exception {
        this.commitLevel = 1;

        this.deleteItems = new ArrayList<String>(commitLevel * 2);
        this.insertItems = new ArrayList<SObject>(commitLevel * 2);
        this.updateItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertKeyColumn = "";

        this.binding = binding;

        loggedIn = true;

        return true;
    }

    /**
     * login with binding
     */
    public boolean login(SoapBindingStub binding, int commitLevel, boolean exceptionForErrors, String errorLogFile)
            throws Exception {

        if (commitLevel < 0)
            commitLevel = 1;
        else if (commitLevel > 200)
            commitLevel = 200;

        this.commitLevel = commitLevel;
        this.exceptionForErrors = exceptionForErrors;
        if (errorLogFile != null && errorLogFile.trim().length() > 0) {
            logWriter = new java.io.BufferedWriter(new java.io.FileWriter(errorLogFile));
        }

        this.deleteItems = new ArrayList<String>(commitLevel * 2);
        this.insertItems = new ArrayList<SObject>(commitLevel * 2);
        this.updateItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertKeyColumn = "";

        this.binding = binding;

        loggedIn = true;

        return true;
    }

    /**
     * login again
     */
    private boolean login() throws Exception {
        return login(username, password, endPoint);
    }

    /**
     * logout
     */
    public void logout() throws Exception {
        Object returnObject = null;
        // if there are still records to be commited:
        try {
            if (insertItems.size() > 0) {
                SObject[] accs = insertItems.toArray(new SObject[insertItems.size()]);
                SaveResult[] sr = binding.create(accs);
                insertItems.clear();
                accs = null;

                if (exceptionForErrors && sr != null && sr.length != 0) {
                    for (SaveResult result : sr) {
                        StringBuilder errors = new StringBuilder("");
                        if (result.isSuccess()) {
                            // TODO: send back the ID
                        } else {
                            for (Error error : result.getErrors()) {
                                errors.append(error.getMessage()).append("\n");
                                if (logWriter != null) {
                                    logWriter.append("\tStatus Code: ").append(error.getStatusCode().toString());
                                    logWriter.newLine();
                                    logWriter.newLine();
                                    logWriter.append("\tFields: ");
                                    boolean flag = false;
                                    for (String field : error.getFields()) {
                                        if (flag) {
                                            logWriter.append(", ");
                                        } else {
                                            flag = true;
                                        }
                                        logWriter.append(field);
                                    }
                                    logWriter.newLine();
                                    logWriter.newLine();

                                    logWriter.append("\tMessage: ").append(error.getMessage());

                                    logWriter.newLine();

                                    logWriter
                                            .append("\t--------------------------------------------------------------------------------");

                                    logWriter.newLine();
                                    logWriter.newLine();

                                }
                            }
                        }
                        if (exceptionForErrors && errors.toString().length() > 0) {
                            if (logWriter != null) {
                                logWriter.close();
                            }
                            throw new Exception(errors.toString());
                        }
                    }
                }

                returnObject = sr;
            }
            if (deleteItems.size() > 0) {
                String[] dels = deleteItems.toArray(new String[deleteItems.size()]);
                DeleteResult[] dr = binding.delete(dels);
                deleteItems.clear();
                dels = null;

                if (exceptionForErrors && dr != null && dr.length != 0) {
                    for (DeleteResult result : dr) {
                        StringBuilder errors = new StringBuilder("");
                        if (result.isSuccess()) {
                            // TODO: send back the ID
                        } else {
                            for (Error error : result.getErrors()) {
                                errors.append(error.getMessage()).append("\n");
                                if (logWriter != null) {
                                    logWriter.append("\tStatus Code: ").append(error.getStatusCode().toString());
                                    logWriter.newLine();
                                    logWriter.newLine();
                                    logWriter.append("\tFields: ");
                                    boolean flag = false;
                                    for (String field : error.getFields()) {
                                        if (flag) {
                                            logWriter.append(", ");
                                        } else {
                                            flag = true;
                                        }
                                        logWriter.append(field);
                                    }
                                    logWriter.newLine();
                                    logWriter.newLine();

                                    logWriter.append("\tMessage: ").append(error.getMessage());

                                    logWriter.newLine();

                                    logWriter
                                            .append("\t--------------------------------------------------------------------------------");

                                    logWriter.newLine();
                                    logWriter.newLine();

                                }
                            }
                        }
                        if (exceptionForErrors && errors.toString().length() > 0) {
                            if (logWriter != null) {
                                logWriter.close();
                            }
                            throw new Exception(errors.toString());
                        }
                    }
                }

                returnObject = dr;
            }
            if (updateItems.size() > 0) {
                SObject[] upds = updateItems.toArray(new SObject[updateItems.size()]);
                SaveResult[] saveResults = binding.update(upds);
                updateItems.clear();
                upds = null;

                if (exceptionForErrors && saveResults != null && saveResults.length != 0) {
                    for (SaveResult result : saveResults) {
                        StringBuilder errors = new StringBuilder("");
                        if (result.isSuccess()) {
                            // TODO: send back the ID
                        } else {
                            for (Error error : result.getErrors()) {
                                errors.append(error.getMessage()).append("\n");
                                if (logWriter != null) {
                                    logWriter.append("\tStatus Code: ").append(error.getStatusCode().toString());
                                    logWriter.newLine();
                                    logWriter.newLine();
                                    logWriter.append("\tFields: ");
                                    boolean flag = false;
                                    for (String field : error.getFields()) {
                                        if (flag) {
                                            logWriter.append(", ");
                                        } else {
                                            flag = true;
                                        }
                                        logWriter.append(field);
                                    }
                                    logWriter.newLine();
                                    logWriter.newLine();

                                    logWriter.append("\tMessage: ").append(error.getMessage());

                                    logWriter.newLine();

                                    logWriter
                                            .append("\t--------------------------------------------------------------------------------");

                                    logWriter.newLine();
                                    logWriter.newLine();

                                }
                            }
                        }
                        if (exceptionForErrors && errors.toString().length() > 0) {
                            if (logWriter != null) {
                                logWriter.close();
                            }
                            throw new Exception(errors.toString());
                        }
                    }
                }

                returnObject = saveResults;
            }
            if (upsertItems.size() > 0) {
                SObject[] upds = upsertItems.toArray(new SObject[upsertItems.size()]);
                UpsertResult[] upsertResults = binding.upsert(upsertKeyColumn, upds);
                upsertItems.clear();
                upds = null;

                if (exceptionForErrors && upsertResults != null && upsertResults.length != 0) {
                    for (UpsertResult result : upsertResults) {
                        StringBuilder errors = new StringBuilder("");
                        if (result.isSuccess()) {
                            // TODO: send back the ID
                        } else {
                            for (Error error : result.getErrors()) {
                                errors.append(error.getMessage()).append("\n");
                                if (logWriter != null) {
                                    logWriter.append("\tStatus Code: ").append(error.getStatusCode().toString());
                                    logWriter.newLine();
                                    logWriter.newLine();
                                    logWriter.append("\tFields: ");
                                    boolean flag = false;
                                    for (String field : error.getFields()) {
                                        if (flag) {
                                            logWriter.append(", ");
                                        } else {
                                            flag = true;
                                        }
                                        logWriter.append(field);
                                    }
                                    logWriter.newLine();
                                    logWriter.newLine();

                                    logWriter.append("\tMessage: ").append(error.getMessage());

                                    logWriter.newLine();

                                    logWriter
                                            .append("\t--------------------------------------------------------------------------------");

                                    logWriter.newLine();
                                    logWriter.newLine();

                                }
                            }
                        }
                        if (exceptionForErrors && errors.toString().length() > 0) {
                            if (logWriter != null) {
                                logWriter.close();
                            }
                            throw new Exception(errors.toString());
                        }
                    }
                }
                returnObject = upsertResults;
            }
        } catch (Exception e) {
            throw new Exception(e.toString());
        }

        if (logWriter != null) {
            logWriter.close();
        }
        loggedIn = false;
    }

    /**
     * create, one time one record.
     */
    public SaveResult[] insert(String tablename, MessageElement[] nameValues) throws Exception {
        if (tablename == null || tablename.trim().length() == 0)
            return null;

        if (nameValues == null || nameValues.length == 0)
            return null;

        SObject item = new SObject();
        item.setType(tablename);
        item.set_any(nameValues);
        // item.setId("00T9000000VLEqBDAX");
        insertItems.add(item);

        if (insertItems.size() >= commitLevel) {
            SObject[] accs = insertItems.toArray(new SObject[insertItems.size()]);
            SaveResult[] sr = binding.create(accs);
            insertItems.clear();
            accs = null;

            if (sr != null && sr.length != 0) {
                for (SaveResult result : sr) {
                    StringBuilder errors = new StringBuilder("");
                    if (result.isSuccess()) {
                        // TODO: send back the ID
                    } else {
                        for (Error error : result.getErrors()) {
                            errors.append(error.getMessage()).append("\n");
                            if (logWriter != null) {
                                logWriter.append("\tStatus Code: ").append(error.getStatusCode().toString());
                                logWriter.newLine();
                                logWriter.newLine();
                                logWriter.append("\tFields: ");
                                boolean flag = false;
                                for (String field : error.getFields()) {
                                    if (flag) {
                                        logWriter.append(", ");
                                    } else {
                                        flag = true;
                                    }
                                    logWriter.append(field);
                                }
                                logWriter.newLine();
                                logWriter.newLine();

                                logWriter.append("\tMessage: ").append(error.getMessage());

                                logWriter.newLine();

                                logWriter
                                        .append("\t--------------------------------------------------------------------------------");

                                logWriter.newLine();
                                logWriter.newLine();

                            }
                        }
                    }
                    if (exceptionForErrors && errors.toString().length() > 0) {
                        if (logWriter != null) {
                            logWriter.close();
                        }
                        throw new Exception(errors.toString());
                    }
                }
            }

            return sr;
        }
        return null;
    }

    public MessageElement newMessageElement(String name, Object value) throws Exception {

        MessageElement me = new MessageElement("", name); // , value);
        me.setObjectValue(value);
        Element e = me.getAsDOM();
        e.removeAttribute("xsi:type");
        e.removeAttribute("xmlns:ns1");
        e.removeAttribute("xmlns:xsd");
        e.removeAttribute("xmlns:xsi");

        me = new MessageElement(e);
        return me;
    }

    /**
     * delete, one time one record.
     */
    public DeleteResult[] delete(String id) throws Exception {
        if (id == null)
            return null;

        // String[] ids = new String[] { id };
        deleteItems.add(id);

        if (deleteItems.size() >= commitLevel) {
            String[] dels = deleteItems.toArray(new String[deleteItems.size()]);
            DeleteResult[] dr = binding.delete(dels);
            deleteItems.clear();
            dels = null;

            if (dr != null && dr.length != 0) {
                for (DeleteResult result : dr) {
                    StringBuilder errors = new StringBuilder("");
                    if (result.isSuccess()) {
                        // TODO: send back the ID
                    } else {
                        for (Error error : result.getErrors()) {
                            errors.append(error.getMessage()).append("\n");
                            if (logWriter != null) {
                                logWriter.append("\tStatus Code: ").append(error.getStatusCode().toString());
                                logWriter.newLine();
                                logWriter.newLine();
                                logWriter.append("\tFields: ");
                                boolean flag = false;
                                for (String field : error.getFields()) {
                                    if (flag) {
                                        logWriter.append(", ");
                                    } else {
                                        flag = true;
                                    }
                                    logWriter.append(field);
                                }
                                logWriter.newLine();
                                logWriter.newLine();

                                logWriter.append("\tMessage: ").append(error.getMessage());

                                logWriter.newLine();

                                logWriter
                                        .append("\t--------------------------------------------------------------------------------");

                                logWriter.newLine();
                                logWriter.newLine();

                            }
                        }
                    }
                    if (exceptionForErrors && errors.toString().length() > 0) {
                        if (logWriter != null) {
                            logWriter.close();
                        }
                        throw new Exception(errors.toString());
                    }
                }
            }

            return dr;
        }
        return null;
    }

    /**
     * getFields
     */
    public Field[] getFields(String tablename) throws Exception {
        if (tablename == null)
            return null;

        DescribeSObjectResult describeSObjectResult = binding.describeSObject(tablename);

        if (describeSObjectResult != null) {
            Field[] fields = describeSObjectResult.getFields();

            return fields;

        }

        return null;
    }

    /**
     * getTables
     */
    public String[] getTables() throws Exception {

        DescribeGlobalResult describeGlobalResult = binding.describeGlobal();

        String[] types = null;
        if (describeGlobalResult != null)

            types = describeGlobalResult.getTypes();

        return types;
    }

    /**
     * query
     */
    public List<SObject> query(String queryString) throws Exception {

        List<SObject> queryResultList = new ArrayList<SObject>();
        QueryResult qr = null;
        QueryOptions qo = new QueryOptions();
        qo.setBatchSize(new Integer(250));
        binding.setHeader(sforceService.getServiceName().getNamespaceURI(), "QueryOptions", qo);

        qr = binding.query(queryString);

        boolean bContinue = true;

        while (bContinue && qr != null && qr.getRecords() != null) {

            // process the query results
            for (int i = 0; i < qr.getRecords().length; i++) {
                queryResultList.add(qr.getRecords()[i]);
            }
            // handle the loop + 1 problem by checking the most recent queryResult
            if (qr.isDone()) {
                bContinue = false;
            } else {
                qr = binding.queryMore(qr.getQueryLocator());
            }
        }

        return queryResultList;

    }

    /**
     * update, one record, one time.
     */
    public SaveResult[] update(String tablename, String id, MessageElement[] updatefields, String[] fieldsToNull)
            throws Exception {

        SObject item = new SObject(); // create the account object to hold our changes
        item.setType(tablename);
        item.setId(id); // need to have the id so that API knows which account to update
        item.set_any(updatefields); // set a new value for the name property
        item.setFieldsToNull(fieldsToNull);// set a null field for the name property
        updateItems.add(item);

        // call the update passing an array of object
        if (updateItems.size() >= commitLevel) {
            SObject[] upds = updateItems.toArray(new SObject[updateItems.size()]);
            SaveResult[] saveResults = binding.update(upds);
            updateItems.clear();
            upds = null;

            if (exceptionForErrors && saveResults != null && saveResults.length != 0) {
                for (SaveResult result : saveResults) {
                    StringBuilder errors = new StringBuilder("");
                    if (result.isSuccess()) {
                        // TODO: send back the ID
                    } else {
                        for (Error error : result.getErrors()) {
                            errors.append(error.getMessage()).append("\n");
                            if (logWriter != null) {
                                logWriter.append("\tStatus Code: ").append(error.getStatusCode().toString());
                                logWriter.newLine();
                                logWriter.newLine();
                                logWriter.append("\tFields: ");
                                boolean flag = false;
                                for (String field : error.getFields()) {
                                    if (flag) {
                                        logWriter.append(", ");
                                    } else {
                                        flag = true;
                                    }
                                    logWriter.append(field);
                                }
                                logWriter.newLine();
                                logWriter.newLine();

                                logWriter.append("\tMessage: ").append(error.getMessage());

                                logWriter.newLine();

                                logWriter
                                        .append("\t--------------------------------------------------------------------------------");

                                logWriter.newLine();
                                logWriter.newLine();

                            }
                        }
                    }
                    if (exceptionForErrors && errors.toString().length() > 0) {
                        if (logWriter != null) {
                            logWriter.close();
                        }
                        throw new Exception(errors.toString());
                    }

                }
            }

            return saveResults;
        }
        return null;

    }

    /**
     * upsert, one record, one time.
     */
    public UpsertResult[] upsert(String tablename, String upsertkey, MessageElement[] updatefields, String[] fieldsToNull)
            throws Exception {

        // create the account object to hold our changes
        SObject item = new SObject();
        item.setType(tablename);
        // set a new value for the name property
        item.set_any(updatefields);
        item.setFieldsToNull(fieldsToNull);// set a null field for the name property
        upsertItems.add(item);

        upsertKeyColumn = upsertkey;

        // call the update passing an array of object
        if (upsertItems.size() >= commitLevel) {
            SObject[] upds = upsertItems.toArray(new SObject[upsertItems.size()]);
            UpsertResult[] upsertResults = binding.upsert(upsertKeyColumn, upds);
            upsertItems.clear();
            upds = null;

            if (exceptionForErrors && upsertResults != null && upsertResults.length != 0) {
                for (UpsertResult result : upsertResults) {
                    StringBuilder errors = new StringBuilder("");
                    if (result.isSuccess()) {
                        // TODO: send back the ID
                    } else {
                        for (Error error : result.getErrors()) {
                            errors.append(error.getMessage()).append("\n");
                            if (logWriter != null) {
                                logWriter.append("\tStatus Code: ").append(error.getStatusCode().toString());
                                logWriter.newLine();
                                logWriter.newLine();
                                logWriter.append("\tFields: ");
                                boolean flag = false;
                                for (String field : error.getFields()) {
                                    if (flag) {
                                        logWriter.append(", ");
                                    } else {
                                        flag = true;
                                    }
                                    logWriter.append(field);
                                }
                                logWriter.newLine();
                                logWriter.newLine();

                                logWriter.append("\tMessage: ").append(error.getMessage());

                                logWriter.newLine();

                                logWriter
                                        .append("\t--------------------------------------------------------------------------------");

                                logWriter.newLine();
                                logWriter.newLine();

                            }
                        }
                    }
                    if (exceptionForErrors && errors.toString().length() > 0) {
                        if (logWriter != null) {
                            logWriter.close();
                        }
                        throw new Exception(errors.toString());
                    }

                }
            }
            return upsertResults;
        }
        return null;

    }

    public Calendar getServerTimestamp() throws Exception {
        return this.binding.getServerTimestamp().getTimestamp();
    }

    public Map<String, String> readResult(Object[] results) throws Exception {
        Map<String, String> resultMessage = null;
        if (results instanceof SaveResult[]) {
            for (SaveResult result : (SaveResult[]) results) {
                resultMessage = new HashMap<String, String>();
                resultMessage.put("id", result.getId());
                resultMessage.put("success", String.valueOf(result.isSuccess()));
                if (!result.isSuccess()) {
                    for (Error error : result.getErrors()) {
                        if (error.getStatusCode() != null)
                            resultMessage.put("StatusCode", error.getStatusCode().toString());
                        if (error.getFields() != null) {
                            StringBuffer fields = new StringBuffer();
                            for (String field : error.getFields()) {
                                fields.append(field);
                                fields.append(",");
                            }
                            if (fields.length() > 0) {
                                fields.deleteCharAt(fields.length() - 1);
                            }
                            resultMessage.put("Fields", fields.toString());
                        }
                        resultMessage.put("Message", error.getMessage());
                    }
                }
            }
            return resultMessage;
        } else if (results instanceof DeleteResult[]) {
            for (DeleteResult result : (DeleteResult[]) results) {
                resultMessage = new HashMap<String, String>();
                resultMessage.put("id", result.getId());
                resultMessage.put("success", String.valueOf(result.isSuccess()));
                if (!result.isSuccess()) {
                    for (Error error : result.getErrors()) {
                        if (error.getStatusCode() != null)
                            resultMessage.put("StatusCode", error.getStatusCode().toString());
                        resultMessage.put("Message", error.getMessage());
                    }
                }
            }
            return resultMessage;
        } else if (results instanceof UpsertResult[]) {
            for (UpsertResult result : (UpsertResult[]) results) {
                resultMessage = new HashMap<String, String>();
                resultMessage.put("id", result.getId());
                resultMessage.put("success", String.valueOf(result.isSuccess()));
                resultMessage.put("created", String.valueOf(result.isCreated()));
                if (!result.isSuccess()) {
                    for (Error error : result.getErrors()) {
                        if (error.getStatusCode() != null)
                            resultMessage.put("StatusCode", error.getStatusCode().toString());
                        if (error.getFields() != null) {
                            StringBuffer fields = new StringBuffer();
                            for (String field : error.getFields()) {
                                fields.append(field);
                                fields.append(",");
                            }
                            if (fields.length() > 0) {
                                fields.deleteCharAt(fields.length() - 1);
                            }
                            resultMessage.put("Fields", fields.toString());
                        }
                        resultMessage.put("Message", error.getMessage());
                    }
                }
            }
            return resultMessage;
        }
        return null;
    }
}
