package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TBD")
public class CausaleMovimentoMagazzino implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String ADDIZIONE = "+";
	public static final String SOTTRAZIONE = "-";
	public static final String NULLA = "N";
	
	private static final String nomeTabella = "dbo.TBD"; //TODO - tabella da creare?
	
	private Integer id;
	private String causale;
	private String descrizione;
	private String privilegioUtente;
	private String operazioneEsistenza;
	private String operazioneDisponibile;
	private String operazioneImpegnato;
	private String segnoInOut;
	private String tipoCausale;

	public String getNomeTabella() {
		return nomeTabella;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getPrivilegioUtente() {
		return privilegioUtente;
	}

	public void setPrivilegioUtente(String privilegioUtente) {
		this.privilegioUtente = privilegioUtente;
	}

	public String getOperazioneEsistenza() {
		return operazioneEsistenza;
	}

	public void setOperazioneEsistenza(String operazioneEsistenza) {
		this.operazioneEsistenza = operazioneEsistenza;
	}

	public String getOperazioneDisponibile() {
		return operazioneDisponibile;
	}

	public void setOperazioneDisponibile(String operazioneDisponibile) {
		this.operazioneDisponibile = operazioneDisponibile;
	}

	public String getOperazioneImpegnato() {
		return operazioneImpegnato;
	}

	public void setOperazioneImpegnato(String operazioneImpegnato) {
		this.operazioneImpegnato = operazioneImpegnato;
	}

	public String getSegnoInOut() {
		return segnoInOut;
	}

	public void setSegnoInOut(String segnoInOut) {
		this.segnoInOut = segnoInOut;
	}

	public String getTipoCausale() {
		return tipoCausale;
	}

	public void setTipoCausale(String tipoCausale) {
		this.tipoCausale = tipoCausale;
	}

}
