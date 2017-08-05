/**
 * 
 * This file is part of the AircraftSimulator Project, written as 
 * part of the assessment for CAB302, semester 1, 2016. 
 * 
 */
package asgn2Simulators;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


/**
 * @author 
 *
 */

public class GUISimulator extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = -7031008862559936404L;
	private static final int WIDTH = 800;
	private static final int HEIGHT = 650;
	
	private double prob_Total = 0;
	private double prob_Cancel = 0;
	private String[] logCustomInput;
	
	private int[] firstcapacity;
	private int[] businesscapacity;
	private int[] premiumcapacity;
	private int[] economycapacity;
	private int[] queue;
	private int[] refused;
	private int[] totalbooking;
	private int[] available;
	
	private JPanel displayPanel;
	private JPanel operationPanel;
	private JPanel logPanel;
	
	private Chartpanel cp;
	private JScrollPane jscroll;
	private JScrollPane jscroll2;
	private JTabbedPane jtabbed;
	
	private JPanel pnlLeft;
	private JPanel pnlTop;
	private JPanel pnlRight;
	
	private JButton runSimulation;
	private JButton showChart;
	private JButton customInput;
	private JButton resetAll;
	
	private JTextArea txtAreaDisplay;
	private JTextArea txtAreaDisplay2;
	
	private JTextField txtRngSeed;
	private JTextField txtDailyMean;
	private JTextField txtQueueSize;
	private JTextField txtCancellation;
	private JTextField txtFirst;
	private JTextField txtBusiness;
	private JTextField txtPremium;
	private JTextField txtEconomy;
	
	private JLabel simulationLabel;
	private JLabel progressLabel;
	private JLabel fareLabel;
	private JLabel rngSeed;
	private JLabel dailyMean;
	private JLabel queueSize;
	private JLabel cancellation;
	private JLabel first;
	private JLabel business;
	private JLabel premium;
	private JLabel economy;
	
	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
		firstcapacity =  new int [Constants.DURATION+1];
		businesscapacity = new int [Constants.DURATION+1];
		premiumcapacity = new int [Constants.DURATION+1];
		economycapacity = new int [Constants.DURATION+1];
		queue = new int [Constants.DURATION+1];
		refused = new int [Constants.DURATION+1];
		totalbooking = new int [Constants.DURATION+1];
		available = new int [Constants.DURATION+1];
	}
	
	private JPanel createJPanel(){
		JPanel jp = new JPanel();
		return jp;
	}
	
	private JTextField createTextField() {
		JTextField jtxtfield = new JTextField(15);
		jtxtfield.setPreferredSize(new Dimension(15,30));
		return jtxtfield;
	}
	
	private JLabel createLabel(String label) {
		JLabel jlabel = new JLabel(label);
		jlabel.setFont(new Font("Arial", Font.PLAIN, 20));
		return jlabel;
	}
	
	private JButton createJButton(String button) {
		JButton jb = new JButton(button);
		jb.addActionListener(this);
		jb.setFont(new Font("Arial", Font.PLAIN, 18));
		return jb;
	}

	private JTextArea createTextArea() {
		JTextArea jta = new JTextArea(); 
		jta.setEditable(false);
		jta.setLineWrap(true);
		jta.setFont(new Font("Arial",Font.PLAIN, 15));
		jta.setBorder(BorderFactory.createEtchedBorder());
		return jta;
	}
	
	private void createGUI() {
		setSize(WIDTH, HEIGHT);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLayout(new BorderLayout());
	   
	    logCustomInput = new String [9];
	    
	    jtabbed = new JTabbedPane();
	    
	    
	    simulationLabel = createLabel("Simulation");
	    progressLabel = createLabel("Progress");
	    fareLabel = createLabel("Fare Class");
	    rngSeed = createLabel("RNG Seed:");
	    dailyMean = createLabel("Daily Mean:");
	    queueSize = createLabel("Queue Size:");
	    cancellation = createLabel("Cancellation:");
	    first = createLabel("First:");
	    business = createLabel("Business:");
	    premium = createLabel("Premium:");
	    economy = createLabel("Economy:");
	
	    runSimulation = createJButton("Run Simulation");
	    showChart = createJButton("Chart1");
	    customInput = createJButton("Default");
	    showChart.setEnabled(false);
	    resetAll = createJButton("Reset");
	    
	    txtRngSeed = createTextField();
	    txtDailyMean = createTextField();
	    txtQueueSize = createTextField();
	    txtCancellation = createTextField();
	    txtFirst = createTextField();
	    txtBusiness = createTextField();
	    txtPremium = createTextField();
	    txtEconomy = createTextField();
	    
	    displayPanel = createJPanel();
	    operationPanel = createJPanel();
	    logPanel = createJPanel();
	     
	    pnlLeft = createJPanel();
	    pnlTop = createJPanel();
	    pnlRight = createJPanel();
	  
	    txtAreaDisplay = createTextArea();
	    txtAreaDisplay2 = createTextArea();
	    
	    displayPanel.setLayout(new BorderLayout());
	    logPanel.setLayout(new BorderLayout());
	    
	    jscroll = addScrollPane(txtAreaDisplay);  
	    jscroll2 = addScrollPane(txtAreaDisplay2);
	    
	    displayPanel.add(jscroll,BorderLayout.CENTER);
	    logPanel.add(jscroll2, BorderLayout.CENTER);
	    
	    jtabbed.add("Main", displayPanel);
	    jtabbed.add("Logs", logPanel);
	    
	    layoutOperationPanel();
	    
	    this.getContentPane().add(pnlTop, BorderLayout.NORTH);
	    this.getContentPane().add(jtabbed, BorderLayout.CENTER);
	    this.getContentPane().add(pnlLeft, BorderLayout.WEST);
	    this.getContentPane().add(operationPanel, BorderLayout.SOUTH);
	    this.getContentPane().add(pnlRight, BorderLayout.EAST);
	    repaint(); 
	    this.setVisible(true);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	}


	@Override
	public void run() {
		createGUI();
	}

	private void layoutOperationPanel() {
		GridBagLayout layout = new GridBagLayout();
	    operationPanel.setLayout(layout);
	    //add components to grid
	    GridBagConstraints constraints = new GridBagConstraints(); 
	    
	    //Defaults
	    constraints.fill = GridBagConstraints.NONE;
	    constraints.anchor = GridBagConstraints.WEST;
	    constraints.weightx = 1.0;
	    constraints.weighty = 1.0;
	    constraints.insets = new Insets(4, 4, 4, 4);
	    
	    addToPanel(operationPanel,simulationLabel,constraints,1,0,1,1);
	    addToPanel(operationPanel,fareLabel,constraints,6,0,1,1);
	    constraints.anchor = GridBagConstraints.CENTER;
	    addToPanel(operationPanel,progressLabel,constraints,8,0,1,1);
	    
	    addToPanel(operationPanel,progressLabel,constraints,11,0,2,1);
	    
	    addToPanel(operationPanel,rngSeed,constraints,0,1,1,1);
	    
	    addToPanel(operationPanel,txtRngSeed,constraints,1,1,1,1);
	    addToPanel(operationPanel,dailyMean,constraints,0,2,1,1);
	    addToPanel(operationPanel,txtDailyMean,constraints,1,2,1,1);
	    addToPanel(operationPanel,queueSize,constraints,0,3,1,1);
	    addToPanel(operationPanel,txtQueueSize,constraints,1,3,1,1);
	    addToPanel(operationPanel,cancellation,constraints,0,4,1,1);
	    addToPanel(operationPanel,txtCancellation,constraints,1,4,1,1);
	   
	    addToPanel(operationPanel,first,constraints,5,1,1,1);
	    addToPanel(operationPanel,txtFirst,constraints,6,1,1,1);
	    addToPanel(operationPanel,business,constraints,5,2,1,1);
	    addToPanel(operationPanel,txtBusiness,constraints,6,2,1,1);
	    addToPanel(operationPanel,premium,constraints,5,3,1,1);
	    addToPanel(operationPanel,txtPremium,constraints,6,3,1,1);
	    addToPanel(operationPanel,economy,constraints,5,4,1,1);
	    addToPanel(operationPanel,txtEconomy,constraints,6,4,1,1);
	    
	    constraints.fill = GridBagConstraints.BOTH;
	    
	    addToPanel(operationPanel,runSimulation,constraints,11,1,1,1);
	    addToPanel(operationPanel,showChart,constraints,11,2,1,1);
	    addToPanel(operationPanel,customInput,constraints,11,3,1,1);
	    addToPanel(operationPanel,resetAll,constraints,11,4,1,1);
	}
	
	/**
     * 
     * A convenience method to add a component to given grid bag
     * layout locations.
     *
     * @param c the component to add
     * @param constraints the grid bag constraints to use
     * @param x the x grid position
     * @param y the y grid position
     * @param w the grid width
     * @param h the grid height
     */
	private void addToPanel(JPanel jp,Component c, GridBagConstraints constraints, int x, int y, int w, int h) {  
	    constraints.gridx = x;
	    constraints.gridy = y;
	    constraints.gridwidth=w;
	    constraints.gridheight=h;
	    jp.add(c, constraints);
	}

	/**
	 * 
	 * @param index <code>int</code> Index of day flights
	 * @param first <code>int</code> first classes values
	 * @param business <code>int</code> business classes values
	 * @param premium <code>int</code> premium classes values
	 * @param economy <code>int</code> economy classes values
	 * @param queue <code>int</code> queue values
	 * @param refused <code>int</code> refused values
	 * @param totalbooking <code>int</code> totalbooking values
	 * @param available <code>int</code> available values
	 */
	public void getCharOutput(int index, int first, int business, int premium, int economy, int queue, int refused,
			int totalbooking, int available) {
		
		this.firstcapacity[index] = first;
		this.businesscapacity[index] = business;
		this.premiumcapacity[index] = premium;
		this.economycapacity[index] = economy;
		this.queue[index] = queue;
		this.refused[index] = refused;
		this.totalbooking[index] = totalbooking;
		this.available[index] = available;
	
	}
	 
	/**
	 * Method to Read the data in Log Files
	 * @param str <code>String</code> message of every data in Log Files
	 */
	public void getLogOutput(String str) {
		txtAreaDisplay.append(str);
		txtAreaDisplay2.append(str);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//Get event source 
		Object src=e.getSource(); 
		JButton btnGetText = (JButton) e.getSource();
		
		if(src==runSimulation) {
			txtAreaDisplay.setText("");
			showChart.setEnabled(false);
			
			refreshPaint();
			
			if(!txtFirst.getText().trim().isEmpty()) {
				checkProbabilityTotalValues();
			}	
			if(txtRngSeed.isEditable()) {
				if (!emptyInput()) {
					JOptionPane.showMessageDialog(this, "Null inputs are not allowed", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!validateInput()) {
					JOptionPane.showMessageDialog(this, "Alphabetic or Unknown inputs are not allowed", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!validateValuesInput()) {
					JOptionPane.showMessageDialog(this, "Negative inputs or zero are not allowed", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!validateProbabilityInput()) {
					JOptionPane.showMessageDialog(this, "Total Probability of Fare Classes should not exceed 1", "Error", JOptionPane.ERROR_MESSAGE);
				} else if(!validateProbabilityMax()) {
					JOptionPane.showMessageDialog(this, "The Total Probability of Fare Classes should be 1","Error",JOptionPane.ERROR_MESSAGE);
				} else if(!validateProbabilityCancelInput()) {
					JOptionPane.showMessageDialog(this, "Cancel Probability is not less than 0 or more than 1", "Cancel Probability", JOptionPane.ERROR_MESSAGE);
				} else {
					txtAreaDisplay.setText("");
					txtAreaDisplay2.setText("");
					JOptionPane.showMessageDialog(this, "The Simulation is Running...","Running...", JOptionPane.INFORMATION_MESSAGE);
					
					setCustomLogInput();
					
					SimulationRunner.main(logCustomInput);
					cp = new Chartpanel("Simulation", firstcapacity, businesscapacity, premiumcapacity, economycapacity, 
							queue, refused, totalbooking, available);
				
					showChart.setEnabled(true);
				}
				
			} else {
				txtAreaDisplay.setText("");
				txtAreaDisplay2.setText("");
				JOptionPane.showMessageDialog(this, "The Simulation is Running...","Running...", JOptionPane.INFORMATION_MESSAGE);
				String[] args = {};
				SimulationRunner.main(args);
					cp = new Chartpanel("Simulation", firstcapacity, businesscapacity, premiumcapacity, economycapacity, 
							queue, refused, totalbooking, available);
				
				showChart.setEnabled(true);
			}
			
		} else if(src==showChart) {
			if(btnGetText.getText()=="Chart1") {
				showChart.setText("Chart2");
				cp.simulationChartOption("Simulation 1");
				
			} else {
				showChart.setText("Chart1");
				cp.simulationChartOption("Simulation 2");
			}
			showChartPanel();
			
		} else if(src==customInput) {
			txtAreaDisplay.setText("");
			if(btnGetText.getText()=="Custom") {
				customInput.setText("Default");
				enabledTextField();
				this.showChart.setEnabled(false);
			} else {
				customInput.setText("Custom");
				setDefaultLogInput();
				disabledTextField();
				this.showChart.setEnabled(false);
			}
			showChart.setText("Chart1");
			refreshPaint();
			
		} else if(src==resetAll) {
			refreshPaint();
			this.showChart.setEnabled(false);
			resetAll();
		}
	}
	
	/**
	 * Method to refresh and show Chart in the Panel
	 */
	private void showChartPanel() {
		this.getContentPane().remove(jtabbed);
		this.jtabbed.removeAll();
		this.jtabbed.add("Main",cp);
		this.logPanel.add(jscroll2,BorderLayout.CENTER);
		this.jtabbed.add("Logs", logPanel);
		
		this.getContentPane().add(jtabbed,BorderLayout.CENTER);
		this.getContentPane().revalidate();
		this.getContentPane().repaint();
	}
	
	/**
	 * Method to set Custom setting to Log Files
	 */
	private void setCustomLogInput() {
		double sd_booking;
		logCustomInput[0] = txtRngSeed.getText();
		logCustomInput[1] = txtQueueSize.getText();
		logCustomInput[2] = txtDailyMean.getText();
		sd_booking = 0.33 * Double.parseDouble(txtDailyMean.getText());
		logCustomInput[3]= String.valueOf(sd_booking);
		logCustomInput[4] = txtFirst.getText();
		logCustomInput[5] = txtBusiness.getText();
		logCustomInput[6] = txtPremium.getText();
		logCustomInput[7] = txtEconomy.getText();
		logCustomInput[8] = txtCancellation.getText();
	}
	
	/**
	 * Method to set Default setting to Log Files
	 */
	private void setDefaultLogInput() {
		txtRngSeed.setText(String.valueOf(Constants.DEFAULT_SEED));
		txtDailyMean.setText(String.valueOf(Constants.DEFAULT_DAILY_BOOKING_MEAN));
		txtQueueSize.setText(String.valueOf(Constants.DEFAULT_MAX_QUEUE_SIZE));
		txtCancellation.setText(String.valueOf(Constants.DEFAULT_CANCELLATION_PROB));
		txtFirst.setText(String.valueOf(Constants.DEFAULT_FIRST_PROB));
		txtBusiness.setText(String.valueOf(Constants.DEFAULT_BUSINESS_PROB));
		txtPremium.setText(String.valueOf(Constants.DEFAULT_PREMIUM_PROB));
		txtEconomy.setText(String.valueOf(Constants.DEFAULT_ECONOMY_PROB));
	}
	
	/**
	 * Method  to retrieve the probability of Fare Classes
	 * 
	 */
	private void checkProbabilityTotalValues() {
		double prob_First = Double.parseDouble(txtFirst.getText());
		double prob_Business = Double.parseDouble(txtBusiness.getText());
		double prob_Premium = Double.parseDouble(txtPremium.getText());
		double prob_Economy = Double.parseDouble(txtEconomy.getText());
		prob_Cancel = Double.parseDouble(txtCancellation.getText());
	    prob_Total = prob_First + prob_Business + prob_Premium + prob_Economy;
	}
	
	/**
	 * Method to check the Total Probability of Fare Classes
	 *
	 * @return true it is valid, otherwise false;
	 */
	private boolean validateProbabilityInput() {
	
	    if(prob_Total>1 || prob_Total<0){
	    	return false;
	    } else {
	    	return true;
	    }
	    
	}
	
	/**
	 * Method to check Total Probability of Fare Classes is equal to 1
	 * 
	 * @return true if it is 1, otherwise false;
	 */
	private boolean validateProbabilityMax() {
		
	    if(prob_Total==1){
	    	return true;
	    } else {
	    	return false;
	    }
	    
	}
	
	/**
	 * Method to check Cancellation Probability
	 * 
	 * @return true if probability is not more than 1 or less than 0, otherwise false
	 */
	private boolean validateProbabilityCancelInput() {
		if (prob_Cancel>1 || prob_Cancel<0) {
	    	return false;
	    }
		return true;
	}
	
	/**
	 * Method to check the null inputs in JTextField
	 * 
	 * @return true if not null, otherwise false
	 */
	private boolean emptyInput() {
		//Check empty input
		if(txtRngSeed.getText().trim().isEmpty() || txtDailyMean.getText().trim().isEmpty() || txtQueueSize.getText().trim().isEmpty() || 
		txtCancellation.getText().trim().isEmpty() || txtFirst.getText().trim().isEmpty() || txtBusiness.getText().trim().isEmpty() || 
		   txtPremium.getText().trim().isEmpty() || txtEconomy.getText().trim().isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Method to restrict the numeric values in JTextField whether it is numeric or double
	 * 
	 * @return true it is correct, otherwise false
	 */
	private boolean validateInput() {
		//Check interger input
		if(!validateInteger(txtRngSeed.getText().trim()) || !validateInteger(txtDailyMean.getText().trim()) || !validateInteger(txtQueueSize.getText().trim()) ||
				!validateInteger(txtCancellation.getText().trim()) || !validateInteger(txtFirst.getText().trim()) || !validateInteger(txtBusiness.getText().trim()) || 
				!validateInteger(txtPremium.getText().trim()) || !validateInteger(txtEconomy.getText().trim())){
			return false;
		}
		return true;
	}
	
	/**
	 * Method to check Numeric values in JTextField
	 * 
	 * @return true if is numeric, otherwise false
	 */
	private boolean validateValuesInput() {
		if(Integer.parseInt(txtRngSeed.getText())<=0 || Integer.parseInt(txtQueueSize.getText())<0 || Double.parseDouble(txtDailyMean.getText())<=0 ||
				 Double.parseDouble(txtCancellation.getText())<0 || Double.parseDouble(txtFirst.getText())<0 || Double.parseDouble(txtBusiness.getText())<0 || 
					Double.parseDouble(txtPremium.getText())<0 || Double.parseDouble(txtEconomy.getText())<0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Method to check validity of numeric values in JTextField
	 * 
	 * @param str <code>String</code> string values of JTextField
	 * @return true if  values is integer, otherwise false
	 */
	private boolean validateInteger(String str) {
		boolean valid = true;
		for(int i = 0; i<str.length(); i++) {
			if(Character.isDigit(str.charAt(i))) {
				valid = true;
			} else {
				valid = false;
				break;
			}
		}
		valid = isDouble(str);
		return valid;
	}
	
	/**
	 * Method to refresh the paint in GUI
	 */
	private void refreshPaint() {
		this.getContentPane().removeAll();
		this.jtabbed.removeAll();
		this.jtabbed.add("Main",displayPanel);
		this.jtabbed.add("Logs", logPanel);
		this.getContentPane().add(pnlTop, BorderLayout.NORTH);
	    this.getContentPane().add(jtabbed, BorderLayout.CENTER);
	    this.getContentPane().add(pnlLeft, BorderLayout.WEST);
	    this.getContentPane().add(operationPanel, BorderLayout.SOUTH);
	    this.getContentPane().add(pnlRight, BorderLayout.EAST);
		this.getContentPane().revalidate();
		this.getContentPane().repaint();
	
	}
	
	/**
	 * To check the values in textField is double
	 * 
	 * @param str <code>String</code> string values of JTextField
	 * @return true if values is Double, otherwise false;
	 */
	private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	/**
	 * 
	 * @param jta <code>String</code> Name of JScrollPane
	 * @return new JScrollPane
	 */
	private JScrollPane addScrollPane(JTextArea jta) {
		JScrollPane jpane = new JScrollPane(jta);
		jpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		return jpane;
	}
	
	/**
	 * Method to reset back to original status
	 */
	private void resetAll() {
		enabledTextField();
		showChart.setEnabled(false);
		customInput.setText("Default");
		txtRngSeed.setText("");
		txtDailyMean.setText("");
		txtQueueSize.setText("");
		txtCancellation.setText("");
		txtFirst.setText("");
		txtBusiness.setText("");
		txtPremium.setText("");
		txtEconomy.setText("");
		txtAreaDisplay.setText("");
		txtAreaDisplay2.setText("");
		displayPanel.removeAll();
		displayPanel.add(txtAreaDisplay, BorderLayout.CENTER);
		displayPanel.repaint();
		logPanel.removeAll();
		logPanel.add(txtAreaDisplay2, BorderLayout.CENTER);
		logPanel.repaint();
	}
	
	/**
	 * Method to enable textfield
	 */
	private void enabledTextField() {
		txtRngSeed.setEditable(true);
		txtDailyMean.setEditable(true);
		txtQueueSize.setEditable(true);
		txtCancellation.setEditable(true);
		txtFirst.setEditable(true);
		txtBusiness.setEditable(true);
		txtPremium.setEditable(true);
		txtEconomy.setEditable(true);
	}
	
	/**
	 * Method to disable textfield
	 */
	private void disabledTextField() {
		txtRngSeed.setEditable(false);
		txtDailyMean.setEditable(false);
		txtQueueSize.setEditable(false);
		txtCancellation.setEditable(false);
		txtFirst.setEditable(false);
		txtBusiness.setEditable(false);
		txtPremium.setEditable(false);
		txtEconomy.setEditable(false);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new GUISimulator("Aircraft Simulator"));

	}

}
