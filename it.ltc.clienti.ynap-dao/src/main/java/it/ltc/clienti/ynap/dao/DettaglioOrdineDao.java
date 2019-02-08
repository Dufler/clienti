package it.ltc.clienti.ynap.dao;

import java.util.List;

import it.ltc.clienti.ynap.model.DettaglioOrdine;
import it.ltc.database.dao.CRUDDao;

public class DettaglioOrdineDao extends CRUDDao<DettaglioOrdine> {

	public DettaglioOrdineDao() {
		super("legacy-ynap", DettaglioOrdine.class);
	}

	@Override
	protected void updateValues(DettaglioOrdine oldEntity, DettaglioOrdine entity) {
		// TODO Auto-generated method stub
		
	}
	
	public List<DettaglioOrdine> trovaDaNumeroLista(String numeroLista) {
		List<DettaglioOrdine> entities = findAllEqualTo("NrLista", numeroLista);
		return entities;
	}

}
