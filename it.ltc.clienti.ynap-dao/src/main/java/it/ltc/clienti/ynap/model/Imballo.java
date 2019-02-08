package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="RighiImballo")
public class Imballo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idRighiImballo", unique=true, nullable=false)
	private int id;
	
	@Column(name="NrLista", length=21, columnDefinition="varchar(21)")
	private String numeroLista;
	
	@Column(name="QtaImballata")
	private Integer quantitàImballata;
	
	@Column(name="CodiceArticolo", length=50, columnDefinition="varchar(50)")
	private String codiceArticolo;
	
	@Column(name="KeyColloSpe", length=10, columnDefinition="char(10)")
	private String idCollo;

	public Imballo() {}

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

	public Integer getQuantitàImballata() {
		return quantitàImballata;
	}

	public void setQuantitàImballata(Integer quantitàImballata) {
		this.quantitàImballata = quantitàImballata;
	}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	public String getIdCollo() {
		return idCollo;
	}

	public void setIdCollo(String idCollo) {
		this.idCollo = idCollo;
	}

}
