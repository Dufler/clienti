package it.ltc.ciesse.scambiodati.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.utility.miscellanea.string.StringParser;
import it.ltc.utility.miscellanea.string.StringUtility;

public class DocumentiEntrataRighe {
	
	public static final String CONDIZIONE_ELIMINA = "ELIMINA";
	
	private static final PakiTestaDao daoTestata = new PakiTestaDao(Import.persistenceUnit);
	private static final HashMap<String, PakiTesta> mappaTestate = new HashMap<>();
	
	private static final ArticoliDao daoArticoli = new ArticoliDao(Import.persistenceUnit);
	private static final HashMap<String, Articoli> mappaArticoli = new HashMap<>();
	
	public static List<PakiArticolo> parsaRigheDocumento(List<String> righe) {
		List<PakiArticolo> righeDocumento = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 1001);
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione == 1 || operazione == 2) {
				String riferimentoTestata = parser.getStringa(1, 21);
				PakiTesta testata = trovaTestata(riferimentoTestata);
				if (testata == null) 
					throw new RuntimeException("La testata non esiste, riferimento: '" + riferimentoTestata + "'");
				int numeroRiga = parser.getIntero(21, 31);
				String modello = parser.getStringa(31, 82);
				String sku = parser.getStringa(990, 1000);
				String magazzino = parser.getStringa(82, 92);
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
						riga.setIdPakiTesta(testata.getIdTestaPaki());
						riga.setMagazzino(magazzino);
						riga.setMagazzinoltc("PG1"); //Metto il default.
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
			} else if (operazione == 3) {
				String riferimentoTestata = parser.getStringa(1, 21);
				PakiTesta testata = trovaTestata(riferimentoTestata);
				if (testata == null) 
					throw new RuntimeException("La testata non esiste, riferimento: '" + riferimentoTestata + "'");
				int numeroRiga = parser.getIntero(21, 31);
				PakiArticolo riga = new PakiArticolo();
				riga.setIdPakiTesta(testata.getIdTestaPaki());
				riga.setRigaPacki(numeroRiga);
				riga.setNrDispo(CONDIZIONE_ELIMINA);
				righeDocumento.add(riga);
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
			int index = Integer.parseInt(datiArticolo[1]);
			if (!mappaListe.containsKey(idUnivoco)) {
				mappaListe.put(idUnivoco, new PakiArticolo[40]);
			}
			mappaListe.get(idUnivoco)[index] = riga;
		}
		//Per ogni sotto lista faccio una riga.
		for (String idUnivoco : mappaListe.keySet()) {
			PakiArticolo[] array = mappaListe.get(idUnivoco);
			//Compongo la stringa di 4 parti: 1 - dati generali, 2 dichiarato, 3 riscontrato, 4 id univoco
			StringBuilder datiGenarali = new StringBuilder();
			StringBuilder dichiarato = new StringBuilder();
			StringBuilder riscontrato = new StringBuilder();
			//1 - dati generali, trovo il primo pakiarticolo non null e li prendo da li.
			for (PakiArticolo riga : array) {
				if (riga != null) {
					datiGenarali.append("2"); //Fisso l'operazione a update
					datiGenarali.append(utility.getFormattedString(riga.getNrOrdineFor(), 20));
					datiGenarali.append(utility.getFormattedString(Integer.toString(riga.getRigaPacki()), 10));
					datiGenarali.append(utility.getFormattedString(riga.getBarcodeCollo(), 50)); //E' salvato in questo campo.
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
			String rigaDocumento = datiGenarali.toString() + dichiarato.toString() + riscontrato.toString() + idUnivoco;
			righeDocumento.add(rigaDocumento.toString());
		}
		return righeDocumento;
	}

}
