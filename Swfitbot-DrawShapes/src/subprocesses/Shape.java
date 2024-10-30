package subprocesses;

import swiftbot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Shape {
	private String name;
	private ArrayList<Integer> dimensions = new ArrayList<Integer>();
	private ArrayList<Double> angles = new ArrayList<Double>();
	public double duration = 0;

	public Shape() {
		// Init Constructor
	}

	public void setShape(String name, ArrayList<Integer> dimensions) {	
		// Sets the shape properties
		this.name = name;
		this.dimensions = dimensions;
		if (this.name == "Triangle") {
			this.angles = CalculateAnglesForTriangle(this.dimensions);
		}
	}
	
	public ArrayList<Double> CalculateAnglesForTriangle(ArrayList<Integer> dimensions) {
		// Uses law of cosines to converet sides to angles in a triangle
		ArrayList<Double> angles = new ArrayList<Double>();
		int a = dimensions.get(0);
		int b = dimensions.get(1);
		int c = dimensions.get(2);
		//System.out.println("a: "+a+"\nb: "+b+"\nc: "+c);
		
		// Law of consines (from radians to degrees) (angleC = Cos-1((a^2 + b^2 - c^2)/2ab) * (180/3.141))
		double C = 180-Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2))/(2*a*b))*(180/Math.PI);
		double B = 180-Math.acos((Math.pow(a, 2) + Math.pow(c, 2) - Math.pow(b, 2))/(2*a*c))*(180/Math.PI);
		double A = 180-Math.acos((Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2))/(2*b*c))*(180/Math.PI);
		//System.out.println("A: "+A+"\nB: "+B+"\nC: "+C);

		angles.add(C);
		angles.add(B);
		angles.add(A);
		return angles;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<Integer> getDimensions() {
		return this.dimensions;
	}
	
	public ArrayList<Double> getAngles() {
		return this.angles;
	}

	public double getDuration() {
		return this.duration;
	}
}
