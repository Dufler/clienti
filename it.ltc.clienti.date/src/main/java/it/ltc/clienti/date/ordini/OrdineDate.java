package it.ltc.clienti.date.ordini;

import java.util.Date;

import it.ltc.utility.miscellanea.string.annotation.Campo;
import it.ltc.utility.miscellanea.string.annotation.TipoCampo;

/**
 * Viene fatto un controllo sulla ragione sociale, se quella normale manca si vanno ad usare tutti gli attributi con Contabile.
 * @author Damiano
 *
 */
public class OrdineDate {
	
	@Campo(start=240, length=35)
	public String ragioneSociale;
	
	@Campo(start=31, length=35)
	public String ragioneSocialeContabile;
	
	/**
	 * Il valore di default 0000 corrisponde all'Italia.
	 */
	@Campo(start=365, length=4, defaultValue = "0000", useDefaultValueIfEmpty=true)
	public String codiceNazione;
	
	@Campo(start=156, length=4, defaultValue = "0000", useDefaultValueIfEmpty=true)
	public String codiceNazioneContabile;
	
	@Campo(start=275, length=30)
	public String indirizzo;
	
	@Campo(start=66, length=30)
	public String indirizzoContabile;
	
	@Campo(start=305, length=6)
	public String cap;
	
	@Campo(start=96, length=6)
	public String capContabile;
	
	@Campo(start=311, length=30)
	public String citta;
	
	@Campo(start=102, length=30)
	public String cittaContabile;
	
	@Campo(start=361, length=4)
	public String provincia;
	
	@Campo(start=152, length=4)
	public String provinciaContabile;
	
	@Campo(start=25, length=6)
	public String codiceClienteParte1;
	
	@Campo(start=234, length=6, defaultValue="000000", useDefaultValueIfEmpty=true, useDefaultValueOnError=true)
	public String codiceClienteParte2;
	
	@Campo(start=411, length=16)
	public String telefono;
	
	@Campo(start=202, length=16)
	public String telefonoContabile;	
	
	@Campo(start=7, length=8, dateFormat="yyyyMMdd", type=TipoCampo.DATA)
	public Date dataOrdine;
	
//	@Campo(start=500, length=8, dateFormat="yyyyMMdd", type=TipoCampo.DATA)
//	public Date dataConsegna;
	
	@Campo(start=0, length=1)
	public String tipoDocumento;
	
	@Campo(start=783, length=50)
	public String note;
	
	/**
	 * Se il valore è 4 è una stringa con delle note ma senza prodotti.
	 */
	@Campo(start=669, length=1, defaultValue="2", type=TipoCampo.NUMERICO_INTERO)
	public Integer tipoRiga;
	
	@Campo(start=768, length=13)
	public String barcode;
	
	@Campo(start=657, length=8, decimals=2, type=TipoCampo.NUMERICO_DECIMALE)
	public Double valoreContrassegno;
	
	@Campo(start=585, length=2)
	public String tipoContrassegno;
	
	@Campo(start=665, length=3, type=TipoCampo.NUMERICO_INTERO)
	public Integer numeroRiga;
	
	@Campo(start=0, length=1)
	public String sezioneDocumento;
	
	@Campo(start=1, length=6)
	public String numeroOrdine;
	
	@Campo(start=7, length=4)
	public Integer anno;
	
	@Campo(start=853, length=6, type=TipoCampo.NUMERICO_INTERO)
	public Integer quantitàSfuso;
	
	@Campo(start=859, length=5, type=TipoCampo.NUMERICO_INTERO)
	public Integer quantitàCasse;
	
	@Campo(start=985, length=88, type=TipoCampo.STRINGA)
	public String quantitàPerTaglia;
	
	@Campo(start=783, length=70)
	public String noteRiga;
	
	@Campo(start=874, length=1)
	public String tipoAssortimento;
	
	@Campo(start=1437, length=4)
	public String tipoFFW;
	
	public String getTipoFFW() {
		return tipoFFW;
	}

	public void setTipoFFW(String tipoFFW) {
		this.tipoFFW = tipoFFW;
	}

	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	public void setNumeroOrdine(String numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
	}

	public Integer getQuantitàSfuso() {
		return quantitàSfuso;
	}

	public void setQuantitàSfuso(Integer quantitàSfuso) {
		this.quantitàSfuso = quantitàSfuso;
	}

	public Integer getQuantitàCasse() {
		return quantitàCasse;
	}

	public void setQuantitàCasse(Integer quantitàCasse) {
		this.quantitàCasse = quantitàCasse;
	}

	public OrdineDate() {}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getRagioneSocialeContabile() {
		return ragioneSocialeContabile;
	}

	public void setRagioneSocialeContabile(String ragioneSocialeContabile) {
		this.ragioneSocialeContabile = ragioneSocialeContabile;
	}

	public String getCodiceNazione() {
		return codiceNazione;
	}

	public void setCodiceNazione(String codiceNazione) {
		this.codiceNazione = codiceNazione;
	}

	public String getCodiceNazioneContabile() {
		return codiceNazioneContabile;
	}

	public void setCodiceNazioneContabile(String codiceNazioneContabile) {
		this.codiceNazioneContabile = codiceNazioneContabile;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getIndirizzoContabile() {
		return indirizzoContabile;
	}

	public void setIndirizzoContabile(String indirizzoContabile) {
		this.indirizzoContabile = indirizzoContabile;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCapContabile() {
		return capContabile;
	}

	public void setCapContabile(String capContabile) {
		this.capContabile = capContabile;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getCittaContabile() {
		return cittaContabile;
	}

	public void setCittaContabile(String cittaContabile) {
		this.cittaContabile = cittaContabile;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getProvinciaContabile() {
		return provinciaContabile;
	}

	public void setProvinciaContabile(String provinciaContabile) {
		this.provinciaContabile = provinciaContabile;
	}

	public String getCodiceClienteParte1() {
		return codiceClienteParte1;
	}

	public void setCodiceClienteParte1(String codiceClienteParte1) {
		this.codiceClienteParte1 = codiceClienteParte1;
	}

	public String getCodiceClienteParte2() {
		return codiceClienteParte2;
	}

	public void setCodiceClienteParte2(String codiceClienteParte2) {
		this.codiceClienteParte2 = codiceClienteParte2;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefonoContabile() {
		return telefonoContabile;
	}

	public void setTelefonoContabile(String telefonoContabile) {
		this.telefonoContabile = telefonoContabile;
	}

	public Date getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

//	public Date getDataConsegna() {
//		return dataConsegna;
//	}
//
//	public void setDataConsegna(Date dataConsegna) {
//		this.dataConsegna = dataConsegna;
//	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getTipoRiga() {
		return tipoRiga;
	}

	public void setTipoRiga(Integer tipoRiga) {
		this.tipoRiga = tipoRiga;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Double getValoreContrassegno() {
		return valoreContrassegno;
	}

	public void setValoreContrassegno(Double valoreContrassegno) {
		this.valoreContrassegno = valoreContrassegno;
	}

	public String getTipoContrassegno() {
		return tipoContrassegno;
	}

	public void setTipoContrassegno(String tipoContrassegno) {
		this.tipoContrassegno = tipoContrassegno;
	}

	public Integer getNumeroRiga() {
		return numeroRiga;
	}

	public void setNumeroRiga(Integer numeroRiga) {
		this.numeroRiga = numeroRiga;
	}

	public String getQuantitàPerTaglia() {
		return quantitàPerTaglia;
	}

	public void setQuantitàPerTaglia(String quantitàPerTaglia) {
		this.quantitàPerTaglia = quantitàPerTaglia;
	}

	public String getNoteRiga() {
		return noteRiga;
	}

	public void setNoteRiga(String noteRiga) {
		this.noteRiga = noteRiga;
	}

	public String getTipoAssortimento() {
		return tipoAssortimento;
	}

	public void setTipoAssortimento(String tipoAssortimento) {
		this.tipoAssortimento = tipoAssortimento;
	}

	public String getSezioneDocumento() {
		return sezioneDocumento;
	}

	public void setSezioneDocumento(String sezioneDocumento) {
		this.sezioneDocumento = sezioneDocumento;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}
}
