package managetheairport;

/**
 *
 * @author KaiChong
 */
public class HighWindWarningSystem {
    
    // Class variable
    private boolean highWindStatus;
    
    // Class constructor
    public HighWindWarningSystem() {
        this.highWindStatus=false;
    }
    
    // Class methods
    // Return status of high wind
    public boolean getWindStatus() {
        return highWindStatus;
    }
    
    // Check for high wind
    public void checkForHighWind() {
        if (this.generateNumWithRange(1, 2)==1) // Randomly generate 1 or 2
            this.highWindStatus=true; // there's high wind if randomly generated number is 1
        else
            this.highWindStatus=false; // otherwise, no high wind
    }
    
    // Random number generator that is accessible only within this class
    private int generateNumWithRange(int min, int max) {
        int range = (max-min)+1;
        return (int)(Math.random()*range)+min;
    }
    
}
