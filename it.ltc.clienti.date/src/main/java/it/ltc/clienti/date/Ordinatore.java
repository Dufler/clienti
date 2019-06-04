package it.ltc.clienti.date;

import java.io.File;
import java.util.Comparator;

public class Ordinatore implements Comparator<File> {

	@Override
	public int compare(File o1, File o2) {
//		if (o1.getName().endsWith("xls"))
		return 0;
	}

}
