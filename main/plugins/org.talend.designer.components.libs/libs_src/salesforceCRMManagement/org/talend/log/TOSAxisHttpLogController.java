package org.talend.log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.axis.components.logger.LogFactory;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;

public class TOSAxisHttpLogController {

    TOSAxisHttpSimpleLog log = null;

    // nothing, just in order to make Jet simply
    // boolean enableLoging = false;

    List<String> logContentList = new ArrayList<String>();

    private static TOSAxisHttpLogController instance = null;

    public static void init() {

        System.setProperty(org.apache.commons.logging.impl.LogFactoryImpl.LOG_PROPERTY, "org.talend.log.TOSAxisHttpSimpleLog");

    }

    public static synchronized TOSAxisHttpLogController getInstance() {
        if (instance == null) {
            instance = new TOSAxisHttpLogController();
        }
        return instance;
    }

    private TOSAxisHttpLogController() {

        Log log = LogFactory.getLog(HTTPSender.class.getName());
        if (log instanceof TOSAxisHttpSimpleLog) {
            this.log = (TOSAxisHttpSimpleLog) LogFactory.getLog(HTTPSender.class.getName());
        } else {
            throw new RuntimeException(
                    "Please set the TOSAxisHttpSimpleLog as the default logger for axis.jar. Usually, call TOSAxisHttpLogController.init() before any method in axis.jar");
        }

    }

    // start loging
    public void startLoging() {

        log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);
        log.clearBuffer();

        // Important
        logContentList.clear();

    }

    // stop loging, after call this method, then there call getLogContent().
    public void stopLoging() {

        // Important
        extractHttpContent();

        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        log.clearBuffer();

    }

    // get http request XML
    public String getHttpRequestXML() {

        if (logContentList.size() > 0) {
            return logContentList.get(0);
        }

        return "";
    }

    // get http response XML
    public String getHttpResponseXML() {

        if (logContentList.size() > 1) {
            return logContentList.get(1);
        }

        return "";
    }

    private void extractHttpContent() {

        String content = log.getLogContent();

        try {
            Pattern regex = Pattern.compile("<soapenv:Envelope .*</soapenv:Envelope>");
            Matcher regexMatcher = regex.matcher(content);
            while (regexMatcher.find()) {
                String group = regexMatcher.group();
                logContentList.add(group);
            }
        } catch (PatternSyntaxException ex) {
            // nothing to do, ignore it
        }

    }

}
