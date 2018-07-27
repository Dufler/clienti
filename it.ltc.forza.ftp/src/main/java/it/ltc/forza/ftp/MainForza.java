package it.ltc.forza.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.forza.ftp.model.LinnworksInvenctoryLine;
import it.ltc.forza.ftp.model.LinnworksOrderLine;
import it.ltc.forza.ftp.model.LinnworksOrderStatus;
import it.ltc.forza.ftp.model.ProdottoInScadenza;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.utility.csv.FileCSV;
import it.ltc.utility.ftp.FTP;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;

public class MainForza {

	private static final Logger logger = Logger.getLogger("Forza FTP data exchange");
	
	private static final String subjectRicezioneOrdini = "Ricezione Ordini Forza Industries";
	private static final String subjectAlertErroriRicezione = "Alert - Ricezione Ordini Forza Industries";
	private static final String subjectAlertScadenze = "Alert - Prodotti in scadenza Forza Industries";
	
	private static List<String> destinatari;
	private static List<String> destinatariAlert;
	private static List<String> destinatariIT;
	
	//Dinamiche in base al file settings
	public static String remoteFolder;
	public static String remoteFolderProcessed;
	public static String remoteFolderErrors;
	public static String localPath;
	public static String nomeFileOrders;
	public static String nomeFileStatus;
	public static String nomeFileInvenctory;
	public static String persistenceUnit;
	public static int giorniScandenza;
	public static int giorniScandenzaFutura;
	
	private static SimpleDateFormat sdfRenamer;
	private static SimpleDateFormat sdf;
	private static FTP ftpClient;
	private static MailMan mailMan;
	
	
	public static void main(String[] args) {
		logger.info("Avvio procedura.");
		try {
			//Setup
			setup();
			//Importazione degli ordini
			importazione();
			//Esportazione dello stato degli ordini
			esportazione();
			//Controllo delle scadenze
			checkScadenzeFuture();
			checkScadenze();
		} catch (Exception e) {
			String message = "Errore imprevisto durante la sincronizzazione dati per Forza Industries.\r\n" + e.getMessage();
			String subject = "Alert - Sincronizzazione Forza Industries";
			sendEmail(message, subject, destinatariIT);
			logger.error(message);
		}
		logger.info("Termine procedura.");
	}
	
	private static void checkScadenzeFuture() {
		//Controllo tutti i colli, i prodotti contenuti e le relative scadenze.
		ManagerScadenze managerScadenze = new ManagerScadenze(persistenceUnit);
		List<ProdottoInScadenza> scadenze = managerScadenze.getAvvisoProdottiInScadenza(giorniScandenzaFutura);
		if (scadenze != null && !scadenze.isEmpty()) {
			String messaggio = "Sono stati trovati prodotti prossimi alla scadenza: ";
			for (ProdottoInScadenza prodotto : scadenze) {
				messaggio += prodotto.getQuantità() + " X " + prodotto.getSku() + " (" + sdf.format(prodotto.getDataScadenza()) + ")\r\n";
			}
			logger.info(messaggio);
			sendEmail(messaggio, subjectAlertScadenze, destinatariIT);
		} else {
			logger.info("Nessun prodotto in scadenza.");
		}
	}
	
	private static void checkScadenze() {
		//Controllo tutti i colli, i prodotti contenuti e le relative scadenze.
		ManagerScadenze managerScadenze = new ManagerScadenze(persistenceUnit);
		List<ColliPack> scadenze = managerScadenze.getColliPackInScadenza(giorniScandenza);
		if (!scadenze.isEmpty()) {
			ManagerAssegnazione managerAssegnazione = new ManagerAssegnazione(persistenceUnit);
			TestataOrdini testata = managerAssegnazione.creaOrdineDaProdotti(scadenze);
			String messaggio = "Sono stati trovati prodotti prossimi alla scadenza ed è stato creato un apposito ordine per prelevarli.\r\nNumero di lista: " + testata.getNrLista() + "\r\nTotale dei pezzi: " + testata.getQtaTotaleSpedire();
			logger.info(messaggio);
			sendEmail(messaggio, subjectAlertScadenze, destinatariIT);
		} else {
			logger.info("Nessun prodotto in scadenza.");
		}
	}

	private static void esportazione() {
		//Invio l'aggiornamento sullo stato degli ordini.
		ManagerStatoOrdini managerStatoOrdini = new ManagerStatoOrdini(persistenceUnit);
		List<LinnworksOrderStatus> lista = managerStatoOrdini.recuperaStatoOrdini();
		//Se il contenitore ha almeno un elemento effettuerò un upload
		if (!lista.isEmpty()) {
			logger.info("Avvio la creazione del file con gli stati per aggiornare " + lista.size() + " ordini.");
			List<String> files = ftpClient.getFiles(remoteFolder);
			Date now = new Date();
			String processDate = sdfRenamer.format(now);
			String tempFilePath = localPath + processDate + "_" + nomeFileStatus;
			File fileUpdate = new File(tempFilePath);
			//Genero il contenuto del file
			try (FileWriter writer = new FileWriter(fileUpdate)) {
				StringBuffer sb = new StringBuffer();
				//Se già esiste un file chiamato "order_status.csv" lo scarico ed eseguo un append
				if (files.contains(nomeFileStatus)) {
					ftpClient.download(fileUpdate.getPath(), remoteFolder + nomeFileStatus);
					FileReader reader = new FileReader(fileUpdate);
					BufferedReader bReader = new BufferedReader(reader);
					String line = bReader.readLine();
					while (line != null) {
						sb.append(line + "\r\n");
						line = bReader.readLine();
					}
					bReader.close();
				} else {
					sb.append(LinnworksOrderStatus.COLUMN_NAMES + "\r\n");
				}
				for (LinnworksOrderStatus order : lista) {
					sb.append(order.toString());
					sb.append("\r\n");
				}
				writer.write(sb.toString());
				writer.flush();
			} catch (IOException e) {
				logger.error("Errore durante l'elaborazione del file d'aggiornamento stati.");
				logger.error(e);
				e.printStackTrace();
			}
			//Upload del file
			boolean upload = ftpClient.upload(fileUpdate.getPath(), remoteFolder + nomeFileStatus);
			if (!upload)
				logger.error("Impossibile caricare il file degli aggiornamenti.");
		}
		//Allineo il magazzino Linnworks
		ManagerMagazzino managerMagazzino = new ManagerMagazzino(persistenceUnit);
		List<LinnworksInvenctoryLine> dettagliCarichiDaInviare = managerMagazzino.recuperaGiacenzeDiMagazzino();
		if (!dettagliCarichiDaInviare.isEmpty()) {
			Date now = new Date();
			String processDate = sdfRenamer.format(now);
			String tempFilePath = localPath + processDate + "_" + nomeFileInvenctory;
			File fileUpdate = new File(tempFilePath);
			//Genero il contenuto del file
			try (FileWriter writer = new FileWriter(fileUpdate)) {
				StringBuffer sb = new StringBuffer();
				sb.append(LinnworksInvenctoryLine.COLUMN_NAMES + "\r\n");
				for (LinnworksInvenctoryLine line : dettagliCarichiDaInviare) {
					sb.append(line.toString());
					sb.append("\r\n");
				}
				writer.write(sb.toString());
				writer.flush();
			} catch (IOException e) {
				logger.error("Errore durante l'upload del file dei carichi.");
				logger.error(e);
				e.printStackTrace();
			}
			//Upload del file
			boolean upload = ftpClient.upload(fileUpdate.getPath(), remoteFolder + nomeFileInvenctory);
			if (!upload)
				logger.error("Impossibile caricare il file dei carichi.");
		}
	}

	private static void importazione() {
		ManagerImportazione managerImportazione = new ManagerImportazione(persistenceUnit);
		//Mi collego al server FTP e scarico la lista dei file .csv contenenti gli ordini
		List<String> files = ftpClient.getFiles(remoteFolder);
		for (String fileName : files) {
			if (!fileName.startsWith(nomeFileOrders))
				continue;
			//Scarico ogni file trovato
			FileCSV csv = downloadAndRead(fileName);
			//Se il file restituito è buono lo leggo e lo elaboro
			if (csv != null && !csv.getRighe().isEmpty()) {
				HashMap<String, List<LinnworksOrderLine>> orders = parseOrders(csv);
				List<String> okOrders = new LinkedList<>();
				List<String> failedOrders = new LinkedList<>();
				//Per ogni ordine trovato
				int successfullOrders = 0;
				for (String orderID : orders.keySet()) {
					//Controllo se esiste già un ordine con quel "OrderId", se è in stato "INSE" o "ERRO" ne genero uno nuovo, altrimenti passo al prossimo.
					boolean exists = managerImportazione.checkIfOrderExists(orderID);
					if (!exists) {
						List<LinnworksOrderLine> righe = orders.get(orderID);
						//Genero l'oggetto MOrdine a partire da ogni riga nel file
						MOrdine ordine = managerImportazione.generateOrder(righe);
						//elaboro il model Ordine generato e sposto il file nello storico
						boolean success = managerImportazione.validateAndSaveOrder(ordine);
						//Genero la descrizione dell'ordine per il riepilogo mail.
						String orderDescription = getOrderDescription(ordine);
						if (success) {	
							okOrders.add(orderDescription);
							successfullOrders += 1;
						} else {
							failedOrders.add(orderDescription);
							logger.error("Impossibile inserire l'ordine '" + orderID + "'");
						}
					} else {
						logger.info("Ordine '" + orderID + "' gia' presente a sistema, passo al successivo.");
						successfullOrders += 1;
					}
				}
				if (successfullOrders == orders.size()) {
					//se Ok nella cartella "processed"
					moveCSVFile(fileName, remoteFolderProcessed);
					String messaggio = "Sono arrivati " + okOrders.size() + " nuovi ordini da evadere. \r\n";
					messaggio += "Riferimento dei nuovi ordini: \r\n";
					for (String order : okOrders)
						messaggio += order + " \r\n";
					sendEmail(messaggio, subjectRicezioneOrdini, destinatari);
				} else {
					//Se ci sono errori nella cartella "errors" e mando una mail di alert.
					moveCSVFile(fileName, remoteFolderErrors);
					String messaggio = "";
					if (okOrders.size() > 0) {
						messaggio += "Sono arrivati " + okOrders.size() + " nuovi ordini da evadere.\r\n";
						messaggio += "Riferimento dei nuovi ordini: \r\n";
						for (String order : okOrders)
							messaggio += order + " \r\n";
					}
					messaggio += "Ci sono stati errori nell'importazione del file csv, controllare il file di log e/o le note dell'ordine.";
					messaggio += "Riferimento degli ordini con problemi: \r\n";
					for (String order : failedOrders)
						messaggio += order + " \r\n";
					sendEmail(messaggio, subjectAlertErroriRicezione, destinatariAlert);
					System.out.println("(Test) Ci sono stati errori, sposterei il file e manderei una mail.");
				}
			}
		}
	}
	
	private static String getOrderDescription(MOrdine ordine) {
		String orderDescription = "";
		if (ordine != null) {
			orderDescription += ordine.getRiferimentoordine();
			if (ordine.getDestinatario() != null)
				orderDescription += " per " + ordine.getDestinatario().getRagionesociale();
			String note = ordine.getNote();
			if (note != null && !note.isEmpty())
				orderDescription += ", note: '" + note + "'";
		}
		return orderDescription;
	}

	private static void sendEmail(String message, String subject, List<String> riceventi) {
		//String subject = "Ricezione Ordini Forza Industries";
		//if (alert)
		//	subject = "ALERT - " + subject;
		Email mail = new Email(subject, message);
		boolean invio = mailMan.invia(riceventi, mail);
		if (!invio) {
			logger.error("Impossibile inviare il messaggio di posta: ");
			logger.error(message);
		}
	}

	private static void moveCSVFile(String fileName, String remotefolderprocessed) {
		Date now = new Date();
		String processDate = sdfRenamer.format(now);
		String rename = remotefolderprocessed + processDate + "_" + fileName;
		boolean success = ftpClient.rename(fileName, rename);
		if (!success)
			logger.error("Impossibile rinominare o spostare il file '" + fileName + "'");
	}

	/**
	 * Restituisce una mappa di ordini e righe d'ordine parsando il file .csv passato come argomento.
	 * @param csv l'oggetto che mappa il file .csv da processare.
	 * @return una mappa le cui chiavi sono gli "Order ID" di linnworks.
	 */
	private static HashMap<String, List<LinnworksOrderLine>> parseOrders(FileCSV csv) {
		HashMap<String, List<LinnworksOrderLine>> orders = new HashMap<String, List<LinnworksOrderLine>>();
		for (String[] line : csv.getRighe()) {
			try {
				LinnworksOrderLine orderLine = new LinnworksOrderLine(csv.getMappaColonne(), line);
				String orderID = orderLine.getOrderId();
				if (!orders.containsKey(orderID)) {
					List<LinnworksOrderLine> list = new LinkedList<LinnworksOrderLine>();
					orders.put(orderID, list);
				}
				List<LinnworksOrderLine> list = orders.get(orderID);
				list.add(orderLine);
			} catch (Exception e) {
				String message = "Impossibile importare il csv: i nomi delle colonne sono stati cambiati dal cliente.";
				logger.error(message);
				logger.error(e);
				sendEmail(message, subjectAlertErroriRicezione, destinatariIT);
			}
		}
		return orders;
	}
	
	/**
	 * Scarica e legge il file .csv rappresentato dal nome passato come argomento.
	 * Le possibili eccezioni di I/O vengono loggate e tamponate.
	 * @param fileName
	 * @return
	 */
	private static FileCSV downloadAndRead(String fileName) {
		FileCSV csv;
		try {
			String remoteRelativePath = remoteFolder + fileName;
			Date now = new Date();
			String processDate = sdfRenamer.format(now);
			String localTempFile = localPath + processDate + "_" + fileName;
			boolean download = ftpClient.download(localTempFile, remoteRelativePath);
			if (!download)
				throw new RuntimeException("Impossibile scaricare il file: '" + fileName + "'");
			File file = new File(localTempFile);
			csv = FileCSV.leggiFile(file, true, "\\|", "\\|"); //csv = FileCSV.leggiFile(file, true, ",", "\".*\",");
		} catch (Exception e) {
			csv = null;
			String errorMessage = e.getMessage();
			for (StackTraceElement element : e.getStackTrace()) {
				errorMessage += element.toString();
			}
			logger.error(errorMessage);
		}
		return csv;
	}
	
	/**
	 * Setup del client FTP, del client Mail, dei controller ordini e dell'entity manager testate legacy.
	 */
	private static void setup() {
		ConfigurationUtility config = ConfigurationUtility.getInstance();
		ftpClient = config.getFTPClient();
		mailMan = config.getMailMan();
		//Destinatari semplici, ricevono tutto
		destinatari = new LinkedList<String>();
		destinatari.addAll(config.getIndirizziDestinatari());
		//Destinatari alert, ricevono solo gli alert
		destinatariAlert = new LinkedList<String>();
		destinatariAlert.addAll(destinatari);
		destinatariAlert.addAll(config.getIndirizziResponsabili());
		//Destinatari IT, ricevono solo gli errori fatali della procedura
		destinatariIT = new LinkedList<>();
		destinatariIT.addAll(config.getIndirizziResponsabili());
		remoteFolder = config.getRemoteFolder();
		remoteFolderProcessed = config.getRemoteArchiveFolder();
		remoteFolderErrors = config.getRemoteErrorFolder();
		localPath = config.getLocalFolder();
		nomeFileStatus = config.getNomeFileStatus();
		nomeFileInvenctory = config.getNomeFileInvectory();
		nomeFileOrders = config.getNomeFileOrders();
		persistenceUnit = config.getPersistenceUnit();
		giorniScandenza = config.getAvvisoGiorniScadenza();
		giorniScandenzaFutura = config.getAvvisoGiorniScadenzaFutura();
		
		sdfRenamer = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		
	}

}
