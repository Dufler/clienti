package it.ltc.coltorti.webcam.controller.strategies;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.ltc.coltorti.webcam.controller.Strategy;
import it.ltc.coltorti.webcam.view.MainFrame;

/**
 * Strategia per l'acquisizione di foto di capi appesi e stesi.
 * Vengono scattate 2 foto: fronte e retro.
 * @author Damiano
 *
 */
public class StrategiaProdotto2 extends Strategy {
	
	private enum Stato { INIZIO, FOTO2, FINE }

	private Stato stato;
	private String immagineFronte;
	private String immagineRetro;
	
	private JButton bottoneFronte;
	private JButton bottoneRetro;
	
	public StrategiaProdotto2(String lista, String seriale, MainFrame frame) {
		super(lista, seriale, frame);
		stato = Stato.INIZIO;
	}
	
	public String getIndicazioni() {
		String indicazioni;
		switch (stato) {
			case INIZIO : indicazioni = "1: Fare una foto al davanti del prodotto."; break;
			case FOTO2 : indicazioni = "2: Fare una foto al retro del prodotto."; break;
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
				immagineFronte = getPath();
				boolean successo = scattaFoto(immagineFronte);
				if (successo) {
					bottoneFronte.setEnabled(true);
					stato = Stato.FOTO2;
				} else {
					notificaErrore();
				}				
			} break;
			case FOTO2 : {
				immagineRetro = getPath();
				boolean successo = scattaFoto(immagineRetro);
				if (successo) {
					bottoneRetro.setEnabled(true);
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
			case FOTO2 : bottoneFronte.setEnabled(false); stato = Stato.INIZIO; break;
			case FINE : bottoneRetro.setEnabled(false); stato = Stato.FOTO2; break;
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
			case INIZIO : path = pathBase + lista + "\\S_" + oggetto + "_1.png"; break;
			case FOTO2 : path = pathBase + lista + "\\S_" + oggetto + "_2.png"; break;
			case FINE : path = null; break;
			default : path = null;
		}
		return path;
	}

	@Override
	public JPanel getPannelloAnteprime() {
		JPanel pannelloAnteprime = new JPanel();
		
		bottoneFronte = new JButton();
		bottoneFronte.setText("Apri Fronte");
		bottoneFronte.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineFronte);
			}
		});
		bottoneFronte.setEnabled(false);
		
		bottoneRetro = new JButton();
		bottoneRetro.setText("Apri Retro");
		bottoneRetro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineRetro);
			}
		});
		bottoneRetro.setEnabled(false);
		
		pannelloAnteprime.add(bottoneFronte);
		pannelloAnteprime.add(bottoneRetro);
		
		return pannelloAnteprime;
	}

}
