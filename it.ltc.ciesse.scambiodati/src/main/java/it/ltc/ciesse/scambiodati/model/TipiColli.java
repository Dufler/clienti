package it.ltc.ciesse.scambiodati.model;

import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.ImballoDao;
import it.ltc.database.model.legacy.Imballi;
import it.ltc.utility.miscellanea.string.StringUtility;

public class TipiColli {
	
	private static final ImballoDao daoImballi = new ImballoDao(Import.persistenceUnit);
	
	public static List<String> esportaTipiColli() {
		StringUtility utility = new StringUtility();
		List<String> righe = new LinkedList<>();
		List<Imballi> entities = daoImballi.trovaTutti();
		for (Imballi formato : entities) {
			StringBuilder riga = new StringBuilder();
			riga.append(utility.getFormattedString(formato.getCodImballo(), 4));
			riga.append(utility.getFormattedString(formato.getDescrizione(), 20));
			riga.append(utility.getFormattedString((int) (formato.getH() * 10), 10));
			riga.append(utility.getFormattedString((int) (formato.getL() * 10), 10));
			riga.append(utility.getFormattedString((int) (formato.getZ() * 10), 10));
			riga.append(utility.getFormattedString((int) (formato.getVolume() * 1000), 10));
			riga.append(utility.getFormattedString("", 10));
			riga.append(utility.getFormattedString(formato.getPesoGr(), 10));
			righe.add(riga.toString());
		}
		return righe;
	}

}
