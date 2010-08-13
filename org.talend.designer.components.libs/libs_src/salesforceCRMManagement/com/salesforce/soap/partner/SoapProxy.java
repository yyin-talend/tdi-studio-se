package com.salesforce.soap.partner;

public class SoapProxy implements com.salesforce.soap.partner.Soap {
  private String _endpoint = null;
  private com.salesforce.soap.partner.Soap soap = null;
  
  public SoapProxy() {
    _initSoapProxy();
  }
  
  public SoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSoapProxy();
  }
  
  private void _initSoapProxy() {
    try {
      soap = (new com.salesforce.soap.partner.SforceServiceLocator()).getSoap();
      if (soap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)soap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)soap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (soap != null)
      ((javax.xml.rpc.Stub)soap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.salesforce.soap.partner.Soap getSoap() {
    if (soap == null)
      _initSoapProxy();
    return soap;
  }
  
  public com.salesforce.soap.partner.LoginResult login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault, com.salesforce.soap.partner.fault.LoginFault{
    if (soap == null)
      _initSoapProxy();
    return soap.login(username, password);
  }
  
  public com.salesforce.soap.partner.DescribeSObjectResult describeSObject(java.lang.String sObjectType) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeSObject(sObjectType);
  }
  
  public com.salesforce.soap.partner.DescribeSObjectResult[] describeSObjects(java.lang.String[] sObjectType) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeSObjects(sObjectType);
  }
  
  public com.salesforce.soap.partner.DescribeGlobalResult describeGlobal() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeGlobal();
  }
  
  public com.salesforce.soap.partner.DescribeDataCategoryGroupResult[] describeDataCategoryGroups(java.lang.String[] sObjectType) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeDataCategoryGroups(sObjectType);
  }
  
  public com.salesforce.soap.partner.DescribeDataCategoryGroupStructureResult[] describeDataCategoryGroupStructures(com.salesforce.soap.partner.DataCategoryGroupSobjectTypePair[] pairs, boolean topCategoriesOnly) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeDataCategoryGroupStructures(pairs, topCategoriesOnly);
  }
  
  public com.salesforce.soap.partner.DescribeLayoutResult describeLayout(java.lang.String sObjectType, java.lang.String[] recordTypeIds) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeLayout(sObjectType, recordTypeIds);
  }
  
  public com.salesforce.soap.partner.DescribeSoftphoneLayoutResult describeSoftphoneLayout() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeSoftphoneLayout();
  }
  
  public com.salesforce.soap.partner.DescribeTabSetResult[] describeTabs() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeTabs();
  }
  
  public com.salesforce.soap.partner.SaveResult[] create(com.salesforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.create(sObjects);
  }
  
  public com.salesforce.soap.partner.SaveResult[] update(com.salesforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.update(sObjects);
  }
  
  public com.salesforce.soap.partner.UpsertResult[] upsert(java.lang.String externalIDFieldName, com.salesforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.upsert(externalIDFieldName, sObjects);
  }
  
  public com.salesforce.soap.partner.MergeResult[] merge(com.salesforce.soap.partner.MergeRequest[] request) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.merge(request);
  }
  
  public com.salesforce.soap.partner.DeleteResult[] delete(java.lang.String[] ids) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.delete(ids);
  }
  
  public com.salesforce.soap.partner.UndeleteResult[] undelete(java.lang.String[] ids) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.undelete(ids);
  }
  
  public com.salesforce.soap.partner.EmptyRecycleBinResult[] emptyRecycleBin(java.lang.String[] ids) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.emptyRecycleBin(ids);
  }
  
  public com.salesforce.soap.partner.sobject.SObject[] retrieve(java.lang.String fieldList, java.lang.String sObjectType, java.lang.String[] ids) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.MalformedQueryFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.retrieve(fieldList, sObjectType, ids);
  }
  
  public com.salesforce.soap.partner.ProcessResult[] process(com.salesforce.soap.partner.ProcessRequest[] actions) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.process(actions);
  }
  
  public com.salesforce.soap.partner.LeadConvertResult[] convertLead(com.salesforce.soap.partner.LeadConvert[] leadConverts) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.convertLead(leadConverts);
  }
  
  public void logout() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    soap.logout();
  }
  
  public com.salesforce.soap.partner.InvalidateSessionsResult[] invalidateSessions(java.lang.String[] sessionIds) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.invalidateSessions(sessionIds);
  }
  
  public com.salesforce.soap.partner.GetDeletedResult getDeleted(java.lang.String sObjectType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.getDeleted(sObjectType, startDate, endDate);
  }
  
  public com.salesforce.soap.partner.GetUpdatedResult getUpdated(java.lang.String sObjectType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.getUpdated(sObjectType, startDate, endDate);
  }
  
  public com.salesforce.soap.partner.QueryResult query(java.lang.String queryString) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.MalformedQueryFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault, com.salesforce.soap.partner.fault.InvalidQueryLocatorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.query(queryString);
  }
  
  public com.salesforce.soap.partner.QueryResult queryAll(java.lang.String queryString) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.MalformedQueryFault, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault, com.salesforce.soap.partner.fault.InvalidQueryLocatorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.queryAll(queryString);
  }
  
  public com.salesforce.soap.partner.QueryResult queryMore(java.lang.String queryLocator) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault, com.salesforce.soap.partner.fault.InvalidQueryLocatorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.queryMore(queryLocator);
  }
  
  public com.salesforce.soap.partner.SearchResult search(java.lang.String searchString) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidSObjectFault, com.salesforce.soap.partner.fault.MalformedSearchFault, com.salesforce.soap.partner.fault.InvalidFieldFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.search(searchString);
  }
  
  public com.salesforce.soap.partner.GetServerTimestampResult getServerTimestamp() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.getServerTimestamp();
  }
  
  public com.salesforce.soap.partner.SetPasswordResult setPassword(java.lang.String userId, java.lang.String password) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault, com.salesforce.soap.partner.fault.InvalidNewPasswordFault{
    if (soap == null)
      _initSoapProxy();
    return soap.setPassword(userId, password);
  }
  
  public com.salesforce.soap.partner.ResetPasswordResult resetPassword(java.lang.String userId) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.InvalidIdFault, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.resetPassword(userId);
  }
  
  public com.salesforce.soap.partner.GetUserInfoResult getUserInfo() throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.getUserInfo();
  }
  
  public com.salesforce.soap.partner.SendEmailResult[] sendEmail(com.salesforce.soap.partner.Email[] messages) throws java.rmi.RemoteException, com.salesforce.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.sendEmail(messages);
  }
  
  
}