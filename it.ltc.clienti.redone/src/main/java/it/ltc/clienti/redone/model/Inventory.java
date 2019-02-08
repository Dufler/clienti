package it.ltc.clienti.redone.model;

import it.ltc.utility.csv.annotation.CampoCSV;

public class Inventory {
	
	@CampoCSV(name = "product", position = 1)
	private String product;
	
	@CampoCSV(name = "warehouse", position = 2)
	private String warehouse;
	
	@CampoCSV(name = "available", position = 3)
	private int available;
	
	@CampoCSV(name = "existant", position = 4)
	private int existant;
	
	public Inventory() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		Inventory other = (Inventory) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
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
		return "Inventory [product=" + product + ", warehouse=" + warehouse + ", available=" + available + ", existant="
				+ existant + "]";
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public int getExistant() {
		return existant;
	}

	public void setExistant(int existant) {
		this.existant = existant;
	}

}
