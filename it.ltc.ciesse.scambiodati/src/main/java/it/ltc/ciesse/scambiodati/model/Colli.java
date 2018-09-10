package it.ltc.ciesse.scambiodati.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.ColliImballoDao;
import it.ltc.database.model.legacy.ColliImballo;
import it.ltc.database.model.legacy.ColliPreleva;
import it.ltc.utility.miscellanea.string.StringParser;
import it.ltc.utility.miscellanea.string.StringUtility;

public class Colli {
	
	public static final String PATTERN_DATA = "dd/MM/yyyy HH:mm:ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATA);
	
	private static final ColliImballoDao daoColli = new ColliImballoDao(Import.persistenceUnit);
	
	public static List<String> esportaColli(List<ColliImballo> colli, String riferimentoOrdine) {
		StringUtility utility = new StringUtility();
		List<String> righe = new LinkedList<>();
		//List<ColliImballo> colli = daoColli.trovaDaNumeroLista(numeroLista);
		for (ColliImballo collo : colli) {
			StringBuilder riga = new StringBuilder();
			riga.append("1"); //Lo fisso ad inserimento.
			riga.append(utility.getFormattedString(collo.getKeyColloSpe(), 15));
			riga.append(utility.getFormattedString(collo.getCodFormato(), 4));
			riga.append(utility.getFormattedString("in preavviso", 50)); //ho fissato il valore
			riga.append(sdf.format(collo.getDataCrea()));
			riga.append(sdf.format(collo.getDataCrea()));
			double peso = collo.getPesoKg() != null ? collo.getPesoKg() : 0;
			riga.append(utility.getFormattedString((int) (peso * 1000), 10));
			riga.append(utility.getFormattedString("", 19)); //filler 01
			riga.append(utility.getFormattedString("", 20)); //non presente
			riga.append(utility.getFormattedString("", 50)); //filler 02
			riga.append(utility.getFormattedString(riferimentoOrdine, 20));
			righe.add(riga.toString());
		}
		return righe;
	}
	
	public static List<ColliPreleva> importaColli(List<String> righe) {
		List<ColliPreleva> colli = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 228, PATTERN_DATA, PATTERN_DATA);
		do {
			String numeroCollo = parser.getStringa(1, 16);
			ColliImballo colloImballato = daoColli.trovaDaNumeroCollo(numeroCollo);
			if (colloImballato == null)
				throw new RuntimeException("Nessun collo trovato con n° '" + numeroCollo + "'");
			Date dataDocumento = parser.getDataSoloGiorno(70, 89);
			if (dataDocumento == null)
				dataDocumento = new Date();
			String vettore = parser.getStringa(138, 158);
			if (vettore.length() > 25)
				vettore = vettore.substring(0, 25);
			String ddt = parser.getStringa(208, 228);
			ColliPreleva collo = new ColliPreleva();
			collo.setBarcodeCorriere(colloImballato.getBarCodeImb());
			collo.setCodiceCorriere(vettore);
			collo.setDataDistinta(new Timestamp(dataDocumento.getTime()));
			collo.setKeyColloPre(numeroCollo);
			collo.setNrColloCliente(0);
			collo.setNrLista(colloImballato.getNrLista());
			collo.setPoNumber(ddt);
			collo.setVet1(vettore);
			colli.add(collo);
		} while (parser.prossimaLinea());
		return colli;
	}

}