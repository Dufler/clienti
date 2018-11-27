package it.ltc.clienti.artcraft.carichi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.clienti.artcraft.ConfigurationUtility;
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
	
	private static EsportaCarichi instance;
	
	private final StringUtility su;
	private final SimpleDateFormat sdfToday;
	private final SimpleDateFormat sdfNow;
	private Date now;
	private final HashMap<String, Articoli> mappaArticoli;
	
	private final String persistenceUnit;
	private final String pathCartellaExport;
	
	private final ArticoliDao daoArticoli;
	private final PakiTestaDao daoCarichi;
	private final PakiArticoloDao daoRighe;

	private EsportaCarichi() {
		su = new StringUtility();
		sdfToday = new SimpleDateFormat("ddMMyyyy");
		sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
		mappaArticoli = new HashMap<>();
		persistenceUnit = ConfigurationUtility.getInstance().getPersistenceUnit();
		pathCartellaExport = ConfigurationUtility.getInstance().getLocalFolderOUT();
		daoArticoli = new ArticoliDao(persistenceUnit);
		daoCarichi = new PakiTestaDao(persistenceUnit);
		daoRighe = new PakiArticoloDao(persistenceUnit);
	}

	public static EsportaCarichi getInstance() {
		if (instance == null) {
			instance = new EsportaCarichi();
		}
		return instance;
	}
	
	public void esportaCarichi() {
		logger.info("Avvio la procedura di esportazione dei carichi");
		//trovo i carichi pronti per essere esportati
		List<PakiTesta> carichi = daoCarichi.trovaCarichiChiusiDaEsportare();
		logger.info(carichi.isEmpty() ? "Non sono stati trovati carichi da esportare." : "Stanno per essere esportati " + carichi.size() + " carichi.");
		for (PakiTesta carico : carichi) {
			//imposto la data, aspetto un secondo per evitare sovrapposizioni
			try { Thread.sleep(1001); } catch (Exception e) { logger.error(e); }
			now = new Date();
			//per ogni carico trovato recupero le righe collegate
			List<PakiArticolo> righeCarico = daoRighe.trovaRigheDaCarico(carico.getIdTestaPaki());
			//genero le righe corrispondenti
			HashMap<String, RigaCaricoArtcraft> infoCarico = recuperaInfoCarico(righeCarico);
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
	
	private HashMap<String, RigaCaricoArtcraft> recuperaInfoCarico(List<PakiArticolo> righe) {
		HashMap<String, RigaCaricoArtcraft> mappaInfoCarico = new HashMap<>();
		for (PakiArticolo riga : righe) {
			String idUnivoco = riga.getCodUnicoArt();
			String magazzino = riga.getMagazzino();
			String key = idUnivoco + "§" + magazzino;
			if (!mappaInfoCarico.containsKey(key)) {
				Articoli articolo = trovaArticolo(idUnivoco);
				String codiceArticolo = articolo != null ? articolo.getCodArtStr() : "X"; //Anche qui il vecchio fa proprio così :/
				String barcode = articolo != null ? articolo.getBarraEAN() : "0000000000000"; //Il vecchio fa proprio così
				boolean cassa = articolo != null ? "CASSE".equals(articolo.getUm()) : false;
				String barcodeProdotto = cassa ? "" : barcode;
				String barcodeCassa = cassa ? barcode : "";
				String numerata = articolo != null ? articolo.getNumerata() : "001";
				int posizione = articolo.getUmPos();
				String ordineFornitore = riga.getNrOrdineFor();
				RigaCaricoArtcraft info = new RigaCaricoArtcraft(codiceArticolo, numerata, posizione, magazzino, barcodeProdotto, barcodeCassa, ordineFornitore);
				mappaInfoCarico.put(key, info);
			}
			RigaCaricoArtcraft info = mappaInfoCarico.get(key);
			info.setTotaleDichiarato(info.getTotaleDichiarato() + riga.getQtaPaki());
			info.setTotaleRiscontrato(info.getTotaleRiscontrato() + riga.getQtaVerificata());
		}
		return mappaInfoCarico;
	}
	
	private List<String> generaRigheFileCarico(PakiTesta carico, HashMap<String, RigaCaricoArtcraft> righe) {
		List<String> righeFile = new LinkedList<String>();
		Date dataCarico = carico.getDataPaki() != null ? carico.getDataPaki() : now;
		String codiceFornitore = carico.getCodFornitore() != null ? carico.getCodFornitore() : "21870";
		String fornitore = carico.getRagSocFor() != null ? carico.getRagSocFor() : "CROCS EUROPE B.V.";
		for (RigaCaricoArtcraft riga : righe.values()) {
			StringBuilder line = new StringBuilder();
			line.append(su.getFormattedString(carico.getNrPaki(), 20)); //strNrDoc, 16 caratteri
			line.append(sdfToday.format(dataCarico)); //strDataDoc, se non c'è prende la data attuale.
			line.append(su.getFormattedString(codiceFornitore, 20)); //strCodFor
			line.append(su.getFormattedString(fornitore, 35)); //strRagSoc
			line.append("000000"); //strNrCollo, viene messa fissa
			line.append(su.getFormattedString(riga.getCodiceArticolo(), 40)); //strCodArticolo
			line.append(su.getFormattedString(riga.getNumerata(), 10)); //strnumerata
			line.append(su.getFormattedString(riga.getPosizione(), 2)); //strPosizione
			line.append("                    "); //spazio vuoto
			line.append(su.getFormattedString(riga.getTotaleDichiarato(), 5));
			line.append(su.getFormattedString(riga.getTotaleRiscontrato(), 5));
			line.append("0"); //ci mette uno 0
			line.append(su.getFormattedString(riga.getMagazzino(), 3)); //rstamagazzino, quanti caratteri? //FIXME: Il codice sul vecchio è probabilmente sbagliato in fase di importazione. Ho trovato su un file d'esempio il magazzino RE-01 che non esiste nella tabella magazzini.
			line.append(su.getFormattedString(carico.getTipodocumento(), 10)); //strTipodocumento
			line.append(su.getFormattedString(riga.getBarcodeCassa(), 40)); //strCasse
			line.append(su.getFormattedString(riga.getOrdineFornitore(), 20)); //strNrOrdineFor
			line.append(su.getFormattedString(riga.getBarcodeProdotto(), 40)); //strBarra
			righeFile.add(line.toString());
		}
		return righeFile;
	}
	
	private boolean scriviFileCarico(List<String> righe) {
		String nomeFile = "TR07_" + sdfNow.format(now) + ".TXT";
		String pathDocumento = pathCartellaExport + nomeFile;
		boolean exportDocumento = FileUtility.writeFile(pathDocumento, righe);
		if (!exportDocumento)
			logger.error("Impossibile esportare il documento di carico: " + nomeFile);
		return exportDocumento;
	}

}
