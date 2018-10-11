package it.ltc.ciesse.scambiodati;

import it.ltc.ciesse.scambiodati.logic.Export;
import it.ltc.ciesse.scambiodati.logic.Import;

/**
 * Main dello scambio dati, viene accettata una stringa di argomenti che corrispondono alle funzioni da eseguire:
 * - i : importazione dati
 * - e : esportazione dati
 * - mg : esportazione movimenti non comunicati e giacenza dei magazzini
 * Nel caso in cui non siano specificati argomenti viene eseguita l'importazione ed esportazione dati.
 * @author Damiano
 *
 */
public class MainCiesse {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			importaDati();
			esportaDati();
		} else for (String arg : args) {
			switch (arg) {
				case "i" : importaDati(); break;
				case "e" : esportaDati(); break;
				case "mg" : esportaMovimentiEGiacenza(); break;
				default : throw new UnsupportedOperationException("Opzione non riconosciuta: '" + arg + "', le opzioni valide sono: 'i' (importazione dati), 'e', (esportazione dati), 'mg' (esportazione movimenti e giacenza)");
			}
		}		
	}
	
	public static void importaDati() {
		Import importatore = Import.getInstance();
		importatore.importaDati();
	}
	
	public static void esportaDati() {
		Export esportatore = Export.getInstance();
		esportatore.esportaDati();
	}
	
	/**
	 * La giacenza e i movimenti vanno esportati solo 1 volta al giorno.
	 */
	public static void esportaMovimentiEGiacenza() {
		Export esportatore = Export.getInstance();
		esportatore.esportaMovimentiEGiacenza();
	}

}
