package subprocesses;

public class Timer {
    public boolean running;
    public double timer;
    public double startTime;
    public double finalTime;


    public Timer() {
        // Properties
        this.running = false;
        this.timer = 0;
        this.startTime = 0;
        this.finalTime = 0;
    }
    
    public void start() {
        // Resets the current timer
        this.running = true;
        this.startTime = System.currentTimeMillis();
        this.timer = this.startTime;
    }

    public void end() {
        // End timer
        this.running = false;
        this.finalTime = this.getElapsedTime();
    }

    public double getElapsedTime() {
        // Returns the total duration of the timer as a double
        return (this.timer - this.startTime);
    }

    public void update() {
        // Only updates the current timer if running
        if (this.running) {
            this.timer = System.currentTimeMillis();
        }
    }
}
