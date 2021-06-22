package org.talend.ms.crm.odata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.ServiceUnavailableException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.http.HttpClientException;
import org.apache.olingo.client.api.serialization.ODataSerializer;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.commons.api.Constants;
import org.talend.ms.crm.MSCRMClient;
import org.talend.ms.crm.sdk.Instance;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;

public class MSCRMClientTest {
	
	   private static String RESOURCE = "https://talendsupport.crm.dynamics.com/";

	    private final static String CLIENT_ID = "d6bf51bd-c127-4575-b4d4-893ae8db7723";

	    private final static String USERNAME = "dynamics@talend365.onmicrosoft.com";

	    private final static String PASSWORD = "R.)`:9>\\mrX3";

	    private final static String AUTHORITY = "https://login.windows.net/common/oauth2/authorize";

	public static void main(String[] args) throws Exception {

			ClientConfiguration configuration = ClientConfigurationFactory
                    .buildOAuthNativeClientConfiguration(CLIENT_ID,USERNAME,PASSWORD,AUTHORITY);
			configuration.setTimeout(6000);
			configuration.setReuseHttpClient(false);
			configuration.setMaxRetry(5,1000);
			MSCRMClient client = new MSCRMClient(configuration, "talendsupport", "https://globaldisco.crm.dynamics.com/api/discovery/v2.0/Instances(UniqueName='talendsupport')");
			client.getOnlineConnection();

	}
	
	public static String doGet(String url) throws Exception {
		
//		System.out.println(getAccessToken() );
		HttpGet request = new HttpGet(url);
		
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken() );

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getProtocolVersion());              // HTTP/1.1
            System.out.println(response.getStatusLine().getStatusCode());   // 200
            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                // return it as a String
                result = EntityUtils.toString(entity);
//                JsonArray jsonArray = JsonParser
//                        .parseString(result)
//                        .getAsJsonObject().get("value").getAsJsonArray();
//                Gson gson = new Gson();
//                Instance instance = gson.fromJson(jsonArray.get(0), Instance.class);
//                System.out.println(instance.apiUrl);
                
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JsonNode tree = mapper.readTree(result);
                Iterator<JsonNode> iter = tree.path("value").elements();
                while (iter.hasNext()){
                    JsonNode node = iter.next();
                    Instance instance = mapper.readValue(node.toString(), Instance.class);
                    System.out.println(instance.getApiUrl());
                }
            }
            return result;
        }

	}
	
    protected HttpEntity convertToHttpEntity(ClientEntity entity) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(output, Constants.UTF8);
            ODataClient odataClient =ODataClientFactory.getClient();
			final ODataSerializer serializer = odataClient.getSerializer(org.apache.olingo.commons.api.format.ContentType.JSON);
            serializer.write(writer, odataClient.getBinder().getEntity(entity));
            HttpEntity httpEntity = new ByteArrayEntity(output.toByteArray(),
                    org.apache.http.entity.ContentType.APPLICATION_JSON);
            return httpEntity;
        } catch (Exception e) {
            throw new HttpClientException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

	private static String sendPOST(String url) throws IOException {

		String result = "";
		HttpPost post = new HttpPost(url);

		// add request parameters or form parameters
		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("username", "abc"));
		urlParameters.add(new BasicNameValuePair("password", "123"));
		urlParameters.add(new BasicNameValuePair("custom", "secret"));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {

			result = EntityUtils.toString(response.getEntity());
		}

		return result;
	}
	
	public static String getAccessToken() throws ServiceUnavailableException {
		AuthenticationContext context = null;
        AuthenticationResult result = null;
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(1);
            context = new AuthenticationContext("https://login.windows.net/common/oauth2/authorize", false, service);
            Future<AuthenticationResult> future = context.acquireToken("https://globaldisco.crm.dynamics.com/",
            		CLIENT_ID, USERNAME, PASSWORD,
                    null);
            result = future.get();
            System.out.println(result.getAccessToken());
        } catch (Exception e) {
            throw new ServiceUnavailableException(e.getMessage());
        } finally {
            service.shutdown();
        }
        return result.getAccessToken();
	}

}
