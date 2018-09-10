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

public class SpostaPezziInventarioMain {

	public static final String persistenceUnit = "legacy-coltorti";
	public static final int ID_INVENTARIO = 3500;
	public static final int ID_INVENTARIO_BIS = 4358;
	
	public static void main(String[] args) {
		//Preparo l'accesso ai dati.
		PakiTestaDao daoTestate = new PakiTestaDao(persistenceUnit);
		PakiArticoloDao daoRighe = new PakiArticoloDao(persistenceUnit);
		ColliPackDao daoArticoli = new ColliPackDao(persistenceUnit);
		//Prendo i pakiarticolo e i collipack collegati all'inventario, salvo i pakiarticolo in una mappa <IDunivocoarticolo, List<pakiarticolo>>
		List<ColliPack> colliPackInventario = daoArticoli.trovaProdottiNelCarico(ID_INVENTARIO);
		List<PakiArticolo> righeInventario = daoRighe.trovaRigheDaCarico(ID_INVENTARIO);
//		HashMap<String, List<PakiArticolo>> mappaInventario = new HashMap<>();
//		for (PakiArticolo riga : righeInventario) {
//			if (!mappaInventario.containsKey(riga.getCodUnicoArt())) {
//				List<PakiArticolo> list = new LinkedList<>();
//				mappaInventario.put(riga.getCodUnicoArt(), list);
//			}
//			mappaInventario.get(riga.getCodUnicoArt()).add(riga);
//		}
		HashMap<Integer, PakiArticolo> mappaInventario = new HashMap<>();
		for (PakiArticolo riga : righeInventario) {
			mappaInventario.put(riga.getIdPakiArticolo(), riga);
		}
		//Prendo i pakitesta in stato "IN_LAVORAZIONE" e "LAVORATO" e li salvo in una mappa <ID, pakitesta>
		//Inoltre per ogni pakitesta trovato prendo i pakiarticolo collegati e li salvo in una mappa <IDunivocoarticolo, List<pakiarticolo>>
		HashMap<Integer, PakiTesta> mappaTestate = new HashMap<>();
		HashMap<String, List<PakiArticolo>> mappaRighe = new HashMap<>();
		List<PakiTesta> testateArrivate = daoTestate.trovaDaStato("ARRIVATO");
		for (PakiTesta testata : testateArrivate) {
			if (!"CARICO".equals(testata.getTipodocumento()) || testata.getIdTestaPaki() == ID_INVENTARIO || testata.getIdTestaPaki() == ID_INVENTARIO_BIS)
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
		List<PakiTesta> testateInLavorazione = daoTestate.trovaDaStato("IN_LAVORAZIONE");
		for (PakiTesta testata : testateInLavorazione) {
			if (!"CARICO".equals(testata.getTipodocumento()) || testata.getIdTestaPaki() == ID_INVENTARIO || testata.getIdTestaPaki() == ID_INVENTARIO_BIS)
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
		List<PakiTesta> testateLavorate = daoTestate.trovaDaStato("LAVORATO");
		for (PakiTesta testata : testateLavorate) {
			if (!"CARICO".equals(testata.getTipodocumento()) || testata.getIdTestaPaki() == ID_INVENTARIO || testata.getIdTestaPaki() == ID_INVENTARIO_BIS)
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
		//Per ogni collipack trovato vado a vedere se riesco a trovare qualche pakiarticolo fra i carichi aperti in base all'iD univoco articolo corrispondente
		for (ColliPack articolo : colliPackInventario) {
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
						}
						break; //Esco dal ciclo
					}
				}				
			}
			
		}
			
	}

}
