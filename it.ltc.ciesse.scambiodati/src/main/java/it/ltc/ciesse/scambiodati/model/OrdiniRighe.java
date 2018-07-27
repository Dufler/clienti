package it.ltc.ciesse.scambiodati.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.utility.miscellanea.string.StringParser;
import it.ltc.utility.miscellanea.string.StringUtility;

public class OrdiniRighe {
	
	public static final String CANCELLAZIONE = "ELIMINA";
	
	public static List<RighiOrdine> parsaRigheOrdine(List<String> righe) {
		TestataOrdiniDao daoTestate = new TestataOrdiniDao(Import.persistenceUnit);
		ArticoliDao daoArticoli = new ArticoliDao(Import.persistenceUnit);
		List<RighiOrdine> righeOrdine = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 615);
		do {
			int operazione = parser.getIntero(0, 1);
			String riferimento = parser.getStringa(1, 21);
			TestataOrdini testata = daoTestate.trovaDaRiferimento(riferimento);
			if (testata == null)
				throw new RuntimeException("Nessun ordine trovato con questo riferimento. (" + riferimento + ")");
			int numeroRiga = parser.getIntero(21, 31);
			String magazzino = parser.getStringa(133, 143);
			//String stagione = parser.getStringa(143, 193); //Non mappato
			String riferimentoCliente = parser.getStringa(213, 243);
			String diversificazione = parser.getStringa(243, 283);
			if (diversificazione != null && diversificazione.length() > 30) {
				diversificazione = diversificazione.substring(0, 30);
			}
			String codiceArticolo = parser.getStringa(1044, 1054); //Da verificare
			int indexTaglia = 283;
			int counter = 1;
			int lunghezzaCampo = 10;
			while (indexTaglia < indexTaglia + lunghezzaCampo * 40) {
				if (operazione == 1 || operazione == 2) {
					int quantità = parser.getIntero(indexTaglia + (counter - 1) * lunghezzaCampo, indexTaglia + (counter) * lunghezzaCampo);
					String skuEffettivo = codiceArticolo + "_" + counter;
					Articoli articolo = daoArticoli.trovaDaSKU(skuEffettivo);
					if (articolo == null)
						throw new RuntimeException("Nessun articolo corrispondente trovato. (" + skuEffettivo + ")");
					RighiOrdine riga = new RighiOrdine();
					riga.setBarraEAN(articolo.getBarraEAN());
					riga.setBarraUPC(articolo.getBarraUPC());
					riga.setCodBarre(articolo.getCodBarre());
					riga.setCodiceArticolo(articolo.getCodArtStr());
					riga.setColore(articolo.getColore());
					riga.setDataOrdine(testata.getDataOrdine());
					riga.setDescrizione(articolo.getDescrizione());
					riga.setIdDestina(testata.getIdDestina());
					riga.setIdTestataOrdine(testata.getIdTestaCorr());
					riga.setIdUnicoArt(articolo.getIdUniArticolo());
					riga.setMagazzino(magazzino);
					riga.setPONumber(riferimentoCliente);
					riga.setNoteCliente(diversificazione);
					riga.setNrLista(testata.getNrLista());
					riga.setNrOrdine(testata.getNrOrdine());
					riga.setNrRigo(numeroRiga);
					riga.setQtaSpedizione(quantità);
					riga.setTaglia(articolo.getTaglia());
					riga.setTipoord(testata.getTipoOrdine());
					righeOrdine.add(riga);
				} else if (operazione == 3) {
					RighiOrdine riga = new RighiOrdine();
					riga.setNrLista(testata.getNrLista());
					riga.setNrRigo(numeroRiga);
					riga.setTipoord(CANCELLAZIONE);
					righeOrdine.add(riga);
				}
				counter++;
			}
		} while (parser.prossimaLinea());
		return righeOrdine;
	}
	
	public static List<String> esportaRigheOrdine(List<RighiOrdine> righeOrdine) {
		StringUtility utility = new StringUtility(" ", " ", false, false);
		List<String> righe = new LinkedList<>();
		//Creo un'insieme d'array con lo stesso modello
		HashMap<String, RighiOrdine[]> mappaListe = new HashMap<>(); //meglio array!
		for (RighiOrdine riga : righeOrdine) {
			String[] datiArticolo = riga.getIdUnicoArt().split("_");
			String idUnivoco = datiArticolo[0];
			int index = Integer.parseInt(datiArticolo[1]);
			if (!mappaListe.containsKey(idUnivoco)) {
				mappaListe.put(idUnivoco, new RighiOrdine[40]);
			}
			mappaListe.get(idUnivoco)[index] = riga;
		}		
		for (String idUnivoco : mappaListe.keySet()) {
			RighiOrdine[] array = mappaListe.get(idUnivoco);
			StringBuilder riga = new StringBuilder();
			StringBuilder quantitàRichieste = new StringBuilder();
			StringBuilder quantitàSpedite = new StringBuilder();
			for (RighiOrdine rigaOrdine : array) {
				if (rigaOrdine != null) {
					riga.append("2"); //La fisso a 2
					riga.append(utility.getFormattedString(rigaOrdine.getNrOrdine(), 20));
					riga.append(utility.getFormattedString(rigaOrdine.getNrRigo(), 10));
					riga.append(utility.getFormattedString("", 2)); //Filler 01
					riga.append(utility.getFormattedString("", 50)); //Commento - non mappato
					riga.append(utility.getFormattedString(rigaOrdine.getCodiceArticolo(), 50));
					riga.append(utility.getFormattedString(rigaOrdine.getMagazzino(), 10));
					riga.append(utility.getFormattedString("", 50)); //Stagione - non mappato
					riga.append(utility.getFormattedString("", 10)); //Bagno - non mappato
					riga.append(utility.getFormattedString("", 10)); //Etichetta - non mappato
					riga.append(utility.getFormattedString(rigaOrdine.getPONumber(), 30)); //riferimento del cliente finale da stampare sull'etichetta
					riga.append(utility.getFormattedString(rigaOrdine.getNoteCliente(), 40)); //Diversificazione
					//Esco dal for, mi basta sola la prima
				}
			}
			for (RighiOrdine rigaOrdine : array) {
				int quantitàRichiesta = rigaOrdine != null ? rigaOrdine.getQtaSpedizione() : 0;
				quantitàRichieste.append(utility.getFormattedString(quantitàRichiesta, 10));
				int quantitàSpedita = rigaOrdine != null ? rigaOrdine.getQtaImballata() : 0;
				quantitàSpedite.append(utility.getFormattedString(quantitàSpedita, 10));
			}
			righe.add(riga.toString() + quantitàRichieste.toString() + quantitàSpedite.toString() + idUnivoco);
		}
		return righe;
	}

}
