package it.ltc.ciesse.scambiodati.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.ProdottoOrdinato;
import it.ltc.utility.miscellanea.string.StringParser;
import it.ltc.utility.miscellanea.string.StringUtility;

public class OrdiniRighe {
	
	public static final String CANCELLAZIONE = "ELIMINA";
	
	public static String parsaRigheOrdine(HashMap<String, MOrdine> mappaOrdini, List<String> righe) {
		//ArticoliDao daoArticoli = new ArticoliDao(Import.persistenceUnit);
		String riferimento;
		MagazzinoDao daoMagazzini = new MagazzinoDao(ConfigurationUtility.getInstance().getPersistenceUnit());
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 991);
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione != 1)
				throw new RuntimeException("Operazione non consentita sulle righe d'ordine. (update/delete)");
			riferimento = parser.getStringa(1, 21);
			MOrdine ordine = mappaOrdini.get(riferimento);
			if (ordine == null) 
				throw new RuntimeException("Nessun ordine trovato con questo riferimento. (" + riferimento + ")");
			int numeroRiga = parser.getIntero(21, 31);
			String codificaMagazzino = parser.getStringa(140, 150);
			Magazzini magazzino = daoMagazzini.trovaDaCodificaCliente(codificaMagazzino);
			if (magazzino == null)
				throw new RuntimeException("Nessun magazzino trovato con questo riferimento. (" + codificaMagazzino + ")");
			String riferimentoCliente = parser.getStringa(216, 245);
			String diversificazione = parser.getStringa(245, 285);
			if (diversificazione != null && diversificazione.length() > 30) {
				diversificazione = diversificazione.substring(0, 30);
			}
			String codiceArticolo = parser.getStringa(1088, 1098); //Da verificare
			int indexTaglia = 285;
			int counter = 1;
			int lunghezzaCampo = 10;
			while (counter < 40) {
				int quantità = parser.getIntero(indexTaglia + (counter - 1) * lunghezzaCampo, indexTaglia + (counter) * lunghezzaCampo);
				if (quantità > 0) {
					String skuEffettivo = codiceArticolo + "_" + counter;
//					Articoli articolo = daoArticoli.trovaDaSKU(skuEffettivo);
//					if (articolo == null)
//						throw new RuntimeException("Nessun articolo corrispondente trovato. (" + skuEffettivo + ")");
					ProdottoOrdinato prodotto = new ProdottoOrdinato();
					prodotto.setChiave(skuEffettivo);
//					prodotto.setChiavelegacy(articolo.getIdUniArticolo());
					prodotto.setQuantita(quantità);
					prodotto.setMagazzinoCliente(magazzino.getMagaCliente());
					prodotto.setMagazzinoLTC(magazzino.getCodiceMag());
					prodotto.setNumeroRiga(numeroRiga);
					prodotto.setRiferimentoCliente(riferimentoCliente);
					prodotto.setNote(diversificazione);
					ordine.getProdotti().add(prodotto);
				}
				counter++;
			}
		} while (parser.prossimaLinea());
		return riferimento;
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
