package it.ltc.ciesse.scambiodati.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
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
import it.ltc.ciesse.scambiodati.model.NazioneCliente;
import it.ltc.ciesse.scambiodati.model.OrdiniRighe;
import it.ltc.ciesse.scambiodati.model.OrdiniTestata;
import it.ltc.ciesse.scambiodati.model.Stagione;
import it.ltc.ciesse.scambiodati.model.Vettori;
import it.ltc.database.dao.legacy.ColliPrelevaDao;
import it.ltc.database.dao.legacy.ColoriDao;
import it.ltc.database.dao.legacy.CorrieriDao;
import it.ltc.database.dao.legacy.DestinatariDao;
import it.ltc.database.dao.legacy.FornitoreDao;
import it.ltc.database.dao.legacy.NazioniDao;
import it.ltc.database.dao.legacy.NumerateDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.dao.legacy.StagioniDao;
import it.ltc.database.dao.legacy.TempCorrDao;
import it.ltc.database.dao.legacy.bundle.CasseDao;
import it.ltc.database.model.legacy.ColliPreleva;
import it.ltc.database.model.legacy.Colori;
import it.ltc.database.model.legacy.Corrieri;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.database.model.legacy.Nazioni;
import it.ltc.database.model.legacy.NumerataLegacy;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.database.model.legacy.Stagioni;
import it.ltc.database.model.legacy.TempCorr;
import it.ltc.database.model.legacy.bundle.Casse;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.exception.ProductAlreadyExistentException;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.prodotto.MProdotto;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;
import it.ltc.utility.miscellanea.file.FileUtility;

public class Import {
	
	private static final Logger logger = Logger.getLogger(Import.class);
	
	private final String persistenceUnit;
	
	private final String pathCartellaImport;
	private final String pathCartellaImportStorico;
	private final String pathCartellaImportErrori;
	private final String pathCartellaImportNonUsati;
	
	private static Import instance;
	
	private final HashMap<String, MOrdine> mappaOrdini;
	private final HashMap<String, File> mappaFileTestateOrdini;
	private final HashMap<String, File> mappaFileRigheOrdini;
	
	private final List<String> messaggiInfo;
	private final List<String> messaggiErrore;

	private Import() {
		ConfigurationUtility config = ConfigurationUtility.getInstance();
		persistenceUnit = config.getPersistenceUnit();
		pathCartellaImport = config.getLocalFolderIN();
		pathCartellaImportStorico = config.getLocalFolderINStorico();
		pathCartellaImportErrori = config.getLocalFolderINErrori();
		pathCartellaImportNonUsati = config.getLocalFolderINNonUsati();
		mappaOrdini = new HashMap<>();
		mappaFileTestateOrdini = new HashMap<>();
		mappaFileRigheOrdini = new HashMap<>();
		messaggiInfo = new LinkedList<>();
		messaggiErrore = new LinkedList<>();
	}

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
		//Reset sulla lista di messaggi
		messaggiInfo.clear();
		messaggiErrore.clear();
		//Trovo i files da importare
		File folder = new File(pathCartellaImport);
		File[] files = folder.listFiles();
		List<File> filesDaImportare = new LinkedList<>();
		for (File file : files) {
			if (file.isFile()) {
				filesDaImportare.add(file);
			}
		}
		filesDaImportare.sort(new Ordinatore());
		//Per ogni file contenuto nella cartella avvio una modalità diversa in base alla sua tipologia.
		for (File file : filesDaImportare) {
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.endsWith("chk")) {
					importaFileNonConforme(file);
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
		//Se ho ricevuto ordini li inserisco a sistema
		if (!mappaOrdini.isEmpty())
			inserisciOrdini();
		//Invio una mail di riepilogo
		inviaMailRiepilogo();
	}
	
	private void inviaMailRiepilogo() {
		//Vado a fare una mail di riepilogo solo se ci sono messaggi.
		if (messaggiInfo.size() + messaggiErrore.size() > 0) {
			List<String> destinatari = ConfigurationUtility.getInstance().getIndirizziDestinatari();
			MailMan postino = ConfigurationUtility.getInstance().getMailMan();
			String subject = "Riepilogo Scambio Dati Ciesse";
			if (!messaggiErrore.isEmpty()) {
				subject = "Alert - " + subject;
				destinatari.addAll(ConfigurationUtility.getInstance().getIndirizziResponsabili());
			}
			StringBuilder body = new StringBuilder();
			for (String message : messaggiInfo) {
				body.append(message);
				body.append("\r\n");
			}
			for (String message : messaggiErrore) {
				body.append(message);
				body.append("\r\n");
			}
			Email mail = new Email(subject, body.toString());
			postino.invia(destinatari, mail);
		}		
	}

	private void spostaFileConErrori(File fileConErrori) {
		String nomeFile = fileConErrori.getName();
		File fileDaSpostare = new File(pathCartellaImportErrori + nomeFile);
		boolean spostato = fileConErrori.renameTo(fileDaSpostare);
		if (spostato) {
			logger.info("Spostato il file '" + nomeFile + "' in '" + pathCartellaImportErrori + "'");
		}
	}
	
	private void spostaFileNelloStorico(File fileStorico) {
		String nomeFile = fileStorico.getName();
		File fileDaSpostare = new File(pathCartellaImportStorico + nomeFile);
		boolean spostato = fileStorico.renameTo(fileDaSpostare);
		if (spostato) {
			logger.info("Spostato il file '" + nomeFile + "' in '" + pathCartellaImportStorico + "'");
		}
	}
	
	private void importaFileNonConforme(File fileNonConforme) {
		logger.warn("File con nome non conforme: '" + fileNonConforme.getName());
		//Sposto il file nella cartella degli errori.
		String nomeFile = fileNonConforme.getName();
		File fileDaSpostare = new File(pathCartellaImportNonUsati + nomeFile);
		boolean spostato = fileNonConforme.renameTo(fileDaSpostare);
		if (spostato) {
			logger.info("Spostato il file '" + nomeFile + "' in '" + pathCartellaImportNonUsati + "'");
		}
	}
	
	private void importaNazioni(File fileNazioni) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileNazioni);
			List<Nazioni> nazioni = NazioneCliente.parsaNazioni(lines);
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
			messaggiErrore.add(e.getMessage());
			logger.error(e);
			spostaFileConErrori(fileNazioni);
		}
	}
	
	private void importaTaglie(File fileTaglie) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileTaglie);
			List<NumerataLegacy> numerate = ClasseTaglie.parsaTaglie(lines);
			NumerateDao daoNumerate = new NumerateDao(persistenceUnit);
			//Inserisco quelle nuove
			for (NumerataLegacy numerata : numerate) {
				NumerataLegacy entity = daoNumerate.trovaDaCodice(numerata.getCodice());
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
			messaggiErrore.add(e.getMessage());
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
			messaggiErrore.add(e.getMessage());
			logger.error(e);
			spostaFileConErrori(fileColori);
		}		
	}
	
	private void importaAssortimenti(File fileAssortimenti) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileAssortimenti);
			List<Casse> kits = Assortimenti.parsaKitArticoli(lines);
			CasseDao daoKit = new CasseDao(persistenceUnit);
			for (Casse kit : kits) {
				Casse entity = daoKit.trovaDaSkuBundleEProdotto(kit.getSkuBundle(), kit.getSkuProdotto());
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
			messaggiErrore.add(e.getMessage());
			logger.error(e);
			spostaFileConErrori(fileAssortimenti);
		}
	}
	
	private void importaArticoli(File fileArticoli) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileArticoli);
			List<MProdotto> articoli = Articolo.parsaArticoli(lines);
			ControllerArticoli controller = new ControllerArticoli();
			int prodottiInseriti = 0;
			for (MProdotto articolo : articoli) {
				try {
					controller.valida(articolo);
					controller.inserisci(articolo);
				} catch (ProductAlreadyExistentException e) {
					//DO NOTHING! Ce li ripassano in continuazione.
				} catch (ModelValidationException | ModelPersistenceException e) {
					logger.error(e);
					messaggiErrore.add(e.getMessage());
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileArticoli);
			if (prodottiInseriti > 0)
				messaggiInfo.add("Sono stati inseriti a sistema " + prodottiInseriti + " nuovi prodotti.");
		} catch (Exception e) {
			messaggiErrore.add(e.getMessage());
			logger.error(e);
			for (StackTraceElement trace : e.getStackTrace())
				logger.error(trace);
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
			messaggiErrore.add(e.getMessage());
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
			messaggiErrore.add(e.getMessage());
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
			messaggiErrore.add(e.getMessage());
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
			messaggiErrore.add(e.getMessage());
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
					else
						messaggiInfo.add("Inserito nuovo carico " + entity.getNrPaki());
				}
			}
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileTestateCarichi);
		} catch (Exception e) {
			messaggiErrore.add(e.getMessage());
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
				List<PakiArticolo> entities = daoRighe.trovaRigheDaCaricoNumeroRigaEProdotto(riga.getIdPakiTesta(), riga.getRigaPacki(), riga.getCodUnicoArt());
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
			//Sposto il file nella cartella di storico
			spostaFileNelloStorico(fileRigheCarichi);
		} catch (Exception e) {
			messaggiErrore.add(e.getMessage());
			logger.error(e);
			spostaFileConErrori(fileRigheCarichi);
		}
	}
	
	private void importaTestateOrdini(File fileTestateOrdini) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileTestateOrdini);
			List<MOrdine> ordini = OrdiniTestata.parsaOrdini(lines);
			for (MOrdine ordine : ordini) {
				mappaOrdini.put(ordine.getRiferimentoOrdine(), ordine);
				//Mi segno il file per spostarlo successivamente, al termine dell'importazione dell'ordine.
				mappaFileTestateOrdini.put(ordine.getRiferimentoOrdine(), fileTestateOrdini);
			}
			//Sposto il file nella cartella di storico
			//spostaFileNelloStorico(fileTestateOrdini);
		} catch (Exception e) {
			messaggiErrore.add(e.getMessage());
			logger.error(e);
			spostaFileConErrori(fileTestateOrdini);
		}
	}
	
	private void importaRigheOrdini(File fileRigheOrdini) {
		try {
			ArrayList<String> lines = FileUtility.readLines(fileRigheOrdini);
			String riferimentoOrdine = OrdiniRighe.parsaRigheOrdine(mappaOrdini, lines);
			mappaFileRigheOrdini.put(riferimentoOrdine, fileRigheOrdini);
			//Sposto il file nella cartella di storico
			//spostaFileNelloStorico(fileRigheOrdini);
		} catch (Exception e) {
			messaggiErrore.add(e.getMessage());
			logger.error(e);
			spostaFileConErrori(fileRigheOrdini);
		}
	}
	
	private void inserisciOrdini() {
		ContollerOrdini controller = new ContollerOrdini();
		for (MOrdine ordine : mappaOrdini.values()) {
			//Mi appunto i files da spostare contenenti le info sull'ordine
			File fileTestata = mappaFileTestateOrdini.get(ordine.getRiferimentoOrdine());
			File fileRighe = mappaFileRigheOrdini.get(ordine.getRiferimentoOrdine());
			//Vado in validazione e scrittura
			try {
				controller.valida(ordine);
				controller.inserisci(ordine);
				messaggiInfo.add("Inserito nuovo ordine " + ordine.getRiferimentoOrdine());
				//Sposto i files nello storico
				spostaFileNelloStorico(fileTestata);
				spostaFileNelloStorico(fileRighe);
			} catch (Exception e) {
				messaggiErrore.add(e.getMessage());
				logger.error(e);
				//Sposto i files nella cartella errori
				spostaFileConErrori(fileTestata);
				spostaFileConErrori(fileRighe);
			}			
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
			messaggiErrore.add(e.getMessage());
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
					else
						messaggiInfo.add("Inserito nuove info per spedizione ordine " + spedizione.getNrLista());
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
			messaggiErrore.add(e.getMessage());
			logger.error(e);
			spostaFileConErrori(fileSpedizioni);
		}
	}

}
