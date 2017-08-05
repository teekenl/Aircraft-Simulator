/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Simulators;

/**
 * This class represents exceptions generated during the
 * simulation, from classes which utilise the A380 and Passenger hierarchy.
 * @author hogan 
 */
@SuppressWarnings("serial") // We're not interested in binary i/o here
public class SimulationException extends Exception {
	
	/**
	 * Creates a new instance of SimulationException.
	 * 
	 * @param message String holding an informative message about the problem encountered
	 */
	public SimulationException(String message) {
		super("Simulation Exception: " + message);
	}
}
