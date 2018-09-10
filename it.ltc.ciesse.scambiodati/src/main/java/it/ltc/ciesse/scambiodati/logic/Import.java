package it.ltc.ciesse.scambiodati.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.ciesse.scambiodati.model.Articolo;
import it.ltc.ciesse.scambiodati.model.Assortimenti;
import it.ltc.ciesse.scambiodati.model.ClasseTaglie;
import it.ltc.ciesse.scambiodati.model.ClientiDestinatari;
import it.ltc.ciesse.scambiodati.model.Colli;
import it.ltc.ciesse.scambiodati.model.Colore;
import it.ltc.ciesse.scambiodati.model.DDTSpedizione;
import it.ltc.ciesse.scambiodati.model.DocumentiEntrataRighe;
import it.ltc.ciesse.scambiodati.model.DocumentiEntrataTestata;
import it.ltc.ciesse.scambiodati.model.Fornitore;
import it.ltc.ciesse.scambiodati.model.Nazione;
import it.ltc.ciesse.scambiodati.model.OrdiniRighe;
import it.ltc.ciesse.scambiodati.model.OrdiniTestata;
import it.ltc.ciesse.scambiodati.model.Stagione;
import it.ltc.ciesse.scambiodati.model.Vettori;
import it.ltc.database.dao.legacy.ArtibarDao;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.ColliPrelevaDao;
import it.ltc.database.dao.legacy.ColoriDao;
import it.ltc.database.dao.legacy.CorrieriDao;
import it.ltc.database.dao.legacy.DestinatariDao;
import it.ltc.database.dao.legacy.FornitoreDao;
import it.ltc.database.dao.legacy.NazioniDao;
import it.ltc.database.dao.legacy.NumerateDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.dao.legacy.RighiOrdineDao;
import it.ltc.database.dao.legacy.StagioniDao;
import it.ltc.database.dao.legacy.TempCorrDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.dao.legacy.bundle.CasseKitDao;
import it.ltc.database.model.legacy.ArtiBar;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.ColliPreleva;
import it.ltc.database.model.legacy.Colori;
import it.ltc.database.model.legacy.Corrieri;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.database.model.legacy.Nazioni;
import it.ltc.database.model.legacy.Numerata;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.Stagioni;
import it.ltc.database.model.legacy.TempCorr;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.bundle.CasseKIT;
import it.ltc.utility.miscellanea.file.FileUtility;

public class Import {
	
	private static final Logger logger = Logger.getLogger(Import.class);
	
	public static final String persistenceUnit = "legacy-test";
	
	public static final String PATH_CARTELLA_IMPORT = "\\\\192.168.0.10\\e$\\Gestionali\\Ciesse\\FTP\\IN";
	public static final String PATH_CARTELLA_IMPORT_STORICO = "\\\\192.168.0.10\\e$\\Gestionali\\Ciesse\\FTP\\IN\\storico\\";
	public static final String PATH_CARTELLA_IMPORT_ERRORI = "\\\\192.168.0.10\\e$\\Gestionali\\Ciesse\\FTP\\IN\\errori\\";
	
	private static Import instance;

	private Import() {}

	public static Import getInstance() {
		if (instance == null) {
			instance = new Import();
		}
		return instance;
	}
	
	/**
	 * Scarica i files dal server FTP e li deposita nella cartella di importazione.
	 */
	public void scaricaFiles() {
		
	}
	
	/**
	 * Importa i dati presenti nei files contenuti nella cartella di importazione.<br>
	 * I files importati correttamente vengono spostati nella cartella storico, i files che hanno provocato errori vengono spostati nella cartella errori.
	 */
	public void importaDati() {
		File folder = new File(PATH_CARTELLA_IMPORT);
		File[] files = folder.listFiles();
		List<File> filesDaImportare = new LinkedList<>();
		for (File file : files) {
			if (file.isFile()) {
				filesDaImportare.add(file);
			}
		}
		filesDaImportare.sort(new Ordinatore());
		//Arrays.sort(files, new Ordinatore());
		//Per ogni file contenuto nella cartella avvio una modalità diversa in base alla sua tipologia.
		for (File file : filesDaImportare) {
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.endsWith("chk")) {
					spostaFileNelloStorico(file);
				} else {
					String fileType = fileName.split("_|\\d")[0];
					switch (fileType) {
						case "Nazioni" : importaNazioni(file); break;
						case "Stagioni" : importaStagioni(file); break;
						case "ClasseTaglie" : importaTaglie(file); break;
						case "Colori" : importaColori(file); break;
						case "Articoli" : importaArticoli(file); break;
						case "Assortimenti" : importaAssortimenti(file); break;
						case "Clienti" : importaDestinatari(file); break;
						case "Fornitori" : importaFornitori(file); break;
						case "Vettori" : importaVettori(file); break;
						case "DocumentiEntrata" : importaTestateCarichi(file); break;
						case "RigheDocumentiEntrata" : importaRigheCarichi(file); break;
						case "OrdiniClienti" : importaTestateOrdini(file); break;
						case "RigheOrdiniClienti" : importaRigheOrdini(file); break;
						case "Colli" : importaColliDaSpedire(file); break;
						case "DDTSpedizione" : importaDatiSpedizioni(file); break;
						default : importaFileNonConforme(file); break;
					}
				}
			}
		}
		
	}

	private void spostaFileConErrori(File fileConErrori) {
		String nomeFile = fileConErrori.getName();
		File fileDaSpostare = new File(PATH_CARTELLA_IMPORT_ERRORI + nomeFile);
		boolean spostato = fileConErrori.renameTo(fileDaSpostare);
		if (spostato) {
			logger.info("Spostato il file '" + nomeFile + "' in '" + PATH_CARTELLA_IMPORT_ERRORI + "'");
		}
	}
	
	private void spostaFileNelloStorico(File fileStorico) {
		String nomeFile = fileStorico.getName();
		File fileDaSpostare = new File(PATH_CARTELLA_IMPORT_STORICO + nomeFile);
		boolean spostato = fileStorico.renameTo(fileDaSpostare);
		if (spostato) {
			logger.info("Spostato il file '" + nomeFile + "' in '" + PATH_CARTELLA_IMPORT_STORICO + "'");
		}
	}
	
	private void importaFileNonConforme(File fileNonConforme) {
		logger.warn("File con nome non conforme: '" + fileNonConforme.getName());
		//Sposto il file nella cartella degli errori.
		spostaFileConErrori(fileNonConforme);
	}
	
	private void importaNazioni(File fileNazioni) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileNazioni);
			List<Nazioni> nazioni = Nazione.parsaNazioni(lines);
			NazioniDao daoNazioni = new NazioniDao(persistenceUnit);
			for (Nazioni nazione : nazioni) {
				Nazioni entity = daoNazioni.trovaDaCodiceISO(nazione.getCodIso());
				if (entity == null) {
					entity = daoNazioni.inserisci(nazione);
					if (entity == null) throw new RuntimeException("Impossibile inserire la nazione: " + nazione.toString());
				}				
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileNazioni);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileNazioni);
		}
	}
	
	private void importaTaglie(File fileTaglie) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileTaglie);
			List<Numerata> numerate = ClasseTaglie.parsaTaglie(lines);
			NumerateDao daoNumerate = new NumerateDao(persistenceUnit);
			//Inserisco quelle nuove
			for (Numerata numerata : numerate) {
				Numerata entity = daoNumerate.trovaDaCodice(numerata.getCodice());
				if (entity == null) {
					entity = daoNumerate.inserisci(numerata);
					if (entity == null) throw new RuntimeException("Impossibile inserire la numerata: " + numerata.toString());
				} else { //Era già presente, vado in aggiornamento.
					numerata.setIdNumerata(entity.getIdNumerata());
					entity = daoNumerate.aggiorna(numerata);
					if (entity == null) throw new RuntimeException("Impossibile aggiornare la numerata: " + numerata.toString());
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileTaglie);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileTaglie);
		}
	}
	
	private void importaColori(File fileColori) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileColori);
			List<Colori> colori = Colore.parseColori(lines);
			ColoriDao daoColori = new ColoriDao(persistenceUnit);
			for (Colori colore : colori) {
				Colori entity = daoColori.trovaDaCodice(colore.getCodice());
				if (entity != null) {
					entity = daoColori.aggiorna(colore);
					if (entity == null) throw new RuntimeException("Impossibile aggiornare il colore: " + colore.toString());
				} else {
					entity = daoColori.inserisci(colore);
					if (entity == null) throw new RuntimeException("Impossibile inserire il colore: " + colore.toString());
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileColori);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileColori);
		}		
	}
	
	private void importaAssortimenti(File fileAssortimenti) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileAssortimenti);
			List<CasseKIT> kits = Assortimenti.parsaKitArticoli(lines);
			CasseKitDao daoKit = new CasseKitDao(persistenceUnit);
			for (CasseKIT kit : kits) {
				CasseKIT entity = daoKit.trovaDaSkuBundleEProdotto(kit.getSkuBundle(), kit.getSkuProdotto());
				if (entity == null) {
					entity = daoKit.inserisci(kit);
					if (entity == null) {
						throw new RuntimeException("Impossibile inserire il kit: " + kit.toString());
					}
				} else {
					kit.setId(entity.getId());
					entity = daoKit.aggiorna(kit);
					if (entity == null) {
						throw new RuntimeException("Impossibile aggiornare il kit: " + kit.toString());
					}
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileAssortimenti);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileAssortimenti);
		}
	}
	
	private void importaArticoli(File fileArticoli) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileArticoli);
			List<Articoli> articoli = Articolo.parsaArticoli(lines);
			ArticoliDao daoArticoli = new ArticoliDao(persistenceUnit);
			ArtibarDao daoBarcode = new ArtibarDao(persistenceUnit);
			for (Articoli articolo : articoli) {
				Articoli entity = daoArticoli.trovaDaSKU(articolo.getCodArtStr());
				if (entity == null) {
					entity = daoArticoli.inserisci(articolo);
					if (entity != null) {
						ArtiBar barcode = new ArtiBar();
						barcode.setBarraEAN(entity.getBarraEAN());
						barcode.setBarraUPC(entity.getBarraUPC());
						barcode.setCodiceArticolo(entity.getCodArtStr());
						barcode.setIdUniArticolo(entity.getIdUniArticolo());
						barcode.setTaglia(entity.getTaglia());
						barcode = daoBarcode.inserisci(barcode);
						if (barcode == null)
							throw new RuntimeException("Impossibile inserire il barcode: " + entity.getBarraEAN());
					} else {
						throw new RuntimeException("Impossibile inserire l'articolo: " + articolo.toString());
					}
				} else {
					articolo.setIdArticolo(entity.getIdArticolo());
					entity = daoArticoli.aggiorna(articolo);
					if (entity == null) {
						throw new RuntimeException("Impossibile aggiornare l'articolo: " + articolo.toString());
					} else {
						ArtiBar barcode = daoBarcode.trovaDaSKU(entity.getCodArtStr());
						barcode.setBarraEAN(articolo.getBarraEAN());
						barcode.setBarraUPC(articolo.getBarraUPC());
						barcode = daoBarcode.aggiorna(barcode);
						if (barcode == null)
							throw new RuntimeException("Impossibile aggiornare il barcode: " + entity.getBarraEAN());
					}
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileArticoli);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileArticoli);
		}	
	}
	
	private void importaDestinatari(File fileDestinatari) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileDestinatari);
			List<Destinatari> destinatari = ClientiDestinatari.parsaDestinatari(lines);
			DestinatariDao daoDestinatari = new DestinatariDao(persistenceUnit);
			for (Destinatari destinatario : destinatari) {
				Destinatari entity = daoDestinatari.trovaDaCodice(destinatario.getCodDestina());
				if (entity != null) {
					destinatario.setIdDestina(entity.getIdDestina());
					entity = daoDestinatari.aggiorna(destinatario);
					if (entity == null) 
						throw new RuntimeException("Impossibile aggiornare il destinatario: " + destinatario.getCodDestina());
				} else {
					entity = daoDestinatari.inserisci(destinatario);
					if (entity == null) 
						throw new RuntimeException("Impossibile inserire il destinatario: " + destinatario.getCodDestina());
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileDestinatari);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileDestinatari);
		}	
	}
	
	private void importaFornitori(File fileFornitori) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileFornitori);
			List<Fornitori> fornitori = Fornitore.parsaFornitori(lines);
			FornitoreDao daoFornitori = new FornitoreDao(persistenceUnit);
			for (Fornitori fornitore : fornitori) {
				Fornitori entity = daoFornitori.trovaDaCodice(fornitore.getCodiceFornitore());
				if (entity != null) {
					fornitore.setIdFornitore(entity.getIdFornitore());
					entity = daoFornitori.aggiorna(fornitore);
					if (entity == null) 
						throw new RuntimeException("Impossibile aggiornare il fornitore: " + fornitore.getCodiceFornitore());
				} else {
					entity = daoFornitori.inserisci(fornitore);
					if (entity == null) 
						throw new RuntimeException("Impossibile inserire il fornitore: " + fornitore.getCodiceFornitore());
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileFornitori);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileFornitori);
		}
	}
	
	private void importaVettori(File fileVettori) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileVettori);
			List<Corrieri> vettori = Vettori.parsaCorrieri(lines);
			CorrieriDao daoVettori = new CorrieriDao(persistenceUnit);
			for (Corrieri vettore : vettori) {
				Corrieri entity = daoVettori.trovaDaCodiceCliente(vettore.getCodiceCliente());
				if (entity != null) {
					vettore.setIdCorriere(entity.getIdCorriere());
					entity = daoVettori.aggiorna(vettore);
					if (entity == null) 
						throw new RuntimeException("Impossibile aggiornare il vettore: " + vettore.getCodiceCliente());
				} else {
					entity = daoVettori.inserisci(vettore);
					if (entity == null) 
						throw new RuntimeException("Impossibile inserire il vettore: " + vettore.getCodiceCliente());
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileVettori);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileVettori);
		}
	}
	
	private void importaStagioni(File fileStagioni) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileStagioni);
			List<Stagioni> stagioni = Stagione.parsaStagioni(lines);
			StagioniDao daoStagioni = new StagioniDao(persistenceUnit);
			for (Stagioni stagione : stagioni) {
				Stagioni entity = daoStagioni.trovaDaCodice(stagione.getCodice());
				if (entity != null) {
					stagione.setIdStagione(entity.getIdStagione());
					entity = daoStagioni.aggiorna(stagione);
					if (entity == null) 
						throw new RuntimeException("Impossibile aggiornare la stagione: " + stagione.getCodice());
				} else {
					entity = daoStagioni.inserisci(stagione);
					if (entity == null) 
						throw new RuntimeException("Impossibile inserire la stagione: " + stagione.getCodice());
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileStagioni);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileStagioni);
		}
	}
	
	private void importaTestateCarichi(File fileTestateCarichi) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileTestateCarichi);
			List<PakiTesta> testate = DocumentiEntrataTestata.parsaTestate(lines);
			PakiTestaDao daoTestate = new PakiTestaDao(persistenceUnit);
			for (PakiTesta testata : testate) {
				//Controllo se mi hanno chiesto di eliminarla e se lo stato è congruente con la richiesta di eliminazione.
				if (DocumentiEntrataTestata.CONDIZIONE_ELIMINA.equals(testata.getFlussoDichiarato())) {
					PakiTesta entity = daoTestate.trovaDaRiferimento(testata.getNrPaki());
					if (entity == null)
						logger.error("La testata del documento di carico: " + testata.getNrPaki() + " non esiste e non sarà eliminata.");
					else {
						entity = daoTestate.elimina(testata);
						if (entity == null) 
							throw new RuntimeException("Impossibile eliminare la testata del documento di carico: " + testata.getNrPaki());
					}
				} else { //Altrimenti procedo come di consueto con inserimenti/aggiornamenti
					PakiTesta entity = daoTestate.trovaDaRiferimento(testata.getNrPaki());
					if (entity != null) {
						testata.setIdTestaPaki(entity.getIdTestaPaki());
						entity = daoTestate.aggiorna(testata);
						if (entity == null) 
								throw new RuntimeException("Impossibile aggiornare la testata del documento di carico: " + testata.getNrPaki());
					} else {
						entity = daoTestate.inserisci(testata);
						if (entity == null) 
							throw new RuntimeException("Impossibile inserire la testata del documento di carico: " + testata.getNrPaki());
					}
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileTestateCarichi);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileTestateCarichi);
		}
	}
	
	private void importaRigheCarichi(File fileRigheCarichi) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileRigheCarichi);
			List<PakiArticolo> righe = DocumentiEntrataRighe.parsaRigheDocumento(lines);
			PakiArticoloDao daoRighe = new PakiArticoloDao(persistenceUnit);
			for (PakiArticolo riga : righe) {
				//Controllo se mi hanno chiesto di eliminarla e se lo stato è congruente con la richiesta di eliminazione.
				if (DocumentiEntrataTestata.CONDIZIONE_ELIMINA.equals(riga.getNrDispo())) {
					List<PakiArticolo> entities = daoRighe.trovaRigheDaCaricoENumeroRiga(riga.getIdPakiTesta(), riga.getRigaPacki());
					if (entities.isEmpty())
						logger.error("Nessuna riga trovata per il documento di carico ID: " + riga.getIdPakiTesta() + " e numero riga: " + riga.getRigaPacki() + ", non saranno eliminate.");
					else {
						for (PakiArticolo entity : entities) {
							if (daoRighe.elimina(entity) == null) 
								throw new RuntimeException("Impossibile eliminare la riga ID: " + riga.getIdPakiArticolo());
						}
						
					}
				} else { //Altrimenti procedo come di consueto con inserimenti/aggiornamenti
					List<PakiArticolo> entities = daoRighe.trovaRigheDaCaricoEProdotto(riga.getIdPakiTesta(), riga.getCodUnicoArt());
					if (entities.size() == 1) {
						PakiArticolo entity = entities.get(0);
						riga.setIdPakiArticolo(entity.getIdPakiArticolo());
						entity = daoRighe.aggiorna(riga);
						if (entity == null) 
								throw new RuntimeException("Impossibile aggiornare la riga del documento di carico ID: " + riga.getIdPakiArticolo());
					} else if (entities.size() == 0) {
						PakiArticolo entity = daoRighe.inserisci(riga);
						if (entity == null) 
							throw new RuntimeException("Impossibile inserire la riga del documento di carico con l'articolo: " + riga.getCodUnicoArt());
					}
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileRigheCarichi);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileRigheCarichi);
		}
	}
	
	private void importaTestateOrdini(File fileTestateOrdini) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileTestateOrdini);
			List<TestataOrdini> testate = OrdiniTestata.parsaOrdini(lines);
			TestataOrdiniDao daoTestate = new TestataOrdiniDao(persistenceUnit);
			for (TestataOrdini testata : testate) {
				String riferimento = testata.getRifOrdineCli();
				//Controllo se mi hanno chiesto di eliminarla e se lo stato è congruente con la richiesta di eliminazione.
				if (OrdiniTestata.CANCELLAZIONE.equals(testata.getTipoDoc())) {
					TestataOrdini entity = daoTestate.trovaDaRiferimento(riferimento);
					if (entity == null)
						logger.error("La testata dell'ordine: " + riferimento + " non esiste e non sarà eliminata.");
					else {
						entity = daoTestate.elimina(testata);
						if (entity == null) 
							throw new RuntimeException("Impossibile eliminare la testata dell'ordine: " + riferimento);
					}
				} else { //Altrimenti procedo come di consueto con inserimenti/aggiornamenti
					TestataOrdini entity = daoTestate.trovaDaRiferimento(testata.getRifOrdineCli());
					if (entity != null) {
						testata.setIdTestaSped(entity.getIdTestaSped());
						entity = daoTestate.aggiorna(testata);
						if (entity == null) 
								throw new RuntimeException("Impossibile aggiornare la testata dell'ordine: " + riferimento);
					} else {
						entity = daoTestate.inserisci(testata);
						if (entity == null) 
							throw new RuntimeException("Impossibile inserire la testata dell'ordine: " + riferimento);
					}
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileTestateOrdini);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileTestateOrdini);
		}
	}
	
	private void importaRigheOrdini(File fileRigheOrdini) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileRigheOrdini);
			List<RighiOrdine> righe = OrdiniRighe.parsaRigheOrdine(lines);
			RighiOrdineDao daoRighe = new RighiOrdineDao(persistenceUnit);
			for (RighiOrdine riga : righe) {
				//Controllo se mi hanno chiesto di eliminarla e se lo stato è congruente con la richiesta di eliminazione.
				if (OrdiniRighe.CANCELLAZIONE.equals(riga.getTipoord())) {
					List<RighiOrdine> entities = daoRighe.trovaRigheDaOrdineENumeroRiga(riga.getIdTestataOrdine(), riga.getNrRigo());
					if (entities.isEmpty())
						logger.error("Nessuna riga trovata per l'ordine ID: " + riga.getIdTestataOrdine() + " e numero riga: " + riga.getNrRigo() + ", non saranno eliminate.");
					else {
						for (RighiOrdine entity : entities) {
							if (daoRighe.elimina(entity) == null) 
								throw new RuntimeException("Impossibile eliminare la riga d'ordine ID: " + riga.getIdRigoOrdine());
						}
						
					}
				} else { //Altrimenti procedo come di consueto con inserimenti/aggiornamenti
					List<RighiOrdine> entities = daoRighe.trovaRigheDaOrdineEProdotto(riga.getIdTestataOrdine(), riga.getIdUnicoArt());
					if (entities.size() == 1) {
						RighiOrdine entity = entities.get(0);
						riga.setIdRigoOrdine(entity.getIdRigoOrdine());
						entity = daoRighe.aggiorna(riga);
						if (entity == null) 
								throw new RuntimeException("Impossibile aggiornare la riga d'ordine ID: " + riga.getIdRigoOrdine());
					} else if (entities.size() == 0) {
						RighiOrdine entity = daoRighe.inserisci(riga);
						if (entity == null) 
							throw new RuntimeException("Impossibile inserire la riga d'ordine con l'articolo: " + riga.getIdUnicoArt());
					}
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileRigheOrdini);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileRigheOrdini);
		}
	}
	
	private void importaColliDaSpedire(File fileColli) {
		try {
			ColliPrelevaDao daoColli = new ColliPrelevaDao(persistenceUnit);
			ArrayList<String> lines = FileUtility.readLines(fileColli);
			List<ColliPreleva> colli = Colli.importaColli(lines);
			for (ColliPreleva collo : colli) {
				//Verifico se è già presente oppure no
				ColliPreleva entity = daoColli.trovaDaNumeroCollo(collo.getKeyColloPre());
				if (entity == null) {
					entity = daoColli.inserisci(collo);
					if (entity == null) 
						throw new RuntimeException("Impossibile inserire il collo da prelevare: " + collo.getKeyColloPre());
				} else {
					collo.setIdColliPreleva(entity.getIdColliPreleva());
					entity = daoColli.aggiorna(collo);
					if (entity == null) 
						throw new RuntimeException("Impossibile aggiornare il collo da prelevare: " + collo.getKeyColloPre());
				}				
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileColli);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileColli);
		}
	}
	
	private void importaDatiSpedizioni(File fileSpedizioni) {
		try {
			TempCorrDao daoSpedizioni = new TempCorrDao(persistenceUnit);
			ArrayList<String> lines = FileUtility.readLines(fileSpedizioni);
			List<TempCorr> spedizioni = DDTSpedizione.parsaDDT(lines);
			for (TempCorr spedizione : spedizioni) {
				//Verifico se è già presente oppure no
				TempCorr entity = daoSpedizioni.trovaDaNumeroLista(spedizione.getNrLista());
				if (entity == null) {
					entity = daoSpedizioni.inserisci(spedizione);
					if (entity == null) 
						throw new RuntimeException("Impossibile inserire i dati sulla spedizione con numero di lista: " + spedizione.getNrLista());
				} else {
					spedizione.setIdTempCor(entity.getIdTempCor());
					entity = daoSpedizioni.aggiorna(spedizione);
					if (entity == null) 
						throw new RuntimeException("Impossibile aggiornare i dati sulla spedizione con numero di lista: " + spedizione.getNrLista());
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileSpedizioni);
		} catch (Exception e) {
			logger.error(e);
			spostaFileConErrori(fileSpedizioni);
		}
	}

}
