package it.ltc.coltorti.webcam.controller.strategies;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.ltc.coltorti.webcam.controller.Strategy;
import it.ltc.coltorti.webcam.view.MainFrame;

public class StrategiaBancale extends Strategy {

	private enum Stato { INIZIO, FINE }

	private Stato stato;
	private String immagineIntero;
	
	private JButton bottoneIntero;
	
	public StrategiaBancale(String lista, String bancale, MainFrame frame) {
		super(lista, bancale, frame);
		stato = Stato.INIZIO;
	}
	
	public String getIndicazioni() {
		String indicazioni;
		switch (stato) {
			case INIZIO : indicazioni = "1: Fare una foto al bancale per intero."; break;
			case FINE : indicazioni = "3: Per salvare l'immagine cliccare su fine."; break;
			default : indicazioni = "";
		}
		return indicazioni;
	}

	public boolean isScattaAbilitato() {
		boolean abilitato;
		switch (stato) {
			case INIZIO : abilitato = true; break;
			case FINE : abilitato = false; break;
			default : abilitato = false;
		}
		return abilitato;
	}

	public boolean isAnnullaAbilitato() {
		boolean abilitato;
		switch (stato) {
			case INIZIO : abilitato = false; break;
			case FINE : abilitato = true; break;
			default : abilitato = false;
		}
		return abilitato;
	}

	public boolean isFineAbilitato() {
		boolean abilitato;
		switch (stato) {
			case INIZIO : abilitato = false; break;
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
			case FINE : bottoneIntero.setEnabled(false); stato = Stato.INIZIO; break;
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
			case INIZIO : path = pathBase + lista + "\\B_" + oggetto + ".png"; break;
			default : path = null;
		}
		return path;
	}

	@Override
	public JPanel getPannelloAnteprime() {
		JPanel pannelloAnteprime = new JPanel();
		
		bottoneIntero = new JButton();
		bottoneIntero.setText("Apri Bancale");
		bottoneIntero.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineIntero);
			}
		});
		bottoneIntero.setEnabled(false);
		
		pannelloAnteprime.add(bottoneIntero);
		
		return pannelloAnteprime;
	}

}
