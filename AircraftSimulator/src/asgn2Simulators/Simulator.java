/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Simulators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import asgn2Aircraft.AircraftException;
import asgn2Aircraft.Bookings;
import asgn2Passengers.Business;
import asgn2Passengers.Economy;
import asgn2Passengers.First;
import asgn2Passengers.Passenger;
import asgn2Passengers.PassengerException;
import asgn2Passengers.Premium;


/**
 * Class to hold  parameters and to provide random trial services for the simulation. 
 * Simulation relies by default on parameters set in the file {@link asgn2Simulators.Constants}.  
 * User has the option to set these parameters using the multi-argument constructor. 
 * Methods provide daily booking levels according to the distribution N(meanBookings,sdBookings^2), 
 * and outcomes for fare class selection (or passenger creation) and other random decisions.   
 * 
 * @author hogan
 *
 */
public class Simulator {
	
	/**
	 * Static utility method to implement a <a href="http://en.wikipedia.org/wiki/Bernoulli_trial">Bernoulli Trial</a>, 
	 * a coin toss with two outcomes: success (probability successProb) and failure (probability 1-successProb)
	 * 
	 * @param rng <code>Random</code> generating the random sequence of numbers
	 * @param successProb double holding the success probability 
	 * @return true if trial was successful, false otherwise
	 */
	public static boolean randomSuccess(Random rng,double successProb) {
		boolean result = rng.nextDouble() <= successProb;
		return result;
	}
	
	private double meanDailyBookings;
	private double sdDailyBookings; 
	private int seed;
	private double cancelProb;
	private Random rng;
	private int maxQueueSize; 
	
	private double firstProb;
	private double businessProb;
	private double premiumProb; 
	private double economyProb;
	
	private Queue<Passenger> queue;
	private List<Passenger> refused; 
	private List<Passenger> cancelled;
	private List<Flights> schedule;
	
	private int currentBookings;
	private int totalFirst; 
	private int totalBusiness;
	private int totalPremium;
	private int totalEconomy; 
	private int totalFlown; 
	private int totalEmpty;
 
	private String status;


	/**
	 * Constructor for Simulator using defaults
	 * 
	 * @throws SimulationException if one or more probabilities are invalid, or if (meanDailyBookings < 0) OR 
	 * (sdDailyBookings < 0). See {@link #Simulator(int, int, double, double, double, double, double, double, double)}
	 */
	public Simulator() throws SimulationException {
		this(Constants.DEFAULT_SEED,Constants.DEFAULT_MAX_QUEUE_SIZE,Constants.DEFAULT_DAILY_BOOKING_MEAN,Constants.DEFAULT_DAILY_BOOKING_SD,
	 		 Constants.DEFAULT_FIRST_PROB,Constants.DEFAULT_BUSINESS_PROB,
			 Constants.DEFAULT_PREMIUM_PROB,Constants.DEFAULT_ECONOMY_PROB,
			 Constants.DEFAULT_CANCELLATION_PROB);
	}
	
	/**
	 * Constructor for simulator allowing setting of all values. Parameters tell the story. 
	 * 
	 * @param seed <code>int</code> random number generator seed
	 * @param maxQueueSize <code>int</code> holding max permissible queue size
	 * @param meanDailyBookings <code>double</code> holding the mean of the Normal Distribution of daily bookings 
	 * @param sdDailyBookings <code>double</code> holding the standard deviation of the booking distribution
	 * @param firstProb <code>double</code> holding the probability that current booking will be First Class 
	 * @param businessProb <code>double</code> holding the probability that current booking will be Business Class 
	 * @param premiumProb <code>double</code> holding the probability that current booking will be Premium Economy
	 * @param economyProb <code>double</code> holding the probability that current booking will be Economy Class
	 * @param cancelProb <code>double</code> holding the probability of cancellation from confirmedSeat
	 * @throws SimulationException if one or more probabilities are invalid, or if meanDailyBookings < 0 or sdDailyBookings < 0
	 */
	public Simulator(int seed,int maxQueueSize,double meanDailyBookings, double sdDailyBookings,
			double firstProb, double businessProb, double premiumProb, double economyProb, double cancelProb) throws SimulationException {
		checkProbabilties(firstProb, businessProb, premiumProb, economyProb);
		checkSimParamsAndThrowExceptions(maxQueueSize, meanDailyBookings, sdDailyBookings);

		this.meanDailyBookings = meanDailyBookings;
		this.sdDailyBookings = sdDailyBookings;
		this.maxQueueSize = maxQueueSize;
		this.seed = seed;
		this.firstProb = firstProb;
		this.businessProb = businessProb;
		this.premiumProb = premiumProb;
		this.economyProb = economyProb;
		this.cancelProb = cancelProb; 
		
		this.rng = new Random(this.seed);
		this.schedule = new ArrayList<Flights>();
		this.queue = new LinkedList<Passenger>(); 
		this.refused = new ArrayList<Passenger>();
		this.cancelled = new ArrayList<Passenger>();
		
		this.currentBookings=0;
		this.totalFirst=0;
		this.totalBusiness=0;
		this.totalPremium=0;
		this.totalEconomy=0; 
		this.totalFlown=0;
		this.totalEmpty=0;
		this.status = "";
	}
	
	
	/**
	 * Method to create a New Passenger. Fare class is decided using probabilities 
	 * set in the constructor. Usually, these will be the values from {@link asgn1Simulators.Constants}. We return the appropriate 
	 * object from the {@link asgn1Passengers.Passenger} hierarchy. 
	 * 
	 * @return new <code>Passenger</code> of type <code>{First,Business,Premium,Economy}</code> 
	 * according to RNG
	 * @throws PassengerException if invalid constructor parameters. 
	 * See {@link asgn1Passengers.Passenger#Passenger(int,int)}
	 */
	public Passenger createPassenger(int bookingTime,int departureTime) throws PassengerException {
		double testValue = rng.nextDouble();
		double busTest = this.firstProb+this.businessProb;
		double premTest = busTest+this.premiumProb;
		
		if (testValue >= (1.0 - this.firstProb)) {
			return new First(bookingTime,departureTime);
		} else if (testValue >= (1.0 - busTest)) {
			return new Business(bookingTime,departureTime); 
		} else if (testValue >= (1.0 - premTest)) {
			return new Premium(bookingTime,departureTime); 
		} else {
			return new Economy(bookingTime,departureTime);
		}
	}

	/**
	 * Simple method to create scheduled departures for each flying day of the simulation 
	 * 
	 * @throws AircraftException if problems with arguments to {@link asgn2Aircraft.Aircraft} constructor
	 */
	public void createSchedule() throws AircraftException {
		for (int time=0; time<=Constants.DURATION-Constants.FIRST_FLIGHT; time++) {
			this.schedule.add(new Flights(time+Constants.FIRST_FLIGHT));
		}
	}

	/**
	 * Method to clear queued and cancelled passengers at the end of the simulation. States changed 
	 * to Refused and added to the refused collection. 
	 *  
	 * @param time <code>int</code> time operation performed. 
	 * @throws PassengerException See {@link asgn2Passengers.Passenger#refusePassenger(int)}
	 */
	public void finaliseQueuedAndCancelledPassengers(int time) throws PassengerException {
		for (Passenger p : this.queue) {
			p.refusePassenger(time);
			this.status += Log.setPassengerMsg(p,"Q","R");
		}
		this.refused.addAll(this.queue);
		this.queue.clear();
		
		for (Passenger p : this.cancelled) {
			p.refusePassenger(time);
			this.status += Log.setPassengerMsg(p,"N","R");
		}
		this.refused.addAll(this.cancelled);
		this.cancelled.clear();
	}

	/**
	 * Simple method to report the final state of the simulation 
	 * 
	 * @return <code>String</code> reporting final totals 
	 */
	public String finalState() {
		String str = "Final Totals: [F" + this.getTotalFirst()
					+ ":J" + this.getTotalBusiness()
					+ ":P" + this.getTotalPremium()
					+ ":Y" + this.getTotalEconomy()
					+ ":T" + this.getTotalFlown()
					+ ":E" + this.getTotalEmpty()
					+ ":R" + this.numRefused() +"]\n";
		return str; 
	}

	/**
	 * Wrapper to fly all passengers at the specified time 
	 * 	 
	 * @param time <code>int</code> departure time for flights  
	 * @throws SimulationException See {@link #getFlights(int)}
	 * @throws PassengerException See {@link asgn2Simulators.Flights#flyPassengers(int)}
	 */
	public void flyPassengers(int time) throws SimulationException, PassengerException {
		Flights flights = this.getFlights(time);
		flights.flyPassengers(time); 
	}

	/**
	 * Method to generate bookings according to Gaussian RNG, and to confirm, queue or refuse
	 * the bookings. 
	 * 
	 * @param time <code>int</code> time operation performed 
	 * @throws AircraftException See {@link #processNewPassenger(Flights, Passenger, int, int)}
	 * @throws PassengerException See {@link #createPassenger(int, int)}
	 * @throws SimulationException See {@link #processNewPassenger(Flights, Passenger, int, int)}, 
	 * {@link #createPassenger(int, int)}
	 */
	public void generateAndHandleBookings(int time) throws AircraftException, PassengerException, SimulationException {
		//Total Booking period:  FIRST_FLIGHT until DURATION 
		//Correct for current time, possible end of simulation 
		assert Constants.MAX_BOOKING_PERIOD > Constants.FIRST_FLIGHT; 
		int bookingStart = Math.max(time, Constants.FIRST_FLIGHT);
		int bookingEnd = Math.min(time+Constants.MAX_BOOKING_PERIOD,Constants.DURATION);
		int bookingDays = bookingEnd - bookingStart +1; 
		
		this.currentBookings = this.getDailyBookings(); 
		int bookingsPerDay = Math.max(1,((int) Math.floor(this.currentBookings/bookingDays)));	
		
		//Process the whole booking period 
		for (int departureTime=bookingStart; departureTime<=bookingEnd; departureTime++) {
			Flights flights = this.getFlights(departureTime);
			
			for (int pass=0; pass<=bookingsPerDay-1; pass++) {
				//Passenger in New state 
				Passenger p = this.createPassenger(time, departureTime);
				processNewPassenger(flights, p, time, departureTime);
			}
		}
	}

	/**
	 * Method to set number of bookings according to Gaussian RNG
	 * 
	 * @return <code>double</code> random bookings drawn from N(meanDailyBookings,sdDailyBookings) or MINIMUM_BOOKINGS, whichever is greater
	 */
	public int getDailyBookings() {
		//z ~ N(0,1) so transform 
		double z = this.rng.nextGaussian(); 
		double x = z*this.sdDailyBookings + this.meanDailyBookings;
		int bookings = ((int) x);
		return Math.max(bookings,Constants.MINIMUM_BOOKINGS);
	}
	
	/**
	 * Method to grab the flight schedule for the specified day
	 * 
	 * @param departureTime <code>int</code> schedule to retrieve
	 * @return <code>Flights</code> for the day 
	 * @throws SimulationException if (departureTime < FIRST_FLIGHT) OR 
	 * (departureTime > DURATION)
	 */
	public Flights getFlights(int departureTime) throws SimulationException {
		checkValidDepartureTimeAndThrowException(departureTime);
		return this.schedule.get(departureTime-Constants.FIRST_FLIGHT);
	}

	/**
	 * Method to report on the status of the flight schedule for the selected time 
	 * 
	 * @param time <code>int</code> time selected 
	 * @return <code>Bookings</code> containing current passenger counts 
	 * @throws SimulationException See {@link #getFlights(int)}
	 */
	public Bookings getFlightStatus(int time) throws SimulationException {
		Flights flights = this.getFlights(time);
		return flights.getCurrentCounts();
	}
	
	/**
	 * @param time <code>int</code> current time - presently unused
	 * @return <code>String</code> with current status string and newline 
	 */
	public String getStatus(int time) {
		return this.status + "\n";
	}

	/**
	 * Status update at current time showing passengers across all aircraft in the schedule.
	 * Additional information supplied if in flying period (see <code>boolean</code>). 
	 * 
	 * @param time <code>int</code> current time 
	 * @param flying <code>boolean</code> in flying period FIRST_FLIGHT to DURATION
	 * @return <code>String</code> report on Passenger counts at current time 
	 * @throws SimulationException See {@link #getFlights(int)}
	 */
	public String getSummary(int time, boolean flying) throws SimulationException {
		String str = time + ":" + this.currentBookings;
		if (flying) {
			Flights flights = this.getFlights(time);
			Bookings counts = flights.getCurrentCounts();
			str += ":F" + counts.getNumFirst() 
					+ ":J" + counts.getNumBusiness()
					+ ":P" + counts.getNumPremium()
					+ ":Y" + counts.getNumEconomy() 
					+ ":T" + counts.getTotal()
					+ ":E" + counts.getAvailable();
		} 
		str += ":Q" + this.numInQueue() +
			   ":R" + this.numRefused() +"\n";
		return str; 
	}
	
	/**
	 * Simple getter for total Business Passengers flown 
	 * 
	 * @return totalBusiness
	 */
	public int getTotalBusiness() {
		return totalBusiness;
	}
	
	/**
	 * Simple getter for total Economy Passengers flown 
	 * 
	 * @return totalEconomy
	 */
	public int getTotalEconomy() {
		return totalEconomy;
	}

	/**
	 * Simple getter for total seats remaining empty
	 *  
	 * @return totalEmpty
	 */
	public int getTotalEmpty() {
		return totalEmpty;
	}
	
	/**
	 * Simple getter for total First Passengers flown 
	 * 
	 * @return totalFirst
	 */
	public int getTotalFirst() {
		return totalFirst;
	}
	
	/**
	 * Simple getter for total Passengers flown 
	 * 
	 * @return totalFlown
	 */
	public int getTotalFlown() {
		return totalFlown;
	}

	/**
	 * Simple getter for total Premium Passengers flown
	 *  
	 * @return totalPremium
	 */
	public int getTotalPremium() {
		return totalPremium;
	}

	/**
	 * Simple status for size of queue (current)
	 * 
	 * @return #queue
	 */
	public int numInQueue() {
		return this.queue.size();
	}
	
	/**
	 * Simple status for number of passengers refused (cumulative)
	 * 
	 * @return #refused
	 */
	public int numRefused() {
		return this.refused.size();
	}

	/**
	 * Method to traverse the flights over the cancellation period, performing Bernoulli 
	 * trials for cancellation of each passenger. We consider cancellations up to 
	 * {@link asgn2Simulators.Constants#CANCELLATION_PERIOD} in the future.  
	 * 
	 * @param time <code>int</code> flight departure time 
	 * @throws SimulationException See {@link #getFlights(int)}
	 * @throws PassengerException See {@link asgn2Simulators.Flights#cancelBookings(Random, double, int)}
	 * @throws AircraftException
	 */
	public void processNewCancellations(int time) throws SimulationException, PassengerException, AircraftException {
		//Cancellation period: correct for starting time, end of simulation 
		int cancelPeriodStart = Math.max(time,Constants.FIRST_FLIGHT);
		int cancelPeriodEnd = time + Constants.CANCELLATION_PERIOD;
		cancelPeriodEnd = Math.min(Constants.DURATION,cancelPeriodEnd); 

		for (int cancel=cancelPeriodStart; cancel<=cancelPeriodEnd; cancel++) {
			Flights flights = this.getFlights(cancel);
			this.cancelled.addAll(flights.cancelBookings(this.rng, this.cancelProb, time)); 
		}
	}
	
	/**
	 * Method to process the queue based on state at time specified. Passengers remaining in the 
	 * Queue longer than the limit are discarded. 
	 * 
	 * @param time <code>int</code> time of operation 
	 * @throws SimulationException See {@link #getFlights(int)}
	 * @throws AircraftException See {@link asgn2Simulators.Flights#addPassenger(Passenger, int)}
	 * @throws PassengerException See {@link asgn2Simulators.Simulator#refuseBooking(Passenger, int)} and 
	 * {@link asgn2Simulators.Flights#addPassenger(Passenger, int)}
	 */
	public void processQueue(int time) throws SimulationException, AircraftException, PassengerException {
		while (!this.queueEmpty()) {
			Passenger p = queue.peek(); 
			boolean deQueued = false; 
			
			//Passenger can be confirmed on any flight up to MAX_QUEUING_PERIOD after 
			//original departure time, but can't be before time or after simulation end  
			int departureTime = p.getDepartureTime(); 
			int minConfirmationTime = Math.max(time, departureTime+1);
			int maxConfirmationTime = departureTime + Constants.MAX_QUEUING_PERIOD;
			maxConfirmationTime = Math.min(maxConfirmationTime,Constants.DURATION);
			
			if (time > maxConfirmationTime) {
				p = this.queue.poll();
				this.refuseBooking(p,time);
			} else { 
				//We see if we can confirm seat during allowable queueing period 
				for (int newDeparture=minConfirmationTime; newDeparture<=maxConfirmationTime; newDeparture++) {
					Flights flights = this.getFlights(newDeparture);
					if (flights.seatsAvailable(p)) {
						p = this.queue.poll(); 
						flights.addPassenger(p,time);
						deQueued = true;
						break; 
					}
				}
				
				//Queue is blocked
				if (!deQueued) {
					break; 
				}
			}
		}
	}

	/**
	 * Method to process upgrades across the daily flights at time specified 
	 * 
	 * @param time <code>int</code> time of operation 
	 * @throws SimulationException See {@link asgn2Simulators.Simulator#getFlights(int)}
	 */
	public void processUpgrades(int time) throws SimulationException {
		Flights flights = this.getFlights(time);
		flights.upgradePassengers();
	}

	/**
	 * Simple status showing whether queue is empty
	 * 
	 * @return true if queue empty, false otherwise
	 */
	public boolean queueEmpty() {
		return this.queue.size() == 0;
	}

	/**
	 * Simple status showing whether queue is full
	 * 
	 * @return true if queue full, false otherwise
	 */
	public boolean queueFull() {
		return this.queue.size() == this.maxQueueSize;
	}

	/**
	 * Method to add a Passenger to the simulator queue. 
	 * Precondition is a test that space is available in the queue
	 * 
	 * @param p <code>Passenger</code> to be added to the queue 
	 * @param queueTime <code>int</code> time operation performed 
	 * @param departureTime <code>int</code> preferred departureTime 
	 * @throws PassengerException if <code>Passenger</code> is in incorrect state 
	 * OR queueTime OR departureTime is invalid. See {@link asgn2Passengers.Passenger#queuePassenger(int, int)}
	 * @throws SimulationException if <code>isFull(queue)</code>. 
	 */
	public void queuePassenger(Passenger p,int queueTime,int departureTime) throws SimulationException, PassengerException { 
		if (this.queueFull()) {
			throw new SimulationException("Queue is full");
		}
		p.queuePassenger(queueTime, departureTime);
		this.status += Log.setPassengerMsg(p,"N","Q");
		this.queue.add(p);
	}

	/**
	 * Method to explore rebooking of cancelled passengers up to 
	 * {@link asgn2Simulators.Constants#CANCELLATION_PERIOD} days after 
	 * their intended departureTime. 
	 * 
	 * @param time <code>int</code> containing time operation performed
	 * @throws AircraftException, PassengerException See {@link asgn2Aircraft.Aircraft#confirmBooking(Passenger, int)}
	 * @throws SimulationException See {@link asgn2Simulators.Simulator#getFlights(int)}
	 */
	public void rebookCancelledPassengers(int time) throws AircraftException, PassengerException, SimulationException {
		for (Passenger p : this.cancelled) {
			boolean rebooked = false; 
			int departureTime = p.getDepartureTime(); 
			int maxRebookingTime = departureTime + Constants.CANCELLATION_PERIOD;
			maxRebookingTime = Math.min(maxRebookingTime,Constants.DURATION);
			
			for (int rebook=departureTime+1; rebook<=maxRebookingTime; rebook++) {
				Flights flights = this.getFlights(rebook);
				if (flights.seatsAvailable(p)) {
					flights.addPassenger(p,time);
					rebooked=true; 
					break; 
				}
			}
			
			if (!rebooked) {
				int newDepartureTime = Math.max(time+1, departureTime);
				queueOrRefusePassenger(p, time, newDepartureTime);
			}
		}
		this.cancelled.clear();
	}

	/**
	 * Method to add a Passenger to the refused list. 
	 * 
	 * @param p <code>Passenger</code> to be added to the refused list 
	 * @param refusalTime <code>int</code> time operation performed 
	 * @throws PassengerException if <code>Passenger</code> is in incorrect state 
	 * OR refusalTime is invalid. See {@link asgn2Passengers.Passenger#refusePassenger(int)}. 
	 */
	public void refuseBooking(Passenger p,int refusalTime) throws PassengerException { 
		p.refusePassenger(refusalTime);
		this.status += Log.setPassengerMsg(p,"N/Q","R");
		this.refused.add(p);
	}

	/**
	 * Simple helper method to rest the status message - called each simulation step 
	 * 
	 * @param time <code>int</code> Current simulation step 
	 */
	public void resetStatus(int time) {
		this.status = time + ":";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Simulator [meanDailyBookings=" + meanDailyBookings + ", sdDailyBookings=" + sdDailyBookings
				+ ", seed=" + seed + ", firstProb=" + firstProb + ", businessProb="
				+ businessProb + ", premiumProb=" + premiumProb 
				+ ", economyProb=" + economyProb + ", maxQueueSize=" + maxQueueSize 
				+ ", cancellationProb=" + cancelProb + "]";
	}

	/**
	 * Helper to update the total passenger counts with the latest flight schedule 
	 * 
	 * @param time <code>int</code> time selected 
	 * @return <code>Bookings</code> containing current passenger counts 
	 * @throws SimulationException See {@link #getFlights(int)}
	 */
	public void updateTotalCounts(int time) throws SimulationException {
		Flights flights = this.getFlights(time);
		Bookings counts = flights.getCurrentCounts();
		this.totalFirst += counts.getNumFirst();
		this.totalBusiness += counts.getNumBusiness();
		this.totalPremium += counts.getNumPremium();
		this.totalEconomy += counts.getNumEconomy();
		this.totalFlown += counts.getTotal();
		this.totalEmpty += counts.getAvailable();
	}

	/**
	 * Helper to check all probabilities are valid
	 * 
	 * @throws SimulationException if invalidProb({firstProb,businessProb,premiumProb,economyProb})
	 */
	private void checkProbabilties(double firstProb, double businessProb,
			double premiumProb, double economyProb) throws SimulationException {
		String msg = "";
		boolean throwExcept = false; 
		if (invalidProbability(firstProb)) {
			msg += " firstProb ";
			throwExcept = true;
		} 
		if (invalidProbability(businessProb)) {
			msg += " businessProb ";
			throwExcept = true;
		}
		if (invalidProbability(premiumProb)) {
			msg += " premiumProb ";
			throwExcept = true;
		}
		if (invalidProbability(economyProb)) {
			msg += " economyProb ";
			throwExcept = true;
		}
		if (throwExcept) {
			throw new SimulationException(msg + " must lie in [0,1]");
		}
	}

	/**
	 * Helper method to check Simulation Parameters 
	 * 
	 * @throws SimulationException if {maxQueueSize,meanDailyBookings,sdDailyBookings} < 0
	 */
	private void checkSimParamsAndThrowExceptions(int maxQueueSize, double meanDailyBookings, double sdDailyBookings)
			throws SimulationException {
		if ((maxQueueSize<0) || (meanDailyBookings <0) || (sdDailyBookings <0)) {
			throw new SimulationException(" Invalid queueSize, mean or standard deviation");
		}
	}

	/**
	 * Boolean helper method to detect invalid departureTime, throwing SimulationException 
	 * 
	 * @param departureTime <code>int</code> to be tested
	 * @throws SimulationException if isInvalid(departureTime)
	 */
	private void checkValidDepartureTimeAndThrowException(int departureTime) throws SimulationException {
		if ((departureTime < Constants.FIRST_FLIGHT) || (departureTime > Constants.DURATION)) {
			throw new SimulationException("Invalid departure time");
		}
	}

	/**
	 * Boolean helper method to detect invalid probability 
	 * 
	 * @param prob double holding probability 
	 * @return true if invalid (prob > 1.0) OR (prob < 0.0); false otherwise 
	 */
	private boolean invalidProbability(double prob) {
		return (prob < 0.0) || (prob > 1.0);
	}

	/**
	 * Helper to wrap the processing of new passengers - confirm or queueOrRefuse 
	 */
	private void processNewPassenger(Flights flights, Passenger p, int time, int departureTime)
			throws AircraftException, PassengerException, SimulationException {
		if (flights.seatsAvailable(p)) {
			flights.addPassenger(p, time);
		} else {
			queueOrRefusePassenger(p, time, departureTime);
		}
	}

	/**
	 * Helper to wrap the queuePassenger and refuseBooking combination
	 */
	private void queueOrRefusePassenger(Passenger p, int time, int departureTime)
			throws SimulationException, PassengerException {
		if (!this.queueFull()) {
			this.queuePassenger(p,time,departureTime); 
		} else {
			this.refuseBooking(p,time);
		}
	}
}

