package it.ltc.clienti.redone.esportazione;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.clienti.redone.ConfigurationUtility;
import it.ltc.clienti.redone.model.PickPack;
import it.ltc.database.model.legacy.ColliImballo;
import it.ltc.database.model.legacy.Imballi;
import it.ltc.database.model.legacy.RighiImballo;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.model.persistence.ordine.EsportatoreImballoSQLServer;
import it.ltc.utility.csv.CSVObjectExporter;

public class EsportatoreImballi extends EsportatoreImballoSQLServer {
	
	private static final Logger logger = Logger.getLogger(EsportatoreImballi.class);
	
	public static final String DEFAULT_FILE_NAME = "PICKPACK_";
	public static final String DEFAULT_EXTENSION = ".csv";

	public EsportatoreImballi(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	protected boolean esportaOrdine(TestataOrdini testata, List<RighiOrdine> righe, List<RighiImballo> imballi,	List<ColliImballo> colli) {
		boolean export = true;
		try {
			//Preparo una mappa dei colli
			HashMap<String, ColliImballo> mappaColli = new HashMap<>();
			for (ColliImballo collo : colli) {
				mappaColli.put(collo.getKeyColloSpe(), collo);
			}
			//Accorpo i righiimballo perchè non riescono a gestirli sennò
			HashMap<String, Integer> mappaImballi = new HashMap<>();
			for (RighiImballo imballo : imballi) {
				String key = imballo.getNrRigoOrdine() + "#" + imballo.getCodBarre() + "#" + imballo.getKeyColloSpe();
				int quantità = mappaImballi.containsKey(key) ? mappaImballi.get(key) : 0;
				quantità += imballo.getQtaImballata();
				mappaImballi.put(key, quantità);
			}
			List<PickPack> righeCsv = new LinkedList<>();
			//for (RighiImballo imballo : imballi) {
			for (String key : mappaImballi.keySet()) {
				//Recupero le info dalla chiave della mappa
				int quantità = mappaImballi.get(key);
				String[] valori = key.split("#");
				int rigaOrdine = Integer.parseInt(valori[0]);
				String codiceArticolo = valori[1]; //In realtà è il barcode perchè sono delle fave.
				String keyColloSpe = valori[2];
				//Preparo la riga csv
				PickPack rigaCsv = new PickPack();
				//Aggiungo le info sull'ordine
				rigaCsv.setIdentifier(testata.getRifOrdineCli());
				rigaCsv.setReference(testata.getNrOrdine());
				//Aggiungo le info sul prodotto
				rigaCsv.setRow(rigaOrdine); //rigaCsv.setRow(imballo.getNrRigoOrdine());
				rigaCsv.setQuantity(quantità); //rigaCsv.setQuantity(imballo.getQtaImballata());
				rigaCsv.setProduct(codiceArticolo); //rigaCsv.setProduct(imballo.getCodiceArticolo());
				//Aggiungo le info sul collo
				ColliImballo collo = mappaColli.get(keyColloSpe); //mappaColli.get(imballo.getKeyColloSpe());
				if (collo == null) throw new RuntimeException("Il collo indicato nella riga non esiste. (Collo: " + keyColloSpe + ")");
				Imballi formato = daoFormati.trovaDaCodice(collo.getCodFormato());
				if (formato == null) throw new RuntimeException("Il formato indicato per il collo non esiste. (ID collo : " + collo.getIdColliImballo() + ", Formato: " + collo.getCodFormato() + ")");
				rigaCsv.setParcel(collo.getKeyColloSpe());
				rigaCsv.setParcelWeight(collo.getPesoKg());
				rigaCsv.setParcelHeight((int) formato.getH());
				rigaCsv.setParcellength((int) formato.getL());
				rigaCsv.setParcelWidth((int) formato.getZ());
				righeCsv.add(rigaCsv);
			}
			String exportPath = getFileExportPath();
			CSVObjectExporter<PickPack> csvExporter = new CSVObjectExporter<>("|", CSVObjectExporter.DEFAULT_NEW_LINE, PickPack.class);
			export = csvExporter.esportaOggetti(exportPath, righeCsv);
		} catch (Exception e) {
			export = false;
			logger.error(e.getMessage(), e);
		}
		return export;
	}
	
	private String getFileExportPath() {
		String folder = ConfigurationUtility.getInstance().getFolderPathOUT();
		String date = sdf.format(new Date());
		String completeFileName = folder + DEFAULT_FILE_NAME + date + DEFAULT_EXTENSION;
		return completeFileName;
	}

}
