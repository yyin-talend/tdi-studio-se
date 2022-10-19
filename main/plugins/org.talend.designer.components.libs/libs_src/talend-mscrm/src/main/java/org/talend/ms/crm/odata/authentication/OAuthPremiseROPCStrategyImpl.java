package org.talend.ms.crm.odata.authentication;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.olingo.client.api.communication.request.ODataRequest;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.talend.ms.crm.odata.ClientConfiguration;
import org.talend.ms.crm.odata.authentication.httpclienthelper.HttpClient;
import org.talend.ms.crm.odata.authentication.httpclienthelper.HttpResponse;
import org.talend.ms.crm.odata.authentication.httpclienthelper.RequestHttpContext;
import org.talend.ms.crm.odata.authentication.httpclienthelper.Token;
import org.talend.ms.crm.odata.httpclientfactory.IHttpclientFactoryObservable;
import org.talend.ms.crm.odata.httpclientfactory.OAuthHttpClientFactory;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class OAuthPremiseROPCStrategyImpl implements IAuthStrategy {

    private final static int MAX_REDIRECT_AUTH_CODE = 3;

    private final ClientConfiguration conf;

    private Token token;
    private OAuthHttpClientFactory httpClientFactory;


    OAuthPremiseROPCStrategyImpl(ClientConfiguration conf) {
        this.conf = conf;
    }

    @Override
    public void init() throws AuthenticationException {
        oauthFlow();
    }

    @Override
    public IHttpclientFactoryObservable getHttpClientFactory() throws AuthenticationException {
        oauthFlow();

        if (httpClientFactory == null) {
            httpClientFactory = new OAuthHttpClientFactory(this.conf);
        }

        return httpClientFactory;
    }

    @Override
    public void refreshAuth() throws AuthenticationException {
        oauthFlow();
    }

    @Override
    public void configureRequest(ODataRequest request) {
        request.addCustomHeader(HttpHeader.AUTHORIZATION, "Bearer " + token.getAccess_token());
    }

    @Override
    public void configureRequest(HttpRequestBase request) {
        request.addHeader(HttpHeader.AUTHORIZATION, "Bearer " + token.getAccess_token());
    }

    private void oauthFlow() throws AuthenticationException {
        int retry = 0;

        while (true) {
            retry++;
            try {
                final Optional<Token> token = retrieveToken();
                if (token.isPresent()) {
                    this.token = token.get();
                    break;
                } else {
                    throw new IllegalArgumentException("Can't retrieve oauth token, but no exception has been raised.");
                }

            } catch (IOException | InterruptedException | IllegalArgumentException e) {
                if (retry < conf.getMaxRetryTimes()) {
                    try {
                        Thread.sleep(conf.getIntervalTime());
                    } catch (InterruptedException e1) {
                        // ignore
                    }
                    continue;
                }

                throw new AuthenticationException("Can't retrieve ms crm oauth token after '" + retry + "' retries : " + e.getMessage());
            }
        }
    }

    private Token json2Token(String json) throws JsonIOException, JsonSyntaxException {
        Gson gson = new Gson();
        JsonReader jsr = new JsonReader(new StringReader(json));
        return gson.fromJson(jsr, Token.class);
    }

    private Optional<Token> retrieveToken() throws IOException, InterruptedException, IllegalArgumentException {
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "password");
        body.put("username", conf.getUserName());
        body.put("password", conf.getPassword());
        body.put("client_id", conf.getClientId());

        if (conf.getClientSecret() != null && !"".equals(conf.getClientSecret().trim())) {
            body.put("client_secret", conf.getClientSecret());
        }

        if(conf.getForceResource() != null && !conf.getForceResource().trim().isEmpty()) {
            body.put("resource", conf.getForceResource());
        }

        if (conf.getScope() != null && !conf.getScope().trim().isEmpty()) {
            body.put("scope", conf.getScope());
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");


        RequestHttpContext queryContext = new RequestHttpContext("POST",
                conf.getOAuthTokenEndpoint(),
                Collections.emptyMap(),
                headers,
                true,
                conf.getTimeout() * 1000,
                conf.getTimeout() * 1000);

        queryContext.setBodyContent(body);

        HttpClient client = new HttpClient(queryContext);

        // Redirect are followed by the java http client
        final HttpResponse call = client.call(new AtomicInteger(-1), e -> true);

        if (call.getStatus() < 200 && call.getStatus() >= 300) {
            throw new IllegalArgumentException(String.format("Failing retrieving MS CRM OAuth token with ROPC flow, return status is %s\n%s", call.getStatus(), call.getBody()));
        }

        Token token = null;
        try {
            token = json2Token(call.getBody());
        } catch (JsonIOException | JsonSyntaxException e) {
            throw new IllegalArgumentException("Can't parse retrieve ms crm oauth token : " + e.getMessage(), e);
        }
        return Optional.ofNullable(token);
    }

}
