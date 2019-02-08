package it.ltc.clienti.redone;

import it.ltc.utility.configuration.ConfigurationParserWithUtils;

public class ConfigurationUtility extends ConfigurationParserWithUtils {

	private static final String configPath = "/configuration.properties";

	private static ConfigurationUtility instance;

	private final String persistenceUnit;
	private final String folderPathIN;
	private final String folderPathOUT;
	private final String folderPathDOCS;
	private final String folderPathBackUpExport;
	private final boolean verbose;

	private ConfigurationUtility() {
		super(configPath);

		verbose = Boolean.parseBoolean(configuration.get("verbose"));
		persistenceUnit = configuration.get("persistence_unit");
		folderPathIN = configuration.get("folder_path_in");
		folderPathOUT = configuration.get("folder_path_out");
		folderPathDOCS = configuration.get("folder_path_docs");
		folderPathBackUpExport = configuration.get("folder_path_backup_export");
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

	public String getFolderPathIN() {
		return folderPathIN;
	}

	public String getFolderPathOUT() {
		return folderPathOUT;
	}

	public String getFolderPathDOCS() {
		return folderPathDOCS;
	}

	public String getFolderPathBackUpExport() {
		return folderPathBackUpExport;
	}

	public boolean isVerbose() {
		return verbose;
	}

}
