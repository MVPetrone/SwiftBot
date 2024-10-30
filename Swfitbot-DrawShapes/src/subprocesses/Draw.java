package subprocesses;

import swiftbot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Draw {
	private static SwiftBotAPI swiftBot;
	private Prompt prompt;
	private Scanner reader = new Scanner(System.in);
	private Lights lights;

	ArrayList<Shape> shapesDrawn = new ArrayList<Shape>();
	double tenCmTime;
	double fModifier = 0.465;
	double rModifier = fModifier*1.2;
	
	public Draw(SwiftBotAPI swiftBot, Lights lights) {
		this.swiftBot = swiftBot;
		this.lights = lights;
		this.prompt = new Prompt();
		
		this.tenCmTime = 0;
	}

	public boolean verify(Shape shape) {
		boolean succ = true;
		for (int dim: shape.getDimensions()) {
			if (dim<15||dim>85) {
				succ = false;
			}
		}
		return succ;
	}

	public void shape(Shape shape) {
		// Ask for input
		this.prompt.Draw0(shape.getName());
		String ans = reader.nextLine();
		this.prompt.erraseLine(1);

		switch (ans) {
			case "d" -> {
				// Catch errors
				if (!verify(shape)) {
					// Prompt User that shape has invalid dimensions
					return;
				}
		
				// Turn on lights (blue)
				lights.on(Lights.BLUE);
		
				// Start timer
				double start = System.currentTimeMillis();

				// Select Shape to draw
				switch (shape.getName()) {
					case "Square" -> {
						square(shape.getDimensions());
					}
					case "Triangle" -> {
						triangle(shape.getDimensions(), shape.getAngles());
					}
					case "Circle" -> {
						circle(shape.getDimensions());
					}
				}
				// Stop timer
				double elapsed = System.currentTimeMillis()-start;
				shape.duration = elapsed;

				// Save shape drawn
				shapesDrawn.add(shape);

				// Turn lights (green)
				this.lights.on(Lights.GREEN);

				// Prompt user shape completed
				this.prompt.Draw2(elapsed);
			}
		}
	}
	
	public void square(ArrayList<Integer> dimensions) {
		int dist = dimensions.get(0);
		for (int i=0; i<4; i++) {
			// move forward (cm)
			forward(dist);
			// rotate (degrees)
			rotate(true, 90);
		}
	}
	
	public void triangle(ArrayList<Integer> dimensions, ArrayList<Double> angles) {
		for (int i=0; i<3; i++) {
			// move forward (cm)
			forward(dimensions.get(i));
			// rotate (degrees)
			rotate(true, angles.get(i));
		}
	}
	
	public void circle(ArrayList<Integer> dimensions) {
		// turn and move at the same time
		
	}
	
	public void forward(double distance) {
		int vel = 100;

		double dur = ((distance/10)*this.tenCmTime)*(fModifier);

		// move forward using tenCmTime
		this.swiftBot.move(vel, vel, (int)dur);
	}
	public void rotate(boolean clockwise, double degrees) {
		// Properties
		int lVel = 0;
		int rVel = 0;

		// Check for valid orientation
		if (clockwise==true) {
			lVel = 100;
			rVel = 0;
		} else if (clockwise==false) {
			lVel = 0;
			rVel = 100;
		} else {
			// Invalid rotation orientation input
			return;
		}

		// Check for valid degrees
		if (!(degrees>=0 && degrees<=360)) {
			// Invalid rotation degrees input
			return;
		}
		// turn degrees using tenCmTime
		int radius = 13; // In (cm)

		//System.out.println((90.0/360.0)*(2*Math.PI*radius));
		double distance = (degrees/360.0)*(2*Math.PI*radius);
		//System.out.println("dist: "+distance);
		double duration = ((distance/10)*this.tenCmTime)*(rModifier); // Final Duration + Modifier
		//System.out.println("dur: "+duration);

		this.swiftBot.move(lVel, rVel, (int)duration);
	}

	public void setTenCmTime(double tenCmTime) {
		this.tenCmTime = tenCmTime;
	}

	public ArrayList<Shape> getShapesDrawn() {
		return this.shapesDrawn;
	}
}
