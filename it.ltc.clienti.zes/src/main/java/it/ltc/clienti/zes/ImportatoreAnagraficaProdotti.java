package it.ltc.clienti.zes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.model.interfaces.exception.ModelAlreadyExistentException;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.prodotto.Cassa;
import it.ltc.model.interfaces.prodotto.MProdotto;
import it.ltc.model.persistence.prodotto.ControllerProdottoSQLServer;
import it.ltc.utility.csv.FileCSV;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;

public class ImportatoreAnagraficaProdotti extends ControllerProdottoSQLServer {
	
	private static final Logger logger = Logger.getLogger(ImportatoreAnagraficaProdotti.class);
	
	public static final String regexFileArticoli = "TA_\\d{6}_\\d{6}\\.csv";
	
	private static final String MODELLO = "modello";
	private static final String BARCODE = "barcode";
	private static final String DESCRIZIONE = "descr";
	private static final String COLORE = "col";
	private static final String TAGLIA = "taglia";
	private static final String STAGIONE = "stagione";
	private static final String CATEGORIA_MERCEOLOGICA = "tipologia";
	private static final String BRAND = "brand";
	private static final String CODICE_ARTICOLO = "chiavecliente";
	private static final String MADE_IN = "madein";
	private static final String PESO = "peso";
	private static final String ALTEZZA = "h";
	private static final String LUNGHEZZA = "l";
	private static final String LARGHEZZA = "z";
	private static final String VALORE_DOGANALE = "valoredoganale";
	
	private final String folderPath;
	private final SimpleDateFormat sdf;
	private final MailMan postino;
	private final List<String> destinatari;

	public ImportatoreAnagraficaProdotti(String folderPath, String persistenceUnit) {
		super(persistenceUnit);
		this.folderPath = folderPath;
		this.sdf = new SimpleDateFormat("yyMMddHHmmss");
		this.postino = ConfigurationUtility.getInstance().getMailMan();
		this.destinatari = ConfigurationUtility.getInstance().getIndirizziDestinatari();
	}
	
	public void importaAnagrafiche() {
		//recupero i files che contengono gli ordini
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				continue;
			} else if (file.isFile() && file.getName().matches(regexFileArticoli)) {				
				//Tento di leggere il file
				try {
					logger.info("Esamino il file '" + file.getName() + "'");
					//Vado ad estrarre le info contenute nel csv
					FileCSV csv = FileCSV.leggiFile(file);
					//parso le informazioni sugli ordini
					Collection<MProdotto> articoli = parsaArticoli(csv);
					logger.info("Sono state trovate " + articoli.size() + " anagrafiche nel file.");
					//li inserisco a sistema.
					int nuoveAnagrafiche = 0;
					int anagraficheGiàPresenti = 0;
					List<String> anagraficheErrate = new LinkedList<>();
					for (MProdotto articolo : articoli) {
						try {
							valida(articolo);
							inserisci(articolo);
							nuoveAnagrafiche++;
						} catch (ModelAlreadyExistentException e) { 
							logger.error(e.getMessage(), e);
							anagraficheGiàPresenti++;
						} catch (ModelValidationException | ModelPersistenceException e) {
							anagraficheErrate.add(articolo.getChiaveCliente());
							logger.error(e.getMessage(), e);
						}						
					}
					spostaFileNelloStorico(file);
					String subject = "ZeS - Importazione Anagrafica Prodotti";
					StringBuilder body = new StringBuilder("Sono stati importati " + nuoveAnagrafiche + " nuovi articoli su " + articoli.size() + ".");
					if (anagraficheGiàPresenti > 0) {
						body.append("\r\n");
						body.append(anagraficheGiàPresenti);
						body.append(" anagrafiche erano già presenti a sistema.");
					}
					if (!anagraficheErrate.isEmpty()) {
						body.append("\r\n");
						body.append("Le seguenti anagrafiche non sono state importate a causa di problemi:");
						for (String problema : anagraficheErrate) {
							body.append("\r\n");
							body.append(problema);
						}
					}
					Email mail = new Email(subject, body.toString());
					boolean invio = postino.invia(destinatari, mail);
					if (!invio) {
						logger.error("Impossibile inviare la mail con il riepilogo dell'importazione anagrafiche.");
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					spostaFileConErrori(file);
					String subject = "Alert: ZeS - Importazione Anagrafica Prodotti";
					String body = "Si sono verificati errori durante l'importazione delle anagrafiche: " + e.getMessage();
					Email mail = new Email(subject, body);
					boolean invio = postino.invia(destinatari, mail);
					if (!invio) {
						logger.error("Impossibile inviare la mail con gli errori riscontrati durante l'importazione anagrafiche.");
					}
				}				
			}
		}
	}
	
	private void spostaFileConErrori(File fileConErrori) {
		String nomeFile = fileConErrori.getName();
		String pathFolderErrori = folderPath + "errori\\";
		String dataOraLavorazione = sdf.format(new Date());
		File fileDaSpostare = new File(pathFolderErrori + dataOraLavorazione + "_" + nomeFile);
		boolean spostato = fileConErrori.renameTo(fileDaSpostare);
		if (spostato) {
			logger.info("Spostato il file '" + nomeFile + "' in '" + pathFolderErrori + "'");
		}
	}
	
	private void spostaFileNelloStorico(File fileStorico) {
		String nomeFile = fileStorico.getName();
		String pathFolderStorico = folderPath + "storico\\";
		String dataOraLavorazione = sdf.format(new Date());
		File fileDaSpostare = new File(pathFolderStorico + dataOraLavorazione + "_" + nomeFile);
		boolean spostato = fileStorico.renameTo(fileDaSpostare);
		if (spostato) {
			logger.info("Spostato il file '" + nomeFile + "' in '" + pathFolderStorico + "'");
		}
	}
	
	private Collection<MProdotto> parsaArticoli(FileCSV csv) {
		LinkedList<MProdotto> articoli = new LinkedList<>();
		while (csv.prossimaRiga()) {
			MProdotto articolo = new MProdotto();
			articolo.setBarcode(csv.getStringa(BARCODE));
			articolo.setBarcodeFornitore(csv.getStringa(BARCODE));
			articolo.setBrand(csv.getStringa(BRAND));
			articolo.setCategoria(csv.getStringa(CATEGORIA_MERCEOLOGICA));
			articolo.setChiaveCliente(csv.getStringa(CODICE_ARTICOLO));
			articolo.setCodiceModello(csv.getStringa(MODELLO));
			articolo.setColore(csv.getStringa(COLORE));
			articolo.setDescrizione(csv.getStringa(DESCRIZIONE));
			articolo.setH(csv.getIntero(ALTEZZA));
			articolo.setL(csv.getIntero(LUNGHEZZA));
			articolo.setZ(csv.getIntero(LARGHEZZA));
			articolo.setMadeIn(csv.getStringa(MADE_IN));
			articolo.setPeso(csv.getIntero(PESO));
			articolo.setStagione(csv.getStringa(STAGIONE));
			articolo.setTaglia(csv.getStringa(TAGLIA));
			articolo.setValore(csv.getNumerico(VALORE_DOGANALE));
			//Fissi per ZeS
			articolo.setCassa(Cassa.NO);
			articolo.setComposizione("x");
			articoli.add(articolo);
		}
		return articoli;
	}
	

}
