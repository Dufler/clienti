//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.07.20 alle 12:01:50 PM CEST 
//


package it.ltc.model.item.out;

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
 *         &lt;element ref="{}MOV_ID"/>
 *         &lt;element ref="{}MOV_Date"/>
 *         &lt;element ref="{}MOV_User"/>
 *         &lt;element ref="{}MOV_Note1"/>
 *         &lt;element ref="{}MOV_Type"/>
 *         &lt;element ref="{}MOV_CarrierID"/>
 *         &lt;element ref="{}MOV_UDC"/>
 *         &lt;element ref="{}From"/>
 *         &lt;element ref="{}To"/>
 *         &lt;element ref="{}Items"/>
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
@XmlRootElement(name = "Mov")
public class Mov {

    @XmlElement(name = "MOV_ID")
    protected int movid;
    @XmlElement(name = "MOV_Date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar movDate;
    @XmlElement(name = "MOV_User")
    protected int movUser;
    @XmlElement(name = "MOV_Note1", required = true)
    protected String movNote1;
    @XmlElement(name = "MOV_Type")
    protected int movType;
    @XmlElement(name = "MOV_CarrierID")
    protected int movCarrierID;
    @XmlElement(name = "MOV_UDC", required = true)
    protected String movudc;
    @XmlElement(name = "From", required = true)
    protected From from;
    @XmlElement(name = "To", required = true)
    protected To to;
    @XmlElement(name = "Items", required = true)
    protected Items items;

    /**
     * Recupera il valore della proprietà movid.
     * 
     */
    public int getMOVID() {
        return movid;
    }

    /**
     * Imposta il valore della proprietà movid.
     * 
     */
    public void setMOVID(int value) {
        this.movid = value;
    }

    /**
     * Recupera il valore della proprietà movDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMOVDate() {
        return movDate;
    }

    /**
     * Imposta il valore della proprietà movDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMOVDate(XMLGregorianCalendar value) {
        this.movDate = value;
    }

    /**
     * Recupera il valore della proprietà movUser.
     * 
     */
    public int getMOVUser() {
        return movUser;
    }

    /**
     * Imposta il valore della proprietà movUser.
     * 
     */
    public void setMOVUser(int value) {
        this.movUser = value;
    }

    /**
     * Recupera il valore della proprietà movNote1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMOVNote1() {
        return movNote1;
    }

    /**
     * Imposta il valore della proprietà movNote1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMOVNote1(String value) {
        this.movNote1 = value;
    }

    /**
     * Recupera il valore della proprietà movType.
     * 
     */
    public int getMOVType() {
        return movType;
    }

    /**
     * Imposta il valore della proprietà movType.
     * 
     */
    public void setMOVType(int value) {
        this.movType = value;
    }

    /**
     * Recupera il valore della proprietà movCarrierID.
     * 
     */
    public int getMOVCarrierID() {
        return movCarrierID;
    }

    /**
     * Imposta il valore della proprietà movCarrierID.
     * 
     */
    public void setMOVCarrierID(int value) {
        this.movCarrierID = value;
    }

    /**
     * Recupera il valore della proprietà movudc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMOVUDC() {
        return movudc;
    }

    /**
     * Imposta il valore della proprietà movudc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMOVUDC(String value) {
        this.movudc = value;
    }

    /**
     * Recupera il valore della proprietà from.
     * 
     * @return
     *     possible object is
     *     {@link From }
     *     
     */
    public From getFrom() {
        return from;
    }

    /**
     * Imposta il valore della proprietà from.
     * 
     * @param value
     *     allowed object is
     *     {@link From }
     *     
     */
    public void setFrom(From value) {
        this.from = value;
    }

    /**
     * Recupera il valore della proprietà to.
     * 
     * @return
     *     possible object is
     *     {@link To }
     *     
     */
    public To getTo() {
        return to;
    }

    /**
     * Imposta il valore della proprietà to.
     * 
     * @param value
     *     allowed object is
     *     {@link To }
     *     
     */
    public void setTo(To value) {
        this.to = value;
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
