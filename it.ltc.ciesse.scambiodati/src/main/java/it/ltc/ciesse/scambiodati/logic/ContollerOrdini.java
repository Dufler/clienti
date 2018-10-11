package it.ltc.ciesse.scambiodati.logic;

import it.ltc.database.model.legacy.Destinatari;
import it.ltc.model.interfaces.exception.ModelPersistenceException;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;
import it.ltc.model.persistence.ordine.ControllerOrdiniSQLServer;

public class ContollerOrdini extends ControllerOrdiniSQLServer {
	
	public ContollerOrdini() {
		super(Import.persistenceUnit);
	}
	
	@Override
	public Destinatari ottieniDestinatario(MIndirizzo destinatario) throws ModelPersistenceException {
		Destinatari entity = daoDestinatari.trovaDaCodice(destinatario.getCodice());
		if (entity == null)
			throw new ModelPersistenceException("Il destinatario indicato per l'ordine non esiste: " + destinatario.getCodice());
		return entity;
	}
	
}
