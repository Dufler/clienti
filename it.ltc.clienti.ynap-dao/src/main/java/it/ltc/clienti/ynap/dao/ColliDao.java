package it.ltc.clienti.ynap.dao;

import java.util.List;

import it.ltc.clienti.ynap.model.Collo;
import it.ltc.database.dao.CRUDDao;

public class ColliDao extends CRUDDao<Collo> {

	public ColliDao() {
		super("legacy-ynap", Collo.class);
	}
	
	public Collo aggiorna(Collo collo) {
		Collo entity = update(collo, collo.getId());
		return entity;
	}

	@Override
	protected void updateValues(Collo oldEntity, Collo entity) {
		oldEntity.setAnno(entity.getAnno());
		oldEntity.setBarcodeCliente(entity.getBarcodeCliente());
		oldEntity.setBarcodeCollo(entity.getBarcodeCollo());
		oldEntity.setCancellato(entity.getCancellato());
		oldEntity.setDataDistruzione(entity.getDataDistruzione());
		oldEntity.setDataLettura(entity.getDataLettura());
		oldEntity.setDistrutto(entity.getDistrutto());
		oldEntity.setFonteLettura(entity.getFonteLettura());
		oldEntity.setIdBox(entity.getIdBox());
		oldEntity.setIdPackingList(entity.getIdPackingList());
		oldEntity.setNote(entity.getNote());
		oldEntity.setNumeroCollo(entity.getNumeroCollo());
		oldEntity.setProgressivoNumeroCollo(entity.getProgressivoNumeroCollo());
		oldEntity.setUtenteDistruttore(entity.getUtenteDistruttore());
	}
	
	public List<Collo> trovaColliNonCancellati() {
		List<Collo> entities = findAllEqualTo("Cancellato", "NO");
		return entities;
	}

}
