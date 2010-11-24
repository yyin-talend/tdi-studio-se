

/**
 * SforceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.2  Built on : Sep 06, 2010 (09:42:01 CEST)
 */

    package com.salesforce.soap.partner;

    /*
     *  SforceService java interface
     */

    public interface SforceService {
          

        /**
          * Auto generated method signature
          * Merge and update a set of sObjects based on object id
                    * @param merge121
                
                    * @param sessionHeader122
                
                    * @param callOptions123
                
                    * @param assignmentRuleHeader124
                
                    * @param mruHeader125
                
                    * @param allowFieldTruncationHeader126
                
                    * @param disableFeedTrackingHeader127
                
                    * @param debuggingHeader128
                
                    * @param packageVersionHeader129
                
                    * @param emailHeader130
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.InvalidFieldFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.MergeResponse merge(

                        com.salesforce.soap.partner.Merge merge121,com.salesforce.soap.partner.SessionHeader sessionHeader122,com.salesforce.soap.partner.CallOptions callOptions123,com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader124,com.salesforce.soap.partner.MruHeader mruHeader125,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader126,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader127,com.salesforce.soap.partner.DebuggingHeader debuggingHeader128,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader129,com.salesforce.soap.partner.EmailHeader emailHeader130)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.InvalidFieldFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Merge and update a set of sObjects based on object id
                * @param merge121
            
                * @param sessionHeader122
            
                * @param callOptions123
            
                * @param assignmentRuleHeader124
            
                * @param mruHeader125
            
                * @param allowFieldTruncationHeader126
            
                * @param disableFeedTrackingHeader127
            
                * @param debuggingHeader128
            
                * @param packageVersionHeader129
            
                * @param emailHeader130
            
          */
        public void startmerge(

            com.salesforce.soap.partner.Merge merge121,com.salesforce.soap.partner.SessionHeader sessionHeader122,
                com.salesforce.soap.partner.CallOptions callOptions123,
                com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader124,
                com.salesforce.soap.partner.MruHeader mruHeader125,
                com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader126,
                com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader127,
                com.salesforce.soap.partner.DebuggingHeader debuggingHeader128,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader129,
                com.salesforce.soap.partner.EmailHeader emailHeader130,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Returns standard information relevant to the current user
                    * @param getUserInfo132
                
                    * @param sessionHeader133
                
                    * @param callOptions134
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.GetUserInfoResponse getUserInfo(

                        com.salesforce.soap.partner.GetUserInfo getUserInfo132,com.salesforce.soap.partner.SessionHeader sessionHeader133,com.salesforce.soap.partner.CallOptions callOptions134)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Returns standard information relevant to the current user
                * @param getUserInfo132
            
                * @param sessionHeader133
            
                * @param callOptions134
            
          */
        public void startgetUserInfo(

            com.salesforce.soap.partner.GetUserInfo getUserInfo132,com.salesforce.soap.partner.SessionHeader sessionHeader133,
                com.salesforce.soap.partner.CallOptions callOptions134,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Describe the layout of the SoftPhone
                    * @param describeSoftphoneLayout136
                
                    * @param sessionHeader137
                
                    * @param callOptions138
                
                    * @param packageVersionHeader139
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.DescribeSoftphoneLayoutResponse describeSoftphoneLayout(

                        com.salesforce.soap.partner.DescribeSoftphoneLayout describeSoftphoneLayout136,com.salesforce.soap.partner.SessionHeader sessionHeader137,com.salesforce.soap.partner.CallOptions callOptions138,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader139)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Describe the layout of the SoftPhone
                * @param describeSoftphoneLayout136
            
                * @param sessionHeader137
            
                * @param callOptions138
            
                * @param packageVersionHeader139
            
          */
        public void startdescribeSoftphoneLayout(

            com.salesforce.soap.partner.DescribeSoftphoneLayout describeSoftphoneLayout136,com.salesforce.soap.partner.SessionHeader sessionHeader137,
                com.salesforce.soap.partner.CallOptions callOptions138,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader139,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Update a set of sObjects
                    * @param update141
                
                    * @param sessionHeader142
                
                    * @param callOptions143
                
                    * @param assignmentRuleHeader144
                
                    * @param mruHeader145
                
                    * @param allowFieldTruncationHeader146
                
                    * @param disableFeedTrackingHeader147
                
                    * @param allOrNoneHeader148
                
                    * @param debuggingHeader149
                
                    * @param packageVersionHeader150
                
                    * @param emailHeader151
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.InvalidFieldFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.UpdateResponse update(

                        com.salesforce.soap.partner.Update update141,com.salesforce.soap.partner.SessionHeader sessionHeader142,com.salesforce.soap.partner.CallOptions callOptions143,com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader144,com.salesforce.soap.partner.MruHeader mruHeader145,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader146,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader147,com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader148,com.salesforce.soap.partner.DebuggingHeader debuggingHeader149,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader150,com.salesforce.soap.partner.EmailHeader emailHeader151)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.InvalidFieldFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Update a set of sObjects
                * @param update141
            
                * @param sessionHeader142
            
                * @param callOptions143
            
                * @param assignmentRuleHeader144
            
                * @param mruHeader145
            
                * @param allowFieldTruncationHeader146
            
                * @param disableFeedTrackingHeader147
            
                * @param allOrNoneHeader148
            
                * @param debuggingHeader149
            
                * @param packageVersionHeader150
            
                * @param emailHeader151
            
          */
        public void startupdate(

            com.salesforce.soap.partner.Update update141,com.salesforce.soap.partner.SessionHeader sessionHeader142,
                com.salesforce.soap.partner.CallOptions callOptions143,
                com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader144,
                com.salesforce.soap.partner.MruHeader mruHeader145,
                com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader146,
                com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader147,
                com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader148,
                com.salesforce.soap.partner.DebuggingHeader debuggingHeader149,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader150,
                com.salesforce.soap.partner.EmailHeader emailHeader151,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Set a user's password
                    * @param setPassword153
                
                    * @param sessionHeader154
                
                    * @param callOptions155
                
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
             * @throws com.salesforce.soap.partner.InvalidNewPasswordFault : 
         */

         
                     public com.salesforce.soap.partner.SetPasswordResponse setPassword(

                        com.salesforce.soap.partner.SetPassword setPassword153,com.salesforce.soap.partner.SessionHeader sessionHeader154,com.salesforce.soap.partner.CallOptions callOptions155)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault
          ,com.salesforce.soap.partner.InvalidNewPasswordFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Set a user's password
                * @param setPassword153
            
                * @param sessionHeader154
            
                * @param callOptions155
            
          */
        public void startsetPassword(

            com.salesforce.soap.partner.SetPassword setPassword153,com.salesforce.soap.partner.SessionHeader sessionHeader154,
                com.salesforce.soap.partner.CallOptions callOptions155,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Logout the current user, invalidating the current session.
                    * @param logout157
                
                    * @param sessionHeader158
                
                    * @param callOptions159
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.LogoutResponse logout(

                        com.salesforce.soap.partner.Logout logout157,com.salesforce.soap.partner.SessionHeader sessionHeader158,com.salesforce.soap.partner.CallOptions callOptions159)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Logout the current user, invalidating the current session.
                * @param logout157
            
                * @param sessionHeader158
            
                * @param callOptions159
            
          */
        public void startlogout(

            com.salesforce.soap.partner.Logout logout157,com.salesforce.soap.partner.SessionHeader sessionHeader158,
                com.salesforce.soap.partner.CallOptions callOptions159,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Get a set of sObjects
                    * @param retrieve161
                
                    * @param sessionHeader162
                
                    * @param callOptions163
                
                    * @param queryOptions164
                
                    * @param mruHeader165
                
                    * @param packageVersionHeader166
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.MalformedQueryFault : 
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.InvalidFieldFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.RetrieveResponse retrieve(

                        com.salesforce.soap.partner.Retrieve retrieve161,com.salesforce.soap.partner.SessionHeader sessionHeader162,com.salesforce.soap.partner.CallOptions callOptions163,com.salesforce.soap.partner.QueryOptions queryOptions164,com.salesforce.soap.partner.MruHeader mruHeader165,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader166)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.MalformedQueryFault
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.InvalidFieldFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Get a set of sObjects
                * @param retrieve161
            
                * @param sessionHeader162
            
                * @param callOptions163
            
                * @param queryOptions164
            
                * @param mruHeader165
            
                * @param packageVersionHeader166
            
          */
        public void startretrieve(

            com.salesforce.soap.partner.Retrieve retrieve161,com.salesforce.soap.partner.SessionHeader sessionHeader162,
                com.salesforce.soap.partner.CallOptions callOptions163,
                com.salesforce.soap.partner.QueryOptions queryOptions164,
                com.salesforce.soap.partner.MruHeader mruHeader165,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader166,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Create a Query Cursor, including deleted sObjects
                    * @param queryAll168
                
                    * @param sessionHeader169
                
                    * @param callOptions170
                
                    * @param queryOptions171
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.MalformedQueryFault : 
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.InvalidFieldFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
             * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault : 
         */

         
                     public com.salesforce.soap.partner.QueryAllResponse queryAll(

                        com.salesforce.soap.partner.QueryAll queryAll168,com.salesforce.soap.partner.SessionHeader sessionHeader169,com.salesforce.soap.partner.CallOptions callOptions170,com.salesforce.soap.partner.QueryOptions queryOptions171)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.MalformedQueryFault
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.InvalidFieldFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault
          ,com.salesforce.soap.partner.InvalidQueryLocatorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Create a Query Cursor, including deleted sObjects
                * @param queryAll168
            
                * @param sessionHeader169
            
                * @param callOptions170
            
                * @param queryOptions171
            
          */
        public void startqueryAll(

            com.salesforce.soap.partner.QueryAll queryAll168,com.salesforce.soap.partner.SessionHeader sessionHeader169,
                com.salesforce.soap.partner.CallOptions callOptions170,
                com.salesforce.soap.partner.QueryOptions queryOptions171,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Get the IDs for updated sObjects
                    * @param getUpdated173
                
                    * @param sessionHeader174
                
                    * @param callOptions175
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.GetUpdatedResponse getUpdated(

                        com.salesforce.soap.partner.GetUpdated getUpdated173,com.salesforce.soap.partner.SessionHeader sessionHeader174,com.salesforce.soap.partner.CallOptions callOptions175)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Get the IDs for updated sObjects
                * @param getUpdated173
            
                * @param sessionHeader174
            
                * @param callOptions175
            
          */
        public void startgetUpdated(

            com.salesforce.soap.partner.GetUpdated getUpdated173,com.salesforce.soap.partner.SessionHeader sessionHeader174,
                com.salesforce.soap.partner.CallOptions callOptions175,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Undelete a set of sObjects
                    * @param undelete177
                
                    * @param sessionHeader178
                
                    * @param callOptions179
                
                    * @param allowFieldTruncationHeader180
                
                    * @param disableFeedTrackingHeader181
                
                    * @param allOrNoneHeader182
                
                    * @param debuggingHeader183
                
                    * @param packageVersionHeader184
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.UndeleteResponse undelete(

                        com.salesforce.soap.partner.Undelete undelete177,com.salesforce.soap.partner.SessionHeader sessionHeader178,com.salesforce.soap.partner.CallOptions callOptions179,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader180,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader181,com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader182,com.salesforce.soap.partner.DebuggingHeader debuggingHeader183,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader184)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Undelete a set of sObjects
                * @param undelete177
            
                * @param sessionHeader178
            
                * @param callOptions179
            
                * @param allowFieldTruncationHeader180
            
                * @param disableFeedTrackingHeader181
            
                * @param allOrNoneHeader182
            
                * @param debuggingHeader183
            
                * @param packageVersionHeader184
            
          */
        public void startundelete(

            com.salesforce.soap.partner.Undelete undelete177,com.salesforce.soap.partner.SessionHeader sessionHeader178,
                com.salesforce.soap.partner.CallOptions callOptions179,
                com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader180,
                com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader181,
                com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader182,
                com.salesforce.soap.partner.DebuggingHeader debuggingHeader183,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader184,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Create a set of new sObjects
                    * @param create186
                
                    * @param sessionHeader187
                
                    * @param callOptions188
                
                    * @param assignmentRuleHeader189
                
                    * @param mruHeader190
                
                    * @param allowFieldTruncationHeader191
                
                    * @param disableFeedTrackingHeader192
                
                    * @param allOrNoneHeader193
                
                    * @param debuggingHeader194
                
                    * @param packageVersionHeader195
                
                    * @param emailHeader196
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.InvalidFieldFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.CreateResponse create(

                        com.salesforce.soap.partner.Create create186,com.salesforce.soap.partner.SessionHeader sessionHeader187,com.salesforce.soap.partner.CallOptions callOptions188,com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader189,com.salesforce.soap.partner.MruHeader mruHeader190,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader191,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader192,com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader193,com.salesforce.soap.partner.DebuggingHeader debuggingHeader194,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader195,com.salesforce.soap.partner.EmailHeader emailHeader196)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.InvalidFieldFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Create a set of new sObjects
                * @param create186
            
                * @param sessionHeader187
            
                * @param callOptions188
            
                * @param assignmentRuleHeader189
            
                * @param mruHeader190
            
                * @param allowFieldTruncationHeader191
            
                * @param disableFeedTrackingHeader192
            
                * @param allOrNoneHeader193
            
                * @param debuggingHeader194
            
                * @param packageVersionHeader195
            
                * @param emailHeader196
            
          */
        public void startcreate(

            com.salesforce.soap.partner.Create create186,com.salesforce.soap.partner.SessionHeader sessionHeader187,
                com.salesforce.soap.partner.CallOptions callOptions188,
                com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader189,
                com.salesforce.soap.partner.MruHeader mruHeader190,
                com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader191,
                com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader192,
                com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader193,
                com.salesforce.soap.partner.DebuggingHeader debuggingHeader194,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader195,
                com.salesforce.soap.partner.EmailHeader emailHeader196,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Send outbound email
                    * @param sendEmail198
                
                    * @param sessionHeader199
                
                    * @param callOptions200
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.SendEmailResponse sendEmail(

                        com.salesforce.soap.partner.SendEmail sendEmail198,com.salesforce.soap.partner.SessionHeader sessionHeader199,com.salesforce.soap.partner.CallOptions callOptions200)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Send outbound email
                * @param sendEmail198
            
                * @param sessionHeader199
            
                * @param callOptions200
            
          */
        public void startsendEmail(

            com.salesforce.soap.partner.SendEmail sendEmail198,com.salesforce.soap.partner.SessionHeader sessionHeader199,
                com.salesforce.soap.partner.CallOptions callOptions200,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Search for sObjects
                    * @param search202
                
                    * @param sessionHeader203
                
                    * @param callOptions204
                
                    * @param packageVersionHeader205
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.MalformedSearchFault : 
             * @throws com.salesforce.soap.partner.InvalidFieldFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.SearchResponse search(

                        com.salesforce.soap.partner.Search search202,com.salesforce.soap.partner.SessionHeader sessionHeader203,com.salesforce.soap.partner.CallOptions callOptions204,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader205)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.MalformedSearchFault
          ,com.salesforce.soap.partner.InvalidFieldFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Search for sObjects
                * @param search202
            
                * @param sessionHeader203
            
                * @param callOptions204
            
                * @param packageVersionHeader205
            
          */
        public void startsearch(

            com.salesforce.soap.partner.Search search202,com.salesforce.soap.partner.SessionHeader sessionHeader203,
                com.salesforce.soap.partner.CallOptions callOptions204,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader205,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Create a Query Cursor
                    * @param query207
                
                    * @param sessionHeader208
                
                    * @param callOptions209
                
                    * @param queryOptions210
                
                    * @param mruHeader211
                
                    * @param packageVersionHeader212
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.MalformedQueryFault : 
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.InvalidFieldFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
             * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault : 
         */

         
                     public com.salesforce.soap.partner.QueryResponse query(

                        com.salesforce.soap.partner.Query query207,com.salesforce.soap.partner.SessionHeader sessionHeader208,com.salesforce.soap.partner.CallOptions callOptions209,com.salesforce.soap.partner.QueryOptions queryOptions210,com.salesforce.soap.partner.MruHeader mruHeader211,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader212)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.MalformedQueryFault
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.InvalidFieldFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault
          ,com.salesforce.soap.partner.InvalidQueryLocatorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Create a Query Cursor
                * @param query207
            
                * @param sessionHeader208
            
                * @param callOptions209
            
                * @param queryOptions210
            
                * @param mruHeader211
            
                * @param packageVersionHeader212
            
          */
        public void startquery(

            com.salesforce.soap.partner.Query query207,com.salesforce.soap.partner.SessionHeader sessionHeader208,
                com.salesforce.soap.partner.CallOptions callOptions209,
                com.salesforce.soap.partner.QueryOptions queryOptions210,
                com.salesforce.soap.partner.MruHeader mruHeader211,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader212,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Get the IDs for deleted sObjects
                    * @param getDeleted214
                
                    * @param sessionHeader215
                
                    * @param callOptions216
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.GetDeletedResponse getDeleted(

                        com.salesforce.soap.partner.GetDeleted getDeleted214,com.salesforce.soap.partner.SessionHeader sessionHeader215,com.salesforce.soap.partner.CallOptions callOptions216)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Get the IDs for deleted sObjects
                * @param getDeleted214
            
                * @param sessionHeader215
            
                * @param callOptions216
            
          */
        public void startgetDeleted(

            com.salesforce.soap.partner.GetDeleted getDeleted214,com.salesforce.soap.partner.SessionHeader sessionHeader215,
                com.salesforce.soap.partner.CallOptions callOptions216,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Submit an entity to a workflow process or process a workitem
                    * @param process218
                
                    * @param sessionHeader219
                
                    * @param callOptions220
                
                    * @param allowFieldTruncationHeader221
                
                    * @param disableFeedTrackingHeader222
                
                    * @param debuggingHeader223
                
                    * @param packageVersionHeader224
                
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.ProcessResponse process(

                        com.salesforce.soap.partner.Process process218,com.salesforce.soap.partner.SessionHeader sessionHeader219,com.salesforce.soap.partner.CallOptions callOptions220,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader221,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader222,com.salesforce.soap.partner.DebuggingHeader debuggingHeader223,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader224)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Submit an entity to a workflow process or process a workitem
                * @param process218
            
                * @param sessionHeader219
            
                * @param callOptions220
            
                * @param allowFieldTruncationHeader221
            
                * @param disableFeedTrackingHeader222
            
                * @param debuggingHeader223
            
                * @param packageVersionHeader224
            
          */
        public void startprocess(

            com.salesforce.soap.partner.Process process218,com.salesforce.soap.partner.SessionHeader sessionHeader219,
                com.salesforce.soap.partner.CallOptions callOptions220,
                com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader221,
                com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader222,
                com.salesforce.soap.partner.DebuggingHeader debuggingHeader223,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader224,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Describe the data category group structures for a given set of pair of types and data category group name
                    * @param describeDataCategoryGroupStructures226
                
                    * @param sessionHeader227
                
                    * @param callOptions228
                
                    * @param packageVersionHeader229
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.DescribeDataCategoryGroupStructuresResponse describeDataCategoryGroupStructures(

                        com.salesforce.soap.partner.DescribeDataCategoryGroupStructures describeDataCategoryGroupStructures226,com.salesforce.soap.partner.SessionHeader sessionHeader227,com.salesforce.soap.partner.CallOptions callOptions228,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader229)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Describe the data category group structures for a given set of pair of types and data category group name
                * @param describeDataCategoryGroupStructures226
            
                * @param sessionHeader227
            
                * @param callOptions228
            
                * @param packageVersionHeader229
            
          */
        public void startdescribeDataCategoryGroupStructures(

            com.salesforce.soap.partner.DescribeDataCategoryGroupStructures describeDataCategoryGroupStructures226,com.salesforce.soap.partner.SessionHeader sessionHeader227,
                com.salesforce.soap.partner.CallOptions callOptions228,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader229,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Reset a user's password
                    * @param resetPassword231
                
                    * @param sessionHeader232
                
                    * @param callOptions233
                
                    * @param emailHeader234
                
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.ResetPasswordResponse resetPassword(

                        com.salesforce.soap.partner.ResetPassword resetPassword231,com.salesforce.soap.partner.SessionHeader sessionHeader232,com.salesforce.soap.partner.CallOptions callOptions233,com.salesforce.soap.partner.EmailHeader emailHeader234)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Reset a user's password
                * @param resetPassword231
            
                * @param sessionHeader232
            
                * @param callOptions233
            
                * @param emailHeader234
            
          */
        public void startresetPassword(

            com.salesforce.soap.partner.ResetPassword resetPassword231,com.salesforce.soap.partner.SessionHeader sessionHeader232,
                com.salesforce.soap.partner.CallOptions callOptions233,
                com.salesforce.soap.partner.EmailHeader emailHeader234,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Describe the Global state
                    * @param describeGlobal236
                
                    * @param sessionHeader237
                
                    * @param callOptions238
                
                    * @param packageVersionHeader239
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.DescribeGlobalResponse describeGlobal(

                        com.salesforce.soap.partner.DescribeGlobal describeGlobal236,com.salesforce.soap.partner.SessionHeader sessionHeader237,com.salesforce.soap.partner.CallOptions callOptions238,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader239)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Describe the Global state
                * @param describeGlobal236
            
                * @param sessionHeader237
            
                * @param callOptions238
            
                * @param packageVersionHeader239
            
          */
        public void startdescribeGlobal(

            com.salesforce.soap.partner.DescribeGlobal describeGlobal236,com.salesforce.soap.partner.SessionHeader sessionHeader237,
                com.salesforce.soap.partner.CallOptions callOptions238,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader239,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Describe the layout of an sObject
                    * @param describeLayout241
                
                    * @param sessionHeader242
                
                    * @param callOptions243
                
                    * @param packageVersionHeader244
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.DescribeLayoutResponse describeLayout(

                        com.salesforce.soap.partner.DescribeLayoutE describeLayout241,com.salesforce.soap.partner.SessionHeader sessionHeader242,com.salesforce.soap.partner.CallOptions callOptions243,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader244)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Describe the layout of an sObject
                * @param describeLayout241
            
                * @param sessionHeader242
            
                * @param callOptions243
            
                * @param packageVersionHeader244
            
          */
        public void startdescribeLayout(

            com.salesforce.soap.partner.DescribeLayoutE describeLayout241,com.salesforce.soap.partner.SessionHeader sessionHeader242,
                com.salesforce.soap.partner.CallOptions callOptions243,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader244,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Describe the tabs that appear on a users page
                    * @param describeTabs246
                
                    * @param sessionHeader247
                
                    * @param callOptions248
                
                    * @param packageVersionHeader249
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.DescribeTabsResponse describeTabs(

                        com.salesforce.soap.partner.DescribeTabs describeTabs246,com.salesforce.soap.partner.SessionHeader sessionHeader247,com.salesforce.soap.partner.CallOptions callOptions248,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader249)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Describe the tabs that appear on a users page
                * @param describeTabs246
            
                * @param sessionHeader247
            
                * @param callOptions248
            
                * @param packageVersionHeader249
            
          */
        public void startdescribeTabs(

            com.salesforce.soap.partner.DescribeTabs describeTabs246,com.salesforce.soap.partner.SessionHeader sessionHeader247,
                com.salesforce.soap.partner.CallOptions callOptions248,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader249,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Describe all the data category groups available for a given set of types
                    * @param describeDataCategoryGroups251
                
                    * @param sessionHeader252
                
                    * @param callOptions253
                
                    * @param packageVersionHeader254
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.DescribeDataCategoryGroupsResponse describeDataCategoryGroups(

                        com.salesforce.soap.partner.DescribeDataCategoryGroups describeDataCategoryGroups251,com.salesforce.soap.partner.SessionHeader sessionHeader252,com.salesforce.soap.partner.CallOptions callOptions253,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader254)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Describe all the data category groups available for a given set of types
                * @param describeDataCategoryGroups251
            
                * @param sessionHeader252
            
                * @param callOptions253
            
                * @param packageVersionHeader254
            
          */
        public void startdescribeDataCategoryGroups(

            com.salesforce.soap.partner.DescribeDataCategoryGroups describeDataCategoryGroups251,com.salesforce.soap.partner.SessionHeader sessionHeader252,
                com.salesforce.soap.partner.CallOptions callOptions253,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader254,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Gets server timestamp
                    * @param getServerTimestamp256
                
                    * @param sessionHeader257
                
                    * @param callOptions258
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.GetServerTimestampResponse getServerTimestamp(

                        com.salesforce.soap.partner.GetServerTimestamp getServerTimestamp256,com.salesforce.soap.partner.SessionHeader sessionHeader257,com.salesforce.soap.partner.CallOptions callOptions258)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Gets server timestamp
                * @param getServerTimestamp256
            
                * @param sessionHeader257
            
                * @param callOptions258
            
          */
        public void startgetServerTimestamp(

            com.salesforce.soap.partner.GetServerTimestamp getServerTimestamp256,com.salesforce.soap.partner.SessionHeader sessionHeader257,
                com.salesforce.soap.partner.CallOptions callOptions258,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Logs out and invalidates session ids
                    * @param invalidateSessions260
                
                    * @param sessionHeader261
                
                    * @param callOptions262
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.InvalidateSessionsResponse invalidateSessions(

                        com.salesforce.soap.partner.InvalidateSessions invalidateSessions260,com.salesforce.soap.partner.SessionHeader sessionHeader261,com.salesforce.soap.partner.CallOptions callOptions262)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Logs out and invalidates session ids
                * @param invalidateSessions260
            
                * @param sessionHeader261
            
                * @param callOptions262
            
          */
        public void startinvalidateSessions(

            com.salesforce.soap.partner.InvalidateSessions invalidateSessions260,com.salesforce.soap.partner.SessionHeader sessionHeader261,
                com.salesforce.soap.partner.CallOptions callOptions262,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Describe an sObject
                    * @param describeSObject264
                
                    * @param sessionHeader265
                
                    * @param callOptions266
                
                    * @param packageVersionHeader267
                
                    * @param localeOptions268
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.DescribeSObjectResponse describeSObject(

                        com.salesforce.soap.partner.DescribeSObject describeSObject264,com.salesforce.soap.partner.SessionHeader sessionHeader265,com.salesforce.soap.partner.CallOptions callOptions266,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader267,com.salesforce.soap.partner.LocaleOptions localeOptions268)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Describe an sObject
                * @param describeSObject264
            
                * @param sessionHeader265
            
                * @param callOptions266
            
                * @param packageVersionHeader267
            
                * @param localeOptions268
            
          */
        public void startdescribeSObject(

            com.salesforce.soap.partner.DescribeSObject describeSObject264,com.salesforce.soap.partner.SessionHeader sessionHeader265,
                com.salesforce.soap.partner.CallOptions callOptions266,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader267,
                com.salesforce.soap.partner.LocaleOptions localeOptions268,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Login to the Salesforce.com SOAP Api
                    * @param login270
                
                    * @param loginScopeHeader271
                
                    * @param callOptions272
                
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
             * @throws com.salesforce.soap.partner.LoginFault : 
         */

         
                     public com.salesforce.soap.partner.LoginResponse login(

                        com.salesforce.soap.partner.Login login270,com.salesforce.soap.partner.LoginScopeHeader loginScopeHeader271,com.salesforce.soap.partner.CallOptions callOptions272)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault
          ,com.salesforce.soap.partner.LoginFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Login to the Salesforce.com SOAP Api
                * @param login270
            
                * @param loginScopeHeader271
            
                * @param callOptions272
            
          */
        public void startlogin(

            com.salesforce.soap.partner.Login login270,com.salesforce.soap.partner.LoginScopeHeader loginScopeHeader271,
                com.salesforce.soap.partner.CallOptions callOptions272,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Gets the next batch of sObjects from a query
                    * @param queryMore274
                
                    * @param sessionHeader275
                
                    * @param callOptions276
                
                    * @param queryOptions277
                
             * @throws com.salesforce.soap.partner.InvalidFieldFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
             * @throws com.salesforce.soap.partner.InvalidQueryLocatorFault : 
         */

         
                     public com.salesforce.soap.partner.QueryMoreResponse queryMore(

                        com.salesforce.soap.partner.QueryMore queryMore274,com.salesforce.soap.partner.SessionHeader sessionHeader275,com.salesforce.soap.partner.CallOptions callOptions276,com.salesforce.soap.partner.QueryOptions queryOptions277)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidFieldFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault
          ,com.salesforce.soap.partner.InvalidQueryLocatorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Gets the next batch of sObjects from a query
                * @param queryMore274
            
                * @param sessionHeader275
            
                * @param callOptions276
            
                * @param queryOptions277
            
          */
        public void startqueryMore(

            com.salesforce.soap.partner.QueryMore queryMore274,com.salesforce.soap.partner.SessionHeader sessionHeader275,
                com.salesforce.soap.partner.CallOptions callOptions276,
                com.salesforce.soap.partner.QueryOptions queryOptions277,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Describe a number sObjects
                    * @param describeSObjects279
                
                    * @param sessionHeader280
                
                    * @param callOptions281
                
                    * @param packageVersionHeader282
                
                    * @param localeOptions283
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.DescribeSObjectsResponse describeSObjects(

                        com.salesforce.soap.partner.DescribeSObjects describeSObjects279,com.salesforce.soap.partner.SessionHeader sessionHeader280,com.salesforce.soap.partner.CallOptions callOptions281,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader282,com.salesforce.soap.partner.LocaleOptions localeOptions283)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Describe a number sObjects
                * @param describeSObjects279
            
                * @param sessionHeader280
            
                * @param callOptions281
            
                * @param packageVersionHeader282
            
                * @param localeOptions283
            
          */
        public void startdescribeSObjects(

            com.salesforce.soap.partner.DescribeSObjects describeSObjects279,com.salesforce.soap.partner.SessionHeader sessionHeader280,
                com.salesforce.soap.partner.CallOptions callOptions281,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader282,
                com.salesforce.soap.partner.LocaleOptions localeOptions283,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Empty a set of sObjects from the recycle bin
                    * @param emptyRecycleBin285
                
                    * @param sessionHeader286
                
                    * @param callOptions287
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.EmptyRecycleBinResponse emptyRecycleBin(

                        com.salesforce.soap.partner.EmptyRecycleBin emptyRecycleBin285,com.salesforce.soap.partner.SessionHeader sessionHeader286,com.salesforce.soap.partner.CallOptions callOptions287)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Empty a set of sObjects from the recycle bin
                * @param emptyRecycleBin285
            
                * @param sessionHeader286
            
                * @param callOptions287
            
          */
        public void startemptyRecycleBin(

            com.salesforce.soap.partner.EmptyRecycleBin emptyRecycleBin285,com.salesforce.soap.partner.SessionHeader sessionHeader286,
                com.salesforce.soap.partner.CallOptions callOptions287,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Update or insert a set of sObjects based on object id
                    * @param upsert289
                
                    * @param sessionHeader290
                
                    * @param callOptions291
                
                    * @param assignmentRuleHeader292
                
                    * @param mruHeader293
                
                    * @param allowFieldTruncationHeader294
                
                    * @param disableFeedTrackingHeader295
                
                    * @param allOrNoneHeader296
                
                    * @param debuggingHeader297
                
                    * @param packageVersionHeader298
                
                    * @param emailHeader299
                
             * @throws com.salesforce.soap.partner.InvalidSObjectFault : 
             * @throws com.salesforce.soap.partner.InvalidIdFault : 
             * @throws com.salesforce.soap.partner.InvalidFieldFault : 
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.UpsertResponse upsert(

                        com.salesforce.soap.partner.Upsert upsert289,com.salesforce.soap.partner.SessionHeader sessionHeader290,com.salesforce.soap.partner.CallOptions callOptions291,com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader292,com.salesforce.soap.partner.MruHeader mruHeader293,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader294,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader295,com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader296,com.salesforce.soap.partner.DebuggingHeader debuggingHeader297,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader298,com.salesforce.soap.partner.EmailHeader emailHeader299)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.InvalidSObjectFault
          ,com.salesforce.soap.partner.InvalidIdFault
          ,com.salesforce.soap.partner.InvalidFieldFault
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Update or insert a set of sObjects based on object id
                * @param upsert289
            
                * @param sessionHeader290
            
                * @param callOptions291
            
                * @param assignmentRuleHeader292
            
                * @param mruHeader293
            
                * @param allowFieldTruncationHeader294
            
                * @param disableFeedTrackingHeader295
            
                * @param allOrNoneHeader296
            
                * @param debuggingHeader297
            
                * @param packageVersionHeader298
            
                * @param emailHeader299
            
          */
        public void startupsert(

            com.salesforce.soap.partner.Upsert upsert289,com.salesforce.soap.partner.SessionHeader sessionHeader290,
                com.salesforce.soap.partner.CallOptions callOptions291,
                com.salesforce.soap.partner.AssignmentRuleHeader assignmentRuleHeader292,
                com.salesforce.soap.partner.MruHeader mruHeader293,
                com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader294,
                com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader295,
                com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader296,
                com.salesforce.soap.partner.DebuggingHeader debuggingHeader297,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader298,
                com.salesforce.soap.partner.EmailHeader emailHeader299,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * convert a set of leads
                    * @param convertLead301
                
                    * @param sessionHeader302
                
                    * @param callOptions303
                
                    * @param allowFieldTruncationHeader304
                
                    * @param disableFeedTrackingHeader305
                
                    * @param debuggingHeader306
                
                    * @param packageVersionHeader307
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.ConvertLeadResponse convertLead(

                        com.salesforce.soap.partner.ConvertLead convertLead301,com.salesforce.soap.partner.SessionHeader sessionHeader302,com.salesforce.soap.partner.CallOptions callOptions303,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader304,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader305,com.salesforce.soap.partner.DebuggingHeader debuggingHeader306,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader307)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * convert a set of leads
                * @param convertLead301
            
                * @param sessionHeader302
            
                * @param callOptions303
            
                * @param allowFieldTruncationHeader304
            
                * @param disableFeedTrackingHeader305
            
                * @param debuggingHeader306
            
                * @param packageVersionHeader307
            
          */
        public void startconvertLead(

            com.salesforce.soap.partner.ConvertLead convertLead301,com.salesforce.soap.partner.SessionHeader sessionHeader302,
                com.salesforce.soap.partner.CallOptions callOptions303,
                com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader304,
                com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader305,
                com.salesforce.soap.partner.DebuggingHeader debuggingHeader306,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader307,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Delete a set of sObjects
                    * @param delete309
                
                    * @param sessionHeader310
                
                    * @param callOptions311
                
                    * @param packageVersionHeader312
                
                    * @param userTerritoryDeleteHeader313
                
                    * @param emailHeader314
                
                    * @param allowFieldTruncationHeader315
                
                    * @param disableFeedTrackingHeader316
                
                    * @param allOrNoneHeader317
                
                    * @param debuggingHeader318
                
             * @throws com.salesforce.soap.partner.UnexpectedErrorFault : 
         */

         
                     public com.salesforce.soap.partner.DeleteResponse delete(

                        com.salesforce.soap.partner.Delete delete309,com.salesforce.soap.partner.SessionHeader sessionHeader310,com.salesforce.soap.partner.CallOptions callOptions311,com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader312,com.salesforce.soap.partner.UserTerritoryDeleteHeader userTerritoryDeleteHeader313,com.salesforce.soap.partner.EmailHeader emailHeader314,com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader315,com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader316,com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader317,com.salesforce.soap.partner.DebuggingHeader debuggingHeader318)
                        throws java.rmi.RemoteException
             
          ,com.salesforce.soap.partner.UnexpectedErrorFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Delete a set of sObjects
                * @param delete309
            
                * @param sessionHeader310
            
                * @param callOptions311
            
                * @param packageVersionHeader312
            
                * @param userTerritoryDeleteHeader313
            
                * @param emailHeader314
            
                * @param allowFieldTruncationHeader315
            
                * @param disableFeedTrackingHeader316
            
                * @param allOrNoneHeader317
            
                * @param debuggingHeader318
            
          */
        public void startdelete(

            com.salesforce.soap.partner.Delete delete309,com.salesforce.soap.partner.SessionHeader sessionHeader310,
                com.salesforce.soap.partner.CallOptions callOptions311,
                com.salesforce.soap.partner.PackageVersionHeader packageVersionHeader312,
                com.salesforce.soap.partner.UserTerritoryDeleteHeader userTerritoryDeleteHeader313,
                com.salesforce.soap.partner.EmailHeader emailHeader314,
                com.salesforce.soap.partner.AllowFieldTruncationHeader allowFieldTruncationHeader315,
                com.salesforce.soap.partner.DisableFeedTrackingHeader disableFeedTrackingHeader316,
                com.salesforce.soap.partner.AllOrNoneHeader allOrNoneHeader317,
                com.salesforce.soap.partner.DebuggingHeader debuggingHeader318,
                

            final com.salesforce.soap.partner.SforceServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    