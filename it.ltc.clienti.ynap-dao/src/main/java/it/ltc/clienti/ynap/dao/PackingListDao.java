package it.ltc.clienti.ynap.dao;

import java.util.LinkedList;
import java.util.List;

import it.ltc.clienti.ynap.model.PackingList;
import it.ltc.database.dao.CRUDDao;
import it.ltc.database.dao.CondizioneWhere;

public class PackingListDao extends CRUDDao<PackingList> {

	public PackingListDao() {
		super("legacy-ynap", PackingList.class);
	}

	@Override
	protected void updateValues(PackingList oldEntity, PackingList entity) {
		// TODO Auto-generated method stub
		
	}
	
	public PackingList trovaDaRiferimentoEQualita(String riferimento, String qualita) {
		List<CondizioneWhere> conditions = new LinkedList<>();
		conditions.add(new CondizioneWhere("NrPaki", riferimento));
		conditions.add(new CondizioneWhere("TipoPacking", qualita));
		PackingList entity = findOne(conditions);
		return entity;
	}
	
	public List<PackingList> trovaCarichiGenerati() {
		List<CondizioneWhere> conditions = new LinkedList<>();
		conditions.add(new CondizioneWhere("GeneratoMov", "SI"));
		conditions.add(new CondizioneWhere("FlagTra", "S"));
		List<PackingList> entitites = findAll(conditions);
		return entitites;
	}

}
