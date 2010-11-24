// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;

import com.salesforce.soap.partner.Create;
import com.salesforce.soap.partner.Delete;
import com.salesforce.soap.partner.DeleteResponse;
import com.salesforce.soap.partner.DeleteResult;
import com.salesforce.soap.partner.Error;
import com.salesforce.soap.partner.GetDeleted;
import com.salesforce.soap.partner.GetDeletedResult;
import com.salesforce.soap.partner.GetServerTimestamp;
import com.salesforce.soap.partner.GetUpdated;
import com.salesforce.soap.partner.GetUpdatedResult;
import com.salesforce.soap.partner.ID;
import com.salesforce.soap.partner.Login;
import com.salesforce.soap.partner.LoginResult;
import com.salesforce.soap.partner.Query;
import com.salesforce.soap.partner.QueryAll;
import com.salesforce.soap.partner.QueryLocator;
import com.salesforce.soap.partner.QueryMore;
import com.salesforce.soap.partner.QueryOptions;
import com.salesforce.soap.partner.QueryResult;
import com.salesforce.soap.partner.Retrieve;
import com.salesforce.soap.partner.SaveResult;
import com.salesforce.soap.partner.SessionHeader;
import com.salesforce.soap.partner.SforceServiceStub;
import com.salesforce.soap.partner.Update;
import com.salesforce.soap.partner.Upsert;
import com.salesforce.soap.partner.UpsertResult;
import com.salesforce.soap.partner.sobject.SObject;

/**
 * DOC bchen class global comment. Detailled comment
 */
public class SforceManagementImpl implements SforceManagement {

    private SforceServiceStub stub;

    private SessionHeader sh;

    public SforceServiceStub getStub() {
        return stub;
    }

    public SessionHeader getSessionHeader() {
        return sh;
    }

    private void needCompression(Options options) {
        options.setProperty(HTTPConstants.MC_ACCEPT_GZIP, Boolean.TRUE);
        options.setProperty(HTTPConstants.MC_GZIP_REQUEST, Boolean.TRUE);
    }

    private void setTimeout(Options options, int timeout) {
        options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, timeout);
        // options.setProperty(HTTPConstants.SO_TIMEOUT, timeout);
    }

    public boolean login(String endpoint, String username, String password, int timeout, boolean needCompression)
            throws Exception {
        if (endpoint == null || endpoint.trim().length() == 0)
            return false;
        if (username == null || username.trim().length() == 0)
            return false;
        if (password == null || password.trim().length() == 0)
            return false;

        this.commitLevel = 1;

        this.deleteItems = new ArrayList<ID>(commitLevel * 2);
        this.insertItems = new ArrayList<SObject>(commitLevel * 2);
        this.updateItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertKeyColumn = "";

        stub = new SforceServiceStub(endpoint);
        Options options = stub._getServiceClient().getOptions();

        if (needCompression) {
            needCompression(options);
        }
        setTimeout(options, timeout);

        Login login = new Login();
        login.setUsername(username);
        login.setPassword(password);

        LoginResult loginResult = stub.login(login, null, null).getResult();
        sh = new SessionHeader();
        sh.setSessionId(loginResult.getSessionId());
        stub = new SforceServiceStub(loginResult.getServerUrl());
        options = stub._getServiceClient().getOptions();

        if (needCompression) {
            needCompression(options);
        }
        setTimeout(options, timeout);

        return true;
    }

    public void login(SforceServiceStub stub, SessionHeader sh) throws Exception {
        this.commitLevel = 1;

        this.deleteItems = new ArrayList<ID>(commitLevel * 2);
        this.insertItems = new ArrayList<SObject>(commitLevel * 2);
        this.updateItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertKeyColumn = "";

        this.stub = stub;
        this.sh = sh;
    }

    private boolean exceptionForErrors = false;

    private java.io.BufferedWriter logWriter = null;

    private int commitLevel = 0;

    private ArrayList<ID> deleteItems;

    private ArrayList<SObject> insertItems;

    private ArrayList<SObject> upsertItems;

    private ArrayList<SObject> updateItems;

    private String upsertKeyColumn;

    public boolean login(String endpoint, String username, String password, int timeout, boolean needCompression,
            int commitLevel, boolean exceptionForErrors, String errorLogFile) throws Exception {
        if (username == null || username.trim().length() == 0)
            return false;
        if (password == null || password.trim().length() == 0)
            return false;
        if (endpoint == null || endpoint.trim().length() == 0)
            return false;

        if (commitLevel < 0)
            commitLevel = 1;
        else if (commitLevel > 200)
            commitLevel = 200;

        this.commitLevel = commitLevel;
        this.exceptionForErrors = exceptionForErrors;
        if (errorLogFile != null && errorLogFile.trim().length() > 0) {
            logWriter = new java.io.BufferedWriter(new java.io.FileWriter(errorLogFile));
        }

        this.deleteItems = new ArrayList<ID>(commitLevel * 2);
        this.insertItems = new ArrayList<SObject>(commitLevel * 2);
        this.updateItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertKeyColumn = "";

        stub = new SforceServiceStub(endpoint);
        Options options = stub._getServiceClient().getOptions();

        if (needCompression) {
            needCompression(options);
        }
        setTimeout(options, timeout);

        Login login = new Login();
        login.setUsername(username);
        login.setPassword(password);

        LoginResult loginResult = stub.login(login, null, null).getResult();
        sh = new SessionHeader();
        sh.setSessionId(loginResult.getSessionId());
        stub = new SforceServiceStub(loginResult.getServerUrl());
        options = stub._getServiceClient().getOptions();

        if (needCompression) {
            needCompression(options);
        }
        setTimeout(options, timeout);

        return true;
    }

    public void login(SforceServiceStub stub, SessionHeader sh, int commitLevel, boolean exceptionForErrors, String errorLogFile)
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

        this.deleteItems = new ArrayList<ID>(commitLevel * 2);
        this.insertItems = new ArrayList<SObject>(commitLevel * 2);
        this.updateItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertItems = new ArrayList<SObject>(commitLevel * 2);
        this.upsertKeyColumn = "";

        this.stub = stub;
        this.sh = sh;
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
                Create create = new Create();
                create.setSObjects(accs);
                SaveResult[] sr = stub.create(create, sh, null, null, null, null, null, null, null, null, null).getResult();
                insertItems.clear();
                accs = null;

                if (exceptionForErrors && sr != null && sr.length != 0) {
                    for (SaveResult result : sr) {
                        StringBuilder errors = new StringBuilder("");
                        if (result.getSuccess()) {
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
                ID[] delIDs = deleteItems.toArray(new ID[deleteItems.size()]);
                Delete dels = new Delete();
                dels.setIds(delIDs);
                DeleteResponse dresp = stub.delete(dels, sh, null, null, null, null, null, null, null, null);
                DeleteResult[] dr = dresp.getResult();
                deleteItems.clear();
                delIDs = null;

                if (exceptionForErrors && dr != null && dr.length != 0) {
                    for (DeleteResult result : dr) {
                        StringBuilder errors = new StringBuilder("");
                        if (result.getSuccess()) {
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
                Update update = new Update();
                update.setSObjects(upds);
                SaveResult[] saveResults = stub.update(update, sh, null, null, null, null, null, null, null, null, null)
                        .getResult();
                updateItems.clear();
                upds = null;

                if (exceptionForErrors && saveResults != null && saveResults.length != 0) {
                    for (SaveResult result : saveResults) {
                        StringBuilder errors = new StringBuilder("");
                        if (result.getSuccess()) {
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
                Upsert upsert = new Upsert();
                upsert.setSObjects(upds);
                upsert.setExternalIDFieldName(upsertKeyColumn);
                UpsertResult[] upsertResults = stub.upsert(upsert, sh, null, null, null, null, null, null, null, null, null)
                        .getResult();
                upsertItems.clear();
                upds = null;

                if (exceptionForErrors && upsertResults != null && upsertResults.length != 0) {
                    for (UpsertResult result : upsertResults) {
                        StringBuilder errors = new StringBuilder("");
                        if (result.getSuccess()) {
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
    }

    /**
     * delete, one time one record.
     */
    public DeleteResult[] delete(String id) throws Exception {
        if (id == null)
            return null;

        // String[] ids = new String[] { id };
        ID dID = new ID();
        dID.setID(id);
        deleteItems.add(dID);

        if (deleteItems.size() >= commitLevel) {
            ID[] delIDs = deleteItems.toArray(new ID[deleteItems.size()]);
            Delete dels = new Delete();
            dels.setIds(delIDs);
            DeleteResponse dresp = stub.delete(dels, sh, null, null, null, null, null, null, null, null);
            DeleteResult[] dr = dresp.getResult();
            deleteItems.clear();
            delIDs = null;

            if (dr != null && dr.length != 0) {
                for (DeleteResult result : dr) {
                    StringBuilder errors = new StringBuilder("");
                    if (result.getSuccess()) {
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

    public OMElement newOMElement(String name, String value) throws Exception {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMElement ome = fac.createOMElement(name, null);
        ome.addChild(fac.createOMText(ome, value));
        return ome;
    }

    /**
     * create, one time one record.
     */
    public SaveResult[] insert(String tablename, OMElement[] nameValues) throws Exception {
        if (tablename == null || tablename.trim().length() == 0)
            return null;

        if (nameValues == null || nameValues.length == 0)
            return null;

        SObject item = new SObject();
        item.setType(tablename);
        item.setExtraElement(nameValues);
        // item.setId("00T9000000VLEqBDAX");
        insertItems.add(item);

        if (insertItems.size() >= commitLevel) {
            SObject[] accs = insertItems.toArray(new SObject[insertItems.size()]);
            Create create = new Create();
            create.setSObjects(accs);
            SaveResult[] sr = stub.create(create, sh, null, null, null, null, null, null, null, null, null).getResult();
            insertItems.clear();
            accs = null;

            if (sr != null && sr.length != 0) {
                for (SaveResult result : sr) {
                    StringBuilder errors = new StringBuilder("");
                    if (result.getSuccess()) {
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

    /**
     * update, one record, one time.
     */
    public SaveResult[] update(String tablename, String idStr, OMElement[] updatefields, String[] fieldsToNull) throws Exception {

        SObject item = new SObject(); // create the account object to hold our changes
        item.setType(tablename);
        ID id = new ID();
        id.setID(idStr);
        item.setId(id); // need to have the id so that API knows which account to update
        item.setExtraElement(updatefields); // set a new value for the name property
        item.setFieldsToNull(fieldsToNull);// set a null field for the name property
        updateItems.add(item);

        // call the update passing an array of object
        if (updateItems.size() >= commitLevel) {
            SObject[] upds = updateItems.toArray(new SObject[updateItems.size()]);
            Update update = new Update();
            update.setSObjects(upds);
            SaveResult[] saveResults = stub.update(update, sh, null, null, null, null, null, null, null, null, null).getResult();
            updateItems.clear();
            upds = null;

            if (exceptionForErrors && saveResults != null && saveResults.length != 0) {
                for (SaveResult result : saveResults) {
                    StringBuilder errors = new StringBuilder("");
                    if (result.getSuccess()) {
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
    public UpsertResult[] upsert(String tablename, String upsertkey, OMElement[] updatefields, String[] fieldsToNull)
            throws Exception {

        // create the account object to hold our changes
        SObject item = new SObject();
        item.setType(tablename);
        // set a new value for the name property
        item.setExtraElement(updatefields);
        item.setFieldsToNull(fieldsToNull);// set a null field for the name property
        upsertItems.add(item);

        upsertKeyColumn = upsertkey;

        // call the update passing an array of object
        if (upsertItems.size() >= commitLevel) {
            SObject[] upds = upsertItems.toArray(new SObject[upsertItems.size()]);
            Upsert upsert = new Upsert();
            upsert.setSObjects(upds);
            upsert.setExternalIDFieldName(upsertKeyColumn);
            UpsertResult[] upsertResults = stub.upsert(upsert, sh, null, null, null, null, null, null, null, null, null)
                    .getResult();
            upsertItems.clear();
            upds = null;

            if (exceptionForErrors && upsertResults != null && upsertResults.length != 0) {
                for (UpsertResult result : upsertResults) {
                    StringBuilder errors = new StringBuilder("");
                    if (result.getSuccess()) {
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

    public Map<String, String> readResult(Object[] results) throws Exception {
        Map<String, String> resultMessage = null;
        if (results instanceof SaveResult[]) {
            for (SaveResult result : (SaveResult[]) results) {
                resultMessage = new HashMap<String, String>();
                resultMessage.put("id", result.getId().getID());
                resultMessage.put("success", String.valueOf(result.getSuccess()));
                if (!result.getSuccess()) {
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
                resultMessage.put("id", result.getId().getID());
                resultMessage.put("success", String.valueOf(result.getSuccess()));
                if (!result.getSuccess()) {
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
                resultMessage.put("id", result.getId().getID());
                resultMessage.put("success", String.valueOf(result.getSuccess()));
                resultMessage.put("created", String.valueOf(result.getCreated()));
                if (!result.getSuccess()) {
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

    public Calendar getServerTimestamp() throws Exception {
        return stub.getServerTimestamp(new GetServerTimestamp(), sh, null).getResult().getTimestamp();
    }

    public ID[] getUpdated(String objectType, Calendar startDate, Calendar endDate) throws Exception {
        GetUpdated getUpdated = new GetUpdated();
        getUpdated.setSObjectType(objectType);
        getUpdated.setStartDate(startDate);
        getUpdated.setEndDate(endDate);
        GetUpdatedResult result = stub.getUpdated(getUpdated, sh, null).getResult();
        ID[] ids = result.getIds();
        return ids;
    }

    public SObject[] retrieve(ID[] ids, String objectType, String fieldsList) throws Exception {
        Retrieve retrieve = new Retrieve();
        retrieve.setFieldList(fieldsList);
        retrieve.setIds(ids);
        retrieve.setSObjectType(objectType);
        SObject[] results = stub.retrieve(retrieve, sh, null, null, null, null).getResult();
        // for (SObject sob : results) {
        // OMElement[] omes = sob.getExtraElement();
        // for (int i = 0; i < omes.length; i++) {
        // omes[i].getText();
        // omes[i].getLocalName();
        // }
        // }
        return results;
    }

    public GetDeletedResult getDeleted(String objectType, Calendar startDate, Calendar endDate) throws Exception {
        GetDeleted getDeleted = new GetDeleted();
        getDeleted.setSObjectType(objectType);
        getDeleted.setStartDate(startDate);
        getDeleted.setEndDate(endDate);
        GetDeletedResult result = stub.getDeleted(getDeleted, sh, null).getResult();
        // DeletedRecord[] deletedRecords = result.getDeletedRecords();
        // List<String> ids = new ArrayList<String>();
        // for (DeletedRecord deletedRecord : deletedRecords) {
        // ids.add(deletedRecord.getId().getID());
        // }
        return result;
    }

    public QueryResult queryAll(String soql, int batchSize) throws Exception {
        QueryAll queryAll = new QueryAll();
        queryAll.setQueryString(soql);
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setBatchSize(batchSize);
        QueryResult qr = stub.queryAll(queryAll, sh, null, queryOptions).getResult();
        return qr;
    }

    public QueryResult queryMore(QueryLocator queryLocator, int batchSize) throws Exception {
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setBatchSize(batchSize);
        QueryMore queryMore = new QueryMore();
        queryMore.setQueryLocator(queryLocator);
        QueryResult qr = stub.queryMore(queryMore, sh, null, queryOptions).getResult();
        return qr;
    }

    public QueryResult query(String soql, int batchSize) throws Exception {
        Query query = new Query();
        query.setQueryString(soql);
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setBatchSize(batchSize);
        QueryResult qr = stub.query(query, sh, null, queryOptions, null, null).getResult();
        return qr;
    }
}
