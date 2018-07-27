package it.ltc.coltorti.webcam.controller.strategies;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.ltc.coltorti.webcam.controller.Strategy;
import it.ltc.coltorti.webcam.view.MainFrame;

public class StrategiaEAN extends Strategy {

	private enum Stato { INIZIO, FOTO2, FINE }

	private Stato stato;
	private String immagineColtorti;
	private String immagineBarcodeOriginale;
	
	private JButton bottoneColtorti;
	private JButton bottoneBarcodeOriginale;
	
	public StrategiaEAN(String lista, String seriale, MainFrame frame) {
		super(lista, seriale, frame);
		stato = Stato.INIZIO;
	}
	
	public String getIndicazioni() {
		String indicazioni;
		switch (stato) {
			case INIZIO : indicazioni = "1: Fare una foto al barcode/QRCode coltorti."; break;
			case FOTO2 : indicazioni = "2: Fare una foto al cartellino con il barcode del fornitore originale."; break;
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
				immagineColtorti = getPath();
				boolean successo = scattaFoto(immagineColtorti);
				if (successo) {
					bottoneColtorti.setEnabled(true);
					stato = Stato.FOTO2;
				} else {
					notificaErrore();
				}				
			} break;
			case FOTO2 : {
				immagineBarcodeOriginale = getPath();
				boolean successo = scattaFoto(immagineBarcodeOriginale);
				if (successo) {
					bottoneBarcodeOriginale.setEnabled(true);
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
			case FOTO2 : bottoneColtorti.setEnabled(false); stato = Stato.INIZIO; break;
			case FINE : bottoneBarcodeOriginale.setEnabled(false); stato = Stato.FOTO2; break;
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
			case INIZIO : path = pathBase + lista + "\\E_" + oggetto + "_1.png"; break;
			case FOTO2 : path = pathBase + lista + "\\E_" + oggetto + "_2.png"; break;
			case FINE : path = null; break;
			default : path = null;
		}
		return path;
	}

	@Override
	public JPanel getPannelloAnteprime() {
		JPanel pannelloAnteprime = new JPanel();
		
		bottoneColtorti = new JButton();
		bottoneColtorti.setText("Apri Barcode Coltorti");
		bottoneColtorti.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineColtorti);
			}
		});
		bottoneColtorti.setEnabled(false);
		
		bottoneBarcodeOriginale = new JButton();
		bottoneBarcodeOriginale.setText("Apri Barcode Originale");
		bottoneBarcodeOriginale.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineBarcodeOriginale);
			}
		});
		bottoneBarcodeOriginale.setEnabled(false);
		
		pannelloAnteprime.add(bottoneColtorti);
		pannelloAnteprime.add(bottoneBarcodeOriginale);
		
		return pannelloAnteprime;
	}

}
