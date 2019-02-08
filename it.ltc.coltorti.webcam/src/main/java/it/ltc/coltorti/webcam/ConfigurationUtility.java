package it.ltc.coltorti.webcam;

import it.ltc.utility.configuration.ConfigurationParser;

public class ConfigurationUtility extends ConfigurationParser {
	
	private static final String configPath = "/settings.properties";
	
	private final String folderPath;
	
	private static ConfigurationUtility instance;

	public static ConfigurationUtility getInstance() {
		if (instance == null) {
			instance = new ConfigurationUtility();
		}
		return instance;
	}

	private ConfigurationUtility() {
		super(configPath);	
		folderPath = configuration.get("folder_path");
	}

	public String getFolderPath() {
		return folderPath;
	}

}
