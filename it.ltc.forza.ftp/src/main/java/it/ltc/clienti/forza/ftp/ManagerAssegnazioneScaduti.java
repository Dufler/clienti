package it.ltc.clienti.forza.ftp;

import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.database.dao.ordini.AssegnazioneOrdine;
import it.ltc.database.dao.ordini.AssegnazioneProdotto;
import it.ltc.database.dao.ordini.ManagerAssegnazione;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.TestataOrdini;

public class ManagerAssegnazioneScaduti extends ManagerAssegnazione {
	
	private static final Logger logger = Logger.getLogger(ManagerAssegnazioneScaduti.class);

	public ManagerAssegnazioneScaduti(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	public void assegnaScaduti(TestataOrdini testata, List<RighiOrdine> righe, List<ColliPack> prodotti) {
		logger.info("Procedo con l'assegnazione dell'ordine con scadenze forzandolo sui specifici prodotti.");
		//Forza l'assegnazione delle righe sui colli pack specificati
		AssegnazioneOrdine assegnazioneOrdine = new AssegnazioneOrdine(testata);
		for (RighiOrdine riga : righe) {
			AssegnazioneProdotto assegnazione = new AssegnazioneProdotto(riga);
			assegnazioneOrdine.aggiungiAssegnazione(assegnazione);
			assegnaSuColliAPrelievo(prodotti, assegnazione, testata, riga);
		}
		if (!prodotti.isEmpty()) {
			throw new RuntimeException("Non sono stati utilizzati tutti i colli pack!?");
		}
		//Aggiorno i campi necessari sulla testata
		aggiornaValoriTestata(testata);
		//Procedi come in maniera useta.
		logger.info("Preparazione assegnazione terminata, procedo con la scrittura sul DB.");
		assegnaOrdine(assegnazioneOrdine);
	}

}
