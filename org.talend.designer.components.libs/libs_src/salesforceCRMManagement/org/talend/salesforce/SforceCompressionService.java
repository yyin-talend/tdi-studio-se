package org.talend.salesforce;

import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceException;

import org.apache.axis.transport.http.HTTPConstants;

import com.sforce.soap.partner.SforceServiceLocator;

public class SforceCompressionService extends SforceServiceLocator {

    public Call createCall() throws ServiceException {
        Call call = super.createCall();
        call.setProperty(HTTPConstants.MC_ACCEPT_GZIP, Boolean.TRUE);
        call.setProperty(HTTPConstants.MC_GZIP_REQUEST, Boolean.TRUE);
        return call;
    }
}
