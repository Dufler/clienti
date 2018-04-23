package it.ltc.forza.ftp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.forza.ftp.model.LinnworksInvenctoryLine;

public class ManagerMagazzino extends Dao {
	
	private final SimpleDateFormat sdf;
	
	public ManagerMagazzino(String persistenceUnit) {
		super(persistenceUnit);
		sdf = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	private Articoli getArticoloDyIDUniArticolo(String idUnivoco) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Articoli> criteria = cb.createQuery(Articoli.class);
		Root<Articoli> member = criteria.from(Articoli.class);
		Predicate condizioneID = cb.equal(member.get("idUniArticolo"), idUnivoco);
		criteria.select(member).where(condizioneID);
		List<Articoli> list = em.createQuery(criteria).setMaxResults(1).getResultList();
		em.close();
		Articoli match = list.size() == 1 ? list.get(0) : null;
		return match;
	}
	
	private List<MagaSd> getSaldi() {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<MagaSd> criteria = cb.createQuery(MagaSd.class);
		Root<MagaSd> member = criteria.from(MagaSd.class);
		criteria.select(member);
		List<MagaSd> list = em.createQuery(criteria).getResultList();
		em.close();
		return list;
	}
	
	public List<LinnworksInvenctoryLine> recuperaGiacenzeDiMagazzino() {
		//Recupero tutti gli elementi di MagaSd e metto insieme quelli che hanno il medesimo ID univoco articolo.
		Date now = new Date();
		String today = sdf.format(now);
		List<LinnworksInvenctoryLine> dettagliCarichiDaInviare = new LinkedList<>();
		HashMap<String, Integer> mappaQuantita = new HashMap<>();
		List<MagaSd> saldi = getSaldi();
		for (MagaSd saldo : saldi) {
			//Se la disponibilità è negativa la faccio diventare 0.
			int disponibile = saldo.getDisponibile() >= 0 ? saldo.getDisponibile() : 0;
			//Guardo se ho già registrato una disponibilità per un altro magazzino, se si allora li sommo.
			String id = saldo.getIdUniArticolo();
			int vecchiaQuantita = mappaQuantita.containsKey(id) ? mappaQuantita.get(id) : 0;
			vecchiaQuantita += disponibile;
			mappaQuantita.put(id, vecchiaQuantita);
		}
		//Per ognuno degli elementi trovati genero una riga
		for (String key : mappaQuantita.keySet()) {
			Articoli articolo = getArticoloDyIDUniArticolo(key);
			int quantita = mappaQuantita.get(key);
			if (articolo != null) {
				String sku = articolo.getCodArtStr();
				String ean = articolo.getBarraEAN();
				String quantity = Integer.toString(quantita);
				LinnworksInvenctoryLine line = new LinnworksInvenctoryLine(sku, ean, quantity, "LTC", today, "Forza");
				dettagliCarichiDaInviare.add(line);
			}
		}
		return dettagliCarichiDaInviare;
	}

}