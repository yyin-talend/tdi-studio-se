package org.talend.designer.components.exchange.jobs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.AbstractContent;
import us.monoid.web.FormData;
import us.monoid.web.Resty;
import us.monoid.web.TextResource;
import us.monoid.web.mime.MultipartContent;

public class RestClient {

    public void webServiceToken(String[] args) {
        Resty r = new Resty();

        JSONObject token = new us.monoid.json.JSONObject();

        try {
            JSONObject tokenMessage = new JSONObject();
            tokenMessage.put("version", "4.2.2r56343");
            tokenMessage.put("uniqueId", "XXXXXX");
            tokenMessage.put("typeStudio", "TOS");

            JSONObject properties = new JSONObject();
            properties.put("tos.count.localprojects", "10");
            properties.put("tos.count.jobs", "48");
            properties.put("tos.count.jobsperproject", "5");
            // ... //
            tokenMessage.put("properties", properties);

            token.put("tokenStudio", tokenMessage);

            AbstractContent ac = Resty.content(token);
            MultipartContent mpc = Resty.form(new FormData("data", ac));

            TextResource result = r.text("http://www.talend.com/TalendRegisterWS/tokenstudio.php", mpc);
            // System.out.println(new JSONObject(result.toString()).getString("result"));
            System.out.println(result.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public final static String exchangeWSServer = "http://www.talendforge.org/exchange/webservices/";

    // public final String exchangeWSServer = "http://10.42.20.42/studioWebservices/exchange/server/";
    public void searchExtension(String[] args) {

        // Resty r = new Resty();
        JSONObject tokenMessage = new JSONObject();
        try {
            tokenMessage.put("typeExtension", "TOS");
            tokenMessage.put("versionStudio", "4.2.1");
            tokenMessage.put("search", "");

            JSONObject token = new us.monoid.json.JSONObject();
            token.put("searchExtension", tokenMessage);

            System.out.println(token);

            // AbstractContent ac = Resty.content(token);
            // MultipartContent mpc = Resty.form(new FormData("data", ac));

            // TextResource result = r
            // .get("http://10.42.20.42/studioWebservices/exchange/server/availableExtension.php?data="+token, mpc);
            // System.out.println(result);
            // HttpURLConnection huc = new HttpURLConnection(new
            // URL("http://10.42.20.42/studioWebservices/exchange/server/availableExtension.php?data="+token));
            // String s = huc.getContent();

            String u = exchangeWSServer + "availableExtension.php?data=" + token;

            JSONObject answer = readJsonFromUrl(u);
            System.out.println(answer);

            JSONObject p = (JSONObject) answer.get("resultSearch");
            JSONArray o = p.getJSONArray("extensions");
            int size = o.length();
            System.out.println("List " + size + " extensions");
            for (int i = 0; i < size; i++) {
                JSONObject extension = o.getJSONObject(i);
                System.out.println("------------------------------------------------------------");
                System.out.println("New Extension");
                System.out.println("    author: " + extension.getString("author"));
                System.out.println("    idExtension: " + extension.getString("idExtension"));
                System.out.println("    label: " + extension.getString("label"));
                System.out.println("    rate: " + extension.getString("rate"));
                System.out.println("    typeExtension: " + extension.getString("typeExtension"));
                System.out.println("    versionExtension: " + extension.getString("versionExtension"));
                System.out.println("------------------------------------------------------------");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void viewDetail(String[] args) {
        Resty r = new Resty();

        JSONObject token = new us.monoid.json.JSONObject();

        try {
            JSONObject tokenMessage = new JSONObject();
            tokenMessage.put("typeExtension", "tos");
            tokenMessage.put("idExtension", "346");

            token.put("viewDetail", tokenMessage);

            AbstractContent ac = Resty.content(token);
            MultipartContent mpc = Resty.form(new FormData("data", ac));

            TextResource result = r.text(exchangeWSServer + "availableExtension.php", mpc);

            JSONObject jbo = new JSONObject(result.toString());

            JSONObject resultViewDetail = jbo.getJSONObject("resultViewDetail");
            JSONObject extension = resultViewDetail.getJSONObject("extension");

            System.out.println("idExtension : " + extension.get("idExtension"));
            System.out.println("label : " + extension.get("label"));
            System.out.println("lastVersionAvailable : " + extension.get("lastVersionAvailable"));
            System.out.println("publicationDate : " + extension.get("publicationDate"));
            System.out.println("description : " + extension.get("description"));
            System.out.println("rate : " + extension.get("rate"));
            System.out.println("author : " + extension.get("author"));

            System.out.println("reviews : " + extension.get("reviews"));

            JSONArray o = extension.getJSONArray("reviews");
            int size = o.length();
            System.out.println("List " + size + " Reviews");
            for (int i = 0; i < size; i++) {
                JSONObject review = o.getJSONObject(i);
                System.out.println("------------------------------------------------------------");
                System.out.println("New Review");
                System.out.println("    author: " + review.getString("author"));
                System.out.println("    title: " + review.getString("title"));
                System.out.println("    comment: " + review.getString("comment"));
                System.out.println("    rate: " + review.getString("rate"));
                System.out.println("------------------------------------------------------------");
            }
            System.out.println(result.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 
     * DOC hcyi Comment method "searchContributedExtensionJSONArray".
     * 
     * @return
     */
    public void searchContributedExtensionJSONArray() {
        JSONObject tokenMessage = new JSONObject();
        JSONArray o = null;
        try {
            tokenMessage.put("username", "yi");
            tokenMessage.put("passwordHash", "f06bdffd3093557270536c0ba80d0144dbafa916");
            JSONObject token = new us.monoid.json.JSONObject();
            token.put("contributedExtension", tokenMessage);

            String u = exchangeWSServer + "contributedExtension.php?data=" + token;
            JSONObject answer = readJsonFromUrl(u);
            System.out.println(answer);

            JSONObject p = (JSONObject) answer.get("listContributedExtension");
            o = p.getJSONArray("extensions");
            int size = o.length();
            System.out.println("List " + size + " extensions");
            for (int i = 0; i < size; i++) {
                JSONObject extension = o.getJSONObject(i);
                System.out.println("------------------------------------------------------------");
                System.out.println("Contributed Extension");
                System.out.println("    typeExtension: " + extension.getString("typeExtension"));
                System.out.println("    idExtension: " + extension.getString("idExtension"));
                System.out.println("    name: " + extension.getString("name"));
                System.out.println("    dateExtension: " + extension.getString("dateExtension"));
                System.out.println("    lastVersion: " + extension.getString("lastVersion"));
                System.out.println("    linkDownload: " + extension.getString("linkDownload"));
                System.out.println("------------------------------------------------------------");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RestClient rc = new RestClient();
        rc.searchContributedExtensionJSONArray();
        // rc.searchExtension(args);
        // rc.viewDetail(args);
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        String jsonText = "";
        JSONObject json = null;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            jsonText = readAll(rd);
            json = new JSONObject(jsonText);

        } catch (Exception e) {
            System.out.println(jsonText);
        } finally {
            is.close();
        }
        return json;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
