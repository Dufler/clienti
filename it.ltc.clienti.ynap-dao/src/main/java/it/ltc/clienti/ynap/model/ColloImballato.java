package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ColliImballo")
public class ColloImballato implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idColliImballo", unique=true, nullable=false)
	private int id;
	
	@Column(name="KeyColloSpe", length=10, columnDefinition="char(10)")
	private String idCollo;
	
	@Column(name="PesoKg", columnDefinition="money")
	private Double peso;
	
	@Column(name="NrLista", length=21, columnDefinition="varchar(21)")
	private String numeroLista;
	
	public ColloImballato() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdCollo() {
		return idCollo;
	}

	public void setIdCollo(String idCollo) {
		this.idCollo = idCollo;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public String getNumeroLista() {
		return numeroLista;
	}

	public void setNumeroLista(String numeroLista) {
		this.numeroLista = numeroLista;
	}

}
