package it.ltc.clienti.redone.importazione;

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

public class ImportatoreProdotti extends ControllerProdottoSQLServer {
	
	private static final Logger logger = Logger.getLogger(ImportatoreProdotti.class);

	public ImportatoreProdotti(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	public RisultatoImportazione importaArticoli(FileCSV csv) {
		//boolean importazioneOk = true;
		String nomeFile = csv.getNomeFile();
		int totali = 0;
		int inseriti = 0;
		int giàPresenti = 0;
		List<String> erroriValidazione = new LinkedList<>();
		List<String> erroriGenerici = new LinkedList<>();
		while(csv.prossimaRiga()) {
			try {
				totali += 1;
				MProdotto prodotto = parsaProdotto(csv);
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
		//return importazioneOk;
		ConfigurazioneMessaggiImportazioneStandard messaggi = getMessaggi();
		return new RisultatoImportazioneProdotti(messaggi, nomeFile, totali, inseriti, giàPresenti, erroriValidazione, erroriGenerici);
	}
	
	private MProdotto parsaProdotto(FileCSV csv) {
		MProdotto model = new MProdotto();
		model.setBarcode(csv.getStringa("barcode"));
		model.setBrand(csv.getStringa("brand"));
		model.setCassa(Cassa.NO);
		model.setCategoria("STESO");
		model.setChiaveCliente(csv.getStringa("sku"));
		model.setCodiceModello(csv.getStringa("model"));
		model.setColore(csv.getStringa("color"));
		model.setComposizione(csv.getStringa("composition"));
		model.setDescrizione(csv.getStringa("description"));
		model.setDescrizioneAggiuntiva(csv.getStringa("description"));
		model.setMadeIn(csv.getStringa("madein"));
		model.setNote(csv.getStringa("notes"));
		model.setTaglia(csv.getStringa("size"));
		return model;
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
