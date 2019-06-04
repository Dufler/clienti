package it.ltc.clienti.date.ordini;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import it.ltc.database.dao.legacy.NazioniDao;
import it.ltc.database.model.legacy.Nazioni;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.model.interfaces.exception.ModelAlreadyExistentException;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.importatore.ConfigurazioneMessaggiImportazioneStandard;
import it.ltc.model.interfaces.importatore.RisultatoImportazione;
import it.ltc.model.interfaces.importatore.RisultatoImportazioneStandard;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;
import it.ltc.model.interfaces.ordine.MContrassegno;
import it.ltc.model.interfaces.ordine.MInfoSpedizione;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.ProdottoOrdinato;
import it.ltc.model.interfaces.ordine.TipoContrassegno;
import it.ltc.model.interfaces.ordine.TipoIDProdotto;
import it.ltc.model.persistence.ordine.ControllerOrdiniSQLServer;
import it.ltc.utility.miscellanea.string.ObjectParser;

public class ImportaOrdini extends ControllerOrdiniSQLServer {
	
	private static final Logger logger = Logger.getLogger(ImportaOrdini.class);
	
	private final NazioniDao daoNazioni;

	public ImportaOrdini(String persistenceUnit) {
		super(persistenceUnit);
		this.daoNazioni = new NazioniDao(persistenceUnit);
	}
	
	public RisultatoImportazione importa(File file) throws IOException {
		String nomeFile = file.getName();
		int totali = 0;
		int inseriti = 0;
		int giàPresenti = 0;
		List<String> erroriValidazione = new LinkedList<>();
		List<String> erroriGenerici = new LinkedList<>();
		HashMap<String, MOrdine> mappaOrdini = new HashMap<>();
		try {
			ObjectParser<OrdineDate> parser = new ObjectParser<>(OrdineDate.class, 1445);
			List<OrdineDate> ordini = parser.parsaOggetto(file);
			//parse degli ordini contenuti nel file
			for (OrdineDate ordine : ordini) {
				//Inserisco le informazioni di testata se necessario
				if (!mappaOrdini.containsKey(ordine.getNumeroOrdine())) {
					MOrdine testata = new MOrdine();
					//testata.setTipoImportazione(TipoImportazioneOrdine.SENZA_IMPEGNO);
					testata.setDataDocumento(ordine.getDataOrdine());
					testata.setDataOrdine(ordine.getDataOrdine());
					testata.setDestinatario(getDestinatario(ordine));
					testata.setMittente(getMittente(ordine));
					testata.setNote(ordine.getNote());
					testata.setRiferimentoDocumento(ordine.getNumeroOrdine());
					testata.setRiferimentoOrdine(ordine.getSezioneDocumento() + "/" + ordine.getNumeroOrdine() + "/" + ordine.getAnno());
					//Tipo d'ordine
					String tipo = ordine.getTipoFFW().equals("FFW1") ? "SPREPACK" : "ORD";
					testata.setTipo(tipo);
					testata.setTipoDocumento("DDT");
					testata.setTipoIdentificazioneProdotti(TipoIDProdotto.BARCODE);
					testata.setNomeFile(file.getName());
					//Aggiungo le info sulla spedizione
					MInfoSpedizione spedizione = new MInfoSpedizione();
					String corriere = testata.getDestinatario().getNazione().equals("ITA") ? "TNT" : "DHL";
					spedizione.setCorriere(corriere);
					spedizione.setTipoDocumento("DDT");
					spedizione.setDataDocumento(ordine.getDataOrdine());
					spedizione.setRiferimentoDocumento(ordine.getNumeroOrdine());
					if (ordine.getValoreContrassegno() != null && ordine.getValoreContrassegno() > 0) {
						MContrassegno contrassegno = new MContrassegno();
						contrassegno.setValore(ordine.getValoreContrassegno());
						contrassegno.setTipo(TipoContrassegno.valueOf(ordine.getTipoContrassegno()));
						contrassegno.setValuta("EUR");
						spedizione.setContrassegno(contrassegno);
					}					
					testata.setInfoSpedizione(spedizione);
					mappaOrdini.put(ordine.getNumeroOrdine(), testata);
				}
				//Aggiungo la riga se contiene oggetti
				if (ordine.getTipoRiga() != 4) {
					MOrdine testata = mappaOrdini.get(ordine.getNumeroOrdine());
					ProdottoOrdinato prodotto = new ProdottoOrdinato();
					prodotto.setBarcode(ordine.getBarcode());
					prodotto.setMagazzinoLTC("PG1");
					prodotto.setMagazzinoCliente("LP1");
					prodotto.setNumeroRiga(ordine.getNumeroRiga());
					//prodotto.setQuantita(ordine.getQuantità());
					prodotto.setQuantita(parsaQuantità(ordine.getQuantitàPerTaglia(), ordine.getTipoAssortimento(), ordine.getQuantitàCasse()));
					ordine.getQuantitàPerTaglia();
					prodotto.setNote(ordine.getNoteRiga());
					testata.aggiungiProdotto(prodotto);
				}						
			}
			//validazione e inserimento.
			for (String riferimento : mappaOrdini.keySet()) {
				totali += 1;
				MOrdine ordine = mappaOrdini.get(riferimento);
				valida(ordine);
				MOrdine inserito = inserisci(ordine);
				if (inserito != null) {
					//FIX: Aggiorno il campo nrdoc riportandoci l'ID affinchè sia univoco in fase di spedizione.
					try {
						TestataOrdini testata = daoOrdini.trovaDaID(inserito.getId());
						testata.setNrDoc(Integer.toString(testata.getIdTestaSped()));
						testata = daoOrdini.aggiorna(testata);
						if (testata == null)
							logger.error("Aggiornamento fallito del nrdoc sulla testata ID: " + inserito.getId());
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					inseriti += 1;
				}
			}
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
		ConfigurazioneMessaggiImportazioneStandard messaggi = getMessaggi();
		RisultatoImportazioneOrdini risultato = new RisultatoImportazioneOrdini(messaggi, nomeFile, totali, inseriti, giàPresenti, erroriValidazione, erroriGenerici);
		return risultato;
	}
	
//	private int parsaQuantità(String quantitàPerTaglia, String tipoAssortimento, int quantità) {
//		int totale = 0;
//		for (int index = 0; index < 88; index +=4) {
//			try {
//				totale += Integer.parseInt(quantitàPerTaglia.substring(index, index + 4));
//			} catch (Exception e) { 
//				logger.error(e.getMessage(), e);
//				totale = -1;
//				break;
//			}
//		}
//		if (tipoAssortimento.equals("C")) {
//			totale = totale / quantità;
//		}
//		return totale;
//	}
	
	private int parsaQuantità(String quantitàPerTaglia, String tipoAssortimento, int quantitàCasse) {
		int totale = 0;
		if (tipoAssortimento.equals("C")) {
			totale = quantitàCasse;
		} else for (int index = 0; index < 88; index +=4) {
			try {
				totale += Integer.parseInt(quantitàPerTaglia.substring(index, index + 4));
			} catch (Exception e) { 
				logger.error(e.getMessage(), e);
				totale = -1;
				break;
			}
		}		
		return totale;
	}
	
	/**
	 * Per Date ho la particolarità per cui i riferimenti agli ordini vengono riciclati ogni anno.
	 * Mi assicuro che sia univoco solo all'interno dello stesso anno.
	 */
	public TestataOrdini trovaOrdineDaRiferimento(String riferimento) {
		GregorianCalendar now = new GregorianCalendar();
		String riferimentoEffettivo = now.get(Calendar.YEAR) + riferimento;
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestataOrdini> criteria = cb.createQuery(TestataOrdini.class);
		Root<TestataOrdini> member = criteria.from(TestataOrdini.class);
		criteria.select(member).where(cb.equal(member.get("rifOrdineCli"), riferimentoEffettivo));
		List<TestataOrdini> list = em.createQuery(criteria).setMaxResults(1).getResultList();
		em.close();
		TestataOrdini ordine = list.size() == 1 ? list.get(0) : null;
		return ordine;
	}
	
	/**
	 * Per Date ho la particolarità per cui i riferimenti agli ordini vengono riciclati ogni anno.
	 * Aggiungo l'anno davanti per renderlo univoco all'interno dello stesso anno.
	 */
	@Override
	public boolean inserisciInformazioniAggiuntiveTestata(MOrdine ordine, TestataOrdini testata) {
		String riferimento = testata.getAnnoOrdine().toString() + ordine.getRiferimentoOrdine();
		testata.setRifOrdineCli(riferimento);
		return true;
	}
	
	protected MIndirizzo getDestinatario(OrdineDate ordine) {
		MIndirizzo destinatario = new MIndirizzo();
		boolean usaContabile = ordine.getRagioneSociale().isEmpty();
		if (usaContabile) {
			//Recupero la nazione corretta
			Nazioni nazione = daoNazioni.trovaDaCodificaCliente(ordine.getCodiceNazioneContabile());
			if (nazione == null)
				throw new ModelValidationException("La codifica di nazione indicata non esiste a sistema. (" + ordine.getCodiceNazioneContabile() + ")");
			//recupero la provincia
			String provincia = ordine.getProvinciaContabile();
			if (provincia == null || provincia.isEmpty())
				provincia = ordine.getProvincia();
			if (provincia == null || provincia.isEmpty())
				provincia = "XX";
			//Compongo l'indirizzo
			destinatario.setCap(ordine.getCapContabile());
			destinatario.setCodice(ordine.getCodiceClienteParte1());
			destinatario.setIndirizzo(ordine.getIndirizzoContabile());
			destinatario.setLocalita(ordine.getCittaContabile());
			destinatario.setNazione(nazione.getCodIso());
			destinatario.setProvincia(provincia);
			destinatario.setRagioneSociale(ordine.getRagioneSocialeContabile());
			destinatario.setTelefono(ordine.getTelefonoContabile());
		} else {
			//Recupero la nazione corretta
			Nazioni nazione = daoNazioni.trovaDaCodificaCliente(ordine.getCodiceNazione());
			if (nazione == null)
				throw new ModelValidationException("La codifica di nazione indicata non esiste a sistema. (" + ordine.getCodiceNazione() + ")");
			//recupero la provincia
			String provincia = ordine.getProvincia();
			if (provincia == null || provincia.isEmpty())
				provincia = ordine.getProvinciaContabile();
			if (provincia == null || provincia.isEmpty())
				provincia = "XX";
			//Compongo l'indirizzo
			destinatario.setCap(ordine.getCap());
			destinatario.setCodice(ordine.getCodiceClienteParte1());
			destinatario.setIndirizzo(ordine.getIndirizzo());
			destinatario.setLocalita(ordine.getCitta());
			destinatario.setNazione(nazione.getCodIso());
			destinatario.setProvincia(provincia);
			destinatario.setRagioneSociale(ordine.getRagioneSociale());
			destinatario.setTelefono(ordine.getTelefono());
		}		
		return destinatario;
	}
	
	protected MIndirizzo getMittente(OrdineDate ordine) {
		MIndirizzo mittente = new MIndirizzo();
		mittente.setCap("06073");
		mittente.setCodice("DEFAULT");
		mittente.setEmail("support@ltc-logistics.it");
		mittente.setIndirizzo("Via Firenze, 41");
		mittente.setLocalita("Corciano");
		mittente.setNazione("IT");
		mittente.setProvincia("PG");
		mittente.setRagioneSociale("DATE");
		mittente.setTelefono("");
		return mittente;
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
