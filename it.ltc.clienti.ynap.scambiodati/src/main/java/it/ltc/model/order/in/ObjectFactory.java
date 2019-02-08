//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.10 alle 11:21:15 AM CEST 
//


package it.ltc.model.order.in;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.ltc.model.order.in package. 
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

    private final static QName _PACKWeight_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "PACK_Weight");
    private final static QName _ITPackID_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "IT_Pack_ID");
    private final static QName _ORDGW_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "ORD_GW");
    private final static QName _ORDRef_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "ORD_Ref");
    private final static QName _PACKTN_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "PACK_TN");
    private final static QName _ITInvoicePrice_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "IT_InvoicePrice");
    private final static QName _SHIPInvoiceNum_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "SHIP_InvoiceNum");
    private final static QName _ITCodiceMatricola_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "IT_CodiceMatricola");
    private final static QName _ORDNum_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "ORD_Num");
    private final static QName _ITConfDate_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "IT_ConfDate");
    private final static QName _ORDDeleted_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "ORD_Deleted");
    private final static QName _SHIPEffServLev_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "SHIP_EffServLev");
    private final static QName _PACKInvoiceNum_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "PACK_InvoiceNum");
    private final static QName _PACKConsID_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "PACK_Cons_ID");
    private final static QName _SHIPDate_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "SHIP_Date");
    private final static QName _SHIPTNRS_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "SHIP_TN_RS");
    private final static QName _SHIPConsID_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "SHIP_Cons_ID");
    private final static QName _ITRMMID_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "IT_RMM_ID");
    private final static QName _ORDType_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "ORD_Type");
    private final static QName _PACKID_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "PACK_ID");
    private final static QName _SHIPTN_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "SHIP_TN");
    private final static QName _ORDNote_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "ORD_Note");
    private final static QName _PACKType_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "PACK_Type");
    private final static QName _ORDDate_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "ORD_Date");
    private final static QName _ITNotShippedNote_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "IT_NotShippedNote");
    private final static QName _ITNotShippedType_QNAME = new QName("http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", "IT_NotShippedType");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.ltc.model.order.in
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Orders }
     * 
     */
    public Orders createOrders() {
        return new Orders();
    }

    /**
     * Create an instance of {@link Order }
     * 
     */
    public Order createOrder() {
        return new Order();
    }

    /**
     * Create an instance of {@link Shipment }
     * 
     */
    public Shipment createShipment() {
        return new Shipment();
    }

    /**
     * Create an instance of {@link Packages }
     * 
     */
    public Packages createPackages() {
        return new Packages();
    }

    /**
     * Create an instance of {@link Package }
     * 
     */
    public Package createPackage() {
        return new Package();
    }

    /**
     * Create an instance of {@link Items }
     * 
     */
    public Items createItems() {
        return new Items();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "PACK_Weight")
    public JAXBElement<BigDecimal> createPACKWeight(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_PACKWeight_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "IT_Pack_ID")
    public JAXBElement<String> createITPackID(String value) {
        return new JAXBElement<String>(_ITPackID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "ORD_GW")
    public JAXBElement<String> createORDGW(String value) {
        return new JAXBElement<String>(_ORDGW_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "ORD_Ref")
    public JAXBElement<Integer> createORDRef(Integer value) {
        return new JAXBElement<Integer>(_ORDRef_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "PACK_TN")
    public JAXBElement<String> createPACKTN(String value) {
        return new JAXBElement<String>(_PACKTN_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "IT_InvoicePrice")
    public JAXBElement<BigDecimal> createITInvoicePrice(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITInvoicePrice_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "SHIP_InvoiceNum")
    public JAXBElement<String> createSHIPInvoiceNum(String value) {
        return new JAXBElement<String>(_SHIPInvoiceNum_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "IT_CodiceMatricola")
    public JAXBElement<String> createITCodiceMatricola(String value) {
        return new JAXBElement<String>(_ITCodiceMatricola_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "ORD_Num")
    public JAXBElement<String> createORDNum(String value) {
        return new JAXBElement<String>(_ORDNum_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "IT_ConfDate")
    public JAXBElement<XMLGregorianCalendar> createITConfDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ITConfDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "ORD_Deleted")
    public JAXBElement<Integer> createORDDeleted(Integer value) {
        return new JAXBElement<Integer>(_ORDDeleted_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "SHIP_EffServLev")
    public JAXBElement<Integer> createSHIPEffServLev(Integer value) {
        return new JAXBElement<Integer>(_SHIPEffServLev_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "PACK_InvoiceNum")
    public JAXBElement<String> createPACKInvoiceNum(String value) {
        return new JAXBElement<String>(_PACKInvoiceNum_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "PACK_Cons_ID")
    public JAXBElement<String> createPACKConsID(String value) {
        return new JAXBElement<String>(_PACKConsID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "SHIP_Date")
    public JAXBElement<XMLGregorianCalendar> createSHIPDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_SHIPDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "SHIP_TN_RS")
    public JAXBElement<String> createSHIPTNRS(String value) {
        return new JAXBElement<String>(_SHIPTNRS_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "SHIP_Cons_ID")
    public JAXBElement<String> createSHIPConsID(String value) {
        return new JAXBElement<String>(_SHIPConsID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "IT_RMM_ID")
    public JAXBElement<Integer> createITRMMID(Integer value) {
        return new JAXBElement<Integer>(_ITRMMID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "ORD_Type")
    public JAXBElement<Integer> createORDType(Integer value) {
        return new JAXBElement<Integer>(_ORDType_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "PACK_ID")
    public JAXBElement<String> createPACKID(String value) {
        return new JAXBElement<String>(_PACKID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "SHIP_TN")
    public JAXBElement<String> createSHIPTN(String value) {
        return new JAXBElement<String>(_SHIPTN_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "ORD_Note")
    public JAXBElement<String> createORDNote(String value) {
        return new JAXBElement<String>(_ORDNote_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "PACK_Type")
    public JAXBElement<String> createPACKType(String value) {
        return new JAXBElement<String>(_PACKType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "ORD_Date")
    public JAXBElement<XMLGregorianCalendar> createORDDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ORDDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "IT_NotShippedNote")
    public JAXBElement<String> createITNotShippedNote(String value) {
        return new JAXBElement<String>(_ITNotShippedNote_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound", name = "IT_NotShippedType")
    public JAXBElement<String> createITNotShippedType(String value) {
        return new JAXBElement<String>(_ITNotShippedType_QNAME, String.class, null, value);
    }

}
