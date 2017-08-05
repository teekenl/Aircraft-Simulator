/**
 * 
 */
package asgn2Passengers;

/**
 * @author hogan
 *
 */
public class Premium extends Passenger {

	/**
	 * Premium Constructor (Partially Supplied)
	 * Passenger is created in New state, later given a Confirmed Premium Class reservation, 
	 * Queued, or Refused booking if waiting list is full. 
	 * 
	 * @param bookingTime <code>int</code> day of the original booking. 
	 * @param departureTime <code>int</code> day of the intended flight.  
	 * @throws PassengerException if invalid bookingTime or departureTime 
	 * @see asgnPassengers.Passenger#Passenger(int,int)
	 */
	public Premium(int bookingTime,int departureTime) throws PassengerException {
		//Stuff here
		super(bookingTime,departureTime);
		this.passID = "P:" + this.passID;
	}
	
	/**
	 * Simple constructor to support {@link asgn2Passengers.Passenger#upgrade()} in other subclasses
	 */
	protected Premium() {
		
	}
	
	
	@Override
	public String noSeatsMsg() {
		return "No seats available in Premium";
	}

	@Override
	public Passenger upgrade() {
		// Create new passenger to upgrade from Premium to Business
		Passenger upgrade_Business = new Premium();
		// Copy all information of Passenger from Premium to Business
		this.copyPassengerState(upgrade_Business);
		// Update the PassID to Business
		upgrade_Business.passID = "J(U)" + upgrade_Business.passID;	
		return upgrade_Business;
	}
	
	
}
