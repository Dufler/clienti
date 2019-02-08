package it.ltc.clienti.redone.model;

import java.util.Date;

import it.ltc.utility.csv.annotation.CampoCSV;

/**
 * Rappresenta il flusso di ritorno del carico.
 * @author Damiano
 *
 */
public class InboundCount {
	
	@CampoCSV(name="identifier", position = 1)
	private String identifier;
	
	@CampoCSV(name="reference", position = 2)
	private String reference;
	
	@CampoCSV(name="arrivalDate", position = 3)
	private Date arrivalDate;
	
	@CampoCSV(name="row", position = 4)
	private int row;
	
	@CampoCSV(name="product", position = 5)
	private String product;
	
	@CampoCSV(name="expectedquantity", position = 6)
	private int expectedquantity;
	
	@CampoCSV(name="realquantity", position = 7)
	private int realquantity;
	
	@CampoCSV(name="warehouse", position = 8)
	private String warehouse;
	
	@CampoCSV(name="serialnumbers", position = 9)
	private String serialnumbers;
	
	public InboundCount() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + row;
		result = prime * result + ((warehouse == null) ? 0 : warehouse.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InboundCount other = (InboundCount) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (row != other.row)
			return false;
		if (warehouse == null) {
			if (other.warehouse != null)
				return false;
		} else if (!warehouse.equals(other.warehouse))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InboundCount [identifier=" + identifier + ", reference=" + reference + ", arrivalDate=" + arrivalDate
				+ ", row=" + row + ", product=" + product + ", expectedquantity=" + expectedquantity + ", realquantity="
				+ realquantity + ", warehouse=" + warehouse + ", serialnumbers=" + serialnumbers + "]";
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getExpectedquantity() {
		return expectedquantity;
	}

	public void setExpectedquantity(int expectedquantity) {
		this.expectedquantity = expectedquantity;
	}

	public int getRealquantity() {
		return realquantity;
	}

	public void setRealquantity(int realquantity) {
		this.realquantity = realquantity;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getSerialnumbers() {
		return serialnumbers;
	}

	public void setSerialnumbers(String serialnumbers) {
		this.serialnumbers = serialnumbers;
	}

}
