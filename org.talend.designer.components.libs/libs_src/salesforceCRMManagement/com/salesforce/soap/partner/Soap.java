/**
 * Soap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.salesforce.soap.partner;

public interface Soap extends java.rmi.Remote {

    /**
     * Login to the Salesforce.com SOAP Api
     */
    public com.salesforce.soap.partner.LoginResult login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault, com.salesforce.soap.partner.fault.LoginFault;

    /**
     * Describe an sObject
     */
    public com.salesforce.soap.partner.DescribeSObjectResult describeSObject(java.lang.String sObjectType) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Describe a number sObjects
     */
    public com.salesforce.soap.partner.DescribeSObjectResult[] describeSObjects(java.lang.String[] sObjectType) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Describe the Global state
     */
    public com.salesforce.soap.partner.DescribeGlobalResult describeGlobal() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Describe all the data category groups available for a given
     * set of types
     */
    public com.salesforce.soap.partner.DescribeDataCategoryGroupResult[] describeDataCategoryGroups(java.lang.String[] sObjectType) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Describe the data category group structures for a given set
     * of pair of types and data category group name
     */
    public com.salesforce.soap.partner.DescribeDataCategoryGroupStructureResult[] describeDataCategoryGroupStructures(com.salesforce.soap.partner.DataCategoryGroupSobjectTypePair[] pairs, boolean topCategoriesOnly) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Describe the layout of an sObject
     */
    public com.salesforce.soap.partner.DescribeLayoutResult describeLayout(java.lang.String sObjectType, java.lang.String[] recordTypeIds) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Describe the layout of the SoftPhone
     */
    public com.salesforce.soap.partner.DescribeSoftphoneLayoutResult describeSoftphoneLayout() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Describe the tabs that appear on a users page
     */
    public com.salesforce.soap.partner.DescribeTabSetResult[] describeTabs() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Create a set of new sObjects
     */
    public com.salesforce.soap.partner.SaveResult[] create(com.salesforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Update a set of sObjects
     */
    public com.salesforce.soap.partner.SaveResult[] update(com.salesforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Update or insert a set of sObjects based on object id
     */
    public com.salesforce.soap.partner.UpsertResult[] upsert(java.lang.String externalIDFieldName, com.salesforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Merge and update a set of sObjects based on object id
     */
    public com.salesforce.soap.partner.MergeResult[] merge(com.salesforce.soap.partner.MergeRequest[] request) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Delete a set of sObjects
     */
    public com.salesforce.soap.partner.DeleteResult[] delete(java.lang.String[] ids) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Undelete a set of sObjects
     */
    public com.salesforce.soap.partner.UndeleteResult[] undelete(java.lang.String[] ids) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Empty a set of sObjects from the recycle bin
     */
    public com.salesforce.soap.partner.EmptyRecycleBinResult[] emptyRecycleBin(java.lang.String[] ids) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Get a set of sObjects
     */
    public com.salesforce.soap.partner.sobject.SObject[] retrieve(java.lang.String fieldList, java.lang.String sObjectType, java.lang.String[] ids) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.MalformedQueryFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Submit an entity to a workflow process or process a workitem
     */
    public com.salesforce.soap.partner.ProcessResult[] process(com.salesforce.soap.partner.ProcessRequest[] actions) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * convert a set of leads
     */
    public com.salesforce.soap.partner.LeadConvertResult[] convertLead(com.salesforce.soap.partner.LeadConvert[] leadConverts) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Logout the current user, invalidating the current session.
     */
    public void logout() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Logs out and invalidates session ids
     */
    public com.salesforce.soap.partner.InvalidateSessionsResult[] invalidateSessions(java.lang.String[] sessionIds) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Get the IDs for deleted sObjects
     */
    public com.salesforce.soap.partner.GetDeletedResult getDeleted(java.lang.String sObjectType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Get the IDs for updated sObjects
     */
    public com.salesforce.soap.partner.GetUpdatedResult getUpdated(java.lang.String sObjectType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Create a Query Cursor
     */
    public com.salesforce.soap.partner.QueryResult query(java.lang.String queryString) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.MalformedQueryFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault, com.salesforce.soap.partner.fault.InvalidQueryLocatorFault;

    /**
     * Create a Query Cursor, including deleted sObjects
     */
    public com.salesforce.soap.partner.QueryResult queryAll(java.lang.String queryString) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.MalformedQueryFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault, com.salesforce.soap.partner.fault.InvalidQueryLocatorFault;

    /**
     * Gets the next batch of sObjects from a query
     */
    public com.salesforce.soap.partner.QueryResult queryMore(java.lang.String queryLocator) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault, com.salesforce.soap.partner.fault.InvalidQueryLocatorFault;

    /**
     * Search for sObjects
     */
    public com.salesforce.soap.partner.SearchResult search(java.lang.String searchString) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.MalformedSearchFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Gets server timestamp
     */
    public com.salesforce.soap.partner.GetServerTimestampResult getServerTimestamp() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Set a user's password
     */
    public com.salesforce.soap.partner.SetPasswordResult setPassword(java.lang.String userId, java.lang.String password) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault, com.salesforce.soap.partner.fault.InvalidNewPasswordFault;

    /**
     * Reset a user's password
     */
    public com.salesforce.soap.partner.ResetPasswordResult resetPassword(java.lang.String userId) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Returns standard information relevant to the current user
     */
    public com.salesforce.soap.partner.GetUserInfoResult getUserInfo() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;

    /**
     * Send outbound email
     */
    public com.salesforce.soap.partner.SendEmailResult[] sendEmail(com.salesforce.soap.partner.Email[] messages) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault;
}
