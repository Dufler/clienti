package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Destinatari")
public class Destinatario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdDestina", unique=true, nullable=false)
	private int id;
	
	@Column(name="CodDestina", length=30, columnDefinition="varchar(30)")
	private String codiceDestinatario;
	
	@Column(name="CodDestinaN")
	private int codiceDestinatarioNumerico;
	
	@Column(name="RagSoc1", length=100, columnDefinition="varchar(100)")
	private String ragioneSociale1;
	
	@Column(name="RagSoc2", length=50, columnDefinition="varchar(50)")
	private String ragioneSociale2;
	
	@Column(name="Indirizzo", length=100, columnDefinition="varchar(100)")
	private String indirizzo;
	
	@Column(name="Cap", length=100, columnDefinition="varchar(100)")
	private String cap;
	
	@Column(name="Località", length=100, columnDefinition="varchar(100)")
	private String località;
	
	@Column(name="Provincia", length=100, columnDefinition="varchar(100)")
	private String provincia;
	
	@Column(name="Nazione", length=100, columnDefinition="varchar(100)")
	private String nazione;
	
	@Column(name="Note", length=100, columnDefinition="text")
	private String note;
	
	@Column(name="Tel", length=50, columnDefinition="varchar(50)")
	private String telefono;
	
	@Column(name="Fax", length=50, columnDefinition="varchar(50)")
	private String fax;
	
	@Column(name="Email", length=100, columnDefinition="varchar(100)")
	private String email;
	
	@Column(name="CodNaz", length=3, columnDefinition="char(3)")
	private String codiceISONazione;
	
	@Column(name="CodContabile", length=30, columnDefinition="varchar(30)")
	private String codiceContabile;
	
	@Column(name="idContabile")
	private int idContabile;
	
	@Column(name="TipoDestina", length=4, columnDefinition="char(4)")
	private String tipo;

	public Destinatario() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodiceDestinatario() {
		return codiceDestinatario;
	}

	public void setCodiceDestinatario(String codiceDestinatario) {
		this.codiceDestinatario = codiceDestinatario;
	}

	public int getCodiceDestinatarioNumerico() {
		return codiceDestinatarioNumerico;
	}

	public void setCodiceDestinatarioNumerico(int codiceDestinatarioNumerico) {
		this.codiceDestinatarioNumerico = codiceDestinatarioNumerico;
	}

	public String getRagioneSociale1() {
		return ragioneSociale1;
	}

	public void setRagioneSociale1(String ragioneSociale1) {
		this.ragioneSociale1 = ragioneSociale1;
	}

	public String getRagioneSociale2() {
		return ragioneSociale2;
	}

	public void setRagioneSociale2(String ragioneSociale2) {
		this.ragioneSociale2 = ragioneSociale2;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getLocalità() {
		return località;
	}

	public void setLocalità(String località) {
		this.località = località;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCodiceISONazione() {
		return codiceISONazione;
	}

	public void setCodiceISONazione(String codiceISONazione) {
		this.codiceISONazione = codiceISONazione;
	}

	public String getCodiceContabile() {
		return codiceContabile;
	}

	public void setCodiceContabile(String codiceContabile) {
		this.codiceContabile = codiceContabile;
	}

	public int getIdContabile() {
		return idContabile;
	}

	public void setIdContabile(int idContabile) {
		this.idContabile = idContabile;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
