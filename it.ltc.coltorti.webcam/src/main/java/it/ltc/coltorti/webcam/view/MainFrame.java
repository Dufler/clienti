package it.ltc.coltorti.webcam.view;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.github.sarxos.webcam.WebcamPanel;

import it.ltc.coltorti.webcam.Modalita;
import it.ltc.coltorti.webcam.controller.Strategy;
import it.ltc.coltorti.webcam.controller.StrategyFactory;
import it.ltc.coltorti.webcam.controller.WebcamController;
import java.awt.Font;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final String messaggioChiusura = "Impossibile chiudere il programma, seguire l'operatività indicata a video e al terminare premere il tasto Chiudi";
	private static final String titoloChiusura = "Impossibile chiudere il programma!";
	private static final String titoloErrore = "Errore!";
	
	private final Modalita modalita;
	private final String lista;
	private final String oggetto;
	
	private Strategy strategy;
	
	private JLabel lblIndicazioni;
	private JButton btnScatta;
	private JButton btnAnnulla;
	private JButton btnFine;
	private WebcamPanel pannelloWebcam;
	
	/**
	 * Costruttore base che inizializza in autonomia i componenti grafici richiesti.
	 */
	public MainFrame(Modalita modalita, String lista, String oggetto) {
		super("LTC Webcam data acquisition");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.modalita = modalita;
		this.lista = lista;
		this.oggetto = oggetto;
		setup();
		costruisci();
		decora();
	}
	
	/**
	 * Inizializza gli elementi necessari
	 */
	protected void setup() {
		pannelloWebcam = WebcamController.getInstance().getPanel();
		strategy = StrategyFactory.getStrategy(modalita, lista, oggetto, this);
	}
	
	/**
	 * Assembla i componenti grafici richiesti.
	 */
	protected void costruisci() {
		getContentPane().add(pannelloWebcam);
		
		JPanel pannelloIndicazioni = new JPanel();
		getContentPane().add(pannelloIndicazioni, BorderLayout.NORTH);
		pannelloIndicazioni.setLayout(new BoxLayout(pannelloIndicazioni, BoxLayout.X_AXIS));
		
		lblIndicazioni = new JLabel("Indicazioni");
		lblIndicazioni.setFont(new Font("Tahoma", Font.BOLD, 15));
		pannelloIndicazioni.add(lblIndicazioni);
		
		JPanel pannelloInferiore = new JPanel();
		getContentPane().add(pannelloInferiore, BorderLayout.SOUTH);
		pannelloInferiore.setLayout(new BorderLayout(0, 0));
		
		JPanel pannelloControlli = new JPanel();
		pannelloInferiore.add(pannelloControlli, BorderLayout.WEST);
		pannelloControlli.setLayout(new BoxLayout(pannelloControlli, BoxLayout.Y_AXIS));
		
		btnScatta = new JButton("Scatta!");
		btnScatta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				strategy.scatta();
				decora();
			}
		});
		pannelloControlli.add(btnScatta);
		
		btnAnnulla = new JButton("Annulla");
		btnAnnulla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				strategy.annulla();
				decora();
			}
		});
		pannelloControlli.add(btnAnnulla);
		
		btnFine = new JButton("Fine");
		btnFine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				strategy.fine();
			}
		});
		pannelloControlli.add(btnFine);
		
		JPanel pannelloGalleria = new JPanel();
		pannelloInferiore.add(pannelloGalleria);
		pannelloGalleria.setLayout(new BoxLayout(pannelloGalleria, BoxLayout.X_AXIS));
		
		JPanel pannelloStrategy = strategy.getPannelloAnteprime();
		pannelloGalleria.add(pannelloStrategy);
	}
	
	/**
	 * Decora gli elementi grafici in base alla strategia.
	 */
	protected void decora() {
		lblIndicazioni.setText(strategy.getIndicazioni());
		btnScatta.setEnabled(strategy.isScattaAbilitato());
		btnAnnulla.setEnabled(strategy.isAnnullaAbilitato());
		btnFine.setEnabled(strategy.isFineAbilitato());
	}
	
	/**
	 * Mostra un messaggio di avvertimento per l'utente indicando che non è possibile uscire dalla procedura fintanto che non si sono fatte le foto richieste.
	 */
	protected void previeniChiusura() {
		JOptionPane.showMessageDialog(this, messaggioChiusura, titoloChiusura, JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Mostra il messaggio d'errore specificato come parametro all'utente.
	 * @param errorMessage
	 */
	public void notificaErroreIO(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, titoloErrore, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Apre il frame per poterci lavorare.
	 */
	public void apri() {
		setResizable(false);
		setAlwaysOnTop(true);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                previeniChiusura();
            }
        });
		pack();
		setVisible(true);
	}

}
