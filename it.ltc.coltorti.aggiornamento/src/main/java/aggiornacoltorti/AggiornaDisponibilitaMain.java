package aggiornacoltorti;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.ltc.database.dao.legacy.MagaSdDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.dao.legacy.RighiOrdineDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.TestataOrdini;

public class AggiornaDisponibilitaMain {
	
	public static final String persistenceUnit = "legacy-coltorti";
	
	private static HashMap<String, Integer> mappaDisponibile;
	private static HashMap<String, Integer> mappaImpegnato;
	private static HashMap<String, Integer> mappaEsistenza;
	
	private static void aggiungiDisponibile(String idUniArticolo, int quantità) {
		int disponibile = mappaDisponibile.containsKey(idUniArticolo) ? mappaDisponibile.get(idUniArticolo) : 0;
		disponibile += quantità;
		mappaDisponibile.put(idUniArticolo, disponibile);
	}
	
	private static void aggiungiEsistenza(String idUniArticolo, int quantità) {
		int esistenza = mappaEsistenza.containsKey(idUniArticolo) ? mappaEsistenza.get(idUniArticolo) : 0;
		esistenza += quantità;
		mappaEsistenza.put(idUniArticolo, esistenza);
	}
	
	private static void aggiungiImpegnato(String idUniArticolo, int quantità) {
		int impegnato = mappaImpegnato.containsKey(idUniArticolo) ? mappaImpegnato.get(idUniArticolo) : 0;
		impegnato += quantità;
		mappaImpegnato.put(idUniArticolo, impegnato);
	}

	public static void main(String[] args) {
		mappaDisponibile = new HashMap<>();
		mappaImpegnato = new HashMap<>();
		mappaEsistenza = new HashMap<>();
		//Prendo tutti i carichi con stato chiuso, considero i prodotti all'interno e li sommo ai disponibili e esistenza.
		PakiTestaDao daoCarichi = new PakiTestaDao(persistenceUnit);
		PakiArticoloDao daoRigheCarichi = new PakiArticoloDao(persistenceUnit);
		List<PakiTesta> carichi = daoCarichi.trovaDaStato("CHIUSO");
		for (PakiTesta carico : carichi) {
			List<PakiArticolo> righe = daoRigheCarichi.trovaRigheDaCarico(carico.getIdTestaPaki());
			for (PakiArticolo riga : righe) {
				int quantità = riga.getQtaVerificata();
				String idUniArticolo = riga.getCodUnicoArt();
				if (idUniArticolo.equals("180623040653735"))
					System.out.println("trovato carico");
				aggiungiDisponibile(idUniArticolo, quantità);
				aggiungiEsistenza(idUniArticolo, quantità);
			}
		}
		//Prendo tutti gli ordini, in base allo stato vado ad alzare l'impegno e ad abbassare disponibliità ed esistenza
		TestataOrdiniDao daoTestate = new TestataOrdiniDao(persistenceUnit);
		RighiOrdineDao daoRigheOrdine = new RighiOrdineDao(persistenceUnit);
		List<TestataOrdini> importati = daoTestate.trovaDaStato("IMPO");
		for (TestataOrdini testata : importati) {
			List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaIDOrdine(testata.getIdTestaSped());
			for (RighiOrdine riga : righe) {
				String idUniArticolo = riga.getIdUnicoArt();
				if (idUniArticolo.equals("180623040653735"))
					System.out.println("trovato ordine");
				int quantità = riga.getQtaSpedizione(); //riga.getQtaImballata();
				aggiungiDisponibile(idUniArticolo, -quantità);
				aggiungiImpegnato(idUniArticolo, quantità);
			}
		}
		List<TestataOrdini> inImabllo = daoTestate.trovaDaStato("INIB");
		for (TestataOrdini testata : inImabllo) {
			List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaIDOrdine(testata.getIdTestaSped());
			for (RighiOrdine riga : righe) {
				String idUniArticolo = riga.getIdUnicoArt();
				if (idUniArticolo.equals("180623040653735"))
					System.out.println("trovato ordine");
				int quantità = riga.getQtaSpedizione(); //riga.getQtaImballata();
				aggiungiDisponibile(idUniArticolo, -quantità);
				aggiungiImpegnato(idUniArticolo, quantità);
			}
		}
		List<TestataOrdini> differenzaImabllo = daoTestate.trovaDaStato("DIIB");
		for (TestataOrdini testata : differenzaImabllo) {
			List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaIDOrdine(testata.getIdTestaSped());
			for (RighiOrdine riga : righe) {
				String idUniArticolo = riga.getIdUnicoArt();
				if (idUniArticolo.equals("180623040653735"))
					System.out.println("trovato ordine");
				int quantità = riga.getQtaSpedizione(); //riga.getQtaImballata();
				aggiungiDisponibile(idUniArticolo, -quantità);
				aggiungiImpegnato(idUniArticolo, quantità);
			}
		}
		List<TestataOrdini> completatoImabllo = daoTestate.trovaDaStato("COIB");
		for (TestataOrdini testata : completatoImabllo) {
			List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaIDOrdine(testata.getIdTestaSped());
			for (RighiOrdine riga : righe) {
				String idUniArticolo = riga.getIdUnicoArt();
				if (idUniArticolo.equals("180623040653735"))
					System.out.println("trovato ordine");
				int quantità = riga.getQtaImballata();
				aggiungiDisponibile(idUniArticolo, -quantità);
				aggiungiImpegnato(idUniArticolo, quantità);
			}
		}
		List<TestataOrdini> elaborati = daoTestate.trovaDaStato("ELAB");
		for (TestataOrdini testata : elaborati) {
			List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaIDOrdine(testata.getIdTestaSped());
			for (RighiOrdine riga : righe) {
				String idUniArticolo = riga.getIdUnicoArt();
				if (idUniArticolo.equals("180623040653735"))
					System.out.println("trovato ordine");
				int quantità = riga.getQtaImballata();
				aggiungiDisponibile(idUniArticolo, -quantità);
				aggiungiEsistenza(idUniArticolo, -quantità);
			}
		}
		List<TestataOrdini> inSpedizione = daoTestate.trovaDaStato("INSP");
		for (TestataOrdini testata : inSpedizione) {
			List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaIDOrdine(testata.getIdTestaSped());
			for (RighiOrdine riga : righe) {
				String idUniArticolo = riga.getIdUnicoArt();
				if (idUniArticolo.equals("180623040653735"))
					System.out.println("trovato ordine");
				int quantità = riga.getQtaImballata();
				aggiungiDisponibile(idUniArticolo, -quantità);
				aggiungiEsistenza(idUniArticolo, -quantità);
			}
		}
		List<TestataOrdini> spediti = daoTestate.trovaDaStato("SPED");
		for (TestataOrdini testata : spediti) {
			List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaIDOrdine(testata.getIdTestaSped());
			for (RighiOrdine riga : righe) {
				String idUniArticolo = riga.getIdUnicoArt();
				if (idUniArticolo.equals("180623040653735"))
					System.out.println("trovato ordine");
				int quantità = riga.getQtaImballata();
				aggiungiDisponibile(idUniArticolo, -quantità);
				aggiungiEsistenza(idUniArticolo, -quantità);
			}
		}
		//Vado a scrivere le quantità disponibili, impegnati ed esistenti
		MagaSdDao daoSaldi = new MagaSdDao(persistenceUnit);
		List<MagaSd> saldiCorrenti = daoSaldi.trovaTuttiPerMagazzino("PG1");
		Set<MagaSd> saldiSbagliati = new HashSet<>();
		for (MagaSd saldo : saldiCorrenti) {
			String idUniArticolo = saldo.getIdUniArticolo();
			if (idUniArticolo.equals("180623040653735"))
				System.out.println("trovato saldo");
			//Disponibile
			Integer checkDisponibile = mappaDisponibile.get(idUniArticolo);
			if (checkDisponibile == null) {
				System.out.println("Disponibile non presente");
				saldiSbagliati.add(saldo);
			}				
			int disponibile = mappaDisponibile.containsKey(idUniArticolo) ? mappaDisponibile.get(idUniArticolo) : 0;
			if (disponibile != saldo.getDisponibile()) {
				System.out.println(" Disponibile non corretto, trovato: " + saldo.getDisponibile() + ", atteso: " + disponibile + " (" + idUniArticolo + ")");
				saldiSbagliati.add(saldo);
			}
			//Impegnato
			int impegnato = mappaImpegnato.containsKey(idUniArticolo) ? mappaImpegnato.get(idUniArticolo) : 0;
			if (impegnato != saldo.getImpegnato()) {
				System.out.println("Impegnato non corretto, trovato: " + saldo.getImpegnato() + ", atteso: " + impegnato + " (" + idUniArticolo + ")");
				saldiSbagliati.add(saldo);
			}
			//Esistenza
			Integer checkEsistenza = mappaEsistenza.get(idUniArticolo);
			if (checkEsistenza == null) {
				System.out.println("Esistenza non presente");
				saldiSbagliati.add(saldo);
			}
			int esistenza = mappaEsistenza.containsKey(idUniArticolo) ? mappaEsistenza.get(idUniArticolo) : 0;
			if (esistenza != saldo.getEsistenza()) {
				System.out.println("Esistenza non corretta, trovato: " + saldo.getEsistenza() + ", atteso: " + esistenza + " (" + idUniArticolo + ")");
				saldiSbagliati.add(saldo);
			}
		}
		System.out.println("Saldi sbagliati:");
		for (MagaSd saldo : saldiSbagliati) {
			System.out.println(saldo);
		}
	}

}
