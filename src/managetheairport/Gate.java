package managetheairport;
import java.util.ArrayList;
/**
 *
 * @author kaichong
 */
public class Gate {
    
    // Class variables
    private boolean isAvailable;
    private int gateNumber;
    
    private Plane planeParkingArea;
    private ArrayList passengersAtGate;
    private long timePlaneIsDone;
    private int numOfPlanesGateServed;
    
    // Class Constructr
    public Gate(int g) {
        this.isAvailable=true;
        this.gateNumber=g;
        planeParkingArea=null;
        this.numOfPlanesGateServed=0;
        passengersAtGate=new ArrayList();
    }
    
    // Class methods
    // Returns gate number
    public int getGateNumber() {
        return gateNumber;
    }
    
    // Stores plane object into this gate
    public void sendPlaneToThisGate(Plane p) {
        this.planeParkingArea=p; // Store plane in variable
        this.isAvailable=false; // Set gate's availability to false
        this.timePlaneIsDone=System.currentTimeMillis()+900000; // Store time that plane should be done at this gate
        this.numOfPlanesGateServed+=1; // Increase the number of planes this gate served by one
        
        // Remove passengers from plane and store in gate
        for (int i=0; i<p.getFlightCapacity(); i++) {
            passengersAtGate.add(p.alightOnePassenger());
        }
    }
    
    // Returns the availability of the gate
    public boolean getAvailability() {
        return isAvailable;
    }
    
    // This method changes isAvailability to true if 15 minutes have passed and returns the availability.
    public boolean checkAndTweakAvailability() {
        if (System.currentTimeMillis()>timePlaneIsDone) { // If 15 minutes have passed
            this.isAvailable=true; // Set availability to true
            this.planeParkingArea=null; // Empty parking area
            return true; // Return true since 15 minutes is up
        }
        return false; // Otherwise, returns false since 15 minutes isn't up yet
    }
    
    // Returns the number of planes this gate served
    public int getNumberOfPlanesThisGateServed() {
        return numOfPlanesGateServed;
    }
    
    // Returns the number of passengers this gate served
    public int getNumberOfPassengersThisGateServed() {
        return passengersAtGate.size();
    }
    
}
