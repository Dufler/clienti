package it.ltc.logic;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import it.ltc.clienti.ynap.dao.PackingListDao;
import it.ltc.clienti.ynap.dao.PackingListDettagliDao;
import it.ltc.clienti.ynap.model.AnagraficaOggetto;
import it.ltc.clienti.ynap.model.BarcodeOggetto;
import it.ltc.clienti.ynap.model.PackingList;
import it.ltc.clienti.ynap.model.PackingListDettaglio;
import it.ltc.database.dao.Dao;
import it.ltc.model.item.out.Item;
import it.ltc.model.item.out.Mov;
import it.ltc.model.item.out.Movs;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;

public class ImportaListaCarico extends Dao {
	
	private static final Logger logger = Logger.getLogger(ImportaListaCarico.class);
	
	private static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
	
	private static final String PRIMA_SCELTA = "Prima_Scelta";
	private static final String SECONDA_SCELTA = "Seconda_Scelta";
	private static final String ALTRA_SCELTA = "???";

	private static ImportaListaCarico instance;

	private Date now;
	private boolean logDettagliato;

	private final SimpleDateFormat formatter;
	
	private final PackingListDao managerPackingList;
	private final PackingListDettagliDao managerDettagli;
//	private final AnagraficaOggettoDao managerAnagrafica;
	
//	private final HashMap<String, AnagraficaOggetto> mappaIDUnivociSeriali;

	public static ImportaListaCarico getInstance() {
		if (instance == null)
			instance = new ImportaListaCarico();
		return instance;
	}

	private ImportaListaCarico() {
		super("legacy-ynap");
		formatter = new SimpleDateFormat("yyyyMMdd");
		
		managerPackingList = new PackingListDao();
		managerDettagli = new PackingListDettagliDao();
//		managerAnagrafica = new AnagraficaOggettoDao();
		
//		mappaIDUnivociSeriali = new HashMap<>();
	}

	public void elaboraXMLRicevuti() {
		logger.info("Avvio la procedura di importazione degli XML del carico in arrivo.");
		now = new Date();
		List<String> fileNames = getFileNamesToImport();
		int fileImportati = 0;
		for (String fileName : fileNames) {
			try {
				// Per ognuno degli xml recuperati ne leggo il contenuto
				logger.info("Procedo con: " + fileName);
				File file = new File(fileName);
				JAXBContext jaxbContext = JAXBContext.newInstance(Movs.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Movs oggettiInArrivo = (Movs) jaxbUnmarshaller.unmarshal(file);
				// Log del contenuto del file
				stampa(oggettiInArrivo);
				// Scrittura del file
				Mov listaOggettiInArrivo = oggettiInArrivo.getMov().get(0);
				boolean successo = scriviDB(listaOggettiInArrivo);
				if (successo) {
					fileImportati += 1;
					aggiornaFileSentinella(fileName);
					logger.info("File " + fileName + " importato correttamente.");
				} else {
					logger.error("File " + fileName + " non è stato importato.");
				}
			} catch (JAXBException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		int fileConErrori = fileNames.size() - fileImportati;
		if (fileImportati > 0 || fileConErrori > 0) {
			String messaggioDiRiepilogo = "Sono stati importati correttemente " + fileImportati + " files.";
			if (fileConErrori > 0)
				messaggioDiRiepilogo += " " + fileConErrori
						+ " non sono stati importati a causa di errori, controllare il file di log.";
			logger.info(messaggioDiRiepilogo);
			inviaMessaggioDiRiepilogo(messaggioDiRiepilogo);
		}
	}

	private void inviaMessaggioDiRiepilogo(String messaggio) {
		String oggettoMail = "Riepilogo importazione del carico YNAP";
		Email mail = new Email(oggettoMail, messaggio);
		List<String> destinatariDaAvvisare = ConfigurationUtility.getInstance().getIndirizziDestinatari();
		MailMan postino = ConfigurationUtility.getInstance().getMailMan();
		boolean invio = postino.invia(destinatariDaAvvisare, mail);
		if (invio)
			logger.info("mail di riepilogo inviata con successo.");
		else
			logger.error("non è stato possibile inviare la mail di riepilogo");
	}
	
	//Questo metodo è stato usato per fare test in locale senza SFTP.
//	private List<String> getFileNamesToImport() {
//		String folderPath = "C:\\Users\\Damiano\\Desktop\\ynap\\corretti\\";
//		File folder = new File(folderPath);
//		String[] array = folder.list();
//		List<String> list = new ArrayList<>();
//		for (String file : array) {
//			list.add(folderPath + file);
//		}
//		return list;
//	}

	private List<String> getFileNamesToImport() {
		// TODO - Inoltre verifico che non abbia già letto questo file.
		logger.info("Recupero i files da importare");
		GestoreSFTP gestore = GestoreSFTP.getInstance(GestoreSFTP.STRATEGY_LISTA_DI_CARICO);
		List<String> localCopyNames = gestore.getNomiFiles();
		return localCopyNames;
	}

	private void aggiornaFileSentinella(String fileDaAggiornare) {
		logger.info("Aggiorno il relativo file sentinella.");
		GestoreSFTP gestore = GestoreSFTP.getInstance(GestoreSFTP.STRATEGY_LISTA_DI_CARICO);
		gestore.aggiornaFileSentinella(fileDaAggiornare);
	}

	private boolean scriviDB(Mov movimento) {
		logger.info("Scrivo su DB il movimento: " + movimento.getMOVID());
		boolean successo;
		// Controllo se ho già inserito questo packing list:
		// se ce l'ho già restituisco l'id e lo uso per inserire i dettagli
		// se non ce l'ho ancora lo inserisco nel DB e uso il nuovo id per i
		// dettagli
		PackingList packingList = elaboraPackingList(movimento);
		// Elaboro il resto delle informazioni
//		Date dataMovimento = movimento.getMOVDate().toGregorianCalendar().getTime();
		// Se il flusso proviene dalla fonte 138 elimino i primi 2 caratteri.
//		int tipoMovimento = movimento.getMOVType();
//		String idCollo = movimento.getMOVUDC();
//		if (tipoMovimento == 138 && idCollo != null) {
//			idCollo = idCollo.substring(2);
//		}
//		int idMovimento = movimento.getMOVID();
//		int idTrasportatore = movimento.getMOVCarrierID();
//		int idUtenteYNAP = movimento.getMOVUser();
//		String note = movimento.getMOVNote1();
//		int codiceDA = movimento.getFrom().getFromID();
//		int codiceA = movimento.getTo().getToID();
		List<Item> oggetti = movimento.getItems().getItem();
		//Edit 30-01-2018: YNAP ha introdotto un bug per cui gli stessi seriali arrivano più volte su file diversi.
		//prima di procedere all'importazione elimino tutti i seriali che sono già presenti in pakiarticolo
		List<Item> oggettiDaRimuovere = new LinkedList<>();
		for (Item item : oggetti) {
			PackingListDettaglio match = managerDettagli.trovaDaSerialeEStato(item.getITCode(), "INSERITO");
			if (match != null) {
				oggettiDaRimuovere.add(item);
			}
		}
		//Elimino quelli non validi
		for (Item item : oggettiDaRimuovere) {
			oggetti.remove(item);
		}
		//Procedo alla scrittura nel DB
		EntityManager em = getManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			//Inserisco il packing list, se necessario
			if (packingList.getId() == 0) {
				em.persist(packingList);
			}
			//Inserisco le righe
			for (Item item : oggetti) {
				// Anagrafica
				String idUnivocoArticolo = eleboraAnagrafica(em, item);
				// Oggetto
				PackingListDettaglio articolo = getDettaglio(movimento, item, idUnivocoArticolo);
				articolo.setIdPackingList(packingList.getId());
				em.persist(articolo);
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
	
	private PackingListDettaglio getDettaglio(Mov movimento, Item item, String idUnivocoArticolo) {
		String idCollo = movimento.getMOVUDC();
		if (movimento.getMOVType() == 138 && idCollo != null) {
			idCollo = idCollo.substring(2);
		}
		PackingListDettaglio articolo = new PackingListDettaglio();
		//articolo.setIdPackingList(idPackingList);
		articolo.setCodiceUnivocoArticolo(idUnivocoArticolo);
		articolo.setDataMovimento(movimento.getMOVDate().toGregorianCalendar().getTime());
		articolo.setIdCollo(idCollo);
		articolo.setIdMovimento(movimento.getMOVID());
		articolo.setIdTrasportatore(movimento.getMOVCarrierID());
		articolo.setIdUtenteYNAP(movimento.getMOVUser());
		articolo.setNote(movimento.getMOVNote1());
		articolo.setTipoMovimento(movimento.getMOVType());
		articolo.setCodiceDA(movimento.getFrom().getFromID());
		articolo.setCodiceA(movimento.getTo().getToID());
		articolo.setCodiceRFID(item.getITCode());
		articolo.setIdRigaOrdine(item.getITRMMID());
		articolo.setStato("INSERITO");
		articolo.setQuantitàVerificata(0);
		articolo.setDescrizione(item.getITDescr());
		return articolo;
	}

	/**
	 * Fix rispetto alla documentazione, non ci passano l'ID del movimento
	 * corretto. Originariamente potevo utilizzare come ID del movimento il
	 * campo MOVID. Ora i movimenti vengono raggruppati sulla data di
	 * importazione.
	 * 
	 * @param movimento
	 * @return l'ID del pakiTesta
	 */
	private PackingList elaboraPackingList(Mov movimento) {
		//Le condizioni necessarie a capire se accorpare un movimento sono la data d'importazione e la qualita'
		String tipologiaQualitaMovimento = getTipoQualitaMovimento(movimento);
		String idMovimento = formatter.format(now);
		//Vedo se esiste un packing list già presente a sistema che soddisfi queste condizioni
		String riferimento = idMovimento + "-" + tipologiaQualitaMovimento;
		PackingList listaOggetti = managerPackingList.trovaDaRiferimentoEQualita(riferimento, tipologiaQualitaMovimento);
		//Se lo trovo restituisco quello, i movimenti saranno accorpati li altrimenti ne genero uno.
		if (listaOggetti == null) {
			listaOggetti = new PackingList();
			Date dataMovimento = movimento.getMOVDate().toGregorianCalendar().getTime();
			listaOggetti.setDataCreazione(dataMovimento);
			listaOggetti.setGenerato("NO");
			listaOggetti.setStato("N");
		}
		return listaOggetti;
	}

	/**
	 * Restituisce la tipologia di qualita' del movimento, i possibili valori sono:
	 * - Prima scelta
	 * - Seconda scelta
	 * - ??? (caso che non dovrebbe verificarsi mai in teoria)
	 * Vengono controllati tutti gli oggetti all'interno del movimento, la loro qualita' viene discriminata in base al
	 * valore del campo from_id e to_id che identificano i magazzini logici YNAP, divisi per qualita'. 
	 * @param movimento
	 * @return
	 */
	private String getTipoQualitaMovimento(Mov movimento) {
		String qualita;
		int to = movimento.getTo().getToID();
		switch (to) {
			case 8089 : qualita = PRIMA_SCELTA; break;
			case 8090 : qualita = PRIMA_SCELTA; break;
			case 9480 : qualita = SECONDA_SCELTA; break;
			case 9481 : qualita = SECONDA_SCELTA; break;
			default : qualita = ALTRA_SCELTA;
		}
		return qualita;
	}

	private String eleboraAnagrafica(EntityManager em, Item item) {
		// Far restituire l'ID univoco articolo dell'oggetto, se già presente.
		String codiceRFID = item.getITCode();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AnagraficaOggetto> criteria = cb.createQuery(AnagraficaOggetto.class);
        Root<AnagraficaOggetto> member = criteria.from(AnagraficaOggetto.class);
        criteria.select(member).where(cb.equal(member.get("CodArtStr"), codiceRFID));
        List<AnagraficaOggetto> lista = em.createQuery(criteria).setMaxResults(1).getResultList();
		AnagraficaOggetto anagrafica = lista.isEmpty()? null : lista.get(0);
		if (anagrafica == null) {
			// Anagrafica non ancora presente per il dato seriale, la inserisco
			anagrafica = new AnagraficaOggetto();
			anagrafica.setIdUnivocoArticolo(getIDUnivoco());
			anagrafica.setDescrizione(item.getITDescr());
			anagrafica.setTipologiaMerce(item.getITAS());
			anagrafica.setStagione(item.getITSeason());
			anagrafica.setCodiceArticolo(codiceRFID);
			anagrafica.setTaglia("UNI");
			em.persist(anagrafica);
			// Inserisco anche il barcode
			BarcodeOggetto barcode = new BarcodeOggetto();
			barcode.setIdUnivocoArticolo(anagrafica.getIdUnivocoArticolo());
			barcode.setBarcodeEAN(codiceRFID);
			barcode.setBarcodeUPC(codiceRFID);
			barcode.setCodiceArticolo(codiceRFID);
			em.persist(barcode);
		}
		return anagrafica.getIdUnivocoArticolo();
	}
	
	protected static synchronized String getIDUnivoco() {
		LocalDateTime now = LocalDateTime.now();
		String chiave = now.format(sdf);
		//Mi fermo cinque millisecondi per evitare ID duplicati.
		try { Thread.sleep(5); } catch (InterruptedException e) { logger.warn("Impossibile mandare in sleep il thread, possibile ID univoco articolo duplicato.");}
		return chiave;
	}

	private void stampa(Movs movs) {
		logger.info("File YNAP n°: " + movs.getFileID());
		for (Mov m : movs.getMov()) {
			logger.info("Lista di carico n°: " + m.getMOVID());
			logger.info("Data di arrivo: " + m.getMOVDate());
			logger.info("Lista degli oggetti: ");
			if (logDettagliato) {
				for (Item i : m.getItems().getItem()) {
					logger.info("RFID: " + i.getITCode());
					logger.info("Descrizione: " + i.getITDescr());
				}
			}
		}
	}

}