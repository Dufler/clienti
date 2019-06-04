package it.ltc.logic;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

import it.ltc.clienti.ynap.dao.DettaglioOrdineDao;
import it.ltc.clienti.ynap.dao.ImballoDao;
import it.ltc.clienti.ynap.dao.OggettiDao;
import it.ltc.clienti.ynap.dao.OrdineDao;
import it.ltc.clienti.ynap.dao.UbicazioneOggettoDao;
import it.ltc.clienti.ynap.model.DettaglioOrdine;
import it.ltc.clienti.ynap.model.Imballo;
import it.ltc.clienti.ynap.model.Oggetto;
import it.ltc.clienti.ynap.model.Ordine;
import it.ltc.clienti.ynap.model.UbicazioneOggetto;
import it.ltc.database.dao.Dao;
import it.ltc.model.order.in.Item;
import it.ltc.model.order.in.Items;
import it.ltc.model.order.in.Order;
import it.ltc.model.order.in.Orders;
import it.ltc.model.order.in.Packages;
import it.ltc.model.order.in.Shipment;
import it.ltc.utility.ftp.SFTP;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;

public class InviaEvasioneOrdine extends Dao {

	private static final Logger logger = Logger.getLogger(InviaEvasioneOrdine.class);

	public static final String ERRORE_QT_DA_IMBALLARE = "La quantità da imballare in RighiOrdine non è 1.";
	public static final String ERRORE_RIGHI_IMBALLO = "Non è stata trovata una corrispondenza esatta in dbo.RighiImballo.";
	public static final String ERRORE_RIGHI_UBICAZIONE = "Non è stata trovata una corrispondenza esatta in dbo.Righiubicpre.";
	public static final String ERRORE_COLLI_PACK = "Non è stata trovata una corrispondenza esatta in dbo.ColliPack.";
	public static final String ERRORE_RIGHI_ORDINE = "La quantità imballata è 0 ma non è stato specificato il motivo per cui non è stato imballato.";

	private final OrdineDao managerOrdini;
	private final DettaglioOrdineDao managerDettagli;
	private final ImballoDao managerImballi;
	private final UbicazioneOggettoDao managerUbicazioni;
	private final OggettiDao managerOggetti;

	private SFTP ftpClient;

	private String outgoingPath;
	private String localTempFolder;

	private DatatypeFactory factory;

	private List<Oggetto> oggettiDaAggiornare;

	private static InviaEvasioneOrdine instance;

	private InviaEvasioneOrdine() {
		super("legacy-ynap");
		setupConfiguration();

		ConfigurationUtility configuration = ConfigurationUtility.getInstance();
		outgoingPath = configuration.getPathFTPOut();
		localTempFolder = configuration.getPathOrdiniOut();
		ftpClient = configuration.getSFTPClient();
		
		oggettiDaAggiornare = new ArrayList<Oggetto>();
		
		managerOrdini = new OrdineDao();
		managerDettagli = new DettaglioOrdineDao();
		managerImballi = new ImballoDao();
		managerUbicazioni = new UbicazioneOggettoDao();
		managerOggetti = new OggettiDao();
	}

	private void setupConfiguration() {
		try {
			factory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static InviaEvasioneOrdine getInstance() {
		if (instance == null)
			instance = new InviaEvasioneOrdine();
		return instance;
	}

	private XMLGregorianCalendar convertiData(Date data) {
		if (data == null)
			data = new Date();
		GregorianCalendar dataCalendario = new GregorianCalendar();
		dataCalendario.setTime(data);
		XMLGregorianCalendar dataXML = factory.newXMLGregorianCalendarDate(dataCalendario.get(Calendar.YEAR),
				dataCalendario.get(Calendar.MONTH) + 1, dataCalendario.get(Calendar.DAY_OF_MONTH),
				DatatypeConstants.FIELD_UNDEFINED);
		return dataXML;
	}

	public void inviaEvasioneOrdini() {
		logger.info("Avvio la procedura di invio della conferma degli ordini imballati.");
		List<Ordine> listaOrdini = managerOrdini.trovaOrdiniDaStato("ELAB");
		logger.info("Stanno per essere elaborati " + listaOrdini.size() + " ordini");
		try {
			for (Ordine ordine : listaOrdini) {
				logger.info("Processo l'ordine: " + ordine.getNumeroOrdine());
				String numeroLista = ordine.getNumeroLista();
				// Oggetto base xml
				Orders radiceXML = new Orders();
				Order ordineXML = new Order();
				radiceXML.setOrder(ordineXML);
				Date dateOrdine = ordine.getDataOrdine();
				ordineXML.setORDDate(convertiData(dateOrdine));
				// TODO - gestire il valore per l'ordine cancellato
				// 0 = not deleted; 1 = deleted by CCare; 2 = deleted for
				// invalid address.
				ordineXML.setORDDeleted(0);
				ordineXML.setORDNote(ordine.getNote());
				ordineXML.setORDNum(ordine.getNumeroOrdine());
				ordineXML.setORDRef(ordine.getPriorità());
				ordineXML.setORDType(ordine.getTipoOrdine());
				// ordineXML.setORDGW(null);
				Shipment spedizione = new Shipment();
				ordineXML.setShipment(spedizione);
				XMLGregorianCalendar dataSpedizione = convertiData(ordine.getDataSpedizione());
				spedizione.setSHIPDate(dataSpedizione);
				HashMap<String, it.ltc.model.order.in.Package> pacchi = new HashMap<String, it.ltc.model.order.in.Package>();
				Items oggetti = new Items();
				ordineXML.setItems(oggetti);
				List<Item> listaOggettiSpediti = oggetti.getItem();
				List<DettaglioOrdine> dettagli = managerDettagli.trovaDaNumeroLista(numeroLista);
				for (DettaglioOrdine dettaglio : dettagli) {
					// Eseguo le verifiche se posso mandare questo elemento nel
					// riscontro
					String codiceRFID = dettaglio.getSerialeRFID();
					String idUnivocoArticolo = dettaglio.getIdUnivocoArticolo();
					int rigaOrdine = dettaglio.getNumeroRigaOrdine();
					String riferimento = "Lista: " + numeroLista + ", seriale: " + codiceRFID + ". ";
					Item oggettoSpedito = new Item();
					// La quantità da imballare deve essere 1 altrimenti
					// significa che non ho trovato l'oggetto.
					if (dettaglio.getQuantitàDaImballare() == 1) {
						// Se non trovo esattamente un elemento corrispondente
						// in RighiImballo sollevo un'eccezione.
						String idColloImballato = "";
						Imballo collo = managerImballi.trovaPezzoImballato(numeroLista, codiceRFID);
						if (collo == null) {
							logger.error("l'ordine: " + ordine.getNumeroOrdine() + " ha un errore in righi imballo per il tag " + dettaglio.getSerialeRFID());
							throw new RuntimeException(riferimento + ERRORE_RIGHI_IMBALLO);
						} else {
							idColloImballato = collo.getIdCollo();
							it.ltc.model.order.in.Package pacco = new it.ltc.model.order.in.Package();
							pacco.setPACKID(idColloImballato);
							BigDecimal peso = BigDecimal.ZERO;
							pacco.setPACKWeight(peso);
							pacchi.put(idColloImballato, pacco);
						}
						// Se non trovo esattamente un elemento corrispondente
						// in Righiubcpre sollevo un'eccezione.
						UbicazioneOggetto ubicpre = managerUbicazioni.trovaPezzoImballato(numeroLista, idUnivocoArticolo);
						if (ubicpre == null) {
							logger.error("l'ordine: " + ordine.getNumeroOrdine() + " ha un errore in righi ubci pre per il tag " + dettaglio.getSerialeRFID());
							throw new RuntimeException(riferimento + ERRORE_RIGHI_UBICAZIONE);
						}
						// Se non trovo esattamente una corrispondenza in ColliPack sollevo un'eccezione.
						List<Oggetto> listaOggetti = managerOggetti.trovaDaStatoESeriale("IMBALLATO", codiceRFID);
						if (listaOggetti.size() != 1) {
							logger.error("l'ordine: " + ordine.getNumeroOrdine() + " ha un errore in colli pack per il tag " + dettaglio.getSerialeRFID());
							throw new RuntimeException(riferimento + ERRORE_COLLI_PACK);
						} else {
							oggettiDaAggiornare.addAll(listaOggetti);
						}
						oggettoSpedito.setITCodiceMatricola(codiceRFID);
						oggettoSpedito.setITConfDate(dataSpedizione);
						oggettoSpedito.setITPackID(idColloImballato);
						oggettoSpedito.setITRMMID(rigaOrdine);
					} else {
						int codificaOggettoNonTrovato = dettaglio.getCodificaOggettoNonTrovato();
						if (codificaOggettoNonTrovato == 0) {
							codificaOggettoNonTrovato = 1;
							// TODO - Inserito un valore di default (non
							// trovato) perchè sul gestionale non sono pronti.
							logger.error("l'ordine: " + ordine.getNumeroOrdine() + " ha un errore in righi ordine per il tag " + dettaglio.getSerialeRFID());
							// throw new RuntimeException(riferimento +
							// ERRORE_RIGHI_ORDINE);
						}
						oggettoSpedito.setITRMMID(rigaOrdine);
						oggettoSpedito.setITCodiceMatricola(codiceRFID);
						oggettoSpedito.setITNotShippedNote(dettaglio.getNoteOggettoNonTrovato());
						oggettoSpedito.setITNotShippedType(Integer.toString(codificaOggettoNonTrovato));
					}
					listaOggettiSpediti.add(oggettoSpedito);
				}
				// Aggiungo tutti i colli trovati
				Packages contenitorePacchi = new Packages();
				spedizione.setPackages(contenitorePacchi);
				List<it.ltc.model.order.in.Package> listaPacchi = contenitorePacchi.getPackage();
				for (String key : pacchi.keySet()) {
					it.ltc.model.order.in.Package pacco = pacchi.get(key);
					listaPacchi.add(pacco);
				}
				// Genero l'xml e lo metto sul server SFTP YNAP
				boolean successo = generaXML(radiceXML);
				// Se tutto è andato a buon fine eseguo gli update richiesti
				if (successo) {
					EntityManager em = getManager();
					EntityTransaction transaction = em.getTransaction();
					try {
						transaction.begin();
						// testata ordini stato ELAB -> SPED
						Ordine ordineUpdate = em.find(Ordine.class, ordine.getId());
						ordineUpdate.setStato("SPED");
						em.merge(ordineUpdate);
						// colli pack stato IMBALLATO -> TRASMESSO
						for (Oggetto oggetto : oggettiDaAggiornare) {
							Oggetto oggettoUpdate = em.find(Oggetto.class, oggetto.getId());
							oggettoUpdate.setStato("TRASMESSO");
							em.merge(oggettoUpdate);
						}
						transaction.commit();
					} catch (Exception e) {
						successo = false;
						logger.error(e.getMessage(), e);
						if (transaction != null && transaction.isActive())
							transaction.rollback();
					} finally {
						em.close();
					}
				}
			}
		} catch (RuntimeException e) {
			// Notifico l'eccezione.
			logger.error(e.getMessage(), e);
			String oggettoMail = "Alert: eccezione nell'invio dell'evasione dell'ordine YNAP";
			String corpoMail = "Errore riscontrato: " + e.getMessage();
			Set<String> destinatariDaAvvisare = ConfigurationUtility.getInstance().getIndirizziDestinatariErrori();
			Email mail = new Email(oggettoMail, corpoMail);
			MailMan postino = ConfigurationUtility.getInstance().getMailMan();
			boolean invio = postino.invia(destinatariDaAvvisare, mail);
			if (!invio)
				logger.error("Impossibile inviare la mail di notifica dell'errore.");
		}
	}

	private boolean generaXML(Orders radiceXML) {
		boolean successo = false;
		try {
			Thread.sleep(1000); //Inserito per far si che anche se vengono processate liste molto piccole e il tempo impiegato sia minore di un secondo i file in uscita abbiano sempre nomi diversi.
			Date oggi = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String dataFormattata = formatter.format(oggi);
			File xml = new File(localTempFolder + "xy" + dataFormattata + ".orders.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Orders.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "/resources/schWhOrdersInbound.xsd");
			jaxbMarshaller.marshal(radiceXML, xml);
			successo = ftpClient.upload(xml.getAbsolutePath(), outgoingPath);
		} catch (JAXBException e) {
			e.printStackTrace();
			logger.error("Errore nella generazione dell'XML.", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("Errore nella generazione dell'XML.", e);
		}
		return successo;
	}

}