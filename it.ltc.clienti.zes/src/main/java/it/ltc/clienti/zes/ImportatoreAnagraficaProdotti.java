package it.ltc.clienti.zes;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.prodotto.MProdotto;
import it.ltc.model.persistence.prodotto.ControllerProdottoSQLServer;
import it.ltc.utility.csv.FileCSV;

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

	public ImportatoreAnagraficaProdotti(String folderPath, String persistenceUnit) {
		super(persistenceUnit);
		this.folderPath = folderPath;
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
					for (MProdotto articolo : articoli) {
						try {
							valida(articolo);
							inserisci(articolo);
						} catch (ModelValidationException | ModelPersistenceException e) {
							logger.error(e);
						}						
					}
					spostaFileNelloStorico(file);
				} catch (Exception e) {
					logger.error(e);
					spostaFileConErrori(file);
				}				
			}
		}
	}
	
	private void spostaFileConErrori(File fileConErrori) {
		String nomeFile = fileConErrori.getName();
		String pathFolderErrori = folderPath + "errori\\";
		File fileDaSpostare = new File(pathFolderErrori + nomeFile);
		boolean spostato = fileConErrori.renameTo(fileDaSpostare);
		if (spostato) {
			logger.info("Spostato il file '" + nomeFile + "' in '" + pathFolderErrori + "'");
		}
	}
	
	private void spostaFileNelloStorico(File fileStorico) {
		String nomeFile = fileStorico.getName();
		String pathFolderStorico = folderPath + "storico\\";
		File fileDaSpostare = new File(pathFolderStorico + nomeFile);
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
			articolo.setCassa("");
			articolo.setComposizione("x");
			articoli.add(articolo);
		}
		return articoli;
	}
	

}
