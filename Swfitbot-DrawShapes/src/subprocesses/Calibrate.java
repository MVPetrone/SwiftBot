package subprocesses;

import swiftbot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Calibrate {
	private static SwiftBotAPI swiftBot;
	private Lights lights;
	private Prompt prompt;

	private Scanner reader;
	
	private boolean running;
	private double tenCmTime = -1;
	private double meanTenCmTime = -1;
	private ArrayList<Double> times;
	private boolean succ;
	
	public Calibrate(SwiftBotAPI swiftBot, Lights lights) {
		this.swiftBot = swiftBot;
		this.lights = lights;
		this.times = new ArrayList<Double>();
	}

	public void start() {
		// Constructor
		this.prompt = new Prompt();
		this.reader = new Scanner(System.in);
		this.succ = false;
		
		// Loop until successful
		this.running = true;
		while (this.running) {
			// Prompt 3.0
			this.prompt.Cali0();
			String answer = reader.next();
			this.prompt.erraseLine(1);

			// Check answer for "c" input
			switch (answer) {
				case "c" -> { // Manual Calibration
					// Prompt 3.1 calibrating robot
					this.prompt.Cali1();
					
					calibrate(50);

					// If successful then finsih loop
					if (this.succ) {
						this.running = false;
					}
				}

				case "s" -> { // Super Calibration
					// Prompt 3.1 calibrating robot
					this.prompt.Cali1();
					
					for (int i=0; i<10; i++) {
						calibrate(50);
						add(this.tenCmTime);
					}

					// If successful then finsih loop
					if (this.succ) {
						this.running = false;
					}
				}

				default -> {
					// Prompt 3.4 (invalid input)
					if (!this.succ) {
						this.prompt.Cali4();
					}
				}
			}
		}
		// Prompt 3.2 (displays success and time)
		this.prompt.Cali2(this.tenCmTime);
		
		// Prompt 3.5 (ask to keep or discard)
		askOptions(this.tenCmTime);
	}
	
	public void calibrate(int velocity) {	
		// Checks starting distance
		double distance = this.swiftBot.useUltrasound();
		if (distance>15) {
			// Prompt user that robot is too far
			this.prompt.Cali8();
			this.succ = false;

			// Red
			lights.on(Lights.RED);
			return;
		}
		// Performs calibration test and sets tenCmTime variable
		try {
			// Lights yellow
			lights.on(Lights.YELLOW);
			
			// Reverse until 10cm ultra sound has been covered
			double time1 = reverseUntil(velocity, 10);
			Thread.sleep(500);
			double time2 = forwardUntil(velocity, 10);

			double meanTime = (time1+time2)/2;
								
			// Define tenCmTime variable 
			setTenCmTime(meanTime);
			this.succ = true;

			lights.off();
			Thread.sleep(500);
			
		} catch(Exception e) {
			// Prompt 3.3
			this.prompt.Cali3();
			this.swiftBot.stopMove();
			this.succ = false;
			
			// Turn off lights
			lights.on(Lights.RED);
		}
	}

	public double reverseUntil(int velocity, double amount) {
		// Get start distance
		double startDist = this.swiftBot.useUltrasound();
		double distance = startDist;

		// Start
		double startTime = System.currentTimeMillis();

		this.swiftBot.startMove(-velocity, -velocity);
		while (distance<amount+startDist) {
			try {
				distance = this.swiftBot.useUltrasound();
				Thread.sleep(20);	
			} catch (Exception e) {
				// Prompt user ultrasound error
			}		
		}
		this.swiftBot.stopMove();

		// Stop
		double endTime = System.currentTimeMillis();
		double elapsedTime = endTime - startTime;
		return elapsedTime;
	}

	public double forwardUntil(int velocity, double amount) {
		// Start
		double startTime = System.currentTimeMillis();

		// Get start distance
		double startDist = this.swiftBot.useUltrasound();
		double distance = startDist;

		this.swiftBot.startMove(velocity, velocity);
		while (distance>startDist-amount) {
			try {
				distance = this.swiftBot.useUltrasound();
				Thread.sleep(20);	
			} catch (Exception e) {
				// Prompt user ultrasound error
			}		
		}
		this.swiftBot.stopMove();

		// Stop
		double endTime = System.currentTimeMillis();
		double elapsedTime = endTime - startTime;
		return elapsedTime;
	}

	public void askOptions(double time) {
		// Prompt the user to 
		this.prompt.Cali5();

		// Ask user input
		String answer = this.reader.next();	
		this.prompt.erraseLine(1);
		switch (answer) {
			case "a" -> {
				// Add calibration
				add(time);
			}
			case "d" -> {
				// Discard calibration
				discard();
			}
			case "c" -> {
				// Replace calibration
				clear();
			}
		}
		// Prompt user current calibration
		this.prompt.Cali7(getMeanTime());
	}
	
	public void add(double time) {
		// Adds calibration time to current list and sets the mean
		// Add to times
		this.times.add(time);

		// Calculate mean
		this.meanTenCmTime = calculateMean();
	}

	public void discard() {
		// Discard Calibration
	}

	public void clear() {
		// Clears calibration
		this.times.clear();
	}

	public boolean verify(double time) {
		if (time<=0 || time >2000) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setTenCmTime(double value) {
		// Sets calibration time
		this.tenCmTime = value;
	}
	
	public double getTenCmTime() {
		// Returns calibration time, optimised for computation
		return this.tenCmTime;
	}
	
	public double calculateMean(){
		// Calulates the mean of all calibration times
		double times = 0;
		int count = 0;
		for (double x: this.times) {
			times += x;
			count++;
		}
		return times/count;
	}

	public void setMeanTime(double value) {
		this.meanTenCmTime = value;
	}

	public double getMeanTime() {
		return this.meanTenCmTime;
	}
}


