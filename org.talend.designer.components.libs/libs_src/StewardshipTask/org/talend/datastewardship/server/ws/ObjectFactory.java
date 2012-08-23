
package org.talend.datastewardship.server.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.talend.datastewardship.server.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetTgtRecord_QNAME = new QName("http://ws.server.datastewardship.talend.org/", "getTgtRecord");
    private final static QName _Echo_QNAME = new QName("http://ws.server.datastewardship.talend.org/", "echo");
    private final static QName _EchoResponse_QNAME = new QName("http://ws.server.datastewardship.talend.org/", "echoResponse");
    private final static QName _DeleteTasksResponse_QNAME = new QName("http://ws.server.datastewardship.talend.org/","deleteTasksResponse");
    private final static QName _GetSrcRecord_QNAME = new QName("http://ws.server.datastewardship.talend.org/", "getSrcRecord");
    private final static QName _GetTgtRecordResponse_QNAME = new QName("http://ws.server.datastewardship.talend.org/", "getTgtRecordResponse");
    private final static QName _DeleteTasks_QNAME = new QName("http://ws.server.datastewardship.talend.org/", "deleteTasks");
    private final static QName _SearchTasks_QNAME = new QName("http://ws.server.datastewardship.talend.org/", "searchTasks");
    private final static QName _GetSrcRecordResponse_QNAME = new QName("http://ws.server.datastewardship.talend.org/", "getSrcRecordResponse");
    private final static QName _SearchTasksResponse_QNAME = new QName("http://ws.server.datastewardship.talend.org/", "searchTasksResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.talend.datastewardship.server.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StringArrayArray }
     * 
     */
    public StringArrayArray createStringArrayArray() {
        return new StringArrayArray();
    }

    /**
     * Create an instance of {@link GetSrcRecord }
     * 
     */
    public GetSrcRecord createGetSrcRecord() {
        return new GetSrcRecord();
    }

    /**
     * Create an instance of {@link SearchTasksResponse }
     * 
     */
    public SearchTasksResponse createSearchTasksResponse() {
        return new SearchTasksResponse();
    }

    /**
     * Create an instance of {@link StringArray }
     * 
     */
    public StringArray createStringArray() {
        return new StringArray();
    }

    /**
     * Create an instance of {@link GetTgtRecordResponse }
     * 
     */
    public GetTgtRecordResponse createGetTgtRecordResponse() {
        return new GetTgtRecordResponse();
    }

    /**
     * Create an instance of {@link GetTgtRecord }
     * 
     */
    public GetTgtRecord createGetTgtRecord() {
        return new GetTgtRecord();
    }

    /**
     * Create an instance of {@link GetSrcRecordResponse }
     * 
     */
    public GetSrcRecordResponse createGetSrcRecordResponse() {
        return new GetSrcRecordResponse();
    }

    /**
     * Create an instance of {@link EchoResponse }
     * 
     */
    public EchoResponse createEchoResponse() {
        return new EchoResponse();
    }

    /**
     * Create an instance of {@link Echo }
     * 
     */
    public Echo createEcho() {
        return new Echo();
    }

    /**
     * Create an instance of {@link SearchTasks }
     * 
     */
    public SearchTasks createSearchTasks() {
        return new SearchTasks();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTgtRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.datastewardship.talend.org/", name = "getTgtRecord")
    public JAXBElement<GetTgtRecord> createGetTgtRecord(GetTgtRecord value) {
        return new JAXBElement<GetTgtRecord>(_GetTgtRecord_QNAME, GetTgtRecord.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Echo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.datastewardship.talend.org/", name = "echo")
    public JAXBElement<Echo> createEcho(Echo value) {
        return new JAXBElement<Echo>(_Echo_QNAME, Echo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EchoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.datastewardship.talend.org/", name = "echoResponse")
    public JAXBElement<EchoResponse> createEchoResponse(EchoResponse value) {
        return new JAXBElement<EchoResponse>(_EchoResponse_QNAME, EchoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTasksResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.datastewardship.talend.org/", name = "deleteTasksResponse")
    public JAXBElement<DeleteTasksResponse> createDeleteTasksResponse(DeleteTasksResponse value) {
        return new JAXBElement<DeleteTasksResponse>(_DeleteTasksResponse_QNAME, DeleteTasksResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSrcRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.datastewardship.talend.org/", name = "getSrcRecord")
    public JAXBElement<GetSrcRecord> createGetSrcRecord(GetSrcRecord value) {
        return new JAXBElement<GetSrcRecord>(_GetSrcRecord_QNAME, GetSrcRecord.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTgtRecordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.datastewardship.talend.org/", name = "getTgtRecordResponse")
    public JAXBElement<GetTgtRecordResponse> createGetTgtRecordResponse(GetTgtRecordResponse value) {
        return new JAXBElement<GetTgtRecordResponse>(_GetTgtRecordResponse_QNAME, GetTgtRecordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchTasks }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.datastewardship.talend.org/", name = "searchTasks")
    public JAXBElement<SearchTasks> createSearchTasks(SearchTasks value) {
        return new JAXBElement<SearchTasks>(_SearchTasks_QNAME, SearchTasks.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTasks }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.datastewardship.talend.org/", name = "deleteTasks")
    public JAXBElement<DeleteTasks> createDeleteTasks(DeleteTasks value) {
        return new JAXBElement<DeleteTasks>(_DeleteTasks_QNAME, DeleteTasks.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSrcRecordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.datastewardship.talend.org/", name = "getSrcRecordResponse")
    public JAXBElement<GetSrcRecordResponse> createGetSrcRecordResponse(GetSrcRecordResponse value) {
        return new JAXBElement<GetSrcRecordResponse>(_GetSrcRecordResponse_QNAME, GetSrcRecordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchTasksResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.datastewardship.talend.org/", name = "searchTasksResponse")
    public JAXBElement<SearchTasksResponse> createSearchTasksResponse(SearchTasksResponse value) {
        return new JAXBElement<SearchTasksResponse>(_SearchTasksResponse_QNAME, SearchTasksResponse.class, null, value);
    }

}
