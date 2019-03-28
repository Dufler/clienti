package it.ltc.clienti.forza.ftp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import it.ltc.clienti.forza.ConfigurationUtility;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.MagaMov;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.model.interfaces.ordine.MInfoSpedizione;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.ProdottoOrdinato;
import it.ltc.model.interfaces.ordine.TipoIDProdotto;
import it.ltc.model.persistence.ordine.ControllerOrdiniSQLServer;

public class ManagerOrdineScaduti extends ControllerOrdiniSQLServer {
	
	private static final Logger logger = Logger.getLogger(ManagerOrdineScaduti.class);
	
	private final ManagerAssegnazioneScaduti managerAssegnazione;

	public ManagerOrdineScaduti(String persistenceUnit) {
		super(persistenceUnit);
		managerAssegnazione = new ManagerAssegnazioneScaduti(persistenceUnit);
	}
	
	public MOrdine creaOrdineDaProdotti(List<ColliPack> prodotti) {
		//Creo la testata
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String riferimento = "Scaduti " + sdf.format(now);
		MOrdine ordine = new MOrdine();
		ordine.setDataConsegna(now);
		ordine.setDataDocumento(now);
		ordine.setDataOrdine(now);
		ordine.setDestinatario(ConfigurationUtility.getInstance().getMittente());
		ordine.setMittente(ConfigurationUtility.getInstance().getMittente());
		ordine.setRiferimentoDocumento(riferimento);
		ordine.setRiferimentoOrdine(riferimento);
		
		ordine.setStato("IMPO");
		ordine.setTipo("SCADUTI");
		ordine.setTipoDocumento("DDT");
		ordine.setTipoIdentificazioneProdotti(TipoIDProdotto.CHIAVE);
		ordine.setNote("Prelievo merce scaduta.");
		
		//Info spedizione
		MInfoSpedizione infoSpedizione = new MInfoSpedizione();
		infoSpedizione.setCodiceCorriere("GLS");
		infoSpedizione.setCorriere("GLS");
		infoSpedizione.setServizioCorriere("DEF");
		ordine.setInfoSpedizione(infoSpedizione);
		
		//Inserisco le righe
		int numeroRiga = 1;
		for (ColliPack prodotto : prodotti) {
			ProdottoOrdinato riga = new ProdottoOrdinato();
			riga.setChiave(prodotto.getCodArtStr());
			riga.setMagazzinoCliente(prodotto.getMagazzino());
			riga.setMagazzinoLTC(prodotto.getMagazzino());
			riga.setNote("Scaduto");
			riga.setNumeroRiga(numeroRiga++);
			riga.setQuantita(prodotto.getQta() - prodotto.getQtaimpegnata());
			riga.setRiferimentoCliente(riferimento);
			ordine.aggiungiProdotto(riga);
		}
		//Vado in inserimento e tento di assegnarlo
		try {
			valida(ordine);
			inserisci(ordine);
			assegna(ordine, prodotti);
		} catch (Exception e) {
			ordine = null;
			logger.error(e.getMessage(), e);
		}
		return ordine;
	}
	
	@Override
	public boolean inserisciInformazioniAggiuntiveMovimenti(MOrdine ordine, MagaMov movimento) {
		movimento.setCausale("IOS");
		movimento.setTipo("IP");
		movimento.setUtente("SERVIZIO SCADENZE");
		movimento.setDocCat("S");
		movimento.setTipo("OR");
		movimento.setDocNote("Impegnato perchè scaduto.");
		return true;
	}
	
	private void assegna(MOrdine ordine, List<ColliPack> prodotti) {
		TestataOrdini testata = daoOrdini.trovaDaRiferimento(ordine.getRiferimentoOrdine());
		List<RighiOrdine> righe = daoRigheOrdine.trovaRigheDaAssegnare(testata.getIdTestaSped());
		managerAssegnazione.assegnaScaduti(testata, righe, prodotti);
	}

}
