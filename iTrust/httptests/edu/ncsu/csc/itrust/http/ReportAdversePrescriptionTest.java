package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

public class ReportAdversePrescriptionTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.hcp0();
		gen.ovMed();
		gen.patient2();
		gen.patient1();
		
	}

	public void testReport() throws Exception {
		
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		wr = wr.getLinkWith("Prescription Records").click();
		assertEquals("iTrust - Get My Prescription Report", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		//patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		patientForm = wr.getForms()[1];
		patientForm.setParameter("checking0","Y");
		patientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Report Adverse Event", wr.getTitle());
		patientForm = wr.getFormWithID("mainForm");
		patientForm.getScriptableObject().setParameterValue("Comment", "My joints hurt and my muscles ache. I've been having severe nausea after taking this medication.");
		wr = patientForm.submit();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertTrue(wr.getText().contains("Adverse Event Successfully Reported"));
	}
	
	public void testReportAdverseEventsButton() throws Exception{
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Prescription Records").click();
		assertEquals("iTrust - Get My Prescription Report", wr.getTitle());
		WebForm patientForm = wr.getForms()[0];
		patientForm.getButtons()[0].click();
		wr = wc.getCurrentPage();		
		for(int i = 0; i < wr.getElementNames().length; i++){
			assertNotSame("adevent", wr.getElementNames()[i]);
		}
	}
}
