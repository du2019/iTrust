package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class ViewMyReportRequestsTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp();
		gen.hcp0();
		gen.patient2();
	}
	
	public void testViewMyReportRequests() throws Exception{
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
	
		wr = wr.getLinkWith("My Report Requests").click();
		assertFalse(wr.getText().contains("Exception"));
		wr = wr.getLinkWith("Add a new Report Request").click();
		
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();

		WebTable table = wr.getTableStartingWithPrefix("Report Requests");
		assertTrue(table.getCellAsText(2, 4).contains("Requested"));
		WebLink[] weblinks = wr.getLinks();
		for(int i = 0; i < weblinks.length; i++) {
			if(weblinks[i].getText().equals("View")) {
				wr = weblinks[i].click();
				break;
			}
		}
		
		assertEquals("iTrust - Comprehensive Patient Report", wr.getTitle());
		
		wr = wr.getLinkWith("My Report Requests").click();
		table = wr.getTableStartingWithPrefix("Report Requests");
		assertTrue(table.getCellAsText(2, 4).contains("Viewed"));
		
		weblinks = wr.getLinks();
		for(int i = 0; i < weblinks.length; i++) {
			if(weblinks[i].getText().equals("View")) {
				wr = weblinks[i].click();
				break;
			}
		}
		
		assertEquals("iTrust - Comprehensive Patient Report", wr.getTitle());
		
		
	}

}
