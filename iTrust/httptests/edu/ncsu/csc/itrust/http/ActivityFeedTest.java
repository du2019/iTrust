package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Use Case 43
 */
public class ActivityFeedTest extends iTrustHTTPTest {
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testOlderActivities() throws Exception {
		gen.transactionLog6();
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		int entries = 0;
		String s = wr.getText();
		int start = s.indexOf("Activity Feed</legend>");
		int end = s.indexOf("<a href=\"home.jsp?date=");
		s = s.substring(start, end);
		// Check for 20 items
		entries = 0;
		while (s.contains("<div")) {
			entries++;
			s = s.substring(s.indexOf("<div") + 1);
		}
		assertEquals(entries,20);
		wr = wr.getLinkWith("Older Activities").click();
		s = wr.getText();
		s = s.substring(s.indexOf("<legend"), s.indexOf("</legend"));
		// Check for 40 items
		entries = 40;
		while (s.contains("<div")) {
			entries++;
			s = s.substring(s.indexOf("<div") + 1);
		}
		assertEquals(entries,40);
		wr = wr.getLinkWith("Older Activities").click();
		s = wr.getText();
		s = s.substring(s.indexOf("Activity Feed</legend>"), s.lastIndexOf("</fieldset>"));
		entries = 0;
		
		while (s.contains("<div")) {
			entries++;
			s = s.substring(s.indexOf("<div") + 1);
		}
		assertEquals(entries - 3,60);
	}
	
	public void testUpdateActivityFeed() throws Exception {
		gen.transactionLog6();
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		int entries = 0;
		wr = wr.getLinkWith("Older Activities").click();
		wr = wr.getLinkWith("Older Activities").click();
		wr = wr.getLinkWith("Refresh").click();
		
		String s = wr.getText();
		s = s.substring(s.lastIndexOf("<fieldset"), s.lastIndexOf("</fieldset"));
		
		while (s.contains("<div")) {
			entries++;
			s = s.substring(s.indexOf("<div") + 1);
		}
		assertEquals(20,entries);
	}
	
	public void testViewActivityFeed() throws Exception {
		gen.transactionLog5();
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		String s = wr.getText();

		Date d = new Date();
		d.setTime(d.getTime() - 3*24*60*60*1000);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		assertTrue(s.contains("Kelly Doctor</a> viewed your prescription report yesterday at 8:15AM."));
		assertTrue(s.contains("Andy Programmer viewed your prescription report yesterday at 9:43AM."));
		assertTrue(s.contains("Justin Time</a> created an emergency report for you yesterday at 10:04AM."));
		assertTrue(s.contains("Gandalf Stormcrow</a> edited your office visit yesterday at 11:18AM."));
		assertTrue(s.contains("FirstUAP LastUAP</a> edited your lab procedure results yesterday at 12:02PM."));
		assertTrue(s.contains("Gandalf Stormcrow</a> viewed your risk factors yesterday at 12:58PM."));
		assertTrue(s.contains("FirstUAP LastUAP</a> viewed your risk factors yesterday at 1:02PM."));
		assertTrue(s.contains("Kelly Doctor</a> viewed your risk factors yesterday at 1:15PM."));
		assertTrue(s.contains("Kelly Doctor</a> edited your office visit on " + sdf.format(d) + " at 2:23PM."));
	}
}
