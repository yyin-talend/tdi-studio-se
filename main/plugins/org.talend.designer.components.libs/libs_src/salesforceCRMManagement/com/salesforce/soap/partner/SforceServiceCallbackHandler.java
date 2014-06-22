/**
 * SforceServiceCallbackHandler.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.5.2 Built on : Sep 06, 2010 (09:42:01 CEST)
 */

package com.salesforce.soap.partner;

/**
 * SforceServiceCallbackHandler Callback class, Users can extend this class and implement their own receiveResult and
 * receiveError methods.
 */
public abstract class SforceServiceCallbackHandler {

    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking Web service call is finished and
     * appropriate method of this CallBack is called.
     * 
     * @param clientData Object mechanism by which the user can pass in user data that will be avilable at the time this
     * callback is called.
     */
    public SforceServiceCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public SforceServiceCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */

    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for merge method override this method for handling normal response from
     * merge operation
     */
    public void receiveResultmerge(com.salesforce.soap.partner.MergeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from merge operation
     */
    public void receiveErrormerge(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getUserInfo method override this method for handling normal response
     * from getUserInfo operation
     */
    public void receiveResultgetUserInfo(com.salesforce.soap.partner.GetUserInfoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from getUserInfo operation
     */
    public void receiveErrorgetUserInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for describeSoftphoneLayout method override this method for handling normal
     * response from describeSoftphoneLayout operation
     */
    public void receiveResultdescribeSoftphoneLayout(com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from describeSoftphoneLayout
     * operation
     */
    public void receiveErrordescribeSoftphoneLayout(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for update method override this method for handling normal response from
     * update operation
     */
    public void receiveResultupdate(com.salesforce.soap.partner.UpdateResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from update operation
     */
    public void receiveErrorupdate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for setPassword method override this method for handling normal response
     * from setPassword operation
     */
    public void receiveResultsetPassword(com.salesforce.soap.partner.SetPasswordResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from setPassword operation
     */
    public void receiveErrorsetPassword(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for logout method override this method for handling normal response from
     * logout operation
     */
    public void receiveResultlogout(com.salesforce.soap.partner.LogoutResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from logout operation
     */
    public void receiveErrorlogout(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for retrieve method override this method for handling normal response from
     * retrieve operation
     */
    public void receiveResultretrieve(com.salesforce.soap.partner.RetrieveResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from retrieve operation
     */
    public void receiveErrorretrieve(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for queryAll method override this method for handling normal response from
     * queryAll operation
     */
    public void receiveResultqueryAll(com.salesforce.soap.partner.QueryAllResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from queryAll operation
     */
    public void receiveErrorqueryAll(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getUpdated method override this method for handling normal response
     * from getUpdated operation
     */
    public void receiveResultgetUpdated(com.salesforce.soap.partner.GetUpdatedResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from getUpdated operation
     */
    public void receiveErrorgetUpdated(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for undelete method override this method for handling normal response from
     * undelete operation
     */
    public void receiveResultundelete(com.salesforce.soap.partner.UndeleteResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from undelete operation
     */
    public void receiveErrorundelete(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for create method override this method for handling normal response from
     * create operation
     */
    public void receiveResultcreate(com.salesforce.soap.partner.CreateResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from create operation
     */
    public void receiveErrorcreate(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sendEmail method override this method for handling normal response from
     * sendEmail operation
     */
    public void receiveResultsendEmail(com.salesforce.soap.partner.SendEmailResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from sendEmail operation
     */
    public void receiveErrorsendEmail(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for search method override this method for handling normal response from
     * search operation
     */
    public void receiveResultsearch(com.salesforce.soap.partner.SearchResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from search operation
     */
    public void receiveErrorsearch(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for query method override this method for handling normal response from
     * query operation
     */
    public void receiveResultquery(com.salesforce.soap.partner.QueryResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from query operation
     */
    public void receiveErrorquery(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getDeleted method override this method for handling normal response
     * from getDeleted operation
     */
    public void receiveResultgetDeleted(com.salesforce.soap.partner.GetDeletedResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from getDeleted operation
     */
    public void receiveErrorgetDeleted(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for process method override this method for handling normal response from
     * process operation
     */
    public void receiveResultprocess(com.salesforce.soap.partner.ProcessResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from process operation
     */
    public void receiveErrorprocess(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for describeDataCategoryGroupStructures method override this method for
     * handling normal response from describeDataCategoryGroupStructures operation
     */
    public void receiveResultdescribeDataCategoryGroupStructures(
            com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from
     * describeDataCategoryGroupStructures operation
     */
    public void receiveErrordescribeDataCategoryGroupStructures(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for resetPassword method override this method for handling normal response
     * from resetPassword operation
     */
    public void receiveResultresetPassword(com.salesforce.soap.partner.ResetPasswordResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from resetPassword operation
     */
    public void receiveErrorresetPassword(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for describeGlobal method override this method for handling normal response
     * from describeGlobal operation
     */
    public void receiveResultdescribeGlobal(com.salesforce.soap.partner.DescribeGlobalResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from describeGlobal operation
     */
    public void receiveErrordescribeGlobal(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for describeLayout method override this method for handling normal response
     * from describeLayout operation
     */
    public void receiveResultdescribeLayout(com.salesforce.soap.partner.DescribeLayoutResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from describeLayout operation
     */
    public void receiveErrordescribeLayout(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for describeTabs method override this method for handling normal response
     * from describeTabs operation
     */
    public void receiveResultdescribeTabs(com.salesforce.soap.partner.DescribeTabsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from describeTabs operation
     */
    public void receiveErrordescribeTabs(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for describeDataCategoryGroups method override this method for handling
     * normal response from describeDataCategoryGroups operation
     */
    public void receiveResultdescribeDataCategoryGroups(com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from
     * describeDataCategoryGroups operation
     */
    public void receiveErrordescribeDataCategoryGroups(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getServerTimestamp method override this method for handling normal
     * response from getServerTimestamp operation
     */
    public void receiveResultgetServerTimestamp(com.salesforce.soap.partner.GetServerTimestampResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from getServerTimestamp
     * operation
     */
    public void receiveErrorgetServerTimestamp(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for invalidateSessions method override this method for handling normal
     * response from invalidateSessions operation
     */
    public void receiveResultinvalidateSessions(com.salesforce.soap.partner.InvalidateSessionsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from invalidateSessions
     * operation
     */
    public void receiveErrorinvalidateSessions(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for describeSObject method override this method for handling normal
     * response from describeSObject operation
     */
    public void receiveResultdescribeSObject(com.salesforce.soap.partner.DescribeSObjectResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from describeSObject
     * operation
     */
    public void receiveErrordescribeSObject(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for login method override this method for handling normal response from
     * login operation
     */
    public void receiveResultlogin(com.salesforce.soap.partner.LoginResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from login operation
     */
    public void receiveErrorlogin(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for queryMore method override this method for handling normal response from
     * queryMore operation
     */
    public void receiveResultqueryMore(com.salesforce.soap.partner.QueryMoreResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from queryMore operation
     */
    public void receiveErrorqueryMore(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for describeSObjects method override this method for handling normal
     * response from describeSObjects operation
     */
    public void receiveResultdescribeSObjects(com.salesforce.soap.partner.DescribeSObjectsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from describeSObjects
     * operation
     */
    public void receiveErrordescribeSObjects(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for emptyRecycleBin method override this method for handling normal
     * response from emptyRecycleBin operation
     */
    public void receiveResultemptyRecycleBin(com.salesforce.soap.partner.EmptyRecycleBinResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from emptyRecycleBin
     * operation
     */
    public void receiveErroremptyRecycleBin(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for upsert method override this method for handling normal response from
     * upsert operation
     */
    public void receiveResultupsert(com.salesforce.soap.partner.UpsertResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from upsert operation
     */
    public void receiveErrorupsert(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for convertLead method override this method for handling normal response
     * from convertLead operation
     */
    public void receiveResultconvertLead(com.salesforce.soap.partner.ConvertLeadResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from convertLead operation
     */
    public void receiveErrorconvertLead(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for delete method override this method for handling normal response from
     * delete operation
     */
    public void receiveResultdelete(com.salesforce.soap.partner.DeleteResponse result) {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling error response from delete operation
     */
    public void receiveErrordelete(java.lang.Exception e) {
    }

}
