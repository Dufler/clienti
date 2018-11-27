package it.ltc.clienti.date.ordini;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import it.ltc.clienti.date.ConfigurationUtility;
import it.ltc.database.dao.legacy.NazioniDao;
import it.ltc.database.model.legacy.Nazioni;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.model.interfaces.exception.ModelValidationException;
import it.ltc.model.interfaces.indirizzo.MIndirizzo;
import it.ltc.model.interfaces.ordine.MOrdine;
import it.ltc.model.interfaces.ordine.ProdottoOrdinato;
import it.ltc.model.interfaces.ordine.TipoIDProdotto;
import it.ltc.model.interfaces.ordine.TipoOrdine;
import it.ltc.model.persistence.ordine.ControllerOrdiniSQLServer;
import it.ltc.utility.miscellanea.string.ObjectParser;

public class ImportaOrdini extends ControllerOrdiniSQLServer {
	
	private static final Logger logger = Logger.getLogger(ImportaOrdini.class);
	
	private final String folderPath;
	private final String regexNomeFileOrdini;
	
	private final NazioniDao daoNazioni;

	public ImportaOrdini(String persistenceUnit) {
		super(persistenceUnit);
		ConfigurationUtility config = ConfigurationUtility.getInstance();
		this.folderPath = config.getFolderPathImport();
		this.regexNomeFileOrdini = config.getRegexOrdini();
		this.daoNazioni = new NazioniDao(persistenceUnit);
	}
	
	public void importa() {
		//recupero i files con gli ordini
		File folder = new File(folderPath);
		for (File file : folder.listFiles()) {
			//Controllo ogni file per vedere se contiene ordini.
			if (file.isFile() && file.getName().toUpperCase().matches(regexNomeFileOrdini)) {
				try {
					//parso il file.
					//ArrayList<String> lines = FileUtility.readLines(file);
					ObjectParser<OrdineDate> parser = new ObjectParser<>(OrdineDate.class, 1445);
					List<OrdineDate> ordini = parser.parsaOggetto(file);
					//genero i modelli.
					HashMap<String, MOrdine> mappaOrdini = new HashMap<>();
					for (OrdineDate ordine : ordini) {
						//Inserisco le informazioni di testata se necessario
						if (!mappaOrdini.containsKey(ordine.getNumeroOrdine())) {
							MOrdine testata = new MOrdine();
							testata.setDataDocumento(ordine.getDataOrdine());
							testata.setDataOrdine(ordine.getDataOrdine());
							testata.setDestinatario(getDestinatario(ordine));
							testata.setMittente(getMittente(ordine));
							testata.setNote(ordine.getNote());
							testata.setRiferimentoDocumento(ordine.getNumeroOrdine());
							testata.setRiferimentoOrdine(ordine.getNumeroOrdine());
							testata.setTipo(TipoOrdine.PRN);
							testata.setTipoDocumento(ordine.getTipoDocumento());
							testata.setTipoIdentificazioneProdotti(TipoIDProdotto.BARCODE);
							mappaOrdini.put(ordine.getNumeroOrdine(), testata);
						}
						//Aggiungo la riga se contiene oggetti
						if (ordine.getTipoRiga() != 4) {
							MOrdine testata = mappaOrdini.get(ordine.getNumeroOrdine());
							ProdottoOrdinato prodotto = new ProdottoOrdinato();
							prodotto.setBarcode(ordine.getBarcode());
							prodotto.setMagazzinoLTC("PG1");
							prodotto.setMagazzinoCliente("LP1");
							prodotto.setNumeroRiga(ordine.getNumeroRiga());
							prodotto.setQuantita(ordine.getQuantità());
							prodotto.setNote(ordine.getNoteRiga());
							testata.aggiungiProdotto(prodotto);
						}						
					}
					//importo gli ordini.
					for (MOrdine ordine : mappaOrdini.values()) {
						valida(ordine);
						inserisci(ordine);
					}
					//sposto il file nello storico
					spostaFile(file, folderPath + "storico\\");
				} catch (Exception e) {
					logger.error(e);
					spostaFile(file, folderPath + "errori\\");
				}				
			}
		}		
	}
	
	/**
	 * Per Date ho la particolarità per cui i riferimenti agli ordini vengono riciclati ogni anno.
	 * Mi assicuro che sia univoco solo all'interno dello stesso anno.
	 */
	public TestataOrdini trovaOrdineDaRiferimento(String riferimento) {
		GregorianCalendar now = new GregorianCalendar();
		String riferimentoEffettivo = now.get(Calendar.YEAR) + riferimento;
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TestataOrdini> criteria = cb.createQuery(TestataOrdini.class);
		Root<TestataOrdini> member = criteria.from(TestataOrdini.class);
		criteria.select(member).where(cb.equal(member.get("rifOrdineCli"), riferimentoEffettivo));
		List<TestataOrdini> list = em.createQuery(criteria).setMaxResults(1).getResultList();
		em.close();
		TestataOrdini ordine = list.size() == 1 ? list.get(0) : null;
		return ordine;
	}
	
	/**
	 * Per Date ho la particolarità per cui i riferimenti agli ordini vengono riciclati ogni anno.
	 * Aggiungo l'anno davanti per renderlo univoco all'interno dello stesso anno.
	 */
	@Override
	public boolean inserisciInformazioniAggiuntiveTestata(MOrdine ordine, TestataOrdini testata) {
		String riferimento = testata.getAnnoOrdine().toString() + ordine.getRiferimentoOrdine();
		testata.setRifOrdineCli(riferimento);
		return true;
	}
	
	protected MIndirizzo getDestinatario(OrdineDate ordine) {
		MIndirizzo destinatario = new MIndirizzo();
		boolean usaContabile = ordine.getRagioneSociale().isEmpty();
		if (usaContabile) {
			//Recupero la nazione corretta
			Nazioni nazione = daoNazioni.trovaDaCodificaCliente(ordine.getCodiceNazioneContabile());
			if (nazione == null)
				throw new ModelValidationException("La codifica di nazione indicata non esiste a sistema. (" + ordine.getCodiceNazioneContabile() + ")");
			destinatario.setCap(ordine.getCapContabile());
			destinatario.setCodice(ordine.getCodiceClienteParte1());
			destinatario.setIndirizzo(ordine.getIndirizzoContabile());
			destinatario.setLocalita(ordine.getCittaContabile());
			destinatario.setNazione(nazione.getCodIso());
			destinatario.setProvincia(ordine.getProvinciaContabile());
			destinatario.setRagioneSociale(ordine.getRagioneSocialeContabile());
			destinatario.setTelefono(ordine.getTelefonoContabile());
		} else {
			//Recupero la nazione corretta
			Nazioni nazione = daoNazioni.trovaDaCodificaCliente(ordine.getCodiceNazione());
			if (nazione == null)
				throw new ModelValidationException("La codifica di nazione indicata non esiste a sistema. (" + ordine.getCodiceNazione() + ")");
			destinatario.setCap(ordine.getCap());
			destinatario.setCodice(ordine.getCodiceClienteParte1());
			destinatario.setIndirizzo(ordine.getIndirizzo());
			destinatario.setLocalita(ordine.getCitta());
			destinatario.setNazione(nazione.getCodIso());
			destinatario.setProvincia(ordine.getProvincia());
			destinatario.setRagioneSociale(ordine.getRagioneSociale());
			destinatario.setTelefono(ordine.getTelefono());
		}		
		return destinatario;
	}
	
	protected MIndirizzo getMittente(OrdineDate ordine) {
		MIndirizzo mittente = new MIndirizzo();
		mittente.setCap("06073");
		mittente.setCodice("DEFAULT");
		mittente.setEmail("support@ltc-logistics.it");
		mittente.setIndirizzo("Via Firenze, 41");
		mittente.setLocalita("Corciano");
		mittente.setNazione("IT");
		mittente.setProvincia("PG");
		mittente.setRagioneSociale("DATE");
		mittente.setTelefono("");
		return mittente;
	}

}
