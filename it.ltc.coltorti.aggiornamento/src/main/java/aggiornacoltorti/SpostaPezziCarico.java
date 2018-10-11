package aggiornacoltorti;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.legacy.ColliPackDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;

public class SpostaPezziCarico {
	
	public static final String persistenceUnit = "legacy-coltorti";
	
	private static SpostaPezziCarico instance;
	
	private final PakiTestaDao daoTestate;
	private final PakiArticoloDao daoRighe;
	private final ColliPackDao daoArticoli;
	
	private List<ColliPack> colliPackInventario;
	
	private final HashMap<Integer, PakiArticolo> mappaInventario;
	private final HashMap<Integer, PakiTesta> mappaTestate;
	private final HashMap<String, List<PakiArticolo>> mappaRighe;

	private SpostaPezziCarico() {
		//Dao
		daoTestate = new PakiTestaDao(persistenceUnit);
		daoRighe = new PakiArticoloDao(persistenceUnit);
		daoArticoli = new ColliPackDao(persistenceUnit);
		//Mappe
		mappaInventario = new HashMap<>();
		mappaTestate = new HashMap<>();
		mappaRighe = new HashMap<>();
	}

	public static SpostaPezziCarico getInstance() {
		if (instance == null) {
			instance = new SpostaPezziCarico();
		}
		return instance;
	}
	
	public void spostaPezzi(PakiTesta sorgente, List<PakiTesta> destinazioni) {
		recuperaPezziSorgente(sorgente.getIdTestaPaki());
		recuperaDichiaratoDestinazioni(sorgente, destinazioni);
		spostaPezzi();
	}
	
	/**
	 * Prendo i pakiarticolo e i collipack collegati all'inventario, salvo i pakiarticolo in una mappa <IDunivocoarticolo, List<pakiarticolo>>
	 */
	private void recuperaPezziSorgente(int idCaricoSorgente) {
		colliPackInventario = daoArticoli.trovaProdottiNelCarico(idCaricoSorgente);
		List<PakiArticolo> righeInventario = daoRighe.trovaRigheDaCarico(idCaricoSorgente);
		for (PakiArticolo riga : righeInventario) {
			mappaInventario.put(riga.getIdPakiArticolo(), riga);
		}
	}
	
	private void recuperaDichiaratoDestinazioni(PakiTesta sorgente, List<PakiTesta> destinazioni) {
		for (PakiTesta testata : destinazioni) {
			//Controllo che i carichi "destinazione" siano di tipo "CARICO" escludendo così inventari, resi, boutique etc. e che non sia la stessa sorgente.
			if (!"CARICO".equals(testata.getTipodocumento()) || testata.getIdTestaPaki() == sorgente.getIdTestaPaki())
				continue;
			mappaTestate.put(testata.getIdTestaPaki(), testata);
			List<PakiArticolo> righeCarico = daoRighe.trovaRigheDaCarico(testata.getIdTestaPaki());
			for (PakiArticolo riga : righeCarico) {
				if (!mappaRighe.containsKey(riga.getCodUnicoArt())) {
					List<PakiArticolo> list = new LinkedList<>();
					mappaRighe.put(riga.getCodUnicoArt(), list);
				}
				mappaRighe.get(riga.getCodUnicoArt()).add(riga);
			}
		}
	}
	
	/**
	 * Per ogni collipack trovato vado a vedere se riesco a trovare qualche pakiarticolo fra i carichi aperti in base all'iD univoco articolo corrispondente
	 */
	private void spostaPezzi() {
		int pezziSpostati = 0;
		for (ColliPack articolo : colliPackInventario) {
			//Controllo che l'articolo non sia stato impegnato.
			if ("S".equals(articolo.getFlagimp()))
				continue;
			//se esiste allora aggiorno tutto il necessario come segue:
			if (mappaRighe.containsKey(articolo.getCodiceArticolo().trim())) {
				//Check: la qta deve essere 1!
				if (articolo.getQta() != 1) {
					System.out.println("Qualcosa è andato storto! qta su collipack diversa da 1.");
					continue;
				}
				PakiArticolo rigaInventarioCheck = mappaInventario.get(articolo.getIdPakiarticolo());
				if (rigaInventarioCheck.getQtaVerificata() - 1 < 0) {
					System.out.println("Qualcosa è andato storto! aggiornamento manuale.");
					continue;
				}
				List<PakiArticolo> righeCorrispondenti = mappaRighe.get(articolo.getCodiceArticolo().trim());
				for (PakiArticolo riga : righeCorrispondenti) {
					if (riga.getQtaVerificata() < riga.getQtaPaki()) {
						//1) nel pakiarticolo dell'inventario abbasso la qtaverificata e aumento la qtapredoc.
						PakiArticolo rigaInventario = mappaInventario.get(articolo.getIdPakiarticolo());
						if (rigaInventario == null) {
							System.out.println("Qualcosa è andato storto! Non ho trovato la riga dell'inventario.");
						} else {
							rigaInventario.setQtaVerificata(rigaInventario.getQtaVerificata() - 1);
							rigaInventario.setQtaPreDoc(rigaInventario.getQtaPreDoc() + 1);
							PakiArticolo update = daoRighe.aggiorna(rigaInventario);
							if (update == null) {
								System.out.println("Qualcosa è andato storto! Aggiornamento fallito.");
							}
						}
						//2) nel pakiarticolo "aperto" aumento la qtaverificata.
						riga.setQtaVerificata(riga.getQtaVerificata() + 1);
						//3) nel collipack cambio il riferimento al pakiarticolo e al pakitesta.
						articolo.setIdPakiarticolo(riga.getIdPakiArticolo());
						articolo.setIdTestaPaki(riga.getIdPakiTesta());
						//4) nel pakitesta corrispondente aumento la qta verificata di 1.
						PakiTesta testata = mappaTestate.get(riga.getIdPakiTesta());
						if (testata == null) {
							System.out.println("Qualcosa è andato storto! Non ho trovato la testata.");
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
							System.out.println("Qualcosa è andato storto! Aggiornamento fallito.");
						} else {
							pezziSpostati++;
						}
						break; //Esco dal ciclo
					}
				}				
			}
		}
		System.out.println("Totale dei pezzi trasferiti: " + pezziSpostati);
	}

}
