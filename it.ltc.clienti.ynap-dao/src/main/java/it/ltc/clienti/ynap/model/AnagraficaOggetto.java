package it.ltc.clienti.ynap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Articoli")
public class AnagraficaOggetto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdArticolo", unique=true, nullable=false)
	private int id;
	
	@Column(name="IdUniArticolo", nullable=false, length=25, columnDefinition="varchar(25)")
	private String idUnivocoArticolo;
	
	@Column(name="Descrizione", length=500, columnDefinition="varchar(500)")
	private String descrizione;
	
	@Column(name="Stagione", length=30, columnDefinition="varchar(30)")
	private String stagione;
	
	@Column(name="CatMercGruppo", nullable=false, length=50, columnDefinition="varchar(50)")
	private String tipologiaMerce;
	
	@Column(name="CodArtStr", length=50, columnDefinition="varchar(50)")
	private String codiceArticolo;
	
	@Column(name="Modello", length=40, columnDefinition="varchar(40)")
	private String modello;
	
	@Column(name="Taglia", length=20, columnDefinition="varchar(20)")
	private String taglia;
	
	@Column(name="Colore", length=40, columnDefinition="varchar(40)")
	private String colore;
	
	@Column(name="DescAggiuntiva", columnDefinition="text")
	private String descrizioneAggiuntiva;
	
	@Column(name="Marchio")
	private Integer marchio;

//	
//	public static String getNextIDUnivoArticolo() {
//		if (progressivo == -1) {
//			EntityManager<AnagraficaOggetto> managerAnagrafica = new EntityManager<AnagraficaOggetto>(AnagraficaOggetto.class, SQLServerFactory.getDBMS(Database.YNAP));
//			List<AnagraficaOggetto> articoliOrdinati = managerAnagrafica.executeQuery("SELECT TOP(1) * FROM dbo.Articoli ORDER BY IdArticolo DESC");
//			if (articoliOrdinati.isEmpty())
//				progressivo = 1;
//			else
//				progressivo = articoliOrdinati.get(0).getId() + 1;
//		} else {
//			progressivo++;
//		}
//		StringUtility myUtility = new StringUtility();
//		return "ID0099" + myUtility.getFormattedString(progressivo, 9);
//	}

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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getStagione() {
		return stagione;
	}

	public void setStagione(String stagione) {
		this.stagione = stagione;
	}

	public String getTipologiaMerce() {
		return tipologiaMerce;
	}

	public void setTipologiaMerce(String tipologiaMerce) {
		this.tipologiaMerce = tipologiaMerce;
	}

	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getTaglia() {
		return taglia;
	}

	public void setTaglia(String taglia) {
		this.taglia = taglia;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public String getDescrizioneAggiuntiva() {
		return descrizioneAggiuntiva;
	}

	public void setDescrizioneAggiuntiva(String descrizioneAggiuntiva) {
		this.descrizioneAggiuntiva = descrizioneAggiuntiva;
	}

	public Integer getMarchio() {
		return marchio;
	}

	public void setMarchio(Integer marchio) {
		this.marchio = marchio;
	}

}
