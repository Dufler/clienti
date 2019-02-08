package it.ltc.clienti.forza.ftp;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import it.ltc.clienti.forza.ftp.model.ProdottoInScadenza;
import it.ltc.database.dao.Dao;
import it.ltc.database.model.legacy.ColliPack;

public class ManagerScadenze extends Dao {
	
	private static final Logger logger = Logger.getLogger(ManagerScadenze.class);

	public ManagerScadenze(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	public List<ProdottoInScadenza> getAvvisoProdottiInScadenza(int giorniScadenza) {
		//Ricerco i prodotti che stanno per scadere.
		List<ProdottoInScadenza> prodotti = new LinkedList<ProdottoInScadenza>();
		GregorianCalendar scadenza = new GregorianCalendar();
		scadenza.add(Calendar.DAY_OF_YEAR, giorniScadenza);
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<ColliPack> criteria = cb.createQuery(ColliPack.class);
	        Root<ColliPack> member = criteria.from(ColliPack.class);
	        Predicate condizioneImpegno = cb.equal(member.get("flagimp"), "N");
	        Predicate condizioneScadenza = cb.lessThan(member.get("dataScadenza"), scadenza.getTime());
	        Predicate condizioneLotto = cb.isNull(member.get("lotto"));
	        criteria.select(member).where(cb.and(condizioneImpegno, condizioneScadenza, condizioneLotto));
	        List<ColliPack> lista = em.createQuery(criteria).getResultList();
			for (ColliPack prodotto: lista) {
				prodotto.setLotto("SCADENZA");
				em.merge(prodotto);
				ProdottoInScadenza scadente = new ProdottoInScadenza(prodotto.getCodArtStr(), prodotto.getNrIdColloPk(), prodotto.getQta(), prodotto.getDataScadenza());
				prodotti.add(scadente);
			}
			t.commit();
		} catch (Exception e) {
			prodotti = null;
			logger.error(e.getMessage(), e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return prodotti;
	}
	
	public List<ProdottoInScadenza> getProdottiInScadenza(int giorniScadenza) {
		//Ricerco i prodotti che stanno per scadere.
		GregorianCalendar scadenza = new GregorianCalendar();
		scadenza.add(Calendar.DAY_OF_YEAR, giorniScadenza);
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ColliPack> criteria = cb.createQuery(ColliPack.class);
        Root<ColliPack> member = criteria.from(ColliPack.class);
        Predicate condizioneImpegno = cb.equal(member.get("flagimp"), "N");
        Predicate condizioneScadenza = cb.lessThan(member.get("dataScadenza"), scadenza.getTime());
        criteria.select(member).where(cb.and(condizioneImpegno, condizioneScadenza));
		List<ColliPack> lista = em.createQuery(criteria).getResultList();
		em.close();
		List<ProdottoInScadenza> prodotti = new LinkedList<ProdottoInScadenza>();
		for (ColliPack prodotto: lista) {
			ProdottoInScadenza scadente = new ProdottoInScadenza(prodotto.getCodArtStr(), prodotto.getNrIdColloPk(), prodotto.getQta(), prodotto.getDataScadenza());
			prodotti.add(scadente);
		}
		return prodotti;
	}
	
	public List<ColliPack> getColliPackInScadenza(int giorniScadenza) {
		//Ricerco i prodotti che stanno per scadere.
		GregorianCalendar scadenza = new GregorianCalendar();
		scadenza.add(Calendar.DAY_OF_YEAR, giorniScadenza);
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ColliPack> criteria = cb.createQuery(ColliPack.class);
        Root<ColliPack> member = criteria.from(ColliPack.class);
        Predicate condizioneGenerazioneCarico = cb.equal(member.get("flagtc"), 1); //31-05-2018: Ho aggiunto questa condizione perchè hanno contato carichi con merce scaduta che non sono mai stati generati.
        Predicate condizioneImpegno = cb.equal(member.get("flagimp"), "N");
        Predicate condizioneScadenza = cb.lessThan(member.get("dataScadenza"), scadenza.getTime());
        criteria.select(member).where(cb.and(condizioneGenerazioneCarico, condizioneImpegno, condizioneScadenza));
		List<ColliPack> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

}
