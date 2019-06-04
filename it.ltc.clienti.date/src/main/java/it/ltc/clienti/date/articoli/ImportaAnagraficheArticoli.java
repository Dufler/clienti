package it.ltc.clienti.date.articoli;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.model.interfaces.exception.ModelAlreadyExistentException;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.importatore.ConfigurazioneMessaggiImportazioneStandard;
import it.ltc.model.interfaces.importatore.RisultatoImportazione;
import it.ltc.model.interfaces.importatore.RisultatoImportazioneStandard;
import it.ltc.model.interfaces.prodotto.Cassa;
import it.ltc.model.interfaces.prodotto.MProdotto;
import it.ltc.model.persistence.prodotto.ControllerProdottoSQLServer;
import it.ltc.utility.csv.FileCSV;
import it.ltc.utility.microsoft.converter.XLStoCSV;

public class ImportaAnagraficheArticoli extends ControllerProdottoSQLServer {
	
	private static final Logger logger = Logger.getLogger(ImportaAnagraficheArticoli.class);
	
//	private final String folderPath;
//	private final String regexNomeFileArticoli;

	public ImportaAnagraficheArticoli(String persistenceUnit) {
		super(persistenceUnit);
//		ConfigurationUtility config = ConfigurationUtility.getInstance();
//		this.folderPath = config.getFolderPathImport();
//		this.regexNomeFileArticoli = config.getRegexArticoli();
	}
	
	public RisultatoImportazione importa(File file) {
		FileCSV csv = XLStoCSV.getCSV(file);
		String nomeFile = csv.getNomeFile();
		int totali = 0;
		int inseriti = 0;
		int giàPresenti = 0;
		List<String> erroriValidazione = new LinkedList<>();
		List<String> erroriGenerici = new LinkedList<>();
		for (int riga = 0; riga < csv.getRighe().size(); riga++) {
			try {
				totali += 1;
				MProdotto prodotto = parsaProdotto(csv, riga);
				valida(prodotto);
				MProdotto inserito = inserisci(prodotto);
				if (inserito != null) 
					inseriti += 1;
			} catch (ModelAlreadyExistentException e) {
				giàPresenti += 1;
				logger.warn(e.getMessage());
			} catch (ModelValidationException | ModelPersistenceException e) {
				erroriValidazione.add(e.getMessage());
				logger.error(e.getMessage(), e);
				//importazioneOk = false;
			} catch (Exception e) {
				erroriGenerici.add(e.getMessage());
				logger.error(e.getMessage(), e);
				//importazioneOk = false;
			}
		}
		ConfigurazioneMessaggiImportazioneStandard messaggi = getMessaggi();
		return new RisultatoImportazioneProdotti(messaggi, nomeFile, totali, inseriti, giàPresenti, erroriValidazione, erroriGenerici);
	}
	
	private MProdotto parsaProdotto(FileCSV csv, int riga) {
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
//		prodotto.setColore("-");
//		prodotto.setComposizione("-");
		prodotto.setDescrizione(descrizione);
		prodotto.setMadeIn("ITA");
		prodotto.setTaglia(taglia);
		prodotto.setNumerata(numerata);
		return prodotto;
	}
	
	public ConfigurazioneMessaggiImportazioneStandard getMessaggi() {
		ConfigurazioneMessaggiImportazioneStandard messaggi = new ConfigurazioneMessaggiImportazioneStandard();
		messaggi.setIntro("Risultato importazione prodotti dal file: ");
		messaggi.setTotali("Anagrafiche prodotto contenute nel file: ");
		messaggi.setElementiInseriti("Nuove anagrafiche prodotto inserite a sistema: ");
		messaggi.setNessunNuovoElemento("Alert: nessuna nuova anagrafica prodotto inserita a sistema!");
		messaggi.setElementiGiaPresenti("Anagrafiche prodotto già presenti a sistema: ");
		messaggi.setErroriValidazione("Anagrafiche prodotto con errori in validazione: ");
		messaggi.setErroriGenerici("Anagrafiche prodotto con errori generici: ");	
		return messaggi;
	}
	
	private class RisultatoImportazioneProdotti extends RisultatoImportazioneStandard {
		
		RisultatoImportazioneProdotti(ConfigurazioneMessaggiImportazioneStandard messaggi, String nomeFile, int totali, int inseriti, int giàPresenti, List<String> erroreValidazione, List<String> erroreGenerico) {
			super(messaggi, nomeFile, totali, inseriti, giàPresenti, erroreValidazione, erroreGenerico);
		}
		
	}

}
