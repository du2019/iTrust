package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

/**
 * Use Case 10
 */
public class PersonalHealthRecordsUseCaseTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	public void testEditPatient() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("PHR Information").click();
		
        assertEquals("iTrust - Please Select a Patient", wr.getTitle());
      
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Andy Programmer"));
	}
	
	public void testInvalidPatientDates() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Patient Information").click();
		
        assertEquals("iTrust - Please Select a Patient", wr.getTitle());
      
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Patient Information"));
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.getScriptableObject().setParameterValue("dateOfDeathStr", "01/03/2050");
		wr = editPatientForm.submit();
		assertTrue(wr.getText().contains("future"));
		
	}
	
	public void testInvalidPatientBirthDates() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("Patient Information").click();
		
        assertEquals("iTrust - Please Select a Patient", wr.getTitle());
      
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Patient Information"));
		
		WebForm editPatientForm = wr.getForms()[0];
		editPatientForm.getScriptableObject().setParameterValue("dateOfDeathStr", "");
		editPatientForm.getScriptableObject().setParameterValue("dateOfBirthStr", "01/03/2050");
		wr = editPatientForm.submit();
		assertTrue(wr.getText().contains("future"));
		
	}
	
	
	public void testRepresent() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("PHR Information").click();
		      
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		
		wr = wr.getLinkWith("Baby Programmer").click();
		
		// Clicking on a representee's name takes you to their records
		assertTrue(wr.getText().contains("Andy Programmer"));
		assertTrue(wr.getText().contains("Diabetes with ketoacidosis"));
		assertTrue(wr.getText().contains("Grandparent"));
		wr = wr.getLinkWith("Random Person").click();
		
		assertTrue(wr.getText().contains("nobody@gmail.com"));
	}

	public void testAllergy() throws Exception {
		// Login
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		wr = wr.getLinkWith("PHR Information").click();
		      
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = patientForm.submit();
		
		// Add allergy
		WebForm wf = wr.getFormWithName("AddAllergy");
		wf.getScriptableObject().setParameterValue("description", "Oxygen");
		wr = wf.submit();
		
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Allergy Added"));
		
		wr = wr.getLinkWith("Show Fake Emails").click();
		assertTrue(wr.getText().contains("Your medical records have been altered"));
	}
}
