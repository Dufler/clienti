package it.ltc.ciesse.scambiodati;

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
	private final boolean verbose;
	private final boolean testing;
	
	// Cartelle Locali
	private final String localFolderIN;
	private final String localFolderINStorico;
	private final String localFolderINErrori;
	private final String localFolderINNonUsati;
	private final String localFolderOUT;
	private final String localFolderOUTStorico;

	private ConfigurationUtility() {
		try {
			InputStream stream = ConfigurationUtility.class.getResourceAsStream(configPath);
			configuration = new Configuration(stream, false);
			verbose = Boolean.parseBoolean(configuration.get("verbose"));
			testing = Boolean.parseBoolean(configuration.get("test"));
			//Cartelle locali
			if (testing) {
				persistenceUnit = configuration.get("test_persistence_unit");
				localFolderIN = configuration.get("test_folder_local_in");
				localFolderINStorico = configuration.get("test_folder_local_in_storico");
				localFolderINErrori = configuration.get("test_folder_local_in_errori");
				localFolderINNonUsati = configuration.get("test_folder_local_in_nonusati");
				localFolderOUT = configuration.get("test_folder_local_out");
				localFolderOUTStorico = configuration.get("test_folder_local_out_storico");
			} else {
				persistenceUnit = configuration.get("persistence_unit");
				localFolderIN = configuration.get("folder_local_in");
				localFolderINStorico = configuration.get("folder_local_in_storico");
				localFolderINErrori = configuration.get("folder_local_in_errori");
				localFolderINNonUsati = configuration.get("folder_local_in_nonusati");
				localFolderOUT = configuration.get("folder_local_out");
				localFolderOUTStorico = configuration.get("folder_local_out_storico");
			}
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

	public String getLocalFolderINStorico() {
		return localFolderINStorico;
	}

	public String getLocalFolderINErrori() {
		return localFolderINErrori;
	}

	public String getLocalFolderINNonUsati() {
		return localFolderINNonUsati;
	}

	public String getLocalFolderOUTStorico() {
		return localFolderOUTStorico;
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
	public List<String> getIndirizziResponsabili() {
		List<String> destinatari = new LinkedList<String>();
		String indirizzi = configuration.get("email_destinatari_responsabili_indirizzi");
		for (String indirizzo : indirizzi.split(","))
			destinatari.add(indirizzo);
		return destinatari;
	}

}
