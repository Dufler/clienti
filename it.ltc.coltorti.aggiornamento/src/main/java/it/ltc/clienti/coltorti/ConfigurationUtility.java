package it.ltc.clienti.coltorti;

import it.ltc.utility.configuration.ConfigurationParser;


public class ConfigurationUtility extends ConfigurationParser {
	
	private static final String configPath = "/configuration.properties";
	
	private static ConfigurationUtility instance;
	
	private final String persistenceUnit;
	private final String folderPath;
	private final boolean verbose;

	private ConfigurationUtility() {
		super(configPath);
		verbose = Boolean.parseBoolean(configuration.get("verbose"));
		persistenceUnit = configuration.get("persistence_unit");
		folderPath = configuration.get("folder_path");
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

}
