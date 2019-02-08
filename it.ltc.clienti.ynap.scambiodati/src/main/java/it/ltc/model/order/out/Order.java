//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.07.20 alle 11:28:44 AM CEST 
//


package it.ltc.model.order.out;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}ORD_Date"/>
 *         &lt;element ref="{}ORD_Num"/>
 *         &lt;element ref="{}ORD_Ref"/>
 *         &lt;element ref="{}ORD_Type"/>
 *         &lt;element ref="{}ORD_Gift"/>
 *         &lt;element ref="{}ORD_Note"/>
 *         &lt;element ref="{}ORD_BoxType"/>
 *         &lt;element ref="{}Receiver"/>
 *         &lt;element ref="{}Shipment"/>
 *         &lt;element ref="{}Invoice"/>
 *         &lt;element ref="{}Items"/>
 *         &lt;element ref="{}PreOrder_Items" minOccurs="0"/>
 *         &lt;element ref="{}ORD_GW" minOccurs="0"/>
 *         &lt;element ref="{}ORD_TermOfSale" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Order")
public class Order {

    @XmlElement(name = "ORD_Date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar ordDate;
    @XmlElement(name = "ORD_Num", required = true)
    protected String ordNum;
    @XmlElement(name = "ORD_Ref")
    protected int ordRef;
    @XmlElement(name = "ORD_Type")
    protected int ordType;
    @XmlElement(name = "ORD_Gift")
    protected int ordGift;
    @XmlElement(name = "ORD_Note", required = true)
    protected String ordNote;
    @XmlElement(name = "ORD_BoxType")
    protected int ordBoxType;
    @XmlElement(name = "Receiver", required = true)
    protected Receiver receiver;
    @XmlElement(name = "Shipment", required = true)
    protected Shipment shipment;
    @XmlElement(name = "Invoice", required = true)
    protected Invoice invoice;
    @XmlElement(name = "Items", required = true)
    protected Items items;
    @XmlElement(name = "PreOrder_Items")
    protected PreOrderItems preOrderItems;
    @XmlElement(name = "ORD_GW")
    protected String ordgw;
    @XmlElement(name = "ORD_TermOfSale")
    protected String ordTermOfSale;

    /**
     * Recupera il valore della proprietà ordDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getORDDate() {
        return ordDate;
    }

    /**
     * Imposta il valore della proprietà ordDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setORDDate(XMLGregorianCalendar value) {
        this.ordDate = value;
    }

    /**
     * Recupera il valore della proprietà ordNum.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDNum() {
        return ordNum;
    }

    /**
     * Imposta il valore della proprietà ordNum.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDNum(String value) {
        this.ordNum = value;
    }

    /**
     * Recupera il valore della proprietà ordRef.
     * 
     */
    public int getORDRef() {
        return ordRef;
    }

    /**
     * Imposta il valore della proprietà ordRef.
     * 
     */
    public void setORDRef(int value) {
        this.ordRef = value;
    }

    /**
     * Recupera il valore della proprietà ordType.
     * 
     */
    public int getORDType() {
        return ordType;
    }

    /**
     * Imposta il valore della proprietà ordType.
     * 
     */
    public void setORDType(int value) {
        this.ordType = value;
    }

    /**
     * Recupera il valore della proprietà ordGift.
     * 
     */
    public int getORDGift() {
        return ordGift;
    }

    /**
     * Imposta il valore della proprietà ordGift.
     * 
     */
    public void setORDGift(int value) {
        this.ordGift = value;
    }

    /**
     * Recupera il valore della proprietà ordNote.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDNote() {
        return ordNote;
    }

    /**
     * Imposta il valore della proprietà ordNote.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDNote(String value) {
        this.ordNote = value;
    }

    /**
     * Recupera il valore della proprietà ordBoxType.
     * 
     */
    public int getORDBoxType() {
        return ordBoxType;
    }

    /**
     * Imposta il valore della proprietà ordBoxType.
     * 
     */
    public void setORDBoxType(int value) {
        this.ordBoxType = value;
    }

    /**
     * Recupera il valore della proprietà receiver.
     * 
     * @return
     *     possible object is
     *     {@link Receiver }
     *     
     */
    public Receiver getReceiver() {
        return receiver;
    }

    /**
     * Imposta il valore della proprietà receiver.
     * 
     * @param value
     *     allowed object is
     *     {@link Receiver }
     *     
     */
    public void setReceiver(Receiver value) {
        this.receiver = value;
    }

    /**
     * Recupera il valore della proprietà shipment.
     * 
     * @return
     *     possible object is
     *     {@link Shipment }
     *     
     */
    public Shipment getShipment() {
        return shipment;
    }

    /**
     * Imposta il valore della proprietà shipment.
     * 
     * @param value
     *     allowed object is
     *     {@link Shipment }
     *     
     */
    public void setShipment(Shipment value) {
        this.shipment = value;
    }

    /**
     * Recupera il valore della proprietà invoice.
     * 
     * @return
     *     possible object is
     *     {@link Invoice }
     *     
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * Imposta il valore della proprietà invoice.
     * 
     * @param value
     *     allowed object is
     *     {@link Invoice }
     *     
     */
    public void setInvoice(Invoice value) {
        this.invoice = value;
    }

    /**
     * Recupera il valore della proprietà items.
     * 
     * @return
     *     possible object is
     *     {@link Items }
     *     
     */
    public Items getItems() {
        return items;
    }

    /**
     * Imposta il valore della proprietà items.
     * 
     * @param value
     *     allowed object is
     *     {@link Items }
     *     
     */
    public void setItems(Items value) {
        this.items = value;
    }

    /**
     * Recupera il valore della proprietà preOrderItems.
     * 
     * @return
     *     possible object is
     *     {@link PreOrderItems }
     *     
     */
    public PreOrderItems getPreOrderItems() {
        return preOrderItems;
    }

    /**
     * Imposta il valore della proprietà preOrderItems.
     * 
     * @param value
     *     allowed object is
     *     {@link PreOrderItems }
     *     
     */
    public void setPreOrderItems(PreOrderItems value) {
        this.preOrderItems = value;
    }

    /**
     * Recupera il valore della proprietà ordgw.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDGW() {
        return ordgw;
    }

    /**
     * Imposta il valore della proprietà ordgw.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDGW(String value) {
        this.ordgw = value;
    }

    /**
     * Recupera il valore della proprietà ordTermOfSale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDTermOfSale() {
        return ordTermOfSale;
    }

    /**
     * Imposta il valore della proprietà ordTermOfSale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDTermOfSale(String value) {
        this.ordTermOfSale = value;
    }

}
