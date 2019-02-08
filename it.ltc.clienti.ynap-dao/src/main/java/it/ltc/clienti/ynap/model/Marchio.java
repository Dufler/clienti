package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="Marchi")
@NamedQuery(name="Marchi.progressivo", query="SELECT MAX(m.Codice) FROM Marchi m")
public class Marchio implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdMarchio", unique=true, nullable=false)
	private int id;
	
	@Column(name="Codice", nullable=false)
	private int codice;
	
	@Column(name="Descrizione", nullable=false, length=50, columnDefinition="varchar(50)")
	private String nome;
	
	public Marchio() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCodice() {
		return codice;
	}

	public void setCodice(int codice) {
		this.codice = codice;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
