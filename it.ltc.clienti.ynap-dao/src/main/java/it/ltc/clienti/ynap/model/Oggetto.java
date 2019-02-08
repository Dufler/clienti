package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ColliPack")
public class Oggetto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdColliPack", unique=true, nullable=false)
	private int id;
	
	@Column(name="CodiceArticolo", length=21, columnDefinition="char(21)")
	private String codiceUnivocoArticolo;
	
	@Column(name="NrIdColloPk", length=9, columnDefinition="char(9)")
	private String numeroCollo;
	
	@Column(name="Descrizione", length=50, columnDefinition="varchar(50)")
	private String descrizione;
	
	@Column(name="IdTestaPaki")
	private int idPackingList;
	
	@Column(name="IdPakiarticolo")
	private int idPackingListDettaglio;
	
	@Column(name="CodArtStr", length=50, columnDefinition="varchar(50)")
	private String codiceRFID;
	
	@Column(name="Stato", length=20, columnDefinition="varchar(20)")
	private String stato;
	
	@Column(name="Flagpre", length=1, columnDefinition="char(1)")
	private String flag;
	
	@Column(name="Qta")
	private int quantità;
	
	@Column(name="KeyUbicaCar", nullable=false, length=50, columnDefinition="varchar(50)")
	private String ubicazione;
	
	public Oggetto() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodiceUnivocoArticolo() {
		return codiceUnivocoArticolo;
	}

	public void setCodiceUnivocoArticolo(String codiceUnivocoArticolo) {
		this.codiceUnivocoArticolo = codiceUnivocoArticolo;
	}

	public String getNumeroCollo() {
		return numeroCollo;
	}

	public void setNumeroCollo(String numeroCollo) {
		this.numeroCollo = numeroCollo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getIdPackingList() {
		return idPackingList;
	}

	public void setIdPackingList(int idPackingList) {
		this.idPackingList = idPackingList;
	}

	public int getIdPackingListDettaglio() {
		return idPackingListDettaglio;
	}

	public void setIdPackingListDettaglio(int idPackingListDettaglio) {
		this.idPackingListDettaglio = idPackingListDettaglio;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getQuantità() {
		return quantità;
	}

	public void setQuantità(int quantità) {
		this.quantità = quantità;
	}

	public String getUbicazione() {
		return ubicazione;
	}

	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

}