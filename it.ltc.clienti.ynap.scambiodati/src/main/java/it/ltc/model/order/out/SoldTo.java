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
 *         &lt;element ref="{}ST_Name"/>
 *         &lt;element ref="{}ST_Addr1"/>
 *         &lt;element ref="{}ST_Addr2" minOccurs="0"/>
 *         &lt;element ref="{}ST_City"/>
 *         &lt;element ref="{}ST_ZIP"/>
 *         &lt;element ref="{}ST_Prov"/>
 *         &lt;element ref="{}ST_CountryCode"/>
 *         &lt;element ref="{}ST_Phone"/>
 *         &lt;element ref="{}ST_Email"/>
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
@XmlRootElement(name = "SoldTo")
public class SoldTo {

    @XmlElement(name = "ST_Name", required = true)
    protected String stName;
    @XmlElement(name = "ST_Addr1", required = true)
    protected String stAddr1;
    @XmlElement(name = "ST_Addr2")
    protected String stAddr2;
    @XmlElement(name = "ST_City", required = true)
    protected String stCity;
    @XmlElement(name = "ST_ZIP", required = true)
    protected String stzip;
    @XmlElement(name = "ST_Prov", required = true)
    protected String stProv;
    @XmlElement(name = "ST_CountryCode", required = true)
    protected String stCountryCode;
    @XmlElement(name = "ST_Phone", required = true)
    protected String stPhone;
    @XmlElement(name = "ST_Email", required = true)
    protected String stEmail;

    /**
     * Recupera il valore della proprietà stName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTName() {
        return stName;
    }

    /**
     * Imposta il valore della proprietà stName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTName(String value) {
        this.stName = value;
    }

    /**
     * Recupera il valore della proprietà stAddr1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTAddr1() {
        return stAddr1;
    }

    /**
     * Imposta il valore della proprietà stAddr1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTAddr1(String value) {
        this.stAddr1 = value;
    }

    /**
     * Recupera il valore della proprietà stAddr2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTAddr2() {
        return stAddr2;
    }

    /**
     * Imposta il valore della proprietà stAddr2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTAddr2(String value) {
        this.stAddr2 = value;
    }

    /**
     * Recupera il valore della proprietà stCity.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTCity() {
        return stCity;
    }

    /**
     * Imposta il valore della proprietà stCity.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTCity(String value) {
        this.stCity = value;
    }

    /**
     * Recupera il valore della proprietà stzip.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTZIP() {
        return stzip;
    }

    /**
     * Imposta il valore della proprietà stzip.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTZIP(String value) {
        this.stzip = value;
    }

    /**
     * Recupera il valore della proprietà stProv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTProv() {
        return stProv;
    }

    /**
     * Imposta il valore della proprietà stProv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTProv(String value) {
        this.stProv = value;
    }

    /**
     * Recupera il valore della proprietà stCountryCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTCountryCode() {
        return stCountryCode;
    }

    /**
     * Imposta il valore della proprietà stCountryCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTCountryCode(String value) {
        this.stCountryCode = value;
    }

    /**
     * Recupera il valore della proprietà stPhone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTPhone() {
        return stPhone;
    }

    /**
     * Imposta il valore della proprietà stPhone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTPhone(String value) {
        this.stPhone = value;
    }

    /**
     * Recupera il valore della proprietà stEmail.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTEmail() {
        return stEmail;
    }

    /**
     * Imposta il valore della proprietà stEmail.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTEmail(String value) {
        this.stEmail = value;
    }

}
