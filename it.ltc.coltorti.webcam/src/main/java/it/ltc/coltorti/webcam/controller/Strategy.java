package it.ltc.coltorti.webcam.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import it.ltc.coltorti.webcam.view.MainFrame;

/**
 * Strategia per l'acquisizione delle foto.
 * @author Damiano
 *
 */
public abstract class Strategy {
	
	protected static final String pathBase = "\\\\192.168.0.14\\Coltorti\\";

	protected final String lista;
	protected final String oggetto;
	protected final MainFrame frame;
	
	public Strategy(String lista, String oggetto, MainFrame frame) {
		this.lista = lista;
		this.oggetto = oggetto;
		this.frame = frame;
		controlloCartellaOrdine();
	}
	
	/**
	 * Restituisce una stringa contenente indicazioni testuali per l'operatore.
	 * @return
	 */
	public abstract String getIndicazioni();

	/**
	 * Indica se è possibile scattare una foto.
	 * @return
	 */
	public abstract boolean isScattaAbilitato();

	/**
	 * Indica se è possibile annullare l'acquisizione di una foto.
	 * @return
	 */
	public abstract boolean isAnnullaAbilitato();

	/**
	 * Indica se è possibile terminare l'acquisizione delle foto.
	 * @return
	 */
	public abstract boolean isFineAbilitato();

	/**
	 * Scatta la foto e passa allo stato successivo.
	 */
	public abstract void scatta();

	/**
	 * Annulla l'ultimo scatto e torna allo stato precedente.
	 */
	public abstract void annulla();

	/**
	 * Termina l'acquisizione delle immagini e salva le foto scattate.
	 */
	public abstract void fine();
	
	/**
	 * Restituisce una stringa che identifica dove salvare la foto e che nome attribuirgli in base alla strategia e allo stato.
	 */
	public abstract String getPath();
	
	public abstract JPanel getPannelloAnteprime();
	
	/**
	 * Controlla che la cartella relativa all'ordine sia disponibile, se necessario, la crea.
	 */
	protected void controlloCartellaOrdine() {
		File cartellaOrdine = new File(pathBase + lista);
		if (!cartellaOrdine.isDirectory()) {
			boolean success = cartellaOrdine.mkdirs();
			if (!success) {
				System.out.println("Impossibile creare la cartella nel percoroso specificato. (" + cartellaOrdine.getAbsolutePath() + ")");
			}
		}
	}
	
	/**
	 * Apre la foto che si trova nel path indicato come argomento.
	 * @param path
	 */
	protected void apriFoto(String path) {
		try {
			File foto = new File(path);
			Desktop.getDesktop().open(foto);
		} catch (IOException e) {
			System.out.println("Impossibile aprire l'immagine specificata. (" + path + ")");
			e.printStackTrace();
		}
	}
	
	/**
	 * Scatta una foto e la salva come PNG nel path indicato come argomento.
	 * @param path
	 */
	protected boolean scattaFoto(String path) {
		return WebcamController.getInstance().capturePhoto(path);
	}
	
	protected void notificaErrore() {
		String errore = WebcamController.getInstance().getErrorMessage();
		frame.notificaErroreIO(errore);
	}
	
	/**
	 * Chiude l'applicazione.
	 */
	protected void chiudiApplicazione() {
		WebcamController.getInstance().closeWebcam();
		System.exit(0);
	}

}
