package it.ltc.logic;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import it.ltc.clienti.ynap.dao.PackingListDao;
import it.ltc.clienti.ynap.dao.PackingListDettagliDao;
import it.ltc.clienti.ynap.model.PackingList;
import it.ltc.clienti.ynap.model.PackingListDettaglio;
import it.ltc.database.dao.Dao;
import it.ltc.model.item.in.From;
import it.ltc.model.item.in.Item;
import it.ltc.model.item.in.Items;
import it.ltc.model.item.in.Mov;
import it.ltc.model.item.in.Movs;
import it.ltc.model.item.in.To;
import it.ltc.utility.configuration.Configuration;
import it.ltc.utility.ftp.SFTP;

public class InviaRiscontro extends Dao {

	private static final Logger logger = Logger.getLogger(InviaRiscontro.class);

	private final PackingListDao managerPackingList;
	private final PackingListDettagliDao managerDettagli;

	private Configuration configuration;
	private SFTP ftpClient;

	private String host;
	private String username;
	private String password;
	private String outgoingPath;
	private String localTempFolder;

	private DatatypeFactory factory;

	private static InviaRiscontro instance;

	private InviaRiscontro() {
		super("legacy-ynap");
		setupConfiguration();
		setupFTP();
		
		managerPackingList = new PackingListDao();
		managerDettagli = new PackingListDettagliDao();
	}

	public static InviaRiscontro getInstance() {
		if (instance == null)
			instance = new InviaRiscontro();
		return instance;
	}

	private void setupConfiguration() {
		try {
			factory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	private void setupFTP() {
		try {
			configuration = new Configuration("/resources/configuration.properties", false);
			host = configuration.get("sftp_host");
			username = configuration.get("sftp_username");
			password = configuration.get("sftp_password");
			outgoingPath = configuration.get("outgoing_path");
			localTempFolder = configuration.get("app_carichi_out_path");
			ftpClient = new SFTP(host, username, password);
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	public void inviaRiscontroNuovo() {
		logger.info("Avvio della procedura di riscontro del carico.");
		List<PackingList> documenti = managerPackingList.trovaCarichiGenerati();
		logger.info("Sono stati trovati " + documenti.size() + " documenti da inviare.");
		for (PackingList documento : documenti) {
			List<PackingListDettaglio> articoliLetti = managerDettagli.trovaDaCarico(documento.getId());
			// Raggruppo gli articoli da riscontrare per MOV_ID
			HashMap<Integer, List<PackingListDettaglio>> listaDocumenti = new HashMap<Integer, List<PackingListDettaglio>>();
			for (PackingListDettaglio articolo : articoliLetti) {
				Integer movID = articolo.getIdMovimento();
				if (listaDocumenti.containsKey(movID)) {
					List<PackingListDettaglio> listaDettagli = listaDocumenti.get(movID);
					listaDettagli.add(articolo);
				} else {
					List<PackingListDettaglio> listaDettagli = new ArrayList<PackingListDettaglio>();
					listaDettagli.add(articolo);
					listaDocumenti.put(movID, listaDettagli);
				}
			}
			// Per ogni MOV_ID genero un file xml
			Movs articoliRiscontrati = new Movs();
			List<Mov> listaMovimenti = articoliRiscontrati.getMov();
			Set<Integer> listaMovID = listaDocumenti.keySet();
			Iterator<Integer> iteratore = listaMovID.iterator();
			while (iteratore.hasNext()) {
				Integer movID = iteratore.next();
				List<PackingListDettaglio> listaOggetti = listaDocumenti.get(movID);
				int idMovimentoInt = movID;
				Mov articoli = new Mov();
				articoli.setMOVID(idMovimentoInt);
				// Moviment Date
				GregorianCalendar oggi = new GregorianCalendar();
				XMLGregorianCalendar data = articoli.getMOVDate();
				data = factory.newXMLGregorianCalendarDate(oggi.get(Calendar.YEAR), oggi.get(Calendar.MONTH) + 1, oggi.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
				articoli.setMOVDate(data);
				// Confirmation Date
				GregorianCalendar oggiConf = new GregorianCalendar();
				XMLGregorianCalendar dataConf = articoli.getMOVConfDate();
				dataConf = factory.newXMLGregorianCalendarDate(oggiConf.get(Calendar.YEAR),	oggiConf.get(Calendar.MONTH) + 1, oggiConf.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
				articoli.setMOVConfDate(dataConf);
				boolean FromToSet = false;
				listaMovimenti.add(articoli);
				Items oggetti = new Items();
				articoli.setItems(oggetti);
				List<Item> listaArticoli = oggetti.getItem();
				for (PackingListDettaglio articolo : listaOggetti) {
					// Da fare solo una volta, è uguale per tutti.
					if (!FromToSet) {
						From from = new From();
						from.setFromID(articolo.getCodiceDA());
						articoli.setFrom(from);
						To to = new To();
						to.setToID(articolo.getCodiceA());
						articoli.setTo(to);
					}
					Item articoloRiscontrato = new Item();
					// Inserisci le proprietà dell'articolo
					articoloRiscontrato.setITRMMID(articolo.getIdRigaOrdine());
					articoloRiscontrato.setITCode(articolo.getCodiceRFID());
					// Se la quantità trovata è maggiore di 0 inserisco la data,
					// altrimenti no e significa che non l'ho trovato nel carico
					// anche se mi aspettavo di trovarlo.
					if (articolo.getQuantitàVerificata() > 0)
						articoloRiscontrato.setITConfDate(dataConf);
					listaArticoli.add(articoloRiscontrato);
				}
			}
			// Scrivo l'xml
			boolean successo = elaboraXML(articoliRiscontrati);
			if (successo) {
				logger.info("Il documento " + documento.getFileID() + " è stato inviato con successo!");
				boolean update = aggiornaStatoCarico(documento, articoliLetti);
				if (!update)
					logger.error("Impossibile aggiornare lo stato di trasmissione del carico ID " + documento.getId());
			} else {
				logger.info("Attenzione! Il documento " + documento.getFileID() + " ha generato errori.");
			}
		}
	}
	
	private boolean aggiornaStatoCarico(PackingList documento, List<PackingListDettaglio> dettagli) {
		boolean update;
		EntityManager em = getManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			//Aggiornamento testata
			PackingList entity = em.find(PackingList.class, documento.getId());
			entity.setStato("T");
			em.merge(entity);
			//Aggiornamento righe
			for (PackingListDettaglio dettaglio : dettagli) {
				PackingListDettaglio e = em.find(PackingListDettaglio.class, dettaglio.getId());
				e.setStato("TRASMESSO");
				em.merge(e);
			}
			transaction.commit();
			update = true;
		} catch (Exception e) {
			update = false;
			logger.error(e.getMessage(), e);
			if (transaction != null && transaction.isActive())
				transaction.rollback();
		} finally {
			em.close();
		}		
		return update;
	}

	private boolean elaboraXML(Movs articoliRiscontrati) {
		boolean successo = false;
		try {
			Date oggi = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String dataFormattata = formatter.format(oggi);
			File file = new File(localTempFolder + "xy" + dataFormattata + ".items.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Movs.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "/resources/schWhItemsInbound.xsd");
			jaxbMarshaller.marshal(articoliRiscontrati, file);
			successo = ftpClient.upload(file.getAbsolutePath(), outgoingPath);
		} catch (JAXBException e) {
			logger.error(e);
			e.printStackTrace();
			// TODO - manda una mail a support
		}
		return successo;
	}

}