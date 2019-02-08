package it.ltc.clienti.artcraft;

import it.ltc.utility.configuration.ConfigurationParserWithUtils;


public class ConfigurationUtility extends ConfigurationParserWithUtils {
	
	private static final String configPath = "/configuration.properties";
	
	private static ConfigurationUtility instance;
	
	private final String persistenceUnit;
	private final boolean verbose;
	
	// Cartelle Locali
	private final String localFolderIN;
	private final String localFolderOUT;
	private final String localFolderTEMPOUT;
	private final String localFolderPDF;

	// Cartelle FTP
	private final String remoteFolder;
	private final String remoteStoricFolder;
	private final String remoteFolderPrecarico;
	private final String remoteStoricFolderPercarico;
	private final String remotePDFFolder;
	private final String remotePDFStoricFolder;
	private final String remoteUploadFolderCarichi;
	private final String remoteUploadFolderOut;

	private ConfigurationUtility() {
		super(configPath);
		verbose = Boolean.parseBoolean(configuration.get("verbose"));
		persistenceUnit = configuration.get("persistence_unit");
		// Cartelle locali
		localFolderIN = configuration.get("folder_local_in");
		localFolderOUT = configuration.get("folder_local_out");
		localFolderTEMPOUT = configuration.get("folder_local_tempout");
		localFolderPDF = configuration.get("folder_local_pdf");
		// Cartelle FTP
		remoteFolder = configuration.get("folder_ftp_in");
		remoteStoricFolder = configuration.get("folder_ftp_in_archive");
		remoteFolderPrecarico = configuration.get("folder_ftp_precarico");
		remoteStoricFolderPercarico = configuration.get("folder_ftp_precarico_archive");
		remotePDFFolder = configuration.get("folder_ftp_pdf");
		remotePDFStoricFolder = configuration.get("folder_ftp_pdf_archive");
		remoteUploadFolderCarichi = configuration.get("folder_ftp_carichi");
		remoteUploadFolderOut = configuration.get("folder_ftp_out");
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

	public String getLocalFolderTEMPOUT() {
		return localFolderTEMPOUT;
	}

	public String getLocalFolderPDF() {
		return localFolderPDF;
	}

	public String getRemoteFolder() {
		return remoteFolder;
	}

	public String getRemoteStoricFolder() {
		return remoteStoricFolder;
	}

	public String getRemoteFolderPrecarico() {
		return remoteFolderPrecarico;
	}

	public String getRemoteStoricFolderPercarico() {
		return remoteStoricFolderPercarico;
	}

	public String getRemotePDFFolder() {
		return remotePDFFolder;
	}

	public String getRemotePDFStoricFolder() {
		return remotePDFStoricFolder;
	}

	public String getRemoteUploadFolderCarichi() {
		return remoteUploadFolderCarichi;
	}

	public String getRemoteUploadFolderOut() {
		return remoteUploadFolderOut;
	}

	public boolean isVerbose() {
		return verbose;
	}

}
