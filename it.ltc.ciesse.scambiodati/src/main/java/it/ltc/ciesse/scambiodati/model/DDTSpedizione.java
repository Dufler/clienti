package it.ltc.ciesse.scambiodati.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
import it.ltc.database.dao.legacy.ColliImballoDao;
import it.ltc.database.dao.legacy.ColliPrelevaDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.model.legacy.ColliImballo;
import it.ltc.database.model.legacy.ColliPreleva;
import it.ltc.database.model.legacy.TempCorr;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.utility.miscellanea.string.StringParser;

public class DDTSpedizione {
	
	public static final String PATTERN_DATA = "dd/MM/yyyy HH:mm:ss";
	
	private static final TestataOrdiniDao daoOrdini = new TestataOrdiniDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	private static final ColliPrelevaDao daoColli = new ColliPrelevaDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	private static final ColliImballoDao daoImballi = new ColliImballoDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	
	public static List<TempCorr> parsaDDT(List<String> righe) {
		List<TempCorr> documenti = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 190, PATTERN_DATA, PATTERN_DATA);
		do {
			//Estraggo le informazioni rilevanti dalla riga di testo.
			String numeroDDT = parser.getStringa(1, 21);
			Date dataDocumento = parser.getDataSoloGiorno(21, 40);
			String codiceCliente = parser.getStringa(40, 60); 
			String vettore = parser.getStringa(60, 80);
			//Date dataSpedizione = parser.getDataSoloGiorno(80, 99);
			//int totalePezzi = parser.getIntero(99, 109);
			double contrassegno = parser.getDecimale(109, 119, 3);
			//double assicurazione = parser.getDecimale(119, 129, 3);
			String tipoContrassegno = parser.getStringa(144, 149);
			String divisaContrassegno = parser.getStringa(149, 152);
			String note = parser.getStringa(155, 190);
			System.out.println("");
			//Recupero i colli e l'ordine collegato.
			List<ColliPreleva> colli = daoColli.trovaDaRiferimentoCliente(numeroDDT);
			if (colli.isEmpty())
				throw new RuntimeException("Nessun collo trovato per il riferimento: '" + numeroDDT + "'");
			String numeroDiLista = colli.get(0).getNrLista();
			//Controllo che il numero di lista sia valido
			if (numeroDiLista == null)
				throw new RuntimeException("Il numero di lista non è valido (null)");
			//Controllo che tutti i colli abbiano lo stesso numero di lista
			for (ColliPreleva collo : colli) {
				if (!numeroDiLista.equals(collo.getNrLista()))
					throw new RuntimeException("I colli indicati per una stessa spedizione appartengono a liste di prelievo diverse.");
			}
			TestataOrdini ordine = daoOrdini.trovaDaNumeroLista(numeroDiLista);
			if (ordine == null)
				throw new RuntimeException("Nessun ordine trovato con numero di lista: '" + numeroDiLista + "'");
			//Controllo che il destinatario indicato sia lo stesso
			if (codiceCliente == null || !codiceCliente.equals(ordine.getCodCliente()))
				throw new RuntimeException("Il codice cliente indicato per la spedizione non coincide con quello dell'ordine. (" + codiceCliente + ")");
			//calcolo il peso
			double peso = 0;
			List<ColliImballo> colliImballati = daoImballi.trovaDaNumeroLista(numeroDiLista);
			for (ColliImballo collo : colliImballati) {
				peso += collo.getPesoKg() != null ? collo.getPesoKg() : 0;
			}			
			TempCorr spedizione = new TempCorr();
			spedizione.setCodCliente(ordine.getCodCliente());
			spedizione.setCodcorriere(vettore);
			spedizione.setDataDocu(new Date(dataDocumento.getTime()));
			spedizione.setDivisa(divisaContrassegno);
			spedizione.setNote(note);
			spedizione.setNrColli(colli.size());
			spedizione.setNrDoc(numeroDDT);
			spedizione.setNrLista(numeroDiLista);
			spedizione.setNrOrdine(ordine.getRifOrdineCli());
			spedizione.setPesoKg(peso);
			spedizione.setTipoDocu("DDT");
			spedizione.setTipoIncasso(tipoContrassegno);
			spedizione.setValContra(contrassegno);
			spedizione.setGenerato("NO");
			documenti.add(spedizione);
		} while (parser.prossimaLinea());
		return documenti;
	}

}
