package it.ltc.clienti.redone.model;

import it.ltc.utility.csv.annotation.CampoCSV;

public class PickPack {
	
	@CampoCSV(name = "identifier", position = 1)
	private String identifier;
	
	@CampoCSV(name = "reference", position = 2)
	private String reference;
	
	@CampoCSV(name = "parcel", position = 3)
	private String parcel;
	
	@CampoCSV(name = "parcelweight", position = 4, decimals=3)
	private double parcelWeight;
	
	@CampoCSV(name = "parcelheight", position = 5)
	private int parcelHeight;
	
	@CampoCSV(name = "parcelwidth", position = 6)
	private int parcelWidth;
	
	@CampoCSV(name = "parcelLength", position = 7)
	private int parcellength;
	
	@CampoCSV(name = "row", position = 8)
	private int row;
	
//	@CampoCSV(name = "quantityrequested", position = 9)
//	private int quantityRequested;
//	
//	@CampoCSV(name = "quantityPackaged", position = 10)
//	private int quantitypackaged;
	
	@CampoCSV(name = "quantity", position = 10)
	private int quantity;
	
	@CampoCSV(name = "product", position = 11)
	private String product;
	
	public PickPack() {}

	@Override
	public String toString() {
		return "PickPack [identifier=" + identifier + ", reference=" + reference + ", parcel=" + parcel
				+ ", parcelWeight=" + parcelWeight + ", parcelHeight=" + parcelHeight + ", parcelWidth=" + parcelWidth
				+ ", parcellength=" + parcellength + ", row=" + row + ", quantity=" + quantity
				+ ", product=" + product + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((parcel == null) ? 0 : parcel.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + row;
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
		PickPack other = (PickPack) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (parcel == null) {
			if (other.parcel != null)
				return false;
		} else if (!parcel.equals(other.parcel))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (row != other.row)
			return false;
		return true;
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

	public String getParcel() {
		return parcel;
	}

	public void setParcel(String parcel) {
		this.parcel = parcel;
	}

	public double getParcelWeight() {
		return parcelWeight;
	}

	public void setParcelWeight(double parcelWeight) {
		this.parcelWeight = parcelWeight;
	}

	public int getParcelHeight() {
		return parcelHeight;
	}

	public void setParcelHeight(int parcelHeight) {
		this.parcelHeight = parcelHeight;
	}

	public int getParcelWidth() {
		return parcelWidth;
	}

	public void setParcelWidth(int parcelWidth) {
		this.parcelWidth = parcelWidth;
	}

	public int getParcellength() {
		return parcellength;
	}

	public void setParcellength(int parcellength) {
		this.parcellength = parcellength;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

//	public int getQuantityRequested() {
//		return quantityRequested;
//	}
//
//	public void setQuantityRequested(int quantityRequested) {
//		this.quantityRequested = quantityRequested;
//	}
//
//	public int getQuantitypackaged() {
//		return quantitypackaged;
//	}
//
//	public void setQuantitypackaged(int quantitypackaged) {
//		this.quantitypackaged = quantitypackaged;
//	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

}
