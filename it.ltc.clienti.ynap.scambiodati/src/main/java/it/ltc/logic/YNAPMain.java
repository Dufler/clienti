package it.ltc.logic;

public class YNAPMain {

	public static void main(String[] args) throws Exception {
		System.out.println("Procedura avviata.");
		String uso;
		if (args.length == 1)
			uso = args[0];
		else
			uso = "0";
		switch(uso) {
			case "0" : elaboraCarichiRicevuti(); break;
			case "1" : inviaRiscontro(); break;
			case "2" : elaboraOrdiniRicevuti(); break;
			case "3" : inviaEvasioneOrdine(); break;
			case "4" : eliminaColliVuoti(); break;
			case "5" : inviaInventario(); break;
//			case "7" : inviaRiscontroManualmente(); break;
//			case "8" : copiaDescrizione(); break;
//			case "9" : cambiaIDUnivoco(); break;
//			case "10" : stampaUbicazione(); break;
			default : uso(); break;
		}
		System.out.println("Procedura completata.");
		System.exit(0);
	}
	
//	private static void stampaUbicazione() throws Exception {
//		Utility.getInstance().stampaUbicazione();
//	}

//	private static void cambiaIDUnivoco() {
//		Utility utility = Utility.getInstance();
//		//utility.cambiaIDUnivocoPerAnagrafica();
//		//utility.cambiaIDUnivocoPerColliPack();
//		utility.cambiaIDUnivocoPerPakiArticolo();
//	}

//	private static void copiaDescrizione() {
//		Utility utility = Utility.getInstance();
//		utility.copiaDescrizione();
//	}

//	private static void inviaRiscontroManualmente() {
//		Utility utility = Utility.getInstance();
//		if (utility.ciSonoTutti()) {
//			utility.spostaMatricole();
//		}		
//	}

	private static void uso(){
		System.out.println("Scegli una modalità valida!");
	}
	
	private static void elaboraCarichiRicevuti() {
		ImportaListaCarico.getInstance().elaboraXMLRicevuti();
	}
	
	private static void inviaRiscontro() {
		InviaRiscontro.getInstance().inviaRiscontroNuovo();
	}
	
	private static void elaboraOrdiniRicevuti() {
		ImportaOrdine.getInstance().importaOrdini();
	}
	
	private static void inviaEvasioneOrdine() {
		InviaEvasioneOrdine.getInstance().inviaEvasioneOrdini();
	}
	
	private static void eliminaColliVuoti() {
		EliminaColliVuoti.getInstance().eliminaColliVuoti();
	}
	
	private static void inviaInventario() {
		InventarioSettimanale.getInstance().inviaInventario();
	}

}
