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
@Table(name="MagaMov")
public class MovimentoMagazzino implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdMagaMov", unique=true, nullable=false)
	private int id;
	
	@Column(name="Causale", nullable=false, length=3, columnDefinition="char(3)")
	private String causale;
	
	@Column(name="DataMovMag", columnDefinition="datetime")
	private Date dataMovimento;
	
	@Column(name="OraMovMag")
	private Integer oraMovimento;
	
	@Column(name="IdUniArticolo", nullable=false, length=25, columnDefinition="varchar(25)")
	private String idUnivocoArticolo;
	
	@Column(name="DocData", columnDefinition="datetime")
	private Date dataOrdine;
	
	@Column(name="DocCat", length=1, columnDefinition="char(1)")
	private String categoriaDocumento;
	
	@Column(name="DocTipo", length=3, columnDefinition="char(3)")
	private String tipoDocumento;
	
	@Column(name="DocNr", length=40, columnDefinition="varchar(40)")
	private String numeroLista;
	
	@Column(name="DocNote", length=40, columnDefinition="varchar(40)")
	private String noteDocumento;
	
	@Column(name="Segno", length=1, columnDefinition="char(1)")
	private String segno;
	
	@Column(name="Tipo", length=1, columnDefinition="char(1)")
	private String tipo;
	
	@Column(name="Utente", length=20, columnDefinition="varchar(20)")
	private String utente;
	
	@Column(name="KeyCollo", length=20, columnDefinition="varchar(20)")
	private String collo;
	
	@Column(name="SegnoEsi", length=1, columnDefinition="char(1)")
	private String segnoEsistenza;
	
	@Column(name="SegnoImp", length=1, columnDefinition="char(1)")
	private String segnoImpegnato;
	
	@Column(name="SegnoDis", length=1, columnDefinition="char(1)")
	private String segnoDisponibile;
	
	@Column(name="IncTotali", length=2, columnDefinition="char(2)")
	private String totali;
	
	@Column(name="Cancellato", nullable=false, length=2, columnDefinition="char(2)")
	private String cancellato;
	
	@Column(name="Esistenzamov", nullable=false)
	private int esistenza;
	
	@Column(name="disponibilemov", nullable=false)
	private int disponibile;
	
	@Column(name="impegnatomov", nullable=false)
	private int impegnato;
	
	@Column(name="trasmesso", nullable=false, length=2, columnDefinition="char(2)")
	private String trasmesso;

	public MovimentoMagazzino() {}

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
