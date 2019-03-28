package it.ltc.clienti.ynap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="TestataOrdini")
@NamedQuery(name="TestataOrdini.progressivo", query="SELECT MAX(t.numeroListaNumerico) FROM Ordine t")
public class Ordine implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
//	private static int progressivo = -1;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdTestaSped", unique=true, nullable=false)
	private int id;
	
	@Column(name="NrLista", length=21, columnDefinition="varchar(21)")
	private String numeroLista;
	
	@Column(name="NrListaArrivato")
	private Integer numeroListaNumerico;
	
	@Column(name="RifOrdineCli", length=20, columnDefinition="varchar(20)")
	private String riferimentoOrdineCliente;
	
	@Column(name="NrOrdine", length=20, columnDefinition="varchar(20)")
	private String numeroOrdine;
	
	@Column(name="Note", length=100, columnDefinition="varchar(100)")
	private String note;
	
	@Column(name="DataOrdine", columnDefinition="datetime")
	private Date dataOrdine;
	
	@Column(name="CodCliente", length=20, columnDefinition="varchar(20)")
	private String codiceCliente;
	
	@Column(name="Stato", length=4, columnDefinition="varchar(4)")
	private String stato;
	
	@Column(name="QtaTotaleSpedire")
	private Integer quantitàTotale;
	
	@Column(name="Corriere", length=30, columnDefinition="varchar(30)")
	private String corriere;
	
	@Column(name="CodCorriere", length=10, columnDefinition="varchar(10)")
	private String codiceCorriere;
	
	@Column(name="NomeFileArrivo", length=50, columnDefinition="varchar(50)")
	private String nomeFile;
	
	@Column(name="IdDestina")
	private Integer idDestinatario;
	
	@Column(name="DataConsegna", columnDefinition="datetime")
	private Date dataConsegna;
	
	@Column(name="Ragstampe", length=20, columnDefinition="varchar(20)")
	private String raggruppamentoOrdini;
	
	@Column(name="Priorita")
	private Integer priorità;
	
	@Column(name="TipoOrdineCliente")
	private Integer tipoOrdine;
	
	@Column(name="DataSpedizione", columnDefinition="datetime")
	private Date dataSpedizione;
	
	public Ordine() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumeroLista() {
		return numeroLista;
	}

	public void setNumeroLista(String numeroLista) {
		this.numeroLista = numeroLista;
	}

	public Integer getNumeroListaNumerico() {
		return numeroListaNumerico;
	}

	public void setNumeroListaNumerico(Integer numeroListaNumerico) {
		this.numeroListaNumerico = numeroListaNumerico;
	}

	public String getRiferimentoOrdineCliente() {
		return riferimentoOrdineCliente;
	}

	public void setRiferimentoOrdineCliente(String riferimentoOrdineCliente) {
		this.riferimentoOrdineCliente = riferimentoOrdineCliente;
	}

	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	public void setNumeroOrdine(String numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public String getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(String codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Integer getQuantitàTotale() {
		return quantitàTotale;
	}

	public void setQuantitàTotale(Integer quantitàTotale) {
		this.quantitàTotale = quantitàTotale;
	}

	public String getCorriere() {
		return corriere;
	}

	public void setCorriere(String corriere) {
		this.corriere = corriere;
	}

	public String getCodiceCorriere() {
		return codiceCorriere;
	}

	public void setCodiceCorriere(String codiceCorriere) {
		this.codiceCorriere = codiceCorriere;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public Integer getIdDestinatario() {
		return idDestinatario;
	}

	public void setIdDestinatario(Integer idDestinatario) {
		this.idDestinatario = idDestinatario;
	}

	public Date getDataConsegna() {
		return dataConsegna;
	}

	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	public String getRaggruppamentoOrdini() {
		return raggruppamentoOrdini;
	}

	public void setRaggruppamentoOrdini(String raggruppamentoOrdini) {
		this.raggruppamentoOrdini = raggruppamentoOrdini;
	}

	public Integer getPriorità() {
		return priorità;
	}

	public void setPriorità(Integer priorità) {
		this.priorità = priorità;
	}

	public Integer getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(Integer tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}

	public Date getDataSpedizione() {
		return dataSpedizione;
	}

	public void setDataSpedizione(Date dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

//	public static int getProgressivoNumeroListaArrivato() {
//		if (progressivo == -1) {
//			EntityManager<Ordine> managerOrdini = new EntityManager<Ordine>(Ordine.class, SQLServerFactory.getDBMS(Database.YNAP));
//			List<Ordine> listaOrdini = managerOrdini.getEntities();
//			progressivo = 1;
//			for (Ordine ordine : listaOrdini) {
//				if (progressivo < ordine.getNumeroListaNumerico())
//					progressivo = ordine.getNumeroListaNumerico();
//			}
//			progressivo += 1;
//		} else {
//			progressivo += 1;
//		}
//		return progressivo;
//	}

}
