package it.ltc.ciesse.scambiodati.logic;

import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
import it.ltc.database.dao.legacy.DestinatariDao;
import it.ltc.database.model.legacy.Destinatari;

public class RipulitoreIndirizzi {
	
	private static final Logger logger = Logger.getLogger(RipulitoreIndirizzi.class);
	
	private final DestinatariDao daoIndirizzi;
	
	public RipulitoreIndirizzi() {
		String persistenceUnit = ConfigurationUtility.getInstance().getPersistenceUnit();
		daoIndirizzi = new DestinatariDao(persistenceUnit);
	}
	
	public void ripulisciIndirizzi() {
		//trovo tutti gli indirizzi dei destinatari presenti a sistema.
		List<Destinatari> indirizzi = daoIndirizzi.trovaTutti();
		//gli tolgo tutti i caratteri speciali.
		for (Destinatari indirizzo : indirizzi) {
			indirizzo = daoIndirizzi.ripulisciCaratteri(indirizzo);
			if (daoIndirizzi.aggiorna(indirizzo) == null) {
				logger.error("Impossibile aggiornare l'indirizzo: " + indirizzo.toString());
			}
		}
	}

}
