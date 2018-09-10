package it.ltc.ciesse.scambiodati.model;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.ColoriDao;
import it.ltc.database.dao.legacy.NumerateDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.Colori;
import it.ltc.database.model.legacy.Numerata;
import it.ltc.utility.miscellanea.string.StringParser;

public class Articolo {
	
	private static final Logger logger = Logger.getLogger(Articolo.class);
	
	private static final SimpleDateFormat idUnivocoGenerator = new SimpleDateFormat("yyMMddHHmmssSSS");
	
	private static HashMap<String, Numerata> mappaNumerate = new HashMap<>();
	private static NumerateDao daoNumerate = new NumerateDao(Import.persistenceUnit);
	
	private static HashMap<String, Colori> mappaColori = new HashMap<>();
	private static ColoriDao daoColori = new ColoriDao(Import.persistenceUnit);
	
	public static List<Articoli> parsaArticoli(List<String> righe) {
		List<Articoli> articoli = new LinkedList<>();
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
				int pezziPerConfezione = 1; //parser.getIntero(267, 277); l'ho tolto perchè l'imballo non lo gestisce bene.
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
						Articoli articolo = new Articoli();
						articolo.setIdUniArticolo(getIDUnivoco());
						articolo.setCodArtStr(idUnivoco + "_" + counter);
						articolo.setModello(Integer.toString(idUnivoco));
						articolo.setBarraEAN(barcode);
						articolo.setBarraUPC(barcode);
						articolo.setCodBarre(barcode);
						articolo.setCategoria(categoriaMerceologica);
						articolo.setCatMercGruppo(categoriaMerceologica);
						articolo.setCodArtOld(codice);
						articolo.setColore(descrizioneColore);
						articolo.setDescrizione(descrizione);
						articolo.setDescAggiuntiva(descrizioneAlternativa);
						articolo.setLinea(linea);
						articolo.setStagione(stagione);
						articolo.setTaglia(veraTaglia);
						articolo.setUmPos(counter);
						articolo.setUm(unitaMisura);
						articolo.setQtaConf(pezziPerConfezione);
						articoli.add(articolo);
					}
					indexBarcode += 30;
					counter++;
				}
			}
		} while (parser.prossimaLinea());
		return articoli;
	}
	
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
	
	public static Numerata trovaNumerata(String taglia) {
		if (!mappaNumerate.containsKey(taglia)) {
			Numerata numerata = daoNumerate.trovaDaCodice(taglia);
			mappaNumerate.put(taglia, numerata);
		}
		return mappaNumerate.get(taglia);
	}
	
	public static Colori trovaColore(String codiceColore) {
		if (!mappaColori.containsKey(codiceColore)) {
			Colori colore = daoColori.trovaDaCodice(codiceColore);
			mappaColori.put(codiceColore, colore);
		}
		return mappaColori.get(codiceColore);
	}
	
	protected static synchronized String getIDUnivoco() {
		Date now = new Date();
		String chiave = idUnivocoGenerator.format(now);
		//Mi fermo un millisecondo per evitare ID duplicati.
		try { Thread.sleep(1); } catch (InterruptedException e) {}
		return chiave;
	}

}
