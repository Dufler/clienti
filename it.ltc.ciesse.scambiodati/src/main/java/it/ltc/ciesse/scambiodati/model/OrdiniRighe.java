package it.ltc.ciesse.scambiodati.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.ProdottoOrdinato;
import it.ltc.utility.miscellanea.string.StringParser;
import it.ltc.utility.miscellanea.string.StringUtility;

public class OrdiniRighe {
	
	public static final String CANCELLAZIONE = "ELIMINA";
	
	public static void parsaRigheOrdine(HashMap<String, MOrdine> mappaOrdini, List<String> righe) {
		//ArticoliDao daoArticoli = new ArticoliDao(Import.persistenceUnit);
		MagazzinoDao daoMagazzini = new MagazzinoDao(Import.persistenceUnit);
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 991);
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione != 1)
				throw new RuntimeException("Operazione non consentita sulle righe d'ordine. (update/delete)");
			String riferimento = parser.getStringa(1, 21);
			MOrdine ordine = mappaOrdini.get(riferimento);
			if (ordine == null) throw new RuntimeException("Nessun ordine trovato con questo riferimento. (" + riferimento + ")");
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
	}
	
//	public static List<RighiOrdine> parsaRigheOrdine(List<String> righe) {
//		TestataOrdiniDao daoTestate = new TestataOrdiniDao(Import.persistenceUnit);
//		MagazzinoDao daoMagazzini = new MagazzinoDao(Import.persistenceUnit);
//		ArticoliDao daoArticoli = new ArticoliDao(Import.persistenceUnit);
//		MagaSdDao daoSaldi = new MagaSdDao(Import.persistenceUnit);
//		MagaMovDao daoMovimenti = new MagaMovDao(Import.persistenceUnit);
//		List<RighiOrdine> righeOrdine = new LinkedList<>();
//		List<MagaMov> movimentiDaInserire = new LinkedList<>();
//		List<MagaSd> saldiDaAggiornare = new LinkedList<>();
//		List<MagaSd> saldiDaInserire = new LinkedList<>();
//		String[] lines = new String[righe.size()];
//		lines = righe.toArray(lines);
//		StringParser parser = new StringParser(lines, 991);
//		do {
//			int operazione = parser.getIntero(0, 1);
//			String riferimento = parser.getStringa(1, 21);
//			TestataOrdini testata = daoTestate.trovaDaRiferimento(riferimento);
//			if (testata == null)
//				throw new RuntimeException("Nessun ordine trovato con questo riferimento. (" + riferimento + ")");
//			int numeroRiga = parser.getIntero(21, 31);
//			String codificaMagazzino = parser.getStringa(140, 150);
//			Magazzini magazzino = daoMagazzini.trovaDaCodificaCliente(codificaMagazzino);
//			if (magazzino == null)
//				throw new RuntimeException("Nessun magazzino trovato con questo riferimento. (" + codificaMagazzino + ")");
//			//String stagione = parser.getStringa(143, 193); //Non mappato
//			String riferimentoCliente = parser.getStringa(216, 245);
//			String diversificazione = parser.getStringa(245, 285);
//			if (diversificazione != null && diversificazione.length() > 30) {
//				diversificazione = diversificazione.substring(0, 30);
//			}
//			String codiceArticolo = parser.getStringa(1088, 1098); //Da verificare
//			int indexTaglia = 285;
//			int counter = 1;
//			int lunghezzaCampo = 10;
//			while (counter < 40) {
//				if (operazione == 1 /*|| operazione == 2*/) {
//					int quantità = parser.getIntero(indexTaglia + (counter - 1) * lunghezzaCampo, indexTaglia + (counter) * lunghezzaCampo);
//					if (quantità > 0) {
//						String skuEffettivo = codiceArticolo + "_" + counter;
//						Articoli articolo = daoArticoli.trovaDaSKU(skuEffettivo);
//						if (articolo == null)
//							throw new RuntimeException("Nessun articolo corrispondente trovato. (" + skuEffettivo + ")");
//						RighiOrdine riga = new RighiOrdine();
//						riga.setBarraEAN(articolo.getBarraEAN());
//						riga.setBarraUPC(articolo.getBarraUPC());
//						riga.setCodBarre(articolo.getCodBarre());
//						riga.setCodiceArticolo(articolo.getCodArtStr());
//						riga.setColore(articolo.getColore());
//						riga.setDataOrdine(testata.getDataOrdine());
//						riga.setDescrizione(articolo.getDescrizione());
//						riga.setIdDestina(testata.getIdDestina());
//						riga.setIdTestataOrdine(testata.getIdTestaSped());
//						riga.setIdUnicoArt(articolo.getIdUniArticolo());
//						riga.setMagazzino(magazzino.getCodiceMag());
//						riga.setPONumber(riferimentoCliente);
//						riga.setNoteCliente(diversificazione);
//						riga.setNrLista(testata.getNrLista());
//						riga.setRagstampe1(testata.getNrLista());
//						riga.setNrOrdine(testata.getNrOrdine());
//						riga.setNrRigo(numeroRiga);
//						riga.setQtaSpedizione(quantità);
//						riga.setTaglia(articolo.getTaglia());
//						riga.setTipoord(testata.getTipoOrdine());
//						righeOrdine.add(riga);
//						//Saldo
//						MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(articolo.getIdUniArticolo(), magazzino.getCodiceMag());
//						if (saldo == null) {
//							saldo = new MagaSd();
//							saldo.setCodMaga(magazzino.getCodiceMag());
//							saldo.setIdUniArticolo(articolo.getIdUniArticolo());
//							saldo.setImpegnato(quantità);
//							saldiDaInserire.add(saldo);
//						} else {
//							saldo.setImpegnato(saldo.getImpegnato() + quantità);
//							saldiDaAggiornare.add(saldo);
//						}						
//						//Movimento
//						MagaMov movimento = new MagaMov();
//						movimento.setCausale("IOS");
//						movimento.setCodMaga(magazzino.getCodiceMag());
//						movimento.setDocCat("O");
//						movimento.setDocTipo("ORD");
//						movimento.setDocNr(testata.getNrLista());
//						movimento.setDocNote("IMPEGNATO DA ORDINE");
//						movimento.setQuantita(quantità);
//						movimento.setSegno("+");
//						movimento.setTipo("IP");
//						movimento.setUtente("WS");
//						movimento.setSegnoEsi("N");
//						movimento.setSegnoImp("+");
//						movimento.setSegnoDis("-");
//						movimento.setIncTotali("NO");
//						movimento.setEsistenzamov(saldo.getEsistenza());
//						movimento.setDisponibilemov(saldo.getDisponibile());
//						movimento.setImpegnatomov(saldo.getImpegnato());
//						movimentiDaInserire.add(movimento);
//					}					
//				} /*else if (operazione == 3) {
//					RighiOrdine riga = new RighiOrdine();
//					riga.setNrLista(testata.getNrLista());
//					riga.setNrRigo(numeroRiga);
//					riga.setTipoord(CANCELLAZIONE);
//					righeOrdine.add(riga);
//				}*/
//				else {
//					throw new RuntimeException("Operazione non consentita sulle righe d'ordine. (update/delete)");
//				}
//				counter++;
//			}
//		} while (parser.prossimaLinea());
//		return righeOrdine;
//	}
	
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
