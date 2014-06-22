/**
 * MktowsPort.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.marketo.www.mktows;

public interface MktowsPort extends java.rmi.Remote {

    public int getTimeout();

    public void setTimeout(int timeout);

    public String getClientAccessID();

    public void setClientAccessID(String clientAccessID);

    public String getSecretKey();

    public void setSecretKey(String secretKey);

    /**
     * Send a Marketo email.
     */
    public com.marketo.www.mktows.SuccessDescribeMObject describeMObject(
            com.marketo.www.mktows.ParamsDescribeMObject paramsDescribeMObject) throws java.rmi.RemoteException;

    /**
     * Get a list campaigns.
     */
    public com.marketo.www.mktows.SuccessGetCampaignsForSource getCampaignsForSource(
            com.marketo.www.mktows.ParamsGetCampaignsForSource paramsGetCampaignsForSource) throws java.rmi.RemoteException;

    /**
     * Get all details about a lead.
     */
    public com.marketo.www.mktows.SuccessGetLead getLead(com.marketo.www.mktows.ParamsGetLead paramsGetLead)
            throws java.rmi.RemoteException;

    /**
     * Get all activity log details about a lead.
     */
    public com.marketo.www.mktows.SuccessGetLeadActivity getLeadActivity(
            com.marketo.www.mktows.ParamsGetLeadActivity paramsGetLeadActivity) throws java.rmi.RemoteException;

    /**
     * Get changes for all leads.
     */
    public com.marketo.www.mktows.SuccessGetLeadChanges getLeadChanges(
            com.marketo.www.mktows.ParamsGetLeadChanges paramsGetLeadChanges) throws java.rmi.RemoteException;

    /**
     * Get all details about one or more leads.
     */
    public com.marketo.www.mktows.SuccessGetMultipleLeads getMultipleLeads(
            com.marketo.www.mktows.ParamsGetMultipleLeads paramsGetMultipleLeads) throws java.rmi.RemoteException;

    /**
     * Send a Marketo email.
     */
    public com.marketo.www.mktows.SuccessListMObjects listMObjects(com.marketo.www.mktows.ParamsListMObjects paramsListMObjects)
            throws java.rmi.RemoteException;

    /**
     * Perform an operation on a Marketo List, like add lead or remove lead.
     */
    public com.marketo.www.mktows.SuccessListOperation listOperation(
            com.marketo.www.mktows.ParamsListOperation paramsListOperation) throws java.rmi.RemoteException;

    /**
     * Request to add a set of leads to a campaign.
     */
    public com.marketo.www.mktows.SuccessRequestCampaign requestCampaign(
            com.marketo.www.mktows.ParamsRequestCampaign paramsRequestCampaign) throws java.rmi.RemoteException;

    /**
     * Create or update a lead.
     */
    public com.marketo.www.mktows.SuccessSyncLead syncLead(com.marketo.www.mktows.ParamsSyncLead paramsSyncLead)
            throws java.rmi.RemoteException;

    /**
     * Create or update one or more leads.
     */
    public com.marketo.www.mktows.SuccessSyncMultipleLeads syncMultipleLeads(
            com.marketo.www.mktows.ParamsSyncMultipleLeads paramsSyncMultipleLeads) throws java.rmi.RemoteException;
}
