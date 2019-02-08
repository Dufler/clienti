package it.ltc.ciesse.scambiodati.model;

import java.util.LinkedList;
import java.util.List;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.bundle.Casse;
import it.ltc.utility.miscellanea.string.StringParser;

public class Assortimenti {
	
	private static final ArticoliDao daoArticoli = new ArticoliDao(ConfigurationUtility.getInstance().getPersistenceUnit());
	
	public static List<Casse> parsaKitArticoli(List<String> righe) {
		List<Casse> assortimenti = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 507);
		do {
			int operazione = parser.getIntero(0, 1);
			try {
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
					Integer qta = parser.getIntero(indexQta, indexQta + 10);
					if (qta != null && qta > 0) {
						Articoli articolo = daoArticoli.trovaDaSKU(skuBase + "_" + counter);
						if (articolo == null)
							throw new RuntimeException("Impossibile trovare l'anagrafica dell'articolo: '" + skuBase + "_" + counter + "'");
						Casse cassa = new Casse();
						cassa.setSkuBundle(kit.getIdUniArticolo());
						cassa.setSkuProdotto(articolo.getIdUniArticolo());
						cassa.setQuantitaProdotto(qta);
						cassa.setTipo("STANDARD");
						cassa.setModello(skuBase);
						cassa.setCodiceCassa(skuBase);
						assortimenti.add(cassa);
					}
					indexQta += 10;
					counter++;
				}
			}
			} catch (RuntimeException e) {
				//Non ho trovato l'articolo corrispondente, salto alla riga successiva.
			}
		} while (parser.prossimaLinea());
		return assortimenti;
	}

}
