package it.ltc.logic;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import it.ltc.clienti.ynap.model.AnagraficaOggetto;
import it.ltc.clienti.ynap.model.Destinatario;
import it.ltc.clienti.ynap.model.DettaglioOrdine;
import it.ltc.clienti.ynap.model.Marchio;
import it.ltc.clienti.ynap.model.MovimentoMagazzino;
import it.ltc.clienti.ynap.model.Ordine;
import it.ltc.clienti.ynap.model.SaldiMagazzino;
import it.ltc.database.dao.Dao;
import it.ltc.logic.GestoreSFTP.Strategy;
import it.ltc.model.order.out.Item;
import it.ltc.model.order.out.Order;
import it.ltc.model.order.out.Orders;
import it.ltc.model.order.out.Receiver;
import it.ltc.model.order.out.Shipment;
import it.ltc.model.order.out.SoldTo;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;
import it.ltc.utility.miscellanea.string.StringUtility;

public class ImportaOrdine extends Dao {

	private static final Logger logger = Logger.getLogger(ImportaOrdine.class);
	
	private final StringUtility utility;

	private static ImportaOrdine instance;

	private ImportaOrdine() {
		super("legacy-ynap");
		
		utility = new StringUtility("0", "0", true, true);
	}

	public static ImportaOrdine getInstance() {
		if (instance == null)
			instance = new ImportaOrdine();
		return instance;
	}
	
	//Questo metodo è stato usato per fare test in locale senza SFTP.
//	private List<String> getFileNamesToImport() {
//		String folderPath = "C:\\Users\\Damiano\\Desktop\\ynap\\ordine\\";
//		File folder = new File(folderPath);
//		String[] array = folder.list();
//		List<String> list = new LinkedList<>();
//		for (String file : array) {
//			list.add(folderPath + file);
//		}
//		return list;
//	}

	private List<String> getFileNamesToImport() {
		logger.info("Recupero la lista di files da importare");
		GestoreSFTP gestore = new GestoreSFTP(Strategy.ORDINI);
		List<String> localCopyNames = gestore.getNomiFiles();
		return localCopyNames;
	}
	
	private Destinatario getDestinatarioContabile(Receiver destinatario, SoldTo destinatarioContabile) {
		Destinatario contabile = new Destinatario();
		contabile.setCodiceDestinatario("C" + destinatario.getRECID());
		contabile.setCodiceDestinatarioNumerico(destinatario.getRECID());
		contabile.setTipo("CON");
		contabile.setCap(destinatarioContabile.getSTZIP());
		contabile.setCodiceISONazione(destinatarioContabile.getSTCountryCode());
		contabile.setEmail(destinatarioContabile.getSTEmail());
		contabile.setIndirizzo(destinatarioContabile.getSTAddr1() + destinatarioContabile.getSTAddr2());
		contabile.setLocalità(destinatarioContabile.getSTCity());
		contabile.setNazione(destinatarioContabile.getSTCountryCode());
		contabile.setFax(destinatarioContabile.getSTPhone());
		contabile.setProvincia(destinatarioContabile.getSTProv());
		contabile.setRagioneSociale1(destinatarioContabile.getSTName());
		contabile.setRagioneSociale2(" ");
		contabile.setCodiceContabile(" ");
		contabile.setTelefono(destinatarioContabile.getSTPhone());
		return contabile;
	}
	
	private Destinatario getDestinatarioMerce(Receiver destinatario) {
		Destinatario destinatarioMerce = new Destinatario();
		destinatarioMerce.setCap(destinatario.getRECZIP());
		destinatarioMerce.setCodiceContabile("C" + destinatario.getRECID());
		destinatarioMerce.setCodiceDestinatario(Integer.toString(destinatario.getRECID()));
		destinatarioMerce.setCodiceDestinatarioNumerico(destinatario.getRECID());
		destinatarioMerce.setCodiceISONazione(destinatario.getRECCountryCode());
		destinatarioMerce.setEmail(destinatario.getRECEmail());
		destinatarioMerce.setFax(destinatario.getRECPhone2());
//		destinatarioMerce.setIdContabile(idContabileDB);
		destinatarioMerce.setIndirizzo(destinatario.getRECAddr1() + destinatario.getRECAddr2());
		destinatarioMerce.setLocalità(destinatario.getRECCity());
		destinatarioMerce.setNazione(destinatario.getRECCountryName());
		destinatarioMerce.setNote(destinatario.getRECMess());
		destinatarioMerce.setProvincia(destinatario.getRECProv());
		destinatarioMerce.setRagioneSociale1(destinatario.getRECName());
		destinatarioMerce.setRagioneSociale2("");
		destinatarioMerce.setTelefono(destinatario.getRECPhone());
		destinatarioMerce.setTipo("DES");
		return destinatarioMerce;
	}
	
	private Ordine getTestata(String nomeFile, Order ordine, Destinatario destinatario, int progressivo) {
		Shipment spedizione = ordine.getShipment();
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		Date dataConsegna = null;
		try {
			String dataPrivistaConsegna = spedizione.getSHIPDeliveryDate();
			if (dataPrivistaConsegna == null || dataPrivistaConsegna.isEmpty())
				dataPrivistaConsegna = "1900-01-01";
			dataConsegna = dateParser.parse(dataPrivistaConsegna);
		} catch (ParseException e) {
			logger.error(e);
		}
		SimpleDateFormat formattaAnno = new SimpleDateFormat("yy");
		String anno = formattaAnno.format(new Date());
		
		String numeroLista = anno + utility.getFormattedString(ordine.getORDNum(), 14, "0", false);
		
		Ordine nuovoOrdine = new Ordine();
		
		nuovoOrdine.setCodiceCliente(destinatario.getCodiceDestinatario());
		nuovoOrdine.setIdDestinatario(destinatario.getId());
		
		nuovoOrdine.setCodiceCorriere(Integer.toString(spedizione.getSHIPCourierID()));
		nuovoOrdine.setCorriere(Integer.toString(spedizione.getSHIPCourierID()));
		nuovoOrdine.setDataConsegna(dataConsegna);
		
		nuovoOrdine.setDataOrdine(ordine.getORDDate().toGregorianCalendar().getTime());
		
		// nuovoOrdine.setNomeFile(Integer.toString(fileID));
		nuovoOrdine.setNomeFile(nomeFile);
		nuovoOrdine.setNote(ordine.getORDNote());
		nuovoOrdine.setNumeroLista(numeroLista);
		nuovoOrdine.setNumeroListaNumerico(progressivo);
		nuovoOrdine.setNumeroOrdine(ordine.getORDNum());
		nuovoOrdine.setQuantitàTotale(ordine.getItems().getItem().size());
		nuovoOrdine.setRiferimentoOrdineCliente(ordine.getORDNum());
		nuovoOrdine.setRaggruppamentoOrdini(numeroLista);
		nuovoOrdine.setPriorità(ordine.getORDRef());
		nuovoOrdine.setTipoOrdine(ordine.getORDType());
		nuovoOrdine.setStato("IMPO");
		return nuovoOrdine;
	}
	
	private int elaboraMarchio(EntityManager em, String marchio) {
		//Eseguo una replace dei caratteri "scomodi" mettendolo tutto in uppercase.
		marchio = marchio != null ? marchio.toUpperCase() : "";
		marchio = utility.getVanilla(marchio);
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Marchio> criteria = cb.createQuery(Marchio.class);
        Root<Marchio> member = criteria.from(Marchio.class);
        criteria.select(member).where(cb.equal(member.get("nome"), marchio));
        List<Marchio> lista = em.createQuery(criteria).setMaxResults(1).getResultList();
        Marchio trovato = lista.isEmpty()? null : lista.get(0);
        //Se non l'ho trovato lo inserisco
        if (trovato == null) {
        	//Calcolo il nuovo codice, c'è un indice (univoco e inutile) sul valore codice.
        	int codice = em.createNamedQuery("Marchi.progressivo", Integer.class).getSingleResult() + 1;
        	trovato = new Marchio();
        	trovato.setCodice(codice);
        	trovato.setNome(marchio);
        	em.persist(trovato);
        }
        return trovato.getCodice();
	}
	
	private String getTaglia(Item oggetto) {
		String taglia = oggetto.getITSize();
		if (taglia == null || taglia.isEmpty()) {
			int index = oggetto.getITColSize().lastIndexOf(',');
			taglia = index != -1 ? oggetto.getITColSize().substring(index + 1) : "UNI";
		}
		return taglia;
	}
	
	private String getColore(Item oggetto) {
		String colore = oggetto.getITColor();
		if (colore == null || colore.isEmpty()) {
			int index = oggetto.getITColSize().lastIndexOf(',');
			colore = index != -1 ? oggetto.getITColSize().substring(0, index) : "";
		}
		return colore;
	}
	
	private String aggiornaAnagrafica(EntityManager em, Item oggetto) {
		//Recupero l'anagrafica da aggiornare
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AnagraficaOggetto> criteria = cb.createQuery(AnagraficaOggetto.class);
        Root<AnagraficaOggetto> member = criteria.from(AnagraficaOggetto.class);
        criteria.select(member).where(cb.equal(member.get("codiceArticolo"), oggetto.getITCodiceMatricola()));
        List<AnagraficaOggetto> lista = em.createQuery(criteria).setMaxResults(1).getResultList();
        AnagraficaOggetto daAggiornare = lista.isEmpty()? null : lista.get(0);
		//Se l'ho trovata vado ad aggiornarla integrando i possibili campi
        if (daAggiornare == null)
        	throw new RuntimeException("Impossibile trovare l'anagrafica per il seriale " + oggetto.getITCodiceMatricola());
        daAggiornare.setDescrizioneAggiuntiva(oggetto.getITCustoms());
		daAggiornare.setDescrizione(oggetto.getITArtSti());
		daAggiornare.setModello(oggetto.getITCodiceMatricola());
		daAggiornare.setMarchio(elaboraMarchio(em, oggetto.getITMarchio()));
		daAggiornare.setTaglia(getTaglia(oggetto));
		daAggiornare.setColore(getColore(oggetto));
		em.merge(daAggiornare);
        return daAggiornare.getIdUnivocoArticolo();
	}
	
	private DettaglioOrdine getDettaglio(Ordine nuovoOrdine, Item oggetto, String codiceUnivocoArticolo) {
		DettaglioOrdine daInserire = new DettaglioOrdine();
		daInserire.setBarcode(oggetto.getITCodiceMatricola());
		daInserire.setBarcodeEAN(oggetto.getITCodiceMatricola());
		daInserire.setBarcodeUPC(oggetto.getITCodiceMatricola());
		daInserire.setCodiceCliente(nuovoOrdine.getCodiceCliente());
		daInserire.setIdDestinatario(nuovoOrdine.getIdDestinatario());
		daInserire.setColore(getColore(oggetto));
		daInserire.setDataOrdine(nuovoOrdine.getDataOrdine());
		daInserire.setDescrizione(oggetto.getITArtSti());
		daInserire.setIdUnivocoArticolo(codiceUnivocoArticolo);
		daInserire.setNote(oggetto.getITNote());
		daInserire.setNumeroLista(nuovoOrdine.getNumeroLista());
		daInserire.setNumeroOrdine(nuovoOrdine.getNumeroOrdine());
		daInserire.setIdTestataOrdine(nuovoOrdine.getId());
		// daInserire.setNumeroRigaOrdine(rigaOrdine); questo valore o l'univoco?
		daInserire.setNumeroRigaOrdine(oggetto.getITRMMID());
		daInserire.setRaggruppamentoStampe(nuovoOrdine.getNumeroLista());
		daInserire.setSerialeRFID(oggetto.getITCodiceMatricola());
		daInserire.setTaglia(getTaglia(oggetto));
		daInserire.setQuantitàDaImballare(0);
		daInserire.setQuantitàDaSpedire(1);
		daInserire.setQuantitàDaUbicare(1);
		return daInserire;
	}
	
	private void aggiornaSaldi(EntityManager em, String codiceUnivocoArticolo, String magazzino) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SaldiMagazzino> criteria = cb.createQuery(SaldiMagazzino.class);
        Root<SaldiMagazzino> member = criteria.from(SaldiMagazzino.class);
        criteria.select(member).where(cb.and(cb.equal(member.get("idUnivocoArticolo"), codiceUnivocoArticolo), cb.equal(member.get("magazzino"), magazzino)));
        List<SaldiMagazzino> lista = em.createQuery(criteria).setMaxResults(1).getResultList();
        SaldiMagazzino daAggiornare = lista.isEmpty()? null : lista.get(0);
        if (daAggiornare != null) {
        	 daAggiornare.setDisponibile(daAggiornare.getDisponibile() - 1);
             daAggiornare.setImpegnato(daAggiornare.getImpegnato() + 1);
             em.merge(daAggiornare);
        } else {
        	logger.warn("Nessun saldo di magazzino trovato per l'articolo '" + codiceUnivocoArticolo + "' sul magazzino " + magazzino + ", ne creo uno.");
        	SaldiMagazzino nuovo = new SaldiMagazzino();
        	nuovo.setDisponibile(-1);
        	nuovo.setEsistenza(0);
        	nuovo.setImpegnato(1);
        	nuovo.setIdUnivocoArticolo(codiceUnivocoArticolo);
        	nuovo.setMagazzino(magazzino);
        	em.persist(nuovo);
        }
	}
	
	private MovimentoMagazzino getMovimento(Ordine nuovoOrdine, String codiceUnivocoArticolo) {
		Date oggi = new Date();
		MovimentoMagazzino movimento = new MovimentoMagazzino();
		movimento.setCancellato("NO");
		movimento.setCategoriaDocumento("O");
		movimento.setCausale("IOS");
		movimento.setCollo("0000000000");
		movimento.setDataMovimento(oggi);
		movimento.setDataOrdine(nuovoOrdine.getDataOrdine());
		movimento.setDisponibile(0);
		movimento.setEsistenza(1);
		movimento.setIdUnivocoArticolo(codiceUnivocoArticolo);
		movimento.setImpegnato(1);
		movimento.setNoteDocumento(nuovoOrdine.getNote());
		movimento.setNumeroLista(nuovoOrdine.getNumeroLista());
		SimpleDateFormat ora = new SimpleDateFormat("HHmm");
		movimento.setOraMovimento(Integer.parseInt(ora.format(oggi)));
		movimento.setSegno("+");
		movimento.setSegnoDisponibile("-");
		movimento.setSegnoEsistenza("N");
		movimento.setSegnoImpegnato("+");
		movimento.setTipo("IP");
		movimento.setTipoDocumento("ORD");
		movimento.setTotali("NO");
		movimento.setTrasmesso("NO");
		movimento.setUtente("SRV");
		return movimento;
	}

	private boolean scriviDB(String nomeFile, Orders oggettiDaSpedire) {
		boolean successo = true;
		EntityManager em = getManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			int progressivo = em.createNamedQuery("TestataOrdini.progressivo", Integer.class).getSingleResult() + 1;
			for (Order ordine : oggettiDaSpedire.getOrder()) {
				// Scrivo i dati recuperati sul destinatario contabile
				Destinatario contabile = getDestinatarioContabile(ordine.getReceiver(), ordine.getInvoice().getSoldTo());
				em.persist(contabile);
				// Scrivo i dati recuperati sul destinatario della merce
				Destinatario destinatarioMerce = getDestinatarioMerce(ordine.getReceiver());
				destinatarioMerce.setIdContabile(contabile.getId());
				em.persist(destinatarioMerce);
				// Scrivo i dati recuperati sull'ordine
				Ordine nuovoOrdine = getTestata(nomeFile, ordine, destinatarioMerce, progressivo);
				em.persist(nuovoOrdine);
				// Per ogni oggetto presente nella lista aggiorno l'anagrafica già esistente e lo inserisco nella lista di picking
				// Se non è un oggetto già presente sollevo un'eccezione.
				for (Item oggetto : ordine.getItems().getItem()) {
					// Recupero l'anagrafica già presente e la aggiorno
					String codiceUnivocoArticolo = aggiornaAnagrafica(em, oggetto);
					// Inserisco l'oggetto
					DettaglioOrdine daInserire = getDettaglio(nuovoOrdine, oggetto, codiceUnivocoArticolo);
					em.persist(daInserire);
					// Aggiorno i saldi di magazzino, fisso il magazzino a PU1.
					aggiornaSaldi(em, codiceUnivocoArticolo, "PU1");
					// Aggiorno i movimenti di magazzino
					MovimentoMagazzino movimento = getMovimento(nuovoOrdine, codiceUnivocoArticolo);
					em.persist(movimento);
				}
			}
			transaction.commit();
			successo = true;
		} catch (Exception e) {
			successo = false;
			logger.error(e.getMessage(), e);
			if (transaction != null && transaction.isActive())
				transaction.rollback();
		} finally {
			em.close();
		}
		return successo;
	}

	public void importaOrdini() {
		List<String> nomiFile = getFileNamesToImport();
		logger.info("sono stati trovati " + nomiFile.size() + " files da importare.");
		int importati = 0;
		int conErrori = 0;
		for (String nomeFile : nomiFile) {
			try {
				File file = new File(nomeFile);
				JAXBContext jaxbContext = JAXBContext.newInstance(Orders.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Orders oggettiDaSpedire = (Orders) jaxbUnmarshaller.unmarshal(file);
				boolean successo = scriviDB(file.getName(), oggettiDaSpedire);
				if (successo) {
					aggiornaFileSentinella(nomeFile);
					importati += 1;
				} else {
					conErrori += 1;
				}
			} catch (JAXBException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		//Invio della mail di riepilogo.
		inviaResoconto(importati, conErrori);
	}
	
	private void aggiornaFileSentinella(String nomeFile) {
		GestoreSFTP gestore = new GestoreSFTP(Strategy.ORDINI);
		boolean aggiornamentoStato = gestore.aggiornaFileSentinella(nomeFile);
		if (!aggiornamentoStato)
			logger.error("Impossibile aggiornare il file sentinella: " + nomeFile);
	}

	private void inviaResoconto(int importati, int conErrori) {
		if (importati != 0 || conErrori != 0) {
			String oggettoMail = "Riepilogo importazione degli ordini YNAP";
			String messaggio = "Sono stati importati correttemente " + importati + " files.";
			if (conErrori > 0)
				messaggio += " " + conErrori + " non sono stati importati a causa di errori, controllare il file di log.";
			Email mail = new Email(oggettoMail, messaggio);
			ConfigurationUtility cu = ConfigurationUtility.getInstance();
			Set<String> destinatariDaAvvisare = conErrori > 0 ? cu.getIndirizziDestinatariErrori() : cu.getIndirizziDestinatari();
			MailMan postino = ConfigurationUtility.getInstance().getMailMan();
			boolean invio = postino.invia(destinatariDaAvvisare, mail);
			if (invio)
				logger.info("mail di riepilogo inviata con successo.");
			else
				logger.error("non è stato possibile inviare la mail di riepilogo");
		}
	}
	
//	public void aggiornaDescrizione() throws Exception {
//		int modificati = 0;
//		List<String> oggetti = new LinkedList<String>();
//		FileReader stream = new FileReader("C:/Users/Damiano/Desktop/SS15.csv");
//		BufferedReader reader = new BufferedReader(stream);
//		String line = reader.readLine();
//		while (line != null) {
//			oggetti.add(line);
//			line = reader.readLine();
//		}
//		reader.close();
//		for (String oggetto : oggetti) {
//			// Recupero i dati sull'oggetto
//			String[] dati = oggetto.split(";");
//			String codiceRFID = dati[3];
//			String marchio = dati[5];
//			String categoria = dati[7];
//			String colore = dati[10];
//			String taglia = dati[15];
//			String descrizione = marchio + " " + categoria + " " + colore + " " + taglia;
////			DettaglioOrdine daModificare = new DettaglioOrdine();
////			daModificare.setDescrizione(descrizione);
////			DettaglioOrdine condizioni = new DettaglioOrdine();
////			condizioni.setNumeroLista("1700000020170308");
////			condizioni.setSerialeRFID(codiceRFID);
////			modificati += managerDettaglioOrdini.update(daModificare, condizioni);
//			AnagraficaOggetto filtro = new AnagraficaOggetto();
//			filtro.setCodiceArticolo(codiceRFID);
//			AnagraficaOggetto daAggiornare = managerAnagrafica.getEntity(filtro, true);
//			if (daAggiornare != null) {
//				daAggiornare.setDescrizione(descrizione);
//				boolean update = managerAnagrafica.update(daAggiornare);
//				if (update)
//					modificati += 1;
//			}
//		}
//		managerDettaglioOrdini.commit();
//		System.out.println("Record modificati: " + modificati);
//	}
//
//	public void importaOrdiniManualmente() throws Exception {
//		StringUtility utility = new StringUtility("0", "0", true, true);
//		int oggettiInseriti = 0;
//		int movimentiInseriti = 0;
//		int saldiAggiornati = 0;
//		List<String> oggetti = new LinkedList<String>();
//		FileReader stream = new FileReader("C:/Users/Damiano/Desktop/SS15.csv");
//		BufferedReader reader = new BufferedReader(stream);
//		String line = reader.readLine();
//		while (line != null) {
//			oggetti.add(line);
//			line = reader.readLine();
//		}
//		reader.close();
//		System.out.println("Sarà per essere importato un ordine con " + oggetti.size() + " articoli.");
//		Ordine nuovoOrdine = new Ordine();
//		// Dati ordine
//		Date dataOrdine = new Date();
//		SimpleDateFormat formattaAnno = new SimpleDateFormat("yyyyMMdd");
//		String dataOrdineStringa = formattaAnno.format(dataOrdine);
//		String anno = dataOrdineStringa.substring(2, 4);
//		String numeroOrdine = dataOrdineStringa;
//		String nomeFile = "integrazione campionario SS15 regular.xls";
//		String noteOrdine = "Showroom TJX SS15 Regular";
//		String numeroLista = anno + utility.getFormattedString(numeroOrdine, 14, "0", false);
//		int prioritàOrdine = 1;
//		int tipoOrdine = 1;
//		// Dati destinatario
//		String idDestinatario = dataOrdineStringa;
//		String nomeDestinatario = "Showroom Acqualagna";
//		String noteDestinatario = noteOrdine;
//		String indirizzoDestinatario = "Showroom";
//		String cittàDestinatario = "Acqualagna";
//		String codiceISODestinatario = "IT";
//		String nazioneDestinatario = "IT";
//		String capDestinatario = "61041";
//		String provinciaDestinatario = "PU";
//		String telefonoDestinatario = "0721.700089";
//		String faxDestinatario = "0721.700097";
//		String emailDestinatario = "barbara@ltc-logistics.it";
//		// Dati destinatario contabile
//		String idDestinatarioContabile = "C" + idDestinatario;
//		int idDestinatarioContabileNumerico = Integer.parseInt(idDestinatario);
//		String ragioneSocialeDestinatarioContabile = "L & T.C. srl Filiale Marche";
//		String indirizzoDestinatarioContabile = "Loc. Casenuove, 2";
//		String cittàDestinatarioContabile = "Acqualagna";
//		String codiceISODestinatarioContabile = "IT";
//		String provinciaDestinatarioContabile = "PU";
//		String capDestinatarioContabile = "61041";
//		String telefonoDestinatarioContabile = "0721.700089";
//		String emailDestinarioContabile = "barbara@ltc-logistics.it";
//		// Scrivo i dati recuperati sul destinatario contabile
//		Destinatario contabile = new Destinatario();
//		contabile.setCodiceDestinatario(idDestinatarioContabile);
//		contabile.setCodiceDestinatarioNumerico(idDestinatarioContabileNumerico);
//		contabile.setTipo("CON");
//		contabile.setCap(capDestinatarioContabile);
//		contabile.setCodiceISONazione(codiceISODestinatarioContabile);
//		contabile.setEmail(emailDestinarioContabile);
//		contabile.setIndirizzo(indirizzoDestinatarioContabile);
//		contabile.setLocalità(cittàDestinatarioContabile);
//		contabile.setNazione(codiceISODestinatarioContabile);
//		contabile.setFax(telefonoDestinatarioContabile);
//		contabile.setProvincia(provinciaDestinatarioContabile);
//		contabile.setRagioneSociale1(ragioneSocialeDestinatarioContabile);
//		contabile.setRagioneSociale2(" ");
//		contabile.setCodiceContabile(" ");
//		contabile.setTelefono(telefonoDestinatarioContabile);
//		int idContabileDB = managerDestinatari.insertAndReturnID(contabile);
//		// Scrivo i dati recuperati sul destinatario della merce
//		Destinatario destinatarioMerce = new Destinatario();
//		destinatarioMerce.setCap(capDestinatario);
//		destinatarioMerce.setCodiceContabile(idDestinatarioContabile);
//		destinatarioMerce.setCodiceDestinatario(idDestinatario);
//		destinatarioMerce.setCodiceDestinatarioNumerico(idDestinatarioContabileNumerico);
//		destinatarioMerce.setCodiceISONazione(codiceISODestinatario);
//		destinatarioMerce.setEmail(emailDestinatario);
//		destinatarioMerce.setFax(faxDestinatario);
//		destinatarioMerce.setIdContabile(idContabileDB);
//		destinatarioMerce.setIndirizzo(indirizzoDestinatario);
//		destinatarioMerce.setLocalità(cittàDestinatario);
//		destinatarioMerce.setNazione(nazioneDestinatario);
//		destinatarioMerce.setNote(noteDestinatario);
//		destinatarioMerce.setProvincia(provinciaDestinatario);
//		destinatarioMerce.setRagioneSociale1(nomeDestinatario);
//		destinatarioMerce.setRagioneSociale2("");
//		destinatarioMerce.setTelefono(telefonoDestinatario);
//		destinatarioMerce.setTipo("DES");
//		int idDestinatarioDB = managerDestinatari.insertAndReturnID(destinatarioMerce);
//		// Scrivo i dati recuperati sull'ordine
//		nuovoOrdine.setCodiceCliente(idDestinatario);
//		nuovoOrdine.setCodiceCorriere("NA");
//		nuovoOrdine.setCorriere("NA");
//		nuovoOrdine.setDataConsegna(dataOrdine);
//		nuovoOrdine.setDataOrdine(dataOrdine);
//		nuovoOrdine.setIdDestinatario(idDestinatarioDB);
//		// nuovoOrdine.setNomeFile(Integer.toString(fileID));
//		nuovoOrdine.setNomeFile(nomeFile);
//		nuovoOrdine.setNote(noteOrdine);
//		nuovoOrdine.setNumeroLista(numeroLista);
//		nuovoOrdine.setNumeroListaNumerico(getProgressivoNumeroListaArrivato());
//		nuovoOrdine.setNumeroOrdine(numeroOrdine);
//		nuovoOrdine.setQuantitàTotale(oggetti.size());
//		nuovoOrdine.setRiferimentoOrdineCliente(numeroOrdine);
//		nuovoOrdine.setRaggruppamentoOrdini(numeroLista);
//		nuovoOrdine.setPriorità(prioritàOrdine);
//		nuovoOrdine.setTipoOrdine(tipoOrdine);
//		nuovoOrdine.setStato("INSE");
//		managerOrdini.insert(nuovoOrdine);
//		int rigaOrdine = 0;
//		for (String oggetto : oggetti) {
//			rigaOrdine += 1;
//			// Recupero i dati sull'oggetto
//			String[] dati = oggetto.split(";");
//			String codiceRFID = dati[3];
//			String descrizione = dati[12];
//			if (descrizione.length() > 40)
//				descrizione = descrizione.substring(0, 40);
//			String tagliaColore = dati[15];
//			//String composizioneMateriali = dati[11]; //NON USATO
//			//String sesso = dati[9]; //NON USATO
//			String descrizioneDogana = dati[12];
//			String marchio = dati[5];
//			// String categoria = dati[7]; //NON USATO
//			String colore = dati[10];
//			String taglia = dati[15];
//			String noteOggetto = "";
//			// Recupero l'anagrafica già presente e la aggiorno
//			String codiceUnivocoArticolo = "";
//			AnagraficaOggetto filtro = new AnagraficaOggetto();
//			filtro.setCodiceArticolo(codiceRFID);
//			AnagraficaOggetto daAggiornare = managerAnagrafica.getEntity(filtro);
//			if (daAggiornare != null) {
//				// Recupero l'ID univoco dell'articolo
//				codiceUnivocoArticolo = daAggiornare.getIdUnivocoArticolo();
//				// Aggiorno tutti i campi possibili
//				daAggiornare.setDescrizioneAggiuntiva(descrizioneDogana);
//				daAggiornare.setDescrizione(descrizione);
//				daAggiornare.setModello(codiceRFID);
//				daAggiornare.setMarchio(getMarchio(marchio));
//				if (taglia != null && !taglia.isEmpty()) {
//					daAggiornare.setTaglia(taglia);
//				} else {
//					int index = tagliaColore.lastIndexOf(',');
//					taglia = tagliaColore.substring(index + 1);
//					daAggiornare.setTaglia(taglia);
//				}
//				if (colore != null && !colore.isEmpty()) {
//					daAggiornare.setColore(colore);
//				} else {
//					int index = tagliaColore.lastIndexOf(',');
//					colore = tagliaColore.substring(0, index);
//					daAggiornare.setColore(colore);
//				}
//				managerAnagrafica.update(daAggiornare);
//			} else {
//				logger.error("non sono riuscito ad aggiornare l'anagrafica per il tag: " + codiceRFID);
//			}
//			// Inserisco l'oggetto
//			DettaglioOrdine daInserire = new DettaglioOrdine();
//			daInserire.setBarcode(codiceRFID);
//			daInserire.setBarcodeEAN(codiceRFID);
//			daInserire.setBarcodeUPC(codiceRFID);
//			daInserire.setCodiceCliente(idDestinatario);
//			daInserire.setColore(colore);
//			daInserire.setDataOrdine(dataOrdine);
//			daInserire.setDescrizione(descrizione);
//			daInserire.setIdDestinatario(idDestinatarioDB);
//			daInserire.setIdUnivocoArticolo(codiceUnivocoArticolo);
//			daInserire.setNote(noteOggetto);
//			daInserire.setNumeroLista(numeroLista);
//			daInserire.setNumeroOrdine(numeroOrdine);
//			// daInserire.setNumeroRigaOrdine(rigaOrdine); questo valore o
//			// l'univoco?
//			daInserire.setNumeroRigaOrdine(rigaOrdine);
//			daInserire.setRaggruppamentoStampe(numeroLista);
//			daInserire.setSerialeRFID(codiceRFID);
//			daInserire.setTaglia(taglia);
//			daInserire.setQuantitàDaImballare(0);
//			daInserire.setQuantitàDaSpedire(1);
//			daInserire.setQuantitàDaUbicare(1);
//			boolean inserimentoDettaglio = managerDettaglioOrdini.insert(daInserire);
//			if (inserimentoDettaglio)
//				oggettiInseriti += 1;
//			// Aggiorno i saldi di magazzino
//			if (codiceUnivocoArticolo != null && !codiceUnivocoArticolo.isEmpty()) {
//				SaldiMagazzino filtroSaldi = new SaldiMagazzino();
//				filtroSaldi.setIdUnivocoArticolo(codiceUnivocoArticolo);
//				SaldiMagazzino saldo = managerSaldiMagazzino.getEntity(filtroSaldi);
//				if (saldo != null) {
//					saldo.setImpegnato(1);
//					saldo.setDisponibile(0);
//					// saldo.setEsistenza(1);
//					boolean aggiornamento = managerSaldiMagazzino.update(saldo);
//					if (aggiornamento)
//						saldiAggiornati += 1;
//				} else {
//					logger.error("nessun saldo corrispondente trovato per il codice articolo: " + codiceUnivocoArticolo);
//				}
//			}
//			// Aggiorno i movimenti di magazzino
//			MovimentoMagazzino movimento = new MovimentoMagazzino();
//			movimento.setCancellato("NO");
//			movimento.setCategoriaDocumento("O");
//			movimento.setCausale("IOS");
//			movimento.setCollo("0000000000");
//			movimento.setDataMovimento(dataOrdine);
//			movimento.setDataOrdine(dataOrdine);
//			movimento.setDisponibile(0);
//			movimento.setEsistenza(1);
//			movimento.setIdUnivocoArticolo(codiceUnivocoArticolo);
//			movimento.setImpegnato(1);
//			movimento.setNoteDocumento(noteOrdine);
//			movimento.setNumeroLista(numeroLista);
//			SimpleDateFormat ora = new SimpleDateFormat("HHmm");
//			movimento.setOraMovimento(Integer.parseInt(ora.format(dataOrdine)));
//			movimento.setSegno("+");
//			movimento.setSegnoDisponibile("-");
//			movimento.setSegnoEsistenza("N");
//			movimento.setSegnoImpegnato("+");
//			movimento.setTipo("IP");
//			movimento.setTipoDocumento("ORD");
//			movimento.setTotali("NO");
//			movimento.setTrasmesso("NO");
//			movimento.setUtente("SRV");
//			boolean inserimentoMovimenti = managerMovimentoMagazzino.insert(movimento);
//			if (inserimentoMovimenti)
//				movimentiInseriti += 1;
//		}
//		commitDB();
//		System.out.println("Riepilogo inserimento. articoli: " + oggettiInseriti + ", movimenti: " + movimentiInseriti + ", saldi: " + saldiAggiornati + ", totale iniziali: " + oggetti.size());
//	}

}
