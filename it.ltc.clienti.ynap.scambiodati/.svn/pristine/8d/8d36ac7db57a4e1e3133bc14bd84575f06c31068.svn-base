package it.ltc.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.ltc.utility.configuration.Configuration;
import it.ltc.utility.ftp.SFTP;

public class GestoreSFTP {
	
	public static final int STRATEGY_LISTA_DI_CARICO = 1;
	public static final int STRATEGY_ORDINI = 2;
	
	private final int strategy;
	private Configuration configuration;
	private SFTP ftpClient;
	private String host;
	private String username;
	private String password;
	private String remotePath;
	private String localTempFolder;
	
	private GestoreSFTP(int strategia) {
		strategy = strategia;
		try {
			configuration = new Configuration("/resources/configuration.properties", false);
			host = configuration.get("sftp_host");
			username = configuration.get("sftp_username");
			password = configuration.get("sftp_password");
			remotePath = configuration.get("ingoing_path");
			if (strategy == STRATEGY_LISTA_DI_CARICO)
				localTempFolder = configuration.get("app_carichi_in_path");
			else if (strategy == STRATEGY_ORDINI)
				localTempFolder = configuration.get("app_ordini_in_path");
			ftpClient = new SFTP(host, username, password);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static GestoreSFTP getInstance(int strategia) {
		GestoreSFTP gestore;
		switch(strategia) {
			case STRATEGY_LISTA_DI_CARICO : gestore = new GestoreSFTP(strategia); break;
			case STRATEGY_ORDINI : gestore = new GestoreSFTP(strategia); break;
			default : throw new IllegalArgumentException("Scegli una strategia valida!");
		}
		return gestore;
	}
	
	public List<String> getNomiFiles() {
		List<String> names = ftpClient.getFileList(remotePath);
		List<String> localCopyNames = new ArrayList<String>();
		for (String name : names) {
			if (name.contains(getFilter()) && !(name.endsWith("end") || name.endsWith("imp"))) {
				String remotePathName = remotePath + name;
				String localPathName = localTempFolder + name;
				boolean download = ftpClient.download(remotePathName, localPathName);
				if (download) {
					//Controllo il "file sentinella"
					String nameSentinella = name.replace("xml", "end");
					boolean check = ftpClient.download(remotePath + nameSentinella, localTempFolder + nameSentinella);
					if (check)
						localCopyNames.add(localPathName);
					else
						System.out.println("File sentinella non trovato, il file di ordini non verrà preso in considerazione.");
				} else {
					System.out.println("Errore nel download di: " + remotePathName + " su: " + localPathName);
				}
			}
		}
		// TODO - Inoltre verifico che non abbia già letto questo file.
		return localCopyNames;
	}
	
	public void aggiornaFilesSentinella(List<String> listaFileDaRinominare) {
		for (String nomeFile : listaFileDaRinominare) {
			aggiornaFileSentinella(nomeFile);
		}
	}
	
	public boolean aggiornaFileSentinella(String nomeFile) {
		String nameRemoto = nomeFile.replace(localTempFolder, "");
		String nameSentinella = nameRemoto.replace("xml", "end");
		String nuovoNomeSentinella = nameRemoto.replace("xml", "imp");
		boolean rename = ftpClient.rename(remotePath + nameSentinella, remotePath + nuovoNomeSentinella);
		if (!rename) {
			System.out.println("File sentinella " + nameSentinella + " non aggiornato.");
		}
		return rename;
	}
	
	public String getFilter() {
		String filter;
		switch(strategy) {
			case STRATEGY_LISTA_DI_CARICO : filter = "items"; break;
			case STRATEGY_ORDINI : filter = "order"; break;
			default : filter = "zzz"; //Qualcosa non va, non deve filtrare.
		}
		return filter;
	}

}
