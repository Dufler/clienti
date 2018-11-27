package it.ltc.clienti.forza.ftp;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import it.ltc.clienti.forza.ftp.model.LinnworksOrderStatus;
import it.ltc.clienti.forza.ftp.model.LinnworksOrderStatus.Status;
import it.ltc.database.dao.Dao;
import it.ltc.database.model.legacy.TestataOrdini;

public class ManagerStatoOrdini extends Dao {
	
	private static final Logger logger = Logger.getLogger("Forza FTP data exchange: orders");
	
	public ManagerStatoOrdini(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	private List<TestataOrdini> getOrdiniPerStato(String stato) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestataOrdini> criteria = cb.createQuery(TestataOrdini.class);
		Root<TestataOrdini> member = criteria.from(TestataOrdini.class);
		criteria.select(member).where(cb.equal(member.get("stato"), stato));
		List<TestataOrdini> list = em.createQuery(criteria).getResultList();
		em.close();
		return list;
	}
	
	private boolean aggiornaStatoOrdine(int idOrdine, String stato) {
		boolean update;
		EntityManager em = getManager();
		TestataOrdini testata = em.find(TestataOrdini.class, idOrdine);
		if (testata != null) {
			testata.setStato(stato);
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				em.merge(testata);
				t.commit();
				update = true;
			} catch(Exception e) {
				logger.error(e);
				update = false;
				t.rollback();
			} finally {
				em.close();
			}
		} else {
			update = false;
		}
		return update;
	}
	
	public List<LinnworksOrderStatus> recuperaStatoOrdini() {
		List<LinnworksOrderStatus> lista = new LinkedList<LinnworksOrderStatus>();
		//Recupero tutti gli ordini con errori in stato "INSE"
		List<TestataOrdini> testateInse = getOrdiniPerStato("INSE");
		//Per ogni ordine trovato genero una riga e specifico il motivo dell'errore con quello che trovo su note.
		for (TestataOrdini testata : testateInse) {
			String orderID = testata.getNrOrdine(); //Il campo rifordcli è buono solo per gli ordini correttamente importati.
			String tracking = testata.getNrLetteraVettura();
			String servizio = testata.getTipoTrasporto();
			String errors = testata.getNote();
			LinnworksOrderStatus status = new LinnworksOrderStatus(orderID, Status.ERROR, tracking, servizio, errors);
			lista.add(status);
			//Aggiorno lo stato dell'ordine a "ERRO"
			boolean update = aggiornaStatoOrdine(testata.getIdTestaSped(), "ERRO");
			if (update)
				logger.info("Aggiornato lo stato dell'ordine '" + testata.getNrOrdine() + "' a ERRO.");
			else
				logger.error("Impossibile aggiornare lo stato dell'ordine '"+ testata.getNrOrdine() + "'.");
		}
		List<TestataOrdini> testateAnnu = getOrdiniPerStato("ANNU");
		//Per ogni ordine trovato genero una riga e specifico che è stato cancellato.
		for (TestataOrdini testata : testateAnnu) {
			String orderID = testata.getNrOrdine(); //Il campo rifordcli è buono solo per gli ordini correttamente importati.
			String tracking = testata.getNrLetteraVettura();
			String servizio = testata.getTipoTrasporto();
			String errors = "Order cancelled";
			LinnworksOrderStatus status = new LinnworksOrderStatus(orderID, Status.CANCELED, tracking, servizio, errors);
			lista.add(status);
			//Aggiorno lo stato dell'ordine a "ANNT" (Annullato trasmesso come concordato con Andrea)
			boolean update = aggiornaStatoOrdine(testata.getIdTestaSped(), "ANNT");
			if (update)
				logger.info("Aggiornato lo stato dell'ordine '" + testata.getNrOrdine() + "' a ANNT.");
			else
				logger.error("Impossibile aggiornare lo stato dell'ordine '"+ testata.getNrOrdine() + "'.");
		}			
		//Recupero tutti gli ordini in stato "SPED"
		List<TestataOrdini> testateSped = getOrdiniPerStato("SPED");
		//Per ogni ordine trovato genero una riga e vi riporto il tracking number della spedizione.
		for (TestataOrdini testata : testateSped) {
			String orderID = testata.getRifOrdineCli();
			String tracking = testata.getNrLetteraVettura();
			String servizio = testata.getTipoTrasporto();
			String errors = "";
			LinnworksOrderStatus status = new LinnworksOrderStatus(orderID, Status.SHIPPED, tracking, servizio, errors);
			lista.add(status);
			//Aggiorno lo stato dell'ordine a "FINE"
			boolean update = aggiornaStatoOrdine(testata.getIdTestaSped(), "FINE");
			if (update)
				logger.info("Aggiornato lo stato dell'ordine '" + testata.getNrOrdine() + "' a FINE.");
			else
				logger.error("Impossibile aggiornare lo stato dell'ordine '"+ testata.getNrOrdine() + "'.");
		}
		return lista;
	}

}
