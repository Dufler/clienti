package it.ltc.ciesse.scambiodati.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
import it.ltc.database.dao.legacy.FornitoreDao;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.utility.miscellanea.string.StringParser;
import it.ltc.utility.miscellanea.string.StringUtility;

public class DocumentiEntrataTestata {
	
	public static final String CONDIZIONE_ELIMINA = "ELIMINA";
	public static final String PATTERN_DATA = "dd/MM/yyyy HH:mm:ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATA);
	
	public static List<PakiTesta> parsaTestate(List<String> righe) throws ParseException {
		FornitoreDao daoFornitore = new FornitoreDao(ConfigurationUtility.getInstance().getPersistenceUnit());
		List<PakiTesta> testate = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 219, PATTERN_DATA, PATTERN_DATA);
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione == 1 /*|| operazione == 2*/) {
				PakiTesta testata = new PakiTesta();
				String codice = parser.getStringa(1, 21);
				//String data = parser.getStringa(21, 40);
				Date dataDocumento = parser.getDataSoloGiorno(21, 40);
				String bollaFornitore = parser.getStringa(40, 60);
				//String data2 = parser.getStringa(60, 79);
				Date dataBolla = parser.getDataSoloGiorno(60, 79);
				String fornitore = parser.getStringa(79, 99);
				String reso = parser.getStringa(99, 100);
				//String vettore = parser.getStringa(100, 120); non utilizzato.
				Fornitori f = daoFornitore.trovaDaCodice(fornitore);
				if (f == null) throw new RuntimeException("Il fornitore dichiarato non esiste. (" + fornitore + ")");
				testata.setCodFornitore(f.getCodiceFornitore());
				testata.setIdFornitore(f.getIdFornitore());
				testata.setRagSocFor(f.getRagSoc());
				testata.setDataPaki(new Timestamp(dataDocumento.getTime()));
				testata.setNrPaki(codice);
				testata.setDataArrivo(new Timestamp(dataBolla.getTime()));
				testata.setNrDocInterno(bollaFornitore);
				if (reso.equals("1")) {
					//Date dataAutorizzazioneReso = parser.getDataSoloGiorno(120, 139);
					String dataAutorizzazioneReso = parser.getStringa(120, 139);
					String numeroAutorizzazioneReso = parser.getStringa(139, 159);
					//String autorizzatore = parser.getStringa(159, 209);
					int numeroColli = parser.getIntero(209, 219);
					testata.setNote(numeroAutorizzazioneReso + " " + dataAutorizzazioneReso + " colli: " + numeroColli);
					testata.setTipoDoc("RESO");
					testata.setTipodocumento("RESO");
				} else {
					testata.setTipoDoc("CARICO");
					testata.setTipodocumento("CARICO");
				}
				testate.add(testata);
			} /*else if (operazione == 3) {
				PakiTesta testata = new PakiTesta();
				String codice = parser.getStringa(1, 21);
				testata.setNrPaki(codice);
				testata.setFlussoDichiarato(CONDIZIONE_ELIMINA);
			}*/
			else {
				throw new RuntimeException("Operazione non consentita sulle righe d'ordine. (update/delete)");
			}
		} while (parser.prossimaLinea());		
		return testate;
	}
	
	public static String esportaTestata(PakiTesta testata) {
		StringUtility utility = new StringUtility(" ", " ", false, false);
		StringBuilder riga = new StringBuilder();
		riga.append("2"); //Gli dico sempre di andare in update.
		riga.append(utility.getFormattedString(testata.getNrPaki(), 20));
		riga.append(sdf.format(testata.getDataPaki()));
		riga.append(utility.getFormattedString(testata.getNrDocInterno(), 20));
		riga.append(sdf.format(testata.getDataArrivo()));
		riga.append(utility.getFormattedString(testata.getCodFornitore(), 20));
		riga.append(testata.getTipoDoc().equals("RESO") ? "1" : "0");
		//In teoria mancano le info sul reso che vengono salvate nelle note. Non so se può comportare un problema.
		while (riga.length() < 219) {
			riga.append(" ");
		}
		return riga.toString();
	}
	
//	public static List<String> esportaTestate(List<PakiTesta> testate) {
//		StringUtility utility = new StringUtility(" ", " ", false, false);
//		List<String> righe = new LinkedList<>();
//		for (PakiTesta testata : testate) {
//			StringBuilder riga = new StringBuilder();
//			riga.append("2"); //Gli dico sempre di andare in update.
//			riga.append(utility.getFormattedString(testata.getNrPaki(), 20));
//			riga.append(sdf.format(testata.getDataPaki()));
//			riga.append(utility.getFormattedString(testata.getNrDocInterno(), 20));
//			riga.append(sdf.format(testata.getDataArrivo()));
//			riga.append(utility.getFormattedString(testata.getCodFornitore(), 20));
//			riga.append(testata.getTipoDoc().equals("RESO") ? "1" : "0");
//			//FIXME - In teoria mancano le info sul reso che vengono salvate nelle note. Non so se può comportare un problema.
//			righe.add(riga.toString());
//		}		
//		return righe;
//	}

}
