package managetheairport;

/**
 *
 * @author kaichong
 */
public class Passenger {
    
    // Class variable
    private long ticketNumber;
    
    // Class constructor
    public Passenger(long ticketNumber) {
        this.ticketNumber=ticketNumber; // store ticket number in variable
    }
    
    // Class method
    // Return ticket number of this passenger
    public long getTicketNumber() {
        return ticketNumber;
    }
    
}
