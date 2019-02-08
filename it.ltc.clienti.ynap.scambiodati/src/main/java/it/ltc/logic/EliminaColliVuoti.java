package it.ltc.logic;

import java.util.Date;
import java.util.List;

import it.ltc.clienti.ynap.dao.ColliDao;
import it.ltc.clienti.ynap.dao.OggettiDao;
import it.ltc.clienti.ynap.model.Collo;
import it.ltc.clienti.ynap.model.Oggetto;

public class EliminaColliVuoti {
	
	public static final String STATO_OGGETTO_TERMINATO = "T";
	
	private static EliminaColliVuoti instance;
	
	private final ColliDao managerColli;
	private final OggettiDao managerOggetti;
	
	private EliminaColliVuoti() {
		managerColli = new ColliDao();
		managerOggetti = new OggettiDao();
	}
	
	public static EliminaColliVuoti getInstance() {
		if (instance == null)
			instance = new EliminaColliVuoti();
		return instance;
	}
	
	public void eliminaColliVuoti() {
		Date oggi = new Date();
		List<Collo> colli = managerColli.trovaColliNonCancellati();
		for (Collo collo : colli) {
			List<Oggetto> listaOggetti = managerOggetti.trovaPerCollo(collo.getNumeroCollo());
			boolean tuttiUsciti = true;
			for (Oggetto oggetto : listaOggetti) {
				String stato = oggetto.getFlag();
				if (!STATO_OGGETTO_TERMINATO.equals(stato)) {
					tuttiUsciti = false;
					break;
				}
			}
			if (tuttiUsciti) {
				collo.setCancellato("SI");
				collo.setDistrutto("SI");
				collo.setUtenteDistruttore("SRV");
				collo.setDataDistruzione(oggi);
				managerColli.aggiorna(collo);
			}
		}
	}

}