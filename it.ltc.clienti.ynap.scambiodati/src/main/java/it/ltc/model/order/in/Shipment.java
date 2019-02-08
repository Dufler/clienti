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
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}SHIP_EffServLev" minOccurs="0"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}SHIP_Date" minOccurs="0"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}SHIP_TN" minOccurs="0"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}SHIP_TN_RS" minOccurs="0"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}SHIP_Cons_ID" minOccurs="0"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}SHIP_InvoiceNum" minOccurs="0"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}Packages"/>
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
@XmlRootElement(name = "Shipment")
public class Shipment {

    @XmlElement(name = "SHIP_EffServLev")
    protected Integer shipEffServLev;
    @XmlElement(name = "SHIP_Date")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar shipDate;
    @XmlElement(name = "SHIP_TN")
    protected String shiptn;
    @XmlElement(name = "SHIP_TN_RS")
    protected String shiptnrs;
    @XmlElement(name = "SHIP_Cons_ID")
    protected String shipConsID;
    @XmlElement(name = "SHIP_InvoiceNum")
    protected String shipInvoiceNum;
    @XmlElement(name = "Packages", required = true)
    protected Packages packages;

    /**
     * Recupera il valore della proprietà shipEffServLev.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSHIPEffServLev() {
        return shipEffServLev;
    }

    /**
     * Imposta il valore della proprietà shipEffServLev.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSHIPEffServLev(Integer value) {
        this.shipEffServLev = value;
    }

    /**
     * Recupera il valore della proprietà shipDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSHIPDate() {
        return shipDate;
    }

    /**
     * Imposta il valore della proprietà shipDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSHIPDate(XMLGregorianCalendar value) {
        this.shipDate = value;
    }

    /**
     * Recupera il valore della proprietà shiptn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHIPTN() {
        return shiptn;
    }

    /**
     * Imposta il valore della proprietà shiptn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHIPTN(String value) {
        this.shiptn = value;
    }

    /**
     * Recupera il valore della proprietà shiptnrs.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHIPTNRS() {
        return shiptnrs;
    }

    /**
     * Imposta il valore della proprietà shiptnrs.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHIPTNRS(String value) {
        this.shiptnrs = value;
    }

    /**
     * Recupera il valore della proprietà shipConsID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHIPConsID() {
        return shipConsID;
    }

    /**
     * Imposta il valore della proprietà shipConsID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHIPConsID(String value) {
        this.shipConsID = value;
    }

    /**
     * Recupera il valore della proprietà shipInvoiceNum.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHIPInvoiceNum() {
        return shipInvoiceNum;
    }

    /**
     * Imposta il valore della proprietà shipInvoiceNum.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHIPInvoiceNum(String value) {
        this.shipInvoiceNum = value;
    }

    /**
     * Recupera il valore della proprietà packages.
     * 
     * @return
     *     possible object is
     *     {@link Packages }
     *     
     */
    public Packages getPackages() {
        return packages;
    }

    /**
     * Imposta il valore della proprietà packages.
     * 
     * @param value
     *     allowed object is
     *     {@link Packages }
     *     
     */
    public void setPackages(Packages value) {
        this.packages = value;
    }

}
