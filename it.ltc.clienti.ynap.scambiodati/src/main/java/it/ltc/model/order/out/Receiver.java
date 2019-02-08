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
 *         &lt;element ref="{}REC_ID"/>
 *         &lt;element ref="{}REC_Name"/>
 *         &lt;element ref="{}REC_ShortName"/>
 *         &lt;element ref="{}REC_Mess" minOccurs="0"/>
 *         &lt;element ref="{}REC_Addr1"/>
 *         &lt;element ref="{}REC_Addr2" minOccurs="0"/>
 *         &lt;element ref="{}REC_CareOf"/>
 *         &lt;element ref="{}REC_City"/>
 *         &lt;element ref="{}REC_CountryCode"/>
 *         &lt;element ref="{}REC_CountryName"/>
 *         &lt;element ref="{}REC_ZIP"/>
 *         &lt;element ref="{}REC_Prov"/>
 *         &lt;element ref="{}REC_Phone" minOccurs="0"/>
 *         &lt;element ref="{}REC_Phone2" minOccurs="0"/>
 *         &lt;element ref="{}REC_Email" minOccurs="0"/>
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
@XmlRootElement(name = "Receiver")
public class Receiver {

    @XmlElement(name = "REC_ID")
    protected int recid;
    @XmlElement(name = "REC_Name", required = true)
    protected String recName;
    @XmlElement(name = "REC_ShortName", required = true)
    protected String recShortName;
    @XmlElement(name = "REC_Mess")
    protected String recMess;
    @XmlElement(name = "REC_Addr1", required = true)
    protected String recAddr1;
    @XmlElement(name = "REC_Addr2")
    protected String recAddr2;
    @XmlElement(name = "REC_CareOf", required = true)
    protected String recCareOf;
    @XmlElement(name = "REC_City", required = true)
    protected String recCity;
    @XmlElement(name = "REC_CountryCode", required = true)
    protected String recCountryCode;
    @XmlElement(name = "REC_CountryName", required = true)
    protected String recCountryName;
    @XmlElement(name = "REC_ZIP", required = true)
    protected String reczip;
    @XmlElement(name = "REC_Prov", required = true)
    protected String recProv;
    @XmlElement(name = "REC_Phone")
    protected String recPhone;
    @XmlElement(name = "REC_Phone2")
    protected String recPhone2;
    @XmlElement(name = "REC_Email")
    protected String recEmail;

    /**
     * Recupera il valore della proprietà recid.
     * 
     */
    public int getRECID() {
        return recid;
    }

    /**
     * Imposta il valore della proprietà recid.
     * 
     */
    public void setRECID(int value) {
        this.recid = value;
    }

    /**
     * Recupera il valore della proprietà recName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECName() {
        return recName;
    }

    /**
     * Imposta il valore della proprietà recName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECName(String value) {
        this.recName = value;
    }

    /**
     * Recupera il valore della proprietà recShortName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECShortName() {
        return recShortName;
    }

    /**
     * Imposta il valore della proprietà recShortName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECShortName(String value) {
        this.recShortName = value;
    }

    /**
     * Recupera il valore della proprietà recMess.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECMess() {
        return recMess;
    }

    /**
     * Imposta il valore della proprietà recMess.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECMess(String value) {
        this.recMess = value;
    }

    /**
     * Recupera il valore della proprietà recAddr1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECAddr1() {
        return recAddr1;
    }

    /**
     * Imposta il valore della proprietà recAddr1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECAddr1(String value) {
        this.recAddr1 = value;
    }

    /**
     * Recupera il valore della proprietà recAddr2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECAddr2() {
        return recAddr2;
    }

    /**
     * Imposta il valore della proprietà recAddr2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECAddr2(String value) {
        this.recAddr2 = value;
    }

    /**
     * Recupera il valore della proprietà recCareOf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECCareOf() {
        return recCareOf;
    }

    /**
     * Imposta il valore della proprietà recCareOf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECCareOf(String value) {
        this.recCareOf = value;
    }

    /**
     * Recupera il valore della proprietà recCity.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECCity() {
        return recCity;
    }

    /**
     * Imposta il valore della proprietà recCity.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECCity(String value) {
        this.recCity = value;
    }

    /**
     * Recupera il valore della proprietà recCountryCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECCountryCode() {
        return recCountryCode;
    }

    /**
     * Imposta il valore della proprietà recCountryCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECCountryCode(String value) {
        this.recCountryCode = value;
    }

    /**
     * Recupera il valore della proprietà recCountryName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECCountryName() {
        return recCountryName;
    }

    /**
     * Imposta il valore della proprietà recCountryName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECCountryName(String value) {
        this.recCountryName = value;
    }

    /**
     * Recupera il valore della proprietà reczip.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECZIP() {
        return reczip;
    }

    /**
     * Imposta il valore della proprietà reczip.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECZIP(String value) {
        this.reczip = value;
    }

    /**
     * Recupera il valore della proprietà recProv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECProv() {
        return recProv;
    }

    /**
     * Imposta il valore della proprietà recProv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECProv(String value) {
        this.recProv = value;
    }

    /**
     * Recupera il valore della proprietà recPhone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECPhone() {
        return recPhone;
    }

    /**
     * Imposta il valore della proprietà recPhone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECPhone(String value) {
        this.recPhone = value;
    }

    /**
     * Recupera il valore della proprietà recPhone2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECPhone2() {
        return recPhone2;
    }

    /**
     * Imposta il valore della proprietà recPhone2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECPhone2(String value) {
        this.recPhone2 = value;
    }

    /**
     * Recupera il valore della proprietà recEmail.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECEmail() {
        return recEmail;
    }

    /**
     * Imposta il valore della proprietà recEmail.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECEmail(String value) {
        this.recEmail = value;
    }

}
