/**
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import asgn2Aircraft.A380;
import asgn2Aircraft.Aircraft;
import asgn2Aircraft.AircraftException;
import asgn2Passengers.Business;
import asgn2Passengers.First;
import asgn2Passengers.Passenger;
import asgn2Passengers.PassengerException;

/**
 * <code>First</code> to facilitate testing
 * @author Luke Ryan
 *
 */
public class FirstTests {

	private Passenger firstTest;
	
	@Before
	public final void beforeMethod() throws PassengerException{
		firstTest = new First(1400,1500);
	}
	
	@Test (expected = PassengerException.class)
	public final void testFirstInvalidBookingTime() throws PassengerException{
		firstTest = new First(-1400,1900);
	}
	
	@Test (expected = PassengerException.class)
	public final void testFirstInvalidDepatureTime() throws PassengerException{
		firstTest = new First(1400,-1900);
	}
	
	@Test (expected = PassengerException.class)
	public final void testFirstConflictingTimes() throws PassengerException{
		firstTest = new First(1900,1400);
	}
	
	@Test (expected = PassengerException.class)
	public final void testCancelSeatInvalidCancellationTime() throws PassengerException {
		firstTest.cancelSeat(-1400);
	}
	
	@Test (expected = PassengerException.class)
	public final void testCancelSeatInvalidDepartureTime() throws PassengerException {
		firstTest.cancelSeat(1600);
	}
	
	@Test (expected = PassengerException.class)
	public final void testCancelSeatPassengerNotConfirmed() throws PassengerException {
		//throws exception as passenger not confirmed
		firstTest.cancelSeat(0700);
	}
	
	@Test 
	public final void testCancelSeat() throws PassengerException {
		firstTest.confirmSeat(1400, 1500);
		firstTest.cancelSeat(0700);

		assertEquals(firstTest.wasConfirmed(), true);
	}

	@Test (expected = PassengerException.class)
	public final void testConfirmSeatInvalidConfirmationTime() throws PassengerException {
		firstTest.confirmSeat(-0700, 1500);
	}
	
	@Test (expected = PassengerException.class)
	public final void testConfirmSeatInvalidDepartureTime() throws PassengerException {
		firstTest.confirmSeat(1700,1500);
	}
	
	@Test (expected = PassengerException.class)
	public final void testConfirmSeatInvalidPassengerState() throws PassengerException {
		firstTest.flyPassenger(1500);
		firstTest.confirmSeat(1400,1500);
		//throws exception as passenger is already in confirmed state
	}
	
	@Test
	public final void testConfirmSeat() throws PassengerException {
		firstTest.confirmSeat(1400, 1500);
		
		assertEquals(firstTest.isConfirmed(), true);
		assertEquals(firstTest.isQueued(), false);
	}

	@Test (expected = PassengerException.class)
	public final void testFlyPassengerInvalidDepartureTime() throws PassengerException {
		firstTest.confirmSeat(1400, 1500);
		firstTest.flyPassenger(-0700);
	}

	@Test (expected = PassengerException.class)
	public final void testFlyPassengerInvalidConfirmation() throws PassengerException {
		firstTest.flyPassenger(1500);
		//throws exception as passenger is not yet confirmed
	}

	@Test
	public final void testFlyPassenger() throws PassengerException {
		firstTest.confirmSeat(1400, 1500);
		firstTest.flyPassenger(1500);
		
		assertTrue(firstTest.isFlown());
		assertTrue(firstTest.wasConfirmed());
	}

	@Test (expected = PassengerException.class)
	public final void testQueuePassengerInvalidQueueTime() throws PassengerException {
		firstTest.queuePassenger(-0700, 1500);
	}
	
	@Test (expected = PassengerException.class)
	public final void testQueuePassengerInvalidDepartureTime() throws PassengerException {
		firstTest.queuePassenger(1600, 1500);
	}
	
	@Test (expected = PassengerException.class)
	public final void testQueuePassengerPassengerAlreadyConfirmed() throws PassengerException {
		firstTest.confirmSeat(1400, 1500);
		firstTest.queuePassenger(1400, 1500);
	}
	
	@Test (expected = PassengerException.class)
	public final void testQueuePassengerPassengerAlreadyFlown() throws PassengerException {
		firstTest.flyPassenger(1500);
		firstTest.queuePassenger(1400, 1500);
	}
	
	@Test (expected = PassengerException.class)
	public final void testQueuePassengerPassengerRefused() throws PassengerException {
		firstTest.refusePassenger(1400);
		firstTest.queuePassenger(1400, 1500);
	}
	
	@Test
	public final void testQueuePassenger() throws PassengerException {

		firstTest.queuePassenger(1400, 1500);
		
		assertTrue(firstTest.isQueued());
	}

	@Test (expected = PassengerException.class)
	public final void testRefusePassengerInvalidRefusalTime() throws PassengerException {
		firstTest.refusePassenger(-0700);
	}
	
	@Test (expected = PassengerException.class)
	public final void testRefusePassengerRefusalTimeLessThanBookingTime() throws PassengerException {
		firstTest.refusePassenger(1300);
		//throw exception if refusal time is less than booking time 
	}
	
	@Test (expected = PassengerException.class)
	public final void testRefusePassengerPassengerConfirmed() throws PassengerException {
		firstTest.confirmSeat(1400, 1500);
		firstTest.refusePassenger(1450);
	}
	
	@Test
	public final void testRefusePassenger() throws PassengerException {
		firstTest.refusePassenger(1450);
		
		assertTrue(firstTest.isRefused());
	}


	@Test
	public final void testWasConfirmed() throws PassengerException {
		firstTest.confirmSeat(1400, 1500);
		firstTest.flyPassenger(1500);
		
		assertTrue(firstTest.wasConfirmed());
	}


	@Test
	public final void testWasQueued() throws PassengerException {
		firstTest.queuePassenger(1450, 1500);
		firstTest.confirmSeat(1400, 1500);
		
		assertTrue(firstTest.wasQueued());
	}

	@Test
	public final void testCopyPassengerState() throws PassengerException {
		String Pass = firstTest.getPassID();
		
		assertEquals(Pass, "F:2");
	}
	
	@Test
	public final void testUpgrade() throws PassengerException {
		firstTest = new Business(1400,1900);
		String test = firstTest.getPassID();
		Passenger upgraded = firstTest.upgrade();
		String test2 = upgraded.getPassID();
		
		assertNotEquals(test,test2);
		//Test passes if passenger has been upgraded (different passID)
	}
	@Test
	public final void testUpgradeStatus() throws PassengerException {
		firstTest = new Business(1400,1900);
		Passenger upgraded = firstTest.upgrade();
		
		assertTrue(firstTest.isConfirmed()==upgraded.isConfirmed() && firstTest.isNew() == upgraded.isNew() &&
				firstTest.isFlown()==upgraded.isFlown() && firstTest.isQueued()==upgraded.isQueued() && firstTest.isRefused()==upgraded.isRefused());
		//Test passes if passenger has been upgraded (different passID)
	}
	
	@Test
	public final void testUpgradeConfirmationTime() throws PassengerException {
		firstTest = new Business(1400,1900);
		Passenger upgraded = firstTest.upgrade();
		
		assertTrue(firstTest.getConfirmationTime()==upgraded.getConfirmationTime());
		//Test passes if passenger has been upgraded (different passID)
	}
	
	@Test
	public final void testUpgradeBookingTime() throws PassengerException {
		firstTest = new Business(1400,1900);
		Passenger upgraded = firstTest.upgrade();

		assertTrue(firstTest.getBookingTime()==upgraded.getBookingTime());
		//Test passes if passenger has been upgraded (different passID)
	}
	
	@Test
	public final void testUpgradeDepartureTime() throws PassengerException {
		firstTest = new Business(1400,1900);
		Passenger upgraded = firstTest.upgrade();
	
		assertTrue(firstTest.getDepartureTime()==upgraded.getDepartureTime());
		//Test passes if passenger has been upgraded (different passID)
	}
	
	@Test
	public final void testUpgradeEnterQueueTime() throws PassengerException {
		firstTest = new Business(1400,1900);
		Passenger upgraded = firstTest.upgrade();
		
		assertTrue(firstTest.getEnterQueueTime()==upgraded.getEnterQueueTime());
				//Test passes if passenger has been upgraded (different passID)
		
	}
	
	@Test
	public final void testUpgradeExitQueueTime() throws PassengerException {
		firstTest = new Business(1400,1900);
		Passenger upgraded = firstTest.upgrade();
		
		assertTrue(firstTest.getExitQueueTime()==upgraded.getExitQueueTime());
		//Test passes if passenger has been upgraded (different passID)
	}
	
	@Test
	public final void testNumberOfPassengersAfterUpdate() throws PassengerException, AircraftException {
		Passenger premiumTest;
		Passenger businessTest;
		Passenger economyTest;
		
		businessTest = new Business(1400,1900);
		premiumTest = new Business(1400,1900);
		economyTest = new Business(1400,1900);

		Aircraft testA380 = new A380("QF38", 1900);
		testA380.confirmBooking(premiumTest, 1800);
		testA380.confirmBooking(businessTest, 1800);
		testA380.confirmBooking(economyTest, 1800);
		
		testA380.upgradeBookings();
		
		assertEquals(testA380.getNumFirst(),testA380.getNumPassengers());
		
	}

}
