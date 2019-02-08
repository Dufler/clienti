package it.ltc.clienti.ynap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
//@Table(name="MagaMov")
public class MovimentoMagazzino implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdMagaMov", unique=true, nullable=false)
	private int id;
	
	private String causale;
	private Date dataMovimento;
	private int oraMovimento;
	private String idUnivocoArticolo;
	private Date dataOrdine;
	private String categoriaDocumento;
	private String tipoDocumento;
	private String numeroLista;
	private String noteDocumento;
	private String segno;
	private String tipo;
	private String utente;
	private String collo;
	private String segnoEsistenza;
	private String segnoImpegnato;
	private String segnoDisponibile;
	private String totali;
	private String cancellato;
	private int esistenza;
	private int disponibile;
	private int impegnato;
	private String trasmesso;

//	public ColumnList getVariabili() {
//		if (variabili.isEmpty()) {
//			variabili.addAutoincrement("IdMagaSd", "id");
//			variabili.addString("Causale", "causale");
//			variabili.addDate("DataMovMag", "dataMovimento");
//			variabili.addInt("OraMovMag", "oraMovimento");
//			variabili.addString("IdUniArticolo", "idUnivocoArticolo");
//			variabili.addDate("DocData", "dataOrdine");
//			variabili.addString("DocCat", "categoriaDocumento");
//			variabili.addString("DocTipo", "tipoDocumento");
//			variabili.addString("DocNr", "numeroLista");
//			variabili.addString("DocNote", "noteDocumento");
//			variabili.addString("Segno", "segno");
//			variabili.addString("Tipo", "tipo");
//			variabili.addString("Utente", "utente");
//			variabili.addString("KeyCollo", "collo");
//			variabili.addString("SegnoEsi", "segnoEsistenza");
//			variabili.addString("SegnoImp", "segnoImpegnato");
//			variabili.addString("SegnoDis", "segnoDisponibile");
//			variabili.addString("IncTotali", "totali");
//			variabili.addString("Cancellato", "cancellato");
//			variabili.addInt("Esistenzamov", "esistenza");
//			variabili.addInt("disponibilemov", "disponibile");
//			variabili.addInt("impegnatomov", "impegnato");
//			variabili.addString("trasmesso", "trasmesso");
//		}
//		return variabili;
//	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public int getOraMovimento() {
		return oraMovimento;
	}

	public void setOraMovimento(int oraMovimento) {
		this.oraMovimento = oraMovimento;
	}

	public String getIdUnivocoArticolo() {
		return idUnivocoArticolo;
	}

	public void setIdUnivocoArticolo(String idUnivocoArticolo) {
		this.idUnivocoArticolo = idUnivocoArticolo;
	}

	public Date getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public String getCategoriaDocumento() {
		return categoriaDocumento;
	}

	public void setCategoriaDocumento(String categoriaDocumento) {
		this.categoriaDocumento = categoriaDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumeroLista() {
		return numeroLista;
	}

	public void setNumeroLista(String numeroLista) {
		this.numeroLista = numeroLista;
	}

	public String getNoteDocumento() {
		return noteDocumento;
	}

	public void setNoteDocumento(String noteDocumento) {
		this.noteDocumento = noteDocumento;
	}

	public String getSegno() {
		return segno;
	}

	public void setSegno(String segno) {
		this.segno = segno;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getCollo() {
		return collo;
	}

	public void setCollo(String collo) {
		this.collo = collo;
	}

	public String getSegnoEsistenza() {
		return segnoEsistenza;
	}

	public void setSegnoEsistenza(String segnoEsistenza) {
		this.segnoEsistenza = segnoEsistenza;
	}

	public String getSegnoImpegnato() {
		return segnoImpegnato;
	}

	public void setSegnoImpegnato(String segnoImpegnato) {
		this.segnoImpegnato = segnoImpegnato;
	}

	public String getSegnoDisponibile() {
		return segnoDisponibile;
	}

	public void setSegnoDisponibile(String segnoDisponibile) {
		this.segnoDisponibile = segnoDisponibile;
	}

	public String getTotali() {
		return totali;
	}

	public void setTotali(String totali) {
		this.totali = totali;
	}

	public String getCancellato() {
		return cancellato;
	}

	public void setCancellato(String cancellato) {
		this.cancellato = cancellato;
	}

	public int getEsistenza() {
		return esistenza;
	}

	public void setEsistenza(int esistenza) {
		this.esistenza = esistenza;
	}

	public int getDisponibile() {
		return disponibile;
	}

	public void setDisponibile(int disponibile) {
		this.disponibile = disponibile;
	}

	public int getImpegnato() {
		return impegnato;
	}

	public void setImpegnato(int impegnato) {
		this.impegnato = impegnato;
	}

	public String getTrasmesso() {
		return trasmesso;
	}

	public void setTrasmesso(String trasmesso) {
		this.trasmesso = trasmesso;
	}

}
