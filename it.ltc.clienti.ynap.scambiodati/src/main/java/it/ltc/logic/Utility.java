package it.ltc.logic;

public class Utility {

//	private final AnagraficaOggettoDao managerAnagrafica;
//	private final PackingListDettagliDao managerDettagli;
//	private final OggettiDao managerOggetti;

	private static Utility instance;

	private Utility() {
//		managerAnagrafica = new AnagraficaOggettoDao();
//		managerDettagli = new PackingListDettagliDao();
//		managerOggetti = new OggettiDao();
	}

	public static Utility getInstance() {
		if (instance == null)
			instance = new Utility();
		return instance;
	}

//	public void copiaDescrizione() {
//		List<AnagraficaOggetto> anagrafiche = managerAnagrafica.trovaTutti();
//		for (AnagraficaOggetto anagrafica : anagrafiche) {
//			List<PackingListDettaglio> dettagli = managerDettagli.trovaDaSeriale(anagrafica.getCodiceArticolo());
//			for (PackingListDettaglio dettaglio : dettagli) {
//				dettaglio.setDescrizione(anagrafica.getDescrizione());
//				managerDettagli.update(dettaglio);
//			}
//		}
//	}
//
//	public void cambiaIDUnivocoPerAnagrafica() {
//		List<AnagraficaOggetto> anagrafiche = managerAnagrafica.getEntities();
//		for (AnagraficaOggetto anagrafica : anagrafiche) {
//			String serialeRFID = anagrafica.getCodiceArticolo();
//			anagrafica.setIdUnivocoArticolo(serialeRFID);
//			managerAnagrafica.update(anagrafica);
//		}
//	}
//
//	public void cambiaIDUnivocoPerPakiArticolo() {
//		List<PackingListDettaglio> articoli = managerDettagli.getEntities();
//		for (PackingListDettaglio articolo : articoli) {
//			String serialeRFID = articolo.getCodiceRFID();
//			articolo.setCodiceUnivocoArticolo(serialeRFID);
//			managerDettagli.update(articolo);
//		}
//	}
//
//	public void cambiaIDUnivocoPerColliPack() {
//		List<Oggetto> articoli = managerOggetti.getEntities();
//		for (Oggetto articolo : articoli) {
//			String serialeRFID = articolo.getCodiceRFID();
//			articolo.setCodiceUnivocoArticolo(serialeRFID);
//			managerOggetti.update(articolo);
//		}
//	}
//
//	private List<String> getSeriali() {
//		List<String> listaSeriali = new ArrayList<String>();
//		try {
//			FileReader stream = new FileReader("C:/temp/listaBonvicini.txt");
//			BufferedReader reader = new BufferedReader(stream);
//			String line = reader.readLine();
//			while (line != null) {
//				listaSeriali.add(line);
//				line = reader.readLine();
//			}
//			reader.close();
//		} catch (IOException e) {
//			System.out.println(e);
//		}
//		return listaSeriali;
//	}
//
//	public boolean ciSonoTutti() {
//		boolean presenti = true;
//		List<String> listaSeriali = getSeriali();
//		for (String seriale : listaSeriali) {
//			AnagraficaOggetto anagrafica = new AnagraficaOggetto();
//			anagrafica.setCodiceArticolo(seriale);
//			if (managerAnagrafica.getEntity(anagrafica) == null) {
//				presenti = false;
//				System.out.println("Il seriale: '" + seriale + "' non è presente in anagrafica.");
//				break;
//			}
//		}
//		return presenti;
//	}
//
//	public void spostaMatricole() {
//		List<String> listaSeriali = getSeriali();
//		List<String> listaNonPresenti = new ArrayList<String>();
//		List<String> listaGiàRegistrati = new ArrayList<String>();
//		int associazioni = 0;
//		for (String seriale : listaSeriali) {
//			PackingListDettaglio dettaglio = new PackingListDettaglio();
//			dettaglio.setCodiceRFID(seriale);
//			dettaglio = managerDettagli.getEntity(dettaglio);
//			if (dettaglio != null) {
//				if (dettaglio.getQuantitàVerificata() == 0) {
//					associazioni += 1;
//					dettaglio.setIdPackingList(61);
//					managerDettagli.update(dettaglio);
//				} else {
//					listaGiàRegistrati.add(seriale);
//				}
//			} else {
//				listaNonPresenti.add(seriale);
//			}
//		}
//		System.out.println("Report:");
//		System.out.println("Sono state associate correttamente " + associazioni + " matricole.");
//		if (listaNonPresenti.isEmpty() && listaGiàRegistrati.isEmpty()) {
//			System.out.println("Nessun errore riscontrato.");
//		} else {
//			System.out.println("\r\nLista delle matricole non associate:\r\n");
//			for (String matricola : listaNonPresenti)
//				System.out.println(matricola);
//			System.out.println("\r\nLista delle matricole già registrate:\r\n");
//			for (String matricola : listaGiàRegistrati)
//				System.out.println(matricola);
//		}
//	}
//
//	public void stampaUbicazione() throws Exception {
//		FileReader stream = new FileReader("C:/temp/listaAustraliani.txt");
//		BufferedReader reader = new BufferedReader(stream);
//		FileWriter streamOut = new FileWriter("C:/temp/listaDiPrelievoAustraliani2.txt");
//		BufferedWriter writer = new BufferedWriter(streamOut);
//		String separator = "	";
//		String line;
//		while ((line = reader.readLine()) != null) {
//			Oggetto oggetto = new Oggetto();
//			oggetto.setCodiceRFID(line);
//			oggetto = managerOggetti.getEntity(oggetto);
//			AnagraficaOggetto anagrafica = new AnagraficaOggetto();
//			anagrafica.setCodiceArticolo(line);
//			anagrafica = managerAnagrafica.getEntity(anagrafica);
//			String ubicazione;
//			String descrizione;
//			if (oggetto != null) {
//				ubicazione = oggetto.getUbicazione();
//			} else {
//				ubicazione = "Non trovato in magazzino";
//			}
//			if (anagrafica != null) {
//				descrizione = anagrafica.getDescrizione();
//			} else {
//				descrizione = "-";
//			}
//			String info = line + separator + descrizione + separator + ubicazione;
//			System.out.println(info);
//			writer.write(info);
//			writer.newLine();
//		}
//		reader.close();
//		writer.flush();
//		writer.close();
//	}

}