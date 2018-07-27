package aggiornacoltorti;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.database.dao.legacy.ColliImballoDao;
import it.ltc.database.dao.legacy.ColliPrelevaDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.model.legacy.ColliImballo;
import it.ltc.database.model.legacy.ColliPreleva;
import it.ltc.database.model.legacy.TestataOrdini;

public class AggiornaStatoOrdiniSpediti {
	
	public static final String persistenceUnit = "legacy-coltorti";
	
	private static final Logger logger = Logger.getLogger(AggiornaStatoOrdiniSpediti.class);

	public static void main(String[] args) {
		logger.info("Avvio procedura aggiornamento stato ordini.");
		TestataOrdiniDao daoTestate = new TestataOrdiniDao(persistenceUnit);
		ColliImballoDao daoImballati = new ColliImballoDao(persistenceUnit);
		ColliPrelevaDao daoSpedititi = new ColliPrelevaDao(persistenceUnit);
		logger.info("Setup terminato, avvio la ricerca.");
		//Trovo tutti gli ordini per cui abbiamo i documenti di spedizione pronti
		List<TestataOrdini> inspedizione = daoTestate.trovaDaStato("INSP");
		logger.info("Stanno per essere controllati " + inspedizione.size() + " ordini.");
		for (TestataOrdini ordine : inspedizione) {
			//Recupero tutti i colli imballati
			List<ColliImballo> imballati = daoImballati.trovaDaNumeroLista(ordine.getNrLista());
			//Recupero tutti i colli che stanno per essere affidati al corriere
			List<ColliPreleva> spediti = daoSpedititi.trovaDaNumeroLista(ordine.getNrLista());
			//Controllo che ci sia corrispondenza per tutti i colli, se c'è flaggo l'ordine come spedito.
			HashMap<String, Boolean> mappaCorrispondenza = new HashMap<>();
			for (ColliImballo imballato : imballati) {
				mappaCorrispondenza.put(imballato.getKeyColloSpe(), false);
			}
			for (ColliPreleva daSpedire : spediti) {
				if (daSpedire.getSpedito().equals("SI") && mappaCorrispondenza.containsKey(daSpedire.getKeyColloPre())) {
					mappaCorrispondenza.put(daSpedire.getKeyColloPre(), true);
				}
			}
			boolean tuttiSpediti = true;
			for (Boolean spedito : mappaCorrispondenza.values()) {
				if (!spedito) {
					tuttiSpediti = false;
					break;
				}
			}
			if (tuttiSpediti) {
				ordine.setStato("SPED");
				TestataOrdini aggiornata = daoTestate.aggiorna(ordine);
				if (aggiornata == null) {
					logger.error("Impossibile aggiornare lo stato dell'ordine: '" + ordine.getNrLista() + "'");
				} else {
					logger.info("Aggiornato lo stato dell'ordine: " + ordine.getNrLista() + "' a SPED");
				}
			}
		}
		logger.info("Termine procedura.");
	}

}
