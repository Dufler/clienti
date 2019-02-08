package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ArtiBar")
public class BarcodeOggetto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdArtiBar", unique=true, nullable=false)
	private int id;
	
	@Column(name="IdUniArticolo", length=15, columnDefinition="char(15)")
	private String idUnivocoArticolo;
	
	@Column(name="BarraEAN", length=50, columnDefinition="varchar(50)")
	private String barcodeEAN;
	
	@Column(name="BarraUPC", length=50, columnDefinition="varchar(50)")
	private String barcodeUPC;
	
	@Column(name="CodiceArticolo", length=50, columnDefinition="varchar(50)")
	private String codiceArticolo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdUnivocoArticolo() {
		return idUnivocoArticolo;
	}

	public void setIdUnivocoArticolo(String idUnivocoArticolo) {
		this.idUnivocoArticolo = idUnivocoArticolo;
	}

	public String getBarcodeEAN() {
		return barcodeEAN;
	}

	public void setBarcodeEAN(String barcodeEAN) {
		this.barcodeEAN = barcodeEAN;
	}

	public String getBarcodeUPC() {
		return barcodeUPC;
	}

	public void setBarcodeUPC(String barcodeUPC) {
		this.barcodeUPC = barcodeUPC;
	}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

}
