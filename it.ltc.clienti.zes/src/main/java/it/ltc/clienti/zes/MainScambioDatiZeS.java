package it.ltc.clienti.zes;

public class MainScambioDatiZeS {
	
	public static void main(String[] args) {
		String folderPath = ConfigurationUtility.getInstance().getFolderPath();
		String persistenceUnit = ConfigurationUtility.getInstance().getPersistenceUnit();
		ImportatoreAnagraficaProdotti importatoreAnagrafiche = new ImportatoreAnagraficaProdotti(folderPath, persistenceUnit);
		importatoreAnagrafiche.importaAnagrafiche();
		ImportatoreOrdini importatoreOrdini = new ImportatoreOrdini(folderPath, persistenceUnit);
		importatoreOrdini.importaOrdini();
	}

}
