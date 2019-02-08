package it.ltc.clienti.forza.ftp.model;

import java.util.HashMap;

public class AnagraficaProdotti implements Comparable<AnagraficaProdotti> {
	
	public enum Composizione { S, B };
	
	private final String sku;
	private final String descrizione;
	private final String barcode;
	private final String composizione;
	private final Integer q1;
	private final String sku1;
	private final Integer q2;
	private final String sku2;
	
	public AnagraficaProdotti(HashMap<String, Integer> mappaColonne, String[] campi) {
		sku = campi[mappaColonne.get("SKU")];
		descrizione = campi[mappaColonne.get("Item")];
		barcode = campi[mappaColonne.get("Barcode")];
		composizione = campi[mappaColonne.get("Composition")];
		q1 = parseInteger(campi[mappaColonne.get("q1")]);
		sku1 = campi[mappaColonne.get("sku1")];
		q2 = parseInteger(campi[mappaColonne.get("q2")]);
		sku2 = campi[mappaColonne.get("sku2")];
	}
	
	public String getSku() {
		return sku;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getComposizione() {
		return composizione;
	}

	public Integer getQ1() {
		return q1;
	}

	public String getSku1() {
		return sku1;
	}

	public Integer getQ2() {
		return q2;
	}

	public String getSku2() {
		return sku2;
	}

	@Override
	public String toString() {
		return "AnagraficaProdotti [sku=" + sku + ", descrizione=" + descrizione + ", barcode=" + barcode
				+ ", composizione=" + composizione + ", q1=" + q1 + ", sku1=" + sku1 + ", q2=" + q2 + ", sku2=" + sku2
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnagraficaProdotti other = (AnagraficaProdotti) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.equals(other.sku))
			return false;
		return true;
	}

	protected Integer parseInteger(String value) {
		Integer i = null;
		if (value != null) {
			value = value.trim();
			if (!value.isEmpty())
			try {
				i = Integer.parseInt(value);
			} catch (NumberFormatException e) {}
		}
		return i;
	}

	@Override
	public int compareTo(AnagraficaProdotti o) {
		int compare = composizione.compareTo(o.getComposizione()) * -1;
		return compare;
	}

}
