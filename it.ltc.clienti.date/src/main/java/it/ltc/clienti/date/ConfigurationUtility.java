package it.ltc.clienti.date;

import it.ltc.utility.configuration.ConfigurationParser;

public class ConfigurationUtility extends ConfigurationParser {

	private static final String configPath = "/configuration.properties";

	private static ConfigurationUtility instance;

	private final String persistenceUnit;
	private final String folderPathImport;
	private final String folderPathStorico;
	private final String folderPathErrori;
	private final String folderPathExport;
	private final String regexOrdini;
	private final String regexArticoli;
	private final boolean verbose;

	private ConfigurationUtility() {
		super(configPath);
		verbose = Boolean.parseBoolean(configuration.get("verbose"));
		persistenceUnit = configuration.get("persistence_unit");
		folderPathImport = configuration.get("folder_path_import");
		folderPathStorico = configuration.get("folder_path_storico");
		folderPathErrori = configuration.get("folder_path_errori");
		folderPathExport = configuration.get("folder_path_export");
		regexOrdini = configuration.get("regex_nome_file_ordini");
		regexArticoli = configuration.get("regex_nome_file_anagrafiche");
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
	
	public String getFolderPathStorico() {
		return folderPathStorico;
	}

	public String getFolderPathErrori() {
		return folderPathErrori;
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
