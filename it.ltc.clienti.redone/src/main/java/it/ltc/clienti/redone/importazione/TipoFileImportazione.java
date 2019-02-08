package it.ltc.clienti.redone.importazione;

/**
 * Definisce i possibili tipi di files da importare.<br>
 * Sono stati inseriti anche dei "duplicati" con i plurali perchè sono inaffidabili...
 * @author Damiano
 *
 */
public enum TipoFileImportazione {

	PRODUCT,
	PRODUCTS,
	SUPPLIER,
	SUPPLIERS,
	INBOUND,
	INBOUNDS,
	ORDER,
	ORDERS,
	SHIPMENT,
	SHIPMENTS;
	
	public static TipoFileImportazione trovaTipo(String nomeFile) {
		TipoFileImportazione tipo;
		try {			
			String fileType = nomeFile.split("_|\\d")[0].toUpperCase();
			tipo = TipoFileImportazione.valueOf(fileType);
		} catch (Exception e) {
			tipo = null;
		}
		return tipo;
	}
	
}
