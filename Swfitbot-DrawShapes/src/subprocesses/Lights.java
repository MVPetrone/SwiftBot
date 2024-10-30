package subprocesses;

import swiftbot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Lights {
    private static SwiftBotAPI sb;

    private boolean flashing;
    private boolean flashStateOn;
    private double flashTimer;
    private double flashDuration;
    private double flashInterval;
    private int[] flashColour;

    final public static int[] EMPTY = new int[]{0, 0, 0};
	final public static int[] RED = new int[]{255, 0, 0};
	final public static int[] BLUE = new int[]{0, 255, 0};
	final public static int[] GREEN = new int[]{0, 0, 255};
	final public static int[] YELLOW = new int[]{255, 0, 200};
	final public static int[] AMBER = new int[]{150, 0, 120};
	final public static int[] CYAN = new int[]{0, 150, 0};

    public Lights(SwiftBotAPI sb) {
        this.sb = sb;
    }

    public void on(int[] colour) {
        this.sb.fillUnderlights(colour);
    }

    public void off() {
        this.sb.fillUnderlights(EMPTY);
    }

    public void startFlash(double duration, double interval, int[] colour) {
        this.flashDuration = duration; // Duration in milliseconds (light)
        this.flashInterval = interval; // Interval in milliseconds (no light)
        this.flashColour = colour; // Colour being flashed

        // Start timer
        this.flashTimer = System.currentTimeMillis();

        // Set Flag
        this.flashing = true;

    }

    public void stopFlash() {
        // Set Flag
        this.flashing = false;
    }

    public void updateFlash() {
        if (this.flashing = true) {
            // Check status

            // Select correct duration
            double duration;
            if (this.flashStateOn) {
                duration = this.flashDuration;
            } else {
                duration = this.flashInterval;
            }
            // Toggle if time has surpassed duration
            if (System.currentTimeMillis()-this.flashTimer>duration) {
                this.flashTimer = System.currentTimeMillis();
                this.flashStateOn = !this.flashStateOn; // Toggle flash state
            }

            // Update flash
            if (this.flashStateOn) {
                on(this.flashColour);
            } else {
                off();
            }
        }
    }

    public double getFlashTimer() {
        return this.flashTimer;
    }
}
