package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SerialiFF")
public class SerialiFF implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="seriale", unique=true, nullable=false)
	private String seriale;
	
	private String tipo;
	private String stato;
	private String importato;
	
	public SerialiFF() {}

	public String getSeriale() {
		return seriale;
	}

	public void setSeriale(String seriale) {
		this.seriale = seriale;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getImportato() {
		return importato;
	}

	public void setImportato(String importato) {
		this.importato = importato;
	}

}
