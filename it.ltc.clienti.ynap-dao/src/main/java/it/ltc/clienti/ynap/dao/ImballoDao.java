package it.ltc.clienti.ynap.dao;

import java.util.LinkedList;
import java.util.List;

import it.ltc.clienti.ynap.model.Imballo;
import it.ltc.database.dao.CRUDDao;
import it.ltc.database.dao.CondizioneWhere;

public class ImballoDao extends CRUDDao<Imballo> {

	public ImballoDao() {
		super("legacy-ynap", Imballo.class);
	}

	@Override
	protected void updateValues(Imballo oldEntity, Imballo entity) {
		// TODO Auto-generated method stub
		
	}
	
	public Imballo trovaPezzoImballato(String numeroLista, String rfid) {
		List<CondizioneWhere> conditions = new LinkedList<>();
		conditions.add(new CondizioneWhere("NrLista", numeroLista));
		conditions.add(new CondizioneWhere("CodiceArticolo", rfid));
		conditions.add(new CondizioneWhere("QtaImballata", 1));
		Imballo entity = findJustOne(conditions);
		return entity;
	}

}
