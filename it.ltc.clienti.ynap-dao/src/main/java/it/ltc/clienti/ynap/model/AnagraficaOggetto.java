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
	
	private String idUnivocoArticolo;
	private String descrizione;
	private String stagione;
	private String tipologiaMerce;
	private String codiceArticolo;
	private String modello;
	private String taglia;
	private String colore;
	private String descrizioneAggiuntiva;
	private Integer marchio;

//	public ColumnList getVariabili() {
//		if (variabili.isEmpty()) {
//			variabili.addAutoincrement("IdArticolo", "id");
//			variabili.addString("IdUniArticolo", "idUnivocoArticolo");
//			variabili.addString("Descrizione", "descrizione");
//			variabili.addString("Stagione", "stagione");
//			variabili.addString("CatMercGruppo", "tipologiaMerce");
//			variabili.addString("CodArtStr", "codiceArticolo");
//			variabili.addString("Modello", "modello");
//			variabili.addString("Taglia", "taglia");
//			variabili.addString("Colore", "colore");
//			variabili.addString("DescAggiuntiva", "descrizioneAggiuntiva");
//			variabili.addInt("Marchio", "marchio");
//		}
//		return variabili;
//	}
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
