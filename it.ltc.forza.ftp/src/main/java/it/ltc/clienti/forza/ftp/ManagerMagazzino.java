package it.ltc.clienti.forza.ftp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.clienti.forza.ConfigurationUtility;
import it.ltc.clienti.forza.ftp.model.LinnworksInvenctoryLine;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.MagaSdDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.MagaSd;

public class ManagerMagazzino {
	
	private final SimpleDateFormat sdf;
	private final MagaSdDao daoSaldi;
	private final ArticoliDao daoArticoli;
	
	public ManagerMagazzino(String persistenceUnit) {
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		daoSaldi = new MagaSdDao(persistenceUnit);
		daoArticoli = new ArticoliDao(persistenceUnit);
	}
	
	public List<LinnworksInvenctoryLine> recuperaGiacenzeDiMagazzino() {
		//Recupero tutti gli elementi di MagaSd e metto insieme quelli che hanno il medesimo ID univoco articolo.
		Date now = new Date();
		String today = sdf.format(now);
		List<LinnworksInvenctoryLine> dettagliCarichiDaInviare = new LinkedList<>();
		HashMap<String, Integer> mappaQuantita = new HashMap<>();
		List<MagaSd> saldi = daoSaldi.trovaTuttiPerMagazzino(ConfigurationUtility.getInstance().getMagazzinoDefault());
		for (MagaSd saldo : saldi) {
			//Se la disponibilità è negativa la faccio diventare 0.
			int disponibile = saldo.getDisponibile() >= 0 ? saldo.getDisponibile() : 0;
			//Guardo se ho già registrato una disponibilità per un altro magazzino, se si allora li sommo.
			String id = saldo.getIdUniArticolo();
			int vecchiaQuantita = mappaQuantita.containsKey(id) ? mappaQuantita.get(id) : 0;
			vecchiaQuantita += disponibile;
			mappaQuantita.put(id, vecchiaQuantita);
		}
		//Per ognuno degli elementi trovati genero una riga
		for (String key : mappaQuantita.keySet()) {
			Articoli articolo = daoArticoli.trovaDaIDUnivoco(key);
			int quantita = mappaQuantita.get(key);
			if (articolo != null) {
				String sku = articolo.getCodArtStr();
				String ean = articolo.getBarraEAN();
				String quantity = Integer.toString(quantita);
				LinnworksInvenctoryLine line = new LinnworksInvenctoryLine(sku, ean, quantity, "LTC", today, "Forza");
				dettagliCarichiDaInviare.add(line);
			}
		}
		return dettagliCarichiDaInviare;
	}

}