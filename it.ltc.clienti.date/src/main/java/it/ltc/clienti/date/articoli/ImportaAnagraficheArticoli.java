package it.ltc.clienti.date.articoli;

import java.io.File;

import org.apache.log4j.Logger;

import it.ltc.clienti.date.ConfigurationUtility;
import it.ltc.model.persistence.prodotto.ControllerProdottoSQLServer;
import it.ltc.utility.csv.FileCSV;
import it.ltc.utility.microsoft.converter.XLStoCSV;

public class ImportaAnagraficheArticoli extends ControllerProdottoSQLServer {
	
	private static final Logger logger = Logger.getLogger(ImportaAnagraficheArticoli.class);
	
	private final String folderPath;
	private final String regexNomeFileArticoli;

	public ImportaAnagraficheArticoli(String persistenceUnit) {
		super(persistenceUnit);
		ConfigurationUtility config = ConfigurationUtility.getInstance();
		this.folderPath = config.getFolderPathImport();
		this.regexNomeFileArticoli = config.getRegexArticoli();
	}
	
	public void importa() {
		//recupero i files con gli ordini
		File folder = new File(folderPath);
		for (File file : folder.listFiles()) {
			//Controllo ogni file per vedere se contiene ordini.
			if (file.isFile() && file.getName().matches(regexNomeFileArticoli)) {
				try {
					FileCSV csv = XLStoCSV.getCSV(file);
					while (csv.prossimaRiga()) {
						csv.getStringa("");
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					spostaFile(file, folderPath + "errori");
				}				
			}
		}		
	}

}
