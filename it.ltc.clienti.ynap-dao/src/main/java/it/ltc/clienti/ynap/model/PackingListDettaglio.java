package it.ltc.clienti.ynap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PakiArticolo
 * @author Damiano
 *
 */
@Entity
@Table(name="PakiArticolo")
public class PackingListDettaglio implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idPakiArticolo", unique=true, nullable=false)
	private int id;
	
	@Column(name="IdPakiTesta")
	private int idPackingList;
	
	@Column(name="CodUnicoArt", length=15, columnDefinition="char(15)")
	private String codiceUnivocoArticolo;
	
	@Column(name="NrDispo")
	private Integer idMovimento; //MOV_ID
	
	@Column(name="DataAgg", columnDefinition="datetime")
	private Date dataMovimento; //MOV_Date
	
	@Column(name="IDUtenteYNAP")
	private int idUtenteYNAP; //MOV_User
	
	@Column(name="Note", length=255, columnDefinition="varchar(255)")
	private String note; //MOV_Note1
	
	@Column(name="TipoMovimento")
	private Integer tipoMovimento; //MOV_Type
	
	@Column(name="IDTrasportatore")
	private Integer idTrasportatore; //Mov_CarrierId
	
	@Column(name="NrColloFornitore", length=50, columnDefinition="varchar(50)")
	private String idCollo; //Mov_UDC
	
	@Column(name="CodiceDA")
	private Integer codiceDA; //From_ID
	
	@Column(name="CodiceA")
	private Integer codiceA; //To_ID
	
	@Column(name="IDRigaOrdine")
	private Integer idRigaOrdine; //IT_RMM_ID
	
	@Column(name="SerialeRFID", length=16, columnDefinition="char(16)")
	private String codiceRFID; //IT_Code
	
	@Column(name="Stato", length=20, columnDefinition="varchar(20)")
	private String stato;
	
	@Column(name="QtaVerificata")
	private int quantitàVerificata;
	
	//private int quantitàPrelevata;
	
	@Column(name="Descrizione", length=500, columnDefinition="varchar(500)")
	private String descrizione;

	public PackingListDettaglio() {}
	
	@Override
	public String toString() {
		return codiceRFID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdPackingList() {
		return idPackingList;
	}

	public void setIdPackingList(int idPackingList) {
		this.idPackingList = idPackingList;
	}

	public String getCodiceUnivocoArticolo() {
		return codiceUnivocoArticolo;
	}

	public void setCodiceUnivocoArticolo(String codiceUnivocoArticolo) {
		this.codiceUnivocoArticolo = codiceUnivocoArticolo;
	}

	public Integer getIdMovimento() {
		return idMovimento;
	}

	public void setIdMovimento(Integer idMovimento) {
		this.idMovimento = idMovimento;
	}

	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public int getIdUtenteYNAP() {
		return idUtenteYNAP;
	}

	public void setIdUtenteYNAP(int idUtenteYNAP) {
		this.idUtenteYNAP = idUtenteYNAP;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(Integer tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public Integer getIdTrasportatore() {
		return idTrasportatore;
	}

	public void setIdTrasportatore(Integer idTrasportatore) {
		this.idTrasportatore = idTrasportatore;
	}

	public String getIdCollo() {
		return idCollo;
	}

	public void setIdCollo(String idCollo) {
		this.idCollo = idCollo;
	}

	public Integer getCodiceDA() {
		return codiceDA;
	}

	public void setCodiceDA(Integer codiceDA) {
		this.codiceDA = codiceDA;
	}

	public Integer getCodiceA() {
		return codiceA;
	}

	public void setCodiceA(Integer codiceA) {
		this.codiceA = codiceA;
	}

	public Integer getIdRigaOrdine() {
		return idRigaOrdine;
	}

	public void setIdRigaOrdine(Integer idRigaOrdine) {
		this.idRigaOrdine = idRigaOrdine;
	}

	public String getCodiceRFID() {
		return codiceRFID;
	}

	public void setCodiceRFID(String codiceRFID) {
		this.codiceRFID = codiceRFID;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getQuantitàVerificata() {
		return quantitàVerificata;
	}

	public void setQuantitàVerificata(int quantità) {
		this.quantitàVerificata = quantità;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

//	public int getQuantitàPrelevata() {
//		return quantitàPrelevata;
//	}
//
//	public void setQuantitàPrelevata(int quantitàPrelevata) {
//		this.quantitàPrelevata = quantitàPrelevata;
//	}

}