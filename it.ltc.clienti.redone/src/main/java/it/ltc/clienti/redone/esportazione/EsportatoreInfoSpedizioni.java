package it.ltc.clienti.redone.esportazione;

import java.util.Date;

import it.ltc.clienti.redone.ConfigurationUtility;
import it.ltc.clienti.redone.model.ShipmentInfo;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.model.persistence.ordine.EsportatoreInfoSpedizioniSQLServer;
import it.ltc.utility.csv.CSVObjectExporter;

public class EsportatoreInfoSpedizioni extends EsportatoreInfoSpedizioniSQLServer {
	
	public static final String DEFAULT_FILE_NAME = "SHIPMENTINFO_";
	public static final String DEFAULT_EXTENSION = ".csv";

	public EsportatoreInfoSpedizioni(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	protected boolean esportaOrdine(TestataOrdini testata) {
		ShipmentInfo csv = new ShipmentInfo();
		csv.setIdentifier(testata.getRifOrdineCli());
		csv.setDocument(testata.getNrDoc());
		csv.setTrackingNumber(testata.getNrLetteraVettura());
		String exportPath = getFileExportPath();
		CSVObjectExporter<ShipmentInfo> csvExporter = new CSVObjectExporter<>("|", CSVObjectExporter.DEFAULT_NEW_LINE, ShipmentInfo.class);
		boolean export = csvExporter.esportaOggetto(exportPath, csv);
		return export;
	}
	
	private String getFileExportPath() {
		String folder = ConfigurationUtility.getInstance().getFolderPathOUT();
		String date = sdf.format(new Date());
		String completeFileName = folder + DEFAULT_FILE_NAME + date + DEFAULT_EXTENSION;
		return completeFileName;
	}

}
