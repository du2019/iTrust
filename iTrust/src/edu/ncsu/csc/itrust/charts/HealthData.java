package edu.ncsu.csc.itrust.charts;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.links.CategoryItemLinkGenerator;
import de.laures.cewolf.tooltips.CategoryToolTipGenerator;
import edu.ncsu.csc.itrust.beans.HealthRecord;


/**
 * This class handles the data for charting height and weight information in
 * CeWolf/JFreeChart. This class implements DatasetProducer,
 * CategoryToolTipGenerator, CategoryItemLinkGenerator, and Serializable.
 * 
 * @author Fu
 *
 */
public class HealthData implements DatasetProducer, CategoryToolTipGenerator, CategoryItemLinkGenerator, Serializable {
	// Hardcoded months array to make implementation simpler for Adverse Event charts
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -41620615767475576L;

	// Number of quarters to chart. Set to 12 quarters for 3 years.
	private final int historyLength = 12;
	
    private String[] quarters;

    // Initialize the values for each month to 0
    private double[] values;
    
    // Number of entries per quarter, used to calculate average.
    private int[] numEntries;
    
    //private String[] qLabels = {"Jan-Mar","Apr-Jun","Jul-Sep","Oct-Dec"};
    
    // This will be the list of health records
    private List<HealthRecord> healthRecords = new LinkedList<HealthRecord>();
    
    // This will be the name of the prescription or immunization under analysis
    private String codeName;
    
    /**
     * Lets the JSP know if there is any data to be displayed.
     * 
     * @return False if there is no data to be displayed.
     */
    public boolean hasData() {
    	Date threeYearsAgo = new Date();
    	Date recDate;
    	String dateStr;
    	DateFormat year = new SimpleDateFormat("yyyy");
    	DateFormat rest = new SimpleDateFormat("/MM/dd HHmma");
    	DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HHmma");
		dateStr = year.format(threeYearsAgo);
		dateStr = "" + (Integer.parseInt(dateStr) - 3);
		dateStr += rest.format(threeYearsAgo);
		try {
			threeYearsAgo = sdf.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
    	
    	for (HealthRecord hr : healthRecords) {
    		recDate = new Date(hr.getDateRecorded().getTime());
    		if (recDate.before(threeYearsAgo))
    			continue;
    		else
    			return true;
    	}

    	return false;
    }
    
    
    /**
     * Called from the JSP to initialize the list of HealthRecords needed
     * to produce the desired chart.
     * 
     * @param hRecs Health records for the patient being viewed.
     * @param name Type of data that is being graphed (originally Height, Weight, and BMI).
     */
    public void initializeHealthRecords(List<HealthRecord> hRecs, String name){
    	healthRecords = hRecs;
    	this.codeName = name;
    	quarters = new String[historyLength];
    	values = new double[historyLength];
    	numEntries = new int[historyLength];
    	
    	Calendar cal = Calendar.getInstance();

    	cal.add(Calendar.MONTH, -3*(historyLength-1));
    	
    	int startMonth = cal.get(Calendar.MONTH);
    	int startQ = startMonth / 3;
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yy");

    	for (int i = 0; i < historyLength; i++) {
    		quarters[i] = "Q" + ((startQ + i)%4 + 1) + " '" + sdf.format(cal.getTime());
    		values[i] = 0;
    		numEntries[i] = 0;
    		cal.add(Calendar.MONTH, 3);
    	}
    }
    
    /**
     * This method parses the list of Adverse Event Beans to initialize the
     * chart dataset.
     * @param Map parameters passed from CeWolf (though not actually used locally)
     * @return A dataset containing information to be graphed
     * @throws DatasetProduceException
     */
    @SuppressWarnings("unchecked")
	public Object produceDataset(Map params) throws DatasetProduceException {
    	// The DefaultCategoryDataset is used for bar charts.
    	// This dataset class may change based on the type of chart you wish to produce.
        DefaultCategoryDataset dataset = new DefaultCategoryDataset(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * @see java.lang.Object#finalize()
			 */
			protected void finalize() throws Throwable {
				super.finalize();
			}
        };
        
        Calendar cur = Calendar.getInstance();
        int curQuarter = cur.get(Calendar.MONTH) / 3;
        
        for(HealthRecord hRecord : healthRecords) {
        	Calendar cal = Calendar.getInstance();
			cal.setTime(hRecord.getDateRecorded());
			int yearOfReport = cal.get(Calendar.YEAR);
        	int monthOfReport = cal.get(Calendar.MONTH);
        	int quarterOfReport = monthOfReport / 3;
        	int quarter = historyLength - 4*(cur.get(Calendar.YEAR)-yearOfReport) - (curQuarter - quarterOfReport) - 1;
        	// Skip entry if it is out of the history range.
        	if (quarter < 0)
        		continue;
        	
        	// Calculate the average for the quarter.
        	double newValue = 0;
        	if (codeName.equals("Height"))
        		newValue = hRecord.getHeight();
        	else if (codeName.equals("Weight"))
        		newValue = hRecord.getWeight();
        	else if (codeName.equals("BMI"))
        		newValue = (hRecord.getWeight()*703)/(hRecord.getHeight()*hRecord.getHeight());
        	
        	values[quarter] = (values[quarter]*numEntries[quarter] + newValue)/(numEntries[quarter] + 1);
        	numEntries[quarter]++;
        }
        
        // For each month, add the monthly values to the dataset for
        // producing the chart.
        for(int i = 0; i < historyLength; i++) {
        	dataset.addValue(values[i],codeName,quarters[i]);
        }

        return dataset;
    }

    /**
     * This producer's data is invalidated after 5 seconds. By this method the
     * producer can influence Cewolf's caching behavior the way it wants to.
     */
	@SuppressWarnings("unchecked")
	public boolean hasExpired(Map params, Date since) {		
		return (System.currentTimeMillis() - since.getTime())  > 5000;
	}

	/**
	 * @return A unique ID for this DatasetProducer
	 */
	public String getProducerId() {
		return "HealthData DatasetProducer";
	}

    /**
     * @return A link target for a special data item.
     */
    public String generateLink(Object data, int series, Object category) {
        return quarters[series];
    }
    
	/**
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * @see org.jfree.chart.tooltips.CategoryToolTipGenerator#generateToolTip(CategoryDataset, int, int)
	 */
	public String generateToolTip(CategoryDataset arg0, int series, int arg2) {
		return quarters[series];
	}
}
