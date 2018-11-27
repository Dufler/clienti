package it.ltc.clienti.artcraft;

import it.ltc.clienti.artcraft.carichi.EsportaCarichi;
import it.ltc.clienti.artcraft.ftp.ScambioFTP;

/**
 * Funzioni disponibili:
 * - esportazione carichi: opzione -E o esportacarichi
 * - scambio dati FTP: opzione -S o ftp
 * @author Damiano
 *
 */
public class MainArtcraft {

	public static void main(String[] args) {
		if (args.length != 1)
			throw new IllegalArgumentException("Va inserito un solo argomento. Gli argomenti possibili sono: -E (esportazione carichi) e -S (Scambio dati FTP).");
		switch (args[0]) {
			case "-E" : case "esportacarichi" : { EsportaCarichi.getInstance().esportaCarichi(); } break;
			case "-S" : case "ftp" : { ScambioFTP.getInstance().scambia(); } break;
			default : throw new IllegalArgumentException("Argomento non valido. Gli argomenti possibili sono: -E (esportazione carichi) e -S (Scambio dati FTP).");
		}
		
	}

}
