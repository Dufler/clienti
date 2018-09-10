package it.ltc.ciesse.scambiodati.logic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.ciesse.scambiodati.model.Colli;
import it.ltc.ciesse.scambiodati.model.ContenutoColli;
import it.ltc.ciesse.scambiodati.model.DocumentiEntrataRighe;
import it.ltc.ciesse.scambiodati.model.DocumentiEntrataTestata;
import it.ltc.database.dao.legacy.ColliImballoDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.dao.legacy.RighiImballoDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.model.legacy.ColliImballo;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.database.model.legacy.RighiImballo;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.utility.miscellanea.file.FileUtility;

public class Export {
	
	private static final Logger logger = Logger.getLogger(Export.class);
	
	public static final String persistenceUnit = "legacy-test";
	
	public static final String PATH_CARTELLA_EXPORT = "\\\\192.168.0.10\\e$\\Gestionali\\Ciesse\\FTP\\OUT\\";
	
	private static Export instance;

	private final SimpleDateFormat sdf;
	
	private Export() {
		sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	}

	public static Export getInstance() {
		if (instance == null) {
			instance = new Export();
		}
		return instance;
	}
	
	public void esportaDati() {
		esportaCarichi();
		esportaOrdini();
	}
	
	private void generaFileCheck(Date now) {
		String nomeFileCheck = sdf.format(now) + ".chk";
		String contenutoFileCheck = "\r\n";
		String pathFileCheck = PATH_CARTELLA_EXPORT + nomeFileCheck;
		boolean exportFileCheck = FileUtility.writeFile(pathFileCheck, contenutoFileCheck);
		if (!exportFileCheck)
			logger.error("Impossibile esportare il file di controllo: " + nomeFileCheck);
	}
	
	private void esportaCarichi() {
		PakiTestaDao daoCarichi = new PakiTestaDao(persistenceUnit);
		PakiArticoloDao daoRighe = new PakiArticoloDao(persistenceUnit);
		//Trovo tutti i carichi pronti per essere esportati
		List<PakiTesta> carichi = daoCarichi.trovaCarichiChiusiDaEsportare();
		//Elaboro i file di testata
		//Recupero il contenuto per ognuno di questi e elaboro i file di riga
		for (PakiTesta carico : carichi) {
			String rigaDocumentoTestata = DocumentiEntrataTestata.esportaTestata(carico);
			List<PakiArticolo> righe = daoRighe.trovaRigheDaCarico(carico.getIdTestaPaki());
			List<String> righeDocumento = DocumentiEntrataRighe.esportaRigheDocumento(righe);
			//creo i files e li deposito nell'area FTP.
			boolean generazione = generaFilesCarichi(rigaDocumentoTestata, righeDocumento);
			if (generazione) {
				carico.setFlagTra("T");
				if (daoCarichi.aggiorna(carico) == null)
					logger.error("Impossibile aggiornare lo stato di trasmissione del carico ID " + carico.getIdTestaPaki());
				else
					logger.info("Generazione completata per il carico ID " + carico.getIdTestaPaki());
			}
		}		
	}
	
	private boolean generaFilesCarichi(String righeDocumentoTestate, List<String> righeDocumento) {
		Date now = new Date();
		String nomeFileDocumentoTestate = "DocumentiEntrata_" + sdf.format(now) + ".txt";
		String nomeFileDocumentoRighe = "RigheDocumentiEntrata_" + sdf.format(now) + ".txt";
		String pathDocumentoTestate = PATH_CARTELLA_EXPORT + nomeFileDocumentoTestate;
		String pathDocumentoRighe = PATH_CARTELLA_EXPORT + nomeFileDocumentoRighe;
		boolean exportDocumentoTestate = FileUtility.writeFile(pathDocumentoTestate, righeDocumentoTestate);
		if (!exportDocumentoTestate)
			logger.error("Impossibile esportare il documento testate di carico: " + nomeFileDocumentoTestate);
		boolean exportDocumentoRighe = FileUtility.writeFile(pathDocumentoRighe, righeDocumento);
		if (!exportDocumentoRighe)
			logger.error("Impossibile esportare il documento righe di carico: " + nomeFileDocumentoRighe);
		generaFileCheck(now);
		return exportDocumentoTestate && exportDocumentoRighe;
	}
	
	private void esportaOrdini() {
		TestataOrdiniDao daoTestate = new TestataOrdiniDao(persistenceUnit);
		ColliImballoDao daoColli = new ColliImballoDao(persistenceUnit);
		RighiImballoDao daoImballo = new RighiImballoDao(persistenceUnit);
		List<TestataOrdini> testate = daoTestate.trovaDaStato("ELAB");
		for (TestataOrdini testata : testate) {
			//Recupero i colli creati per la lista
			List<ColliImballo> colli = daoColli.trovaDaNumeroLista(testata.getNrLista());
			//Recupero l'imballato per la lista
			List<RighiImballo> pezziImballati = daoImballo.trovaDaNumeroLista(testata.getNrLista());
			//genero le righe con le info sull'imballato e sui colli
			List<String> righeColli = Colli.esportaColli(colli, testata.getRifOrdineCli());
			List<String> righeContenutoColli = ContenutoColli.esportaImballatoDaLista(pezziImballati, testata.getRifOrdineCli());
			//creo i files e li deposito nell'area FTP.
			boolean generazione = generaFilesColli(righeColli, righeContenutoColli);
			if (generazione) {
				testata.setStato("INSP");
				if (daoTestate.aggiorna(testata) == null)
					logger.error("Impossibile aggiornare lo stato di trasmissione dell'ordine ID " + testata.getIdTestaSped());
				else
					logger.info("Generazione completata per l'ordine ID " + testata.getIdTestaSped());
			}
		}
	}
	
	private boolean generaFilesColli(List<String> righeDocumentoColli, List<String> righeDocumentoContenutoColli) {
		Date now = new Date();
		String nomeFileDocumentoColli = "Colli_" + sdf.format(now) + ".txt";
		String nomeFileDocumentoContenutoColli = "ContenutoColli_" + sdf.format(now) + ".txt";
		String pathDocumentoColli = PATH_CARTELLA_EXPORT + nomeFileDocumentoColli;
		String pathDocumentoContenutoColli = PATH_CARTELLA_EXPORT + nomeFileDocumentoContenutoColli;
		boolean exportDocumentoTestate = FileUtility.writeFile(pathDocumentoColli, righeDocumentoColli);
		if (!exportDocumentoTestate)
			logger.error("Impossibile esportare il documento testate di carico: " + nomeFileDocumentoColli);
		boolean exportDocumentoRighe = FileUtility.writeFile(pathDocumentoContenutoColli, righeDocumentoContenutoColli);
		if (!exportDocumentoRighe)
			logger.error("Impossibile esportare il documento righe di carico: " + nomeFileDocumentoContenutoColli);
		generaFileCheck(now);
		return exportDocumentoTestate && exportDocumentoRighe;
	}

}