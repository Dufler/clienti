package it.ltc.clienti.date.articoli;

import java.io.File;

import org.apache.log4j.Logger;

import it.ltc.clienti.date.ConfigurationUtility;
import it.ltc.model.interfaces.prodotto.Cassa;
import it.ltc.model.interfaces.prodotto.MProdotto;
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
					for (int riga = 0; riga < csv.getRighe().size(); riga++) {
						//recupero le info dal foglio excel
						String modello = csv.getStringa(2, riga);
						String descrizione = csv.getStringa(3, riga);
						String tipoCassa = csv.getStringa(4, riga);
						String taglia = csv.getStringa(6, riga);
						String barcode = csv.getStringa(7, riga);
						String barcodeFornitore = csv.getStringa(10, riga);
						String categoria = ""; //csv.getStringa(?, riga);
						//Determino le info necessarie deducibili
						Cassa cassa = tipoCassa == null || tipoCassa.isEmpty() ? Cassa.NO : Cassa.STANDARD;
						String numerata = tipoCassa == null || tipoCassa.isEmpty() ? "001" : "002";
						//creo il model e lo inserisco
						MProdotto prodotto = new MProdotto();
						prodotto.setBarcode(barcode);
						prodotto.setBarcodeFornitore(barcodeFornitore);
						prodotto.setBrand("DATE");
						prodotto.setCassa(cassa);
						prodotto.setCategoria(categoria);
						prodotto.setChiaveCliente(modello + taglia);
						prodotto.setCodiceModello(modello);
//						prodotto.setColore("-");
//						prodotto.setComposizione("-");
						prodotto.setDescrizione(descrizione);
						prodotto.setMadeIn("ITA");
						prodotto.setTaglia(taglia);
						prodotto.setNumerata("001");
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					spostaFile(file, folderPath + "errori");
				}				
			}
		}		
	}

}
