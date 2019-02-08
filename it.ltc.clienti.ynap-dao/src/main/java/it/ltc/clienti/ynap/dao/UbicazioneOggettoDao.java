package it.ltc.clienti.ynap.dao;

import java.util.LinkedList;
import java.util.List;

import it.ltc.clienti.ynap.model.UbicazioneOggetto;
import it.ltc.database.dao.CRUDDao;
import it.ltc.database.dao.CondizioneWhere;

public class UbicazioneOggettoDao extends CRUDDao<UbicazioneOggetto> {

	public UbicazioneOggettoDao() {
		super("legacy-ynap", UbicazioneOggetto.class);
	}

	@Override
	protected void updateValues(UbicazioneOggetto oldEntity, UbicazioneOggetto entity) {
		// TODO Auto-generated method stub
		
	}
	
	public UbicazioneOggetto trovaPezzoImballato(String numeroLista, String idUnivocoArticolo) {
		List<CondizioneWhere> conditions = new LinkedList<>();
		conditions.add(new CondizioneWhere("nrlista", numeroLista));
		conditions.add(new CondizioneWhere("iduniarticolo", idUnivocoArticolo));
		conditions.add(new CondizioneWhere("QtaImballata", 1));
		UbicazioneOggetto entity = findJustOne(conditions);
		return entity;
	}

}
