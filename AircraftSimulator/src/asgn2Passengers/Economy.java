/**
 * 
 */
package asgn2Passengers;

/**
 * @author hogan
 *
 */
public class Economy extends Passenger {

	/**
	 * Economy Constructor (Partially Supplied)
	 * Passenger is created in New state, later given a Confirmed Economy Class reservation, 
	 * Queued, or Refused booking if waiting list is full. 
	 * 
	 * @param bookingTime <code>int</code> day of the original booking. 
	 * @param departureTime <code>int</code> day of the intended flight.  
	 * @throws PassengerException if invalid bookingTime or departureTime 
	 * @see asgnPassengers.Passenger#Passenger(int,int)
	 */
	public Economy(int bookingTime,int departureTime) throws PassengerException {
		//Stuff here
		super(bookingTime,departureTime);
		this.passID = "Y:" + this.passID;
		
	}
	
	@Override
	public String noSeatsMsg() {
		return "No seats available in Economy";
	}

	@Override
	public Passenger upgrade() {
		// Create new passenger to upgrade from Economy to Premium
		Passenger upgrade_Premium = new Premium();
		// Copy all information of Passenger from Economy to Premium
		this.copyPassengerState(upgrade_Premium);
		// Update the PassID to Premium
		upgrade_Premium.passID = "P(U)" + upgrade_Premium.passID;	
		return upgrade_Premium;
	}
}
