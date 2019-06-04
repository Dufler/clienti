package it.ltc.ciesse.scambiodati.logic.exception;

/**
 * Errore che può verificarsi quando non ci sono ancora state passate le info sui colli prima delle info sulla spedizione.
 * @author Damiano
 *
 */
public class NessunColloTrovatoException extends RuntimeException {

	public NessunColloTrovatoException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
