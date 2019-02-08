package it.ltc.clienti.zes;

import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.ordine.MOrdine;

public class MOrdineZeS extends MOrdine {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void validaRiferimento() throws ModelValidationException {
		if (riferimentoOrdine == null || riferimentoOrdine.isEmpty())
			throw new ModelValidationException("Bisogna specificare un riferimento per l'ordine. Es. purchase order number");
		else if (riferimentoDocumento.length() > 40) {
			throw new ModelValidationException("Il riferimento per l'ordine è troppo lungo (MAX 20 caratteri)");
		}
	}

}
