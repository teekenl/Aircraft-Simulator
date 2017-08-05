/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Simulators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import asgn2Aircraft.A380;
import asgn2Aircraft.Aircraft;
import asgn2Aircraft.AircraftException;
import asgn2Aircraft.B747;
import asgn2Aircraft.Bookings;
import asgn2Passengers.Passenger;
import asgn2Passengers.PassengerException;

/**
 * Flights is a utility class to hold a collection of Aircraft for a single day. Most of the methods 
 * rely on those of {@link asgn2Aircraft.Aircraft}, visiting each aircraft in the daily flight list. 
 * 
 * @author hogan
 */
public class Flights {
	
	private List<Aircraft> daily;
	private Bookings counts; 

	/**
	 * Constructor creates a defined set of daily services. 
	 * 
	 * @throws AircraftException if problems with arguments to the {@link asgn2Aircraft.Aircraft} constructor. 
	 */
	public Flights(int time) throws AircraftException {
		this.daily = createDailyServices(time); 
		this.counts = null; 
	}
	
	/**
	 * Adds a Passenger to one of the aircraft in the flight list.
	 * Precondition is a call to {@link #seatsAvailable(Passenger)}
	 * 
	 * @param p <code>Passenger</code> to be added 
	 * @param time <code>int</code> time operation performed 
	 * @throws AircraftException See {@link asgn2Aircraft.Aircraft#confirmBooking(Passenger, int)}
	 * @throws PassengerException See {@link asgn2Aircraft.Aircraft#confirmBooking(Passenger, int)}
	 */
	public void addPassenger(Passenger p,int time) throws AircraftException, PassengerException {
		for (Aircraft a : this.daily) {
			if (a.seatsAvailable(p)) {
				a.confirmBooking(p, time);
				break; 
			}
		}
	}
	
	/**
	 * Cancel bookings across the daily schedule based on cancellation probability 
	 * 
	 * @param rng <code>Random</code> - the active random number generator object 
	 * @param cancelProb <code>double</code> Bernoulli trial cancellation probability 
	 * @param cancelTime <code>int</code> cancellation time 
	 * @return <code>List<Passenger></code> of cancelled passengers
	 * @throws PassengerException See {@link asgn2Aircraft.Aircraft#cancelBooking(Passenger, int)}
	 * @throws AircraftException See {@link asgn2Aircraft.Aircraft#cancelBooking(Passenger, int)}
	 */
	public List<Passenger> cancelBookings(Random rng,double cancelProb,int cancelTime) throws PassengerException, AircraftException {
		List<Passenger> cancelled = new ArrayList<Passenger>(); 
		for (Aircraft a : this.daily) {
			List<Passenger> passengers =  a.getPassengers();
			for (Passenger p : passengers) {
				if (Simulator.randomSuccess(rng, cancelProb)) {
					a.cancelBooking(p, cancelTime);
					cancelled.add(p);
				}
			}
		}
		return cancelled; 
	}
	
	/**
	 * Flies passengers across the daily schedule 
	 * 
	 * @param time <code>int</code> time operation performed
	 * @throws PassengerException See {@link asgn2Aircraft.Aircraft#flyPassengers(int)}
	 */
	public void flyPassengers(int time) throws PassengerException {
		for (Aircraft a : this.daily) {
			a.flyPassengers(time);
		}
	}
	
	/**
	 * Get the total passenger counts, for each fare class, in total, and seats remaining
	 *  
	 * @return <code>Bookings</code> object containing the counts
	 */
	public Bookings getCurrentCounts() {
		this.totalFlightCounts();
		return counts;
	}


	/**
	 * Wrapper to return the status fields of all aircraft in the schedule. 
	 * Used primarily for logging. 
	 * Note that status field includes state transitions 
	 * 
	 * @param time <code>int<code> current time step 
	 * @return <code>String</code> concatenating Aircraft status information  
	 */
	public String getStatus(int time) {
		String str = "";
		for (Aircraft a : this.daily) {
			str += a.getStatus(time);
		}
		return str;
	}
	
	/**
	 * Wrapper to return initial state information for all aircraft in the schedule.
	 * Used primarily for logging. 
	 *  
	 * @return <code>String</code> concatenating Aircraft initial state information  
	 */
	public String initialState() {
		String str = "";
		for (Aircraft a : this.daily) {
			str += a.initialState() + "\n";
		}
		return str;
	}
	
	/**
	 * Given a Passenger, method determines whether there are seats available in that 
	 * fare class across the daily schedule
	 *   
	 * @param p <code>Passenger</code> to be Confirmed
	 * @return <code>boolean</code> true if seats in Class(p); false otherwise
	 */
	public boolean seatsAvailable(Passenger p) {
		boolean status = false; 
		for (Aircraft a : this.daily) {
			if (a.seatsAvailable(p)) {
				status = true;
				break; 
			}
		}
		return status; 
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "";
		for (Aircraft a : this.daily) {
			str += a.toString() + "\n";
		}
		return str;
	}
	
	/**
	 *  Method to upgrade Passengers to try to fill the aircraft seating. 
	 *  Passengers are upgraded solely within their original aircraft. We do 
	 *  not move passengers between flights in the schedule. 
	 */
	public void upgradePassengers() {
		for (Aircraft a : this.daily) {
			a.upgradeBookings();
		}
		
	}

	/**
	 * Helper to create schedule 
	 * 
	 * @param time <code>int</code> departureTime used as index 
	 * @return list of created Aircraft 
	 * @throws AircraftException if problems with arguments to the  {@link asgn2Aircraft.Aircraft} constructor. 
	 */
	private List<Aircraft> createDailyServices(int time) throws AircraftException {
		List<Aircraft> al = new ArrayList<Aircraft>(); 
		al.add(new A380("QF11",time)); 
		al.add(new A380("QF93",time)); 
		al.add(new B747("QF15",time)); 
		return al; 
	}

	/**
	 * Helper to total passenger counts across all aircraft in the schedule 
	 */
	private void totalFlightCounts() {
		this.counts = new Bookings(0,0,0,0,0,0);
		for (Aircraft a : this.daily) {
			Bookings b = a.getBookings();
			int first = this.counts.getNumFirst();
			this.counts.setNumFirst(first + b.getNumFirst());
			int business = this.counts.getNumBusiness();
			this.counts.setNumBusiness(business + b.getNumBusiness());
			int premium = this.counts.getNumPremium();
			this.counts.setNumPremium(premium + b.getNumPremium());
			int economy = this.counts.getNumEconomy();
			this.counts.setNumEconomy(economy + b.getNumEconomy());
			int total = this.counts.getTotal();
			this.counts.setTotal(total + b.getTotal());
			int available = this.counts.getAvailable();
			this.counts.setAvailable(available + b.getAvailable());
		}
	}
}
