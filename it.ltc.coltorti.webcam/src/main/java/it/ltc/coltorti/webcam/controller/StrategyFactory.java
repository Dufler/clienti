package it.ltc.coltorti.webcam.controller;

import it.ltc.coltorti.webcam.Modalita;
import it.ltc.coltorti.webcam.controller.strategies.StrategiaBancale;
import it.ltc.coltorti.webcam.controller.strategies.StrategiaCollo;
import it.ltc.coltorti.webcam.controller.strategies.StrategiaEAN;
import it.ltc.coltorti.webcam.controller.strategies.StrategiaProdotto2;
import it.ltc.coltorti.webcam.controller.strategies.StrategiaProdotto3;
import it.ltc.coltorti.webcam.controller.strategies.StrategiaProdotto5;
import it.ltc.coltorti.webcam.view.MainFrame;

public class StrategyFactory {
	
	public static Strategy getStrategy(Modalita modalita, String lista, String oggetto, MainFrame frame) {
		Strategy strategy;
		switch (modalita) {
			case PRODOTTO2 : strategy = new StrategiaProdotto2(lista, oggetto, frame); break;
			case PRODOTTO3 : strategy = new StrategiaProdotto3(lista, oggetto, frame); break;
			case PRODOTTO5 : strategy = new StrategiaProdotto5(lista, oggetto, frame); break;
			case BANCALE : strategy = new StrategiaBancale(lista, oggetto, frame); break;
			case COLLO : strategy = new StrategiaCollo(lista, oggetto, frame); break;
			case ETICHETTA : strategy = new StrategiaEAN(lista, oggetto, frame); break;
			default : throw new RuntimeException("Nessuna strategia valida trovata!");
		}
		return strategy;
	}

}
