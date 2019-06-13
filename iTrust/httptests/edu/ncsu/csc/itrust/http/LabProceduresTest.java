package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class LabProceduresTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.uap1();
		gen.hcp0();
		gen.patient2();
		gen.patient4();
		gen.loincs();
		gen.labProcedures();
	}
	
	public void testInputLabResults() throws Exception {

		// login with UAP
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 8000000009L, 0L, "");
		
		// go to edit laboratory procedures enter PID 2
		wr = wr.getLinkWith("Laboratory Procedures").click();
		assertTrue(wr.getText().contains("Please Select a Patient"));
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		// view lab procedures
		wr = wc.getCurrentPage();
		// make sure we get the right lab procedure
		assertTrue(wr.getText().contains("Laboratory Procedures"));		
		WebTable wt = wr.getTableStartingWith("Laboratory Procedures");
		assertTrue(wt.getTableCell(2, 7).getText().contains("2007-07-20"));
		assertTrue(wt.getTableCell(2, 1).getText().contains("10763-1"));
		wr = wt.getTableCell(2, 8).getLinkWith("Update").click();
		assertEquals("iTrust - Update Lab Procedure", wr.getTitle());
		WebForm form = wr.getForms()[0];
		// update the procedure
		form.setParameter("Status", "PENDING");
		form.setParameter("Results", "Negative.");
		form.setParameter("Commentary", "All is well.");
		form.getSubmitButtons()[0].click();
		// check the update
		wr = wc.getCurrentPage();
		assertLogged(TransactionType.LAB_RESULTS_EDIT, 8000000009L, 2L, "");
		assertTrue(wr.getText().contains("Updated Laboratory Procedure"));
		wt = wr.getTableStartingWith("Laboratory Procedures");
		assertTrue(wt.getTableCell(2, 3).getText().contains("PENDING"));
		assertTrue(wt.getTableCell(2, 4).getText().contains("All is well."));
		assertTrue(wt.getTableCell(2, 5).getText().contains("Negative"));
		// check log
		wr = wr.getLinkWith("Transaction Log").click();
		assertTrue(wr.getText().contains("Procedure: "));
		assertLogged(TransactionType.LAB_RESULTS_VIEW, 8000000009L, 2L, "");
	}

	/*
	 * Authenticate HCP 9000000000 and Patient 2. HCP 9000000000 has ordered lab procedure 10763-1 for patient
	 * 1 in an office visit on 7/20/2007. InputLabResults has successfully passed. All lab procedure test data
	 * is in database. HCP 9000000000 has authenticated successfully 1. The HCP chooses to view laboratory
	 * procedure results and selects patient 2 2. The LCHP sorts by dates of the last status update. 3. The
	 * LHCP chooses the top procedure (the procedure from InputLabResults). 4. The LHCP allows viewing access
	 * to the laboratory results.
	 */
	public void testHcpLabProc() throws Exception {
		// login hcp
		WebConversation wc = login("9000000000", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - HCP Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
		
		// click on Edit ND Codes
		wr = wr.getLinkWith("Laboratory Procedures").click();
		// choose patient 1
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		// add the codes and description
		assertEquals("iTrust - View Laboratory Procedures", wr.getTitle());
		wr = wr.getLinkWith("Allow/Disallow Viewing").click();
		assertLogged(TransactionType.LAB_RESULTS_VIEW, 9000000000L, 2L, "");
	}

	public void testPatientViewLabResults() throws Exception {
		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
		
		wr = wr.getLinkWith("View My Records").click();
		assertTrue(wr.getText().contains("Lab Procedures"));
		assertTrue(wr.getText().contains("COMPLETED"));
		assertTrue(wr.getText().contains("10640-1"));
		assertLogged(TransactionType.MEDICAL_RECORD_VIEW, 2L, 2L, "");
	}

}
