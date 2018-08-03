package it.ltc.ciesse.scambiodati.model;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.ColliImballoDao;
import it.ltc.database.model.legacy.ColliImballo;
import it.ltc.utility.miscellanea.string.StringUtility;

public class Colli {
	
	public static final String PATTERN_DATA = "dd/MM/yyyy HH:mm:ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATA);
	
	private static final ColliImballoDao daoColli = new ColliImballoDao(Import.persistenceUnit);
	
	public static List<String> esportaColli(String numeroLista) {
		StringUtility utility = new StringUtility();
		List<String> righe = new LinkedList<>();
		List<ColliImballo> colli = daoColli.trovaDaNumeroLista(numeroLista);
		for (ColliImballo collo : colli) {
			StringBuilder riga = new StringBuilder();
			riga.append("1"); //Lo fisso ad inserimento.
			riga.append(utility.getFormattedString(collo.getBarCodeImb(), 15));
			riga.append(utility.getFormattedString(collo.getCodFormato(), 4));
			riga.append(utility.getFormattedString("in preavviso", 50)); //ho fissato il valore
			riga.append(sdf.format(collo.getDataCrea()));
			riga.append(sdf.format(collo.getDataCrea()));
			double peso = collo.getPesoKg() != null ? collo.getPesoKg() : 0;
			riga.append(utility.getFormattedString((int) (peso * 1000), 10));
			riga.append(utility.getFormattedString("", 19)); //filler 01
			riga.append(utility.getFormattedString("", 20)); //non presente
			riga.append(utility.getFormattedString("", 50)); //filler 02
			riga.append(utility.getFormattedString(collo.getNrDocArrivo(), 20)); //TODO: Va cambiato il programma di imballo per fargli riportare il campo rifordcli di testataordini
		}
		return righe;
	}

}
