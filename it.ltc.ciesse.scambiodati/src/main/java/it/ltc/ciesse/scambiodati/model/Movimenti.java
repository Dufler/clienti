package it.ltc.ciesse.scambiodati.model;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.MagaMov;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.utility.miscellanea.string.StringUtility;

public class Movimenti {
	
	public static final String PATTERN_DATA = "dd/MM/yyyy HH:mm:ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATA);
	
	private static final ArticoliDao daoArticoli = new ArticoliDao(Import.persistenceUnit);
	private static final HashMap<String, Articoli> mappaArticoli = new HashMap<>();
	
	private static final MagazzinoDao daoMagazzini = new MagazzinoDao(Import.persistenceUnit);
	private static final HashMap<String, Magazzini> mappaMagazzini = new HashMap<>();

	public static List<String> esportaMovimenti(List<MagaMov> movimenti) {
		StringUtility utility = new StringUtility();
		List<String> righeDocumento = new LinkedList<>();
		for (MagaMov movimento : movimenti) {
			//Recupero l'articolo e il magazzino
			Articoli articolo = trovaArticolo(movimento.getIdUniArticolo());
			if (articolo == null)
				throw new RuntimeException("Impossibile trovare l'articolo con ID univoco: '" + movimento.getIdUniArticolo() + "'");
			Magazzini magazzino = trovaMagazzino(movimento.getCodMaga());
			if (magazzino == null)
				throw new RuntimeException("Impossibile trovare il magazzino con codice: '" + movimento.getCodMaga() + "'");
			StringBuilder sb = new StringBuilder();
			sb.append(utility.getFormattedString(articolo.getCodArtOld(), 52));
			sb.append(utility.getFormattedString(magazzino.getMagaCliente(), 10));
			sb.append(utility.getFormattedString("", 50)); //codice stagione, che non ho.
			sb.append(utility.getFormattedString("", 10)); //bagno, non usato.
			sb.append(utility.getFormattedString("", 10)); //etichetta, non usato.
			sb.append(utility.getFormattedString(movimento.getCausale(), 10)); //Forse la causale va trascodificata.
			//aggiungo taglie vuote, inserisco una quantità solo per la sua.
			for (int index = 1; index < 41; index++) {
				int quantità = index == articolo.getUmPos() ? movimento.getQuantita() : 0;
				sb.append(utility.getFormattedString(quantità, 10));
			}
			sb.append(utility.getFormattedString(movimento.getDocNr(), 20)); //Numero documento
			sb.append(sdf.format(movimento.getDataMovMag())); //Data documento
			sb.append(sdf.format(movimento.getDataMovMag())); //Data conferma, che non ho.
			sb.append(utility.getFormattedString(movimento.getIdMagaMov(), 10)); //ID_Riga, che non ho.
			sb.append(utility.getFormattedString(articolo.getModello(), 10)); //ID articolo Ivy oxford
			righeDocumento.add(sb.toString());
		}
		return righeDocumento;
	}
	
	public static Articoli trovaArticolo(String idUnivoco) {
		if (!mappaArticoli.containsKey(idUnivoco)) {
			Articoli articolo = daoArticoli.trovaDaIDUnivoco(idUnivoco);
			mappaArticoli.put(idUnivoco, articolo);
		}
		return mappaArticoli.get(idUnivoco);
	}
	
	public static Magazzini trovaMagazzino(String codiceMagazzino) {
		if (!mappaMagazzini.containsKey(codiceMagazzino)) {
			Magazzini magazzino = daoMagazzini.trovaDaCodiceLTC(codiceMagazzino);
			mappaMagazzini.put(codiceMagazzino, magazzino);
		}
		return mappaMagazzini.get(codiceMagazzino);
	}
}
