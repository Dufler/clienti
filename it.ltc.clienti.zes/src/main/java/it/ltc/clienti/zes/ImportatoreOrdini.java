package it.ltc.clienti.zes;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;
import it.ltc.model.interfaces.ordine.MContrassegno;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.ProdottoOrdinato;
import it.ltc.model.interfaces.ordine.TipoContrassegno;
import it.ltc.model.interfaces.ordine.TipoIDProdotto;
import it.ltc.model.interfaces.ordine.TipoOrdine;
import it.ltc.model.persistence.ordine.ControllerOrdiniSQLServer;
import it.ltc.utility.csv.FileCSV;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailMan;

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
	
	public static final String CONTRASSEGNO_VALORE = "ValoreContrassegno";
	public static final String CONTRASSEGNO_TIPO = "TipoContrassegno";
	
	public static final String NOTE = "note";
	
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
				//Preparo il postino per notificare l'esito dell'importazione
				MailMan postino = ConfigurationUtility.getInstance().getMailMan();
				List<String> destinatari = ConfigurationUtility.getInstance().getIndirizziDestinatari();
				List<String> destinatariErroriGravi = ConfigurationUtility.getInstance().getIndirizziDestinatariErrori();
				List<String> riferimentiOrdiniOk = new LinkedList<>();
				List<String> riferimentiOrdiniConErrori = new LinkedList<>();
				//Tento di leggere il file
				try {
					boolean importazioneCorretta = true;
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
							riferimentiOrdiniOk.add(ordine.getRiferimentoOrdine() + " per " + ordine.getDestinatario().getRagioneSociale());
						} catch (ModelValidationException | ModelPersistenceException e) {
							importazioneCorretta = false;
							logger.error(e);
							riferimentiOrdiniConErrori.add(ordine.getRiferimentoOrdine() + " per " + ordine.getDestinatario().getRagioneSociale() + ", problema: " + e.getMessage());
						}
					}
					if (importazioneCorretta)
						spostaFileNelloStorico(file);
					else
						spostaFileConErrori(file);
					String subject = "Alert: ZeS - Importazione Ordini";
					StringBuilder body = new StringBuilder();
					if (!riferimentiOrdiniOk.isEmpty()) {
						body.append("Sono stati importati i seguenti ordini: ");
						body.append("\r\n");
						for (String riferimento : riferimentiOrdiniOk) {
							body.append(riferimento);
							body.append("\r\n");
						}
					}
					if (!riferimentiOrdiniConErrori.isEmpty()) {
						body.append("I seguenti ordini non sono stati importati a causa di problemi: ");
						body.append("\r\n");
						for (String riferimento : riferimentiOrdiniConErrori) {
							body.append(riferimento);
							body.append("\r\n");
						}
					}
					if (riferimentiOrdiniOk.isEmpty() && riferimentiOrdiniConErrori.isEmpty()) {
						body.append("Non sono stati importati ordini!");
					}
					Email mail = new Email(subject, body.toString());
					boolean invio = postino.invia(destinatari, mail);
					if (!invio) {
						logger.error("Impossibile inviare la mail con il riepilogo dell'importazione ordini.");
					}
				} catch (Exception e) {
					logger.error(e);
					for (StackTraceElement element : e.getStackTrace()) {
						logger.error(element);
					}
					spostaFileConErrori(file);
					String subject = "Alert: ZeS - Importazione Ordini";
					String body = "Si sono verificati errori durante l'importazione degli ordini: " + e.getMessage();
					Email mail = new Email(subject, body);
					boolean invio = postino.invia(destinatariErroriGravi, mail);
					if (!invio) {
						logger.error("Impossibile inviare la mail con gli errori riscontrati durante l'importazione ordini.");
					}
				}				
			}
		}		
	}
	
	@Override
	protected void checkValiditaCampi(MOrdine ordine) {
		TipoOrdine tipo = ordine.getTipo();
		if (tipo == null)
			throw new ModelValidationException("E' necessario indicare un tipo di ordine.");
		
		String riferimentoOrdine = ordine.getRiferimentoOrdine();
		if (riferimentoOrdine == null || riferimentoOrdine.isEmpty())
			throw new ModelValidationException("E' necessario indicare un riferimento per l'ordine.");
		else if (riferimentoOrdine.length() > 40)
			throw new ModelValidationException("Il riferimento per l'ordine e' troppo lungo. (MAX 40 Caratteri)");
		
		//opzionali
		Integer priorita = ordine.getPriorita();
		if (priorita != null && (priorita < 1 || priorita > 10))
			throw new ModelValidationException("Il valore indicato per la priorita' non e' valido. (" + priorita + ", min 1 - MAX 10)");
		
		String note = ordine.getNote();
		if (note != null && note.length() > 250)
			throw new ModelValidationException("La lunghezza delle note è eccessiva. (MAX 250 caratteri)");
		
		MIndirizzo destinatario = ordine.getDestinatario();
		if (destinatario == null)
			throw new ModelValidationException("E' necessario indicare il destinatario.");
		else
			checkValiditaIndirizzo(destinatario, ordine);
		
		MIndirizzo mittente = ordine.getMittente();
		if (mittente == null)
			throw new ModelValidationException("E' necessario indicare il mittente.");
		else
			checkValiditaIndirizzo(mittente, ordine);
		
		checkValiditaContrassegno(ordine);	
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
		mittente.setRagioneSociale("LTC P/C Forza Industries LTD");
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
		destinatario.setRagioneSociale(csv.getStringa(DESTINATARIO_RAGIONE_SOCIALE));
		destinatario.setTelefono(csv.getStringa(DESTINATARIO_TELEFONO));
		return destinatario;
	}
	
	private Collection<MOrdine> parsaOrdini(FileCSV csv) {
		HashMap<String, MOrdine> mappaOrdini = new HashMap<>();
		while (csv.prossimaRiga()) {
			//Controllo se ho già l'ordine altrimenti genero la testata
			String riferimento = csv.getStringa(RIFERIMENTO);
			//ripulisco il riferimento per farlo diventare di dimensioni accettabili
			//riferimento = riferimento.replaceAll("ORSG/", "");
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
				ordine.setTipo(TipoOrdine.PRN);
				ordine.setTipoDocumento("ORDINE");
				ordine.setTipoIdentificazioneProdotti(TipoIDProdotto.BARCODE);
				ordine.setNote(csv.getStringa(NOTE));
				Double valoreContrassegno = csv.getNumerico(CONTRASSEGNO_VALORE);
				if (valoreContrassegno != null && valoreContrassegno > 0) {
					TipoContrassegno tipo;
					try {
						tipo = TipoContrassegno.valueOf(csv.getStringa(CONTRASSEGNO_TIPO));
					} catch (Exception e) {
						tipo = null;
					}					
					MContrassegno contrassegno = new MContrassegno();
					contrassegno.setValore(valoreContrassegno);
					contrassegno.setTipo(tipo);
					ordine.setContrassegno(contrassegno);
				}
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
