package it.ltc.clienti.date;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import it.ltc.clienti.date.articoli.ImportaAnagraficheArticoli;
import it.ltc.clienti.date.ordini.ImportaOrdini;
import it.ltc.model.interfaces.importatore.ImportatoreFiles;
import it.ltc.model.interfaces.importatore.RisultatoImportazione;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;

public class Importatore extends ImportatoreFiles {
	
	private static final Logger logger = Logger.getLogger(Importatore.class);
	
	private static Importatore instance;
	
	private final ImportaAnagraficheArticoli controllerArticoli;
	private final ImportaOrdini controllerOrdini;

	private Importatore(String persistenceUnit, String pathFolderIn, String pathFolderError, String pathFolderProcessed, String regexFileName) {
		super(pathFolderIn, pathFolderError, pathFolderProcessed, regexFileName);
		
		controllerArticoli = new ImportaAnagraficheArticoli(persistenceUnit);
		controllerOrdini = new ImportaOrdini(persistenceUnit);
	}
	
	public static Importatore getInstance() {
		if (instance == null) {
			ConfigurationUtility config = ConfigurationUtility.getInstance();
			String persistenceUnit = config.getPersistenceUnit();
			String pathImport = config.getFolderPathImport();
			String pathError = config.getFolderPathErrori();
			String pathProcessed = config.getFolderPathStorico();
			String regex = config.getRegexFile();
			instance = new Importatore(persistenceUnit, pathImport, pathError, pathProcessed, regex);
		}
		return instance;
	}

	@Override
	protected void ordinaFiles(List<File> filesDaImportare) {
//		Iterator<File> iterator = filesDaImportare.iterator();
//		while (iterator.hasNext()) {
//			File file = iterator.next();
//			if (file.getName().equalsIgnoreCase("LOG_DATE_19_1000187.txt")) {
//				logger.warn("Salto il file " + file.getName());
//				iterator.remove();
//			}
//		}
	}

	@Override
	protected RisultatoImportazione importaFile(File file) throws Exception {
		RisultatoImportazione importazione;
		TipoFileImportazione tipo = TipoFileImportazione.trovaTipo(file.getName());
		switch (tipo) {
			case ANAGRAFICHE : importazione = controllerArticoli.importa(file); break;
			case ORDINI : importazione = controllerOrdini.importa(file); break;
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
		String subject = "Riepilogo importazione DATE";
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
