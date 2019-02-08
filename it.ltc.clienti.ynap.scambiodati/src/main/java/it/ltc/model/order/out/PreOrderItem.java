//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.07.20 alle 11:28:44 AM CEST 
//


package it.ltc.model.order.out;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{}PO_IT_ExpectedShipDate"/>
 *         &lt;element ref="{}PO_IT_Codice10" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Size" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Price" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_ArtSti" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_ColSize" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Disc" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Comp" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Sex" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Marchio" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Macro" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Micro" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_MadeIn" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Customs" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Color" minOccurs="0"/>
 *         &lt;element ref="{}PO_IT_Note" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;attribute name="Row" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
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
@XmlRootElement(name = "PreOrder_Item")
public class PreOrderItem {

    @XmlElement(name = "PO_IT_ExpectedShipDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar poitExpectedShipDate;
    @XmlElement(name = "PO_IT_Codice10")
    protected String poitCodice10;
    @XmlElement(name = "PO_IT_Size")
    protected String poitSize;
    @XmlElement(name = "PO_IT_Price")
    protected BigDecimal poitPrice;
    @XmlElement(name = "PO_IT_ArtSti")
    protected String poitArtSti;
    @XmlElement(name = "PO_IT_ColSize")
    protected String poitColSize;
    @XmlElement(name = "PO_IT_Disc")
    protected BigDecimal poitDisc;
    @XmlElement(name = "PO_IT_Comp")
    protected String poitComp;
    @XmlElement(name = "PO_IT_Sex")
    protected String poitSex;
    @XmlElement(name = "PO_IT_Marchio")
    protected String poitMarchio;
    @XmlElement(name = "PO_IT_Macro")
    protected String poitMacro;
    @XmlElement(name = "PO_IT_Micro")
    protected String poitMicro;
    @XmlElement(name = "PO_IT_MadeIn")
    protected String poitMadeIn;
    @XmlElement(name = "PO_IT_Customs")
    protected String poitCustoms;
    @XmlElement(name = "PO_IT_Color")
    protected String poitColor;
    @XmlElement(name = "PO_IT_Note")
    protected String poitNote;
    @XmlAttribute(name = "Row", required = true)
    protected int row;

    /**
     * Recupera il valore della proprietà poitExpectedShipDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPOITExpectedShipDate() {
        return poitExpectedShipDate;
    }

    /**
     * Imposta il valore della proprietà poitExpectedShipDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPOITExpectedShipDate(XMLGregorianCalendar value) {
        this.poitExpectedShipDate = value;
    }

    /**
     * Recupera il valore della proprietà poitCodice10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITCodice10() {
        return poitCodice10;
    }

    /**
     * Imposta il valore della proprietà poitCodice10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITCodice10(String value) {
        this.poitCodice10 = value;
    }

    /**
     * Recupera il valore della proprietà poitSize.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITSize() {
        return poitSize;
    }

    /**
     * Imposta il valore della proprietà poitSize.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITSize(String value) {
        this.poitSize = value;
    }

    /**
     * Recupera il valore della proprietà poitPrice.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPOITPrice() {
        return poitPrice;
    }

    /**
     * Imposta il valore della proprietà poitPrice.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPOITPrice(BigDecimal value) {
        this.poitPrice = value;
    }

    /**
     * Recupera il valore della proprietà poitArtSti.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITArtSti() {
        return poitArtSti;
    }

    /**
     * Imposta il valore della proprietà poitArtSti.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITArtSti(String value) {
        this.poitArtSti = value;
    }

    /**
     * Recupera il valore della proprietà poitColSize.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITColSize() {
        return poitColSize;
    }

    /**
     * Imposta il valore della proprietà poitColSize.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITColSize(String value) {
        this.poitColSize = value;
    }

    /**
     * Recupera il valore della proprietà poitDisc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPOITDisc() {
        return poitDisc;
    }

    /**
     * Imposta il valore della proprietà poitDisc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPOITDisc(BigDecimal value) {
        this.poitDisc = value;
    }

    /**
     * Recupera il valore della proprietà poitComp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITComp() {
        return poitComp;
    }

    /**
     * Imposta il valore della proprietà poitComp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITComp(String value) {
        this.poitComp = value;
    }

    /**
     * Recupera il valore della proprietà poitSex.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITSex() {
        return poitSex;
    }

    /**
     * Imposta il valore della proprietà poitSex.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITSex(String value) {
        this.poitSex = value;
    }

    /**
     * Recupera il valore della proprietà poitMarchio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITMarchio() {
        return poitMarchio;
    }

    /**
     * Imposta il valore della proprietà poitMarchio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITMarchio(String value) {
        this.poitMarchio = value;
    }

    /**
     * Recupera il valore della proprietà poitMacro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITMacro() {
        return poitMacro;
    }

    /**
     * Imposta il valore della proprietà poitMacro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITMacro(String value) {
        this.poitMacro = value;
    }

    /**
     * Recupera il valore della proprietà poitMicro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITMicro() {
        return poitMicro;
    }

    /**
     * Imposta il valore della proprietà poitMicro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITMicro(String value) {
        this.poitMicro = value;
    }

    /**
     * Recupera il valore della proprietà poitMadeIn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITMadeIn() {
        return poitMadeIn;
    }

    /**
     * Imposta il valore della proprietà poitMadeIn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITMadeIn(String value) {
        this.poitMadeIn = value;
    }

    /**
     * Recupera il valore della proprietà poitCustoms.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITCustoms() {
        return poitCustoms;
    }

    /**
     * Imposta il valore della proprietà poitCustoms.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITCustoms(String value) {
        this.poitCustoms = value;
    }

    /**
     * Recupera il valore della proprietà poitColor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITColor() {
        return poitColor;
    }

    /**
     * Imposta il valore della proprietà poitColor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITColor(String value) {
        this.poitColor = value;
    }

    /**
     * Recupera il valore della proprietà poitNote.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOITNote() {
        return poitNote;
    }

    /**
     * Imposta il valore della proprietà poitNote.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOITNote(String value) {
        this.poitNote = value;
    }

    /**
     * Recupera il valore della proprietà row.
     * 
     */
    public int getRow() {
        return row;
    }

    /**
     * Imposta il valore della proprietà row.
     * 
     */
    public void setRow(int value) {
        this.row = value;
    }

}
