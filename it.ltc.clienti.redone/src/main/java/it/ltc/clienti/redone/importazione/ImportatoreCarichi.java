package it.ltc.clienti.redone.importazione;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.model.interfaces.carico.MCarico;
import it.ltc.model.interfaces.carico.MRigaCarico;
import it.ltc.model.interfaces.exception.ModelAlreadyExistentException;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.importatore.ConfigurazioneMessaggiImportazioneStandard;
import it.ltc.model.interfaces.importatore.RisultatoImportazione;
import it.ltc.model.interfaces.importatore.RisultatoImportazioneStandard;
import it.ltc.model.interfaces.ordine.TipoIDProdotto;
import it.ltc.model.persistence.carico.ControllerCaricoSQLServer;
import it.ltc.utility.csv.FileCSV;

public class ImportatoreCarichi extends ControllerCaricoSQLServer {
	
	private static final Logger logger = Logger.getLogger(ImportatoreCarichi.class);

	public ImportatoreCarichi(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	public RisultatoImportazione importaCarichi(FileCSV csv) {
		String nomeFile = csv.getNomeFile();
		int totali = 0;
		int inseriti = 0;
		int giàPresenti = 0;
		List<String> erroriValidazione = new LinkedList<>();
		List<String> erroriGenerici = new LinkedList<>();
		//Parso il file ed elaboro le info sui carichi.
		HashMap<String, MCarico> mappaCarichi = new HashMap<>();
		while(csv.prossimaRiga()) {
			parsaCarico(csv, mappaCarichi);
		}
		//Vado in validazione e scrittura delle info trovate.
		for (String riferimento : mappaCarichi.keySet()) {
			MCarico carico = mappaCarichi.get(riferimento);
			try {
				totali += 1;
				valida(carico);
				MCarico inserito = inserisci(carico);
				if (inserito != null)
					inseriti += 1;
			} catch (ModelAlreadyExistentException e) {
				giàPresenti += 1;
				logger.warn(e.getMessage()); //Bisogna capire se loro riescono a mandarci riferimenti univoci per i carichi.
			} catch (ModelValidationException | ModelPersistenceException e) {
				erroriValidazione.add(e.getMessage());
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				erroriGenerici.add(e.getMessage());
				logger.error(e.getMessage(), e);
			}
		}
		ConfigurazioneMessaggiImportazioneStandard messaggi = getMessaggi();
		RisultatoImportazioneCarichi risultato = new RisultatoImportazioneCarichi(messaggi, nomeFile, totali, inseriti, giàPresenti, erroriValidazione, erroriGenerici);
		return risultato;
	}
	
	private void parsaCarico(FileCSV csv, HashMap<String, MCarico> mappaCarichi) {
		String riferimento = csv.getStringa("identifier");
		MCarico model = mappaCarichi.containsKey(riferimento) ? mappaCarichi.get(riferimento) : parsaTestata(csv, mappaCarichi);
		MRigaCarico riga = parsaRiga(csv);
		model.aggiungiProdotto(riga);		
	}
	
	private MRigaCarico parsaRiga(FileCSV csv) {
		MRigaCarico riga = new MRigaCarico();
		String prodotto = csv.getStringa("product");
		if (prodotto == null || prodotto.isEmpty()) 
			throw new ModelValidationException("Nessun riferimento prodotto inserito (CSV: " + csv.getNomeFile() + ")");
		riga.setChiave(prodotto);
		riga.setBarcode(prodotto); //Loro ci passano solo il barcode.
		Integer quantità = csv.getIntero("quantity");
		if (quantità == null) 
			throw new ModelValidationException("Nessuna quantità dichiarata per il prodotto " + prodotto + " (CSV: " + csv.getNomeFile() + ")");
		riga.setQuantitaDichiarata(quantità);
		Integer numeroRiga = csv.getIntero("row");
		riga.setNumeroRiga(numeroRiga != null ? numeroRiga : 0);
		riga.setMagazzinoCliente(csv.getStringa("warehouse"));
		riga.setRiferimentoCliente(csv.getStringa("reference"));
		return riga;
	}
	
	private MCarico parsaTestata(FileCSV csv, HashMap<String, MCarico> mappaCarichi) {
		Date today = new Date();
		MCarico model = new MCarico();
		model.setDataArrivo(csv.getData("arrivaldate"));
		model.setDataCarico(today);
		model.setDataDocumento(csv.getData("arrivaldate"));
		model.setFornitore(csv.getStringa("supplier"));
		model.setRiferimento(csv.getStringa("identifier"));
		model.setRiferimentoDocumento(csv.getStringa("document"));
		String tipo = csv.getStringa("type") != null ? csv.getStringa("type").toUpperCase() : "";
		model.setTipo(tipo);
		model.setTipoDocumento("CARICO");
		model.setTipoIdentificazioneProdotti(TipoIDProdotto.BARCODE);
		//Aggiungo il carico alla mappa
		mappaCarichi.put(csv.getStringa("identifier"), model);
		return model;
	}
	
	public ConfigurazioneMessaggiImportazioneStandard getMessaggi() {
		ConfigurazioneMessaggiImportazioneStandard messaggi = new ConfigurazioneMessaggiImportazioneStandard();
		messaggi.setIntro("Risultato importazione carichi dal file: ");
		messaggi.setTotali("Carichi contenuti nel file: ");
		messaggi.setElementiInseriti("Nuovi carichi inseriti a sistema: ");
		messaggi.setNessunNuovoElemento("Alert: nessun nuovo carico inserito a sistema!");
		messaggi.setElementiGiaPresenti("Carichi già presenti a sistema: ");
		messaggi.setErroriValidazione("Carichi con errori in validazione: ");
		messaggi.setErroriGenerici("Carichi con errori generici: ");	
		return messaggi;
	}
	
	private class RisultatoImportazioneCarichi extends RisultatoImportazioneStandard {
		
		RisultatoImportazioneCarichi(ConfigurazioneMessaggiImportazioneStandard messaggi, String nomeFile, int totali, int inseriti, int giàPresenti, List<String> erroreValidazione, List<String> erroreGenerico) {
			super(messaggi, nomeFile, totali, inseriti, giàPresenti, erroreValidazione, erroreGenerico);
		}
		
	}

}
