package it.ltc.clienti.redone.importazione;

import java.io.File;
import java.util.Comparator;

/**
 * Classe che si occupa di ordinare correttamente i files da importare.<br>
 * (es. anagrafiche articoli prima dei carichi)
 * @author Damiano
 *
 */
public class Ordinatore implements Comparator<File> {

	@Override
	public int compare(File o1, File o2) {
		int compare;
		String fileName1 = o1 != null && o1.getName() != null ? o1.getName() : "";
		String fileName2 = o2 != null && o2.getName() != null ? o2.getName() : "";
		try {
			TipoFileImportazione file1 = TipoFileImportazione.trovaTipo(fileName1);
			TipoFileImportazione file2 = TipoFileImportazione.trovaTipo(fileName2);
			compare = file1.compareTo(file2);
			if (compare == 0)
				compare = fileName1.compareTo(fileName2);
		} catch (Exception e) {
			compare = fileName1.compareTo(fileName2);
		}
		return compare;
	}

}
