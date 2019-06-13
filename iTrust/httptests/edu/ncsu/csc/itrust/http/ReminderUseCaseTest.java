package edu.ncsu.csc.itrust.http;

import java.util.Calendar;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.DBBuilder;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class ReminderUseCaseTest extends iTrustHTTPTest {
	protected void setUp() throws Exception {
		super.setUp(); // clear tables is called in super
		gen.standardData();
	}
	
	public void testAdminSendReminder() throws Exception {
		// Create DB for this test case
		String DIR = "sql/data";
		DAOFactory factory = TestDAOFactory.getTestInstance();
		new DBBuilder(factory).executeSQLFile(DIR + "/clearMessages.sql");
		new DBBuilder(factory).executeSQLFile(DIR + "/clearAppointments.sql");
		new DBBuilder(factory).executeSQLFile(DIR + "/reminderCase1.sql");
		
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Send Appointment Reminders").click();
		
		wr.getForms()[0].setParameter("threshold", "3");
		wr = wr.getForms()[0].submit();
		
		// Checks that message are sent
		assertTrue(wr.getText().contains("4 message(s) sent."));
	}
	
	public void testAdminCheckSystemReminderOutbox() throws Exception {
		// Create DB for this test case
		String DIR = "sql/data";
		DAOFactory factory = TestDAOFactory.getTestInstance();
		new DBBuilder(factory).executeSQLFile(DIR + "/clearMessages.sql");
		new DBBuilder(factory).executeSQLFile(DIR + "/clearAppointments.sql");
		new DBBuilder(factory).executeSQLFile(DIR + "/reminderCase2.sql");
		
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Send Appointment Reminders").click();
		wr.getForms()[0].setParameter("threshold", "2");
		wr = wr.getForms()[0].submit();

		Calendar now = Calendar.getInstance();
		String time = now.get(Calendar.YEAR)+"-"+((now.get(Calendar.MONTH)+1)<10?"0"+(now.get(Calendar.MONTH)+1):(now.get(Calendar.MONTH)+1))+"-"+(now.get(Calendar.DAY_OF_MONTH)<10?"0"+now.get(Calendar.DAY_OF_MONTH):now.get(Calendar.DAY_OF_MONTH))+" "+(now.get(Calendar.HOUR_OF_DAY)<10?"0"+now.get(Calendar.HOUR_OF_DAY):now.get(Calendar.HOUR_OF_DAY))+":"+(now.get(Calendar.MINUTE)<10?"0"+now.get(Calendar.MINUTE):now.get(Calendar.MINUTE));
		
		wr = wr.getLinkWith("System Reminder Outbox").click();
		
		// Check order
		assertTrue(wr.getText().contains("Andy Programmer"));
		assertTrue(wr.getText().contains("Reminder: upcoming appointment in 1 day(s)"));
		assertTrue(wr.getTables()[1].getRows()[1].getText().contains(time));

		assertTrue(wr.getText().contains("Andy Programmer"));
		assertTrue(wr.getText().contains("Reminder: upcoming appointment in 2 day(s)"));
		assertTrue(wr.getTables()[1].getRows()[2].getText().contains(time));
		
		assertTrue(wr.getText().contains("Baby Programmer"));
		assertTrue(wr.getText().contains("Reminder: upcoming appointment in 1 day(s)"));
		assertTrue(wr.getTables()[1].getRows()[3].getText().contains(time));
	}
	
	public void testAdminSystemReminderOutboxDetail() throws Exception {
		// Create DB for this test case
		String DIR = "sql/data";
		DAOFactory factory = TestDAOFactory.getTestInstance();
		new DBBuilder(factory).executeSQLFile(DIR + "/clearMessages.sql");
		new DBBuilder(factory).executeSQLFile(DIR + "/clearAppointments.sql");
		new DBBuilder(factory).executeSQLFile(DIR + "/reminderCase3.sql");
		
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Send Appointment Reminders").click();
		wr.getForms()[0].setParameter("threshold", "2");
		wr = wr.getForms()[0].submit();

		Calendar now = Calendar.getInstance();
		String time = now.get(Calendar.YEAR)+"-"+((now.get(Calendar.MONTH)+1)<10?"0"+(now.get(Calendar.MONTH)+1):(now.get(Calendar.MONTH)+1))+"-"+(now.get(Calendar.DAY_OF_MONTH)<10?"0"+now.get(Calendar.DAY_OF_MONTH):now.get(Calendar.DAY_OF_MONTH))+" "+(now.get(Calendar.HOUR_OF_DAY)<10?"0"+now.get(Calendar.HOUR_OF_DAY):now.get(Calendar.HOUR_OF_DAY))+":"+(now.get(Calendar.MINUTE)<10?"0"+now.get(Calendar.MINUTE):now.get(Calendar.MINUTE));
		
		wr = wr.getLinkWith("System Reminder Outbox").click();
		wr = wr.getLinkWith("Read").click();
		
		// Check message details
		assertTrue(wr.getTables()[0].getRows()[0].getText().contains("To: Andy Programmer"));
		assertTrue(wr.getTables()[0].getRows()[1].getText().contains("Subject: Reminder: upcoming appointment in 1 day(s)"));
		assertTrue(wr.getTables()[0].getRows()[2].getText().contains(time));
		String expectedTime = ""+((now.get(Calendar.MONTH)+1)<10?"0"+(now.get(Calendar.MONTH)+1):(now.get(Calendar.MONTH)+1))+"-"+((now.get(Calendar.DAY_OF_MONTH)+1)<10?"0"+(now.get(Calendar.DAY_OF_MONTH)+1):(now.get(Calendar.DAY_OF_MONTH)+1))+"-"+now.get(Calendar.YEAR);
		System.out.println(expectedTime);
		assertTrue(wr.getTables()[1].getRows()[1].getText().contains("You have an appointment on 02:00 PM, "+expectedTime+" with Dr. Kelly Doctor."));
	}
	
	public void testAdminSystemNoSystemReminders() throws Exception{
		// Create DB for this test case
		String DIR = "sql/data";
		DAOFactory factory = TestDAOFactory.getTestInstance();
		new DBBuilder(factory).executeSQLFile(DIR + "/clearMessages.sql");
		new DBBuilder(factory).executeSQLFile(DIR + "/clearAppointments.sql");
		
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		
		wr = wr.getLinkWith("Send Appointment Reminders").click();
		wr.getForms()[0].setParameter("threshold", "2");
		wr = wr.getForms()[0].submit();
		
		// Checks that message are sent
		assertTrue(wr.getText().contains("0 message(s) sent."));
	}
}
