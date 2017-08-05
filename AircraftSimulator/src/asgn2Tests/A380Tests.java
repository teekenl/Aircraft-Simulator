/**
 * The test cases of A380 Classes for Assignment 2 Semester 2016
 */
package asgn2Tests;

import static org.junit.Assert.*;


import java.util.List;

import org.junit.Before;
import org.junit.Test;

import asgn2Aircraft.A380;
import asgn2Aircraft.Aircraft;
import asgn2Aircraft.AircraftException;
import asgn2Passengers.Business;
import asgn2Passengers.Economy;
import asgn2Passengers.First;
import asgn2Passengers.Passenger;
import asgn2Passengers.PassengerException;
import asgn2Passengers.Premium;


/**
 * <code>A380</code> to facilitate testing
 * @author TeeKen Lau
 *
 */
public class A380Tests {

	private Aircraft A380Test;
	List<Passenger> seats;
	
	/**
     * Test method for setup constructor
	 */
	@Before 
	public void setup() throws AircraftException{
		A380Test = new A380("QF38",1900);
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#A380(String, int, int, int, int, int)}}.
	 */
	@Test(expected = AircraftException.class)
	public void testA380NullFlightCode() throws AircraftException {
		A380Test = new A380(null, 1900);
		
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#A380(String, int, int, int, int, int)}}.
	 */
	@Test(expected = AircraftException.class)
	public void testA380NegativeDepartureTime() throws AircraftException {
		A380Test = new A380("QF38", -1900);
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#flightEmpty()}}.
	 */
	@Test
	public void testA380FlightEmptyWithoutBooking() throws AircraftException {
		A380Test = new A380("QF38", 1800);
		
		assertTrue(A380Test.flightEmpty());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#flightEmpty()}}.
	 */
	@Test
	public void testA380FlightEmptyWithCancelBooking() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
		
		A380Test.confirmBooking(pass_First, 1400);
		A380Test.confirmBooking(pass_Business, 1500);
		
		A380Test.cancelBooking(pass_First, 1400);
		A380Test.cancelBooking(pass_Business, 1500);
		
		assertTrue(A380Test.flightEmpty());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#flightEmpty()}}.
	 */
	@Test
	public void testA380FlightNotEmptyWithCancelBooking() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
		
		A380Test.confirmBooking(pass_First, 1400);
		A380Test.confirmBooking(pass_Business, 1800);
		A380Test.cancelBooking(pass_First, 1400);
	
		assertFalse(A380Test.flightEmpty());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#flightEmpty()}}.
	 */
	@Test 
	public void testA380FlightNotEmptyWithConfirmBooking() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1200, 1400);
		Passenger pass_Business = new Business(1200, 1800);
		
		A380Test.confirmBooking(pass_First, 1700);
		A380Test.confirmBooking(pass_Business, 1800);
		
		assertFalse(A380Test.flightEmpty());
	}

	/**
	 * Test method for {@link asgn2Aircraft.A380#getPassengers()}}.
	 */
	@Test
	public void testA380CopiedPassenger() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1700, 2200);
		Passenger pass_Business = new Business(2000, 2100);
		
		
		A380Test.confirmBooking(pass_First, 1700);
		A380Test.confirmBooking(pass_Business, 1600);
		
		assertTrue(1700==A380Test.getPassengers().get(0).getConfirmationTime()&&1600==A380Test.getPassengers().get(1).getConfirmationTime());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#flightFull()}}.
	 */
	@Test 
	public void testA380FlightFullWithoutBooking() throws AircraftException {
		A380Test = new A380("QF38", 1800, 0, 0, 0, 0);
		
		assertTrue(A380Test.flightFull());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#flightFull()}}.
	 */
	@Test 
	public void testA380FlightNotFullWithoutBooking() throws AircraftException, PassengerException {
	
		assertFalse(A380Test.flightFull());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#confirmBooking(Passenger, int)}}.
	 */
	@Test 
	public void testA380FlightNotFullWithConfirmBooking() throws AircraftException, PassengerException {
		A380Test = new A380("QF38", 1900, 4, 2, 0, 0);
		Passenger pass_First = new First(1700, 2200);
		Passenger pass_First2 = new First(2000, 2100);
		Passenger pass_Business = new Business(1800, 2000);
		
		A380Test.confirmBooking(pass_First, 1600);
		A380Test.confirmBooking(pass_First2, 1700);
		A380Test.confirmBooking(pass_Business,1800);
		
		assertFalse(A380Test.flightFull());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#confirmBooking(Passenger, int)}}.
	 */
	@Test 
	public void testA380FlightFullWithConfirmBooking() throws AircraftException, PassengerException {
		A380Test = new A380("QF38", 1900, 4, 2, 0, 0);
		Passenger pass_First = new First(1700, 2200);
		Passenger pass_First2 = new First(2000, 2100);
		Passenger pass_Business = new Business(1800, 2000);
		
		A380Test.confirmBooking(pass_First, 1600);
		A380Test.confirmBooking(pass_First2, 1700);
		A380Test.confirmBooking(pass_Business,1800);
		
		assertFalse(A380Test.flightFull());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#seatsAvailable(Passenger)}}.
	 */
	@Test 
	public void testA380SeatAvailable() throws AircraftException, PassengerException {
		A380Test = new A380("QF38", 1800, 1, 1, 1, 1);
		Passenger pass_First = new First(1500, 1600);
		Passenger pass_First2 = new First(1300, 1500);
		Passenger pass_Business = new Business(1300,1500);
		Passenger pass_Business2 = new Business(1100, 1400);
		
		A380Test.confirmBooking(pass_First, 1600);
		A380Test.confirmBooking(pass_Business, 1700);
	
		assertFalse(A380Test.seatsAvailable(pass_Business2) || A380Test.seatsAvailable(pass_First2));		
		
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#cancelBooking(Passenger, int)}}.
	 */
	@Test 
	public void testA380FlightNotFullWithCancelBooking() throws AircraftException, PassengerException {
		A380Test = new A380("QF38", 1900, 1, 0, 0, 0);
		Passenger pass_First = new First(1700, 2200);

		
		A380Test.confirmBooking(pass_First, 1600);
		A380Test.cancelBooking(pass_First, 1700);
		
		assertFalse(A380Test.flightFull());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#seatsAvailable(Passenger)}}.
	 */
	@Test(expected = AircraftException.class)
	public void testA380ConfirmBookingNoSeatAvailable() throws AircraftException, PassengerException {
		A380Test = new A380("QF38", 1800, 1, 0, 0, 0);
		Passenger pass_First = new First(1200, 1400);
		Passenger pass_First2 = new First(1400, 1500);
	
		A380Test.confirmBooking(pass_First, 1700);
		A380Test.confirmBooking(pass_First2, 1600);
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#seatsAvailable(Passenger)}}.
	 */
	@Test
	public void testA380CancelBookingSeatAvailable() throws AircraftException, PassengerException {
		A380Test = new A380("QF38", 1800, 1, 0, 0, 0);
		Passenger pass_First = new First(1200, 1400);
		
		A380Test.confirmBooking(pass_First, 1700);
		A380Test.cancelBooking(pass_First, 1800);
		
		assertTrue(A380Test.seatsAvailable(pass_First));
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#flightEmpty()}}.
	 */
	@Test(expected = AircraftException.class)
	public void testA380CancelBookingNoHasPassenger() throws AircraftException, PassengerException {
		A380Test = new A380("QF38", 1800, 1, 0, 0, 0);
		Passenger pass_First = new First(1200, 1400);

		A380Test.cancelBooking(pass_First, 1800);
	
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#hasPassenger(Passenger)}}.
	 */
	@Test
	public void testA380HasPassengers() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);

		A380Test.confirmBooking(pass_First,1700);
		A380Test.confirmBooking(pass_Business, 1400);
		
		assertTrue(A380Test.hasPassenger(pass_First) && 
				A380Test.hasPassenger(pass_Business));
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#hasPassenger(Passenger)}}.
	 */
	@Test
	public void testA380NotHasPassengers() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
		
		A380Test.confirmBooking(pass_Business, 1400);
		
		assertFalse(A380Test.hasPassenger(pass_First));
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#confirmBooking(Passenger, int)}}.
	 */
	@Test
	public void testA380ConfirmBooking() throws AircraftException, PassengerException {
		Passenger pass_Business = new Business(1200, 1800);
		Passenger pass_Economy = new Economy(1700, 1900);
		Passenger pass_Premium = new Premium(1100, 1500);
		Passenger pass_First = new First(1200, 1400);
		
	
		A380Test.confirmBooking(pass_First, 1300);
		A380Test.confirmBooking(pass_Business, 1800);
		A380Test.confirmBooking(pass_Premium, 1300);
		A380Test.confirmBooking(pass_Economy, 1800);

		assertTrue(!A380Test.flightEmpty() && A380Test.getNumFirst()==1 && 
				A380Test.getNumBusiness()==1 && A380Test.getNumEconomy()==1 && A380Test.getNumPremium()==1);
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#confirmBooking(Passenger, int)}}.
	 */
	@Test
	public void testA380NotConfirmBooking() throws AircraftException, PassengerException {

		assertTrue(A380Test.flightEmpty() && A380Test.getNumFirst()==0 && 
				A380Test.getNumBusiness()==0 && A380Test.getNumEconomy()==0 && A380Test.getNumPremium()==0);
	}

	
	/**
	 * Test method for {@link asgn2Aircraft.A380#cancelBooking(Passenger, int)}}.
	 */
	@Test(expected = AircraftException.class)
	public void testA380CancelBookingException() throws PassengerException, AircraftException {
		Passenger pass_First = new First(1100, 1300);
	
		A380Test.cancelBooking(pass_First, 1900);
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#cancelBooking(Passenger, int)}}.
	 */
	@Test 
	public void testA380CancelBooking() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
		Passenger pass_Premium = new Premium(1400, 1600);
		Passenger pass_Economy = new Economy(1300, 1700);
	
		A380Test.confirmBooking(pass_First, 1400);
		A380Test.confirmBooking(pass_Business, 1500);
		A380Test.confirmBooking(pass_Premium, 1600);
		A380Test.confirmBooking(pass_Economy, 1700);
		
		A380Test.cancelBooking(pass_First, 1400);
		A380Test.cancelBooking(pass_Business, 1500);

		assertTrue(!A380Test.flightEmpty() && A380Test.getNumFirst()==0 
				&& A380Test.getNumBusiness()==0 && A380Test.getNumPremium()!=0);
		
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#cancelBooking(Passenger, int)}}.
	 */
	@Test (expected = AircraftException.class)
	public void testA380CancelNotHasPassenger() throws PassengerException, AircraftException {
		Passenger pass_First = new First(1100, 1300);
		A380Test.cancelBooking(pass_First, 1700);
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#seatsAvailable(Passenger)}}.
	 */
	@Test 
	public void testConfirmBookingSeatAvailable() throws PassengerException, AircraftException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1000, 1400);
		A380Test.confirmBooking(pass_First, 1700);
		
		assertTrue(A380Test.seatsAvailable(pass_Business));
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#cancelBooking(Passenger, int)}}.
	 */
	@Test 
	public void testA380NotCancelBooking() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
		Passenger pass_Premium = new Premium(1400, 1600);
		Passenger pass_Economy = new Economy(1300, 1700);
	
		A380Test.confirmBooking(pass_First, 1400);
		A380Test.confirmBooking(pass_Business, 1500);
		A380Test.confirmBooking(pass_Premium, 1600);
		A380Test.confirmBooking(pass_Economy, 1700);
		

		assertTrue(!A380Test.flightEmpty() && A380Test.getNumFirst()==1 
				&& A380Test.getNumBusiness()==1 && A380Test.getNumPremium()==1);
		
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#confirmBooking(Passenger, int)}}.
	 */
	@Test 
	public void testA380SeatAvailableFullFirst() throws PassengerException, AircraftException {
		try{
			A380Test = new A380("QF38", 1900, 0, 0, 0, 0);
			Passenger pass_First = new First(1100, 1300);
			Passenger pass_First2 = new First(1200, 1400);
			
			A380Test.confirmBooking(pass_First, 1600);
			A380Test.confirmBooking(pass_First2, 1600);
		} catch (AircraftException e) {
			assertEquals(e.getMessage().substring(20), "No seats available in First");
		} finally {
		} 
		
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#confirmBooking(Passenger, int)}}.
	 */
	@Test 
	public void testA380SeatAvailableFullBusiness() throws PassengerException, AircraftException {
		try{
			A380Test = new A380("QF38", 1900, 0, 1, 0, 0);
			Passenger pass_Business = new Business(1100, 1300);
			Passenger pass_Business2 = new Business(1200, 1400);
		
			A380Test.confirmBooking(pass_Business, 1600);
			A380Test.confirmBooking(pass_Business2, 1700);
		} catch (AircraftException e) {
			assertEquals(e.getMessage().substring(20), "No seats available in Business");
		} 
		
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#confirmBooking(Passenger, int)}}.
	 */
	@Test 
	public void testA380SeatAvailableFullPremium() throws PassengerException, AircraftException {
		try{
			A380Test = new A380("QF38", 1900, 0, 0, 1, 0);
			Passenger pass_Premium = new Premium(1100, 1300);
			Passenger pass_Premium2 = new Premium(1000, 1200);
		
			A380Test.confirmBooking(pass_Premium, 1600);
			A380Test.confirmBooking(pass_Premium2, 1700);
		} catch (AircraftException e) {
			assertEquals(e.getMessage().substring(20), "No seats available in Premium");
		} 
		
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#confirmBooking(Passenger, int)}}.
	 */
	@Test 
	public void testA380SeatAvailableFullEconomy() throws PassengerException, AircraftException {
		try{
			A380Test = new A380("QF38", 1900, 0, 0, 0, 1);
			Passenger pass_Economy = new Economy(1100, 1300);
			Passenger pass_Economy2 = new Economy(1100, 1300);
			
			A380Test.confirmBooking(pass_Economy, 1600);
			A380Test.confirmBooking(pass_Economy2, 1800);
		} catch (AircraftException e) {
			assertEquals(e.getMessage().substring(20), "No seats available in Economy");
		} 
		
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#getBookings()}}.
	 */
	@Test
	public void testA380ConfirmedBookingStatus() throws AircraftException, PassengerException  {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
		
		A380Test.confirmBooking(pass_First, 1600);
		A380Test.confirmBooking(pass_Business, 1500);
		
		assertTrue(A380Test.getBookings().getNumBusiness()==1 && A380Test.getBookings().getNumEconomy()==0 
				&& A380Test.getBookings().getNumPremium()==0 && A380Test.getBookings().getNumFirst()==1 
					&& A380Test.getBookings().getTotal()==2 && A380Test.getBookings().getAvailable()==482);
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#getBookings()}}.
	 */
	@Test
	public void testA380CancelBookingStatus() throws AircraftException, PassengerException  {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
		
		A380Test.confirmBooking(pass_First, 1600);
		A380Test.confirmBooking(pass_Business, 1500);
		A380Test.cancelBooking(pass_First, 1700);
		assertTrue(A380Test.getBookings().getNumBusiness()==1 && A380Test.getBookings().getNumEconomy()==0 
				&& A380Test.getBookings().getNumPremium()==0 && A380Test.getBookings().getNumFirst()==0 
					&& A380Test.getBookings().getTotal()==1 && A380Test.getBookings().getAvailable()==483);
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#flyPassengers(int)}}.
	 */
	@Test(expected = PassengerException.class)
	public void testA380FlyPassengerException() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
		
		A380Test.confirmBooking(pass_First, 1700);
		A380Test.confirmBooking(pass_Business, 1600);
		
		A380Test.getPassengers().get(0).flyPassenger(2000);
		A380Test.getPassengers().get(1).cancelSeat(1700);
		
		A380Test.flyPassengers(2000);
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#flyPassengers(int)}}.
	 */
	@Test 
	public void testA380FlyPassenger() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
	
		A380Test.confirmBooking(pass_First, 1400);
		A380Test.confirmBooking(pass_Business, 1500);
		
		A380Test.flyPassengers(2000);
		
		assertTrue(A380Test.getPassengers().get(0).isFlown()&& 
						A380Test.getPassengers().get(1).isFlown());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#flyPassengers(int)}}.
	 */
	@Test 
	public void testA380NotFlyPassenger() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
	
		A380Test.confirmBooking(pass_First, 1400);
		A380Test.confirmBooking(pass_Business, 1500);
		
		
		assertFalse(A380Test.getPassengers().get(0).isFlown()&& 
						A380Test.getPassengers().get(1).isFlown());
	}
	
	/**
	 * Test method for {@link asgn2Aircraft.A380#upgradeBookings()}}.
	 */
	@Test 
	public void testA380UpgradeBooking() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
		Passenger pass_Premium = new Premium(1400, 1600);
		Passenger pass_Economy = new Economy(1300, 1700);
	
		A380Test.confirmBooking(pass_First,1700);
		A380Test.confirmBooking(pass_Premium, 1600);
		A380Test.confirmBooking(pass_Business, 1500);
		A380Test.confirmBooking(pass_Economy, 1500);
		A380Test.upgradeBookings();
		
		assertTrue(A380Test.getNumBusiness()==1 && A380Test.getNumFirst()==2 
				&& A380Test.getNumPremium()==1 && A380Test.getNumEconomy()==0);
	}

	/**
	 * Test method for {@link asgn2Aircraft.A380#upgradeBookings()}}.
	 */
	@Test 
	public void testA380NotUpgradeBooking() throws AircraftException, PassengerException {
		Passenger pass_First = new First(1100, 1300);
		Passenger pass_Business = new Business(1200, 1400);
		Passenger pass_Premium = new Premium(1400, 1600);
		Passenger pass_Economy = new Economy(1300, 1700);
	
		A380Test.confirmBooking(pass_First,1700);
		A380Test.confirmBooking(pass_Premium, 1600);
		A380Test.confirmBooking(pass_Business, 1500);
		A380Test.confirmBooking(pass_Economy, 1500);
		
		
		assertTrue(A380Test.getNumBusiness()==1 && A380Test.getNumFirst()==1 
				&& A380Test.getNumPremium()==1 && A380Test.getNumEconomy()==1);
	}

	
	

	
}
