package it.ltc.clienti.redone.importazione;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import it.ltc.clienti.redone.ConfigurationUtility;
import it.ltc.model.interfaces.importatore.ImportatoreFiles;
import it.ltc.model.interfaces.importatore.RisultatoImportazione;
import it.ltc.utility.csv.FileCSV;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;

public class Importatore extends ImportatoreFiles {
	
	private static final Logger logger = Logger.getLogger(Importatore.class);
	
	private static final String CSV_SEPARATOR = "\\|";
	private static final String CSV_DATE_FORMAT = "yyyyddMM";
	private static final String REGEX_FILE_NAMES = "\\w+_\\d+\\.csv";
	
	private static Importatore instance;
	
	private final ImportatoreProdotti controllerProdotti;
	private final ImportatoreFornitori controllerFornitori;
	private final ImportatoreCarichi controllerCarichi;
	private final ImportatoreOrdini controllerOrdini;
	private final ImportatoreInfoSpedizione controllerSpedizioni;

	private Importatore(String persistenceUnit, String pathIn) {
		super(pathIn, pathIn + "error\\", pathIn + "processed\\", REGEX_FILE_NAMES); 
		
		controllerProdotti = new ImportatoreProdotti(persistenceUnit);
		controllerFornitori = new ImportatoreFornitori(persistenceUnit);
		controllerCarichi = new ImportatoreCarichi(persistenceUnit);
		controllerOrdini = new ImportatoreOrdini(persistenceUnit);
		controllerSpedizioni = new ImportatoreInfoSpedizione(persistenceUnit);
	}

	public static Importatore getInstance() {
		if (instance == null) {
			String persistenceUnit = ConfigurationUtility.getInstance().getPersistenceUnit();
			String pathIn = ConfigurationUtility.getInstance().getFolderPathIN();
			instance = new Importatore(persistenceUnit, pathIn);
		}
		return instance;
	}

	@Override
	protected void ordinaFiles(List<File> filesDaImportare) {
		filesDaImportare.sort(new Ordinatore());
	}

	@Override
	protected RisultatoImportazione importaFile(File file) throws Exception {
		//in base al tipo di file eseguo una diversa importazione.
		RisultatoImportazione importazione;
		FileCSV csv = FileCSV.leggiFile(file, true, CSV_SEPARATOR, CSV_SEPARATOR, CSV_DATE_FORMAT);
		TipoFileImportazione fileType = TipoFileImportazione.trovaTipo(file.getName());
		if (fileType == null)
			importazione = null;
		else switch (fileType) {
			case PRODUCT : case PRODUCTS : importazione = controllerProdotti.importaArticoli(csv); break;
			case SUPPLIER : case SUPPLIERS : importazione = controllerFornitori.importaFornitori(csv); break;
			case INBOUND : case INBOUNDS : importazione = controllerCarichi.importaCarichi(csv); break;
			case ORDER : case ORDERS : importazione = controllerOrdini.importaOrdini(csv); break;
			case SHIPMENT : case SHIPMENTS : importazione = controllerSpedizioni.importaSpedizioni(csv); break;
			default : importazione = null;
		}
		return importazione;
	}

	@Override
	protected void inviaReportImportazione(List<RisultatoImportazione> risultati) {
		boolean alert = false;
		StringBuilder sb = new StringBuilder("Report importazione files\r\n");
		for (RisultatoImportazione importazione : risultati) {
			sb.append(importazione.getDescrizioneRisultato());
			sb.append("\r\n");
			if (importazione.isInErrore())
				alert = true;
		}
		String subject = "Riepilogo importazione ReDone";
		MailMan postino = ConfigurationUtility.getInstance().getMailMan();
		Set<String> destinatari = ConfigurationUtility.getInstance().getIndirizziDestinatari();
		if (alert) {
			destinatari.addAll(ConfigurationUtility.getInstance().getIndirizziDestinatariErrori());
		}
		Email mail = new Email(subject, sb.toString());
		boolean invio = postino.invia(destinatari, mail);
		if (!invio)
			logger.error("Impossibile inviare la mail di report sull'esportazione.");
	}

}
