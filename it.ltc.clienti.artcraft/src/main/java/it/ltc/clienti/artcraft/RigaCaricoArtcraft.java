package it.ltc.clienti.artcraft;

public class RigaCaricoArtcraft {
	
	private final String codiceArticolo;
	private final String numerata;
	private final int posizione;
	private final String magazzino;
	private final String barcodeProdotto;
	private final String barcodeCassa;
	private final String ordineFornitore;
	
	private int totaleDichiarato;
	private int totaleRiscontrato;
	
	public RigaCaricoArtcraft(String codiceArticolo, String numerata, int posizione, String magazzino, String barcodeProdotto, String barcodeCassa, String ordineFornitore) {
		this.codiceArticolo = codiceArticolo;
		this.numerata = numerata;
		this.posizione = posizione;
		this.magazzino = magazzino;
		this.barcodeProdotto = barcodeProdotto;
		this.barcodeCassa = barcodeCassa;
		this.ordineFornitore = ordineFornitore;
		this.setTotaleDichiarato(0);
		this.setTotaleRiscontrato(0);
	}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public String getNumerata() {
		return numerata;
	}

	public int getPosizione() {
		return posizione;
	}

	public String getMagazzino() {
		return magazzino;
	}

	public String getBarcodeProdotto() {
		return barcodeProdotto;
	}

	public String getBarcodeCassa() {
		return barcodeCassa;
	}

	public String getOrdineFornitore() {
		return ordineFornitore;
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
