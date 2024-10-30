package subprocesses;

import swiftbot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Prompt {
	
	public Prompt() {	
	}
	
	public void Main0() {
		System.out.println("Welcome to the SwiftBot DrawShape Task");
	}
	
	public void Main1() {
		QRCode0();
	}
	
	public void Main2() {
		System.out.println("Terminating! See you again soon.");
	}
	
	public void Main3() {
		System.out.println("\nInvalid Input! Restarting Program!\n");
	}

	public void Main4() {
		System.out.println("Input what you would like to do next");
		System.out.println("(1) Draw Shape \n(2) Calibrate \n(3) Terminate"+"\n");
	}
	
	public void QRCode0() {
		System.out.println("Please place QR Code infront of SwiftBot and type 's' then press Enter");
	}
	
	public void QRCode1() {
		System.out.println("Input Validation Successful");
	}
	
	public void QRCode2() {
		System.out.println("Invalid Shape! Restarting QR Scan...");
	}
	
	public void QRCode3() {
		System.out.println("Invalid Dimensions! Restarting QR Scan...");
	}
	
	public void QRCode4() {
		System.out.println("Error Scanning QR Code");
	}
	
	public void QRCode5() {
		System.out.println("Invalid Input! Restarting QR Scan...");
	}

	public void QRCode6() {
		System.out.println("Scanning QRCode...");
	}
	
	public void QRCode7() {
		System.out.println("\nThe two smallest sides need to be larger than the largest side\n");
	}

	public void Cali0() {
		System.out.println("\nCalibration Test: In order to start calibration test");
		System.out.println("please place the Robot facing a wall on the surface");
		System.out.println("(c) Calibation Test - Manual\n(s) Super Calibration - Auto"+"\n");
	}
	
	public void Cali1() {
		System.out.println("\nCalibrating Robot...");
	}
	
	public void Cali2(double tenCmTime) {
		System.out.println("Calibration Test Successful!");
		System.out.println("The time it takes to travel 10cm is " + tenCmTime/1000 + "seconds");
	}
	
	public void Cali3() {
		System.out.println("Calibration Error! Restarting Calibration");
	}
	
	public void Cali4() {
		System.out.println("Invalid Input! Restarting Calibration");
	}
	
	public void Cali5() {
		System.out.println("Would you like to keep this calibration?\n(a) Add (d) Discard (c) Clear All");
	}

	public void Cali6() {
		System.out.println("\nPlease Enter a valid Calibration before attempting to draw a shape!\n");
	}

	public void Cali7(double mean) {
		System.out.println("\nCurrent Calibration: "+(int)mean+"\n");
	}

	public void Cali8() {
		System.out.println("Error: Place within 15cm of a wall to start calibration test");
	}
	
	public void Draw0(String shape) {
		System.out.println("\nDraw Shape: Place Robot in a place with enough space");
		System.out.println("(d) Draw " + shape+"\n");
	}
	
	public void Draw1() {
		System.out.println("Drawing Shape...");
	}
	
	public void Draw2(double time) {
		System.out.println("Shape completed in "+time/1000+" seconds");
	}
	
	public void Term0() {
		System.out.println("Terminating! See you again soon");
	}
	
	public void Term1() {
		System.out.println("Ending Draw Shape Task...");
	}

	public void Save0() {
		System.out.println("Error Creating Save File");
	}
	
	public void Save1() {
		System.out.println("There are no drawn shapes to save");
	}
	
	public void Save2() {
		System.out.println("Error Writing to file");
	}
	
	public void erraseLine(int count) {
		System.out.print(String.format("\033[%dA",count));	
		System.out.print("\033[2K"); // Erase line content
	}
}