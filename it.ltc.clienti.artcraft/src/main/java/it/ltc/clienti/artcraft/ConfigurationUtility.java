package it.ltc.clienti.artcraft;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.utility.configuration.Configuration;
import it.ltc.utility.ftp.FTP;
import it.ltc.utility.mail.MailMan;


public class ConfigurationUtility {
	
	private static final Logger logger = Logger.getLogger(ConfigurationUtility.class);
	
	private static final String configPath = "/configuration.properties";
	
	private static ConfigurationUtility instance;
	
	private final Configuration configuration;
	
	private final String persistenceUnit;
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
			InputStream stream = ConfigurationUtility.class.getResourceAsStream(configPath);
			configuration = new Configuration(stream, false);
			verbose = Boolean.parseBoolean(configuration.get("verbose"));
			persistenceUnit = configuration.get("persistence_unit");
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
			logger.error(e);
			String errorMessage = "Impossibile caricare i files di configurazione.";
			throw new RuntimeException(errorMessage);
		}
	}

	public static ConfigurationUtility getInstance() {
		if (instance == null) {
			instance = new ConfigurationUtility();
		}
		return instance;
	}

	public String getPersistenceUnit() {
		return persistenceUnit;
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

	public boolean isVerbose() {
		return verbose;
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
