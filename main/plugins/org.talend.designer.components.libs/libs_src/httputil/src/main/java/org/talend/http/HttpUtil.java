package org.talend.http;

import java.util.List;
import java.util.ArrayList;

public class HttpUtil {
    
    public static void handHttpResponse(org.apache.http.HttpResponse response) throws java.io.IOException {
        if(response == null) {
            return;
        }
        
        if(response.getStatusLine().getStatusCode() != 200) {//login fail
            if (response.getStatusLine().getStatusCode() == 400) {
                String returnedValue = org.apache.http.util.EntityUtils.toString(response.getEntity());
                net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(returnedValue);
                throw new RuntimeException(object.getString("cause"));
            } else if (response.getStatusLine().getStatusCode() == 403) {
                String returnedValue = org.apache.http.util.EntityUtils.toString(response.getEntity());
                net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(returnedValue);
                throw new RuntimeException(object.getString("message"));
            } else {
                throw new RuntimeException(response.getStatusLine().getReasonPhrase());
            }
        }
    }
    
    public static void handHttpResponse(java.net.HttpURLConnection connection) throws java.io.IOException {
        if(connection == null) {
            return;
        }
        
        int responseCode = connection.getResponseCode();
        String responseMessage = connection.getResponseMessage();
        if(responseCode != 200) {//login fail
            throw new RuntimeException("" + responseCode + " : " + responseMessage);
        }
    }
    
}