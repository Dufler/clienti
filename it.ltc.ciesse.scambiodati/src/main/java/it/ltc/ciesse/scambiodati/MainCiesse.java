package it.ltc.ciesse.scambiodati;

import it.ltc.ciesse.scambiodati.logic.Export;
import it.ltc.ciesse.scambiodati.logic.Import;

public class MainCiesse {

	public static void main(String[] args) {
		Import importatore = Import.getInstance();
		importatore.importaDati();
		Export esportatore = Export.getInstance();
		esportatore.esportaDati();
	}

}
