/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Simulators;

/**
 * Global constants for the simulation.
 * 
 * @author hogan
 *
 */
public class Constants {
	//Basic simulation time parameters - unchangeable 
	//All times are given in calendar days 
	public static final int WEEK = 7; 
	public static final int FIRST_FLIGHT = 3*WEEK;  
	public static final int MAX_BOOKING_PERIOD = 6*WEEK;
	public static final int CANCELLATION_PERIOD = 1*WEEK; 
	public static final int MAX_QUEUING_PERIOD = 1*WEEK;
	public static final int DURATION = 18*WEEK;
	
	//RNG and Probs - assume mean daily bookings around total capacity 
	public static final int DEFAULT_SEED = 100; 
	public static final int DEFAULT_MAX_QUEUE_SIZE = 500;
	public static final double DEFAULT_DAILY_BOOKING_MEAN = 1300.0;
	public static final double DEFAULT_DAILY_BOOKING_SD = 0.33*Constants.DEFAULT_DAILY_BOOKING_MEAN;
	public static final int MINIMUM_BOOKINGS = 300;
	public static final double DEFAULT_FIRST_PROB = 0.03;
	public static final double DEFAULT_BUSINESS_PROB = 0.14;
	public static final double DEFAULT_PREMIUM_PROB = 0.13;
	public static final double DEFAULT_ECONOMY_PROB = 0.70;
	public static final double DEFAULT_CANCELLATION_PROB = 0.1;
}
