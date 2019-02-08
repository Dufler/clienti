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
 *         &lt;element ref="{}IT_RMM_ID"/>
 *         &lt;element ref="{}IT_Code"/>
 *         &lt;element ref="{}IT_Descr"/>
 *         &lt;element ref="{}IT_Season"/>
 *         &lt;element ref="{}IT_AS"/>
 *         &lt;element ref="{}IT_LOADLOCATION" minOccurs="0"/>
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
@XmlRootElement(name = "Item")
public class Item {

    @XmlElement(name = "IT_RMM_ID")
    protected int itrmmid;
    @XmlElement(name = "IT_Code", required = true)
    protected String itCode;
    @XmlElement(name = "IT_Descr", required = true)
    protected String itDescr;
    @XmlElement(name = "IT_Season", required = true)
    protected String itSeason;
    @XmlElement(name = "IT_AS", required = true)
    protected String itas;
    @XmlElement(name = "IT_LOADLOCATION")
    protected String itloadlocation;

    /**
     * Recupera il valore della proprietà itrmmid.
     * 
     */
    public int getITRMMID() {
        return itrmmid;
    }

    /**
     * Imposta il valore della proprietà itrmmid.
     * 
     */
    public void setITRMMID(int value) {
        this.itrmmid = value;
    }

    /**
     * Recupera il valore della proprietà itCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITCode() {
        return itCode;
    }

    /**
     * Imposta il valore della proprietà itCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITCode(String value) {
        this.itCode = value;
    }

    /**
     * Recupera il valore della proprietà itDescr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITDescr() {
        return itDescr;
    }

    /**
     * Imposta il valore della proprietà itDescr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITDescr(String value) {
        this.itDescr = value;
    }

    /**
     * Recupera il valore della proprietà itSeason.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITSeason() {
        return itSeason;
    }

    /**
     * Imposta il valore della proprietà itSeason.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITSeason(String value) {
        this.itSeason = value;
    }

    /**
     * Recupera il valore della proprietà itas.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITAS() {
        return itas;
    }

    /**
     * Imposta il valore della proprietà itas.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITAS(String value) {
        this.itas = value;
    }

    /**
     * Recupera il valore della proprietà itloadlocation.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITLOADLOCATION() {
        return itloadlocation;
    }

    /**
     * Imposta il valore della proprietà itloadlocation.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITLOADLOCATION(String value) {
        this.itloadlocation = value;
    }

}
