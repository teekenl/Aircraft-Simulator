/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Aircraft;

/**
 * This class represents exceptions generated during the
 * simulation arising from the <code>Aircraft</code> hierarchy.
 * 
 * @author hogan 
 */
@SuppressWarnings("serial") // We're not interested in binary i/o here
public class AircraftException extends Exception {
	
	/**
	 * Creates a new instance of AircraftException.
	 * 
	 * @param message String holding an informative message about the problem encountered
	 */
	public AircraftException(String message) {
		super("Aircraft Exception: " + message);
	}
}
