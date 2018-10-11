package it.ltc.ciesse.scambiodati.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.RighiImballo;
import it.ltc.utility.miscellanea.string.StringUtility;

public class ContenutoColli {
	
	//private static final RighiImballoDao daoImballato = new RighiImballoDao(Import.persistenceUnit);
	
	public static List<String> esportaImballatoDaLista(List<RighiImballo> imballati, String riferimentoOrdine) {
		ArticoliDao daoArticoli = new ArticoliDao(Import.persistenceUnit);
		StringUtility utility = new StringUtility();
		List<String> righe = new LinkedList<>();
		HashMap<String, RighiImballo[]> mappaListe = new HashMap<>(); //meglio array!
		for (RighiImballo imballato : imballati) {
			//elaboro la chiave data dall'articolo e dal collo
			String[] datiArticolo = imballato.getCodiceArticolo().split("_");
			String idUnivoco = datiArticolo[0];
			int index = Integer.parseInt(datiArticolo[1]) - 1;
			String key = idUnivoco + "§" + imballato.getKeyColloSpe() + "§" + imballato.getNrRigoOrdine();
			//Vado a vedere se la mappa lo contiene già o se devo inserirlo
			if (!mappaListe.containsKey(key)) {
				mappaListe.put(key, new RighiImballo[40]);
			}
			RighiImballo presente = mappaListe.get(key)[index];
			if (presente == null) {
				 mappaListe.get(key)[index] = imballato;
			} else {
				presente.setQtaImballata(presente.getQtaImballata() + imballato.getQtaImballata());
			}
		}
		for (String key : mappaListe.keySet()) {
			//Recupero i dati necessari
			String[] dati = key.split("§");
			String idUnivoco = dati[0];
			String collo = dati[1];
			String rigoOrdine = dati[2];
			RighiImballo[] array = mappaListe.get(key);
			//Preparo la riga
			StringBuilder riga = new StringBuilder();
			//All'inizio inserisco tutti i dati sull'articolo e il collo.
			for (RighiImballo imballato : array) {
				if (imballato == null) {
					continue;
				} else {
					//Trovo l'articolo
					Articoli articolo = daoArticoli.trovaDaSKU(imballato.getCodiceArticolo());
					if (articolo == null)
						throw new RuntimeException("Non è stata trovata l'anagrafica per l'articolo imballato: '" + imballato.getCodiceArticolo() + "'");
					riga.append(utility.getFormattedString(riferimentoOrdine, 20));
					riga.append(utility.getFormattedString(rigoOrdine, 10));
					riga.append(utility.getFormattedString(collo, 15));
					riga.append(utility.getFormattedString(articolo.getCodArtOld(), 52));
					riga.append(utility.getFormattedString("", 10)); //Magazzino (che non ho nella riga di imballo)
					riga.append(utility.getFormattedString("", 50)); //Stagione (che non ho nella riga di imballo)
					riga.append(utility.getFormattedString("", 10)); //Bagno ?
					riga.append(utility.getFormattedString("", 10)); //Etichetta ?
					break; //Mi basta la prima per i dati generali, esco dal ciclo.
				}
			}
			//Poi inserisco i dati sulle taglie
			for (RighiImballo imballato : array) {
				int quantita = imballato != null ? imballato.getQtaImballata() : 0;
				riga.append(utility.getFormattedString(quantita, 10));
			}
			//riga.append(taglie.toString());
			riga.append(utility.getFormattedString(idUnivoco, 10)); 
			righe.add(riga.toString());
		}
		return righe;
	}

}
