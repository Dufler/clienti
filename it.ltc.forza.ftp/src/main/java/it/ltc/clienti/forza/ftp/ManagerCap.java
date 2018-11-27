package it.ltc.clienti.forza.ftp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.centrale.Cap;

public class ManagerCap extends Dao {
	
	private static ManagerCap instance;

	private ManagerCap() {
		super("produzione");
	}

	public static ManagerCap getInstance() {
		if (instance == null) {
			instance = new ManagerCap();
		}
		return instance;
	}
	
	public Cap trovaCap(String cap) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cap> criteria = cb.createQuery(Cap.class);
		Root<Cap> member = criteria.from(Cap.class);
		criteria.select(member).where(cb.equal(member.get("cap"), cap));
		List<Cap> list = em.createQuery(criteria).setMaxResults(1).getResultList();
		em.close();
		Cap match = list.size() == 1 ? list.get(0) : null;
		return match;
	}

}
