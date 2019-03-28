package it.ltc.clienti.redone;

import org.apache.log4j.Logger;

import it.ltc.clienti.redone.esportazione.Esportatore;
import it.ltc.clienti.redone.importazione.Importatore;

public class MainScambioDatiRedone {
	
	private static final Logger logger = Logger.getLogger(MainScambioDatiRedone.class);

	public static void main(String[] args) {
		logger.info("Avvio lo scambio dati.");
		//Importo i dati
		Importatore.getInstance().importa();
		//Esporto i dati
		Esportatore.getInstance().esporta();
	}

}
