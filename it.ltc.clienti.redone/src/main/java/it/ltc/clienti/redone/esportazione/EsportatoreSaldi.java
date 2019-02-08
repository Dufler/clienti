package it.ltc.clienti.redone.esportazione;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.clienti.redone.ConfigurationUtility;
import it.ltc.clienti.redone.model.Inventory;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.MagaMov;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.model.persistence.saldi.EsportatoreSaldiMovimentiSQLServer;
import it.ltc.utility.csv.CSVObjectExporter;

public class EsportatoreSaldi extends EsportatoreSaldiMovimentiSQLServer {
	
	private static final Logger logger = Logger.getLogger(EsportatoreSaldi.class);
	
	public static final String DEFAULT_FILE_NAME = "INVENTORY_";
	public static final String DEFAULT_EXTENSION = ".csv";

	public EsportatoreSaldi(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	protected boolean esportaSaldi(List<MagaSd> saldi) {
		boolean export = true;
		try {
			List<Inventory> righeCsv = new LinkedList<>();
			for (MagaSd saldo : saldi) {
				//Recupero le info sul prodotto
				Articoli articolo = daoArticoli.trovaDaIDUnivoco(saldo.getIdUniArticolo());
				if (articolo == null) throw new RuntimeException("Non è stato trovato l'articolo con ID univoco: '" + saldo.getIdUniArticolo() + "', ID saldo: " + saldo.getIdMagaSd());
				//Recupero le info sul magazzino
				Magazzini magazzino = daoMagazzini.trovaDaCodiceLTC(saldo.getCodMaga());
				if (magazzino == null) throw new RuntimeException("Non è stato trovato il magazzino con codice: '" + saldo.getCodMaga() + "', ID saldo: " + saldo.getIdMagaSd());
				Inventory rigaCsv = new Inventory();
				rigaCsv.setAvailable(saldo.getDisponibile());
				rigaCsv.setExistant(rigaCsv.getExistant());
				rigaCsv.setProduct(articolo.getCodArtStr());
				rigaCsv.setWarehouse(magazzino.getMagaCliente());
				righeCsv.add(rigaCsv);
			}
			String exportPath = getFileExportPath();
			CSVObjectExporter<Inventory> csvExporter = new CSVObjectExporter<>("|", CSVObjectExporter.DEFAULT_NEW_LINE, Inventory.class);
			export = csvExporter.esportaOggetti(exportPath, righeCsv);
		} catch (Exception e) {
			export = false;
			logger.error(e.getMessage(), e);
		}
		return export;
	}

	@Override
	protected boolean esportaMovimenti(List<MagaMov> movimenti) {
		//DO NOTHING! Attualmente non è richiesto.
		return false;
	}
	
	private String getFileExportPath() {
		String folder = ConfigurationUtility.getInstance().getFolderPathOUT();
		String date = sdf.format(new Date());
		String completeFileName = folder + DEFAULT_FILE_NAME + date + DEFAULT_EXTENSION;
		return completeFileName;
	}

}
