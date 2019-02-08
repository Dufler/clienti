//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.10 alle 11:21:15 AM CEST 
//


package it.ltc.model.order.in;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}PACK_ID"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}PACK_Type"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}PACK_Weight"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}PACK_TN" minOccurs="0"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}PACK_Cons_ID" minOccurs="0"/>
 *         &lt;element ref="{http://WHExchange.EAI.INT_ORD.Messaging.Schemas.Warehouse.schWhOrdersInbound}PACK_InvoiceNum" minOccurs="0"/>
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
@XmlRootElement(name = "Package")
public class Package {

    @XmlElement(name = "PACK_ID", required = true)
    protected String packid;
    @XmlElement(name = "PACK_Type", required = true)
    protected String packType;
    @XmlElement(name = "PACK_Weight", required = true)
    protected BigDecimal packWeight;
    @XmlElement(name = "PACK_TN")
    protected String packtn;
    @XmlElement(name = "PACK_Cons_ID")
    protected String packConsID;
    @XmlElement(name = "PACK_InvoiceNum")
    protected String packInvoiceNum;

    /**
     * Recupera il valore della proprietà packid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPACKID() {
        return packid;
    }

    /**
     * Imposta il valore della proprietà packid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPACKID(String value) {
        this.packid = value;
    }

    /**
     * Recupera il valore della proprietà packType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPACKType() {
        return packType;
    }

    /**
     * Imposta il valore della proprietà packType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPACKType(String value) {
        this.packType = value;
    }

    /**
     * Recupera il valore della proprietà packWeight.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPACKWeight() {
        return packWeight;
    }

    /**
     * Imposta il valore della proprietà packWeight.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPACKWeight(BigDecimal value) {
        this.packWeight = value;
    }

    /**
     * Recupera il valore della proprietà packtn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPACKTN() {
        return packtn;
    }

    /**
     * Imposta il valore della proprietà packtn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPACKTN(String value) {
        this.packtn = value;
    }

    /**
     * Recupera il valore della proprietà packConsID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPACKConsID() {
        return packConsID;
    }

    /**
     * Imposta il valore della proprietà packConsID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPACKConsID(String value) {
        this.packConsID = value;
    }

    /**
     * Recupera il valore della proprietà packInvoiceNum.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPACKInvoiceNum() {
        return packInvoiceNum;
    }

    /**
     * Imposta il valore della proprietà packInvoiceNum.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPACKInvoiceNum(String value) {
        this.packInvoiceNum = value;
    }

}
