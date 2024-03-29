package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 11
 */
public class DocumentOfficeVisitUseCaseTest extends iTrustHTTPTest {
	
	protected void setUp() throws Exception{
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
	}

	public void testAddLabProcedure() throws Exception {
		// login UAP
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// click 06/10/2007
		wr.getLinkWith("06/10/2007").click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editOfficeVisit.jsp?ovID=955", wr.getURL().toString());
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		//add new lab procedure
		WebForm form = wr.getForms()[0];
		form.setParameter("addLabProcID", "10666-6");
		form.getSubmitButton("addLP").click();
		wr = wc.getCurrentPage();
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("Information Successfully Updated"));
	}
	
	
	public void testRemoveLabProcedure() throws Exception {
		// login UAP
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - UAP Home", wr.getTitle());
		// click Document Office Visit
		wr = wr.getLinkWith("Document Office Visit").click();
		// choose patient 2
		WebForm patientForm = wr.getForms()[0];
		patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
		patientForm.getButtons()[1].click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
		// click 10/10/2005
		wr.getLinkWith("06/10/2007").click();
		wr = wc.getCurrentPage();
		assertEquals(ADDRESS + "auth/hcp-uap/editOfficeVisit.jsp?ovID=955", wr.getURL().toString());
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		//remove lab procedure
		WebTable wt = wr.getTableStartingWith("Laboratory Procedures");
		assertFalse(wt.getText().contains("No Laboratory Procedures on record"));
		//click the remove link
		wt = wr.getTableStartingWith("Laboratory Procedures");
		wr = wt.getTableCell(2, 5).getLinkWith("Remove").click();
		assertLogged(TransactionType.OFFICE_VISIT_EDIT, 8000000009L, 2L, "Office visit");
		assertEquals("iTrust - Document Office Visit", wr.getTitle());
		assertTrue(wr.getText().contains("Information Successfully Updated"));
		assertLogged(TransactionType.OFFICE_VISIT_VIEW, 8000000009L, 2L, "Office visit");
		wt = wr.getTableStartingWith("Laboratory Procedures");
		assertTrue(wt.getText().contains("No Laboratory Procedures on record"));
	}




}
