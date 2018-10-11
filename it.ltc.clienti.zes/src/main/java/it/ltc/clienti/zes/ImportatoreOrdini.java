package it.ltc.clienti.zes;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.ProdottoOrdinato;
import it.ltc.model.persistence.ordine.ControllerOrdiniSQLServer;
import it.ltc.utility.csv.FileCSV;

public class ImportatoreOrdini extends ControllerOrdiniSQLServer {

	private static final Logger logger = Logger.getLogger(ImportatoreOrdini.class);
	
	public static final String regexFileOrdini = "TO_\\d{6}_\\d{6}\\.csv";
	
	public static final String RIFERIMENTO = "ChiaveDocumento";
	public static final String DATA_CONSEGNA = "DataConsegna";
	public static final String DATA_DOCUMENTO = "DataDocumento";
	
	public static final String DESTINATARIO_RAGIONE_SOCIALE = "RagioneSociale_Destinatario";
	public static final String DESTINATARIO_INDIRIZZO = "Indirizzo_Destinatario";
	public static final String DESTINATARIO_LOCALITA = "Localita_Destinatario";
	public static final String DESTINATARIO_PROVINCIA = "Provincia_Destinatario";
	public static final String DESTINATARIO_CAP = "Cap_Destinatario";
	public static final String DESTINATARIO_NAZIONE = "SiglaISO_Destinatario";
	public static final String DESTINATARIO_TELEFONO = "Tel_Destinatario";
	public static final String DESTINATARIO_EMAIL = "Email_Destinatario";
	
	public static final String RIGA_NUMERO = "NumRiga";
	public static final String RIGA_BARCODE = "barcode";
	public static final String RIGA_QUANTITA = "qta";
	public static final String RIGA_MAGAZZINO = "magazzino";
	
	private final String folderPath;
	
	public ImportatoreOrdini(String folderPath, String persistenceUnit) {
		super(persistenceUnit);
		this.folderPath = folderPath;
	}
	
	public void importaOrdini() {
		//recupero i files che contengono gli ordini
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				continue;
			} else if (file.isFile() && file.getName().matches(regexFileOrdini)) {
				//Tento di leggere il file
				try {
					logger.info("Esamino il file '" + file.getName() + "'");
					//Vado ad estrarre le info contenute nel csv
					FileCSV csv = FileCSV.leggiFile(file);
					//parso le informazioni sugli ordini
					Collection<MOrdine> ordini = parsaOrdini(csv);
					logger.info("Sono state trovati " + ordini.size() + " ordini nel file.");
					//li inserisco a sistema.
					for (MOrdine ordine : ordini) {
						try {
							valida(ordine);
							inserisci(ordine);
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
	
	private MIndirizzo getMittente() {
		MIndirizzo mittente = new MIndirizzo();
		mittente.setCap("06073");
		mittente.setEmail("traffic@ltc-logistics.it");
		mittente.setIndirizzo("Via Firenze, 41");
		mittente.setLocalita("Corciano");
		mittente.setNazione("ITA");
		mittente.setProvincia("PG");
		mittente.setRagionesociale("LTC P/C Forza Industries LTD");
		mittente.setTelefono("075506861");
		return mittente;
	}
	
	private MIndirizzo getDestinatario(FileCSV csv) {
		MIndirizzo destinatario = new MIndirizzo();
		destinatario.setCap(csv.getStringa(DESTINATARIO_CAP));
		destinatario.setEmail(csv.getStringa(DESTINATARIO_EMAIL));
		destinatario.setIndirizzo(csv.getStringa(DESTINATARIO_INDIRIZZO));
		destinatario.setLocalita(csv.getStringa(DESTINATARIO_LOCALITA));
		destinatario.setNazione(csv.getStringa(DESTINATARIO_NAZIONE));
		destinatario.setProvincia(csv.getStringa(DESTINATARIO_PROVINCIA));
		destinatario.setRagionesociale(csv.getStringa(DESTINATARIO_RAGIONE_SOCIALE));
		destinatario.setTelefono(csv.getStringa(DESTINATARIO_TELEFONO));
		return destinatario;
	}
	
	private Collection<MOrdine> parsaOrdini(FileCSV csv) {
		HashMap<String, MOrdine> mappaOrdini = new HashMap<>();
		while (csv.prossimaRiga()) {
			//Controllo se ho già l'ordine altrimenti genero la testata
			String riferimento = csv.getStringa(RIFERIMENTO);
			//ripulisco il riferimento per farlo diventare di dimensioni accettabili
			riferimento = riferimento.replaceAll("ORSG/", "");
			riferimento = riferimento.replaceAll(" del.+", "");
			if (!mappaOrdini.containsKey(riferimento)) {
				MOrdine ordine = new MOrdine();
				ordine.setCodiceCorriere("TNT");
				ordine.setCorriere("TNT");
				ordine.setDataConsegna(csv.getData(DATA_CONSEGNA));
				ordine.setDataDocumento(csv.getData(DATA_DOCUMENTO));
				ordine.setDataOrdine(csv.getData(DATA_DOCUMENTO));
				ordine.setDestinatario(getDestinatario(csv));
				ordine.setMittente(getMittente());
				ordine.setRiferimentoDocumento(riferimento);
				ordine.setRiferimentoOrdine(riferimento);
				ordine.setTipo("ORDINE");
				ordine.setTipoDocumento("ORDINE");
				ordine.setTipoIdentificazioneProdotti("BARCODE");
				mappaOrdini.put(riferimento, ordine);
			}
			MOrdine ordine = mappaOrdini.get(riferimento);
			//Inserisco il prodotto
			ProdottoOrdinato prodotto = new ProdottoOrdinato();
			prodotto.setBarcode(csv.getStringa(RIGA_BARCODE));
			prodotto.setMagazzinoCliente(csv.getStringa(RIGA_MAGAZZINO));
			prodotto.setQuantita(csv.getIntero(RIGA_QUANTITA));
			prodotto.setNumeroRiga(csv.getIntero(RIGA_NUMERO));
			ordine.aggiungiProdotto(prodotto);
		}		
		return mappaOrdini.values();
	}

}
