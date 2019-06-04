package it.ltc.clienti.date;

/**
 * Definisce i possibili tipi di files da importare.<br>
 * Sono stati inseriti anche dei "duplicati" con i plurali perchè sono inaffidabili...
 * @author Damiano
 *
 */
public enum TipoFileImportazione {

	ANAGRAFICHE,
	ORDINI;
	
	public static TipoFileImportazione trovaTipo(String nomeFile) {
		TipoFileImportazione tipo;
		try {			
			boolean excel = nomeFile.endsWith("xls") || nomeFile.endsWith("xlsx");
			tipo = excel ? TipoFileImportazione.ANAGRAFICHE : TipoFileImportazione.ORDINI;
		} catch (Exception e) {
			tipo = null;
		}
		return tipo;
	}
	
}
