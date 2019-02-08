package it.ltc.clienti.redone.esportazione;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import it.ltc.clienti.redone.ConfigurationUtility;
import it.ltc.clienti.redone.model.InboundCount;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.model.persistence.carico.EsportatoreCaricoSQLServer;
import it.ltc.utility.csv.CSVObjectExporter;

public class EsportatoreCarichi extends EsportatoreCaricoSQLServer {
	
	public static final String DEFAULT_FILE_NAME = "INBOUNDCOUNT_";
	public static final String DEFAULT_EXTENSION = ".csv";

	public EsportatoreCarichi(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public boolean esportaCarico(PakiTesta carico, List<PakiArticolo> righe) {
		List<InboundCount> righeCsv = new LinkedList<>();
		for (PakiArticolo riga : righe) {
			InboundCount rigaCsv = new InboundCount();
			//Info Carico
			rigaCsv.setArrivalDate(carico.getDataArrivo());
			rigaCsv.setIdentifier(carico.getNrPaki());
			rigaCsv.setReference(carico.getNrPaki());
			//Info Riga
			rigaCsv.setExpectedquantity(riga.getQtaPaki());
			rigaCsv.setRealquantity(riga.getQtaVerificata());
			rigaCsv.setRow(riga.getRigaPacki());
			rigaCsv.setWarehouse(riga.getMagazzino());
			rigaCsv.setProduct(riga.getCodBarre()); //Ci metto il barcode, per loro va bene questo.
			righeCsv.add(rigaCsv);
		}
		String exportPath = getFileExportPath();
		CSVObjectExporter<InboundCount> csvExporter = new CSVObjectExporter<>("|", CSVObjectExporter.DEFAULT_NEW_LINE, InboundCount.class);
		boolean export = csvExporter.esportaOggetti(exportPath, righeCsv);
		return export;
	}
	
	private String getFileExportPath() {
		String folder = ConfigurationUtility.getInstance().getFolderPathOUT();
		String date = sdf.format(new Date());
		String completeFileName = folder + DEFAULT_FILE_NAME + date + DEFAULT_EXTENSION;
		return completeFileName;
	}

}
