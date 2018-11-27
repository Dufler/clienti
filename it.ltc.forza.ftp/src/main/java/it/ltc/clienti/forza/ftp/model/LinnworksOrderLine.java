package it.ltc.clienti.forza.ftp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.ltc.utility.csv.FileCSV;

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
	
	public LinnworksOrderLine(FileCSV csv, String[] campi) {
		orderId = campi[csv.getIndiceColonna("Order Id")];
//		referenceNumber = campi[csv.getIndiceColonna("Reference number")];
//		externalReference = campi[csv.getIndiceColonna("External reference")];
//		ChannelReference = campi[csv.getIndiceColonna("Channel reference")];
		emailDestinazione = campi[csv.getIndiceColonna("Customer email address")];
		
		//Cambiamento in produzione del 13/10/2017
		//telefonoDestinazione = campi[csv.getIndiceColonna("Customer buyer phone number")];
		telefonoDestinazione = campi[csv.getIndiceColonna("Buyer phone number")];
		
//		CustomerCompany = campi[csv.getIndiceColonna("Customer company")];
		
		//Cambiamento in produzione del 13/10/2017
		//ragioneSocialeDestinazione = campi[csv.getIndiceColonna("Shipping customer name")];
		//Cambiamento in produzione del 01/12/2017
		String customerName = campi[csv.getIndiceColonna("Customer name")];
		String customerCompany = campi[csv.getIndiceColonna("Customer company")];
		ragioneSocialeDestinazione = customerName.isEmpty() ? customerCompany : customerName;
		
		//Cambiamento in produzione del 13/10/2017
		//indirizzoDestinazione1 = campi[csv.getIndiceColonna("Shipping address 1")];
		//indirizzoDestinazione2 = campi[csv.getIndiceColonna("Shipping address 2")];
		//indirizzoDestinazione3 = campi[csv.getIndiceColonna("Shipping address 3")];
		indirizzoDestinazione1 = campi[csv.getIndiceColonna("Address 1")];
		indirizzoDestinazione2 = campi[csv.getIndiceColonna("Address 2")];
		indirizzoDestinazione3 = campi[csv.getIndiceColonna("Address 3")];
		
		//Cambiamento in produzione del 13/10/2017
		//localitaDestinazione = campi[csv.getIndiceColonna("Shipping town")];
		//provinciaDestinazione = campi[csv.getIndiceColonna("Shipping region")];
		//capDestinazione = campi[csv.getIndiceColonna("Shipping postcode")];
		//nazioneDestinazione = campi[csv.getIndiceColonna("Shipping country")];
		//iso2NazioneDestinazione = campi[csv.getIndiceColonna("Shipping country code")];
		localitaDestinazione = campi[csv.getIndiceColonna("Town")];
		provinciaDestinazione = campi[csv.getIndiceColonna("Region")];
		capDestinazione = campi[csv.getIndiceColonna("Postcode")];
		nazioneDestinazione = campi[csv.getIndiceColonna("Country")];
		iso2NazioneDestinazione = campi[csv.getIndiceColonna("Country code")];
		
		
//		BillingName = campi[csv.getIndiceColonna("Billing name")];
//		BillingCompany = campi[csv.getIndiceColonna("Billing company")];
//		BillingAddress1 = campi[csv.getIndiceColonna("Billing address 1")];
//		BillingAddress2 = campi[csv.getIndiceColonna("Billing address 2")];
//		BillingAddress3 = campi[csv.getIndiceColonna("Billing address 3")];
//		BillingTown = campi[csv.getIndiceColonna("Billing town")];
//		BillingRegion = campi[csv.getIndiceColonna("Billing region")];
//		BillingPostcode = campi[csv.getIndiceColonna("Billing postcode")];
//		BillingCountryName = campi[csv.getIndiceColonna("Billing country name")];
//		BillingPhoneNumber = campi[csv.getIndiceColonna("Billing phone number")];
		Date rDate = parseDate(campi[csv.getIndiceColonna("Received date")]);
		if (rDate == null)
			rDate = new Date();
		ReceivedDate = new Date(rDate.getTime());
//		ShippingCost = campi[csv.getIndiceColonna("Shipping cost")];
//		OrderTax = campi[csv.getIndiceColonna("Order tax")];
		costoTotale = parseDouble(campi[csv.getIndiceColonna("Order total")]);
//		Currency = campi[csv.getIndiceColonna("Currency")];
//		Paid = campi[csv.getIndiceColonna("Paid")];
//		Status = campi[csv.getIndiceColonna("Status")];
//		Source = campi[csv.getIndiceColonna("Source")];
//		SubSource = campi[csv.getIndiceColonna("SubSource")];
//		DispatchBy = campi[csv.getIndiceColonna("Dispatch By")];
//		CreatedDate = campi[csv.getIndiceColonna("Created Date")];
//		ShippingServiceName = campi[csv.getIndiceColonna("Shipping service name")];
//		ShippingServiceTag = campi[csv.getIndiceColonna("Shipping service tag")];
//		ShippingServiceCode = campi[csv.getIndiceColonna("Shipping service code")];
//		ShippingServiceVendor = campi[csv.getIndiceColonna("Shipping service vendor")];
//		PackagingGroup = campi[csv.getIndiceColonna("Packaging group")];
//		TrackingNumber = campi[csv.getIndiceColonna("Tracking number")];
//		ChannelBuyerName = campi[csv.getIndiceColonna("Channel buyer name")];
//		Marker = campi[csv.getIndiceColonna("Marker")];
//		PaymentMethod = campi[csv.getIndiceColonna("Payment method")];
//		OnHold = campi[csv.getIndiceColonna("On hold")];
//		FulfillmentLocation = campi[csv.getIndiceColonna("Fulfillment location")];
		sku = campi[csv.getIndiceColonna("SKU")];
//		ItemTitle = campi[csv.getIndiceColonna("Item title")];
//		OriginalTitle = campi[csv.getIndiceColonna("Original title")];
//		ChannelSKU = campi[csv.getIndiceColonna("Channel SKU")];
//		ItemNumber = campi[csv.getIndiceColonna("Item number")];
		Integer q = parseInteger(campi[csv.getIndiceColonna("Quantity")]);
		quantita = q != null ? q : 0;
//		UnitCost = campi[csv.getIndiceColonna("Unit cost")];
//		LineDiscount = campi[csv.getIndiceColonna("Line discount")];
//		TaxRate = campi[csv.getIndiceColonna("Tax rate")];
//		LineTax = campi[csv.getIndiceColonna("Line tax")];
//		LineTotalExcludingTax = campi[csv.getIndiceColonna("Line total excluding tax")];
//		LineTotal = campi[csv.getIndiceColonna("Line total")];
//		IsService = campi[csv.getIndiceColonna("Is service")];
//		CompositeParentSKU = campi[csv.getIndiceColonna("Composite parent SKU")];
//		CompositeParentOrderItemNumber = campi[csv.getIndiceColonna("Composite parent order item number")];
		note = campi[csv.getIndiceColonna("Order Notes")];
//		TotalWeight = parseDouble(campi[csv.getIndiceColonna("Total Weight")]);
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
