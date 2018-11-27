package it.ltc.clienti.date;

import it.ltc.clienti.date.articoli.ImportaAnagraficheArticoli;
import it.ltc.clienti.date.carichi.EsportaCarichi;
import it.ltc.clienti.date.ordini.ImportaOrdini;

public class MainDate {

	public static void main(String[] args) {
		ConfigurationUtility config = ConfigurationUtility.getInstance();
		//Importo le anagrafiche
		ImportaAnagraficheArticoli anagrafiche = new ImportaAnagraficheArticoli(config.getPersistenceUnit());
		anagrafiche.importa();
		//Esporto i carichi
		EsportaCarichi export = new EsportaCarichi(config.getPersistenceUnit());
		export.esportaCarichi();
		//Importo gli ordini
		ImportaOrdini ordini = new ImportaOrdini(config.getPersistenceUnit());
		ordini.importa();
	}

}
