package managetheairport;

/**
 *
 * @author kaichong
 */
public class StatisticsSystem {
    
    /**
     * Variables
     */
    
    // Portion 1
    private int numOfPlanesLanded; // Also the number of planes at landing queue by the end of program
    private int numOfLargePlanesLanded;
    
    private int numOfPlanesTookOff; // Also the number of planes at takeoff queue by the end of program
    private int numOfSmallPlanesTookOff;
    
    // Portion 2
    private long totalMillisecondsAtLandingQueue;
    private long totalMillisecondsAtTakeOffQueue;
    
    // Portion 3
    private int numOfPlanesThatMissedTargetTime;
    
    /**
     * Methods
     */
    
    public StatisticsSystem() {
        this.numOfPlanesLanded=0;
        this.numOfLargePlanesLanded=0;
        
        this.numOfPlanesTookOff=0;
        this.numOfSmallPlanesTookOff=0;
        
        this.totalMillisecondsAtLandingQueue=0;
        this.totalMillisecondsAtTakeOffQueue=0;
        
        this.numOfPlanesThatMissedTargetTime=0;
    }
    
    // Increase stats
    public void increasePlanesLandedByOne() {
        numOfPlanesLanded+=1;
    }
    
    public void increaseLargePlanesLandedByOne() {
        numOfLargePlanesLanded+=1;
    }
    
    public void increasePlanesTookOffByOne() {
        numOfPlanesTookOff+=1;
    }
    
    public void increaseSmallPlanesTookOffByOne() {
        numOfSmallPlanesTookOff+=1;
    }
    
    public void increasePlanesThatMissedTargetTimeByOne() {
        numOfPlanesThatMissedTargetTime+=1;
    }
    
    // Get stats
    public int getNumOfPlanesLanded() {
        return this.numOfPlanesLanded;
    }
    
    public int getNumOfPlanesTookOff() {
        return this.numOfPlanesTookOff;
    }
    
    public int getNumOfPlanesThatMissedTargetTime() {
        return this.numOfPlanesThatMissedTargetTime;
    }
    
    // Add to methods
    public void addToTotalMillisecondsForLandingQueue(long milli) {
        this.totalMillisecondsAtLandingQueue+=milli;
    }
    
    public void addToTotalMillisecondsForTakeOffQueue(long milli) {
        this.totalMillisecondsAtTakeOffQueue+=milli;
    }
    
    // Get percentage
    public double getLargePlanesOverPlanesLandedPercentage() {
        return ((double)this.numOfLargePlanesLanded/(double)this.numOfPlanesLanded)*100.0;
    }
    
    public double getSmallPlanesOverPlanesTookOffPercentage() {
        return ((double)this.numOfSmallPlanesTookOff/(double)this.numOfPlanesTookOff)*100.0;
    }
    
    // Get average time
    public long getAverageTimeInMillisecondsAtLandingQueue() {
        return (this.totalMillisecondsAtLandingQueue/this.numOfPlanesLanded);
    }
    public long getAverageTimeInMillisecondsAtTakeOffQueue() {
        return (this.totalMillisecondsAtTakeOffQueue/this.numOfPlanesTookOff);
    }
    
    // Generator
    public int generateNumWithRange(int min, int max) {
        return (int)(Math.random()*((max-min)+1))+min;
    }
    
    // Converter
    public long convertMillisecondsToMinutes(long milli) {
        return milli/60000;
    }
    
    public long convertMinutesToMilliseconds(long minutes) {
        return minutes*60000;
    }
    
    // Checker
    public boolean rangeInputChecker(int input, int min, int max) {
        if (input<min || input>max) {
            return false;
        } else {
            return true;
        }
    }
}
