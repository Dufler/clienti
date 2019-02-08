package it.ltc.logic;

import it.ltc.utility.configuration.ConfigurationParserWithUtils;

public class ConfigurationUtility extends ConfigurationParserWithUtils {

	private static final String configPath = "/configuration.properties";
	
	private static ConfigurationUtility instance;
	
	private final boolean verbose;
	
	private final String persistenceUnit;

	private ConfigurationUtility() {
		super(configPath);
		//Verbose
		verbose = Boolean.parseBoolean(configuration.get("log_dettagliato"));
		//Persistence Unit
		persistenceUnit = configuration.get("persistence_unit");
	}

	public static ConfigurationUtility getInstance() {
		if (instance == null) {
			instance = new ConfigurationUtility();
		}
		return instance;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public String getPersistenceUnit() {
		return persistenceUnit;
	}

}
