package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
//import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

public class SendEmailNotificationTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.icd9cmCodes();
		gen.ndCodes();
		gen.hospitals();
		gen.hcp1();
		gen.hcp2();
		gen.hcp3();
		gen.er4();
		gen.patient9();
		
		gen.UC32Acceptance();
		gen.clearLoginFailures();
	}
	
	public void testPrescriptionRenewalEmail() throws Exception {
		
		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Potential Prescription-Renewals").click();
		assertTrue(wr.getText().contains("Darryl Thompson"));
		wr = wr.getLinkWith("Darryl Thompson").click();
		assertTrue(wr.getText().contains("Send Email Form"));
	}
	
	public void testOfficeVisitRemindersEmail() throws Exception {
		
		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Office Visit Reminders").click();
		assertTrue(wr.getText().contains("Patients Needing Visits"));
		wr = wr.getForms()[0].submit();
		assertTrue(wr.getText().contains("Darryl Thompson"));
		wr = wr.getLinkWith("Darryl Thompson").click();
		assertTrue(wr.getText().contains("Send Email Form"));
	}
	
	public void testSendAnEmail() throws Exception {
		
		WebConversation wc = login("9900000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		
		wr = wr.getLinkWith("Office Visit Reminders").click();
		assertTrue(wr.getText().contains("Patients Needing Visits"));
		wr = wr.getForms()[0].submit();
		assertTrue(wr.getText().contains("Darryl Thompson"));
		wr = wr.getLinkWith("Darryl Thompson").click();
		assertTrue(wr.getText().contains("Send Email Form"));
		wr = wr.getForms()[0].submit();
		assertTrue(wr.getText().contains("Your Email was sent:"));
	}
}
