package it.ltc.forza.ftp;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.forza.ftp.model.AnagraficaProdotti;
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
public class MainImportazioneDati {
	
	private static final Logger logger = Logger.getLogger("Forza Products Importation");
	
	private static final String filePath = "C:/Users/Damiano/Documents/LTC/clienti/forza/privalia.csv";

	public static void main(String[] args) throws Exception {
		logger.info("Avvio procedura.");
		int prodottiSQLServer = 0;
		//Leggo il .csv contenente le anagrafiche
		File file = new File(filePath);
		FileCSV csv = FileCSV.leggiFile(file, true, ";", ";");
		//Salvo momentaneamente le anagrafiche in una lista anzichè salvarle subito
		List<AnagraficaProdotti> nuoveAnagrafiche = new LinkedList<>();
		for (String[] riga : csv.getRighe()) {
			AnagraficaProdotti prodotto = new AnagraficaProdotti(csv.getMappaColonne(), riga);
			nuoveAnagrafiche.add(prodotto);
		}
		//Ordino la lista in modo da inserire prima i prodotti single unit e poi i bundle
		logger.info("Sono stati trovati " + nuoveAnagrafiche.size() + " nuovi prodotti.");
		nuoveAnagrafiche.sort(null);
		for (AnagraficaProdotti prodotto : nuoveAnagrafiche) {
			//Legacy - SQLServer
			boolean salvatoSQLServer = salvaProdottoSQLServer(prodotto);
			if (salvatoSQLServer)
				prodottiSQLServer += 1;
			else
				logger.error("Impossibile salvare su SQL Server il prodotto: " + prodotto);
		}
		//Report
		logger.info("Report inserimento SQL Server: " + prodottiSQLServer + " nuovi prodotti.");
		logger.info("Termine procedura.");
	}
	
	private static boolean salvaProdottoSQLServer(AnagraficaProdotti prodotto) {
//		//Genero le info richieste
//		boolean bundle = prodotto.getComposizione().equals(AnagraficaProdotti.Composizione.B.name());
//		Integer umPos = bundle ? 2 : 1; 
//		String idUniArticolo = prodotto.getSku();
//		while (idUniArticolo.length() < 15) {
//			idUniArticolo += "0";
//		}
//		//Passo alla valorizzazione e inserimento dell'articolo
//		Articoli articolo = new Articoli();
//		articolo.setBarcode(prodotto.getBarcode());
//		articolo.setCodiceArticolo(prodotto.getSku());
//		articolo.setColore("-");
//		articolo.setDescrizione(prodotto.getDescrizione());
//		articolo.setIdUnivocoArticolo(idUniArticolo);
//		articolo.setNumerata("-");
//		articolo.setStagione("-");
//		articolo.setUmPos(umPos);
//		articolo.setTaglia("UNI");
//		articolo.setCatMercDett("FORZA");
//		articolo.setCatMercGruppo("FORZA");
//		articolo.setLinea("FORZA");
//		boolean insertArticolo = managerArticoli.insert(articolo);
//		//Passo alla valorizzazione e inserimento del barcode
//		ArtiBar barcode = new ArtiBar();
//		barcode.setBarcodeEAN(prodotto.getBarcode());
//		barcode.setBarcodeUPC(prodotto.getBarcode());
//		barcode.setCodiceArticolo(prodotto.getSku());
//		barcode.setIdUniArticolo(idUniArticolo);
//		boolean insertBarcode = managerBarcode.insert(barcode);
//		return insertArticolo && insertBarcode;
		return false; //FIXME - Invece che scrivere la procedura qui sarebbe meglio reimplementare il controller sul progetto it.ltc.model.persistence
	}

}
