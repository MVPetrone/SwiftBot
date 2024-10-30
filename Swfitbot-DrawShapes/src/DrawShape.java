/* 
javac -cp "lib/SwiftBotAPI-5.1.0.jar" -d bin src/DrawShape.java src/subprocesses/QRCode.java src/subprocesses/Calibrate.java src/subprocesses/Draw.java src/subprocesses/Shape.java src/subprocesses/Prompt.java src/subprocesses/Controller.java src/subprocesses/SaveMaker.java
java -cp "lib/SwiftBotAPI-5.1.0.jar:bin" DrawShape

- add flashing yellow lights for scanning qr /
- remove angles being saved for square /
- add distance checker to stop calibration from being too far away from the wall (15cm)
- add draw circle
*/

import swiftbot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import subprocesses.*;

public class DrawShape {
	public static DrawShape drawshape = new DrawShape();
	public static SwiftBotAPI swiftBot = new SwiftBotAPI();

	public static Scanner reader;
	public static QRCode qr;
	public static Calibrate cali;
	public static Shape shape;
	public static Draw draw;
	public static Prompt prompt;
	public static SaveMaker saveMaker;
	public static Lights lights;

	public static void main(String[] args) {
		// Main Constructor
		//drawshape = new DrawShape();
		//swiftBot = new SwiftBotAPI();
		
		reader = new Scanner(System.in);
		lights = new Lights(swiftBot);
		qr = new QRCode(swiftBot, lights);
		cali = new Calibrate(swiftBot, lights);
		shape = new Shape();
		draw = new Draw(swiftBot, lights);
		prompt = new Prompt();
		saveMaker = new SaveMaker(swiftBot);

		// Set terminate button
		swiftBot.enableButton(Button.X, () -> {
			terminate();
		});

		start();
	}

	public static void start() {
		boolean running = true;	
		// Prompt welcome message
		prompt.Main0();
	
		// Main Loop
		while (running) {
			String answer = askOptions();
			
			switch (answer) {
			
				case "1" -> {
					// Turn off LEDs
					lights.off();
					
					// Qr code
					qr.start();
					
					// Create shape
					shape.setShape(qr.shapeNameConverter(qr.getName()), qr.getDimenions());

					// Calibration
					boolean succ = cali.verify(cali.getMeanTime());
					if (succ) {
						draw.setTenCmTime(cali.getMeanTime());
	
						// Draw shape
						draw.shape(shape);					
					} else {
						// Light red
						lights.on(Lights.RED);

						// Prompt User To Calibrate
						prompt.Cali6();
					}
				}
				case "2" -> {
					// Calibrate
					cali.start();
				}				
				case "3" -> {
					// Terminate
					reader.close();
	                running = false;
					terminate();
				}	
				default -> {
					// Invalid input
					prompt.Main3();
				}
			}			
		}
		
		terminate();
	}

	public static String askOptions() {
		// Prompt 1.0
		prompt.Main4();
		String answer = reader.next();
		prompt.erraseLine(1);
		return answer;
	}

	public static void terminate() {
		// Prompt user that there is termination
		prompt.Term0();

		// Attempt to write to file
		saveMaker.createSave(draw.getShapesDrawn());

		// Exit
		System.exit(0);
	}
}
