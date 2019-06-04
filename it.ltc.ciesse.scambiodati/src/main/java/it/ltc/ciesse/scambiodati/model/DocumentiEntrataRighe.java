package it.ltc.ciesse.scambiodati.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
import it.ltc.ciesse.scambiodati.logic.exception.CaricoNonAncoraInseritoException;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.utility.miscellanea.string.StringParser;
import it.ltc.utility.miscellanea.string.StringUtility;

public class DocumentiEntrataRighe {
	
	public static final String CONDIZIONE_ELIMINA = "ELIMINA";
	
	private static final PakiTestaDao daoTestata = new PakiTestaDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	private static final HashMap<String, PakiTesta> mappaTestate = new HashMap<>();
	
	private static final ArticoliDao daoArticoli = new ArticoliDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	private static final HashMap<String, Articoli> mappaArticoli = new HashMap<>();
	
	private static final MagazzinoDao daoMagazzini = new MagazzinoDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	
	private static final PakiArticoloDao daoRighe = new PakiArticoloDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	
	public static List<PakiArticolo> parsaRigheDocumento(List<String> righe) {
		List<PakiArticolo> righeDocumento = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 1001);
		//Eseguo dei controlli preliminari: 1) esiste la testata dichiarata, 2) non ci sono già altre righe inserite.
		String riferimentoTestata = parser.getStringa(1, 21);
		PakiTesta testata = trovaTestata(riferimentoTestata);
		if (testata == null) 
			throw new CaricoNonAncoraInseritoException("La testata non esiste, riferimento: '" + riferimentoTestata + "'");
		List<PakiArticolo> righeGiaPresenti = daoRighe.trovaRigheDaCarico(testata.getIdTestaPaki());
		if (!righeGiaPresenti.isEmpty()) {
			throw new RuntimeException("Le righe sono già state inserite per il carico indicato, riferimento: '" + riferimentoTestata + "'");
		}
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione == 1 || operazione == 2) {	
				int numeroRiga = parser.getIntero(21, 31);
				String modello = parser.getStringa(31, 83);
				String sku = parser.getStringa(989, 1000);
				String codiceMagazzino = parser.getStringa(83, 92);
				//Condizione assurda perchè sono degli incapaci
				if (codiceMagazzino.equals("E"))
					codiceMagazzino = "E_LTC";
				Magazzini magazzino = daoMagazzini.trovaDaCodificaCliente(codiceMagazzino);
				if (magazzino == null) 
					throw new RuntimeException("Il magazzino indicato non esiste. (Magazzino errato: " + codiceMagazzino + ", Riferimento carico: '" + riferimentoTestata + "'");
				String stagione = parser.getStringa(91, 141);
				int indexQta = 183;
				int counter = 1;
				while (indexQta < 582) {
					int qta = parser.getIntero(indexQta, indexQta + 10);
					if (qta > 0) {
						String skuCompleto = sku + "_" + counter;
						Articoli articolo = trovaArticolo(skuCompleto);
						if (articolo == null) 
							throw new RuntimeException("L'articolo non esiste, SKU: '" + skuCompleto + "'");
						PakiArticolo riga = new PakiArticolo();
						riga.setBarcodeCollo(modello); //Uso questo campo per mettere il modello
						riga.setCodArtStr(articolo.getCodArtStr());
						riga.setCodBarre(articolo.getCodBarre());
						riga.setCodUnicoArt(articolo.getIdUniArticolo());
						riga.setIdArticolo(articolo.getIdArticolo());
						riga.setIdPakiTesta(testata.getIdTestaPaki());
						riga.setMagazzino(magazzino.getMagaCliente());
						riga.setMagazzinoltc(magazzino.getCodiceMag());
						riga.setStagcarico(stagione.length() > 10 ? stagione.substring(0, 10) : stagione); //Prendo solo i primi 10 caratteri.
						riga.setNrDispo("");
						riga.setNrOrdineFor(testata.getNrPaki());
						riga.setQtaPaki(qta);
						riga.setRigaPacki(numeroRiga);
						righeDocumento.add(riga);
					}
					indexQta += 10;
					counter++;
				}
			}
		} while (parser.prossimaLinea());
		return righeDocumento;
	}
	
	public static PakiTesta trovaTestata(String riferimentoTestata) {
		if (!mappaTestate.containsKey(riferimentoTestata)) {
			PakiTesta testata = daoTestata.trovaDaRiferimento(riferimentoTestata);
			mappaTestate.put(riferimentoTestata, testata);
		}
		return mappaTestate.get(riferimentoTestata);
	}
	
	public static Articoli trovaArticolo(String sku) {
		if (!mappaArticoli.containsKey(sku)) {
			Articoli articolo = daoArticoli.trovaDaSKU(sku);
			mappaArticoli.put(sku, articolo);
		}
		return mappaArticoli.get(sku);
	}
	
	public static List<String> esportaRigheDocumento(List<PakiArticolo> righe) {
		StringUtility utility = new StringUtility();
		List<String> righeDocumento = new LinkedList<>();
		//Separo la lista in sottoliste con articoli dello stesso modello
		HashMap<String, PakiArticolo[]> mappaListe = new HashMap<>(); //meglio array!
		for (PakiArticolo riga : righe) {
			String[] datiArticolo = riga.getCodArtStr().split("_");
			String idUnivoco = datiArticolo[0];
			int index = Integer.parseInt(datiArticolo[1]) - 1;
			String key = idUnivoco + "§" + riga.getMagazzinoltc() + "§" + riga.getRigaPacki();
			if (!mappaListe.containsKey(key)) {
				mappaListe.put(key, new PakiArticolo[40]);
			}
			mappaListe.get(key)[index] = riga;
		}
		//Per ogni sotto lista faccio una riga.
		for (String key : mappaListe.keySet()) {
			String[] dati = key.split("§");
			String idUnivoco = dati[0];
			PakiArticolo[] array = mappaListe.get(key);
			//Compongo la stringa di 4 parti: 1 - dati generali, 2 dichiarato, 3 riscontrato, 4 id univoco
			StringBuilder datiGenarali = new StringBuilder();
			StringBuilder dichiarato = new StringBuilder();
			StringBuilder riscontrato = new StringBuilder();
			//1 - dati generali, trovo il primo pakiarticolo non null e li prendo da li.
			for (PakiArticolo riga : array) {
				if (riga != null) {
					//Recupero l'articolo per otterenere il codartold
					Articoli prodotto = trovaArticolo(riga.getCodArtStr());
					if (prodotto == null) throw new RuntimeException("Nessun articolo trovato per il codice articolo " + riga.getCodArtStr() + " nella riga di carico ID " + riga.getIdPakiArticolo());
					datiGenarali.append("2"); //Fisso l'operazione a update
					datiGenarali.append(utility.getFormattedString(riga.getNrOrdineFor(), 20));
					datiGenarali.append(utility.getFormattedString(Integer.toString(riga.getRigaPacki()), 10));
					//datiGenarali.append(utility.getFormattedString(riga.getBarcodeCollo(), 52)); //E' salvato in questo campo.
					datiGenarali.append(utility.getFormattedString(prodotto.getCodArtOld(), 52)); //Lo prendo da qui perchè se fanno righe da inserimento non ce lo mettono.
					datiGenarali.append(utility.getFormattedString(riga.getMagazzino(), 10));
					datiGenarali.append(utility.getFormattedString(riga.getStagcarico(), 50));
					datiGenarali.append(utility.getFormattedString("", 10)); //bagno - non mappato e non richiesto
					datiGenarali.append(utility.getFormattedString("", 10)); //etichetta - non mappato e non richiesto
					datiGenarali.append(utility.getFormattedString("", 20)); //codice ordine fornitore - non mappato e non richiesto
					break; //Mi basta la prima per i dati generali, esco dal ciclo.
				}				
			}
			//2 e 3 - Quantità dichiarate e riscontrate
			for (PakiArticolo riga : array) {
				int dichiatata = riga != null ? riga.getQtaPaki() : 0;
				dichiarato.append(utility.getFormattedString(dichiatata, 10));
				int riscontrata = riga != null ? riga.getQtaVerificata() : 0;
				riscontrato.append(utility.getFormattedString(riscontrata, 10));
			}
			//Assemblo la riga e la aggiungo al documento
			StringBuilder finale = new StringBuilder();
			finale.append(datiGenarali.toString());
			finale.append(dichiarato.toString());
			finale.append(riscontrato.toString());
			finale.append(utility.getFormattedString(idUnivoco, 10, " ", true));
			//Aggiungo spazio in fondo se necessario
			while (finale.length() < 1001) {
				finale.append(" ");
			}
			righeDocumento.add(finale.toString());
		}
		return righeDocumento;
	}

}
