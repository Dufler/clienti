package it.ltc.clienti.redone.model.specific;

import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;

public class IndirizzoFornitore extends MIndirizzo {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void valida() throws ModelValidationException {
		//Ragione sociale
		if (ragioneSociale == null || ragioneSociale.isEmpty())
			throw new ModelValidationException("Bisogna specificare la ragione sociale.");
		else if (ragioneSociale.length() > 100) 
			throw new ModelValidationException("La ragione sociale specificata e' troppo lunga. (Max 100 caratteri)");
		//Indirizzo
		if (indirizzo == null || indirizzo.isEmpty())
			throw new ModelValidationException("Bisogna specificare l'indirizzo.");
		else if (indirizzo.length() > 250)
			throw new ModelValidationException("L'indirizzo specificato e' troppo lungo. (Max 250 caratteri)");
		//CAP
		if (cap == null || cap.isEmpty()) 
			throw new ModelValidationException("Bisogna specificare il cap.");
		else if (cap.length() < 2 || cap.length() > 10)
			throw new ModelValidationException("Il cap specificato non è valido.");
		//Localita
		if (localita == null || localita.isEmpty())
			throw new ModelValidationException("Bisogna specificare la localita'.");
		else if (localita.length() > 50)
			throw new ModelValidationException("La localita' specificata e' troppo lunga. (Max 40 caratteri)");
		//Provincia
		if (provincia == null || provincia.isEmpty())
			throw new ModelValidationException("Bisogna specificare la provincia.");
		else if (provincia.length() != 2)
			throw new ModelValidationException("La provincia non è valida. Es. PG. Indicare XX per l'estero.");
		//Nazione
//		if (nazione == null || nazione.isEmpty())
//			throw new ModelValidationException("Bisogna specificare la nazione.");
//		else if (nazione.length() != 3)
//			throw new ModelValidationException("La nazione specificata non è valida. Utilizzare il codice ISO a 3 caratteri. Es. ITA");
		//Telefono
		if (telefono != null && telefono.length() > 30)
			throw new ModelValidationException("Il numero di telefono specificato e' troppo lungo. (Max 30 caratteri) contattare il reparto IT di LTC");
		//Email
		if (email != null && email.length() > 100)
			throw new ModelValidationException("L'indirizzo email specificato e' troppo lungo. (Max 100 caratteri) contattare il reparto IT di LTC");
	}

}
