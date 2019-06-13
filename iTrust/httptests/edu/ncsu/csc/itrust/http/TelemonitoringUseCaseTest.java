package edu.ncsu.csc.itrust.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

/**
 * Use Case 34
 */
public class TelemonitoringUseCaseTest extends iTrustHTTPTest {
	
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}
	
	public void testAddPatientsToMonitor() throws Exception {		
		// login HCP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		//Click Edit Patient List
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm confirmForm = wr.getForms()[0];
		assertEquals("Add Andy Programmer", confirmForm.getButtons()[0].getValue());
		confirmForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Added"));
	}

	public void testRemovePatientsToMonitor() throws Exception {
		//Add patient 1 to HCP 9000000000's monitoring list
		gen.remoteMonitoring2();
		// login HCP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		//Click Edit Patient List
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm confirmForm = wr.getForms()[0];
		assertEquals("Remove Random Person", confirmForm.getButtons()[0].getValue());
		confirmForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Random Person Removed"));
	}
	
	public void testReportPatientStatus() throws Exception {
		//Add patient 1 to HCP 9000000000's monitoring list
		gen.remoteMonitoring2();
		// login Patient
		WebConversation wc = login("1", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		//Click Report Status
		wr = wr.getLinkWith("Report Status").click();
		WebForm dataForm = wr.getForms()[0];
		dataForm.getScriptableObject().setParameterValue("systolicBloodPressure", "100");
		dataForm.getScriptableObject().setParameterValue("diastolicBloodPressure", "75");
		dataForm.getScriptableObject().setParameterValue("glucoseLevel", "120");
		dataForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Added"));
	}
	
	public void testViewMonitoringList() throws Exception {
		//Sets up all preconditions listed in acceptance test
		gen.remoteMonitoring3();
		// login HCP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		wr = wr.getLinkWith("Monitor Patients").click();
		//Verify all data
		WebTable table = wr.getTableStartingWithPrefix("Patient Data");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        
        assertEquals("Random Person (MID 1)", table.getCellAsText(2, 0));
		assertTrue(table.getCellAsText(2, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(2, 1).contains("08:00:00"));
		assertEquals("160", table.getCellAsText(2, 2));
		assertEquals("110", table.getCellAsText(2, 3));
		assertEquals("60", table.getCellAsText(2, 4));
		assertEquals("Andy Programmer", table.getCellAsText(2, 5));		
		//Highlighting for abnormal data
		assertEquals("#ffff00", table.getRows()[2].getAttribute("bgcolor"));
		
		assertEquals("Random Person (MID 1)", table.getCellAsText(3, 0));
		assertTrue(table.getCellAsText(3, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(3, 1).contains("07:15:00"));
		assertEquals("100", table.getCellAsText(3, 2));
		assertEquals("70", table.getCellAsText(3, 3));
		assertEquals("90", table.getCellAsText(3, 4));
		assertEquals("FirstUAP LastUAP", table.getCellAsText(3, 5));
		
		assertEquals("Random Person (MID 1)", table.getCellAsText(4, 0));
		assertTrue(table.getCellAsText(4, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(4, 1).contains("05:30:00"));
		assertEquals("90", table.getCellAsText(4, 2));
		assertEquals("60", table.getCellAsText(4, 3));
		assertEquals("80", table.getCellAsText(4, 4));
		assertEquals("Random Person", table.getCellAsText(4, 5));
		
		assertEquals("Baby Programmer (MID 5)", table.getCellAsText(5, 0));
		assertEquals("No Information Provided", table.getCellAsText(5, 1));
		assertEquals("", table.getCellAsText(5, 2));
		assertEquals("", table.getCellAsText(5, 3));
		assertEquals("", table.getCellAsText(5, 4));
		assertEquals("", table.getCellAsText(5, 5));
		//Highlighting for no entry
		assertEquals("#ff6666", table.getRows()[5].getAttribute("bgcolor"));
		
	}
	
	public void testViewAdditionalInformation() throws Exception{
		int i = 1;
		assertEquals(1, i);
		gen.remoteMonitoringAdditional();
		/*Run the testViewAdditionalInformation.html file as a Selenium test case next to see 
		the equivalent of an acceptance test for this.  There is a javascript that prevents 
		httpunit from running this test as is below.
		
		// login HCP
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		wr = wr.getLinkWith("Monitor Patients").click();
		assertEquals("iTrust - Monitor Patients", wr.getTitle());
		//Choose the additional information page for patient 1 and validate contents
		wr = wr.getLinkWith("Random Person").click();
		WebForm form = wr.getForms()[0];
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        java.util.Date date = new java.util.Date();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.WEEK_OF_YEAR, -1);
        String previousDate = dateFormat.format(now.getTime());
        String currentDate = dateFormat.format(date);
		form.setParameter("startDate", previousDate);
		form.setParameter("endDate", currentDate);
		form.getSubmitButtons()[0].click();
		wr = wc.getCurrentPage();
		
		WebTable table = wr.getTableStartingWithPrefix("Patient Details");
		assertEquals("Random Person", table.getCellAsText(1, 1));
		assertEquals("919-971-0000", table.getCellAsText(2, 1));
		
		table = wr.getTableStartingWithPrefix("Patient's Representatives");
		assertEquals("Andy Programmer", table.getCellAsText(1, 1));
		assertEquals("555-555-5555", table.getCellAsText(1, 3));
		
		table = wr.getTableStartingWithPrefix("Patient Monitoring Statistics");
		assertTrue(table.getCellAsText(2, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(2, 1).contains("08:00:00"));
		assertEquals("160", table.getCellAsText(2, 2));
		assertEquals("110", table.getCellAsText(2, 3));
		assertEquals("60", table.getCellAsText(2, 4));
		assertEquals("Andy Programmer", table.getCellAsText(2, 5));		
		
		assertTrue(table.getCellAsText(3, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(3, 1).contains("07:15:00"));
		assertEquals("100", table.getCellAsText(3, 2));
		assertEquals("70", table.getCellAsText(3, 3));
		assertEquals("90", table.getCellAsText(3, 4));
		assertEquals("FirstUAP LastUAP", table.getCellAsText(3, 5));
		
		assertTrue(table.getCellAsText(4, 1).contains(dateFormat.format(date)));
		assertTrue(table.getCellAsText(4, 1).contains("05:30:00"));
		assertEquals("90", table.getCellAsText(4, 2));
		assertEquals("60", table.getCellAsText(4, 3));
		assertEquals("80", table.getCellAsText(4, 4));
		assertEquals("Random Person", table.getCellAsText(4, 5));
		
		assertTrue(table.getCellAsText(5, 1).contains("13:15:00"));
		assertEquals("100", table.getCellAsText(5, 2));
		assertEquals("75", table.getCellAsText(5, 3));
		assertEquals("100", table.getCellAsText(5, 4));
		assertEquals("Random Person", table.getCellAsText(5, 5));
		
		assertTrue(table.getCellAsText(6, 1).contains("17:15:00"));
		assertEquals("100", table.getCellAsText(6, 2));
		assertEquals("80", table.getCellAsText(6, 3));
		assertEquals("110", table.getCellAsText(6, 4));
		assertEquals("Andy Programmer", table.getCellAsText(6, 5));
		
		assertTrue(table.getCellAsText(7, 1).contains("02:15:00"));
		assertEquals("95", table.getCellAsText(7, 2));
		assertEquals("65", table.getCellAsText(7, 3));
		assertEquals("95", table.getCellAsText(7, 4));
		assertEquals("FirstUAP LastUAP", table.getCellAsText(7, 5));*/
	}
	
	public void testUapReportPatientStatus() throws Exception{
		gen.remoteMonitoringUAP();
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		wr = wr.getLinkWith("Report Status").click();
		assertEquals("iTrust - View Monitored Patients",wr.getTitle());
		wr = wr.getLinkWith("Andy Programmer").click();
		assertEquals("iTrust - Report Status",wr.getTitle());
		WebForm dataForm = wr.getForms()[0];
		dataForm.getScriptableObject().setParameterValue("systolicBloodPressure", "100");
		dataForm.getScriptableObject().setParameterValue("diastolicBloodPressure", "75");
		dataForm.getScriptableObject().setParameterValue("glucoseLevel", "120");
		dataForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Added"));

	}
	
	public void testRepresentativeReportPatientStatus() throws Exception {
		gen.remoteMonitoring4();
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		wr = wr.getLinkWith("Report Status").click();
		assertEquals("iTrust - Report Status", wr.getTitle());
		wr = wr.getLinkWith("Random Person").click();
		WebForm dataForm = wr.getForms()[0];
		//Must submit here due to implementation of link. The link uses
		//javascript to dynamically submit the form and the script doesn't run
		//via httptest so we must manually submit the form after clicking.
		wr = dataForm.submit();
		dataForm = wr.getForms()[0];
		dataForm.getScriptableObject().setParameterValue("glucoseLevel", "120");
		dataForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Added"));
		
	}
	
	public void testUapAddPatientToMonitorTest() throws Exception{
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm confirmForm = wr.getForms()[0];
		assertEquals("Add Andy Programmer", confirmForm.getButtons()[0].getValue());
		confirmForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Added"));
}

	public void testUAPAddHCPMonitor() throws Exception{
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());

		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm confirmForm = wr.getForms()[0];
		assertEquals("Add Andy Programmer", confirmForm.getButtons()[0].getValue());
		confirmForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Added"));
		
		//go to reporting page
		wr = wr.getLinkWith("Report Status").click();
		assertTrue(wr.getText().contains("Andy Programmer"));
		wr = wr.getLinkWith("Andy Programmer").click();
		assertEquals("iTrust - Report Status",wr.getTitle());
		WebForm dataForm = wr.getForms()[0];
		dataForm.getScriptableObject().setParameterValue("systolicBloodPressure", "110");
		dataForm.getScriptableObject().setParameterValue("diastolicBloodPressure", "85");
		dataForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Added"));

		//logout
		wr = wr.getLinkWith("Logout").click();
		
		//log back in
		WebConversation wcHCP = login("9000000000", "pw");
		wr = wcHCP.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientFormForHCP = wr.getForms()[0];
		patientFormForHCP.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientFormForHCP.getButtons()[1].click();
		wr = wcHCP.getCurrentPage();
		WebForm confirmFormForHCP = wr.getForms()[0];
		assertEquals("Add Andy Programmer", confirmFormForHCP.getButtons()[0].getValue());
		confirmFormForHCP.getButtons()[0].click();
		wr = wcHCP.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Added"));
		
		wr = wr.getLinkWith("Monitor Patients").click();
		WebTable table = wr.getTableStartingWithPrefix("Patient Data");
		
        assertEquals("Andy Programmer (MID 2)", table.getCellAsText(2, 0));
		assertEquals("110", table.getCellAsText(2, 2));
		assertEquals("85", table.getCellAsText(2, 3));
		assertEquals("", table.getCellAsText(2, 4));
		
		
		
	}
	public void testUAPAddReportDeleteCannotReport() throws Exception{
		//log in to iTrust
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		
		//add Patient 2 to reporting list
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm confirmForm = wr.getForms()[0];
		assertEquals("Add Andy Programmer", confirmForm.getButtons()[0].getValue());
		confirmForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Added"));
		
		//go to reporting page
		wr = wr.getLinkWith("Report Status").click();
		assertTrue(wr.getText().contains("Andy Programmer"));
		wr = wr.getLinkWith("Andy Programmer").click();
		assertEquals("iTrust - Report Status",wr.getTitle());
		WebForm dataForm = wr.getForms()[0];
		dataForm.getScriptableObject().setParameterValue("systolicBloodPressure", "100");
		dataForm.getScriptableObject().setParameterValue("diastolicBloodPressure", "75");
		dataForm.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Added"));

		//remove Patient 2 from reporting list
		wr = wr.getLinkWith("Edit Patient List").click();
		WebForm patientFormNew = wr.getForms()[0];
		patientFormNew.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientFormNew.getButtons()[1].click();
		wr = wc.getCurrentPage();
		WebForm confirmFormNew = wr.getForms()[0];
		assertEquals("Remove Andy Programmer", confirmFormNew.getButtons()[0].getValue());
		confirmFormNew.getButtons()[0].click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Patient Andy Programmer Removed"));
		
		//go to reporting page to confirm you cannot report for Patient 2
		wr = wr.getLinkWith("Report Status").click();
		assertFalse(wr.getText().contains("Andy Programmer"));

		
	}
}
