//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.07.20 alle 12:01:50 PM CEST 
//


package it.ltc.model.item.out;

import java.util.ArrayList;
import java.util.List;
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
 *       &lt;sequence>
 *         &lt;element ref="{}FileID" minOccurs="0"/>
 *         &lt;sequence maxOccurs="unbounded">
 *           &lt;element ref="{}Mov"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fileID",
    "mov"
})
@XmlRootElement(name = "Movs")
public class Movs {

    @XmlElement(name = "FileID")
    protected Integer fileID;
    @XmlElement(name = "Mov", required = true)
    protected List<Mov> mov;

    /**
     * Recupera il valore della proprietà fileID.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFileID() {
        return fileID;
    }

    /**
     * Imposta il valore della proprietà fileID.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFileID(Integer value) {
        this.fileID = value;
    }

    /**
     * Gets the value of the mov property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mov property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMov().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mov }
     * 
     * 
     */
    public List<Mov> getMov() {
        if (mov == null) {
            mov = new ArrayList<Mov>();
        }
        return this.mov;
    }

}
