package it.ltc.clienti.chio;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.prodotto.Cassa;
import it.ltc.model.interfaces.prodotto.MProdotto;
import it.ltc.model.persistence.prodotto.ControllerProdottoSQLServer;
import it.ltc.utility.csv.FileCSV;
import it.ltc.utility.microsoft.converter.XLStoCSV;

public class ChioAnagraficheMain {
	
	private static final Logger logger = Logger.getLogger(ChioAnagraficheMain.class);

	public static void main(String[] args) {
		File inputFile = new File("C:\\Users\\Damiano\\Documents\\LTC\\Chio\\3RD DESCRIPTIONS.xls");
		FileCSV csv = XLStoCSV.getCSV(inputFile);
		List<MProdotto> prodotti = new LinkedList<>();
		while (csv.prossimaRiga()) {
			//Recupero le info necessarie
			String descrizione = csv.getStringa("descrizione");
			String modello = csv.getStringa("sku");
			String colore = csv.getStringa("colore");
			String taglia = csv.getStringa("taglia");
			//Ricavo le info
			if (descrizione.length() > 100)
				descrizione = descrizione.substring(0, 100);
			modello = modello.replaceAll("\\.", "");
			String sku = modello + taglia;
			//Construisco il modello
			MProdotto prodotto = new MProdotto();
			prodotto.setBarcode(sku);
			prodotto.setBarcodeFornitore(sku);
			prodotto.setBrand("CHIO");
			prodotto.setCassa(Cassa.NO);
			prodotto.setCategoria("COSTUMI");
			prodotto.setChiaveCliente(sku);
			prodotto.setCodiceModello(modello);
			prodotto.setColore(colore);
			prodotto.setDescrizione(descrizione);
			prodotto.setDescrizioneAggiuntiva(descrizione);
			prodotto.setSkuFornitore(sku);
			prodotto.setTaglia(taglia);
			prodotti.add(prodotto);
		}
		int nuoviProdotti = 0;
		ControllerProdottoSQLServer controller = new ControllerProdottoSQLServer("legacy-chio");
		for (MProdotto prodotto : prodotti) {
			try {
				controller.valida(prodotto);
				MProdotto inserito = controller.inserisci(prodotto);
				if (inserito.getId() > 0)
					nuoviProdotti++;
			} catch (ModelValidationException e) {
				//Anagrafica già presente, probabilmente...
				logger.error(e.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		logger.info("Sono stati inseriti " + nuoviProdotti + " nuovi prodotti.");
	}

}
