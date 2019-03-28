package it.ltc.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.clienti.ynap.dao.OggettiDao;
import it.ltc.clienti.ynap.model.Oggetto;
import it.ltc.utility.ftp.SFTP;

public class InventarioSettimanale {
	
	private static final Logger logger = Logger.getLogger(InventarioSettimanale.class);
	
	private static final String FILTRO_PER_STATO = "INSERITO";
	private static final String FILTRO_PER_STATO_2 = "IMBALLATO";
	
	private static final String LogisticHub_ID = "10";
	private static final String MicroHub_ID = "ACHUB";
	private static final String Mag_ID = "";
	private static final String SEPARATORE = "|";
	
	private final OggettiDao managerOggetti;
	private BufferedWriter writer;
	private String data;
	
	private final SFTP ftpClient;
	
	private final String outgoingPath;
	private final String localTempFolder;
	private File fileInventario;
	
	private static InventarioSettimanale instance;
	
	private InventarioSettimanale() {
		ConfigurationUtility configuration = ConfigurationUtility.getInstance();
		outgoingPath = configuration.getPathFTPOut();
		localTempFolder = configuration.getPathInventario();
		ftpClient = configuration.getSFTPClient();
		setup();
		
		managerOggetti = new OggettiDao();
	}
	
	public static InventarioSettimanale getInstance() {
		if (instance == null)
			instance = new InventarioSettimanale();
		return instance;
	}
	
	private void setup() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		data = formatter.format(new Date());
		try {
			fileInventario = new File(localTempFolder + "stock_" + data + ".txt");
			FileWriter stream = new FileWriter(fileInventario);
			writer = new BufferedWriter(stream);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void inviaInventario() {
		logger.info("Comincio l'inventario.");
		List<Oggetto> listaOggetti = managerOggetti.trovaPerStato(FILTRO_PER_STATO);
		for (Oggetto oggetto : listaOggetti) {
			String tag = oggetto.getCodiceRFID();
			String collo = oggetto.getNumeroCollo();
			String ubicazione = oggetto.getUbicazione();
			String riga = "";
			//LogisticHub_ID (obbligatorio) = E’ un valore fisso deciso da YNAP
			riga += LogisticHub_ID + SEPARATORE;
			//DataCreazioneInv (obbligatorio) = Data di creazione dell’Inventario in formato YYYYMMDD
			riga += data + SEPARATORE;
			//MicroHUB_ID = E’ un valore fisso deciso da YNAP
			riga += MicroHub_ID + SEPARATORE;
			//MAG_ID, va lasciato vuoto
			riga += Mag_ID + SEPARATORE;
			//UDC = Storage Box ID
			riga += collo + SEPARATORE;
			//CodiceMatricola = (obbligatorio) YNAP items code
			riga += tag + SEPARATORE;
			//Location = (obbligatorio) L&TC location dell’item
			riga += ubicazione + "\r\n";
			try {
				writer.write(riga);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		List<Oggetto> listaOggetti2 = managerOggetti.trovaPerStato(FILTRO_PER_STATO_2);
		for (Oggetto oggetto : listaOggetti2) {
			String tag = oggetto.getCodiceRFID();
			String collo = oggetto.getNumeroCollo();
			String ubicazione = oggetto.getUbicazione();
			String riga = "";
			//LogisticHub_ID (obbligatorio) = E’ un valore fisso deciso da YNAP
			riga += LogisticHub_ID + SEPARATORE;
			//DataCreazioneInv (obbligatorio) = Data di creazione dell’Inventario in formato YYYYMMDD
			riga += data + SEPARATORE;
			//MicroHUB_ID = E’ un valore fisso deciso da YNAP
			riga += MicroHub_ID + SEPARATORE;
			//MAG_ID, va lasciato vuoto
			riga += Mag_ID + SEPARATORE;
			//UDC = Storage Box ID
			riga += collo + SEPARATORE;
			//CodiceMatricola = (obbligatorio) YNAP items code
			riga += tag + SEPARATORE;
			//Location = (obbligatorio) L&TC location dell’item
			riga += ubicazione + "\r\n";
			try {
				writer.write(riga);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		try {
			writer.flush();
			writer.close();
			logger.info("File d'inventario generato: " + fileInventario.getAbsolutePath());
			boolean successo = ftpClient.upload(fileInventario.getAbsolutePath(), outgoingPath);
			if (successo)
				logger.info("Inventario inviato con successo.");
			else
				logger.error("Errore nell'invio dell'inventario.");
		} catch (IOException e) {
			logger.error(e);
		}
	}

}
