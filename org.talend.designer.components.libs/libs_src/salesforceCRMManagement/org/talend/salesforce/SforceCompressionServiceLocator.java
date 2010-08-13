package org.talend.salesforce;

import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceException;

import org.apache.axis.transport.http.HTTPConstants;

import com.salesforce.soap.partner.SforceServiceLocator;

public class SforceCompressionServiceLocator extends SforceServiceLocator {

    public SforceCompressionServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Call createCall() throws ServiceException {
        Call call = super.createCall();
        call.setProperty(HTTPConstants.MC_ACCEPT_GZIP, Boolean.TRUE);
        call.setProperty(HTTPConstants.MC_GZIP_REQUEST, Boolean.TRUE);
        return call;
    }
}
