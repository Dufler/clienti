package it.ltc.ciesse.scambiodati.model;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.common.NazioneDao;
import it.ltc.database.model.centrale.Nazione;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.utility.miscellanea.string.StringParser;

public class ClientiDestinatari {
	
	public static List<Destinatari> parsaDestinatari(List<String> righe) {
		//NazioniDao daoNazioni = new NazioniDao(ConfigurationUtility.getInstance().getPersistenceUnit());
		NazioneDao daoNazioni = new NazioneDao("produzione");
		List<Destinatari> destinatari = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 1853);
		do {
			//Vado a fare solo inserimenti e modifiche, evito le cancellazioni.
			int operazione = parser.getIntero(0, 1);
			if (operazione == 1 || operazione == 2) {
				String codice = parser.getStringa(1, 21); //Non usato?
				String nazione = parser.getStringa(21, 71);
				String ragioneSociale = parser.getStringa(1483, 1533);//parser.getStringa(71, 121);
				String indirizzo = parser.getStringa(1533, 1583); //parser.getStringa(121, 171);
				//String indirizzo2 = parser.getStringa(171, 221);
				String cap = parser.getStringa(1583, 1593);//parser.getStringa(221, 231);
				String localita = parser.getStringa(1593, 1643);
				String provincia = parser.getStringa(1643, 1645);
				//Nazioni n = daoNazioni.trovaDaNome(nazione);
				//String iso = n != null ? n.getCodIso() : "";
				Nazione n = daoNazioni.trovaDaCodiceISO2(nazione);				
				Destinatari destinatario = new Destinatari();
				destinatario.setCodDestina(codice);
				destinatario.setCap(cap);
				destinatario.setCodIso("");
				destinatario.setIndirizzo(indirizzo);
				destinatario.setLocalita(localita);
				destinatario.setCodIso(n != null ? n.getCodiceIsoTre() : "");
				destinatario.setCodNaz(n != null ? n.getCodiceIsoDue() : "");
				destinatario.setNazione(nazione);
				destinatario.setProvincia(provincia);
				destinatario.setRagSoc1(ragioneSociale); //TODO verificare lunghezze massime per tutti i campi.
				destinatari.add(destinatario);
			}			
		} while (parser.prossimaLinea());
		return destinatari;
	}

}
