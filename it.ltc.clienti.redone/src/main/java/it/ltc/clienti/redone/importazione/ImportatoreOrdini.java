package it.ltc.clienti.redone.importazione;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.model.interfaces.exception.ModelAlreadyExistentException;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.importatore.ConfigurazioneMessaggiImportazioneStandard;
import it.ltc.model.interfaces.importatore.RisultatoImportazione;
import it.ltc.model.interfaces.importatore.RisultatoImportazioneStandard;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;
import it.ltc.model.interfaces.ordine.MInfoSpedizione;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.ProdottoOrdinato;
import it.ltc.model.interfaces.ordine.TipoIDProdotto;
import it.ltc.model.persistence.ordine.ControllerOrdiniSQLServer;
import it.ltc.utility.csv.FileCSV;

public class ImportatoreOrdini extends ControllerOrdiniSQLServer {
	
	private static final Logger logger = Logger.getLogger(ImportatoreOrdini.class);

	public ImportatoreOrdini(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	public RisultatoImportazione importaOrdini(FileCSV csv) {
		String nomeFile = csv.getNomeFile();
		int totali = 0;
		int inseriti = 0;
		int giàPresenti = 0;
		List<String> erroriValidazione = new LinkedList<>();
		List<String> erroriGenerici = new LinkedList<>();
		HashMap<String, MOrdine> mappaOrdini = new HashMap<>();
		while(csv.prossimaRiga()) {
			parsaOrdine(csv, mappaOrdini);
		}
		for (String riferimento : mappaOrdini.keySet()) {
			MOrdine ordine = mappaOrdini.get(riferimento);
			try {
				totali += 1;
				valida(ordine);
				MOrdine inserito = inserisci(ordine);
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
		RisultatoImportazioneOrdini risultato = new RisultatoImportazioneOrdini(messaggi, nomeFile, totali, inseriti, giàPresenti, erroriValidazione, erroriGenerici);
		return risultato;
	}
	
	private void parsaOrdine(FileCSV csv, HashMap<String, MOrdine> mappaOrdini) {
		String riferimento = csv.getStringa("identifier");
		MOrdine model = mappaOrdini.containsKey(riferimento) ? mappaOrdini.get(riferimento) : parsaTestata(csv, mappaOrdini);
		ProdottoOrdinato riga = parsaRiga(csv);
		model.aggiungiProdotto(riga);		
	}
	
	private MOrdine parsaTestata(FileCSV csv, HashMap<String, MOrdine> mappaOrdini) {
		MOrdine model = new MOrdine();
		model.setDataConsegna(csv.getData("deliverydate"));
		model.setDataOrdine(csv.getData("orderdate"));
		model.setDestinatario(parsaDestinatario(csv));
		model.setMittente(parsaMittente(csv));
		model.setNote(csv.getStringa("notes"));
		model.setPriorita(csv.getIntero("priority"));
		model.setRiferimentoDocumento(csv.getStringa("reference"));
		model.setRiferimentoOrdine(csv.getStringa("identifier"));
		model.setTipo(csv.getStringa("type"));
		model.setTipoIdentificazioneProdotti(TipoIDProdotto.BARCODE);
		model.setInfoSpedizione(parsaInfoSpedizione(csv));
		//Aggiungo l'ordine alla mappa
		mappaOrdini.put(csv.getStringa("identifier"), model);
		return model;
	}
	
	private MInfoSpedizione parsaInfoSpedizione(FileCSV csv) {
		MInfoSpedizione infoSpedizione = new MInfoSpedizione();
		infoSpedizione.setCodiceCorriere("TNT");
		infoSpedizione.setCorriere("TNT");
		infoSpedizione.setServizioCorriere("DEF");
		return infoSpedizione;
	}
	
	private MIndirizzo parsaDestinatario(FileCSV csv) {
		MIndirizzo destinatario = new MIndirizzo();
		destinatario.setCap(csv.getStringa("customerzipcode"));
		destinatario.setEmail(csv.getStringa("customeremail"));
		destinatario.setIndirizzo(csv.getStringa("customeraddress"));
		destinatario.setLocalita(csv.getStringa("customertown"));
		destinatario.setNazione(csv.getStringa("customernation"));
		destinatario.setProvincia("XX" /*csv.getStringa("customerprovince")*/); //FIXME - Ho inserito momentaneamente la provincia come XX (estero) in quanto ce la passano in maniera errata.
		destinatario.setRagioneSociale(csv.getStringa("customername"));
		destinatario.setTelefono(csv.getStringa("customerphone"));
		return destinatario;
	}
	
	private MIndirizzo parsaMittente(FileCSV csv) {
		MIndirizzo mittente = new MIndirizzo();
		mittente.setCap("59100");
		mittente.setEmail("infopo@ltc-logistics.it");
		mittente.setIndirizzo("Via di Golfienti 4/17");
		mittente.setLocalita("Interporto Prato");
		mittente.setNazione("ITA");
		mittente.setProvincia("PO");
		mittente.setRagioneSociale("ReDone");
		mittente.setTelefono("0574521112");
		return mittente;
	}
	
	private ProdottoOrdinato parsaRiga(FileCSV csv) {
		ProdottoOrdinato riga = new ProdottoOrdinato();
		riga.setBarcode(csv.getStringa("product"));
		riga.setMagazzinoCliente(csv.getStringa("warehouse"));
		riga.setNumeroRiga(csv.getIntero("row") != null ? csv.getIntero("row") : 0);
		riga.setQuantita(csv.getIntero("quantity") != null ? csv.getIntero("quantity") : 0);
		riga.setRiferimentoCliente(csv.getStringa("reference"));
		return riga;
	}
	
	public ConfigurazioneMessaggiImportazioneStandard getMessaggi() {
		ConfigurazioneMessaggiImportazioneStandard messaggi = new ConfigurazioneMessaggiImportazioneStandard();
		messaggi.setIntro("Risultato importazione ordini dal file: ");
		messaggi.setTotali("Ordini contenuti nel file: ");
		messaggi.setElementiInseriti("Nuovi ordini inseriti a sistema: ");
		messaggi.setNessunNuovoElemento("Alert: nessun nuovo ordine inserito a sistema!");
		messaggi.setElementiGiaPresenti("Ordini già presenti a sistema: ");
		messaggi.setErroriValidazione("Ordini con errori in validazione: ");
		messaggi.setErroriGenerici("Ordini con errori generici: ");	
		return messaggi;
	}
	
	private class RisultatoImportazioneOrdini extends RisultatoImportazioneStandard {
		
		RisultatoImportazioneOrdini(ConfigurazioneMessaggiImportazioneStandard messaggi, String nomeFile, int totali, int inseriti, int giàPresenti, List<String> erroreValidazione, List<String> erroreGenerico) {
			super(messaggi, nomeFile, totali, inseriti, giàPresenti, erroreValidazione, erroreGenerico);
		}
		
	}

}
