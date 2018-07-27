package it.ltc.ciesse.scambiodati.model;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.model.legacy.Nazioni;
import it.ltc.utility.miscellanea.string.StringParser;

public class Nazione {
	
	private final String codiceISO;
	private final String nome;
	
	public Nazione(String codiceISO, String nome) {
		super();
		this.codiceISO = codiceISO;
		this.nome = nome;
	}

	public String getCodiceISO() {
		return codiceISO;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return "Nazione [codiceISO=" + codiceISO + ", nome=" + nome + "]";
	}

	public static List<Nazioni> parsaNazioni(List<String> righe) {
		List<Nazioni> nazioni = new LinkedList<>();
		String[] lines = new String[righe.size()];
		lines = righe.toArray(lines);
		StringParser parser = new StringParser(lines, 53);
		do {
			String nome = parser.getStringa(0, 50);
			String iso = parser.getStringa(50, 53);
			Nazioni nazione = new Nazioni();
			nazione.setCodIso(iso);
			nazione.setDescrizione(nome);
			nazione.setMembro("NO");
			nazioni.add(nazione);
		} while (parser.prossimaLinea());
		return nazioni;
	}

}
