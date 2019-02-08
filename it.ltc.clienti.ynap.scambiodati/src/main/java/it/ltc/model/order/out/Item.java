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
 *         &lt;element ref="{}IT_CodiceMatricola"/>
 *         &lt;element ref="{}IT_ArtSti"/>
 *         &lt;element ref="{}IT_ColSize"/>
 *         &lt;element ref="{}IT_Price"/>
 *         &lt;element ref="{}IT_TransferPrice" minOccurs="0"/>
 *         &lt;element ref="{}IT_TransferPriceCurrISO" minOccurs="0"/>
 *         &lt;element ref="{}IT_Disc"/>
 *         &lt;element ref="{}IT_Comp"/>
 *         &lt;element ref="{}IT_Sex"/>
 *         &lt;element ref="{}IT_Marchio" minOccurs="0"/>
 *         &lt;element ref="{}IT_Macro" minOccurs="0"/>
 *         &lt;element ref="{}IT_Micro" minOccurs="0"/>
 *         &lt;element ref="{}IT_MadeIn" minOccurs="0"/>
 *         &lt;element ref="{}IT_Customs" minOccurs="0"/>
 *         &lt;element ref="{}IT_Color" minOccurs="0"/>
 *         &lt;element ref="{}IT_Size" minOccurs="0"/>
 *         &lt;element ref="{}IT_Note" minOccurs="0"/>
 *         &lt;element ref="{}IT_SoldTo" minOccurs="0"/>
 *         &lt;element ref="{}IT_Invoice" minOccurs="0"/>
 *         &lt;element ref="{}IT_CompType_ID" minOccurs="0"/>
 *         &lt;element ref="{}IT_LinkPhoto" minOccurs="0"/>
 *         &lt;element ref="{}IT_Type" minOccurs="0"/>
 *         &lt;element ref="{}IT_Cost" minOccurs="0"/>
 *         &lt;element ref="{}IT_PriorityLevel" minOccurs="0"/>
 *         &lt;element ref="{}IT_Jw_ItemSerialNumber" minOccurs="0"/>
 *         &lt;element ref="{}IT_Jw_Descr" minOccurs="0"/>
 *         &lt;element ref="{}IT_Jw_Gold750" minOccurs="0"/>
 *         &lt;element ref="{}IT_Jw_Gold375" minOccurs="0"/>
 *         &lt;element ref="{}IT_Jw_Silver" minOccurs="0"/>
 *         &lt;element ref="{}IT_Jw_Carats" minOccurs="0"/>
 *         &lt;element ref="{}IT_Jw_Platinum" minOccurs="0"/>
 *         &lt;element ref="{}IT_Gadget" minOccurs="0"/>
 *         &lt;element ref="{}IT_CustomerOrderID" minOccurs="0"/>
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
@XmlRootElement(name = "Item")
public class Item {

    @XmlElement(name = "IT_RMM_ID")
    protected int itrmmid;
    @XmlElement(name = "IT_CodiceMatricola", required = true)
    protected String itCodiceMatricola;
    @XmlElement(name = "IT_ArtSti", required = true)
    protected String itArtSti;
    @XmlElement(name = "IT_ColSize", required = true)
    protected String itColSize;
    @XmlElement(name = "IT_Price", required = true)
    protected BigDecimal itPrice;
    @XmlElement(name = "IT_TransferPrice")
    protected BigDecimal itTransferPrice;
    @XmlElement(name = "IT_TransferPriceCurrISO")
    protected String itTransferPriceCurrISO;
    @XmlElement(name = "IT_Disc", required = true)
    protected BigDecimal itDisc;
    @XmlElement(name = "IT_Comp", required = true)
    protected String itComp;
    @XmlElement(name = "IT_Sex", required = true)
    protected String itSex;
    @XmlElement(name = "IT_Marchio")
    protected String itMarchio;
    @XmlElement(name = "IT_Macro")
    protected String itMacro;
    @XmlElement(name = "IT_Micro")
    protected String itMicro;
    @XmlElement(name = "IT_MadeIn")
    protected String itMadeIn;
    @XmlElement(name = "IT_Customs")
    protected String itCustoms;
    @XmlElement(name = "IT_Color")
    protected String itColor;
    @XmlElement(name = "IT_Size")
    protected String itSize;
    @XmlElement(name = "IT_Note")
    protected String itNote;
    @XmlElement(name = "IT_SoldTo")
    protected ITSoldTo itSoldTo;
    @XmlElement(name = "IT_Invoice")
    protected ITInvoice itInvoice;
    @XmlElement(name = "IT_CompType_ID")
    protected Integer itCompTypeID;
    @XmlElement(name = "IT_LinkPhoto")
    protected String itLinkPhoto;
    @XmlElement(name = "IT_Type")
    protected String itType;
    @XmlElement(name = "IT_Cost")
    protected BigDecimal itCost;
    @XmlElement(name = "IT_PriorityLevel")
    protected Integer itPriorityLevel;
    @XmlElement(name = "IT_Jw_ItemSerialNumber")
    protected String itJwItemSerialNumber;
    @XmlElement(name = "IT_Jw_Descr")
    protected String itJwDescr;
    @XmlElement(name = "IT_Jw_Gold750")
    protected BigDecimal itJwGold750;
    @XmlElement(name = "IT_Jw_Gold375")
    protected BigDecimal itJwGold375;
    @XmlElement(name = "IT_Jw_Silver")
    protected BigDecimal itJwSilver;
    @XmlElement(name = "IT_Jw_Carats")
    protected BigDecimal itJwCarats;
    @XmlElement(name = "IT_Jw_Platinum")
    protected BigDecimal itJwPlatinum;
    @XmlElement(name = "IT_Gadget")
    protected Integer itGadget;
    @XmlElement(name = "IT_CustomerOrderID")
    protected Integer itCustomerOrderID;
    @XmlAttribute(name = "Row", required = true)
    protected int row;

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
     * Recupera il valore della proprietà itCodiceMatricola.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITCodiceMatricola() {
        return itCodiceMatricola;
    }

    /**
     * Imposta il valore della proprietà itCodiceMatricola.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITCodiceMatricola(String value) {
        this.itCodiceMatricola = value;
    }

    /**
     * Recupera il valore della proprietà itArtSti.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITArtSti() {
        return itArtSti;
    }

    /**
     * Imposta il valore della proprietà itArtSti.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITArtSti(String value) {
        this.itArtSti = value;
    }

    /**
     * Recupera il valore della proprietà itColSize.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITColSize() {
        return itColSize;
    }

    /**
     * Imposta il valore della proprietà itColSize.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITColSize(String value) {
        this.itColSize = value;
    }

    /**
     * Recupera il valore della proprietà itPrice.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getITPrice() {
        return itPrice;
    }

    /**
     * Imposta il valore della proprietà itPrice.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setITPrice(BigDecimal value) {
        this.itPrice = value;
    }

    /**
     * Recupera il valore della proprietà itTransferPrice.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getITTransferPrice() {
        return itTransferPrice;
    }

    /**
     * Imposta il valore della proprietà itTransferPrice.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setITTransferPrice(BigDecimal value) {
        this.itTransferPrice = value;
    }

    /**
     * Recupera il valore della proprietà itTransferPriceCurrISO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITTransferPriceCurrISO() {
        return itTransferPriceCurrISO;
    }

    /**
     * Imposta il valore della proprietà itTransferPriceCurrISO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITTransferPriceCurrISO(String value) {
        this.itTransferPriceCurrISO = value;
    }

    /**
     * Recupera il valore della proprietà itDisc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getITDisc() {
        return itDisc;
    }

    /**
     * Imposta il valore della proprietà itDisc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setITDisc(BigDecimal value) {
        this.itDisc = value;
    }

    /**
     * Recupera il valore della proprietà itComp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITComp() {
        return itComp;
    }

    /**
     * Imposta il valore della proprietà itComp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITComp(String value) {
        this.itComp = value;
    }

    /**
     * Recupera il valore della proprietà itSex.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITSex() {
        return itSex;
    }

    /**
     * Imposta il valore della proprietà itSex.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITSex(String value) {
        this.itSex = value;
    }

    /**
     * Recupera il valore della proprietà itMarchio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITMarchio() {
        return itMarchio;
    }

    /**
     * Imposta il valore della proprietà itMarchio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITMarchio(String value) {
        this.itMarchio = value;
    }

    /**
     * Recupera il valore della proprietà itMacro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITMacro() {
        return itMacro;
    }

    /**
     * Imposta il valore della proprietà itMacro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITMacro(String value) {
        this.itMacro = value;
    }

    /**
     * Recupera il valore della proprietà itMicro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITMicro() {
        return itMicro;
    }

    /**
     * Imposta il valore della proprietà itMicro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITMicro(String value) {
        this.itMicro = value;
    }

    /**
     * Recupera il valore della proprietà itMadeIn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITMadeIn() {
        return itMadeIn;
    }

    /**
     * Imposta il valore della proprietà itMadeIn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITMadeIn(String value) {
        this.itMadeIn = value;
    }

    /**
     * Recupera il valore della proprietà itCustoms.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITCustoms() {
        return itCustoms;
    }

    /**
     * Imposta il valore della proprietà itCustoms.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITCustoms(String value) {
        this.itCustoms = value;
    }

    /**
     * Recupera il valore della proprietà itColor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITColor() {
        return itColor;
    }

    /**
     * Imposta il valore della proprietà itColor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITColor(String value) {
        this.itColor = value;
    }

    /**
     * Recupera il valore della proprietà itSize.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITSize() {
        return itSize;
    }

    /**
     * Imposta il valore della proprietà itSize.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITSize(String value) {
        this.itSize = value;
    }

    /**
     * Recupera il valore della proprietà itNote.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITNote() {
        return itNote;
    }

    /**
     * Imposta il valore della proprietà itNote.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITNote(String value) {
        this.itNote = value;
    }

    /**
     * Recupera il valore della proprietà itSoldTo.
     * 
     * @return
     *     possible object is
     *     {@link ITSoldTo }
     *     
     */
    public ITSoldTo getITSoldTo() {
        return itSoldTo;
    }

    /**
     * Imposta il valore della proprietà itSoldTo.
     * 
     * @param value
     *     allowed object is
     *     {@link ITSoldTo }
     *     
     */
    public void setITSoldTo(ITSoldTo value) {
        this.itSoldTo = value;
    }

    /**
     * Recupera il valore della proprietà itInvoice.
     * 
     * @return
     *     possible object is
     *     {@link ITInvoice }
     *     
     */
    public ITInvoice getITInvoice() {
        return itInvoice;
    }

    /**
     * Imposta il valore della proprietà itInvoice.
     * 
     * @param value
     *     allowed object is
     *     {@link ITInvoice }
     *     
     */
    public void setITInvoice(ITInvoice value) {
        this.itInvoice = value;
    }

    /**
     * Recupera il valore della proprietà itCompTypeID.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getITCompTypeID() {
        return itCompTypeID;
    }

    /**
     * Imposta il valore della proprietà itCompTypeID.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setITCompTypeID(Integer value) {
        this.itCompTypeID = value;
    }

    /**
     * Recupera il valore della proprietà itLinkPhoto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITLinkPhoto() {
        return itLinkPhoto;
    }

    /**
     * Imposta il valore della proprietà itLinkPhoto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITLinkPhoto(String value) {
        this.itLinkPhoto = value;
    }

    /**
     * Recupera il valore della proprietà itType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITType() {
        return itType;
    }

    /**
     * Imposta il valore della proprietà itType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITType(String value) {
        this.itType = value;
    }

    /**
     * Recupera il valore della proprietà itCost.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getITCost() {
        return itCost;
    }

    /**
     * Imposta il valore della proprietà itCost.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setITCost(BigDecimal value) {
        this.itCost = value;
    }

    /**
     * Recupera il valore della proprietà itPriorityLevel.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getITPriorityLevel() {
        return itPriorityLevel;
    }

    /**
     * Imposta il valore della proprietà itPriorityLevel.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setITPriorityLevel(Integer value) {
        this.itPriorityLevel = value;
    }

    /**
     * Recupera il valore della proprietà itJwItemSerialNumber.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITJwItemSerialNumber() {
        return itJwItemSerialNumber;
    }

    /**
     * Imposta il valore della proprietà itJwItemSerialNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITJwItemSerialNumber(String value) {
        this.itJwItemSerialNumber = value;
    }

    /**
     * Recupera il valore della proprietà itJwDescr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITJwDescr() {
        return itJwDescr;
    }

    /**
     * Imposta il valore della proprietà itJwDescr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITJwDescr(String value) {
        this.itJwDescr = value;
    }

    /**
     * Recupera il valore della proprietà itJwGold750.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getITJwGold750() {
        return itJwGold750;
    }

    /**
     * Imposta il valore della proprietà itJwGold750.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setITJwGold750(BigDecimal value) {
        this.itJwGold750 = value;
    }

    /**
     * Recupera il valore della proprietà itJwGold375.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getITJwGold375() {
        return itJwGold375;
    }

    /**
     * Imposta il valore della proprietà itJwGold375.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setITJwGold375(BigDecimal value) {
        this.itJwGold375 = value;
    }

    /**
     * Recupera il valore della proprietà itJwSilver.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getITJwSilver() {
        return itJwSilver;
    }

    /**
     * Imposta il valore della proprietà itJwSilver.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setITJwSilver(BigDecimal value) {
        this.itJwSilver = value;
    }

    /**
     * Recupera il valore della proprietà itJwCarats.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getITJwCarats() {
        return itJwCarats;
    }

    /**
     * Imposta il valore della proprietà itJwCarats.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setITJwCarats(BigDecimal value) {
        this.itJwCarats = value;
    }

    /**
     * Recupera il valore della proprietà itJwPlatinum.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getITJwPlatinum() {
        return itJwPlatinum;
    }

    /**
     * Imposta il valore della proprietà itJwPlatinum.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setITJwPlatinum(BigDecimal value) {
        this.itJwPlatinum = value;
    }

    /**
     * Recupera il valore della proprietà itGadget.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getITGadget() {
        return itGadget;
    }

    /**
     * Imposta il valore della proprietà itGadget.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setITGadget(Integer value) {
        this.itGadget = value;
    }

    /**
     * Recupera il valore della proprietà itCustomerOrderID.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getITCustomerOrderID() {
        return itCustomerOrderID;
    }

    /**
     * Imposta il valore della proprietà itCustomerOrderID.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setITCustomerOrderID(Integer value) {
        this.itCustomerOrderID = value;
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
