package it.ltc.clienti.date;

public class MainDate {

	public static void main(String[] args) {
//		ConfigurationUtility config = ConfigurationUtility.getInstance();
//		String persistenceUnit = config.getPersistenceUnit();
		//Importo le anagrafiche
//		ImportaAnagraficheArticoli anagrafiche = new ImportaAnagraficheArticoli(persistenceUnit);
//		anagrafiche.importa();
		//Esporto i carichi
//		EsportaCarichi export = new EsportaCarichi(persistenceUnit);
//		export.esportaCarichi();
		//Importo gli ordini
//		ImportaOrdini ordini = new ImportaOrdini(persistenceUnit);
//		ordini.importa();
		Importatore.getInstance().importa();
	}

}
