package org.talend.ms.crm.odata.authentication.httpclienthelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public final class HttpResponse {
    static Logger logger = LoggerFactory.getLogger(HttpResponse.class.getName());
    private int status;
    private Map<String, List<String>> headers;
    private String body;

    private Optional<String> code = null;

    public static HttpResponse fromHttpUrlConnection(HttpURLConnection conn) throws IOException {
        final int status = conn.getResponseCode();
        final Map<String, List<String>> respHeaders = conn.getHeaderFields();

        HttpResponse context = new HttpResponse(status);
        context.setHeaders(respHeaders);

        logger.info("CREATE HTTPRESPONSE : " + status);
        respHeaders.entrySet().stream().map((Map.Entry<String, List<String>> e) ->
                e.getKey() + " : " + e.getValue().stream().collect(Collectors.joining(" / "))
        ).forEach((String e) -> logger.info("\theader : " + e));
        try(BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))){
            final String body = in.lines().collect(Collectors.joining("\n"));
            logger.info("\tbody : begin ----------------------------------------");
            logger.info("\t" + body);
            logger.info("\tbody : end ------------------------------------------");
            context.setBody(body);
        }

        return context;
    }

    private HttpResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    private Optional<List<String>> _getAllValuesHeader(String key) {
        if (this.getHeaders() == null || this.getHeaders().size() <= 0) {
            return Optional.empty();
        }

        if (!this.getHeaders().containsKey(key)) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.getHeaders().get(key));
    }

    public Optional<List<String>> getAllValuesHeader(String key) {
        String keyUpper = ("" + key.charAt(0)).toUpperCase() + key.substring(1);
        String keyLower = ("" + key.charAt(0)).toLowerCase() + key.substring(1);

        Optional<List<String>> values = _getAllValuesHeader(keyUpper);
        if(!values.isPresent()){
            values = _getAllValuesHeader(keyLower);
        }

        return values;
    }

    public Optional<String> getFirstValueHeader(String key) {
        logger.info("HTTPRESPONSE : getFirstValueHeader of " + key);
        final Optional<List<String>> values = getAllValuesHeader(key);
        if(!values.isPresent()){
            logger.info("HTTPRESPONSE : getFirstValueHeader is empty!");
            return Optional.empty();
        }

        final List<String> v = values.get();
        logger.info("HTTPRESPONSE : getFirstValueHeader get value \"" + v + "\"");
        if (v.size() <= 0) {
            return Optional.empty();
        }

        return Optional.ofNullable(v.get(0));
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Extract 'Code' parameter from the redirect url if exist.
     * @return An Optional with the code if any.
     */
    private Optional<String> _extractCode(){
        final Optional<String> optLocation = this.getFirstValueHeader("Location");
        logger.info("HTTPRESPONSE - 'Location' header present ? " + optLocation.isPresent());
        if (!optLocation.isPresent()) {
            return Optional.empty();
        }
        final String optLocationValue = optLocation.get();
        logger.info("HTTPRESPONSE - 'Location' header = " + optLocationValue);

        final String[] split = optLocationValue.split("&|\\?");

        logger.info("HTTPRESPONSE : Location split result:");
        Arrays.asList(split).stream().forEach(e -> logger.info("\t" + e));
        final Optional<String> optCode = Arrays.stream(split).filter(e -> e.startsWith("code=")).findFirst();

        if (optCode.isPresent()) {
            final String optCodeValue = optCode.get();
            String code = optCodeValue.substring(5);
            logger.info("HTTPRESPONSE retrieved code \"" + optCodeValue + "\" => " + code);
            return Optional.ofNullable(code);
        }

        return Optional.empty();
    }

    public Optional<String> extractCode(){
        if(code != null){
            return code;
        }

        code = _extractCode();
        return code;
    }
}
