package it.ltc.forza.ftp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Classe che mappa ogni riga dei file .csv con gli ordini. 
 * @author Damiano
 *
 */
public class LinnworksOrderLine {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private final String orderId;
	//private final String referenceNumber;
	//private final String externalReference;
	//private final String ChannelReference;
	private final String emailDestinazione;
	private final String telefonoDestinazione;
	//private final String CustomerCompany;
	private final String ragioneSocialeDestinazione;
	private final String indirizzoDestinazione1;
	private final String indirizzoDestinazione2;
	private final String indirizzoDestinazione3;
	private final String localitaDestinazione;
	private final String provinciaDestinazione;
	private final String capDestinazione;
	private final String nazioneDestinazione;
	private final String iso2NazioneDestinazione;
//	private final String BillingName;
//	private final String BillingCompany;
//	private final String BillingAddress1;
//	private final String BillingAddress2;
//	private final String BillingAddress3;
//	private final String BillingTown;
//	private final String BillingRegion;
//	private final String BillingPostcode;
//	private final String BillingCountryName;
//	private final String BillingPhoneNumber;
	private final Date ReceivedDate;
//	private final String ShippingCost;
//	private final String OrderTax;
	private final Double costoTotale;
//	private final String Currency;
//	private final String Paid;
//	private final String Status;
//	private final String Source;
//	private final String SubSource;
//	private final String DispatchBy;
//	private final String CreatedDate;
//	private final String ShippingServiceName;
//	private final String ShippingServiceTag;
//	private final String ShippingServiceCode;
//	private final String ShippingServiceVendor;
//	private final String PackagingGroup;
//	private final String TrackingNumber;
//	private final String ChannelBuyerName;
//	private final String Marker;
//	private final String PaymentMethod;
//	private final String OnHold;
//	private final String FulfillmentLocation;
	private final String sku;
//	private final String ItemTitle;
//	private final String OriginalTitle;
//	private final String ChannelSKU;
//	private final String ItemNumber;
	private final Integer quantita;
//	private final String UnitCost;
//	private final String LineDiscount;
//	private final String TaxRate;
//	private final String LineTax;
//	private final String LineTotalExcludingTax;
//	private final String LineTotal;
//	private final String IsService;
//	private final String CompositeParentSKU;
//	private final String CompositeParentOrderItemNumber;
	private final String note;
//	private final Double TotalWeight;
	
	public LinnworksOrderLine(HashMap<String, Integer> mappaColonne, String[] campi) {
		orderId = campi[mappaColonne.get("Order Id")];
//		referenceNumber = campi[mappaColonne.get("Reference number")];
//		externalReference = campi[mappaColonne.get("External reference")];
//		ChannelReference = campi[mappaColonne.get("Channel reference")];
		emailDestinazione = campi[mappaColonne.get("Customer email address")];
		
		//Cambiamento in produzione del 13/10/2017
		//telefonoDestinazione = campi[mappaColonne.get("Customer buyer phone number")];
		telefonoDestinazione = campi[mappaColonne.get("Buyer phone number")];
		
//		CustomerCompany = campi[mappaColonne.get("Customer company")];
		
		//Cambiamento in produzione del 13/10/2017
		//ragioneSocialeDestinazione = campi[mappaColonne.get("Shipping customer name")];
		//Cambiamento in produzione del 01/12/2017
		String customerName = campi[mappaColonne.get("Customer name")];
		String customerCompany = campi[mappaColonne.get("Customer company")];
		ragioneSocialeDestinazione = customerName.isEmpty() ? customerCompany : customerName;
		
		//Cambiamento in produzione del 13/10/2017
		//indirizzoDestinazione1 = campi[mappaColonne.get("Shipping address 1")];
		//indirizzoDestinazione2 = campi[mappaColonne.get("Shipping address 2")];
		//indirizzoDestinazione3 = campi[mappaColonne.get("Shipping address 3")];
		indirizzoDestinazione1 = campi[mappaColonne.get("Address 1")];
		indirizzoDestinazione2 = campi[mappaColonne.get("Address 2")];
		indirizzoDestinazione3 = campi[mappaColonne.get("Address 3")];
		
		//Cambiamento in produzione del 13/10/2017
		//localitaDestinazione = campi[mappaColonne.get("Shipping town")];
		//provinciaDestinazione = campi[mappaColonne.get("Shipping region")];
		//capDestinazione = campi[mappaColonne.get("Shipping postcode")];
		//nazioneDestinazione = campi[mappaColonne.get("Shipping country")];
		//iso2NazioneDestinazione = campi[mappaColonne.get("Shipping country code")];
		localitaDestinazione = campi[mappaColonne.get("Town")];
		provinciaDestinazione = campi[mappaColonne.get("Region")];
		capDestinazione = campi[mappaColonne.get("Postcode")];
		nazioneDestinazione = campi[mappaColonne.get("Country")];
		iso2NazioneDestinazione = campi[mappaColonne.get("Country code")];
		
		
//		BillingName = campi[mappaColonne.get("Billing name")];
//		BillingCompany = campi[mappaColonne.get("Billing company")];
//		BillingAddress1 = campi[mappaColonne.get("Billing address 1")];
//		BillingAddress2 = campi[mappaColonne.get("Billing address 2")];
//		BillingAddress3 = campi[mappaColonne.get("Billing address 3")];
//		BillingTown = campi[mappaColonne.get("Billing town")];
//		BillingRegion = campi[mappaColonne.get("Billing region")];
//		BillingPostcode = campi[mappaColonne.get("Billing postcode")];
//		BillingCountryName = campi[mappaColonne.get("Billing country name")];
//		BillingPhoneNumber = campi[mappaColonne.get("Billing phone number")];
		Date rDate = parseDate(campi[mappaColonne.get("Received date")]);
		if (rDate == null)
			rDate = new Date();
		ReceivedDate = new Date(rDate.getTime());
//		ShippingCost = campi[mappaColonne.get("Shipping cost")];
//		OrderTax = campi[mappaColonne.get("Order tax")];
		costoTotale = parseDouble(campi[mappaColonne.get("Order total")]);
//		Currency = campi[mappaColonne.get("Currency")];
//		Paid = campi[mappaColonne.get("Paid")];
//		Status = campi[mappaColonne.get("Status")];
//		Source = campi[mappaColonne.get("Source")];
//		SubSource = campi[mappaColonne.get("SubSource")];
//		DispatchBy = campi[mappaColonne.get("Dispatch By")];
//		CreatedDate = campi[mappaColonne.get("Created Date")];
//		ShippingServiceName = campi[mappaColonne.get("Shipping service name")];
//		ShippingServiceTag = campi[mappaColonne.get("Shipping service tag")];
//		ShippingServiceCode = campi[mappaColonne.get("Shipping service code")];
//		ShippingServiceVendor = campi[mappaColonne.get("Shipping service vendor")];
//		PackagingGroup = campi[mappaColonne.get("Packaging group")];
//		TrackingNumber = campi[mappaColonne.get("Tracking number")];
//		ChannelBuyerName = campi[mappaColonne.get("Channel buyer name")];
//		Marker = campi[mappaColonne.get("Marker")];
//		PaymentMethod = campi[mappaColonne.get("Payment method")];
//		OnHold = campi[mappaColonne.get("On hold")];
//		FulfillmentLocation = campi[mappaColonne.get("Fulfillment location")];
		sku = campi[mappaColonne.get("SKU")];
//		ItemTitle = campi[mappaColonne.get("Item title")];
//		OriginalTitle = campi[mappaColonne.get("Original title")];
//		ChannelSKU = campi[mappaColonne.get("Channel SKU")];
//		ItemNumber = campi[mappaColonne.get("Item number")];
		Integer q = parseInteger(campi[mappaColonne.get("Quantity")]);
		quantita = q != null ? q : 0;
//		UnitCost = campi[mappaColonne.get("Unit cost")];
//		LineDiscount = campi[mappaColonne.get("Line discount")];
//		TaxRate = campi[mappaColonne.get("Tax rate")];
//		LineTax = campi[mappaColonne.get("Line tax")];
//		LineTotalExcludingTax = campi[mappaColonne.get("Line total excluding tax")];
//		LineTotal = campi[mappaColonne.get("Line total")];
//		IsService = campi[mappaColonne.get("Is service")];
//		CompositeParentSKU = campi[mappaColonne.get("Composite parent SKU")];
//		CompositeParentOrderItemNumber = campi[mappaColonne.get("Composite parent order item number")];
		note = campi[mappaColonne.get("Order Notes")];
//		TotalWeight = parseDouble(campi[mappaColonne.get("Total Weight")]);
	}
	
	protected Integer parseInteger(String value) {
		Integer i = null;
		if (value != null) {
			value = value.trim();
			if (!value.isEmpty())
			try {
				i = Integer.parseInt(value);
			} catch (NumberFormatException e) {}
		}
		return i;
	}
	
	protected Double parseDouble(String value) {
		Double d = null;
		if (value != null) {
			value = value.trim();
			if (!value.isEmpty())
				try {
					d = Double.parseDouble(value);
				} catch (NumberFormatException e) {}
		}
		return d;
	}
	
	protected Date parseDate(String value) {
		Date d = null;
		try {
			d = sdf.parse(value);
		} catch (ParseException e) {}
		return d;
	}

	@Override
	public String toString() {
		return "LinnworksOrder [orderId=" + orderId + ", ShippingCustomerName=" + ragioneSocialeDestinazione
				+ ", ReceivedDate=" + ReceivedDate + ", SKU=" + sku + ", Quantity=" + quantita + "]";
	}

	public String getOrderId() {
		return orderId;
	}

	public Date getReceivedDate() {
		return ReceivedDate;
	}

	public String getNote() {
		return note;
	}

	public Double getCostoTotale() {
		return costoTotale;
	}

	public String getEmailDestinazione() {
		return emailDestinazione;
	}

	public String getTelefonoDestinazione() {
		return telefonoDestinazione;
	}

	public String getRagioneSocialeDestinazione() {
		return ragioneSocialeDestinazione;
	}

	public String getIndirizzoDestinazione1() {
		return indirizzoDestinazione1;
	}

	public String getIndirizzoDestinazione2() {
		return indirizzoDestinazione2;
	}

	public String getIndirizzoDestinazione3() {
		return indirizzoDestinazione3;
	}

	public String getLocalitaDestinazione() {
		return localitaDestinazione;
	}

	public String getProvinciaDestinazione() {
		return provinciaDestinazione;
	}

	public String getCapDestinazione() {
		return capDestinazione;
	}

	public String getNazioneDestinazione() {
		return nazioneDestinazione;
	}

	public String getIso2NazioneDestinazione() {
		return iso2NazioneDestinazione;
	}

	public String getSku() {
		return sku;
	}

	public Integer getQuantita() {
		return quantita;
	}

}
