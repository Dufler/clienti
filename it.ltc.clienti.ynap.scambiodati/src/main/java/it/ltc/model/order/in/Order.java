//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.10 alle 11:21:15 AM CEST 
//


package it.ltc.model.order.in;

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
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}ORD_Date"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}ORD_Num"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}ORD_Deleted"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}ORD_Ref"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}ORD_Type"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}ORD_Note" minOccurs="0"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}ORD_GW" minOccurs="0"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}Shipment"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}Items"/>
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
    @XmlElement(name = "ORD_Deleted")
    protected int ordDeleted;
    @XmlElement(name = "ORD_Ref")
    protected int ordRef;
    @XmlElement(name = "ORD_Type")
    protected int ordType;
    @XmlElement(name = "ORD_Note")
    protected String ordNote;
    @XmlElement(name = "ORD_GW")
    protected String ordgw;
    @XmlElement(name = "Shipment", required = true)
    protected Shipment shipment;
    @XmlElement(name = "Items", required = true)
    protected Items items;

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
     * Recupera il valore della proprietà ordDeleted.
     * 
     */
    public int getORDDeleted() {
        return ordDeleted;
    }

    /**
     * Imposta il valore della proprietà ordDeleted.
     * 
     */
    public void setORDDeleted(int value) {
        this.ordDeleted = value;
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

}
