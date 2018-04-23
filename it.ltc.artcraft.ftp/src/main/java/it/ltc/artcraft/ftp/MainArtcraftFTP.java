package it.ltc.artcraft.ftp;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.utility.ftp.FTP;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;

/**
 * Classe monolitica di main che esegue il download e l'upload dei files sul sito FTP di Artcraft.
 * @author Damiano
 *
 */
public class MainArtcraftFTP {
	
	private static final Logger logger = Logger.getLogger("Artcraft FTP");
	
	private static ConfigurationUtility configurator;
	
	private static FTP client;
	private static MailMan mailer;
	private static List<String> regolari;
	private static List<String> supervisori;
	
	private static boolean verbose;
	
	private static boolean erroriInDownload;
	private static boolean erroriInUpload;
	private static boolean erroriPDF;
	
	private static int pdf;
	private static int carichi;
	private static int articoli;
	private static int classi;
	private static int marchi;
	private static int taglie;
	private static int bolle;
	private static int corrieri;
	private static int nonRiconosciuto;
	
	private static int tr07;
	private static int tr08;
	
	public static void main(String[] args) {
		setup(args);
		logVerbose("Avvio procedura.");
		downloadFiles();
		downloadPDF();
		uploadFiles();
		riepilogo();
		logVerbose("Termine procedura.");
	}
	
	/**
	 * Stampa un messaggio di log solo se è impostato come verboso.
	 * @param message
	 */
	private static void logVerbose(String message) {
		if (verbose) {
			System.out.println(message);
			logger.info(message);
		}
	}
	
	/**
	 * Istanzia il configuratore, il client FTP, il client di posta e la verbosita'
	 * @param args
	 */
	private static void setup(String[] args) {
		configurator = ConfigurationUtility.getInstance();
		client = configurator.getFTPClient();
		mailer = configurator.getMailMan();
		regolari = configurator.getIndirizziDestinatari();
		supervisori = configurator.getIndirizziResponsabili();
		carichi = 0;
		articoli = 0;
		classi = 0;
		marchi = 0;
		taglie = 0;
		bolle = 0;
		corrieri = 0;
		nonRiconosciuto = 0;
		pdf = 0;
		erroriInDownload = false;
		erroriInUpload = false;
		erroriPDF = false;
		//Default: non loggare tutto a meno che non venga esplicitamente richiesto passando come argument "v" o "verbose".
		verbose = configurator.isVerbose();
		//Esamino tutti gli argomenti passati per trovare funzioni aggiuntive da espletare.
		if (args.length > 0)
			logger.info("Argument richiesti:");
		for (String argument : args) {
			logger.info("Argument: " + argument);
			if (argument.equals("verbose") || argument.equals("v") || argument.equals("-verbose") || argument.equals("-v"))
				verbose = true;
		}
	}
	
	/**
	 * Scarica i PDF
	 */
	private static void downloadPDF() {
		logVerbose("Avvio la fase di download dei PDF.");
		try {
			String remotePDFFolder = configurator.getRemotePDFFolder();
			String localFolderPDF = configurator.getLocalFolderPDF();
			String remotePDFStoricFolder = configurator.getRemotePDFStoricFolder();
			List<String> files = client.getFiles(remotePDFFolder);
			for (String file : files) {
				String tipoFileDaNome = file.toUpperCase();
				if (tipoFileDaNome.endsWith("PDF")) {
					boolean success = downloadAndMove(file, localFolderPDF, remotePDFFolder, remotePDFStoricFolder);
					if (success)
						pdf += 1;
				}
			}
		} catch (Exception e) {
			String message = "Errori durante la copia dei PDF: " + e;
			System.out.println(message);
			logger.error(message);
			erroriPDF = true;
		}
	}
 	
	/**
	 * Scarica i file per lo scambio dati.
	 */
	private static void downloadFiles() {
		logVerbose("Avvio la fase di download dei files.");
		try {
			//Cartella IN
			String remoteFolder = configurator.getRemoteFolder();
			String localFolderIN = configurator.getLocalFolderIN();
			String remoteStoricFolder = configurator.getRemoteStoricFolder();
			List<String> files = client.getFiles(remoteFolder);
			logVerbose("Trovati " + files.size() + " file da scaricare  nella cartella '" + remoteFolder + "'");
			for (String file : files) {
				boolean success = downloadAndMove(file, localFolderIN, remoteFolder, remoteStoricFolder);
				if (success) {
					String tipoFileDaNome = file.toUpperCase();
					if (tipoFileDaNome.startsWith("ARTICOLI")) {
						articoli += 1;
					} else if (tipoFileDaNome.startsWith("CLASS")) {
						classi += 1;
					} else if (tipoFileDaNome.startsWith("MARCHI")) {
						classi += 1;
					} else if (tipoFileDaNome.startsWith("TAGLIE")) {
						taglie += 1;
					} else if (tipoFileDaNome.startsWith("BOLLE")) {
						bolle += 1;
					} else if (tipoFileDaNome.startsWith("CORRIERI")) {
						corrieri += 1;
					} else {
						String message = "Scaricato e spostato nello storico un file di tipo non riconosciuto: '" + file + "'";
						System.out.println(message);
						logger.info(message);
						nonRiconosciuto += 1;
					}
				} else {
					erroriInDownload = true;
				}
			}
			//Cartella precarico
			String remoteFolderPrecarico = configurator.getRemoteFolderPrecarico();
			String remoteStoricFolderPercarico = configurator.getRemoteStoricFolderPercarico();
			List<String> filesPrecarico = client.getFiles(remoteFolderPrecarico);
			logVerbose("Trovati " + filesPrecarico.size() + " file da scaricare  nella cartella '" + remoteFolderPrecarico + "'");
			for (String file : filesPrecarico) {
				boolean success = downloadAndMove(file, localFolderIN, remoteFolderPrecarico, remoteStoricFolderPercarico);
				if (success) {
					String tipoFileDaNome = file.toUpperCase();
					if (tipoFileDaNome.startsWith("CARICHI")) {
						carichi += 1;
					} else {
						logger.info("Scaricato e spostato nello storico un file di tipo non riconosciuto: '" + file + "'");
						nonRiconosciuto += 1;
					}
				} else {
					erroriInDownload = true;
				}
			}
		} catch (Exception e) {
			String message = "Errori durante il download: " + e;
			System.out.println(message);
			logger.error(message);
			erroriInDownload = true;
		}
		//Aggiunta: non lo invio per email ma lo loggo.
		if (erroriInDownload) {
			logger.error("Ci sono stati errori durante il download dei files.");
		}
	}
	
	private static boolean downloadAndMove(String fileName, String localFolder, String remoteFolder, String remoteStoricFolder) {
		boolean success = false;
		String localPath = localFolder + fileName;
		String remoteRelativePath = remoteFolder + fileName;
		String remoteRelativeStoricPath = remoteStoricFolder + fileName;
		boolean download = client.download(localPath, remoteRelativePath);
		if (download) {
			logVerbose("scaricato il file: '" + fileName + "'");
			boolean upload = client.upload(localPath, remoteRelativeStoricPath);
			boolean delete = client.deleteFile(remoteRelativePath);
			if (upload && delete) {
				logVerbose("spostato il file: '" + fileName + "' nella cartella FTP '" + remoteStoricFolder + "'");
				success = true;
			} else {
				String message = "Impossibile spostare nello storico FTP Artcraft il file: '" + fileName + "', upolad archivio: " + upload + ", cancellazione: " + delete;
				System.out.println(message);
				logger.error(message);
			}
		} else {
			String message = "Impossibile scaricare il file: '" + fileName + "'";
			System.out.println(message);
			logger.error(message);
		}
		return success;
	}
	
	/**
	 * Carica i file per lo scambio dati.
	 */
	private static void uploadFiles() {
		logVerbose("Avvio la fase di upload dei files.");
		try {
			String localFolderOUT = configurator.getLocalFolderOUT();
			String remoteUploadFolderCarichi = configurator.getRemoteUploadFolderCarichi();
			String remoteUploadFolderOut = configurator.getRemoteUploadFolderOut();
			File outFolder = new File(localFolderOUT);
			File[] files = outFolder.listFiles();
			logVerbose("Trovati " + files.length + " file da caricare  nella cartella '" + localFolderOUT + "'");
			for (File file : files) {
				if (file.isFile() && file.renameTo(file)) {
					String fileName = file.getName();
					if (fileName.startsWith("TR07")) {
						boolean success = uploadAndMove(file, remoteUploadFolderCarichi);
						if (success) {
							tr07 += 1;
						} else {
							erroriInUpload = true;
						}
					} else if (fileName.startsWith("TR08")) {
						boolean success = uploadAndMove(file, remoteUploadFolderOut);
						if (success) {
							tr08 += 1;
						} else {
							erroriInUpload = true;
						}
					}
				}
			}
		} catch (Exception e) {
			String message = "Errori durante l'upload: " + e;
			System.out.println(message);
			logger.error(message);
			erroriInUpload = true;
		}
	}
	
	private static boolean uploadAndMove(File file, String remoteFolder) {
		boolean success = false;
		String localPath = file.getPath();
		String remoteRelativePath = remoteFolder + file.getName();
		String localFolderTEMPOUT = configurator.getLocalFolderTEMPOUT();
		File localStoricPath = new File(localFolderTEMPOUT + file.getName());
		boolean upload = client.upload(localPath, remoteRelativePath);
		if (upload) {
			boolean move = file.renameTo(localStoricPath);
			if (move) {
				logVerbose("caricato il file: '" + file + "'");
				success = true;
			} else {
				String message = "Impossibile spostare nello storico locale il file: '" + file + "'";
				System.out.println(message);
				logger.error(message);
			}
		} else {
			String message = "Impossibile caricare il file: '" + file + "'";
			System.out.println(message);
			logger.error(message);
		}
		return success;
	}
	
	/**
	 * Logga l'esito delle operazioni e invia una mail di riepilogo.
	 * Nel caso in cui si sono riscontrati errori viene inviata una mail anche ai supervisori.
	 */
	private static void riepilogo() {
		//Download
		int totaleDownload = carichi + articoli + classi + marchi + taglie + bolle + corrieri + nonRiconosciuto + pdf;
		if (totaleDownload > 0) {
			logger.info("Riepilogo download: scaricati " + totaleDownload + " files.");
			if (pdf > 0)
				logger.info("PDF da stampare: " + pdf);
			if (carichi > 0)
				logger.info("Carichi: " + carichi);
			if (articoli > 0)
				logger.info("Articoli: " + articoli);
			if (classi > 0)
				logger.info("Classi: " + classi);
			if (marchi > 0)
				logger.info("Marchi: " + marchi);
			if (taglie > 0)
				logger.info("Taglie: " + taglie);
			if (bolle > 0)
				logger.info("Bolle: " + bolle);
			if (corrieri > 0)
				logger.info("Corrieri: " + corrieri);
			if (nonRiconosciuto > 0)
				logger.info("Tipo sconosciuto: " + nonRiconosciuto);
		} else {
			logVerbose("Nessun file scaricato.");
		}
		//Upload
		int totaleUpload = tr07 + tr08;
		if (totaleUpload > 0) {
			logger.info("Riepilogo upload: caricati " + totaleUpload + " files.");
			if (tr07 > 0)
				logger.info("Carichi: " + tr07);
			if (tr08 > 0)
				logger.info("Ordini: " + tr08);
		} else {
			logVerbose("Nessun file caricato.");
		}
		//Mail
		if (/*erroriInDownload ||*/ erroriInUpload || erroriPDF || totaleDownload > 0 || totaleUpload > 0) {
			List<String> destinatari = new LinkedList<>();
			destinatari.addAll(regolari);
			//Aggiungo la mailing list support solo se ci sono stati problemi.
			if (/*erroriInDownload ||*/ erroriInUpload || erroriPDF)
				destinatari.addAll(supervisori);
			String subject = "Riepilogo download e upload FTP Artcraft";
			if (/*erroriInDownload ||*/ erroriInUpload)
				subject = "ALERT - " + subject;
			String body = "";
//			if (erroriInDownload) {
//				body += "ATTENZIONE: Sono stati riscontrati errori durante il download dei file, contattare il CED.\r\n";
//			}
			if (erroriInUpload) {
				body += "ATTENZIONE: Sono stati riscontrati errori durante l'upload dei tracciati, contattare il CED.\r\n";
			}
			if (erroriPDF) {
				body += "ATTENZIONE: Sono stati riscontrati errori durante la copia dei PDF, contattare il CED.\r\n";
			}
			if (totaleDownload > 0) {
				body += "Riepilogo download: scaricati " + totaleDownload + " files.\r\n";
				if (pdf > 0)
					body += "PDF da stampare: " + pdf + "\r\n";
				if (carichi > 0)
					body += "Carichi: " + carichi + "\r\n";
				if (articoli > 0)
					body += "Articoli: " + articoli + "\r\n";
				if (classi > 0)
					body += "Classi: " + classi + "\r\n";
				if (marchi > 0)
					body += "Marchi: " + marchi + "\r\n";
				if (taglie > 0)
					body += "Taglie: " + taglie + "\r\n";
				if (bolle > 0)
					body += "Bolle: " + bolle + "\r\n";
				if (corrieri > 0)
					body += "Corrieri: " + corrieri + "\r\n";
				if (nonRiconosciuto > 0)
					body += "Tipo sconosciuto: " + nonRiconosciuto + "\r\n";
			}
			if (totaleUpload > 0) {
				body += "Riepilogo upload: caricati " + totaleUpload + " files.\r\n";
				if (tr07 > 0)
					body += "Carichi: " + tr07 + "\r\n";
				if (tr08 > 0)
					body += "Ordini: " + tr08 + "\r\n";
			}
			Email mail = new Email(subject, body);
			boolean invio = mailer.invia(destinatari, mail);
			if (!invio) {
				String message = "Impossibile inviare la mail di riepilogo.";
				System.out.println(message);
				logger.error(message);
			}
		}
	}

}
