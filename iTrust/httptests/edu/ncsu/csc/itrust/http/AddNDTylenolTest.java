package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;


public class AddNDTylenolTest extends iTrustHTTPTest {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.admin1();
	}

	/*
	 * Authenticate admin 90000000001
	 * Choose Edit ND Codes
	 * Code1: 55154
	 * Code2: 1922
	 * Description: "tylenol Tablets"
	 */
	public void testCreateValidHCP() throws Exception {
		// login admin
		WebConversation wc = login("9000000001", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Admin Home", wr.getTitle());
		// click on Edit ND Codes
		wr = wr.getLinkWith("Edit ND Codes").click();
		// add the codes and description
		assertEquals("iTrust - Maintain ND Codes", wr.getTitle());
		WebForm form = wr.getForms()[0];
		form.setParameter("code1", "55154");
		form.setParameter("code2", "1922");
		form.setParameter("description", "Tylenol Tablets");
		form.getSubmitButtons()[1].click();
		wr = wc.getCurrentPage();
		// verify change
		assertTrue(wr.getURL().toString().contains("auth/admin/editNDCodes.jsp"));
		assertTrue(wr.getText().contains("Success: 551541922 - Tylenol Tablets added"));
	}
	

}
