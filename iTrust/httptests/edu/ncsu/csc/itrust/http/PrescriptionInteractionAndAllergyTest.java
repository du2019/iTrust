package edu.ncsu.csc.itrust.http;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

/**
 * Use Case 37
 */
public class PrescriptionInteractionAndAllergyTest extends iTrustHTTPTest {
	
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.hcp0();
	}

	public void testNoAllergyPrescribe() throws Exception{
		gen.patient2();
		gen.officeVisit2();
		gen.ndCodes1();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		wr = wr.getLinkWith("Document Office Visit").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		wr = wr.getLinkWith("7/15/2009").click();
		patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("addMedID", "081096");
		patientForm.getScriptableObject().setParameterValue("dosage", "15");
		
		patientForm.getScriptableObject().setParameterValue("startDate", new SimpleDateFormat("07/20/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("endDate", new SimpleDateFormat("08/15/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("instructions", "Take twice daily with water");
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
	}

	public void testAllergicPrescribe() throws Exception{
		gen.patient2();
		gen.officeVisit4();
		gen.ndCodes1();
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		wr = wr.getLinkWith("Document Office Visit").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		wr = wr.getLinkWith("9/15/2009").click();
		patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("addMedID", "081096");
		patientForm.getScriptableObject().setParameterValue("dosage", "15");
		
		patientForm.getScriptableObject().setParameterValue("startDate", new SimpleDateFormat("09/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("endDate", new SimpleDateFormat("10/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("instructions", "Take twice daily with water");
		
		patientForm.getScriptableObject().setParameterValue("testMed", "081096");
		patientForm.getScriptableObject().setParameterValue("medDos", "15");
		patientForm.getScriptableObject().setParameterValue("medStart", new SimpleDateFormat("09/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("medEnd", new SimpleDateFormat("10/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("medInst", "Take twice daily with water");

	
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Allergy: Aspirin"));
		patientForm = wr.getForms()[0];
		patientForm.getButtonWithID("continue").click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
	}

	public void testInteractionAndAllergyPrescribe() throws Exception{

		gen.patient2();
		gen.officeVisit4();
		gen.ndCodes1();
		gen.drugInteractions3();
		
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		wr = wr.getLinkWith("Document Office Visit").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		wr = wr.getLinkWith("9/15/2009").click();
		patientForm = wr.getForms()[0];

		patientForm.getScriptableObject().setParameterValue("addMedID", "619580501");
		patientForm.getScriptableObject().setParameterValue("dosage", "10");
		
		patientForm.getScriptableObject().setParameterValue("startDate", new SimpleDateFormat("09/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("endDate", new SimpleDateFormat("10/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("instructions", "Take once a day");
		
		patientForm.getScriptableObject().setParameterValue("testMed", "619580501");
		patientForm.getScriptableObject().setParameterValue("medDos", "10");
		patientForm.getScriptableObject().setParameterValue("medStart", new SimpleDateFormat("09/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("medEnd", new SimpleDateFormat("10/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("medInst", "Take once a day");

	
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		
		
		patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("addMedID", "081096");
		patientForm.getScriptableObject().setParameterValue("dosage", "15");
		
		patientForm.getScriptableObject().setParameterValue("startDate", new SimpleDateFormat("10/15/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("endDate", new SimpleDateFormat("10/31/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("instructions", "Take twice daily with water");
		
		patientForm.getScriptableObject().setParameterValue("testMed", "081096");
		patientForm.getScriptableObject().setParameterValue("medDos", "15");
		patientForm.getScriptableObject().setParameterValue("medStart", new SimpleDateFormat("10/15/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("medEnd", new SimpleDateFormat("10/31/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("medInst", "Take twice daily with water");

	
		wr = patientForm.submit();
		assertTrue(wr.getText().contains("Allergy: Aspirin"));
		assertTrue(wr.getText().contains("Currently Prescribed: Adefovir"));
		patientForm = wr.getForms()[0];
		patientForm.getButtonWithID("continue").click();
		wr = wc.getCurrentPage();
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		


	}

	public void testInteractionNoPrescribe() throws Exception{
		gen.patient1();
		gen.officeVisit3();
		gen.ndCodes1();
		gen.drugInteractions3();

		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		wr = wr.getLinkWith("Document Office Visit").click();
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "1");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		wr = wr.getLinkWith("9/17/2009").click();
		patientForm = wr.getForms()[0];
		
		patientForm.getScriptableObject().setParameterValue("addMedID", "619580501");
		patientForm.getScriptableObject().setParameterValue("dosage", "10");
		patientForm.getScriptableObject().setParameterValue("startDate", new SimpleDateFormat("09/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("endDate", new SimpleDateFormat("11/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("instructions", "Take once daily with meal");
		
		patientForm.getScriptableObject().setParameterValue("testMed", "619580501");
		patientForm.getScriptableObject().setParameterValue("medDos", "10");
		patientForm.getScriptableObject().setParameterValue("medStart", new SimpleDateFormat("09/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("medEnd", new SimpleDateFormat("11/22/2009").format(new Date()));
		patientForm.getScriptableObject().setParameterValue("medInst", "Take once daily with meal");

		wr = patientForm.submit();
		
		assertTrue(wr.getText().contains("Currently Prescribed: Aspirin."));
		patientForm = wr.getForms()[0];
		patientForm.getButtonWithID("cancel").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Edit Personal Health Record",wr.getTitle());
		assertTrue(wr.getText().contains("Random Person"));

		
	}

}
