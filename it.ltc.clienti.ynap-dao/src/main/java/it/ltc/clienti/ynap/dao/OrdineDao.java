package it.ltc.clienti.ynap.dao;

import java.util.List;

import it.ltc.clienti.ynap.model.Ordine;
import it.ltc.database.dao.CRUDDao;

public class OrdineDao extends CRUDDao<Ordine> {

	public OrdineDao() {
		super("legacy-ynap", Ordine.class);
	}

	@Override
	protected void updateValues(Ordine oldEntity, Ordine entity) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Ordine> trovaOrdiniDaStato(String stato) {
		List<Ordine> entities = findAllEqualTo("Stato", stato);
		return entities;
	}

}
