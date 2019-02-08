package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Righiubicpre")
public class UbicazioneOggetto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idubica", unique=true, nullable=false)
	private int id;
	
	@Column(name="nrlista", length=20, columnDefinition="varchar(20)")
	private String numeroLista;
	
	@Column(name="iduniarticolo", length=50, columnDefinition="varchar(50)")
	private String idUnivocoArticolo;
	
	@Column(name="QtaImballata")
	private int quantitàImballata;
	
	public UbicazioneOggetto() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumeroLista() {
		return numeroLista;
	}

	public void setNumeroLista(String numeroLista) {
		this.numeroLista = numeroLista;
	}

	public String getIdUnivocoArticolo() {
		return idUnivocoArticolo;
	}

	public void setIdUnivocoArticolo(String idUnivocoArticolo) {
		this.idUnivocoArticolo = idUnivocoArticolo;
	}

	public int getQuantitàImballata() {
		return quantitàImballata;
	}

	public void setQuantitàImballata(int quantitàImballata) {
		this.quantitàImballata = quantitàImballata;
	}

}
