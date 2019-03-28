package it.ltc.clienti.ynap.dao;

import java.util.LinkedList;
import java.util.List;

import it.ltc.clienti.ynap.model.PackingListDettaglio;
import it.ltc.database.dao.CRUDDao;
import it.ltc.database.dao.CondizioneWhere;

public class PackingListDettagliDao extends CRUDDao<PackingListDettaglio> {

	public PackingListDettagliDao() {
		super("legacy-ynap", PackingListDettaglio.class);
	}
	
	public PackingListDettaglio inserisci(PackingListDettaglio dettaglio) {
		PackingListDettaglio entity = insert(dettaglio);
		return entity;
	}

	@Override
	protected void updateValues(PackingListDettaglio oldEntity, PackingListDettaglio entity) {
		// TODO Auto-generated method stub
		
	}
	
	public List<PackingListDettaglio> trovaDaSeriale(String seriale) {
		List<PackingListDettaglio> entities = findAllEqualTo("codiceRFID", seriale);
		return entities;
	}
	
	public PackingListDettaglio trovaDaSerialeEStato(String seriale, String stato) {
		List<CondizioneWhere> conditions = new LinkedList<>();
		conditions.add(new CondizioneWhere("codiceRFID", seriale));
		conditions.add(new CondizioneWhere("stato", stato));
		PackingListDettaglio entity = findOne(conditions);
		return entity;
	}
	
	public List<PackingListDettaglio> trovaDaCarico(int idCarico) {
		List<PackingListDettaglio> entities = findAllEqualTo("idPackingList", idCarico);
		return entities;
	}

}
