package it.ltc.coltorti.webcam.controller;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class WebcamController {
	
	private static WebcamController instance;
	
	private final Webcam webcam;
	
	private String errorMessage;

	private WebcamController() {
		webcam = Webcam.getDefault();
		if (webcam == null)
			throw new RuntimeException("Nessuna webcam trovata!");
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		errorMessage = "";
	}

	public static WebcamController getInstance() {
		if (instance == null) {
			instance = new WebcamController();
		}
		return instance;
	}
	
	public WebcamPanel getPanel() {
		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(false);
		panel.setDisplayDebugInfo(false);
		panel.setImageSizeDisplayed(false);
		panel.setMirrored(false);
		return panel;
	}
	
	public boolean capturePhoto(String path) {
		boolean success;
		try {
			webcam.open();
			success = ImageIO.write(webcam.getImage(), "PNG", new File(path));
			if (!success)
				System.out.println("Impossibile salvare l'immagine nel percoroso specificato. (" + path + ")");
		} catch (IOException e) {
			success = false;
			errorMessage = e.getLocalizedMessage();
			e.printStackTrace();
		}
		return success;
	}
	
	public void closeWebcam() {
		if (webcam.isOpen())
			webcam.close();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		closeWebcam();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
