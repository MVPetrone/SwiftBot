package subprocesses;
import swiftbot.*;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Qrcode {
    public static SwiftBotAPI swiftBot;
    private Lights lights;

    // Properties
    public String code;
	public boolean running;
	private boolean scanning;
    public boolean succ;

    public Qrcode(SwiftBotAPI swiftBot, Lights lights) {
		this.swiftBot = swiftBot;
        this.lights = lights;
        this.succ = false;
	}

    public void start() {
        // Properties
		this.running = true;
		this.scanning = false;
		
		while (this.running) {
			// Start Flash
			lights.startFlash(1000, 1000, Lights.AMBER, Lights.EMPTY);
			
			// Start Scanning for qr code
			this.scanning = true;
			while (this.scanning) {
				try{
					// Read Qr
					String qr = ReadQR();
					Thread.sleep(20);
					
					// Update lights
					lights.updateFlash();

					// Check if scan is correct
					this.succ = CheckQR(qr);
                    if (this.succ) {
                        this.code = qr;
						System.out.println("Qr code successful: "+this.code);
                    }
				} catch (Exception e) {
					//System.out.println();
				}
			}		
		}
    }

    public String ReadQR() {
		// Reads QR Code, returns "" if invalid
		try{   
	        BufferedImage img = this.swiftBot.getQRImage();
	        String code = this.swiftBot.decodeQRImage(img);
	        
	        // Check if QR is empty
	        if(code.isEmpty()) {
	            return "";
	        } else {
	        	// QR Code is read and returned
	        	return code;
	        }
	    } catch(Exception e) {
	    	return "";
	    }
	}

    public boolean CheckQR(String qr) {
		if (!qr.isEmpty()) {	
            // Format qr contents
            int value = Integer.valueOf(qr.split(":")[0]);
            String colour = qr.split(":")[1];

			// Verify QR Contents
			boolean[] succs = VerifyQR(value, colour);
			if (succs[0]) {
				if (succs[1]) {
                    // Successful Qr Code
					this.scanning = false;
					this.running = false;

					// Stop flash
					lights.stopFlash();

					// Lights green
					this.lights.on(Lights.CYAN);
                    return true;
				} else {
                    // Error: Incorrect colour
                    System.out.println("Incorrect Colour: try again");
                    return false;
				}
			} else {
                // Error: Incorrect value
                System.out.println("Incorrect Value: try again");
                return false;
			}
		} else {
            // Error: Code empty
            //System.out.println("Incorrect Code: try again");
            return false;
        }
	}

    public boolean[] VerifyQR(int value, String colour) {
		// On default, success is true
		boolean succValue = true;
		boolean succColour = true;
		
        // Check if value correct
        if (!(value>=0 && value<=100)) {
            succValue = false;
        }
		
        // Check if colour correct
        if (!Arrays.asList("RED", "GREEN", "BLUE", "WHITE").contains(colour.toUpperCase())) {
            succColour = false;
        } 
		
		// Success is returned as a boolean list
        boolean[] succs = {succValue, succColour};
		return succs;
	}

    public void exit() {
        this.lights.exit();
        this.running = false;
        this.scanning = false;
    }
}
