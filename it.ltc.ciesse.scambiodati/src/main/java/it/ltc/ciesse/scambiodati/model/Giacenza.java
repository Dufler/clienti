package it.ltc.ciesse.scambiodati.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.utility.miscellanea.string.StringUtility;

public class Giacenza {
	
	private static final ArticoliDao daoArticoli = new ArticoliDao(Import.persistenceUnit);
	private static final HashMap<String, Articoli> mappaArticoliPerIDUnivoco = new HashMap<>();
	private static final HashMap<String, Articoli> mappaArticoliPerSKU = new HashMap<>();
	
	private static final MagazzinoDao daoMagazzini = new MagazzinoDao(Import.persistenceUnit);
	private static final HashMap<String, Magazzini> mappaMagazzini = new HashMap<>();
	
	public static List<String> esportaSaldi(List<MagaSd> saldi) {
		StringUtility utility = new StringUtility();
		List<String> righeDocumento = new LinkedList<>();
		//accorpo gli articoli dello stesso modello
		HashMap<String, int[]> mappaQuantità = new HashMap<>();
		for (MagaSd saldo : saldi) {
			Articoli articolo = trovaArticolo(saldo.getIdUniArticolo());
			String magazzino = saldo.getCodMaga();
			String key = magazzino + "§" + articolo.getModello() + "§" + articolo.getCodArtOld();
			if (!mappaQuantità.containsKey(key)) {
				mappaQuantità.put(key, new int[41]);
			}
			int[] quantità = mappaQuantità.get(key);
			quantità[articolo.getUmPos()] += saldo.getDisponibile();
		}
		for (String key : mappaQuantità.keySet()) {
			//recupero tutte le info necessarie
			String[] datiArticolo = key.split("§");
			String magazzino = datiArticolo[0];
			String modello = datiArticolo[1];
			String codiceArticolo = datiArticolo[2];
			Magazzini m = trovaMagazzino(magazzino);
			if (m == null)
				throw new RuntimeException("Impossibile trovare il magazzino: '" + magazzino + "'");
			int[] quantità = mappaQuantità.get(key);
			StringBuilder sb = new StringBuilder();
			sb.append(utility.getFormattedString(codiceArticolo, 52));
			sb.append(utility.getFormattedString(m.getMagaCliente(), 10));
			sb.append(utility.getFormattedString("", 50)); //stagione, che non ho.
			sb.append(utility.getFormattedString("", 10)); //bagno, che non ho.
			sb.append(utility.getFormattedString("", 10)); //etichetta, che non ho.
			for (int index = 1; index < quantità.length; index++) {
				sb.append(utility.getFormattedString(quantità[index], 10));
			}
			sb.append(utility.getFormattedString(modello, 10));
			righeDocumento.add(sb.toString());
		}
		return righeDocumento;
	}
	
	public static Articoli trovaArticolo(String idUnivoco) {
		if (!mappaArticoliPerIDUnivoco.containsKey(idUnivoco)) {
			Articoli articolo = daoArticoli.trovaDaIDUnivoco(idUnivoco);
			mappaArticoliPerIDUnivoco.put(idUnivoco, articolo);
			mappaArticoliPerSKU.put(articolo.getCodArtStr(), articolo);
		}
		return mappaArticoliPerIDUnivoco.get(idUnivoco);
	}
	
	public static Magazzini trovaMagazzino(String codiceMagazzino) {
		if (!mappaMagazzini.containsKey(codiceMagazzino)) {
			Magazzini magazzino = daoMagazzini.trovaDaCodiceLTC(codiceMagazzino);
			mappaMagazzini.put(codiceMagazzino, magazzino);
		}
		return mappaMagazzini.get(codiceMagazzino);
	}

}
