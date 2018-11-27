package it.ltc.clienti.date.carichi;

/**
 * Classe che mappa le info di base di una riga del file esportato verso DATE alla generazione di un carico.
 * @author Damiano
 *
 */
public class RigaCaricoDate {
	
	private final String idUnivocoArticolo;
	private final String codiceArticolo;
	private final String barcode;
	private final String numerata;
	private int totaleDichiarato;
	private int totaleRiscontrato;
	
	public RigaCaricoDate(String idUnivocoArticolo, String codiceArticolo, String barcode, String numerata) {
		this.idUnivocoArticolo = idUnivocoArticolo;
		this.codiceArticolo = codiceArticolo;
		this.barcode = barcode;
		this.numerata = numerata;
		this.totaleDichiarato = 0;
		this.totaleRiscontrato = 0;
	}

	public String getIdUnivocoArticolo() {
		return idUnivocoArticolo;
	}
	
	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getNumerata() {
		return numerata;
	}

	public int getTotaleDichiarato() {
		return totaleDichiarato;
	}

	public void setTotaleDichiarato(int totaleDichiarato) {
		this.totaleDichiarato = totaleDichiarato;
	}

	public int getTotaleRiscontrato() {
		return totaleRiscontrato;
	}

	public void setTotaleRiscontrato(int totaleRiscontrato) {
		this.totaleRiscontrato = totaleRiscontrato;
	}

}
