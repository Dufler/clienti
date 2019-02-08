package it.ltc.clienti.forza;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;
import it.ltc.utility.configuration.ConfigurationParserWithUtils;

/**
 * Classe helper che aiuta la nella configurazione dei parametri e restituisce
 * oggetti già valorizzati in base al contenuto di "settings.properties"
 * 
 * @author Damiano
 *
 */
public class ConfigurationUtility extends ConfigurationParserWithUtils {

	private static ConfigurationUtility instance;

	private final boolean test;
	private final boolean verbose;

	// Cartelle Locali
	private final String localFolderOrders;
	private final String localFolderInventory;
	private final String localFolderStatus;

	// Cartelle FTP
	private final String remoteFolder;
	private final String remoteArchiveFolder;
	private final String remoteErrorFolder;

	// Corriere
	private final String corriere;
	private final String codiceClienteCorriere;
	private final String servizioCorriere;

	// Persistence Unit
	private final String persistenceUnit;

	// Varie
	private final String nomeFileStatus;
	private final String nomeFileOrders;
	private final String magazzinoDefault;
	private final String nomeFileInvectory;
	private final int avvisoGiorniScadenza;
	private final int avvisoGiorniScadenzaFutura;

	private ConfigurationUtility() {
		super("/settings.properties");
		// Test
		test = Boolean.parseBoolean(configuration.get("test"));
		// Verbose
		verbose = Boolean.parseBoolean(configuration.get("verbose"));
		// Cartelle locali
		if (test) {
			localFolderOrders = configuration.get("test_local_folder_orders");
			localFolderInventory = configuration.get("test_local_folder_inventory");
			localFolderStatus = configuration.get("test_local_folder_status");
		} else {
			localFolderOrders = configuration.get("local_folder_orders");
			localFolderInventory = configuration.get("local_folder_inventory");
			localFolderStatus = configuration.get("local_folder_status");
		}
		// Cartelle FTP
		remoteFolder = configuration.get("folder_ftp");
		remoteArchiveFolder = configuration.get("folder_ftp_archive");
		remoteErrorFolder = configuration.get("folder_ftp_error");
		// Persistence Unit
		persistenceUnit = configuration.get("persistence_unit");
		// Corriere
		corriere = configuration.get("corriere");
		codiceClienteCorriere = configuration.get("corriere_codice_cliente");
		servizioCorriere = configuration.get("corriere_servizio");
		// Varie
		nomeFileStatus = configuration.get("file_name_status");
		nomeFileOrders = configuration.get("file_name_orders");
		nomeFileInvectory = configuration.get("file_name_invectory");
		magazzinoDefault = configuration.get("magazzino_default");
		avvisoGiorniScadenza = Integer.parseInt(configuration.get("avviso_giorni_scadenza"));
		avvisoGiorniScadenzaFutura = Integer.parseInt(configuration.get("avviso_giorni_scadenza_futura"));
	}

	public static ConfigurationUtility getInstance() {
		if (null == instance) {
			instance = new ConfigurationUtility();
		}
		return instance;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public String getLocalFolderOrders() {
		return localFolderOrders;
	}

	public String getLocalFolderInventory() {
		return localFolderInventory;
	}

	public String getLocalFolderStatus() {
		return localFolderStatus;
	}

	public String getRemoteFolder() {
		return remoteFolder;
	}

	public String getRemoteArchiveFolder() {
		return remoteArchiveFolder;
	}

	public String getRemoteErrorFolder() {
		return remoteErrorFolder;
	}

	public String getCorriere() {
		return corriere;
	}

	public String getCodiceClienteCorriere() {
		return codiceClienteCorriere;
	}

	public String getServizioCorriere() {
		return servizioCorriere;
	}

	public String getPersistenceUnit() {
		return persistenceUnit;
	}

	public String getNomeFileStatus() {
		return nomeFileStatus;
	}

	public String getNomeFileOrders() {
		return nomeFileOrders;
	}

	public String getNomeFileInvectory() {
		return nomeFileInvectory;
	}

	public String getMagazzinoDefault() {
		return magazzinoDefault;
	}

	/**
	 * Restituisce la commessa di default per come e' stata impostata nel file di
	 * configurazione.
	 * 
	 * @return la commessa di default.
	 */
	public Commessa getCommessaDefault() {
		Commessa commessa = new Commessa();
		commessa.setId(Integer.valueOf(configuration.get("commessa_id")));
		commessa.setIdCliente(Integer.valueOf(configuration.get("commessa_id_cliente")));
		commessa.setVecchioDbIndirizzo(configuration.get("commessa_vecchio_db_indirizzo"));
		commessa.setVecchioDbNome(configuration.get("commessa_vecchio_db_nome"));
		commessa.setNome(configuration.get("commessa_nome"));
		commessa.setNomeRisorsa(configuration.get("commessa_nome_risorsa"));
		return commessa;
	}

	/**
	 * Restituisce in maniera dinamica l'indirizzo del mittente in base alle info
	 * nella configurazione.
	 * 
	 * @return il model dell'indirizzo del mittente.
	 */
	public MIndirizzo getMittente() {
		MIndirizzo indirizzo = new MIndirizzo();
		indirizzo.setCap(configuration.get("mittente_cap"));
		indirizzo.setEmail(configuration.get("mittente_email"));
		indirizzo.setIndirizzo(configuration.get("mittente_indirizzo"));
		indirizzo.setLocalita(configuration.get("mittente_localita"));
		indirizzo.setNazione(configuration.get("mittente_nazione"));
		indirizzo.setProvincia(configuration.get("mittente_provincia"));
		indirizzo.setRagioneSociale(configuration.get("mittente_ragione_sociale"));
		indirizzo.setTelefono(configuration.get("mittente_telefono"));
		return indirizzo;
	}

	public List<String> getStatiErrore() {
		List<String> statiErrore = new LinkedList<>();
		String stati = configuration.get("lista_stati_errore");
		String[] arrayStati = stati.split(",");
		for (String stato : arrayStati) {
			statiErrore.add(stato);
		}
		return statiErrore;
	}

	public int getAvvisoGiorniScadenza() {
		return avvisoGiorniScadenza;
	}

	public int getAvvisoGiorniScadenzaFutura() {
		return avvisoGiorniScadenzaFutura;
	}

}
