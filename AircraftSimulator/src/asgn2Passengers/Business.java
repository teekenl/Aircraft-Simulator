/**
 * 
 */
package asgn2Passengers;

/**
 * @author hogan
 *
 */
public class Business extends Passenger {

	/**
	 * Business Constructor (Partially Supplied) 
	 * Passenger is created in New state, later given a Confirmed Business Class reservation, 
	 * Queued, or Refused booking if waiting list is full. 
	 * 
	 * @param bookingTime <code>int</code> day of the original booking. 
	 * @param departureTime <code>int</code> day of the intended flight.  
	 * @throws PassengerException if invalid bookingTime or departureTime 
	 * @see asgnPassengers.Passenger#Passenger(int,int)
	 */
	public Business(int bookingTime, int departureTime) throws PassengerException {
		super(bookingTime,departureTime);
		this.passID = "J:" + this.passID;
	}
	
	/**
	 * Simple constructor to support {@link asgn2Passengers.Passenger#upgrade()} in other subclasses
	 */
	protected Business() {
		
	}
	
	@Override
	public String noSeatsMsg() {
		return "No seats available in Business";
	}

	@Override
	public Passenger upgrade() {
		// Create new passenger to upgrade from Business to First
		Passenger upgrade_First = new Premium();
		// Copy all information of Passenger from Business to First
		this.copyPassengerState(upgrade_First);
		// Update the PassID to First
		upgrade_First.passID = "F(U)" + upgrade_First.passID;			
		return upgrade_First;
	}
}
