/**
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import asgn2Passengers.Business;
import asgn2Passengers.Passenger;
import asgn2Passengers.PassengerException;

/**
 * <code>Economy</code> to facilitate testing
 * @author Luke Ryan
 *
 */
public class EconomyTests {

	private Passenger economyTest;

	/**
	 * Test method for {@link asgn2Passengers.Business#upgrade()}.
	 * @throws PassengerException 
	 */
	@Test
	public final void testUpgrade() throws PassengerException {
		economyTest = new Business(1400,1900);
		String test = economyTest.getPassID();
		Passenger upgraded = economyTest.upgrade();
		String test2 = upgraded.getPassID();
		
		assertNotEquals(test,test2);
		//Test passes if passenger has been upgraded (different passID)
	}


}
