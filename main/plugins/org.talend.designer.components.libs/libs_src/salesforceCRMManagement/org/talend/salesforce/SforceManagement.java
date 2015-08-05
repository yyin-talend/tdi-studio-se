// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
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

import java.util.Calendar;
import java.util.Map;

import org.apache.axiom.om.OMElement;

import com.salesforce.soap.partner.CallOptions;
import com.salesforce.soap.partner.DeleteResult;
import com.salesforce.soap.partner.GetDeletedResult;
import com.salesforce.soap.partner.ID;
import com.salesforce.soap.partner.QueryLocator;
import com.salesforce.soap.partner.QueryResult;
import com.salesforce.soap.partner.SaveResult;
import com.salesforce.soap.partner.SessionHeader;
import com.salesforce.soap.partner.SforceServiceStub;
import com.salesforce.soap.partner.UpsertResult;
import com.salesforce.soap.partner.sobject.SObject;

/**
 * DOC bchen class global comment. Detailled comment
 */
public interface SforceManagement {

    public SforceServiceStub getStub();

    public SessionHeader getSessionHeader();

    public CallOptions getCallOptions();

    public void setCallOptions(CallOptions co);

    public void setClientID(String clientID);

    public boolean login(String endpoint, String username, String password, int timeout, boolean needCompression)
            throws Exception;

    public boolean login(String endpoint, String username, String password, String timeout, boolean needCompression)
            throws Exception;

    public void login(SforceServiceStub stub, SessionHeader sh) throws Exception;

    public boolean login(String endpoint, String username, String password, int timeout, boolean needCompression,
            int commitLevel, boolean exceptionForErrors, String errorLogFile) throws Exception;

    public boolean login(String endpoint, String username, String password, String timeout, boolean needCompression,
            int commitLevel, boolean exceptionForErrors, String errorLogFile) throws Exception;

    public void login(SforceServiceStub stub, SessionHeader sh, int commitLevel, boolean exceptionForErrors, String errorLogFile)
            throws Exception;

    public void logout() throws Exception;

    public DeleteResult[] delete(String id) throws Exception;

    public OMElement newOMElement(String name, String value) throws Exception;

    public SaveResult[] insert(String tablename, OMElement[] nameValues) throws Exception;

    public SaveResult[] update(String tablename, String idStr, OMElement[] updatefields, String[] fieldsToNull) throws Exception;

    public UpsertResult[] upsert(String tablename, String upsertkey, OMElement[] updatefields, String[] fieldsToNull)
            throws Exception;

    public Map<String, String> readResult(Object[] results) throws Exception;

    public Calendar getServerTimestamp() throws Exception;

    public QueryResult queryMore(QueryLocator queryLocator, int batchSize) throws Exception;

    public QueryResult query(String soql, int batchSize) throws Exception;

    public QueryResult queryAll(String soql, int batchSize) throws Exception;

    public GetDeletedResult getDeleted(String objectType, Calendar startDate, Calendar endDate) throws Exception;

    public SObject[] retrieve(ID[] ids, String objectType, String fieldsList) throws Exception;

    public ID[] getUpdated(String objectType, Calendar startDate, Calendar endDate) throws Exception;
}
