/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Simulators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import asgn2Aircraft.Bookings;
import asgn2Passengers.Business;
import asgn2Passengers.First;
import asgn2Passengers.Passenger;
import asgn2Passengers.Premium;

/**
 * Class to support logging for the Aircraft Simulator 
 *  
 * @author hogan
 *
 */
public class Log {
	
	//Controls logging of detailed status information 
	public static final boolean SAVE_STATUS = false; 
	
	/**
	 * Helper to set Passenger transition messages
	 * 
	 * @param p <code>Passenger</code> making a transition (uses F,J,P,Y)
	 * @param source <code>String</code> holding starting state (uses N,Q,C) - {New,Queued,Confirmed}
	 * @param target <code>String</code> holding finishing state (uses Q,C,R,F) - (Queued,Confirmed,Refused,Flown)
	 * @return <code>String</code> containing transition in the form: |(F|J|P|Y):(N|Q|C)>(Q|C|R|F)| 
	 */
	public static String setPassengerMsg(Passenger p,String source, String target) {
		String str="";
		if (p instanceof First) {
			str += "F";
		} else if (p instanceof Business) {
			str += "J";
		} else if (p instanceof Premium) {
			str += "P";
		} else {
			str += "Y";
		}
		return "|"+str+":"+source+">"+target+"|";
	}
	
	/**
	 * Helper to set Passenger upgrade messages. 
	 * 
	 * @param p <code>Passenger</code> to be upgraded 
	 * @return <code>String</code> containing transition in the form: |(J|P|Y)>(F|J|P)| 
	 */
	public static String setUpgradeMsg(Passenger p) {
		String str="";
		if (p instanceof Business) {
			str += "J>F";
		} else if (p instanceof Premium) {
			str += "P>J";
		} else {
			str += "Y>P";
		}
		return "|U:"+str+"|";
	}
	
	private BufferedWriter writer = null;
	private BufferedWriter detWriter = null;
	
	/**
	 * Constructor establishes log files based on the current time in the canonical directory 
	 *
	 * @throws IOException if the log files or BufferedWriters cannot be created
	 */
	public Log () throws IOException {
		//File management based on http://stackoverflow.com/questions/15754523/how-to-write-text-file-java 
        File logFile = new File(getLogTime());

        // This will output the full path where the file will be written to...
        System.out.println(logFile.getCanonicalPath());
        this.writer = new BufferedWriter(new FileWriter(logFile));
        
        if (Log.SAVE_STATUS) {
	        File detFile = new File(getLogTime()+"Detail");
	        this.detWriter = new BufferedWriter(new FileWriter(detFile));
        }
	}
	
	/**
	 * Log final state information and clean up 
	 * 
	 * @param sim <code>Simulator</code> being used 
	 * @throws IOException on write or closure failures 
	 */
	public void finalise(Simulator sim, GUISimulator gs) throws IOException {
		String time = getLogTime(); 
		writer.write("\n" + time + ": End of Simulation\n");
		gs.getLogOutput("\n" + time + ": End of Simulation\n");
		writer.write(sim.finalState());
		gs.getLogOutput(sim.finalState());
		writer.close();
		
		if (Log.SAVE_STATUS) {
			detWriter.write("\n" + time + ": End of Simulation\n");
			detWriter.close(); 
		}
	}
	
	/**
	 * Log initial state of the simulation and aircraft
	 * Note we grab aircraft from first valid schedule 
	 * 
	 * @param sim <code>Simulator</code> providing parameters 
	 * @throws IOException on write failures 
	 * @throws SimulationException See {@link asgn2Simulators.Simulator#getFlights(int)}
	 */
	public void initialEntry(Simulator sim, GUISimulator gs) throws IOException, SimulationException {
		writer.write(getLogTime() + ": Start of Simulation\n");
		gs.getLogOutput(getLogTime() + ": Start of Simulation\n");
		writer.write(sim.toString() + "\n");
		gs.getLogOutput(sim.toString() + "\n");
		String capacities = sim.getFlights(Constants.FIRST_FLIGHT).initialState();
		writer.write(capacities);
		gs.getLogOutput(capacities);
	}
	
	/**
	 * Log summary entry for each time step 
	 * Note: Aircraft bookings appear only once Flights have begun 
	 * 
	 * @param time <code>int</code> holding current simulation time step 
	 * @param sim <code>Simulator</code> controlling simulation 
	 * @throws SimulationException See {@link asgn2Simulators.Simulator#getSummary(int, boolean)}
	 * @throws IOException on write failures 
	 */
	public void logEntry(int time,Simulator sim, GUISimulator gs, int index) throws IOException, SimulationException {
		boolean flying = (time >= Constants.FIRST_FLIGHT);
		writer.write(sim.getSummary(time, flying));
		gs.getLogOutput(sim.getSummary(time, flying));
		
		// Read values to Plot the Graph
		if(flying) {
			Flights flight = sim.getFlights(time);
			Bookings counts = flight.getCurrentCounts();
					gs.getCharOutput(index, counts.getNumFirst() , counts.getNumBusiness(), counts.getNumPremium(), counts.getNumEconomy(), 
							sim.numInQueue(), sim.numRefused(), counts.getTotal(), counts.getAvailable());
		}
		
	}
	
	/**
	 * Log Queue and Refused Transitions for each time step. 
	 * Logging controlled by {@link #SAVE_STATUS} 
	 * 
	 * @param time <code>int</code> holding current simulation time step 
	 * @param sim <code>Simulator</code> controlling simulation 
	 * @throws IOException on write failures 
	 */
	public void logQREntries(int time,Simulator sim) throws IOException {
		if (Log.SAVE_STATUS) {
			detWriter.write(sim.getStatus(time));
		}
	}
	
	/**
	 * Log Aircraft Transitions for each time step. 
	 * Logging controlled by {@link #SAVE_STATUS} 
	 * 
	 * @param time <code>int</code> holding current simulation time step 
	 * @param sim <code>Simulator</code> controlling simulation 
	 * @throws IOException on write failures 
	 * @throws SimulationException See {@link Simulator#getFlights(int)}
	 */
	public void logFlightEntries(int time,Simulator sim) throws IOException, SimulationException {
		if (Log.SAVE_STATUS) {
			Flights flights = sim.getFlights(time); 
			detWriter.write(flights.getStatus(time));
		}
	}
	
	/**
	 * Helper returning Log Time format for filename
	 * @return filename String yyyyMMdd_HHmmss
	 */
	private String getLogTime() {
		String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		return timeLog;
	}

}
