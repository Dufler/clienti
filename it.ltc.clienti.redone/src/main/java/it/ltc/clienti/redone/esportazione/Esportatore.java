package it.ltc.clienti.redone.esportazione;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.clienti.redone.ConfigurationUtility;
import it.ltc.model.interfaces.esportatore.EsportatoreFiles;
import it.ltc.model.interfaces.esportatore.RisultatoEsportazione;

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
		logger.info("Esporto i carichi pronti.");
		controllerCarichi.esportaCarichi();
		logger.info("Esporto gli ordini imballati.");
		controllerImballi.esportaOrdiniImballati();
		logger.info("Esporto gli ordini spediti.");
		controllerInfoSpedizioni.esportaInfoSpedizioni();
		return risultati;
	}
	
	protected void inviaReportEsportazione(List<RisultatoEsportazione> risultatiEsportazione) {
		//TODO
	}

}
