package it.ltc.ciesse.scambiodati.model;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.model.legacy.Colori;
import it.ltc.utility.miscellanea.string.StringParser;

public class Colore {
	
	public static List<Colori> parseColori(List<String> righe) {
		List<Colori> colori = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 61);
		do {
			String codice = parser.getStringa(1, 13);
			String descrizione = parser.getStringa(13, 63);
			Colori colore = new Colori();
			colore.setCodice(codice);
			colore.setDescrizione(descrizione);
			colori.add(colore);
		} while (parser.prossimaLinea());
		return colori;
	}

}
