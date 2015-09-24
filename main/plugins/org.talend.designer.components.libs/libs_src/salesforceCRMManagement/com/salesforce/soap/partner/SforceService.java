/**
 * SforceService.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.salesforce.soap.partner;

/*
 * SforceService java interface
 */

public interface SforceService {

    /**
     * Auto generated method signature Merge and update a set of sObjects based on object id
     * 
     * @param merge221
     * 
     * @param sessionHeader222
     * 
     * @param callOptions223
     * 
     * @param assignmentRuleHeader224
     * 
     * @param mruHeader225
     * 
     * @param allowFieldTruncationHeader226
     * 
     * @param disableFeedTrackingHeader227
     * 
     * @param streamingEnabledHeader228
     * 
     * @param duplicateRuleHeader229
     * 
     * @param localeOptions230
     * 
     * @param debuggingHeader231
     * 
     * @param packageVersionHeader232
     * 
     * @param emailHeader233
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.MergeResponse merge(

    com.salesforce.soap.partner.Merge merge221, com.salesforce.soap.partner.SessionHeader sessionHeader222,
            com.salesforce.soap.partner.CallOptions callOptions223,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader224,
            com.salesforce.soap.partner.MruHeader mruHeader225,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader226,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader227,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader228,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader229,
            com.salesforce.soap.partner.LocaleOptions localeOptions230,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader231,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader232,
            com.salesforce.soap.partner.EmailHeader emailHeader233) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.InvalidIdFault,
            com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Merge and update a set of sObjects based on object id
     * 
     * @param merge221
     * 
     * @param sessionHeader222
     * 
     * @param callOptions223
     * 
     * @param assignmentRuleHeader224
     * 
     * @param mruHeader225
     * 
     * @param allowFieldTruncationHeader226
     * 
     * @param disableFeedTrackingHeader227
     * 
     * @param streamingEnabledHeader228
     * 
     * @param duplicateRuleHeader229
     * 
     * @param localeOptions230
     * 
     * @param debuggingHeader231
     * 
     * @param packageVersionHeader232
     * 
     * @param emailHeader233
     */
    public void startmerge(

    com.salesforce.soap.partner.Merge merge221, com.salesforce.soap.partner.SessionHeader sessionHeader222,
            com.salesforce.soap.partner.CallOptions callOptions223,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader224,
            com.salesforce.soap.partner.MruHeader mruHeader225,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader226,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader227,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader228,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader229,
            com.salesforce.soap.partner.LocaleOptions localeOptions230,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader231,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader232,
            com.salesforce.soap.partner.EmailHeader emailHeader233,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Returns standard information relevant to the current user
     * 
     * @param getUserInfo235
     * 
     * @param sessionHeader236
     * 
     * @param callOptions237
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.GetUserInfoResponse getUserInfo(

    com.salesforce.soap.partner.GetUserInfo getUserInfo235, com.salesforce.soap.partner.SessionHeader sessionHeader236,
            com.salesforce.soap.partner.CallOptions callOptions237) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Returns standard information relevant to the current user
     * 
     * @param getUserInfo235
     * 
     * @param sessionHeader236
     * 
     * @param callOptions237
     */
    public void startgetUserInfo(

    com.salesforce.soap.partner.GetUserInfo getUserInfo235, com.salesforce.soap.partner.SessionHeader sessionHeader236,
            com.salesforce.soap.partner.CallOptions callOptions237,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe all tabs available to a user
     * 
     * @param describeAllTabs239
     * 
     * @param sessionHeader240
     * 
     * @param callOptions241
     * 
     * @param packageVersionHeader242
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeAllTabsResponse describeAllTabs(

    com.salesforce.soap.partner.DescribeAllTabs describeAllTabs239, com.salesforce.soap.partner.SessionHeader sessionHeader240,
            com.salesforce.soap.partner.CallOptions callOptions241,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader242) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe all tabs available to a user
     * 
     * @param describeAllTabs239
     * 
     * @param sessionHeader240
     * 
     * @param callOptions241
     * 
     * @param packageVersionHeader242
     */
    public void startdescribeAllTabs(

    com.salesforce.soap.partner.DescribeAllTabs describeAllTabs239, com.salesforce.soap.partner.SessionHeader sessionHeader240,
            com.salesforce.soap.partner.CallOptions callOptions241,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader242,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the details of a series of quick actions
     * 
     * @param describeQuickActions244
     * 
     * @param sessionHeader245
     * 
     * @param callOptions246
     * 
     * @param packageVersionHeader247
     * 
     * @param localeOptions248
     */

    public com.salesforce.soap.partner.DescribeQuickActionsResponse describeQuickActions(

    com.salesforce.soap.partner.DescribeQuickActions describeQuickActions244,
            com.salesforce.soap.partner.SessionHeader sessionHeader245, com.salesforce.soap.partner.CallOptions callOptions246,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader247,
            com.salesforce.soap.partner.LocaleOptions localeOptions248) throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the details of a series of quick actions
     * 
     * @param describeQuickActions244
     * 
     * @param sessionHeader245
     * 
     * @param callOptions246
     * 
     * @param packageVersionHeader247
     * 
     * @param localeOptions248
     */
    public void startdescribeQuickActions(

    com.salesforce.soap.partner.DescribeQuickActions describeQuickActions244,
            com.salesforce.soap.partner.SessionHeader sessionHeader245, com.salesforce.soap.partner.CallOptions callOptions246,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader247,
            com.salesforce.soap.partner.LocaleOptions localeOptions248,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the layout of the SoftPhone
     * 
     * @param describeSoftphoneLayout250
     * 
     * @param sessionHeader251
     * 
     * @param callOptions252
     * 
     * @param packageVersionHeader253
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse describeSoftphoneLayout(

    com.salesforce.soap.partner.DescribeSoftphoneLayout describeSoftphoneLayout250,
            com.salesforce.soap.partner.SessionHeader sessionHeader251, com.salesforce.soap.partner.CallOptions callOptions252,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader253) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the layout of the SoftPhone
     * 
     * @param describeSoftphoneLayout250
     * 
     * @param sessionHeader251
     * 
     * @param callOptions252
     * 
     * @param packageVersionHeader253
     */
    public void startdescribeSoftphoneLayout(

    com.salesforce.soap.partner.DescribeSoftphoneLayout describeSoftphoneLayout250,
            com.salesforce.soap.partner.SessionHeader sessionHeader251, com.salesforce.soap.partner.CallOptions callOptions252,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader253,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the primary compact layouts for the sObjects requested
     * 
     * @param describePrimaryCompactLayouts255
     * 
     * @param sessionHeader256
     * 
     * @param callOptions257
     * 
     * @param packageVersionHeader258
     */

    public com.salesforce.soap.partner.DescribePrimaryCompactLayoutsResponse describePrimaryCompactLayouts(

    com.salesforce.soap.partner.DescribePrimaryCompactLayouts describePrimaryCompactLayouts255,
            com.salesforce.soap.partner.SessionHeader sessionHeader256, com.salesforce.soap.partner.CallOptions callOptions257,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader258) throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the primary compact layouts for the sObjects requested
     * 
     * @param describePrimaryCompactLayouts255
     * 
     * @param sessionHeader256
     * 
     * @param callOptions257
     * 
     * @param packageVersionHeader258
     */
    public void startdescribePrimaryCompactLayouts(

    com.salesforce.soap.partner.DescribePrimaryCompactLayouts describePrimaryCompactLayouts255,
            com.salesforce.soap.partner.SessionHeader sessionHeader256, com.salesforce.soap.partner.CallOptions callOptions257,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader258,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Execute the specified list view and return the presentation-ready results.
     * 
     * @param executeListView260
     * 
     * @param sessionHeader261
     * 
     * @param callOptions262
     * 
     * @param mruHeader263
     */

    public com.salesforce.soap.partner.ExecuteListViewResponse executeListView(

    com.salesforce.soap.partner.ExecuteListView executeListView260, com.salesforce.soap.partner.SessionHeader sessionHeader261,
            com.salesforce.soap.partner.CallOptions callOptions262, com.salesforce.soap.partner.MruHeader mruHeader263)
            throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations Execute the specified list view and return the presentation-ready
     * results.
     * 
     * @param executeListView260
     * 
     * @param sessionHeader261
     * 
     * @param callOptions262
     * 
     * @param mruHeader263
     */
    public void startexecuteListView(

    com.salesforce.soap.partner.ExecuteListView executeListView260, com.salesforce.soap.partner.SessionHeader sessionHeader261,
            com.salesforce.soap.partner.CallOptions callOptions262, com.salesforce.soap.partner.MruHeader mruHeader263,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Update a set of sObjects
     * 
     * @param update265
     * 
     * @param sessionHeader266
     * 
     * @param callOptions267
     * 
     * @param assignmentRuleHeader268
     * 
     * @param mruHeader269
     * 
     * @param allowFieldTruncationHeader270
     * 
     * @param disableFeedTrackingHeader271
     * 
     * @param streamingEnabledHeader272
     * 
     * @param allOrNoneHeader273
     * 
     * @param duplicateRuleHeader274
     * 
     * @param localeOptions275
     * 
     * @param debuggingHeader276
     * 
     * @param packageVersionHeader277
     * 
     * @param emailHeader278
     * 
     * @param ownerChangeOptions279
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.UpdateResponse update(

    com.salesforce.soap.partner.Update update265, com.salesforce.soap.partner.SessionHeader sessionHeader266,
            com.salesforce.soap.partner.CallOptions callOptions267,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader268,
            com.salesforce.soap.partner.MruHeader mruHeader269,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader270,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader271,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader272,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader273,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader274,
            com.salesforce.soap.partner.LocaleOptions localeOptions275,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader276,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader277,
            com.salesforce.soap.partner.EmailHeader emailHeader278,
            com.salesforce.soap.partner.OwnerChangeOptions ownerChangeOptions279) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.InvalidIdFault,
            com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Update a set of sObjects
     * 
     * @param update265
     * 
     * @param sessionHeader266
     * 
     * @param callOptions267
     * 
     * @param assignmentRuleHeader268
     * 
     * @param mruHeader269
     * 
     * @param allowFieldTruncationHeader270
     * 
     * @param disableFeedTrackingHeader271
     * 
     * @param streamingEnabledHeader272
     * 
     * @param allOrNoneHeader273
     * 
     * @param duplicateRuleHeader274
     * 
     * @param localeOptions275
     * 
     * @param debuggingHeader276
     * 
     * @param packageVersionHeader277
     * 
     * @param emailHeader278
     * 
     * @param ownerChangeOptions279
     */
    public void startupdate(

    com.salesforce.soap.partner.Update update265, com.salesforce.soap.partner.SessionHeader sessionHeader266,
            com.salesforce.soap.partner.CallOptions callOptions267,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader268,
            com.salesforce.soap.partner.MruHeader mruHeader269,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader270,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader271,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader272,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader273,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader274,
            com.salesforce.soap.partner.LocaleOptions localeOptions275,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader276,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader277,
            com.salesforce.soap.partner.EmailHeader emailHeader278,
            com.salesforce.soap.partner.OwnerChangeOptions ownerChangeOptions279,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Perform a series of predefined actions such as quick create or log a task
     * 
     * @param performQuickActions281
     * 
     * @param sessionHeader282
     * 
     * @param callOptions283
     * 
     * @param assignmentRuleHeader284
     * 
     * @param mruHeader285
     * 
     * @param allowFieldTruncationHeader286
     * 
     * @param disableFeedTrackingHeader287
     * 
     * @param streamingEnabledHeader288
     * 
     * @param allOrNoneHeader289
     * 
     * @param duplicateRuleHeader290
     * 
     * @param localeOptions291
     * 
     * @param debuggingHeader292
     * 
     * @param packageVersionHeader293
     * 
     * @param emailHeader294
     * 
     * @param ownerChangeOptions295
     */

    public com.salesforce.soap.partner.PerformQuickActionsResponse performQuickActions(

    com.salesforce.soap.partner.PerformQuickActions performQuickActions281,
            com.salesforce.soap.partner.SessionHeader sessionHeader282, com.salesforce.soap.partner.CallOptions callOptions283,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader284,
            com.salesforce.soap.partner.MruHeader mruHeader285,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader286,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader287,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader288,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader289,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader290,
            com.salesforce.soap.partner.LocaleOptions localeOptions291,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader292,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader293,
            com.salesforce.soap.partner.EmailHeader emailHeader294,
            com.salesforce.soap.partner.OwnerChangeOptions ownerChangeOptions295) throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations Perform a series of predefined actions such as quick create or log a
     * task
     * 
     * @param performQuickActions281
     * 
     * @param sessionHeader282
     * 
     * @param callOptions283
     * 
     * @param assignmentRuleHeader284
     * 
     * @param mruHeader285
     * 
     * @param allowFieldTruncationHeader286
     * 
     * @param disableFeedTrackingHeader287
     * 
     * @param streamingEnabledHeader288
     * 
     * @param allOrNoneHeader289
     * 
     * @param duplicateRuleHeader290
     * 
     * @param localeOptions291
     * 
     * @param debuggingHeader292
     * 
     * @param packageVersionHeader293
     * 
     * @param emailHeader294
     * 
     * @param ownerChangeOptions295
     */
    public void startperformQuickActions(

    com.salesforce.soap.partner.PerformQuickActions performQuickActions281,
            com.salesforce.soap.partner.SessionHeader sessionHeader282, com.salesforce.soap.partner.CallOptions callOptions283,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader284,
            com.salesforce.soap.partner.MruHeader mruHeader285,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader286,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader287,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader288,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader289,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader290,
            com.salesforce.soap.partner.LocaleOptions localeOptions291,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader292,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader293,
            com.salesforce.soap.partner.EmailHeader emailHeader294,
            com.salesforce.soap.partner.OwnerChangeOptions ownerChangeOptions295,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Set a user's password
     * 
     * @param setPassword297
     * 
     * @param sessionHeader298
     * 
     * @param callOptions299
     * 
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     * @throws com.salesforce.soap.partner.InvalidNewPasswordFault :
     */

    public com.salesforce.soap.partner.SetPasswordResponse setPassword(

    com.salesforce.soap.partner.SetPassword setPassword297, com.salesforce.soap.partner.SessionHeader sessionHeader298,
            com.salesforce.soap.partner.CallOptions callOptions299) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.UnexpectedErrorFault,
            com.salesforce.soap.partner.InvalidNewPasswordFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Set a user's password
     * 
     * @param setPassword297
     * 
     * @param sessionHeader298
     * 
     * @param callOptions299
     */
    public void startsetPassword(

    com.salesforce.soap.partner.SetPassword setPassword297, com.salesforce.soap.partner.SessionHeader sessionHeader298,
            com.salesforce.soap.partner.CallOptions callOptions299,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Logout the current user, invalidating the current session.
     * 
     * @param logout301
     * 
     * @param sessionHeader302
     * 
     * @param callOptions303
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.LogoutResponse logout(

    com.salesforce.soap.partner.Logout logout301, com.salesforce.soap.partner.SessionHeader sessionHeader302,
            com.salesforce.soap.partner.CallOptions callOptions303) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Logout the current user, invalidating the current session.
     * 
     * @param logout301
     * 
     * @param sessionHeader302
     * 
     * @param callOptions303
     */
    public void startlogout(

    com.salesforce.soap.partner.Logout logout301, com.salesforce.soap.partner.SessionHeader sessionHeader302,
            com.salesforce.soap.partner.CallOptions callOptions303,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Get a set of sObjects
     * 
     * @param retrieve305
     * 
     * @param sessionHeader306
     * 
     * @param callOptions307
     * 
     * @param queryOptions308
     * 
     * @param mruHeader309
     * 
     * @param packageVersionHeader310
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.MalformedQueryFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.RetrieveResponse retrieve(

    com.salesforce.soap.partner.Retrieve retrieve305, com.salesforce.soap.partner.SessionHeader sessionHeader306,
            com.salesforce.soap.partner.CallOptions callOptions307, com.salesforce.soap.partner.QueryOptions queryOptions308,
            com.salesforce.soap.partner.MruHeader mruHeader309,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader310) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.MalformedQueryFault,
            com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.InvalidFieldFault,
            com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Get a set of sObjects
     * 
     * @param retrieve305
     * 
     * @param sessionHeader306
     * 
     * @param callOptions307
     * 
     * @param queryOptions308
     * 
     * @param mruHeader309
     * 
     * @param packageVersionHeader310
     */
    public void startretrieve(

    com.salesforce.soap.partner.Retrieve retrieve305, com.salesforce.soap.partner.SessionHeader sessionHeader306,
            com.salesforce.soap.partner.CallOptions callOptions307, com.salesforce.soap.partner.QueryOptions queryOptions308,
            com.salesforce.soap.partner.MruHeader mruHeader309,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader310,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Create a Query Cursor, including deleted sObjects
     * 
     * @param queryAll312
     * 
     * @param sessionHeader313
     * 
     * @param callOptions314
     * 
     * @param queryOptions315
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.MalformedQueryFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault :
     */

    public com.salesforce.soap.partner.QueryAllResponse queryAll(

    com.salesforce.soap.partner.QueryAll queryAll312, com.salesforce.soap.partner.SessionHeader sessionHeader313,
            com.salesforce.soap.partner.CallOptions callOptions314, com.salesforce.soap.partner.QueryOptions queryOptions315)
            throws java.rmi.RemoteException

            , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.MalformedQueryFault,
            com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.InvalidFieldFault,
            com.salesforce.soap.partner.UnexpectedErrorFault, com.salesforce.soap.partner.InvalidQueryLocatorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Create a Query Cursor, including deleted sObjects
     * 
     * @param queryAll312
     * 
     * @param sessionHeader313
     * 
     * @param callOptions314
     * 
     * @param queryOptions315
     */
    public void startqueryAll(

    com.salesforce.soap.partner.QueryAll queryAll312, com.salesforce.soap.partner.SessionHeader sessionHeader313,
            com.salesforce.soap.partner.CallOptions callOptions314, com.salesforce.soap.partner.QueryOptions queryOptions315,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Undelete a set of sObjects
     * 
     * @param undelete317
     * 
     * @param sessionHeader318
     * 
     * @param callOptions319
     * 
     * @param allowFieldTruncationHeader320
     * 
     * @param disableFeedTrackingHeader321
     * 
     * @param streamingEnabledHeader322
     * 
     * @param allOrNoneHeader323
     * 
     * @param duplicateRuleHeader324
     * 
     * @param localeOptions325
     * 
     * @param debuggingHeader326
     * 
     * @param packageVersionHeader327
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.UndeleteResponse undelete(

    com.salesforce.soap.partner.Undelete undelete317, com.salesforce.soap.partner.SessionHeader sessionHeader318,
            com.salesforce.soap.partner.CallOptions callOptions319,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader320,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader321,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader322,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader323,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader324,
            com.salesforce.soap.partner.LocaleOptions localeOptions325,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader326,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader327) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Undelete a set of sObjects
     * 
     * @param undelete317
     * 
     * @param sessionHeader318
     * 
     * @param callOptions319
     * 
     * @param allowFieldTruncationHeader320
     * 
     * @param disableFeedTrackingHeader321
     * 
     * @param streamingEnabledHeader322
     * 
     * @param allOrNoneHeader323
     * 
     * @param duplicateRuleHeader324
     * 
     * @param localeOptions325
     * 
     * @param debuggingHeader326
     * 
     * @param packageVersionHeader327
     */
    public void startundelete(

    com.salesforce.soap.partner.Undelete undelete317, com.salesforce.soap.partner.SessionHeader sessionHeader318,
            com.salesforce.soap.partner.CallOptions callOptions319,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader320,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader321,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader322,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader323,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader324,
            com.salesforce.soap.partner.LocaleOptions localeOptions325,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader326,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader327,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Get the IDs for updated sObjects
     * 
     * @param getUpdated329
     * 
     * @param sessionHeader330
     * 
     * @param callOptions331
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.GetUpdatedResponse getUpdated(

    com.salesforce.soap.partner.GetUpdated getUpdated329, com.salesforce.soap.partner.SessionHeader sessionHeader330,
            com.salesforce.soap.partner.CallOptions callOptions331) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Get the IDs for updated sObjects
     * 
     * @param getUpdated329
     * 
     * @param sessionHeader330
     * 
     * @param callOptions331
     */
    public void startgetUpdated(

    com.salesforce.soap.partner.GetUpdated getUpdated329, com.salesforce.soap.partner.SessionHeader sessionHeader330,
            com.salesforce.soap.partner.CallOptions callOptions331,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Create a set of new sObjects
     * 
     * @param create333
     * 
     * @param sessionHeader334
     * 
     * @param callOptions335
     * 
     * @param assignmentRuleHeader336
     * 
     * @param mruHeader337
     * 
     * @param allowFieldTruncationHeader338
     * 
     * @param disableFeedTrackingHeader339
     * 
     * @param streamingEnabledHeader340
     * 
     * @param allOrNoneHeader341
     * 
     * @param duplicateRuleHeader342
     * 
     * @param localeOptions343
     * 
     * @param debuggingHeader344
     * 
     * @param packageVersionHeader345
     * 
     * @param emailHeader346
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.CreateResponse create(

    com.salesforce.soap.partner.Create create333, com.salesforce.soap.partner.SessionHeader sessionHeader334,
            com.salesforce.soap.partner.CallOptions callOptions335,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader336,
            com.salesforce.soap.partner.MruHeader mruHeader337,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader338,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader339,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader340,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader341,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader342,
            com.salesforce.soap.partner.LocaleOptions localeOptions343,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader344,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader345,
            com.salesforce.soap.partner.EmailHeader emailHeader346) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.InvalidIdFault,
            com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Create a set of new sObjects
     * 
     * @param create333
     * 
     * @param sessionHeader334
     * 
     * @param callOptions335
     * 
     * @param assignmentRuleHeader336
     * 
     * @param mruHeader337
     * 
     * @param allowFieldTruncationHeader338
     * 
     * @param disableFeedTrackingHeader339
     * 
     * @param streamingEnabledHeader340
     * 
     * @param allOrNoneHeader341
     * 
     * @param duplicateRuleHeader342
     * 
     * @param localeOptions343
     * 
     * @param debuggingHeader344
     * 
     * @param packageVersionHeader345
     * 
     * @param emailHeader346
     */
    public void startcreate(

    com.salesforce.soap.partner.Create create333, com.salesforce.soap.partner.SessionHeader sessionHeader334,
            com.salesforce.soap.partner.CallOptions callOptions335,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader336,
            com.salesforce.soap.partner.MruHeader mruHeader337,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader338,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader339,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader340,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader341,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader342,
            com.salesforce.soap.partner.LocaleOptions localeOptions343,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader344,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader345,
            com.salesforce.soap.partner.EmailHeader emailHeader346,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the details of a series of quick actions available for the given contextType
     * 
     * @param describeAvailableQuickActions348
     * 
     * @param sessionHeader349
     * 
     * @param callOptions350
     * 
     * @param packageVersionHeader351
     * 
     * @param localeOptions352
     */

    public com.salesforce.soap.partner.DescribeAvailableQuickActionsResponse describeAvailableQuickActions(

    com.salesforce.soap.partner.DescribeAvailableQuickActions describeAvailableQuickActions348,
            com.salesforce.soap.partner.SessionHeader sessionHeader349, com.salesforce.soap.partner.CallOptions callOptions350,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader351,
            com.salesforce.soap.partner.LocaleOptions localeOptions352) throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the details of a series of quick actions available for the
     * given contextType
     * 
     * @param describeAvailableQuickActions348
     * 
     * @param sessionHeader349
     * 
     * @param callOptions350
     * 
     * @param packageVersionHeader351
     * 
     * @param localeOptions352
     */
    public void startdescribeAvailableQuickActions(

    com.salesforce.soap.partner.DescribeAvailableQuickActions describeAvailableQuickActions348,
            com.salesforce.soap.partner.SessionHeader sessionHeader349, com.salesforce.soap.partner.CallOptions callOptions350,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader351,
            com.salesforce.soap.partner.LocaleOptions localeOptions352,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Send outbound email
     * 
     * @param sendEmail354
     * 
     * @param sessionHeader355
     * 
     * @param callOptions356
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.SendEmailResponse sendEmail(

    com.salesforce.soap.partner.SendEmail sendEmail354, com.salesforce.soap.partner.SessionHeader sessionHeader355,
            com.salesforce.soap.partner.CallOptions callOptions356) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Send outbound email
     * 
     * @param sendEmail354
     * 
     * @param sessionHeader355
     * 
     * @param callOptions356
     */
    public void startsendEmail(

    com.salesforce.soap.partner.SendEmail sendEmail354, com.salesforce.soap.partner.SessionHeader sessionHeader355,
            com.salesforce.soap.partner.CallOptions callOptions356,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Search for sObjects
     * 
     * @param search358
     * 
     * @param sessionHeader359
     * 
     * @param callOptions360
     * 
     * @param packageVersionHeader361
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.MalformedSearchFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.SearchResponse search(

    com.salesforce.soap.partner.Search search358, com.salesforce.soap.partner.SessionHeader sessionHeader359,
            com.salesforce.soap.partner.CallOptions callOptions360,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader361) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.MalformedSearchFault,
            com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Search for sObjects
     * 
     * @param search358
     * 
     * @param sessionHeader359
     * 
     * @param callOptions360
     * 
     * @param packageVersionHeader361
     */
    public void startsearch(

    com.salesforce.soap.partner.Search search358, com.salesforce.soap.partner.SessionHeader sessionHeader359,
            com.salesforce.soap.partner.CallOptions callOptions360,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader361,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Return the renameable nouns from the server for use in presentation using the salesforce grammar
     * engine
     * 
     * @param describeNouns363
     * 
     * @param sessionHeader364
     * 
     * @param callOptions365
     * 
     * @param packageVersionHeader366
     * 
     * @param localeOptions367
     */

    public com.salesforce.soap.partner.DescribeNounsResponse describeNouns(

    com.salesforce.soap.partner.DescribeNouns describeNouns363, com.salesforce.soap.partner.SessionHeader sessionHeader364,
            com.salesforce.soap.partner.CallOptions callOptions365,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader366,
            com.salesforce.soap.partner.LocaleOptions localeOptions367) throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations Return the renameable nouns from the server for use in presentation
     * using the salesforce grammar engine
     * 
     * @param describeNouns363
     * 
     * @param sessionHeader364
     * 
     * @param callOptions365
     * 
     * @param packageVersionHeader366
     * 
     * @param localeOptions367
     */
    public void startdescribeNouns(

    com.salesforce.soap.partner.DescribeNouns describeNouns363, com.salesforce.soap.partner.SessionHeader sessionHeader364,
            com.salesforce.soap.partner.CallOptions callOptions365,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader366,
            com.salesforce.soap.partner.LocaleOptions localeOptions367,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Create a Query Cursor
     * 
     * @param query369
     * 
     * @param sessionHeader370
     * 
     * @param callOptions371
     * 
     * @param queryOptions372
     * 
     * @param mruHeader373
     * 
     * @param packageVersionHeader374
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.MalformedQueryFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault :
     */

    public com.salesforce.soap.partner.QueryResponse query(

    com.salesforce.soap.partner.Query query369, com.salesforce.soap.partner.SessionHeader sessionHeader370,
            com.salesforce.soap.partner.CallOptions callOptions371, com.salesforce.soap.partner.QueryOptions queryOptions372,
            com.salesforce.soap.partner.MruHeader mruHeader373,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader374) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.MalformedQueryFault,
            com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.InvalidFieldFault,
            com.salesforce.soap.partner.UnexpectedErrorFault, com.salesforce.soap.partner.InvalidQueryLocatorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Create a Query Cursor
     * 
     * @param query369
     * 
     * @param sessionHeader370
     * 
     * @param callOptions371
     * 
     * @param queryOptions372
     * 
     * @param mruHeader373
     * 
     * @param packageVersionHeader374
     */
    public void startquery(

    com.salesforce.soap.partner.Query query369, com.salesforce.soap.partner.SessionHeader sessionHeader370,
            com.salesforce.soap.partner.CallOptions callOptions371, com.salesforce.soap.partner.QueryOptions queryOptions372,
            com.salesforce.soap.partner.MruHeader mruHeader373,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader374,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe Themes
     * 
     * @param describeTheme376
     * 
     * @param sessionHeader377
     * 
     * @param callOptions378
     * 
     * @param packageVersionHeader379
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeThemeResponse describeTheme(

    com.salesforce.soap.partner.DescribeTheme describeTheme376, com.salesforce.soap.partner.SessionHeader sessionHeader377,
            com.salesforce.soap.partner.CallOptions callOptions378,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader379) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe Themes
     * 
     * @param describeTheme376
     * 
     * @param sessionHeader377
     * 
     * @param callOptions378
     * 
     * @param packageVersionHeader379
     */
    public void startdescribeTheme(

    com.salesforce.soap.partner.DescribeTheme describeTheme376, com.salesforce.soap.partner.SessionHeader sessionHeader377,
            com.salesforce.soap.partner.CallOptions callOptions378,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader379,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the ListViews of a SObject as SOQL metadata for the generation of SOQL.
     * 
     * @param describeSObjectListViews381
     * 
     * @param sessionHeader382
     * 
     * @param callOptions383
     * 
     * @param packageVersionHeader384
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeSObjectListViewsResponse describeSObjectListViews(

    com.salesforce.soap.partner.DescribeSObjectListViews describeSObjectListViews381,
            com.salesforce.soap.partner.SessionHeader sessionHeader382, com.salesforce.soap.partner.CallOptions callOptions383,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader384) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the ListViews of a SObject as SOQL metadata for the generation
     * of SOQL.
     * 
     * @param describeSObjectListViews381
     * 
     * @param sessionHeader382
     * 
     * @param callOptions383
     * 
     * @param packageVersionHeader384
     */
    public void startdescribeSObjectListViews(

    com.salesforce.soap.partner.DescribeSObjectListViews describeSObjectListViews381,
            com.salesforce.soap.partner.SessionHeader sessionHeader382, com.salesforce.soap.partner.CallOptions callOptions383,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader384,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Get the IDs for deleted sObjects
     * 
     * @param getDeleted386
     * 
     * @param sessionHeader387
     * 
     * @param callOptions388
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.GetDeletedResponse getDeleted(

    com.salesforce.soap.partner.GetDeleted getDeleted386, com.salesforce.soap.partner.SessionHeader sessionHeader387,
            com.salesforce.soap.partner.CallOptions callOptions388) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Get the IDs for deleted sObjects
     * 
     * @param getDeleted386
     * 
     * @param sessionHeader387
     * 
     * @param callOptions388
     */
    public void startgetDeleted(

    com.salesforce.soap.partner.GetDeleted getDeleted386, com.salesforce.soap.partner.SessionHeader sessionHeader387,
            com.salesforce.soap.partner.CallOptions callOptions388,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe a list of FlexiPage and their contents
     * 
     * @param describeFlexiPages390
     * 
     * @param sessionHeader391
     * 
     * @param callOptions392
     * 
     * @param packageVersionHeader393
     * 
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeFlexiPagesResponse describeFlexiPages(

    com.salesforce.soap.partner.DescribeFlexiPages describeFlexiPages390,
            com.salesforce.soap.partner.SessionHeader sessionHeader391, com.salesforce.soap.partner.CallOptions callOptions392,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader393) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe a list of FlexiPage and their contents
     * 
     * @param describeFlexiPages390
     * 
     * @param sessionHeader391
     * 
     * @param callOptions392
     * 
     * @param packageVersionHeader393
     */
    public void startdescribeFlexiPages(

    com.salesforce.soap.partner.DescribeFlexiPages describeFlexiPages390,
            com.salesforce.soap.partner.SessionHeader sessionHeader391, com.salesforce.soap.partner.CallOptions callOptions392,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader393,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe a list of objects representing the order and scope of objects on a users search result page
     * 
     * @param describeSearchScopeOrder395
     * 
     * @param sessionHeader396
     * 
     * @param callOptions397
     * 
     * @param packageVersionHeader398
     */

    public com.salesforce.soap.partner.DescribeSearchScopeOrderResponse describeSearchScopeOrder(

    com.salesforce.soap.partner.DescribeSearchScopeOrder describeSearchScopeOrder395,
            com.salesforce.soap.partner.SessionHeader sessionHeader396, com.salesforce.soap.partner.CallOptions callOptions397,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader398) throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe a list of objects representing the order and scope of objects
     * on a users search result page
     * 
     * @param describeSearchScopeOrder395
     * 
     * @param sessionHeader396
     * 
     * @param callOptions397
     * 
     * @param packageVersionHeader398
     */
    public void startdescribeSearchScopeOrder(

    com.salesforce.soap.partner.DescribeSearchScopeOrder describeSearchScopeOrder395,
            com.salesforce.soap.partner.SessionHeader sessionHeader396, com.salesforce.soap.partner.CallOptions callOptions397,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader398,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the ListViews as SOQL metadata for the generation of SOQL.
     * 
     * @param describeSoqlListViews400
     * 
     * @param sessionHeader401
     * 
     * @param callOptions402
     * 
     * @param packageVersionHeader403
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeSoqlListViewsResponse describeSoqlListViews(

    com.salesforce.soap.partner.DescribeSoqlListViews describeSoqlListViews400,
            com.salesforce.soap.partner.SessionHeader sessionHeader401, com.salesforce.soap.partner.CallOptions callOptions402,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader403) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the ListViews as SOQL metadata for the generation of SOQL.
     * 
     * @param describeSoqlListViews400
     * 
     * @param sessionHeader401
     * 
     * @param callOptions402
     * 
     * @param packageVersionHeader403
     */
    public void startdescribeSoqlListViews(

    com.salesforce.soap.partner.DescribeSoqlListViews describeSoqlListViews400,
            com.salesforce.soap.partner.SessionHeader sessionHeader401, com.salesforce.soap.partner.CallOptions callOptions402,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader403,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the search view of an sObject
     * 
     * @param describeSearchLayouts405
     * 
     * @param sessionHeader406
     * 
     * @param callOptions407
     * 
     * @param packageVersionHeader408
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeSearchLayoutsResponse describeSearchLayouts(

    com.salesforce.soap.partner.DescribeSearchLayouts describeSearchLayouts405,
            com.salesforce.soap.partner.SessionHeader sessionHeader406, com.salesforce.soap.partner.CallOptions callOptions407,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader408) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the search view of an sObject
     * 
     * @param describeSearchLayouts405
     * 
     * @param sessionHeader406
     * 
     * @param callOptions407
     * 
     * @param packageVersionHeader408
     */
    public void startdescribeSearchLayouts(

    com.salesforce.soap.partner.DescribeSearchLayouts describeSearchLayouts405,
            com.salesforce.soap.partner.SessionHeader sessionHeader406, com.salesforce.soap.partner.CallOptions callOptions407,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader408,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Submit an entity to a workflow process or process a workitem
     * 
     * @param process410
     * 
     * @param sessionHeader411
     * 
     * @param callOptions412
     * 
     * @param allowFieldTruncationHeader413
     * 
     * @param disableFeedTrackingHeader414
     * 
     * @param streamingEnabledHeader415
     * 
     * @param duplicateRuleHeader416
     * 
     * @param localeOptions417
     * 
     * @param debuggingHeader418
     * 
     * @param packageVersionHeader419
     * 
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.ProcessResponse process(

    com.salesforce.soap.partner.Process process410, com.salesforce.soap.partner.SessionHeader sessionHeader411,
            com.salesforce.soap.partner.CallOptions callOptions412,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader413,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader414,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader415,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader416,
            com.salesforce.soap.partner.LocaleOptions localeOptions417,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader418,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader419) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Submit an entity to a workflow process or process a workitem
     * 
     * @param process410
     * 
     * @param sessionHeader411
     * 
     * @param callOptions412
     * 
     * @param allowFieldTruncationHeader413
     * 
     * @param disableFeedTrackingHeader414
     * 
     * @param streamingEnabledHeader415
     * 
     * @param duplicateRuleHeader416
     * 
     * @param localeOptions417
     * 
     * @param debuggingHeader418
     * 
     * @param packageVersionHeader419
     */
    public void startprocess(

    com.salesforce.soap.partner.Process process410, com.salesforce.soap.partner.SessionHeader sessionHeader411,
            com.salesforce.soap.partner.CallOptions callOptions412,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader413,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader414,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader415,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader416,
            com.salesforce.soap.partner.LocaleOptions localeOptions417,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader418,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader419,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the data category group structures for a given set of pair of types and data category group
     * name
     * 
     * @param describeDataCategoryGroupStructures421
     * 
     * @param sessionHeader422
     * 
     * @param callOptions423
     * 
     * @param packageVersionHeader424
     * 
     * @param localeOptions425
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse describeDataCategoryGroupStructures(

    com.salesforce.soap.partner.DescribeDataCategoryGroupStructures describeDataCategoryGroupStructures421,
            com.salesforce.soap.partner.SessionHeader sessionHeader422, com.salesforce.soap.partner.CallOptions callOptions423,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader424,
            com.salesforce.soap.partner.LocaleOptions localeOptions425) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the data category group structures for a given set of pair of
     * types and data category group name
     * 
     * @param describeDataCategoryGroupStructures421
     * 
     * @param sessionHeader422
     * 
     * @param callOptions423
     * 
     * @param packageVersionHeader424
     * 
     * @param localeOptions425
     */
    public void startdescribeDataCategoryGroupStructures(

    com.salesforce.soap.partner.DescribeDataCategoryGroupStructures describeDataCategoryGroupStructures421,
            com.salesforce.soap.partner.SessionHeader sessionHeader422, com.salesforce.soap.partner.CallOptions callOptions423,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader424,
            com.salesforce.soap.partner.LocaleOptions localeOptions425,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Reset a user's password
     * 
     * @param resetPassword427
     * 
     * @param sessionHeader428
     * 
     * @param callOptions429
     * 
     * @param emailHeader430
     * 
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.ResetPasswordResponse resetPassword(

    com.salesforce.soap.partner.ResetPassword resetPassword427, com.salesforce.soap.partner.SessionHeader sessionHeader428,
            com.salesforce.soap.partner.CallOptions callOptions429, com.salesforce.soap.partner.EmailHeader emailHeader430)
            throws java.rmi.RemoteException

            , com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Reset a user's password
     * 
     * @param resetPassword427
     * 
     * @param sessionHeader428
     * 
     * @param callOptions429
     * 
     * @param emailHeader430
     */
    public void startresetPassword(

    com.salesforce.soap.partner.ResetPassword resetPassword427, com.salesforce.soap.partner.SessionHeader sessionHeader428,
            com.salesforce.soap.partner.CallOptions callOptions429, com.salesforce.soap.partner.EmailHeader emailHeader430,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the Global state
     * 
     * @param describeGlobal432
     * 
     * @param sessionHeader433
     * 
     * @param callOptions434
     * 
     * @param packageVersionHeader435
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeGlobalResponse describeGlobal(

    com.salesforce.soap.partner.DescribeGlobal describeGlobal432, com.salesforce.soap.partner.SessionHeader sessionHeader433,
            com.salesforce.soap.partner.CallOptions callOptions434,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader435) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the Global state
     * 
     * @param describeGlobal432
     * 
     * @param sessionHeader433
     * 
     * @param callOptions434
     * 
     * @param packageVersionHeader435
     */
    public void startdescribeGlobal(

    com.salesforce.soap.partner.DescribeGlobal describeGlobal432, com.salesforce.soap.partner.SessionHeader sessionHeader433,
            com.salesforce.soap.partner.CallOptions callOptions434,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader435,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the items in an AppMenu
     * 
     * @param describeAppMenu437
     * 
     * @param sessionHeader438
     * 
     * @param callOptions439
     * 
     * @param packageVersionHeader440
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeAppMenuResponse describeAppMenu(

    com.salesforce.soap.partner.DescribeAppMenu describeAppMenu437, com.salesforce.soap.partner.SessionHeader sessionHeader438,
            com.salesforce.soap.partner.CallOptions callOptions439,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader440) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the items in an AppMenu
     * 
     * @param describeAppMenu437
     * 
     * @param sessionHeader438
     * 
     * @param callOptions439
     * 
     * @param packageVersionHeader440
     */
    public void startdescribeAppMenu(

    com.salesforce.soap.partner.DescribeAppMenu describeAppMenu437, com.salesforce.soap.partner.SessionHeader sessionHeader438,
            com.salesforce.soap.partner.CallOptions callOptions439,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader440,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the compact layouts of the given sObject
     * 
     * @param describeCompactLayouts442
     * 
     * @param sessionHeader443
     * 
     * @param callOptions444
     * 
     * @param packageVersionHeader445
     */

    public com.salesforce.soap.partner.DescribeCompactLayoutsResponse describeCompactLayouts(

    com.salesforce.soap.partner.DescribeCompactLayouts describeCompactLayouts442,
            com.salesforce.soap.partner.SessionHeader sessionHeader443, com.salesforce.soap.partner.CallOptions callOptions444,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader445) throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the compact layouts of the given sObject
     * 
     * @param describeCompactLayouts442
     * 
     * @param sessionHeader443
     * 
     * @param callOptions444
     * 
     * @param packageVersionHeader445
     */
    public void startdescribeCompactLayouts(

    com.salesforce.soap.partner.DescribeCompactLayouts describeCompactLayouts442,
            com.salesforce.soap.partner.SessionHeader sessionHeader443, com.salesforce.soap.partner.CallOptions callOptions444,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader445,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the approval layouts of the given sObject
     * 
     * @param describeApprovalLayout447
     * 
     * @param sessionHeader448
     * 
     * @param callOptions449
     * 
     * @param packageVersionHeader450
     */

    public com.salesforce.soap.partner.DescribeApprovalLayoutResponse describeApprovalLayout(

    com.salesforce.soap.partner.DescribeApprovalLayoutE describeApprovalLayout447,
            com.salesforce.soap.partner.SessionHeader sessionHeader448, com.salesforce.soap.partner.CallOptions callOptions449,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader450) throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the approval layouts of the given sObject
     * 
     * @param describeApprovalLayout447
     * 
     * @param sessionHeader448
     * 
     * @param callOptions449
     * 
     * @param packageVersionHeader450
     */
    public void startdescribeApprovalLayout(

    com.salesforce.soap.partner.DescribeApprovalLayoutE describeApprovalLayout447,
            com.salesforce.soap.partner.SessionHeader sessionHeader448, com.salesforce.soap.partner.CallOptions callOptions449,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader450,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Send existing draft EmailMessage
     * 
     * @param sendEmailMessage452
     * 
     * @param sessionHeader453
     * 
     * @param callOptions454
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.SendEmailMessageResponse sendEmailMessage(

    com.salesforce.soap.partner.SendEmailMessage sendEmailMessage452, com.salesforce.soap.partner.SessionHeader sessionHeader453,
            com.salesforce.soap.partner.CallOptions callOptions454) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Send existing draft EmailMessage
     * 
     * @param sendEmailMessage452
     * 
     * @param sessionHeader453
     * 
     * @param callOptions454
     */
    public void startsendEmailMessage(

    com.salesforce.soap.partner.SendEmailMessage sendEmailMessage452, com.salesforce.soap.partner.SessionHeader sessionHeader453,
            com.salesforce.soap.partner.CallOptions callOptions454,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the layout of the given sObject or the given actionable global page.
     * 
     * @param describeLayout456
     * 
     * @param sessionHeader457
     * 
     * @param callOptions458
     * 
     * @param packageVersionHeader459
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeLayoutResponse describeLayout(

    com.salesforce.soap.partner.DescribeLayoutE describeLayout456, com.salesforce.soap.partner.SessionHeader sessionHeader457,
            com.salesforce.soap.partner.CallOptions callOptions458,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader459) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.InvalidIdFault,
            com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the layout of the given sObject or the given actionable global
     * page.
     * 
     * @param describeLayout456
     * 
     * @param sessionHeader457
     * 
     * @param callOptions458
     * 
     * @param packageVersionHeader459
     */
    public void startdescribeLayout(

    com.salesforce.soap.partner.DescribeLayoutE describeLayout456, com.salesforce.soap.partner.SessionHeader sessionHeader457,
            com.salesforce.soap.partner.CallOptions callOptions458,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader459,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the tabs that appear on a users page
     * 
     * @param describeTabs461
     * 
     * @param sessionHeader462
     * 
     * @param callOptions463
     * 
     * @param packageVersionHeader464
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeTabsResponse describeTabs(

    com.salesforce.soap.partner.DescribeTabs describeTabs461, com.salesforce.soap.partner.SessionHeader sessionHeader462,
            com.salesforce.soap.partner.CallOptions callOptions463,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader464) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the tabs that appear on a users page
     * 
     * @param describeTabs461
     * 
     * @param sessionHeader462
     * 
     * @param callOptions463
     * 
     * @param packageVersionHeader464
     */
    public void startdescribeTabs(

    com.salesforce.soap.partner.DescribeTabs describeTabs461, com.salesforce.soap.partner.SessionHeader sessionHeader462,
            com.salesforce.soap.partner.CallOptions callOptions463,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader464,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describes your Knowledge settings, such as if knowledgeEnabled is on or off, its default language and
     * supported languages
     * 
     * @param describeKnowledgeSettings466
     * 
     * @param sessionHeader467
     * 
     * @param callOptions468
     * 
     * @param packageVersionHeader469
     * 
     * @param localeOptions470
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeKnowledgeSettingsResponse describeKnowledgeSettings(

    com.salesforce.soap.partner.DescribeKnowledgeSettings describeKnowledgeSettings466,
            com.salesforce.soap.partner.SessionHeader sessionHeader467, com.salesforce.soap.partner.CallOptions callOptions468,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader469,
            com.salesforce.soap.partner.LocaleOptions localeOptions470) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describes your Knowledge settings, such as if knowledgeEnabled is on or
     * off, its default language and supported languages
     * 
     * @param describeKnowledgeSettings466
     * 
     * @param sessionHeader467
     * 
     * @param callOptions468
     * 
     * @param packageVersionHeader469
     * 
     * @param localeOptions470
     */
    public void startdescribeKnowledgeSettings(

    com.salesforce.soap.partner.DescribeKnowledgeSettings describeKnowledgeSettings466,
            com.salesforce.soap.partner.SessionHeader sessionHeader467, com.salesforce.soap.partner.CallOptions callOptions468,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader469,
            com.salesforce.soap.partner.LocaleOptions localeOptions470,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe all the data category groups available for a given set of types
     * 
     * @param describeDataCategoryGroups472
     * 
     * @param sessionHeader473
     * 
     * @param callOptions474
     * 
     * @param packageVersionHeader475
     * 
     * @param localeOptions476
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse describeDataCategoryGroups(

    com.salesforce.soap.partner.DescribeDataCategoryGroups describeDataCategoryGroups472,
            com.salesforce.soap.partner.SessionHeader sessionHeader473, com.salesforce.soap.partner.CallOptions callOptions474,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader475,
            com.salesforce.soap.partner.LocaleOptions localeOptions476) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe all the data category groups available for a given set of types
     * 
     * @param describeDataCategoryGroups472
     * 
     * @param sessionHeader473
     * 
     * @param callOptions474
     * 
     * @param packageVersionHeader475
     * 
     * @param localeOptions476
     */
    public void startdescribeDataCategoryGroups(

    com.salesforce.soap.partner.DescribeDataCategoryGroups describeDataCategoryGroups472,
            com.salesforce.soap.partner.SessionHeader sessionHeader473, com.salesforce.soap.partner.CallOptions callOptions474,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader475,
            com.salesforce.soap.partner.LocaleOptions localeOptions476,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Gets server timestamp
     * 
     * @param getServerTimestamp478
     * 
     * @param sessionHeader479
     * 
     * @param callOptions480
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.GetServerTimestampResponse getServerTimestamp(

    com.salesforce.soap.partner.GetServerTimestamp getServerTimestamp478,
            com.salesforce.soap.partner.SessionHeader sessionHeader479, com.salesforce.soap.partner.CallOptions callOptions480)
            throws java.rmi.RemoteException

            , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Gets server timestamp
     * 
     * @param getServerTimestamp478
     * 
     * @param sessionHeader479
     * 
     * @param callOptions480
     */
    public void startgetServerTimestamp(

    com.salesforce.soap.partner.GetServerTimestamp getServerTimestamp478,
            com.salesforce.soap.partner.SessionHeader sessionHeader479, com.salesforce.soap.partner.CallOptions callOptions480,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Logs out and invalidates session ids
     * 
     * @param invalidateSessions482
     * 
     * @param sessionHeader483
     * 
     * @param callOptions484
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.InvalidateSessionsResponse invalidateSessions(

    com.salesforce.soap.partner.InvalidateSessions invalidateSessions482,
            com.salesforce.soap.partner.SessionHeader sessionHeader483, com.salesforce.soap.partner.CallOptions callOptions484)
            throws java.rmi.RemoteException

            , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Logs out and invalidates session ids
     * 
     * @param invalidateSessions482
     * 
     * @param sessionHeader483
     * 
     * @param callOptions484
     */
    public void startinvalidateSessions(

    com.salesforce.soap.partner.InvalidateSessions invalidateSessions482,
            com.salesforce.soap.partner.SessionHeader sessionHeader483, com.salesforce.soap.partner.CallOptions callOptions484,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe an sObject
     * 
     * @param describeSObject486
     * 
     * @param sessionHeader487
     * 
     * @param callOptions488
     * 
     * @param packageVersionHeader489
     * 
     * @param localeOptions490
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeSObjectResponse describeSObject(

    com.salesforce.soap.partner.DescribeSObject describeSObject486, com.salesforce.soap.partner.SessionHeader sessionHeader487,
            com.salesforce.soap.partner.CallOptions callOptions488,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader489,
            com.salesforce.soap.partner.LocaleOptions localeOptions490) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe an sObject
     * 
     * @param describeSObject486
     * 
     * @param sessionHeader487
     * 
     * @param callOptions488
     * 
     * @param packageVersionHeader489
     * 
     * @param localeOptions490
     */
    public void startdescribeSObject(

    com.salesforce.soap.partner.DescribeSObject describeSObject486, com.salesforce.soap.partner.SessionHeader sessionHeader487,
            com.salesforce.soap.partner.CallOptions callOptions488,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader489,
            com.salesforce.soap.partner.LocaleOptions localeOptions490,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Login to the Salesforce.com SOAP Api
     * 
     * @param login492
     * 
     * @param loginScopeHeader493
     * 
     * @param callOptions494
     * 
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     * @throws com.salesforce.soap.partner.LoginFault :
     */

    public com.salesforce.soap.partner.LoginResponse login(

    com.salesforce.soap.partner.Login login492, com.salesforce.soap.partner.LoginScopeHeader loginScopeHeader493,
            com.salesforce.soap.partner.CallOptions callOptions494) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.UnexpectedErrorFault,
            com.salesforce.soap.partner.LoginFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Login to the Salesforce.com SOAP Api
     * 
     * @param login492
     * 
     * @param loginScopeHeader493
     * 
     * @param callOptions494
     */
    public void startlogin(

    com.salesforce.soap.partner.Login login492, com.salesforce.soap.partner.LoginScopeHeader loginScopeHeader493,
            com.salesforce.soap.partner.CallOptions callOptions494,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Retreive the template sobjects, if appropriate, for the given quick action names in a given context
     * 
     * @param retrieveQuickActionTemplates496
     * 
     * @param sessionHeader497
     * 
     * @param callOptions498
     * 
     * @param packageVersionHeader499
     * 
     * @param localeOptions500
     */

    public com.salesforce.soap.partner.RetrieveQuickActionTemplatesResponse retrieveQuickActionTemplates(

    com.salesforce.soap.partner.RetrieveQuickActionTemplates retrieveQuickActionTemplates496,
            com.salesforce.soap.partner.SessionHeader sessionHeader497, com.salesforce.soap.partner.CallOptions callOptions498,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader499,
            com.salesforce.soap.partner.LocaleOptions localeOptions500) throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations Retreive the template sobjects, if appropriate, for the given quick
     * action names in a given context
     * 
     * @param retrieveQuickActionTemplates496
     * 
     * @param sessionHeader497
     * 
     * @param callOptions498
     * 
     * @param packageVersionHeader499
     * 
     * @param localeOptions500
     */
    public void startretrieveQuickActionTemplates(

    com.salesforce.soap.partner.RetrieveQuickActionTemplates retrieveQuickActionTemplates496,
            com.salesforce.soap.partner.SessionHeader sessionHeader497, com.salesforce.soap.partner.CallOptions callOptions498,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader499,
            com.salesforce.soap.partner.LocaleOptions localeOptions500,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Gets the next batch of sObjects from a query
     * 
     * @param queryMore502
     * 
     * @param sessionHeader503
     * 
     * @param callOptions504
     * 
     * @param queryOptions505
     * 
     * @throws com.salesforce.soap.partner.MalformedQueryFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault :
     */

    public com.salesforce.soap.partner.QueryMoreResponse queryMore(

    com.salesforce.soap.partner.QueryMore queryMore502, com.salesforce.soap.partner.SessionHeader sessionHeader503,
            com.salesforce.soap.partner.CallOptions callOptions504, com.salesforce.soap.partner.QueryOptions queryOptions505)
            throws java.rmi.RemoteException

            , com.salesforce.soap.partner.MalformedQueryFault, com.salesforce.soap.partner.InvalidFieldFault,
            com.salesforce.soap.partner.UnexpectedErrorFault, com.salesforce.soap.partner.InvalidQueryLocatorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Gets the next batch of sObjects from a query
     * 
     * @param queryMore502
     * 
     * @param sessionHeader503
     * 
     * @param callOptions504
     * 
     * @param queryOptions505
     */
    public void startqueryMore(

    com.salesforce.soap.partner.QueryMore queryMore502, com.salesforce.soap.partner.SessionHeader sessionHeader503,
            com.salesforce.soap.partner.CallOptions callOptions504, com.salesforce.soap.partner.QueryOptions queryOptions505,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe a number sObjects
     * 
     * @param describeSObjects507
     * 
     * @param sessionHeader508
     * 
     * @param callOptions509
     * 
     * @param packageVersionHeader510
     * 
     * @param localeOptions511
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeSObjectsResponse describeSObjects(

    com.salesforce.soap.partner.DescribeSObjects describeSObjects507, com.salesforce.soap.partner.SessionHeader sessionHeader508,
            com.salesforce.soap.partner.CallOptions callOptions509,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader510,
            com.salesforce.soap.partner.LocaleOptions localeOptions511) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe a number sObjects
     * 
     * @param describeSObjects507
     * 
     * @param sessionHeader508
     * 
     * @param callOptions509
     * 
     * @param packageVersionHeader510
     * 
     * @param localeOptions511
     */
    public void startdescribeSObjects(

    com.salesforce.soap.partner.DescribeSObjects describeSObjects507, com.salesforce.soap.partner.SessionHeader sessionHeader508,
            com.salesforce.soap.partner.CallOptions callOptions509,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader510,
            com.salesforce.soap.partner.LocaleOptions localeOptions511,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Empty a set of sObjects from the recycle bin
     * 
     * @param emptyRecycleBin513
     * 
     * @param sessionHeader514
     * 
     * @param callOptions515
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.EmptyRecycleBinResponse emptyRecycleBin(

    com.salesforce.soap.partner.EmptyRecycleBin emptyRecycleBin513, com.salesforce.soap.partner.SessionHeader sessionHeader514,
            com.salesforce.soap.partner.CallOptions callOptions515) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Empty a set of sObjects from the recycle bin
     * 
     * @param emptyRecycleBin513
     * 
     * @param sessionHeader514
     * 
     * @param callOptions515
     */
    public void startemptyRecycleBin(

    com.salesforce.soap.partner.EmptyRecycleBin emptyRecycleBin513, com.salesforce.soap.partner.SessionHeader sessionHeader514,
            com.salesforce.soap.partner.CallOptions callOptions515,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Update or insert a set of sObjects based on object id
     * 
     * @param upsert517
     * 
     * @param sessionHeader518
     * 
     * @param callOptions519
     * 
     * @param assignmentRuleHeader520
     * 
     * @param mruHeader521
     * 
     * @param allowFieldTruncationHeader522
     * 
     * @param disableFeedTrackingHeader523
     * 
     * @param streamingEnabledHeader524
     * 
     * @param allOrNoneHeader525
     * 
     * @param duplicateRuleHeader526
     * 
     * @param localeOptions527
     * 
     * @param debuggingHeader528
     * 
     * @param packageVersionHeader529
     * 
     * @param emailHeader530
     * 
     * @param ownerChangeOptions531
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.UpsertResponse upsert(

    com.salesforce.soap.partner.Upsert upsert517, com.salesforce.soap.partner.SessionHeader sessionHeader518,
            com.salesforce.soap.partner.CallOptions callOptions519,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader520,
            com.salesforce.soap.partner.MruHeader mruHeader521,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader522,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader523,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader524,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader525,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader526,
            com.salesforce.soap.partner.LocaleOptions localeOptions527,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader528,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader529,
            com.salesforce.soap.partner.EmailHeader emailHeader530,
            com.salesforce.soap.partner.OwnerChangeOptions ownerChangeOptions531) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.InvalidIdFault,
            com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Update or insert a set of sObjects based on object id
     * 
     * @param upsert517
     * 
     * @param sessionHeader518
     * 
     * @param callOptions519
     * 
     * @param assignmentRuleHeader520
     * 
     * @param mruHeader521
     * 
     * @param allowFieldTruncationHeader522
     * 
     * @param disableFeedTrackingHeader523
     * 
     * @param streamingEnabledHeader524
     * 
     * @param allOrNoneHeader525
     * 
     * @param duplicateRuleHeader526
     * 
     * @param localeOptions527
     * 
     * @param debuggingHeader528
     * 
     * @param packageVersionHeader529
     * 
     * @param emailHeader530
     * 
     * @param ownerChangeOptions531
     */
    public void startupsert(

    com.salesforce.soap.partner.Upsert upsert517, com.salesforce.soap.partner.SessionHeader sessionHeader518,
            com.salesforce.soap.partner.CallOptions callOptions519,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader520,
            com.salesforce.soap.partner.MruHeader mruHeader521,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader522,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader523,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader524,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader525,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader526,
            com.salesforce.soap.partner.LocaleOptions localeOptions527,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader528,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader529,
            com.salesforce.soap.partner.EmailHeader emailHeader530,
            com.salesforce.soap.partner.OwnerChangeOptions ownerChangeOptions531,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature convert a set of leads
     * 
     * @param convertLead533
     * 
     * @param sessionHeader534
     * 
     * @param callOptions535
     * 
     * @param allowFieldTruncationHeader536
     * 
     * @param disableFeedTrackingHeader537
     * 
     * @param streamingEnabledHeader538
     * 
     * @param duplicateRuleHeader539
     * 
     * @param localeOptions540
     * 
     * @param debuggingHeader541
     * 
     * @param packageVersionHeader542
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.ConvertLeadResponse convertLead(

    com.salesforce.soap.partner.ConvertLead convertLead533, com.salesforce.soap.partner.SessionHeader sessionHeader534,
            com.salesforce.soap.partner.CallOptions callOptions535,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader536,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader537,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader538,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader539,
            com.salesforce.soap.partner.LocaleOptions localeOptions540,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader541,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader542) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations convert a set of leads
     * 
     * @param convertLead533
     * 
     * @param sessionHeader534
     * 
     * @param callOptions535
     * 
     * @param allowFieldTruncationHeader536
     * 
     * @param disableFeedTrackingHeader537
     * 
     * @param streamingEnabledHeader538
     * 
     * @param duplicateRuleHeader539
     * 
     * @param localeOptions540
     * 
     * @param debuggingHeader541
     * 
     * @param packageVersionHeader542
     */
    public void startconvertLead(

    com.salesforce.soap.partner.ConvertLead convertLead533, com.salesforce.soap.partner.SessionHeader sessionHeader534,
            com.salesforce.soap.partner.CallOptions callOptions535,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader536,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader537,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader538,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader539,
            com.salesforce.soap.partner.LocaleOptions localeOptions540,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader541,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader542,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Delete a set of sObjects
     * 
     * @param delete544
     * 
     * @param sessionHeader545
     * 
     * @param callOptions546
     * 
     * @param packageVersionHeader547
     * 
     * @param userTerritoryDeleteHeader548
     * 
     * @param emailHeader549
     * 
     * @param allowFieldTruncationHeader550
     * 
     * @param disableFeedTrackingHeader551
     * 
     * @param streamingEnabledHeader552
     * 
     * @param allOrNoneHeader553
     * 
     * @param duplicateRuleHeader554
     * 
     * @param localeOptions555
     * 
     * @param debuggingHeader556
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DeleteResponse delete(

    com.salesforce.soap.partner.Delete delete544, com.salesforce.soap.partner.SessionHeader sessionHeader545,
            com.salesforce.soap.partner.CallOptions callOptions546,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader547,
            com.salesforce.soap.partner.UserTerritoryDeleteHeader userTerritoryDeleteHeader548,
            com.salesforce.soap.partner.EmailHeader emailHeader549,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader550,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader551,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader552,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader553,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader554,
            com.salesforce.soap.partner.LocaleOptions localeOptions555,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader556) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Delete a set of sObjects
     * 
     * @param delete544
     * 
     * @param sessionHeader545
     * 
     * @param callOptions546
     * 
     * @param packageVersionHeader547
     * 
     * @param userTerritoryDeleteHeader548
     * 
     * @param emailHeader549
     * 
     * @param allowFieldTruncationHeader550
     * 
     * @param disableFeedTrackingHeader551
     * 
     * @param streamingEnabledHeader552
     * 
     * @param allOrNoneHeader553
     * 
     * @param duplicateRuleHeader554
     * 
     * @param localeOptions555
     * 
     * @param debuggingHeader556
     */
    public void startdelete(

    com.salesforce.soap.partner.Delete delete544, com.salesforce.soap.partner.SessionHeader sessionHeader545,
            com.salesforce.soap.partner.CallOptions callOptions546,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader547,
            com.salesforce.soap.partner.UserTerritoryDeleteHeader userTerritoryDeleteHeader548,
            com.salesforce.soap.partner.EmailHeader emailHeader549,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader550,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader551,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader552,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader553,
            com.salesforce.soap.partner.DuplicateRuleHeader duplicateRuleHeader554,
            com.salesforce.soap.partner.LocaleOptions localeOptions555,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader556,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe Gloal and Themes
     * 
     * @param describeGlobalTheme558
     * 
     * @param sessionHeader559
     * 
     * @param callOptions560
     * 
     * @param packageVersionHeader561
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeGlobalThemeResponse describeGlobalTheme(

    com.salesforce.soap.partner.DescribeGlobalThemeE describeGlobalTheme558,
            com.salesforce.soap.partner.SessionHeader sessionHeader559, com.salesforce.soap.partner.CallOptions callOptions560,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader561) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe Gloal and Themes
     * 
     * @param describeGlobalTheme558
     * 
     * @param sessionHeader559
     * 
     * @param callOptions560
     * 
     * @param packageVersionHeader561
     */
    public void startdescribeGlobalTheme(

    com.salesforce.soap.partner.DescribeGlobalThemeE describeGlobalTheme558,
            com.salesforce.soap.partner.SessionHeader sessionHeader559, com.salesforce.soap.partner.CallOptions callOptions560,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader561,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    //
}
