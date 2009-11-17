/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.talend.ws.helper.conf;

import javax.wsdl.xml.WSDLLocator;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.configuration.security.ProxyAuthorizationPolicy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.transports.http.configuration.ProxyServerType;

/**
 *
 * @author rlamarche
 */
public class ServiceHelperConfiguration {

    private String proxyServer;
    private int proxyPort;
    private Long connectionTimeout;
    private Long receiveTimeout;
    private String cookie;
    private String username;
    private String password;
    private String proxyUsername;
    private String proxyPassword;

    public WSDLLocator createWSDLLocator(String wsdlUri) {
        return new WSDLLocatorImpl(this, wsdlUri);
    }

    private void updateClientPolicy(HTTPClientPolicy hTTPClientPolicy) {
        if (proxyServer != null) {
            hTTPClientPolicy.setProxyServer(proxyServer);
            hTTPClientPolicy.setProxyServerPort(proxyPort);
            hTTPClientPolicy.setProxyServerType(ProxyServerType.HTTP);
        } else {
            hTTPClientPolicy.setProxyServer(null);
        }
        if (connectionTimeout != null) {
            hTTPClientPolicy.setConnectionTimeout(connectionTimeout);
        }
        if (receiveTimeout != null) {
            hTTPClientPolicy.setReceiveTimeout(receiveTimeout);
        }
        if (cookie != null) {
            hTTPClientPolicy.setCookie(cookie);
        } else {
            hTTPClientPolicy.setCookie(null);
        }
    }

    public void configureHttpConduit(HTTPConduit httpConduit) {
        httpConduit.setAuthorization(createAuthorizationPolicy());
        httpConduit.setProxyAuthorization(createProxyAuthorizationPolicy());
        updateClientPolicy(httpConduit.getClient());
    }

    private AuthorizationPolicy createAuthorizationPolicy() {
        if (username != null) {
            AuthorizationPolicy authorizationPolicy = new AuthorizationPolicy();
            //authorizationPolicy.setAuthorizationType("Basic");
            authorizationPolicy.setUserName(username);
            authorizationPolicy.setPassword(password);

            return authorizationPolicy;
        } else {
            return null;
        }
    }

    private ProxyAuthorizationPolicy createProxyAuthorizationPolicy() {
        if (proxyUsername != null) {
            ProxyAuthorizationPolicy authorizationPolicy = new ProxyAuthorizationPolicy();
            //authorizationPolicy.setAuthorizationType("Basic");
            authorizationPolicy.setUserName(proxyUsername);
            authorizationPolicy.setPassword(proxyPassword);

            return authorizationPolicy;
        } else {
            return null;
        }
    }

    public Long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyServer() {
        return proxyServer;
    }

    public void setProxyServer(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    public Long getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(Long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }
}
