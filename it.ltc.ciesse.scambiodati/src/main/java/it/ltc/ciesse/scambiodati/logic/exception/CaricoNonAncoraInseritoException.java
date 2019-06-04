package it.ltc.ciesse.scambiodati.logic.exception;

/**
 * Eccezione che viene lanciata quando vengono passate delle righe di carico ma non è ancora stata passata la testata corrispondente.
 * @author Damiano
 *
 */
public class CaricoNonAncoraInseritoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CaricoNonAncoraInseritoException(String message) {
		super(message);
	}

}
