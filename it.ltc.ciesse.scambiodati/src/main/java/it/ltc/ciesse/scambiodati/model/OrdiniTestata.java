package it.ltc.ciesse.scambiodati.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
import it.ltc.database.dao.legacy.DestinatariDao;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;
import it.ltc.model.interfaces.ordine.MInfoSpedizione;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.TipoIDProdotto;
import it.ltc.utility.miscellanea.string.StringParser;
import it.ltc.utility.miscellanea.string.StringUtility;

public class OrdiniTestata {
	
	public static final String CANCELLAZIONE = "ELIMINA";
	public static final String PATTERN_DATA = "dd/MM/yyyy HH:mm:ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static List<MOrdine> parsaOrdini(List<String> righe) {
		//DestinatariDao daoDestinatari = new DestinatariDao(Import.persistenceUnit);
		List<MOrdine> ordini = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 615, PATTERN_DATA, PATTERN_DATA);
		do {
			int operazione = parser.getIntero(0, 1);
			String riferimento = parser.getStringa(1, 21);
			String cliente = parser.getStringa(21, 41);
			Date dataDocumento = parser.getDataSoloGiorno(41, 60);
			if (dataDocumento == null)
				dataDocumento = new Date();
			Date dataConsegna = parser.getDataSoloGiorno(60, 79);
			if (dataConsegna == null)
				dataConsegna = new Date();
			//String tipoEvasione = parser.getStringa(79, 80); //Non mappato
			//String condizionePagamaneto = parser.getStringa(80, 84); //Non mappato
			String vettore = parser.getStringa(88, 108);
			String note = parser.getStringa(164, 414);
			String tipo = parser.getStringa(614, 615);
			//solo inserimento, se chiedono cancellazione o aggiornamento lancio un'eccezione.
			if (operazione == 1) {
				MIndirizzo destinatario = new MIndirizzo();
				destinatario.setCodice(cliente);
				MOrdine ordine = new MOrdine();
				ordine.setDataConsegna(dataConsegna);
				ordine.setDataOrdine(dataDocumento);
				ordine.setDestinatario(destinatario);
				ordine.setMittente(getMittente());
				ordine.setNote(note);
				ordine.setParticolarita(null);
				ordine.setPriorita(1);
				ordine.setRiferimentoDocumento(riferimento);
				ordine.setRiferimentoOrdine(riferimento);
				
				ordine.setTipo("PRN");
				ordine.setTipoIdentificazioneProdotti(TipoIDProdotto.CHIAVE);
				ordine.setDataDocumento(new Date());
				ordine.setTipoDocumento(tipo);
				
				MInfoSpedizione infoSpedizione = new MInfoSpedizione();
				infoSpedizione.setCodiceCorriere(vettore);
				infoSpedizione.setCorriere(vettore);
				infoSpedizione.setServizioCorriere("DEF");
				ordine.setInfoSpedizione(infoSpedizione);
				
				ordini.add(ordine);
			}
		} while (parser.prossimaLinea());	
		return ordini;
	}
	
	private static MIndirizzo getMittente() {
		MIndirizzo mittente = new MIndirizzo();
		mittente.setCap("61033");
		mittente.setCodice("DEFAULT");
		mittente.setEmail("support@ltc-logistics.it");
		mittente.setIndirizzo("Via Fermignano, 1");
		mittente.setLocalita("Fermignano");
		mittente.setNazione("ITA");
		mittente.setProvincia("PU");
		mittente.setRagioneSociale("CiEsse");
		mittente.setTelefono("");
		return mittente;
	}
	
	public static List<TestataOrdini> parsaTestate(List<String> righe) {
		DestinatariDao daoDestinatari = new DestinatariDao(ConfigurationUtility.getInstance().getPersistenceUnit());
		List<TestataOrdini> testate = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 615, PATTERN_DATA, PATTERN_DATA);
		do {
			int operazione = parser.getIntero(0, 1);
			String riferimento = parser.getStringa(1, 21);
			String cliente = parser.getStringa(21, 41);
			Date dataDocumento = parser.getDataSoloGiorno(41, 60);
			if (dataDocumento == null)
				dataDocumento = new Date();
			Date dataConsegna = parser.getDataSoloGiorno(60, 79);
			if (dataConsegna == null)
				dataConsegna = new Date();
			//String tipoEvasione = parser.getStringa(79, 80); //Non mappato
			//String condizionePagamaneto = parser.getStringa(80, 84); //Non mappato
			String vettore = parser.getStringa(88, 108);
			String note = parser.getStringa(164, 214);
			String tipo = parser.getStringa(614, 615);
			//Inserimento o aggiornamento.
			if (operazione == 1 /*|| operazione == 2*/) {
				//Check sul destinatario
				Destinatari destinatario = daoDestinatari.trovaDaCodice(cliente);
				if (destinatario == null)
					throw new RuntimeException("Il destinatario indicato per l'ordine non esiste: " + cliente);
				TestataOrdini testata = new TestataOrdini();
				testata.setCodCliente(cliente);
				testata.setCorriere(vettore);
				testata.setDataConsegna(new Date(dataConsegna.getTime()));
				testata.setDataDoc(new Date(dataDocumento.getTime()));
				testata.setDataOrdine(new Date(dataDocumento.getTime()));
				testata.setIdDestina(destinatario.getIdDestina());
				testata.setNote(note);
				testata.setNrDoc(riferimento);
				testata.setRifOrdineCli(riferimento);
				testata.setNrOrdine(riferimento);
				testata.setTipoDoc("ORDINE");
				testata.setTipoOrdine(tipo);
				testata.setStato("IMPO");
				testate.add(testata);
			} /*else if (operazione == 3) { //Lo posso cancellare solo se non abbiamo cominciato a lavorarlo.
				TestataOrdini testata = new TestataOrdini();
				testata.setRifOrdineCli(riferimento);
				testata.setTipoDoc(CANCELLAZIONE);
				testate.add(testata);
			}*/
			else {
				throw new RuntimeException("Operazione non consentita sulle righe d'ordine. (update/delete)");
			}
		} while (parser.prossimaLinea());	
		return testate;
	}
	
	public static List<String> esportaOrdini(List<TestataOrdini> testate) {
		StringUtility utility = new StringUtility(" ", " ", false, false);
		List<String> righe = new LinkedList<>();
		for (TestataOrdini testata : testate) {
			StringBuilder riga = new StringBuilder();
			riga.append("2"); //Fisso l'operazione a update.
			riga.append(testata.getRifOrdineCli());
			riga.append(testata.getCodCliente());
			riga.append(testata.getDataDoc() != null ? sdf.format(testata.getDataDoc()) : utility.getFormattedString("", 19));
			riga.append(testata.getDataConsegna() != null ? sdf.format(testata.getDataConsegna()) : utility.getFormattedString("", 19));
			riga.append(utility.getFormattedString("", 1)); //Tipo evasione
			riga.append(utility.getFormattedString("", 4)); //Condizione pagamento
			riga.append(utility.getFormattedString("", 4)); //Flag 02
			riga.append(utility.getFormattedString(testata.getCorriere(), 20));
			riga.append(utility.getFormattedString("", 50)); //Flag 03
			riga.append(utility.getFormattedString("", 2)); //Urgenza
			riga.append(utility.getFormattedString("", 1)); //Filler 04
			riga.append(utility.getFormattedString("", 1)); //Filler 05
			riga.append(utility.getFormattedString("", 2)); //Filler 06
			riga.append(utility.getFormattedString(testata.getNote(), 50));
			riga.append(utility.getFormattedString("", 50)); //Note 2
			riga.append(utility.getFormattedString("", 50)); //Note 3
			riga.append(utility.getFormattedString("", 50)); //Note 4
			riga.append(utility.getFormattedString("", 50)); //Note 5
			riga.append(utility.getFormattedString("", 100)); //Note BP1
			riga.append(utility.getFormattedString("", 100)); //Note BP2
			riga.append(testata.getTipoOrdine());
			righe.add(riga.toString());
		}
		return righe;
	}

}
