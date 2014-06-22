package com.marketo.www.mktows;

public class MktowsPortProxy implements com.marketo.www.mktows.MktowsPort {

    private String _endpoint = null;

    private String _clientAccessID = null;

    private String _secretKey = null;

    private int _timeout;

    private com.marketo.www.mktows.MktowsPort mktowsPort = null;

    public MktowsPortProxy() {
        _initMktowsPortProxy();
    }

    public MktowsPortProxy(String endpoint, String clientAccessID, String secretKey) {
        _endpoint = endpoint;
        _clientAccessID = clientAccessID;
        _secretKey = secretKey;
        _initMktowsPortProxy();
    }

    private void _initMktowsPortProxy() {
        try {
            mktowsPort = (new com.marketo.www.mktows.MktMktowsApiServiceLocator()).getMktowsApiSoapPort();
            if (mktowsPort != null) {
                if (_endpoint != null)
                    ((javax.xml.rpc.Stub) mktowsPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
                else
                    _endpoint = (String) ((javax.xml.rpc.Stub) mktowsPort)._getProperty("javax.xml.rpc.service.endpoint.address");
            }
            mktowsPort.setClientAccessID(_clientAccessID);
            mktowsPort.setSecretKey(_secretKey);
            mktowsPort.setTimeout(_timeout);
        } catch (javax.xml.rpc.ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (mktowsPort != null)
            ((javax.xml.rpc.Stub) mktowsPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    }

    public com.marketo.www.mktows.MktowsPort getMktowsPort() {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort;
    }

    public com.marketo.www.mktows.SuccessDescribeMObject describeMObject(
            com.marketo.www.mktows.ParamsDescribeMObject paramsDescribeMObject) throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.describeMObject(paramsDescribeMObject);
    }

    public com.marketo.www.mktows.SuccessGetCampaignsForSource getCampaignsForSource(
            com.marketo.www.mktows.ParamsGetCampaignsForSource paramsGetCampaignsForSource) throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.getCampaignsForSource(paramsGetCampaignsForSource);
    }

    public com.marketo.www.mktows.SuccessGetLead getLead(com.marketo.www.mktows.ParamsGetLead paramsGetLead)
            throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.getLead(paramsGetLead);
    }

    public com.marketo.www.mktows.SuccessGetLeadActivity getLeadActivity(
            com.marketo.www.mktows.ParamsGetLeadActivity paramsGetLeadActivity) throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.getLeadActivity(paramsGetLeadActivity);
    }

    public com.marketo.www.mktows.SuccessGetLeadChanges getLeadChanges(
            com.marketo.www.mktows.ParamsGetLeadChanges paramsGetLeadChanges) throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.getLeadChanges(paramsGetLeadChanges);
    }

    public com.marketo.www.mktows.SuccessGetMultipleLeads getMultipleLeads(
            com.marketo.www.mktows.ParamsGetMultipleLeads paramsGetMultipleLeads) throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.getMultipleLeads(paramsGetMultipleLeads);
    }

    public com.marketo.www.mktows.SuccessListMObjects listMObjects(com.marketo.www.mktows.ParamsListMObjects paramsListMObjects)
            throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.listMObjects(paramsListMObjects);
    }

    public com.marketo.www.mktows.SuccessListOperation listOperation(
            com.marketo.www.mktows.ParamsListOperation paramsListOperation) throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.listOperation(paramsListOperation);
    }

    public com.marketo.www.mktows.SuccessRequestCampaign requestCampaign(
            com.marketo.www.mktows.ParamsRequestCampaign paramsRequestCampaign) throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.requestCampaign(paramsRequestCampaign);
    }

    public com.marketo.www.mktows.SuccessSyncLead syncLead(com.marketo.www.mktows.ParamsSyncLead paramsSyncLead)
            throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.syncLead(paramsSyncLead);
    }

    public com.marketo.www.mktows.SuccessSyncMultipleLeads syncMultipleLeads(
            com.marketo.www.mktows.ParamsSyncMultipleLeads paramsSyncMultipleLeads) throws java.rmi.RemoteException {
        if (mktowsPort == null)
            _initMktowsPortProxy();
        return mktowsPort.syncMultipleLeads(paramsSyncMultipleLeads);
    }

    public String getClientAccessID() {
        return _clientAccessID;
    }

    public void setClientAccessID(String clientAccessID) {
        _clientAccessID = clientAccessID;
        mktowsPort.setClientAccessID(_clientAccessID);
    }

    public String getSecretKey() {
        return _secretKey;
    }

    public void setSecretKey(String secretKey) {
        _secretKey = secretKey;
        mktowsPort.setSecretKey(_secretKey);
    }

    public int getTimeout() {
        return _timeout;
    }

    public void setTimeout(int timeout) {
        this._timeout = timeout;
        mktowsPort.setTimeout(_timeout);
    }

}
