package it.ltc.ciesse.scambiodati.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import it.ltc.database.model.legacy.TestaCorr;
import it.ltc.utility.miscellanea.string.StringUtility;

public class DataDDTSpedizione {
	
	//private static final Logger logger = Logger.getLogger(DataDDTSpedizione.class);
	
	public static final String PATTERN_DATA = "dd/MM/yyyy HH:mm:ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATA);	
	
	public static List<String> esportaDataSpedizione(List<TestaCorr> spedizioni, Date data) {
		StringUtility utility = new StringUtility();
		List<String> righeDocumento = new LinkedList<>();
		for (TestaCorr spedizione : spedizioni) {
			StringBuilder riga = new StringBuilder("2"); //L'operazione viene fissata a 2.
			riga.append(utility.getFormattedString(spedizione.getMittenteAlfa(), 20));
			riga.append(sdf.format(data));
			righeDocumento.add(riga.toString());
		}
		return righeDocumento;
	}

}
