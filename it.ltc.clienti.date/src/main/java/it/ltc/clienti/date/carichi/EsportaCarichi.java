package it.ltc.clienti.date.carichi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.clienti.date.ConfigurationUtility;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.utility.miscellanea.file.FileUtility;
import it.ltc.utility.miscellanea.string.StringUtility;

public class EsportaCarichi {
	
	private static final Logger logger = Logger.getLogger(EsportaCarichi.class);
	
	private final String pathFolderExport;
	
	private final StringUtility su;
	private final SimpleDateFormat sdfToday;
	private final SimpleDateFormat sdfNow;
	private Date now;
	private final HashMap<String, Articoli> mappaArticoli;
	
	private final ArticoliDao daoArticoli;
	private final PakiTestaDao daoCarichi;
	private final PakiArticoloDao daoRighe;

	public EsportaCarichi(String persistenceUnit) {
		su = new StringUtility();
		sdfToday = new SimpleDateFormat("yyyyMMdd");
		sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
		mappaArticoli = new HashMap<>();
		
		daoArticoli = new ArticoliDao(persistenceUnit);
		daoCarichi = new PakiTestaDao(persistenceUnit);
		daoRighe = new PakiArticoloDao(persistenceUnit);
		
		pathFolderExport = ConfigurationUtility.getInstance().getFolderPathExport();
	}
	
	public void esportaCarichi() {
		logger.info("Avvio la procedura di esportazione dei carichi");
		//trovo i carichi pronti per essere esportati
		List<PakiTesta> carichi = daoCarichi.trovaCarichiChiusiDaEsportare();
		logger.info(carichi.isEmpty() ? "Non sono stati trovati carichi da esportare." : "Stanno per essere esportati " + carichi.size() + " carichi.");
		for (PakiTesta carico : carichi) {
			//imposto la data, aspetto un secondo per evitare sovrapposizioni
			try { Thread.sleep(1001); } catch (Exception e) { logger.error(e.getMessage(), e); }
			now = new Date();
			//per ogni carico trovato recupero le righe collegate
			List<PakiArticolo> righeCarico = daoRighe.trovaRigheDaCarico(carico.getIdTestaPaki());
			//genero le righe corrispondenti
			HashMap<String, RigaCaricoDate> infoCarico = recuperaInfoCarico(righeCarico);
			//Scrivo il file e lo deposito nella cartella out
			List<String> righeFile = generaRigheFileCarico(carico, infoCarico);
			//se tutto è andato bene aggiorno la testata
			boolean export = scriviFileCarico(righeFile);
			if (export) {
				carico.setFlagTra("T");
				if (daoCarichi.aggiorna(carico) == null)
					logger.error("Impossibile aggiornare lo stato di trasmissione del carico ID " + carico.getIdTestaPaki());
				else
					logger.info("Generazione completata per il carico ID " + carico.getIdTestaPaki());
			}
		}
		logger.info("Procedura terminata!");
	}
	
	private Articoli trovaArticolo(String idUnivoco) {
		if (!mappaArticoli.containsKey(idUnivoco)) {
			Articoli articolo = daoArticoli.trovaDaIDUnivoco(idUnivoco);
			if (articolo == null)
				logger.error("Impossibile trovare l'articolo con ID univoco: '" + idUnivoco + "'");
			mappaArticoli.put(idUnivoco, articolo);
		}
		return mappaArticoli.get(idUnivoco);
	}
	
	private HashMap<String, RigaCaricoDate> recuperaInfoCarico(List<PakiArticolo> righe) {
		HashMap<String, RigaCaricoDate> mappaInfoCarico = new HashMap<>();
		for (PakiArticolo riga : righe) {
			String idUnivoco = riga.getCodUnicoArt();
			if (!mappaInfoCarico.containsKey(idUnivoco)) {
				Articoli articolo = trovaArticolo(idUnivoco);
				String codiceArticolo = articolo != null ? articolo.getCodArtStr() : "X"; //Anche qui il vecchio fa proprio così :/
				String barcode = articolo != null ? articolo.getBarraEAN() : "0000000000000"; //Il vecchio fa proprio così
				String numerata = articolo != null ? articolo.getNumerata() : "001";
				RigaCaricoDate info = new RigaCaricoDate(idUnivoco, codiceArticolo, barcode, numerata);
				mappaInfoCarico.put(idUnivoco, info);
			}
			RigaCaricoDate info = mappaInfoCarico.get(idUnivoco);
			info.setTotaleDichiarato(info.getTotaleDichiarato() + riga.getQtaPaki());
			info.setTotaleRiscontrato(info.getTotaleRiscontrato() + riga.getQtaVerificata());
		}
		return mappaInfoCarico;
	}
	
	private List<String> generaRigheFileCarico(PakiTesta carico, HashMap<String, RigaCaricoDate> righe) {
		List<String> righeFile = new LinkedList<String>();
		Date dataInizio = carico.getDataInizio() != null ? carico.getDataInizio() : now;
		String fornitore = carico.getRagSocFor() != null ? carico.getRagSocFor() : "D.A.T.E.";
		for (RigaCaricoDate riga : righe.values()) {
			StringBuilder line = new StringBuilder();
			line.append("IC"); //strTipoDo, fisso cominciano tutte così.
			line.append("          "); //strCodLog, fisso.
			line.append(su.getFormattedString(carico.getNrPaki(), 16)); //strNrDoc, 16 caratteri
			line.append(sdfToday.format(now)); //strDataDoc, sembra che prenda la data attuale
			line.append(sdfToday.format(dataInizio)); //strdatreg, il campo dataInizio sul pakitesta
			line.append("000000000"); //strCodFor, è stato messo fisso... :/
			line.append(su.getFormattedString(fornitore, 35)); //strRagSoc
			line.append("                              "); //strnonave, viene messa fissa
			line.append("0000000000000000"); //strdtpdta, viene messa fissa
			line.append(su.getFormattedString("", 115)); //115 spazi... :/
			line.append(riga.getBarcode()); //strbarra
			line.append(su.getFormattedString(riga.getCodiceArticolo(), 56)); //strCodArticolo
			line.append(su.getFormattedString(riga.getNumerata(), 7)); //strnumerata
			line.append("0000000000"); //strsezord
			line.append(su.getFormattedString(riga.getTotaleDichiarato(), 6));
			line.append(su.getFormattedString(riga.getTotaleRiscontrato(), 6));
			line.append(su.getFormattedString(riga.getTotaleDichiarato(), 6)); //li mette due volte, in teoria va a guardare la numerata ma sembra che faccia la stessa identica cosa sia per "001" che per "002"
			line.append(su.getFormattedString(riga.getTotaleRiscontrato(), 6));
			line.append(su.getFormattedString("", 227)); //227 spazi... :/
			righeFile.add(line.toString());
		}
		return righeFile;
	}
	
	private boolean scriviFileCarico(List<String> righe) {
		String nomeFile = "TR07_" + sdfNow.format(now) + ".TXT";
		String pathDocumento = pathFolderExport + nomeFile;
		boolean exportDocumento = FileUtility.writeFile(pathDocumento, righe);
		if (!exportDocumento)
			logger.error("Impossibile esportare il documento di carico: " + nomeFile);
		return exportDocumento;
	}

}
