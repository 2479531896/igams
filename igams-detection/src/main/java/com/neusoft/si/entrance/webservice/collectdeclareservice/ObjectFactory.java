
package com.neusoft.si.entrance.webservice.collectdeclareservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.neusoft.si.entrance.webservice.collectdeclareservice package. 
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

    private final static QName _AfterDeclareResponse_QNAME = new QName("http://collectdeclareservice.webservice.entrance.si.neusoft.com/", "afterDeclareResponse");
    private final static QName _TotalDeclare_QNAME = new QName("http://collectdeclareservice.webservice.entrance.si.neusoft.com/", "totalDeclare");
    private final static QName _SingleDeclare_QNAME = new QName("http://collectdeclareservice.webservice.entrance.si.neusoft.com/", "singleDeclare");
    private final static QName _SingleDeclareResponse_QNAME = new QName("http://collectdeclareservice.webservice.entrance.si.neusoft.com/", "singleDeclareResponse");
    private final static QName _Exception_QNAME = new QName("http://collectdeclareservice.webservice.entrance.si.neusoft.com/", "Exception");
    private final static QName _TotalDeclareResponse_QNAME = new QName("http://collectdeclareservice.webservice.entrance.si.neusoft.com/", "totalDeclareResponse");
    private final static QName _AfterDeclare_QNAME = new QName("http://collectdeclareservice.webservice.entrance.si.neusoft.com/", "afterDeclare");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.neusoft.si.entrance.webservice.collectdeclareservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AfterDeclareResponse }
     * 
     */
    public AfterDeclareResponse createAfterDeclareResponse() {
        return new AfterDeclareResponse();
    }

    /**
     * Create an instance of {@link SingleDeclare }
     * 
     */
    public SingleDeclare createSingleDeclare() {
        return new SingleDeclare();
    }

    /**
     * Create an instance of {@link TotalDeclare }
     * 
     */
    public TotalDeclare createTotalDeclare() {
        return new TotalDeclare();
    }

    /**
     * Create an instance of {@link SingleDeclareResponse }
     * 
     */
    public SingleDeclareResponse createSingleDeclareResponse() {
        return new SingleDeclareResponse();
    }

    /**
     * Create an instance of {@link AfterDeclare }
     * 
     */
    public AfterDeclare createAfterDeclare() {
        return new AfterDeclare();
    }

    /**
     * Create an instance of {@link TotalDeclareResponse }
     * 
     */
    public TotalDeclareResponse createTotalDeclareResponse() {
        return new TotalDeclareResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AfterDeclareResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://collectdeclareservice.webservice.entrance.si.neusoft.com/", name = "afterDeclareResponse")
    public JAXBElement<AfterDeclareResponse> createAfterDeclareResponse(AfterDeclareResponse value) {
        return new JAXBElement<AfterDeclareResponse>(_AfterDeclareResponse_QNAME, AfterDeclareResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TotalDeclare }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://collectdeclareservice.webservice.entrance.si.neusoft.com/", name = "totalDeclare")
    public JAXBElement<TotalDeclare> createTotalDeclare(TotalDeclare value) {
        return new JAXBElement<TotalDeclare>(_TotalDeclare_QNAME, TotalDeclare.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SingleDeclare }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://collectdeclareservice.webservice.entrance.si.neusoft.com/", name = "singleDeclare")
    public JAXBElement<SingleDeclare> createSingleDeclare(SingleDeclare value) {
        return new JAXBElement<SingleDeclare>(_SingleDeclare_QNAME, SingleDeclare.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SingleDeclareResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://collectdeclareservice.webservice.entrance.si.neusoft.com/", name = "singleDeclareResponse")
    public JAXBElement<SingleDeclareResponse> createSingleDeclareResponse(SingleDeclareResponse value) {
        return new JAXBElement<SingleDeclareResponse>(_SingleDeclareResponse_QNAME, SingleDeclareResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://collectdeclareservice.webservice.entrance.si.neusoft.com/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TotalDeclareResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://collectdeclareservice.webservice.entrance.si.neusoft.com/", name = "totalDeclareResponse")
    public JAXBElement<TotalDeclareResponse> createTotalDeclareResponse(TotalDeclareResponse value) {
        return new JAXBElement<TotalDeclareResponse>(_TotalDeclareResponse_QNAME, TotalDeclareResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AfterDeclare }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://collectdeclareservice.webservice.entrance.si.neusoft.com/", name = "afterDeclare")
    public JAXBElement<AfterDeclare> createAfterDeclare(AfterDeclare value) {
        return new JAXBElement<AfterDeclare>(_AfterDeclare_QNAME, AfterDeclare.class, null, value);
    }

}
