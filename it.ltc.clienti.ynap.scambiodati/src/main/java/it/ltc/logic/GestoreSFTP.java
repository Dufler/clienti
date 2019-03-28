package it.ltc.logic;

import java.util.ArrayList;
import java.util.List;

import it.ltc.utility.ftp.SFTP;

public class GestoreSFTP {
	
	public enum Strategy { LISTA_DI_CARICO, ORDINI }
	
	private final Strategy strategy;
	private final ConfigurationUtility configuration;
	private final SFTP ftpClient;
	private final String remotePath;
	private final String localTempFolder;
	
	public GestoreSFTP(Strategy strategia) {
		strategy = strategia;
		configuration = ConfigurationUtility.getInstance();
		ftpClient = configuration.getSFTPClient();
		remotePath = configuration.getPathFTPIn();
		switch (strategy) {
			case LISTA_DI_CARICO : localTempFolder = configuration.getPathCarichiIn(); break;
			case ORDINI : localTempFolder = configuration.getPathOrdiniIn(); break;
			default : throw new IllegalArgumentException("Scegli una strategia valida!");
		}		
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
			case LISTA_DI_CARICO : filter = "items"; break;
			case ORDINI : filter = "order"; break;
			default : filter = "XXX"; //Qualcosa non va, non deve filtrare.
		}
		return filter;
	}

}
