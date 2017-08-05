package asgn2Simulators;


import java.awt.BorderLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

@SuppressWarnings("serial")
public class Chartpanel extends JPanel {

    //private static final String TITLE = "Simulation Results";
    private String title;
    private int[] first_;
    private int[] business_;
    private int[] premium_;
    private int[] economy_;
    private int[] queue_;
    private int[] refused_;
    private int[] totalbooking_;
    private int[] available_;
    
    private boolean optionChart;
    
    /**
     * Constructor shares the work with the run method. 
     * @param title Frame display title
     */
    public Chartpanel(final String title, int [] first, int [] business, int []  premium, int [] economy, 
    		int [] queue, int [] refused, int [] totalbooking, int [] available) {
        super();
        this.title = title;
        optionChart = false;
        //assign the array values 
        first_ =  new int [Constants.DURATION+1];
		business_ = new int [Constants.DURATION+1];
		premium_ = new int [Constants.DURATION+1];
		economy_ = new int [Constants.DURATION+1];
		queue_ = new int [Constants.DURATION+1];
		refused_ = new int [Constants.DURATION+1];
		totalbooking_ = new int [Constants.DURATION+1];
		available_ = new int [Constants.DURATION+1];
		
        for(int i=0; i<=Constants.DURATION; i++) {
        	//assign values into arrays
        	first_[i] = first[i];
        	business_[i] = business[i];
        	premium_[i] = premium[i];
        	economy_[i] = economy[i];
        	queue_[i] = queue[i];
        	refused_[i] = refused[i];
        	totalbooking_[i] = totalbooking[i];
        	available_[i] = available[i];
        	
        }
        
        final TimeSeriesCollection dataset = createTimeSeriesData(); 
        JFreeChart chart = createChart(dataset);
        this.add(new ChartPanel(chart), BorderLayout.CENTER);
    }
    
    /**
     * Method to change the chart 1 and 2
     * @param str <code>String</code> Option of Chart in String .
     */
    public void simulationChartOption (String str) {
    	//Option changes to show Chart 1 and 2
    	this.title = str;
    	optionChart = !optionChart;
    	this.removeAll();
    	final TimeSeriesCollection dataset = createTimeSeriesData(); 
        JFreeChart chart = createChart(dataset);
        this.add(new ChartPanel(chart), BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
    
    /**
	 * @return collection of time series for the plot
	 */
	private TimeSeriesCollection createTimeSeriesData() {
		TimeSeriesCollection tsc = new TimeSeriesCollection(); 
		TimeSeries total = new TimeSeries("Total");
		TimeSeries available = new TimeSeries("Seats Available");
		TimeSeries economy = new TimeSeries("Economy"); 
		TimeSeries business = new TimeSeries("Business");
		TimeSeries premium = new TimeSeries("Premium");
		TimeSeries first = new TimeSeries("First");
		TimeSeries queueSize = new TimeSeries("Queue");
		TimeSeries refusedSize = new TimeSeries("Refused");
		
		//Base time, data set up - the calendar is needed for the time points
		Calendar cal = GregorianCalendar.getInstance();	
	
		for(int i=21; i<=Constants.DURATION; i++) {
			cal.set(2016,0,i,6,0);
			Date timePoint = cal.getTime();
			
			business.add(new Day(timePoint), business_[i]);
			economy.add(new Day(timePoint), economy_[i]);
			premium.add(new Day(timePoint), premium_[i]);
			first.add(new Day(timePoint),first_[i]);
			available.add(new Day(timePoint),available_[i]);
			total.add(new Day(timePoint),totalbooking_[i]);
			queueSize.add(new Day(timePoint), queue_[i]);
			refusedSize.add(new Day(timePoint), refused_[i]);
			
		}
	
		//Collection
		if(optionChart) {
			tsc.addSeries(total);
			tsc.addSeries(economy);
			tsc.addSeries(premium);
			tsc.addSeries(first);
			tsc.addSeries(business);
			tsc.addSeries(available);
		} else {
			tsc.addSeries(queueSize);
			tsc.addSeries(refusedSize);
		}
		return tsc; 
	}


    /**
     * Helper method to deliver the Chart - currently uses default colours and auto range 
     * @param dataset TimeSeriesCollection for plotting 
     * @returns chart to be added to panel 
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            this.title, "Day", "Passengers", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setAutoRange(true);
        return result;
    }

 
}