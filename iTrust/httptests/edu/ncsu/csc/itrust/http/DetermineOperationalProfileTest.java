package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class DetermineOperationalProfileTest extends iTrustHTTPTest {
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.uap1();
		gen.tester();
	}


	/*
	 * Precondition: Sample data is in the database. CreatePatient2 has passed.
	 * Login with user 9999999999 and password pw.
	 */
	public void testDetermineOperationalProfile() throws Exception {
		// login as uap and add a patient
		WebConversation wc = login("8000000009", "uappass1");
		WebResponse home = wc.getResponse(ADDRESS + "auth/uap/home.jsp");
		assertEquals("iTrust - UAP Home", home.getTitle());
		WebResponse wr = home.getLinkWith("Add Patient").click();
		WebForm form = wr.getForms()[0];
		form.setParameter("firstName", "bob");
		form.setParameter("lastName", "bob");
		form.setParameter("email","bob@bob.com");
		wr = form.submit();
		wr = wr.getLinkWith("Logout").click();
		wr = wr.getLinkWith("Home").click();
		// login as tester to check the operational profile
		wc = login("9999999999", "pw");
		wr = wc.getCurrentPage();

		WebTable table = wr.getTableStartingWithPrefix("Operation");
		assertEquals("Enter/Edit patient/personnel demographics", table.getCellAsText(1, 0));
		assertEquals("0", table.getCellAsText(1, 1)); //was 1
		assertEquals("0%", table.getCellAsText(1, 2));//was 25%
		assertEquals("0", table.getCellAsText(1, 3));//was 1
		assertEquals("0%", table.getCellAsText(1, 4)); //was 33%
		assertEquals("0", table.getCellAsText(1, 5));//was 0
		assertEquals("0%", table.getCellAsText(1,6));//was 0
		//now check the totals are correct
		assertEquals("Refer patient to hcp", table.getCellAsText(37, 0));
		assertEquals("0", table.getCellAsText(39, 1));//was 4 - supposed to be 3
		assertEquals("0", table.getCellAsText(39, 3));//was 3 - supposed to be 2
		assertEquals("0", table.getCellAsText(39, 5));//supposed to be 1
	}
}
