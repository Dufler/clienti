package it.ltc.coltorti.webcam.controller.strategies;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.ltc.coltorti.webcam.controller.Strategy;
import it.ltc.coltorti.webcam.view.MainFrame;

public class StrategiaProdotto5 extends Strategy {

	private enum Stato { INIZIO, FOTO2, FOTO3, FOTO4, FOTO5, FINE }
	
	private Stato stato;
	private String immagineUno;
	private String immagineDue;
	private String immagineTre;
	private String immagineQuattro;
	private String immagineCinque;
	
	private JButton bottoneUno;
	private JButton bottoneDue;
	private JButton bottoneTre;
	private JButton bottoneQuattro;
	private JButton bottoneCinque;
	
	public StrategiaProdotto5(String lista, String oggetto, MainFrame frame) {
		super(lista, oggetto, frame);
		stato = Stato.INIZIO;
	}

	@Override
	public String getIndicazioni() {
		String indicazioni;
		switch (stato) {
			case INIZIO : indicazioni = "1: Fare una foto al davanti del prodotto."; break;
			case FOTO2 : indicazioni = "2: Fare una foto al retro del prodotto."; break;
			case FOTO3 : indicazioni = "3: Fare una foto al lato sinistro del prodotto."; break;
			case FOTO4 : indicazioni = "4: Fare una foto al lato destro del prodotto."; break;
			case FOTO5 : indicazioni = "5: Fare una foto al sotto del prodotto."; break;
			case FINE : indicazioni = "6: Per salvare le immagini cliccare su fine."; break;
			default : indicazioni = "";
		}
		return indicazioni;
	}

	@Override
	public boolean isScattaAbilitato() {
		boolean abilitato;
		switch (stato) {
			case INIZIO : case FOTO2 : case FOTO3 : case FOTO4 : case FOTO5 : abilitato = true; break;
			case FINE : abilitato = false; break;
			default : abilitato = false;
		}
		return abilitato;
	}

	@Override
	public boolean isAnnullaAbilitato() {
		boolean abilitato;
		switch (stato) {
			case INIZIO : abilitato = false; break;
			case FOTO2 : case FOTO3 : case FOTO4 : case FOTO5 : case FINE : abilitato = true; break;
			default : abilitato = false;
		}
		return abilitato;
	}

	@Override
	public boolean isFineAbilitato() {
		boolean abilitato;
		switch (stato) {
			case INIZIO : case FOTO2 : case FOTO3 : case FOTO4 : case FOTO5 : abilitato = false; break;
			case FINE : abilitato = true; break;
			default : abilitato = false;
		}
		return abilitato;
	}

	@Override
	public void scatta() {
		switch (stato) {
		case INIZIO : {
			immagineUno = getPath();
			boolean successo = scattaFoto(immagineUno);
			if (successo) {
				bottoneUno.setEnabled(true);
				stato = Stato.FOTO2;
			} else {
				notificaErrore();
			}			
		} break;
		case FOTO2 : {
			immagineDue = getPath();
			boolean successo = scattaFoto(immagineDue);
			if (successo) {
				bottoneDue.setEnabled(true);
				stato = Stato.FOTO3;
			} else {
				notificaErrore();
			}			
		} break;
		case FOTO3 : {
			immagineTre = getPath();
			boolean successo = scattaFoto(immagineTre);
			if (successo) {
				bottoneTre.setEnabled(true);
				stato = Stato.FOTO4;
			} else {
				notificaErrore();
			}			
		} break;
		case FOTO4 : {
			immagineQuattro = getPath();
			boolean successo = scattaFoto(immagineQuattro);
			if (successo) {
				bottoneQuattro.setEnabled(true);
				stato = Stato.FOTO5;
			} else {
				notificaErrore();
			}			
		} break;
		case FOTO5 : {
			immagineCinque = getPath();
			boolean successo = scattaFoto(immagineCinque);
			if (successo) {
				bottoneCinque.setEnabled(true);
				stato = Stato.FINE;
			} else {
				notificaErrore();
			}			
		} break;
		default : break;
	}		
	}

	@Override
	public void annulla() {
		switch (stato) {
			case INIZIO : break;
			case FOTO2 : bottoneUno.setEnabled(false); stato = Stato.INIZIO; break;
			case FOTO3 : bottoneDue.setEnabled(false); stato = Stato.FOTO2; break;
			case FOTO4 : bottoneTre.setEnabled(false); stato = Stato.FOTO3; break;
			case FOTO5 : bottoneQuattro.setEnabled(false); stato = Stato.FOTO4; break;
			case FINE : bottoneCinque.setEnabled(false); stato = Stato.FOTO5; break;
			default : break;
		}
	}

	@Override
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
			case FOTO3 : path = pathBase + lista + "\\S_" + oggetto + "_3.png"; break;
			case FOTO4 : path = pathBase + lista + "\\S_" + oggetto + "_4.png"; break;
			case FOTO5 : path = pathBase + lista + "\\S_" + oggetto + "_5.png"; break;
			default : path = null;
		}
		return path;
	}

	@Override
	public JPanel getPannelloAnteprime() {
		JPanel pannelloAnteprime = new JPanel();
		
		bottoneUno = new JButton();
		bottoneUno.setText("Apri Fronte");
		bottoneUno.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineUno);
			}
		});
		bottoneUno.setEnabled(false);
		
		bottoneDue = new JButton();
		bottoneDue.setText("Apri Retro");
		bottoneDue.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineDue);
			}
		});
		bottoneDue.setEnabled(false);
		
		bottoneTre = new JButton();
		bottoneTre.setText("Apri Sinistra");
		bottoneTre.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineTre);
			}
		});
		bottoneTre.setEnabled(false);
		
		bottoneQuattro = new JButton();
		bottoneQuattro.setText("Apri Destra");
		bottoneQuattro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineQuattro);
			}
		});
		bottoneQuattro.setEnabled(false);
		
		bottoneCinque = new JButton();
		bottoneCinque.setText("Apri Sotto");
		bottoneCinque.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriFoto(immagineCinque);
			}
		});
		bottoneCinque.setEnabled(false);
		
		pannelloAnteprime.add(bottoneUno);
		pannelloAnteprime.add(bottoneDue);
		pannelloAnteprime.add(bottoneTre);
		pannelloAnteprime.add(bottoneQuattro);
		pannelloAnteprime.add(bottoneCinque);
		
		return pannelloAnteprime;
	}

}
