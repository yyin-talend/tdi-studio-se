package com.sforce16.soap.partner;

public class SoapProxy implements com.sforce16.soap.partner.Soap {
  private String _endpoint = null;
  private com.sforce16.soap.partner.Soap soap = null;
  
  public SoapProxy() {
    _initSoapProxy();
  }
  
  public SoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSoapProxy();
  }
  
  private void _initSoapProxy() {
    try {
      soap = (new com.sforce16.soap.partner.SforceServiceLocator()).getSoap();
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
  
  public com.sforce16.soap.partner.Soap getSoap() {
    if (soap == null)
      _initSoapProxy();
    return soap;
  }
  
  public com.sforce16.soap.partner.LoginResult login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidIdFault, com.sforce16.soap.partner.fault.LoginFault{
    if (soap == null)
      _initSoapProxy();
    return soap.login(username, password);
  }
  
  public com.sforce16.soap.partner.DescribeSObjectResult describeSObject(java.lang.String sObjectType) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeSObject(sObjectType);
  }
  
  public com.sforce16.soap.partner.DescribeSObjectResult[] describeSObjects(java.lang.String[] sObjectType) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeSObjects(sObjectType);
  }
  
  public com.sforce16.soap.partner.DescribeGlobalResult describeGlobal() throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeGlobal();
  }
  
  public com.sforce16.soap.partner.DescribeLayoutResult describeLayout(java.lang.String sObjectType, java.lang.String[] recordTypeIds) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault, com.sforce16.soap.partner.fault.InvalidIdFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeLayout(sObjectType, recordTypeIds);
  }
  
  public com.sforce16.soap.partner.DescribeSoftphoneLayoutResult describeSoftphoneLayout() throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeSoftphoneLayout();
  }
  
  public com.sforce16.soap.partner.DescribeTabSetResult[] describeTabs() throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.describeTabs();
  }
  
  public com.sforce16.soap.partner.SaveResult[] create(com.sforce16.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault, com.sforce16.soap.partner.fault.InvalidIdFault, com.sforce16.soap.partner.fault.InvalidFieldFault{
    if (soap == null)
      _initSoapProxy();
    return soap.create(sObjects);
  }
  
  public com.sforce16.soap.partner.SaveResult[] update(com.sforce16.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault, com.sforce16.soap.partner.fault.InvalidIdFault, com.sforce16.soap.partner.fault.InvalidFieldFault{
    if (soap == null)
      _initSoapProxy();
    return soap.update(sObjects);
  }
  
  public com.sforce16.soap.partner.UpsertResult[] upsert(java.lang.String externalIDFieldName, com.sforce16.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault, com.sforce16.soap.partner.fault.InvalidIdFault, com.sforce16.soap.partner.fault.InvalidFieldFault{
    if (soap == null)
      _initSoapProxy();
    return soap.upsert(externalIDFieldName, sObjects);
  }
  
  public com.sforce16.soap.partner.MergeResult[] merge(com.sforce16.soap.partner.MergeRequest[] request) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault, com.sforce16.soap.partner.fault.InvalidIdFault, com.sforce16.soap.partner.fault.InvalidFieldFault{
    if (soap == null)
      _initSoapProxy();
    return soap.merge(request);
  }
  
  public com.sforce16.soap.partner.DeleteResult[] delete(java.lang.String[] ids) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.delete(ids);
  }
  
  public com.sforce16.soap.partner.UndeleteResult[] undelete(java.lang.String[] ids) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.undelete(ids);
  }
  
  public com.sforce16.soap.partner.EmptyRecycleBinResult[] emptyRecycleBin(java.lang.String[] ids) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.emptyRecycleBin(ids);
  }
  
  public com.sforce16.soap.partner.sobject.SObject[] retrieve(java.lang.String fieldList, java.lang.String sObjectType, java.lang.String[] ids) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault, com.sforce16.soap.partner.fault.InvalidIdFault, com.sforce16.soap.partner.fault.MalformedQueryFault, com.sforce16.soap.partner.fault.InvalidFieldFault{
    if (soap == null)
      _initSoapProxy();
    return soap.retrieve(fieldList, sObjectType, ids);
  }
  
  public com.sforce16.soap.partner.ProcessResult[] process(com.sforce16.soap.partner.ProcessRequest[] actions) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidIdFault{
    if (soap == null)
      _initSoapProxy();
    return soap.process(actions);
  }
  
  public com.sforce16.soap.partner.LeadConvertResult[] convertLead(com.sforce16.soap.partner.LeadConvert[] leadConverts) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.convertLead(leadConverts);
  }
  
  public void logout() throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    soap.logout();
  }
  
  public com.sforce16.soap.partner.InvalidateSessionsResult[] invalidateSessions(java.lang.String[] sessionIds) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.invalidateSessions(sessionIds);
  }
  
  public com.sforce16.soap.partner.GetDeletedResult getDeleted(java.lang.String sObjectType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault{
    if (soap == null)
      _initSoapProxy();
    return soap.getDeleted(sObjectType, startDate, endDate);
  }
  
  public com.sforce16.soap.partner.GetUpdatedResult getUpdated(java.lang.String sObjectType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault{
    if (soap == null)
      _initSoapProxy();
    return soap.getUpdated(sObjectType, startDate, endDate);
  }
  
  public com.sforce16.soap.partner.QueryResult query(java.lang.String queryString) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault, com.sforce16.soap.partner.fault.InvalidIdFault, com.sforce16.soap.partner.fault.InvalidQueryLocatorFault, com.sforce16.soap.partner.fault.MalformedQueryFault, com.sforce16.soap.partner.fault.InvalidFieldFault{
    if (soap == null)
      _initSoapProxy();
    return soap.query(queryString);
  }
  
  public com.sforce16.soap.partner.QueryResult queryAll(java.lang.String queryString) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault, com.sforce16.soap.partner.fault.InvalidIdFault, com.sforce16.soap.partner.fault.InvalidQueryLocatorFault, com.sforce16.soap.partner.fault.MalformedQueryFault, com.sforce16.soap.partner.fault.InvalidFieldFault{
    if (soap == null)
      _initSoapProxy();
    return soap.queryAll(queryString);
  }
  
  public com.sforce16.soap.partner.QueryResult queryMore(java.lang.String queryLocator) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidQueryLocatorFault, com.sforce16.soap.partner.fault.InvalidFieldFault{
    if (soap == null)
      _initSoapProxy();
    return soap.queryMore(queryLocator);
  }
  
  public com.sforce16.soap.partner.SearchResult search(java.lang.String searchString) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.MalformedSearchFault, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidSObjectFault, com.sforce16.soap.partner.fault.InvalidFieldFault{
    if (soap == null)
      _initSoapProxy();
    return soap.search(searchString);
  }
  
  public com.sforce16.soap.partner.GetServerTimestampResult getServerTimestamp() throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.getServerTimestamp();
  }
  
  public com.sforce16.soap.partner.SetPasswordResult setPassword(java.lang.String userId, java.lang.String password) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidIdFault, com.sforce16.soap.partner.fault.InvalidNewPasswordFault{
    if (soap == null)
      _initSoapProxy();
    return soap.setPassword(userId, password);
  }
  
  public com.sforce16.soap.partner.ResetPasswordResult resetPassword(java.lang.String userId) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault, com.sforce16.soap.partner.fault.InvalidIdFault{
    if (soap == null)
      _initSoapProxy();
    return soap.resetPassword(userId);
  }
  
  public com.sforce16.soap.partner.GetUserInfoResult getUserInfo() throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.getUserInfo();
  }
  
  public com.sforce16.soap.partner.SendEmailResult[] sendEmail(com.sforce16.soap.partner.Email[] messages) throws java.rmi.RemoteException, com.sforce16.soap.partner.fault.UnexpectedErrorFault{
    if (soap == null)
      _initSoapProxy();
    return soap.sendEmail(messages);
  }
  
  
}