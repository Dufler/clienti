package it.ltc.ciesse.scambiodati.model;

import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.logic.Import;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.bundle.CasseKIT;
import it.ltc.utility.miscellanea.string.StringParser;

public class Assortimenti {
	
	private static final ArticoliDao daoArticoli = new ArticoliDao(Import.persistenceUnit);
	
	public static List<CasseKIT> parsaKitArticoli(List<String> righe) {
		List<CasseKIT> assortimenti = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 507);
		do {
			int operazione = parser.getIntero(0, 1);
			if (operazione == 1 || operazione == 2) {
				String skuKit =  parser.getStringa(1, 53);
				Articoli kit = daoArticoli.trovaDaSKUVecchio(skuKit);
				if (kit == null)
					throw new RuntimeException("Impossibile trovare l'anagrafica del kit: '" + skuKit + "'");
				String skuArticolo = parser.getStringa(53, 105);
				Articoli modello = daoArticoli.trovaDaSKUVecchio(skuArticolo);
				if (modello == null)
					throw new RuntimeException("Impossibile trovare l'anagrafica dell'articolo: '" + skuArticolo + "'");
				String skuBase = modello.getCodArtStr().split("_")[0];
				int indexQta = 105;
				int counter = 1;
				while (indexQta < 506) {
					int qta = parser.getIntero(indexQta, indexQta + 10);
					if (qta > 0) {
						Articoli articolo = daoArticoli.trovaDaSKU(skuBase + "_" + counter);
						if (articolo == null)
							throw new RuntimeException("Impossibile trovare l'anagrafica dell'articolo: '" + skuBase + "_" + counter + "'");
						CasseKIT cassa = new CasseKIT();
						cassa.setSkuBundle(kit.getIdUniArticolo());
						cassa.setSkuProdotto(articolo.getIdUniArticolo());
						cassa.setQuantitaProdotto(qta);
						assortimenti.add(cassa);
					}
					indexQta += 10;
					counter++;
				}
			}
		} while (parser.prossimaLinea());
		return assortimenti;
	}

}
