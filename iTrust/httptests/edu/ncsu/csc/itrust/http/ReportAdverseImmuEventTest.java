package edu.ncsu.csc.itrust.http;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

public class ReportAdverseImmuEventTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.hcp0();
		gen.cptCodes();
		gen.ovImmune();
		gen.patient1();
		
	}
	
	public void testReport() throws Exception {
		
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		
		wr = wr.getLinkWith("View My Records").click();
		assertEquals("iTrust - View My Records", wr.getTitle());
		WebLink[] weblinks = wr.getLinks();
		for(int i = 0; i < weblinks.length; i++) {
			if(weblinks[i].getText().equals("Report")) {
				wr = weblinks[i].click();
				break;
			}
		}
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Report Adverse Event", wr.getTitle());
		WebForm patientForm = wr.getFormWithID("mainForm");
		patientForm.getScriptableObject().setParameterValue("Comment", "I've been experiencing extreme fatigue and severe nausea following this immunization.");
		wr = patientForm.submit();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertTrue(wr.getText().contains("Adverse Event Successfully Reported"));
	}
	
}
