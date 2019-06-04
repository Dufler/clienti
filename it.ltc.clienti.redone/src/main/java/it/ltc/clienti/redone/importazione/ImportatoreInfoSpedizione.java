package it.ltc.clienti.redone.importazione;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.TestaCorr;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.model.interfaces.exception.ModelAlreadyExistentException;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.importatore.ConfigurazioneMessaggiImportazioneStandard;
import it.ltc.model.interfaces.importatore.RisultatoImportazione;
import it.ltc.model.interfaces.importatore.RisultatoImportazioneStandard;
import it.ltc.model.interfaces.ordine.MContrassegno;
import it.ltc.model.interfaces.ordine.MInfoSpedizione;
import it.ltc.model.interfaces.ordine.TipoContrassegno;
import it.ltc.model.persistence.ordine.ControllerInfoSpedizioneSQLServer;
import it.ltc.utility.csv.FileCSV;

public class ImportatoreInfoSpedizione extends ControllerInfoSpedizioneSQLServer {
	
	private static final Logger logger = Logger.getLogger(ImportatoreInfoSpedizione.class);

	public ImportatoreInfoSpedizione(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	public RisultatoImportazione importaSpedizioni(FileCSV csv) {
		String nomeFile = csv.getNomeFile();
		int totali = 0;
		int inseriti = 0;
		int giàPresenti = 0;
		List<String> erroriValidazione = new LinkedList<>();
		List<String> erroriGenerici = new LinkedList<>();
		while(csv.prossimaRiga()) {
			try {
				totali += 1;
				MInfoSpedizione ordine = parsaInfoSpedizione(csv);
				valida(ordine);
				MInfoSpedizione inserito = inserisci(ordine);
				if (inserito != null)
					inseriti += 1;
			} catch (ModelAlreadyExistentException e) {
				giàPresenti += 1;
				logger.warn(e.getMessage()); //Bisogna capire se loro riescono a mandarci riferimenti univoci per gli ordini.
			} catch (ModelValidationException | ModelPersistenceException e) {
				erroriValidazione.add(e.getMessage());
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				erroriGenerici.add(e.getMessage());
				logger.error(e.getMessage(), e);
			}
		}
		ConfigurazioneMessaggiImportazioneStandard messaggi = getMessaggi();
		RisultatoImportazioneInfoSpedizione risultato = new RisultatoImportazioneInfoSpedizione(messaggi, nomeFile, totali, inseriti, giàPresenti, erroriValidazione, erroriGenerici);
		return risultato;
	}
	
	private MInfoSpedizione parsaInfoSpedizione(FileCSV csv) {
		MInfoSpedizione model = new MInfoSpedizione();
		model.aggiungiRiferimentoOrdine(csv.getStringa("identifier"));
		model.setCodiceCorriere(csv.getStringa("carriercode"));
		model.setCorriere(csv.getStringa("carrier"));
		model.setDataConsegna(csv.getData("deliverydate"));
		model.setDataDocumento(csv.getData("documentdate"));
		model.setNote(csv.getStringa("notes"));
		model.setRiferimentoDocumento(csv.getStringa("document"));
		String serviceLevel = csv.getStringa("servicelevel");
		model.setServizioCorriere(serviceLevel == null || serviceLevel.isEmpty() ? "DEF" : serviceLevel);
		model.setTipoDocumento("DDT");
		model.setValoreDoganale(csv.getNumerico("customvalue"));
		MContrassegno contrassegno = parsaInfoContrassegno(csv);
		model.setContrassegno(contrassegno);
		return model;
	}
	
	private MContrassegno parsaInfoContrassegno(FileCSV csv) {
		MContrassegno contrassegno;
		try {
			Double valoreContrassegno = csv.getNumerico("codvalue");
			if (valoreContrassegno != null && valoreContrassegno <= 0)
				throw new RuntimeException("Il valore del contrassegno non è valido. (" + valoreContrassegno + ")");
			String tipo = csv.getStringa("codtype") != null ? csv.getStringa("codtype") : "";
			TipoContrassegno tipoContrassegno = TipoContrassegno.valueOf(tipo);
			contrassegno = new MContrassegno();
			contrassegno.setValore(valoreContrassegno);
			contrassegno.setTipo(tipoContrassegno);
		} catch (IllegalArgumentException e) {
			logger.warn("E' stato inserito un valore errato per il tipo di contrassegno. (" + csv.getStringa("codtype") + ")");
			contrassegno = null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			contrassegno = null;
		}		
		return contrassegno;
	}
	
	public ConfigurazioneMessaggiImportazioneStandard getMessaggi() {
		ConfigurazioneMessaggiImportazioneStandard messaggi = new ConfigurazioneMessaggiImportazioneStandard();
		messaggi.setIntro("Risultato importazione dati spedizioni dal file: ");
		messaggi.setTotali("Spedizioni contenute nel file: ");
		messaggi.setElementiInseriti("Nuove spedizioni inseriti a sistema: ");
		messaggi.setNessunNuovoElemento("Alert: nessuna nuova spedizione inserita a sistema!");
		messaggi.setElementiGiaPresenti("Spedizioni già presenti a sistema: ");
		messaggi.setErroriValidazione("Spedizioni con errori in validazione: ");
		messaggi.setErroriGenerici("Spedizioni con errori generici: ");	
		return messaggi;
	}
	
	private class RisultatoImportazioneInfoSpedizione extends RisultatoImportazioneStandard {
		
		RisultatoImportazioneInfoSpedizione(ConfigurazioneMessaggiImportazioneStandard messaggi, String nomeFile, int totali, int inseriti, int giàPresenti, List<String> erroreValidazione, List<String> erroreGenerico) {
			super(messaggi, nomeFile, totali, inseriti, giàPresenti, erroreValidazione, erroreGenerico);
		}
		
	}
	
	protected void setInfoDestinatario(TestataOrdini ordine, TestaCorr spedizione) {
		Destinatari destinatario = controllerIndirizzi.trovaDestinatario(ordine.getIdDestina());
		if (destinatario == null)
			throw new ModelPersistenceException("Il destinatario per la spedizione non è stato trovato!");
		spedizione.setCap(destinatario.getCap());
		spedizione.setIndirizzo(destinatario.getIndirizzo());
		spedizione.setLocalita(destinatario.getLocalita());
		spedizione.setNazione(destinatario.getCodNaz()); //ISO 2 per TNT.
		spedizione.setProvincia(destinatario.getProvincia());
		spedizione.setRagSocDest(destinatario.getRagSoc1());
		spedizione.setRagSocEst(destinatario.getRagSoc2());
	}

}
