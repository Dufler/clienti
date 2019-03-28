package it.ltc.clienti.ynap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * RighiOrdine
 * @author Damiano
 *
 */
@Entity
@Table(name="RighiOrdine")
public class DettaglioOrdine implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idRigoOrdine", unique=true, nullable=false)
	private int id;
	
	@Column(name="IdTestataOrdine", nullable=false)
	private int idTestataOrdine;
	
	@Column(name="NrLista", length=21, columnDefinition="varchar(21)")
	private String numeroLista;
	
	@Column(name="DataOrdine", columnDefinition="datetime")
	private Date dataOrdine;
	
	@Column(name="CodiceArticolo", length=50, columnDefinition="varchar(50)")
	private String serialeRFID;
	
	@Column(name="Taglia", length=20, columnDefinition="varchar(20)")
	private String taglia;
	
	@Column(name="CodCliente", length=20, columnDefinition="varchar(20)")
	private String codiceCliente;
	
	@Column(name="Colore", length=50, columnDefinition="varchar(50)")
	private String colore;
	
	@Column(name="CodBarre", length=20, columnDefinition="varchar(20)")
	private String barcode;
	
	@Column(name="Descrizione", length=70, columnDefinition="varchar(70)")
	private String descrizione;
	
	@Column(name="NrRigo")
	private Integer numeroRigaOrdine;
	
	@Column(name="NrOrdine", length=20, columnDefinition="varchar(20)")
	private String numeroOrdine;
	
	@Column(name="IdDestina")
	private int idDestinatario;
	
	@Column(name="BarraEAN", length=40, columnDefinition="varchar(40)")
	private String barcodeEAN;
	
	@Column(name="BarraUPC", length=40, columnDefinition="varchar(40)")
	private String barcodeUPC;
	
	@Column(name="IdUnicoArt", length=15, columnDefinition="char(15)")
	private String idUnivocoArticolo;
	
	@Column(name="Ragstampe1", length=20, columnDefinition="varchar(20)")
	private String raggruppamentoStampe;
	
	@Column(name="Note", length=200, columnDefinition="varchar(200)")
	private String note;
	
	@Column(name="QtaSpedizione")
	private int quantitàDaSpedire;
	
	@Column(name="Qtadaubicare")
	private int quantitàDaUbicare;
	
	@Column(name="QtaImballata")
	private int quantitàDaImballare;
	
	@Column(name="CodificaOggettoNonTrovato")
	private Integer codificaOggettoNonTrovato;
	
	@Column(name="NoteOggettoNonTrovato", length=200, columnDefinition="varchar(200)")
	private String noteOggettoNonTrovato;

	public DettaglioOrdine() {}
	
	@PrePersist
	public void prePersist() {
		if (colore == null) colore = "???";
		if (taglia == null) taglia = "UNI";
		if (descrizione == null) descrizione = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdTestataOrdine() {
		return idTestataOrdine;
	}

	public void setIdTestataOrdine(int idTestataOrdine) {
		this.idTestataOrdine = idTestataOrdine;
	}

	public String getNumeroLista() {
		return numeroLista;
	}

	public void setNumeroLista(String numeroLista) {
		this.numeroLista = numeroLista;
	}

	public Date getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public String getSerialeRFID() {
		return serialeRFID;
	}

	public void setSerialeRFID(String serialeRFID) {
		this.serialeRFID = serialeRFID;
	}

	public String getTaglia() {
		return taglia;
	}

	public void setTaglia(String taglia) {
		this.taglia = taglia;
	}

	public String getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(String codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getNumeroRigaOrdine() {
		return numeroRigaOrdine;
	}

	public void setNumeroRigaOrdine(Integer numeroRigaOrdine) {
		this.numeroRigaOrdine = numeroRigaOrdine;
	}

	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	public void setNumeroOrdine(String numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
	}

	public int getIdDestinatario() {
		return idDestinatario;
	}

	public void setIdDestinatario(int idDestinatario) {
		this.idDestinatario = idDestinatario;
	}

	public String getBarcodeEAN() {
		return barcodeEAN;
	}

	public void setBarcodeEAN(String barcodeEAN) {
		this.barcodeEAN = barcodeEAN;
	}

	public String getBarcodeUPC() {
		return barcodeUPC;
	}

	public void setBarcodeUPC(String barcodeUPC) {
		this.barcodeUPC = barcodeUPC;
	}

	public String getIdUnivocoArticolo() {
		return idUnivocoArticolo;
	}

	public void setIdUnivocoArticolo(String idUnivocoArticolo) {
		this.idUnivocoArticolo = idUnivocoArticolo;
	}

	public String getRaggruppamentoStampe() {
		return raggruppamentoStampe;
	}

	public void setRaggruppamentoStampe(String raggruppamentoStampe) {
		this.raggruppamentoStampe = raggruppamentoStampe;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getQuantitàDaSpedire() {
		return quantitàDaSpedire;
	}

	public void setQuantitàDaSpedire(int quantitàDaSpedire) {
		this.quantitàDaSpedire = quantitàDaSpedire;
	}

	public int getQuantitàDaUbicare() {
		return quantitàDaUbicare;
	}

	public void setQuantitàDaUbicare(int quantitàDaUbicare) {
		this.quantitàDaUbicare = quantitàDaUbicare;
	}

	public int getQuantitàDaImballare() {
		return quantitàDaImballare;
	}

	public void setQuantitàDaImballare(int quantitàDaImballare) {
		this.quantitàDaImballare = quantitàDaImballare;
	}

	public Integer getCodificaOggettoNonTrovato() {
		return codificaOggettoNonTrovato;
	}

	public void setCodificaOggettoNonTrovato(Integer codificaOggettoNonTrovato) {
		this.codificaOggettoNonTrovato = codificaOggettoNonTrovato;
	}

	public String getNoteOggettoNonTrovato() {
		return noteOggettoNonTrovato;
	}

	public void setNoteOggettoNonTrovato(String noteOggettoNonTrovato) {
		this.noteOggettoNonTrovato = noteOggettoNonTrovato;
	}

}
