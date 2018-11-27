package it.ltc.clienti.forza.ftp.model;

import java.util.HashSet;
import java.util.Set;

import it.ltc.database.model.legacy.ColliCarico;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.Righiubicpre;
import it.ltc.database.model.legacy.Scorte;
import it.ltc.database.model.legacy.Scorte2;
import it.ltc.database.model.legacy.ScorteColli;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.Ubicazioni;

/**
 * Rappresenta il risultato di una assegnazione di una riga d'ordine.<br>
 * Contiene al suo interno la riga originale, la lista dei ColliPack, ColliCarico e Ubicazioni dove trovare i pezzi necessari.<br>
 * Nel caso in cui il prodotto richiesto sia a scorta allora vengono aggiunti degli oggetti Scorte e ScorteColli, inoltre se il prodotto c'è ma non è stato ubicato vengono aggiunti anche degli oggetti Scorta2.
 * @author Damiano
 *
 */
public class AssegnazioneProdotto {
	
	/**
	 * Indica che tipo di assegnazione è stata fatta in questo caso.
	 * @author Damiano
	 *
	 */
	public enum TipoAssegnazione { NORMALE, SCORTE, NON_UBICATA }
	
	private final TestataOrdini testata;
	private final RighiOrdine riga;
	
	private final Set<Righiubicpre> righeUbicazioni;
	private final Set<ColliPack> prodotti;
	private final Set<ColliCarico> colli;
	private final Set<Ubicazioni> ubicazioni;
	
	private final Set<Scorte> scorteUbicate;
	private final Set<Scorte2> scorteNonUbicate;
	private final Set<ScorteColli> colliConScorte;
	
	private TipoAssegnazione tipo;
	
	public AssegnazioneProdotto(TestataOrdini testata, RighiOrdine riga) {
		this.testata = testata;
		this.riga = riga;
		this.righeUbicazioni = new HashSet<>();
		this.prodotti = new HashSet<>();
		this.colli = new HashSet<>();
		this.ubicazioni = new HashSet<>();
		this.scorteUbicate = new HashSet<>();
		this.scorteNonUbicate = new HashSet<>();
		this.colliConScorte = new HashSet<>();
	}

	public TestataOrdini getTestata() {
		return testata;
	}

	public RighiOrdine getRiga() {
		return riga;
	}
	
	public void aggiungiRigaUbicazione(Righiubicpre riga) {
		righeUbicazioni.add(riga);
	}

	public Set<Righiubicpre> getRigheUbicazione() {
		return righeUbicazioni;
	}
	
	public void aggiungiProdotto(ColliPack prodotto) {
		prodotti.add(prodotto);
	}

	public Set<ColliPack> getProdotti() {
		return prodotti;
	}
	
	public void aggiungiColli(ColliCarico collo) {
		colli.add(collo);
	}

	public Set<ColliCarico> getColli() {
		return colli;
	}
	
	public void aggiungiUbicazione(Ubicazioni ubicazione) {
		ubicazioni.add(ubicazione);
	}

	public Set<Ubicazioni> getUbicazioni() {
		return ubicazioni;
	}
	
	public void aggiungiScortaUbicata(Scorte scorta) {
		scorteUbicate.add(scorta);
	}

	public Set<Scorte> getScorteUbicate() {
		return scorteUbicate;
	}
	
	public void aggiungiScorteNonUbicate(Scorte2 scorte) {
		scorteNonUbicate.add(scorte);
	}

	public Set<Scorte2> getScorteNonUbicate() {
		return scorteNonUbicate;
	}
	
	public void aggiungiColloConScorte(ScorteColli collo) {
		colliConScorte.add(collo);
	}

	public Set<ScorteColli> getColliConScorte() {
		return colliConScorte;
	}

	public TipoAssegnazione getTipo() {
		return tipo;
	}

	public void setTipo(TipoAssegnazione tipo) {
		this.tipo = tipo;
	}

}
