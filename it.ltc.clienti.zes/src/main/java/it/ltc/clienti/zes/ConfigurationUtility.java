package it.ltc.clienti.zes;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.utility.configuration.Configuration;
import it.ltc.utility.mail.MailMan;


public class ConfigurationUtility {
	
	private static final Logger logger = Logger.getLogger(ConfigurationUtility.class);
	
	private static final String configPath = "/configuration.properties";
	
	private static ConfigurationUtility instance;
	
	private final Configuration configuration;
	
	private final String persistenceUnit;
	private final String folderPath;
	private final boolean verbose;

	private ConfigurationUtility() {
		try {
			InputStream stream = ConfigurationUtility.class.getResourceAsStream(configPath);
			configuration = new Configuration(stream, false);
			verbose = Boolean.parseBoolean(configuration.get("verbose"));
			persistenceUnit = configuration.get("persistence_unit");
			folderPath = configuration.get("folder_path");
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

	public String getFolderPath() {
		return folderPath;
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
		MailMan mm = new MailMan(mailUser, mailPassword, false);
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
	public List<String> getIndirizziDestinatariErrori() {
		List<String> destinatari = new LinkedList<String>();
		String indirizzi = configuration.get("email_destinatari_indirizzi_errori");
		for (String indirizzo : indirizzi.split(","))
			destinatari.add(indirizzo);
		return destinatari;
	}

}
