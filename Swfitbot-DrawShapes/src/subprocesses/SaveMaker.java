package subprocesses;

import swiftbot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SaveMaker {
    private static SwiftBotAPI swiftBot;
	private Prompt prompt = new Prompt();
    private Scanner reader = new Scanner(System.in);

    private File file;
    private FileWriter writer;
    
    public SaveMaker(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;
    }

    public void createSave(ArrayList<Shape> shapes) {
        // Verify that file should be written
        if (shapes.isEmpty()) {
            // Prompt user that there are no shapes
            prompt.Save1();
            return;
        }

        // Open file
        String path = "save.txt";
        try {
            this.file = new File(path);
            this.file.createNewFile();
        } catch (Exception e){
            // Prompt error making file
            prompt.Save0();
        }

        // Collect Contents
        String fileContent = getFileContent(shapes);

        // Write Contents
        try{
            FileWriter writer = new FileWriter(path);
            writer.write(fileContent);
            writer.close();
        } catch (Exception e) {
            // Prompt Error writing to file
            prompt.Save2();
        }
    }

    public String getFileContent(ArrayList<Shape> shapes) {
        // Collect Contents
        ArrayList<String> content = new ArrayList<String>();
        for (Shape shape: shapes) {
            // Shapes
            content.add("" + shape.getName()+ ": " + shape.getDimensions() + "(angles: "+shape.getAngles()+")");
        }
        // Largest
        Shape largest = getLargest(shapes);
        String contentLargest = "Largest Shape: "+largest.getName()+" "+largest.getDimensions()+"cm";

        // Most frequent
        String[] mostFreq = getMostFreq(shapes);
        String contentMostFreq = "Most Frequent Shape: " + mostFreq[0]+", "+mostFreq[1]+" times";

        // Average time
        double averageTime = getAverageDuration(shapes);
        String contentAverageTime = "Average Time Taken: "+ averageTime/1000 + " seconds";

        // Format Content
        String fileContent = String.join(", ", content) + "\n" + contentLargest +"\n"+ contentMostFreq +"\n"+ contentAverageTime;
        return fileContent;
    }

    public Shape getLargest(ArrayList<Shape> shapes) {
        int count = 0;
        ArrayList<Integer> largestNumbers = new ArrayList<Integer>();

        // Find largest numbers
        for (Shape shape: shapes) {
            ArrayList<Integer> dims = shape.getDimensions();
            Collections.sort(dims, Collections.reverseOrder());
            int x = dims.get(0);
            largestNumbers.add(x);
            count++;
        }
        // Get largest shape using largest numbers
        ArrayList<Integer> temp = largestNumbers;
        Collections.sort(temp, Collections.reverseOrder());
        int index = largestNumbers.indexOf(temp.get(0));
        Shape shape = shapes.get(index);
        return shape;
    }

    public String[] getMostFreq(ArrayList<Shape> shapes) {
        int count = 0;
        int square = 0;
        int triangle = 0;
        int circle = 0;

        // Count shapes
        for (Shape shape: shapes) {
            switch (shape.getName()) {
                case "Square" -> {
                    square++;
                }
                case "Triangle" -> {
                    triangle++;
                }
                case "Circle" -> {
                    circle++;
                }
            }
            count++;
        }
        // Sort counts
        ArrayList<Integer> counts = new ArrayList<Integer>(Arrays.asList(square, triangle, circle));
        ArrayList<Integer> temp2 = counts;
        Collections.sort(temp2, Collections.reverseOrder());
        int index = counts.indexOf(temp2.get(0));
        
        // Find shape with index
        String shape = "No Shape"; 
        String amount = "No Amount";
        switch (index) {
            case 0 -> {
                shape = "Square";
                amount = "" + square;
            }
            case 1 -> {
                shape = "Triangle";
                amount = "" + triangle;
            }
            case 2 -> {
                shape = "Circle";
                amount = "" + circle;
            }
        }
        // Return values
        String[] values = new String[]{shape, amount};
        return values;
    }

    public double getAverageDuration(ArrayList<Shape>shapes) {
        int count = 0;
        double total = 0;
        
        // Find mean
        for (Shape shape: shapes) {
            total += shape.getDuration();
            count++;
        }
        // Return Values
        return total/count;
    }
}
