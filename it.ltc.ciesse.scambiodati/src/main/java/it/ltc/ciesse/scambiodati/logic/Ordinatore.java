package it.ltc.ciesse.scambiodati.logic;

import java.io.File;
import java.util.Comparator;

public class Ordinatore implements Comparator<File> {
	
	private enum Nomi {
		Assortimenti,
		Barcode,
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
		Fornitori,
		Clienti,
		Vettori,
		DocumentiEntrata,
		RigheDocumentiEntrata,
		OrdiniClienti
	}

	@Override
	public int compare(File o1, File o2) {
		int compare;
		try {
			String fileName1 = o1.getName();
			String fileType1 = fileName1.split("_")[0];
			Nomi file1 = Nomi.valueOf(fileType1);
			String fileName2 = o2.getName();
			String fileType2 = fileName2.split("_")[0];
			Nomi file2 = Nomi.valueOf(fileType2);
			compare = file1.compareTo(file2);
		} catch (Exception e) {
			compare = 0;
		}
		return compare;
	}

}
