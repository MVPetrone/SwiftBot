package subprocesses;
import swiftbot.*;

public class Lights {
    public static SwiftBotAPI swiftBot;

    // Properties
    private boolean flashing;
    private boolean flashStateOn;
    private double flashTimer;
    private double flashDuration;
    private double flashInterval;
    private int[] flashColour;
    private int[] flashColour2;

    // Colour constants
    final public static int[] EMPTY = new int[]{0, 0, 0};
	final public static int[] RED = new int[]{255, 0, 0};
	final public static int[] BLUE = new int[]{0, 255, 0};
	final public static int[] GREEN = new int[]{0, 0, 255};
	final public static int[] WHITE = new int[]{255, 255, 255};
	final public static int[] YELLOW = new int[]{255, 0, 200};
	final public static int[] AMBER = new int[]{150, 0, 120};
	final public static int[] CYAN = new int[]{0, 150, 0};

    public Lights(SwiftBotAPI swiftBot) {
        this.swiftBot = swiftBot;
    }

    public void on(int[] colour) {
        this.swiftBot.fillUnderlights(colour);
    }

    public void off() {
        this.swiftBot.fillUnderlights(EMPTY);
    }

    public void startFlash(double duration, double interval, int[] colour, int[] colour2) {
        // Set flash properties
        this.flashDuration = duration; // Duration in milliseconds (light)
        this.flashInterval = interval; // Interval in milliseconds (no light)
        this.flashColour = colour; // Colour being flashed
        this.flashColour2 = colour2; // Colour2 being flashed

        // Start timer
        this.flashTimer = System.currentTimeMillis();

        // Set Flag
        this.flashStateOn = true;
        this.flashing = true;
        this.updateFlash();

    }

    public void stopFlash() {
        // Set Flag
        this.flashing = false;
        this.updateFlash();
    }

    public void updateFlash() {
        if (this.flashing) {
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
            if (this.flashing) {
                if (this.flashStateOn) {
                    on(this.flashColour);
                } else {
                    on(this.flashColour2);
                }
            } else {
                off();
            }
        }
    }

    public void exit() {
        // Terminate all Lights.java functions
        stopFlash();
        off();
        updateFlash();
    }
}
