package it.ltc.forza.ftp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.ColliCarico;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.Righiubicpre;
import it.ltc.database.model.legacy.Scorte;
import it.ltc.database.model.legacy.Scorte2;
import it.ltc.database.model.legacy.ScorteColli;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.Ubicazioni;
import it.ltc.forza.ftp.model.AssegnazioneProdotto;
import it.ltc.forza.ftp.model.AssegnazioneProdotto.TipoAssegnazione;

public class ManagerAssegnazione extends Dao {
	
	private static final Logger logger = Logger.getLogger(ManagerAssegnazione.class);
	
	private final HashMap<String, ColliCarico> mappaColli;
	private final HashMap<String, Ubicazioni> mappaUbicazioni;
	
	public ManagerAssegnazione(String persistenceUnit) {
		super(persistenceUnit);
		mappaColli = new HashMap<>();
		mappaUbicazioni = new HashMap<>();
	}

	/**
	 * Restituisce una lista di ordini che non hanno un assegnazione completa.<br>
	 * Condizioni per la selezione: Stato: IMPO, Quantità assegnata < Quantità totale.
	 * @return
	 */
	public List<TestataOrdini> getOrdiniDaAssegnare() {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestataOrdini> criteria = cb.createQuery(TestataOrdini.class);
		Root<TestataOrdini> member = criteria.from(TestataOrdini.class);
		Predicate condizioneStato = cb.equal(member.get("Stato"), "IMPO");
		Predicate condizionePezziAssegnati = cb.lessThan(member.get("qtaAssegnata"), member.get("qtaTotaleSpedire"));
		criteria.select(member).where(cb.and(condizioneStato, condizionePezziAssegnati));
		List<TestataOrdini> list = em.createQuery(criteria).getResultList();
		em.close();
		return list;
	}
	
	/**
	 * A partire dall'ordine passato nell'argomento restituisce tutte le righe che non sono state ancora completamente assegnate.<br>
	 * Condizioni per la selezione: Quantità da ubicare > 0.
	 * @param testata
	 * @return
	 */
	private List<RighiOrdine> getRigheDaAssegnare(TestataOrdini testata) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RighiOrdine> criteria = cb.createQuery(RighiOrdine.class);
		Root<RighiOrdine> member = criteria.from(RighiOrdine.class);
		Predicate condizionePezziAssegnati = cb.greaterThan(member.get("qtadaubicare"), 0);
		criteria.select(member).where(condizionePezziAssegnati);
		List<RighiOrdine> list = em.createQuery(criteria).getResultList();
		em.close();
		return list;
	}
	
	/**
	 * Prepara l'assegnazione dell'ordine passato come argomento restituendo una lista di oggetti d'assegnazione, uno per ogni riga.
	 * @param testata
	 * @return
	 */
	public List<AssegnazioneProdotto> preparaAssegnazioneOrdine(TestataOrdini testata) {
		//Genero la stringa che identifica la sessione di lavoro
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String sessioneLavoro = sdf.format(new Date());
		testata.setSessioneLavoro(sessioneLavoro);
		List<AssegnazioneProdotto> listaAssegnazione = new LinkedList<>();
		//Trovo le righe d'ordine da assegnare.
		List<RighiOrdine> righeDaAssegnare = getRigheDaAssegnare(testata);
		assegnazioneRiga : for (RighiOrdine riga : righeDaAssegnare) {
			//Creo il contenitore delle informazioni di assegnazione.
			AssegnazioneProdotto assegnazione = new AssegnazioneProdotto(testata, riga);
			listaAssegnazione.add(assegnazione);
			//Controllo i colli pack esistenti dove posso trovare il prodotto.
			List<ColliPack> prodotti = getProdottiInMagazzino(riga);
			//Per ogni prodotto vado a controllare l'ubicazione.
			//Eseguo più giri, all'inizio prendo solo quelli che sono ubicati a prelievo, do priorità a quelli.
			//1) se il collo è ubicato a prelievo lo aggiungo alla assegnazione senza problemi.
			//2) se il collo è ubicato a scorta lo aggiungo alla lista di quelli ubicati a scorta e lo userò se non trovo di meglio.
			//3) se il collo non è ubicato lo aggiungo alla lista di quelli senza ubicazione e andrà a finire nella lista di quelli senza ubicazione.
			Iterator<ColliPack> iteratorPrelievo = prodotti.iterator();
			while (iteratorPrelievo.hasNext()) {
				ColliPack prodotto = iteratorPrelievo.next();
				//Trovo il collo e l'ubicazione corrispondente.
				ColliCarico collo = trovoColloProdotto(prodotto);
				Ubicazioni ubicazione = trovaUbicazioneCollo(collo);
				//Condizioni: Collo ubicato e Ubicazione a prelievo.
				if (collo != null && collo.getUbicato().equals("SI") && ubicazione != null && ubicazione.getTipoUbica().equals("PR")) {
					//Trovo quanti pezzi ci sono e li scalo dalla riga e dal collipack.
					int quantitàGiàAssegnata = testata.getQtaAssegnata();
					int quantitàRigaGiàAssegnata = riga.getQtaAssegnata();
					int quantitàRichiesta = riga.getQtadaubicare();
					int quantitàGiàImpegnata = prodotto.getQtaimpegnata();
					int quantitàOriginale = prodotto.getQta();
					int quantitàDisponibile = quantitàOriginale - quantitàGiàImpegnata;
					//Se basta questa aggiorno gli oggetti e termino l'assegnazione della riga altrimenti scalo quello che è possibile scalare.
					if (quantitàRichiesta < quantitàDisponibile) {
						//Aggiorno testata, riga e prodotto disponibile
						testata.setQtaAssegnata(quantitàGiàAssegnata + quantitàRichiesta);
						riga.setQtadaubicare(0);
						riga.setQtaAssegnata(riga.getQtaSpedizione());
						prodotto.setQtaimpegnata(quantitàGiàImpegnata + quantitàRichiesta);
					} else {
						//Aggiorno testata, riga e prodotto disponibile
						testata.setQtaAssegnata(quantitàGiàAssegnata + quantitàDisponibile);
						riga.setQtadaubicare(quantitàRichiesta - quantitàDisponibile);
						riga.setQtaAssegnata(quantitàRigaGiàAssegnata + quantitàDisponibile);
						prodotto.setQtaimpegnata(quantitàGiàImpegnata + quantitàDisponibile);
						prodotto.setFlagimp("S");
					}
					//Creo la riga d'ubicazione per prelievo
					Righiubicpre rigaUbicazione = new Righiubicpre();
					rigaUbicazione.setIDcollipack(prodotto.getIdColliPack());
					rigaUbicazione.setIdRigoOrdine(riga.getIdRigoOrdine());
					rigaUbicazione.setIduniarticolo(prodotto.getCodiceArticolo());
					rigaUbicazione.setMagazzino(riga.getMagazzino());
					rigaUbicazione.setNrcollo(collo.getNrCollo());
					rigaUbicazione.setNrlista(riga.getNrLista());
					rigaUbicazione.setQuantita(quantitàRichiesta);
					rigaUbicazione.setRaggliste(riga.getNrLista());
					rigaUbicazione.setSessioneLavoro(sessioneLavoro);
					rigaUbicazione.setTipoUbicazione("PR");
					copiaUbicazione(rigaUbicazione, ubicazione);
					//Aggiungo le info all'assegnazione.
					assegnazione.aggiungiRigaUbicazione(rigaUbicazione);
					assegnazione.aggiungiProdotto(prodotto);
					assegnazione.aggiungiColli(collo);
					assegnazione.aggiungiUbicazione(ubicazione);
					assegnazione.setTipo(TipoAssegnazione.NORMALE);
					//Copio le informazioni di ubicazione sulla riga, eventualmente sovrascrivendo.
					riga.setNrcollo(collo.getKeyColloCar());
					copiaUbicazione(riga, ubicazione);
					//Se la quantità richiesta è scesa a 0 procedo con la prossima riga.
					if (riga.getQtadaubicare() == 0) {
						continue assegnazioneRiga;
					}
					//Rimuovo l'elemento corrente dalle successive iterazioni della lista.
					iteratorPrelievo.remove();
				}
			}
			//Controllo se è necessario ancora del prodotto per soddisfare la riga, provo a prenderlo dai colli a scorta.
			if (riga.getQtadaubicare() > 0) {
				Iterator<ColliPack> iteratorScorte = prodotti.iterator();
				while (iteratorScorte.hasNext()) {
					ColliPack prodotto = iteratorScorte.next();				
					int quantitàRichiesta = riga.getQtadaubicare();
					//Trovo il collo e l'ubicazione corrispondente.
					ColliCarico collo = trovoColloProdotto(prodotto);
					Ubicazioni ubicazione = trovaUbicazioneCollo(collo);
					//Condizioni: Collo ubicato e Ubicazione a scorta.
					if (collo != null && collo.getUbicato().equals("SI") && ubicazione != null && ubicazione.getTipoUbica().equals("SC")) {
						//Trovo quanti pezzi ci sono e vedo se bastano a coprire quelli richiesti
						int quantitàGiàImpegnata = prodotto.getQtaimpegnata();
						int quantitàOriginale = prodotto.getQta();
						int quantitàDisponibile = quantitàOriginale - quantitàGiàImpegnata;
						Scorte scorta = new Scorte();
						scorta.setIduniarticolo(prodotto.getCodiceArticolo());
						scorta.setModello(prodotto.getCodiceArticolo());
						scorta.setMagazzino(riga.getMagazzino());
						scorta.setQtascorta(quantitàDisponibile);
						scorta.setQuantita(quantitàRichiesta);
						scorta.setNote("QUANTITA' DA UBICARE: ");
						copiaUbicazione(scorta, ubicazione);
						ScorteColli colloScorta = new ScorteColli();
						colloScorta.setIduniarticolo(prodotto.getCodiceArticolo());
						colloScorta.setKeyColloCar(collo.getKeyColloCar());
						colloScorta.setSessioneLavoro(sessioneLavoro);
						colloScorta.setPezzi(quantitàDisponibile); //TODO - Verificare che vada bene questo valore.
						colloScorta.setUbicazione(ubicazione.getKeyUbica());
						//Creo la riga d'ubicazione per prelievo
						Righiubicpre rigaUbicazione = new Righiubicpre();
						rigaUbicazione.setIDcollipack(prodotto.getIdColliPack());
						rigaUbicazione.setIdRigoOrdine(riga.getIdRigoOrdine());
						rigaUbicazione.setIdubica(ubicazione.getIdUbicazioni());
						rigaUbicazione.setIduniarticolo(prodotto.getCodiceArticolo());
						rigaUbicazione.setMagazzino(riga.getMagazzino());
						rigaUbicazione.setNrcollo(999999999);
						rigaUbicazione.setNrlista(riga.getNrLista());
						rigaUbicazione.setQuantita(quantitàRichiesta);
						rigaUbicazione.setRaggliste(riga.getNrLista());
						rigaUbicazione.setSessioneLavoro(sessioneLavoro);
						rigaUbicazione.setTipoUbicazione("SC");
						copiaUbicazione(rigaUbicazione, ubicazione);
						//Aggiungo all'oggetto assegnazione gli oggetti scorta e scortacollo.
						assegnazione.aggiungiRigaUbicazione(rigaUbicazione);
						assegnazione.aggiungiScortaUbicata(scorta);
						assegnazione.aggiungiColloConScorte(colloScorta);
						assegnazione.setTipo(TipoAssegnazione.SCORTE);
						//Rimuovo l'elemento corrente dalle successive iterazioni della lista.
						iteratorScorte.remove();
						//Scalo la quantità richiesta di quella che è disponibile nella scorta.
						quantitàRichiesta -= quantitàDisponibile;
					}
					//Se la quantità richiesta è scesa a 0 procedo con la prossima riga.
					if (quantitàRichiesta <= 0) {
						continue assegnazioneRiga;
					}
				} 
			}
			//Controllo se è necessario ancora del prodotto per soddisfare la riga, provo a prenderlo dai colli non ubicati.
			if (riga.getQtadaubicare() > 0) {
				Iterator<ColliPack> iteratorNonUbicati = prodotti.iterator();
				while (iteratorNonUbicati.hasNext()) {
					ColliPack prodotto = iteratorNonUbicati.next();
					int quantitàRichiesta = riga.getQtadaubicare();
					//Trovo il collo e l'ubicazione corrispondente.
					ColliCarico collo = trovoColloProdotto(prodotto);
					//Condizioni: Collo non ubicato.
					if (collo != null && collo.getUbicato().equals("NO")) {
						//Trovo quanti pezzi ci sono e vedo se bastano a coprire quelli richiesti
						int quantitàGiàImpegnata = prodotto.getQtaimpegnata();
						int quantitàOriginale = prodotto.getQta();
						int quantitàDisponibile = quantitàOriginale - quantitàGiàImpegnata;
						//Scorta
						Scorte scorta = new Scorte();
						scorta.setIduniarticolo(prodotto.getCodiceArticolo());
						scorta.setModello(prodotto.getCodiceArticolo());
						scorta.setMagazzino(riga.getMagazzino());
						scorta.setQtascorta(quantitàDisponibile);
						scorta.setQuantita(quantitàRichiesta);
						scorta.setNote("QUANTITA' DA UBICARE: ");
						//Scorta non ubicata
						Scorte2 scortaNonUbicata = new Scorte2();
						scortaNonUbicata.setCollo(collo.getKeyColloCar());
						scortaNonUbicata.setIduniarticolo(prodotto.getCodiceArticolo());
						scortaNonUbicata.setQuantita(quantitàDisponibile);
						scortaNonUbicata.setSessioneLavoro(sessioneLavoro);
						//Collo scorta
						ScorteColli colloScorta = new ScorteColli();
						colloScorta.setIduniarticolo(prodotto.getCodiceArticolo());
						colloScorta.setKeyColloCar(collo.getKeyColloCar());
						colloScorta.setSessioneLavoro(sessioneLavoro);
						colloScorta.setPezzi(quantitàDisponibile); //TODO - Verificare che vada bene questo valore.
						//Creo la riga d'ubicazione per prelievo, non avrà indicazioni sull'ubicazione.
						Righiubicpre rigaUbicazione = new Righiubicpre();
						rigaUbicazione.setIDcollipack(prodotto.getIdColliPack());
						rigaUbicazione.setIdRigoOrdine(riga.getIdRigoOrdine());
						rigaUbicazione.setIdubica(0);
						rigaUbicazione.setIduniarticolo(prodotto.getCodiceArticolo());
						rigaUbicazione.setMagazzino(riga.getMagazzino());
						rigaUbicazione.setNrcollo(999999999);
						rigaUbicazione.setNrlista(riga.getNrLista());
						rigaUbicazione.setQuantita(quantitàRichiesta);
						rigaUbicazione.setRaggliste(riga.getNrLista());
						rigaUbicazione.setSessioneLavoro(sessioneLavoro);
						rigaUbicazione.setTipoUbicazione("SC");
						//Aggiungo all'oggetto assegnazione gli oggetti scorta e scortacollo.
						assegnazione.aggiungiScortaUbicata(scorta);
						assegnazione.aggiungiScorteNonUbicate(scortaNonUbicata);
						assegnazione.aggiungiColloConScorte(colloScorta);
						assegnazione.setTipo(TipoAssegnazione.NON_UBICATA);
						//Rimuovo l'elemento corrente dalle successive iterazioni della lista. (Non dovrebbe servire a meno di non scambiare l'ordine)
						iteratorNonUbicati.remove();
						//Scalo la quantità richiesta di quella che è disponibile nella scorta.
						quantitàRichiesta -= quantitàDisponibile;
					}
					//Se la quantità richiesta è scesa a 0 procedo con la prossima riga.
					if (quantitàRichiesta <= 0) {
						continue assegnazioneRiga;
					}
				}
			}
		}
		//Aggiorno i campi necessari sulla testata
		aggiornaValoriTestata(testata);
		return listaAssegnazione;
	}
	
	private void aggiornaValoriTestata(TestataOrdini testata) {
		double quantitàTotaleOrdinata = testata.getQtaTotaleSpedire();
		double quantitàTotaleAssegnata = testata.getQtaAssegnata();
		double percentualeAssegnazione = (quantitàTotaleAssegnata / quantitàTotaleOrdinata) * 100;
		testata.setPercAssegnata(percentualeAssegnazione);
		if (quantitàTotaleAssegnata < quantitàTotaleOrdinata) {
			testata.setStatoUbicazione("UP");
			testata.setFlag1("X");
		} else {
			testata.setStatoUbicazione("UT");
			testata.setFlag1(" ");
		}
	}
	
	private void assegnaSuColliAPrelievo(List<ColliPack> prodotti, AssegnazioneProdotto assegnazione, TestataOrdini testata, RighiOrdine riga) {
		Iterator<ColliPack> iteratorPrelievo = prodotti.iterator();
		while (iteratorPrelievo.hasNext()) {
			ColliPack prodotto = iteratorPrelievo.next();
			//Trovo il collo e l'ubicazione corrispondente.
			ColliCarico collo = trovoColloProdotto(prodotto);
			Ubicazioni ubicazione = trovaUbicazioneCollo(collo);
			//Condizioni: Collo ubicato e Ubicazione a prelievo.
			if (collo != null && collo.getUbicato().equals("SI") && ubicazione != null && ubicazione.getTipoUbica().equals("PR")) {
				//Trovo quanti pezzi ci sono e li scalo dalla riga e dal collipack.
				int quantitàGiàAssegnata = testata.getQtaAssegnata();
				int quantitàRigaGiàAssegnata = riga.getQtaAssegnata();
				int quantitàRichiesta = riga.getQtadaubicare();
				int quantitàGiàImpegnata = prodotto.getQtaimpegnata();
				int quantitàOriginale = prodotto.getQta();
				int quantitàDisponibile = quantitàOriginale - quantitàGiàImpegnata;
				//Se basta questa aggiorno gli oggetti e termino l'assegnazione della riga altrimenti scalo quello che è possibile scalare.
				if (quantitàRichiesta < quantitàDisponibile) {
					//Aggiorno testata, riga e prodotto disponibile
					testata.setQtaAssegnata(quantitàGiàAssegnata + quantitàRichiesta);
					riga.setQtadaubicare(0);
					riga.setQtaAssegnata(riga.getQtaSpedizione());
					prodotto.setQtaimpegnata(quantitàGiàImpegnata + quantitàRichiesta);
				} else {
					//Aggiorno testata, riga e prodotto disponibile
					testata.setQtaAssegnata(quantitàGiàAssegnata + quantitàDisponibile);
					riga.setQtadaubicare(quantitàRichiesta - quantitàDisponibile);
					riga.setQtaAssegnata(quantitàRigaGiàAssegnata + quantitàDisponibile);
					prodotto.setQtaimpegnata(quantitàGiàImpegnata + quantitàDisponibile);
					prodotto.setFlagimp("S");
				}
				//Creo la riga d'ubicazione per prelievo
				Righiubicpre rigaUbicazione = new Righiubicpre();
				rigaUbicazione.setIDcollipack(prodotto.getIdColliPack());
				rigaUbicazione.setIdRigoOrdine(riga.getIdRigoOrdine());
				rigaUbicazione.setIduniarticolo(prodotto.getCodiceArticolo());
				rigaUbicazione.setMagazzino(riga.getMagazzino());
				rigaUbicazione.setNrcollo(collo.getNrCollo());
				rigaUbicazione.setNrlista(riga.getNrLista());
				rigaUbicazione.setQuantita(quantitàRichiesta);
				rigaUbicazione.setRaggliste(riga.getNrLista());
				rigaUbicazione.setSessioneLavoro(testata.getSessioneLavoro());
				rigaUbicazione.setTipoUbicazione("PR");
				copiaUbicazione(rigaUbicazione, ubicazione);
				//Aggiungo le info all'assegnazione.
				assegnazione.aggiungiRigaUbicazione(rigaUbicazione);
				assegnazione.aggiungiProdotto(prodotto);
				assegnazione.aggiungiColli(collo);
				assegnazione.aggiungiUbicazione(ubicazione);
				assegnazione.setTipo(TipoAssegnazione.NORMALE);
				//Copio le informazioni di ubicazione sulla riga, eventualmente sovrascrivendo.
				riga.setNrcollo(collo.getKeyColloCar());
				copiaUbicazione(riga, ubicazione);
				//Rimuovo l'elemento corrente dalle successive iterazioni della lista se non è più disponibile.
				if (prodotto.getFlagimp().equals("S")) {
					iteratorPrelievo.remove();
				}
				//Se la quantità richiesta è scesa a 0 procedo con la prossima riga.
				if (riga.getQtadaubicare() == 0) {
					break;
				}
				
			}
		}
	}
	
	private void assegnaSuColliAScorta(List<ColliPack> prodotti, AssegnazioneProdotto assegnazione, RighiOrdine riga, String sessioneLavoro) {
		Iterator<ColliPack> iteratorScorte = prodotti.iterator();
		while (iteratorScorte.hasNext()) {
			ColliPack prodotto = iteratorScorte.next();				
			int quantitàRichiesta = riga.getQtadaubicare();
			//Trovo il collo e l'ubicazione corrispondente.
			ColliCarico collo = trovoColloProdotto(prodotto);
			Ubicazioni ubicazione = trovaUbicazioneCollo(collo);
			//Condizioni: Collo ubicato e Ubicazione a scorta.
			if (collo != null && collo.getUbicato().equals("SI") && ubicazione != null && ubicazione.getTipoUbica().equals("SC")) {
				//Trovo quanti pezzi ci sono e vedo se bastano a coprire quelli richiesti
				int quantitàGiàImpegnata = prodotto.getQtaimpegnata();
				int quantitàOriginale = prodotto.getQta();
				int quantitàDisponibile = quantitàOriginale - quantitàGiàImpegnata;
				Scorte scorta = new Scorte();
				scorta.setIduniarticolo(prodotto.getCodiceArticolo());
				scorta.setModello(prodotto.getCodiceArticolo());
				scorta.setMagazzino(riga.getMagazzino());
				scorta.setQtascorta(quantitàDisponibile);
				scorta.setQuantita(quantitàRichiesta);
				scorta.setNote("QUANTITA' DA UBICARE: ");
				copiaUbicazione(scorta, ubicazione);
				ScorteColli colloScorta = new ScorteColli();
				colloScorta.setIduniarticolo(prodotto.getCodiceArticolo());
				colloScorta.setKeyColloCar(collo.getKeyColloCar());
				colloScorta.setSessioneLavoro(sessioneLavoro);
				colloScorta.setPezzi(quantitàDisponibile); //TODO - Verificare che vada bene questo valore.
				colloScorta.setUbicazione(ubicazione.getKeyUbica());
				//Creo la riga d'ubicazione per prelievo
				Righiubicpre rigaUbicazione = new Righiubicpre();
				rigaUbicazione.setIDcollipack(prodotto.getIdColliPack());
				rigaUbicazione.setIdRigoOrdine(riga.getIdRigoOrdine());
				rigaUbicazione.setIduniarticolo(prodotto.getCodiceArticolo());
				rigaUbicazione.setMagazzino(riga.getMagazzino());
				rigaUbicazione.setNrcollo(999999999);
				rigaUbicazione.setNrlista(riga.getNrLista());
				rigaUbicazione.setQuantita(quantitàRichiesta);
				rigaUbicazione.setRaggliste(riga.getNrLista());
				rigaUbicazione.setSessioneLavoro(sessioneLavoro);
				rigaUbicazione.setTipoUbicazione("SC");
				copiaUbicazione(rigaUbicazione, ubicazione);
				//Aggiungo all'oggetto assegnazione gli oggetti scorta e scortacollo.
				assegnazione.aggiungiRigaUbicazione(rigaUbicazione);
				assegnazione.aggiungiScortaUbicata(scorta);
				assegnazione.aggiungiColloConScorte(colloScorta);
				assegnazione.setTipo(TipoAssegnazione.SCORTE);
				//Rimuovo l'elemento corrente dalle successive iterazioni della lista.
				iteratorScorte.remove();
				//Scalo la quantità richiesta di quella che è disponibile nella scorta.
				quantitàRichiesta -= quantitàDisponibile;
			}
			//Se la quantità richiesta è scesa a 0 procedo con la prossima riga.
			if (quantitàRichiesta <= 0) {
				break;
			}
		}
	}
	
	private void assegnaSuColliNonUbicati(List<ColliPack> prodotti, AssegnazioneProdotto assegnazione, RighiOrdine riga, String sessioneLavoro) {
		Iterator<ColliPack> iteratorNonUbicati = prodotti.iterator();
		while (iteratorNonUbicati.hasNext()) {
			ColliPack prodotto = iteratorNonUbicati.next();
			int quantitàRichiesta = riga.getQtadaubicare();
			//Trovo il collo e l'ubicazione corrispondente.
			ColliCarico collo = trovoColloProdotto(prodotto);
			//Condizioni: Collo non ubicato.
			if (collo != null && collo.getUbicato().equals("NO")) {
				//Trovo quanti pezzi ci sono e vedo se bastano a coprire quelli richiesti
				int quantitàGiàImpegnata = prodotto.getQtaimpegnata();
				int quantitàOriginale = prodotto.getQta();
				int quantitàDisponibile = quantitàOriginale - quantitàGiàImpegnata;
				//Scorta
				Scorte scorta = new Scorte();
				scorta.setIduniarticolo(prodotto.getCodiceArticolo());
				scorta.setModello(prodotto.getCodiceArticolo());
				scorta.setMagazzino(riga.getMagazzino());
				scorta.setQtascorta(quantitàDisponibile);
				scorta.setQuantita(quantitàRichiesta);
				scorta.setNote("QUANTITA' DA UBICARE: ");
				//Scorta non ubicata
				Scorte2 scortaNonUbicata = new Scorte2();
				scortaNonUbicata.setCollo(collo.getKeyColloCar());
				scortaNonUbicata.setIduniarticolo(prodotto.getCodiceArticolo());
				scortaNonUbicata.setQuantita(quantitàDisponibile);
				scortaNonUbicata.setSessioneLavoro(sessioneLavoro);
				//Collo scorta
				ScorteColli colloScorta = new ScorteColli();
				colloScorta.setIduniarticolo(prodotto.getCodiceArticolo());
				colloScorta.setKeyColloCar(collo.getKeyColloCar());
				colloScorta.setSessioneLavoro(sessioneLavoro);
				colloScorta.setPezzi(quantitàDisponibile); //TODO - Verificare che vada bene questo valore.
				//Creo la riga d'ubicazione per prelievo, non avrà indicazioni sull'ubicazione.
				Righiubicpre rigaUbicazione = new Righiubicpre();
				rigaUbicazione.setIDcollipack(prodotto.getIdColliPack());
				rigaUbicazione.setIdRigoOrdine(riga.getIdRigoOrdine());
				rigaUbicazione.setIduniarticolo(prodotto.getCodiceArticolo());
				rigaUbicazione.setMagazzino(riga.getMagazzino());
				rigaUbicazione.setNrcollo(999999999);
				rigaUbicazione.setNrlista(riga.getNrLista());
				rigaUbicazione.setQuantita(quantitàRichiesta);
				rigaUbicazione.setRaggliste(riga.getNrLista());
				rigaUbicazione.setSessioneLavoro(sessioneLavoro);
				rigaUbicazione.setTipoUbicazione("SC");
				//Aggiungo all'oggetto assegnazione gli oggetti scorta e scortacollo.
				assegnazione.aggiungiScortaUbicata(scorta);
				assegnazione.aggiungiScorteNonUbicate(scortaNonUbicata);
				assegnazione.aggiungiColloConScorte(colloScorta);
				assegnazione.setTipo(TipoAssegnazione.NON_UBICATA);
				//Rimuovo l'elemento corrente dalle successive iterazioni della lista. (Non dovrebbe servire a meno di non scambiare l'ordine)
				iteratorNonUbicati.remove();
				//Scalo la quantità richiesta di quella che è disponibile nella scorta.
				quantitàRichiesta -= quantitàDisponibile;
			}
			//Se la quantità richiesta è scesa a 0 procedo con la prossima riga.
			if (quantitàRichiesta <= 0) {
				break;
			}
		}
	}
	
	private void copiaUbicazione(Righiubicpre riga, Ubicazioni ubicazione) {
		riga.setArea(ubicazione.getArea());
		riga.setBox(ubicazione.getBox());
		riga.setCorsia(ubicazione.getCorsia());
		riga.setPiano(ubicazione.getPiano());
		riga.setScaffale(ubicazione.getScaffale());
		riga.setUbicazione(ubicazione.getKeyUbica());
	}
	
	private void copiaUbicazione(RighiOrdine riga, Ubicazioni ubicazione) {
		riga.setArea(ubicazione.getArea());
		riga.setBox(ubicazione.getBox());
		riga.setCorsia(ubicazione.getCorsia());
		riga.setPiano(ubicazione.getPiano());
		riga.setScaffale(ubicazione.getScaffale());
		riga.setUbicazione(ubicazione.getKeyUbica());
		riga.setKeyUbiPre(ubicazione.getKeyUbica());
	}

	private void copiaUbicazione(Scorte scorta, Ubicazioni ubicazione) {
		scorta.setArea(ubicazione.getArea());
		scorta.setBox(ubicazione.getBox());
		scorta.setCorsia(ubicazione.getCorsia());
		scorta.setPiano(ubicazione.getPiano());
		scorta.setScaffale(ubicazione.getScaffale());
		scorta.setUbicazione(ubicazione.getKeyUbica());
		scorta.setKeyUbicPre(ubicazione.getKeyUbica());
	}

	private ColliCarico trovoColloProdotto(ColliPack prodotto) {
		String numeroCollo = prodotto != null ? prodotto.getNrIdColloPk() : "-";
		//Controllo se l'ho già trovato precedentemente altrimenti effettua una ricerca.
		if (!mappaColli.containsKey(numeroCollo)) {
			EntityManager em = getManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ColliCarico> criteria = cb.createQuery(ColliCarico.class);
			Root<ColliCarico> member = criteria.from(ColliCarico.class);
			Predicate condizioneCancellazione = cb.equal(member.get("cancellato"), "NO");
			Predicate condizioneCollo = cb.equal(member.get("keyColloCar"), numeroCollo);
			criteria.select(member).where(cb.and(condizioneCancellazione, condizioneCollo));
			List<ColliCarico> list = em.createQuery(criteria).setMaxResults(1).getResultList();
			em.close();
			ColliCarico collo = list.isEmpty() ? null : list.get(0);
			mappaColli.put(numeroCollo, collo);
		}
		return mappaColli.get(numeroCollo);
	}
	
	private Ubicazioni trovaUbicazioneCollo(ColliCarico collo) {
		String codiceUbicazione = collo != null ? collo.getKeyUbicaCar() : "-";
		//Controllo se l'ho già trovata precedentemente altrimenti effettua una ricerca.
		if (!mappaUbicazioni.containsKey(codiceUbicazione)) {
			EntityManager em = getManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Ubicazioni> criteria = cb.createQuery(Ubicazioni.class);
			Root<Ubicazioni> member = criteria.from(Ubicazioni.class);
			Predicate condizioneUbicazione = cb.equal(member.get("keyUbica"), codiceUbicazione);
			criteria.select(member).where(condizioneUbicazione);
			List<Ubicazioni> list = em.createQuery(criteria).setMaxResults(1).getResultList();
			em.close();
			Ubicazioni ubicazione = list.isEmpty() ? null : list.get(0);
			mappaUbicazioni.put(codiceUbicazione, ubicazione);
		}
		return mappaUbicazioni.get(codiceUbicazione);
	}
	
	/**
	 * Seleziona tutti i ColliPack disponibili con il prodotto specificato.<br>
	 * Condizioni: ID univoco del prodotto uguale a quello specificato e flagImp = N 
	 * @param idUnivoco
	 * @return
	 */
	private List<ColliPack> getProdottiInMagazzino(RighiOrdine riga) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ColliPack> criteria = cb.createQuery(ColliPack.class);
		Root<ColliPack> member = criteria.from(ColliPack.class);
		Predicate condizioneArticolo = cb.equal(member.get("codiceArticolo"), riga.getCodiceArticolo());
		Predicate condizioneMagazzino = cb.equal(member.get("magazzino"), riga.getMagazzino());
		Predicate condizioneImpegno = cb.equal(member.get("flagimp"), "N");
		criteria.select(member).where(cb.and(condizioneArticolo, condizioneMagazzino, condizioneImpegno));
		List<ColliPack> list = em.createQuery(criteria).getResultList();
		em.close();
		return list;
	}
	
	/**
	 * Scrive nel db la finalizzazione dell'assegnazione e ne restituisce l'esito.
	 * @param assegnazione
	 * @return
	 */
	public boolean assegnaOrdine(List<AssegnazioneProdotto> assegnazioni) {
		boolean esito;
		System.out.println("");
		
		EntityManager em = getManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			//Cancellazione dati assegnazione precedente
			//TODO
			//Aggiorno la testata
			if (!assegnazioni.isEmpty()) {
				TestataOrdini testata = assegnazioni.get(0).getTestata();
				TestataOrdini entity = em.find(TestataOrdini.class, testata.getIdTestaSped());
				aggiornaValoriTestata(entity, testata);
				em.merge(entity);
			}
			//Aggiorno e inserisco tutto il resto
			for (AssegnazioneProdotto assegnazione : assegnazioni) {
				//Eseguo istruzioni in base al tipo d'assegnazione che ho fatto
				switch (assegnazione.getTipo()) {
					case NORMALE : 
					{
						//Inserisco le righe d'ubicazione
						for (Righiubicpre rigaUbicazione : assegnazione.getRigheUbicazione()) {
							em.persist(rigaUbicazione);
						}
						//Aggiorno collipack
						for (ColliPack prodotto : assegnazione.getProdotti()) {
							ColliPack entity = em.find(ColliPack.class, prodotto.getIdColliPack());
							aggiornaValoriColliPack(entity, prodotto);
							em.merge(entity);
						}
						//Aggiorno righe ordini
						RighiOrdine riga = assegnazione.getRiga();
						RighiOrdine entity = em.find(RighiOrdine.class, riga.getIdRigoOrdine());
						aggiornaValoriRigaOrdine(entity, riga);
						em.merge(entity);
					} break;
					case NON_UBICATA : 
					{
						//Inserisco le righe d'ubicazione
						for (Righiubicpre rigaUbicazione : assegnazione.getRigheUbicazione()) {
							em.persist(rigaUbicazione);
						}
						//Inserisco le scorte
						for (Scorte scorta : assegnazione.getScorteUbicate()) {
							em.persist(scorta);
						}
						//Inserisco le scorte non ubicate
						for (Scorte2 scortaNonUbicata : assegnazione.getScorteNonUbicate()) {
							em.persist(scortaNonUbicata);
						}
						//Inserisco i colli a scorta
						for (ScorteColli collo : assegnazione.getColliConScorte()) {
							em.persist(collo);
						}
					} break;
					case SCORTE : 
					{
						//Inserisco le righe d'ubicazione
						for (Righiubicpre rigaUbicazione : assegnazione.getRigheUbicazione()) {
							em.persist(rigaUbicazione);
						}
						//Inserisco le scorte
						for (Scorte scorta : assegnazione.getScorteUbicate()) {
							em.persist(scorta);
						}
						//Inserisco i colli a scorta
						for (ScorteColli collo : assegnazione.getColliConScorte()) {
							em.persist(collo);
						}
					} break;
				}
			}
			transaction.commit();
			esito = true;
		} catch (Exception e) {
			logger.error(e);
			esito = false;
			if (transaction.isActive())
				transaction.rollback();
		} finally {
			em.close();
		}
		return esito;
	}
	
	private void aggiornaValoriRigaOrdine(RighiOrdine entity, RighiOrdine riga) {
		entity.setQtadaubicare(riga.getQtadaubicare());
		entity.setQtaAssegnata(riga.getQtaAssegnata());
		entity.setNrcollo(riga.getNrcollo());
		entity.setArea(riga.getArea());
		entity.setBox(riga.getBox());
		entity.setCorsia(riga.getCorsia());
		entity.setPiano(riga.getPiano());
		entity.setScaffale(riga.getScaffale());
		entity.setUbicazione(riga.getUbicazione());
		entity.setKeyUbiPre(riga.getKeyUbiPre());
	}

	private void aggiornaValoriColliPack(ColliPack entity, ColliPack prodotto) {
		entity.setFlagimp(prodotto.getFlagimp());
		entity.setQta(prodotto.getQta());
		entity.setQtaimpegnata(prodotto.getQtaimpegnata());
	}

	private void aggiornaValoriTestata(TestataOrdini entity, TestataOrdini testata) {
		entity.setQtaAssegnata(testata.getQtaAssegnata());
		entity.setPercAssegnata(testata.getPercAssegnata());
		entity.setSessioneLavoro(testata.getSessioneLavoro());
		entity.setStatoUbicazione(testata.getStatoUbicazione());
		entity.setFlag1(testata.getFlag1());
	}
	
	private TestataOrdini getTestataPerPrelivevoScaduti(List<ColliPack> prodotti) {
		TestataOrdini testata = new TestataOrdini();
		Timestamp now = new Timestamp(new Date().getTime());
		int totaleProdotti = 0;
		for (ColliPack prodotto : prodotti) {
			int quantitàDisponibile = prodotto.getQta() - prodotto.getQtaimpegnata();
			totaleProdotti += quantitàDisponibile;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String sessioneLavoro = sdf.format(now);
		//Valorizzazione
		testata.setCodCliente("GLS");
		testata.setCodCorriere("GLS");
		testata.setCorriere("GLS");
		testata.setIdDestina(16);
		testata.setIdMittente(0);
		testata.setDataDoc(now);
		testata.setDataOrdine(now);
		testata.setFlag1("X");
		testata.setNote("Prelievo merce scaduta.");
		testata.setOperatore("Servizio Scadenze");
		testata.setQtaTotaleSpedire(totaleProdotti);
		testata.setSessioneLavoro(sessioneLavoro);
		testata.setStato("IMPO");
		testata.setStatoUbicazione("UP");
		testata.setTipoDoc("Scadenze");
		testata.setTipoIncasso("");
		testata.setTipoTrasporto("DEF");
		testata.setTipoDoc("DDT");
		testata.setTipoOrdine("ORDINE");
		testata.setValContrassegno(0.0);
		//Inserimento DB
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.persist(testata);
			t.commit();
		} catch (Exception e) {
			logger.error(e);
			testata = null;
			if (t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		//Se l'inserimento non è andato a buon fine lancio l'eccezione.
		if (testata == null)
			throw new RuntimeException("Inserimento della testata d'ordine fallito.");
		return testata;
	}
	
	private Articoli trovaArticoloDaIDUnivoco(String idUnivoco) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Articoli> criteria = cb.createQuery(Articoli.class);
		Root<Articoli> member = criteria.from(Articoli.class);
		Predicate condizioneID = cb.equal(member.get("idUniArticolo"), idUnivoco);
		criteria.select(member).where(condizioneID);
		List<Articoli> list = em.createQuery(criteria).setMaxResults(1).getResultList();
		em.close();
		Articoli articolo = list.isEmpty() ? null : list.get(0);
		return articolo;
	}
	
	private List<RighiOrdine> getRigheOrdinePerPrelievoScaduti(TestataOrdini testata, List<ColliPack> prodotti) {
		int numeroRiga = 1;
		List<RighiOrdine> righe = new LinkedList<>();
		//Valorizzazione delle righe.
		for (ColliPack prodotto : prodotti) {
			//Recupero le info necessarie all'inserimento della riga
			int quantitàDisponibile = prodotto.getQta() - prodotto.getQtaimpegnata();
			Articoli articolo = trovaArticoloDaIDUnivoco(prodotto.getCodiceArticolo());
			//Valorizzo la riga
			RighiOrdine riga = new RighiOrdine();
			riga.setBarraEAN(articolo.getBarraEAN());
			riga.setBarraUPC(articolo.getBarraUPC());
			riga.setCodBarre(articolo.getCodBarre());
			riga.setCodiceArticolo(articolo.getCodArtStr());
			riga.setColore(articolo.getColore());
			riga.setComposizione(articolo.getComposizione());
			riga.setDataOrdine(testata.getDataOrdine());
			riga.setDescrizione(articolo.getDescrizione());
			riga.setIdDestina(testata.getIdDestina());
			riga.setIdTestataOrdine(testata.getIdTestaSped());
			riga.setIdUnicoArt(articolo.getIdUniArticolo());
			riga.setMagazzino(prodotto.getMagazzino());
			riga.setNoteCliente("Scaduto");
			riga.setNrLista(testata.getNrLista());
			riga.setNrOrdine(testata.getNrOrdine());
			riga.setNrRigo(numeroRiga);
			riga.setQtadaubicare(quantitàDisponibile);
			riga.setQtaSpedizione(quantitàDisponibile);
			riga.setRagstampe1(testata.getNrLista());
			riga.setTaglia(articolo.getTaglia());
			//Aggiorno il numero di riga e l'aggiungo alla lista.
			numeroRiga += 1;
			righe.add(riga);
		}
		//Inserimento DB
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		boolean esito;
		try {
			t.begin();
			for (RighiOrdine riga : righe) {
				em.persist(riga);
			}
			t.commit();
			esito = true;
		} catch (Exception e) {
			logger.error(e);
			esito = false;
			if (t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		if (!esito)
			throw new RuntimeException("Inserimento delle righe d'ordine fallito.");
		return righe;
	}

	public TestataOrdini creaOrdineDaProdotti(List<ColliPack> prodotti) {
		//Crea testata ordini e righi ordine in base ai prodotti
		TestataOrdini testata = getTestataPerPrelivevoScaduti(prodotti);	
		List<RighiOrdine> righe = getRigheOrdinePerPrelievoScaduti(testata, prodotti);
		//Forza l'assegnazione delle righe sui colli pack specificati
		List<AssegnazioneProdotto> assegnazioni = assegnaScaduti(testata, righe, prodotti);
		//Aggiorno i campi necessari sulla testata
		aggiornaValoriTestata(testata);
		//Procedi come in maniera useta.
		assegnaOrdine(assegnazioni);
		return testata;
	}

	private List<AssegnazioneProdotto> assegnaScaduti(TestataOrdini testata, List<RighiOrdine> righe, List<ColliPack> prodotti) {
		List<AssegnazioneProdotto> listaAssegnazione = new LinkedList<>();
		for (RighiOrdine riga : righe) {
			AssegnazioneProdotto assegnazione = new AssegnazioneProdotto(testata, riga);
			listaAssegnazione.add(assegnazione);
			assegnaSuColliAPrelievo(prodotti, assegnazione, testata, riga);
		}
		if (!prodotti.isEmpty()) {
			throw new RuntimeException("Non sono stati utilizzati tutti i colli pack!?");
		}
		return listaAssegnazione;
	}
	
}
