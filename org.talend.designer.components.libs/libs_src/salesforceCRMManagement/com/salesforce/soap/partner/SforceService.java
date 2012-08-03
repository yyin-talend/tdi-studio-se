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
     * @param merge128
     * 
     * @param sessionHeader129
     * 
     * @param callOptions130
     * 
     * @param assignmentRuleHeader131
     * 
     * @param mruHeader132
     * 
     * @param allowFieldTruncationHeader133
     * 
     * @param disableFeedTrackingHeader134
     * 
     * @param streamingEnabledHeader135
     * 
     * @param debuggingHeader136
     * 
     * @param packageVersionHeader137
     * 
     * @param emailHeader138
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.MergeResponse merge(

    com.salesforce.soap.partner.Merge merge128, com.salesforce.soap.partner.SessionHeader sessionHeader129,
            com.salesforce.soap.partner.CallOptions callOptions130,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader131,
            com.salesforce.soap.partner.MruHeader mruHeader132,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader133,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader134,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader135,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader136,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader137,
            com.salesforce.soap.partner.EmailHeader emailHeader138) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.InvalidIdFault,
            com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Merge and update a set of sObjects based on object
     * id
     * 
     * @param merge128
     * 
     * @param sessionHeader129
     * 
     * @param callOptions130
     * 
     * @param assignmentRuleHeader131
     * 
     * @param mruHeader132
     * 
     * @param allowFieldTruncationHeader133
     * 
     * @param disableFeedTrackingHeader134
     * 
     * @param streamingEnabledHeader135
     * 
     * @param debuggingHeader136
     * 
     * @param packageVersionHeader137
     * 
     * @param emailHeader138
     */
    public void startmerge(

    com.salesforce.soap.partner.Merge merge128, com.salesforce.soap.partner.SessionHeader sessionHeader129,
            com.salesforce.soap.partner.CallOptions callOptions130,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader131,
            com.salesforce.soap.partner.MruHeader mruHeader132,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader133,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader134,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader135,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader136,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader137,
            com.salesforce.soap.partner.EmailHeader emailHeader138,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Returns standard information relevant to the current user
     * 
     * @param getUserInfo140
     * 
     * @param sessionHeader141
     * 
     * @param callOptions142
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.GetUserInfoResponse getUserInfo(

    com.salesforce.soap.partner.GetUserInfo getUserInfo140, com.salesforce.soap.partner.SessionHeader sessionHeader141,
            com.salesforce.soap.partner.CallOptions callOptions142) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Returns standard information relevant to the current
     * user
     * 
     * @param getUserInfo140
     * 
     * @param sessionHeader141
     * 
     * @param callOptions142
     */
    public void startgetUserInfo(

    com.salesforce.soap.partner.GetUserInfo getUserInfo140, com.salesforce.soap.partner.SessionHeader sessionHeader141,
            com.salesforce.soap.partner.CallOptions callOptions142,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the layout of the SoftPhone
     * 
     * @param describeSoftphoneLayout144
     * 
     * @param sessionHeader145
     * 
     * @param callOptions146
     * 
     * @param packageVersionHeader147
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse describeSoftphoneLayout(

    com.salesforce.soap.partner.DescribeSoftphoneLayout describeSoftphoneLayout144,
            com.salesforce.soap.partner.SessionHeader sessionHeader145, com.salesforce.soap.partner.CallOptions callOptions146,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader147) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the layout of the SoftPhone
     * 
     * @param describeSoftphoneLayout144
     * 
     * @param sessionHeader145
     * 
     * @param callOptions146
     * 
     * @param packageVersionHeader147
     */
    public void startdescribeSoftphoneLayout(

    com.salesforce.soap.partner.DescribeSoftphoneLayout describeSoftphoneLayout144,
            com.salesforce.soap.partner.SessionHeader sessionHeader145, com.salesforce.soap.partner.CallOptions callOptions146,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader147,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Update a set of sObjects
     * 
     * @param update149
     * 
     * @param sessionHeader150
     * 
     * @param callOptions151
     * 
     * @param assignmentRuleHeader152
     * 
     * @param mruHeader153
     * 
     * @param allowFieldTruncationHeader154
     * 
     * @param disableFeedTrackingHeader155
     * 
     * @param streamingEnabledHeader156
     * 
     * @param allOrNoneHeader157
     * 
     * @param debuggingHeader158
     * 
     * @param packageVersionHeader159
     * 
     * @param emailHeader160
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.UpdateResponse update(

    com.salesforce.soap.partner.Update update149, com.salesforce.soap.partner.SessionHeader sessionHeader150,
            com.salesforce.soap.partner.CallOptions callOptions151,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader152,
            com.salesforce.soap.partner.MruHeader mruHeader153,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader154,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader155,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader156,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader157,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader158,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader159,
            com.salesforce.soap.partner.EmailHeader emailHeader160) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.InvalidIdFault,
            com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Update a set of sObjects
     * 
     * @param update149
     * 
     * @param sessionHeader150
     * 
     * @param callOptions151
     * 
     * @param assignmentRuleHeader152
     * 
     * @param mruHeader153
     * 
     * @param allowFieldTruncationHeader154
     * 
     * @param disableFeedTrackingHeader155
     * 
     * @param streamingEnabledHeader156
     * 
     * @param allOrNoneHeader157
     * 
     * @param debuggingHeader158
     * 
     * @param packageVersionHeader159
     * 
     * @param emailHeader160
     */
    public void startupdate(

    com.salesforce.soap.partner.Update update149, com.salesforce.soap.partner.SessionHeader sessionHeader150,
            com.salesforce.soap.partner.CallOptions callOptions151,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader152,
            com.salesforce.soap.partner.MruHeader mruHeader153,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader154,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader155,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader156,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader157,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader158,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader159,
            com.salesforce.soap.partner.EmailHeader emailHeader160,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Set a user's password
     * 
     * @param setPassword162
     * 
     * @param sessionHeader163
     * 
     * @param callOptions164
     * 
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     * @throws com.salesforce.soap.partner.InvalidNewPasswordFault :
     */

    public com.salesforce.soap.partner.SetPasswordResponse setPassword(

    com.salesforce.soap.partner.SetPassword setPassword162, com.salesforce.soap.partner.SessionHeader sessionHeader163,
            com.salesforce.soap.partner.CallOptions callOptions164) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.UnexpectedErrorFault,
            com.salesforce.soap.partner.InvalidNewPasswordFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Set a user's password
     * 
     * @param setPassword162
     * 
     * @param sessionHeader163
     * 
     * @param callOptions164
     */
    public void startsetPassword(

    com.salesforce.soap.partner.SetPassword setPassword162, com.salesforce.soap.partner.SessionHeader sessionHeader163,
            com.salesforce.soap.partner.CallOptions callOptions164,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Logout the current user, invalidating the current session.
     * 
     * @param logout166
     * 
     * @param sessionHeader167
     * 
     * @param callOptions168
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.LogoutResponse logout(

    com.salesforce.soap.partner.Logout logout166, com.salesforce.soap.partner.SessionHeader sessionHeader167,
            com.salesforce.soap.partner.CallOptions callOptions168) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Logout the current user, invalidating the current
     * session.
     * 
     * @param logout166
     * 
     * @param sessionHeader167
     * 
     * @param callOptions168
     */
    public void startlogout(

    com.salesforce.soap.partner.Logout logout166, com.salesforce.soap.partner.SessionHeader sessionHeader167,
            com.salesforce.soap.partner.CallOptions callOptions168,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Get a set of sObjects
     * 
     * @param retrieve170
     * 
     * @param sessionHeader171
     * 
     * @param callOptions172
     * 
     * @param queryOptions173
     * 
     * @param mruHeader174
     * 
     * @param packageVersionHeader175
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.MalformedQueryFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.RetrieveResponse retrieve(

    com.salesforce.soap.partner.Retrieve retrieve170, com.salesforce.soap.partner.SessionHeader sessionHeader171,
            com.salesforce.soap.partner.CallOptions callOptions172, com.salesforce.soap.partner.QueryOptions queryOptions173,
            com.salesforce.soap.partner.MruHeader mruHeader174,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader175) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.MalformedQueryFault,
            com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.InvalidFieldFault,
            com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Get a set of sObjects
     * 
     * @param retrieve170
     * 
     * @param sessionHeader171
     * 
     * @param callOptions172
     * 
     * @param queryOptions173
     * 
     * @param mruHeader174
     * 
     * @param packageVersionHeader175
     */
    public void startretrieve(

    com.salesforce.soap.partner.Retrieve retrieve170, com.salesforce.soap.partner.SessionHeader sessionHeader171,
            com.salesforce.soap.partner.CallOptions callOptions172, com.salesforce.soap.partner.QueryOptions queryOptions173,
            com.salesforce.soap.partner.MruHeader mruHeader174,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader175,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Create a Query Cursor, including deleted sObjects
     * 
     * @param queryAll177
     * 
     * @param sessionHeader178
     * 
     * @param callOptions179
     * 
     * @param queryOptions180
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.MalformedQueryFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault :
     */

    public com.salesforce.soap.partner.QueryAllResponse queryAll(

    com.salesforce.soap.partner.QueryAll queryAll177, com.salesforce.soap.partner.SessionHeader sessionHeader178,
            com.salesforce.soap.partner.CallOptions callOptions179, com.salesforce.soap.partner.QueryOptions queryOptions180)
            throws java.rmi.RemoteException

            , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.MalformedQueryFault,
            com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.InvalidFieldFault,
            com.salesforce.soap.partner.UnexpectedErrorFault, com.salesforce.soap.partner.InvalidQueryLocatorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Create a Query Cursor, including deleted sObjects
     * 
     * @param queryAll177
     * 
     * @param sessionHeader178
     * 
     * @param callOptions179
     * 
     * @param queryOptions180
     */
    public void startqueryAll(

    com.salesforce.soap.partner.QueryAll queryAll177, com.salesforce.soap.partner.SessionHeader sessionHeader178,
            com.salesforce.soap.partner.CallOptions callOptions179, com.salesforce.soap.partner.QueryOptions queryOptions180,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Get the IDs for updated sObjects
     * 
     * @param getUpdated182
     * 
     * @param sessionHeader183
     * 
     * @param callOptions184
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.GetUpdatedResponse getUpdated(

    com.salesforce.soap.partner.GetUpdated getUpdated182, com.salesforce.soap.partner.SessionHeader sessionHeader183,
            com.salesforce.soap.partner.CallOptions callOptions184) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Get the IDs for updated sObjects
     * 
     * @param getUpdated182
     * 
     * @param sessionHeader183
     * 
     * @param callOptions184
     */
    public void startgetUpdated(

    com.salesforce.soap.partner.GetUpdated getUpdated182, com.salesforce.soap.partner.SessionHeader sessionHeader183,
            com.salesforce.soap.partner.CallOptions callOptions184,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Undelete a set of sObjects
     * 
     * @param undelete186
     * 
     * @param sessionHeader187
     * 
     * @param callOptions188
     * 
     * @param allowFieldTruncationHeader189
     * 
     * @param disableFeedTrackingHeader190
     * 
     * @param streamingEnabledHeader191
     * 
     * @param allOrNoneHeader192
     * 
     * @param debuggingHeader193
     * 
     * @param packageVersionHeader194
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.UndeleteResponse undelete(

    com.salesforce.soap.partner.Undelete undelete186, com.salesforce.soap.partner.SessionHeader sessionHeader187,
            com.salesforce.soap.partner.CallOptions callOptions188,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader189,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader190,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader191,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader192,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader193,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader194) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Undelete a set of sObjects
     * 
     * @param undelete186
     * 
     * @param sessionHeader187
     * 
     * @param callOptions188
     * 
     * @param allowFieldTruncationHeader189
     * 
     * @param disableFeedTrackingHeader190
     * 
     * @param streamingEnabledHeader191
     * 
     * @param allOrNoneHeader192
     * 
     * @param debuggingHeader193
     * 
     * @param packageVersionHeader194
     */
    public void startundelete(

    com.salesforce.soap.partner.Undelete undelete186, com.salesforce.soap.partner.SessionHeader sessionHeader187,
            com.salesforce.soap.partner.CallOptions callOptions188,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader189,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader190,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader191,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader192,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader193,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader194,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Create a set of new sObjects
     * 
     * @param create196
     * 
     * @param sessionHeader197
     * 
     * @param callOptions198
     * 
     * @param assignmentRuleHeader199
     * 
     * @param mruHeader200
     * 
     * @param allowFieldTruncationHeader201
     * 
     * @param disableFeedTrackingHeader202
     * 
     * @param streamingEnabledHeader203
     * 
     * @param allOrNoneHeader204
     * 
     * @param debuggingHeader205
     * 
     * @param packageVersionHeader206
     * 
     * @param emailHeader207
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.CreateResponse create(

    com.salesforce.soap.partner.Create create196, com.salesforce.soap.partner.SessionHeader sessionHeader197,
            com.salesforce.soap.partner.CallOptions callOptions198,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader199,
            com.salesforce.soap.partner.MruHeader mruHeader200,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader201,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader202,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader203,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader204,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader205,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader206,
            com.salesforce.soap.partner.EmailHeader emailHeader207) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.InvalidIdFault,
            com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Create a set of new sObjects
     * 
     * @param create196
     * 
     * @param sessionHeader197
     * 
     * @param callOptions198
     * 
     * @param assignmentRuleHeader199
     * 
     * @param mruHeader200
     * 
     * @param allowFieldTruncationHeader201
     * 
     * @param disableFeedTrackingHeader202
     * 
     * @param streamingEnabledHeader203
     * 
     * @param allOrNoneHeader204
     * 
     * @param debuggingHeader205
     * 
     * @param packageVersionHeader206
     * 
     * @param emailHeader207
     */
    public void startcreate(

    com.salesforce.soap.partner.Create create196, com.salesforce.soap.partner.SessionHeader sessionHeader197,
            com.salesforce.soap.partner.CallOptions callOptions198,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader199,
            com.salesforce.soap.partner.MruHeader mruHeader200,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader201,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader202,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader203,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader204,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader205,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader206,
            com.salesforce.soap.partner.EmailHeader emailHeader207,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Send outbound email
     * 
     * @param sendEmail209
     * 
     * @param sessionHeader210
     * 
     * @param callOptions211
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.SendEmailResponse sendEmail(

    com.salesforce.soap.partner.SendEmail sendEmail209, com.salesforce.soap.partner.SessionHeader sessionHeader210,
            com.salesforce.soap.partner.CallOptions callOptions211) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Send outbound email
     * 
     * @param sendEmail209
     * 
     * @param sessionHeader210
     * 
     * @param callOptions211
     */
    public void startsendEmail(

    com.salesforce.soap.partner.SendEmail sendEmail209, com.salesforce.soap.partner.SessionHeader sessionHeader210,
            com.salesforce.soap.partner.CallOptions callOptions211,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Search for sObjects
     * 
     * @param search213
     * 
     * @param sessionHeader214
     * 
     * @param callOptions215
     * 
     * @param packageVersionHeader216
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.MalformedSearchFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.SearchResponse search(

    com.salesforce.soap.partner.Search search213, com.salesforce.soap.partner.SessionHeader sessionHeader214,
            com.salesforce.soap.partner.CallOptions callOptions215,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader216) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.MalformedSearchFault,
            com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Search for sObjects
     * 
     * @param search213
     * 
     * @param sessionHeader214
     * 
     * @param callOptions215
     * 
     * @param packageVersionHeader216
     */
    public void startsearch(

    com.salesforce.soap.partner.Search search213, com.salesforce.soap.partner.SessionHeader sessionHeader214,
            com.salesforce.soap.partner.CallOptions callOptions215,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader216,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Create a Query Cursor
     * 
     * @param query218
     * 
     * @param sessionHeader219
     * 
     * @param callOptions220
     * 
     * @param queryOptions221
     * 
     * @param mruHeader222
     * 
     * @param packageVersionHeader223
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.MalformedQueryFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault :
     */

    public com.salesforce.soap.partner.QueryResponse query(

    com.salesforce.soap.partner.Query query218, com.salesforce.soap.partner.SessionHeader sessionHeader219,
            com.salesforce.soap.partner.CallOptions callOptions220, com.salesforce.soap.partner.QueryOptions queryOptions221,
            com.salesforce.soap.partner.MruHeader mruHeader222,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader223) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.MalformedQueryFault,
            com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.InvalidFieldFault,
            com.salesforce.soap.partner.UnexpectedErrorFault, com.salesforce.soap.partner.InvalidQueryLocatorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Create a Query Cursor
     * 
     * @param query218
     * 
     * @param sessionHeader219
     * 
     * @param callOptions220
     * 
     * @param queryOptions221
     * 
     * @param mruHeader222
     * 
     * @param packageVersionHeader223
     */
    public void startquery(

    com.salesforce.soap.partner.Query query218, com.salesforce.soap.partner.SessionHeader sessionHeader219,
            com.salesforce.soap.partner.CallOptions callOptions220, com.salesforce.soap.partner.QueryOptions queryOptions221,
            com.salesforce.soap.partner.MruHeader mruHeader222,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader223,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Get the IDs for deleted sObjects
     * 
     * @param getDeleted225
     * 
     * @param sessionHeader226
     * 
     * @param callOptions227
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.GetDeletedResponse getDeleted(

    com.salesforce.soap.partner.GetDeleted getDeleted225, com.salesforce.soap.partner.SessionHeader sessionHeader226,
            com.salesforce.soap.partner.CallOptions callOptions227) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Get the IDs for deleted sObjects
     * 
     * @param getDeleted225
     * 
     * @param sessionHeader226
     * 
     * @param callOptions227
     */
    public void startgetDeleted(

    com.salesforce.soap.partner.GetDeleted getDeleted225, com.salesforce.soap.partner.SessionHeader sessionHeader226,
            com.salesforce.soap.partner.CallOptions callOptions227,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Submit an entity to a workflow process or process a workitem
     * 
     * @param process229
     * 
     * @param sessionHeader230
     * 
     * @param callOptions231
     * 
     * @param allowFieldTruncationHeader232
     * 
     * @param disableFeedTrackingHeader233
     * 
     * @param streamingEnabledHeader234
     * 
     * @param debuggingHeader235
     * 
     * @param packageVersionHeader236
     * 
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.ProcessResponse process(

    com.salesforce.soap.partner.Process process229, com.salesforce.soap.partner.SessionHeader sessionHeader230,
            com.salesforce.soap.partner.CallOptions callOptions231,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader232,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader233,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader234,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader235,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader236) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Submit an entity to a workflow process or process a
     * workitem
     * 
     * @param process229
     * 
     * @param sessionHeader230
     * 
     * @param callOptions231
     * 
     * @param allowFieldTruncationHeader232
     * 
     * @param disableFeedTrackingHeader233
     * 
     * @param streamingEnabledHeader234
     * 
     * @param debuggingHeader235
     * 
     * @param packageVersionHeader236
     */
    public void startprocess(

    com.salesforce.soap.partner.Process process229, com.salesforce.soap.partner.SessionHeader sessionHeader230,
            com.salesforce.soap.partner.CallOptions callOptions231,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader232,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader233,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader234,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader235,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader236,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the data category group structures for a given set of pair of types and
     * data category group name
     * 
     * @param describeDataCategoryGroupStructures238
     * 
     * @param sessionHeader239
     * 
     * @param callOptions240
     * 
     * @param packageVersionHeader241
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse describeDataCategoryGroupStructures(

    com.salesforce.soap.partner.DescribeDataCategoryGroupStructures describeDataCategoryGroupStructures238,
            com.salesforce.soap.partner.SessionHeader sessionHeader239, com.salesforce.soap.partner.CallOptions callOptions240,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader241,
            com.salesforce.soap.partner.LocaleOptions localeOptions) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the data category group structures for a
     * given set of pair of types and data category group name
     * 
     * @param describeDataCategoryGroupStructures238
     * 
     * @param sessionHeader239
     * 
     * @param callOptions240
     * 
     * @param packageVersionHeader241
     */
    public void startdescribeDataCategoryGroupStructures(

    com.salesforce.soap.partner.DescribeDataCategoryGroupStructures describeDataCategoryGroupStructures238,
            com.salesforce.soap.partner.SessionHeader sessionHeader239, com.salesforce.soap.partner.CallOptions callOptions240,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader241,
            com.salesforce.soap.partner.LocaleOptions localeOptions,
            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Reset a user's password
     * 
     * @param resetPassword243
     * 
     * @param sessionHeader244
     * 
     * @param callOptions245
     * 
     * @param emailHeader246
     * 
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.ResetPasswordResponse resetPassword(

    com.salesforce.soap.partner.ResetPassword resetPassword243, com.salesforce.soap.partner.SessionHeader sessionHeader244,
            com.salesforce.soap.partner.CallOptions callOptions245, com.salesforce.soap.partner.EmailHeader emailHeader246)
            throws java.rmi.RemoteException

            , com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Reset a user's password
     * 
     * @param resetPassword243
     * 
     * @param sessionHeader244
     * 
     * @param callOptions245
     * 
     * @param emailHeader246
     */
    public void startresetPassword(

    com.salesforce.soap.partner.ResetPassword resetPassword243, com.salesforce.soap.partner.SessionHeader sessionHeader244,
            com.salesforce.soap.partner.CallOptions callOptions245, com.salesforce.soap.partner.EmailHeader emailHeader246,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the Global state
     * 
     * @param describeGlobal248
     * 
     * @param sessionHeader249
     * 
     * @param callOptions250
     * 
     * @param packageVersionHeader251
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeGlobalResponse describeGlobal(

    com.salesforce.soap.partner.DescribeGlobal describeGlobal248, com.salesforce.soap.partner.SessionHeader sessionHeader249,
            com.salesforce.soap.partner.CallOptions callOptions250,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader251) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the Global state
     * 
     * @param describeGlobal248
     * 
     * @param sessionHeader249
     * 
     * @param callOptions250
     * 
     * @param packageVersionHeader251
     */
    public void startdescribeGlobal(

    com.salesforce.soap.partner.DescribeGlobal describeGlobal248, com.salesforce.soap.partner.SessionHeader sessionHeader249,
            com.salesforce.soap.partner.CallOptions callOptions250,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader251,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the layout of an sObject
     * 
     * @param describeLayout253
     * 
     * @param sessionHeader254
     * 
     * @param callOptions255
     * 
     * @param packageVersionHeader256
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeLayoutResponse describeLayout(

    com.salesforce.soap.partner.DescribeLayoutE describeLayout253, com.salesforce.soap.partner.SessionHeader sessionHeader254,
            com.salesforce.soap.partner.CallOptions callOptions255,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader256) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.InvalidIdFault,
            com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the layout of an sObject
     * 
     * @param describeLayout253
     * 
     * @param sessionHeader254
     * 
     * @param callOptions255
     * 
     * @param packageVersionHeader256
     */
    public void startdescribeLayout(

    com.salesforce.soap.partner.DescribeLayoutE describeLayout253, com.salesforce.soap.partner.SessionHeader sessionHeader254,
            com.salesforce.soap.partner.CallOptions callOptions255,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader256,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe the tabs that appear on a users page
     * 
     * @param describeTabs258
     * 
     * @param sessionHeader259
     * 
     * @param callOptions260
     * 
     * @param packageVersionHeader261
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeTabsResponse describeTabs(

    com.salesforce.soap.partner.DescribeTabs describeTabs258, com.salesforce.soap.partner.SessionHeader sessionHeader259,
            com.salesforce.soap.partner.CallOptions callOptions260,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader261) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe the tabs that appear on a users page
     * 
     * @param describeTabs258
     * 
     * @param sessionHeader259
     * 
     * @param callOptions260
     * 
     * @param packageVersionHeader261
     */
    public void startdescribeTabs(

    com.salesforce.soap.partner.DescribeTabs describeTabs258, com.salesforce.soap.partner.SessionHeader sessionHeader259,
            com.salesforce.soap.partner.CallOptions callOptions260,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader261,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe all the data category groups available for a given set of types
     * 
     * @param describeDataCategoryGroups263
     * 
     * @param sessionHeader264
     * 
     * @param callOptions265
     * 
     * @param packageVersionHeader266
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse describeDataCategoryGroups(

    com.salesforce.soap.partner.DescribeDataCategoryGroups describeDataCategoryGroups263,
            com.salesforce.soap.partner.SessionHeader sessionHeader264, com.salesforce.soap.partner.CallOptions callOptions265,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader266,
            com.salesforce.soap.partner.LocaleOptions localeOptions) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe all the data category groups available for
     * a given set of types
     * 
     * @param describeDataCategoryGroups263
     * 
     * @param sessionHeader264
     * 
     * @param callOptions265
     * 
     * @param packageVersionHeader266
     */
    public void startdescribeDataCategoryGroups(

    com.salesforce.soap.partner.DescribeDataCategoryGroups describeDataCategoryGroups263,
            com.salesforce.soap.partner.SessionHeader sessionHeader264, com.salesforce.soap.partner.CallOptions callOptions265,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader266,
            com.salesforce.soap.partner.LocaleOptions localeOptions,
            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Gets server timestamp
     * 
     * @param getServerTimestamp268
     * 
     * @param sessionHeader269
     * 
     * @param callOptions270
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.GetServerTimestampResponse getServerTimestamp(

    com.salesforce.soap.partner.GetServerTimestamp getServerTimestamp268,
            com.salesforce.soap.partner.SessionHeader sessionHeader269, com.salesforce.soap.partner.CallOptions callOptions270)
            throws java.rmi.RemoteException

            , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Gets server timestamp
     * 
     * @param getServerTimestamp268
     * 
     * @param sessionHeader269
     * 
     * @param callOptions270
     */
    public void startgetServerTimestamp(

    com.salesforce.soap.partner.GetServerTimestamp getServerTimestamp268,
            com.salesforce.soap.partner.SessionHeader sessionHeader269, com.salesforce.soap.partner.CallOptions callOptions270,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Logs out and invalidates session ids
     * 
     * @param invalidateSessions272
     * 
     * @param sessionHeader273
     * 
     * @param callOptions274
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.InvalidateSessionsResponse invalidateSessions(

    com.salesforce.soap.partner.InvalidateSessions invalidateSessions272,
            com.salesforce.soap.partner.SessionHeader sessionHeader273, com.salesforce.soap.partner.CallOptions callOptions274)
            throws java.rmi.RemoteException

            , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Logs out and invalidates session ids
     * 
     * @param invalidateSessions272
     * 
     * @param sessionHeader273
     * 
     * @param callOptions274
     */
    public void startinvalidateSessions(

    com.salesforce.soap.partner.InvalidateSessions invalidateSessions272,
            com.salesforce.soap.partner.SessionHeader sessionHeader273, com.salesforce.soap.partner.CallOptions callOptions274,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe an sObject
     * 
     * @param describeSObject276
     * 
     * @param sessionHeader277
     * 
     * @param callOptions278
     * 
     * @param packageVersionHeader279
     * 
     * @param localeOptions280
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeSObjectResponse describeSObject(

    com.salesforce.soap.partner.DescribeSObject describeSObject276, com.salesforce.soap.partner.SessionHeader sessionHeader277,
            com.salesforce.soap.partner.CallOptions callOptions278,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader279,
            com.salesforce.soap.partner.LocaleOptions localeOptions280) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe an sObject
     * 
     * @param describeSObject276
     * 
     * @param sessionHeader277
     * 
     * @param callOptions278
     * 
     * @param packageVersionHeader279
     * 
     * @param localeOptions280
     */
    public void startdescribeSObject(

    com.salesforce.soap.partner.DescribeSObject describeSObject276, com.salesforce.soap.partner.SessionHeader sessionHeader277,
            com.salesforce.soap.partner.CallOptions callOptions278,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader279,
            com.salesforce.soap.partner.LocaleOptions localeOptions280,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Login to the Salesforce.com SOAP Api
     * 
     * @param login282
     * 
     * @param loginScopeHeader283
     * 
     * @param callOptions284
     * 
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     * @throws com.salesforce.soap.partner.LoginFault :
     */

    public com.salesforce.soap.partner.LoginResponse login(

    com.salesforce.soap.partner.Login login282, com.salesforce.soap.partner.LoginScopeHeader loginScopeHeader283,
            com.salesforce.soap.partner.CallOptions callOptions284) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidIdFault, com.salesforce.soap.partner.UnexpectedErrorFault,
            com.salesforce.soap.partner.LoginFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Login to the Salesforce.com SOAP Api
     * 
     * @param login282
     * 
     * @param loginScopeHeader283
     * 
     * @param callOptions284
     */
    public void startlogin(

    com.salesforce.soap.partner.Login login282, com.salesforce.soap.partner.LoginScopeHeader loginScopeHeader283,
            com.salesforce.soap.partner.CallOptions callOptions284,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Gets the next batch of sObjects from a query
     * 
     * @param queryMore286
     * 
     * @param sessionHeader287
     * 
     * @param callOptions288
     * 
     * @param queryOptions289
     * 
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault :
     * @throws MalformedQueryFault 
     */

    public com.salesforce.soap.partner.QueryMoreResponse queryMore(

    com.salesforce.soap.partner.QueryMore queryMore286, com.salesforce.soap.partner.SessionHeader sessionHeader287,
            com.salesforce.soap.partner.CallOptions callOptions288, com.salesforce.soap.partner.QueryOptions queryOptions289)
            throws java.rmi.RemoteException

            , com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault,
            com.salesforce.soap.partner.InvalidQueryLocatorFault, MalformedQueryFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Gets the next batch of sObjects from a query
     * 
     * @param queryMore286
     * 
     * @param sessionHeader287
     * 
     * @param callOptions288
     * 
     * @param queryOptions289
     */
    public void startqueryMore(

    com.salesforce.soap.partner.QueryMore queryMore286, com.salesforce.soap.partner.SessionHeader sessionHeader287,
            com.salesforce.soap.partner.CallOptions callOptions288, com.salesforce.soap.partner.QueryOptions queryOptions289,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Describe a number sObjects
     * 
     * @param describeSObjects291
     * 
     * @param sessionHeader292
     * 
     * @param callOptions293
     * 
     * @param packageVersionHeader294
     * 
     * @param localeOptions295
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DescribeSObjectsResponse describeSObjects(

    com.salesforce.soap.partner.DescribeSObjects describeSObjects291, com.salesforce.soap.partner.SessionHeader sessionHeader292,
            com.salesforce.soap.partner.CallOptions callOptions293,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader294,
            com.salesforce.soap.partner.LocaleOptions localeOptions295) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Describe a number sObjects
     * 
     * @param describeSObjects291
     * 
     * @param sessionHeader292
     * 
     * @param callOptions293
     * 
     * @param packageVersionHeader294
     * 
     * @param localeOptions295
     */
    public void startdescribeSObjects(

    com.salesforce.soap.partner.DescribeSObjects describeSObjects291, com.salesforce.soap.partner.SessionHeader sessionHeader292,
            com.salesforce.soap.partner.CallOptions callOptions293,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader294,
            com.salesforce.soap.partner.LocaleOptions localeOptions295,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Empty a set of sObjects from the recycle bin
     * 
     * @param emptyRecycleBin297
     * 
     * @param sessionHeader298
     * 
     * @param callOptions299
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.EmptyRecycleBinResponse emptyRecycleBin(

    com.salesforce.soap.partner.EmptyRecycleBin emptyRecycleBin297, com.salesforce.soap.partner.SessionHeader sessionHeader298,
            com.salesforce.soap.partner.CallOptions callOptions299) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Empty a set of sObjects from the recycle bin
     * 
     * @param emptyRecycleBin297
     * 
     * @param sessionHeader298
     * 
     * @param callOptions299
     */
    public void startemptyRecycleBin(

    com.salesforce.soap.partner.EmptyRecycleBin emptyRecycleBin297, com.salesforce.soap.partner.SessionHeader sessionHeader298,
            com.salesforce.soap.partner.CallOptions callOptions299,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Update or insert a set of sObjects based on object id
     * 
     * @param upsert301
     * 
     * @param sessionHeader302
     * 
     * @param callOptions303
     * 
     * @param assignmentRuleHeader304
     * 
     * @param mruHeader305
     * 
     * @param allowFieldTruncationHeader306
     * 
     * @param disableFeedTrackingHeader307
     * 
     * @param streamingEnabledHeader308
     * 
     * @param allOrNoneHeader309
     * 
     * @param debuggingHeader310
     * 
     * @param packageVersionHeader311
     * 
     * @param emailHeader312
     * 
     * @throws com.salesforce.soap.partner.InvalidSObjectFault :
     * @throws com.salesforce.soap.partner.InvalidIdFault :
     * @throws com.salesforce.soap.partner.InvalidFieldFault :
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.UpsertResponse upsert(

    com.salesforce.soap.partner.Upsert upsert301, com.salesforce.soap.partner.SessionHeader sessionHeader302,
            com.salesforce.soap.partner.CallOptions callOptions303,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader304,
            com.salesforce.soap.partner.MruHeader mruHeader305,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader306,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader307,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader308,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader309,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader310,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader311,
            com.salesforce.soap.partner.EmailHeader emailHeader312) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.InvalidSObjectFault, com.salesforce.soap.partner.InvalidIdFault,
            com.salesforce.soap.partner.InvalidFieldFault, com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Update or insert a set of sObjects based on object
     * id
     * 
     * @param upsert301
     * 
     * @param sessionHeader302
     * 
     * @param callOptions303
     * 
     * @param assignmentRuleHeader304
     * 
     * @param mruHeader305
     * 
     * @param allowFieldTruncationHeader306
     * 
     * @param disableFeedTrackingHeader307
     * 
     * @param streamingEnabledHeader308
     * 
     * @param allOrNoneHeader309
     * 
     * @param debuggingHeader310
     * 
     * @param packageVersionHeader311
     * 
     * @param emailHeader312
     */
    public void startupsert(

    com.salesforce.soap.partner.Upsert upsert301, com.salesforce.soap.partner.SessionHeader sessionHeader302,
            com.salesforce.soap.partner.CallOptions callOptions303,
            com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader304,
            com.salesforce.soap.partner.MruHeader mruHeader305,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader306,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader307,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader308,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader309,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader310,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader311,
            com.salesforce.soap.partner.EmailHeader emailHeader312,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature convert a set of leads
     * 
     * @param convertLead314
     * 
     * @param sessionHeader315
     * 
     * @param callOptions316
     * 
     * @param allowFieldTruncationHeader317
     * 
     * @param disableFeedTrackingHeader318
     * 
     * @param streamingEnabledHeader319
     * 
     * @param debuggingHeader320
     * 
     * @param packageVersionHeader321
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.ConvertLeadResponse convertLead(

    com.salesforce.soap.partner.ConvertLead convertLead314, com.salesforce.soap.partner.SessionHeader sessionHeader315,
            com.salesforce.soap.partner.CallOptions callOptions316,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader317,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader318,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader319,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader320,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader321) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations convert a set of leads
     * 
     * @param convertLead314
     * 
     * @param sessionHeader315
     * 
     * @param callOptions316
     * 
     * @param allowFieldTruncationHeader317
     * 
     * @param disableFeedTrackingHeader318
     * 
     * @param streamingEnabledHeader319
     * 
     * @param debuggingHeader320
     * 
     * @param packageVersionHeader321
     */
    public void startconvertLead(

    com.salesforce.soap.partner.ConvertLead convertLead314, com.salesforce.soap.partner.SessionHeader sessionHeader315,
            com.salesforce.soap.partner.CallOptions callOptions316,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader317,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader318,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader319,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader320,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader321,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    /**
     * Auto generated method signature Delete a set of sObjects
     * 
     * @param delete323
     * 
     * @param sessionHeader324
     * 
     * @param callOptions325
     * 
     * @param packageVersionHeader326
     * 
     * @param userTerritoryDeleteHeader327
     * 
     * @param emailHeader328
     * 
     * @param allowFieldTruncationHeader329
     * 
     * @param disableFeedTrackingHeader330
     * 
     * @param streamingEnabledHeader331
     * 
     * @param allOrNoneHeader332
     * 
     * @param debuggingHeader333
     * 
     * @throws com.salesforce.soap.partner.UnexpectedErrorFault :
     */

    public com.salesforce.soap.partner.DeleteResponse delete(

    com.salesforce.soap.partner.Delete delete323, com.salesforce.soap.partner.SessionHeader sessionHeader324,
            com.salesforce.soap.partner.CallOptions callOptions325,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader326,
            com.salesforce.soap.partner.UserTerritoryDeleteHeader userTerritoryDeleteHeader327,
            com.salesforce.soap.partner.EmailHeader emailHeader328,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader329,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader330,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader331,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader332,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader333) throws java.rmi.RemoteException

    , com.salesforce.soap.partner.UnexpectedErrorFault;

    /**
     * Auto generated method signature for Asynchronous Invocations Delete a set of sObjects
     * 
     * @param delete323
     * 
     * @param sessionHeader324
     * 
     * @param callOptions325
     * 
     * @param packageVersionHeader326
     * 
     * @param userTerritoryDeleteHeader327
     * 
     * @param emailHeader328
     * 
     * @param allowFieldTruncationHeader329
     * 
     * @param disableFeedTrackingHeader330
     * 
     * @param streamingEnabledHeader331
     * 
     * @param allOrNoneHeader332
     * 
     * @param debuggingHeader333
     */
    public void startdelete(

    com.salesforce.soap.partner.Delete delete323, com.salesforce.soap.partner.SessionHeader sessionHeader324,
            com.salesforce.soap.partner.CallOptions callOptions325,
            com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader326,
            com.salesforce.soap.partner.UserTerritoryDeleteHeader userTerritoryDeleteHeader327,
            com.salesforce.soap.partner.EmailHeader emailHeader328,
            com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader329,
            com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader330,
            com.salesforce.soap.partner.StreamingEnabledHeader streamingEnabledHeader331,
            com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader332,
            com.salesforce.soap.partner.DebuggingHeader debuggingHeader333,

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

    throws java.rmi.RemoteException;

    //
}
