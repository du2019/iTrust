package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
import junit.framework.TestCase;
import java.util.Date;
import java.util.List;

public class ActivityFeedActionTest extends TestCase {
	private ActivityFeedAction action;
	private DAOFactory factory;
	private long mid = 1L;
	
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		
		this.factory = TestDAOFactory.getTestInstance();
		this.action = new ActivityFeedAction(factory, mid);
	}
	
	public void testGetTransactions() throws FormValidationException, SQLException {
		try {
			List<TransactionBean> beans = action.getTransactions(new Date(), 1);
			assertTrue(beans.size() < 20);
		} catch (DBException e) {
			fail();
		}
	}
	
	public void testGetMessageAsSentence() {
		Date dNow = new Date();
		Timestamp tsNow = new Timestamp(dNow.getTime());
		Timestamp tsYesterday = new Timestamp(dNow.getTime() - 1000*60*60*24);
		Timestamp tsLongAgo = new Timestamp(dNow.getTime() - 1000*60*60*24*10);
		SimpleDateFormat formatter = new SimpleDateFormat();
		
		String msg;
		
		msg = action.getMessageAsSentence("", tsNow, TransactionType.PATIENT_CREATE);
		assertTrue(msg.contains(TransactionType.PATIENT_CREATE.getActionPhrase()));
		msg = action.getMessageAsSentence("", tsNow, TransactionType.PATIENT_DISABLE);
		assertTrue(msg.contains(TransactionType.PATIENT_DISABLE.getActionPhrase()));
		msg = action.getMessageAsSentence("", tsNow, TransactionType.DEMOGRAPHICS_VIEW);
		assertTrue(msg.contains(TransactionType.DEMOGRAPHICS_VIEW.getActionPhrase()));
		msg = action.getMessageAsSentence("", tsYesterday, TransactionType.DEMOGRAPHICS_EDIT);
		assertTrue(msg.contains(TransactionType.DEMOGRAPHICS_EDIT.getActionPhrase()));
		msg = action.getMessageAsSentence("", tsYesterday, TransactionType.PATIENT_HEALTH_INFORMATION_EDIT);
		assertTrue(msg.contains(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT.getActionPhrase()));
		msg = action.getMessageAsSentence("", tsLongAgo, TransactionType.PATIENT_HEALTH_INFORMATION_VIEW);
		assertTrue(msg.contains(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW.getActionPhrase()));
		msg = action.getMessageAsSentence("", tsLongAgo, TransactionType.OFFICE_VISIT_VIEW);
		assertTrue(msg.contains(TransactionType.OFFICE_VISIT_VIEW.getActionPhrase()));
	}
}
