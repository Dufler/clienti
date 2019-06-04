package it.ltc.clienti.zes;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;
import it.ltc.model.interfaces.ordine.MContrassegno;
import it.ltc.model.interfaces.ordine.MInfoSpedizione;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.ProdottoOrdinato;
import it.ltc.model.interfaces.ordine.TipoContrassegno;
import it.ltc.model.interfaces.ordine.TipoIDProdotto;
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
	
	public static final String DOGANA_VALORE = "ValoreDoganale";
	public static final String CONTRASSEGNO_VALORE = "ValoreContrassegno";
	public static final String CONTRASSEGNO_TIPO = "TipoContrassegno";
	
	public static final String NOTE = "note";
	
	private final String folderPath;
	
	private final MailMan postino;
	private final Set<String> destinatari;
	private final Set<String> destinatariErroriGravi;
	
	public ImportatoreOrdini(String folderPath, String persistenceUnit) {
		super(persistenceUnit);
		this.folderPath = folderPath;
		this.postino = ConfigurationUtility.getInstance().getMailMan();
		this.destinatari = ConfigurationUtility.getInstance().getIndirizziDestinatari();
		this.destinatariErroriGravi = ConfigurationUtility.getInstance().getIndirizziDestinatariErrori();
	}
	
	public void importaOrdini() {
		//recupero i files che contengono gli ordini
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				continue;
			} else if (file.isFile() && file.getName().matches(regexFileOrdini)) {
				//Preparo le liste con i riferimenti per notificare l'esito dell'importazione
				List<String> riferimentiOrdiniOk = new LinkedList<>();
				List<String> riferimentiOrdiniConErrori = new LinkedList<>();
				//Tento di leggere il file
				try {
					boolean importazioneCorretta = true;
					logger.info("Esamino il file '" + file.getName() + "'");
					//Vado ad estrarre le info contenute nel csv
					FileCSV csv = FileCSV.leggiFile(file);
					//parso le informazioni sugli ordini
					Collection<MOrdineZeS> ordini = parsaOrdini(csv);
					logger.info("Sono state trovati " + ordini.size() + " ordini nel file.");
					//li inserisco a sistema.
					for (MOrdine ordine : ordini) {
						try {
							valida(ordine);
							inserisci(ordine);
							riferimentiOrdiniOk.add(ordine.getRiferimentoOrdine() + " per " + ordine.getDestinatario().getRagioneSociale());
						} catch (ModelValidationException | ModelPersistenceException e) {
							importazioneCorretta = false;
							logger.error(e.getMessage(), e);
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
					logger.error(e.getMessage(), e);
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
	
	private boolean checkContrassegno(Double valore, String tipo, String riferimento) throws ModelValidationException {
		boolean valoreValido = valore != null && valore > 0;
		boolean tipoValido = tipo != null && !tipo.isEmpty();
		if ((valoreValido && !tipoValido) || (!valoreValido && tipoValido)) {
			throw new ModelValidationException("I valori inseriti per il contrassegno non sono validi. (ordine: " + riferimento + ")");
		}
		return valoreValido && tipoValido;
	}
	
	private Collection<MOrdineZeS> parsaOrdini(FileCSV csv) {
		HashMap<String, MOrdineZeS> mappaOrdini = new HashMap<>();
		while (csv.prossimaRiga()) {
			//Controllo se ho già l'ordine altrimenti genero la testata
			String riferimento = csv.getStringa(RIFERIMENTO);
			//ripulisco il riferimento per farlo diventare di dimensioni accettabili
			//riferimento = riferimento.replaceAll("ORSG/", "");
			riferimento = riferimento.replaceAll(" del.+", "");
			if (!mappaOrdini.containsKey(riferimento)) {
				MOrdineZeS ordine = new MOrdineZeS();
				ordine.setDataConsegna(csv.getData(DATA_CONSEGNA));
				ordine.setDataDocumento(csv.getData(DATA_DOCUMENTO));
				ordine.setDataOrdine(csv.getData(DATA_DOCUMENTO));
				ordine.setDestinatario(getDestinatario(csv));
				ordine.setMittente(getMittente());
				ordine.setRiferimentoDocumento(riferimento);
				ordine.setRiferimentoOrdine(riferimento);
				ordine.setTipo("PRN");
				ordine.setTipoDocumento("ORDINE");
				ordine.setTipoIdentificazioneProdotti(TipoIDProdotto.BARCODE);
				ordine.setNote(csv.getStringa(NOTE));
				//Info spedizione
				MInfoSpedizione infoSpedizione = new MInfoSpedizione();
				infoSpedizione.setCodiceCorriere("TNT");
				infoSpedizione.setCorriere("TNT");
				infoSpedizione.setTipoDocumento("DDT");
				infoSpedizione.setDataDocumento(csv.getData(DATA_DOCUMENTO));
				infoSpedizione.setServizioCorriere("DEF");
				infoSpedizione.setValoreDoganale(csv.getNumerico(DOGANA_VALORE));
				Double valoreContrassegno = csv.getNumerico(CONTRASSEGNO_VALORE);
				String tipoContrassegno = csv.getStringa(CONTRASSEGNO_TIPO);
				//check ulteriore perchè sono proprio idioti
				if (checkContrassegno(valoreContrassegno, tipoContrassegno, riferimento)) {
					TipoContrassegno tipo;
					try {
						tipo = TipoContrassegno.valueOf(tipoContrassegno);
					} catch (Exception e) {
						throw new ModelValidationException("Il tipo di contrassegno indicato non è valido. (ordine: " + riferimento + ")");
					}					
					MContrassegno contrassegno = new MContrassegno();
					contrassegno.setValore(valoreContrassegno);
					contrassegno.setTipo(tipo);
					infoSpedizione.setContrassegno(contrassegno);
				}
				ordine.setInfoSpedizione(infoSpedizione);
				mappaOrdini.put(riferimento, ordine);
			}
			MOrdineZeS ordine = mappaOrdini.get(riferimento);
			//Inserisco il prodotto
			ProdottoOrdinato prodotto = new ProdottoOrdinato();
			prodotto.setBarcode(csv.getStringa(RIGA_BARCODE));
			prodotto.setMagazzinoCliente(csv.getStringa(RIGA_MAGAZZINO));
			prodotto.setQuantita(csv.getIntero(RIGA_QUANTITA) != null ? csv.getIntero(RIGA_QUANTITA) : -1);
			prodotto.setNumeroRiga(csv.getIntero(RIGA_NUMERO) != null ? csv.getIntero(RIGA_NUMERO) : 0);
			ordine.aggiungiProdotto(prodotto);
		}		
		return mappaOrdini.values();
	}

}
