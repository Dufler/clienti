package it.ltc.ciesse.scambiodati.logic;

import java.io.File;
import java.util.Comparator;

/**
 * Classe che si occupa di ordinare correttamente i files da importare.<br>
 * (es. anagrafiche articoli prima dei carichi)
 * @author Damiano
 *
 */
public class Ordinatore implements Comparator<File> {
	
	private enum Nomi {
		
		TipiColli,
		UnitaMisura,
		ModalitaDiPagamento,
		ModalitaDiSpedizione,
		MagazziniContabili,
		Nazioni,
		Stagioni,
		ClasseTaglie,
		Colori,
		Articoli,
		Assortimenti,
		Barcode,
		Fornitori,
		Clienti,
		Vettori,
		DocumentiEntrata,
		RigheDocumentiEntrata,
		OrdiniClienti,
		RigheOrdiniClienti,
		Colli,
		DDTSpedizione,
		modelliErrati
		
	}

	@Override
	public int compare(File o1, File o2) {
		int compare;
		String fileName1 = o1 != null && o1.getName() != null ? o1.getName() : "";
		String fileName2 = o2 != null && o2.getName() != null ? o2.getName() : "";
		try {			
			String fileType1 = fileName1.split("_|\\d")[0];
			Nomi file1 = Nomi.valueOf(fileType1);
			String fileType2 = fileName2.split("_|\\d")[0];
			Nomi file2 = Nomi.valueOf(fileType2);
			compare = file1.compareTo(file2);
			if (compare == 0)
				compare = fileName1.compareTo(fileName2);
		} catch (Exception e) {
			compare = fileName1.compareTo(fileName2);
		}
		return compare;
	}

}
