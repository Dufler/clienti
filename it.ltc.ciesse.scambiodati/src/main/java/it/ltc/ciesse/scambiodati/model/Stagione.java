package it.ltc.ciesse.scambiodati.model;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.model.legacy.Stagioni;
import it.ltc.utility.miscellanea.string.StringParser;

public class Stagione {
	
	public static List<Stagioni> parsaStagioni(List<String> righe) {
		List<Stagioni> stagioni = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 56);
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione == 1 || operazione == 2) {
				String descrizione = parser.getStringa(1, 51);
				String codice = parser.getStringa(51, 56);
				Stagioni stagione = new Stagioni();
				stagione.setCodice(codice);
				stagione.setDescrizione(descrizione);
				stagioni.add(stagione);
			}
		} while (parser.prossimaLinea());
		return stagioni;
	}

}
