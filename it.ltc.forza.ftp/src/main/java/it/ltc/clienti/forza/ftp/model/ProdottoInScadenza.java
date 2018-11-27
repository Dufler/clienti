package it.ltc.clienti.forza.ftp.model;

import java.util.Date;

/**
 * Questa classe rappresenta un prodotto in scadenza.<br>
 * I dati fondamentali riportati sono: sku, collo, quantità e data di scadenza.
 * @author Damiano
 *
 */
public class ProdottoInScadenza {
	
	private final String sku;
	private final String collo;
	private final int quantità;
	private final Date dataScadenza;

	public ProdottoInScadenza(String sku, String collo, int quantità, Date dataScadenza) {
		this.sku = sku;
		this.collo = collo;
		this.quantità = quantità;
		this.dataScadenza = dataScadenza;
	}

	public String getSku() {
		return sku;
	}

	public String getCollo() {
		return collo;
	}

	public int getQuantità() {
		return quantità;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	@Override
	public String toString() {
		return "ProdottoInScadenza [sku=" + sku + ", collo=" + collo + ", quantità=" + quantità + ", dataScadenza="
				+ dataScadenza + "]";
	}

}
