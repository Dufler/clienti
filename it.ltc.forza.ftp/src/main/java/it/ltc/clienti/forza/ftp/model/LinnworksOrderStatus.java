package it.ltc.clienti.forza.ftp.model;

/**
 * Classe che mappa una riga del file .csv da restituire sull'avanzamento degli ordini.
 * Il metodo <code>toString</code> è stato ridefinito in maniera da restituire la sua rappresentazione su riga .csv.
 * Il metodo statico <code>getColumnNames</code> restituisce la riga d'intestazione di un file .csv tipo.
 * @author Damiano
 *
 */
public class LinnworksOrderStatus {
	
	/**
	 * Enum che mappa tutti i possibili stati di un ordine su Linnworks.
	 * @author Damiano
	 *
	 */
	public enum Status { SHIPPED, COMPLETE, CANCELED, ERROR	}
	
	public static final String COLUMN_NAMES = "OrderId,Status,TrackingNumber,ShippingService,Error";
	
	private final String orderID;
	private final Status status;
	private final String trackingNumber;
	private final String shippingService;
	private final String error;
	
	/**
	 * Costruttore di default. Vanno specificati: orderID di Linnworks, status, tracking number, servizio della spedizione ed eventuali errori.
	 * @param orderID l'orderID come sui sistemi Linnworks.
	 * @param status lo status dell'ordine, deve essere uno dei possibili valori della enum.
	 * @param trackingNumber il tracking number della spedizione.
	 * @param shippingService il tipo di servizio della spedizione.
	 * @param error eventuali errori nell'ordine. Va inserito solo se lo status indicato è "ERROR"
	 */
	public LinnworksOrderStatus(String orderID, Status status, String trackingNumber, String shippingService, String error) {
		this.orderID = orderID;
		this.status = status;
		this.trackingNumber = trackingNumber;
		this.shippingService = shippingService;
		this.error = error;
	}

	public String getOrderID() {
		return orderID;
	}

	public Status getStatus() {
		return status;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public String getShippingService() {
		return shippingService;
	}

	public String getError() {
		return error;
	}

	@Override
	public String toString() {
		return orderID + "," + status + "," + trackingNumber + "," + shippingService + "," + error;
	}
	
	public static String getColumnNames() {
		return COLUMN_NAMES;
	}

}
