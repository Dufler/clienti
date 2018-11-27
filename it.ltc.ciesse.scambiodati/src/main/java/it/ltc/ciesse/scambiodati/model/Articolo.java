package it.ltc.ciesse.scambiodati.model;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
import it.ltc.database.dao.legacy.ColoriDao;
import it.ltc.database.dao.legacy.NumerateDao;
import it.ltc.database.model.legacy.Colori;
import it.ltc.database.model.legacy.Numerata;
import it.ltc.model.interfaces.prodotto.Cassa;
import it.ltc.model.interfaces.prodotto.MProdotto;
import it.ltc.utility.miscellanea.string.StringParser;

public class Articolo {
	
	private static final Logger logger = Logger.getLogger(Articolo.class);
	
	//private static final SimpleDateFormat idUnivocoGenerator = new SimpleDateFormat("yyMMddHHmmssSSS");
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
	
	//private static StringUtility utility = new StringUtility();
	//private static int idUnivoco = 0;
	
	private static HashMap<String, Numerata> mappaNumerate = new HashMap<>();
	private static NumerateDao daoNumerate = new NumerateDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	
	private static HashMap<String, Colori> mappaColori = new HashMap<>();
	private static ColoriDao daoColori = new ColoriDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	
	public static List<MProdotto> parsaArticoli(List<String> righe) {
		//Operazioni per sveltire il massivo.
		trovaTuttiColori();
		trovaTutteNumerate();
		//Da qui in poi è standard.
		List<MProdotto> articoli = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 1588);
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione == 1 || operazione == 2) {
				String codice = parser.getStringa(1, 53);
				String taglia = parser.getStringa(53, 83);
				Numerata numerata = trovaNumerata(taglia);
				if (numerata == null)
					throw new RuntimeException("Impossibile trovare la taglia della numerata: '" + taglia + "'");
				String codiceColore = parser.getStringa(83, 95);
				Colori colore = trovaColore(codiceColore);
				String descrizioneColore = colore != null ? colore.getDescrizione() : codiceColore;
				if (descrizioneColore != null && descrizioneColore.length() > 40)
					descrizioneColore = descrizioneColore.substring(0, 40);
				if (colore == null) {
					//throw new RuntimeException("Impossibile trovare il colore: '" + codiceColore + "'");
					logger.warn("Impossibile trovare il colore: '" + codiceColore + "'");
				}
				String descrizione = parser.getStringa(95, 155);
				String descrizioneAlternativa = parser.getStringa(155, 185);
				String unitaMisura = parser.getStringa(185, 195);
				//Sarebbe 20 caratteri ma sul db c'è posto solo per 10 e non è una info rilevante.
				if (unitaMisura != null && unitaMisura.length() > 10) {
					unitaMisura.substring(0, 10);
				}
				int idUnivoco = parser.getIntero(256, 265);
				Integer pezziCassa = parser.getIntero(267, 277);
				if (pezziCassa == null) {
					String pezziSegnati = parser.getStringa(267, 277);
					logger.warn("I pezzi segnati per l'articolo '" + codice + "' non sono corretti, valore riportato: '" + pezziSegnati + "', imposto il valore a 1.");
					pezziCassa = 1;
				}
				Cassa cassa = pezziCassa > 1 ? Cassa.STANDARD : Cassa.NO;
				String tipoCassa = pezziCassa > 1 ? Integer.toString(idUnivoco) : "";
				int pezziPerConfezione = 1;
				String linea = parser.getStringa(1501, 1551);
				String categoriaMerceologica = parser.getStringa(1551, 1601);
				String stagione = parser.getStringa(1601, 1611);
				int indexBarcode = 301;
				int counter = 1;
				while (indexBarcode < 1501) {
					String barcode = parser.getStringa(indexBarcode, indexBarcode + 30);
					//Se ho un barcode vado ad inserire/aggiornare l'anagrafica altrimenti passo al prossimo.
					if (!barcode.isEmpty()) {
						String veraTaglia = trovaTaglia(numerata, counter);
						MProdotto articolo = new MProdotto();
						//articolo.setIdUniArticolo(getIDUnivoco());
						articolo.setChiaveCliente(idUnivoco + "_" + counter);
						articolo.setCodiceModello(Integer.toString(idUnivoco));
						articolo.setBarcode(barcode);
						articolo.setCategoria(categoriaMerceologica);
						articolo.setSkuFornitore(codice);
						articolo.setColore(descrizioneColore);
						articolo.setDescrizione(descrizione);
						articolo.setDescrizioneAggiuntiva(descrizioneAlternativa);
						articolo.setBrand(linea);
						articolo.setStagione(stagione);
						articolo.setTaglia(veraTaglia);
						articolo.setNumerata(numerata.getCodice());
						articolo.setPosizioneNumerata(counter);
						articolo.setUnitaMisura(unitaMisura);
						articolo.setPezziConfezione(pezziPerConfezione);
						articolo.setPezziCassa(pezziCassa);
						articolo.setCassa(cassa);
						articolo.setTipoCassa(tipoCassa);
						articoli.add(articolo);
					}
					indexBarcode += 30;
					counter++;
				}
			}
		} while (parser.prossimaLinea());
		return articoli;
	}
	
//	public static List<Articoli> parsaArticoli(List<String> righe) {
//		//Operazioni per sveltire il massivo.
//		trovaTuttiColori();
//		trovaTutteNumerate();
//		//Da qui in poi è standard.
//		List<Articoli> articoli = new LinkedList<>();
//		String[] lines = new String[righe.size()];
//		lines = righe.toArray(lines);
//		StringParser parser = new StringParser(lines, 1588);
//		do {
//			int operazione = parser.getIntero(0, 1);
//			if (operazione == 1 || operazione == 2) {
//				String codice = parser.getStringa(1, 53);
//				String taglia = parser.getStringa(53, 83);
//				Numerata numerata = trovaNumerata(taglia);
//				if (numerata == null)
//					throw new RuntimeException("Impossibile trovare la taglia della numerata: '" + taglia + "'");
//				String codiceColore = parser.getStringa(83, 95);
//				Colori colore = trovaColore(codiceColore);
//				String descrizioneColore = colore != null ? colore.getDescrizione() : codiceColore;
//				if (descrizioneColore != null && descrizioneColore.length() > 40)
//					descrizioneColore = descrizioneColore.substring(0, 40);
//				if (colore == null) {
//					//throw new RuntimeException("Impossibile trovare il colore: '" + codiceColore + "'");
//					logger.warn("Impossibile trovare il colore: '" + codiceColore + "'");
//				}
//				String descrizione = parser.getStringa(95, 155);
//				String descrizioneAlternativa = parser.getStringa(155, 185);
//				String unitaMisura = parser.getStringa(185, 195);
//				//Sarebbe 20 caratteri ma sul db c'è posto solo per 10 e non è una info rilevante.
//				if (unitaMisura != null && unitaMisura.length() > 10) {
//					unitaMisura.substring(0, 10);
//				}
//				int idUnivoco = parser.getIntero(256, 265);
//				Integer pezziCassa = parser.getIntero(267, 277);
//				if (pezziCassa == null) {
//					String pezziSegnati = parser.getStringa(267, 277);
//					logger.warn("I pezzi segnati per l'articolo '" + codice + "' non sono corretti, valore riportato: '" + pezziSegnati + "', imposto il valore a 1.");
//					pezziCassa = 1;
//				}
//				String cassa = pezziCassa > 1 ? "STANDARD" : "NO";
//				String tipoCassa = pezziCassa > 1 ? Integer.toString(idUnivoco) : "";
//				int pezziPerConfezione = 1;
//				String linea = parser.getStringa(1501, 1551);
//				String categoriaMerceologica = parser.getStringa(1551, 1601);
//				String stagione = parser.getStringa(1601, 1611);
//				int indexBarcode = 301;
//				int counter = 1;
//				while (indexBarcode < 1501) {
//					String barcode = parser.getStringa(indexBarcode, indexBarcode + 30);
//					//Se ho un barcode vado ad inserire/aggiornare l'anagrafica altrimenti passo al prossimo.
//					if (!barcode.isEmpty()) {
//						String veraTaglia = trovaTaglia(numerata, counter);
//						Articoli articolo = new Articoli();
//						articolo.setIdUniArticolo(getIDUnivoco());
//						articolo.setCodArtStr(idUnivoco + "_" + counter);
//						articolo.setModello(Integer.toString(idUnivoco));
//						articolo.setBarraEAN(barcode);
//						articolo.setBarraUPC(barcode);
//						articolo.setCodBarre(barcode);
//						articolo.setCategoria(categoriaMerceologica);
//						articolo.setCatMercGruppo(categoriaMerceologica);
//						articolo.setCodArtOld(codice);
//						articolo.setColore(descrizioneColore);
//						articolo.setDescrizione(descrizione);
//						articolo.setDescAggiuntiva(descrizioneAlternativa);
//						articolo.setLinea(linea);
//						articolo.setStagione(stagione);
//						articolo.setTaglia(veraTaglia);
//						articolo.setUmPos(counter);
//						articolo.setUm(unitaMisura);
//						articolo.setQtaConf(pezziPerConfezione);
//						articolo.setPezziCassa(pezziCassa);
//						articolo.setCassa(cassa);
//						articolo.setTipoCassa(tipoCassa);
//						articoli.add(articolo);
//					}
//					indexBarcode += 30;
//					counter++;
//				}
//			}
//		} while (parser.prossimaLinea());
//		return articoli;
//	}
	
	public static String trovaTaglia(Numerata numerata, int counter) {
		String taglia;
		try {
			Method m = Numerata.class.getMethod("getTaglia" + counter);
			Object[] arguments = null;
			taglia = m.invoke(numerata, arguments).toString();
		} catch (Exception e) {
			taglia = "";
		}
		return taglia;
	}
	
	public static void trovaTutteNumerate() {
		List<Numerata> numerate = daoNumerate.trovaTutte();
		for (Numerata numerata : numerate) {
			mappaNumerate.put(numerata.getCodice(), numerata);
		}
	}
	
	public static Numerata trovaNumerata(String taglia) {
		if (!mappaNumerate.containsKey(taglia)) {
			Numerata numerata = daoNumerate.trovaDaCodice(taglia);
			mappaNumerate.put(taglia, numerata);
		}
		return mappaNumerate.get(taglia);
	}
	
	public static void trovaTuttiColori() {
		List<Colori> colori = daoColori.trovaTutti();
		for (Colori colore : colori) {
			mappaColori.put(colore.getCodice(), colore);
		}
	}
	
	public static Colori trovaColore(String codiceColore) {
		if (!mappaColori.containsKey(codiceColore)) {
			Colori colore = daoColori.trovaDaCodice(codiceColore);
			mappaColori.put(codiceColore, colore);
		}
		return mappaColori.get(codiceColore);
	}
	
//	protected static synchronized String getIDUnivoco() {
//		idUnivoco += 1;
//		String nuovoID = "181115" + utility.getFormattedString(idUnivoco, 9);
//
//		return nuovoID;
//	}
	
//	protected static synchronized String getIDUnivoco() {
//		Date now = new Date();
//		String chiave = idUnivocoGenerator.format(now);
//		//Mi fermo cinque millisecondi per evitare ID duplicati.
//		try { Thread.sleep(5); } catch (InterruptedException e) { logger.warn("Impossibile mandare in sleep il thread, possibile ID univoco articolo duplicato.");}
//		return chiave;
//	}
	
	protected static synchronized String getIDUnivoco() {
		LocalDateTime now = LocalDateTime.now();
		String chiave = now.format(formatter);
		//Mi fermo cinque millisecondi per evitare ID duplicati.
		try { Thread.sleep(5); } catch (InterruptedException e) { logger.warn("Impossibile mandare in sleep il thread, possibile ID univoco articolo duplicato.");}
		return chiave;
	}

}
