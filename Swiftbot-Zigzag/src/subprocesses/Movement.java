package subprocesses;

import java.util.Scanner;

import swiftbot.*;

public class Movement {
    public static SwiftBotAPI swiftBot;
    public Scanner reader;

    // Default Surface Duration (milliseconds)
    public int calibrationDuration = 1600;  // Duration for 360 degrees
    public boolean calibrated = false;

    public Movement(SwiftBotAPI swiftBot, Scanner reader) {
        this.swiftBot = swiftBot;
        this.reader = reader;
    }

    public void calibrate() {
        while (!this.calibrated) {
            // Perform Calibration
            System.out.println("Robot will try to spin 360 degrees\nCurrent Duration: "+calibrationDuration);

            // Wait 1 second
            double start = System.currentTimeMillis();
            while (System.currentTimeMillis()-start<1000) {
            }  

            // Turn
            turn(360, 1);

            // Ask if a new duration should be made
            System.out.println("Would you like to enter a new duration?\n(y) Yes\n(n) No");
            String ans = this.reader.nextLine();
            if (ans.equals("y")) {
                System.out.println("Enter new duration");
    
                // Enter new value
                int answer = calibrationDuration;
                boolean valid = false;
                while (!valid) {
                    try {
                        answer = Integer.valueOf(this.reader.nextLine());
                        if (answer<0) {
                            throw new Exception();
                        }
                        valid = true;
                    } catch (Exception e) {
                        System.out.println("Invalid input: "+answer);
                        valid = false;
                    }
                }
                // Assign new calibration
                this.calibrationDuration = (int)(answer);
            } else if (ans.equals("n")) {
                this.calibrated = true;
            } else {
                System.out.println("Invalid input: "+ans);
                return;
            }
        }
    }

    public void forward(int lVel, int rVel, int duration) {
        // Moves the swiftbot
        this.swiftBot.move(lVel, rVel, duration);
    }

    public void turn(double degrees, int direction) {
        // Assign directions and speeds for turning
        int lVel = direction==0 ? -50 : 50;
        int rVel = direction==0 ? 50 : -50;
        double proportion = 360/degrees;
        int duration = calibrationDuration/(int)(proportion);  // Calulates duration in proportion to degrees

        // Turns the swiftbot
        this.swiftBot.move(lVel, rVel, duration);
    }
}
