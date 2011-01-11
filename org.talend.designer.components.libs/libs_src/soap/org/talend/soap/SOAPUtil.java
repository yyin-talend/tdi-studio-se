package org.talend.soap;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SOAPUtil {

    private SOAPConnection connection;

    public static final String SOAP11 = "soap11";

    public static final String SOAP12 = "soap12";

    private String reBodyMessage;

    private String reHeaderMessage;

    private String reFaultMessage;

    private boolean hasFault = false;

    public SOAPUtil() throws SOAPException {
        init();
    }

    private void init() throws SOAPException {
        SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
        connection = soapConnFactory.createConnection();
    }

    public void setBasicAuth(final String username, final String password) {
        Authenticator.setDefault(new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }

    public void setNTLMAuth(String domain, String username, String password) {
        setBasicAuth(domain + "\\" + username, password);
    }

    public void setProxy(String host, int port, String username, String password) {
        System.setProperty("proxySet", "true");
        System.setProperty("proxyHost", host);
        System.setProperty("proxyPort", String.valueOf(port));
        System.setProperty("http.proxyUser", username);
        System.setProperty("http.proxyPassword", password);
    }

    public void invokeSOAP(String version, String destination, String soapAction, String soapMessage) throws SOAPException,
            TransformerException, ParserConfigurationException, FileNotFoundException {
        MessageFactory messageFactory = null;
        if (version.equals(SOAP12)) {
            messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        } else {
            messageFactory = MessageFactory.newInstance();
        }
        SOAPMessage message = messageFactory.createMessage();
        MimeHeaders mimeHeaders = message.getMimeHeaders();
        mimeHeaders.setHeader("SOAPAction", soapAction);

        // Create objects for the message parts
        SOAPPart soapPart = message.getSOAPPart();

        // System.out.println(soapMessage.toString());
        ByteArrayInputStream stream = new ByteArrayInputStream(soapMessage.getBytes());
        StreamSource preppedMsgSrc = new StreamSource(stream);
        soapPart.setContent(preppedMsgSrc);

        // InputStream stream = new FileInputStream(new File("d://soap.txt"));
        // StreamSource preppedMsgSrc = new StreamSource(stream);
        // soapPart.setContent(preppedMsgSrc);

        message.saveChanges();
        // Send the message

        SOAPMessage reply = connection.call(message, destination);

        SOAPPart reSoapPart = reply.getSOAPPart();
        SOAPEnvelope reEnvelope = reSoapPart.getEnvelope();

        SOAPHeader reHeader = reEnvelope.getHeader();
        if (reHeader != null) {
            setReHeaderMessage(Doc2StringWithoutDeclare(extractContentAsDocument(reHeader)));
        }
        SOAPBody reBody = reEnvelope.getBody();
        if (reBody.getFault() != null) {
            hasFault = true;
            setReFaultMessage(Doc2StringWithoutDeclare(extractContentAsDocument(reBody)));
            setReBodyMessage(null);
        } else {
            hasFault = false;
            setReBodyMessage(Doc2StringWithoutDeclare(extractContentAsDocument(reBody)));
            setReFaultMessage(null);
        }
    }

    public void close() throws SOAPException {
        connection.close();
    }

    public boolean hasFault() {
        return hasFault;
    }

    private void setReFaultMessage(String faultMessageStr) {
        reFaultMessage = faultMessageStr;
    }

    public String getReFaultMessage() {
        return reFaultMessage;
    }

    private Document extractContentAsDocument(SOAPBody body) throws SOAPException, TransformerException,
            ParserConfigurationException {
        return body.extractContentAsDocument();
    }

    private void setReBodyMessage(String bodyMessageStr) {
        reBodyMessage = bodyMessageStr;
    }

    public String getReBodyMessage() {
        return reBodyMessage;
    }

    private Document extractContentAsDocument(SOAPHeader header) throws ParserConfigurationException, TransformerException {
        Document document;
        DocumentBuilderFactory factory = new com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        Element element;
        Element headerRootElem = document.createElement("Header");

        Iterator childElements = header.getChildElements();
        org.w3c.dom.Node domNode = null;
        while (childElements.hasNext()) {
            domNode = (org.w3c.dom.Node) childElements.next();
            element = (Element) document.importNode(domNode, true);
            headerRootElem.appendChild(element);
        }
        document.appendChild(headerRootElem);
        return document;
    }

    private void setReHeaderMessage(String headerMessageStr) {
        reHeaderMessage = headerMessageStr;
    }

    public String getReHeaderMessage() {
        return reHeaderMessage;
    }

    private String Doc2StringWithoutDeclare(Document doc) {
        DOMBuilder builder = new DOMBuilder();
        org.jdom.Document jdomDoc = builder.build(doc);
        XMLOutputter outputter = new XMLOutputter();
        return outputter.outputString(jdomDoc.getRootElement());
    }
}
