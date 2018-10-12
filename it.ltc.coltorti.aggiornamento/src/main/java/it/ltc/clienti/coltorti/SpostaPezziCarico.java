package it.ltc.clienti.coltorti;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.database.dao.legacy.ColliPackDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;

public class SpostaPezziCarico {
	
	private static final Logger logger = Logger.getLogger(SpostaPezziCarico.class);
	
	private static SpostaPezziCarico instance;
	
	public final String persistenceUnit;
	
	private final PakiTestaDao daoTestate;
	private final PakiArticoloDao daoRighe;
	private final ColliPackDao daoArticoli;
	
	private final HashMap<Integer, PakiArticolo> mappaRigheSorgente;
	private final HashMap<Integer, PakiTesta> mappaTestateDestinazioni;
	private final HashMap<String, List<PakiArticolo>> mappaRigheDestinazioni;
	
	private List<ColliPack> colliPackSorgente;

	private SpostaPezziCarico() {
		persistenceUnit = ConfigurationUtility.getInstance().getPersistenceUnit();
		//Dao
		daoTestate = new PakiTestaDao(persistenceUnit);
		daoRighe = new PakiArticoloDao(persistenceUnit);
		daoArticoli = new ColliPackDao(persistenceUnit);
		//Mappe
		mappaRigheSorgente = new HashMap<>();
		mappaTestateDestinazioni = new HashMap<>();
		mappaRigheDestinazioni = new HashMap<>();
	}

	public static SpostaPezziCarico getInstance() {
		if (instance == null) {
			instance = new SpostaPezziCarico();
		}
		return instance;
	}
	
	public void spostaPezzi(int idSorgente, int[] idDestinazioni) {
		PakiTesta sorgente = daoTestate.trovaDaID(idSorgente);
		if (sorgente == null)
			throw new RuntimeException("L'ID indicato per la sorgente non corrisponde a nessun carico. (" + idSorgente + ")");
		List<PakiTesta> destinazioni = new LinkedList<>();
		for (int idCarico : idDestinazioni) {
			PakiTesta destinazione = daoTestate.trovaDaID(idCarico);
			if (destinazione == null)
				throw new RuntimeException("L'ID indicato per la destinazione non corrisponde a nessun carico. (" + idCarico + ")");
			destinazioni.add(destinazione);
		}
		recuperaPezziSorgente(sorgente.getIdTestaPaki());
		recuperaDichiaratoDestinazioni(sorgente, destinazioni);
		spostaPezzi();
	}
	
	/**
	 * Prendo i pakiarticolo e i collipack collegati all'inventario, salvo i pakiarticolo in una mappa <IDunivocoarticolo, List<pakiarticolo>>
	 */
	private void recuperaPezziSorgente(int idCaricoSorgente) {
		logger.info("Controllo il carico sorgente.");
		colliPackSorgente = daoArticoli.trovaProdottiNelCarico(idCaricoSorgente);
		List<PakiArticolo> righeInventario = daoRighe.trovaRigheDaCarico(idCaricoSorgente);
		for (PakiArticolo riga : righeInventario) {
			mappaRigheSorgente.put(riga.getIdPakiArticolo(), riga);
		}
		logger.info("Sono state trovate " + righeInventario.size() + " righe e " + colliPackSorgente.size() + " prodotti nel carico sorgente.");
	}
	
	private void recuperaDichiaratoDestinazioni(PakiTesta sorgente, List<PakiTesta> destinazioni) {
		logger.info("Controllo i carichi di destinazione.");
		for (PakiTesta testata : destinazioni) {
			//Controllo che i carichi "destinazione" siano di tipo "CARICO" escludendo così inventari, resi, boutique etc. e che non sia la stessa sorgente.
			if (!"CARICO".equals(testata.getTipodocumento())) {
				logger.warn("Il carico indicato non è del tipo corretto. (ID: " + testata.getIdTestaPaki() + ", Tipo: " + testata.getTipodocumento() + ")");
				continue;
			}
			if (testata.getIdTestaPaki() == sorgente.getIdTestaPaki()) {
				logger.warn("E' stato indicato il carico sia come sorgente che destinazione, verrà ignorato. (ID: " + testata.getIdTestaPaki() + ")");
				continue;
			}
			//Procedo a mappare	
			mappaTestateDestinazioni.put(testata.getIdTestaPaki(), testata);
			List<PakiArticolo> righeCarico = daoRighe.trovaRigheDaCarico(testata.getIdTestaPaki());
			for (PakiArticolo riga : righeCarico) {
				if (!mappaRigheDestinazioni.containsKey(riga.getCodUnicoArt())) {
					List<PakiArticolo> list = new LinkedList<>();
					mappaRigheDestinazioni.put(riga.getCodUnicoArt(), list);
				}
				mappaRigheDestinazioni.get(riga.getCodUnicoArt()).add(riga);
			}
			logger.info("Carico di destinazione mappato. (ID: " + testata.getIdTestaPaki() + ")");
		}
	}
	
	/**
	 * Per ogni collipack trovato vado a vedere se riesco a trovare qualche pakiarticolo fra i carichi aperti in base all'iD univoco articolo corrispondente
	 */
	private void spostaPezzi() {
		int pezziSpostati = 0;
		HashMap<Integer, Integer> mappaPezziSpostati = new HashMap<>();
		for (ColliPack articolo : colliPackSorgente) {
			//Controllo che l'articolo non sia stato impegnato.
			if ("S".equals(articolo.getFlagimp()))
				continue;
			//se esiste allora aggiorno tutto il necessario come segue:
			if (mappaRigheDestinazioni.containsKey(articolo.getCodiceArticolo().trim())) {
				//Check: la qta deve essere 1!
				if (articolo.getQta() != 1) {
					logger.error("(ERRORE) qta su collipack sorgente diversa da 1. (ID collipack: " + articolo.getIdColliPack() + ") passo al prossimo.");
					continue;
				}
				PakiArticolo rigaInventarioCheck = mappaRigheSorgente.get(articolo.getIdPakiarticolo());
				if (rigaInventarioCheck.getQtaVerificata() - 1 < 0) {
					logger.error("(ERRORE) La riga del carico sorgente ha una quantità verificata non sufficiente. (ID pakiarticolo: " + rigaInventarioCheck.getIdPakiArticolo() + ", quantità: " + rigaInventarioCheck.getQtaVerificata() + ") passo al prossimo.");
					continue;
				}
				List<PakiArticolo> righeCorrispondenti = mappaRigheDestinazioni.get(articolo.getCodiceArticolo().trim());
				for (PakiArticolo riga : righeCorrispondenti) {
					if (riga.getQtaVerificata() < riga.getQtaPaki()) {
						//1) nel pakiarticolo dell'inventario abbasso la qtaverificata e aumento la qtapredoc.
						PakiArticolo rigaInventario = mappaRigheSorgente.get(articolo.getIdPakiarticolo());
						if (rigaInventario == null) {
							logger.error("(ERRORE) Non ho trovato la riga dell'carico sorgente. (ID pakiarticolo cercato: " + articolo.getIdPakiarticolo() + ")");
						} else {
							rigaInventario.setQtaVerificata(rigaInventario.getQtaVerificata() - 1);
							rigaInventario.setQtaPreDoc(rigaInventario.getQtaPreDoc() + 1);
							PakiArticolo update = daoRighe.aggiorna(rigaInventario);
							if (update == null) {
								logger.error("(ERRORE) Aggiornamento pakiarticolo sorgente fallito.");
							}
						}
						//2) nel pakiarticolo "aperto" aumento la qtaverificata.
						riga.setQtaVerificata(riga.getQtaVerificata() + 1);
						//3) nel collipack cambio il riferimento al pakiarticolo e al pakitesta.
						articolo.setIdPakiarticolo(riga.getIdPakiArticolo());
						articolo.setIdTestaPaki(riga.getIdPakiTesta());
						//4) nel pakitesta corrispondente aumento la qta verificata di 1.
						PakiTesta testata = mappaTestateDestinazioni.get(riga.getIdPakiTesta());
						if (testata == null) {
							logger.error("(ERRORE) Non ho trovato la testata. (ID pakitesta cercato: " + riga.getIdPakiTesta() + ")");
						} else {
							if (testata.getStato().equals("ARRIVATO")) //Aggiorno lo stato, se necessario.
								testata.setStato("IN_LAVORAZIONE");
							testata.setQtaTotAre(testata.getQtaTotAre() + 1);
						}
						PakiArticolo updateRiga = daoRighe.aggiorna(riga);
						ColliPack updateArticolo = daoArticoli.aggiorna(articolo);
						PakiTesta updateTestata = daoTestate.aggiorna(testata);
						//Verifica il corretto aggiornamento
						if (updateRiga == null || updateArticolo== null || updateTestata == null) {
							logger.error("(ERRORE) Aggiornamento fallito.");
						} else {
							int pezzi = mappaPezziSpostati.containsKey(testata.getIdTestaPaki()) ? mappaPezziSpostati.get(testata.getIdTestaPaki()) + 1 : 1;
							mappaPezziSpostati.put(testata.getIdTestaPaki(), pezzi);
							pezziSpostati++;
						}
						break; //Esco dal ciclo
					}
				}				
			}
		}
		logger.info("Totale dei pezzi trasferiti: " + pezziSpostati);
		for (Integer idTestata : mappaPezziSpostati.keySet()) {
			int pezzi = mappaPezziSpostati.get(idTestata);
			logger.info("Carico ID " + idTestata + ", " + pezzi + " pezzi.");
		}
	}

}
