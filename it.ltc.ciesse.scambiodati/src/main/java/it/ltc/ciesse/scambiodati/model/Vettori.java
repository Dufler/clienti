package it.ltc.ciesse.scambiodati.model;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.model.legacy.Corrieri;
import it.ltc.utility.miscellanea.string.StringParser;

public class Vettori {
	
	public static List<Corrieri> parsaCorrieri(List<String> righe) {
		List<Corrieri> corrieri = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 156);
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione == 1 || operazione == 2) {
				String codice = parser.getStringa(1, 21);
				String codiceTreCaratteri = codice.length() > 3 ? codice.substring(0, 3) : codice;
				String nome = parser.getStringa(53, 103);
				Corrieri corriere = new Corrieri();
				corriere.setCodice(codiceTreCaratteri);
				corriere.setCodiceCliente(codice);
				corriere.setDescrizione(nome);
				//corriere.setPuntoOperatorePartenza("");
				corrieri.add(corriere);
			}
		} while (parser.prossimaLinea());
		return corrieri;
	}

}
