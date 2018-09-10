package it.ltc.ciesse.scambiodati.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.database.model.legacy.RighiImballo;
import it.ltc.utility.miscellanea.string.StringUtility;

public class ContenutoColli {
	
	//private static final RighiImballoDao daoImballato = new RighiImballoDao(Import.persistenceUnit);
	
	public static List<String> esportaImballatoDaLista(List<RighiImballo> imballati, String riferimentoOrdine) {
		StringUtility utility = new StringUtility(" ", " ", false, false);
		List<String> righe = new LinkedList<>();
		//List<RighiImballo> imballati = daoImballato.trovaDaNumeroLista(numeroLista);
		HashMap<String, RighiImballo[]> mappaListe = new HashMap<>(); //meglio array!
		for (RighiImballo imballato : imballati) {
			String[] datiArticolo = imballato.getCodiceArticolo().split("_");
			String idUnivoco = datiArticolo[0];
			int index = Integer.parseInt(datiArticolo[1]);
			if (!mappaListe.containsKey(idUnivoco)) {
				mappaListe.put(idUnivoco, new RighiImballo[40]);
			}
			mappaListe.get(idUnivoco)[index] = imballato;
		}
		for (String idUnivoco : mappaListe.keySet()) {
			RighiImballo[] array = mappaListe.get(idUnivoco);
			StringBuilder riga = new StringBuilder();
			//StringBuilder taglie = new StringBuilder();
			for (RighiImballo imballato : array) {
				if (imballato == null) {
					continue;
				} else {
					riga.append(utility.getFormattedString(riferimentoOrdine, 20));
					riga.append(utility.getFormattedString(imballato.getNrRigoOrdine(), 10));
					riga.append(utility.getFormattedString(imballato.getKeyColloSpe(), 15));
					riga.append(utility.getFormattedString(imballato.getCodiceArticolo(), 50));
					riga.append(utility.getFormattedString("", 10)); //Magazzino (che non ho nella riga di imballo)
					riga.append(utility.getFormattedString("", 50)); //Stagione (che non ho nella riga di imballo)
					riga.append(utility.getFormattedString("", 10)); //Bagno ?
					riga.append(utility.getFormattedString("", 10)); //Etichetta ?
					break; //Mi basta la prima per i dati generali, esco dal ciclo.
				}
			}
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
