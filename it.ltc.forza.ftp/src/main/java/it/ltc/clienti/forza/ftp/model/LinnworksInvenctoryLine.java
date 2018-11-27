package it.ltc.clienti.forza.ftp.model;

/**
 * Classe che mappa una riga del file .csv da restituire sulla lettura dei carichi.
 * Il metodo <code>toString</code> è stato ridefinito in maniera da restituire la sua rappresentazione su riga .csv.
 * Il metodo statico <code>getColumnNames</code> restituisce la riga d'intestazione di un file .csv tipo.
 * @author Damiano
 *
 */
public class LinnworksInvenctoryLine {
	
	public static final String COLUMN_NAMES = "sku,ean,quantity,inbounddocument,arrivaldate,suppliername";
	
	private final String sku;
	private final String ean;
	private final String quantity;
	private final String inboundDocument;
	private final String arrivalDate;
	private final String supplierName;
	
	public LinnworksInvenctoryLine(String sku, String ean, String quantity, String inboundDocument, String arrivalDate,	String supplierName) {
		this.sku = sku;
		this.ean = ean;
		this.quantity = quantity;
		this.inboundDocument = inboundDocument;
		this.arrivalDate = arrivalDate;
		this.supplierName = supplierName;
	}

	public String getSku() {
		return sku;
	}

	public String getEan() {
		return ean;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getInboundDocument() {
		return inboundDocument;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public String getSupplierName() {
		return supplierName;
	}
	
	@Override
	public String toString() {
		return sku + "," + ean + "," + quantity + "," + inboundDocument + "," + arrivalDate + "," + supplierName;
	}
	
	public static String getColumnNames() {
		return COLUMN_NAMES;
	}

}
