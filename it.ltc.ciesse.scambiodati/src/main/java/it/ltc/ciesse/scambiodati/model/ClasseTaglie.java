package it.ltc.ciesse.scambiodati.model;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.database.model.legacy.NumerataLegacy;
import it.ltc.utility.miscellanea.string.StringParser;

public class ClasseTaglie {
	
	private static final Logger logger = Logger.getLogger(ClasseTaglie.class);
	
	public static List<NumerataLegacy> parsaTaglie(List<String> righe) {
		List<NumerataLegacy> numerate = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 281);
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione == 1 || operazione == 2) {
				NumerataLegacy entity = new NumerataLegacy();
				entity.setCodice(parser.getStringa(1, 31));
				int indexTaglia = 81;
				int counter = 1;
				while (indexTaglia < 281) {
					String taglia = parser.getStringa(indexTaglia, indexTaglia + 5);
					setTaglia(entity, counter, taglia);
					indexTaglia += 5;
					counter++;
				}
				numerate.add(entity);
			}
		} while (parser.prossimaLinea());
		return numerate;
	}
	
	public static void setTaglia(NumerataLegacy numerata, int counter, String taglia) {
		try {
			Method m = NumerataLegacy.class.getMethod("setTaglia" + counter, String.class);
			Object[] arguments = new Object[1];
			arguments[0] = taglia;
			m.invoke(numerata, arguments);
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
