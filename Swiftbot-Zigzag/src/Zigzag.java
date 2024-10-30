import swiftbot.*;

import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

import subprocesses.*;

public class Zigzag {
    // Init
    public static SwiftBotAPI swiftBot = new SwiftBotAPI();
    public static Scanner reader = new Scanner(System.in);

    // Subprocesses
    public static Converters converters;
    public static Lights lights;
    public static Qrcode qrcode;
    public static Movement movement;
    public static Timer movementTimer;
    public static Timer mainloopTimer;
    public static SaveMaker saveMaker;

    // Properties
    public static boolean mainloopRunning = false;
    public static boolean prompted = false;
    public static boolean moving = false;

    // Save contents
    public static ArrayList<ArrayList<String>> saveContents = new ArrayList<ArrayList<String>>();
    public static void main(String[] args) {
        // Subprocesses
        converters = new Converters();
        lights = new Lights(swiftBot);
        qrcode = new Qrcode(swiftBot, lights);
        movement = new Movement(swiftBot, reader);
        movementTimer = new Timer();
        saveMaker = new SaveMaker();

        mainloopTimer = new Timer();

        // Enable swiftbot buttons
        swiftBot.enableButton(Button.A, () -> {
            // Start
            if (!moving && !prompted) {
                Start();
            } else if (!moving && prompted) {
                prompted = false;
                StartMovement();
                Start();
            }
        });

        swiftBot.enableButton(Button.B, () -> {
			// Calibrate
            if (prompted && !moving) {
                prompted = false;
                movement.calibrate();
                Start();
            }

		});

        swiftBot.enableButton(Button.Y, () -> {
			// Qr code
            if (!qrcode.running || !qrcode.succ && prompted) {
                prompted = false;
                System.out.println("Scanning for QR Code...");
                qrcode.start();
                Start();
            } else if (qrcode.running || !qrcode.succ) {
                qrcode.exit();
            }

		});

        swiftBot.enableButton(Button.X, () -> {
			// Terminate
            Terminate();
		});

        System.out.println("Hello and welcome to the SwiftBot\nPress A to get started.");

        mainloop(5);
    }

    public static void mainloop(int tps) {
        // Properties
        mainloopRunning = true;

        // Start mainloop timer
        mainloopTimer.start();

        // Update loop
        while (mainloopRunning) {
            mainloopTimer.update();
            if (mainloopTimer.getElapsedTime()>(1.0/tps)*1000) {
                mainloopTimer.start();
                Update();
            }
        }
    }

    public static void Start() {
        // Properties
        prompted = true;

        // Flash Lights
        Blink("green", 400);
        Blink("", 200);
        Blink("green", 400);

        // Main loop
        System.out.println("\nCurrent Code: "+qrcode.code+"\n");
        System.out.println("Press A - Start Movement");
        System.out.println("Press B - Calibrate");
        System.out.println("Press Y - Scan QR Code");
        System.out.println("Press X - Terminate\n");
    }

    public static void StartMovement() {
        // Check if qr code has been scanned
        if (!qrcode.succ) {
            System.out.println("QR Code must be scanned before moving!");
            return;
        }

        moving = true;
        // Get values
        int decimal = GetDecimal();
        String colour = GetColour();
        
        // Blink colour
        Blink(colour, 2000);
        // Print current time
        System.out.println("Starting time: "+CurrentTime());
        // Perform entire movement
        Move(decimal);
        // Print current time
        System.out.println("Ending time: "+CurrentTime());
        System.out.println("Total Duration of movement: "+movementTimer.getElapsedTime()/1000.0);
        // Blink colour
        Blink(colour, 2000);
        moving = false;
    }

    public static int GetDecimal() {
        // Get decimal with or without QR Code
        int decimal;
        if (qrcode.succ) {
            decimal =  Integer.valueOf(qrcode.code.split(":")[0]);
        } else {
            // Default value if there is no successful QR Code
            decimal = 100;
            System.out.println("Invalid Decimal: Using -> "+ decimal);
        }
        return decimal;
    }
    
    public static String GetColour() {
        // Get colour with or without QR Code
        String colour;
        if (qrcode.succ) {
            colour = qrcode.code.split(":")[1];
        } else {
            // Default value if there is no successful QR Code
            colour = "White";
            System.out.println("Invalid Colour: Using -> "+ colour);
        }
        return colour;
    }

    public static void Blink(String colour, int duration) {
        // Get colour
        colour = colour.toUpperCase();
        if (colour.equals("RED")) {
            swiftBot.fillUnderlights(Lights.RED);
        } else if (colour.equals("BLUE")) {
            swiftBot.fillUnderlights(Lights.BLUE);
        } else if (colour.equals("GREEN")) {
            swiftBot.fillUnderlights(Lights.GREEN);
        } else if (colour.equals("WHITE")) {
            swiftBot.fillUnderlights(Lights.WHITE);
        } else {
            swiftBot.fillUnderlights(Lights.EMPTY);
        }

        // Wait for flash to finish
        double start = System.currentTimeMillis();
        while (System.currentTimeMillis()-start<duration) {
            // Wait 2000 milliseconds
        }        
        // End flash
        lights.off();
    }

    public static String CurrentTime() {
        // Gets the current time of day
        return new Date().toString().split(" ")[3];
    }
    
    public static void Move(int decimal) {
        // Reset light
        //lights.off();

        // Format converted values
        String movements = Converters.Movements(decimal);
        int duration = Converters.Duration(decimal);
        int speed = Converters.Speed(decimal);

        System.out.println("\nMovement: "+movements+"\nDuration: "+duration+"\nSpeed: "+speed+"\n");

        // Start elapsed timer
        movementTimer.start();

        // Perform movement
        for (int i=movements.length()-1; i>=0; i--) {  // Loops throught the binary digits from right to left
            // dir = 0 (anti-clockwise)
            // dir = 1 (clockwise)
 
            try {
                int dir = Integer.valueOf(movements.substring(i, i+1));  // Gets one binary digit at a time from right to left
                movement.forward(speed, speed, duration*1000);
                movement.turn(90, dir);
            }  catch (Exception r) {

            }
            
        }
        
        // End elapsed timer
        movementTimer.end();

        // Save to file
        // Gather conent to be saved
        String qrcodeContent = qrcode.code;
        String movementContent = Converters.decToBin(decimal);
        String durationContent = Converters.decToHex(decimal);
        String speedContent = String.valueOf(speed);
        String elapsedContent = String.valueOf((double)(int)movementTimer.finalTime/1000.0);

        // Set content to list
        ArrayList<String> content = new ArrayList<String>(Arrays.asList(qrcodeContent, movementContent, durationContent, speedContent, elapsedContent));

        // Add content list to global list
        saveContents.add(content);

    }

    public static void Save() {
        // Save program to text file
        System.out.println("Saving Content!\nFile is saved at zigzag/save.txt");

        // Go through all saves and save each one
        for (ArrayList<String> content: saveContents) {
            saveMaker.createSave(content);
        }
    }

    // 3 or 'X'
    public static void Terminate() {
        // Save Program if there is anything to save
        if (!saveContents.isEmpty()) {
            Save();
        }

        // End loops
        System.out.println("Closing Program!");
        mainloopRunning = false;

        // Close program
        try {
            swiftBot.stopMove();
            movementTimer.end();
            mainloopTimer.end();
            qrcode.exit();
            lights.exit();
            System.exit(0);
        } catch (Exception e) {

        }
    }

    public static void Update() {
        // Updates the main loop of the program
        movementTimer.update();
    }
}