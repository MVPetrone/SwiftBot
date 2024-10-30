package subprocesses;

import swiftbot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class QRCode {
    private static SwiftBotAPI swiftBot;
	private Lights lights;
	private Prompt prompt;
    
	private String code;
	private boolean running;
	private boolean scanning;
	private String shapeName;
	private ArrayList<Integer> shapeDimensions;
	
	public QRCode(SwiftBotAPI swiftBot, Lights lights) {
		this.swiftBot = swiftBot;		
		this.lights = lights;
	}
	
	public void start() {		
		Scanner reader = new Scanner(System.in);
		this.prompt = new Prompt();
		
		this.code = "";
		this.running = true;
		this.scanning = false;
		
		while (this.running) {
			// Prompt 2.0
			this.prompt.QRCode6();
			
			// Start Flash
			lights.startFlash(1000, 1000, Lights.AMBER);
			
			// Start Scanning for qr code
			this.scanning = true;
			while (this.scanning) {
				try{
					// Read Qr
					String qr = ReadQR();
					Thread.sleep(20);
					
					// Flash yellow lights
					lights.updateFlash();

					// Check if scan is correct
					CheckQR(qr);

				} catch (Exception e) {
					//System.out.println();
				}
			}		
		}			
		// Prompt 2.1
		this.prompt.QRCode1();
	}
	
	public String ReadQR() {
		// Reads QR Code, returns "" if invalid
		try{   
	        BufferedImage img = this.swiftBot.getQRImage();
	        String code = this.swiftBot.decodeQRImage(img);
	        
	        // Check if QR is empty
	        if(code.isEmpty()) {
	        	//Prompt 2.5
	        	//this.prompt.QRCode5();
	            return "";
	        } else {
	        	// QR Code is successfully read and returned
	        	return code;
	        }
	    } catch(Exception e) {
	        // Prompt 2.4
	    	this.prompt.QRCode4();
	    	return "";
	    }
	}

	public void CheckQR(String qr) {
		if (!qr.isEmpty()) {
			this.code = qr;
			setShape(this.code);

			// Verify QR Contents
			ArrayList<Boolean> succs = VerifyQR(this.shapeName, this.shapeDimensions);
			if (succs.get(0)) {
				if (succs.get(1)) {
					this.scanning = false;
					this.running = false;

					// Stop flash
					lights.stopFlash();

					// Lights green
					this.lights.on(Lights.GREEN);
				} else {
					// Prompt 2.3
					this.prompt.QRCode3();
				}
			} else {
				// Prompt 2.2
				this.prompt.QRCode2();
			}
		}
	}

	public ArrayList<Boolean> VerifyQR(String name, ArrayList<Integer> dimensions) {
		// On default, success is true
		boolean succName = true;
		boolean succDim = true;
		
		// Name of shape is checked needing to be "S", "T" or "C"
		if (!Arrays.asList("S", "T", "C").contains(name)) {
			succName = false;
			// Prompt 2.2
			this.prompt.QRCode2();
		} 

		// Dimensions are searched and checked between 15 and 85
		for (int dim: dimensions) {
			if (!(dim>=15 && dim<=85)) {
				succDim = false;
				// Prompt 2.3
				this.prompt.QRCode3();
				break;
			}
		}
		// For Triangle the largest side needs to be smaller than the two smallest sides
		if (name=="T") {
			// Sort dims desc
			ArrayList<Integer> temp = dimensions;
			Collections.sort(temp, Collections.reverseOrder());
			if (temp.get(0)>(temp.get(1)+temp.get(2))) {
				succDim = false;
				// Prompt user that the two smallest sides need to be larger than the largets side
				this.prompt.QRCode7();
				this.prompt.QRCode3();
			}
		}

		// Success is returned as a boolean list
		ArrayList<Boolean> succs = new ArrayList<Boolean>();
		succs.add(succName);
		succs.add(succDim);
		return succs;
	}
	
	public String getNameFromText(String text) {
		// Using the QR code text name of shape is returned
		String[] codeSplit = text.split(" ", 2);
		String nameString = codeSplit[0];
		return nameString;
	}
	
	public ArrayList<Integer> getDimensionsFromText(String text) {
		// Using the QR code text dimensions are returned
		String[] codeSplit = text.split(" ", 2);
		String[] dimensionsString = codeSplit[1].split(" ");
		ArrayList<Integer> dimensions = new ArrayList<Integer>();
		for (String dim: dimensionsString) {
			dimensions.add(Integer.valueOf(dim));
		}		
		return dimensions;
	}
		
	public void setShape(String text) {
		try {
			// Set shape
			this.shapeName = getNameFromText(text);
			this.shapeDimensions = getDimensionsFromText(text);
				
		} catch(Exception e) {
			// Prompt 2.5
			this.prompt.QRCode5();
		}
	}
	
	public String getName() {
		return this.shapeName;
	}
	
	public ArrayList<Integer> getDimenions() {
		return this.shapeDimensions;
	}

	public String shapeNameConverter(String name) {
		String text = null;
		switch (name) {
			case "S" -> {
				text = "Square";
			}
			case "T" -> {
				text = "Triangle";
			}
			case "C" -> {
				text = "Circle";
			}
		}
		
		return text;
	}
}
