package it.ltc.ciesse.scambiodati.logic;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltroFile extends FileFilter {

	@Override
	public boolean accept(File f) {
		boolean accept = f != null && f.isFile();
		return accept;
	}

	@Override
	public String getDescription() {
		return "Il file deve esistere e non essere una cartella.";
	}

}
