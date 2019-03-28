package it.ltc.clienti.date;

import it.ltc.clienti.date.articoli.ImportaAnagraficheArticoli;
import it.ltc.clienti.date.carichi.EsportaCarichi;
import it.ltc.clienti.date.ordini.ImportaOrdini;

public class MainDate {

	public static void main(String[] args) {
		ConfigurationUtility config = ConfigurationUtility.getInstance();
		String persistenceUnit = config.getPersistenceUnit();
		//Importo le anagrafiche
		ImportaAnagraficheArticoli anagrafiche = new ImportaAnagraficheArticoli(persistenceUnit);
		anagrafiche.importa();
		//Esporto i carichi
//		EsportaCarichi export = new EsportaCarichi(persistenceUnit);
//		export.esportaCarichi();
		//Importo gli ordini
		ImportaOrdini ordini = new ImportaOrdini(persistenceUnit);
		ordini.importa();
	}

}
