package it.ltc.clienti.coltorti;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Esegue diversi tipi di aggiornamenti o verifiche:<br>
 * - controllo disponibilità: produce un file con la lista dei prodotti per cui non torna la disponibilità (Opzione -D oppure disponibilita)
 * - aggiorna stato ordini: aggiorna lo stato degli ordini da INSP a SPED per quelli effettivamente usciti (Opzione -S oppure aggiornastato)
 * - sposta pezzi: sposta i pezzi da un carico sorgente ad altri carichi destinazione, l'ordine dei carichi di destinazione è rilevante. (Opzione -P oppure spostapezzi, diventano necessari anche gli argomenti -sorgente N e -destinazioni X Y Z)
 * @author Damiano
 *
 */
public class UtilityColtortiMain {
	
	private static final Logger logger = Logger.getLogger(UtilityColtortiMain.class);

	public static void main(String[] args) throws Exception {
		//Verifico gli argomenti che mi sono stati passati
		if (args.length == 0)
			throw new UnsupportedOperationException("(RTFM) E' necessario inserire uno degli argomenti possibili per avviare il programma.");
		String modalità =  args[0];
		switch (modalità) {
			case "-D" : case "disponibilita" : avviaControlloDisponibilità(args); break;
			case "-S" : case "aggiornastato" : avviaAggiornaStatoOrdini(args); break;
			case "-P" : case "spostapezzi" : avviaSpostamentoPezzi(args); break;
			default : throw new UnsupportedOperationException("(RTFM) Opzione non riconosciuta (" + modalità + ")");
		}
	}
	
	public static void avviaControlloDisponibilità(String[] args) {
		logger.info("Avvio del controllo disponibilità");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "Controllo disponibilità Coltorti " + sdf.format(new Date()) + ".txt";
		String outputFilePath = ConfigurationUtility.getInstance().getFolderPath() + fileName;
		logger.info("Verrà prodotto un output sul path: '" + outputFilePath);
		ControllaDisponibilita controllore = new ControllaDisponibilita();
		controllore.controlla(outputFilePath);
	}
	
	public static void avviaAggiornaStatoOrdini(String[] args) {
		AggiornaStatoOrdiniSpediti aggiornatore = new AggiornaStatoOrdiniSpediti();
		aggiornatore.aggiorna();
	}
	
	public static void avviaSpostamentoPezzi(String[] args) {
		//Eseguo i controlli necessari sugli argomenti
		if (args.length < 5)
			throw new UnsupportedOperationException("(RTFM) Il numero degli argomenti richiesti per la funzione sposta pezzi non è corretto.");
		if (!args[1].equalsIgnoreCase("-sorgente"))
			throw new UnsupportedOperationException("E' necessario specificare il paramentro sorgente.");
		if (!args[3].equalsIgnoreCase("-destinazioni"))
			throw new UnsupportedOperationException("E' necessario specificare il paramentro destinazioni.");
		int idSorgente;
		int[] idDestinazioni = new int[args.length - 4];
		try {
			idSorgente = Integer.parseInt(args[2]);
			for (int index = 4; index < args.length; index++) {
				idDestinazioni[index - 4] = Integer.parseInt(args[index]);
			}
		} catch (Exception e) { throw new UnsupportedOperationException("E' necessario specificare ID validi per i carichi."); }
		SpostaPezziCarico.getInstance().spostaPezzi(idSorgente, idDestinazioni);
	}

}
