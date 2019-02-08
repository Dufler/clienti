package it.ltc.clienti.forza.ftp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.clienti.forza.ConfigurationUtility;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.prodotto.Cassa;
import it.ltc.model.interfaces.prodotto.MProdotto;
import it.ltc.model.persistence.prodotto.ControllerProdottoSQLServer;
import it.ltc.utility.csv.FileCSV;

/**
 * Il file che Wayne mi ha dato è un foglio excel. I passi da seguire sono:
 * 1) aggiungere/modificare i nomi di colonna. I nomi di colonna sono: SKU, Item, Barcode, Composition, sku1, q1, sku2, q2, ...
 * 2) salvare il file in formato .csv separato da ','
 * 3) cambiare la variabile 'filePath' puntando al nuovo file.
 * 4) eseguire!
 * @author Damiano
 *
 */
public class MainImportazioneDati extends ControllerProdottoSQLServer {
	
	public enum Composizione { S, B };
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("yy");
	
	public MainImportazioneDati(String persistenceUnit) {
		super(persistenceUnit);
	}

	private static final Logger logger = Logger.getLogger("Forza Products Importation");
	
	private static final String filePath = "C:/Users/Damiano/Documents/LTC/forza/anagrafiche.csv";
	
//	private static final ArticoliDao daoArticoli = new ArticoliDao("legacy-forza");
//	private static final ArtibarDao daoBarcode = new ArtibarDao("legacy-forza");

	public static void main(String[] args) throws Exception {
		logger.info("Avvio procedura.");
		int prodottiSQLServer = 0;
		//Leggo il .csv contenente le anagrafiche
		File file = new File(filePath);
		FileCSV csv = FileCSV.leggiFile(file, true, ";", ";", FileCSV.DEFAULT_DATE_FORMAT);
		//Salvo momentaneamente le anagrafiche in una lista anzichè salvarle subito
//		List<AnagraficaProdotti> nuoveAnagrafiche = new LinkedList<>();
//		for (String[] riga : csv.getRighe()) {
//			AnagraficaProdotti prodotto = new AnagraficaProdotti(csv.getMappaColonne(), riga);
//			nuoveAnagrafiche.add(prodotto);
//		}
//		//Ordino la lista in modo da inserire prima i prodotti single unit e poi i bundle
//		logger.info("Sono stati trovati " + nuoveAnagrafiche.size() + " nuovi prodotti.");
//		nuoveAnagrafiche.sort(null);
//		for (AnagraficaProdotti prodotto : nuoveAnagrafiche) {
//			//Legacy - SQLServer
//			salvaProdottoSQLServer(prodotto);
//			prodottiSQLServer += 1;
//		}
		MainImportazioneDati istance = new MainImportazioneDati(ConfigurationUtility.getInstance().getPersistenceUnit());
		List<MProdotto> prodotti = istance.parsaProdotti(csv);
		for (MProdotto prodotto : prodotti) {
			try {
				istance.valida(prodotto);
				istance.inserisci(prodotto);
				prodottiSQLServer += 1;
			} catch (ModelValidationException | ModelPersistenceException e) {
				logger.error(e.getMessage(), e);
			}			
		}
		//Report
		logger.info("Report inserimento SQL Server: " + prodottiSQLServer + " nuovi prodotti.");
		logger.info("Termine procedura.");
	}
	
	private List<MProdotto> parsaProdotti(FileCSV csv) {
		Date today = new Date();
		String stagione = "CO" + sdf.format(today);
		List<MProdotto> prodotti = new LinkedList<>();
		while (csv.prossimaRiga()) {
			//Ottengo i valori dei campi
			String sku = csv.getStringa("SKU");
			String descrizione = csv.getStringa("Item");
			String barcode = csv.getStringa("Barcode");
			String composizione = csv.getStringa("Composition");
			int q1 = csv.getIntero("q1") != null ? csv.getIntero("q1") : 0;
//			String sku1 = csv.getStringa("sku1");
			int q2 = csv.getIntero("q2") != null ? csv.getIntero("q2") : 0;
//			String sku2 = csv.getStringa("sku2");
			boolean bundle = composizione.equals(Composizione.B.name());
			//Valorizzo il modello e lo aggiungo alla lista
			MProdotto prodotto = new MProdotto();
			prodotto.setBarcode(barcode);
			prodotto.setBarcodeFornitore(barcode);
			prodotto.setBrand("FORZA");
			prodotto.setCassa(bundle ? Cassa.BUNDLE : Cassa.NO);
			prodotto.setCategoria("INTEGRATORI");
			prodotto.setChiaveCliente(sku);
			prodotto.setCodiceModello(sku);
			prodotto.setColore("-");
			prodotto.setComposizione(composizione);
			prodotto.setDescrizione(descrizione);
			prodotto.setDescrizioneAggiuntiva(descrizione);
			prodotto.setMadeIn("GBR");
			prodotto.setStagione(stagione);
			prodotto.setTaglia("UNI");
			prodotto.setTipoCassa(bundle ? Cassa.BUNDLE.name() : Cassa.NO.name());
			prodotto.setPezziCassa(bundle ? q1 + q2 : 1);
			prodotti.add(prodotto);
			//TODO - Salvare la cassa.
		}
		return prodotti;
	}
	
//	private static void salvaProdottoSQLServer(AnagraficaProdotti prodotto) {
//		//Genero le info richieste
//		boolean bundle = prodotto.getComposizione().equals(AnagraficaProdotti.Composizione.B.name());
//		int umPos = bundle ? 2 : 1; 
//		String idUniArticolo = prodotto.getSku();
//		while (idUniArticolo.length() < 15) {
//			idUniArticolo += "0";
//		}
//		//Passo alla valorizzazione e inserimento dell'articolo
//		Articoli articolo = new Articoli();
//		articolo.setBarraEAN(prodotto.getBarcode());
//		articolo.setBarraUPC(prodotto.getBarcode());
//		articolo.setCodBarre(prodotto.getBarcode());
//		articolo.setCodArtStr(prodotto.getSku());
//		articolo.setColore("-");
//		articolo.setDescrizione(prodotto.getDescrizione());
//		articolo.setIdUniArticolo(idUniArticolo);
//		articolo.setStagione("-");
//		articolo.setTaglia("UNI");
//		articolo.setCatMercDett("FORZA");
//		articolo.setCatMercGruppo("FORZA");
//		articolo.setLinea("FORZA");
//		articolo.setUmPos(umPos);
//		articolo.setCategoria("FORZA");
//		articolo.setModello(prodotto.getSku());
//		articolo = daoArticoli.inserisci(articolo);
//		if (articolo == null)
//			throw new RuntimeException("Impossibile inserire l'articolo " + prodotto.getSku());
//		//Passo alla valorizzazione e inserimento del barcode
//		ArtiBar barcode = new ArtiBar();
//		barcode.setBarraEAN(prodotto.getBarcode());
//		barcode.setBarraUPC(prodotto.getBarcode());
//		barcode.setCodiceArticolo(prodotto.getSku());
//		barcode.setIdUniArticolo(idUniArticolo);
//		barcode = daoBarcode.inserisci(barcode);
//		if (barcode == null)
//			throw new RuntimeException("Impossibile inserire il barcode per l'articolo " + prodotto.getSku());
//		//FIXME - Invece che scrivere la procedura qui sarebbe meglio reimplementare il controller sul progetto it.ltc.model.persistence
//	}

}
