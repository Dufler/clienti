package it.ltc.ciesse.scambiodati;

import it.ltc.utility.configuration.ConfigurationParserWithUtils;


public class ConfigurationUtility extends ConfigurationParserWithUtils {
	
	private static final String configPath = "/configuration.properties";
	
	private static ConfigurationUtility instance;
	
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
		super(configPath);
		verbose = Boolean.parseBoolean(configuration.get("verbose"));
		testing = Boolean.parseBoolean(configuration.get("test"));
		// Cartelle locali
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

}
