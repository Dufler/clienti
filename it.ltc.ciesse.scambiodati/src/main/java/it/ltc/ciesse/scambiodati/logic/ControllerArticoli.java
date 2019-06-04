package it.ltc.ciesse.scambiodati.logic;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
import it.ltc.database.dao.legacy.ColliPackDao;
import it.ltc.database.dao.legacy.RighiOrdineDao;
import it.ltc.database.model.legacy.ArtiBar;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.exception.ModelAlreadyExistentException;
import it.ltc.model.interfaces.prodotto.MProdotto;
import it.ltc.model.persistence.prodotto.ControllerProdottoSQLServer;

public class ControllerArticoli extends ControllerProdottoSQLServer {
	
	private static final Logger logger = Logger.getLogger(ControllerArticoli.class);
	
	private final ColliPackDao daoCollipack;
	private final RighiOrdineDao daoRigheOrdine;

	public ControllerArticoli() {
		super(ConfigurationUtility.getInstance().getPersistenceUnit());
		daoCollipack = new ColliPackDao(ConfigurationUtility.getInstance().getPersistenceUnit());
		daoRigheOrdine = new RighiOrdineDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	}
	
	@Override
	public void valida(MProdotto prodotto) throws ModelValidationException {
		//Controllo sul codice modello e taglia
		Articoli checkModelloTaglia = daoArticoli.trovaDaModelloETaglia(prodotto.getCodiceModello(), prodotto.getTaglia());
		if (checkModelloTaglia != null)
			throw new ModelAlreadyExistentException("(Legacy) E' gia' presente un prodotto con la stessa combinazione codice modello-taglia. (modello: " + prodotto.getCodiceModello() + ", taglia: " + prodotto.getTaglia() + ")");
		//Controllo barcode
		Articoli checkBarcode = daoArticoli.trovaDaBarcode(prodotto.getBarcode());
		if (checkBarcode != null)
			throw new ModelAlreadyExistentException("(Legacy) E' gia' presente un prodotto con lo stesso barcode. (" + prodotto.getBarcode() + ")");
		//Controllo chiave cliente
		Articoli checkChiave = daoArticoli.trovaDaSKU(prodotto.getChiaveCliente());
		if (checkChiave != null)
			throw new ModelAlreadyExistentException("(Legacy) E' gia' presente un prodotto con la stessa chiave identificativa. (" + prodotto.getChiaveCliente() + ")");
	}
	
	public boolean correggiModelli(String modelloCorretto, String modelloErrato) {
		boolean correzione = true;
		//Recupero la lista delle anagrafiche collegate ai modelli
		List<Articoli> corretti = daoArticoli.trovaDaModello(modelloCorretto);
		List<Articoli> errati = daoArticoli.trovaDaModello(modelloErrato);
		//Se una delle 2 liste è vuota lancio subito un'eccezione
		if (corretti == null || corretti.isEmpty()) throw new RuntimeException("Il codice di modello '" + modelloCorretto + "' indicato come corretto non ha anagrafiche corrispondenti.");
		if (errati == null || errati.isEmpty()) throw new RuntimeException("Il codice di modello '" + modelloErrato + "' indicato come errato non ha anagrafiche corrispondenti.");
		//Il controllo su corrispondenza completa è stato voluto eliminare da Gubbiotti e Andrea (potrebbe portare a problemi seri)
		//if (corretti.size() != errati.size()) throw new RuntimeException("Il numero di elementi trovati è diverso. (corretto: " + modelloCorretto + ", errato: " + modelloErrato + ")");
		//Controllo che siano corrispondenti come taglie
		HashMap<String, Articoli> mappaCorretti = checkTaglie(corretti);
		HashMap<String, Articoli> mappaErrati = checkTaglie(errati);
		HashMap<Articoli, Articoli> mappaModelli = checkModelli(mappaCorretti, mappaErrati);
		//Controllo che quelle errate, da rimuovere, non siano presenti in ordini o siano state riscontrate.
		checkPresenza(errati);		
		//Per ognuna delle anagrafiche errate vado a "castrare" codartstr, modello (così che non esista più), codbarre, barraean, barraupc e codartold. Nelle note inserisco il fatto che sia dismesso.
		for (Articoli corretto : mappaModelli.keySet()) {
			Articoli errato = mappaModelli.get(corretto);
			List<ArtiBar> barcodes = daoBarcode.trovaDaIDUnivoco(errato.getIdUniArticolo());
			//Per ognuno degli artibar collegato alle anagrafiche errate vado ad aggiornare l'ID univoco articolo con quello dell'articolo corretto.
			EntityManager em = getManager();
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				errato.setBarraEAN("_" + errato.getBarraEAN());
				errato.setBarraUPC("_" + errato.getBarraUPC());
				errato.setCodBarre("_" + errato.getCodBarre());
				errato.setCodArtStr("_" + errato.getCodArtStr());
				errato.setCodArtOld("_" + errato.getCodArtOld());
				errato.setModello("_" + errato.getModello());
				errato.setNote("Deprecato");
				errato.setUtente("Gubbiotti");
				em.merge(errato);
				for (ArtiBar barcode : barcodes) {
					barcode.setCodiceArticolo(corretto.getCodArtStr());
					barcode.setIdUniArticolo(corretto.getIdUniArticolo());
					barcode.setPosizione(corretto.getUmPos());
					em.merge(barcode);
				}
				t.commit();
				logger.info("L'articolo ID " + errato.getIdArticolo() + " con modello errato '" + modelloErrato + "' è stato dismesso e tutti i barcode presenti sono stati trasferiti sull'articolo ID " + corretto.getIdArticolo() + " con modello '" + modelloCorretto + "'.");
			} catch (Exception e) {
				correzione = false;
				logger.error(e.getMessage(), e);
				if (t != null && t.isActive())
					t.rollback();
			} finally {
				em.close();
			}
		}
		return correzione;
	}
	
	/**
	 * Restituisce una mappa fatta in base alle taglie e controlla che non ce ne siano 2 uguali
	 */
	private HashMap<String, Articoli> checkTaglie(List<Articoli> articoli) {
		HashMap<String, Articoli> mappaTaglie = new HashMap<>();
		for (Articoli articolo : articoli) {
			if (mappaTaglie.containsKey(articolo.getTaglia())) {
				throw new RuntimeException("Ci sono due articoli con la stessa combinazione modello (" + articolo.getModello() + ") e taglia (" + articolo.getTaglia() + ")");
			} else {
				mappaTaglie.put(articolo.getTaglia(), articolo);
			}
		}
		return mappaTaglie;
	}
	
	/**
	 * Associa ogni prodotto corretto ad uno errato.<br>
	 * Controllo che abbiano lo stesso numero di elementi e che per ogni elemento ci sia un compagno.
	 */
	private HashMap<Articoli, Articoli> checkModelli(HashMap<String, Articoli> corretti, HashMap<String, Articoli> errati) {
		HashMap<Articoli, Articoli> mappaModelli = new HashMap<>();
		for (String taglia : corretti.keySet()) {
			Articoli corretto = corretti.get(taglia);
			Articoli errato = errati.get(taglia);
			if (errato != null)
				mappaModelli.put(corretto, errato);
		}		
		return mappaModelli;
	}
	
	/**
	 * Controllo che la lista degli articoli non sia presente in collipack e righiordine
	 */
	private void checkPresenza(List<Articoli> errati) {
		for (Articoli articolo : errati) {
			if (daoCollipack.isProdottoPresenteInMagazzino(articolo.getCodArtStr())) throw new RuntimeException("L'articolo '" + articolo.getCodArtStr() + "' è presente in magazzino, non sarà possibile procedere con l'aggiornamento.");
			if (daoRigheOrdine.isProdottoPresente(articolo.getCodArtStr())) throw new RuntimeException("L'articolo '" + articolo.getCodArtStr() + "' è presente negli ordini, non sarà possibile procedere con l'aggiornamento.");
		}
	}

}
