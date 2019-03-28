package it.ltc.clienti.ynap.dao;

import java.util.LinkedList;
import java.util.List;

import it.ltc.clienti.ynap.model.Oggetto;
import it.ltc.database.dao.CRUDDao;
import it.ltc.database.dao.CondizioneWhere;

public class OggettiDao extends CRUDDao<Oggetto> {

	public OggettiDao() {
		super("legacy-ynap", Oggetto.class);
	}

	@Override
	protected void updateValues(Oggetto oldEntity, Oggetto entity) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Oggetto> trovaDaStatoESeriale(String stato, String rfid) {
		List<CondizioneWhere> conditions = new LinkedList<>();
		conditions.add(new CondizioneWhere("stato", stato));
		conditions.add(new CondizioneWhere("codiceRFID", rfid));
		List<Oggetto> entities = findAll(conditions);
		return entities;
	}
	
	public List<Oggetto> trovaPerStato(String stato) {
		List<Oggetto> entities = findAllEqualTo("stato", stato);
		return entities;
	}
	
	public List<Oggetto> trovaPerCollo(String collo) {
		List<Oggetto> entities = findAllEqualTo("numeroCollo", collo);
		return entities;
	}

}
