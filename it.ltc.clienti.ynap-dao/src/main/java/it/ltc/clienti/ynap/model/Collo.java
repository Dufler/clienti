package it.ltc.clienti.ynap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ColliCarico")
public class Collo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdCollo", unique=true, nullable=false)
	private int id;
	
	@Column(name="NrCollo")
	private int progressivoNumeroCollo;
	
	@Column(name="KeyColloCar", length=9, columnDefinition="char(9)")
	private String numeroCollo;
	
	@Column(name="Barcode", length=12, columnDefinition="varchar(12)")
	private String barcodeCollo;
	
	@Column(name="Anno")
	private int anno;
	
	@Column(name="IdDocumento")
	private int idPackingList;
	
	@Column(name="DataCrea", insertable=false, columnDefinition="datetime")
	private Date dataLettura;
	
	@Column(name="BarcodeCliente", nullable=false, length=50, columnDefinition="varchar(50)")
	private String barcodeCliente;
	
	@Column(name="FonteLettura", length=50, columnDefinition="varchar(50)")
	private String fonteLettura;
	
	@Column(name="Id_Box", length=50, columnDefinition="varchar(50)")
	private String idBox;
	
	@Column(name="Cancellato", length=2, columnDefinition="char(2)")
	private String cancellato;
	
	@Column(name="Note", length=100, columnDefinition="varchar(100)")
	private String note;
	
	@Column(name="Distrutto", length=2, columnDefinition="char(2)")
	private String distrutto;
	
	@Column(name="UteDistruzione", length=20, columnDefinition="varchar(20)")
	private String utenteDistruttore;
	
	@Column(name="DataDistruzione", columnDefinition="datetime")
	private Date dataDistruzione;
	
	public Collo() {}
	
	public boolean equals(Object obj) {
		boolean uguali = false;
		if (obj instanceof Collo) {
			Collo collo = (Collo) obj;
			String barcode = collo.getBarcodeCliente();
			if (barcodeCliente != null && barcodeCliente.equals(barcode))
				uguali = true;
		}
		return uguali;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProgressivoNumeroCollo() {
		return progressivoNumeroCollo;
	}

	public void setProgressivoNumeroCollo(int progressivoNumeroCollo) {
		this.progressivoNumeroCollo = progressivoNumeroCollo;
	}

	public String getNumeroCollo() {
		return numeroCollo;
	}

	public void setNumeroCollo(String numeroCollo) {
		this.numeroCollo = numeroCollo;
	}

	public String getBarcodeCollo() {
		return barcodeCollo;
	}

	public void setBarcodeCollo(String barcodeCollo) {
		this.barcodeCollo = barcodeCollo;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public int getIdPackingList() {
		return idPackingList;
	}

	public void setIdPackingList(int idPackingList) {
		this.idPackingList = idPackingList;
	}

	public Date getDataLettura() {
		return dataLettura;
	}

	public void setDataLettura(Date dataLettura) {
		this.dataLettura = dataLettura;
	}

	public String getBarcodeCliente() {
		return barcodeCliente;
	}

	public void setBarcodeCliente(String barcodeCliente) {
		this.barcodeCliente = barcodeCliente;
	}
	
	public String getFonteLettura() {
		return fonteLettura;
	}

	public void setFonteLettura(String fonteLettura) {
		this.fonteLettura = fonteLettura;
	}

	public String getIdBox() {
		return idBox;
	}

	public void setIdBox(String idBox) {
		this.idBox = idBox;
	}

	public String getCancellato() {
		return cancellato;
	}

	public void setCancellato(String cancellato) {
		this.cancellato = cancellato;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDistrutto() {
		return distrutto;
	}

	public void setDistrutto(String distrutto) {
		this.distrutto = distrutto;
	}

	public String getUtenteDistruttore() {
		return utenteDistruttore;
	}

	public void setUtenteDistruttore(String utenteDistruttore) {
		this.utenteDistruttore = utenteDistruttore;
	}

	public Date getDataDistruzione() {
		return dataDistruzione;
	}

	public void setDataDistruzione(Date dataDistruzione) {
		this.dataDistruzione = dataDistruzione;
	}

}