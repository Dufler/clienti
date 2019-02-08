package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="MagaSd")
public class SaldiMagazzino implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdMagaSd", unique=true, nullable=false)
	private int id;
	
	@Column(name="Impegnato")
	private int impegnato;
	
	@Column(name="Disponibile")
	private int disponibile;
	
	@Column(name="Esistenza")
	private int esistenza;
	
	@Column(name="IdUniArticolo", length=15, columnDefinition="char(15)")
	private String idUnivocoArticolo;

	public SaldiMagazzino() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImpegnato() {
		return impegnato;
	}

	public void setImpegnato(int impegnato) {
		this.impegnato = impegnato;
	}

	public int getDisponibile() {
		return disponibile;
	}

	public void setDisponibile(int disponibile) {
		this.disponibile = disponibile;
	}

	public int getEsistenza() {
		return esistenza;
	}

	public void setEsistenza(int esistenza) {
		this.esistenza = esistenza;
	}

	public String getIdUnivocoArticolo() {
		return idUnivocoArticolo;
	}

	public void setIdUnivocoArticolo(String idUnivocoArticolo) {
		this.idUnivocoArticolo = idUnivocoArticolo;
	}

}
