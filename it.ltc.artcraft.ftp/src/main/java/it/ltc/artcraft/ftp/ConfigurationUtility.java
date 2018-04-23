package it.ltc.artcraft.ftp;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import it.ltc.utility.configuration.Configuration;
import it.ltc.utility.ftp.FTP;
import it.ltc.utility.mail.MailMan;

/**
 * Classe helper che aiuta la nella configurazione dei parametri e restituisce
 * oggetti già valorizzati in base al contenuto di "settings.properties"
 * 
 * @author Damiano
 *
 */
public class ConfigurationUtility {

	private static ConfigurationUtility instance;

	private final Configuration configuration;
	
	private final boolean verbose;

	// Cartelle Locali
	private final String localFolderIN;
	private final String localFolderOUT;
	private final String localFolderTEMPOUT;
	private final String localFolderPDF;

	// Cartelle FTP
	private final String remoteFolder;
	private final String remoteStoricFolder;
	private final String remoteFolderPrecarico;
	private final String remoteStoricFolderPercarico;
	private final String remotePDFFolder;
	private final String remotePDFStoricFolder;
	private final String remoteUploadFolderCarichi;
	private final String remoteUploadFolderOut;

	private ConfigurationUtility() {
		try {
			configuration = new Configuration("/settings.properties", false);
			//Verbose
			verbose = Boolean.parseBoolean(configuration.get("verbose"));
			//Cartelle locali
			localFolderIN = configuration.get("folder_local_in");
			localFolderOUT = configuration.get("folder_local_out");
			localFolderTEMPOUT = configuration.get("folder_local_tempout");
			localFolderPDF = configuration.get("folder_local_pdf");
			//Cartelle FTP
			remoteFolder = configuration.get("folder_ftp_in");
			remoteStoricFolder = configuration.get("folder_ftp_in_archive");
			remoteFolderPrecarico = configuration.get("folder_ftp_precarico");
			remoteStoricFolderPercarico = configuration.get("folder_ftp_precarico_archive");
			remotePDFFolder = configuration.get("folder_ftp_pdf");
			remotePDFStoricFolder = configuration.get("folder_ftp_pdf_archive");
			remoteUploadFolderCarichi = configuration.get("folder_ftp_carichi");
			remoteUploadFolderOut = configuration.get("folder_ftp_out");
		} catch (IOException e) {
			String errorMessage = "Impossibile caricare i files di configurazione.";
			throw new RuntimeException(errorMessage);
		}
	}

	public static ConfigurationUtility getInstance() {
		if (null == instance) {
			instance = new ConfigurationUtility();
		}
		return instance;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public String getLocalFolderIN() {
		return localFolderIN;
	}

	public String getLocalFolderOUT() {
		return localFolderOUT;
	}

	public String getLocalFolderTEMPOUT() {
		return localFolderTEMPOUT;
	}

	public String getLocalFolderPDF() {
		return localFolderPDF;
	}

	public String getRemoteFolder() {
		return remoteFolder;
	}

	public String getRemoteStoricFolder() {
		return remoteStoricFolder;
	}

	public String getRemoteFolderPrecarico() {
		return remoteFolderPrecarico;
	}

	public String getRemoteStoricFolderPercarico() {
		return remoteStoricFolderPercarico;
	}

	public String getRemotePDFFolder() {
		return remotePDFFolder;
	}

	public String getRemotePDFStoricFolder() {
		return remotePDFStoricFolder;
	}

	public String getRemoteUploadFolderCarichi() {
		return remoteUploadFolderCarichi;
	}

	public String getRemoteUploadFolderOut() {
		return remoteUploadFolderOut;
	}

	/**
	 * Restituisce il postino.
	 * 
	 * @return un MailMan gia' configurato.
	 */
	public MailMan getMailMan() {
		String mailUser = configuration.get("email_mittente_indirizzo");
		String mailPassword = configuration.get("email_mittente_password");
		MailMan mm = new MailMan(mailUser, mailPassword);
		return mm;
	}

	/**
	 * Restituisce la lista di indirizzi dei destinatari.
	 * 
	 * @return una lista di indirizzi mail a cui verranno spedite le notifiche.
	 */
	public List<String> getIndirizziDestinatari() {
		List<String> destinatari = new LinkedList<String>();
		String indirizzi = configuration.get("email_destinatari_indirizzi");
		for (String indirizzo : indirizzi.split(","))
			destinatari.add(indirizzo);
		return destinatari;
	}

	/**
	 * Restituisce la lista di indirizzi dei destinatari.
	 * 
	 * @return una lista di indirizzi mail a cui verranno spedite le notifiche.
	 */
	public List<String> getIndirizziResponsabili() {
		List<String> destinatari = new LinkedList<String>();
		String indirizzi = configuration.get("email_destinatari_responsabili_indirizzi");
		for (String indirizzo : indirizzi.split(","))
			destinatari.add(indirizzo);
		return destinatari;
	}

	/**
	 * Restituisce un client FTP.
	 * 
	 * @return un client FTP gia' configurato.
	 */
	public FTP getFTPClient() {
		String ftpHost = configuration.get("ftp_host");
		String ftpUser = configuration.get("ftp_user");
		String ftpPassword = configuration.get("ftp_password");
		FTP client = new FTP(ftpHost, ftpUser, ftpPassword);
		return client;
	}

}
