package edu.ncsu.csc.itrust.action;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Handles retrieving the log of record accesses for a given user Used by viewAccessLog.jsp
 * 
 * @author laurenhayward
 * 
 */
public class ActivityFeedAction {
	private TransactionDAO transDAO;
	private AuthDAO authDAO;
	private long loggedInMID;

	/**
	 * Set up
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person retrieving the logs.
	 */
	public ActivityFeedAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.transDAO = factory.getTransactionDAO();
		this.authDAO = factory.getAuthDAO();
	}

	/**
	 * Returns a list of TransactionBeans between the two dates passed as params
	 * 
	 * @param n Number of "pages" of 20 log entries to retrieve.
	 * @return list of 20*n TransactionBeans
	 * @throws DBException
	 * @throws FormValidationException
	 */
	public List<TransactionBean> getTransactions(Date time, int n) throws DBException,
			FormValidationException {
		List<TransactionBean> fullList = transDAO.getTransactionsAffecting(loggedInMID, time, 20*n+1);
		
		return fullList;
	}
	
	/**
	 * Returns an indicator of the number of days between the current date and the date passed
	 * as a parameter. Returns 0 if the dates are on the same day, 1 if the date passed in is
	 * "yesterday", 2 otherwise.
	 * 
	 * @param d1 First date
	 * @param d2 Second date
	 * @return 0, 1, or 2, depending on the difference in the dates.
	 */
	public static int recent(Date d) {
		int oneDay = 24 * 60 * 60 * 1000;
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date rightNow = new Date();
		if (sdf.format(rightNow).equals(sdf.format(d)))
			return 0;
		d.setTime(d.getTime() + oneDay);
		if (sdf.format(rightNow).equals(sdf.format(d)))
			return 1;
		return 2;
	}
	
	/**
	 * Pulls Action Phrase from the associated TransactionType Enum
	 * Forms an English sentence with actor, action, and timestamp.
	 * @param actor
	 * @param timestamp
	 * @param code
	 * @return
	 */
	public String getMessageAsSentence(String actor, Timestamp timestamp, TransactionType code) {
		String result = actor + " ";
	
		for (TransactionType type : TransactionType.values()) {
			if (code.getCode() == type.getCode() && type.isPatientViewable())
				result += type.getActionPhrase();
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("h:mma.");
		switch(recent(new Date(timestamp.getTime()))) {
		case 0:
			result += " today";
			break;
		case 1:
			result += " yesterday";
			break;
		case 2:
			DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			result += " on " + sdf.format(new Date(timestamp.getTime()));
			break;
		}
		
		result += " at " + formatter.format(timestamp);
		
		
		
		return replaceNameWithYou(result);
	}
	
	private String replaceNameWithYou(String activity)
	{
		try{
			return activity.replace(authDAO.getUserName(loggedInMID), "You");
		} catch(Exception e)
		{
			return activity;
		}
	}
}
