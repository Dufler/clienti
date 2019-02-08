package it.ltc.clienti.ynap.dao;

import java.util.List;

import it.ltc.clienti.ynap.model.AnagraficaOggetto;
import it.ltc.database.dao.CRUDDao;

public class AnagraficaOggettoDao extends CRUDDao<AnagraficaOggetto> {

	public AnagraficaOggettoDao() {
		super("legacy-ynap", AnagraficaOggetto.class);
	}

	@Override
	protected void updateValues(AnagraficaOggetto oldEntity, AnagraficaOggetto entity) {
		// TODO Auto-generated method stub
		
	}
	
	public AnagraficaOggetto trovaDaRFID(String rfid) {
		AnagraficaOggetto entity = findOnlyOneEqualTo("CodArtStr", rfid);
		return entity;
	}
	
	public List<AnagraficaOggetto> trovaTutti() {
		List<AnagraficaOggetto> entities = findAll();
		return entities;
	}

}
