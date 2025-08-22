
package com.neusoft.si.entrance.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.neusoft.si.entrance.webservice package. 
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

    private final static QName _Handle_QNAME = new QName("http://webservice.entrance.si.neusoft.com/", "handle");
    private final static QName _HandleResponse_QNAME = new QName("http://webservice.entrance.si.neusoft.com/", "handleResponse");
    private final static QName _Exception_QNAME = new QName("http://webservice.entrance.si.neusoft.com/", "Exception");
    private final static QName _HandleHistory_QNAME = new QName("http://webservice.entrance.si.neusoft.com/", "handleHistory");
    private final static QName _HandleHistoryResponse_QNAME = new QName("http://webservice.entrance.si.neusoft.com/", "handleHistoryResponse");
    private final static QName _HandleFile_QNAME = new QName("http://webservice.entrance.si.neusoft.com/", "handleFile");
    private final static QName _HandleFileResponse_QNAME = new QName("http://webservice.entrance.si.neusoft.com/", "handleFileResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.neusoft.si.entrance.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HandleHistory }
     * 
     */
    public HandleHistory createHandleHistory() {
        return new HandleHistory();
    }

    /**
     * Create an instance of {@link HandleHistoryResponse }
     * 
     */
    public HandleHistoryResponse createHandleHistoryResponse() {
        return new HandleHistoryResponse();
    }

    /**
     * Create an instance of {@link HandleFile }
     * 
     */
    public HandleFile createHandleFile() {
        return new HandleFile();
    }

    /**
     * Create an instance of {@link HandleFileResponse }
     * 
     */
    public HandleFileResponse createHandleFileResponse() {
        return new HandleFileResponse();
    }

    /**
     * Create an instance of {@link Handle }
     * 
     */
    public Handle createHandle() {
        return new Handle();
    }

    /**
     * Create an instance of {@link HandleResponse }
     * 
     */
    public HandleResponse createHandleResponse() {
        return new HandleResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Handle }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.entrance.si.neusoft.com/", name = "handle")
    public JAXBElement<Handle> createHandle(Handle value) {
        return new JAXBElement<Handle>(_Handle_QNAME, Handle.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HandleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.entrance.si.neusoft.com/", name = "handleResponse")
    public JAXBElement<HandleResponse> createHandleResponse(HandleResponse value) {
        return new JAXBElement<HandleResponse>(_HandleResponse_QNAME, HandleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.entrance.si.neusoft.com/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HandleHistory }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.entrance.si.neusoft.com/", name = "handleHistory")
    public JAXBElement<HandleHistory> createHandleHistory(HandleHistory value) {
        return new JAXBElement<HandleHistory>(_HandleHistory_QNAME, HandleHistory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HandleHistoryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.entrance.si.neusoft.com/", name = "handleHistoryResponse")
    public JAXBElement<HandleHistoryResponse> createHandleHistoryResponse(HandleHistoryResponse value) {
        return new JAXBElement<HandleHistoryResponse>(_HandleHistoryResponse_QNAME, HandleHistoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HandleFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.entrance.si.neusoft.com/", name = "handleFile")
    public JAXBElement<HandleFile> createHandleFile(HandleFile value) {
        return new JAXBElement<HandleFile>(_HandleFile_QNAME, HandleFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HandleFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.entrance.si.neusoft.com/", name = "handleFileResponse")
    public JAXBElement<HandleFileResponse> createHandleFileResponse(HandleFileResponse value) {
        return new JAXBElement<HandleFileResponse>(_HandleFileResponse_QNAME, HandleFileResponse.class, null, value);
    }

}
