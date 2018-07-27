package aggiornacoltorti;

import java.io.File;

import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.utility.csv.FileCSV;

public class AggiornaMain {

	public static void main(String[] args) throws Exception {
		File file = new File("C:\\Users\\Damiano\\Documents\\LTC\\Coltorti\\CorrezioneCategoriaMerceologica.csv");
		FileCSV csv = FileCSV.leggiFile(file, true, ";", ";");
		ArticoliDao dao = new ArticoliDao("legacy-coltorti");
		for (String[] riga : csv.getRighe()) {
			if (riga.length != 2)
				continue;
			String codice = riga[0];
			String categoria = riga[1];
//			String colore = riga[1];
//			String sottoCategoria = riga[2];
//			String composizione = riga[3];
//			String descrizioneAggiuntiva = riga[4];
			Articoli articolo = dao.trovaDaSKU(codice);
			if (articolo != null) {
				articolo.setCategoria(categoria);
				articolo.setCatMercGruppo(categoria);
//				articolo.setColore(colore);
//				articolo.setCatMercDett(sottoCategoria);
//				articolo.setComposizione(composizione);
//				articolo.setDescAggiuntiva(descrizioneAggiuntiva);
				Articoli update = dao.aggiorna(articolo);
				if (update == null)
					System.out.println("Errore");
			} else {
				System.out.println("Non trovato");
			}
		}

	}

}
