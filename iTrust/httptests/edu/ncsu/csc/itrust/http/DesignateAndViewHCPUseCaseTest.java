package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
//import com.meterware.httpunit.FormControl;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import com.meterware.httpunit.TableRow;

/**
 * Use Case 6
 */
public class DesignateAndViewHCPUseCaseTest extends iTrustHTTPTest {

	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.patient_hcp_vists();
	}
	
	public void testReportSeenHCPs0() throws Exception {

		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());

		wr = wr.getLinkWith("My Providers").click();
		assertEquals("iTrust - My Providers", wr.getTitle());
		
		WebTable table = (WebTable)wr.getElementWithID("hcp_table");
		TableRow rows[] = table.getRows();
		
		assertEquals("| HCP Name | Specialty | Address | Date of Office Visit | Designated HCP?", rows[0].getText());
		assertEquals("| Jason Frankenstein | surgeon | 333 Dark Lane Raleigh, NC 27603 | 05/01/2008 |", rows[1].getText());
		assertEquals("| Kelly Doctor | surgeon | 4321 My Road St PO BOX 2 CityName, NY 12345-1234 | 06/10/2007 |",rows[2].getText());
		assertEquals("| Gandalf Stormcrow | none | 4321 My Road St PO BOX 2 CityName, NY 12345-1234 |  |",rows[3].getText());

	}
	
	public void testReportSeenHCPs1() throws Exception {

		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());

		wr = wr.getLinkWith("My Providers").click();
		assertEquals("iTrust - My Providers", wr.getTitle());
		
		WebForm form = wr.getFormWithName("mainForm"); // .getFormWithID("mainForm");
		
		//form.setCheckbox("doctor", "Jason Frankenstein", false);
		form.getScriptableObject().setParameterValue("removeID", "Jason Frankenstein");
		//form.getScriptableObject().setParameterValue("doctor", "Jason Frankenstein");
		//wr.getElementsWithName(doctor)[0]
		
		//form.getScriptableObject().submit();
		//wr = wc.getCurrentPage();
		wr = wr.getForms()[0].submit();
		
		WebTable table = (WebTable)wr.getElementWithID("hcp_table");
		TableRow rows[] = table.getRows();
		
		assertEquals("| Jason Frankenstein | surgeon | 333 Dark Lane Raleigh, NC 27603 | 05/01/2008 |", rows[1].getText());
		
	}
	
	public void testReportSeenHCPs2() throws Exception {

		WebConversation wc = login("2", "pw");
		WebResponse wr = wc.getCurrentPage();
		assertEquals("iTrust - Patient Home", wr.getTitle());

		wr = wr.getLinkWith("My Providers").click();
		assertEquals("iTrust - My Providers", wr.getTitle());
		
		WebForm form = wr.getFormWithID("searchForm");
		form.setParameter("filter_name", "frank");
		form.setParameter("filter_specialty", "pediatrician");
		wr = form.submit();
		assertEquals("iTrust - My Providers", wr.getTitle());
		WebTable table = (WebTable)wr.getElementWithID("filter_hcp_table");
		TableRow rows[] = table.getRows();
		assertEquals("| Lauren Frankenstein | pediatrician | 333 Dark Lane Raleigh, NC 27603 |", rows[1].getText());
	}
}
