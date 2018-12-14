package it.ltc.ciesse.scambiodati.logic;

import it.ltc.ciesse.scambiodati.ConfigurationUtility;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.exception.ProductAlreadyExistentException;
import it.ltc.model.interfaces.prodotto.MProdotto;
import it.ltc.model.persistence.prodotto.ControllerProdottoSQLServer;

public class ControllerArticoli extends ControllerProdottoSQLServer {

	public ControllerArticoli() {
		super(ConfigurationUtility.getInstance().getPersistenceUnit());
	}
	
	@Override
	public void valida(MProdotto prodotto) throws ModelValidationException {
		//Controllo sul codice modello e taglia
		Articoli checkModelloTaglia = daoArticoli.trovaDaModelloETaglia(prodotto.getCodiceModello(), prodotto.getTaglia());
		if (checkModelloTaglia != null)
			throw new ProductAlreadyExistentException("(Legacy) E' gia' presente un prodotto con la stessa combinazione codice modello-taglia. (modello: " + prodotto.getCodiceModello() + ", taglia: " + prodotto.getTaglia() + ")");
		//Controllo barcode
		Articoli checkBarcode = daoArticoli.trovaDaBarcode(prodotto.getBarcode());
		if (checkBarcode != null)
			throw new ProductAlreadyExistentException("(Legacy) E' gia' presente un prodotto con lo stesso barcode. (" + prodotto.getBarcode() + ")");
		//Controllo chiave cliente
		Articoli checkChiave = daoArticoli.trovaDaSKU(prodotto.getChiaveCliente());
		if (checkChiave != null)
			throw new ProductAlreadyExistentException("(Legacy) E' gia' presente un prodotto con la stessa chiave identificativa. (" + prodotto.getChiaveCliente() + ")");
	}

}
