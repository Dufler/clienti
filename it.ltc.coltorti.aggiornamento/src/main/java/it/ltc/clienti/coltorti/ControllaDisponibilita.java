package it.ltc.clienti.coltorti;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

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

public class ControllaDisponibilita {
	
	private static final Logger logger = Logger.getLogger(ControllaDisponibilita.class);
	
	public final String persistenceUnit;
	
	private final HashMap<String, Integer> mappaDisponibile;
	private final HashMap<String, Integer> mappaImpegnato;
	private final HashMap<String, Integer> mappaEsistenza;
	
	private final PakiTestaDao daoCarichi;
	private final PakiArticoloDao daoRigheCarichi;
	
	public ControllaDisponibilita() {
		persistenceUnit = ConfigurationUtility.getInstance().getPersistenceUnit();
		mappaDisponibile = new HashMap<>();
		mappaImpegnato = new HashMap<>();
		mappaEsistenza = new HashMap<>();
		daoCarichi = new PakiTestaDao(persistenceUnit);
		daoRigheCarichi = new PakiArticoloDao(persistenceUnit);
	}
	
	private void aggiungiDisponibile(String idUniArticolo, int quantità) {
		int disponibile = mappaDisponibile.containsKey(idUniArticolo) ? mappaDisponibile.get(idUniArticolo) : 0;
		disponibile += quantità;
		mappaDisponibile.put(idUniArticolo, disponibile);
	}
	
	private void aggiungiEsistenza(String idUniArticolo, int quantità) {
		int esistenza = mappaEsistenza.containsKey(idUniArticolo) ? mappaEsistenza.get(idUniArticolo) : 0;
		esistenza += quantità;
		mappaEsistenza.put(idUniArticolo, esistenza);
	}
	
	private void aggiungiImpegnato(String idUniArticolo, int quantità) {
		int impegnato = mappaImpegnato.containsKey(idUniArticolo) ? mappaImpegnato.get(idUniArticolo) : 0;
		impegnato += quantità;
		mappaImpegnato.put(idUniArticolo, impegnato);
	}

	public void controlla(String outputFilePath) {
		//Prendo tutti i carichi con stato chiuso, considero i prodotti all'interno e li sommo ai disponibili e esistenza.
		List<PakiTesta> carichi = daoCarichi.trovaDaStato("CHIUSO");
		for (PakiTesta carico : carichi) {
			List<PakiArticolo> righe = daoRigheCarichi.trovaRigheDaCarico(carico.getIdTestaPaki());
			for (PakiArticolo riga : righe) {
				int quantità = riga.getQtaVerificata();
				String idUniArticolo = riga.getCodUnicoArt();
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
				int quantità = riga.getQtaSpedizione();
				aggiungiDisponibile(idUniArticolo, -quantità);
				aggiungiImpegnato(idUniArticolo, quantità);
			}
		}
		List<TestataOrdini> inImabllo = daoTestate.trovaDaStato("INIB");
		for (TestataOrdini testata : inImabllo) {
			List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaIDOrdine(testata.getIdTestaSped());
			for (RighiOrdine riga : righe) {
				String idUniArticolo = riga.getIdUnicoArt();
				int quantità = riga.getQtaSpedizione();
				aggiungiDisponibile(idUniArticolo, -quantità);
				aggiungiImpegnato(idUniArticolo, quantità);
			}
		}
		List<TestataOrdini> differenzaImabllo = daoTestate.trovaDaStato("DIIB");
		for (TestataOrdini testata : differenzaImabllo) {
			List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaIDOrdine(testata.getIdTestaSped());
			for (RighiOrdine riga : righe) {
				String idUniArticolo = riga.getIdUnicoArt();
				int quantità = riga.getQtaSpedizione();
				aggiungiDisponibile(idUniArticolo, -quantità);
				aggiungiImpegnato(idUniArticolo, quantità);
			}
		}
		List<TestataOrdini> completatoImabllo = daoTestate.trovaDaStato("COIB");
		for (TestataOrdini testata : completatoImabllo) {
			List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaIDOrdine(testata.getIdTestaSped());
			for (RighiOrdine riga : righe) {
				String idUniArticolo = riga.getIdUnicoArt();
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
		scriviRisultatoSuFile(outputFilePath, saldiSbagliati);
	}
	
	private void scriviRisultatoSuFile(String outputFilePath, Set<MagaSd> saldiSbagliati) {
		try (FileWriter out = new FileWriter(outputFilePath)){
			BufferedWriter bw = new BufferedWriter(out);
			bw.write(saldiSbagliati.isEmpty() ? "Nessun saldo anomalo!" : "Lista dei saldi sbagliati");
			for (MagaSd saldo : saldiSbagliati) {
				bw.newLine();
				bw.write(saldo.toString());
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			logger.error("Impossibile scrivere il file di output. (" + outputFilePath + ")");
		}		
	}

}
