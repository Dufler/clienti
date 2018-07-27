package it.ltc.coltorti.webcam;

import it.ltc.coltorti.webcam.view.MainFrame;

public class WebcamMain {
	
	private static String lista;
	private static String oggetto;
	private static Modalita modalita;

	public static void main(String[] args) {
		setup(args);
		MainFrame frame = new MainFrame(modalita, lista, oggetto);
		frame.apri();
//		SwingUtilities.invokeLater(new Runnable() {
//            
//            public void run() {
//            	MainFrame frame = new MainFrame(modalita, lista, oggetto);
//            	//MainFrame frame = new MainFrame(Modalita.PRODOTTO2, "180000001", "012345678");
//            	frame.apri();
//            }
//        });
	}
	
	private static void setup(String[] args) {
		if (args == null || args.length != 3)
			throw new IllegalArgumentException("Argomenti non corretti, uso: modalità, lista, riferimento oggetto.\r\nEs. PRODOTTO2 1800000001 123456789");
		try {
			modalita = Modalita.valueOf(args[0]);
		} catch (Exception e) { 
			String message = "Modo d'uso non valido, le modalità valide sono:\r\n";
			for (Modalita m : Modalita.values())
				message += m.name() + "\r\n";
			throw new IllegalArgumentException(message); 
		}
		lista = args[1];
		oggetto = args[2];
	}

}
