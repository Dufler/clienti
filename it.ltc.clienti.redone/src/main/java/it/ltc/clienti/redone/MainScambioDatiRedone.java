package it.ltc.clienti.redone;

import it.ltc.clienti.redone.esportazione.Esportatore;
import it.ltc.clienti.redone.importazione.Importatore;

public class MainScambioDatiRedone {

	public static void main(String[] args) {
		//Importo i dati
		Importatore.getInstance().importa();
		//Esporto i dati
		Esportatore.getInstance().esporta();
	}

}
