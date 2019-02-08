package it.ltc.clienti.redone.importazione;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.clienti.redone.model.specific.IndirizzoFornitore;
import it.ltc.model.interfaces.exception.ModelAlreadyExistentException;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.fornitore.MFornitore;
import it.ltc.model.interfaces.fornitore.TipoFornitore;
import it.ltc.model.interfaces.importatore.ConfigurazioneMessaggiImportazioneStandard;
import it.ltc.model.interfaces.importatore.RisultatoImportazione;
import it.ltc.model.interfaces.importatore.RisultatoImportazioneStandard;
import it.ltc.model.persistence.fornitore.ControllerFornitoreSQLServer;
import it.ltc.utility.csv.FileCSV;

public class ImportatoreFornitori extends ControllerFornitoreSQLServer {
	
	private static final Logger logger = Logger.getLogger(ImportatoreFornitori.class);

	public ImportatoreFornitori(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	public RisultatoImportazione importaFornitori(FileCSV csv) {
		String nomeFile = csv.getNomeFile();
		int totali = 0;
		int inseriti = 0;
		int giàPresenti = 0;
		List<String> erroriValidazione = new LinkedList<>();
		List<String> erroriGenerici = new LinkedList<>();
		while(csv.prossimaRiga()) {
			try {
				totali += 1;
				MFornitore fornitore = parsaFornitore(csv);
				valida(fornitore);
				MFornitore inserito = inserisci(fornitore);
				if (inserito != null)
					inseriti += 1;
			} catch (ModelAlreadyExistentException e) {
				giàPresenti += 1;
				logger.warn(e.getMessage());
			} catch (ModelValidationException | ModelPersistenceException e) {
				erroriValidazione.add(e.getMessage());
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				erroriGenerici.add(e.getMessage());
				logger.error(e.getMessage(), e);
			}
		}
		ConfigurazioneMessaggiImportazioneStandard messaggi = getMessaggi();
		RisultatoImportazioneFornitori importazione = new RisultatoImportazioneFornitori(messaggi, nomeFile, totali, inseriti, giàPresenti, erroriValidazione, erroriGenerici);
		return importazione;
	}
	
	private MFornitore parsaFornitore(FileCSV csv) {
		MFornitore model = new MFornitore();
		model.setCodice(csv.getStringa("identifier"));
		model.setRagioneSociale(csv.getStringa("name"));
		model.setTipo(TipoFornitore.CARICO);
		model.setNote(csv.getStringa("notes"));
		IndirizzoFornitore indirizzo = new IndirizzoFornitore();
		indirizzo.setRagioneSociale(csv.getStringa("name"));
		indirizzo.setCap(csv.getStringa("zipcode"));
		indirizzo.setEmail(csv.getStringa("email"));
		indirizzo.setIndirizzo(csv.getStringa("address"));
		indirizzo.setLocalita(csv.getStringa("town"));
		indirizzo.setNazione(csv.getStringa("nation"));
		indirizzo.setProvincia("XX" /*csv.getStringa("province")*/);
		indirizzo.setTelefono(csv.getStringa("phone"));
		model.setIndirizzo(indirizzo);
		return model;
	}
	
	public ConfigurazioneMessaggiImportazioneStandard getMessaggi() {
		ConfigurazioneMessaggiImportazioneStandard messaggi = new ConfigurazioneMessaggiImportazioneStandard();
		messaggi.setIntro("Risultato importazione fornitori dal file: ");
		messaggi.setTotali("Anagrafiche Fornitori contenute nel file: ");
		messaggi.setElementiInseriti("Nuovi fornitori inseriti a sistema: ");
		messaggi.setNessunNuovoElemento("Alert: nessun nuovo fornitore inserito a sistema!");
		messaggi.setElementiGiaPresenti("Fornitori già presenti a sistema: ");
		messaggi.setErroriValidazione("Fornitori con errori in validazione: ");
		messaggi.setErroriGenerici("Fornitori con errori generici: ");	
		return messaggi;
	}
	
	private class RisultatoImportazioneFornitori extends RisultatoImportazioneStandard {
		
		RisultatoImportazioneFornitori(ConfigurazioneMessaggiImportazioneStandard messaggi, String nomeFile, int totali, int inseriti, int giàPresenti, List<String> erroreValidazione, List<String> erroreGenerico) {
			super(messaggi, nomeFile, totali, inseriti, giàPresenti, erroreValidazione, erroreGenerico);
		}

		@Override
		public String getDescrizioneRisultato() {
			//Costruzione del risultato
			StringBuilder sb = new StringBuilder("Risultato importazione fornitori dal file: ");
			sb.append(nomeFile);
			sb.append("\r\n");
			//totali
			sb.append("Fornitori contenuti nel file: ");
			sb.append(totali);
			sb.append("\r\n");
			//effettivamente inserite
			if (inseriti > 0) {
				sb.append("Nuove fornitori inseriti a sistema: ");
				sb.append(inseriti);
				sb.append("\r\n");
			} else {
				sb.append("Alert: nessuna nuova anagrafica fornitore inserita a sistema!\r\n");
			}
			//già presenti a sistema
			if (giàPresenti > 0) {
				sb.append("Fornitori già presenti a sistema: ");
				sb.append(giàPresenti);
				sb.append("\r\n");
			}
			//errori in validazione
			if (erroreValidazione.size() > 0) {
				sb.append("Fornitori con errori in validazione: ");
				sb.append(erroreValidazione.size());
				sb.append("\r\n");
				for (String errore : erroreValidazione) {
					sb.append(errore);
					sb.append("\r\n");
				}
			}
			//errori generici
			if (erroreGenerico.size() > 0) {
				sb.append("Fornitori con errori generici: ");
				sb.append(erroreGenerico.size());
				sb.append("\r\n");
				for (String errore : erroreGenerico) {
					sb.append(errore);
					sb.append("\r\n");
				}
			}	
			return sb.toString();
		}
		
	}

}
