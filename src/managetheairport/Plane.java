package managetheairport;

/**
 *
 * @author kaichong
 */
public class Plane {
    
    // Class variables
    private String flightName;
    private String flightNum;
    private int flightCapacity;
    private boolean isPlaneArriving;
    
    private int timeOfArrival;
    private int timeOfDeparture;
    
    // To store Passenger objects in this plane
    private Queue passengerCompartment;
    
    // Store time before and after queue to calculate time taken in queue
    private long timeBeforeQueueInMilliseconds;
    private long timeAfterQueueInMilliseconds;
    
    // Class constructor
    public Plane(String flightName, String flightNum, int flightCapacity, boolean isPlaneArriving) {
        this.flightName=flightName;
        this.flightNum=flightNum;
        this.flightCapacity=flightCapacity;
        this.isPlaneArriving=isPlaneArriving;
        passengerCompartment=new Queue(flightCapacity);
    }
    
    // Class Methods
    // Plane GET methods
    public String getCompleteName() {
        String completeName=flightName+flightNum;
        return completeName;
    }
    
    public String getFlightNumber() {
        return flightNum;
    }
    
    public int getFlightCapacity() {
        return flightCapacity;
    }
    
    public int getNumberOfPassengersOnboard() {
        return passengerCompartment.size();
    }
    
    public boolean isPlaneArriving() {
        return isPlaneArriving;
    }
    
    // Plane SET methods
    public void setTimeOfArrival(int time) {
        this.timeOfArrival=time;
    }
    
    public void setTimeOfDeparture(int time) {
        this.timeOfDeparture=time;
    }
    
    // ADD TO method
    public void boardOnePassenger(Passenger p) {
        if (passengerCompartment.size()<flightCapacity) {
            passengerCompartment.enqueue(p);
        }
    }
    
    // REMOVE FROM method
    public Passenger alightOnePassenger() {
        return (Passenger)passengerCompartment.dequeue();
    }
    
    // Store time before queue
    public void storeTimeBeforeQueue() {
        this.timeBeforeQueueInMilliseconds=System.currentTimeMillis();
    }
    // Store time after queue
    public void storeTimeAfterQueue() {
        this.timeAfterQueueInMilliseconds=System.currentTimeMillis();
    }
    
    // Get time taken by plane in milliseconds
    public long getTimeTakenInMilliseconds() {
        long timeTaken=timeAfterQueueInMilliseconds-timeBeforeQueueInMilliseconds;
        return timeTaken;
    }
    
}
