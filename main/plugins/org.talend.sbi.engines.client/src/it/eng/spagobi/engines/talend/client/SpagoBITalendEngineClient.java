/**
 * 
 * SpagoBI - The Business Intelligence Free Platform
 * 
 * Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 */
package it.eng.spagobi.engines.talend.client;

import it.eng.spagobi.engines.talend.client.exception.AuthenticationFailedException;
import it.eng.spagobi.engines.talend.client.exception.EngineUnavailableException;
import it.eng.spagobi.engines.talend.client.exception.ServiceInvocationFailedException;
import it.eng.spagobi.engines.talend.client.exception.UnsupportedEngineVersionException;

import java.io.File;
import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.talend.sbi.engines.client.i18n.Messages;
import org.talend.utils.thread.TimeoutManager;

/**
 * @author Andrea Gioia
 * 
 */
public class SpagoBITalendEngineClient implements ISpagoBITalendEngineClient {

    public static final int CLIENTAPI_MAJOR_VERSION_NUMBER = 0;

    public static final int CLIENTAPI_MINOR_VERSION_NUMBER = 5;

    public static final int CLIENTAPI_REVISION_VERSION_NUMBER = 0;

    public static final String CLIENTAPI_VERSION_NUMBER = CLIENTAPI_MAJOR_VERSION_NUMBER
            + Messages.getString("SpagoBITalendEngineClient.0") //$NON-NLS-1$
            + CLIENTAPI_MINOR_VERSION_NUMBER
            + Messages.getString("SpagoBITalendEngineClient.1") + CLIENTAPI_REVISION_VERSION_NUMBER; //$NON-NLS-1$

    private ISpagoBITalendEngineClient client;

    public SpagoBITalendEngineClient(String usr, String pwd, String host, String port, String appContext)
            throws EngineUnavailableException, ServiceInvocationFailedException, UnsupportedEngineVersionException {
        String url = Messages.getString("SpagoBITalendEngineClient.2") + host + Messages.getString("SpagoBITalendEngineClient.3") + port + Messages.getString("SpagoBITalendEngineClient.4") + appContext + "/EngineInfoService"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        String complianceVersion = getEngineComplianceVersion(url);
        String[] versionChunks = complianceVersion.split("\\."); //$NON-NLS-1$
        int major = 0;// Integer.parseInt(versionChunks[0]);
        int minor = 5;// Integer.parseInt(versionChunks[1]);
        if (major > CLIENTAPI_MAJOR_VERSION_NUMBER
                || (major == CLIENTAPI_MAJOR_VERSION_NUMBER && minor > CLIENTAPI_MINOR_VERSION_NUMBER)) {
            throw new UnsupportedEngineVersionException(
                    Messages.getString("SpagoBITalendEngineClient.unsupportEngineer"), complianceVersion); //$NON-NLS-1$
        }

        if (major == 0 && minor == 5) {
            client = new SpagoBITalendEngineClient_0_5_0(usr, pwd, host, port, appContext);
        }
    }

    public boolean deployJob(JobDeploymentDescriptor jobDeploymentDescriptor, File executableJobFiles)
            throws EngineUnavailableException, AuthenticationFailedException, ServiceInvocationFailedException {
        return client.deployJob(jobDeploymentDescriptor, executableJobFiles);
    }

    public String getEngineName() throws EngineUnavailableException, ServiceInvocationFailedException {
        return client.getEngineName();
    }

    public String getEngineVersion() throws EngineUnavailableException, ServiceInvocationFailedException {
        return client.getEngineVersion();
    }

    public boolean isEngineAvailible() throws EngineUnavailableException {
        return client.isEngineAvailible();
    }

    public static String getEngineComplianceVersion(String url) throws EngineUnavailableException,
            ServiceInvocationFailedException {
        String version;
        HttpClient client;
        PostMethod method;
        NameValuePair[] nameValuePairs;

        version = null;
        client = new HttpClient();
        if(TimeoutManager.getSocketTimeout() != null) {
            client.getParams().setIntParameter(TimeoutManager.SOCKET_TIMEOUT, 
                    TimeoutManager.getSocketTimeout());
        }
        if(TimeoutManager.getConnectionTimeout() != null) {
            client.getParams().setIntParameter(TimeoutManager.CONNECTION_TIMEOUT, 
                    TimeoutManager.getConnectionTimeout());
        }
        method = new PostMethod(url);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

        nameValuePairs = new NameValuePair[] { new NameValuePair("infoType", "complianceVersion") }; //$NON-NLS-1$ //$NON-NLS-2$

        method.setRequestBody(nameValuePairs);

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                throw new ServiceInvocationFailedException(
                        Messages.getString("SpagoBITalendEngineClient.serviceFailed"), method.getStatusLine().toString(), method //$NON-NLS-1$
                                .getResponseBodyAsString());
            } else {
                version = method.getResponseBodyAsString();
            }

        } catch (HttpException e) {
            throw new EngineUnavailableException(Messages.getString("SpagoBITalendEngineClient.5", e.getMessage())); //$NON-NLS-1$
        } catch (IOException e) {
            throw new EngineUnavailableException(Messages.getString("SpagoBITalendEngineClient.6", e.getMessage())); //$NON-NLS-1$
        } finally {
            // Release the connection.
            method.releaseConnection();
        }

        return version;
    }
}
