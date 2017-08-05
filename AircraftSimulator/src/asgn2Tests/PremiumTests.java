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
 * <code>Premium</code> to facilitate testing
 * @author Luke Ryan
 *
 */
public class PremiumTests {

	private Passenger premiumTest;

	/**
	 * Test method for {@link asgn2Passengers.Business#upgrade()}.
	 * @throws PassengerException 
	 */
	@Test
	public final void testUpgrade() throws PassengerException {
		premiumTest = new Business(1400,1900);
		String test = premiumTest.getPassID();
		Passenger upgraded = premiumTest.upgrade();
		String test2 = upgraded.getPassID();
		
		assertNotEquals(test,test2);
		//Test passes if passenger has been upgraded (different passID)
	}


}
