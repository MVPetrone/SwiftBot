package subprocesses;

import java.awt.*; import javax.swing.*;

import swiftbot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {
    private static SwiftBotAPI swiftBot;
    private static KeyboardListener kl;

    private boolean running;

    public Controller(SwiftBotAPI swiftBot) {
        // Properties
        this.swiftBot = swiftBot;
        this.kl = new KeyboardListener();
        this.running = true;
    }  
    
    public void start() {
        this.swiftBot.move(50, 50, 2000);
    }    
}

class KeyboardListener {
    public KeyboardListener() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Keyboard Listener started. Press 'q' to quit.");

        // Listen for keyboard input in an infinite loop
        while (true) {
            // Read the next line of input
            String input = scanner.nextLine();

            // Check if the input is 'q' to quit
            if (input.equals("q")) {
                System.out.println("Exiting...");
                System.exit(0);
                break; // Exit the loop
            }

            // Echo the input back to the user
            System.out.println("You entered: " + input);
        }

        // Close the scanner
        scanner.close();
    }
}