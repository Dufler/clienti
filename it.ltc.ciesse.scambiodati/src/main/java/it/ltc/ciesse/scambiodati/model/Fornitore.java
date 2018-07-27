package it.ltc.ciesse.scambiodati.model;

import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.NazioniDao;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.database.model.legacy.Nazioni;
import it.ltc.utility.miscellanea.string.StringParser;

public class Fornitore {
	
	public static List<Fornitori> parsaFornitori(List<String> righe) {
		NazioniDao daoNazioni = new NazioniDao(Import.persistenceUnit);
		List<Fornitori> fornitori = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 343);
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione == 1 || operazione == 2) {
				String codice = parser.getStringa(1, 21);
				String nazione = parser.getStringa(21, 71);
				String nome = parser.getStringa(71, 121);
				String indirizzo = parser.getStringa(121, 221);
				String cap = parser.getStringa(221, 231);
				String localita = parser.getStringa(231, 281);
				String provincia = parser.getStringa(281, 283);
				String telefono = parser.getStringa(283, 303);
				String email = parser.getStringa(323, 343);
				Nazioni n = daoNazioni.trovaDaNome(nazione);
				String iso = n != null ? n.getCodIso() : "";
				Fornitori fornitore = new Fornitori();
				fornitore.setCap(cap);
				fornitore.setCitta(localita);
				fornitore.setCodiceFornitore(codice);
				fornitore.setEMail(email);
				fornitore.setIndirizzo(indirizzo);
				fornitore.setNaz(nazione);
				fornitore.setProv(provincia);
				fornitore.setRagSoc(nome);
				fornitore.setTel(telefono);
				fornitore.setCodnaz(iso);
				fornitori.add(fornitore);
			}
		} while (parser.prossimaLinea());
		return fornitori;
	}

}
