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
		//Il tracking number probabilmente non viene mai aggiornato, ne metto uno fasullo se non lo trovo.
		String trackingNumber = testata.getNrLetteraVettura();
		if (trackingNumber == null || trackingNumber.isEmpty()) {
			trackingNumber = testata.getNrLista();
		}
		//Compilo le info.
		ShipmentInfo csv = new ShipmentInfo();
		csv.setIdentifier(testata.getRifOrdineCli());
		csv.setDocument(testata.getNrDoc());
		csv.setTrackingNumber(trackingNumber);
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
