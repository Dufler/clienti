package it.ltc.clienti.redone.esportazione;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.clienti.redone.ConfigurationUtility;
import it.ltc.model.interfaces.esportatore.EsportatoreFiles;
import it.ltc.model.interfaces.esportatore.RisultatoEsportazione;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;

public class Esportatore extends EsportatoreFiles {
	
	private static final Logger logger = Logger.getLogger(Esportatore.class);
	
	private static Esportatore instance;
	
	private final EsportatoreCarichi controllerCarichi;
	private final EsportatoreImballi controllerImballi;
	private final EsportatoreInfoSpedizioni controllerInfoSpedizioni;

	private Esportatore(String pathOut, String pathBackUp) {
		super(pathOut, pathBackUp);
		String persistenceUnit = ConfigurationUtility.getInstance().getPersistenceUnit();
		controllerCarichi = new EsportatoreCarichi(persistenceUnit);
		controllerImballi = new EsportatoreImballi(persistenceUnit);
		controllerInfoSpedizioni = new EsportatoreInfoSpedizioni(persistenceUnit);
	}

	public static Esportatore getInstance() {
		if (instance == null) {
			String pathOut = ConfigurationUtility.getInstance().getFolderPathOUT();
			String pathBackUp = ConfigurationUtility.getInstance().getFolderPathBackUpExport();
			instance = new Esportatore(pathOut, pathBackUp);
		}
		return instance;
	}
	
	public List<RisultatoEsportazione> getRisultatiEsportazione() {
		List<RisultatoEsportazione> risultati = new LinkedList<>();
		controllerCarichi.esportaCarichi();
		controllerImballi.esportaOrdiniImballati();
		controllerInfoSpedizioni.esportaInfoSpedizioni();
		return risultati;
	}
	
	protected void inviaReportEsportazione(List<RisultatoEsportazione> risultati) {
		boolean alert = false;
		StringBuilder sb = new StringBuilder("Report esportazione files\r\n");
		for (RisultatoEsportazione importazione : risultati) {
			sb.append(importazione.getDescrizioneRisultato());
			sb.append("\r\n");
			if (importazione.isInErrore())
				alert = true;
		}
		String subject = "Riepilogo esportazione ReDone";
		MailMan postino = ConfigurationUtility.getInstance().getMailMan();
		List<String> destinatari = alert ? ConfigurationUtility.getInstance().getIndirizziDestinatari() : ConfigurationUtility.getInstance().getIndirizziDestinatariErrori();
		Email mail = new Email(subject, sb.toString());
		boolean invio = postino.invia(destinatari, mail);
		if (!invio)
			logger.error("Impossibile inviare la mail di report sull'esportazione.");
	}

}
