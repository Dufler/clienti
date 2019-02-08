//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.07.20 alle 12:01:17 PM CEST 
//


package it.ltc.model.item.in;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.ltc.model.item.in package. 
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

    private final static QName _MOVID_QNAME = new QName("", "MOV_ID");
    private final static QName _ITLOADLOCATION_QNAME = new QName("", "IT_LOADLOCATION");
    private final static QName _MOVNote2_QNAME = new QName("", "MOV_Note2");
    private final static QName _MOVConfDate_QNAME = new QName("", "MOV_ConfDate");
    private final static QName _ITRMMID_QNAME = new QName("", "IT_RMM_ID");
    private final static QName _ITCode_QNAME = new QName("", "IT_Code");
    private final static QName _FromID_QNAME = new QName("", "From_ID");
    private final static QName _MOVDate_QNAME = new QName("", "MOV_Date");
    private final static QName _ToID_QNAME = new QName("", "To_ID");
    private final static QName _ITConfDate_QNAME = new QName("", "IT_ConfDate");
    private final static QName _ITLostReason_QNAME = new QName("", "IT_LostReason");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.ltc.model.item.in
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link From }
     * 
     */
    public From createFrom() {
        return new From();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link Mov }
     * 
     */
    public Mov createMov() {
        return new Mov();
    }

    /**
     * Create an instance of {@link To }
     * 
     */
    public To createTo() {
        return new To();
    }

    /**
     * Create an instance of {@link Items }
     * 
     */
    public Items createItems() {
        return new Items();
    }

    /**
     * Create an instance of {@link Movs }
     * 
     */
    public Movs createMovs() {
        return new Movs();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MOV_ID")
    public JAXBElement<Integer> createMOVID(Integer value) {
        return new JAXBElement<Integer>(_MOVID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_LOADLOCATION")
    public JAXBElement<String> createITLOADLOCATION(String value) {
        return new JAXBElement<String>(_ITLOADLOCATION_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MOV_Note2")
    public JAXBElement<String> createMOVNote2(String value) {
        return new JAXBElement<String>(_MOVNote2_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MOV_ConfDate")
    public JAXBElement<XMLGregorianCalendar> createMOVConfDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_MOVConfDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_RMM_ID")
    public JAXBElement<Integer> createITRMMID(Integer value) {
        return new JAXBElement<Integer>(_ITRMMID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Code")
    public JAXBElement<String> createITCode(String value) {
        return new JAXBElement<String>(_ITCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "From_ID")
    public JAXBElement<Integer> createFromID(Integer value) {
        return new JAXBElement<Integer>(_FromID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MOV_Date")
    public JAXBElement<XMLGregorianCalendar> createMOVDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_MOVDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "To_ID")
    public JAXBElement<Integer> createToID(Integer value) {
        return new JAXBElement<Integer>(_ToID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ConfDate")
    public JAXBElement<XMLGregorianCalendar> createITConfDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ITConfDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_LostReason")
    public JAXBElement<Integer> createITLostReason(Integer value) {
        return new JAXBElement<Integer>(_ITLostReason_QNAME, Integer.class, null, value);
    }

}
