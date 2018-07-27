package it.ltc.coltorti.webcam.controller.strategies;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.ltc.coltorti.webcam.controller.Strategy;
import it.ltc.coltorti.webcam.view.MainFrame;

public class StrategiaCollo extends Strategy {

	private enum Stato { INIZIO, FOTO2, FINE }

	private Stato stato;
	private String immagineIntero;
	private String immagineTreQuarti;
	
	private JButton bottoneIntero;
	private JButton bottoneTreQuarti;
	
	public StrategiaCollo(String lista, String collo, MainFrame frame) {
		super(lista, collo, frame);
		stato = Stato.INIZIO;
	}
	
	public String getIndicazioni() {
		String indicazioni;
		switch (stato) {
			case INIZIO : indicazioni = "1: Fare una foto al collo per intero."; break;
			case FOTO2 : indicazioni = "2: Fare una foto al collo a 3/4."; break;
			case FINE : indicazioni = "3: Per salvare le immagini cliccare su fine."; break;
			default : indicazioni = "";
		}
		return indicazioni;
	}

	public boolean isScattaAbilitato() {
		boolean abilitato;
		switch (stato) {
			case INIZIO : case FOTO2 : abilitato = true; break;
			case FINE : abilitato = false; break;
			default : abilitato = false;
		}
		return abilitato;
	}

	public boolean isAnnullaAbilitato() {
		boolean abilitato;
		switch (stato) {
			case INIZIO : abilitato = false; break;
			case FOTO2 : case FINE : abilitato = true; break;
			default : abilitato = false;
		}
		return abilitato;
	}

	public boolean isFineAbilitato() {
		boolean abilitato;
		switch (stato) {
			case INIZIO : case FOTO2 : abilitato = false; break;
			case FINE : abilitato = true; break;
			default : abilitato = false;
		}
		return abilitato;
	}

	public void scatta() {
		switch (stato) {
			case INIZIO : {
				immagineIntero = getPath();
				boolean successo = scattaFoto(immagineIntero);
				if (successo) {
					bottoneIntero.setEnabled(true);
					stato = Stato.FOTO2;
				} else {
					notificaErrore();
				}
			} break;
			case FOTO2 : {
				immagineTreQuarti = getPath();
				boolean successo = scattaFoto(immagineTreQuarti);
				if (successo) {
					bottoneTreQuarti.setEnabled(true);
					stato = Stato.FINE;
				} else {
					notificaErrore();
				}
			} break;
			default : break;
		}		
	}

	public void annulla() {
		switch (stato) {
			case INIZIO : break;
			case FOTO2 : bottoneIntero.setEnabled(false); stato = Stato.INIZIO; break;
			case FINE : bottoneTreQuarti.setEnabled(false); stato = Stato.FOTO2; break;
			default : break;
		}	
	}

	public void fine() {
		switch (stato) {
			case FINE : chiudiApplicazione(); break;
			default : break;
		}	
	}
	
	@Override
	public String getPath() {
		String path;
		switch (stato) {
			case INIZIO : path = pathBase + lista + "\\C_" + oggetto + "_1.png"; break;
			case FOTO2 : path = pathBase + lista + "\\C_" + oggetto + "_2.png"; break;
			case FINE : path = null; break;
			default : path = null;
		}
		return path;
	}

	@Override
	public JPanel getPannelloAnteprime() {
		JPanel pannelloAnteprime = new JPanel();
		
		bottoneIntero = new JButton();
		bottoneIntero.setText("Apri Intero");
		bottoneIntero.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineIntero);
			}
		});
		bottoneIntero.setEnabled(false);
		
		bottoneTreQuarti = new JButton();
		bottoneTreQuarti.setText("Apri 3/4");
		bottoneTreQuarti.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineTreQuarti);
			}
		});
		bottoneTreQuarti.setEnabled(false);
		
		pannelloAnteprime.add(bottoneIntero);
		pannelloAnteprime.add(bottoneTreQuarti);
		
		return pannelloAnteprime;
	}

}
