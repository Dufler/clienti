package it.ltc.clienti.date;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import it.ltc.utility.configuration.Configuration;


public class ConfigurationUtility {
	
	private static final Logger logger = Logger.getLogger(ConfigurationUtility.class);
	
	private static final String configPath = "/configuration.properties";
	
	private static ConfigurationUtility instance;
	
	private final Configuration configuration;
	
	private final String persistenceUnit;
	private final String folderPathImport;
	private final String folderPathExport;
	private final String regexOrdini;
	private final String regexArticoli;
	private final boolean verbose;

	private ConfigurationUtility() {
		try {
			InputStream stream = ConfigurationUtility.class.getResourceAsStream(configPath);
			configuration = new Configuration(stream, false);
			verbose = Boolean.parseBoolean(configuration.get("verbose"));
			persistenceUnit = configuration.get("persistence_unit");
			folderPathImport = configuration.get("folder_path_import");
			folderPathExport = configuration.get("folder_path_export");
			regexOrdini = configuration.get("regex_nome_file_ordini");
			regexArticoli = configuration.get("regex_nome_file_anagrafiche");
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

	public String getFolderPathImport() {
		return folderPathImport;
	}
	
	public String getFolderPathExport() {
		return folderPathExport;
	}

	public String getRegexOrdini() {
		return regexOrdini;
	}

	public String getRegexArticoli() {
		return regexArticoli;
	}

	public boolean isVerbose() {
		return verbose;
	}

}
