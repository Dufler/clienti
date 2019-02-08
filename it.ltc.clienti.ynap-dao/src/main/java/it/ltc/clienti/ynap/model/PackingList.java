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
 * PakiTesta
 * @author Damiano
 *
 */
@Entity
@Table(name="PakiTesta")
public class PackingList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idTestaPaki", unique=true, nullable=false)
	private int id;
	
	@Column(name="NrPaki", length=50, columnDefinition="varchar(50)")
	private String fileID;
	
	@Column(name="DataPaki", columnDefinition="datetime")
	private Date dataCreazione;
	
	@Column(name="FlagTra", length=1, columnDefinition="char(1)")
	private String stato;
	
	@Column(name="GeneratoMov", length=2, columnDefinition="char(2)")
	private String generato;
	
	@Column(name="TipoPacking", length=20, columnDefinition="varchar(20)")
	private String qualita;

	public PackingList() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getGenerato() {
		return generato;
	}

	public void setGenerato(String generato) {
		this.generato = generato;
	}

	public String getQualita() {
		return qualita;
	}

	public void setQualita(String qualita) {
		this.qualita = qualita;
	}

}
