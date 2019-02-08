package it.ltc.clienti.forza.ftp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import it.ltc.clienti.forza.ConfigurationUtility;
import it.ltc.clienti.forza.ftp.model.LinnworksOrderLine;
import it.ltc.database.dao.legacy.bundle.CasseDao;
import it.ltc.database.model.centrale.Cap;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.bundle.Casse;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;
import it.ltc.model.interfaces.ordine.MInfoSpedizione;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.ProdottoOrdinato;
import it.ltc.model.interfaces.ordine.TipoIDProdotto;
import it.ltc.model.persistence.ordine.ControllerOrdiniSQLServer;

public class ManagerImportazione extends ControllerOrdiniSQLServer {
	
	private static final Logger logger = Logger.getLogger("ManagerImportazione");
	
	private static final String statoDefault = "IMPO";
	private static final String bundle = "BUNDLE";
	
	private final SimpleDateFormat sdfRenamer;
	
	private final String corriere;
	private final String codicecorriere;
	private final String servizioCorriere;
	private final MIndirizzo mittente;
	
	private static String magazzinoDefault;
	
	private final List<String> statiErrore;
	
	private final CasseDao daoCasse;
	
	public ManagerImportazione(String persistenceUnit) {
		super(persistenceUnit);
		sdfRenamer = new SimpleDateFormat("yyyyMMddHHmmss");
		//Configurazione
		ConfigurationUtility config = ConfigurationUtility.getInstance();
		magazzinoDefault = config.getMagazzinoDefault();
		corriere = config.getCorriere();
		codicecorriere = config.getCodiceClienteCorriere();
		servizioCorriere = config.getServizioCorriere();
		mittente = config.getMittente();
		statiErrore = config.getStatiErrore();
		daoCasse = new CasseDao(persistenceUnit);
	}
	
	/**
	 * Controlla se già esiste un ordine a sistema con quello specifico Linnworks order ID.
	 * Nel caso in cui esista ma nello stato "INSE" o "ERRO" permette comunque di inserirne uno nuovo.
	 * @param orderID l'Order ID come dentro ai sistemi Linnworks.
	 * @return true se esiste già, false se non esiste oppure se esiste ma è in uno stato di errore.
	 */
	public boolean checkIfOrderExists(String orderID) {
		boolean exists = false;
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestataOrdini> criteria = cb.createQuery(TestataOrdini.class);
		Root<TestataOrdini> member = criteria.from(TestataOrdini.class);
		criteria.select(member).where(cb.equal(member.get("rifOrdineCli"), orderID));
		List<TestataOrdini> list = em.createQuery(criteria).getResultList();
		em.close();
		for (TestataOrdini testata : list) {
			String stato = testata.getStato() != null ? testata.getStato() : "";
			boolean inErrore = statiErrore.contains(stato);
			//Se non è andata in errore allora significa che me l'hanno già passato.
			if (!inErrore)
				exists = true;
		}
		return exists;
	}
	
	/**
	 * Genera un model che rappresenta l'ordine da evadare a partire dalle righe d'ordine passate come argomento.
	 * @param righe
	 * @return
	 */
	public MOrdine generateOrder(List<LinnworksOrderLine> righe) {
		LinnworksOrderLine riga = righe.get(0);
		MOrdine ordine = new MOrdine();
		ordine.setDataOrdine(riga.getReceivedDate());
		ordine.setDestinatario(getDestinatario(riga));
		ordine.setMittente(mittente);
		String note = riga.getNote();
		if (note != null && note.length() > 250)
			note = note.substring(0, 250);
		ordine.setNote(note);
		ordine.setPriorita(1);
		ordine.setRiferimentoDocumento(riga.getOrderId());
		ordine.setRiferimentoOrdine(riga.getOrderId());
		ordine.setStato(statoDefault);
		ordine.setTipo("WEB");
		//Info sui prodotti
		ordine.setTipoIdentificazioneProdotti(TipoIDProdotto.CHIAVE);
		ordine.getProdotti().addAll(getProdotti(righe));
		//Info sulla spedizione
		MInfoSpedizione infoSpedizione = new MInfoSpedizione();
		infoSpedizione.setCorriere(corriere);
		infoSpedizione.setCodiceCorriere(codicecorriere);
		infoSpedizione.setServizioCorriere(servizioCorriere);
		infoSpedizione.setValoreDoganale(riga.getCostoTotale());
		ordine.setInfoSpedizione(infoSpedizione);
		return ordine;
	}
	
	/**
	 * Esegue la validazione del model passato come argomento e ne effettua la persistenza sui DB.
	 * @param ordine l'ordine da validare e salvare.
	 * @return l'esito dell'operazione.
	 */
	public boolean validateAndSaveOrder(MOrdine ordine) {
		boolean success;
		try {
			//mysqlController.valida(ordine);
			valida(ordine);
			//mysqlController.inserisci(ordine);
			inserisci(ordine);
			success = true;
		} catch (ModelValidationException | ModelPersistenceException e) {
			success = false;
			//Inserisco comunque un ordine in stato "INSE", nelle note si trova la motivazione dell'errore.
			ordine.setNote(e.getMessage());
			saveErrateOrder(ordine);
		}
		return success;
	}
	
	public void saveErrateOrder(MOrdine ordine) {
		TestataOrdini testata = new TestataOrdini();
		GregorianCalendar today = new GregorianCalendar();
		Date timeStamp = new Date(today.getTimeInMillis());
		Integer anno = today.get(Calendar.YEAR);
		testata.setAnnodoc(anno);
		testata.setAnnoOrdine(anno);
		testata.setCodiceClienteCorriere(ordine.getInfoSpedizione().getCodiceCorriere());
		testata.setCodCorriere(ordine.getInfoSpedizione().getCorriere());
		testata.setCorriere(ordine.getInfoSpedizione().getCorriere());
		testata.setValoreDoganale(ordine.getInfoSpedizione().getValoreDoganale());
		testata.setDataConsegna(timeStamp);
		testata.setDataDoc(timeStamp);
		testata.setDataOrdine(timeStamp);
		testata.setIdDestina(1);
		testata.setNrLista(sdfRenamer.format(today.getTime()));
		testata.setNote(ordine.getNote());
		testata.setNrOrdine(ordine.getRiferimentoOrdine());
		testata.setRifOrdineCli(sdfRenamer.format(today.getTime()) + "_" + ordine.getRiferimentoOrdine());
		testata.setStato("ERRO");
		testata.setTipoDoc("DDT");
		testata.setIdDestina(16);
		testata.setCodCliente("110358");
		testata.setTipoOrdine("ERRORE");
		System.out.println(testata);
		boolean insert;
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.persist(testata);
			t.commit();
			insert = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			insert = false;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		if (!insert)
			logger.error("Impossibile inserire l'ordine che è andato in errore.");
	}	
	
	private MIndirizzo getDestinatario(LinnworksOrderLine riga) {
		MIndirizzo indirizzo = new MIndirizzo();
		indirizzo.setCap(riga.getCapDestinazione());
		indirizzo.setEmail(riga.getEmailDestinazione());
		//Edit: 30-01-2018: aggiungiamo anche le altre righe dell'indirizzo fino ad un massimo di 35 caratteri.
		String via = riga.getIndirizzoDestinazione1() + " " +  riga.getIndirizzoDestinazione2() + " " + riga.getIndirizzoDestinazione3();
//		if (via.length() > 35)
//			via = via.substring(0, 35);
		indirizzo.setIndirizzo(via);
		indirizzo.setLocalita(riga.getLocalitaDestinazione());
		//EDIT 30/10/2017: Bisogna(va) gestire solo il traffico Italia.
		//Apparentemente un'ordine destinato in Svizzera è andato in giacenza perchè la nazione era fissata ad "ITA"
		String nazione = riga.getIso2NazioneDestinazione();
		if (nazione == null || nazione.isEmpty()) {
			nazione = "ITA";
		}
		indirizzo.setNazione(nazione);
		Cap cap = ManagerCap.getInstance().trovaCap(riga.getCapDestinazione());
		String provincia = cap != null ? cap.getProvincia() : riga.getProvinciaDestinazione();
		if (provincia != null && provincia.length() > 2) {
			provincia = provincia.substring(0, 2);
		}
		indirizzo.setProvincia(provincia);
		indirizzo.setRagioneSociale(riga.getRagioneSocialeDestinazione());
		indirizzo.setTelefono(riga.getTelefonoDestinazione());
		return indirizzo;
	}
	
	private List<ProdottoOrdinato> getProdotti(List<LinnworksOrderLine> righe) {
		List<ProdottoOrdinato> lista = new LinkedList<ProdottoOrdinato>();
		for (LinnworksOrderLine riga : righe) {
			String sku = riga.getSku();
			Articoli articolo = daoArticoli.trovaDaSKU(sku);
			if (articolo == null)
				throw new ModelPersistenceException("Non è stato trovato un articolo per lo sku '" + sku + "'.");
			if (isBundle(articolo)) {
				 //Sembra che se si ordini 1 bundle la quantita' venga passata a 0.
				int bundleOrdinati = riga.getQuantita();
				if (bundleOrdinati > 0) {
					HashMap<String, Integer> mappaProdotti = getBundleComposition(articolo.getIdUniArticolo());
					for (String skuSingleUnit : mappaProdotti.keySet()) {
						ProdottoOrdinato prodotto = new ProdottoOrdinato();
						prodotto.setChiave(skuSingleUnit);
						prodotto.setQuantita(bundleOrdinati * mappaProdotti.get(skuSingleUnit));
						prodotto.setMagazzinoCliente(magazzinoDefault);
						prodotto.setMagazzinoLTC(magazzinoDefault);
						lista.add(prodotto);
					}
				}
			} else {
				ProdottoOrdinato prodotto = new ProdottoOrdinato();
				prodotto.setChiave(sku);
				prodotto.setQuantita(riga.getQuantita());
				prodotto.setMagazzinoCliente(magazzinoDefault);
				prodotto.setMagazzinoLTC(magazzinoDefault);
				lista.add(prodotto);
			}
		}
		return lista;
	}
	
	private boolean isBundle(Articoli articolo) {
		String tipoCassa = articolo.getTipoCassa() != null ? articolo.getTipoCassa() : "";
		boolean isBundle = tipoCassa.equals(bundle);
		return isBundle;
	}
	
	private HashMap<String, Integer> getBundleComposition(String skuBundle) {
		HashMap<String, Integer> prodotti = new HashMap<>();
		
		List<Casse> list = daoCasse.trovaDaSKUBundle(skuBundle);
				
//		EntityManager em = getManager();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Casse> criteria = cb.createQuery(Casse.class);
//		Root<Casse> member = criteria.from(Casse.class);
//		criteria.select(member).where(cb.equal(member.get("skuBundle"), skuBundle));
//		List<Casse> list = em.createQuery(criteria).getResultList();
//		em.close();
			
		for (Casse prodotto : list) {
			prodotti.put(prodotto.getSkuProdotto(), prodotto.getQuantitaProdotto());
		}
		//Controllo aggiuntivo
		if (prodotti.isEmpty())
			throw new ModelPersistenceException("Non è stato trovato un bundle per l'ID '" + skuBundle + "'.");
		return prodotti;
	}

}
