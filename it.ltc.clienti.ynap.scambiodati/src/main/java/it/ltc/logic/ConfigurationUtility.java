package it.ltc.logic;

import it.ltc.utility.configuration.ConfigurationParserWithUtils;

public class ConfigurationUtility extends ConfigurationParserWithUtils {

	private static final String configPath = "/configuration.properties";
	
	private static ConfigurationUtility instance;
	
	private final boolean verbose;
	
	private final String persistenceUnit;
	
	private final String pathBase;
	private final String pathInventario;
	private final String pathFTPIn;
	private final String pathFTPOut;
	private final String pathCarichiIn;
	private final String pathCarichiOut;
	private final String pathOrdiniIn;
	private final String pathOrdiniOut;

	private ConfigurationUtility() {
		super(configPath);
		//Verbose
		verbose = Boolean.parseBoolean(configuration.get("log_dettagliato"));
		//Persistence Unit
		persistenceUnit = configuration.get("persistence_unit");
		//Path locali
		pathBase = configuration.get("app_path");
		pathInventario = configuration.get("app_inv_path");
		pathCarichiIn = configuration.get("app_carichi_in_path");
		pathCarichiOut = configuration.get("app_carichi_out_path");
		pathOrdiniIn = configuration.get("app_ordini_in_path");
		pathOrdiniOut = configuration.get("app_ordini_out_path");				
		//Path SFTP
		pathFTPIn = configuration.get("ingoing_path");
		pathFTPOut = configuration.get("outgoing_path");
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

	public String getPathBase() {
		return pathBase;
	}

	public String getPathInventario() {
		return pathInventario;
	}

	public String getPathFTPIn() {
		return pathFTPIn;
	}

	public String getPathFTPOut() {
		return pathFTPOut;
	}

	public String getPathCarichiIn() {
		return pathCarichiIn;
	}

	public String getPathCarichiOut() {
		return pathCarichiOut;
	}

	public String getPathOrdiniIn() {
		return pathOrdiniIn;
	}

	public String getPathOrdiniOut() {
		return pathOrdiniOut;
	}

}
