/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Aircraft;

/**
 * Simple record type to hold the Passenger number information from an Aircraft and to make 
 * it available for reporting. 
 * 
 * @author hogan
 *
 */
public class Bookings {
	
	private int numFirst;
	private int numBusiness; 
	private int numPremium;
	private int numEconomy;
	private int total;
	private int available;
	

	/**
	 * @param numFirst <code>int</code> number of First Class passengers
	 * @param numBusiness <code>int</code> number of Business Class passengers
	 * @param numPremium <code>int</code> number of Premium Economy Class passengers
	 * @param numEconomy <code>int</code> number of Economy Class passengers
	 * @param total <code>int</code> total number of passengers
	 * @param available <code>int</code> number of seats available
	 */
	public Bookings(int numFirst, int numBusiness, int numPremium, int numEconomy, int total, int available) {
		this.numFirst = numFirst;
		this.numBusiness = numBusiness;
		this.numPremium = numPremium;
		this.numEconomy = numEconomy;
		this.total = total;
		this.available = available;
	}

	/**
	 * Simple getter for number of First passengers
	 *  
	 * @return <code>int</code> number of First Class passengers
	 */
	public int getNumFirst() {
		return numFirst;
	}

	/**
	 * Simple setter for number of First Class passengers
	 *  
	 * @param numFirst <code>int</code> number of First Class passengers
	 */
	public void setNumFirst(int numFirst) {
		this.numFirst = numFirst;
	}


	/**
	 * Simple getter for number of Business Class passengers
	 *  
	 * @return <code>int</code> number of Business Class passengers
	 */
	public int getNumBusiness() {
		return numBusiness;
	}

	/**
	 * Simple setter for number of Business Class passengers
	 *  
	 * @param numBusiness <code>int</code> number of Business Class passengers
	 */
	public void setNumBusiness(int numBusiness) {
		this.numBusiness = numBusiness;
	}


	/**
	 * Simple getter for number of Premium Economy Class passengers
	 *  
	 * @return <code>int</code> number of Premium Economy Class passengers
	 */
	public int getNumPremium() {
		return numPremium;
	}

	/**
	 * Simple setter for number of Premium Economy Class passengers
	 *  
	 * @param numPremium <code>int</code> number of Premium Economy Class passengers
	 */
	public void setNumPremium(int numPremium) {
		this.numPremium = numPremium;
	}

	/**
	 * Simple getter for number of Economy Class passengers
	 *  
	 * @return <code>int</code> number of Economy Class passengers
	 */
	public int getNumEconomy() {
		return numEconomy;
	}

	/**
	 * Simple setter for number of Economy Class passengers
	 *  
	 * @param numEconomy <code>int</code> number of Economy Class passengers
	 */
	public void setNumEconomy(int numEconomy) {
		this.numEconomy = numEconomy;
	}

	/**
	 * Simple getter for total number of passengers
	 *  
	 * @return <code>int</code> total number of passengers
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Simple setter for total number of passengers
	 *  
	 * @param total <code>int</code> total number of passengers
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * Simple getter for number of seats available
	 *  
	 * @return <code>int</code> number of seats available
	 */
	public int getAvailable() {
		return available;
	}

	/**
	 * Simple setter for number of seats available 
	 *  
	 * @param available <code>int</code> number of seats available
	 */
	public void setAvailable(int available) {
		this.available = available;
	}
}
